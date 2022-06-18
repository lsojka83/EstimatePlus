package pl.estimateplus.model;


import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.format.CellNumberStringMod;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import pl.estimateplus.entity.Estimate;
import pl.estimateplus.entity.EstimateItem;
import pl.estimateplus.entity.PriceList;
import pl.estimateplus.entity.PriceListItem;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;


public class Excel {


    public static void main(String[] args) throws IOException {
//        importExcelData();

//        File file = null; //direct run
//        String fileLocation = "Legrand.xlsx";
//        String fileLocation1 = "targetFile.tmp";
//        Resource resource = new ClassPathResource(fileLocation);
//        try {
//            file = resource.getFile();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(resource.getFile().getName().split("\\.")[0]);
    }


    public static PriceList importExcelData(MultipartFile multipartFile) {

        PriceList priceList = new PriceList();
        int itemsCount = 0;
        String priceListName = multipartFile.getOriginalFilename().split("\\.")[0];
        System.out.println(priceListName);

        //load file
        File file = null; //webapp
        String fileLocation = "targetFile.tmp";
        Resource resource = new ClassPathResource(fileLocation);
        try {
            file = resource.getFile();
        } catch (IOException e) {
            priceList.setErrorMessage(e.getMessage());
            return priceList;
        }
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
            priceList.setErrorMessage(e.getMessage());
            return priceList;
        } catch (IOException e) {
//            throw new RuntimeException(e);
            priceList.setErrorMessage(e.getMessage());
            return priceList;
        }

