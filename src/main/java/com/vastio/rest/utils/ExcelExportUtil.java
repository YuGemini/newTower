package com.vastio.rest.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExportUtil {

	public static XSSFWorkbook generateExcel(List<Map<String, String>> list,
			String title) {
		XSSFWorkbook book = new XSSFWorkbook();
		try {
			XSSFSheet sheet = book.createSheet("Sheet1");
			sheet.autoSizeColumn(1, true);// 自适应列宽度
			// 样式设置
			XSSFCellStyle style = book.createCellStyle();
			style.setFillForegroundColor(new XSSFColor(new Color(66, 139, 202,
					1)));
			style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 生成一个字体
			XSSFFont font = book.createFont();
			// font.setColor(XSSFColor.VIOLET.index);
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 把字体应用到当前的样式
			style.setFont(font);

			XSSFCellStyle style2 = book.createCellStyle();
			style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			// 设置上下左右边框
			style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 填充表头标题
			int colSize = list.get(0).entrySet().size();
			System.out.println("size:" + colSize);
			// 合并单元格供标题使用(表名)
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colSize - 1));
			XSSFRow firstRow = sheet.createRow(0);// 第几行（从0开始）
			XSSFCell firstCell = firstRow.createCell(0);
			firstCell.setCellValue(title);
			firstCell.setCellStyle(style);

			// 填充表头header
			XSSFRow row = sheet.createRow(1);
			Set<Entry<String, String>> set = list.get(0).entrySet();
			List<Entry<String, String>> l = new ArrayList<Map.Entry<String, String>>(
					set);
			System.out.println("l:" + l.size());
			for (int i = 0; i < l.size(); i++) {
				String key = l.get(i).getKey();
				System.out.println(key);
				XSSFCell cell = row.createCell(i);
				cell.setCellValue(key);
				cell.setCellStyle(style2);
			}

			// 填充表格内容
			System.out.println("list:" + list.size());
			for (int i = 0; i < list.size(); i++) {
				XSSFRow row2 = sheet.createRow(i + 2);// index：第几行
				Map<String, String> map = list.get(i);
				Set<Entry<String, String>> set2 = map.entrySet();
				List<Entry<String, String>> ll = new ArrayList(set2);
				for (int j = 0; j < ll.size(); j++) {
					String val = ll.get(j).getValue();
					XSSFCell cell = row2.createCell(j);// 第几列：从0开始
					cell.setCellValue(val);
					cell.setCellStyle(style2);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return book;
	}

}
