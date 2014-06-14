package com.scrape.driver;

import java.io.IOException;

public class ScrapeParameters {

	protected final String setProductName, setHomePage, setPhoneNumber, setEmail;
	protected final int setPrice, setRefreshRate;

	protected final String setJob, setLocation;
	
    //private constructor to enforce object creation through builder
    protected ScrapeParameters(ScrapeBuilder builder) {
    	
    	this.setProductName = builder.setProductName;
    	this.setHomePage = builder.setHomePage;
    	this.setPhoneNumber = builder.setPhoneNumber;
    	this.setEmail = builder.setEmail;
    	this.setPrice = builder.setPrice;
    	this.setRefreshRate = builder.setRefreshRate;
    	
    	this.setJob = builder.setJob;
    	this.setLocation = builder.setLocation;
    }
	
    public static class ScrapeBuilder {

    	protected String setProductName;
		private String setHomePage;
		private String setPhoneNumber;
		private String setEmail;
    	private int setPrice, setRefreshRate;

    	private String setJob, setLocation;
    	
        //builder methods for setting property
        public ScrapeBuilder setProductName(String product){this.setProductName = product; return this; }
        public ScrapeBuilder setHomePage(String homepage){this.setHomePage = homepage; return this; }
        public ScrapeBuilder setPhoneNumber(String phonenumber){this.setPhoneNumber = phonenumber; return this; }
        public ScrapeBuilder setEmail(String email){this.setEmail = email; return this; }
        public ScrapeBuilder setPrice(int price){this.setPrice = price; return this; }
        public ScrapeBuilder setRefreshRate(int refreshrate){this.setRefreshRate = refreshrate; return this; }

        public ScrapeBuilder setJob(String job){this.setJob = job; return this; }
        public ScrapeBuilder setLocation(String location){this.setLocation = location; return this; }

        //return fully build object
        public ScrapeParameters build() {
            return new ScrapeParameters(this);
        }
    }

    public ScrapeParameters start() throws InterruptedException, IOException
	{
		RunProgram startscrape = new RunProgram(setProductName,
				setHomePage,
				setPhoneNumber,
				setEmail,
				setPrice,
				setRefreshRate,
				setJob,
				setLocation);
			startscrape.start();
		return null;
	}
}