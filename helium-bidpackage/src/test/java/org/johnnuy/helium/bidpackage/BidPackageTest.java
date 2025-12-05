package org.johnnuy.helium.bidpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Optional;

import org.johnnuy.helium.bidpackage.parser.BidPackageReader;
import org.johnnuy.helium.bidpackage.reformat.December2025Reformatter;
import org.johnnuy.helium.domain.Pairing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BidPackageTest {

	@Test
	void testParseDecember2025() throws IOException {
		InputStream source = BidPackageTest.class.getClassLoader().getResourceAsStream("december_2025.txt");
		Assertions.assertNotNull(source);
		
		December2025Reformatter reformatter = new December2025Reformatter(
				"december_2025",
				Path.of("./target", "tmp"));
		
		BufferedReader cleaned = reformatter.reformat(new BufferedReader(new InputStreamReader(source)));
		
		BidPackageReader parser = new BidPackageReader(cleaned);
		
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
