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
		
		//capture page URL
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
		String s_prelike = driver.findElement(By.xpath("(//div[@class='author'])[1]/span")).getText();
		int prelike = Integer.parseInt(s_prelike.trim().replaceAll(",", ""));
		
		//'like' image
		driver.findElement(By.xpath("(//div[@title='like'])[1]")).click();
		
		//capture points value post-like
		String s_postlike = driver.findElement(By.xpath("(//div[@class='author'])[1]/span")).getText();
		int postlike = Integer.parseInt(s_postlike.trim().replaceAll(",", ""));
		
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
		String s_prelike = driver.findElement(By.xpath("(//div[@class='author'])[1]/span")).getText();
		int prelike = Integer.parseInt(s_prelike.trim().replaceAll(",", ""));
		
		//'like' image
		driver.findElement(By.xpath("(//div[@title='dislike'])[1]")).click();
		
		//capture points value post-like
		String s_postlike = driver.findElement(By.xpath("(//div[@class='author'])[1]/span")).getText();
		int postlike = Integer.parseInt(s_postlike.trim().replaceAll(",", ""));
				
		//assert that the value decremented
		assertEquals(prelike-1, postlike);
	}
	
	@Test
	// Given I am on a random page and there are comments on the image,
	// when I click a person's username,
	// then I should see that person's comments.
	public void test_users() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//click a username
		driver.findElement(By.xpath("((//div[@class='author'])[1]/a)[1]")).click();
	
		//grab the comment elements from this page
		List e = driver.findElements(By.cssSelector("div.comment-item"));
		
		//assert there is at least one comment associated with this user
		assertTrue(e.size() > 0);
		
		//I was initially going to test that there was a link to the image page we
		//	arrived at this user's page from in the same vein as the comment submission itself,
		//	but that did not work because not all comments are loaded at once when the page
		//	is generated; therefore the test can pass or fail based on which comments are
		//	loaded at the topmost part of the page instead of the whole list of the user's
		//	submitted comments.
	}
	
	@Test
	// Given I am on a random page and there are comments on the image,
	// when I click "expand all",
	// then more comments should load on the page.
	public void test_expand() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//if the option to expand all exists
		if(driver.findElement(By.cssSelector("a#expand-comments")) != null)
		{	
			//capture element list initially
			List e1 = driver.findElements(By.cssSelector("div.comment"));
			
			//expand
			driver.findElement(By.cssSelector("a#expand-comments")).click();
			
			//capture element list after expanding
			List e2 = driver.findElements(By.cssSelector("div.comment"));
			
			//assert that there are more comments on the page after expanding the list
			assertTrue(e1.size() < e2.size());
		}
		else
		{
			fail("No expand all option.");
		}
	}
	
	@Test
	// Given I am on a random page and there are comments on the image and the comments have been expanded,
	// when I click "collapse all",
	// then less comments should exist on the page.
	public void test_collapse() 
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the random image icon
		driver.findElement(By.cssSelector("div.random-icon")).click();
		
		//if the option to expand all exists
		if(driver.findElement(By.cssSelector("a#expand-comments")) != null)
		{	
			//expand
			driver.findElement(By.cssSelector("a#expand-comments")).click();
			
			//capture element list initially
			List e1 = driver.findElements(By.cssSelector("div.comment"));
			
			//collapse (incidentally, it's the same element)
			driver.findElement(By.cssSelector("a#expand-comments")).click();
			
			//capture element list after collapsing
			List e2 = driver.findElements(By.cssSelector("div.comment"));
			
			//assert that there are more comments on the page after expanding the list
			assertTrue(e1.size() > e2.size());
			//these numbers will actually be equal
		}
		else
		{
			fail("No expand all option.");
		}
	}
	

}
