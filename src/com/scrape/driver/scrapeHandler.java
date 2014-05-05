package com.scrape.driver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

class ScrapeHandler extends RunProgram {

		private WebDriver driver;
		private MathHandler math;
		private String notificationSubject, notificationBody;
		private String cent;
		
		double discoveredPrice, discoveredShipping;
		String discoveredStringPrice, discoveredStringShipping, discoveredStringFeedback, discoveredCondition, discoveredDescription;
		
		int http501Delay;
		int browserTimerLimit;
		static boolean silentOperation;

		public ScrapeHandler(String produ, String homepa, String phonenu,
			String emai, double setpri, int refreshra) {
			super(produ, homepa, phonenu, emai, setpri, refreshra);
			http501Delay = 3;
			//Browser refresh timeout limit (in seconds)
			browserTimerLimit = 60;
			silentOperation = false;
			cent = "0.01";
		}

		//Open Home Page
		public void StartBrowser() {
			if(!silentOperation) {
				driver = new FirefoxDriver();
				driver.get(super.setHomePage);
	//			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			}
		}
		
		public void setNotificationString() {
			notificationSubject = (productName + " $" + discoveredPrice + " " + discoveredStringShipping + " shipping");
			notificationBody = ("Condition: " + discoveredCondition +
					"\nFeedback: " + discoveredStringFeedback +
					"\nDescription: " + discoveredDescription);
			System.out.println(productName + " is: " + discoveredPrice + " " + discoveredStringShipping);
		}
		
		public void LowestPriceCheck() {
			if(discoveredPrice <= setPrice) {
				//Send Email or SMS
//				NotificationHandler.NotificationSender(setEmail, notificationSubject, notificationBody, setPhoneNumber);
				//Convert double to BigDecimal to do subtraction
				math = new MathHandler();
				System.out.println("Set price was: " + setPrice);
				setPrice = math.BigDecimalSubtraction(cent, discoveredPrice);
				System.out.println("Set price is now: " + setPrice);
				math = null;
			}
			else {
				System.out.println("No update...");
			}
		}
		
		public void PriceToDoubleTypeCaster() {
			math = new MathHandler();
			discoveredPrice = math.myStringToDouble(discoveredStringPrice);
			math = null;
		}
		
		public void StartScrape() throws InterruptedException{
			try {
				if(silentOperation)
					RecursiveJsoupScrapeIOErrorHandler();
				//Read & store lowest item price in string
				else {
					SeleniumFirefoxCssSelectorScrape();
				}
			}
			catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
		public void SeleniumFirefoxCssSelectorScrape() {
			//ADD SUPPORT FOR OPTIONAL DIALOGUE BUYING CHOICES PAGE
			String ignoredText, newText;
			
			discoveredStringPrice = SeleniumFirefoxCssSelector("div[id='olpOfferList'] span[class='a-size-large a-color-price olpOfferPrice a-text-bold']");

			discoveredStringShipping = SeleniumFirefoxCssSelector("div[id='olpOfferList'] div[class='a-column a-span2'] span[class='a-color-secondary']");
			//Clean up shipping string for "FREE Shipping" instances
			if(discoveredStringShipping.charAt(0) == '&')
				discoveredStringShipping="+ FREE Shipping";
			
			discoveredStringFeedback = SeleniumFirefoxCssSelector("p[class='a-spacing-small'] a[href]");
			//Catch "Seller Profile" scrape and prints more accurate "New Seller" instead
			ignoredText = "Seller Profile";
			newText = "New Seller";
			for(int i=0; i<=(discoveredStringFeedback.length()-ignoredText.length()); i++)
				if (discoveredStringFeedback.regionMatches(i, ignoredText, 0, discoveredStringFeedback.length())) {
					//Self note: "regionMatches" can't seem to handle "&"
					discoveredStringFeedback = newText;
//					discoveredStringFeedback = SeleniumFirefoxCssSelector("div[class='a-column a-span5 olpSellerColumn'] span[class='a-size-medium a-text-bold'");
					break;
				}

			discoveredCondition = SeleniumFirefoxCssSelector("div[class='a-column a-span3'] h3[class='a-spacing-small olpCondition']");
			discoveredDescription = SeleniumFirefoxCssSelector("div[class='a-column a-span3'] div[class='comments']");
		}
		
		public String SeleniumFirefoxCssSelector(String cssSelector) throws NoSuchElementException {
			
//			try {
			return driver.findElement(By.cssSelector(cssSelector)).getText();
//			}
	/*		catch (NoSuchElementException ie) {
				return driver.findElement(By.cssSelector(cssSelectorAlt)).getText();
			}
	*/	}
		
		public void PageRefreshBooleanEnable() {
			if(!silentOperation) {
				//RECURSION DOESN'T WORK
				//ONLY GOES THROUGH ONE INSTANCE
				//WHO KNOWS WHY??
				RecursiveRefreshExceptionHandler();
				}
		}
		
		public void RecursiveRefreshExceptionHandler() throws TimeoutException{
			
			try {
				driver.manage().timeouts().pageLoadTimeout(browserTimerLimit, TimeUnit.SECONDS);
				driver.navigate().refresh();
			}
			catch (TimeoutException e)
			{
				//Handle subsequent refreshing
				System.out.println("TimeoutException refreshing again..");
//				driver.findElement(By.tagName("body")).sendKeys("Keys.ESCAPE");
//				Thread.sleep
				ScrapeHandler.this.RecursiveRefreshExceptionHandler();
			}
		}	
		
		public void RecursiveJsoupScrapeIOErrorHandler() throws MalformedURLException, IOException, InterruptedException{
			
			try {
				JsoupScrape(discoveredStringPrice, discoveredCondition, discoveredDescription, setHomePage);
			}
			catch (MalformedURLException e) {
				System.out.println("Malformed URL: " + e.getMessage());
			}
			catch (IOException e) {
				System.out.println("I/O Error: " + e.getMessage());
				ScrapeHandler.IOExceptionDelay(http501Delay);
				ScrapeHandler.this.RecursiveJsoupScrapeIOErrorHandler();
		}
	}	
		
		public static void JsoupScrape(String price, String condition, String description, String homepage) throws MalformedURLException, IOException{

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

//			Document doc2 = Jsoup.connect(homepage).timeout(10000).get();
//			Document doc3 = Jsoup.connect(homepage).timeout(10000).get();
			Document doc2 = doc.clone();
			Document doc3 = doc.clone();
			
//			Node n = doClone();
		
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
		
		public void InterruptedExcp() {
			System.out.println("Program is exiting");
			if(!silentOperation) {
				driver.quit();
			}
		}
		
		public static void IOExceptionDelay(int d) throws InterruptedException {
			System.out.print("Retrying in " + d + " seconds.");
			Thread.sleep(d*1000);
		}
}