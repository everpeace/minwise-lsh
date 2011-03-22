package org.everpeace.search.lsh.distance;

import java.io.UnsupportedEncodingException;

/**
 * levenshtein distance function.
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class LevenshteinDistance {
	public static int calc(String x, String y)
			throws UnsupportedEncodingException {
		int len_x = x.length(), len_y = y.length();
		int[][] row = new int[len_x + 1][len_y + 1];
		int i, j;
		int result;

		for (i = 0; i < len_x + 1; i++)
			row[i][0] = i;
		for (i = 0; i < len_y + 1; i++)
			row[0][i] = i;
		for (i = 1; i <= len_x; ++i) {
			for (j = 1; j <= len_y; ++j) {
				int cost = 0;
				if (x.substring(i - 1, i).equals(y.substring(j - 1, j))) {
					cost = 0;
				} else {
					cost = 1;
				}
				int replace = row[i - 1][j - 1] + cost;// 置き換え
				int delete = row[i][j - 1] + 1;// 削除
				int insert = row[i - 1][j] + 1;// 挿入
				row[i][j] = Math.min(Math.min(replace, delete), insert);
			}
		}
		result = (Integer) (row[len_x][len_y]);

		return result;
	}

	public static int calcIgnoringBlank(String x, String y)
			throws UnsupportedEncodingException {
		String trim_x = x.replaceAll(" ", "");
		String trim_y = y.replaceAll(" ", "");
		return calc(trim_x, trim_y);
	}
}
