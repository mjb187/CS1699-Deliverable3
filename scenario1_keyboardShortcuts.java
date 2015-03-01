//Code written by Mike Byrne
//	mjb187@pitt.edu

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * SCENARIO
 * 
 * As an unregisted user,
 * I want to be prompted to login or sign up when trying to interact with parts of the site
 * so that I can track my activity and compare to the community.
 *
*/

// IMGUR TEST ACCOUNT DETAILS
// U: CS1699testaccount
// P: laboon

public class scenario1_keyboardShortcuts 
{
	//shared driver object
	WebDriver driver;
	
	@Before
	//setup web driver
	public void initialize()
	{
		//setup Firefox driver and implict waits
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		//sign in
		driver.get("http://imgur.com/signin");
		driver.findElement(By.cssSelector("input#username")).click();
		driver.findElement(By.cssSelector("input#username")).sendKeys("CS1699testaccount");
		driver.findElement(By.cssSelector("p.password > input")).click();
		driver.findElement(By.cssSelector("p.password > input")).sendKeys("laboon\n");
	}
	
	@After
	//close web driver
	public void uninitialize()
	{
		//close Firefox driver
		//driver.quit();
	}
	
	@Test
	// Given I am on a random image page,
	// when pressing <= (arrow key)
	// then I expect to be taken to the "previous image" page.
	
	//and 
	
	// Given I am on a random image page,
	// when pressing => (arrow key)
	// then I expect to be taken to the "next image" page.
	public void test_LeftRightArrowKeys() 
	{
		//open one of our uploaded images
		driver.get("http://imgur.com/gallery/X3m1TAG/new");
		
		//capture image element for comparison
		String start_url = driver.getCurrentUrl();
		
		//send the right arrow key
		driver.findElement(By.tagName("html")).sendKeys(Keys.ARROW_RIGHT);
		
		//capture image element for comparison
		String next_url = driver.getCurrentUrl();
		
		//elements should not be the same
		assertNotEquals(start_url, next_url);
		
		//send the left arrow key
		driver.findElement(By.tagName("html")).sendKeys(Keys.ARROW_LEFT);
		
		//capture image element for comparison
		String prev_url = driver.getCurrentUrl();
		
		//elements should not be the same
		assertNotEquals(prev_url, next_url);
	}

}
