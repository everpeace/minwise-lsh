package org.everpeace.search.lsh.distance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.everpeace.search.lsh.distance.LevenshteinDistance;

/**
 * levenshtein distance calcumator app.
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class LevenshteinDistanceCalculator {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		while (true) {
			BufferedReader d = new BufferedReader(new InputStreamReader(
					System.in, "UTF-8"), 1);
			System.out.print("編集前文字列: ");
			System.out.flush();
			String x = d.readLine();
			System.out.println("#x" + x);
			if (x == null)
				break;

			System.out.print("編集後文字列: ");
			System.out.flush();
			String y = d.readLine();
			System.out.println("#y" + y);
			if (y == null)
				break;

			System.out.println("編集距離は " + LevenshteinDistance.calc(x, y)
					+ " です。");
			System.out.println("空白を無視した編集距離は "
					+ LevenshteinDistance.calcIgnoringBlank(x, y) + " です。\n\n");
		}

	}
}
