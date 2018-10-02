package businessLogics;

import handlers.TextHandler;
import businessLogics.ConfigFunctions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;



import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.sun.xml.internal.fastinfoset.sax.Properties;

import commonUtils.PropertiesAndConstants;
import commonUtils.UtilityFunctions;

@SuppressWarnings("unused")
public class LogFunctions {

	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Adding Log Entry Description in Log File
	/// </summary>
	
	public static void LogEntry(String logText, boolean isResult) throws IOException
	{
		if (isResult) PropertiesAndConstants.ResultString = logText;
		TextHandler.UpdateFile(PropertiesAndConstants.LogFile, logText);
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : 
	/// </summary>
	
	public static void SetLogFileHeader() throws IOException
	{

        LogEntry("Environment values............",false);
        LogEntry(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",false);
        LogEntry("Application on which executed : " + PropertiesAndConstants.Application,false);
        LogEntry("Type of testing : " + PropertiesAndConstants.TestType,false);
        LogEntry("Test environment on which application executed : ",false);
        LogEntry("URL : " + PropertiesAndConstants.Url,false);

        LogEntry("Driver Settings...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",false);
        LogEntry("STARTFROMHOMEPAGE <" + PropertiesAndConstants.Url + "> : " +ConfigFunctions.getEnvKeyValue("STARTFROMHOMEPAGE"),false);
        LogEntry("CLEARCOOKIEFOREACHTEST: " + ConfigFunctions.getEnvKeyValue("CLEARCOOKIEFOREACHTEST"),false);
        LogEntry("JAVASCRIPTTIMEOUT: " + ConfigFunctions.getEnvKeyValue("JAVASCRIPTTIMEOUT"),false);
        LogEntry("VERIFYJAVASCRIPTERRORS: " + ConfigFunctions.getEnvKeyValue("VERIFYJAVASCRIPTERRORS"),false);
        LogEntry("ALERTAUTOACCEPT: " + ConfigFunctions.getEnvKeyValue("ALERTAUTOACCEPT"),false);

      //  UtilityFunctions.LogEntry("Execution Project directory  : " + sAppPath);
        LogEntry("Environment file location : " + PropertiesAndConstants.EvnFilePath,false);
        LogEntry("Executed on browser : " + PropertiesAndConstants.Browser,false);
        LogEntry("Repository location : " + PropertiesAndConstants.Repository,false);
        LogEntry("Suite file name and location : " + PropertiesAndConstants.SuiteFile,false);
        //UtilityFunctions.LogEntry("Selenium log file name in Selenium log folder : " + PropertiesAndConstants.seleniumLogFile);

        LogEntry("Test execution report file : " + PropertiesAndConstants.ExcelReportFile,false);
        LogEntry("Test execution log file : " + PropertiesAndConstants.LogFile,false);
        LogEntry(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",false);

        LogEntry("TEMPLORARY CURRENT EXECUTION PARAMETERS:",false);
        LogEntry("Templorary folder path : " + PropertiesAndConstants.TempDirectoryPath,false);
        LogEntry("Templorary TestSuite path : " + PropertiesAndConstants.TempTestSuitePath,false);
        LogEntry("Templorary TestLog path : " + PropertiesAndConstants.TempTestLogPath,false);
        LogEntry("Screen shot location for this execution : " + PropertiesAndConstants.TempDirectoryPath,false);
        LogEntry(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",false);
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : 
	/// </summary>
	
	public static void CreateReportFolder() throws IOException
	{
		System.out.println(" in CreateReportFolder");
		try 
		{
		File ReportFolder=new File(PropertiesAndConstants.CurrentRunReportFolder);
		String dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss-a").format(Calendar.getInstance().getTime());
        File TempFolder=new File(PropertiesAndConstants.TempDirectoryPath);
        File ScreenshotFolder=new File(PropertiesAndConstants.ScreenshotDirectoryPath);
        if(ReportFolder.mkdir())
        {
      	  ReportFolder.mkdir();
      	  TempFolder.mkdir();
      	  ScreenshotFolder.mkdir();
        }
        else { 
        		System.out.println("Cannot Create Temp Directories"); 
        	}
        File ReportFile=new File(PropertiesAndConstants.CurrentRunReportFolder + "\\" + PropertiesAndConstants.Application + "_TA_Report_" +PropertiesAndConstants.Browser + "_" + dateFormat + ".xls");
        
        File ReportTemplateFile=new File(PropertiesAndConstants.CurrentDirectory +PropertiesAndConstants.TemplatePath+"\\ReportTemplate.xls");
        if (!ReportFile.exists()||ReportTemplateFile.exists())
        {
      	    String exeFileName = PropertiesAndConstants.CurrentRunReportFolder + "\\" + PropertiesAndConstants.Application + "_TA_Report_" + PropertiesAndConstants.Browser + "_" + dateFormat +".xls";
            File exeFile=new File(exeFileName);
      	    FileUtils.copyFile(ReportTemplateFile,exeFile);
            PropertiesAndConstants.ExcelReportFile = exeFileName;
        }
        else 
        { 
        	System.out.println("Cannot Create a Report File ");
        	
        }
        File TestScriptsPath=new File(PropertiesAndConstants.TempTestScriptPath);
        if(TestScriptsPath.exists())
        {
        	System.out.println("Copy script files in folder");
           //CopyScriptFilesInTempFolder();
        }
        else
        {
        	System.out.println("Cannot Copy scripts files in Temp folder");
        }
	}
	   catch(Exception e)
	   {
		  LogEntry("Failed to Create the report folder:" + e.getMessage(),false);
	   }
		
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : 
	/// </summary>
	
	public static void CopyScriptFilesInTempFolder() throws IOException
	 {
	      try
	           {
	          
	     		  File scriptssource = new File(PropertiesAndConstants.TempTestScriptPath);
	     		  File[] files = scriptssource.listFiles();
	             	// for (File file : files)
         		 
	          	for(int i=0;i<files.length;i++)
	         	 {
	         		           	          		       		     		
	         		 File source=new File(PropertiesAndConstants.TempTestScriptPath + files[i].getName());
	         		 File destfile = new File(PropertiesAndConstants.TempDirectoryPath + "\\" +files[i].getName());
	         		 FileUtils.copyFile(source, destfile);
	         	 }
	          }
	          catch (IOException e)
	          {
	         		 	LogEntry("Driver Close Exception " + e.getMessage(), false); 
	 				
	          }
	 }
/*	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : 
	/// </summary>
	public static void LogEntry(String logText, boolean isResult,  boolean isPassString)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now = new Date(); 
		if (isResult) PropertiesAndConstants.ResultString = logText;
		if (isPassString)
		{
			//Console.ForegroundColor = ConsoleColor.Green;
			System.console().writer().println(logText);
			//Console.ResetColor();
		}
		else 
		{
			//Console.ForegroundColor = ConsoleColor.Red;
			System.console().writer().println(logText);
			//Console.ResetColor();
		}
		PrintWriter streamWriter = null;
		try
		{
			streamWriter = streamWriter.append(PropertiesAndConstants.LogFile);
			streamWriter.write(dateFormat.format(now)+ " => " + logText);
			streamWriter.close();
		}
		catch (Exception LogFileError)
		{
			JOptionPane.showInputDialog(String.format("Log file Exception occured...."+LogFileError),false);
			//MessageBox.Show(String.format("Log file Exception occured.... {0}", LogFileError),false);
			streamWriter.close();
			//System.Environment.Exit(0);
		}
	}
*/
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : 
	/// </summary>
	
	public static String TakeScreenshot(String screenName) throws IOException
	{
		String sspathname;
		String screenShotPath = PropertiesAndConstants.ScreenshotDirectoryPath;
	    //String screenShotfullPath = Path.GetFullPath(screenShotPath);
		String screenShotfullPath = screenShotPath;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String dateparameter = dateFormat.format(date);
		dateparameter = dateparameter.replace(':', '-').replace('/', '-').replace(' ', '_');
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss-a").format(Calendar.getInstance().getTime());
		if (!UtilityFunctions.IsNullOrWhiteSpace(ConfigFunctions.getEnvKeyValue("SCREENSHOTNAME")))
		{
			sspathname =PropertiesAndConstants.ScreenshotDirectoryPath+"\\"+PropertiesAndConstants.TestScriptName+timeStamp+".png";
		}
		else
		{
			sspathname =PropertiesAndConstants.ScreenshotDirectoryPath+"\\"+PropertiesAndConstants.TestScriptName+timeStamp+".png";
		}



		File scrFile = ((TakesScreenshot)PropertiesAndConstants.Selenium).getScreenshotAs(OutputType.FILE);
		// Now you can do whatever you need to do with it, for example copsomewhere
		
		FileUtils.copyFile(scrFile, new File(PropertiesAndConstants.ScreenshotDirectoryPath+"\\"+PropertiesAndConstants.TestScriptName+timeStamp+".png"));
		//ScreenCapture ss = Properties.getSelenium().manage(). ((ITakesScreenshot)GlobalClass.GetDriver).GetScreenshot();
		//String screenshot = ss.AsBase64EncodedString;

		//ss.SaveAsFile(sspathname, ImageFormat.Png);
		return sspathname;
	}
	
	/// <summary>
			/// Author            :  Harshal Kakade
			/// Description       : 
			/// </summary>
			
			public static void MTSetLogFileHeader() throws IOException
			{
				System.out.println("in MTSetLogFileHeader" );
		        LogEntry("Environment Values............",false);
		        LogEntry("****************************************************",false);
		        LogEntry("Brand Name: " + PropertiesAndConstants.Application,false);
		        LogEntry("Type of testing : " + PropertiesAndConstants.TestType,false);
//		        String url=ConfigFunctions.getEnvKeyValue("URL");
//		        LogEntry("Test environment on which application executed : "+url,false);
		       
//		        LogEntry("URL : " + PropertiesAndConstants.Url,false);

//		        LogEntry("Driver Settings...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",false);
//		        LogEntry("STARTFROMHOMEPAGE <" + PropertiesAndConstants.Url + "> : " +ConfigFunctions.getEnvKeyValue("STARTFROMHOMEPAGE"),false);
//		        LogEntry("CLEARCOOKIEFOREACHTEST: " + ConfigFunctions.getEnvKeyValue("CLEARCOOKIEFOREACHTEST"),false);
//		        LogEntry("JAVASCRIPTTIMEOUT: " + ConfigFunctions.getEnvKeyValue("JAVASCRIPTTIMEOUT"),false);
//		        LogEntry("VERIFYJAVASCRIPTERRORS: " + ConfigFunctions.getEnvKeyValue("VERIFYJAVASCRIPTERRORS"),false);
//		        LogEntry("ALERTAUTOACCEPT: " + ConfigFunctions.getEnvKeyValue("ALERTAUTOACCEPT"),false);

		      //  UtilityFunctions.LogEntry("Execution Project directory  : " + sAppPath);
		        LogEntry("Environment File :- " + PropertiesAndConstants.EvnFilePath,false);
//		    	String DeviceType=ConfigFunctions.getEnvKeyValue("DEVICETYPE");
//		    	int device = Integer.parseInt(DeviceType);
//		    	if(device==1)
//		    	{
//		    	LogEntry("Executed on : ANDROID DEVICE",false);
//		    	}
//		    	else if(device==2)
//		    	{
//		    		LogEntry("Executed on : iOS DEVICE",false);
//		    	}
//		    	else
//		    	{
//		    		LogEntry("Something went wrong in choosing device for execution " ,false);
//		    	}
		        LogEntry("Object Repository :- " + PropertiesAndConstants.Repository,false);
		        LogEntry("Suite File :- " + PropertiesAndConstants.SuiteFile,false);
		        //UtilityFunctions.LogEntry("Selenium log file name in Selenium log folder : " + PropertiesAndConstants.seleniumLogFile);

		        LogEntry("Report File :- " + PropertiesAndConstants.ExcelReportFile,false);
		        LogEntry("Log File :- " + PropertiesAndConstants.LogFile,false);
//		        LogEntry(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>",false);

//		        LogEntry("TEMPLORARY CURRENT EXECUTION PARAMETERS:",false);
//		        LogEntry("Templorary folder path : " + PropertiesAndConstants.TempDirectoryPath,false);
//		        LogEntry("Templorary TestSuite path : " + PropertiesAndConstants.TempTestSuitePath,false);
//		        LogEntry("Templorary TestLog path : " + PropertiesAndConstants.TempTestLogPath,false);
//		        LogEntry("Screen shot location for this execution : " + PropertiesAndConstants.TempDirectoryPath,false);
		        LogEntry("**************************************************",false);
			}
		
	
}
