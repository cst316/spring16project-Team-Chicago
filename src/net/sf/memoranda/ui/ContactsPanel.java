package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.memoranda.Contact;
import net.sf.memoranda.ContactManager;
import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.History;
import net.sf.memoranda.util.Local;

public class ContactsPanel extends JPanel{
	
	private enum SelectionContext {
		ALL,
		PROJECT,
	}
	private SelectionContext _selectionContext = SelectionContext.ALL;
    BorderLayout borderLayout1 = new BorderLayout();
    JButton historyBackB = new JButton();
    JButton historyForwardB = new JButton();
    JButton newContactB = new JButton();
    JButton editContactB = new JButton();
    JButton removeContactB = new JButton();
    JToolBar contactsToolBar = new JToolBar();
    ContactsAllPanel contactsAllPanel = new ContactsAllPanel(this);
    ContactsProjectPanel contactsProjectPanel = new ContactsProjectPanel(this);
    JSplitPane contactsSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JPopupMenu contactPPMenu = new JPopupMenu();
    JMenuItem ppEditContact = new JMenuItem();
    JMenuItem ppRemoveContact = new JMenuItem();
    JMenuItem ppNewContact = new JMenuItem();
    DailyItemsPanel parentPanel = null;
    private boolean _addToProject = true;

    public ContactsPanel(DailyItemsPanel _parentPanel) {
        try {
            parentPanel = _parentPanel;
            jbInit();
        }
        catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }
    void jbInit() throws Exception {
        contactsToolBar.setFloatable(false);

        historyBackB.setAction(History.historyBackAction);
        historyBackB.setFocusable(false);
        historyBackB.setBorderPainted(false);
        historyBackB.setToolTipText(Local.getString("History back"));
        historyBackB.setRequestFocusEnabled(false);
        historyBackB.setPreferredSize(new Dimension(24, 24));
        historyBackB.setMinimumSize(new Dimension(24, 24));
        historyBackB.setMaximumSize(new Dimension(24, 24));
        historyBackB.setText("");

        historyForwardB.setAction(History.historyForwardAction);
        historyForwardB.setBorderPainted(false);
        historyForwardB.setFocusable(false);
        historyForwardB.setPreferredSize(new Dimension(24, 24));
        historyForwardB.setRequestFocusEnabled(false);
        historyForwardB.setToolTipText(Local.getString("History forward"));
        historyForwardB.setMinimumSize(new Dimension(24, 24));
        historyForwardB.setMaximumSize(new Dimension(24, 24));
        historyForwardB.setText("");

        newContactB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_new.png")));
        newContactB.setEnabled(true);
        newContactB.setMaximumSize(new Dimension(24, 24));
        newContactB.setMinimumSize(new Dimension(24, 24));
        newContactB.setToolTipText(Local.getString("New contact"));
        newContactB.setRequestFocusEnabled(false);
        newContactB.setPreferredSize(new Dimension(24, 24));
        newContactB.setFocusable(false);
        newContactB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newContactB_actionPerformed(e);
            }
        });
        newContactB.setBorderPainted(false);

        editContactB.setBorderPainted(false);
        editContactB.setFocusable(false);
        editContactB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editContactB_actionPerformed(e);
            }
        });
        editContactB.setPreferredSize(new Dimension(24, 24));
        editContactB.setRequestFocusEnabled(false);
        editContactB.setToolTipText(Local.getString("Edit contact"));
        editContactB.setMinimumSize(new Dimension(24, 24));
        editContactB.setMaximumSize(new Dimension(24, 24));
        editContactB.setEnabled(true);
        editContactB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_edit.png")));

        removeContactB.setBorderPainted(false);
        removeContactB.setFocusable(false);
        removeContactB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeContactB_actionPerformed(e);
            }
        });
        removeContactB.setPreferredSize(new Dimension(24, 24));
        removeContactB.setRequestFocusEnabled(false);
        removeContactB.setToolTipText(Local.getString("Remove contact"));
        removeContactB.setMinimumSize(new Dimension(24, 24));
        removeContactB.setMaximumSize(new Dimension(24, 24));
        removeContactB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_remove.png")));

        this.setLayout(borderLayout1);
        contactPPMenu.setFont(new java.awt.Font("Dialog", 1, 10));
        ppEditContact.setFont(new java.awt.Font("Dialog", 1, 11));
        ppEditContact.setText(Local.getString("Edit contact") + "...");
        ppEditContact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppEditContact_actionPerformed(e);
            }
        });
        ppEditContact.setEnabled(false);
        ppEditContact.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_edit.png")));
        ppRemoveContact.setFont(new java.awt.Font("Dialog", 1, 11));
        ppRemoveContact.setText(Local.getString("Remove contact"));
        ppRemoveContact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppRemoveContact_actionPerformed(e);
            }
        });
        ppRemoveContact.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_remove.png")));
        ppRemoveContact.setEnabled(false);
        ppNewContact.setFont(new java.awt.Font("Dialog", 1, 11));
        ppNewContact.setText(Local.getString("New contact") + "...");
        ppNewContact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ppNewContact_actionPerformed(e);
            }
        });
        ppNewContact.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/contact_new.png")));
        contactsToolBar.add(historyBackB, null);
        contactsToolBar.add(historyForwardB, null);
        contactsToolBar.addSeparator(new Dimension(8, 24));

        contactsToolBar.add(newContactB, null);
        contactsToolBar.add(removeContactB, null);
        contactsToolBar.addSeparator(new Dimension(8, 24));
        contactsToolBar.add(editContactB, null);

        this.add(contactsToolBar, BorderLayout.NORTH);
        
        contactsSplitPane.setLeftComponent(contactsProjectPanel);
        contactsSplitPane.setRightComponent(contactsAllPanel);
        contactsSplitPane.setResizeWeight(0.5);
        this.add(contactsSplitPane, BorderLayout.CENTER);

        PopupListener ppListener = new PopupListener();

        ContactsTable cAllTable = contactsAllPanel.getTable();
        ContactsTable cProjectsTable = contactsProjectPanel.getTable();
        cAllTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                boolean enbl = cAllTable.getSelectedRow() > -1;
                cProjectsTable.getSelectionModel().clearSelection();
                _selectionContext = SelectionContext.ALL;
                editContactB.setEnabled(enbl);
                ppEditContact.setEnabled(enbl);
                removeContactB.setEnabled(enbl);
                ppRemoveContact.setEnabled(enbl);
            }
        });
        cProjectsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                boolean enbl = cProjectsTable.getSelectedRow() > -1;
                cAllTable.getSelectionModel().clearSelection();
                _selectionContext = SelectionContext.PROJECT;
                editContactB.setEnabled(enbl);
                ppEditContact.setEnabled(enbl);
                removeContactB.setEnabled(enbl);
                ppRemoveContact.setEnabled(enbl);
            }
        });
        editContactB.setEnabled(false);
        removeContactB.setEnabled(false);
        contactPPMenu.add(ppEditContact);
        contactPPMenu.addSeparator();
        contactPPMenu.add(ppNewContact);
        contactPPMenu.add(ppRemoveContact);
		
		// remove contacts using the DEL key
		cAllTable.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e){
				if(cAllTable.getSelectedRows().length > 0 && e.getKeyCode()==KeyEvent.VK_DELETE) {
					ppRemoveContact_actionPerformed(null);
				}
			}
			public void	keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){} 
		});
		cProjectsTable.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e){
				if(cProjectsTable.getSelectedRows().length > 0 && e.getKeyCode()==KeyEvent.VK_DELETE) {
					ppRemoveContact_actionPerformed(null);
				}
			}
			public void	keyReleased(KeyEvent e){}
			public void keyTyped(KeyEvent e){} 
		});
		//contactsSplitPane.setDividerLocation(this.getWidth()-300);
		/*projectContactsTable.setDropMode(DropMode.INSERT);
		projectContactsTable.setTransferHandler(new TransferHandler(){
			
			@Override
			public boolean canImport(TransferSupport support) {
				boolean importable = false;
				DataFlavor[] permittedFlavors = projectContactsTable.getTransferableContacts().getTransferDataFlavors();
				for(int i = 0; i < permittedFlavors.length; i++) {
					if(support.isDataFlavorSupported(permittedFlavors[i])) {
						importable = true;
						break;
					}
				}
				return importable;
			}
			
			@Override
			public boolean importData(TransferSupport support) {
				boolean success = false;
				if(canImport(support)) {
					ArrayList<Contact> contacts = (TransferableContacts)support.getTransferable();
					contacts.forEach(contact -> {
						contact.addProject(CurrentProject.get());
						ContactManager.updateContact(contact);
					});
				}
			    saveContacts();
				return success;
			}
		});
		
		contactTable.setDragEnabled(true);
		contactTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		contactTable.setTransferHandler(new TransferHandler() {
			
			@Override
			protected Transferable createTransferable(JComponent c) {
				return contactTable.getTransferableContacts();
			}
			
		});*/
    }
    void editContactB_actionPerformed(ActionEvent e) {
    	ContactsTable cTable = null;
    	if(_selectionContext == SelectionContext.ALL) {
    		cTable = contactsAllPanel.getTable();
    	}
    	else if(_selectionContext == SelectionContext.PROJECT) {
    		cTable = contactsProjectPanel.getTable();
    	}
    	if(cTable != null) {
	        ContactDialog dlg = new ContactDialog(App.getFrame(), Local.getString("Contact"));
	        Contact contact = (Contact)cTable.getModel().getValueAt(
	        		cTable.getSelectedRow(),
		            ContactsTable.CONTACT
	        	);
	        dlg.txtFirstName.setText(contact.getFirstName());
	        dlg.txtLastName.setText(contact.getLastName());
	        dlg.txtEmailAddress.setText(contact.getEmailAddress());
	        dlg.txtTelephone.setText(contact.getPhoneNumber());
	        dlg.txtOrganization.setText(contact.getOrganization());
	        dlg.cbAddToProject.setEnabled(false);
	        if(!contact.inProject(CurrentProject.get())) {
	        	dlg.cbAddToProject.setSelected(false);
	        	if(_selectionContext == SelectionContext.ALL) {
	        		dlg.cbAddToProject.setEnabled(true);
	        	}
	        }
	        else {
	        	dlg.cbAddToProject.setSelected(true);
	        }
	        Dimension frmSize = App.getFrame().getSize();
	        Point loc = App.getFrame().getLocation();
	        dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x, (frmSize.height - dlg.getSize().height) / 2 + loc.y);
	        dlg.setVisible(true);
	        if (dlg.CANCELLED) return;
	        contact.setFirstName(dlg.txtFirstName.getText());
	        contact.setLastName(dlg.txtLastName.getText());
	        contact.setPhoneNumber(dlg.txtTelephone.getText());
	        contact.setEmailAddress(dlg.txtEmailAddress.getText());
	        contact.setOrganization(dlg.txtOrganization.getText());
	        
	        if(dlg.cbAddToProject.isSelected() && dlg.cbAddToProject.isEnabled()) {
	    		contact.addProject(CurrentProject.get());
	    	}
	        
	        ContactManager.updateContact(contact);
		    saveContacts();
    	}
    }
    
    void newContactB_actionPerformed(ActionEvent e) {
    	ContactDialog dlg = new ContactDialog(App.getFrame(), Local.getString("New contact"));
    	dlg.cbAddToProject.setSelected(_addToProject);
    	Dimension frmSize = App.getFrame().getSize();
    	Point loc = App.getFrame().getLocation();

    	dlg.setLocation((frmSize.width - dlg.getSize().width) / 2 + loc.x, (frmSize.height - dlg.getSize().height) / 2 + loc.y);
		dlg.setVisible(true);
    	if (dlg.CANCELLED)
    		return;
    	Contact newContact = new Contact(
    			dlg.txtFirstName.getText(), 
    			dlg.txtLastName.getText(),
    			dlg.txtTelephone.getText(),
    			dlg.txtEmailAddress.getText()
    			
    		);
    	newContact.setOrganization(dlg.txtOrganization.getText());
    	if(dlg.cbAddToProject.isSelected()) {
    		newContact.addProject(CurrentProject.get());
    		_addToProject = true;
    	}
    	else {
    		_addToProject = false;
    	}
    	ContactManager.addContact(newContact);
    	saveContacts();
    }

    private void saveContacts() {
		ContactManager.saveContactList();
		contactsAllPanel.getTable().refresh();
		contactsProjectPanel.getTable().refresh();
        parentPanel.calendar.jnCalendar.updateUI();
        parentPanel.updateIndicators();
    }

    void removeContactB_actionPerformed(ActionEvent e) {
		String msg;
		Contact contact;
		ContactsTable cTable = null;
		if(_selectionContext == SelectionContext.ALL) {
			cTable = contactsAllPanel.getTable();
		}
		else if(_selectionContext == SelectionContext.PROJECT) {
			cTable = contactsProjectPanel.getTable();
			
		}
		if(cTable != null) {
			if(_selectionContext == SelectionContext.ALL) {
				if(cTable.getSelectedRows().length > 1) 
					msg = Local.getString("WARNING: This will remove the selected contacts from all projects and delete the contacts") + " " + cTable.getSelectedRows().length 
						+ " " + Local.getString("contacts") + "\n" + Local.getString("Are you sure?");
				else {
					contact = (Contact)cTable.getModel().getValueAt(
							cTable.getSelectedRow(),
		                ContactsTable.CONTACT);
					msg = Local.getString("WARNING: This will remove the selected contact from all projects and delete the contact") + "\n'" 
						+ contact.getFirstName() + " " + contact.getLastName() + "'\n" + Local.getString("Are you sure?");
				}

		        int n =
		            JOptionPane.showConfirmDialog(
		                App.getFrame(),
		                msg,
		                Local.getString("Remove contacts"),
		                JOptionPane.YES_NO_OPTION);
		        if (n != JOptionPane.YES_OPTION) return;
			}

	        for(int i=0; i< cTable.getSelectedRows().length; i++) {
				contact = (Contact) cTable.getModel().getValueAt(
						cTable.getSelectedRows()[i], ContactsTable.CONTACT);
				if(_selectionContext == SelectionContext.ALL) {
					ContactManager.removeContact(contact);
				}
				else if(_selectionContext == SelectionContext.PROJECT) {
					contact.removeProject(CurrentProject.get());
					ContactManager.updateContact(contact);
				}
			}
	        contactsAllPanel.getTable().getSelectionModel().clearSelection();
	        contactsAllPanel.getTable().getSelectionModel().clearSelection();
	        saveContacts();
		}
    }

    class PopupListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
            	editContactB_actionPerformed(null);
            }
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
            	contactPPMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    void ppEditContact_actionPerformed(ActionEvent e) {
        editContactB_actionPerformed(e);
    }
    void ppRemoveContact_actionPerformed(ActionEvent e) {
        removeContactB_actionPerformed(e);
    }
    void ppNewContact_actionPerformed(ActionEvent e) {
        newContactB_actionPerformed(e);
    }
}

