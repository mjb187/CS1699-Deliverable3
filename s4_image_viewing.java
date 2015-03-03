//Code written by Jordan McAleer
//	jmm291@pitt.edu

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

/*
 * USER STORY
 * 
 * As an unregistered user,
 * I want to change the display order and gallery
 * so that I can view a specific type of image in my preference order.
*/

public class s4_image_viewing {

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
	
	@Test	/** FAILS: This is the reason we used the arrow key to change pages. **/
	// This proves that Selenium method of clicking the next button does not work for what reasons we are not sure;
	// after investigation, it appears the navNext has an eventListener on click, but there is a
	// null value for the onclick and an HTMLDivElement of "click" calls an html page.
	// We were unsure of how to handle this so we used the arrow key that we knew worked.
	public void test_nextButton()
	{
		//open the newest first page (in the hot gallery)
		driver.get("http://imgur.com/hot/time");
		
		//click an image
		driver.findElement(By.cssSelector("a.image-list-link")).click();
		
		//capture page URL for comparison
		String start_url = driver.getCurrentUrl();
		
		//clicks for the next image
		driver.findElement(By.cssSelector("div.navNext")).click();
		
		//capture page URL for comparison
		String next_url = driver.getCurrentUrl();
		
		//URLs should not be the same
		assertNotEquals(start_url, next_url);
	}
	
	@Test
	// Given I am on the newest first home page of Most Viral,
	// when I click the first image and input the arrow key
	// then the next image should have an upload time earlier than the first.
	public void test_hotNewestFirstOption() 
	{
		//open the newest first page (in the hot gallery)
		driver.get("http://imgur.com/hot/time");
		
		//click an image
		driver.findElement(By.cssSelector("a.image-list-link")).click();
		
		// The following is the correct date, but returns null
		//String string = driver.findElement(By.xpath("(//div[@class='uploaded-time right'])[1]/span")).getAttribute("title");
		
		//get time of image
		String string1 = driver.findElement(By.xpath("(//span[@data-reactid='.4.2'])[1]")).getText();
		int time1 = time_ago(string1);

		//send the right arrow key for the next image
		// SEE FAILED TEST ABOVE
		driver.findElement(By.tagName("html")).sendKeys(Keys.ARROW_RIGHT);
		
		//get time of second image
		String string2 = driver.findElement(By.xpath("(//span[@data-reactid='.4.2'])[1]")).getText();
		int time2 = time_ago(string2);
		
		//assert that the first image is newer than or at the same time as the second
		assertTrue(time1 <= time2);
	}
	
	// Turns the string at the top of an image page into an integer of minutes
	public int time_ago(String str)
	{
		int time = 0;
		if(str.indexOf("minute") >= 0) {
			if(str.startsWith("a"))
				time = 1;
			else
				time = Integer.parseInt(str.substring(0, str.indexOf(' ')));
		}
		else if(str.indexOf("hour") >= 0) {
			if(str.startsWith("an"))
				time = 60;
			else
				time = 60 * Integer.parseInt(str.substring(0, str.indexOf(' ')));
		}
		else if(str.indexOf("day") >= 0) {
			if(str.startsWith("a"))
				time = 60*24;
			else
				time = 24* 60 * Integer.parseInt(str.substring(0, str.indexOf(' ')));
		}
		
		// assumes times do not go over days in newest first and seconds do not matter
		return time;
	}
	
	@Test
	// Given I am on the popularity home page of Most Viral,
	// when I click the first image and input the arrow key
	// then the next image should have less points per view.
	/** This test fails sometimes possibly due to a different popularity algorithm.
	 *  We believe the popularity algorithm should be based on the number of image
	 *  points per view of the image. **/
	public void test_hotPopularityOption() 
	{
		//open the most popular page (in the hot gallery)
		driver.get("http://imgur.com/hot/viral");
		
		//click an image
		driver.findElement(By.cssSelector("a.image-list-link")).click();
		
		//get points and views of the first image
		String counter = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		String views = driver.findElement(By.cssSelector("div.views-info > span")).getText();
		int image1_points = Integer.parseInt(counter.trim().replaceAll(",", ""));
		int image1_views = Integer.parseInt((views.trim().replaceAll(",", "")).replaceAll("k","000").replaceAll("m", "000000"));

		//send the right arrow key for the next image
		// SEE FAILED TEST ABOVE
		driver.findElement(By.tagName("html")).sendKeys(Keys.ARROW_RIGHT);
		
		//get points and views of second image
		counter = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		views = driver.findElement(By.cssSelector("div.views-info > span")).getText();
		int image2_points = Integer.parseInt(counter.trim().replaceAll(",", ""));
		int image2_views = Integer.parseInt((views.trim().replaceAll(",", "")).replaceAll("k","000").replaceAll("m", "000000"));
		
		//creates the ratio of image to views
		double image1_pop = (double)image1_points/image1_views;
		double image2_pop = (double)image2_points/image2_views;
		
		//assert that the first image has a higher ratio than the second
		assertTrue(image1_pop > image2_pop);
	}
	
