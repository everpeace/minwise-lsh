package org.everpeace.search.lsh.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * data file generator utility.
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class NameFileGen {
	static long seed = 12345L;
	static Random r = new Random(seed);
	private static final int NUM_REG = 3000000;
	public static final String OUTPUTFILENAME = "sample-names.txt";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int count = 0;
		long start = System.currentTimeMillis();

		FileReader in = new FileReader("sei.txt");
		BufferedReader br = new BufferedReader(in);
		List<String> sei = new ArrayList<String>();
		while (br.ready()) {
			sei.add(br.readLine().trim());
		}
		br.close();

		in = new FileReader("mei.txt");
		br = new BufferedReader(in);
		List<String> mei = new ArrayList<String>();

		while (br.ready()) {
			mei.add(br.readLine().trim());
		}
		br.close();

		in = new FileReader("M95_SEI.txt");
		br = new BufferedReader(in);
		List<String> r_sei = new ArrayList<String>();
		while (br.ready()) {
			r_sei.add(br.readLine().trim());
		}
		br.close();

		in = new FileReader("M95_MEI.TXT");
		br = new BufferedReader(in);
		List<String> r_mei = new ArrayList<String>();

		while (br.ready()) {
			r_mei.add(br.readLine().trim());
		}
		br.close();

		// FileWriter out = new FileWriter(OUTPUTFILENAME);
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				new File(OUTPUTFILENAME)), "Shift_JIS");
		BufferedWriter bw = new BufferedWriter(out);

		for (int i = 0; i < NUM_REG; i++) {
			String rname;
			if (r.nextFloat() >= 0.05) {
				rname = randomChoose(sei).trim() + randomChoose(mei).trim();
			} else {
				rname = randomChoose(r_sei).trim() + randomChoose(r_mei).trim();
			}
			bw.write(rname + "\n");
			count++;
			System.out.println(count + ": " + rname);
		}

		long end = System.currentTimeMillis();
		// System.out.println(testee.getIndex());
		System.out.println(count + "names output in " + (end - start)
				+ "[ms]!\n");

	}

	private static String randomChoose(List<String> l) {
		return l.get(r.nextInt(l.size()));
	}
}
