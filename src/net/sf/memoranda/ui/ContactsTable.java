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
import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.NoteList;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectListener;
import net.sf.memoranda.ResourcesList;
import net.sf.memoranda.TaskList;
import net.sf.memoranda.util.Local;

public class ContactsTable  extends JTable {

    public static final int CONTACT_ID = 101;
	public static final int CONTACT = 100;
	public static enum Type {
		ALL,
		PROJECT
	}
	private TransferableContacts transferableContacts = new TransferableContacts();
	private Type type;
	private NameFilter nameFilter = new NameFilter();
	private TableRowSorter<ContactsTableModel> rowSorter;

    ArrayList<Contact> contacts = new ArrayList<Contact>();
    /**
     * Constructor for ContactsTable.
     */
    public ContactsTable(Type type) {
        super();
        this.type = type;
        ContactsTableModel cModel = new ContactsTableModel();
        setModel(cModel);
        initTable();
        this.setShowGrid(false);
        CurrentProject.addProjectListener(new ProjectListener() {

			@Override
			public void projectChange(Project prj, NoteList nl, TaskList tl, ResourcesList rl) {}

			@Override
			public void projectWasChanged() {
				refresh();
			}
        	
        });
    }
    
    public TransferableContacts getTransferableContacts() {
    	return transferableContacts;
    }

    public void initTable() {
    	if(type == Type.ALL) {
    		contacts = ContactManager.getAllContacts();
    	}
    	else {
    		contacts = ContactManager.getProjectContacts(CurrentProject.get());
    	}
    	rowSorter = new TableRowSorter<ContactsTableModel>((ContactsTableModel)this.getModel());
        rowSorter.setRowFilter(nameFilter);
        setRowSorter(rowSorter);
        //getColumnModel().getColumn(0).setPreferredWidth(60);
        //getColumnModel().getColumn(0).setMaxWidth(60);
        clearSelection();
        updateUI();
    }

    public void refresh() {
        initTable();
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
    	super.valueChanged(e);
    	transferableContacts.clear();
    	int[] indexes = this.getSelectedRows();
    	for(int i = 0; i < indexes.length; i++) {
    		transferableContacts.add((Contact)this.getModel().getValueAt(indexes[i], ContactsTable.CONTACT));
    	}
    }
    
    private boolean pressed;
    
    private boolean isUnselected(MouseEvent e) {
         Point pt = e.getPoint();
         int row = rowAtPoint(pt);
         int col = columnAtPoint(pt);
         return row >= 0 && col >= 0 && !super.isCellSelected(row, col);
    }
    
    @Override
    protected void processMouseEvent(MouseEvent e) {
         pressed = e.getID() == MouseEvent.MOUSE_PRESSED
                   && SwingUtilities.isLeftMouseButton(e)
                   && !e.isShiftDown() && !e.isControlDown()
                   && isUnselected(e);
         try {
              if (pressed)
                   clearSelection();
              super.processMouseEvent(e);
         } finally {
              pressed = false;
         }
    }
    
    @Override
    public boolean isCellSelected(int row, int col) {
         return pressed ? true : super.isCellSelected(row, col);
    }
    
    /*@Override
    public void processMouseEvent(MouseEvent e) {
    	if(e.getID() == MouseEvent.MOUSE_DRAGGED &&
    			SwingUtilities.isLeftMouseButton(e) &&
    			!e.isShiftDown() &&
    			!e.isControlDown()) {
    		Point point = e.getPoint();
    		int rowIndex = this.rowAtPoint(point);
    		if(rowIndex >= 0) {
    			this.addRowSelectionInterval(rowIndex, rowIndex);
    		}
    	}
    	else {
    		super.processMouseEvent(e);
    	}
    }*/
    
    /*@Override
    protected void processMouseEvent(MouseEvent e) {
         if (e.getID() == MouseEvent.MOUSE_PRESSED && SwingUtilities.isLeftMouseButton(e)) {
        	 if (e.isShiftDown()) {
        		 Point point = 
        	 }
        	 else if(e.isControlDown()) {
        		 
        	 }
         }
         super.processMouseEvent(e);
    }
    
    @Override
    protected void processKeyEvent(KeyEvent e) {
    	
    }*/

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
    
    class TransferableContacts extends ArrayList<Contact> implements Transferable {
    	
    	private DataFlavor dataFlavor = null;
    	
    	public TransferableContacts() {
    		super();
    		try {
				dataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + 
						";contact=" + TransferableContacts.class.getName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
    	}
    	
		@Override
		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor[] returnFlavors = null;
			if(dataFlavor != null) {
				returnFlavors = new DataFlavor[1];
				returnFlavors[0] = dataFlavor;
			}
			return returnFlavors;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			boolean supported = false;
			if(dataFlavor != null && dataFlavor.equals(flavor)) { 
				supported = true;
			}
			return supported;
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if(!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
			return this;
		}
    	
    }

	public void setNameFilterText(String text) {
		if(text != null) {
			nameFilter.filterString = text;
			refresh();
		}
	}
	
	private class NameFilter extends RowFilter<ContactsTableModel, Integer> {
		
		public String filterString = "";
		
		@Override
		public boolean include(javax.swing.RowFilter.Entry<? extends ContactsTableModel, ? extends Integer> entry) {
			boolean matches = false;
			ContactsTableModel ctModel = (ContactsTableModel)entry.getModel();
			final int i = entry.getIdentifier().intValue();
			Contact contact = (Contact)ctModel.getValueAt(i, ContactsTable.CONTACT);
			String nameString = contact.getFirstName() + " " + contact.getLastName();
			if(nameString.startsWith(filterString)) {
				matches = true;
			}
			return matches;
		}
		
	}
}