	@Test
	// Given I am on the highest scoring home page of Most Viral,
	// when I click the first image and input the arrow key
	// then the next image should have less points than the first.
	/** This test fails sometimes possibly due to a different highest scoring algorithm.
	 *  We believe the highest algorithm should be the decreasing order of image points
	 *  with a predefined time limit--this was not tested because the feature failed. **/
	public void test_hotHighestScoringOption() 
	{
		//open the top scoring images of the day
		driver.get("http://imgur.com/top");
		
		//click an image
		driver.findElement(By.cssSelector("a.image-list-link")).click();
		
		//get points of the first image
		String counter = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		int image1_points = Integer.parseInt(counter.trim().replaceAll(",", ""));

		//send the right arrow key for the next image
		// SEE FAILED TEST ABOVE
		driver.findElement(By.tagName("html")).sendKeys(Keys.ARROW_RIGHT);
		
		//get points of second image
		counter = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		int image2_points = Integer.parseInt(counter.trim().replaceAll(",", ""));
		
		//assert that the first image has more points than or is equal to the second
		assertTrue(image1_points >= image2_points);
	}
	
	@Test
	// Given I am on the newest first home page of User Submitted,
	// when I click the first image and input the arrow key
	// then the next image should have an upload time earlier than the first.
	public void test_newNewestFirstOption() 
	{
		//open the newest first page (in the new gallery)
		driver.get("http://imgur.com/new/time");
		
		//click an image
		driver.findElement(By.cssSelector("a.image-list-link")).click();
		
		// The following is the correct date, but returns null
		//String string = driver.findElement(By.xpath("(//div[@class='uploaded-time right'])[1]/span")).getAttribute("title");
		
		//get time of image
		String string1 = driver.findElement(By.xpath("(//span[@data-reactid='.4.2'])[1]")).getText();
		int time1 = time_ago(string1);

		//send the right arrow key for the next image
		// SEE FAILED TEST ABOVE
		driver.findElement(By.tagName("html")).sendKeys(Keys.ARROW_RIGHT);
		
		//get time of second image
		String string2 = driver.findElement(By.xpath("(//span[@data-reactid='.4.2'])[1]")).getText();
		int time2 = time_ago(string2);
		
		//assert that the first image is newer than or at the same time as the second
		assertTrue(time1 <= time2);
	}
	
	@Test
	// Given I am on the popularity home page of User Submitted,
	// when I click the first image and input the arrow key
	// then the next image should have less points per view.
	/** This test fails sometimes possibly due to a different popularity algorithm.
	 *  We believe the popularity algorithm should be based on the number of image
	 *  points per view of the image. **/
	public void test_newPopularityOption() 
	{
		//open the most popular page (in the new gallery)
		driver.get("http://imgur.com/new/viral");
		
		//click an image
		driver.findElement(By.cssSelector("a.image-list-link")).click();
		
		//get points and views of the first image
		String counter = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		String views = driver.findElement(By.cssSelector("div.views-info > span")).getText();
		int image1_points = Integer.parseInt(counter.trim().replaceAll(",", ""));
		int image1_views = Integer.parseInt((views.trim().replaceAll(",", "")).replaceAll("k","000").replaceAll("m", "000000"));

		//send the right arrow key for the next image
		// SEE FAILED TEST ABOVE
		driver.findElement(By.tagName("html")).sendKeys(Keys.ARROW_RIGHT);
		
		//get points and views of second image
		counter = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		views = driver.findElement(By.cssSelector("div.views-info > span")).getText();
		int image2_points = Integer.parseInt(counter.trim().replaceAll(",", ""));
		int image2_views = Integer.parseInt((views.trim().replaceAll(",", "")).replaceAll("k","000").replaceAll("m", "000000"));
		
		//creates the ratio of image to views
		double image1_pop = (double)image1_points/image1_views;
		double image2_pop = (double)image2_points/image2_views;
		
		//assert that the first image has a higher ratio than the second
		assertTrue(image1_pop >= image2_pop);
	}
	
	@Test
	// Given I am on the rising home page of User Submitted,
	// when I click the first image and input the arrow key
	// then the first image should have more points than the second.
	/** This test fails sometimes possibly due to a different highest scoring algorithm.
	 *  We believe the highest algorithm should be the decreasing order of image points
	 *  with a predefined time limit--this was not tested because the feature failed. **/
	public void test_newRisingOption() 
	{
		//open the rising page of User Submitted
		driver.get("http://imgur.com/new/rising");
		
		//click an image
		driver.findElement(By.cssSelector("a.image-list-link")).click();
		
		//get points of the first image
		String counter = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		int image1_points = Integer.parseInt(counter.trim().replaceAll(",", ""));

		//send the right arrow key for the next image
		// SEE FAILED TEST ABOVE
		driver.findElement(By.tagName("html")).sendKeys(Keys.ARROW_RIGHT);
		
		//get points of second image
		counter = driver.findElement(By.cssSelector("div.point-info > span")).getText();
		int image2_points = Integer.parseInt(counter.trim().replaceAll(",", ""));
		
		//assert that the first image has more points than or is equal to the second
		assertTrue(image1_points >= image2_points);
	}
}
