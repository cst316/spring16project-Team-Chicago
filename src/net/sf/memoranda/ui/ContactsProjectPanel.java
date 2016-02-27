package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * The ContactsProjectPanel class is used in the ContactsPanel class to display
 * and manipulate <code>Contact</code> objects associated with a
 * <code>Project</code> in the application.
 * 
 * @author Jonathan Hinkle
 * @see ContactsPanel
 * @see {@link net.sf.memoranda.Contact}
 * @see {@link net.sf.memoranda.Project}
 *
 */
@SuppressWarnings("serial")
public class ContactsProjectPanel extends JPanel {

	// Header
	private JLabel _headerTitle = new JLabel();
	// Scroll Pane
	private JScrollPane _scrollPane = new JScrollPane();
	// Scroll Pane Contents
	private ContactsTable _contactsTable = new ContactsTable(ContactsTable.Type.PROJECT);

	/**
	 * Constructor for the ContactsProjectPanel.
	 */
	public ContactsProjectPanel() {
		_jbInit();
	}

	public ContactsTable getTable() {
		return _contactsTable;
	}

	private void _jbInit() {
		this.setLayout(new BorderLayout());

		this._buildHeader();
		this._buildScrollPane();

		this.add(_headerTitle, BorderLayout.NORTH);
		this.add(_scrollPane, BorderLayout.CENTER);
	}

	private void _buildHeader() {
		final Font oldFont = _headerTitle.getFont();
		_headerTitle.setPreferredSize(new Dimension(200, 48));
		_headerTitle.setMinimumSize(new Dimension(200, 48));
		_headerTitle.setFont(new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize() * 2));
		_headerTitle.setText("Project Contacts");
	}

	private void _buildScrollPane() {
		_contactsTable.setMaximumSize(new Dimension(32767, 32767));
		_contactsTable.setRowHeight(24);

		_scrollPane.getViewport().setBackground(Color.white);
		_scrollPane.getViewport().add(_contactsTable, null);
	}

}
