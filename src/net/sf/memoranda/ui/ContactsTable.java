package net.sf.memoranda.ui;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

import net.sf.memoranda.Contact;
import net.sf.memoranda.ContactManager;
import net.sf.memoranda.util.Local;

public class ContactsTable  extends JTable {

    public static final int CONTACT_ID = 101;
	public static final int CONTACT = 100;

    ArrayList<Contact> contacts = new ArrayList<Contact>();
    /**
     * Constructor for ContactsTable.
     */
    public ContactsTable() {
        super();
        setModel(new ContactsTableModel());
        initTable();
        this.setShowGrid(false);
    }

    public void initTable() {
        contacts = ContactManager.getAllContacts();
        //getColumnModel().getColumn(0).setPreferredWidth(60);
        //getColumnModel().getColumn(0).setMaxWidth(60);
        clearSelection();
        updateUI();
    }

    public void refresh() {
        initTable();
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        return new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
                Component comp;
                comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                comp.setForeground(java.awt.Color.black);
                return comp;
            }
        };
    }

    class ContactsTableModel extends AbstractTableModel {

        String[] columnNames = {
            Local.getString("First Name"),
            Local.getString("Last Name"),
            Local.getString("Email"),
            Local.getString("Telephone"),
            Local.getString("Organization")
        };

        ContactsTableModel() {
            super();
        }

        public int getColumnCount() {
            return 5;
        }

        public int getRowCount() {
			int i;
			try {
				i = contacts.size();
			}
			catch(NullPointerException e) {
				i = 1;
			}
			return i;
        }

        public Object getValueAt(int row, int col) {
           Contact ev = (Contact)contacts.get(row);
           if (col == 0)
                return ev.getFirstName();
           else if (col == 1)
                return ev.getLastName();
           else if (col == 2)
                return ev.getEmailAddress();
           else if (col == 3)
               return ev.getPhoneNumber();
           else if (col == 4)
               return ev.getOrganization();
           else return ev;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }
    }
}

