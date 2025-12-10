package org.johnnuy.helium.bidpackage.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.regex.Matcher;

import org.johnnuy.helium.bidpackage.BidPackageTest;
import org.johnnuy.helium.domain.Pairing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BidPackageReaderTest {

	@Test
	void testTripLineRegexNoExceptions() {
		Matcher m = BidPackageReader.TRIP_LINE.matcher("TRIP #1397 T3501 (OL) [0,0,4,0] YYZ: 1______ effective DEC 08-DEC 08 no exceptions.");
		Assertions.assertTrue(m.matches());
		String[] values = {
				"1397",
				"T3501",
				"OL",
				"[0,0,4,0]",
				"YYZ",
				"1______",
				"DEC 08-DEC 08",
				"no exceptions.",
				null
		};

		for (int g = 1; g <= m.groupCount(); g++) {
			Assertions.assertEquals(m.group(g), values[g-1]);
		}
	}
	
	@Test
	void testTripLineRegexWithExceptions() {
		Matcher m = BidPackageReader.TRIP_LINE.matcher("TRIP #1401 T3505 (OL) [0,0,4,0] YYZ: _23____ effective DEC 02-DEC 17 except DEC 03 DEC 09 DEC 10 DEC 16");
		Assertions.assertTrue(m.matches());
		String[] values = {
				"1401",
				"T3505",
				"OL",
				"[0,0,4,0]",
				"YYZ",
				"_23____",
				"DEC 02-DEC 17",
				"except",
				"DEC 03 DEC 09 DEC 10 DEC 16"
		};

		for (int g = 1; g <= m.groupCount(); g++) {
			Assertions.assertEquals(m.group(g), values[g-1]);
		}
	}
		

	@Test
	void testParseSimplePairingNoExceptions() throws IOException {
		InputStream source = BidPackageTest.class.getClassLoader().getResourceAsStream("pairings/single_no_exceptions.txt");
		Assertions.assertNotNull(source);
		BidPackageReader parser = new BidPackageReader(new BufferedReader(new InputStreamReader(source)));

		Optional<Pairing> pairing;

		int count = 0;
		while ((pairing = parser.nextPairing()).isPresent()) {
			System.out.println(
					"Found pairing for trip #%d with id %s and crew compliment %s".formatted(
							pairing.get().getTripNumber(), 
							pairing.get().getPairingId(), 
							pairing.get().getCrewCompliment()));
			count++;
		}
		System.out.println("Found %d pairings".formatted(count));
	}	
}
