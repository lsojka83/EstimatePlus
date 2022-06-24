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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Excel {

    private static String TMP_DIR = System.getProperty("java.io.tmpdir");
    private static String fileLocation = TMP_DIR + "/importedFile.tmp";
    private static File file = new File(fileLocation);
    private static Path path = Paths.get(fileLocation);


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

    public static Map<Integer, String> getColumnsNames(MultipartFile multipartFile, String firstRowIsColumnsNames) {

        Map<Integer, String> columnsAssignmentMap = new HashMap<>();
        //load file

        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }

        if (file != null && workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;
            Row row = sheet.getRow(0);
            if (firstRowIsColumnsNames != null && firstRowIsColumnsNames.equals("yes")) {


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
                for (String columnName : data.get(0)) {
                    columnsAssignmentMap.put(i, columnName);
                    i++;
                }
            } else {
                int columnCount = Integer.valueOf(row.getLastCellNum());
                for (i = 0; i <= columnCount; i++) {
                    columnsAssignmentMap.put(i, "Column number " + (i + 1));
                }
            }

        }
        return columnsAssignmentMap;
    }

    public static Map<Integer, List<String>> getExcelData(MultipartFile multipartFile, String
            firstRowIsColumnsNames) {

        Map<Integer, List<String>> data = new HashMap<>();

        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Workbook workbook = null;

        try {
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {

            throw new RuntimeException(e);
        }

        if (file != null && workbook != null) {
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;

            if (firstRowIsColumnsNames != null && firstRowIsColumnsNames.equals("yes")) {
                sheet.removeRow(sheet.getRow(0));
            }

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

        }


        return data;
    }

    public static String getFileName(MultipartFile multipartFile) {

        return multipartFile.getOriginalFilename().split("\\.")[0];
    }


    public static PriceList importFromExcelData(Map<Integer, List<String>> data, ColumnAssigment
            columnAssigment, String firstRowIsColumnsNames, String fileName) {

        PriceList priceList = new PriceList();
        int itemsCount = 0;
        String priceListName = fileName;

        List<PriceListItem> priceListItems = new ArrayList<>();


        int i = data.keySet().size();
        priceList.setNumberOfItems(Long.valueOf(i));

        for (Map.Entry<Integer, List<String>> entry : data.entrySet()) {

            String vendorName = fileName;  //vendorName - file name is assigned


            String referenceNumber = "-"; //referenceNumber
            try {
                referenceNumber = entry.getValue().get(columnAssigment.referenceNumberColumnNumber);
            }catch (Exception e)
            {
                priceList.setErrorMessage(priceList.getErrorMessage() + " - "+entry.getKey()+" - "+e.getMessage());
//                return priceList;
            }

            String description = "no description"; //description
            try {
                if (!entry.getValue().get(columnAssigment.descriptionColumnNumber).isEmpty() && !entry.getValue().get(columnAssigment.descriptionColumnNumber).isBlank()) {
                    description = entry.getValue().get(columnAssigment.descriptionColumnNumber);
                }
            }catch (Exception e)
            {
                priceList.setErrorMessage(priceList.getErrorMessage() + " - "+entry.getKey()+" - "+e.getMessage());
//                return priceList;
            }

            String brand = "-"; //brand
            try {
                brand = entry.getValue().get(columnAssigment.brandColumnNumber);
            }catch (Exception e)
            {
                priceList.setErrorMessage(priceList.getErrorMessage() + " - "+entry.getKey()+" - "+e.getMessage());
//                return priceList;
            }

            String comment = "-"; //comment

            String s = "0";
            BigDecimal unitNetPrice; //unitNetPrice
            try {
                s = entry.getValue().get(columnAssigment.unitNetPriceColumnNumber).trim();
                unitNetPrice = new BigDecimal(s).setScale(2);

            } catch (Exception e) {
//                priceList.setErrorMessage(e.getMessage());
//                s="0";
//                unitNetPrice = new BigDecimal(s).setScale(2);
                priceList.setErrorMessage(priceList.getErrorMessage() + " - "+entry.getKey()+" - "+e.getMessage());
                return priceList;
            }

            String unit = "-"; //unit
            try {
                 unit = entry.getValue().get(columnAssigment.unitColumnNumber);
            }
            catch (Exception e)
            {
                priceList.setErrorMessage(priceList.getErrorMessage() + " - "+entry.getKey()+" - "+e.getMessage());
//                return priceList;
            }

            int baseVatRate = 23;


            try {

                if (!entry.getValue().get(columnAssigment.baseVatRateColumnNumber).isEmpty() && !entry.getValue().get(columnAssigment.baseVatRateColumnNumber).isBlank()) {
                    try {
                        if (entry.getValue().get(columnAssigment.baseVatRateColumnNumber).contains("\\.")) {
                            baseVatRate = Integer.parseInt(entry.getValue().get(columnAssigment.baseVatRateColumnNumber).split("\\.")[0]);
                        } else if (entry.getValue().get(columnAssigment.baseVatRateColumnNumber).contains("\\,")) {
                            baseVatRate = Integer.parseInt(entry.getValue().get(columnAssigment.baseVatRateColumnNumber).split("\\,")[0]);
                        } else {
                            baseVatRate = Integer.parseInt(entry.getValue().get(columnAssigment.baseVatRateColumnNumber));
                        }

                    } catch (NumberFormatException e) {
                        priceList.setErrorMessage(priceList.getErrorMessage() + " - "+entry.getKey()+" - "+e.getMessage());
//                        return priceList;
                    } catch (Exception e) {
                        priceList.setErrorMessage(priceList.getErrorMessage() + " - "+entry.getKey()+" - "+e.getMessage());
//                        return priceList;
                    }
                }
            } catch (Exception e) {
                priceList.setErrorMessage(priceList.getErrorMessage() + " - "+entry.getKey()+" - "+e.getMessage());
            }
            //addedOn
            PriceListItem priceListItem = new PriceListItem(vendorName, referenceNumber, description, brand, comment, unitNetPrice, unit, baseVatRate);
            priceListItems.add(priceListItem);

        }

        priceList.setUserOwned(false);
        priceList.setPriceListItems(priceListItems);
        priceList.setName(priceListName);

        return priceList;
    }

    public static XSSFWorkbook getExcelWorkbook(Estimate estimate) {

        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(estimate.getName());
        sheet.setColumnWidth(0, 1000); //se. no.
        sheet.setColumnWidth(1, 4000); //referenceNumber
        sheet.setColumnWidth(2, 15000); //description
        sheet.setColumnWidth(3, 4000); //brand
        sheet.setColumnWidth(4, 4000); //unitNetPrice
        sheet.setColumnWidth(5, 2000); //quantity
        sheet.setColumnWidth(6, 4000); //unit
        sheet.setColumnWidth(7, 4000); //total net price
        sheet.setColumnWidth(8, 2000); //individualVatRate
        sheet.setColumnWidth(9, 15000); //comment

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

        for (EstimateItem ei : estimate.getEstimateItems()) {
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
