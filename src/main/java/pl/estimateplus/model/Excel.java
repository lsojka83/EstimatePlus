package pl.estimateplus.model;


import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import pl.estimateplus.entity.PriceList;
import pl.estimateplus.entity.PriceListItem;

import java.io.*;

import java.math.BigDecimal;
import java.math.BigInteger;
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

        String s = "799.89";

        double d = Double.parseDouble(s);
        d = d*100;
        Long l = null;
//        l = Long.parseLong();

        BigDecimal b  = new BigDecimal(s);

        System.out.println(b);

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
            e.printStackTrace();
        }
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Workbook workbook = null;
        List<PriceListItem> priceListItems = new ArrayList<>();

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
                    unitNetPrice = new BigDecimal("0").setScale(2); //unitNetPrice
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
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e)
                {
                    System.out.println(e.getMessage());
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
}
