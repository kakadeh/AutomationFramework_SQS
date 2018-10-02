package handlers;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import businessLogics.LogFunctions;
import commonUtils.UtilityFunctions;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class ExcelHandler {
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Closing All Excel Files If Already Opened
	/// </summary>
	
	public static void CloseAllExcelFiles() throws IOException, Exception
	{
		if (UtilityFunctions.isProcessRunning("EXCEL.EXE"))
		{
			Runtime.getRuntime().exec("taskkill /IM EXCEL.EXE");
		}
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Creating Excel Workbook
	/// </summary>
	
	public static Workbook ExcelOpenWorkbook(String path) throws BiffException, IOException
    {
        Workbook excelWorkBook;
        File excelwoorbookfile=new File(path);
        excelWorkBook=Workbook.getWorkbook(excelwoorbookfile);
        return excelWorkBook;
    }
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Opening Excel Workbook
	/// </summary>
	
	public static Workbook OpenExcelToRead(String FilePath) throws IOException, BiffException
	{
		File inputWorkbook = new File(FilePath);
		if(inputWorkbook.exists())
        {
			return Workbook.getWorkbook(inputWorkbook);
			
        }
		else
		{
			LogFunctions.LogEntry("File does not exist...", false);
			return null;
		}
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Reading Data From Excel Workbook
	/// </summary>
	
	public static String ReadExcelCell(Workbook WorkbookToRead,int SheetIndex, int Row,int Column) throws IOException, BiffException
	{
		jxl.Sheet sheet = WorkbookToRead.getSheet(SheetIndex); 
		String cellValue =((jxl.Sheet) sheet).getCell(Column, Row).getContents().toString(); 
		return cellValue;
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Closing Excel Workbook
	/// </summary>
	
	public static void CloseExcel(Workbook WorkbookToClose) throws IOException, BiffException
	{
		WorkbookToClose.close();
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Fetchinng Cell Value From Excel Workbook 
	/// </summary>
	
	public static String FetchCellValue(String Sheetpath,int SheetIndex, int Row,int Column) throws Exception
	{
		Workbook workbook = Workbook.getWorkbook(new File(Sheetpath)); 
		jxl.Sheet sheet = workbook.getSheet(SheetIndex); 
		String cellValue =((jxl.Sheet) sheet).getCell(Column, Row).getContents().toString(); 
		workbook.close();
		return (cellValue);
	}
	
	/*
	public static String FetchCellValue(String Sheetpath,int SheetIndex, int Row,int Column) throws Exception
	{
		File inputWorkbook = new File(Sheetpath);
		Workbook workbook = null ;
		jxl.Sheet sheet;
		String cellvalue="";
		if(inputWorkbook.exists())
        {
			workbook = Workbook.getWorkbook(inputWorkbook);
			sheet = workbook.getSheet(0);
			 cellvalue= sheet.getCell(Column, Row).getContents().toString();
			 workbook.close();
			 return (cellvalue);
        }
		else
			 LogFunctions.LogEntry("File does not exist...", false);
		return "";
	}
	*/
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Writing String Data in Excel Sheet Cell
	/// </summary>
	
	public static void WriteToSheet(String SheetPath, int SheetIndex,int Row,int Column,String Value) throws Exception
	{
		File WritableFile=new File(SheetPath);
		FileInputStream FileToWrite = new FileInputStream(WritableFile);                
		HSSFWorkbook ReportWorkbook = new HSSFWorkbook(FileToWrite);
		HSSFSheet Reportsheet = ReportWorkbook.getSheetAt(SheetIndex);
		HSSFCell cell=Reportsheet.getRow(Row).getCell(Column);
	    cell.setCellValue(Value);
		FileOutputStream CloseFile=new FileOutputStream(WritableFile);
		ReportWorkbook.write(CloseFile);
		CloseFile.close();
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Writing Link in Excel Sheet Cell
	/// </summary>
	
	public static void WriteLinkToSheet(String SheetPath, int SheetIndex,int Row,int Column,String Value, String LinkValue) throws Exception
	{
		File WritableFile=new File(SheetPath);
		FileInputStream FileToWrite = new FileInputStream(WritableFile);                
		HSSFWorkbook ReportWorkbook = new HSSFWorkbook(FileToWrite);
		HSSFSheet Reportsheet = ReportWorkbook.getSheetAt(SheetIndex);
		//HSSFHyperlink Link=new HSSFHyperlink(HSSFHyperlink.LINK_URL);
		//Link.setAddress(LinkValue);
		
		HSSFCell cell=Reportsheet.getRow(Row).getCell(Column);
	    cell.setCellValue(Value);
	    //cell.setHyperlink(Link);
		FileOutputStream CloseFile=new FileOutputStream(WritableFile);
		ReportWorkbook.write(CloseFile);
		CloseFile.close();
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Reading Excel Sheet
	/// </summary>
	
	public static void ReadSheet(String FilePath) throws BiffException, IOException
	{
		File ReadableFile = new File(FilePath);
		Workbook SuiteWorkbook=Workbook.getWorkbook(ReadableFile);              
		jxl.Sheet SuiteSheet =  SuiteWorkbook.getSheet(0);
	}
	/*public static void copyRow(HSSFWorkbook workbook, HSSFSheet worksheet, int sourceRowNum, int destinationRowNum)
	{
		HSSFRow newRow = worksheet.getRow(destinationRowNum);
        HSSFRow sourceRow = worksheet.getRow(sourceRowNum);
        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) 
        {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else 
        {
            newRow = worksheet.createRow(destinationRowNum);
        }
        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) 
        {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = sourceRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }
         // Copy style from old cell and apply to new cell
            HSSFCellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            ;
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (newCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
	}
	}*/

}
