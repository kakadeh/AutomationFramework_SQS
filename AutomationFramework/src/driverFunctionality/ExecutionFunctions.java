package driverFunctionality;

import businessLogics.ConfigFunctions;
import businessLogics.LogFunctions;
import businessLogics.SuiteFunctions;
import commonUtils.PropertiesAndConstants;
import commonUtils.UtilityFunctions;
import handlers.ExcelHandler;

public class ExecutionFunctions {
	
	public static void main(String args[]) throws Exception
	{

	 		try
			{			
				System.out.println("Execution begins :");
				
				ExcelHandler.CloseAllExcelFiles();
				
				UtilityFunctions.CloseFireFoxBrowsers();
				System.out.println("closed Open Excel and Firefox browsers");
				
				UtilityFunctions.SetDefaultValues();
				System.out.println("Default Values Set");
				
				ConfigFunctions.populateEnvDictionary(PropertiesAndConstants.EvnFilePath);
				System.out.println("Env Directory Populated");
				
				ConfigFunctions.SetupEnvValues();
				System.out.println("Environment Values Set");
				
							
				if(PropertiesAndConstants.TempSuitPathsflag == 0){
									
					for(;PropertiesAndConstants.TempSuitePathsCount != PropertiesAndConstants.TempSuitPathsflag;)
					{	
					
						if(PropertiesAndConstants.TempSuitPathsflag > 0)
						{
							ConfigFunctions.SetupEnvValues();
							System.out.println("Environment Values Set");
						}
						
						//ORFunctions.PopulateObjRepositoryDictionary(PropertiesAndConstants.Repository);
						LogFunctions.LogEntry("Test Execution was started...", false);		
					
						SuiteFunctions.ProcessSuiteFile();
						
						System.out.println(PropertiesAndConstants.TempSuitePathsCount);
						
						System.out.println(PropertiesAndConstants.TempSuitPathsflag);
						
						PropertiesAndConstants.TempSuitPathsflag++;
						
						System.out.println(PropertiesAndConstants.TempSuitPathsflag);
						
					}
					
				}
				else{
					
					ConfigFunctions.SetupEnvValues();
					System.out.println("Environment Values Set");
					
					//ORFunctions.PopulateObjRepositoryDictionary(PropertiesAndConstants.Repository);
					LogFunctions.LogEntry("Test Execution was started...", false);
					
					SuiteFunctions.ProcessSuiteFile();
				}
				
				//SeleniumHandler.SetupTest();
			}
			catch (Exception startExecutionException) 
			{	
				LogFunctions.LogEntry("Error while starting the batch run.", false);
				LogFunctions.LogEntry("Reason: " + startExecutionException.getMessage(), false);
			}
		
	 		LogFunctions.LogEntry("***********Execution Completed Successfully**********", false);
	 	
	 	}
}
	 	

