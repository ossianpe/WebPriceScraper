package com.scrape.driver;

import java.math.BigDecimal;
import java.math.MathContext;

class MathHandler {
	
	//Parse dollar amount strings to doubles
	// Requires decimal point value
	public static double myStringToDouble(String str) {
		double answer = 0;
		double factor = 0.01;
		for (int i = str.length()-1; i >= 0; i--) {
			if (str.charAt(i)=='0'||str.charAt(i)=='1'
			||str.charAt(i)=='2'||str.charAt(i)=='3'
			||str.charAt(i)=='4'||str.charAt(i)=='5'
			||str.charAt(i)=='6'||str.charAt(i)=='7'
			||str.charAt(i)=='8'||str.charAt(i)=='9') {
				answer += (str.charAt(i) - '0') * factor;
				factor *= 10;
			}
		}
		return answer;
	}
	
	public double BigDecimalSubtraction(String ncent, double ndiscoveredPrice){
		//Convert result to setPrice
		BigDecimal bdPrice = new BigDecimal(ndiscoveredPrice, MathContext.DECIMAL64);

		//setPrice reflects one penny less than lowest priced item
		BigDecimal bdCent = new BigDecimal(ncent);
		bdPrice = bdPrice.subtract(bdCent);
		return bdPrice.doubleValue();
	}
	
	
}