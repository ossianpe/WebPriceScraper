WebScraper
==========

Quick description:
Scrapes Amazon for cheapest item and provides SMS/email alert.

Details:
-Main operation:
Program scrapes Amazon for lowest price. Price is defined in parameters. If item is not the lowest the program will wait for a period of time and refresh the page. This activity will continue indefinitely until an item becomes available with a price equal or lower than the price the user set the parameter to.
-Upon lower priced item listing:
When the item becomes available at or less than the desired value the program will send a SMS and email alert with the defined product name, price, user feedback, condition, and description. After finding a cheaper item, the program will set the lowest price to the price of the lowest priced item - 1 cent and continue to loop for cheaper items to come available.

Includes user configurable parameters in RunThread:

//		Thread parameters:
//			(String, //Item description to be provided in SMS/email notify
//			String, //URL link on Amazon
//			String, //SMS/email address to send notification
//			int, //Highest desired price
//			int); //Refresh Amazon page rate (in seconds)	

Note: Program runs multithreaded instances of RunThread so many different items can be watched and parameters can be set.
