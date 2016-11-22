import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	
	static WebDriver driver;
	
	public static void main(String[] args) throws InterruptedException{
	System.setProperty("webdriver.gecko.driver", "geckodriver");
	driver = new FirefoxDriver();
	driver.get("http://www.instagram.com");
		
	WebDriverWait wait = new WebDriverWait(driver, 40);
	
	By loginObject = By.linkText("Log in");
	wait.until(ExpectedConditions.elementToBeClickable(loginObject));
	driver.findElement(loginObject).click();
	
	driver.findElement(By.name("username")).sendKeys("enterusername here");
	driver.findElement(By.name("password")).sendKeys("enter password here");;

	}
}
