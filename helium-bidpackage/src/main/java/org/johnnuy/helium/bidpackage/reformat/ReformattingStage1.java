package org.johnnuy.helium.bidpackage.reformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

/**
 * Step 1 of the reformatting is used to ensure the TRIP # is on it's own line
 * 
 */
public class ReformattingStage1 implements ReformattingStage {
	
	private String name;
	private Path tmpDir;
	
	public ReformattingStage1(String name, Path tmpDir) {
		this.name = name;
		this.tmpDir = tmpDir;
	}

	@Override
	public BufferedReader reformat(BufferedReader in) throws IOException {
		Path step1 = tmpDir.resolve("%s_step1.txt".formatted(name));
		Files.createDirectories(step1.getParent());
		try (OutputStream stream = Files.newOutputStream(step1, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING); 
				Writer out = new OutputStreamWriter(stream)) {
			String line = null;
			while ((line = in.readLine()) != null) {
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
		return Files.newBufferedReader(step1);
	}
}