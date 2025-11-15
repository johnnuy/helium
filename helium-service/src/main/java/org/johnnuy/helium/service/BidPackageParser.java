package org.johnnuy.helium.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

	private String name;
	private Path tmpDir;
	private BufferedReader reader;
	private Integer currentTrip = null;

	public BidPackageParser(String name, Path tmpDir, Reader reader) {
		this.name = name;
		this.tmpDir = tmpDir;
		try {
			Files.createDirectories(tmpDir);
			this.reader = new BufferedReader(reader);
			
			reformat();
			skipToNextPairing();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	@Override
	public Optional<Pairing> nextPairing() {
		if (currentTrip == null) {
			return Optional.empty();
		}
		try {
			Pairing pairing = new Pairing().setTripNumber(currentTrip);

			/* next line contains the pairing id */
			String pairingId = reader.readLine();
			if (!PAIRING_LINE.matcher(pairingId).matches()) {
				throw new RuntimeException("Unable to identify pairingId for trip %d".formatted(currentTrip));
			}
			pairing.setPairingId(pairingId);

			skipToNextPairing();

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
	 * prepares the file for parsing by reformatting it properly
	 * 
	 * @throws IOException
	 */
	private void reformat() throws IOException {
		reformat_step1();
	}

	/**
	 * Step 1 of the reformatting is used to ensure the TRIP # is on it's own line
	 * 
	 * @throws IOException
	 */
	private void reformat_step1() throws IOException {
		Path step1 = tmpDir.resolve("%s_step1.txt".formatted(name));
		Files.createDirectories(step1.getParent());
		try (OutputStream stream = Files.newOutputStream(step1, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING); Writer out = new OutputStreamWriter(stream)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Matcher m = TRIP_LINE.matcher(line);
				if (m.matches() && StringUtils.isNotBlank(m.group(3))) {
					/* here we have a mixed line, split this up */
					out.write(m.group(1) + "\n");
					out.write(m.group(3) + "\n");
				} else {
					out.write(line + "\n");
				}
			}
		}
		this.reader = Files.newBufferedReader(step1);
	}
}
