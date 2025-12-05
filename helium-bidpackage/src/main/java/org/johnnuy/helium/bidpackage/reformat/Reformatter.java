package org.johnnuy.helium.bidpackage.reformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.helium.bidpackage.BidPackageHandler;

public interface Reformatter extends BidPackageHandler {

	BufferedReader reformat(BufferedReader reader) throws IOException;
	
	/**
	 * writes the line to the given writer
	 * @param writer
	 * @param message
	 * @throws IOException
	 */
	default void writeLine(Writer writer, String message) throws IOException {
		writer.write("%s%n".formatted(StringUtils.trim(message)));
	}
}
