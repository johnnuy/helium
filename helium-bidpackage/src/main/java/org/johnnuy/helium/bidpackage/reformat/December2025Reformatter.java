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
 * Stage 1 is used to ensure we have line breaks where needed, when exporting the data from PDF there are missing line breaks
 */
public class December2025Reformatter implements Reformatter {
	
	private String name;
	private Path tmpDir;
	
	private String horizotal = "____________________________________________________________________________________________________";
//	
//	private String[] prefixes = {
//		"____________________________________________________________________________________________________",
//		"TRIP",
//		"MO TU",
//		"-- ",
//		"01 ",
//		"D--",
//		"E--",
//		"C--",
//		"29 ",
//		"--",
//		"TAFB",
//		"_____",
//		"TRIP"
//	};
	
	public December2025Reformatter(String name, Path tmpDir) {
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
				if (!line.startsWith(horizotal) && line.contains(horizotal)) {
					int index = line.indexOf(horizotal);
					writeLine(out, line.substring(0, index));
					writeLine(out, line.substring(index));
				}
				else if (line.startsWith(horizotal) && line.contains("TAFB:")) {
					int index = line.indexOf("TAFB:");
					writeLine(out, line.substring(0, index));
					writeLine(out, line.substring(index));
				}
				else if (line.startsWith(horizotal) && line.contains("TRIP")) {
					int index = line.indexOf("TRIP");
					writeLine(out, line.substring(0, index));
					writeLine(out, line.substring(index));
				}
				else if (line.startsWith("TRIP") && line.contains("MO TU WE")) {
					int index = line.indexOf("MO TU WE");
					writeLine(out, line.substring(0, index));
					writeLine(out, line.substring(index));
				}
				else if (line.startsWith("MO TU WE") && !line.endsWith("CREW COMP")) {
					int index = line.indexOf("CREW COMP");
					writeLine(out, line.substring(0, index + 9));
					writeLine(out, line.substring(index + 9));
				}
				else if ((line.startsWith("-- --") || line.startsWith("01 --")) && line.contains("---------D")) {
					int index = line.indexOf("D");
					writeLine(out, line.substring(0, index));
					writeLine(out, line.substring(index));
				}
				else if ((line.startsWith("E--") || line.startsWith("E15")) && line.contains("]C")) {
					int index = line.indexOf("]C");
					writeLine(out, line.substring(0, index + 1));
					writeLine(out, line.substring(index + 1));
				}
				else if ((line.startsWith("C--") || line.startsWith("C22")) && line.contains("]-")) {
					int index = line.indexOf("]-");
					writeLine(out, line.substring(0, index + 1));
					writeLine(out, line.substring(index + 1));
				}
				else {
					/* check if the line needs to be split */
					writeLine(out, line);
				}
			}
		}
		return Files.newBufferedReader(step1);
	}
}