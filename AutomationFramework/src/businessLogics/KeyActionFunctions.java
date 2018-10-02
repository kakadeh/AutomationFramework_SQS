package businessLogics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.ranges.RangeException;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.google.common.base.Predicate;

import commonUtils.PropertiesAndConstants;
import commonUtils.UtilityFunctions;
import handlers.SeleniumHandler;
import jxl.Sheet;
import jxl.Workbook;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class KeyActionFunctions {
	public boolean _result = false;
	Map<String, String> _images = new HashMap<String, String>();
	Map<String, String> _imagesCheckResult = new HashMap<String, String>();
	Map<String, String> _links = new HashMap<String, String>();
	Map<String, String> _linksCheckResult = new HashMap<String, String>();
	String[] total_games=new String[100];
	int total_no_games=0;
	int total_no_star=0;
	int total_on_star=0;
	int total_off_star=0;
	
	public enum KeyAction
	{
		ImportCase,
		NavigateToURL,
		Click,
		ClickSafeIfPresent,
		JavaScriptClick,
		CheckBoxCheck,
		CheckBoxUnCheck,
		DoubleClickElement,
		RightClickElement,
		TypeText,
		SelectElementByText,
		OpenNewTab,
		OpenNewTabOrWindowByJavaScript,
		CloseAdditionalTab,
		CloseMainTab,
		SwitchToMainTab,
		SwitchToAdditionalTab,
		ReturnToPreviousPage,
		RefreshPage,
		AlertAccept,
		ElementMouseOver,
		MoveToElement,
		MoveMouseToElement,
		VerifyIsElementPresentAndDisplayed,
		VerifyIsElementNotDisplayed,
		VerifyIsTextPresent,
		VerifyIsTextNotPresent,
		VerifyIsElementContainsText,
		VerifyIsURLContainsText,
		VerifyIsURLNotContainsText,
		VerifyIsTitleContainsText,
		VerifyIsTitleNotContainsText,
		VerifyIsAlertPresent,
		VerifyIsImageDisplayed,
		VerifyElementAttribute,
		VerifyCssAttribute,
		VerifyIsElementNotContainsAttribute,
		VerifyAllImagesOnThePage,
		VerifyAllLinksOnThePage,
		VerifyAllImagesInElement,
		VerifyAllLinksButtonsInElement,
		VerifyBackgroundImageForElement,
		VerifyAllBackgroundImagesOnThePage,
		AddCookie,
		ClearAllCookies,
		ShowHiddenElement,
		RestartBrowser, 
		SwitchToNewWindow,
		GetRandomValue,
		
		
		// Mobile Key Actions
				MTNavigateToURL,
				MTClick,
				MTTypeText,
				MTVerifyIsElementPresentAndDisplayed,
				MTVerifyIsImageDisplayed,
				MTVerifyAllImagesOnThePage,
				MTVerifyIsTextPresent,
				MTPreviousPage,
				MTWait,
				MTItemSelector,
				MTVerifyErrorMessage,
				MTClickLink
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Restarting Browser with current URL if input URL is Null
	/// </summary>
	
	public Boolean RestartBrowser(String inputData) throws Exception
	{
		// Implementation:
		// - Restart with URL
		// - Restart with current URL
		if (inputData.equals("")||inputData.equals(null))
		{
			String currentURL = PropertiesAndConstants.Selenium.getCurrentUrl();
			SeleniumHandler.CloseSeleniumDriver();
			NavigateToUrl(currentURL);

			return true;
		}
		else
		{
			//SeleniumHandler.CloseSeleniumDriver();
			//wait(250);
			//PropertiesAndConstants.SwitchDriver();
			NavigateToUrl(inputData);
			
			return true;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Checking Weather an Excel File is valid or not	/// </summary>
	/// </summary>
	
	public boolean importCase(String inputData) throws Exception
	{
		if ((inputData.equals("")||inputData.equals(null)) && !(inputData.contains(".xls")))
		{
			LogFunctions.LogEntry("Incorrect Name of File for Import", false);
			return false;
		}
		//File file = new File(PropertiesAndConstants.ReusableScriptPath + inputData);
		
		System.out.println(PropertiesAndConstants.ReusableScriptPath);
		File file = new File(PropertiesAndConstants.ReusableScriptPath);
		
		if(!file.exists())
		{
			LogFunctions.LogEntry("File for Import {0} NOT FOUND"+ inputData, false);
			return false;
		}
		PropertiesAndConstants.IsStepSkip = true;
		PropertiesAndConstants.ResultString = "Import steps from file - Pass";
		LogFunctions.LogEntry("Import steps from file - Pass", false);
		return true;
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : To Generate random value
	/// </summary>
	
	public boolean GetRandomValue(String VariableName)throws Exception
	{
		try{
			Random rnd = new Random();
			int n = 100000 + rnd.nextInt(900000);
			
			String RandomValue = "NeoDroid"+n;
			SetVariableValue(VariableName, RandomValue);
			return true; 
			}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Clear browser cookies
	/// </summary>
	
	public boolean ClearAllCookies() throws Exception
	{
		try
		{
			PropertiesAndConstants.Selenium.manage().deleteAllCookies();
			LogFunctions.LogEntry("Clear all Browser Cookies - Pass", false);
			return true;
		}
		catch (Exception e) {
			LogFunctions.LogEntry("Cannot Clear Browser Cookies - Fail", false);
			return false;
		}
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : add Cookie to browser 
	/// </summary>
	
	public boolean AddCookie(String Cookie) throws Exception
	{
		try
		{
			String [] cookieKeyValue = Cookie.split("="); 
			Cookie cookie = new Cookie(cookieKeyValue[0], cookieKeyValue[1]);
			PropertiesAndConstants.Selenium.manage().addCookie(cookie);
			LogFunctions.LogEntry("Cookies successfully added- Pass", false);
			return true;
		}
		catch (Exception e) {
			LogFunctions.LogEntry("Cannot add Cookies to Browser - Fail", false);
			return false;
		}
	}

	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Navigate to URL which is given as a Parameter
	/// </summary>
		
	
	public Boolean NavigateToUrl(String url) throws Exception
	{
		System.out.println("url:"+url);
		if (url.equals(null)||url.equals(""))
		{
			LogFunctions.LogEntry("Navigate to URL"+url+" - Fail", false);
			LogFunctions.LogEntry("Incorrect URL:"+ url+" in Value field in TestScript file.", false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else { PropertiesAndConstants.scriptExecutionResult = "Pass"; }
			return false;
		}

		if (url.contains("http://") || url.contains("https://"))
		{
			// Should be added try/finally // Wait WebDriver Exception / Lost Connection...
			try
			{
				PropertiesAndConstants.Selenium.get(url);
				WaitForReadyStateComplete();
			}
			catch (Exception e)
			{
				// Restart Driver Application
				LogFunctions.LogEntry("Exception in process of Navigate to URL...", false);
                SeleniumHandler.CloseSeleniumDriver();
				LogFunctions.LogEntry("Restatrt WebDriver Application...", false);
				//PropertiesAndConstants.SwitchDriver();
				//LogFunctions.LogEntry("Try to Navigate to URL again...", false);
				//NavigateToUrl(url);
			}
			//if (url.contains(PropertiesAndConstants.Selenium.getCurrentUrl()) || url.contains(PropertiesAndConstants.Selenium.getCurrentUrl()))
             if(!UtilityFunctions.IsNullOrWhiteSpace(PropertiesAndConstants.Selenium.getCurrentUrl()))
			{
				LogFunctions.LogEntry("Navigate to URL"+url+"- Pass" , false);
				return true;
			}
			else
			{
				LogFunctions.LogEntry("Navigate to URL"+url+" - Fail" , false);
				LogFunctions.LogEntry("Current browser URL: Actual -"+PropertiesAndConstants.Selenium.getCurrentUrl()+" Expected - "+url, false);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail")
				{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
				else { PropertiesAndConstants.scriptExecutionResult = "Pass"; }
				return false;
			}
		}
		else
		{
			// Should be added try/finally
			if (!PropertiesAndConstants.Url.endsWith("/") && !url.startsWith("/"))
			{
				url = "/" + url;
			}
			if (PropertiesAndConstants.Url.endsWith("/") && url.startsWith("/"))
			{
				url = url.replaceFirst("/", "");
			}
			String urlToNavigate = PropertiesAndConstants.Url + url;
			// Should be added try/finally // Wait WebDriver Exception / Lost Connection...
			try
			{
				PropertiesAndConstants.Selenium.navigate().to(urlToNavigate);
				WaitForReadyStateComplete();
			}
			catch (WebDriverException e)
			{
				// Restart Driver Application
				LogFunctions.LogEntry("Exception in process of Navigate to URL...", false);
				SeleniumHandler.CloseSeleniumDriver();
				LogFunctions.LogEntry("Restatrt WebDriver Application...", false);
				//PropertiesAndConstants.SwitchDriver();
				//LogFunctions.LogEntry("Try to Navigate to URL again...", false);
				//NavigateToUrl(url);
			}
			if (!(PropertiesAndConstants.Selenium.getCurrentUrl().equals(null)||PropertiesAndConstants.Selenium.getCurrentUrl().equals("")))
			{
				LogFunctions.LogEntry(("Navigate to URL"+urlToNavigate+" - Pass" ), false);
				return true;
			}
			else
			{
				LogFunctions.LogEntry("Navigate to URL"+url+" - Fail" ,false);
				LogFunctions.LogEntry("Current browser URL: Actual -"+PropertiesAndConstants.Selenium.getCurrentUrl()+"; Expected -"+url, false);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail")
				{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
				else { PropertiesAndConstants.scriptExecutionResult = "Pass"; }
				return false;
			}
		}
	}
		
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Click element without Frame 
	/// </summary>
	
	private boolean Click(String locator) throws Exception
	{
		if (locator.equals(null)||locator.equals(""))
		{
			LogFunctions.LogEntry("Click on :"+locator+" - Fail - Incorrect Locator :"+locator, false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		WebElement element = WaitForElementPresent(locator);

		if (element != null && element.isDisplayed())
		{
			element.click();
			LogFunctions.LogEntry("Click on :"+locator+" - Pass", false);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Click on :"+locator+" - Fail", false);
			return false;
		}
	}

	

	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Click element in Frame
	/// </summary>
	
	
	public boolean ClickInFrame(String name, String locator, String frames) throws Exception
	{
		if (locator.equals(null) || locator.equals(""))
		{
			LogFunctions.LogEntry("Click on :"+name+" - Fail - Incorrect Locator :"+locator, false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!(frames.equals("")||frames.equals(null)))
			{
			 
			 SwitchToFrames(frames);
			}
		WebElement element = WaitForElementPresent(locator);

		if (!element.equals( null) && element.isDisplayed())
		{
			element.click();
			if (ConfigFunctions.getEnvKeyValue("ALERTAUTOACCEPT").toUpperCase() == "YES") ProcessUnexpectedAlert();
			LogFunctions.LogEntry("Click on :"+name+" - Pass",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail") PropertiesAndConstants.scriptExecutionResult = "Fail";
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			// ***** For Processing UnExpected Alert
			SwitchToDefaultContent(frames);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Click on :"+name+" - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			SwitchToDefaultContent(frames);
			return false;
		}
	}

	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :Switch to Frame for Working with elements in this frame
	/// for working with <frame>elements</frame> OR <iframe>elements</iframe>
	/// </summary>
	
	@SuppressWarnings("null")
	private void SwitchToFrames(String frames) throws Exception
	{
		WebDriverWait framewait = new WebDriverWait(PropertiesAndConstants.Selenium, 120000);
		SwitchToDefaultContent(frames);
		String[] framesArray = null;
		if (frames.contains("||"))
		{
				framesArray = frames.split("\\|\\|");
				PropertiesAndConstants.Selenium.switchTo().defaultContent();
		for (int i = 0; i < framesArray.length; i++)
		{
			if (framesArray[i] != null)
			{
				WebElement frameElement = FindElement(framesArray[i]);

				if (frameElement != null)
				{
					LogFunctions.LogEntry("Frame Element with locator {0} FOUND! "+ framesArray[i], false);

					PropertiesAndConstants.Selenium.switchTo().frame(frameElement);
					WaitForReadyStateComplete();
					Thread.sleep(1000);
				}
				else 
				{ 
					LogFunctions.LogEntry(String.format("Cannot Find Frame Element with locator {0}! ", framesArray[i]), false); 
				}
			}
			else
			{
				LogFunctions.LogEntry(String.format("Incorrect Frame Element locator {0} ! ", framesArray[i]), false);
			}
		}
		}
		else
		{
			WebElement frameElement = FindElement(frames);
			if (frameElement != null)
			{
				LogFunctions.LogEntry("Frame Element with locator {0} FOUND! "+ frames, false);
				SwitchToDefaultContent();
				//PropertiesAndConstants.Selenium.switchTo().defaultContent();
				//PropertiesAndConstants.Selenium.switchTo().defaultContent();
				PropertiesAndConstants.Selenium.switchTo().frame(frameElement);
				WaitForReadyStateComplete();
				Thread.sleep(1000);
			}
			else 
			{ 
				LogFunctions.LogEntry(String.format("Cannot Find Frame Element with locator {0}! ", frames), false); 
			}
				
				
		}
		
			
		
		}
	


	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Switch WebDriver in a Top Content of Document without Frame
	/// </summary>
	

	private void SwitchToDefaultContent() throws Exception
	{
		try
		{
			Thread.sleep(250); // Templorary Workaround for No Responce Exception
			PropertiesAndConstants.Selenium.switchTo().defaultContent();
			Thread.sleep(250); // Templorary Workaround for No Responce Exception
		}
		catch(Exception exc)
		{
			LogFunctions.LogEntry("Cannot Switch to Default content - Fail", false);
			LogFunctions.LogEntry("Reason: " + exc.getMessage(),false);
			LogFunctions.LogEntry("Details: " + exc,false);
		}
	}

	

	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Switch WebDriver in a Top Content of Document with Frame
	/// </summary>
	/// </summary>
	
	private void SwitchToDefaultContent(String frames) throws Exception
	{
		// if (isAlertPresent()) return;
		if (!(frames.equals("")||frames.equals(null)))
		{
			try
			{
				Thread.sleep(500); // Temporary Workaround for No Responce Exception
				PropertiesAndConstants.Selenium.switchTo().defaultContent();
				Thread.sleep(500); // Temporary Workaround for No Responce Exception
			}
			catch (Exception frameExc)
			{
				LogFunctions.LogEntry("Cannot Switch to Default content - Fail", false);
				LogFunctions.LogEntry("Details: " + frameExc,false);
			}
		}
	}

	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Method Wait until element presence in DOM 
	/// </summary>
		
	private WebElement WaitForElementPresent(String locator) throws IOException, Exception
	{
		if (locator.equals("")||locator.equals(null))
		{	
			LogFunctions.LogEntry("Incorrect Locator :" + locator, false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return null;
		}
		try
		{	WebDriverWait waitAppear = new WebDriverWait(PropertiesAndConstants.Selenium,45);
			WebElement element = waitAppear.until(ExpectedConditions.visibilityOf(FindElement(locator)));
			return element;
		}
		catch (Exception e)
		{	
			LogFunctions.LogEntry("Wait for element"+locator+" with locator"+locator+" present (TimeOut) - Fail", false);
			return null;
		}
	}
	
	/// <summary>
		/// Author            :  Harshal Kakade
		/// Description       : Method Wait until element presence in DOM 
		/// </summary>
			
		private WebElement MTWaitForElementPresent(String locator) throws IOException, Exception
		{
			if (locator.equals("")||locator.equals(null))
			{	
				LogFunctions.LogEntry("Incorrect Locator :" + locator, false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return null;
			}
			try
			{	WebDriverWait waitAppear = new WebDriverWait(PropertiesAndConstants.mt,45);
				WebElement element = waitAppear.until(ExpectedConditions.visibilityOf(MTFindElement(locator)));
				return element;
			}
			catch (Exception e)
			{	
				LogFunctions.LogEntry("Wait for element"+locator+" with locator"+locator+" present (TimeOut) - Fail", false);
				return null;
			}
		}

	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Web Driver Find element in the Top Document
	/// </summary>


	private WebElement MTFindElement(String locator) throws Exception
	{
		System.out.println(locator);
		WebElement element = null;

		if (locator.equals("")||locator.equals(null))
		{
			LogFunctions.LogEntry("Incorrect Locator :"+ locator, false);
			return null;
		}

		else if ((locator.contains("#") || locator.contains(".") || locator.contains(">") || locator.contains("~") ||
				locator.contains("+") || locator.contains("*") || locator.contains("$") ||
				locator.contains("^") || locator.contains(" ") || locator.contains("[")) && (!locator.contains("/") && !locator.contains("@")))
		{
			try // by CSS
			{
				PropertiesAndConstants.mt.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				element = PropertiesAndConstants.mt.findElement(By.cssSelector(locator));
				PropertiesAndConstants.mt.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				return element;
			}
			catch (NoSuchElementException e) {}//{ LogFunctions.LogEntry("FindByCSS - Fail", false); } //{ LogFunctions.LogEntry("Find Element By CSS - Fail. Reason: " + noElementExc , false); }
			//finally (IllegalLocatorException e) { } // { LogFunctions.LogEntry("Find Element By CSS - Fail. Reason: " + illegalLocatorExc, false); }
			catch (InvalidElementStateException e) { } // { LogFunctions.LogEntry("Find Element By CSS - Fail. Reason: " + invalidElementStateExc, false); }
		}

		else if ((locator.contains("/") || locator.contains("contains") || locator.contains("text()") || locator.contains("@") || locator.contains("*") || locator.contains("////") ||
				locator.contains("////") || locator.contains("[") || locator.contains("]")) && (!locator.contains("#") && !locator.contains(">")))
		{
			try // by XPATH
			{
				PropertiesAndConstants.mt.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				element = PropertiesAndConstants.mt.findElement(By.xpath(locator));
				PropertiesAndConstants.mt.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				return element;
			}
			catch (NoSuchElementException e)
			{}
		}

		else if (!locator.contains("#") && !locator.contains(".") && !locator.contains(">") && !locator.contains("(") &&
				!locator.contains(")") && !locator.contains("/") && !locator.contains("@") && !locator.contains("*") &&
				!locator.contains("$") && !locator.contains("^") && !locator.contains(" ") && !locator.contains("+"))
		{
			
			try // by Id
			{
				PropertiesAndConstants.mt.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				return element = PropertiesAndConstants.mt.findElement(By.id(locator));
			}
			catch (Exception e){}{ LogFunctions.LogEntry("FindById - Fail", false); }
			

			try // by ClassName
			{
				PropertiesAndConstants.mt.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				element = PropertiesAndConstants.mt.findElement(By.className(locator));
			
		
				return element;
			}
			catch (Exception e){}
			{ LogFunctions.LogEntry("FindByClassName - Fail", false); }
			
		}

		else if (!locator.contains("#") && !locator.contains(">") && !locator.contains(".") && !locator.contains("(") &&
				!locator.contains(")") && !locator.contains("/") && !locator.contains("@") && !locator.contains("*") &&
				!locator.contains("$") && !locator.contains("^") || locator.contains(" ") )
		{
			try // by LinkText
			{
				PropertiesAndConstants.mt.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				element = PropertiesAndConstants.mt.findElement(By.linkText(locator));
				//PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(120000,TimeUnit.SECONDS);
				return element;
			}
			catch (NoSuchElementException e) {}
		}
		// ***** Need to Add Fail code for Test Result
		return null;
	}

	/// <summary>
		/// Author            :  Harshal Kakade
		/// Description       : Web Driver Find element in the Top Document
		/// </summary>


		private WebElement FindElement(String locator) throws Exception
		{
			WebElement element = null;

			if (locator.equals("")||locator.equals(null))
			{
				LogFunctions.LogEntry("Incorrect Locator :"+ locator, false);
				return null;
			}

			else if ((locator.contains("#") || locator.contains(".") || locator.contains(">") || locator.contains("~") ||
					locator.contains("+") || locator.contains("*") || locator.contains("$") ||
					locator.contains("^") || locator.contains(" ") || locator.contains("[")) && (!locator.contains("/") && !locator.contains("@")))
			{
				try // by CSS
				{
					PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
					element = PropertiesAndConstants.Selenium.findElement(By.cssSelector(locator));
					PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
					return element;
				}
				catch (NoSuchElementException e) {}//{ LogFunctions.LogEntry("FindByCSS - Fail", false); } //{ LogFunctions.LogEntry("Find Element By CSS - Fail. Reason: " + noElementExc , false); }
				//finally (IllegalLocatorException e) { } // { LogFunctions.LogEntry("Find Element By CSS - Fail. Reason: " + illegalLocatorExc, false); }
				catch (InvalidElementStateException e) { } // { LogFunctions.LogEntry("Find Element By CSS - Fail. Reason: " + invalidElementStateExc, false); }
			}

			else if ((locator.contains("/") || locator.contains("contains") || locator.contains("text()") || locator.contains("@") || locator.contains("*") || locator.contains("////") ||
					locator.contains("////") || locator.contains("[") || locator.contains("]")) && (!locator.contains("#") && !locator.contains(">")))
			{
				try // by XPATH
				{
					PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
					element = PropertiesAndConstants.Selenium.findElement(By.xpath(locator));
					PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
					return element;
				}
				catch (NoSuchElementException e){}// { LogFunctions.LogEntry("FindByXPATH - Fail", false); }//{ LogFunctions.LogEntry("Find Element By XPATH - Fail. Reason: " + noElementExc, false); }
				//finally (IllegalLocatorException) { } //{ LogFunctions.LogEntry("Find Element By XPATH - Fail. Reason: " + illegalLocatorExc, false); }
				//finally (InvalidSelectorException) { } //{ LogFunctions.LogEntry("Find Element By XPATH - Fail. Reason: " + invalidSelectorExc, false); }
				//finally (InvalidElementStateException) { } //{ LogFunctions.LogEntry("Find Element By XPATH - Fail. Reason: " + invalidElementStateExc, false); }
			}

			else if (!locator.contains("#") && !locator.contains(".") && !locator.contains(">") && !locator.contains("(") &&
					!locator.contains(")") && !locator.contains("/") && !locator.contains("@") && !locator.contains("*") &&
					!locator.contains("$") && !locator.contains("^") && !locator.contains(" ") && !locator.contains("+"))
			{
				
				try // by Id
				{
					PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
					return element = PropertiesAndConstants.Selenium.findElement(By.id(locator));
				}
				catch (Exception e){}{ LogFunctions.LogEntry("FindById - Fail", false); }
				//finally (IllegalLocatorException) { }
				//finally (InvalidSelectorException) { }
				//finally (InvalidElementStateException) { }

				try // by ClassName
				{
					PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
					element = PropertiesAndConstants.Selenium.findElement(By.className(locator));
				
			
					return element;
				}
				catch (Exception e){}
				{ LogFunctions.LogEntry("FindByClassName - Fail", false); }
				// finally (IllegalLocatorException) { }
				//finally (InvalidSelectorException) { }
				//finally (InvalidElementStateException) { }
			}

			else if (!locator.contains("#") && !locator.contains(">") && !locator.contains(".") && !locator.contains("(") &&
					!locator.contains(")") && !locator.contains("/") && !locator.contains("@") && !locator.contains("*") &&
					!locator.contains("$") && !locator.contains("^") || locator.contains(" ") )
			{
				try // by LinkText
				{
					PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
					element = PropertiesAndConstants.Selenium.findElement(By.linkText(locator));
					//PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(120000,TimeUnit.SECONDS);
					return element;
				}
				catch (NoSuchElementException e) {}//{ LogFunctions.LogEntry("FindByLinkText - Fail", false); }
				// finally (IllegalLocatorException) { }
				// finally (InvalidSelectorException) { }
				//  finally (InvalidElementStateException) { }
			}
			// ***** Need to Add Fail code for Test Result
			return null;
		}

	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Wait Until browser property readyState = 4 (complete)
	/// </summary>
	
	private void WaitForReadyStateComplete() throws Exception
	{
		try
		{
			try
			{
				String readystateproperty ="";
				//String readystateproperty = ((JavaScriptExecutor)PropertiesAndConstants.Selenium).executeAsyncScript("return document.readyState").toString();
				if (readystateproperty == "complete")
					return;
				else
					return;
			}
			catch (Exception exc)
			{
				LogFunctions.LogEntry("Fail for wait ReadyState property to complete - Fail ", false);
				LogFunctions.LogEntry("Reason: " + exc, false);
				return;
			}       
		}
		catch (Exception e)
		{
			LogFunctions.LogEntry("Wait ReadyState property to complete (Timeout) - Fail ", false);
			//   LogFunctions.TakeScreenshoot("ReadyStateProperty_Fail"); // Templorary
		}
	}

	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Wait Until browser property readyState = 4 (complete)
	/// </summary>
	private void ProcessUnexpectedAlert()throws Exception
	{
		try
		{
			String alerttext = PropertiesAndConstants.Selenium.switchTo().alert().getText();
			try { //   LogFunctions.TakeScreenshoot("UnexpectedAlert");
			}
			catch(Exception e) { }
			PropertiesAndConstants.Selenium.switchTo().alert().accept();
			LogFunctions.LogEntry("Accept Alert", false);
			LogFunctions.LogEntry("Alert text: " + alerttext, false);
		}
		catch(Exception e)
		{
			LogFunctions.LogEntry("Alert not Present", false);
		}
	}
		
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : 
	/// </summary>
	
	public boolean JavaScriptClick(String locator, String frames) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Click on :"+locator+" - Fail - Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) SwitchToFrames(frames);

		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{
			//  ((IJavaScriptExecutor)PropertiesAndConstants.GetDriver).ExecuteScript("arguments[0].click();", element);
			if (ConfigFunctions.getEnvKeyValue("ALERTAUTOACCEPT").toUpperCase()== "YES") ProcessUnexpectedAlert();
			LogFunctions.LogEntry("JavaScript Click on :"+locator+" - Pass",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail") PropertiesAndConstants.scriptExecutionResult = "Fail";
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			// ***** For Processing UnExpected Alert
			SwitchToDefaultContent(frames);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("JavaScript Click on :"+locator+" - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			SwitchToDefaultContent(frames);
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Check Weather a Checkbox is Selected or not without Frame
	/// </summary>
	
	private boolean CheckBoxCheck(String name, String locator) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("CheckBox Check"+ name+" - Fail - Incorrect Locator :"+locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		WebElement element = WaitForElementPresent(locator);

		if (element != null && element.isDisplayed())
		{
			if (!element.isSelected())
			{
				element.click();
				LogFunctions.LogEntry("CheckBox Check :"+name+" - Pass" ,false);
			}
			else
			{
				LogFunctions.LogEntry("CheckBox was Checked yet :"+name+"- Pass" ,false);
			}
			return true;
		}
		else
		{
			LogFunctions.LogEntry("CheckBox Check :"+ name+" - Fail",false);
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Check Weather a Checkbox is check or not with Frame
	/// </summary>
	
	public boolean CheckBoxCheck(String name, String locator, String frames) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("CheckBox Check"+name+" - Fail - Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) SwitchToFrames(frames);
		WebElement element = WaitForElementPresent(locator);

		if (element != null && element.isDisplayed())
		{
			if (!element.isSelected())
			{
				element.click();
				LogFunctions.LogEntry("CheckBox Check :"+name+" - Pass" ,false);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail") PropertiesAndConstants.scriptExecutionResult = "Fail";
				else PropertiesAndConstants.scriptExecutionResult = "Pass";
			}
			else
			{
				LogFunctions.LogEntry("CheckBox was Checked yet :"+name+" - Pass" ,false);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail") PropertiesAndConstants.scriptExecutionResult = "Fail";
				else PropertiesAndConstants.scriptExecutionResult = "Pass";
			}
			SwitchToDefaultContent(frames);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("CheckBox Check :"+name+"- Fail" ,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			SwitchToDefaultContent(frames);
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Verify that page source contains text
	/// </summary>
	
	public boolean VerifyIsTextPresent(String inputData) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(inputData))
		{
			LogFunctions.LogEntry("Incorrect Input Data :"+inputData,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (PropertiesAndConstants.Selenium.getPageSource().contains(inputData))
		{
			LogFunctions.LogEntry("Text :"+inputData+" : Present : - Pass" ,false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Text :"+inputData+" : NOT Present  - Fail" ,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Click Link / Button if present without fail
	/// </summary>
	
		public boolean ClickSafeIfPresent(String locator, String frames) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Click on :"+locator+" - Fail - Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) SwitchToFrames(frames);
		WebElement element = WaitForElementPresent(locator);

		if (element != null && element.isDisplayed())
		{
			element.click();
			if (   ConfigFunctions.getEnvKeyValue("ALERTAUTOACCEPT").toUpperCase() == "YES") ProcessUnexpectedAlert();
			LogFunctions.LogEntry("Click on :"+locator+" - Pass",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail") PropertiesAndConstants.scriptExecutionResult = "Fail";
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			SwitchToDefaultContent(frames);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Cannot Click on :"+locator+" - Element not Present/Displayed",false);
			SwitchToDefaultContent(frames);
			return true;
		}
	}
		
		
		/// <summary>
		/// Author            :  Harshal Kakade
		/// Description       : Check Box UnCheck or Not without Frame
		/// </summary>	

	private boolean CheckBoxUnCheck(String name, String locator) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("CheckBox UnCheck"+name+" - Fail - Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		WebElement element = WaitForElementPresent(locator);

		if (element != null && element.isDisplayed())
		{
			if (element.isSelected())
			{
				element.click();
				LogFunctions.LogEntry("CheckBox UnCheck :"+name+ "- Pass",false);
			}
			else LogFunctions.LogEntry("CheckBox was Unchecked yet :"+name+"- Pass",false);

			return true;
		}
		else
		{
			LogFunctions.LogEntry("CheckBox UnCheck :"+name+" - Fail",false);
			return false;
		}
	}
	
	

	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Check Box UnCheck or Not with Frames
	/// </summary>	
	public boolean CheckBoxUnCheck(String name, String locator, String frames) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("CheckBox UnCheck"+name+"- Fail - Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) SwitchToFrames(frames);
		WebElement element = WaitForElementPresent(locator);

		if (element != null && element.isDisplayed())
		{
			if (element.isSelected())
			{
				element.click();
				LogFunctions.LogEntry("CheckBox UnCheck :"+name+" - Pass",false);
			}
			else    LogFunctions.LogEntry("CheckBox was Unchecked yet :"+name+" - Pass",false);

			if (PropertiesAndConstants.scriptExecutionResult == "Fail") PropertiesAndConstants.scriptExecutionResult = "Fail";
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			SwitchToDefaultContent(frames);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("CheckBox UnCheck :"+name+" - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			SwitchToDefaultContent(frames);
			return false;
		}
	}
	
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Check Double Click Element Without Frame
	/// </summary>	
	
	
	private boolean DoubleClickElement(String name, String locator)throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Double Click button :"+name+" - Fail - Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		WebElement element = WaitForElementPresent(locator);
		if (element != null)
		{
			
			Actions action = new Actions(PropertiesAndConstants.Selenium);
			action.doubleClick(element).perform();
			Thread.sleep(250);
			LogFunctions.LogEntry("Double Click button :"+name+" - Pass",false);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Double Click button :"+name+" : Cannot Find element - Fail",false);
			return false;
		}
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Check Double Click Element With Frame
	/// </summary>	


	public boolean DoubleClickElement(String name, String locator, String frames) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry(String.format("Double Click button : \"{0}\" - Fail - Incorrect Locator : \"{1}\"", name, locator),false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) SwitchToFrames(frames);
		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{
			Actions action = new Actions(PropertiesAndConstants.Selenium);
			action.doubleClick(element).perform();
			Thread.sleep(300);
			LogFunctions.LogEntry(String.format("Double Click button : \"{0}\" - Pass", name),false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail") PropertiesAndConstants.scriptExecutionResult = "Fail";
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			SwitchToDefaultContent(frames);
			return true;
		}
		else
		{
			LogFunctions.LogEntry(String.format("Double Click button : \"{0}\" : Cannot Find element - Fail", name),false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			SwitchToDefaultContent(frames);
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Context Click (Right Button Mouse Click)
	/// </summary>	
	
	public boolean RightClickElement(String name, String locator, String frames)throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Right Click button :"+name+" - Fail - Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) SwitchToFrames(frames);
		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{
			Actions actionRightClick = new Actions(PropertiesAndConstants.Selenium);
			Actions actionMove = new Actions(PropertiesAndConstants.Selenium);
			actionMove.moveToElement(element).perform();
			actionRightClick.contextClick(element).perform();
			SwitchToDefaultContent(frames);
			LogFunctions.LogEntry("Right Click button :"+name+"- Pass",false);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Cannot find element for Right Click - Fail",false);
			SwitchToDefaultContent(frames);
			return false;
		}

	}
	
	

	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Type text into element (Example: TextBox) without Frame
	/// </summary>	
	
	
	private boolean InputTextElement(String locator, String text)throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Type text into :"+locator+" - Fail - Incorrect Locator :"+ locator,false);
			return false;
		}

		WebElement element = WaitForElementPresent(locator);

		if (element != null && element.isDisplayed())
		{
			element.clear();
			Thread.sleep(250);
			element.sendKeys(text);
			Thread.sleep(250);
			LogFunctions.LogEntry("Enter text into :"+locator+" - Pass" ,false);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Enter text into :"+ locator+" - Fail",false);
			return false;
		}
	}

		
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Type text into element (Example: TextBox) with Frame
	/// </summary>	
	
	public boolean InputTextElement(String locator, String frames, String text)throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Type text into :"+locator+" - Fail - Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) 
			{
			  SwitchToFrames(frames);
			}
		WebElement element = WaitForElementPresent(locator);

		if (element != null && element.isDisplayed())
		{
			if (UtilityFunctions.IsNullOrWhiteSpace(frames)) element.clear(); // Clear() cannot working with Frames
			Thread.sleep(250);
			element.sendKeys(text);
			Thread.sleep(300);
			LogFunctions.LogEntry("Enter text into :"+locator+" - Pass" ,false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			SwitchToDefaultContent(frames);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Enter text into :"+locator+" - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			SwitchToDefaultContent(frames);
			return false;
		}
	}
		
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Verify that WebElement Contain Text
	/// </summary>	
		
	public boolean VerifyIsElementContainsText( String locator, String frames, String inputData)throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator) || UtilityFunctions.IsNullOrWhiteSpace(inputData))
		{
			LogFunctions.LogEntry("Incorrect locator"+locator+" or Input data :"+ inputData,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames))
			SwitchToFrames(frames);

		WaitForElementPresent(locator);
		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{

			for (int i = 0; i <= 10; i++)
			{
				if (!element.isDisplayed()) 
					Thread.sleep(500);
				else break;
			}

			String elementattrtext = element.getAttribute("textContent");

			if (elementattrtext != null && (elementattrtext.trim().toLowerCase().contains(inputData.toLowerCase()) ||
					elementattrtext.trim().toLowerCase().equals(inputData.toLowerCase())))
			{
				LogFunctions.LogEntry("Element :"+locator+" Contains text"+inputData+" : - Pass",false);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail")
				{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
				else PropertiesAndConstants.scriptExecutionResult = "Pass";
				SwitchToDefaultContent(frames);
				return true;
			}
			else
			{
				LogFunctions.LogEntry("Element :"+locator+"not Contains text"+inputData+". Actual:"+elementattrtext+" : - Fail",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				SwitchToDefaultContent(frames);
				return false;
			}
		}

		else
		{
			LogFunctions.LogEntry("Cannot find Element :"+locator+": - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			PropertiesAndConstants.Selenium.switchTo().defaultContent();
			return false;
		}
	}


	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Select Element from select node using Text
	/// </summary>	
	
	public boolean SelectElementByText(String name, String locator, String frames, String text)throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Select item from :"+name+" - Fail - Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (UtilityFunctions.IsNullOrWhiteSpace(text))
		{
			LogFunctions.LogEntry("Select item from"+name+"- Fail - Incorrect/Null Value for Select",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) 
			SwitchToFrames(frames);
		WebElement selectnode = WaitForElementPresent(locator);

		if (selectnode != null)
		{
			Actions actions = new Actions(PropertiesAndConstants.Selenium);
            actions.moveToElement(selectnode).perform();
            
            if(selectnode.isEnabled())selectnode.click();
            
            Select select = new Select(selectnode);
            
            selectnode.sendKeys(text);
            select.selectByVisibleText(text.trim());


			//WebElement select = FindElement(locator);
			//((JavascriptExecutor)PropertiesAndConstants.Selenium).executeScript("arguments[0].style.opacity = 100;", select);
			//(new Select(select)).selectByVisibleText(text);
						
			//JavascriptExecutor js = (JavascriptExecutor) PropertiesAndConstants.Selenium;
			//SelectElement select = new SelectElement(selectnode);
			//Select select = new Select(selectnode);
			//select.selectByVisibleText(text);
            
			
			LogFunctions.LogEntry("Select element from:"+name+" - Pass",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			SwitchToDefaultContent(frames);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Cannot select element from :"+name+" - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			SwitchToDefaultContent(frames);
			return false;
		}
	}

	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Click Element with atttr target='_blank' and open new Tab or Window
	/// </summary>	
	
	public boolean OpenNewTabOrWindow(String name, String locator, String frames)throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator :"+locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) SwitchToFrames(frames);

		WebElement element = WaitForElementPresent(locator);

		if (element != null && element.isDisplayed())
		{
			try
			{
				element.click();
				Thread.sleep(1000);
				//PropertiesAndConstants.AdditionalDriverWindowHandle = GlobalClass.GetDriver.WindowHandles[1];
				PropertiesAndConstants.AdditionalDriverWindowHandle=PropertiesAndConstants.Selenium.getWindowHandle();
				//GlobalClass.GetDriver.SwitchTo().Window(GlobalClass.AdditionalDriverWindowHandle);
				
				for(String Winhadle : PropertiesAndConstants.Selenium.getWindowHandles()){
					PropertiesAndConstants.Selenium.switchTo().window(Winhadle);
				}
					
				LogFunctions.LogEntry("Open new tab - Pass",false);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail")
				{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
				else PropertiesAndConstants.scriptExecutionResult = "Pass";
				SwitchToDefaultContent(frames);
				//PropertiesAndConstants.Selenium.switchTo().window(PropertiesAndConstants.AdditionalDriverWindowHandle);
				WaitForReadyStateComplete();
				return true;
			}
			catch (IllegalArgumentException outofrangeexc) 
			// catch (ArgumentOutOfRangeException )
			{
				LogFunctions.LogEntry("Cannot open new tab - Fail",false);
				LogFunctions.LogEntry("Reason: " + outofrangeexc.getMessage(), false);
				LogFunctions.LogEntry("Details: " + outofrangeexc, false);
				SwitchToDefaultContent(frames);
				return false;
			}
			catch (Exception exc)
			{

				//  Console.ForegroundColor = ConsoleColor.Red;
				LogFunctions.LogEntry("Cannot open new tab - Fail",false);
				LogFunctions.LogEntry("Reason: " + exc.getMessage(), false);
				LogFunctions.LogEntry("Details: " + exc, false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				// Console.ResetColor();
				SwitchToDefaultContent(frames);
				return false;
			}   
		}
		else
		{
			LogFunctions.LogEntry("Cannot open new tab (Element for Click not Found) - Fail",false);
			return false;
		}
	}
	
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Switch to the new window opened.
	/// </summary>	
	public boolean SwitchToNewWindow() throws IOException
	{
		try
		{
		String parentWindow = PropertiesAndConstants.Selenium.getWindowHandle();
		Set<String> handles =  PropertiesAndConstants.Selenium.getWindowHandles();
		   for(String windowHandle  : handles)
		       {
		       if(!windowHandle.equals(parentWindow))
		          {
		    	   PropertiesAndConstants.Selenium.switchTo().window(windowHandle);
		         
		          }
		       }
		   LogFunctions.LogEntry("Switch to new window - Pass",false);
		   return true;
			
	}catch(Exception exc)
	{
		LogFunctions.LogEntry("Cannot open new tab - Fail",false);
		LogFunctions.LogEntry("Reason: " + exc.getMessage(), false);
		LogFunctions.LogEntry("Details: " + exc, false);
		return false;
	}
	}

	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Click Element with atttr target='_blank' and open new Tab or Window using JavaScript (onClick action)
	/// </summary>	
	
	public boolean OpenNewTabOrWindowByJavaScript(String locator, String frames)throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) SwitchToFrames(frames);

		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{
			try
			{
				// ((JavaScriptExecutor)PropertiesAndConstants.Selenium).ExecuteScript("arguments[0].click();", element);
				Thread.sleep(1000);
				PropertiesAndConstants.AdditionalDriverWindowHandle = PropertiesAndConstants.Selenium.getWindowHandle();
				LogFunctions.LogEntry("Open new tab by JavaScript - Pass",true);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail")
				{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
				else PropertiesAndConstants.scriptExecutionResult = "Pass";
				SwitchToDefaultContent(frames);
				PropertiesAndConstants.Selenium.switchTo().window(PropertiesAndConstants.AdditionalDriverWindowHandle);
				WaitForReadyStateComplete();
				return true;
			}
			catch (RangeException outofrangeexc)
			{
				LogFunctions.LogEntry("Cannot open new tab by JavaScript - Fail",false);
				LogFunctions.LogEntry("Reason: " + outofrangeexc.getMessage(), false);
				LogFunctions.LogEntry("Details: " + outofrangeexc, false);
				SwitchToDefaultContent(frames);
				return false;
			}
			catch (Exception exc)
			{
				// Console.ForegroundColor = ConsoleColor.Red;
				LogFunctions.LogEntry("Cannot open new tab - Fail",false);
				LogFunctions.LogEntry("Reason: " + exc.getMessage(), false);
				LogFunctions.LogEntry("Details: " + exc, false); 
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				// Console.ResetColor();
				SwitchToDefaultContent(frames);
				return false;
			}
		}
		else
		{
			LogFunctions.LogEntry("Cannot open new tab (Element for Click not Found) - Fail",false);
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Close Additional Tab or Window (which opened earlier)
	/// </summary>	
	
	public boolean CloseAdditionalTabOrWindow()throws Exception
	{
		//String main = PropertiesAndConstants.Selenium.getWindowHandle();
		//Set<String> whs = PropertiesAndConstants.Selenium.getWindowHandles();
		
		ArrayList<String> main = new ArrayList<String> (PropertiesAndConstants.Selenium.getWindowHandles());
		
		if(main.size() > 1)
		{
		PropertiesAndConstants.Selenium.switchTo().window(main.get(1));
		PropertiesAndConstants.Selenium.close();
		PropertiesAndConstants.Selenium.switchTo().window(main.get(0));
		LogFunctions.LogEntry("Close tab - Pass",false);
		
		if (PropertiesAndConstants.scriptExecutionResult == "Fail")
		{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
		else PropertiesAndConstants.scriptExecutionResult = "Pass";
		return true;
		}
		
		/*
		if (whs.size() > 1)
		{
			//PropertiesAndConstants.Selenium.switchTo().window(PropertiesAndConstants.AdditionalDriverWindowHandle).close();
			//PropertiesAndConstants.Selenium.switchTo().window(PropertiesAndConstants.MainDriverWindowHandle);
		
			whs.remove(main);
            assert whs.size()==1;
            PropertiesAndConstants.Selenium.switchTo().window((String) whs.toArray()[0]);
            PropertiesAndConstants.Selenium.close();
            PropertiesAndConstants.Selenium.switchTo().window(main);
			
			LogFunctions.LogEntry("Close tab - Pass",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}*/
		else
		{
			LogFunctions.LogEntry("Cannot close tab - Fail",false);
			return false;
		} 
		//return true;
	}

	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Close Main Tab or Window
	/// </summary>	
	
	public boolean CloseMainTabOrWindow()throws Exception
	{
		Set<String> whs = PropertiesAndConstants.Selenium.getWindowHandles();
				
		if (whs.size() > 1)
		{
			PropertiesAndConstants.Selenium.switchTo().window(PropertiesAndConstants.MainDriverWindowHandle).close();
			PropertiesAndConstants.Selenium.switchTo().window(PropertiesAndConstants.AdditionalDriverWindowHandle);
			PropertiesAndConstants.MainDriverWindowHandle = PropertiesAndConstants.Selenium.getWindowHandle();
			LogFunctions.LogEntry("Close tab - Pass",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Cannot close tab - Fail",false);
			return false;
		}
	}

	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Instructs the driver to Switch a Main Tab or window.
	/// </summary>	
	
	public boolean SwitchToMainTab()throws Exception
	{
		if (PropertiesAndConstants.MainDriverWindowHandle != null)
		{
			try
			{
				PropertiesAndConstants.Selenium.switchTo().window(PropertiesAndConstants.MainDriverWindowHandle);
				Thread.sleep(500);
				return true;
			}
			catch (Exception switchTabExc)
			{
				LogFunctions.LogEntry("Cannot Switch in a Main Tab - Fail",false);
				LogFunctions.LogEntry("Reason: " + switchTabExc.getMessage(), false);
				LogFunctions.LogEntry("Details: " + switchTabExc, false);
				return false;
			}
		}
		else
		{
			LogFunctions.LogEntry("Cannot Switch in a Main Tab (WindowHandle is Null) - Fail",false);
			return false;
		}
	}

	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Instructs the driver to Switch a Additional (opened later) Tab or Window
	/// </summary>	
	
	public boolean SwitchToAdditionalTab()throws Exception
	{
		if (PropertiesAndConstants.AdditionalDriverWindowHandle != null)
		{
			try
			{
				PropertiesAndConstants.Selenium.switchTo().window(PropertiesAndConstants.AdditionalDriverWindowHandle);
				Thread.sleep(500);
				return true;
			}
			catch (Exception switchTabExc)
			{
				LogFunctions.LogEntry("Cannot Switch in an Additional Tab - Fail",false);
				LogFunctions.LogEntry("Reason: " + switchTabExc.getMessage(), false);
				LogFunctions.LogEntry("Details: " + switchTabExc, false);
				return false;
			}
		}
		else
		{
			LogFunctions.LogEntry("Cannot Switch in a Additional Tab (WindowHandle is Null) - Fail",false);
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Browser Back (Return)
	/// </summary>	
	
	public boolean ReturnToPreviousPage()throws Exception
	{
		try
		{
			PropertiesAndConstants.Selenium.navigate().back();
			Thread.sleep(250);
			WaitForReadyStateComplete();
			LogFunctions.LogEntry("Return to previous page - Pass",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}
		catch(Exception exc)
		{
			LogFunctions.LogEntry("Cannot Return to previous page - Fail",false);
			return false;
		}
	}

	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Browser Refresh (Refresh)
	/// </summary>	
	
	public boolean RefreshPage() throws Exception
	{
		try
		{
			PropertiesAndConstants.Selenium.navigate().refresh();
			WaitForReadyStateComplete();
			LogFunctions.LogEntry("Refresh page - Pass",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}
		catch (WebDriverException wdExc)
		{
			LogFunctions.LogEntry("Cannot Refresh page (WebDriverException) - Fail",false);
			LogFunctions.LogEntry("WebDriverException Details " + wdExc.getMessage(),false);
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Accept Alert if We expected that Alert Appear
	/// </summary>	
	
	public boolean AlertAccept() throws Exception
    {
          Alert alert = null;
          
          Thread.sleep(500);
          int i=0;
             while(i++<5)
          //alertWait.until(d =>
       {
           try
           {
               alert = PropertiesAndConstants.Selenium.switchTo().alert();
               if (alert != null) break;
               
           }
           catch (Exception exc)
           {
               LogFunctions.LogEntry("Cannot Switch to Alert - Fail ", false);
               LogFunctions.LogEntry("Reason: " + exc, false);
               return false;
           }
       };

          if (alert != null)
          {
                Thread.sleep(500);
                PropertiesAndConstants.Selenium.switchTo().alert().accept();
                Thread.sleep(500);
                LogFunctions.LogEntry("Alert was Accepted - Pass",false);
                SwitchToDefaultContent();
                return true;
          }
          else
          {
                LogFunctions.LogEntry("Cannot Accept alert - Fail",false);
                SwitchToDefaultContent();
                return false;
          }
    }

	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : WebDriver Action Focus Element
	/// </summary>	
	
	public boolean MoveToElement(String locator, String frames) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Focus on element :"+locator+" - Fail - Incorrect Locator :"+locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames))
		{
			
			SwitchToDefaultContent(frames);
			String[] framesArray=frames.split("||");
			int x=0;
			LinkedList<String> list=new LinkedList<String>();
			String[] frameArray1=new String[list.size()];
			for(int index=0;index<framesArray.length;index++)
			{
				if(framesArray[index].length()!=0 )
				{
					list.add(framesArray[index]);
					x++;

				}
			}
			for(int j=0;j<list.size();j++)
			{
				frameArray1[j]=list.get(j); 
			}
			//  String  framesArray = frames.Split(new String[] { "||" }, StringSplitOptions.RemoveEmptyEntries);
			for (int i = 0; i < framesArray.length; i++)
			{
				if (FindElement(frameArray1[i]) != null)
				{
					LogFunctions.LogEntry("Frame Element with locator"+frameArray1[i]+" FOUND!!! ", false);
					PropertiesAndConstants.Selenium.switchTo().frame(FindElement(framesArray[i]));

					//  framewait.Until<WebElement>(((d) => PropertiesAndConstants.GetDriver.FindElement(By.TagName("body"))));
				}
				else
				{
					LogFunctions.LogEntry("CANNOT FOUND Frame Element with locator"+frameArray1[i]+" !!! " , false);
					return false;
				}
			}
		}

		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{
			Actions action = new Actions(PropertiesAndConstants.Selenium);
			action.moveToElement(element).build().perform();
			LogFunctions.LogEntry("Focus on element :"+locator+" - Pass",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Cannot find element :"+locator+"- Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}

	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : WebDriver Action Focus Element
	/// </summary>	
	
//	public boolean MoveMouseToElement(String name, String locator, String frames) throws Exception
//	{	
//		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
//		{
//			LogFunctions.LogEntry("Focus on element :"+name+" - Fail - Incorrect Locator :"+ locator,false);
//			PropertiesAndConstants.scriptExecutionResult = "Fail";
//			return false;
//		}
//
//		if (!UtilityFunctions.IsNullOrWhiteSpace(frames))
//		{
//			
//			SwitchToDefaultContent(frames);
//			String[] framesArray=frames.split("||");
//			int x=0;
//			LinkedList<String> list=new LinkedList<String>();
//			String[] frameArray1=new String[list.size()];
//			for(int index=0;index<framesArray.length;index++)
//			{
//				if(framesArray[index].length()!=0 )
//				{
//					list.add(framesArray[index]);
//					x++;
//
//				}
//			}
//			for(int j=0;j<list.size();j++)
//			{
//				frameArray1[j]=list.get(j); 
//			}
//			// var framesArray = frames.Split(new String[] { "||" }, StringSplitOptions.RemoveEmptyEntries);
//			for (int i = 0; i < frameArray1.length; i++)
//			{
//				if (FindElement(framesArray[i]) != null)
//				{
//					LogFunctions.LogEntry("Frame Element with locator"+framesArray[i]+" FOUND!!! " , false);
//					PropertiesAndConstants.Selenium.switchTo().frame(FindElement(framesArray[i]));
//
//					//  framewait.Until<WebElement>(((d) => PropertiesAndConstants.GetDriver.FindElement(By.TagName("body"))));
//				}
//				else
//				{
//					LogFunctions.LogEntry("CANNOT FOUND Frame Element with locator"+framesArray[i]+"!!! " , false);
//					return false;
//				}
//			}
//		}
//			
//		
//		//Actions act = new Actions(PropertiesAndConstants.Selenium);
//		
//		WebElement element = WaitForElementPresent(name, locator);
//			
//	//	act.moveToElement(element);
//				
//		if (element != null)
//		{
//			Locatable hoverItem = (Locatable)element;
//			Mouse mouse = ((HasInputDevices)PropertiesAndConstants.Selenium).getMouse();
//			// mouse.MouseMove(hoverItem.Coordinates);
//			mouse.mouseMove((Coordinates) hoverItem.getLocator());
//			LogFunctions.LogEntry("Move Mouse to element :"+name+" - Pass",false);
//			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
//			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
//			else PropertiesAndConstants.scriptExecutionResult = "Pass";
//			return true;
//		}
//		else
//		{
//			LogFunctions.LogEntry("Cannot find element :"+name+" - Fail",false);
//			PropertiesAndConstants.scriptExecutionResult = "Fail";
//			return false;
//		}
//	}

	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Verify that element present and displayed on the page
	/// </summary>	
		
	public boolean VerifyIsElementPresentAndDisplayed( String locator, String frames) throws Exception
	{
		
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
		
		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) SwitchToFrames(frames);
		
		
		WebElement element = WaitForElementPresent(locator);
				
		if (element != null)
		{
			for (int i = 0; i <= 10; i++)
			{
				if (!element.isDisplayed()) Thread.sleep(500);
				else break;
			}
			
			System.out.println("next step");
			
			if (element.isDisplayed())
			{
				LogFunctions.LogEntry("Element :"+ locator+"present - Pass",false);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail")
				{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
				else PropertiesAndConstants.scriptExecutionResult = "Pass";
				SwitchToDefaultContent(frames);
				return true;
			}
			else
			{	
				LogFunctions.LogEntry("Element :"+locator+" not Displayed - Fail",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				SwitchToDefaultContent(frames);
				return false;
			}
		}
		else
		{	LogFunctions.LogEntry("Element :"+locator+" not found - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			SwitchToDefaultContent(frames);
			return false;
		}
	}

	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Verify that element DOESN'T displayed on the page
	/// </summary>	
	
	public boolean VerifyIsElementNotDisplayed(String locator, String frames) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator :"+locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames)) SwitchToFrames(frames);
		Thread.sleep(2000); // Wait 2 Second Until element Appear
		WebElement element = FindElement(locator);

		if (element != null)
		{
			if (!element.isDisplayed())
			{
				LogFunctions.LogEntry("Element :"+locator+" not displayed - Pass",false);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail")
				{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
				else PropertiesAndConstants.scriptExecutionResult = "Pass";
				return true;
			}
			else
			{
				LogFunctions.LogEntry("Element :"+locator+" displayed - Fail",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return false;
			}
		}
		else
		{
			LogFunctions.LogEntry("Element :"+locator+" not present - Pass",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Verify that Current page URL contains input data
	/// </summary>	
	
	public boolean VerifyIsPageURLContainsText(String inputData) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(inputData))
		{
			LogFunctions.LogEntry("Incorrect Input data :"+ inputData,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
		if (PropertiesAndConstants.Selenium.getCurrentUrl().toLowerCase().contains(inputData.toLowerCase()))
		{
			LogFunctions.LogEntry("Current browser URL Contains: -"+inputData+" - Pass " ,false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Current browser URL: Actual -"+PropertiesAndConstants.Selenium.getCurrentUrl()+"; Expected Contains: -"+inputData+" - Fail " ,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Verify that Current page URL DOES'N contain input data
	/// </summary>	
	
	public boolean VerifyIsPageURLNotContainsText(String inputData) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(inputData))
		{
			LogFunctions.LogEntry("Incorrect Input data :"+ inputData,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
		if (!PropertiesAndConstants.Selenium.getCurrentUrl().toLowerCase().contains(inputData.toLowerCase()))
		{
			LogFunctions.LogEntry("Current browser URL NOT Contains: -"+ inputData+" - Pass ",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Current browser URL: Actual "+ PropertiesAndConstants.Selenium.getCurrentUrl()+"; Expected Not Contains: -"+inputData+" - Fail ",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Verify that Current Browser Title DOESN'T contain input data
	/// </summary>	
	
	public boolean VerifyIsTitleNotContainsText(String inputData) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(inputData))
		{
			LogFunctions.LogEntry("Incorrect Input data :"+inputData,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!PropertiesAndConstants.Selenium.getTitle().toLowerCase().contains(inputData.toLowerCase()))
		{
			LogFunctions.LogEntry("Current browser Title Doesn't Contain: -"+inputData+" - Pass ",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Current browser Title: Actual -"+ PropertiesAndConstants.Selenium.getTitle()+"; Expected Doesn't Contain: -"+inputData+"- Fail ",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}

	

	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Verify is Alert Present (if we Expected)
	/// </summary>	
	
	public boolean VerifyIsAlertPresent() throws Exception
	{
		try
		{
			Alert alert = PropertiesAndConstants.Selenium.switchTo().alert();
			String alertText = alert.getText();
			LogFunctions.LogEntry("Alert with Text :"+alertText+" present - Pass",false);
			return true;
		}
		catch(Exception exc)
		{
			LogFunctions.LogEntry("Alert doesn't present - Fail",false);
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Verify scr attribute for a <img></img> tag in DOM
	/// </summary>	
	
	public boolean VerifyIsImageDisplayed( String locator) throws Exception
	{
		
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{
			if (element.getTagName().equals("img"))
			{
				String srcAttribute = element.getAttribute("src");

				if (srcAttribute != null)
				{ 
					PropertiesAndConstants.Selenium.navigate().to(srcAttribute);
					String driverTitle = PropertiesAndConstants.Selenium.getTitle();
					PropertiesAndConstants.Selenium.navigate().back();

					if (!driverTitle.contains("404") && !driverTitle.contains("not found") && !driverTitle.contains("Runtime Error")
							&& !driverTitle.contains("Problem loading"))
					{
						
						LogFunctions.LogEntry("Image src attribute..."+srcAttribute+".... is Valid - Pass" ,false);
						if (PropertiesAndConstants.scriptExecutionResult == "Fail")
						{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
						else PropertiesAndConstants.scriptExecutionResult = "Pass";
						return true;
					}
					else
					{
						LogFunctions.LogEntry("Image src attribute Incorrect -Image"+srcAttribute+" Not Found - Fail ",false);
						PropertiesAndConstants.scriptExecutionResult = "Fail";
						return false;
					}
				}
				else
				{
					LogFunctions.LogEntry("Image src attribute:"+srcAttribute+" Actual = Null - Fail ",false);
					PropertiesAndConstants.scriptExecutionResult = "Fail";
					return false;
				}
			}
			else
			{
				LogFunctions.LogEntry("Current WebElement is NOT an IMAGE <img> - Fail"+ locator,false);
				LogFunctions.LogEntry("Current element tag is"+ element.getTagName(), false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return false;
			}
		}
		else
		{
			LogFunctions.LogEntry("Cannot Find element with locator :"+locator+" - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Verify that Current Browser Title contains input data
	/// </summary>
	
	public boolean VerifyIsTitleContainsText(String inputData) throws IOException
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(inputData))
		{
			LogFunctions.LogEntry("Incorrect Input data :"+ inputData,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (PropertiesAndConstants.Selenium.getTitle().toLowerCase().contains(inputData.toLowerCase()))
		{
			LogFunctions.LogEntry("Current browser Title Contains: -"+inputData+ "- Pass ",false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else PropertiesAndConstants.scriptExecutionResult = "Pass";
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Current browser Title: Actual -"+PropertiesAndConstants.Selenium.getTitle()+"; Expected Contains: -"+inputData+" - Fail ",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}

	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Check CSS property for element (format -> attrName:value)
	/// </summary>
	
	public boolean VerifyCssAttribute( String locator, String inputdata) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator - Fail",false);
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(inputdata) && !inputdata.contains(":"))
		{
			LogFunctions.LogEntry("Incorrect CSS Value - Fail",false);
			return false;
		}

		String[] cssParameters = inputdata.split(":");
		String cssProperty = cssParameters[0];
		String cssCheckValue = cssParameters[1];
		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{
			String cssValue = element.getCssValue(cssProperty);
			if (!UtilityFunctions.IsNullOrWhiteSpace(cssValue))
			{
				if (cssValue.toLowerCase().contains(cssCheckValue.toLowerCase()))
				{
					LogFunctions.LogEntry("Check Css Attribute"+cssProperty+": Act:"+cssValue+" - Exp Contains:"+cssCheckValue+" - Pass",false);
					if (PropertiesAndConstants.scriptExecutionResult == "Fail")
					{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
					else PropertiesAndConstants.scriptExecutionResult = "Pass";
					return true;
				}
				else
				{
					LogFunctions.LogEntry("Css Attribute"+cssProperty+": Act:"+cssValue+" - Exp Contains:"+cssCheckValue+" - Fail",false);
					PropertiesAndConstants.scriptExecutionResult = "Fail";
					return false;
				}
			}
			else
			{
				LogFunctions.LogEntry("Css attribute"+cssValue+" is Null or Empty - Fail",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return false;
			}
		}
		else
		{
			LogFunctions.LogEntry("Cannot get Css attribute"+cssProperty+" - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Check that attribute for element contains a Value (format -> attrName=value)
	/// </summary>
	
	public boolean VerifyElementAttribute( String locator, String inputData) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (UtilityFunctions.IsNullOrWhiteSpace(inputData) || !inputData.contains("="))
		{
			LogFunctions.LogEntry("Incorrect Input Data :"+ inputData,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		inputData = inputData.trim().replace(" =", "=");
		String[] dataArray=inputData.split("=");
		// var dataArray = inputData.Split(new String[] { "=" }, StringSplitOptions.None);
		String attributeName = dataArray[0];
		String attributeValue = dataArray[1];

		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{
			String currentAttributeValue = element.getAttribute(attributeName);

			if (currentAttributeValue == null)
			{
				LogFunctions.LogEntry("Element"+locator+" Doesn't Contain attribute"+attributeName+ "- Fail",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return false;
			}

			if (currentAttributeValue.contains(attributeValue) ||
					currentAttributeValue.equals(attributeValue))
			{
				LogFunctions.LogEntry("Element"+locator+" attribute"+attributeName+" Contains Value"+attributeValue+" - Pass",false);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail")
				{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
				else PropertiesAndConstants.scriptExecutionResult = "Pass";
				return true;
			}
			else
			{
				LogFunctions.LogEntry("Element"+locator+ "attribute"+attributeName+" Doesn't Contain Value"+attributeValue+" - Fail",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return false;
			}
		}
		else
		{
			LogFunctions.LogEntry("Cannot Find element with locator :"+locator+" - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  Check that attribute for element DOESN'T contain a Value (format -> attrName=value)
	/// </summary>
	
	public boolean VerifyIsElementNotContainsAttribute( String locator, String inputData) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator :"+ locator,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (UtilityFunctions.IsNullOrWhiteSpace(inputData))
		{
			LogFunctions.LogEntry("Incorrect Input Data :"+ inputData,false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		String attributeName = inputData.trim();

		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{

			if (UtilityFunctions.IsNullOrWhiteSpace(element.getAttribute(attributeName)))
			{
				LogFunctions.LogEntry("Element"+locator+" doesn't Contain attribute"+attributeName+"  - Pass",false);
				if (PropertiesAndConstants.scriptExecutionResult == "Fail")
				{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
				else PropertiesAndConstants.scriptExecutionResult = "Pass";
				return true;
			}
			else
			{
				LogFunctions.LogEntry("Element"+locator+" Contains attribute"+attributeName+"  - Fail",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return false;
			}
		}
		else
		{
			LogFunctions.LogEntry("Cannot Find element with locator :"+locator+" - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :  
	/// </summary>
		
	public boolean VerifyAllImagesOnThePage() throws Exception
	{
		if (   ConfigFunctions.getEnvKeyValue("IGNORECHECKALLIMAGESSTEP") == "YES")
		{
			LogFunctions.LogEntry("IGNORED - Disabled in the Config",false);
			PropertiesAndConstants.IsStepSkip = true;
			return true;
		}
		if (!UtilityFunctions.IsNullOrWhiteSpace(PropertiesAndConstants.Selenium.getPageSource()))
		{
			_images.clear();
			_imagesCheckResult.clear();
			String pageUrl = PropertiesAndConstants.Selenium.getCurrentUrl();
			List<WebElement> imageElements = PropertiesAndConstants.Selenium.findElements(By.tagName("img"));
			int counter = 1;
			boolean isStepStatusPass = true;

			if (imageElements != null)
			{
				for (WebElement webElement : imageElements) 
				{
					String srcValue = webElement.getAttribute("src");
					String locationKey = webElement.getLocation().toString().replace("{", "Image_").replace("}", "Number_" + counter).replace('=', '_');
					if (!UtilityFunctions.IsNullOrWhiteSpace(srcValue) && srcValue != "#") _images.put(locationKey, srcValue);
					counter++;

				}

				for (String  image : _images.keySet())
				{

					String key = image;
					String valueSrcUrl = _images.get(image);
					if (valueSrcUrl.contains("gif")) continue; // (FF gif Timeout Exception)

					if (!UtilityFunctions.IsNullOrWhiteSpace(valueSrcUrl))
					{
						PropertiesAndConstants.Selenium.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
						PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
						try
						{
							PropertiesAndConstants.Selenium.navigate().to(valueSrcUrl);
							
							try
							{	
								//WaitForElementPresent("BodyElement","html>body>img");

															
								/*
								PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(05, TimeUnit.SECONDS);
								if(valueSrcUrl.contains("gmsImages"))
								{
									WaitForElementPresent("BodyElement","html>body");
								}
								else{
								WaitForElementPresent("BodyElement","html>body>img");
								}		
								*/		
																			
							}
							catch (WebDriverException wExc)
							{
								LogFunctions.LogEntry("Webdriver Timeout Exception: Wait for Body / Img Element - " + wExc, false);
							}
							//Thread.Sleep(300);
						}
						catch (TimeoutException exc)
						{
							LogFunctions.LogEntry("Timeout in process of  navigate to URL: " + valueSrcUrl, false);
							PropertiesAndConstants.scriptExecutionResult = "Fail";
							isStepStatusPass = false;
							continue;
						}
						String driverTitle = PropertiesAndConstants.Selenium.getTitle();
						//PropertiesAndConstants.GetDriver.Navigate().Back();

						if (!UtilityFunctions.IsNullOrWhiteSpace(driverTitle) && !driverTitle.contains("404") && !driverTitle.contains("not found") && !driverTitle.contains("Runtime Error")
								&& !driverTitle.contains("Problem loading"))
						{
							LogFunctions.LogEntry("Image src attribute"+valueSrcUrl+" for element location"+key+" is Valid - Pass", false);
							if (PropertiesAndConstants.scriptExecutionResult == "Fail")
							{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
							else PropertiesAndConstants.scriptExecutionResult = "Pass";
							continue;
						}
						else
						{
							LogFunctions.LogEntry("Image src attribute Incorrect -Image"+valueSrcUrl+" with location"+key+" Not Found - Fail ", false);
							PropertiesAndConstants.scriptExecutionResult = "Fail";
							isStepStatusPass = false;
							continue;
						}
					}
					else
					{
						LogFunctions.LogEntry("Image src attribute Empty - Image"+valueSrcUrl+" with location"+key+" Not Found - Fail ", false);
						PropertiesAndConstants.scriptExecutionResult = "Fail";
						isStepStatusPass = false;
						continue;
					}
				}

				SeleniumHandler.SetDefaultDriverConfig();

				if (!isStepStatusPass)
				{
					NavigateToUrl(pageUrl);
					PropertiesAndConstants.scriptExecutionResult = "Fail";
					LogFunctions.LogEntry("Page Contains Invalid Images! - Fail ",false);
					return false;
				}
				else
				{
					NavigateToUrl(pageUrl);
					LogFunctions.LogEntry("All Images are Valid - Pass ",false);
					return true;
				}
			}
			else // if Elements Colection
			{
				LogFunctions.LogEntry("Cannot find any element with tag img  - Fail ",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return false;
			}
		}
		else // if Page Source
		{
			LogFunctions.LogEntry("Page Source is Empty  - Fail ",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : 
	/// </summary>
		
	public boolean VerifyAllLinksOnThePage() throws Exception
	{
		_links.clear();
		_linksCheckResult.clear();

		if (!UtilityFunctions.IsNullOrWhiteSpace(PropertiesAndConstants.Selenium.getPageSource()))
		{
			String pageUrl = PropertiesAndConstants.Selenium.getCurrentUrl();
			List<WebElement> linkElements = PropertiesAndConstants.Selenium.findElements(By.tagName("a"));
			int counter = 1;
			boolean isStepStatusPass = true;
			String driverTitle;

			if (linkElements != null)
			{
				for (WebElement element : linkElements)
				{
					String hrefValue = element.getAttribute("href");
					String locationKey = element.getLocation().toString().replace("{", "Link_").replace("}", "Number_" + counter).replace('=', '_');
					if (!UtilityFunctions.IsNullOrWhiteSpace(hrefValue) && hrefValue != "#") _links.put(locationKey, hrefValue);
					counter++;
				}

				counter = 1;

				for(String link : _links.keySet())
				{
					if (counter % 10 == 0)
					{
						Thread.sleep(3000);
					}

					String key = link;
					String valueHrefUrl = _links.get(link);

					try
					{
						PropertiesAndConstants.Selenium.navigate().to(valueHrefUrl);
						try
						{
							WaitForElementPresent("html>body");
						}
						catch (WebDriverException wExc)
						{
							LogFunctions.LogEntry("Webdriver Timeout Exception: Wait for Body Element - " + wExc, false);
						}
						Thread.sleep(500);
						driverTitle = PropertiesAndConstants.Selenium.getTitle();
					}
					catch (WebDriverException exc)
					{
						LogFunctions.LogEntry("Exception in process of navigate to URL: " + valueHrefUrl, false);
						driverTitle = "404";
						SeleniumHandler.CloseSeleniumDriver();
						Thread.sleep(1500);
						SeleniumHandler.SwitchDriver();
						continue;
					}
					catch (Exception exc)
					{
						LogFunctions.LogEntry("Unexpected exception in process of navigate to URL: " + valueHrefUrl, false);
						driverTitle = "404";
						SeleniumHandler.CloseSeleniumDriver();
						Thread.sleep(1500);
						SeleniumHandler.SwitchDriver();
						continue;
					}

					if (PropertiesAndConstants.Selenium.getWindowHandles().size() > 1)
					{
						for (String openedBrouserWindowHandles : PropertiesAndConstants.Selenium.getWindowHandles())
						{
							if (openedBrouserWindowHandles != PropertiesAndConstants.MainDriverWindowHandle)
							{
								PropertiesAndConstants.Selenium.switchTo().window(openedBrouserWindowHandles).close();
								Thread.sleep(250);
							}
						}
						PropertiesAndConstants.Selenium.switchTo().window(PropertiesAndConstants.MainDriverWindowHandle);
					}

					if (!driverTitle.contains("404") && !driverTitle.contains("not found") && !driverTitle.contains("Runtime Error")
							&& !driverTitle.contains("Problem loading") && !driverTitle.contains("Whoops") && !driverTitle.contains("Google Search"))
					{
						LogFunctions.LogEntry("Link href attribute"+valueHrefUrl+" for element location"+key+" is Valid - Pass", false);
						if (PropertiesAndConstants.scriptExecutionResult == "Fail")
						{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
						else PropertiesAndConstants.scriptExecutionResult = "Pass";
						counter++;
						continue;
					}
					else
					{
						LogFunctions.LogEntry("Link Href attribute Incorrect - Source"+valueHrefUrl+" with location"+key+ "Not Found - Fail ", false);
						LogFunctions.LogEntry("Actual Title:"+ driverTitle, false);
						PropertiesAndConstants.scriptExecutionResult = "Fail";
						isStepStatusPass = false;
						counter++;
						continue;
					}
				}

				if (!isStepStatusPass)
				{
					NavigateToUrl(pageUrl);
					PropertiesAndConstants.scriptExecutionResult = "Fail";
					LogFunctions.LogEntry("Page Contains Incorrect Links! - Fail ",false);
					return false;
				}
				else
				{
					NavigateToUrl(pageUrl);
					LogFunctions.LogEntry("All Links are Valid - Pass ",false);
					return true;
				}
			}
			else
			{
				LogFunctions.LogEntry("Cannot find any element with tag a - Fail ",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return false;
			}
		}
		else
		{
			LogFunctions.LogEntry("Page Source is Empty  - Fail ",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
	
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       :   Try to check collection of elements (Images <img>)
	/// </summary>
		
	public boolean VerifyAllImagesInElement(String locator) throws Exception
	{
		if (!UtilityFunctions.IsNullOrWhiteSpace(PropertiesAndConstants.Selenium.getPageSource()))
		{
			_images.clear();
			_imagesCheckResult.clear();
			String pageUrl = PropertiesAndConstants.Selenium.getCurrentUrl();

			List<WebElement> imageElements = FindElements(locator);
			int imageElementsCount = imageElements.size();
			int elementsCount = 0;
			int counter = 1;
			boolean isStepStatusPass = true;
			String driverTitle = null;

			if (imageElements != null)
			{
				for (WebElement element : imageElements)
				{
					String srcValue = element.getAttribute("src");
					String locationKey = element.getLocation().toString().replace("{", "Image_").replace("}", "_Number" + counter).replace('=', '_');
					if (!UtilityFunctions.IsNullOrWhiteSpace(srcValue) && srcValue != "#") _images.put(locationKey, srcValue);
					counter++;
				}

				for (String image : _images.keySet())
				{
					String key = image;
					String valueSrcUrl = _images.get(image);
					elementsCount = _images.size();

					if (!UtilityFunctions.IsNullOrWhiteSpace(valueSrcUrl))
					{
						try
						{
							PropertiesAndConstants.Selenium.navigate().to(valueSrcUrl);
							try
							{
								WaitForElementPresent("html>body>img");
							}
							catch (WebDriverException wExc)
							{
								LogFunctions.LogEntry("Webdriver Timeout Exception: Wait for Body / Img Element - " + wExc, false);
							}
							Thread.sleep(300);
							driverTitle = PropertiesAndConstants.Selenium.getTitle();
						}
						catch (WebDriverException exc)
						{
							LogFunctions.LogEntry("Timeout in process of  navigate to URL: " + valueSrcUrl, false);
							isStepStatusPass = false;
							SeleniumHandler.CloseSeleniumDriver();
							Thread.sleep(1500);
							SeleniumHandler.SwitchDriver();
							continue;
						}

						if (!UtilityFunctions.IsNullOrWhiteSpace(driverTitle) && !driverTitle.contains("404") && !driverTitle.contains("not found") && !driverTitle.contains("Runtime Error")
								&& !driverTitle.contains("Problem loading") && !driverTitle.contains("Google Search") && !driverTitle.contains("Google Search"))
						{
							LogFunctions.LogEntry("Image src attribute"+valueSrcUrl+" for element location"+key+" is Valid - Pass", false);
							if (PropertiesAndConstants.scriptExecutionResult == "Fail")
							{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
							else PropertiesAndConstants.scriptExecutionResult = "Pass";
							continue;
						}
						else
						{
							LogFunctions.LogEntry("Image src attribute Incorrect -Image"+valueSrcUrl+" with location"+key+" Not Found - Fail ", false);
							PropertiesAndConstants.scriptExecutionResult = "Fail";
							isStepStatusPass = false;
							continue;
						}
					}
					else
					{
						LogFunctions.LogEntry("Image src attribute Empty - Image"+valueSrcUrl+" with location"+key+" Not Found - Fail ", false);
						PropertiesAndConstants.scriptExecutionResult = "Fail";
						isStepStatusPass = false;
						continue;
					}
				}

				if (!isStepStatusPass || imageElementsCount != elementsCount)
				{
					NavigateToUrl(pageUrl);
					LogFunctions.LogEntry("Page Contains Invalid Images! Count Exp:"+imageElementsCount+" Act:"+elementsCount+" - Fail ",false);
					return false;
				}
				else
				{
					NavigateToUrl(pageUrl);
					LogFunctions.LogEntry("All Images are Valid. Count Exp:"+imageElementsCount+" Act:"+elementsCount+"  - Pass ",false);
					return true;
				}
			}
			else // if Elements Colection
			{
				LogFunctions.LogEntry("Cannot find any element with tag img  - Fail ",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return false;
			}
		}
		else // if Page Source
		{
			LogFunctions.LogEntry("Page Source is Empty  - Fail ",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
		
		
		/// <summary>
		/// Author            :  Harshal Kakade
		/// Description       :  Web Driver Find elements if element not Unique
		/// </summary>	
	
	private List<WebElement> FindElements(String locator) throws Exception
	{
		List<WebElement> elements = null;

		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator :"+ locator, false);
			return null;
		}

		if ((locator.contains("#") || locator.contains(".") || locator.contains(">") || locator.contains("~") ||
				locator.contains("+") || locator.contains("*") || locator.contains("$") ||
				locator.contains("^") || locator.contains(" ") || locator.contains("[")) && (!locator.contains("/") && !locator.contains("@")))
		{
			try // by CSS
			{
				PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(10000,TimeUnit.MILLISECONDS);
				elements = PropertiesAndConstants.Selenium.findElements(By.cssSelector(locator));
				PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				return elements;
			}
			catch (NoSuchElementException exc) { LogFunctions.LogEntry("FindByCSS - Fail", false); } //{ Utilities.LogEntry("Find Element By CSS - Fail. Reason: " + noElementExc , false); }
			// catch (IllegalLocatorException exc) { } // { Utilities.LogEntry("Find Element By CSS - Fail. Reason: " + illegalLocatorExc, false); }
			//  catch (InvalidSelectorException exc) { } //{ Utilities.LogEntry("Find Element By CSS - Fail. Reason: " + invalidSelectorExc, false); }
			//  catch (InvalidElementStateException) { } // { Utilities.LogEntry("Find Element By CSS - Fail. Reason: " + invalidElementStateExc, false); }
		}

		if ((locator.contains("/") || locator.contains("contains") || locator.contains("text()") || locator.contains("@") || locator.contains("*") || locator.contains("////") ||
				locator.contains("////") || locator.contains("[") || locator.contains("]")) && (!locator.contains("#") && !locator.contains(">")))
		{
			try // by XPATH
			{
				PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(10000,TimeUnit.MILLISECONDS);
				elements = PropertiesAndConstants.Selenium.findElements(By.xpath(locator));
				PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				return elements;
			}
			catch (NoSuchElementException exc) { LogFunctions.LogEntry("FindByXPATH - Fail", false); }//{ Utilities.LogEntry("Find Element By XPATH - Fail. Reason: " + noElementExc, false); }
			// catch (IllegalLocatorException) { } //{ Utilities.LogEntry("Find Element By XPATH - Fail. Reason: " + illegalLocatorExc, false); }
			// catch (InvalidSelectorException) { } //{ Utilities.LogEntry("Find Element By XPATH - Fail. Reason: " + invalidSelectorExc, false); }
			// catch (InvalidElementStateException) { } //{ Utilities.LogEntry("Find Element By XPATH - Fail. Reason: " + invalidElementStateExc, false); }
		}

		if (!locator.contains("#") && !locator.contains(".") && !locator.contains(">") && !locator.contains("(") &&
				!locator.contains(")") && !locator.contains("/") && !locator.contains("@") && !locator.contains("*") &&
				!locator.contains("$") && !locator.contains("^") && !locator.contains(" ") && !locator.contains("+"))
		{
			try // by Id
			{
				PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(3000,TimeUnit.MILLISECONDS);
				elements = PropertiesAndConstants.Selenium.findElements(By.id(locator));
				PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				return elements;
			}
			catch (NoSuchElementException exc) { LogFunctions.LogEntry("FindById - Fail", false); }
			//  catch (IllegalLocatorException) { }
			// catch (InvalidSelectorException) { }
			//  catch (InvalidElementStateException) { }

			try // by ClassName
			{
				PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(3000,TimeUnit.MILLISECONDS);
				elements = PropertiesAndConstants.Selenium.findElements(By.className(locator));
				PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				return elements;
			}
			catch (NoSuchElementException exc) { LogFunctions.LogEntry("FindByClassName - Fail", false); }
			//  catch (IllegalLocatorException) { }
			//   catch (InvalidSelectorException) { }
			//  catch (InvalidElementStateException) { }
		}

		if (!locator.contains("#") && !locator.contains(">") && !locator.contains(".") && !locator.contains("(") &&
				!locator.contains(")") && !locator.contains("/") && !locator.contains("@") && !locator.contains("*") &&
				!locator.contains("$") && !locator.contains("^") || locator.contains(" "))
		{
			try // by LinkText
			{
				PropertiesAndConstants.Selenium.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				elements = PropertiesAndConstants.Selenium.findElements(By.linkText(locator));
				
				return elements;
			}
			catch (Exception exc) { LogFunctions.LogEntry("FindByLinkText - Fail", false); }
			// catch (IllegalLocatorException) { }
			// catch (InvalidSelectorException) { }
			// catch (InvalidElementStateException) { }
		}
		// ***** Need to Add Fail code for Test Result
		return null;
	}

	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Select Element from select node using Text (without Frames)
	/// </summary>	
	
	private boolean SelectElementByText(String name, String locator, String text) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Select item from :"+name+" - Fail - Incorrect Locator :"+ locator,false);
			return false;
		}

		if (UtilityFunctions.IsNullOrWhiteSpace(text))
		{
			LogFunctions.LogEntry("Select item from \"" +name+ "\"- Fail - Incorrect/Null Value for Select",false);
			return false;
		}
		
		

		WebElement selectnode = WaitForElementPresent(locator);

		if (selectnode != null)
		{
			Select select=new Select(selectnode);
			select.selectByVisibleText(text);
			LogFunctions.LogEntry("Select element from: \""+name+"\" - Pass",false);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Cannot select element from : \""+name+"\" - Fail",false);
			return false;
		}
	}

	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Select Element from select node using Text (without Frames)
	/// </summary>	
	
	public boolean VerifyBackgroundImageForElement(String locator) throws Exception
	{
		String backgroundImageAttribute = "EMPTY";
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator - Fail",false);
			return false;
		}

		WebElement element = WaitForElementPresent(locator);

		if (element != null)
		{
			backgroundImageAttribute = element.getCssValue("background-image");
			if (!UtilityFunctions.IsNullOrEmpty(backgroundImageAttribute))
			{
				backgroundImageAttribute = backgroundImageAttribute.replace("url(\"", " ").replace("\")", " ").trim();
				PropertiesAndConstants.Selenium.navigate().to(backgroundImageAttribute);
				String driverTitle = PropertiesAndConstants.Selenium.getTitle();
				PropertiesAndConstants.Selenium.navigate().back();

				if (!driverTitle.contains("404") && !driverTitle.contains("not found") && !driverTitle.contains("Runtime Error")
						&& !driverTitle.contains("Problem loading"))
				{
					LogFunctions.LogEntry("Background Image css attribute"+backgroundImageAttribute+" is Valid - Pass",false);
					if (PropertiesAndConstants.scriptExecutionResult == "Fail")
					{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
					else PropertiesAndConstants.scriptExecutionResult = "Pass";
					return true;
				}
				else
				{
					LogFunctions.LogEntry("Background Image css attribute Incorrect -Image"+backgroundImageAttribute+" Not Found - Fail ",false);
					PropertiesAndConstants.scriptExecutionResult = "Fail";
					return false;
				}
			}
			else
			{
				LogFunctions.LogEntry("Background Image css attribute"+backgroundImageAttribute+" is Null or Empty - Fail",false);
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				return false;
			}
		}
		else
		{
			LogFunctions.LogEntry("Cannot get Css attribute"+backgroundImageAttribute+ "for element"+locator+" with locator"+locator+" - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
	}
	
	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Select Element from select node using Text (without Frames)
	/// </summary>	
	public boolean VerifyAllBackgroundImagesOnThePage()
	{
		throw new NotImplementedException();
	}


	
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Set Css property display:block for Element
	/// </summary>	
		
	@SuppressWarnings("unchecked")
	public boolean ShowHiddenElement( String locator, String frames)throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Incorrect Locator : \""+locator+"\"",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}

		if (!UtilityFunctions.IsNullOrWhiteSpace(frames))
		{
			WebDriverWait framewait = new WebDriverWait(PropertiesAndConstants.Selenium,30);
			SwitchToDefaultContent(frames);
			String[] framesArray = frames.split("||"); 
			int x=0;
			LinkedList<String> list=new LinkedList<String>();
			String[] frameArray1=new String[list.size()];
			for(int index=0;index<framesArray.length;index++)
			{
				if(framesArray[index].length()!=0 )
				{
					list.add(framesArray[index]);
					x++;

				}
			}
			for(int j=0;j<list.size();j++)
			{
				frameArray1[j]=list.get(j); 
			}

			// var framesArray = frames.Split(new String[] { "||" }, StringSplitOptions.RemoveEmptyEntries);
			for (int i = 0; i < frameArray1.length; i++)
			{
				if (FindElement(framesArray[i]) != null)
				{
					LogFunctions.LogEntry("Frame Element with locator"+framesArray[i]+" FOUND!!! ", false);
					PropertiesAndConstants.Selenium.switchTo().frame(FindElement(framesArray[i]));
					//  framewait.Until<WebElement>( PropertiesAndConstants.Selenium.FindElement(By.tagName("body"))));
					framewait.until((Predicate<WebDriver>) PropertiesAndConstants.Selenium.findElement(By.tagName("body")));
				}
				else
				{
					LogFunctions.LogEntry("CANNOT FOUND Frame Element with locator"+framesArray[i]+" !!! ", false);
					return false;
				}
			}
		}
		
		
		WebElement element = WaitForElementPresent( locator);
		
		if (element != null)
		{
			UtilityFunctions.ExecuteScript(String.format("arguments[0].style.display='block';", element));
			LogFunctions.LogEntry("Set 'display' property as 'block' - Pass",false);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Cannot set 'display' property - Fail",false);
			return false;
		}
	}
			
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Context Click (Right Button Mouse Click without entering in Frames)
	/// </summary>	
	
	@SuppressWarnings("unused")
	private boolean RightClickElement(String name, String locator) throws Exception
	{
		if (UtilityFunctions.IsNullOrWhiteSpace(locator))
		{
			LogFunctions.LogEntry("Right Click button : \""+name+"\" - Fail - Incorrect Locator : \""+locator+"\"",false);
			return false;
		}

		WebElement element = WaitForElementPresent( locator);

		if (element != null)
		{
			Actions actionRightClick = new Actions(PropertiesAndConstants.Selenium);
			Actions actionMove = new Actions(PropertiesAndConstants.Selenium);
			actionMove.moveToElement(element).perform();
			actionRightClick.contextClick(element).perform();
			LogFunctions.LogEntry("Right Click button : \""+name+"\" - Pass",false);
			return true;
		}
		else
		{
			LogFunctions.LogEntry("Cannot find element for Right Click - Fail",false);
			return false;
		}

	}
		
	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Verify that page source DOESN'T contain text
	/// </summary>	
	
    public boolean VerifyIsTextNotPresent(String inputData) throws Exception
    {
    	if (UtilityFunctions.IsNullOrWhiteSpace(inputData))
        {
            LogFunctions.LogEntry("Incorrect Input Data : \""+inputData+"\"",false);
            PropertiesAndConstants.scriptExecutionResult = "Fail";
            return false;
        }
        if (!PropertiesAndConstants.Selenium.getPageSource().contains(inputData))
        {
            LogFunctions.LogEntry("Text :"+inputData+" NOT Present : - Pass",false);
            if (PropertiesAndConstants.scriptExecutionResult == "Fail")
            { PropertiesAndConstants.scriptExecutionResult = "Fail"; }
            else PropertiesAndConstants.scriptExecutionResult = "Pass";
            return true;
        }
        else
        {
            LogFunctions.LogEntry("Text :"+inputData+" Present : - Fail",false);
            PropertiesAndConstants.scriptExecutionResult = "Fail";
            return false;
        }
    }
 	
	public static void SetVariableValue(String keyName, String value) throws IOException
    {
        if (!PropertiesAndConstants.varDataDictionary.containsKey(keyName))
        {
       	 PropertiesAndConstants.varDataDictionary.put(keyName, value);

            LogFunctions.LogEntry("Pass - Variable - set with the value - "+keyName+"=> STEP ID : "+value+" ", false);
        }
        else
       	 LogFunctions.LogEntry("Fail - Variable already exist in the list - "+keyName, false);
    }

	public static String GetVariableValue(String keyName) throws IOException
    {
		System.out.println(keyName);
		if (PropertiesAndConstants.varDataDictionary.get(keyName).isEmpty())
		{
        	LogFunctions.LogEntry("Fail - Variable value for "+keyName+ " not found", false);
        	return "";
		}
		else
        {
        	LogFunctions.LogEntry("Pass - Variable value for "+keyName+ " found", false);
        	return PropertiesAndConstants.varDataDictionary.get(keyName);
        }
    }
	
	public <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
		  for (E e : enumClass.getEnumConstants()) {
		    if(e.name().equals(value)) { return true; }
		  }
		  return false;
		}


	/// <summary>
	/// Author            :  Harshal Kakade
	/// Description       : Select Element from select node using Text (without Frames)
	/// </summary>
	
	@SuppressWarnings("finally")
	public void ProcessScriptTemplate(Workbook testScriptPath) throws Exception, IllegalArgumentException 
	{
		
		_result=false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now = new Date(); 
		Sheet testScriptSheet=testScriptPath.getSheet(0);
		
		String resultsToExcel = ConfigFunctions.getEnvKeyValue("RESULTSTOEXCEL");
		int startRow = 0;
		int rowcount_script = testScriptSheet.getRows();
		do{	startRow++;}
		while (!((String)(testScriptSheet.getCell(0,startRow).getContents()) ).equals("StepId") );
		startRow++;
    	int rowcount =startRow;
	    for(int row=startRow;row<=rowcount_script;row++)
	    {
	    	boolean result=((String)(testScriptSheet.getCell(0,row).getContents())).contains("Step");
	    	if(result==true)
	    	{
	    		 rowcount++;
	    	}
	    	else
	    	{
	    		break;
	    	}
	    }
		for (int rowID = startRow; rowID <= rowcount-1; rowID++)
		{
			if (PropertiesAndConstants.FailStepsPerScriptCounter > PropertiesAndConstants.FailStepsCountLimit) break; // Should be Configurable
			
			String keyActionName = (String)(testScriptSheet.getCell(2,rowID).getContents());
			PropertiesAndConstants.ResultString = "No log report";
			if (keyActionName.equals(null)&&keyActionName.equals(""))
			{
				LogFunctions.LogEntry("Processing test script was completed...", false);

				break;
			}
            String stepID = (String)(testScriptSheet.getCell(0,rowID).getContents());
            System.out.println(stepID);
			String objName = (String)(testScriptSheet.getCell(3,rowID).getContents());
			System.out.println("objName"+objName);
			System.out.println("keyAction"+keyActionName);
			String inputData = (String)(testScriptSheet.getCell(4,rowID).getContents());
			if(PropertiesAndConstants.isTestDataFileExist)
			{
				inputData = PropertiesAndConstants.testDataDictionary.get(inputData);
			}
			
			if(inputData.contains("VAR_") && !(keyActionName.trim().equals("GetRandomValue"))&&!(keyActionName.trim().equals("CreateUmbracoNode")))
			{
				inputData = GetVariableValue(inputData);
			}
		//	String additionalParameters = (String)(testScriptSheet.getCell(rowID, 9).getContents()); // Currently not used
			// ***************************************************
			String usrObjName = objName;
	        String actObjName = ORFunctions.GetObjRepositoryKeyValue(usrObjName).trim();
			String frameObj =   ORFunctions.GetObjFrameValue(usrObjName).trim();
			// ***** Additional Verify for Start Page
			PropertiesAndConstants.IsStepSkip = false;
			PropertiesAndConstants.TempScreenshotPath = null;
			PropertiesAndConstants.TempPageSourcePath = null;
			
			
			// ############################# region Switch Case  ##################################################
								
		    LogFunctions.LogEntry("Step ID :--------- "+ stepID,false);
		    if (isInEnum(keyActionName, KeyAction.class))
		    {
		    	
			KeyAction keyActionname = KeyAction.valueOf(keyActionName);
		
			try // Switch Wrapper
			{
				switch (keyActionname)
				{
				case ImportCase:
				{

					_result = importCase(inputData);
					break;
				}
				case NavigateToURL:
				{
					_result = NavigateToUrl(inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath = LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case Click:
				{
					_result = Click(usrObjName);
					Thread.sleep(250); // Templorary Parameter
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case ClickSafeIfPresent:
				{
					_result = ClickSafeIfPresent(usrObjName,  frameObj);
					Thread.sleep(250); // Templorary Parameter
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case JavaScriptClick:
				{
					_result = JavaScriptClick(usrObjName,  frameObj);
					Thread.sleep(250); // Templorary Parameter
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				
				case CheckBoxUnCheck:

                {

                        _result = CheckBoxUnCheck(usrObjName,  frameObj);

                        if (!_result)

                        {

                                PropertiesAndConstants.IsTestExecutionPassed = false;

                                try { PropertiesAndConstants.TempScreenshotPath =   LogFunctions.TakeScreenshot(keyActionName); }

                                finally { break; }

                        }

                        break;

                }

				case CheckBoxCheck:
				{
					_result = CheckBoxCheck(usrObjName, frameObj);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =   LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}

				case DoubleClickElement:
				{
					_result = DoubleClickElement(usrObjName,  frameObj);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =    LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case RightClickElement:
				{
					_result = RightClickElement(usrObjName, frameObj);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =    LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case TypeText:
				{
					_result = InputTextElement(usrObjName, frameObj, inputData);
					//Thread.Sleep(250);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =   LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case SelectElementByText:
				{
					_result=SelectElementByText(usrObjName, frameObj, inputData);
					if(_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath = LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}    

				case OpenNewTab:
				{
					_result = OpenNewTabOrWindow(usrObjName, actObjName, frameObj);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case SwitchToNewWindow:
				{
					_result = SwitchToNewWindow();
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
			
				case OpenNewTabOrWindowByJavaScript:
				{
					_result = OpenNewTabOrWindowByJavaScript(usrObjName,  frameObj);
					if (_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =    LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case CloseAdditionalTab:
				{
					_result = CloseAdditionalTabOrWindow();
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =   LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}


				case CloseMainTab:
				{
					_result = CloseMainTabOrWindow();
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =    LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}


				case SwitchToMainTab:
				{
					_result = SwitchToMainTab();
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =   LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}


				case SwitchToAdditionalTab:
				{
					_result = SwitchToAdditionalTab();
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =    LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case ReturnToPreviousPage:
				{
					_result = ReturnToPreviousPage();
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =    LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;

				}
				case RefreshPage:
				{
					_result = RefreshPage();
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath = LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case AlertAccept:
				{
					_result = AlertAccept();
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case ElementMouseOver:
				{
					//result = driveractions.
							if (!_result)
							{
								PropertiesAndConstants.IsTestExecutionPassed = false;
								try { PropertiesAndConstants.TempScreenshotPath =    LogFunctions.TakeScreenshot(keyActionName); }
								finally { break; }
							}
					break;
				}
				case MoveToElement:
				{
					_result =MoveToElement(usrObjName, frameObj);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
//				case MoveMouseToElement:
//				{
//					_result=MoveMouseToElement(usrObjName, actObjName, frameObj);
//					if (!_result)
//					{
//						PropertiesAndConstants.IsTestExecutionPassed = false;
//						try { PropertiesAndConstants.TempScreenshotPath = LogFunctions.TakeScreenshot(keyActionName); }
//						finally { break; }
//					}
//					break;
//				}
//

				case VerifyIsElementPresentAndDisplayed:
				{
					_result=VerifyIsElementPresentAndDisplayed(usrObjName,  frameObj);

					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { 
							PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); 
						}
						finally 
						{ 
							break; 
						}
					}
					break;
				}

				case VerifyIsURLContainsText:
				{
					_result = VerifyIsPageURLContainsText(inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					break;
				}
				case VerifyIsURLNotContainsText:
				{
					_result=VerifyIsPageURLNotContainsText(inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					break;
				}
				case VerifyIsTitleContainsText:
				{

					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					break;
				}
				case VerifyIsTitleNotContainsText:
				{
					VerifyIsTitleNotContainsText(inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					break;
				}
				case VerifyIsAlertPresent:
				{
					_result=VerifyIsAlertPresent();
					{
						if (!_result)
						{
							PropertiesAndConstants.IsTestExecutionPassed = false;
							try { PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); }
							catch(Exception exc)
							{ break; }
						}
						break;
					}


				}

				case VerifyIsImageDisplayed:
				{
					_result=VerifyIsImageDisplayed(objName);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case VerifyElementAttribute:
				{
					_result=VerifyElementAttribute(objName,  frameObj);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					break;
				}
				case VerifyCssAttribute:
				{
					_result=VerifyCssAttribute(objName, inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					break;
				}
				case VerifyIsElementNotContainsAttribute:
				{
					_result=VerifyIsElementNotContainsAttribute(objName, inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					break;
				}

				case VerifyAllImagesOnThePage:
				{    _result=VerifyAllImagesOnThePage();
					LogFunctions.LogEntry("Start Checking All Images on the Page...", false);
					if (ConfigFunctions.getEnvKeyValue("BROWSER") == "IE")
					{
						_result = true;
						PropertiesAndConstants.IsStepSkip = true;
						PropertiesAndConstants.ResultString = "Not supported for IE - Pass";
								break;
					}

					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					LogFunctions.LogEntry("Checking All Images on the Page - Completed", false);
					break;

				}
				case VerifyAllLinksOnThePage:
				{
					{
						_result=VerifyAllLinksOnThePage();
						LogFunctions.LogEntry("Start Checking All Links on the Page...", false);
						if (ConfigFunctions.getEnvKeyValue("BROWSER") == "IE")
						{
							_result = true;
							PropertiesAndConstants.IsStepSkip = true;
							PropertiesAndConstants.ResultString = "Not supported for IE - Pass";
									break;
						}

						if (!_result)
						{
							PropertiesAndConstants.IsTestExecutionPassed = false;
						}
						LogFunctions.LogEntry("Checking All Links on the Page - Completed", false);
						break;
					}

				}
				case VerifyAllImagesInElement:
				{
					_result=VerifyAllImagesInElement(objName);
					LogFunctions.LogEntry("Start Checking Images Collection...", false);
					if (ConfigFunctions.getEnvKeyValue("BROWSER") == "IE")
					{
						_result = true;
						PropertiesAndConstants.IsStepSkip = true;
						PropertiesAndConstants.ResultString = "Not supported for IE - Pass";
						break;
					}

					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					LogFunctions.LogEntry("Checking Images Collection - Completed", false);
					break;
				}
				case VerifyAllLinksButtonsInElement:
				{
					LogFunctions.LogEntry("Start Checking Links/Buttons Collection...", false);
					if (ConfigFunctions.getEnvKeyValue("BROWSER") == "IE")
					{
						_result = true;
						PropertiesAndConstants.IsStepSkip = true;
						PropertiesAndConstants.ResultString = "Not supported for IE - Pass";
						break;
					}

					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					LogFunctions.LogEntry("Checking Links/Buttons Collection - Completed", false);
					break;
				}
				case VerifyBackgroundImageForElement:
				{
					_result=VerifyBackgroundImageForElement(objName);
					break;
				}
				case VerifyAllBackgroundImagesOnThePage:
				{
					_result=VerifyAllBackgroundImagesOnThePage();
					break;
				}
				case VerifyIsElementNotDisplayed:
				{

					_result = VerifyIsElementNotDisplayed(usrObjName,  frameObj);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =    LogFunctions.TakeScreenshot(keyActionName); }
						catch(Exception exc) 
						{  

							break;
						}
					}
					break;

				}
				case VerifyIsTextPresent:
				{ 
					_result=VerifyIsTextPresent(inputData);
					if(_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try 
						{             		   
							String source = PropertiesAndConstants.Selenium.getPageSource();
							

							PropertiesAndConstants.TempPageSourcePath = PropertiesAndConstants.TempDirectoryPath + String.format("PageSource_" + "{0}_{1:HH-mm-ss}",
							PropertiesAndConstants.TestScriptName, dateFormat.format(now)) + ".txt";

							//StreamWriter sourceFile = new StreamWriter(PropertiesAndConstants.TempPageSourcePath, true);
							PrintWriter sourceFile = new PrintWriter(new FileOutputStream(new File(PropertiesAndConstants.TempPageSourcePath), true));
							  
                                  sourceFile.write(source.toString());
                                  sourceFile.close();
                              //    sourceFile.Dispose();
                             break;

						}
						catch (Exception exc) 
						{
							LogFunctions.LogEntry("Cannot create a PageSourse text file.", false);
							LogFunctions.LogEntry("Reason: " + exc.getMessage(), false);
							LogFunctions.LogEntry("Details: " + exc, false);
							break;
						}
					}
					break;
				}
				  case VerifyIsTextNotPresent:
				  {
					 _result=VerifyIsTextNotPresent(inputData);
                      if (!_result)
                      {
                          PropertiesAndConstants.IsTestExecutionPassed = false;
                          try
                          {
                              String source = PropertiesAndConstants.Selenium.getPageSource();
                              PropertiesAndConstants.TempPageSourcePath = PropertiesAndConstants.TempDirectoryPath + String.format("PageSource_" + "{0}_{1:HH-mm-ss}",
                              PropertiesAndConstants.TestScriptName,dateFormat.format(now) ) + ".txt";
                             // using (StreamWriter sourceFile = new StreamWriter(PropertiesAndConstants.TempPageSourcePath, true))
                          	 PrintWriter sourceFile = new PrintWriter(new FileOutputStream(new File(PropertiesAndConstants.TempPageSourcePath), true));
                                  sourceFile.write(source.toString());
                                  sourceFile.close();
                               //   sourceFile.Dispose();
                              break;
                          }
                          catch (Exception exc)
                          {
                              LogFunctions.LogEntry("Cannot create a PageSourse text file.", false);
                              LogFunctions.LogEntry("Reason: " + exc.getMessage(), false);
                              LogFunctions.LogEntry("Details: " + exc, false);
                              break;
                          }

                      }
                      break;
				  }

				// ***** Verification Methods: *****
				//  #endregion ***** Verification Methods: *****

				case AddCookie:
				{
					_result = AddCookie(inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					break;
				}

				case ClearAllCookies:
				{
					_result = ClearAllCookies();
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					break;
				}
				case ShowHiddenElement:
				{
					_result = ShowHiddenElement(objName,  frameObj);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath = LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case RestartBrowser:
				{
					_result = RestartBrowser(inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
					}
					break;
				}
				case GetRandomValue:
				{
					_result = GetRandomValue(inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =   LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				case VerifyIsElementContainsText:
				{
					_result = VerifyIsElementContainsText(objName,  frameObj, inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =   LogFunctions.TakeScreenshot(keyActionName); }
						finally { break; }
					}
					break;
				}
				
				// Key Actions for Mobile			
				case MTClick:
				{
					Thread.sleep(1000);
					_result = MTClick(usrObjName);
					Thread.sleep(25000); // Templorary Parameter
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =LogFunctions.TakeScreenshot(keyActionName); }
						finally {break;}
					}
					break;
				}
				case MTClickLink:
				{
					Thread.sleep(1000);
					_result = MTClickLink(usrObjName);
					Thread.sleep(25000); // Templorary Parameter
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =LogFunctions.TakeScreenshot(keyActionName); }
						finally {break;}
					}
					break;
				}
				
				case MTNavigateToURL:
				{
					Thread.sleep(1000);
					_result = MTNavigateToUrl(inputData);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath = LogFunctions.TakeScreenshot(keyActionName); }
						finally {break;}
					}
					break;
				}
				
				case MTTypeText:
				{
					Thread.sleep(1000);
					_result = MTInputTextElement(usrObjName, inputData);
					//Thread.Sleep(250);
					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { PropertiesAndConstants.TempScreenshotPath =   LogFunctions.TakeScreenshot(keyActionName); }
						finally {break;}
					}
					break;
				}

				case MTVerifyIsElementPresentAndDisplayed:
				{
					 Thread.sleep(1000);
					_result=MTVerifyIsElementPresentAndDisplayed(usrObjName);

					if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { 
							PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); 
						}
						finally 
						{break;}
					}
					break;
				}
			  case MTVerifyIsTextPresent:
			  {
				  Thread.sleep(10000);
				  _result=MTVerifyIsTextPresent(usrObjName, inputData);
				  if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { 
							PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); 
						}
						finally 
						{break;}
					}
					break;
				  
			  }
			  
			  case MTPreviousPage:
			  {
				  Thread.sleep(10000);
				  _result=MTPreviousPage();
				  if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { 
							PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); 
						}
						finally 
						{break;}
					}
					break;
				  
			  }
			  
			  case MTWait:
			  {
				  Thread.sleep(10000);
				  _result=MTWait();
				  if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { 
							PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); 
						}
						finally 
						{break;}
					}
					break;
				  
			  }
			  
			  case  MTItemSelector:
			  {
				  Thread.sleep(10000);
				  _result= MTItemSelector(usrObjName, inputData);
				  if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { 
							PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); 
						}
						finally 
						{break;}
					}
					break;
				  
			  }
			  case  MTVerifyErrorMessage:
			  {
				  Thread.sleep(10000);
				  _result= MTVerifyErrorMessage(usrObjName, inputData);
				  if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { 
							PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); 
						}
						finally 
						{break;}
					}
					break;
				  
			  }
			  
			  case  MTVerifyIsImageDisplayed:
			  {
				  Thread.sleep(10000);
				  _result= MTVerifyIsImageDisplayed(usrObjName);
				  if (!_result)
					{
						PropertiesAndConstants.IsTestExecutionPassed = false;
						try { 
							PropertiesAndConstants.TempScreenshotPath =  LogFunctions.TakeScreenshot(keyActionName); 
						}
						finally 
						{break;}
					}
					break;
				  
			  }
			  			  
			  default:
				{
					_result = false;
					LogFunctions.LogEntry("Provide the right KeyAction and re-execute the script. The given KeyAciton \""+keyActionName+"\", doesn't exists. Refere the help for more information",false);
							PropertiesAndConstants.IsTestExecutionPassed = false;
							break;
				}
				}
			}
			catch(TimeoutException wdtexc)
			{
				//  Console.ForegroundColor = ConsoleColor.Red;
				LogFunctions.LogEntry("Exception from KeyAction (WebDriverTimeoutException)",false);
				LogFunctions.LogEntry("Reason: " + wdtexc.getMessage(), false);
				LogFunctions.LogEntry("Details: " + wdtexc, false);
				//  Console.ResetColor();
				_result = false;
				PropertiesAndConstants.IsTestExecutionPassed = false;
				PropertiesAndConstants.scriptExecutionResult = "Fail";
			}
			catch (WebDriverException wdexc)
			{
				// Console.ForegroundColor = ConsoleColor.Red;
				LogFunctions.LogEntry("Exception from KeyAction (WebDriverException)",false);
				LogFunctions.LogEntry("Reason: " + wdexc.getMessage(), false);
				LogFunctions.LogEntry("Details: " + wdexc, false);
				//Console.ResetColor();
				_result = false;
				PropertiesAndConstants.IsTestExecutionPassed = false;
				PropertiesAndConstants.scriptExecutionResult = "Fail";
			}
			catch(IllegalArgumentException excep)
			{
				LogFunctions.LogEntry("Exception from KeyAction "+ keyActionName,false);
				LogFunctions.LogEntry("Reason: " + excep.getMessage(), false);
				LogFunctions.LogEntry("Details: " + excep, false);
				//  Console.ResetColor();
				_result = false;
				PropertiesAndConstants.IsTestExecutionPassed = false;
				PropertiesAndConstants.scriptExecutionResult = "Fail";
			}
			//  #endregion
		    }
		    else
		    {
		    	System.out.println("stepid:"+stepID);
		    	System.out.println("objName:"+objName);
		    	System.out.println("dontknow:"+(String)(testScriptSheet.getCell(0,rowID).getContents()));
		    	System.out.println("inputdata:"+inputData);
		    	LogFunctions.LogEntry("Exception from KeyAction: "+ keyActionName,false);
	
				//  Console.ResetColor();
				_result = false;
				PropertiesAndConstants.IsTestExecutionPassed = false;
				PropertiesAndConstants.scriptExecutionResult = "Fail";
		    }

			//PropertiesAndConstants.GetDriver.SwitchTo().DefaultContent();

			if (!_result && !keyActionName.contains("Verify") && !keyActionName.contains("Cookie") &&
					!keyActionName.contains("SafeLink")) // Add for Verify
			{
				PropertiesAndConstants.IsTestExecutionPassed = false;
				PropertiesAndConstants.scriptExecutionResult = "Fail";
				UtilityFunctions.CaptureExecutionResults(testScriptPath, testScriptSheet, rowID, _result, resultsToExcel);

				break;
			}
			    else   UtilityFunctions.CaptureExecutionResults(testScriptPath, testScriptSheet, rowID, _result, resultsToExcel);
		   }
	  }
	
	/*
	 Author            :  Harshal Kakade
	 Description       : This is to Navigate to given URL
	 */	
	public static boolean MTNavigateToUrl(String url) throws Exception
	{
		System.out.println("In the method MTNavigateToURL" +url);
		if (url.equals(null)||url.equals(""))
		{
			LogFunctions.LogEntry("Navigate to URL'"+url+"' - Fail", false);
			LogFunctions.LogEntry("Incorrect URL:'"+ url+"' in Value field in TestScript file.", false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else { PropertiesAndConstants.scriptExecutionResult = "Pass"; }
			return false;
		}
		try
		{
			PropertiesAndConstants.mt.get(url);
			LogFunctions.LogEntry("Navigate to URL'"+url+"' - Pass", false);
			return true;
		}
		catch (Exception e)
		{
			LogFunctions.LogEntry("Navigate to URL'"+url+"' - Fail", false);
			if (PropertiesAndConstants.scriptExecutionResult == "Fail")
			{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
			else { PropertiesAndConstants.scriptExecutionResult = "Pass"; }
			return false;
		}
		
	}
	
		
	/*
	 Author            : Harshal Kakade
	 Description       : This is to check whether Image displayed on page or not.
	 */
	private boolean MTVerifyIsImageDisplayed(String locator) throws Exception
	{
		if (locator.equals(null)||locator.equals(""))
		{
			LogFunctions.LogEntry("Check for image :"+locator+" - Fail - Incorrect Locator :"+locator, false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
		
		try 
		{
			WebElement ele = MTFindElement(locator);
			ele.isDisplayed();			
			LogFunctions.LogEntry("Check for image :"+locator+" - Pass", false);
			return true;
		}
		catch(Exception clickException)
		{
			LogFunctions.LogEntry("Check for image :"+locator+" - Fail", false);
			LogFunctions.LogEntry("Details: " + clickException, false);
			return false;
		}
	}
	
	/*
	 Author            : Harshal Kakade
	 Description       : This is to click on a particular object/element/button.
	 */
	private boolean MTClick(String locator) throws Exception
	{
		if (locator.equals(null)||locator.equals(""))
		{
			LogFunctions.LogEntry("Click on :"+locator+" - Fail - Incorrect Locator :"+locator, false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
		
		try 
		{
			WebElement clickEle = MTFindElement(locator);
			MTWaitForElementPresent(locator);
			clickEle.click();
			LogFunctions.LogEntry("Click on :"+locator+" - Pass", false);
			return true;
		}
		catch(Exception clickException)
		{
			LogFunctions.LogEntry("Click on :"+locator+" - Fail", false);
			LogFunctions.LogEntry("Details: " + clickException, false);
			return false;
		}
	}
	
	/*
	 Author            : Harshal Kakade
	 Description       : This is to click on a particular Link.
	 */
	private boolean MTClickLink(String locator) throws Exception
	{
		if (locator.equals(null)||locator.equals(""))
		{
			LogFunctions.LogEntry("Click on Link:"+locator+" - Fail - Incorrect Locator :"+locator, false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
		
		try 
		{
			MTWaitForElementPresent(locator);
			PropertiesAndConstants.mt.findElement(By.linkText(locator));
			LogFunctions.LogEntry("Click on Link:"+locator+" - Pass", false);
			return true;
		}
		catch(Exception clickLinkException)
		{
			LogFunctions.LogEntry("Click on Link:"+locator+" - Fail", false);
			LogFunctions.LogEntry("Details: " + clickLinkException, false);
			return false;
		}

		
	}
	
	/*
	 Author            : Harshal Kakade
	 Description       : This is to enter text in text field.
	 */	
	private boolean MTInputTextElement(String locator, String text)throws Exception
	{
		if (locator.equals(null)||locator.equals(""))
		{
			LogFunctions.LogEntry("Type text into :"+locator+" - Fail - Incorrect Locator :"+locator, false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
		
		try
		{
			MTWaitForElementPresent(locator);
			WebElement inputText = MTFindElement(locator);
			inputText.clear();
			inputText.sendKeys(text);
			Thread.sleep(250);
			LogFunctions.LogEntry("Enter text into :"+locator+" - Pass" ,false);
			return true;
		}
		catch(Exception typeTextException)
		{
			LogFunctions.LogEntry("Enter text into :"+ locator+" - Fail",false);
			LogFunctions.LogEntry("Details: " + typeTextException, false);
			return false;
		}
		
	}
	
	/*
	 Author            : Harshal Kakade
	 Description       : This is to check whether particular object/element is present or not.
	 */	
	
	public boolean MTVerifyIsElementPresentAndDisplayed(String locator) throws Exception
	{
		if (locator.equals(null)||locator.equals(""))
		{
			LogFunctions.LogEntry("Wait for "+locator+" - Fail - Incorrect Locator :"+locator, false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
		
		try{
		Thread.sleep(1000);	
		WebElement ele1 = MTFindElement(locator);
		ele1.isDisplayed();
		LogFunctions.LogEntry("Element :'"+ locator+"' is present - Pass",false);
		if (PropertiesAndConstants.scriptExecutionResult == "Fail")
		{ PropertiesAndConstants.scriptExecutionResult = "Fail"; }
		else PropertiesAndConstants.scriptExecutionResult = "Pass";
		
		return true;
		}
		catch(ElementNotFoundException eleNotFound)
		{
			LogFunctions.LogEntry("Element :'"+locator+"' not Found - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			
			return false;
		}
		catch(ElementNotVisibleException eleNotVisible)
		{
			LogFunctions.LogEntry("Element :'"+locator+"' not Visible - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			
			return false;
		}
		
		catch(Exception verifyEleExc)
		{
			LogFunctions.LogEntry("Element :'"+locator+"' not found - Fail",false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			
			return false;
		}
	}
	
	/*
	 Author            : Harshal Kakade
	 Description       : This is to check for a text is present or not.
	 */	
	public boolean MTVerifyIsTextPresent(String locator, String text) throws Exception
	{
		if (locator.equals(null)||locator.equals(""))
		{
			LogFunctions.LogEntry("Verify is text '"+text+"' present - Fail - Incorrect Locator :"+locator, false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
		
		try
		{
			MTWaitForElementPresent(locator);
			WebElement textEle = MTFindElement(locator);
			String text1=textEle.getText();
			text=text.trim();
			text1=text1.trim();
			if(text.equals(text1))
			{
				Thread.sleep(250);
				LogFunctions.LogEntry("Verify is text '"+text+"' present - Pass" ,false);
				return true;
			}
			else
			{
				Thread.sleep(250);
				LogFunctions.LogEntry("Verify is text '"+text+"' present - Fail",false);
				return false;
			}
			
			
		}
		catch(Exception verifyTextPresent)
		{
			LogFunctions.LogEntry("Enter text into :'"+text+"' - Fail",false);
			LogFunctions.LogEntry("Details: " + verifyTextPresent, false);
			return false;
		}
	}
	
	/*
	 Author            : Harshal Kakade
	 Description       : This is to take browser page back.
	 */	
	public boolean MTPreviousPage() throws Exception
	{
		
		try
		{
			PropertiesAndConstants.mt.navigate().back();
			LogFunctions.LogEntry("Browser page Back : - Pass",false);
			return true;
			
		}
		catch(Exception previousPage)
		{
			LogFunctions.LogEntry("Browser page Back : - Fail",false);
			LogFunctions.LogEntry("Details: " + previousPage, false);
			return false;
		}
	}
	
	/*
	 Author            : Harshal Kakade
	 Description       : Hard wait of 10 seconds .
	 */	
	public boolean MTWait() throws Exception
	{
		
		try
		{
			Thread.sleep(10000);
			LogFunctions.LogEntry("Thread wait for 10 Sec : - Pass",false);
			return true;
			
		}
		catch(Exception wait)
		{
			LogFunctions.LogEntry("Thread wait for 10 Sec : - Fail",false);
			LogFunctions.LogEntry("Details: " + wait, false);
			return false;
		}
	}
	
	/*
	 Author            : Harshal Kakade
	 Description       : To Select particular Item/Value from Drop Down Box.
	 */	
	public boolean MTItemSelector( String locator, String text)
	{
         try{
		if (locator.equals(null)||locator.equals(""))
		{
			LogFunctions.LogEntry("Item Select :"+locator+" - Fail - Incorrect Locator :"+locator, false);
			PropertiesAndConstants.scriptExecutionResult = "Fail";
			return false;
		}
         }
         catch(Exception excel)
         {
        	 
         }
		try
		{
			Thread.sleep(250);
			WebElement ele = MTFindElement(locator);
			Select sel = new Select(ele);
			sel.selectByVisibleText(text);
			LogFunctions.LogEntry("Item Select :"+locator+" - Pass" ,false);
			return true;
		}
		catch(Exception itemSelectException)
		{
			try {
				LogFunctions.LogEntry("Item Select :"+ locator+" - Fail",false);
				LogFunctions.LogEntry("Details: " + itemSelectException, false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return false;
		}
		
	}
	
	/*
	 Author            : Harshal Kakade
	 Description       : To verify proper error message displayed on page.
	 */	
	public boolean MTVerifyErrorMessage(String locator, String text)
	{
        try
        {
        	if (locator.equals(null)||locator.equals(""))
        	{
        		LogFunctions.LogEntry("Verify Message :'"+locator+"' - Fail - Incorrect Locator :"+locator, false);
        		PropertiesAndConstants.scriptExecutionResult = "Fail";
        		return false;
        	}
        }
        catch(Exception excel)
        	{
       	 
        	}
	
		try
		{
			Thread.sleep(250);
			WebElement ele = MTFindElement(locator);
			if(text.equals(ele.getText()))
			{
				LogFunctions.LogEntry("Verify Message :'"+text+"' - Pass" ,false);
				return true;
			}
				else
				{
					LogFunctions.LogEntry("Verify Message :'"+text+"' - Fail" ,false);
					return false;
				}
		}
		catch(Exception itemSelectException)
		{
			try {
				LogFunctions.LogEntry("Verify Message :'"+ text+"' - Fail",false);
				LogFunctions.LogEntry("Details: " + itemSelectException, false);
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return false;
			
		}
		
	}	
}
