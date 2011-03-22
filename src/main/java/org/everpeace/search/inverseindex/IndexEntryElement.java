package org.everpeace.search.inverseindex;

import java.util.Comparator;
import java.util.TreeSet;

import org.everpeace.search.lsh.GramSigner;
import org.everpeace.search.lsh.NGramGen;

/**
 * entry element of inverted index.
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 */
public class IndexEntryElement {
	final String element;
	final Long signature;

	/**
	 * @param element
	 */
	public IndexEntryElement(String element) {
		super();
		this.element = element;
		TreeSet<String> grams = NGramGen.generate(element);
		TreeSet<Long> signs = new TreeSet<Long>();
		for (String g : grams) {
			signs.add(GramSigner.sign(g));
		}
		signature = signs.first();
	}

	public String getElement() {
		return element;
	}

	public Long getSignature() {
		return signature;
	}

	public String toString() {
		return this.element + "(" + this.signature + ")";
	}

	public static Comparator<IndexEntryElement> comparator() {
		return new Comparator<IndexEntryElement>() {
			@Override
			public int compare(IndexEntryElement o1, IndexEntryElement o2) {
				if (o1.getElement().equals(o2.getElement()))
					return 0;
				if (o1.getSignature() - o2.getSignature() != 0)
					return Long.signum(o1.getSignature() - o2.getSignature());
				return Integer.signum(o1.getElement().hashCode()
						- o2.getElement().hashCode());
			}
		};
	}
}