        Workbook workbook = null;
        List<PriceListItem> priceListItems = new ArrayList<>();

        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
//            e.printStackTrace();
            priceList.setErrorMessage(e.getMessage());
            return priceList;
        } catch (InvalidFormatException e) {

//            throw new RuntimeException(e);
            priceList.setErrorMessage(e.getMessage());
            return priceList;
        }

        if (file != null && workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);

            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;
            for (Row row : sheet) {
                data.put(i, new ArrayList<String>());
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING: {
                            data.get(i).add(cell.getRichStringCellValue().getString());
                            break;
                        }
                        case NUMERIC: {
                            if (DateUtil.isCellDateFormatted(cell)) {
                                data.get(i).add(cell.getDateCellValue() + "");
                            } else {
                                data.get(i).add(cell.getNumericCellValue() + "");
                            }
                            break;
                        }
                        case BOOLEAN: {
                            data.get(i).add(cell.getBooleanCellValue() + "");
                            break;
                        }
                        case FORMULA: {
                            data.get(i).add(cell.getCellFormula() + "");
                            break;
                        }
                        default:
                            data.get(i).add(" ");
                    }
                }
                i++;
            }
            priceList.setNumberOfItems(Long.valueOf(i));

            for (Map.Entry<Integer, List<String>> entry : data.entrySet()) {
                //vendorName
                String vendorName = multipartFile.getOriginalFilename().split("\\.")[0];

                String referenceNumber = entry.getValue().get(6); //referenceNumber
                String description = "no description";
                if (!entry.getValue().get(7).isEmpty() && !entry.getValue().get(7).isBlank()) {
                    description = entry.getValue().get(7); //description
                }
                String brand = entry.getValue().get(5); //brand
                String comment = "no comment";//comment

                BigDecimal unitNetPrice;
                try {
                    String s = entry.getValue().get(9).trim();
                    unitNetPrice = new BigDecimal(s).setScale(2); //unitNetPrice

                }
                catch (Exception e)
                {
//                    unitNetPrice = new BigDecimal("0").setScale(2); //unitNetPrice
                    priceList.setErrorMessage(e.getMessage());
                    return priceList;
                }

                String unit = entry.getValue().get(10); //unit

                int baseVatRate = 23;


                try {

                    if (!entry.getValue().get(14).isEmpty() && !entry.getValue().get(14).isBlank()) {
                        try {
                            if(entry.getValue().get(14).contains("\\.")) {
                                baseVatRate = Integer.parseInt(entry.getValue().get(14).split("\\.")[0]);
                            }else if(entry.getValue().get(14).contains("\\,"))
                            {
                                baseVatRate = Integer.parseInt(entry.getValue().get(14).split("\\,")[0]);
                            }
                            else
                            {
                                baseVatRate = Integer.parseInt(entry.getValue().get(14));
                            }

                        } catch (NumberFormatException e) {
//                            e.printStackTrace();
                            priceList.setErrorMessage(e.getMessage());
                            return priceList;
                        } catch (Exception e) {
//                            e.printStackTrace();
                            priceList.setErrorMessage(e.getMessage());
                            return priceList;
                        }
                    }
                }catch (Exception e)
                {
                    priceList.setErrorMessage(e.getMessage());
                    return priceList;
                }
                //addedOn
                PriceListItem priceListItem = new PriceListItem(vendorName, referenceNumber, description, brand, comment, unitNetPrice, unit, baseVatRate);
                priceListItems.add(priceListItem);
//                System.out.println(priceListItem);

            }
        }
        priceList.setUserOwned(false);
        priceList.setPriceListItems(priceListItems);
        priceList.setName(priceListName);

        return priceList;
    }

    public static XSSFWorkbook getExcelWorkbook(Estimate estimate) {

        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(estimate.getName());
        sheet.setColumnWidth(0,1000); //se. no.
        sheet.setColumnWidth(1,4000); //referenceNumber
        sheet.setColumnWidth(2,15000); //description
        sheet.setColumnWidth(3,4000); //brand
        sheet.setColumnWidth(4,4000); //unitNetPrice
        sheet.setColumnWidth(5,2000); //quantity
        sheet.setColumnWidth(6,4000); //unit
        sheet.setColumnWidth(7,4000); //total net price
        sheet.setColumnWidth(8,2000); //individualVatRate
        sheet.setColumnWidth(9,15000); //comment

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("#");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Reference Number");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Description");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Brand");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Unit Net Price");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(5);
        headerCell.setCellValue("Quantity");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(6);
        headerCell.setCellValue("Unit");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(7);
        headerCell.setCellValue("Total price");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(8);
        headerCell.setCellValue("Vat Rate");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(9);
        headerCell.setCellValue("Comment");
        headerCell.setCellStyle(headerStyle);


        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        int rowIndex = 1; // row below header row

        for(EstimateItem ei: estimate.getEstimateItems())
        {
            Row row = sheet.createRow(rowIndex);

            Cell cell = row.createCell(0);
            cell.setCellValue(rowIndex);
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(ei.getPriceListItem().getReferenceNumber());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(ei.getPriceListItem().getDescription());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(ei.getPriceListItem().getBrand());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(ei.getPriceListItem().getUnitNetPrice().doubleValue());
            cell.setCellStyle(style);

            cell = row.createCell(5);
            cell.setCellValue(ei.getQuantity());
            cell.setCellStyle(style);

            cell = row.createCell(6);
            cell.setCellValue(ei.getPriceListItem().getUnit());
            cell.setCellStyle(style);

            cell = row.createCell(7);
            cell.setCellValue(ei.getTotalNetPrice().doubleValue());
            cell.setCellStyle(style);

            cell = row.createCell(8);
            cell.setCellValue(ei.getIndividualVatRate());
            cell.setCellStyle(style);

            cell = row.createCell(9);
            cell.setCellValue(ei.getPriceListItem().getComment());
            cell.setCellStyle(style);

            rowIndex++;
        }

        rowIndex++; //extra row


        Row row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(6);
        cell.setCellValue("TotalNetAmount");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue(estimate.getTotalNetAmount().doubleValue());
        cell.setCellStyle(style);

        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(6);
        cell.setCellValue("TotalVatAmount");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue(estimate.getTotalVatAmount().floatValue());
        cell.setCellStyle(style);

        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(6);
        cell.setCellValue("TotalGrossAmount");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue(estimate.getTotalGrossAmount().floatValue());
        cell.setCellStyle(style);



        return workbook;
    }

}
