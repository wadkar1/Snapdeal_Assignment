package com.sf.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
	public void writeXLSXFileByColumnNumber(String filename, int col, String value) throws IOException {
		try {
			FileInputStream file = new FileInputStream("externalFiles/downloadFiles/" + filename);

			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Cell cell = null;
			int rowCount = sheet.getPhysicalNumberOfRows();
			int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
			// Update the value of cell
			for (int i = 1; i < rowCount; i++) {
				XSSFRow sheetrow = sheet.getRow(i);
				cell = sheetrow.getCell(col);

				cell.setCellValue(value);
			}
			file.close();

			FileOutputStream outFile = new FileOutputStream(new File("externalFiles/downloadFiles/" + filename));
			workbook.write(outFile);
			outFile.close();
			Log.info("Updated the values in Excel file successfully");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeXLSXFileByColumnName(String filename, String column_Name, String value) throws IOException {
		FileInputStream file = new FileInputStream("externalFiles/downloadFiles/" + filename);

		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int totalRows = sheet.getPhysicalNumberOfRows();
		Map<String, Integer> map = new HashMap<String, Integer>(); // Create map
		XSSFRow row1 = sheet.getRow(0); // Get first row
		short first_Column = row1.getFirstCellNum(); // get the first column index for a row
		short last_Column = row1.getLastCellNum(); // get the last column index for a row
		for (short col_Index = first_Column; col_Index < last_Column; col_Index++) { // loop from first to last index
			XSSFCell cell = row1.getCell(col_Index); // get the cell
			map.put(cell.getStringCellValue(), cell.getColumnIndex()); // add the cell contents (name of column) and
																		// cell index to the map
		}
		int desired_Column_Index = map.get(column_Name); // get the column index for the column with header name
		for (int i = 1; i < totalRows; i++) {
			XSSFRow sheetrow = sheet.getRow(i);
			Cell cell = sheetrow.getCell(desired_Column_Index);
			cell.setCellValue(value);
		}
		file.close();

		FileOutputStream outFile = new FileOutputStream(new File("externalFiles/downloadFiles/" + filename));
		workbook.write(outFile);
		outFile.close();
		Log.info("Updated the values in Excel file successfully");
	}

	public void writeXLSXFileByColumnNameInParticularRow(String filename, int row_Number, String column_Name,
			String value) throws IOException {
		FileInputStream file = new FileInputStream("externalFiles/downloadFiles/" + filename);

		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int totalRows = sheet.getPhysicalNumberOfRows();
		Map<String, Integer> map = new HashMap<String, Integer>(); // Create map
		XSSFRow row1 = sheet.getRow(0); // Get first row
		short first_Column = row1.getFirstCellNum(); // get the first column index for a row
		short last_Column = row1.getLastCellNum(); // get the last column index for a row
		for (short col_Index = first_Column; col_Index < last_Column; col_Index++) { // loop from first to last index
			XSSFCell cell = row1.getCell(col_Index); // get the cell
			map.put(cell.getStringCellValue(), cell.getColumnIndex()); // add the cell contents (name of column) and
																		// cell index to the map
		}
		int desired_Column_Index = map.get(column_Name); // get the column index for the column with header name
		XSSFRow sheetrow = sheet.getRow(row_Number);
		Cell cell = sheetrow.getCell(desired_Column_Index);
		cell.setCellValue(value);
		file.close();

		FileOutputStream outFile = new FileOutputStream(new File("externalFiles/downloadFiles/" + filename));
		workbook.write(outFile);
		outFile.close();
		Log.info("Updated the values in Excel file successfully");
	}
}
