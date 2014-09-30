package com.dreamforce.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;


/**
 * Date Utils abstraction on top Apache commons Lang 3
 * @author Sunand
 *
 */
public class DateUtil {

	public static void main(String args[]){
		System.out.println("Date====>"+DateUtil.addDays(new Date(),2, "yyyy-MM-dd'T'HH:mm:ss"));
	}
	/**
	 * Increment or decrement months and also fetch the date in the required format.
	 * @param date Date Object
	 * @param amount Can take +ve and -ve integers for increase/decrease
	 * @param format Sample format yyyy-MM-dd
	 * @return
	 */
	public static String addMonths(Date date, int amount, String format) {
		date = DateUtils.addMonths(date, amount);
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		return fmt.format(date);
	}
	
	/**
	 * Increment or decrement days and also fetch the date in the required format
	 * @param date Date Object
	 * @param amount Can take +ve and -ve integers for increase/decrease
	 * @param format Sample format yyyy-MM-dd
	 * @return
	 */
	public static String addWeeks(Date date, int amount, String format) {
		date = DateUtils.addWeeks(date, amount);
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		return fmt.format(date);
	}
	
	public static String addDays(Date date, int amount, String format) {
		date = DateUtils.addDays(date, amount);
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		return fmt.format(date);
	}
}
