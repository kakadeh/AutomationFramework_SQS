package businessLogics;


import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import org.w3c.dom.Document;

import commonUtils.PropertiesAndConstants;
import handlers.ExcelHandler;
import handlers.XMLHandler;

public class ConfigFunctions {
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Opening XML File and Calling a Function to Store All the Objects of Environment File In a HashMap
	/// </summary>
	
	public static void populateEnvDictionary(String envFilePath) 
    {
		try
		{
			Document doc = XMLHandler.OpenXML(envFilePath);
			PropertiesAndConstants.envDictionary = XMLHandler.populateXMLDictionary(doc);
			//XMLHandler.CloseXML(envFilePath);
			
		}
		catch (Exception e) {
			System.out.println("Failed to Populate Environment Details"+e.getMessage());
		}
     }
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Getting the value of Objects in Environment File which are stored in HashMap
	/// </summary>
	
	public static String getEnvKeyValue(String keyName)
	{
		try
		{
			String value;
			value = PropertiesAndConstants.envDictionary.get(keyName);
			return value;
		}
		catch (Exception e) {
			System.out.println("Failed to Fetch the Environemnt Value for Key:"+keyName+e.getMessage());
			return "";
		}	
	}
	
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Setting Up Default Values of All The Objects in Environment File 
	/// </summary>
	 
