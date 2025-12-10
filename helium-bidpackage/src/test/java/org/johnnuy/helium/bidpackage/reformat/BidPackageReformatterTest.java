package org.johnnuy.helium.bidpackage.reformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;

import org.johnnuy.helium.bidpackage.BidPackageTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class BidPackageReformatterTest {

	@ParameterizedTest
	@ValueSource(strings = { "yeg", "yqb", "yul", "yvr", "ywg", "yyc", "yyz" })
	void testParseDecember2025(String airport) throws IOException {
		InputStream source = BidPackageTest.class.getClassLoader().getResourceAsStream("foxit/december_2025/%s.txt".formatted(airport));
		Assertions.assertNotNull(source);

		BidPackageReformatterV1 reformatter = new BidPackageReformatterV1(
				"foxit_december_2025_%s".formatted(airport),
				Path.of("./target", "tmp"));

		BufferedReader cleaned = reformatter.reformat(new BufferedReader(new InputStreamReader(source)));
		cleaned.close();
	}
}
