package com.scrape.driver;

import java.io.IOException;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

class RunProgram implements Runnable {
	// Instance variables for storing object data
	private Thread t;
	private int pageRefreshRate;

	protected String productName;
	protected String setHomePage, setEmail, setPhoneNumber;
	protected double setPrice;

	public RunProgram(String produ, String homepa, String phonenu, String emai, double setpri, int refreshra) {
		productName = produ;
		setHomePage = homepa;
		setPhoneNumber = phonenu;
		setEmail = emai;
		setPrice = setpri;
		pageRefreshRate = refreshra*1000;
	}

	public void start() throws InterruptedException, IOException
	{
		if (t == null)
		{
			t = new Thread (this);
			t.start();
		}
	}

	public void run() {
		ScrapeHandler scrape = new ScrapeHandler(productName, setHomePage, setPhoneNumber, setEmail, setPrice, pageRefreshRate);
		scrape.StartBrowser();
		while(true)
		{
			try {
				scrape.StartScrape();
				scrape.PriceToDoubleTypeCaster();
				scrape.setNotificationString();
				//If less than desired price send email
				scrape.LowestPriceCheck();
				Thread.sleep(pageRefreshRate);
				scrape.PageRefreshBooleanEnable();
			}
			catch (TimeoutException e)
			{
				System.out.println("Refresh timeout.. refreshing page");
				scrape.RecursiveRefreshExceptionHandler();
			}	
			catch (NoSuchElementException e)
			{
				System.out.println("No such element.. refreshing page in " + pageRefreshRate/2000 + " seconds");
				try {
					Thread.sleep(pageRefreshRate/2);
				}
				catch (InterruptedException e1) {
					//If program is interrupted quit Firefox instance
					scrape.InterruptedExcp();
				}
				scrape.RecursiveRefreshExceptionHandler();
			}
			catch (InterruptedException ie) {
				//If program is interrupted quit Firefox instance
				scrape.InterruptedExcp();
			}
		}
	}
}