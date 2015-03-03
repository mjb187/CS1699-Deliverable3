//Code written by Jordan McAleer
//	jmm291@pitt.edu

import static org.junit.Assert.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * USER STORY
 * 
 * As a registered user,
 * I want to upload images and albums to my respective galleries
 * so that I can view my images and albums in my given space.
*/

public class s5_uploadImages {

	//shared driver object
	private static WebDriver driver;
	
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
		//rand = new Random();
	}
	
	@AfterClass
	//close web driver
	public static void teardown()
	{
		//close Firefox driver
		driver.quit();
	}

	@Test
	// Given I am on home page and have an image URL,
	// when I click the upload image, insert the URL, click the start upload button
	// then the image should appear as the first image in my images gallery.
	public void test_uploadImage()
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the upload button
		driver.findElement(By.cssSelector("a.upload-button")).click();
		
		//pass the form a URL to try to upload (need to spoof pasting into the text area)
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys("http://i.imgur.com/3gqZYSX.jpg");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "c");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "v");
		
		//click the start upload button
		driver.findElement(By.cssSelector("button#upload-global-start-button")).click();
		
		//waits for the site to upload the image and be brought to the image page
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("target-clippy-image")));
		// target-clippy-image is an element within the image page
		
		//capture page URL for comparison
		String image_url = driver.getCurrentUrl();
		
		//open user image gallery
		driver.get("http://cs1699testaccount.imgur.com/all/");
		
		//gets URL of first thumbnail in image gallery
		String thumb_url = driver.findElement(By.xpath("//a[@class='cboxElement'][1]")).getAttribute("href");
		
		//checks that the last image uploaded entered into the gallery
		assertEquals(image_url, thumb_url);
	}
	
	@Test
	// Given I am on home page and have two image URLs,
	// when I click the upload image, insert the two URLs, click the create album, and start upload
	// then the album should appear as the first album in my albums gallery.
	public void test_uploadAlbum()
	{
		//open the homepage
		driver.get("http://imgur.com/");
		
		//click the upload button
		driver.findElement(By.cssSelector("a.upload-button")).click();
		
		//pass the form a URL to try to upload (need to spoof pasting into the text area)
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys("http://i.imgur.com/3gqZYSX.jpg");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "c");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "v");
			
		//pass the form a URL to try to upload (need to spoof pasting into the text area)
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys("http://i.imgur.com/LXCBsEd.gif");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "a");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "c");
		driver.findElement(By.cssSelector("textarea#upload-global-link-input")).sendKeys(Keys.CONTROL, "v");
			
		//click the album option button
		driver.findElement(By.cssSelector("button#upload-global-album-checkbox")).click();
		
		//click the start upload button
		driver.findElement(By.cssSelector("button#upload-global-start-button")).click();
		
		//waits for the site to upload the album and be brought to the album page
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("scrollpanel")));
		// scrollpanel is an element within the album page
		
		//capture page URL for comparison
		String image_url = driver.getCurrentUrl();
		
		//open user album gallery
		driver.get("http://cs1699testaccount.imgur.com/");
		
		//gets URL of first thumbnail in image gallery
		String thumb_url = driver.findElement(By.xpath("(//div[@class='cover'])[1]/a")).getAttribute("href");
		
		//checks that the last image uploaded entered into the gallery
		assertEquals(image_url, thumb_url);
	}
}
