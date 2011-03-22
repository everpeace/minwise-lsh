package org.everpeace.search.inverseindex;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.everpeace.search.lsh.GramSigner;
import org.everpeace.search.lsh.NGramGen;

/**
 * inverted index using tree-set.
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class InvertedIndex {
	Map<Long, TreeSet<IndexEntryElement>> index = new TreeMap<Long, TreeSet<IndexEntryElement>>();

	public void putWord(String s) {
		TreeSet<String> grams = NGramGen.generate(s);
		for (String g : grams) {
			IndexEntryElement e = new IndexEntryElement(s);
			Long sign_g = GramSigner.sign(g);
			if (index.get(sign_g) == null) {
				index.put(sign_g, genTreeSet());
			}
			TreeSet<IndexEntryElement> set = index.get(sign_g);
			set.add(e);
			index.put(sign_g, set);
			// System.out.print(g + "=>" + sign_g + " ");
		}
		// System.out.println();
	}

	public TreeSet<IndexEntryElement> getCandidates(String query, int th,
			boolean flag) {
		TreeSet<IndexEntryElement> ret = genTreeSet();
		TreeSet<IndexEntryElement> realret = genTreeSet();
		TreeSet<String> qgrams = NGramGen.generate(query);

		System.out.print("query's " + NGramGen.DIM + "-grams:");
		for (String s : qgrams) {
			System.out.print(s + " (" + GramSigner.sign(s) + "), ");
		}
		System.out.println();
		Long prefix_key = null;

		if (flag) {
			// qk+1 -th prefix
			System.out.print((NGramGen.DIM * th + 1) + "-th prefix key:");
			prefix_key = prefix(NGramGen.DIM * th + 1, qgrams);
		}

		if (prefix_key == null)
			System.out.println("\n");
		else
			System.out.println(prefix_key + "\n");

		for (String qg : qgrams) {
			TreeSet<IndexEntryElement> set = index.get(GramSigner.sign(qg));

			System.out.println("scanned inverted indicies;");
			System.out.print(qg + "(" + GramSigner.sign(qg) + ")" + ":");
			if (set != null) {
				for (IndexEntryElement e : set) {
					System.out.print(e.toString());
				}

			}
			System.out.println();

			// inverted indexに2回以上現れたものだけ入れる。
			if (set == null)
				continue;
			if (prefix_key == null) {
				for (IndexEntryElement e : set) {
					if (ret.contains(e))
						realret.add(e);
					else
						ret.add(e);
				}
			} else {
				int count = 0;
				for (IndexEntryElement e : set) {
					// ここにlength filterを入れる。
					if (e.getSignature() <= prefix_key) {
						if (ret.contains(e)) {
							realret.add(e);
						} else {
							ret.add(e);
							count += 1;
						}
					} else {
						System.out.println(count + "elements filtered in, "
								+ (set.size() - count)
								+ "elements filtered out");
						break;
					}
				}
			}
		}

		return realret;
	}

	public TreeSet<String> getCandidateStrings(String query, int th,
			boolean flag) {
		TreeSet<String> ret = new TreeSet<String>();
		TreeSet<IndexEntryElement> cands = this.getCandidates(query, th, flag);
		for (IndexEntryElement e : cands) {
			ret.add(e.getElement());
		}
		return ret;
	}

	private Long prefix(int i, TreeSet<String> qgrams) {
		TreeSet<Long> qgsigns = GramSigner.sign(qgrams);
		if (qgsigns.size() >= i) {
			int c = 1;
			for (Long s : qgsigns) {
				if (c == i)
					return s;
				else
					c++;
			}
		}
		return null;
	}

	private static TreeSet<IndexEntryElement> genTreeSet() {
		return new TreeSet<IndexEntryElement>(IndexEntryElement.comparator());
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		for (Long key : index.keySet()) {
			System.out.print(key + ": ");
			for (IndexEntryElement e : index.get(key)) {
				System.out.print(e.toString() + " ");
			}
			System.out.println();
		}
		return b.toString();
	}
}
