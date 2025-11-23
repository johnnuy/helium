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
import org.springframework.util.Assert;

/**
 * Parser for resolving the pairings from the bid package
 */
public class BidPackageParser implements PairingProvider {

	private static String PAIRING_REGEX = "[T][0-9A-Z]{4}";
	private static Pattern PAIRING_PATTERN = Pattern.compile(PAIRING_REGEX);
	
	private static String TRIP_REGEX = "TRIP #([0-9]+)";
	private static Pattern TRIP_LINE = Pattern.compile("(" + TRIP_REGEX + ")((\\s(" + PAIRING_REGEX + "))?(.*))");
	
	private static String OL_REGEX = "\\([A-Z]{2}\\)";
	private static String COMPLIMENT_REGEX = "\\[[0-9],[0-9],[0-9],[0-9]\\]";
	private static Pattern EFFECTIVE_LINE = Pattern.compile("(" + OL_REGEX + ")\\s(" + COMPLIMENT_REGEX + ")(.*)");
	
	private static String CALENDAR_LINE = "MO TU WE TH FR SA SU DAY FLT#";

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
			if (!PAIRING_PATTERN.matcher(pairingId).matches()) {
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
		reformat_step2();
		reformat_step3();
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
					writeLine(out, m.group(1));
					writeLine(out, m.group(5));
					if (StringUtils.isNotBlank(m.group(6))) {
						writeLine(out, m.group(6));
					}
				} else {
					writeLine(out, line);
				}
			}
		}
		this.reader = Files.newBufferedReader(step1);
	}
	
	/**
	 * Step 2 of the reformatting is used to move some exclusion groupings to the end of the proper exclusions,
	 * they get shifted down occasionally
	 * 
	 * @throws IOException
	 */
	private void reformat_step2() throws IOException {
		Path step2 = tmpDir.resolve("%s_step2.txt".formatted(name));
		Files.createDirectories(step2.getParent());
		try (OutputStream stream = Files.newOutputStream(step2, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING); Writer out = new OutputStreamWriter(stream)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Matcher m = TRIP_LINE.matcher(line);
				if (m.matches()) {
					/* when we get a trip line, we need to check if the next line is a valid pairing or not */
					String secondLine = reader.readLine();
					Assert.notNull(secondLine, "Unable to locate second line for %s".formatted(line));
					Matcher m2 = PAIRING_PATTERN.matcher(secondLine);
					if (m2.matches()) {
						/* all is good, we found our pairing after our trip line, so write both out and continue */
						writeLine(out, line);
						writeLine(out, secondLine);
					}
					else {
						/* here we need to read the further line to ensure it's a trip line */
						String thirdLine = reader.readLine();
						Assert.notNull(thirdLine, "Unable to locate third line for %s".formatted(line));
						Assert.isTrue(PAIRING_PATTERN.matcher(thirdLine).matches(), "Third line for %s is not a pairing line (%s)".formatted(line, thirdLine));
						
						String forthLine = reader.readLine();
						Assert.notNull(forthLine, "Unable to locate forth line for %s".formatted(line));
						
						/* here we need to write out the lines while moving the second line to the end of the forth line */
						writeLine(out, line);
						writeLine(out, thirdLine);
						writeLine(out, forthLine + " " + secondLine);
					}
					
				} else {
					writeLine(out, line);
				}
			}
		}
		this.reader = Files.newBufferedReader(step2);
	}
	
	/**
	 * Step 3 of the reformatting is used to ensure all of the exceptions are on one line before the calendar line
	 * @throws IOException
	 */
	private void reformat_step3() throws IOException {
		Path step3 = tmpDir.resolve("%s_step3.txt".formatted(name));
		Files.createDirectories(step3.getParent());
		try (OutputStream stream = Files.newOutputStream(step3, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING); Writer out = new OutputStreamWriter(stream)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Matcher m = EFFECTIVE_LINE.matcher(line);
				if (m.matches()) {
					/* check if the next line is a calendar line */
					String secondLine = reader.readLine();
					Assert.notNull(secondLine, "Unable to locate second line for %s".formatted(line));
					if (StringUtils.startsWith(secondLine, CALENDAR_LINE)) {
						/* write them out individually */
						writeLine(out, line);
						writeLine(out, secondLine);
					}
					else {
						/* otherwise concatenate */
						writeLine(out, line + " " + secondLine);
					}
					
				} else {
					writeLine(out, line);
				}
			}
		}
		this.reader = Files.newBufferedReader(step3);
	}
	
	private void writeLine(Writer writer, String message) throws IOException {
		writer.write("%s%n".formatted(StringUtils.trim(message)));
	}
}
