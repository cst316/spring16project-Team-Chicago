package net.sf.memoranda.ui;

/**
 * This file contains the IncrementingRowFilter class.
 */

import javax.swing.RowFilter;

/**
 * An IncremetingRowFilter adds methods for incrementing possible filtered rows
 * for view.
 * 
 * @author Jonathan Hinkle
 *
 * @param <M> A Model
 * @param <I> An Integer
 */
public abstract class IncrementingRowFilter<M, I> extends RowFilter<M, I> {

	private int _count;

	public int getPossibleRows() {
		return _count;
	}

	/**
	 * Increments the possible rows.
	 */
	public void incrementPossibleRows() {
		_count++;
	}

	/**
	 * Resets the possible row counter to 0.
	 */
	public void resetPossibleRowsCounter() {
		_count = 0;
	}

}