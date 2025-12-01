package org.johnnuy.helium.bidpackage.reformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Step 3 of the reformatting is used to ensure all of the exceptions are on one
 * line before the calendar line
 * 
 */
public class ReformattingStage4 implements ReformattingStage {
	
	private String name;
	private Path tmpDir;
	
	public ReformattingStage4(String name, Path tmpDir) {
		this.name = name;
		this.tmpDir = tmpDir;
	}

	@Override
	public BufferedReader reformat(BufferedReader in) throws IOException {
		Path step4 = tmpDir.resolve("%s_step4.txt".formatted(name));
		Files.createDirectories(step4.getParent());
		try (OutputStream stream = Files.newOutputStream(step4, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING); Writer out = new OutputStreamWriter(stream)) {
			String line = null;
			while ((line = in.readLine()) != null) {
				if (line.contains(CREW_COMP) && !line.endsWith(CREW_COMP)) {
					int index = line.indexOf(CREW_COMP);
					writeLine(out, line.substring(0, index + CREW_COMP.length()));
					writeLine(out, line.substring(index + CREW_COMP.length()));
				}
				else {
					writeLine(out, line);
				}
			}
		}
		return Files.newBufferedReader(step4);
	}
}