WebScraper
==========

Quick description:<br>
Scrapes Amazon for cheapest item and provides SMS/email alert.<br>

Details:<br>
-Main operation:<br>
Program scrapes Amazon for lowest price. Price is defined in parameters. If item is not the lowest the program will wait for a period of time and refresh the page. This activity will continue indefinitely until an item becomes available with a price equal or lower than the price the user set the parameter to.<br>
-Upon lower priced item listing:<br>
When the item becomes available at or less than the desired value the program will send a SMS and email alert with the defined product name, price, user feedback, condition, and description. After finding a cheaper item, the program will set the lowest price to the price of the lowest priced item - 1 cent and continue to loop for cheaper items to come available.<br>

Includes user configurable parameters in RunThread:<br>

//		Thread parameters:<br>
//			(String, //Item description to be provided in SMS/email notify<br>
//			String, //URL link on Amazon<br>
//			String, //SMS/email address to send notification<br>
//			int, //Highest desired price<br>
//			int); //Refresh Amazon page rate (in seconds)<br>

Note: Program runs multi-threaded instances of RunThread so many different items can be watched and parameters can be set.<br>
