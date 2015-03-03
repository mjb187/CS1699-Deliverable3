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
 * As an unregisted user,
 * I want to be prompted to login or sign up when trying to interact with parts of the site
 * so that I can track my activity and contribute to the community.
*/

public class s2_login
{
	//shared driver object
	private static WebDriver driver;
	
	@BeforeClass
	//setup web driver
	public static void setup()
	{
		//setup Firefox driver and implict waits
		driver = new FirefoxDriver();
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
	// Given I am on a general page,
	// when clicking sign in/sign up
	// then I expect to be taken to a login/sign up page.
	public void test_loginButton() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the signin button
		driver.findElement(By.cssSelector("li.signin-link")).click();
		
		//check for a login form
		WebElement e = driver.findElement(By.xpath("//iframe[@src='https://imgur.com/signin/modal' or @src='https://imgur.com/register/modal']"));
		
		//assert that the login form is found
		assertNotNull(e);
	}
	
	@Test
	// Given I am on a random image page,
	// when clicking favorite
	// then I expect to be prompted to login or sign up.
	public void test_favoriteButton() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//click the favorite icon
		driver.findElement(By.cssSelector("span.favorite-image")).click();
		
		//check for a login form
		WebElement e = driver.findElement(By.xpath("//iframe[@src='https://imgur.com/signin/modal' or @src='https://imgur.com/register/modal']"));
		
		//assert that the login form is found
		assertNotNull(e);
	}
	
	@Test
	// Given I am on a random image page,
	// when clicking the upvote button
	// then I expect to be prompted to login or sign up.
	public void test_upvoteButton() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//click the favorite icon
		driver.findElement(By.cssSelector("span#mainUpArrow")).click();
		
		//check for a login form
		WebElement e = driver.findElement(By.xpath("//iframe[@src='https://imgur.com/signin/modal' or @src='https://imgur.com/register/modal']"));
		
		//assert that the login form is found
		assertNotNull(e);
	}
	
	@Test
	// Given I am on a random image page,
	// when clicking the downvote button
	// then I expect to be prompted to login or sign up.
	public void test_downvoteButton() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//click the favorite icon
		driver.findElement(By.cssSelector("span#mainDownArrow")).click();
		
		//check for a login form
		WebElement e = driver.findElement(By.xpath("//iframe[@src='https://imgur.com/signin/modal' or @src='https://imgur.com/register/modal']"));
		
		//assert that the login form is found
		assertNotNull(e);
	}
	
	@Test
	// Given I am on a random image page
	// when submitting a comment
	// then I expect to be prompted to login or sign up.
	public void test_comment() 
	{
		//open one of our uploaded images (to prevent spamming random images with comments)
		driver.get("http://imgur.com/gallery/X3m1TAG");
		
		//enter a comment
		driver.findElement(By.cssSelector("textarea#caption_textarea")).sendKeys("test comment submission\n");
		
		//check for a login form
		WebElement e = driver.findElement(By.xpath("//iframe[@src='https://imgur.com/signin/modal' or @src='https://imgur.com/register/modal']"));
		
		//assert that the login form is found
		assertNotNull(e);
	}
	
	@Test
	// Given I am on a general page,
	// when attempting to upload an image
	// then I expect to be prompted to login or sign up.
	public void test_uploadButton() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the upload button
		driver.findElement(By.cssSelector("a.upload-button")).click();
		
		//pass the form a URL to try to upload (need to spoof pasting into the text area)
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys("http://imgs.xkcd.com/comics/troubleshooting.png");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "c");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "v");
		
		//click the start upload button
		driver.findElement(By.cssSelector("button#upload-global-start-button")).click();
		
		//check for a login form
		WebElement e = driver.findElement(By.xpath("//iframe[@src='https://imgur.com/signin/modal' or @src='https://imgur.com/register/modal']"));
		
		//assert that the login form is found
		assertNotNull(e);
	}

}
