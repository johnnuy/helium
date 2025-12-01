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
import org.springframework.util.Assert;

/**
 * Step 3 of the reformatting is used to ensure all of the exceptions are on one
 * line before the calendar line
 * 
 */
public class ReformattingStage3 implements ReformattingStage {
	
	private String name;
	private Path tmpDir;
	
	public ReformattingStage3(String name, Path tmpDir) {
		this.name = name;
		this.tmpDir = tmpDir;
	}

	@Override
	public BufferedReader reformat(BufferedReader in) throws IOException {
		Path step3 = tmpDir.resolve("%s_step3.txt".formatted(name));
		Files.createDirectories(step3.getParent());
		try (OutputStream stream = Files.newOutputStream(step3, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING); Writer out = new OutputStreamWriter(stream)) {
			String line = null;
			while ((line = in.readLine()) != null) {
				Matcher m = EFFECTIVE_LINE_REGEX.matcher(line);
				if (m.matches()) {
					/* sometimes the calendar line is appended to the end of the effective line */
					if (line.contains(CALENDAR_DAYS)) {
						int index = line.indexOf(CALENDAR_DAYS);
						writeLine(out, line.substring(0, index));
						writeLine(out, line.substring(index));
					}
					else {
						/* check if the next line is a calendar line */
						String secondLine = in.readLine();
						Assert.notNull(secondLine, "Unable to locate second line for %s".formatted(line));
						if (StringUtils.startsWith(secondLine, CALENDAR_DAYS)) {
							/* write them out individually */
							writeLine(out, line);
							writeLine(out, secondLine);
						} 
						else {
							/* otherwise concatenate */
							writeLine(out, line + " " + secondLine);
						}
					}

				} else {
					writeLine(out, line);
				}
			}
		}
		return Files.newBufferedReader(step3);
	}
}