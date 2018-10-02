package driverFunctionality;

import java.io.IOException;

import businessLogics.ConfigFunctions;
import businessLogics.LogFunctions;
import businessLogics.ORFunctions;
import businessLogics.SuiteFunctions;
import commonUtils.PropertiesAndConstants;
import commonUtils.UtilityFunctions;
import handlers.ExcelHandler;
import handlers.SeleniumHandler;

public class ExecutionsFunctionsMobile {

	public static void main(String args[]) throws Exception {
		try {
			ExcelHandler.CloseAllExcelFiles();
			System.out.println("Closed all open Excel files");
			UtilityFunctions.SetDefaultValues();
			System.out.println("Default Values Set");
			ConfigFunctions.populateEnvDictionary(PropertiesAndConstants.EvnFilePath);
			System.out.println("Env Directory Populated");
			// ConfigFunctions.MTSetupEnvValues();
			System.out.println("All setup Done");

			if (PropertiesAndConstants.TempSuitPathsflag == 0) {
				for (; PropertiesAndConstants.TempSuitePathsCount != PropertiesAndConstants.TempSuitPathsflag;) {
					if (PropertiesAndConstants.TempSuitPathsflag > 0) {
						ConfigFunctions.MTSetupEnvValues();
						System.out.println("Environment Values Set");
					}
					ORFunctions.MTPopulateObjRepositoryDictionary(PropertiesAndConstants.Repository);
					SeleniumHandler.MTSetUpMobileDriver();
					// LogFunctions.LogEntry("Test Execution was started...",
					// false);
					SuiteFunctions.MTProcessSuiteFile();
					System.out.println(PropertiesAndConstants.TempSuitePathsCount);
					System.out.println(PropertiesAndConstants.TempSuitPathsflag);
					PropertiesAndConstants.TempSuitPathsflag++;
					System.out.println(PropertiesAndConstants.TempSuitPathsflag);
				}

			} else {
				ConfigFunctions.MTSetupEnvValues();
				System.out.println("Environment Values Set");
				ORFunctions.PopulateObjRepositoryDictionary(PropertiesAndConstants.Repository);
				SeleniumHandler.MTSetUpMobileDriver();
				// LogFunctions.LogEntry("Test Execution was started...",
				// false);
				SuiteFunctions.MTProcessSuiteFile();
			}

			System.out.println("OVER AND OUT");
		}
		catch (Exception startExecutionException) {
			try {
				LogFunctions.LogEntry("Error while starting the batch run.", false);
				LogFunctions.LogEntry("Reason: " + startExecutionException.getMessage(), false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			LogFunctions.LogEntry("***********Execution Completed Successfully**********", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
