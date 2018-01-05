package com.vastio.rest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelImportUtil {
    public static List<Map<String, String>> parseExcel(InputStream fis) {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();;
        try {
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            // 除去表头和第一行
            // ComnDao dao = SysBeans.getComnDao();
            for (int i = firstRow + 1; i < lastRow + 1; i++) {
                Map map = new HashMap();

                XSSFRow row = sheet.getRow(i);
                int firstCell = row.getFirstCellNum();
                int lastCell = row.getLastCellNum();


                for (int j = firstCell; j < lastCell; j++) {

                    XSSFCell cell2 = sheet.getRow(firstRow + 1).getCell(j);

                    if (cell2.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                        cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
                    }

                    String key = cell2.getStringCellValue();

                    XSSFCell cell = row.getCell(j);

                    if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                        cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    }
                    String val = cell.getStringCellValue();
                    if (val == null || val.equals("")) {
                        val = "0";
                    }
                    // System.out.println(val);

                    if (i == firstRow + 1) {
                        break;
                    } else {
                        map.put(key, val);

                    }
                    // System.out.println(map);
                }
                if (i != firstRow + 1) {
                    data.add(map);
                    System.out.println(map);
                }
            }
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
