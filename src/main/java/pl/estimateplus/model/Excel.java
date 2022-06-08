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

    }


    public static PriceList importExcelData(MultipartFile multipartFile) {

        PriceList priceList = new PriceList();
        int itemsCount = 0;
        String priceListName = multipartFile.getOriginalFilename();
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
//                System.out.println(entry.getValue());
//                String vendorName = fileLocation.split(".")[0];
                String vendorName = multipartFile.getOriginalFilename().split("\\.")[0];

                String referenceNumber = entry.getValue().get(6); //Legrand - referenceNumber
                String description = "no description";
                if (!entry.getValue().get(7).isEmpty() && !entry.getValue().get(7).isBlank()) {
                    description = entry.getValue().get(7); //Legrand - description
                }
                String brand = entry.getValue().get(5); //Legrand - brand
                String comment = "no comment";//comment
                BigDecimal unitNetPrice = BigDecimal.valueOf(1l);

                try {
                    unitNetPrice = BigDecimal.valueOf(Long.valueOf(entry.getValue().get(9).trim())); //Legrand - unitNetPrice
                } catch (Exception e) {
//                    unitNetPrice = BigInteger.valueOf(Long.parseLong(entry.getValue().get(9).substring(0,entry.getValue().get(9).indexOf(point)))); //Legrand - unitNetPrice
//                    System.out.println(entry.getValue().get(6));
//                    System.out.println(entry.getValue().get(9));
//                    e.printStackTrace();
                }
                String unit = entry.getValue().get(10); //Legrand - unit
                int baseVatRate = 23;
                if (!entry.getValue().get(14).isEmpty() && !entry.getValue().get(14).isBlank()) {
                    try {
                        baseVatRate = Integer.parseInt(entry.getValue().get(14).split("\\.")[0]);

//                        baseVatRate = Integer.parseInt(entry.getValue().get(14)); //Legrand - baseVatRate
                    } catch (NumberFormatException e) {

//                    System.out.println(Arrays.toString(entry.getValue().get(14).split(entry.getValue().get(14).replaceAll("[0-9]",""))));
//                    baseVatRate = Integer.valueOf(entry.getValue().get(14).split(entry.getValue().get(14).replaceAll("[0-9]",""))[0]);

//                        System.out.println(entry.getValue().get(14));


//                        System.out.println( entry.getValue().get(14) != null && entry.getValue().get(14).matches("[0-9.]+"));
//                        baseVatRate = Integer.parseInt(entry.getValue().get(14).replace("0","").replace(".",""));

//                        System.out.println(entry.getValue().get(14).split(".")[0]);
//                        baseVatRate = Integer.parseInt(
//                                entry.getValue().get(14)
//                                        .substring(
//                                                0,
////                                                entry.getValue().get(14).indexOf(
////                                                        entry.getValue().get(14).replaceAll("[0-9]", "").trim()
////                                                )
//                                                2
//                                        )
//                        );
                        e.printStackTrace();

                    } catch (Exception e) {

//                        System.out.println(entry.getValue().get(6));
                        e.printStackTrace();


                    }
                }
                //addedOn
                PriceListItem priceListItem = new PriceListItem(vendorName, referenceNumber, description, brand, comment, unitNetPrice, unit, baseVatRate);
                priceListItems.add(priceListItem);
//                System.out.println(priceListItem);

            }
        }
        priceList.setPriceListItems(priceListItems);
        priceList.setName(priceListName);

        return priceList;
    }
}
