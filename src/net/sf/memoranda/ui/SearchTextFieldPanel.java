package net.sf.memoranda.ui;
/**
 * This file contains the SearchPanel class.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;

import org.jdesktop.swingx.JXList;

/**
 * This class implements a panel that responds to changes in a JXList. The panel
 * resizes according to filtered items in a JXList up to the maxListDisplay. The
 * panel is anchored to the bottom of a JComponent. The JXList must be using an
 * implementation of IncrementingRowFilter and have a fixed cell height in order
 * to display and filter the list properly.
 * 
 * @author Jonathan Hinkle
 *
 */
@SuppressWarnings("serial")
class SearchTextFieldPanel extends JPanel {

	private int _offsetX;
	private int _offsetY;
	private JComponent _anchor;
	private JXList _list;
	private int _maxVisibleRows;
	private JLayeredPane _pane;

	/**
	 * Constructor for SearchTextFieldPanel.
	 * 
	 * @param list A JXList to be used for searching.
	 * @param anchor The component which the panel will be anchored to.
	 * @param pane A JLayeredPane where the panel will be displayed.
	 * @param maxVisibleRows The maximum number of items the panel will display
	 *            from the list.
	 */
	SearchTextFieldPanel(JXList list, JComponent anchor, JLayeredPane pane, int maxVisibleRows) {
		super();
		_pane = pane;
		_anchor = anchor;
		_list = list;
		_maxVisibleRows = maxVisibleRows;
		_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		_setOffset(new Point(0, _anchor.getHeight()));
		_setAnchorComponentListener();
		_setPairFocusListener();
		_setListRowFilterListener();
		this.setBorder(BorderFactory.createLineBorder(Color.gray));

		_pane.add(this, JLayeredPane.POPUP_LAYER);
		setVisible(false);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		//_list.setVisible(visible);
		if (visible) {
			_updateListView();
			_updateLocation();
		}
	}

	private void _setOffset(Point offset) {
		this._offsetX = offset.x;
		this._offsetY = offset.y;
	}

	private void _setAnchorComponentListener() {
		_anchor.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				_setOffset(new Point(0, _anchor.getHeight()));
				_updateListView();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// If the component is moved
				_updateLocation();
			}

		});
	}

	private void _setPairFocusListener() {
		final FocusListener focusListener = new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {}

			@Override
			public void focusLost(FocusEvent e) {
				if (!_isPairFocused()) {
					setVisible(false);
				}
			}
		};

		_anchor.addFocusListener(focusListener);
		this.addFocusListener(focusListener);
	}

	private boolean _isPairFocused() {
		boolean focused = false;
		if (_list.isFocusOwner() || _anchor.isFocusOwner()) {
			focused = true;
		}
		return focused;
	}

	private void _setListRowFilterListener() {
		_list.getRowSorter().addRowSorterListener(new RowSorterListener() {

			@Override
			public void sorterChanged(RowSorterEvent e) {
				if (e.getType() == RowSorterEvent.Type.SORTED) {
					_updateListView();
				}
			}

		});
	}

	private void _updateLocation() {
		if (_anchor.isVisible() && isVisible()) {
			Point point = _anchor.getLocation();
			point = SwingUtilities.convertPoint(_anchor.getParent(), point, _pane);
			point.x = point.x + _offsetX;
			point.y = point.y + _offsetY;
			setLocation(point);
		}
	}

	@SuppressWarnings("rawtypes")
	private void _updateListView() {
		if (isVisible()) {
			final IncrementingRowFilter filter = (IncrementingRowFilter) _list.getRowFilter();
			int displayedRows = _maxVisibleRows;
			if (filter != null) {
				// Set the panel size based on number of possible rows up to
				// maximum viewable rows
				final int possibleRows = filter.getPossibleRows();
				if (possibleRows < _maxVisibleRows) {
					displayedRows = possibleRows;
				}
				// Reset the row counter for the next filtering
				filter.resetPossibleRowsCounter();
			}
			_list.setVisibleRowCount(displayedRows);
			_list.setPreferredSize(new Dimension(_anchor.getWidth(), displayedRows * _list.getFixedCellHeight()));
			this.setSize(_list.getPreferredSize());
			_pane.moveToFront(this);
		}
	}
}
