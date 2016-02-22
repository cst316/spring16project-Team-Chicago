package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.memoranda.Contact;
import net.sf.memoranda.ContactManager;
import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.History;
import net.sf.memoranda.util.Local;

/**
 * The ContactsPanel class is used to display one of the main views in the
 * memoranda application. It contains features for adding, removing, and editing
 * <code>Contact</code>s. Two tables are contained in the panel, one listing all
 * contacts within the application, and another listing contacts associated with
 * the <code>CurrentProject</code>.
 * 
 * @author Jonathan Hinkle
 * @see { @link net.sf.memoranda.Contact }
 * @see { @link net.sf.memoranda.CurrentProject }
 *
 */
@SuppressWarnings("serial")
public class ContactsPanel extends JPanel {

	/**
	 * Values are used indicate the current selection context for selected
	 * <Code>Contact</Code>s by which table they are being selected from.
	 *
	 */
	public enum SelectionContext {
		ALL, PROJECT,
	}

	private SelectionContext _selectionContext = SelectionContext.ALL;
	private boolean _addToProject = true;
	private DailyItemsPanel _parentPanel = null;

	// Menu
	private JPopupMenu _contactPPMenu = new JPopupMenu();
	// Menu Contents
	private JMenuItem _ppEditContact = new JMenuItem();
	private JMenuItem _ppRemoveContact = new JMenuItem();
	private JMenuItem _ppNewContact = new JMenuItem();

	// ToolBar
	private JToolBar _contactsToolBar = new JToolBar();
	// ToolBar Contents
	private JButton _historyBackB = new JButton();
	private JButton _historyForwardB = new JButton();
	private JButton _newContactB = new JButton();
	private JButton _editContactB = new JButton();
	private JButton _removeContactB = new JButton();

	// SplitPane
	private JSplitPane _contactsSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	// SplitPane Contents
	private ContactsAllPanel _contactsAllPanel = new ContactsAllPanel();
	private ContactsProjectPanel _contactsProjectPanel = new ContactsProjectPanel();
	private ContactsTable _contactsAllTable;
	private ContactsTable _contactsProjectTable;

	/**
	 * Constructor for the contacts panel.
	 * 
	 * @param parentPanel The parent DailyItemsPanel
	 * @see DailyItemsPanel
	 */
	public ContactsPanel(DailyItemsPanel parentPanel) {
		this._parentPanel = parentPanel;
		_jbInit();
	}

	public SelectionContext getSelectionContext() {
		return _selectionContext;
	}

	private void _jbInit() {
		this.setLayout(new BorderLayout());

		this._buildContactsMenuContents();
		this._buildContactsToolBar();
		this._buildContactsSplitPane();

		this.add(_contactsToolBar, BorderLayout.NORTH);
		this.add(_contactsSplitPane, BorderLayout.CENTER);
	}

