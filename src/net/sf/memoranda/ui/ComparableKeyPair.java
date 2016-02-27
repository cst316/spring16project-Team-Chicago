package net.sf.memoranda.ui;

/**
 * This file contains the ComparableKeyPair class.
 */

import javafx.util.Pair;

/**
 * This class extends Pair with a comparable key.
 * 
 * @author Jonathan Hinkle
 *
 * @param <K> A Comparable object
 * @param <V> An object
 */
@SuppressWarnings("serial")
public class ComparableKeyPair<K extends Comparable<K>, V> extends Pair<K, V>
		implements Comparable<ComparableKeyPair<K, V>> {

	/**
	 * Constructor for ComparableKeyPair.
	 * 
	 * @param label A Comparable object
	 * @param value An object
	 */
	public ComparableKeyPair(K label, V value) {
		super(label, value);
	}

	@Override
	public String toString() {
		return getKey().toString();
	}

	@Override
	public int compareTo(ComparableKeyPair<K, V> o) {
		return getKey().compareTo(o.getKey());
	}
}
