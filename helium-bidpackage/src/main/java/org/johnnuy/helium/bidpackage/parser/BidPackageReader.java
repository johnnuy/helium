package org.johnnuy.helium.bidpackage.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.helium.domain.CrewCompliment;
import org.johnnuy.helium.domain.Pairing;
import org.johnnuy.helium.domain.PairingProvider;
import org.springframework.util.Assert;

/**
 * Parser for resolving the pairings from the bid package
 */
public class BidPackageReader implements PairingProvider {

	static String EFFECTIVE = "effective\\s(.+)";
	static String EXCEPTIONS = "(no exceptions.|except)(?:\\s(.+))?";
	
	static String[] TRIP_COMPONENTS = new String[] {
			"TRIP",					// 
			"#([0-9]+)",			// Trip #
			"([A-Z][0-9A-Z]+)",		// Pairing ID
			"\\(([A-Z]+)\\)",		// OL?
			"(\\[[0-9\\,]+\\])",	// Crew Compliment
			"([A-Z]{3}):",			// Base Airport Code
			"([^\\s]+)",			// Day Mask
			EFFECTIVE,				// Effective Dates
			EXCEPTIONS  			// Exception Dates
	};

	static Pattern TRIP_LINE = Pattern.compile(Stream.of(TRIP_COMPONENTS).collect(Collectors.joining("\\s")));

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
			
			Matcher m = TRIP_LINE.matcher(currentLine);
			Assert.isTrue(m.matches(), "Unable to parse Trip Line '%s'".formatted(currentLine));
			
			Pairing pairing = new Pairing()
					.setTripNumber(Integer.parseInt(m.group(1)))
					.setPairingId(m.group(2))
					.setCrewCompliment(new CrewCompliment(m.group(4)));
			
			currentLine = reader.readLine();
			Assert.state("MO TU WE TH FR SA SU DAY FLT# DEP ARR DEP ARR BLK TOG DUTY CREDIT LO A/C CREW COMP".equals(currentLine), "Invalid second line %s".formatted(currentLine));
			
			return Optional.of(pairing);
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
			if (StringUtils.startsWith(currentLine, TRIP_COMPONENTS[0])) {
				return true;
			}
		}
		return false;
	}
}
