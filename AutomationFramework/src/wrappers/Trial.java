package wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Trial {
	static int a;
	public void sample()
	{
		WebDriver driver=new FirefoxDriver();
		driver.findElement(By.tagName("input"));
		driver.findElement(By.cssSelector("#save"));
	}

}
