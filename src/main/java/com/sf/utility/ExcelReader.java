package com.sf.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	public LinkedHashMap<String, String> getTestEnvironmentFromExcel(String workbook_Location, String sheet_Name,
			String server_Info_Required) throws IOException {
		File file = new File(workbook_Location);
		FileInputStream inputStream = new FileInputStream(file);

		Workbook workbook = null;
		workbook = new HSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheet(sheet_Name);

		LinkedHashMap<String, String> envDetails = new LinkedHashMap<String, String>();

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum(); /* Function to read Environment Details */

		for (int i = 0; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);

			if (row.getCell(0).getStringCellValue().equalsIgnoreCase(server_Info_Required)) {
				for (int j = 0; j < row.getLastCellNum(); j++) {
					envDetails.put(sheet.getRow(0).getCell(j).getStringCellValue(),
							row.getCell(j).getStringCellValue());
				}
			}
		}
		return envDetails;
	}

	public Object[][] testData(String file_Location, String sheet_Name) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file_Location); // Excel sheet file location get mentioned
																				// here
		DataFormatter formatter = new DataFormatter();
		Workbook workbook;
		Sheet worksheet;

		workbook = new HSSFWorkbook(fileInputStream); // get my workbook
		worksheet = workbook.getSheet(sheet_Name);// get my sheet from workbook
		Row row = worksheet.getRow(0); // get my Row which start from 0

		int RowNum = worksheet.getPhysicalNumberOfRows();// count my number of Rows
		int ColNum = row.getLastCellNum(); // get last ColNum

		Object Data[][] = new Object[RowNum - 1][ColNum]; // pass my count data in array /*Function to read Test Data
															// from Excel Sheet*/

		for (int i = 0; i < RowNum - 1; i++) // Loop work for Rows
		{
			Row cuurentrow = worksheet.getRow(i + 1);

			for (int j = 0; j < ColNum; j++) // Loop work for colNum
			{
				if (cuurentrow == null)
					Data[i][j] = "";
				else {
					HSSFCell cell = (HSSFCell) cuurentrow.getCell(j);
					if (cell == null)
						Data[i][j] = ""; // if it get Null value it pass no data
					else {
						String value = formatter.formatCellValue(cuurentrow.getCell(j));
						Data[i][j] = value; // This formatter get my all values as string i.e integer, float all type
											// data value
					}
				}
			}
		}
		;
		return Data;
	}

	public LinkedHashMap<String, Double> orgAndInvoicesFromExcel() throws IOException {
		List<String> duplicateOrgs = new ArrayList<String>();
		LinkedHashSet<String> orgs;
		LinkedHashMap<String, Double> orgsAndSum = new LinkedHashMap<String, Double>();

		File file = new File("February.xlsx");
		FileInputStream inputStream = new FileInputStream(file.getAbsolutePath());
		Workbook workbook = null;
		workbook = new XSSFWorkbook(inputStream);
		DataFormatter formatter = new DataFormatter();
		Sheet sheet = workbook.getSheet("February");

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

		for (int i = 1; i < rowCount + 1; i++) {
			Row row = sheet.getRow(i);
			duplicateOrgs.add(formatter.formatCellValue(row.getCell(0)));

		}

		orgs = new LinkedHashSet<String>(duplicateOrgs);

		Iterator value = orgs.iterator();

		while (value.hasNext()) {
			String org = (String) value.next();
			double sum = 0.0f;
			for (int i = 1; i < rowCount + 1; i++) {
				Row row = sheet.getRow(i);
				if (org.equals(formatter.formatCellValue(row.getCell(0)))) {
					if (formatter.formatCellValue(row.getCell(1)).equals("")) {
						sum = sum + 0.0f;
						sum = Math.round(sum * 100D) / 100D;
					} else {
						sum = sum + Float.valueOf(formatter.formatCellValue(row.getCell(1)));
						sum = Math.round(sum * 100D) / 100D;
					}
				}
			}
			orgsAndSum.put(org, sum);
		}
		return orgsAndSum;
	}

	public String getDesiredCellValue(String fileName, String identifierCellName, String desiredCellName,
			String identifierCellValue) throws IOException {
		File file = new File("externalFiles/downloadFiles/" + fileName);
		FileInputStream inputStream = new FileInputStream(file);
		XSSFWorkbook workbook = null;
		workbook = new XSSFWorkbook(inputStream);
		DataFormatter formatter = new DataFormatter();

		Sheet sheet = workbook.getSheet("Mismatch Report");
		int rowCount = sheet.getPhysicalNumberOfRows();
		int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();

		int fileNoColumn = 0;
		int dataColumn = 0;
		String desiredCellValue = null;

		for (int i = 0; i < columnCount; i++) {
			if (formatter.formatCellValue(sheet.getRow(0).getCell(i)).equals(identifierCellName)) {
				fileNoColumn = i;
			}

			else if (formatter.formatCellValue(sheet.getRow(0).getCell(i)).equals(desiredCellName)) {
				dataColumn = i;
			}
		}

		for (int i = 0; i < rowCount; i++) {
			if (formatter.formatCellValue(sheet.getRow(i).getCell(fileNoColumn)).equals(identifierCellValue)) {
				desiredCellValue = sheet.getRow(i).getCell(dataColumn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
						.getStringCellValue();
			}
		}

		return desiredCellValue;
	}

	public String readXLSXFileByColumnNumber(String filename, int row, int col) throws IOException {
		FileInputStream file = new FileInputStream("externalFiles/downloadFiles/" + filename);

		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		String data = null;
		XSSFRow sheetrow = sheet.getRow(row);
		cell = sheetrow.getCell(col);
		if (cell.getCellType() == CellType.STRING) {
			data = cell.getStringCellValue();
		} else if (cell.getCellType() == CellType.NUMERIC) {
			data = String.valueOf(cell.getNumericCellValue());
		}
		return data;
	}

	public String readXLSXFileByColumnName(String filename, int row, String column_Name) throws IOException {
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
		String data = null;
		XSSFRow dataRow = sheet.getRow(row);
		int desired_Column_Index = map.get(column_Name); // get the column index for the column with header name
		XSSFCell cell1 = dataRow.getCell(desired_Column_Index); // Get the cells for each of the indexes
		if (cell1.getCellType() == CellType.STRING) {
			data = cell1.getStringCellValue();
		} else if (cell1.getCellType() == CellType.NUMERIC) {
			data = String.valueOf(cell1.getNumericCellValue());
		}
		System.out.print(data);
		return data;
	}

	public int getTotalNumberofRows(String filename) throws IOException {
		FileInputStream file = new FileInputStream("externalFiles/downloadFiles/" + filename);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int totalRows = sheet.getPhysicalNumberOfRows();
		return totalRows;
	}

	public int getTotalNumberofColumn(String filename) throws IOException {
		FileInputStream file = new FileInputStream("externalFiles/downloadFiles/" + filename);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int noOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
		return noOfColumns;
	}

	public int getTotalNumberofColumnDynamicExport(String filename, int row) throws IOException {
		FileInputStream file = new FileInputStream("externalFiles/downloadFiles/" + filename);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int noOfColumns = sheet.getRow(row).getPhysicalNumberOfCells();
		return noOfColumns;
	}
}