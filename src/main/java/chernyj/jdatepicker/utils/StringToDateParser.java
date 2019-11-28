package chernyj.jdatepicker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Chernyj Dmitry
 *
 */
public class StringToDateParser {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	public static final String DATE_DELIMETER = ".";

	private static final int DAY_MONTH_NO_SEPARATOR = 4;
	private static final int DAY_MONTH_SEPARATOR = 5;
	private static final int DAY_MONTH_YEAR_SHORT_NO_SEPARATOR = 6;
	private static final int DAY_MONTH_YEAR_FULL_NO_SEPARATOR = 8;

	public static Date parse(String dateStr) {
		if (!checkDigitsInDate(dateStr) || !checkIfNumber(dateStr))
			return null;

		dateStr = convertToCorrectForm(dateStr);

		if (isValid(dateStr))
			return convert(dateStr);

		return null;
	}

	private static String convertToCorrectForm(String dateStr) {

		switch (dateStr.length()) {
		case DAY_MONTH_NO_SEPARATOR:
			dateStr = insertDelimeters(dateStr);
			dateStr += DATE_DELIMETER + CalendarUtils.getYear(null);
			break;
		case DAY_MONTH_SEPARATOR:
			dateStr += DATE_DELIMETER + CalendarUtils.getYear(null);
			break;
		case DAY_MONTH_YEAR_SHORT_NO_SEPARATOR:
			dateStr = insertDelimeters(dateStr);
			dateStr = convertYear(dateStr);
			break;
		case DAY_MONTH_YEAR_FULL_NO_SEPARATOR:
			if (!dateStr.contains(DATE_DELIMETER))
				dateStr = insertDelimeters(dateStr);
			dateStr = convertYear(dateStr);
			break;
		}

		return dateStr;
	}

	private static String insertDelimeters(String dateStr) {
		final int daySeparatorIndex = 2;
		final int monthSeparatorIndex = 5;
		
		if(dateStr.length() < daySeparatorIndex)
			return null;

		String modified = null;
		
		modified = dateStr.substring(0, daySeparatorIndex) + DATE_DELIMETER + dateStr.substring(daySeparatorIndex);

		if (dateStr.length() >= monthSeparatorIndex)
			modified = modified.substring(0, monthSeparatorIndex) + DATE_DELIMETER
					+ modified.substring(monthSeparatorIndex);

		return modified;
	}

	private static boolean isValid(String dateStr) {
		DATE_FORMAT.setLenient(false);
		try {
			DATE_FORMAT.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	private static Date convert(String dateStr) {
		Date date = null;
		try {
			date = DATE_FORMAT.parse(dateStr);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		return date;
	}

	private static boolean checkDigitsInDate(String dateSet) {
		String analize = dateSet.replaceAll("\\.", "");
		int size = analize.length();
		if (size < 4 || size > 8 || size % 2 != 0)
			return false;
		else
			return true;
	}

	private static boolean checkIfNumber(String dateSet) {
		String analize = dateSet.replaceAll("\\.", "");
		try {
			Integer.parseInt(analize);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static String convertYear(String dateSet) {
		final int daySeparator = 0;
		final int monthSeparator = 1;
		final int yearSeparator = 2;
		final int yearShortFormDigits = 2;

		String firstPart = dateSet.split("\\.")[daySeparator] + DATE_DELIMETER + dateSet.split("\\.")[monthSeparator];

		String analize = dateSet.split("\\.")[yearSeparator];
		int yy = Integer.parseInt(analize);
		if (analize.length() == yearShortFormDigits) {
			if (yy < 50)
				yy += 2000;
			else
				yy += 1900;
		}
		return firstPart + DATE_DELIMETER + String.valueOf(yy);
	}

}
