package com.scrape.driver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

class ScrapeTypeFactory{
	public static ScrapeType getScrape(String url) {
		if(url.indexOf("www.amazon") != -1)
			return new AmazonScrapeType();
		if(url.indexOf("www.craigslist") != -1)
			return new CraigslistScrapeType();
		if(url.indexOf("www.indeed") != -1)
			return new IndeedScrapeType();
		return null;
	}
}

abstract class ScrapeType{
	public abstract double runScrape(WebDriver driver, String produ, String homepa, String phonenu,
			String emai, double setpri, int refreshra, String job, String location);
	public abstract void Selenium(WebDriver driver);
	public abstract void JSoup(String price, String condition, String description, String homepage) throws MalformedURLException, IOException;
	public abstract String setNotificationSubject(String produ, double price, String shipping);
	public abstract String setNotificationBody(String cond, String feedba, String descript);
	public abstract void setNotificationString(String productName);
	public String SeleniumCssSelector(WebDriver driver, String cssSelector) throws NoSuchElementException {
		return driver.findElement(By.cssSelector(cssSelector)).getText();
	}
	public void PriceToDoubleTypeCaster() {
		System.out.println("**Shouldn't be here** Inside method to be overidden 'PriceToDoubleTypeCaster'");
	}

	public double LowestPriceCheck(String setPhoneNumber, String setEmail,
			double setPrice) {
		System.out.println("**Shouldn't be here** Inside method to be overidden 'LowestPriceCheck'");
		return setPrice;
	}
}

class AmazonScrapeType extends ScrapeType {
	private double discoveredPrice, discoveredShipping;
	private String discoveredStringPrice, discoveredStringShipping, discoveredStringFeedback, discoveredCondition, discoveredDescription;
	private int browserTimerLimit;
	private MathHandler math;

	private String notificationSubject, notificationBody;
	
	public double runScrape(WebDriver driver, String produ, String homepa, String phonenu,
			String emai, double setpri, int refreshra, String job,
			String location) {
		PriceToDoubleTypeCaster();
		setNotificationString(produ);
		//If less than desired price send email
		discoveredShipping = LowestPriceCheck(phonenu, emai, setpri);
		return discoveredShipping;
	}
	
	public void Selenium(WebDriver driver){
		String ignoredText, newText;
		discoveredStringPrice = SeleniumCssSelector(driver, "div[id='olpOfferList'] span[class='a-size-large a-color-price olpOfferPrice a-text-bold']");
		discoveredStringShipping = SeleniumCssSelector(driver, "div[id='olpOfferList'] div[class='a-column a-span2'] span[class='a-color-secondary']");
		//Cleans up shipping string for "FREE Shipping" instances
		if(discoveredStringShipping.charAt(0) == '&')
			discoveredStringShipping="+ FREE Shipping";
		discoveredStringFeedback = SeleniumCssSelector(driver, "p[class='a-spacing-small'] a[href]");
		//Catches "Seller Profile" scrape and prints more accurate "New Seller" instead
		ignoredText = "Seller Profile";
		newText = "New Seller";
		for(int i=0; i<=(discoveredStringFeedback.length()-ignoredText.length()); i++)
			if (discoveredStringFeedback.regionMatches(i, ignoredText, 0, discoveredStringFeedback.length())) {
				//Self note: "regionMatches" can't seem to handle "&"
				discoveredStringFeedback = newText;
//				discoveredStringFeedback = getCSSString("div[class='a-column a-span5 olpSellerColumn'] span[class='a-size-medium a-text-bold'");
				break;
			}
		discoveredCondition = SeleniumCssSelector(driver, "div[class='a-column a-span3'] h3[class='a-spacing-small olpCondition']");
		discoveredDescription = SeleniumCssSelector(driver, "div[class='a-column a-span3'] div[class='comments']");
	}
	
	public void JSoup(String price, String condition, String description, String homepage) throws MalformedURLException, IOException{

		PrintStream outFile = new PrintStream(new FileOutputStream("output.txt"));
/*
		URL url = new URL("http://www.amazon.com/gp/offer-listing/B0096VCHMI/ref=dp_olp_all_mbc?ie=UTF8&condition=all");
		
		// read text returned by server
	    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	    String line;
	    while ((line = in.readLine()) != null) {
	    	outFile.println(line);
	    }
    	in.close();
*/
		BufferedWriter htmlWriter = new BufferedWriter(new FileWriter(new File("out2.txt")));
		
		int r = 0;
		r++;
		Document doc = Jsoup.connect(homepage).timeout(20000).get();
		htmlWriter.write(doc.toString());
		htmlWriter.flush();
		htmlWriter.close();

//		Document doc2 = Jsoup.connect(homepage).timeout(10000).get();
//		Document doc3 = Jsoup.connect(homepage).timeout(10000).get();
		Document doc2 = doc.clone();
		Document doc3 = doc.clone();
		
//		Node n = doClone();
	
		// Get current item price from Amazon and store
		Elements stringPriceLink = doc.select("span[class=a-size-large a-color-price olpOfferPrice a-text-bold]").eq(0);
		price = stringPriceLink.html();

		// Get current item condition from Amazon and store
		Elements conditionLink = doc2.select("h3[class=a-spacing-small olpCondition]").eq(0);
		condition = conditionLink.html();

		// Get current item condition from Amazon and store
		Elements descriptionLink = doc3.select("div[class=a-column a-span3] div[class=comments]").eq(0);
		description = descriptionLink.html();

		System.out.println("Price: " + price);
		System.out.println("Condi: " + condition);
		System.out.println("Descr: " + description);
		r--;
		System.out.println("URL IO Fetching error 501 Failure hit: " + r);
		r = 0;
		outFile.close();
	}
	
