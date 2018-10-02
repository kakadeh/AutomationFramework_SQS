package businessLogics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import commonUtils.PropertiesAndConstants;
import commonUtils.UtilityFunctions;
import handlers.ExcelHandler;
import handlers.SeleniumHandler;
import jxl.Sheet;
import jxl.Workbook;


public class SuiteFunctions {
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Processing Suit File ,Calling a Function to Add Common Actions In Temp Script Files and Executing them
	/// </summary>
	
	public static void ProcessSuiteFile() throws IOException
	{
		String startTestTime;

		//bool _processed = false; // ***** Can be Used for Parallel test run
		PropertiesAndConstants.IsTestExecutionPassed = true;
		
		String suiteFile = PropertiesAndConstants.TempTestSuitePath;
		
		PropertiesAndConstants.TestCount = 0;
		PropertiesAndConstants.TestPassCount = 0;
		PropertiesAndConstants.TestFailCount = 0;
		PropertiesAndConstants.JavaScriptErrorCount = 0;

		try
		{  
			File file = new File(suiteFile);
		    
			if(file.exists())
			{
			  
			    
				Workbook SuiteWorkbook=Workbook.getWorkbook(file);    	   	    
				Sheet  SuiteSheet =  SuiteWorkbook.getSheet(0);
				String updateResult = PropertiesAndConstants.ExcelReportFile;
			
                File updateResultFile=new File(updateResult);
				FileInputStream reportFile = new FileInputStream(updateResultFile);        		 
				HSSFWorkbook ReportWorkbook = new HSSFWorkbook(reportFile);
				HSSFSheet Reportsheet = ReportWorkbook.getSheetAt(0);
				HSSFCell cell;
				//String reportfile=new File(updateResult) ;
				// Workbook reportbook;
				//reportbook=Workbook.getWorkbook(reportfile);
				//	 Sheet reportsheet = reportbook.getSheet("Sheet1");
				
				int reportOffset = 23;
				int sno = 0;
				int rowcount_suite =  SuiteSheet.getRows();
				
				for (int row_suite = 1; row_suite <= rowcount_suite; row_suite++)
				{
					
					PropertiesAndConstants.FailStepsPerScriptCounter = 0;
					
					try
					{
						
						/*FileInputStream file1 = new FileInputStream(new File(suiteFile));        		 
						HSSFWorkbook SuiteWorkbook = new HSSFWorkbook(file1);
						HSSFSheet SuiteSheet = SuiteWorkbook.getSheetAt(0);*/

						// ***** Add get TestScript Name Without Path
						// String testname = (String)((Excel.Range)suitesheet.Cells[row_suite, 1]).Text;
						String testname= SuiteSheet.getCell(0, row_suite).getContents();
					
						if (testname.equals(null)||testname.equals(""))
						{
							// LogFunctions.LogEntry(String.Format("Processing Suite file was completed in row: " + row_suite, false));
							LogFunctions.LogEntry(String.format("Processing Suite file was completed in row:"+row_suite), false);
							break;
						}
					
				
						File testfile=new File(PropertiesAndConstants.TempDirectoryPath + testname);
						
						PropertiesAndConstants.TestScriptName = testname.replace(".xls", "");	
						// Test was crash in this case  (for next iteration) 
						String run_value =  SuiteSheet.getCell(1,row_suite).getContents();
						
						//String run_Value = (String)((Excel.Range)suitesheet.Cells[row_suite, 2]).Text;
                     
						if ((!UtilityFunctions.IsNullOrEmpty(run_value))&& run_value.trim().toUpperCase().equals( "YES"))
						{
							LogFunctions.LogEntry("Start processing script file : " + PropertiesAndConstants.TestScriptName, false);
							PropertiesAndConstants.scriptExecutionResult = "Pass";
							String testScript = PropertiesAndConstants.TempTestScriptPath+ PropertiesAndConstants.TestScriptName + ".xls";
					      
							LogFunctions.LogEntry(String.format("Processing to add Common Steps..."), false);
							ScriptFunctions.AddCommonActionsInTempScript(testScript);
							LogFunctions.LogEntry(String.format("Process of add Common Steps - Completed"),false);
							LogFunctions.LogEntry("Launch Web Driver process...", false);
							
							//SeleniumHandler.driver = null;
							SeleniumHandler.SwitchDriver(); // ***** Create new Instance of Driver
						
							startTestTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
							Date date=new Date();
							long Starttime=System.currentTimeMillis();
							long Starttimesecond=TimeUnit.SECONDS.toSeconds(Starttime);
							PropertiesAndConstants.MainDriverWindowHandle = PropertiesAndConstants.Selenium.getWindowHandle();
						    String TemptestScript=PropertiesAndConstants.TempDirectoryPath+PropertiesAndConstants.TestScriptName + ".xls";
						   	Workbook testScriptPath=ExcelHandler.ExcelOpenWorkbook(TemptestScript);
						    LogFunctions.LogEntry(String.format("Script path : "+TemptestScript),false);
                            
							// **** Main Script Execution Action
						
							KeyActionFunctions keyAction = new KeyActionFunctions();
						    ScriptFunctions.validateTestDataExist(TemptestScript);
						
							keyAction.ProcessScriptTemplate(testScriptPath); // ***** Try to Add // Wait Exception
							if (testScriptPath != null)
							{
								testScriptPath.close();

								// System.Runtime.InteropServices.Marshal.ReleaseComObject(testScriptPath);
							}
							// ****
							
							String finishTestTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
							long Finishtime=System.currentTimeMillis();
							//  String executionTime = (finishTestTime - startTestTime).ToString("mm\\:ss\\.f");
                           
							Long executionTimeinSec=(Finishtime-Starttime);
							
							double seconds= ((executionTimeinSec/1000.0)%60.0);

							seconds =Double.parseDouble(new DecimalFormat("##.#").format(seconds));

							int minutes=(int) (((executionTimeinSec-seconds)/1000)/60);


							
							TimeUnit.MILLISECONDS.toSeconds(executionTimeinSec);
							//String executionTime=executionTimeinSec.toString();
							String executionTime=String.format("%02d", minutes)+":"+seconds; 

	                     	sno = sno + 1;
							StringBuilder snum = new StringBuilder();
							snum.append("");
							snum.append(sno);
							String Snum = snum.toString();
							cell = Reportsheet.getRow(reportOffset).getCell(0);
							cell.setCellValue(Snum);

							cell = Reportsheet.getRow(reportOffset).getCell(1);
							cell.setCellValue(PropertiesAndConstants.TestScriptName);

							cell = Reportsheet.getRow(reportOffset).getCell(4);
							cell.setCellValue(executionTime);
						
							SeleniumHandler.CloseSeleniumDriver(); // Close Driver
							PropertiesAndConstants.IsFlashDisabled = false; // Reset Flash parameter
							SeleniumHandler.driver = null;							
                            
							if (PropertiesAndConstants.scriptExecutionResult == "Fail")
							{
								PropertiesAndConstants.TestCount++;
								PropertiesAndConstants.TestFailCount++;
												
								String DateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

								LogFunctions.LogEntry(String.format("***** Test script result :"+PropertiesAndConstants.scriptExecutionResult+"*****", PropertiesAndConstants.scriptExecutionResult),false);
								LogFunctions.LogEntry("*** Test script Execution Time: " + executionTime + " ***", false);
								LogFunctions.LogEntry("*************************************************", false);
								 cell=Reportsheet.getRow(reportOffset).getCell(3);
								 cell.setCellValue(PropertiesAndConstants.scriptExecutionResult);
							}
							else
								if (PropertiesAndConstants.scriptExecutionResult == "Pass")
								{
									PropertiesAndConstants.TestCount++;
									PropertiesAndConstants.TestPassCount++;

									//cell=SuiteSheet.getRow(row_suite).getCell(3);
									String DateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
									LogFunctions.LogEntry(String.format("***** Test script result :"+PropertiesAndConstants.scriptExecutionResult+" *****" ),false);
									LogFunctions.LogEntry("*** Test script Execution Time: " + executionTime + " ***", false);
									LogFunctions.LogEntry("*************************************************", false);
									cell=Reportsheet.getRow(reportOffset).getCell(3);
									cell.setCellValue(PropertiesAndConstants.scriptExecutionResult);

									// reportsheet.Cells[reportOffset, 4] = PropertiesAndConstants.scriptExecutionResult;
								}
								else
								{
									PropertiesAndConstants.scriptExecutionResult = "NOT RUN";
									String DateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
									LogFunctions.LogEntry(String.format("Test script name :"+ PropertiesAndConstants.TestScriptName),false);
									LogFunctions.LogEntry(String.format("***** Test script result :"+PropertiesAndConstants.scriptExecutionResult+" *****" ),false);

									cell=Reportsheet.getRow(reportOffset).getCell(3);
									cell.setCellValue(PropertiesAndConstants.scriptExecutionResult);

								}


							reportOffset = reportOffset + 1;
						}
					}
					catch (Exception srcriptRunExc)
					{
						LogFunctions.LogEntry(String.format("Error In the processing of Script file... "+srcriptRunExc.getMessage()), false);
						LogFunctions.LogEntry(String.format("Details:"+ srcriptRunExc), false);
						LogFunctions.LogEntry(String.format("Details:"+ srcriptRunExc.getMessage()), false);
						srcriptRunExc.printStackTrace();
						PropertiesAndConstants.IsTestExecutionPassed = false;
						reportOffset = reportOffset + 1;
						SeleniumHandler.CloseSeleniumDriver();
						Thread.sleep(500);
						continue;
					}
				}
				 	LogFunctions.LogEntry("==================================", false);
	                LogFunctions.LogEntry("Total Statistics: ", false);
	                LogFunctions.LogEntry("Total Tests Run : " + PropertiesAndConstants.TestCount, false);
	                Reportsheet.getRow(10).getCell(2).setCellValue(PropertiesAndConstants.TestCount);
	                LogFunctions.LogEntry("Tests PASSED : " + PropertiesAndConstants.TestPassCount, false);
	                Reportsheet.getRow(11).getCell(2).setCellValue(PropertiesAndConstants.TestPassCount);
	                LogFunctions.LogEntry("Tests FAILED : " + PropertiesAndConstants.TestFailCount, false);
	                Reportsheet.getRow(12).getCell(2).setCellValue(PropertiesAndConstants.TestFailCount);
	                LogFunctions.LogEntry("JavaScript Errors : " + PropertiesAndConstants.JavaScriptErrorCount, false);
	                LogFunctions.LogEntry("==================================", false);

	                // Global Test Run Statistics
	                
	               
	                if (PropertiesAndConstants.IsTestExecutionPassed)
	                {
	                	
	                    Reportsheet.getRow(0).getCell(4).setCellValue("Pass");
	 	            	
	                    LogFunctions.LogEntry("***TEST RUN PASSED***", false);
	                }
	                else
	                {
	                	Reportsheet.getRow(0).getCell(4).setCellValue("Fail");
	 	            
	                    LogFunctions.LogEntry("***TEST RUN FAILED***", false);
	                }
	            	reportFile.close();
	            	FileOutputStream outFile =new FileOutputStream(updateResultFile);
	            	ReportWorkbook.write(outFile);
                    outFile.close();

	                SuiteWorkbook.close();
	                
	               // UtilityFunctions.SendResultMail();
			}
		}
		catch (Exception srcriptRunExc) 
		{
			LogFunctions.LogEntry("Error", false);
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Processing Suit File ,Calling a Function to Add Common Actions In Temp Script Files and Executing them
	/// </summary>
	public static void MTProcessSuiteFile() throws IOException
	{
		String startTestTime;

		//bool _processed = false; // ***** Can be Used for Parallel test run
		PropertiesAndConstants.IsTestExecutionPassed = true;
		
		String suiteFile = PropertiesAndConstants.TempTestSuitePath;
		
		PropertiesAndConstants.TestCount = 0;
		PropertiesAndConstants.TestPassCount = 0;
		PropertiesAndConstants.TestFailCount = 0;
		PropertiesAndConstants.JavaScriptErrorCount = 0;

		try
		{  
			File file = new File(suiteFile);
		    
			if(file.exists())
			{
			  		    
				Workbook SuiteWorkbook=Workbook.getWorkbook(file);    	   	    
				Sheet  SuiteSheet =  SuiteWorkbook.getSheet(0);
				String updateResult = PropertiesAndConstants.ExcelReportFile;
			
                File updateResultFile=new File(updateResult);
				FileInputStream reportFile = new FileInputStream(updateResultFile);        		 
				HSSFWorkbook ReportWorkbook = new HSSFWorkbook(reportFile);
				HSSFSheet Reportsheet = ReportWorkbook.getSheetAt(0);
				HSSFCell cell;
				
				int reportOffset = 23;
				int sno = 0;
				int rowcount_suite =  SuiteSheet.getRows();
				
				for (int row_suite = 1; row_suite <= rowcount_suite; row_suite++)
				{
					
					PropertiesAndConstants.FailStepsPerScriptCounter = 0;
					
					try
					{
						
						String testname= SuiteSheet.getCell(0, row_suite).getContents();
					
						if (testname.equals(null)||testname.equals(""))
						{
							// LogFunctions.LogEntry(String.Format("Processing Suite file was completed in row: " + row_suite, false));
							LogFunctions.LogEntry(String.format("Processing Suite file was completed in row:"+row_suite), false);
							break;
						}
					
				
						File testfile=new File(PropertiesAndConstants.TempDirectoryPath + testname);
						
						PropertiesAndConstants.TestScriptName = testname.replace(".xls", "");	
						// Test was crash in this case  (for next iteration) 
						String run_value =  SuiteSheet.getCell(1,row_suite).getContents();
						
						//String run_Value = (String)((Excel.Range)suitesheet.Cells[row_suite, 2]).Text;
                     
						if ((!UtilityFunctions.IsNullOrEmpty(run_value))&& run_value.trim().toUpperCase().equals( "YES"))
						{
							LogFunctions.LogEntry("Start processing script file : " + PropertiesAndConstants.TestScriptName, false);
							PropertiesAndConstants.scriptExecutionResult = "Pass";
							String testScript = PropertiesAndConstants.TempTestScriptPath+ PropertiesAndConstants.TestScriptName + ".xls";
					      
							LogFunctions.LogEntry(String.format("Processing to add Common Steps..."), false);
							ScriptFunctions.AddCommonActionsInTempScript(testScript);
							LogFunctions.LogEntry(String.format("Process of add Common Steps - Completed"),false);
				
							startTestTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
							Date date=new Date();
							long Starttime=System.currentTimeMillis();
							long Starttimesecond=TimeUnit.SECONDS.toSeconds(Starttime);

						    String TemptestScript=PropertiesAndConstants.TempDirectoryPath+PropertiesAndConstants.TestScriptName + ".xls";
						   	Workbook testScriptPath=ExcelHandler.ExcelOpenWorkbook(TemptestScript);
//						    LogFunctions.LogEntry(String.format("Script path : "+TemptestScript),false);
						    LogFunctions.LogEntry("--------------------Execution Results-------------------", false);
							// **** Main Script Execution Action
						
							KeyActionFunctions keyAction = new KeyActionFunctions();
						    ScriptFunctions.validateTestDataExist(TemptestScript);
						
							keyAction.ProcessScriptTemplate(testScriptPath); // ***** Try to Add // Wait Exception
							if (testScriptPath != null)
							{
								testScriptPath.close();


							}
							// ****
							
							String finishTestTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
							long Finishtime=System.currentTimeMillis();
							//String executionTime = (finishTestTime - startTestTime).ToString("mm\\:ss\\.f");
                           
							Long executionTimeinSec=(Finishtime-Starttime);
							
							double seconds= ((executionTimeinSec/1000.0)%60.0);

							seconds =Double.parseDouble(new DecimalFormat("##.#").format(seconds));

							int minutes=(int) (((executionTimeinSec-seconds)/1000)/60);
							
							TimeUnit.MILLISECONDS.toSeconds(executionTimeinSec);
							//String executionTime=executionTimeinSec.toString();
							String executionTime=String.format("%02d", minutes)+":"+seconds; 
	                     
							sno = sno + 1;
							StringBuilder snum = new StringBuilder();
							snum.append("");
							snum.append(sno);
							String Snum = snum.toString();
							cell = Reportsheet.getRow(reportOffset).getCell(0);
							cell.setCellValue(Snum);

							cell = Reportsheet.getRow(reportOffset).getCell(1);
							cell.setCellValue(PropertiesAndConstants.TestScriptName);

							cell = Reportsheet.getRow(reportOffset).getCell(4);
							cell.setCellValue(executionTime);

							SeleniumHandler.CloseSeleniumDriver(); // Close Driver
							PropertiesAndConstants.IsFlashDisabled = false; // Reset Flash parameter
							SeleniumHandler.driver = null;							
                            
							if (PropertiesAndConstants.scriptExecutionResult == "Fail")
							{
								PropertiesAndConstants.TestCount++;
								PropertiesAndConstants.TestFailCount++;
								//  Suitesheet.Cells[row_suite, 3] = String.Format("//{0:dd-MM-yyyy_HH-mm-ss}// -- FAIL", DateTime.Now);

								//cell=SuiteSheet.getRow(row_suite).getCell(3);
								String DateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
								//cell.setCellValue(DateFormat+" -- FAIL");


								LogFunctions.LogEntry(String.format("***** Test script result :"+PropertiesAndConstants.scriptExecutionResult+"*****", PropertiesAndConstants.scriptExecutionResult),false);
								LogFunctions.LogEntry("*** Test script Execution Time: " + executionTime + " ***", false);
								LogFunctions.LogEntry("*************************************************", false);
								 cell=Reportsheet.getRow(reportOffset).getCell(3);
								 cell.setCellValue(PropertiesAndConstants.scriptExecutionResult);

								//  reportsheet.Cells[reportOffset, 4] = PropertiesAndConstants.scriptExecutionResult;
							}
							else
								if (PropertiesAndConstants.scriptExecutionResult == "Pass")
								{
									PropertiesAndConstants.TestCount++;
									PropertiesAndConstants.TestPassCount++;

									//cell=SuiteSheet.getRow(row_suite).getCell(3);
									String DateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
									//cell.setCellValue(DateFormat);
									//    suitesheet.Cells[row_suite, 3] = String.Format("\\{0:dd-MM-yyyy_HH-mm-ss\\} -- PASS", DateTime.Now);
									LogFunctions.LogEntry(String.format("***** Test script result :"+PropertiesAndConstants.scriptExecutionResult+" *****" ),false);
									LogFunctions.LogEntry("*** Test script Execution Time: " + executionTime + " ***", false);
									LogFunctions.LogEntry("*************************************************", false);
									cell=Reportsheet.getRow(reportOffset).getCell(3);
									cell.setCellValue(PropertiesAndConstants.scriptExecutionResult);

									// reportsheet.Cells[reportOffset, 4] = PropertiesAndConstants.scriptExecutionResult;
								}
								else
								{
									PropertiesAndConstants.scriptExecutionResult = "NOT RUN";

									String DateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
									
									LogFunctions.LogEntry(String.format("Test script name :"+ PropertiesAndConstants.TestScriptName),false);
									LogFunctions.LogEntry(String.format("***** Test script result :"+PropertiesAndConstants.scriptExecutionResult+" *****" ),false);

									cell=Reportsheet.getRow(reportOffset).getCell(3);
									cell.setCellValue(PropertiesAndConstants.scriptExecutionResult);
								}
							reportOffset = reportOffset + 1;
						}
					}
					catch (Exception srcriptRunExc)
					{
						LogFunctions.LogEntry(String.format("Error In the processing of Script file... "+srcriptRunExc.getMessage()), false);
						LogFunctions.LogEntry(String.format("Details:"+ srcriptRunExc), false);
						LogFunctions.LogEntry(String.format("Details:"+ srcriptRunExc.getMessage()), false);
						srcriptRunExc.printStackTrace();
						PropertiesAndConstants.IsTestExecutionPassed = false;
						reportOffset = reportOffset + 1;
						SeleniumHandler.CloseSeleniumDriver();
						Thread.sleep(500);
						continue;
					}
				}
				 	LogFunctions.LogEntry("==================================", false);
	                LogFunctions.LogEntry("Total Statistics: ", false);
	                LogFunctions.LogEntry("Total Tests Run : " + PropertiesAndConstants.TestCount, false);
	                Reportsheet.getRow(10).getCell(2).setCellValue(PropertiesAndConstants.TestCount);
	                LogFunctions.LogEntry("Tests PASSED : " + PropertiesAndConstants.TestPassCount, false);
	                Reportsheet.getRow(11).getCell(2).setCellValue(PropertiesAndConstants.TestPassCount);
	                LogFunctions.LogEntry("Tests FAILED : " + PropertiesAndConstants.TestFailCount, false);
	                Reportsheet.getRow(12).getCell(2).setCellValue(PropertiesAndConstants.TestFailCount);
	                LogFunctions.LogEntry("JavaScript Errors : " + PropertiesAndConstants.JavaScriptErrorCount, false);
	                LogFunctions.LogEntry("==================================", false);

	                // Global Test Run Statistics
	                
	               
	                if (PropertiesAndConstants.IsTestExecutionPassed)
	                {
	                	
	                    Reportsheet.getRow(0).getCell(4).setCellValue("Pass");
	 	            	
	                    LogFunctions.LogEntry("***TEST RUN PASSED***", false);
	                }
	                else
	                {
	                	Reportsheet.getRow(0).getCell(4).setCellValue("Fail");
	 	            
	                    LogFunctions.LogEntry("***TEST RUN FAILED***", false);
	                }
	            	reportFile.close();
	            	FileOutputStream outFile =new FileOutputStream(updateResultFile);
	            	ReportWorkbook.write(outFile);
                    outFile.close();
	
	                SuiteWorkbook.close();
	                
	               // UtilityFunctions.SendResultMail();
	  		}
		}
		catch (Exception srcriptRunExc) 
		{
			LogFunctions.LogEntry("Error", false);
		}
	}


}
