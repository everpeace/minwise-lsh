package org.everpeace.search.lsh.distance;

import java.util.TreeSet;

import org.everpeace.search.lsh.NGramGen;

/**
 * Jaccard distance function.
 */
public class JaccardDistance {

	public static double calcCoefficient(String s, String t) {
		TreeSet<String> gs = NGramGen.generate(s);
		TreeSet<String> ts = NGramGen.generate(t);

		TreeSet<String> cup = new TreeSet<String>();
		cup.addAll(gs);
		cup.addAll(ts);

		gs.retainAll(ts);

		return ((double) gs.size()) / ((double) cup.size());
	}

	public static double calc(String s, String t) {
		return 1 - calcCoefficient(s, t);
	}
}
