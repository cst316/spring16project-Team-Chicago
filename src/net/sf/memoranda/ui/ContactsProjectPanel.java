package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.memoranda.Contact;
import net.sf.memoranda.ContactManager;
import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.History;
import net.sf.memoranda.ui.ContactsTable.TransferableContacts;
import net.sf.memoranda.util.Local;

public class ContactsProjectPanel extends JPanel{
    BorderLayout borderLayout1 = new BorderLayout();
    JScrollPane scrollPane = new JScrollPane();
    JLabel headerTitle = new JLabel();
    ContactsTable contactTable = new ContactsTable(ContactsTable.Type.PROJECT);
    JMenuItem ppEditContact = new JMenuItem();
    JMenuItem ppRemoveContact = new JMenuItem();
    JMenuItem ppNewContact = new JMenuItem();
    ContactsPanel parentPanel = null;

    public ContactsProjectPanel(ContactsPanel _parentPanel) {
        try {
            parentPanel = _parentPanel;
            jbInit();
        }
        catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }
    
    void jbInit() throws Exception {

        this.setLayout(borderLayout1);
        scrollPane.getViewport().setBackground(Color.white);
        contactTable.setMaximumSize(new Dimension(32767, 32767));
        contactTable.setRowHeight(24);
        scrollPane.getViewport().add(contactTable, null);
        
        scrollPane.getViewport().setBackground(Color.white);
        contactTable.setMaximumSize(new Dimension(32767, 32767));
        contactTable.setRowHeight(24);
        scrollPane.getViewport().add(contactTable, null);
        
        headerTitle.setPreferredSize(new Dimension(200,48));
        headerTitle.setMinimumSize(new Dimension(200,48));
        Font oldFont = headerTitle.getFont();
        headerTitle.setFont(new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize()*2));
        headerTitle.setText("Project Contacts");
        
        this.add(headerTitle, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }
    
    public ContactsTable getTable() {
		return contactTable;
	}
}

