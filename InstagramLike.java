import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InstagramLike {

	static WebDriver driver;
	static WebDriverWait wait;
	static Actions action;
	
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "chromedriver");

		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 40);
		action = new Actions(driver);

		
		setup();
		loginToInstagram();
		
		//TODO change this thread.sleep to instead wait for page to load
		Thread.sleep(5000);
	
		likePictures(getUsers());		
	}
	
	private static void likePictures(ArrayList<String> users) {
		List<WebElement> articlesList = driver.findElements(By.tagName("article"));
		if (articlesList.isEmpty()) {
			exitWithErrorMessage("No recent feed found, follow some users before using this bot.");
			return;
		}
		for (WebElement element: articlesList) {
			String username = element.findElements(By.tagName("a")).get(1).getText();// the second index of the <a> tag elements is the username
			if (users.contains(username))
				action.moveToElement(element).doubleClick().build().perform();
		}
	}
	
	private static ArrayList<String> getUsers() {
		ArrayList<String> users = new ArrayList<>();
		try {
			Scanner scanFile = new Scanner(new File("users"));
			
			while (scanFile.hasNextLine())
				users.add(scanFile.nextLine());
			scanFile.close();		
		} catch (FileNotFoundException e) {
			exitWithErrorMessage("File not found... please add a users.txt file in the project directory.");
		}
		return users;
	}
	
	
	private static void setup() { 
		driver.get("http://www.instagram.com");
		
		//opening login page since no specific url for instagram login
		By loginAnchor = By.linkText("Log in");
		wait.until(ExpectedConditions.elementToBeClickable(loginAnchor));
        action.doubleClick(driver.findElement(loginAnchor)).build().perform();
	}
	
	private static void loginToInstagram() {
		String username = null, password = null;
		
		try {
			Scanner fileScan = new Scanner(new File("credentials"));
			System.out.println("Retrieving credentials from credentials.txt");
			username = fileScan.nextLine().substring(10);
			password = fileScan.nextLine().substring(10);
			fileScan.close();
		} catch (FileNotFoundException e) {
			exitWithErrorMessage("File not found... please add a credentials.txt file in the project directory.");
			return;
		}
		
		if (validCredentials(username, password) == false) {//TODO add check to see if credentials on instagram worked
			exitWithErrorMessage("Error occured in retrieiving username and password. Make sure that credentials.txt is correct.");
			return;
		}
		
		
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		
		action.click(driver.findElement(By.xpath("//button[contains(.,'Log in')]"))).build().perform();

	}
	
	private static boolean validCredentials(String username, String password) {
		if (username != null && password != null) {
			// an arbitrary check to just make sure the username and password "seem" valid, this does not check if 
			// the credentials actually work on instagram
			if (username.length() > 3 && password.length() > 3) {
				return true;
			}
		}
		return false;
	}
	
	public static void exitWithErrorMessage(String message) {
		System.err.println(message);
		System.exit(1);
	}
}
