package org.everpeace.search.near_string_search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

import org.everpeace.search.lsh.util.NameFileGen;

/**
 * test application for inverted index of Strings.
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class StringStorageWithInvertedIndexTest {

	private static final int COUNT = 10000;
	StringStorageWithInvertedIndex testee;

//	@Before
	public void setUp() throws Exception {
		testee = new StringStorageWithInvertedIndex();

		long start = System.currentTimeMillis();
		FileReader in = new FileReader(NameFileGen.OUTPUTFILENAME);
		BufferedReader br = new BufferedReader(in);
		int count = 0;
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader bri = new BufferedReader(isr);
		System.out.println("input number of names read: ");
		int limcount = Integer.parseInt(bri.readLine());
		if (limcount < 0)
			limcount = COUNT;
		while (br.ready() && count < limcount) {
			String input = br.readLine();
			testee.register(input.trim());
			count++;
			System.out.println(count + ":register " + input);
		}
		br.close();
		in = new FileReader("data.txt");
		br = new BufferedReader(in);

		while (br.ready()) {
			String input = br.readLine();
			testee.register(input.trim());
			count++;
			System.out.println(count + ":register " + input);
		}
		long end = System.currentTimeMillis();

		System.out.println(count + " names registered in " + (end - start)
				+ "[ms]!\n");
	}

//	@Test
	public final void testGetNearStringFrom() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		while (true) {
			System.out.print("input query: ");
			String query = br.readLine();
			if (query.equals("-1")) {
				br.close();
				return;
			}

			System.out.print("choose distance(0:LevenStein,1:JaroWinkler):");
			int d = 1;
			try {
				d = Integer.parseInt(br.readLine());
			} catch (Exception e) {
				System.out
						.println("invalid input. \"JaroWinkler\" is forcefully selceted.");
			}

			TreeSet<String> ans = new TreeSet<String>();

			long start = 0, end = 0;
			switch (d) {
			case 0:
				System.out.print("input threshold: ");
				int th = 0;
				boolean done = false;
				while (done == false) {
					try {
						th = Integer.parseInt(br.readLine());
						done = true;
					} catch (NumberFormatException e) {
						System.out.println("invalid input.");
						done = true;
					}
				}
				if (th < 0)
					break;

				System.out.println("query:" + query);
				System.out.println("distance type: LevenStein Distance");
				System.out.println("distance threshold:" + th);
				start = System.currentTimeMillis();
				ans = testee.getNearStringFromByLevenStein(query, th);
				end = System.currentTimeMillis();
				break;
			case 1:
				System.out.print("input threshold[0.0--1.0]: ");
				double thr = 0;
				boolean done2 = false;
				while (done2 == false) {
					try {
						thr = Double.parseDouble(br.readLine());
						done2 = true;
					} catch (NumberFormatException e) {
						System.out.println("invalid input.");
						done2 = true;
					}
				}
				if (thr < 0)
					break;

				System.out.println("query:" + query);
				System.out.println("distance type: JaroWinkler Distance");
				System.out.println("distance threshold:" + thr);
				start = System.currentTimeMillis();
				ans = testee.getNearStringFromByJaroWinkler(query, thr);
				end = System.currentTimeMillis();
				break;
			default:
				break;
			}

			System.out.println("search time:" + (end - start) + "[ms]");
			System.out.println("\n" + ans.size() + " canddidates found for \""
					+ query + "\".");
			for (String a : ans) {
				System.out.print(" " + a);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws Exception {
		StringStorageWithInvertedIndexTest test = new StringStorageWithInvertedIndexTest();
		test.setUp();
		test.testGetNearStringFrom();
	}
}