	public static void SetupEnvValues() throws Exception
	 {
		 PropertiesAndConstants.TestType =getEnvKeyValue("TESTTYPE"); 
		 PropertiesAndConstants.TemplatePath=getEnvKeyValue("TEMPLATEPATH");
		 PropertiesAndConstants.Application = getEnvKeyValue("APP").toUpperCase();
         
		 String TempTestSuitePath=getEnvKeyValue("SUITE");
		 /*
		  * PropertiesAndConstants.TempSuitePaths = Arrays.asList(TempTestSuitePath.split(","));
			PropertiesAndConstants.TempSuitePathsCount = PropertiesAndConstants.TempSuitePaths.size();
			PropertiesAndConstants.TempSuitPathsflag++;
			System.out.println(PropertiesAndConstants.TempSuitPathsflag);
			TempTestSuitePath = PropertiesAndConstants.TempSuitePaths.get(PropertiesAndConstants.TempSuitPathsflag);
			PropertiesAndConstants.TestSuitePath=PropertiesAndConstants.CurrentDirectory+TempTestSuitePath+PropertiesAndConstants.Application+"\\"+PropertiesAndConstants.TestType+"\\";
			System.out.println(PropertiesAndConstants.TempTestSuitePath);*
		  * 
		  */
		 	 
		 	if(PropertiesAndConstants.TempSuitPathsflag > -1){
			 
			 PropertiesAndConstants.TestType = PropertiesAndConstants.TempSuitePaths.get(PropertiesAndConstants.TempSuitPathsflag);
					 
			 } 
		 	else if(PropertiesAndConstants.TestType.contains(",")){
			 
			 PropertiesAndConstants.TempSuitePaths = Arrays.asList(PropertiesAndConstants.TestType.split(","));
			 PropertiesAndConstants.TempSuitePathsCount = PropertiesAndConstants.TempSuitePaths.size();		 
			 PropertiesAndConstants.TempSuitPathsflag++;
			 PropertiesAndConstants.TestType = PropertiesAndConstants.TempSuitePaths.get(PropertiesAndConstants.TempSuitPathsflag);
			 }		 
		 
		 
		 PropertiesAndConstants.TempTestSuitePath =PropertiesAndConstants.CurrentDirectory+TempTestSuitePath+PropertiesAndConstants.Application+"_"+PropertiesAndConstants.TestType+"_Suite.xls";
		 PropertiesAndConstants.TestSuitePath=PropertiesAndConstants.CurrentDirectory+TempTestSuitePath+PropertiesAndConstants.Application+"\\"+PropertiesAndConstants.TestType+"\\";
		 
		 String dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss-a").format(Calendar.getInstance().getTime());
		 
		 String currentRunReportFolder = PropertiesAndConstants.CurrentDirectory + getEnvKeyValue("EXEREPPATH") + dateFormat + "_" + PropertiesAndConstants.TestType;
		 PropertiesAndConstants.CurrentRunReportFolder =  currentRunReportFolder;
         PropertiesAndConstants.TempDirectoryPath =  currentRunReportFolder + "\\Temp\\";
     	 PropertiesAndConstants.ScreenshotDirectoryPath=currentRunReportFolder+"\\Screenshots\\";
         
         String logFileName = PropertiesAndConstants.CurrentDirectory + getEnvKeyValue("LOGPATH") + "Log_" + dateFormat + ".log";
         PropertiesAndConstants.LogFile=logFileName;
          
         String tempTestLogPath = PropertiesAndConstants.TempDirectoryPath + "TestExecutionLog.txt";
         PropertiesAndConstants.TempTestLogPath =tempTestLogPath;
          
         
          
          String url = getEnvKeyValue(PropertiesAndConstants.Application + "_" + getEnvKeyValue("ENVIRONMENT"));
          PropertiesAndConstants.Environment = getEnvKeyValue("ENVIRONMENT");
          
          PropertiesAndConstants.Url=url;
        
          PropertiesAndConstants.Browser = getEnvKeyValue("BROWSER");
          
          String repository = PropertiesAndConstants.CurrentDirectory+getEnvKeyValue("OBJREPOSITORY")+PropertiesAndConstants.Application+"_OR.xls";
          PropertiesAndConstants.Repository=repository;
          
          String suiteFile = PropertiesAndConstants.CurrentDirectory + getEnvKeyValue("SUITE") + PropertiesAndConstants.Application + "_" + PropertiesAndConstants.TestType + "_Suite.xls";
          PropertiesAndConstants.SuiteFile = suiteFile;
          String testscriptsdirectory = PropertiesAndConstants.CurrentDirectory + getEnvKeyValue("SCRIPTS") + PropertiesAndConstants.Application + "\\" + PropertiesAndConstants.TestType+"\\";
       
          PropertiesAndConstants.TempTestScriptPath=testscriptsdirectory;
                  
          PropertiesAndConstants.ReusableScriptPath=PropertiesAndConstants.CurrentDirectory+getEnvKeyValue("SCRIPTS");
          
          //Store Email details
          PropertiesAndConstants.EmailToList = getEnvKeyValue("EMAILTO");
          PropertiesAndConstants.EmailFrom = getEnvKeyValue("EMAILFROM");
          PropertiesAndConstants.EmailHost = getEnvKeyValue("EMAILHOST");
          PropertiesAndConstants.EmailPort = getEnvKeyValue("EMAILPORT");
          
          PropertiesAndConstants.EmailUser = getEnvKeyValue("EMAILUSER");
          PropertiesAndConstants.EmailPassword = getEnvKeyValue("EMAILPASSWORD");
          
          // ***** Create a Temporary Directory
       
          
          LogFunctions.CreateReportFolder();
          
          LogFunctions.SetLogFileHeader();
          
          
          // Write to Excel Report
          String updateResult = PropertiesAndConstants.ExcelReportFile;
         
          ExcelHandler.WriteToSheet(updateResult, 0, 2, 2, PropertiesAndConstants.Application);
          ExcelHandler.WriteLinkToSheet(updateResult, 0, 3, 2, url, url);
          ExcelHandler.WriteToSheet(updateResult, 0, 4, 2, PropertiesAndConstants.TestType);
          ExcelHandler.WriteToSheet(updateResult, 0, 6, 3, PropertiesAndConstants.date);
          ExcelHandler.WriteToSheet(updateResult, 0, 15, 2, PropertiesAndConstants.Browser);
          ExcelHandler.WriteLinkToSheet(updateResult, 0, 16, 2, "Test Log", logFileName);
          ExcelHandler.WriteLinkToSheet(updateResult, 0, 17, 2, "Test Suite", suiteFile);
          
          System.out.println("");
          
		  /*
          File updateResultFile=new File(updateResult);
		  FileInputStream reportFile = new FileInputStream(updateResultFile);  
          HSSFWorkbook ReportWorkbook = new HSSFWorkbook(reportFile);
		  HSSFSheet Reportsheet = ReportWorkbook.getSheetAt(0);
		  HSSFCell cell = null;
		  
		  HSSFHyperlink Url_Link=new HSSFHyperlink(HSSFHyperlink.LINK_URL);
		  Url_Link.setAddress(url);
		  HSSFHyperlink Log_Link=new HSSFHyperlink(HSSFHyperlink.LINK_FILE);
		  Log_Link.setAddress(logFileName);
		  HSSFHyperlink Suite_Link=new HSSFHyperlink(HSSFHyperlink.LINK_FILE);
		  Suite_Link.setAddress(suiteFile);
		  
		  Reportsheet.getRow(2).getCell(2).setCellValue(Application);
		  Reportsheet.getRow(3).getCell(2).setCellValue(url);
		  Reportsheet.getRow(3).getCell(2).setHyperlink(Url_Link);
		  Reportsheet.getRow(4).getCell(2).setCellValue(TestType);  
		  Reportsheet.getRow(6).getCell(3).setCellValue(dateFormat);
		  
		  Reportsheet.getRow(15).getCell(3).setCellValue(Browser);
		  Reportsheet.getRow(16).getCell(2).setCellValue("Test Log");
		  Reportsheet.getRow(16).getCell(2).setHyperlink(Log_Link);
		  Reportsheet.getRow(17).getCell(2).setCellValue("Test Suite");
		  Reportsheet.getRow(17).getCell(2).setHyperlink(Suite_Link);
		  reportFile.close();
          FileOutputStream outFile =new FileOutputStream(updateResultFile);
		  ReportWorkbook.write(outFile);
          outFile.close();
          */
          
          
          
	 }
	
