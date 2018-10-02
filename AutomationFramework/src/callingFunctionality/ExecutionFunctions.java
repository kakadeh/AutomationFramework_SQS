package callingFunctionality;

import businessLogics.ConfigFunctions;
import businessLogics.LogFunctions;
import businessLogics.ORFunctions;
import businessLogics.SuiteFunctions;
import commonUtils.PropertiesAndConstants;
import commonUtils.UtilityFunctions;

import handlers.ExcelHandler;

public class ExecutionFunctions {
	
	public static void main(String args[]) throws Exception
	{
		try
		{	
			ExcelHandler.CloseAllExcelFiles();
			UtilityFunctions.CloseFireFoxBrowsers();
			System.out.println("closed");
			UtilityFunctions.SetDefaultValues();
			System.out.println("DefaultValuesSet");
			ConfigFunctions.populateEnvDictionary(PropertiesAndConstants.EvnFilePath);
			System.out.println("EnvDirectoryPopulated");
			ConfigFunctions.SetupEnvValues();
			System.out.println("Environment Values Set");
			ORFunctions.PopulateObjRepositoryDictionary(PropertiesAndConstants.Repository);
			LogFunctions.LogEntry("Test Execution was started...", false);
			//SeleniumHandler.SetupTest();
			SuiteFunctions.ProcessSuiteFile();
			
		}
		catch (Exception startExecutionException) 
		{	
			LogFunctions.LogEntry("Error while starting the batch run.", false);
			LogFunctions.LogEntry("Reason: " + startExecutionException.getMessage(), false);
		}
	}

}
