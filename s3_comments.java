//Code written by Mike Byrne
//	mjb187@pitt.edu

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

/*
 * USER STORY
 * 
 * As a registered, logged-in user,
 * I want to interact with other users via comments
 * so that I can be part of the community and increase my "internet points."
*/

public class s3_comments
{
	//shared driver object
	private static WebDriver driver;
	
	//shared random number generator
	private static Random rand;
	
	@BeforeClass
	//setup web driver
	public static void setup()
	{
		//setup Firefox driver and implict waits
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		
		//sign in
		driver.get("http://imgur.com/signin");
		driver.findElement(By.cssSelector("input#username")).click();
		driver.findElement(By.cssSelector("input#username")).sendKeys("CS1699testaccount");
		driver.findElement(By.cssSelector("p.password > input")).click();
		driver.findElement(By.cssSelector("p.password > input")).sendKeys("laboon\n");
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		//set a browser cookie to sort comments by newest
		driver.manage().addCookie(new Cookie("f_sort", "newest", "imgur.com", "/", new Date(new Long("1555347941703"))));
		
		//instantiate random generator object
		rand = new Random();
	}
	
	@AfterClass
	//close web driver
	public static void teardown()
	{
		//close Firefox driver
		driver.quit();
	}
	
	//return a random integer from 0 to 9999
	private int getRandom()
	{
		return rand.nextInt(10000);
	}
	
	@Test
	// Given I am on a random page,
	// when I submit a comment to an image page,
	// then I expect that comment to be tracked on my user account.
	public void test_comment() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//capture page URL\
		String url = driver.getCurrentUrl();
		
		//submit a comment
		String comment = "what a hilarious image!";
		driver.findElement(By.cssSelector("textarea#caption_textarea")).sendKeys(comment+"\n");
		
		//go to the user comments page
		driver.get("http://imgur.com/user/CS1699testaccount");
		
		//grab the web element that links to the aforementioned url (without the 'http:')
		WebElement e = driver.findElement(By.xpath("//a[@href='" + url.substring(5) + "']"));
		
		//assert that this element exists
		assertNotNull(e);
	}
	
	@Test
	// Given I am on a random page and there are comments on the image,
	// when I click the 'like' arrow,
	// then I expect that comment to gain one 'point'.
	public void test_like() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//capture points value pre-like
		int prelike = Integer.parseInt(driver.findElement(By.xpath("(//div[@class='author'])[1]/span")).getText());
		
		//'like' image
		driver.findElement(By.xpath("(//div[@title='like'])[1]")).click();
		
		//capture points value post-like
		int postlike = Integer.parseInt(driver.findElement(By.xpath("(//div[@class='author'])[1]/span")).getText());
		
		//assert that the value incremented
		assertEquals(prelike+1, postlike);
	}
	
	@Test
	// Given I am on a random page and there are comments on the image,
	// when I click the 'dislike' arrow,
	// then I expect that comment to lose one 'point'.
	public void test_dislike() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//capture points value pre-like
		int prelike = Integer.parseInt(driver.findElement(By.xpath("(//div[@class='author'])[1]/span")).getText());
		
		//'like' image
		driver.findElement(By.xpath("(//div[@title='dislike'])[1]")).click();
		
		//capture points value post-like
		int postlike = Integer.parseInt(driver.findElement(By.xpath("(//div[@class='author'])[1]/span")).getText());
		
		//assert that the value decremented
		assertEquals(prelike-1, postlike);
	}
	
	@Test
	// Given I am on a random page and I have submitted a comment,
	// when I delete my comment from the user's comments page,
	// then I should not see that comment in the user page any longer.
	public void test_delete() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//capture points value pre-like
		int prelike = Integer.parseInt(driver.findElement(By.xpath("(//div[@class='author'])[1]/span")).getText());
		
		//'like' image
		driver.findElement(By.xpath("(//div[@title='dislike'])[1]")).click();
		
		//capture points value post-like
		int postlike = Integer.parseInt(driver.findElement(By.xpath("(//div[@class='author'])[1]/span")).getText());
		
		//assert that the value deccremented
		assertEquals(prelike-1, postlike);
	}
	

}
