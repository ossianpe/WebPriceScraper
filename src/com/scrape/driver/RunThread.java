package com.scrape.driver;

import java.io.IOException;

class RunProgram implements Runnable {
	// Instance variables for storing object data
	private Thread t;
	private int pageRefreshRate;

	protected String productName;
	protected String setHomePage, setEmail, setPhoneNumber;
	protected double setPrice;
	
	protected String setJob, setLocation;
	
	public RunProgram(String produ, String homepa, String phonenu, String emai, double setpri, int refreshra, String job, String location) {
		productName = produ;
		setHomePage = homepa;
		setPhoneNumber = phonenu;
		setEmail = emai;
		setPrice = setpri;
		pageRefreshRate = refreshra;
		setJob = job;
		setLocation = location;
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
		ScrapeHandler run = new ScrapeHandler(productName, setHomePage, setPhoneNumber, setEmail, setPrice, pageRefreshRate, setJob, setLocation);
		run.runScrape(productName, setHomePage, setPhoneNumber, setEmail, setPrice, pageRefreshRate, setJob, setLocation);
//		ScrapeType scrapeTypeFact = ScrapeTypeFactory.getScrape(setHomePage);
//		scrapeTypeFact.runScrape(productName, setHomePage, setPhoneNumber, setEmail, setPrice, pageRefreshRate);
	}
}