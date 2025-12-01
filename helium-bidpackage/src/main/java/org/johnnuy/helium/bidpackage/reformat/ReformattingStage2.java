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

import org.springframework.util.Assert;

/**
 * Step 2 of the reformatting is used to move some exclusion groupings to the
 * end of the proper exclusions, they get shifted down occasionally
 * 
 */
public class ReformattingStage2 implements ReformattingStage {
	
	private String name;
	private Path tmpDir;
	
	public ReformattingStage2(String name, Path tmpDir) {
		this.name = name;
		this.tmpDir = tmpDir;
	}

	@Override
	public BufferedReader reformat(BufferedReader in) throws IOException {
		Path step2 = tmpDir.resolve("%s_step2.txt".formatted(name));
		Files.createDirectories(step2.getParent());
		try (OutputStream stream = Files.newOutputStream(step2, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING); Writer out = new OutputStreamWriter(stream)) {
			String line = null;
			while ((line = in.readLine()) != null) {
				Matcher m = TRIP_LINE.matcher(line);
				if (m.matches()) {
					/*
					 * when we get a trip line, we need to check if the next line is a valid pairing 
					 */
					String secondLine = in.readLine();
					Assert.notNull(secondLine, "Unable to locate second line for %s".formatted(line));
					Matcher m2 = PAIRING_PATTERN.matcher(secondLine);
					if (m2.matches()) {
						/*
						 * all is good, we found our pairing after our trip line, so write both out and
						 * continue
						 */
						writeLine(out, line);
						writeLine(out, secondLine);
						
						/* check the third and forth lines for proper effective ordering, sometimes effective dates are before (1414 in dec 25) */
						String thirdLine = in.readLine();
						Assert.notNull(thirdLine, "Unable to locate third line for %s".formatted(line));
						if (!EFFECTIVE_LINE_REGEX.matcher(thirdLine).matches()) {
							String forthLine = in.readLine();
							Assert.notNull(forthLine, "Unable to locate forth line for %s".formatted(line));
							Assert.isTrue(EFFECTIVE_LINE_REGEX.matcher(forthLine).matches(), "Forth line for %s is not an effectline line (%s)".formatted(line, forthLine));
							/* invert */
							writeLine(out, forthLine + " " + thirdLine);
						}
						else {
							writeLine(out, thirdLine);
						}
						
					} else {
						/* here we need to read the further line to ensure it's a trip line */
						String thirdLine = in.readLine();
						Assert.notNull(thirdLine, "Unable to locate third line for %s".formatted(line));
						Assert.isTrue(PAIRING_PATTERN.matcher(thirdLine).matches(), "Third line for %s is not a pairing line (%s)".formatted(line, thirdLine));

						String forthLine = in.readLine();
						Assert.notNull(forthLine, "Unable to locate forth line for %s".formatted(line));

						/*
						 * here we need to write out the lines while moving the second line to the end
						 * of the forth line
						 */
						writeLine(out, line);
						writeLine(out, thirdLine);
						writeLine(out, forthLine + " " + secondLine);
					}

				} else {
					writeLine(out, line);
				}
			}
		}
		return Files.newBufferedReader(step2);
	}
}