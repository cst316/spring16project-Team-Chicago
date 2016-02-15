package net.sf.memoranda.ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.RowFilter;
import javax.swing.RowSorter;

import net.sf.memoranda.Contact;
import net.sf.memoranda.ContactManager;
import net.sf.memoranda.ContactManagerListener;
import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.NoteList;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectListener;
import net.sf.memoranda.ResourcesList;
import net.sf.memoranda.TaskList;
import net.sf.memoranda.util.Local;

/**
 * The ContactsTable class is used to display <code>Contact</code>s in the
 * Memoranda application GUI. It can be used to display all <code>Contact</code>s
 * within the application or to display <code>Contact</code>s associated with
 * the <code>CurrentProject</code>.
 * 
 * @author Jonathan Hinkle
 * @see {@link net.sf.memoranda.Contact }
 * @see {@link net.sf.memoranda.CurrentProject }
 *
 */
public class ContactsTable  extends JTable {

    public static final int CONTACT_ID = 101;
	public static final int CONTACT = 100;
	public static enum Type {
		ALL,
		PROJECT
	}
	
	private TransferableContacts<Contact> _transferableContacts = new TransferableContacts<Contact>();
	private Type _type;
	private NameFilter _nameFilter = new NameFilter();
	private TableRowSorter<ContactsTableModel> _rowSorter;
	private ArrayList<Contact> _contacts = new ArrayList<Contact>();
	
	
    /**
     * Constructor for ContactsTable. Accepts a <code>ContactsTable.Type</code>
     * which sets how the table retrieves the contacts it shows
     * 
     * @param type The type of table to be used
     */
    public ContactsTable(Type type) {
        super();
        this._type = type;
        ContactsTableModel cModel = new ContactsTableModel();
        setModel(cModel);
        _initTable();
        this.setShowGrid(false);
        ContactManager.addContactManagerListener(new ContactManagerListener() {

			@Override
			public void contactManagerChanged() {
				refresh();
			}

        });
    }
    
    
    public void setNameFilter(String text) {
		if(text != null) {
			_nameFilter._filterString = text;
			refresh();
		}
	}
    
    
    public TransferableContacts getTransferableContacts() {
    	return _transferableContacts;
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
    

    /**
     * Refreshes the contact table, updating its contents
     */
    public void refresh() {
        _initTable();
    }
    
    
    /**
     * @see javax.swing.JTable#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
    	super.valueChanged(e);
    	_transferableContacts.clear();
    	int[] indexes = this.getSelectedRows();
    	for(int i = 0; i < indexes.length; i++) {
    		Contact c = (Contact)this.getModel().getValueAt(indexes[i], ContactsTable.CONTACT);
    		_transferableContacts.add(c);
    	}
    }
    
    private void _initTable() {
    	if(_type == Type.ALL) {
    		_contacts = ContactManager.getAllContacts();
    	}
    	else {
    		_contacts = ContactManager.getContacts(CurrentProject.get());
    	}
    	_rowSorter = new TableRowSorter<ContactsTableModel>((ContactsTableModel)this.getModel());
        _rowSorter.setRowFilter(_nameFilter);
        setRowSorter(_rowSorter);
        //getColumnModel().getColumn(0).setPreferredWidth(60);
        //getColumnModel().getColumn(0).setMaxWidth(60);
        clearSelection();
        updateUI();
    }


    /**
     * The ContactsTableModel as a table model for the ContactsTable class
     * 
     * @author Jonathan Hinkle
     *
     */
    public class ContactsTableModel extends AbstractTableModel {

        String[] columnNames = {
            Local.getString("First Name"),
            Local.getString("Last Name"),
            Local.getString("Email"),
            Local.getString("Telephone"),
            Local.getString("Organization")
        };

        
        /**
         * Constructor for the ContactsTableModel
         */
        public ContactsTableModel() {
            super();
        }

        
        public int getColumnCount() {
            return 5;
        }
        

        public int getRowCount() {
			int i;
			try {
				i = _contacts.size();
			}
			catch(NullPointerException e) {
				i = 1;
			}
			return i;
        }
        

        public Object getValueAt(int row, int col) {
           Contact ev = (Contact)_contacts.get(row);
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
    
    
    /**
     * A transferable <code>ArrayList</code> of <code>Contact</code>s used
     * by the UI for drag-n-drop functionality. This class is currently not being used for its full
     * purpose until problems with selection are fixed to allow multiple contacts to be transfered.
     * 
     * @author Jonathan
     *
     */
    public class TransferableContacts<C extends Contact> extends ArrayList<C> implements Transferable {
    	
    	private DataFlavor _dataFlavor = null;
    	
    	
    	/**
    	 * Constructor for TransferableContacts
    	 */
    	public TransferableContacts() {
    		super();
    		try {
				_dataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + 
						";contact=" + TransferableContacts.class.getName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
    	}
    	
    	
		/**
		 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
		 */
		@Override
		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor[] returnFlavors = null;
			if(_dataFlavor != null) {
				returnFlavors = new DataFlavor[1];
				returnFlavors[0] = _dataFlavor;
			}
			return returnFlavors;
		}
		

		/**
		 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
		 */
		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			boolean supported = false;
			if(_dataFlavor != null && _dataFlavor.equals(flavor)) { 
				supported = true;
			}
			return supported;
		}

		
		/**
		 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
		 */
		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if(!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
			return this;
		}
    }
	
    
	private class NameFilter extends RowFilter<ContactsTableModel, Integer> {
		
		private String _filterString = "";
		
		
		@Override
		public boolean include(javax.swing.RowFilter.Entry<? extends ContactsTableModel, ? extends Integer> entry) {
			boolean matches = false;
			ContactsTableModel ctModel = (ContactsTableModel)entry.getModel();
			final int i = entry.getIdentifier().intValue();
			Contact contact = (Contact)ctModel.getValueAt(i, ContactsTable.CONTACT);
			String nameString = contact.getFirstName() + " " + contact.getLastName();
			if(nameString.startsWith(_filterString)) {
				matches = true;
			}
			return matches;
		}
	}
}

