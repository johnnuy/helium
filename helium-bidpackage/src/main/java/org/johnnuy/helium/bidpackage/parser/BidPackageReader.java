package org.johnnuy.helium.bidpackage.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.helium.bidpackage.BidPackageHandler;
import org.johnnuy.helium.domain.CrewCompliment;
import org.johnnuy.helium.domain.Pairing;
import org.johnnuy.helium.domain.PairingProvider;
import org.springframework.util.Assert;

/**
 * Parser for resolving the pairings from the bid package
 */
public class BidPackageReader implements PairingProvider, BidPackageHandler {
	
	private BufferedReader reader;

	public BidPackageReader(BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public Optional<Pairing> nextPairing() {
		try {
			/* check if there's any more pairings to go */
			Optional<Integer> trip = skipToNextPairing();
			if (trip.isEmpty()) {
				return Optional.empty();
			}

			/* if we found a trip id, then build a pairing from it */
			Pairing pairing = trip.map(t -> new Pairing().setTripNumber(t)).get();

			/* second line is for the pairing */
			String currLine = reader.readLine();
			Matcher currMatcher = PAIRING_PATTERN.matcher(currLine);
			Assert.isTrue(currMatcher.matches(), "Unable to identify pairingId for trip %d".formatted(pairing.getTripNumber()));
			pairing.setPairingId(currMatcher.group(1));

			/* third line contains the crew compliment, and the effective days */
			currLine = reader.readLine();
			currMatcher = EFFECTIVE_LINE_REGEX.matcher(currLine);
			Assert.isTrue(currMatcher.matches(), "Unable to parse effective line (%s) for trip %d".formatted(currLine, pairing.getTripNumber()));
			pairing.setCrewCompliment(new CrewCompliment()
					.setCaptains(Integer.parseInt(currMatcher.group(3)))
					.setOfficers(Integer.parseInt(currMatcher.group(4)))
					.setFlightAttendants(Integer.parseInt(currMatcher.group(5)))
					.setOther(Integer.parseInt(currMatcher.group(6))));

			/* forth line is a calendar line, nothing here we need */
			currLine = reader.readLine();
			Assert.isTrue(StringUtils.startsWith(currLine, CALENDAR_DAYS), "Unable to identifiy calendar line for trip %d".formatted(pairing.getTripNumber()));
						

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
	private Optional<Integer> skipToNextPairing() throws IOException {
		String currLine = null;
		while ((currLine = reader.readLine()) != null) {
			Matcher m = TRIP_LINE.matcher(currLine);
			if (m.matches()) {
				return Optional.of(Integer.parseInt(m.group(2)));
			}
		}
		return Optional.empty();
	}

}
