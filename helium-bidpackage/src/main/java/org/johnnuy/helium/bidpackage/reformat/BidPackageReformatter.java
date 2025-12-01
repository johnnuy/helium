package org.johnnuy.helium.bidpackage.reformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;

import org.johnnuy.helium.bidpackage.parser.BidPackageHandler;

/**
 * Used to reformat the bid package it can be parsed easily
 */
public class BidPackageReformatter implements BidPackageHandler {

	private ReformattingStage1 stage1;
	private ReformattingStage2 stage2;
	private ReformattingStage3 stage3;
	private ReformattingStage4 stage4;

	/**
	 * Creates a new bid package reformatter
	 * @param name
	 * @param tmpDir
	 */
	public BidPackageReformatter(String name, Path tmpDir) {
		this.stage1 = new ReformattingStage1(name, tmpDir);
		this.stage2 = new ReformattingStage2(name, tmpDir);
		this.stage3 = new ReformattingStage3(name, tmpDir);
		this.stage4 = new ReformattingStage4(name, tmpDir);
	}

	/**
	 * performs the reformatting of each stage in sequence
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public BufferedReader reformat(BufferedReader in) throws IOException {
		return stage4.reformat(
				stage3.reformat(
						stage2.reformat(
								stage1.reformat(in))));
	}
}
