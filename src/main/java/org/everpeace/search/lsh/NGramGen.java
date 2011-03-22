package org.everpeace.search.lsh;

import java.util.TreeSet;

/**
 * n-gram generator.
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class NGramGen {
	public static final int DIM = 2;

	public static TreeSet<String> generate(String s) {
		TreeSet<String> ret = new TreeSet<String>();
		String pad = "";
		for (int i = 0; i < DIM - 1; i++) {
			pad = pad + "*";
		}
		String target = pad + s + pad;

		for (int i = 0; i < s.length() + DIM - 1; i++) {
			ret.add(target.substring(i, i + DIM));
		}
		return ret;
	}
}
