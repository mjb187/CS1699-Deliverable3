//Code written by Mike Byrne
//	mjb187@pitt.edu

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

/*
 * USER STORY
 * 
 * As a registered, logged-in user,
 * I want to be able to interact with the site via keyboard shortcuts
 * so that I can easily browse the site without a mouse.
*/

// IMGUR TEST ACCOUNT DETAILS
// U: CS1699testaccount
// P: laboon

public class s1_keyboardShortcuts 
{
	//shared driver object
	private static WebDriver driver;
	
	@BeforeClass
	//setup web driver
	public static void setup()
	{
		//setup Firefox driver and implict waits
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		//sign in
		driver.get("http://imgur.com/signin");
		driver.findElement(By.cssSelector("input#username")).click();
		driver.findElement(By.cssSelector("input#username")).sendKeys("CS1699testaccount");
		driver.findElement(By.cssSelector("p.password > input")).click();
		driver.findElement(By.cssSelector("p.password > input")).sendKeys("laboon\n");
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	@AfterClass
	//close web driver
	public static void teardown()
	{
		//close Firefox driver
		driver.quit();
	}
	
	@Test
	// Given I am on a random image page,
	// when pressing => (arrow key)
	// then I expect to be taken to the "next image" page (the next image in the list).
	public void test_rightArrowKey() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//capture page URL for comparison
		String start_url = driver.getCurrentUrl();
		
		//send the right arrow key
		driver.findElement(By.tagName("html")).sendKeys(Keys.ARROW_RIGHT);
		
		//capture page URL for comparison
		String next_url = driver.getCurrentUrl();
		
		//URLs should not be the same
		assertNotEquals(start_url, next_url);
	}
	
	@Test
	// Given I am on a random image page,
	// when pressing <= (arrow key)
	// then I expect to be taken to the "previous image" page (the previous image in the list).
	public void test_leftArrowKey() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//capture page URL for comparison
		String start_url = driver.getCurrentUrl();
		
		//send the left arrow key
		driver.findElement(By.tagName("html")).sendKeys(Keys.ARROW_LEFT);
		
		//capture page URL for comparison
		String prev_url = driver.getCurrentUrl();
		
		//URLs should not be the same
		assertNotEquals(start_url, prev_url);
	}
	
	@Test
	// Given I am on a random image page,
	// when pressing '+' (actually the '=' key)
	// then I expect to upvote that image.
	public void test_plus() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//get the like counter value
		String s_counter_before = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		int counter_before = Integer.parseInt(s_counter_before.trim().replaceAll(",", ""));
		
		//send the + key
		driver.findElement(By.tagName("html")).sendKeys("=");
		
		//get the new like counter value
		String s_counter_after = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		int counter_after = Integer.parseInt(s_counter_after.trim().replaceAll(",", ""));
		
		//assert that the counter was incremented
		assertTrue(counter_before < counter_after);
	}
	
	@Test
	// Given I am on a random image page,
	// when pressing '-'
	// then I expect to downvote that image.
	public void test_minus() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//get the like counter value
		String s_counter_before = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		int counter_before = Integer.parseInt(s_counter_before.trim().replaceAll(",", ""));
		
		//send the - key
		driver.findElement(By.tagName("html")).sendKeys("-");
		
		//get the new like counter value
		String s_counter_after = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		int counter_after = Integer.parseInt(s_counter_after.trim().replaceAll(",", ""));
		
		//assert that the counter was decremented
		System.out.println(counter_before + " | " + counter_after);
		assertTrue(counter_before > counter_after);
	}
	
	@Test
	// Given I am on a random image page,
	// when pressing '0'
	// then I expect to have that image added to my account's favorites list.
	public void test_favorite() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//get the like button's classes before
		String classes_before = driver.findElement(By.cssSelector("span.favorite-image")).getAttribute("class");
		
		//assert that the like button did not have a "favorited" class prior to favoriting
		assertFalse(classes_before.contains("favorited"));
		
		//send the 0 key
		driver.findElement(By.tagName("html")).sendKeys("0");
		
		//get the like button's classes after
		String classes_after = driver.findElement(By.cssSelector("span.favorite-image")).getAttribute("class");
		
		//assert that the like button has a "favorited" class applied after favoriting
		assertTrue(classes_after.contains("favorited"));
		
		//save image ID
		String id = driver.getCurrentUrl();
		id = id.substring(id.lastIndexOf('/')+1);
		
		//go to the user's favorites page
		driver.get("http://imgur.com/account/favorites");
		
		//grab the element with this image ID
		WebElement e = driver.findElement(By.cssSelector("div#" + id));
		
		//assert that there exists an element with the same image ID
		assertNotNull(e);
	}

}
