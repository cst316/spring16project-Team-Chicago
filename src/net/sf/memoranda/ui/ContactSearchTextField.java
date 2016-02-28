package net.sf.memoranda.ui;

/**
 * This file contains the ContactSearchTextField class.
 */

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;
import javafx.util.Pair;
import net.sf.memoranda.Contact;
import net.sf.memoranda.ContactManager;

/**
 * A Contact Search JTextField which is used for searching and selecting
 * contacts from a list of contacts based on first and last name.
 * 
 * @author Jonathan Hinkle
 *
 */
@SuppressWarnings("serial")
public class ContactSearchTextField extends JTextField {

	private SearchTextFieldPanel _searchPanel;
	private DefaultListModel<ComparableKeyPair<String, Contact>> _contactsListModel;
	private NameFilter<DefaultListModel<?>, Integer> _searchListFilter;
	private JXList _contactsList;
	private JLayeredPane _layeredPane;
	private ComparableKeyPair<String, Contact> _selection = null;

	/**
	 * Constructs the ContactSearchTextField.
	 * 
	 * @param layeredPane A layered pane which will hold the search list
	 * @param maxContactsShown The maximum number of contacts to be displayed in
	 *            the search.
	 */
	public ContactSearchTextField(JLayeredPane layeredPane, int maxContactsShown) {
		super();

		_layeredPane = layeredPane;

		_contactsListModel = _createContactsListModel();

		_contactsList = new JXList(_contactsListModel, true);
		_contactsList.setFixedCellHeight(25);

		_searchPanel = new SearchTextFieldPanel(_contactsList, this, _layeredPane, 10);
		_searchPanel.setLayout(new BorderLayout());
		_searchPanel.add(_contactsList, BorderLayout.CENTER);

		_searchListFilter = new NameFilter<DefaultListModel<?>, Integer>();

		_setSelectionListener();
		_setDocumentListener();
	}

	public JXList getList() {
		return _contactsList;
	}

	public ComparableKeyPair<String, Contact> getSelection() {
		return _selection;
	}

	private static DefaultListModel<ComparableKeyPair<String, Contact>> _createContactsListModel() {
		final DefaultListModel<ComparableKeyPair<String, Contact>> contactsListModel = new DefaultListModel<ComparableKeyPair<String, Contact>>();

		// Populate the contacts list model
		final Vector<ComparableKeyPair<String, Contact>> contactsVector = _wrapContactsInPairs();
		for (int i = 0; i < contactsVector.size(); i++) {
			contactsListModel.addElement(contactsVector.get(i));
		}

		return contactsListModel;
	}

	@SuppressWarnings("unchecked")
	private static <E extends ComparableKeyPair<?, ?>> Vector<E> _wrapContactsInPairs() {
		final ArrayList<Contact> contacts = ContactManager.getAllContacts();
		final Vector<ComparableKeyPair<String, Contact>> comboContacts = new Vector<ComparableKeyPair<String, Contact>>();
		final Iterator<Contact> contactI = contacts.iterator();

		// Add Pairs (the contacts first/last name as the key, and the contact
		// itself as the value) to the new vector
		for (Contact contact; contactI.hasNext();) {
			contact = contactI.next();
			final String name = contact.getFirstName()
					+ (contact.getLastName().length() > 0 ? " " + contact.getLastName() : "");
			comboContacts.add(new ComparableKeyPair<String, Contact>(name, contact));
		}

		// Sort the contacts
		comboContacts.sort(new Comparator<ComparableKeyPair<String, Contact>>() {

			@Override
			public int compare(ComparableKeyPair<String, Contact> o1, ComparableKeyPair<String, Contact> o2) {
				return o1.compareTo(o2);
			}

		});

		return (Vector<E>) comboContacts;
	}

	private void _setSelectionListener() {
		_contactsList.addListSelectionListener(new ListSelectionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChanged(ListSelectionEvent e) {
				final ComparableKeyPair<?, ?> pair = (ComparableKeyPair<?, ?>) _contactsList.getSelectedValue();
				if (pair != null) {
					_contactsList.clearSelection();
					_selection = (ComparableKeyPair<String, Contact>) pair;
					setText(pair.toString());
					_searchPanel.setVisible(false);
				}
				else if (_searchPanel.isVisible()) {
					_selection = null;
				}
			}
		});
	}

	private void _setDocumentListener() {
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						_togglePanel();
					}
				});
			}

		});
	}

	private void _togglePanel() {
		if (getText().length() > 0) {
			if (!_searchPanel.isVisible()) {
				_searchPanel.setVisible(true);
			}
			_searchListFilter._filterString = getText();
			_contactsList.setRowFilter(_searchListFilter);
		}
		else {
			_searchPanel.setVisible(false);
		}
	}

	private class NameFilter<E extends DefaultListModel<?>, I> extends IncrementingRowFilter<E, I> {

		private String _filterString = "";

		@Override
		public boolean include(javax.swing.RowFilter.Entry<? extends E, ? extends I> entry) {
			boolean matches = false;
			final DefaultListModel<?> ctModel = (DefaultListModel<?>) entry.getModel();
			final int i = ((Integer) entry.getIdentifier()).intValue();
			final String contact = ((String) ((Pair<?, ?>) ctModel.getElementAt(i)).getKey()).toLowerCase();
			if (contact.startsWith(_filterString.toLowerCase())) {
				incrementPossibleRows();
				matches = true;
			}
			return matches;
		}
	}
}
