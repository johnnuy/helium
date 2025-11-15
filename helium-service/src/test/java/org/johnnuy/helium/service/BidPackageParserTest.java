package org.johnnuy.helium.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Optional;

import org.johnnuy.helium.service.domain.Pairing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BidPackageParserTest {

	@Test
	void testParseDecember2025() throws IOException {
		InputStream source = BidPackageParserTest.class.getClassLoader().getResourceAsStream("december_2025.txt");
		Assertions.assertNotNull(source);
		
		BidPackageParser parser = new BidPackageParser(
				"december_2025",
				Path.of("./target", "tmp"),
				new InputStreamReader(source));
		
		Optional<Pairing> pairing;
		
		int count = 0;
		while ((pairing = parser.nextPairing()).isPresent()) {
			System.out.println("Found pairing for trip #%d with id %s".formatted(pairing.get().getTripNumber(), pairing.get().getPairingId()));
			count++;
		}
		System.out.println("Found %d pairings".formatted(count));
	}
}
