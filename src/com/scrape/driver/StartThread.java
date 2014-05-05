package com.scrape.driver;

import java.io.IOException;

public class StartThread {
	public static void main(String args[]) throws InterruptedException, IOException {
//		Thread parameters are:
//			(String, //Item description to be provided in SMS/email notify
//			String, //URL link on Amazon
//			String, //SMS/email address to send notification
//			int, //Highest desired price
//			int); //Refresh Amazon page rate (in seconds)	
		RunProgram macbook256 = new RunProgram("rMBP 13 256GB",
			"http://www.amazon.com/gp/offer-listing/B0096VBXQE/sr=8-2/qid=1398474367/ref=olp_tab_all?ie=UTF8&colid=&coliid=&me=&qid=1398474367&seller=&sr=8-2",
			"2489825810",
			"roxiestargazer@gmail.com",
			1199,
			300);
		macbook256.start();
		RunProgram macbook512 = new RunProgram("rMBP 13 512GB",
			"http://www.amazon.com/gp/offer-listing/B0096VCHMI/sr=8-3/qid=1398474367/ref=olp_tab_all?ie=UTF8&colid=&coliid=&me=&qid=1398474367&seller=&sr=8-3",
			"2489825810",
			"roxiestargazer@gmail.com",
			1350,
			300);
		macbook512.start();
/*		RunProgram tshirt = new RunProgram("Bullet Bill T-Shirt",
				"http://www.amazon.com/gp/offer-listing/B00A3BPA1U/sr=8-1/qid=1399060364/ref=olp_tab_all?ie=UTF8&colid=&coliid=&qid=1399060364&seller=&sr=8-1",
				"2489825810",
				"roxiestargazer@gmail.com",
				10,
				300);
		tshirt.start();
		RunProgram acer2gb = new RunProgram("Acer C720 2GB",
			"http://www.amazon.com/gp/offer-listing/B00FNPD1VW/ref=dp_olp_all_mbc?ie=UTF8&condition=all",
			"2489825810",
			"roxiestargazer@gmail.com",
			130,
			60);
		acer2gb.start();
		RunProgram acer28444gb = new RunProgram("Acer C720-2844 4GB",
			"http://www.amazon.com/gp/offer-listing/B00GZB88MU/ref=dp_olp_all_mbc?ie=UTF8&condition=all",
			"2489825810",
			"roxiestargazer@gmail.com",
			160,
			60);
		acer28444gb.start();
		RunProgram acer28004gb = new RunProgram("Acer C720-2800 4GB",
			"http://www.amazon.com/gp/offer-listing/B00FNPD1OY/ref=dp_olp_all_mbc?ie=UTF8&condition=all",
			"2489825810",
			"roxiestargazer@gmail.com",
			180,
			60);
		acer28004gb.start();
		RunProgram lisaItems = new RunProgram("Travelon Security Socks, Large, Black",
			"http://www.amazon.com/gp/offer-listing/B00COE2XU0/ref=dp_olp_all_mbc?ie=UTF8&condition=all",
			"2489825810",
//			"roxiestargazer@gmail.com",
			"drcampylobacter@gmail.com",
			13,
			15000);
//		lisaItems.start();
*/	}
}