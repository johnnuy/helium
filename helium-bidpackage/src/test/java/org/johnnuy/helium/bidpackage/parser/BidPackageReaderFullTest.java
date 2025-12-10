package org.johnnuy.helium.bidpackage.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Optional;

import org.johnnuy.helium.bidpackage.BidPackageTest;
import org.johnnuy.helium.bidpackage.reformat.BidPackageReformatterV1;
import org.johnnuy.helium.domain.Pairing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class BidPackageReaderFullTest {
	
	@ParameterizedTest
	@ValueSource(strings = { "yeg", "yqb", "yul", "yvr", "ywg", "yyc", "yyz" })
	void testParseDecember2025BidPackage(String airport) throws IOException {
		InputStream source = BidPackageTest.class.getClassLoader().getResourceAsStream("foxit/december_2025/%s.txt".formatted(airport));
		Assertions.assertNotNull(source);
		BidPackageReformatterV1 reformatter = new BidPackageReformatterV1(
				"foxit_december_2025_%s".formatted(airport),
				Path.of("./target", "tmp"));
		
		BufferedReader cleaned = reformatter.reformat(new BufferedReader(new InputStreamReader(source)));
		BidPackageReader parser = new BidPackageReader(cleaned);
		
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
		System.out.println("Found %d pairings for %s".formatted(count, airport));
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "yeg", "yqb", "yul", "yvr", "ywg", "yyc", "yyz" })
	void testParseJanuary2026BidPackage(String airport) throws IOException {
		InputStream source = BidPackageTest.class.getClassLoader().getResourceAsStream("foxit/january_2026/%s.txt".formatted(airport));
		Assertions.assertNotNull(source);
		BidPackageReformatterV1 reformatter = new BidPackageReformatterV1(
				"foxit_january_2026_%s".formatted(airport),
				Path.of("./target", "tmp"));
		
		BufferedReader cleaned = reformatter.reformat(new BufferedReader(new InputStreamReader(source)));
		BidPackageReader parser = new BidPackageReader(cleaned);
		
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
		System.out.println("Found %d pairings for %s".formatted(count, airport));
	}
}
