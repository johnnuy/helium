package org.johnnuy.helium.bidpackage.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import org.johnnuy.helium.bidpackage.BidPackageTest;
import org.johnnuy.helium.domain.Pairing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BidPackageReaderTest {

	@Test
	void testParseSimplePairing() throws IOException {
		InputStream source = BidPackageTest.class.getClassLoader().getResourceAsStream("pairings/simple.txt");
		Assertions.assertNotNull(source);		
		BidPackageReader parser = new BidPackageReader(new BufferedReader(new InputStreamReader(source)));
		
		Optional<Pairing> pairing;
		
		int count = 0;
		while ((pairing = parser.nextPairing()).isPresent()) {
			System.out.println(
					"Found pairing for trip #%d with id %s and crew compliment %s".formatted(pairing.get().getTripNumber(), pairing.get().getPairingId(), pairing.get().getCrewCompliment()));
			count++;
		}
		System.out.println("Found %d pairings".formatted(count));
	}
}
