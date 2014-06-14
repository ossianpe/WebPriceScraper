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

		private ScrapeType scrapeTypeFact = ScrapeTypeFactory.getScrape(super.setHomePage);
		
		private String discoveredStringPrice, discoveredStringShipping, discoveredStringFeedback, discoveredCondition, discoveredDescription;
		
		private int http501Delay;
		private int browserTimerLimit;
		private static boolean silentOperation;
		private static boolean initialRun = true;

		protected WebDriver driver;
		
		public ScrapeHandler(String produ, String homepa, String phonenu,
			String emai, double setpri, int refreshra, String job, String location) {
			super(produ, homepa, phonenu, emai, setpri, refreshra, job, location);
			http501Delay = 3;
			//Browser refresh timeout limit (in seconds)
			browserTimerLimit = 60;
			silentOperation = false;
		}

		public void runScrape(String produ, String homepa, String phonenu,
				String emai, double setpri, int refreshra, String job, String location) {
			StartBrowser();
			while(true)
			{
				try {
					StartScrape();
					setpri = scrapeTypeFact.runScrape(driver, produ, homepa, phonenu, emai, setpri, refreshra, job, location);
					Thread.sleep(refreshra*1000);
					PageRefreshBooleanEnable();
					initialRun = setSuccessiveRuns();
				}
				catch (TimeoutException e)
				{
					System.out.println("Refresh timeout.. refreshing page");
					RecursiveRefreshExceptionHandler();
				}	
				catch (NoSuchElementException e)
				{
					System.out.println("No such element.. refreshing page in " + refreshra*1000/2000 + " seconds");
					try {
						Thread.sleep(refreshra*1000/2);
					}
					catch (InterruptedException e1) {
						//If program is interrupted quit Firefox instance
						InterruptedExcp();
					}
					RecursiveRefreshExceptionHandler();
				}
				catch (InterruptedException ie) {
					//If program is interrupted quit Firefox instance
					InterruptedExcp();
				}
			}			
		}

		public static boolean getInitialRun() {
			return initialRun;
		}
		
		public boolean setSuccessiveRuns() {
			return false;
		}
		
		public void setsetPrice(double pri) {
			setPrice = pri;
		}
		
		//Open Home Page
		public void StartBrowser() {
			if(!silentOperation) {
				driver = new FirefoxDriver();
				driver.get(super.setHomePage);
	//			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			}
		}
		
		public void StartScrape() throws InterruptedException{
			try {
				if(silentOperation)
					RecursiveJsoupScrapeIOErrorHandler();
				//Read & store lowest item price in string
				else {
//					SeleniumFirefoxCssSelectorScrape();
					scrapeTypeFact.Selenium(driver);
				}
			}
			catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
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
				scrapeTypeFact.JSoup(discoveredStringPrice, discoveredCondition, discoveredDescription, setHomePage);
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