package org.everpeace.search.near_string_search;

import java.io.UnsupportedEncodingException;
import java.util.TreeSet;

import org.everpeace.search.inverseindex.InvertedIndex;
import org.everpeace.search.lsh.distance.JaroWinklerDistance;
import org.everpeace.search.lsh.distance.LevenshteinDistance;

/**
 * Inverted Index of Strings.
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class StringStorageWithInvertedIndex {
	InvertedIndex index;

	public InvertedIndex getIndex() {
		return index;
	}

	public StringStorageWithInvertedIndex() {
		super();
		index = new InvertedIndex();
	}

	public void register(String s) {
		index.putWord(s);
	}

	public TreeSet<String> getNearStringFromByLevenStein(String query, int th)
			throws UnsupportedEncodingException {
		TreeSet<String> ret = new TreeSet<String>();
		TreeSet<String> cands = index.getCandidateStrings(query, th, true);
		for (String cand : cands) {
			// System.out.println(cand + " ");
			int d = LevenshteinDistance.calc(query, cand);
			System.out.print(cand + "<->" + query + ":" + d);
			if (d <= th) {
				ret.add(cand);
				System.out.println(" <<<FOUND!!");
			}
			System.out.println();
		}
		System.out.println(cands.size() + " words scanned.");
		return ret;
	}

	public TreeSet<String> getNearStringFromByJaroWinkler(String query,
			double th) throws UnsupportedEncodingException {
		TreeSet<String> ret = new TreeSet<String>();
		TreeSet<String> cands = index.getCandidateStrings(query, -1, false);
		System.out.println("scanned words:");
		for (String cand : cands) {
			// System.out.print(cand + " ");
			double d = JaroWinklerDistance.JARO_WINKLER_DISTANCE.distance(
					query, cand);
			System.out.print(cand + "<->" + query + ":" + d);
			if (d <= th) {
				ret.add(cand);
				System.out.print(" <<<FOUND!!");
			}
			System.out.println();
		}
		System.out.println("\n" + cands.size() + " words scanned.");
		return ret;
	}
}
