package org.johnnuy.helium.bidpackage.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.helium.bidpackage.BidPackageHandler;
import org.johnnuy.helium.domain.CrewCompliment;
import org.johnnuy.helium.domain.Pairing;
import org.johnnuy.helium.domain.PairingProvider;
import org.springframework.util.Assert;

/**
 * Parser for resolving the pairings from the bid package
 */
public class BidPackageReader implements PairingProvider {

	private static interface Tokens {
		public static String TRIP = "TRIP";
		public static String EFFECTIVE = "effective";
		public static String NO_EXCEPTIONS = "no exceptions.";
	}

	private static Pattern TRIP_LINE = Pattern.compile(
			"%s\\s#([0-9]+)\\sT([0-9A-Z]+)\\s\\(([A-Z]+)\\)\\s\\[([0-9\\,]+)\\]\\s([A-Z]{3}):\\s([^\\s]+)\\s%s\\s(.+)\\s%s"
			.formatted(Tokens.TRIP, Tokens.EFFECTIVE, Tokens.NO_EXCEPTIONS)
			);
	
	public static void main(String[] args) {
		List.of(Tokens.TRIP,
				"#([0-9]+)",
				"([0-9A-Z]+)"
				).stream().collect(Collectors.joining("\\s"));
		
		Matcher m = TRIP_LINE.matcher("TRIP #1397 T3501 (OL) [0,0,4,0] YYZ: 1______ effective DEC 08-DEC 08 no exceptions.");
		if (m.matches()) {
			for (int g=0; g<=m.groupCount();g++) {
				System.out.println(m.group(g));
			}
		}
		else {
			System.out.println("DOES NOT MATCH");
		}
	}

	private BufferedReader reader;

	private String currentLine = null;

	public BidPackageReader(BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public Optional<Pairing> nextPairing() {
		try {
			/* check if there's any more pairings to go */
			if (!skipToNextPairing()) {
				return Optional.empty();
			}

			return Optional.empty();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	/**
	 * helper method that searches for the next TRIP_LINE for identifying the start
	 * of a new pairing
	 * 
	 * @throws IOException
	 */
	private boolean skipToNextPairing() throws IOException {
		while ((currentLine = reader.readLine()) != null) {
			if (StringUtils.startsWith(currentLine, Tokens.TRIP)) {
				return true;
			}
		}
		return false;
	}
}