	private void _buildContactsMenuContents() {
		_contactPPMenu.setFont(new java.awt.Font("Dialog", 1, 10));
		_ppEditContact.setFont(new java.awt.Font("Dialog", 1, 11));
		_ppEditContact.setText(Local.getString("Edit contact") + "...");
		_ppEditContact.setEnabled(false);
		_ppEditContact.setIcon(
				new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_edit.png")));
		_ppEditContact.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				_ppEditContactActionPerformed(event);
			}
		});

		_ppRemoveContact.setEnabled(false);
		_ppRemoveContact.setFont(new java.awt.Font("Dialog", 1, 11));
		_ppRemoveContact.setText(Local.getString("Remove contact"));
		_ppRemoveContact.setIcon(
				new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_remove.png")));
		_ppRemoveContact.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				_ppRemoveContactActionPerformed(event);
			}
		});

		_ppNewContact.setFont(new java.awt.Font("Dialog", 1, 11));
		_ppNewContact.setText(Local.getString("New contact") + "...");
		_ppNewContact.setIcon(
				new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_new.png")));
		_ppNewContact.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				_ppNewContactActionPerformed(event);
			}
		});

		_contactPPMenu.add(_ppEditContact);
		_contactPPMenu.addSeparator();
		_contactPPMenu.add(_ppNewContact);
		_contactPPMenu.add(_ppRemoveContact);
	}

	private void _buildContactsToolBar() {
		_historyBackB.setAction(History.historyBackAction);
		_historyBackB.setFocusable(false);
		_historyBackB.setBorderPainted(false);
		_historyBackB.setToolTipText(Local.getString("History back"));
		_historyBackB.setRequestFocusEnabled(false);
		_historyBackB.setPreferredSize(new Dimension(24, 24));
		_historyBackB.setMinimumSize(new Dimension(24, 24));
		_historyBackB.setMaximumSize(new Dimension(24, 24));
		_historyBackB.setText("");

		_historyForwardB.setAction(History.historyForwardAction);
		_historyForwardB.setBorderPainted(false);
		_historyForwardB.setFocusable(false);
		_historyForwardB.setPreferredSize(new Dimension(24, 24));
		_historyForwardB.setRequestFocusEnabled(false);
		_historyForwardB.setToolTipText(Local.getString("History forward"));
		_historyForwardB.setMinimumSize(new Dimension(24, 24));
		_historyForwardB.setMaximumSize(new Dimension(24, 24));
		_historyForwardB.setText("");

		_newContactB.setBorderPainted(false);
		_newContactB.setEnabled(true);
		_newContactB.setMaximumSize(new Dimension(24, 24));
		_newContactB.setMinimumSize(new Dimension(24, 24));
		_newContactB.setToolTipText(Local.getString("New contact"));
		_newContactB.setRequestFocusEnabled(false);
		_newContactB.setPreferredSize(new Dimension(24, 24));
		_newContactB.setFocusable(false);
		_newContactB.setIcon(
				new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_new.png")));
		_newContactB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				_newContactActionPerformed(event);
			}
		});

		_editContactB.setBorderPainted(false);
		_editContactB.setFocusable(false);
		_editContactB.setPreferredSize(new Dimension(24, 24));
		_editContactB.setRequestFocusEnabled(false);
		_editContactB.setToolTipText(Local.getString("Edit contact"));
		_editContactB.setMinimumSize(new Dimension(24, 24));
		_editContactB.setMaximumSize(new Dimension(24, 24));
		_editContactB.setEnabled(false);
		_editContactB.setIcon(
				new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_edit.png")));
		_editContactB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				_editContactActionPerformed(event);
			}
		});

		_removeContactB.setBorderPainted(false);
		_removeContactB.setFocusable(false);
		_removeContactB.setPreferredSize(new Dimension(24, 24));
		_removeContactB.setRequestFocusEnabled(false);
		_removeContactB.setToolTipText(Local.getString("Remove contact"));
		_removeContactB.setMinimumSize(new Dimension(24, 24));
		_removeContactB.setMaximumSize(new Dimension(24, 24));
		_removeContactB.setEnabled(false);
		_removeContactB.setIcon(
				new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_remove.png")));
		_removeContactB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				_removeContactActionPerformed(event);
			}
		});

		_contactsToolBar.add(_historyBackB, null);
		_contactsToolBar.add(_historyForwardB, null);
		_contactsToolBar.addSeparator(new Dimension(8, 24));
		_contactsToolBar.add(_newContactB, null);
		_contactsToolBar.add(_removeContactB, null);
		_contactsToolBar.addSeparator(new Dimension(8, 24));
		_contactsToolBar.add(_editContactB, null);
		_contactsToolBar.setFloatable(false);
	}

	private void _buildContactsSplitPane() {
		_contactsAllTable = _contactsAllPanel.getTable();
		_contactsProjectTable = _contactsProjectPanel.getTable();

		_contactsAllTable.setFillsViewportHeight(true);
		_contactsProjectTable.setFillsViewportHeight(true);
		_contactsProjectTable.setDropMode(DropMode.INSERT_ROWS);
		_contactsAllTable.setDropMode(DropMode.INSERT_ROWS);
		_contactsAllTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_contactsProjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_contactsAllTable.setDragEnabled(true);
		_contactsProjectTable.setDragEnabled(true);

		_contactsAllTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent event) {
				final boolean enbl = _contactsAllTable.getSelectedRow() > -1;
				_contactsProjectTable.getSelectionModel().clearSelection();
				_selectionContext = SelectionContext.ALL;
				_editContactB.setEnabled(enbl);
				_ppEditContact.setEnabled(enbl);
				_removeContactB.setEnabled(enbl);
				_ppRemoveContact.setEnabled(enbl);
			}
		});

		_contactsProjectTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent event) {
				final boolean enbl = _contactsProjectTable.getSelectedRow() > -1;
				_contactsAllTable.getSelectionModel().clearSelection();
				_selectionContext = SelectionContext.PROJECT;
				_editContactB.setEnabled(enbl);
				_ppEditContact.setEnabled(enbl);
				_removeContactB.setEnabled(enbl);
				_ppRemoveContact.setEnabled(enbl);
			}
		});

		// Add a key listener to allow the delete key to be used on selected
		// contacts
		_contactsAllTable.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent event) {
				if (_contactsAllTable.getSelectedRows().length > 0 && event.getKeyCode() == KeyEvent.VK_DELETE) {
					_ppRemoveContactActionPerformed(null);
				}
			}

			public void keyReleased(KeyEvent event) {}

			public void keyTyped(KeyEvent event) {}
		});

		// Add a key listener to allow the delete key to be used on selected
		// contacts
		_contactsProjectTable.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent event) {
				if (_contactsProjectTable.getSelectedRows().length > 0 && event.getKeyCode() == KeyEvent.VK_DELETE) {
					_ppRemoveContactActionPerformed(null);
				}
			}

			public void keyReleased(KeyEvent event) {}

			public void keyTyped(KeyEvent event) {}
		});

		// Set the transfer handler for the contacts table to handle drag n drop
		_contactsProjectTable.setTransferHandler(new TransferHandler() {

			@Override
			public boolean canImport(TransferSupport support) {
				return support.isDataFlavorSupported(DataFlavor.stringFlavor);
			}

			@Override
			public boolean importData(TransferSupport support) {
				boolean success = false;
				if (canImport(support)) {
					try {
						final String id = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
						final Contact contact = new Contact("", "");
						contact.setID(id);
						final Contact removed = ContactManager.removeContact(contact);
						removed.addProjectID(CurrentProject.get().getID());
						ContactManager.addContact(removed);
						success = true;
					}
					catch (UnsupportedFlavorException e) {
						success = false;
					}
					catch (IOException e) {
						success = false;
					}
				}
				return success;
			}

			public int getSourceActions(JComponent comp) {
				return COPY;
			}

			@Override
			protected Transferable createTransferable(JComponent comp) {
				return _contactsAllTable.getTransferableContacts();
			}
		});

		// Set the transfer handler for the contacts table to handle drag n drop
		_contactsAllTable.setTransferHandler(new TransferHandler() {

			@Override
			protected Transferable createTransferable(JComponent comp) {
				final int index = _contactsAllTable.getSelectedRow();
				final String selectedID = (String) _contactsAllTable.getModel().getValueAt(index,
						ContactsTable.CONTACT_ID);
				final StringSelection transID = new StringSelection(selectedID);
				return transID;
			}

			@Override
			public int getSourceActions(JComponent comp) {
				return COPY;
			}

			@Override
			public void exportDone(JComponent source, Transferable data, int action) {
				_refreshTables();
			}

		});

		_contactsAllTable.addMouseListener(new PopupListener());
		_contactsProjectTable.addMouseListener(new PopupListener());

		_contactsSplitPane.setLeftComponent(_contactsProjectPanel);
		_contactsSplitPane.setRightComponent(_contactsAllPanel);
		_contactsSplitPane.setResizeWeight(0.5);
	}

	private void _editContactActionPerformed(ActionEvent event) {
		ContactsTable cTable = null;
		if (_selectionContext == SelectionContext.ALL) {
			cTable = _contactsAllPanel.getTable();
		}
		else if (_selectionContext == SelectionContext.PROJECT) {
			cTable = _contactsProjectPanel.getTable();
		}
		if (cTable != null) {
			final ContactDialog dlg = new ContactDialog(App.getFrame(), Local.getString("Contact"));
			final Contact contact = (Contact) cTable.getModel().getValueAt(cTable.getSelectedRow(),
					ContactsTable.CONTACT);
			dlg.getTxtFirstName().setText(contact.getFirstName());
			dlg.getTxtLastName().setText(contact.getLastName());
			dlg.getTxtEmailAddress().setText(contact.getEmailAddress());
			dlg.getTxtTelephone().setText(contact.getPhoneNumber());
			dlg.getTxtOrganization().setText(contact.getOrganization());
			dlg.getCBAddToProject().setEnabled(false);
			if (!contact.getProjectIDs().contains(CurrentProject.get().getID())) {
				dlg.getCBAddToProject().setSelected(false);
				if (_selectionContext == SelectionContext.ALL) {
					dlg.getCBAddToProject().setEnabled(true);
				}
			}
			else {
				dlg.getCBAddToProject().setSelected(true);
			}
			final Dimension frmSize = App.getFrame().getSize();
			final Point loc = App.getFrame().getLocation();
			dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x,
					(frmSize.height - dlg.getSize().height) / 2 + loc.y);
			dlg.setVisible(true);
			if (dlg.isCancelled()) {
				return;
			}

			final Contact editedContact = contact.copy();
			editedContact.setFirstName(dlg.getTxtFirstName().getText().trim());
			editedContact.setLastName(dlg.getTxtLastName().getText().trim());
			editedContact.setPhoneNumber(dlg.getTxtTelephone().getText().trim());
			editedContact.setEmailAddress(dlg.getTxtEmailAddress().getText().trim());
			editedContact.setOrganization(dlg.getTxtOrganization().getText().trim());

			if (!contact.hasSameInfo(editedContact)) {
				if (dlg.getCBAddToProject().isSelected() && dlg.getCBAddToProject().isEnabled()) {
					editedContact.addProjectID(CurrentProject.get().getID());
				}
				ContactManager.updateContact(editedContact);
				_refreshTables();
			}
		}
	}

	private void _newContactActionPerformed(ActionEvent event) {
		final ContactDialog dlg = new ContactDialog(App.getFrame(), Local.getString("New contact"));
		dlg.getCBAddToProject().setSelected(_addToProject);
		final Dimension frmSize = App.getFrame().getSize();
		final Point loc = App.getFrame().getLocation();

		dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x,
				(frmSize.height - dlg.getSize().height) / 2 + loc.y);
		dlg.setVisible(true);
		if (dlg.isCancelled()) {
			return;
		}
		final Contact newContact = new Contact(dlg.getTxtFirstName().getText(), dlg.getTxtLastName().getText(),
				dlg.getTxtTelephone().getText(), dlg.getTxtEmailAddress().getText()

		);
		newContact.setOrganization(dlg.getTxtOrganization().getText());
		if (dlg.getCBAddToProject().isSelected()) {
			newContact.addProjectID(CurrentProject.get().getID());
			_addToProject = true;
		}
		else {
			_addToProject = false;
		}
		ContactManager.addContact(newContact);
		_refreshTables();
	}

	private void _refreshTables() {
		_contactsAllPanel.getTable().refresh();
		_contactsProjectPanel.getTable().refresh();
		_parentPanel.calendar.jnCalendar.updateUI();
		_parentPanel.updateIndicators();
	}

	private void _removeContactActionPerformed(ActionEvent event) {
		String msg;
		Contact contact;
		ContactsTable cTable = null;
		if (_selectionContext == SelectionContext.ALL) {
			cTable = _contactsAllPanel.getTable();
		}
		else if (_selectionContext == SelectionContext.PROJECT) {
			cTable = _contactsProjectPanel.getTable();

		}
		if (cTable != null) {
			if (_selectionContext == SelectionContext.ALL) {
				if (cTable.getSelectedRows().length > 1) {
					msg = Local.getString(
							"WARNING: This will remove the selected contacts from all projects and delete the contacts")
							+ " " + cTable.getSelectedRows().length + " " + Local.getString("contacts") + "\n"
							+ Local.getString("Are you sure?");
				}
				else {
					contact = (Contact) cTable.getModel().getValueAt(cTable.getSelectedRow(), ContactsTable.CONTACT);
					msg = Local.getString(
							"WARNING: This will remove the selected contact from all projects and delete the contact")
							+ "\n'" + contact.getFirstName() + " " + contact.getLastName() + "'\n"
							+ Local.getString("Are you sure?");
				}

				final int option = JOptionPane.showConfirmDialog(App.getFrame(), msg,
						Local.getString("Remove contacts"), JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION) {
					return;
				}
			}

			for (int i = 0; i < cTable.getSelectedRows().length; i++) {
				contact = (Contact) cTable.getModel().getValueAt(cTable.getSelectedRows()[i], ContactsTable.CONTACT);
				if (_selectionContext == SelectionContext.ALL) {
					ContactManager.removeContact(contact);
				}
				else if (_selectionContext == SelectionContext.PROJECT) {
					contact.removeProjectID(CurrentProject.get().getID());
					ContactManager.updateContact(contact);
				}
			}
			_contactsAllPanel.getTable().getSelectionModel().clearSelection();
			_contactsAllPanel.getTable().getSelectionModel().clearSelection();
			_refreshTables();
		}
	}

	private void _ppEditContactActionPerformed(ActionEvent event) {
		_editContactActionPerformed(event);
	}

	private void _ppRemoveContactActionPerformed(ActionEvent event) {
		_removeContactActionPerformed(event);
	}

	private void _ppNewContactActionPerformed(ActionEvent event) {
		_newContactActionPerformed(event);
	}

	private class PopupListener extends MouseAdapter {

		public void mouseClicked(MouseEvent event) {
			if (event.getClickCount() == 2) {
				_editContactActionPerformed(null);
			}
		}

		public void mousePressed(MouseEvent event) {
			maybeShowPopup(event);
		}

		public void mouseReleased(MouseEvent event) {
			maybeShowPopup(event);
		}

		private void maybeShowPopup(MouseEvent event) {
			if (event.isPopupTrigger()) {
				_contactPPMenu.show(event.getComponent(), event.getX(), event.getY());
			}
		}
	}
}
