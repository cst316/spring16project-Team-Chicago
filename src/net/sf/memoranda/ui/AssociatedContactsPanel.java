package net.sf.memoranda.ui;

/**
 * This file includes an AssociatedContactPanel class which is used in
 * associating contacts with other components.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import net.sf.memoranda.Contact;
import net.sf.memoranda.ContactManager;

/**
 * This class is used to create a JPanel which can be imported into a JComponent
 * to be used for filtering and associating contacts with another component.
 * 
 * @author Jonathan Hinkle
 *
 */
@SuppressWarnings("serial")
public class AssociatedContactsPanel extends JPanel {

	private Hashtable<String, JPanel> _contactPanels = new Hashtable<String, JPanel>();
	private ContactSearchTextField _searchTextField;
	private JButton _addContactButton;
	private JPanel _contactPanel = new JPanel(new BorderLayout());
	private boolean _populating = false;
	private AssociatedContactObservable _observable = new AssociatedContactObservable();
	private int _listItemHeight = 15;

	/**
	 * Initializes the AssociatedContactsPanel. This must be called before the
	 * object can be used. The JLayeredPane is the pane in which the search bar
	 * will be displayed.
	 * 
	 * @param pane A JLayeredPane
	 */
	public void initialize(JLayeredPane pane) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		_searchTextField = new ContactSearchTextField(pane, 10);
		_addContactButton = new JButton();
		_addContactButton.setPreferredSize(new Dimension(24, 24));
		_addContactButton.setText("+");
		_addContactButton.setBorder(null);
		_addContactButton.setBorderPainted(false);
		_addContactButton.setFocusPainted(false);
		_addContactButton.setContentAreaFilled(true);
		_addContactButton.setBackground(new Color(90, 179, 34));

		_setAddContactButtonListener();

		_contactPanel.add(_addContactButton, BorderLayout.WEST);
		_contactPanel.add(_searchTextField, BorderLayout.CENTER);

		this.add(_contactPanel);
	}

	/**
	 * Populates the panel with a list of contacts by id.
	 * 
	 * @param ids An array of contact ids.
	 */
	public void populateContactPanel(String[] ids) {
		_populating = true;

		for (int i = 0; i < ids.length; i++) {
			final Contact contact = ContactManager.getContact(ids[i]);
			if (contact != null) {
				final String name = contact.getFirstName()
						+ (contact.getLastName().length() > 0 ? " " + contact.getLastName() : "");
				final ComparableKeyPair<String, Contact> pair = new ComparableKeyPair<String, Contact>(name, contact);
				_addContact(pair);
			}
		}

		_populating = false;
		_fireObserverNotification();
	}

	public Enumeration<String> getContactIDs() {
		return _contactPanels.keys();
	}

	public Observable getObservable() {
		return _observable;
	}

	public ContactSearchTextField getInternalTextField() {
		return _searchTextField;
	}

	public void setListItemHeight(int i) {
		if (i < 0) {
			i = 0;
		}
		_listItemHeight = i;
	}

	private void _setAddContactButtonListener() {
		_addContactButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && _addContactButton.isEnabled()) {
					final ComparableKeyPair<String, Contact> pair = _searchTextField.getSelection();
					if (pair != null) {
						_addContact(pair);
						_searchTextField.setText("");
					}
				}
			}

		});
	}

	private void _addContact(ComparableKeyPair<String, Contact> pair) {
		final String id = pair.getValue().getID();

		// Create a contact list item and add to the panel if the contact is not
		// already represented, then shift the search box to the bottom of the
		// list.
		if (!_contactPanels.containsKey(id)) {
			final JPanel panel = _createContactListItem(pair);
			panel.setVisible(true);
			this.add(panel);
			_contactPanels.put(pair.getValue().getID(), panel);
			_shiftContactTextField();
		}
	}

	private JPanel _createContactListItem(ComparableKeyPair<String, Contact> pair) {

		final Contact contact = pair.getValue();

		// Wrap a Label and an associated remove button inside a JPanel and
		// return.
		final JLabel label = new JLabel(pair.getKey());
		label.setPreferredSize(new Dimension((int) label.getPreferredSize().getWidth(), _listItemHeight));
		label.setVisible(true);
		label.setBorder(new EmptyBorder(0, 5, 0, 0));
		label.setToolTipText("<html>" + "<b>First Name:</b>   " + contact.getFirstName() + "<br>"
				+ "<b>Last Name:</b>   " + contact.getLastName() + "<br>" + "<b>Email Address:</b>   "
				+ contact.getEmailAddress() + "<br>" + "<b>Phonenumber:</b>   " + contact.getPhoneNumber() + "<br>"
				+ "<b>Organization:</b>   " + contact.getOrganization() + "<br>" + "</html>");

		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

		final JButton removeButton = new JButton("X");
		final int size = (int) label.getPreferredSize().getHeight();
		removeButton.setPreferredSize(new Dimension(size, size));
		removeButton.setFocusPainted(false);
		removeButton.setContentAreaFilled(false);
		removeButton.setBorderPainted(false);
		removeButton.setVisible(true);
		removeButton.setBorder(null);

		removeButton.addMouseListener(new MouseAdapter() {

			private String _id = pair.getValue().getID();

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					_removeContact(_id);
				}
			}
		});

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.gray));
		panel.add(removeButton, BorderLayout.WEST);
		panel.add(label, BorderLayout.CENTER);

		return panel;
	}

	private void _fireObserverNotification() {
		// Notifies observers when the list of contact items has changed.
		_observable.setChanged();
		_observable.notifyObservers();
	}

	private void _shiftContactTextField() {
		// Shifts the search text field to the bottom of the list.
		this.remove(_contactPanel);
		this.add(_contactPanel);
		if (!_populating) {
			_fireObserverNotification();
		}
	}

	private void _removeContact(String id) {
		if (_contactPanels.containsKey(id)) {
			final JPanel panel = _contactPanels.remove(id);
			this.remove(panel);
			_shiftContactTextField();
		}
	}

	private class AssociatedContactObservable extends Observable {

		@Override
		protected void setChanged() {
			super.setChanged();
		}

		@Override
		protected void clearChanged() {
			super.clearChanged();
		}
	}
}
