package com.utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelUtility {

    public static Map<String, String> getSheetDataAsMap(String sheetName, String id, String testDataExcelPath) {
        Map<String, String> rowData = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(testDataExcelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet " + sheetName + " not found!");
                return rowData;
            }
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                System.out.println("Header row is empty!");
                return rowData;
            }
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                if (currentRow == null) continue;

                Cell idCell = currentRow.getCell(0); // Assuming ID is in the first column
                if (idCell != null && idCell.toString().equals(id)) {
                    // Map header-value pairs for the matching row
                    for (int colIndex = 0; colIndex < headerRow.getLastCellNum(); colIndex++) {
                        Cell headerCell = headerRow.getCell(colIndex);
                        Cell valueCell = currentRow.getCell(colIndex);

                        if (headerCell != null) {
                            String header = headerCell.getStringCellValue();
                            String value = valueCell != null ? valueCell.toString() : "";
                            rowData.put(header, value);
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rowData;
    }

    public static List<String> getColumnValues(String filePath, String sheetName, String columnName) {
        List<String> values = new ArrayList<>();
        int columnIndex = -1;

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName); // Read the specified sheet
            if (sheet == null) {
                System.out.println("Sheet '" + sheetName + "' not found!");
                return values;
            }

            Row headerRow = sheet.getRow(0); // Get the header row
            if (headerRow == null) {
                System.out.println("Header row is empty!");
                return values;
            }

            // Find the column index dynamically
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().equalsIgnoreCase(columnName)) {
                    columnIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (columnIndex == -1) {
                System.out.println("Column '" + columnName + "' not found!");
                return values;
            }

            // Loop through rows (starting from row 1, skipping header)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    if (cell != null) {
                        values.add(cell.toString()); // Store cell value as a string
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }

    public static List<Long> getColumnValuesAsLong(String filePath, String sheetName, String columnName) {
        List<Long> values = new ArrayList<>();
        int columnIndex = -1;

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet '" + sheetName + "' not found!");
                return values;
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                System.out.println("Header row is empty!");
                return values;
            }

            // Find column index dynamically
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().equalsIgnoreCase(columnName)) {
                    columnIndex = cell.getColumnIndex();
                    break;
                }
            }

            if (columnIndex == -1) {
                System.out.println("Column '" + columnName + "' not found!");
                return values;
            }

            // Loop through rows (starting from row 1, skipping header)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case NUMERIC:
                                values.add(Math.round(cell.getNumericCellValue())); // Convert double to long
                                break;
                            case STRING:
                                try {
                                    values.add(Long.parseLong(cell.getStringCellValue().trim())); // Convert string to long if possible
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid number format at row " + i + ": " + cell.getStringCellValue());
                                }
                                break;
                            default:
                                System.out.println("Skipping unsupported cell type at row " + i);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }


}
