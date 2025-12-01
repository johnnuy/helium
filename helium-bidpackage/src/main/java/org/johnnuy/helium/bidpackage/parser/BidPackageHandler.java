package org.johnnuy.helium.bidpackage.parser;

import java.util.regex.Pattern;

public interface BidPackageHandler {

	public static String PAIRING_REGEX = "[T][0-9A-Z]{4}";
	public static Pattern PAIRING_PATTERN = Pattern.compile("(" + PAIRING_REGEX + ")");

	public static String TRIP_REGEX = "TRIP #([0-9]+)";
	public static Pattern TRIP_LINE = Pattern.compile("(" + TRIP_REGEX + ")((\\s(" + PAIRING_REGEX + "))?(.*))");

	public static String OL_REGEX = "\\([A-Z]{2}\\)";
	public static String COMPLIMENT_REGEX = "\\[([0-9]),([0-9]),([0-9]),([0-9])\\]";
	public static Pattern EFFECTIVE_LINE_REGEX = Pattern.compile("(" + OL_REGEX + ")\\s(" + COMPLIMENT_REGEX + ")(.*)");

	public static String CALENDAR_DAYS = "MO TU WE TH FR SA SU";
	public static String CALENDAR_LINE = CALENDAR_DAYS + " DAY FLT#";
	
	public static String CREW_COMP = "CREW COMP";
	
}
