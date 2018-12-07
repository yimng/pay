/**
 *
 * @Title ReadExcelUtil.java
 * @author wanglu
 * @date 2018年5月8日
 */
package com.edupay.commons.utils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * 读取Excel 
 * Title ReadExcelUtil
 * @author wanglu
 * @date 2018年5月8日
 */
public class ReadExcelUtil {

	/**
	 * 解析excel数据
	 * @param file 导入excel文件
	 * @param numSheet 要解析的sheet
	 * @param rowStr 数据开始行
	 * @param SDF2  日期格式 默认 yyyy-MM-dd
	 * @return
	 * @throws IOException 
	 */
	public static List<Map<String, Object>> readExcel(MultipartFile file,
			Integer numSheet, Integer rowStr,SimpleDateFormat SDF2) throws IOException {
		if(SDF2 == null){
			SDF2 = new SimpleDateFormat("yyyy-MM-dd");
		}
		String fileName = file.getOriginalFilename();
		if (fileName.endsWith("xls")) {
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
			return readXls(hssfWorkbook, numSheet, rowStr,SDF2);
		} else if (fileName.endsWith("xlsx")) {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
			return readXlsx(xssfWorkbook, numSheet, rowStr,SDF2);
		}
		return null;
	}

	/**
	 * Read the Excel 2010
	 * @param xssfWorkbook
	 * @param numSheet
	 * @param rowStr
	 * @return
	 * @throws IOException
	 */
	private static List<Map<String, Object>> readXlsx(XSSFWorkbook xssfWorkbook,
			Integer numSheet, Integer rowStr,SimpleDateFormat SDF2) throws IOException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// Read the Sheet
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
		removeBlankRows(xssfSheet);
		// Read the Row
		for (int rowNum = rowStr; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if (xssfRow == null) {
				continue;
			}
			Map<String, Object> row = new HashMap<String, Object>();
			// 循环列Cell
			for (int cellNum = 0; cellNum <= xssfRow.getLastCellNum(); cellNum++) {
				XSSFCell xssfCell = xssfRow.getCell(cellNum);
				if (xssfCell == null) {
					continue;
				}
				// 去掉首尾空格
				String parsedValue = getValue(xssfCell,SDF2);
				if (StringUtils.isBlank(parsedValue)) {
					parsedValue = "";
				} else {
					parsedValue = parsedValue.trim();
				}
				if(StringUtils.isNotBlank(parsedValue)){
					row.put("C_" + cellNum, parsedValue);
				}
			}
			if(!row.isEmpty()){
				list.add(row);
			}
		}
		return list;
	}

	/**
	 * Read the Excel 2003-2007
	 * @param hssfWorkbook
	 * @param numSheet
	 * @param rowStr
	 * @return
	 * @throws IOException
	 */
	private static List<Map<String, Object>> readXls(HSSFWorkbook hssfWorkbook, Integer numSheet,
			Integer rowStr,SimpleDateFormat SDF2) throws IOException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// Read the Sheet
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
		// Read the Row
		for (int rowNum = rowStr; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow hssfRow = hssfSheet.getRow(rowNum);
			if (hssfRow == null) {
				continue;
			}
			Map<String, Object> row = new HashMap<String, Object>();
			// 循环列Cell
			for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
				HSSFCell hssfCell = hssfRow.getCell(cellNum);
				if (hssfCell == null) {
					continue;
				}
				// 去掉首尾空格
				String parsedValue = getValue(hssfCell,SDF2);
				if (StringUtils.isBlank(parsedValue)) {
					parsedValue = "";
				} else {
					parsedValue = parsedValue.trim();
				}
				if(StringUtils.isNotBlank(parsedValue)){
					row.put("C_" + cellNum, parsedValue);
				}
			}
			if(!row.isEmpty()){
				list.add(row);
			}
		}
		return list;
	}

	/**
	 * Remove empty rows after the last row contains data. xlsx version.
	 * 
	 * @param hsheet
	 */
	public static void removeBlankRows(XSSFSheet hsheet) {
		boolean stop = false;
		boolean nonBlankRowFound;
		short c;
		XSSFRow lastRow = null;
		XSSFCell cell = null;

		while (stop == false) {
			nonBlankRowFound = false;
			lastRow = hsheet.getRow(hsheet.getLastRowNum());
			if (lastRow.getFirstCellNum() >= 0 && lastRow.getLastCellNum() >= 0) {
				for (c = lastRow.getFirstCellNum(); c <= lastRow
						.getLastCellNum(); c++) {
					cell = lastRow.getCell((int) c);
					if (cell != null
							&& lastRow.getCell((int) c).getCellType() != XSSFCell.CELL_TYPE_BLANK) {
						nonBlankRowFound = true;
					}
				}
			}
			if (nonBlankRowFound == true) {
				stop = true;
			} else {
				hsheet.removeRow(lastRow);
			}
		}
	}

	/**
	 * Remove empty rows after the last row contains data. xls version.
	 * 
	 * @param hsheet
	 */
	public static void removeBlankRows(HSSFSheet hsheet) {
		boolean stop = false;
		boolean nonBlankRowFound;
		short c;
		HSSFRow lastRow = null;
		HSSFCell cell = null;

		while (stop == false) {
			nonBlankRowFound = false;
			lastRow = hsheet.getRow(hsheet.getLastRowNum());
			for (c = lastRow.getFirstCellNum(); c <= lastRow.getLastCellNum(); c++) {
				cell = lastRow.getCell((int) c);
				if (cell != null
						&& lastRow.getCell((int) c).getCellType() != XSSFCell.CELL_TYPE_BLANK) {
					nonBlankRowFound = true;
				}
			}
			if (nonBlankRowFound == true) {
				stop = true;
			} else {
				hsheet.removeRow(lastRow);
			}
		}
	}

	/**
	 * 解析cell内容统一返回String
	 * 
	 * @param xssfRow
	 * @return
	 */
	private static String getValue(XSSFCell xssfRow,SimpleDateFormat SDF2) {
		if (xssfRow.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(xssfRow)) {
				return SDF2.format(xssfRow.getDateCellValue());
			}
			DecimalFormat df = new DecimalFormat("#.##");
			return df.format(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	/**
	 * 解析cell内容统一返回String
	 * 
	 * @param hssfCell
	 * @return
	 */
	private static String getValue(HSSFCell hssfCell,SimpleDateFormat SDF2) {
		if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
				return SDF2.format(hssfCell.getDateCellValue());
			}
			DecimalFormat df = new DecimalFormat("#.##");
			return df.format(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

}
