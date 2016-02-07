package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sf.memoranda.util.Local;

/**
 * The ContactsAllPanel class is used in the ContactsPanel class to display and manipulate all
 * <code>Contact</code> objects in the application.
 * 
 * @author Jonathan Hinkle
 * @see ContactsPanel
 * @see {@link net.sf.memoranda.Contact}
 *
 */
public class ContactsAllPanel extends JPanel{
	
	//Header
	private JPanel _headerPanel = new JPanel();
	//Header Contents
    private JLabel _headerTitle = new JLabel();
    private JToolBar _tbSearch = new JToolBar();
    private JLabel _lblSearch = new JLabel();
    private JTextField _txtSearch = new JTextField();
    
    //Scroll Pane
    private JScrollPane _scrollPane = new JScrollPane();
    //Scroll Pane Contents
    private ContactsTable _contactTable = new ContactsTable(ContactsTable.Type.ALL);

    
    public ContactsAllPanel() {
        try {
            _jbInit();
        }
        catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }
    
    
    public ContactsTable getTable() {
		return _contactTable;
	}
    
    
    private void _jbInit() throws Exception {
        this.setLayout(new BorderLayout());
        
        this._buildHeader();
        this._buildScrollPane();
        
        this.add(_headerPanel, BorderLayout.NORTH);
        this.add(_scrollPane, BorderLayout.CENTER);
    }
    
    
    private void _buildHeader() {
    	Font oldFont = _headerTitle.getFont();
        _headerTitle.setFont(new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize()*2));
        _headerTitle.setPreferredSize(new Dimension(200,48));
        _headerTitle.setMinimumSize(new Dimension(200,48));
        _headerTitle.setText("All Contacts");
        
        _lblSearch.setEnabled(true);
        _lblSearch.setText("Search:");
        _lblSearch.setMinimumSize(new Dimension(75, 24));
        _lblSearch.setRequestFocusEnabled(false);
        _lblSearch.setPreferredSize(new Dimension(75, 24));
        _lblSearch.setFocusable(false);
        
        _txtSearch.setEnabled(true);
        _txtSearch.setMinimumSize(new Dimension(350, 24));
        _txtSearch.setToolTipText(Local.getString("Search by first and last name"));
        _txtSearch.setPreferredSize(new Dimension(350, 24));
        _txtSearch.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				_contactTable.setNameFilter(_txtSearch.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				_contactTable.setNameFilter(_txtSearch.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				_contactTable.setNameFilter(_txtSearch.getText());
			}
			
        });
        
        _tbSearch.add(_lblSearch);
        _tbSearch.add(_txtSearch);
        _tbSearch.setFloatable(false);
        
        _headerPanel.setLayout(new BorderLayout());
        _headerPanel.add(_headerTitle, BorderLayout.NORTH);
        _headerPanel.add(_tbSearch, BorderLayout.CENTER);
    }
    
    private void _buildScrollPane() {
    	_contactTable.setMaximumSize(new Dimension(32767, 32767));
        _contactTable.setRowHeight(24);
        
        _scrollPane.getViewport().setBackground(Color.white);
        _scrollPane.getViewport().add(_contactTable, null);
    }
}