	public String setNotificationSubject(String produ, double price, String shipping) {
		return (produ + " $" +
				price + " " + 
				shipping + " shipping");
	}
	
	public String setNotificationBody(String cond, String feedba, String descript) {
		return ("Condition: " + cond +
				"\nFeedback: " + feedba +
				"\nDescription: " + descript);
	}

	public void setNotificationString(String productName) {
		//Sets notification strings according to scrape type in ScrapeTypeFactory
		notificationSubject = setNotificationSubject(productName, discoveredPrice, discoveredStringShipping);
		notificationBody = setNotificationBody(discoveredCondition, discoveredStringFeedback, discoveredDescription);
		System.out.println(setNotificationSubject(productName, discoveredPrice, discoveredStringShipping));	
	}
	
	public void PriceToDoubleTypeCaster() {
		math = new MathHandler();
		discoveredPrice = math.myStringToDouble(discoveredStringPrice);
		math = null;
	}
	
	public double LowestPriceCheck(String phonenu, String emai, double setpri) {
		if(discoveredPrice <= setpri) {
			String cent = "0.01";
			//Send Email or SMS
			NotificationHandler.NotificationSender(emai, notificationSubject, notificationBody, phonenu);
			//Convert double to BigDecimal to do subtraction
			math = new MathHandler();
			System.out.println("Set price was: " + setpri);
			setpri = math.BigDecimalSubtraction(cent, discoveredPrice);
			System.out.println("Set price is now: " + setpri);
			math = null;
		}
		else {
			System.out.println("No update...");
		}
		return setpri;
	}

}

class CraigslistScrapeType extends ScrapeType {
	public double runScrape(WebDriver driver, String produ, String homepa, String phonenu,
			String emai, double setpri, int refreshra, String job, String location) {
		return setpri;
	}
	public void Selenium(WebDriver driver) {
	}
	public void JSoup(String price, String condition, String description, String homepage) {
	}
	public String setNotificationSubject(String produ, double price, String shipping) {
		return "nopenotsubjclisst";
	}
	public String setNotificationBody(String cond, String feedba, String descript) {
		return "notbodyclist";
	}
	public void setNotificationString(String productName) {
	}
}

class IndeedScrapeType extends ScrapeType {
	
	public double runScrape(WebDriver driver, String produ, String homepa, String phonenu,
			String emai, double setpri, int refreshra, String job, String location) {
		if(ScrapeHandler.getInitialRun()==true) {
			if(location.contentEquals("null"))
				location = "";
			setpri = 0;
			InitialRun(driver, job, location);
		}
		//Use setpri as counter for href search results "Next" link locator
		setpri++;
		SelectMatchingJob(driver, job, location, setpri);
		ScrapeJobDescription(driver, job);
		Selenium(driver);
		return setpri;
	}
	public void Selenium(WebDriver driver) {
	}
	public void InitialRun(WebDriver driver, String job, String location) {
		WebElement enterJob=driver.findElement(By.name("q"));
		enterJob.clear();
		enterJob.sendKeys(job);
		enterJob=driver.findElement(By.name("l"));
		enterJob.clear();
		enterJob.sendKeys(location);
		enterJob.sendKeys(Keys.RETURN);
	}
	public void SelectMatchingJob(WebDriver driver, String job, String location, double setpri) {
//		WebElement elem = driver.findElement(By.xpath("//*[contains(.,'engineer')]")); 
//		elem.click();
		String currentJob;
		
		List<WebElement> link_boxes=driver.findElements(By.xpath("//*[@rel='nofollow']"));
		 System.out.println("Number of boxes "+link_boxes.size() );
		
		 for(int j=0;j<link_boxes.size();j++){
			 
			 WebElement box = link_boxes.get(j);
			 List<WebElement> links = box.findElements(By.tagName("a"));
			 System.out.println("Total links for---"+link_boxes.get(j+1)+"---are--- "+links.size() );
			 System.out.println("====================================="+j);
			 for(int i=1 ; i<links.size();i++){
			 System.out.println("*********************************************");
			 System.out.println(links.get(i).getText());
			 System.out.println(links.get(i).getAttribute("href"));
			 
			 }
		 }
		
/*		for(int i=10; i>0; i--) {
			currentJob = SeleniumCssSelector(driver, "p a[rel='nofollow']");
			if((currentJob.indexOf(job)!= -1) || (currentJob.indexOf("Engineer")!= -1)
				|| (currentJob.indexOf("ENGINEER")!= -1) || (currentJob.indexOf("ENGINEERING")!= -1))
				driver.findElement(By.partialLinkText(currentJob)).click();
			ScrapeJobDescription(driver, job);
		}
*/	}
	public void ScrapeJobDescription(WebDriver driver, String job) {
		String positionTitle, companyName;
		positionTitle = SeleniumCssSelector(driver, "font[size='+1']");
		System.out.println("positiontitle: " + positionTitle);
		companyName = SeleniumCssSelector(driver, "span[class='source']");
		System.out.println("companyname: " + companyName);
		driver.findElement(By.partialLinkText("Next")).click();
	}
	public void searchForJobs(String job) {
	}
	public void JSoup(String price, String condition, String description, String homepage) {
	}
	public String setNotificationSubject(String produ, double price, String shipping) {
		return "nopenotsubindeed";
	}
	public String setNotificationBody(String cond, String feedba, String descript) {
		return "notbodyindeed";
	}
	public void setNotificationString(String productName) {
	}
}