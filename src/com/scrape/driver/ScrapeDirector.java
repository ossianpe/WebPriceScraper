package com.scrape.driver;

import java.io.IOException;

public class ScrapeDirector {
    public static void main(String args[]) throws InterruptedException, IOException {
        //Creating object using Builder pattern in java
    	ScrapeParameters scrapeDirector = new ScrapeParameters.ScrapeBuilder()
    			.setProductName("rMBP13 256GB")
        		.setHomePage("http://www.amazon.com/gp/offer-listing/B0096VBXQE/sr=8-2/qid=1398474367/ref=olp_tab_all?ie=UTF8&colid=&coliid=&me=&qid=1398474367&seller=&sr=8-2")
        		.setPhoneNumber("")
        		.setEmail("")
        		.setPrice(1149)
        		.setRefreshRate(30)
        		.build();
		scrapeDirector.start();
/*    	ScrapeParameters scrapeDirector2 = new ScrapeParameters.ScrapeBuilder()
    			.setProductName("rMBP13 512GB")
        		.setHomePage("http://www.amazon.com/gp/offer-listing/B0096VCHMI/ref=dp_olp_all_mbc?ie=UTF8&condition=all")
        		.setPhoneNumber("2489825810")
        		.setEmail("roxiestargazer@gmail.com")
        		.setPrice(1300)
        		.setRefreshRate(30)
        		.build();
    	scrapeDirector2.start();
*/    	ScrapeParameters scrapeDirectorIndeed = new ScrapeParameters.ScrapeBuilder()
		.setJob("engineer")
		.setLocation("california")
		.setHomePage("http://www.indeed.com/m/")
		.setPhoneNumber("")
		.setEmail("")
		.setRefreshRate(90)
		.build();
//    	scrapeDirectorIndeed.start();
//        System.out.println(scrapedirector);
    }
}

class PrivateData {
	// NOTE: These values MUST be filled out to receive SMS and
	//       email notifications.
	// Twilio User & Token information
	// Find your Account Sid and Token at twilio.com/user/account
	private static final String ACCOUNT_SID = "";
  	private static final String AUTH_TOKEN = "";
  	// Registered sending phone number
  	// Must be configured/confirmed on Twilio first
  	private static final String FROM_PHONE_NUMBER = "";
  	// Gmail Account name and password
  	private static final String GMAIL_ACCOUNT = "";
  	private static final String GMAIL_PASSWORD = "";
  	
  	public String getACCOUNT_SID(){
  		return ACCOUNT_SID;
  	}
  	public String getAUTH_TOKEN(){
  		return AUTH_TOKEN;
  	}
  	public String getFROM_PHONE_NUMBER(){
  		return FROM_PHONE_NUMBER;
  	}
  	public String getGMAIL_ACCOUNT(){
  		return GMAIL_ACCOUNT;
  	}
  	public String getGMAIL_PASSWORD(){
  		return GMAIL_PASSWORD;
  	}
}