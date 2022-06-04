package pl.estimateplus.model;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import org.springframework.core.io.Resource;
import pl.estimateplus.entity.PriceListItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;


public class Excel {


    public static void main(String[] args) {
        importExcelData();
    }


    public static List<PriceListItem> importExcelData() {


//        FileInputStream file = null; //direct run
        File file = null; //webapp

        Workbook workbook = null;
        List<PriceListItem> priceListItems = new ArrayList<>();

        String fileLocation = "Legrand.xlsx";
        Resource resource = new ClassPathResource(fileLocation);

        try {
//            InputStream input =  resource.getInputStream();
//            file = new FileInputStream(new File(fileLocation));

            file = resource.getFile();
            workbook = new XSSFWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        catch (InvalidFormatException e) {

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

            for (Map.Entry<Integer, List<String>> entry : data.entrySet()) {
                //vendorName
//                System.out.println(entry.getValue());
//                String vendorName = fileLocation.split(".")[0];
                String vendorName = "Legrand";
                String referenceNumber = entry.getValue().get(6); //Legrand - referenceNumber
                String description = "no description";
                if (!entry.getValue().get(7).isEmpty() && !entry.getValue().get(7).isBlank()) {
                    description = entry.getValue().get(7); //Legrand - description
                }
                String brand = entry.getValue().get(5); //Legrand - brand
                String comment = "no comment";//comment
                BigInteger unitNetPrice = BigInteger.valueOf(1l);
                String point = entry.getValue().get(9).trim().replaceAll("[0-9]", "");

                try {
                    unitNetPrice = BigInteger.valueOf(Long.valueOf(entry.getValue().get(9).trim())); //Legrand - unitNetPrice
                }catch (Exception e)
                {
//                    unitNetPrice = BigInteger.valueOf(Long.parseLong(entry.getValue().get(9).substring(0,entry.getValue().get(9).indexOf(point)))); //Legrand - unitNetPrice
//                    System.out.println(entry.getValue().get(6));
//                    System.out.println(entry.getValue().get(9));
//                    e.printStackTrace();
                }
                String unit = entry.getValue().get(10); //Legrand - unit
                int baseVatRate = 23;
                if (!entry.getValue().get(14).isEmpty() && !entry.getValue().get(14).isBlank()) {
                    try {
                        baseVatRate = Integer.parseInt(entry.getValue().get(14)); //Legrand - baseVatRate
                    } catch (NumberFormatException e) {
//                    System.out.println(entry.getValue().get(14).replaceAll("[0-9]",""));
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

                    } catch (Exception e) {

//                        System.out.println(entry.getValue().get(6));
                        e.printStackTrace();


                    }
                }
                //addedOn
                PriceListItem priceListItem = new PriceListItem(vendorName, referenceNumber, description, brand, comment, unitNetPrice, unit, baseVatRate);
                priceListItems.add(priceListItem);
                System.out.println(priceListItem);

            }
        }
        return priceListItems;
    }
}
