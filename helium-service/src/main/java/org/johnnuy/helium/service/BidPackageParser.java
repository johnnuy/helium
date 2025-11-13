package org.johnnuy.helium.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.helium.service.domain.Pairing;

/**
 * Parser for resolving the pairings from the bid package
 */
public class BidPackageParser implements PairingProvider {

	private static Pattern TRIP_LINE = Pattern.compile("(TRIP\s#([0-9]+))(.*)");
	private static Pattern PAIRING_LINE = Pattern.compile("[T][0-9A-Z]{4}");
	
	private BufferedReader reader;
	private Integer currentTrip = null;
	
	public BidPackageParser(Reader reader) {
		this.reader = new BufferedReader(reader);
		try {
			reformat();
			skipToNextPairing();
		}
		catch(IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	@Override
	public Optional<Pairing> nextPairing() {
		if (currentTrip == null) {
			return Optional.empty();
		}
		try {
			Pairing pairing = new Pairing()
					.setTripNumber(currentTrip);
			
			/* next line contains the pairing id */
			String pairingId = reader.readLine();
			if (!PAIRING_LINE.matcher(pairingId).matches()) {
				throw new RuntimeException("Unable to identify pairingId for trip %d".formatted(currentTrip));
			}
			pairing.setPairingId(pairingId);
			
			skipToNextPairing();
			
			return Optional.of(pairing);
		}
		catch(IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	/**
	 * helper method that searches for the next TRIP_LINE for identifying the start of a new pairing
	 * @throws IOException
	 */
	private void skipToNextPairing() throws IOException {
		String currLine = null;
		while ((currLine = reader.readLine()) != null) {
			Matcher m = TRIP_LINE.matcher(currLine);
			if (m.matches()) {
				currentTrip = Integer.parseInt(m.group(2));
				return;
			}
		}
		currentTrip = null;
	}
	
	/**
	 * reformats the file to allow it to parse easily
	 */
	private void reformat() throws IOException {
		StringBuilder buffer = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			Matcher m = TRIP_LINE.matcher(line);
			if (m.matches() && StringUtils.isNotBlank(m.group(3))) {
				/* here we have a mixed line, split this up */			
				buffer.append(m.group(1) + "\n");
				buffer.append(m.group(3) + "\n");
			}
			else {
				buffer.append(line + "\n");
			}
		}
		this.reader = new BufferedReader(new StringReader(buffer.toString()));
	}
}
