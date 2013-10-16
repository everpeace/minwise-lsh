package org.everpeace.search.near_string_search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

import org.everpeace.search.lsh.util.NameFileGen;

/**
 * test application for LSH tables.
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class StringStrageWithLSHashTableTest {
	private static final int COUNT = 10000;
	private static final long[][] SEEDS = {
			{ 1260851439L, 861534349L, 2395240291L, 2599523806L, 1367579008L,
					96800422L },
			{ 1380212987L, 3354726954L, 3115534740L, 1977213931L, 1479742734L,
					922619828L },
			{ 666730981L, 1617030557L, 3595812276L, 2042030471L, 2688817699L,
					64989983L },
			{ 1312628505L, 271436686L, 4095332428L, 3005538173L, 3300023017L,
					253416192L },
			{ 2662292292L, 4260258559L, 1751258183L, 339858601L, 2125443673L,
					1886229088L },
			{ 3326940266L, 1937119901L, 3004790716L, 3687236182L, 3652224461L,
					3099559567L },
			{ 1971307895L, 920382546L, 3315402915L, 1108585733L, 1056346947L,
					1905784167L },
			{ 257201055L, 2694593900L, 4174070009L, 1285838164L, 3216278895L,
					3369481960L },
			{ 1130255504L, 3620618635L, 3464100623L, 4102717072L, 1423451226L,
					2489297303L },
			{ 2016248254L, 3977432677L, 3900888879L, 3503193688L, 2145436452L,
					266495998L },
			{ 458358064L, 3799840290L, 240422999L, 2198955840L, 626907114L,
					2748408562L },
			{ 817923486L, 679063373L, 3352882650L, 3620040313L, 1177428255L,
					1538904824L },
			{ 3129899781L, 1852001570L, 2085768453L, 293004975L, 2424253612L,
					2067389707L },
			{ 2808351202L, 2372539207L, 394141297L, 2469501324L, 3549415730L,
					1364257899L },
			{ 2192334091L, 1934017447L, 1816041498L, 4064744893L, 4037130048L,
					543412150L },
			{ 3241270510L, 1371285834L, 264090989L, 1036680073L, 1118828387L,
					4249834349L },
			{ 1284534426L, 652450901L, 1867603841L, 2536214964L, 1034732289L,
					518350784L },
			{ 1222149314L, 393831694L, 3935797912L, 1159425591L, 2812259531L,
					3260241180L },
			{ 2440971866L, 1760130795L, 1755481556L, 2982455451L, 3920970307L,
					1337450858L },
			{ 841437558L, 3080653575L, 3596450788L, 463179782L, 676559043L,
					739114414L }, };

	StringStrageWithLSHashTable testee;

	// @Before
	public void setUp() throws Exception {
		testee = new StringStrageWithLSHashTable(SEEDS);

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

//		in = new FileReader("data.txt");
//		br = new BufferedReader(in);
//
//		while (br.ready()) {
//			String input = br.readLine();
//			testee.register(input.trim());
//			count++;
//			System.out.println(count + ":register " + input);
//		}
		long end = System.currentTimeMillis();

		System.out.println(count + " names registered in " + (end - start)
				+ "[ms]!\n");

		FileWriter wr = new FileWriter(new File("tables.out"));
		BufferedWriter bw = new BufferedWriter(wr);
		bw.write(testee.toString());
		bw.close();
	}

	// @Test
	public void test() throws IOException {
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
				System.out.print("input threshold[0.0--1.0]: ");
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
				System.out.println("distance type: LevenStein Distance");
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
		StringStrageWithLSHashTableTest test = new StringStrageWithLSHashTableTest();
		test.setUp();
		test.test();
	}
}
