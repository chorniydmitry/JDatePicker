package chernyj.jdatepicker.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Chernyj Dmitry
 *
 */
public class CalendarUtils {
	public static final int DAYS_IN_WEEK = 7;
	public static final int MONTHS_IN_YEAR = 12;
	public static final int WEEAKS_IN_MONTH = 4;
	public static final int WEEAKS_IN_MONTH_SHOWING = 6;
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	
	
	private static final int DAY_OF_WEEK_OFFSET = -2;
	
	public static String[] getNumbersOfWeeks(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		
		Calendar firstDayOfMonth = new GregorianCalendar(year, month, 1);
		
		int firstWeekNum = firstDayOfMonth.get(Calendar.WEEK_OF_YEAR);
		

		String[] weeksNums = new String[WEEAKS_IN_MONTH_SHOWING];
		
		for(int i = 0; i < weeksNums.length; i++) {
			weeksNums[i] = String.valueOf(firstWeekNum);
			firstWeekNum ++;
		}
		
		return weeksNums;
	}
	
	public static String[] getNumbersOfWeeks() {
		return getNumbersOfWeeks(Calendar.getInstance().getTime());
	}
	

	public static String[][] getDays(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		
		Calendar firstDayOfMonth = new GregorianCalendar(year, month, 1);
		int firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);
		if(firstDayOfWeek == 1)
			firstDayOfWeek = 8;
		
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		String[][] daysCaps = new String[WEEAKS_IN_MONTH_SHOWING][DAYS_IN_WEEK];
		
		int currentDay = 1;
		
		for(int i = 0; i < CalendarUtils.WEEAKS_IN_MONTH_SHOWING; i++) {
			
			for(int j = 0; j < CalendarUtils.DAYS_IN_WEEK; j++) {
				if((i == 0 && firstDayOfWeek + DAY_OF_WEEK_OFFSET > j) || (currentDay > daysInMonth)) {
					daysCaps[i][j] = "";
					continue;
				}
				
				daysCaps[i][j] = String.valueOf(currentDay);
				currentDay++;
			}
		}
		
		return daysCaps;
	}
	
	public static String[][] getDays() {
		return getDays(Calendar.getInstance().getTime());
	}
	
	public static String getDateStr(Date date) {
		Calendar calendar = Calendar.getInstance();
		if(date != null)
			return DATE_FORMAT.format(date);
		
		Date current = calendar.getTime();
		return DATE_FORMAT.format(current);
	}
	
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		if(date != null)
			calendar.setTime(date);
		
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getMonthIndex(Date date) {
		Calendar calendar = Calendar.getInstance();
		if(date != null)
			calendar.setTime(date);
		
		return calendar.get(Calendar.MONTH);
	}
	
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		if(date != null)
			calendar.setTime(date);
		
		return calendar.get(Calendar.YEAR);
	}
}
