package handlers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import businessLogics.ConfigFunctions;
import businessLogics.KeyActionFunctions;
import businessLogics.LogFunctions;
import commonUtils.PropertiesAndConstants;
import commonUtils.PropertiesAndConstants.Browsers;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class SeleniumHandler {
	
	public static WebDriver driver;	 
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Setting Up Selenium Driver With Base URL
	/// </summary>
	public static void SetUpSelenium() throws IOException
	{
		 try
	        {
			 	String BaseURL=ConfigFunctions.getEnvKeyValue("BASEURL");
			 	PropertiesAndConstants.Selenium = new FirefoxDriver();
	            PropertiesAndConstants.Selenium.get(BaseURL);
		
	            PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
	            PropertiesAndConstants.Selenium.manage().window().maximize();
		       	 ((JavascriptExecutor) PropertiesAndConstants.Selenium).executeScript("window.focus();");
		
	       }
		 
		 catch (Exception seleniumStart)
			{
			 	LogFunctions.LogEntry("Cannot set up Selenium", false);
	            LogFunctions.LogEntry("Reason: " + seleniumStart.getMessage(), false);
			}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Closing Selenium Driver
	/// </summary>
	 public static void CloseSeleniumDriver() throws Exception
	    {
	        try
	        {
	            PropertiesAndConstants.Selenium.quit();
	            //selenium.Dispose();
	            //wait(300);
	            
	            LogFunctions.LogEntry("Close Webdriver Process - Completed",false);
	        }
	        catch (Exception exc) 
	        { 
	        	LogFunctions.LogEntry("driver Close Exception " + exc.getMessage(), false); 
	        }
	        PropertiesAndConstants.Selenium = null;
	    }
	
	 
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Switching From one Browser To Other
	/// </summary>
	 public static WebDriver SwitchDriver() throws Exception
	    {
	        String browserType = ConfigFunctions.getEnvKeyValue("BROWSER");
	        
	        EventFiringWebDriver driverHandler;
	    	
	        if (driver == null)
	        { 
	       
	        	KeyActionFunctions driverAction = new KeyActionFunctions();
	            //Firefox, InternetExplorer, Chrome
	        
	        
	        	Browsers currentBrowser = Browsers.valueOf(browserType.toUpperCase());
	        	
	            switch(currentBrowser)
	            {
	                case FF:
	                    {
	                        
	                    	FirefoxProfile profile = null;
	                    	//FirefoxDriver driver = null;
	                        //if (Properties.IsFlashDisabled)
	                        //{
	                            //UtilityFunctions.LogEntry("Flash Disabled Test - YES", false);
	                            //if (Directory.Exists(Properties.AppHomeDrive + "TestAutomation\\TAF\\Drivers\\FireFoxProfileWithoutFlash"))
	                            //{
	                        	
	                            	//profile = new FirefoxProfile((new File(Properties.AppHomeDrive + "TestAutomation\\TAF\\Drivers\\FireFoxProfileWithoutFlash\\")));
	                            //}
	                            //else
	                            //{
	                                //profile = new FirefoxProfile();
	                                //UtilityFunctions.LogEntry("Cannot Find FireFox profile file", false);
	                            //}
	                        //}
	                        //else
	                        //{
	                        	profile = new FirefoxProfile();
	                        //}
	                        //profile.Port = profilePort;
	                       
	                        profile.setAcceptUntrustedCertificates(true);
	                        profile.setPreference("app.update.auto", false);
	                        profile.setPreference("app.update.enabled", false);
	                        profile.setPreference("app.update.silent", true);
	                        if (ConfigFunctions.getEnvKeyValue("VERIFYJAVASCRIPTERRORS").toUpperCase().equals( "YES"))
	                        {
	                        	File f = new File(PropertiesAndConstants.CurrentDirectory + "Automation\\Drivers\\JSErrorCollector.xpi");
	                            if (f.exists())
	                            {
	                            	LogFunctions.LogEntry("Set Up JavaScript Error collector...", false);
	                            	//profile.AddExtension(Properties.AppHomeDrive + "TestAutomation\\TAF\\Drivers\\JSErrorCollector.xpi");
	                            	LogFunctions.LogEntry("Set Up JavaScript Error collector - Completed", false);
	                            }
	                            else LogFunctions.LogEntry("Cannot Find FireFox extension for JavaScript Error collector file", false);
	                        }

	                        try 
	                        { 
	                        	
	                        	driver = new FirefoxDriver(profile); 
	                        }
	                        catch (WebDriverException webDriverExc)
	                        {
	                        	LogFunctions.LogEntry("Exception in process of Start WebDriver.", false);
	                        	LogFunctions.LogEntry("Reason: " + webDriverExc.getMessage(), false);
	                        	LogFunctions.LogEntry("Details: " + webDriverExc, false);
	                            //profilePort++;
	                        	LogFunctions.LogEntry("Retry to Launch Webdriver Browser...",false);
	                            SwitchDriver();
	                        }
	                        
	                     

	                        driver.manage().window().maximize();
	                      
	                       // SetDefaultDriverConfig();
	                        driverHandler = new EventFiringWebDriver(driver);
	                        PropertiesAndConstants.Selenium = driver;
	                        return driver;
	                    }
	                case IE:
	                    {
	                        //var ieoptions = new InternetExplorerOptions();
	                        //ieoptions.IgnoreZoomLevel = true;
	                        //ieoptions.IntroduceInstabilityByIgnoringProtectedModeSettings = true;
	                        //ieoptions.UnexpectedAlertBehavior = InternetExplorerUnexpectedAlertBehavior.Accept;
	              
	                    	File file = new File(PropertiesAndConstants.CurrentDirectory + "Automation\\Drivers\\IEDriverServer.exe");
	                    	
	                    	System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
	                    	driver = new InternetExplorerDriver();
	                    	
	                        //driver = new InternetExplorerDriver(Properties.AppHomeDrive  + "TestAutomation\\TAF\\Drivers", ieoptions);
	                        driver.manage().window().maximize();
	                        driver.manage().deleteAllCookies();
	                        SetDefaultDriverConfig();
	                        driverHandler = new EventFiringWebDriver(driver);
	                        
	                        PropertiesAndConstants.Selenium = driver;
	                     
	                        return driver;
	                    }
	                case CH:
	                    {
	                    	File file = new File(PropertiesAndConstants.CurrentDirectory + "Automation\\Drivers\\chromedriver.exe");
	                    	
	                    	System.setProperty("webdriver.chrome.driver",file.getAbsolutePath()); 
	                   
	                    	driver = new ChromeDriver();
	                    
	                        try { 
	                        		driver.manage().window().maximize(); 
	                        	}
	                        catch (Exception E)
	                        	{ 
	                        		LogFunctions.LogEntry("Driver Exception : Cannot maximize window", false); 
	                        	}
	                        SetDefaultDriverConfig();
	                        driverHandler = new EventFiringWebDriver(driver);
	                           
	                        PropertiesAndConstants.Selenium = driver;
	                        return driver;
	                    }
	                case OP:
	                    {
	                        // ***** To be define
	                        //return new OperaDriver();
	                        driverHandler = new EventFiringWebDriver(driver);
	                        
	                        return null;
	                    }
	                case AN:
	                    {
	                        // ***** To be define
	                        //return new AndriodDriver();
	                        driverHandler = new EventFiringWebDriver(driver);
	                        
	                        return null;
	                    }
	                case SA:
	                    {
	                        // ***** To be define
	                        //return new SafariDriver();
	                        driverHandler = new EventFiringWebDriver(driver);
	                        
	                        return null;
	                    }
	            }
	        }
	        else
	        {
	            PropertiesAndConstants.MainDriverWindowHandle = driver.getWindowHandle();
	            return driver;
	        }
			return driver;
	    }
	
	 public static void SetDefaultDriverConfig() throws IOException
	    {
	        LogFunctions.LogEntry("Set Default Config...", false);
	        String pageLoadTimeout = null;
	        String javaScriptExecuteTimeout;
	        String elementWaitTimeout;
	        String failStepsCountForStopScript;
	        try
	        {
	            pageLoadTimeout=(ConfigFunctions.getEnvKeyValue("PAGELOADTIMEOUT"));
	            if (!pageLoadTimeout.equals(null)&&!pageLoadTimeout.equals(""))
	            {
	               // driver.manage().timeouts().SetPageLoadTimeout(TimeSpan.FromSeconds(pageLoadTimeout));
	            	driver.manage().timeouts().implicitlyWait(Integer.parseInt(pageLoadTimeout), TimeUnit.MILLISECONDS);
	            }
	            else driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	        }
	        catch (WebDriverException wdsetexc)
	        {
	            LogFunctions.LogEntry("Cannot set PageLoadTimeout", false);
	            LogFunctions.LogEntry("Reason: " + wdsetexc.getMessage(),false);
	        }
	        try
	        {
	        	javaScriptExecuteTimeout=ConfigFunctions.getEnvKeyValue("JAVASCRIPTTIMEOUT");
	        	if(!javaScriptExecuteTimeout.equals(null)&&!javaScriptExecuteTimeout.equals(""))
	           // if (Integer.parseInt(XmlHandler.getEnvKeyValue("JAVASCRIPTTIMEOUT"), out javaScriptExecuteTimeout))
	                //driver.Manage().Timeouts().SetScriptTimeout(TimeSpan.FromSeconds(javaScriptExecuteTimeout));
	            	driver.manage().timeouts().implicitlyWait(Integer.parseInt(javaScriptExecuteTimeout), TimeUnit.MILLISECONDS);
	            else  driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	        }
	        catch (WebDriverException wdsetexc)
	        {
	            LogFunctions.LogEntry("Cannot set ScriptTimeout", false);
	            LogFunctions.LogEntry("Reason: " + wdsetexc.getMessage(), false);
	        }

	        try
	        {
	        	elementWaitTimeout=ConfigFunctions.getEnvKeyValue("ELEMENTWAITTIMEOUT");
	        	if(!elementWaitTimeout.equals(null)&&!elementWaitTimeout.equals(""))
	        	
	           // if (Integer.parseInt(XmlHandler.getEnvKeyValue("ELEMENTWAITTIMEOUT"), out elementWaitTimeout))
	               // driver.Manage().Timeouts().ImplicitlyWait(TimeSpan.FromSeconds(elementWaitTimeout));
	            	driver.manage().timeouts().implicitlyWait(Integer.parseInt(elementWaitTimeout), TimeUnit.MILLISECONDS);
	            else driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
	        }
	        catch (WebDriverException wdsetexc)
	        {
	            LogFunctions.LogEntry("Wait for Element Timeout", false);
	            LogFunctions.LogEntry("Reason: " + wdsetexc.getMessage(), false);
	        }

	        try
	        {
	        	failStepsCountForStopScript=ConfigFunctions.getEnvKeyValue("STOPIFFAILSTEPSCOUNT");
	        	if(!failStepsCountForStopScript.equals(null)&&!failStepsCountForStopScript.equals(""))
	           // if (Integer.parseInt(XmlHandler.getEnvKeyValue("STOPIFFAILSTEPSCOUNT"), out failStepsCountForStopScript))
	            {
	                PropertiesAndConstants.FailStepsCountLimit = Integer.parseInt(failStepsCountForStopScript);
	                LogFunctions.LogEntry("Stop Script when Fail steps Count limit will be" + failStepsCountForStopScript, false);
	            }
	            else
	            {
	                PropertiesAndConstants.FailStepsCountLimit = Integer.MAX_VALUE;
	                LogFunctions.LogEntry("Stop Script when Fail steps Count limit - Disabled", false);
	            }
	        }
	        catch (Exception e)
	        {
	            LogFunctions.LogEntry("Cannot Set Stop Script when Fail steps Count limit will be achieved parameter", false);
	            LogFunctions.LogEntry("Reason: " + e.getMessage(), false);
	        }
	     
	        PropertiesAndConstants.MainDriverWindowHandle = driver.getWindowHandle();
	        LogFunctions.LogEntry("Set Default Config - Completed", false);
	    }
	 
 /// <summary>
		   /// Author            :  Harshal Kakade
		   /// Description       : Setting Up Appium
		   /// </summary>
		   public static void MTSetUpMobileDriver() throws IOException
		   {
		     try
		           {

		      String BROWSER_NAME=ConfigFunctions.getEnvKeyValue("BROWSER_NAME");
		      String PLATFORM_VERSION=ConfigFunctions.getEnvKeyValue("PLATFORM_VERSION");
		      String PLATFORM_NAME=ConfigFunctions.getEnvKeyValue("PLATFORM_NAME");
		      String DEVICE_NAME=ConfigFunctions.getEnvKeyValue("DEVICE_NAME");
		      		      
		        DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability(MobileCapabilityType.BROWSER_NAME,BROWSER_NAME);
		    	capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,PLATFORM_VERSION);
		    	capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,PLATFORM_NAME);
		    	capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,DEVICE_NAME);
		    	capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"Appium");
		        System.out.println("Connecting to Mobile");
				PropertiesAndConstants.mt = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
				System.out.println("Appium Setup successfully ");

          }
		     
		     catch (Exception seleniumStart)
		     {
		       LogFunctions.LogEntry("Cannot set up Appium", false);
		               LogFunctions.LogEntry("Reason: " + seleniumStart.getMessage(), false);
		     }
		   }

	
}