	/// <summary>
		/// Author            :  Harshal Kakade
		/// Description       : Setting Up Default Values of All The Objects in Environment File 
		/// </summary>
		 public static void MTSetupEnvValues() throws Exception
	     {
		 PropertiesAndConstants.TestType =getEnvKeyValue("TESTTYPE"); 
		 PropertiesAndConstants.TemplatePath=getEnvKeyValue("TEMPLATEPATH");
		 PropertiesAndConstants.Application = getEnvKeyValue("APP").toUpperCase();
	     
		 String TempTestSuitePath=getEnvKeyValue("SUITE");
		 System.out.println(TempTestSuitePath);
		 
		 if(PropertiesAndConstants.TempSuitPathsflag > -1){
			 
			 PropertiesAndConstants.TestType = PropertiesAndConstants.TempSuitePaths.get(PropertiesAndConstants.TempSuitPathsflag);
					 
			 } 
		 	else if(PropertiesAndConstants.TestType.contains(",")){
			 
			 PropertiesAndConstants.TempSuitePaths = Arrays.asList(PropertiesAndConstants.TestType.split(","));
			 PropertiesAndConstants.TempSuitePathsCount = PropertiesAndConstants.TempSuitePaths.size();		 
			 PropertiesAndConstants.TempSuitPathsflag++;
			 PropertiesAndConstants.TestType = PropertiesAndConstants.TempSuitePaths.get(PropertiesAndConstants.TempSuitPathsflag);
			 }		
		 
		 PropertiesAndConstants.TempTestSuitePath =PropertiesAndConstants.CurrentDirectory+TempTestSuitePath+PropertiesAndConstants.Application+"_"+PropertiesAndConstants.TestType+"_Suite.xls";
		 PropertiesAndConstants.TestSuitePath=PropertiesAndConstants.CurrentDirectory+TempTestSuitePath+PropertiesAndConstants.Application+"\\"+PropertiesAndConstants.TestType+"\\";
		
		 String dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss-a").format(Calendar.getInstance().getTime());
		 
		 String currentRunReportFolder = PropertiesAndConstants.CurrentDirectory + getEnvKeyValue("EXEREPPATH") + dateFormat + "_" + PropertiesAndConstants.TestType;
		 PropertiesAndConstants.CurrentRunReportFolder =  currentRunReportFolder;
	     PropertiesAndConstants.TempDirectoryPath =  currentRunReportFolder + "\\Temp\\";
	 	 PropertiesAndConstants.ScreenshotDirectoryPath=currentRunReportFolder+"\\Screenshots\\";
	    
	     String logFileName = PropertiesAndConstants.CurrentDirectory + getEnvKeyValue("LOGPATH") + "Log_" + dateFormat + ".log";
	     PropertiesAndConstants.LogFile=logFileName;
	      
	     String tempTestLogPath = PropertiesAndConstants.TempDirectoryPath + "TestExecutionLog.txt";
	     PropertiesAndConstants.TempTestLogPath =tempTestLogPath;
	      
	     
	      
	      String url = getEnvKeyValue(PropertiesAndConstants.Application + "_" + getEnvKeyValue("ENVIRONMENT"));
	      PropertiesAndConstants.Environment = getEnvKeyValue("ENVIRONMENT");
	      
	      PropertiesAndConstants.Url=url;
	    
	      PropertiesAndConstants.Browser = getEnvKeyValue("BROWSER");
	      
	      String repository = PropertiesAndConstants.CurrentDirectory+getEnvKeyValue("OBJREPOSITORY")+PropertiesAndConstants.Application+"_MT_OR.xls";
	      PropertiesAndConstants.Repository=repository;
	      
	      String suiteFile = PropertiesAndConstants.CurrentDirectory + getEnvKeyValue("SUITE") + PropertiesAndConstants.Application + "_" + PropertiesAndConstants.TestType + "_Suite.xls";
	      PropertiesAndConstants.SuiteFile = suiteFile;
	      String testscriptsdirectory = PropertiesAndConstants.CurrentDirectory + getEnvKeyValue("SCRIPTS") + PropertiesAndConstants.Application + "\\" + PropertiesAndConstants.TestType+"\\";
	   
	      PropertiesAndConstants.TempTestScriptPath=testscriptsdirectory;
	    
	      PropertiesAndConstants.ReusableScriptPath=PropertiesAndConstants.CurrentDirectory+getEnvKeyValue("SCRIPTS");
	      
	      //Store Email details
	      PropertiesAndConstants.EmailToList = getEnvKeyValue("EMAILTO");
	      PropertiesAndConstants.EmailFrom = getEnvKeyValue("EMAILFROM");
	      PropertiesAndConstants.EmailHost = getEnvKeyValue("EMAILHOST");
	      PropertiesAndConstants.EmailPort = getEnvKeyValue("EMAILPORT");
	      
	      PropertiesAndConstants.EmailUser = getEnvKeyValue("EMAILUSER");
	      PropertiesAndConstants.EmailPassword = getEnvKeyValue("EMAILPASSWORD");
	      
	      // ***** Create a Temporary Directory
	   
	      
	      LogFunctions.CreateReportFolder();
	      
	      LogFunctions.MTSetLogFileHeader();
	      
	      try{
	      
	    System.out.println("ConfigFunctions.MTSetupEnvValues-Doneaadhi");
	      // Write to Excel Report
	      String updateResult = PropertiesAndConstants.ExcelReportFile;
	     
	      ExcelHandler.WriteToSheet(updateResult, 0, 2, 2, PropertiesAndConstants.Application);
	      ExcelHandler.WriteLinkToSheet(updateResult, 0, 3, 2, url, url);
	      ExcelHandler.WriteToSheet(updateResult, 0, 4, 2, PropertiesAndConstants.TestType);
	      ExcelHandler.WriteToSheet(updateResult, 0, 6, 3, PropertiesAndConstants.date);
	      ExcelHandler.WriteToSheet(updateResult, 0, 15, 2, PropertiesAndConstants.Browser);
	      ExcelHandler.WriteLinkToSheet(updateResult, 0, 16, 2, "Test Log", logFileName);
	      ExcelHandler.WriteLinkToSheet(updateResult, 0, 17, 2, "Test Suite", suiteFile);
	      
	      System.out.println("ConfigFunctions.MTSetupEnvValues-Done");
	      }
	      catch(Exception e)
	      {
	    	  System.out.println(e.getLocalizedMessage());
	    	  System.out.println(e.getMessage());
	      }
	}

}
