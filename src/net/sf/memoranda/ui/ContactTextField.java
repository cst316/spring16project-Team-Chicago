package net.sf.memoranda.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JTextField;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import net.sf.memoranda.Contact;
import net.sf.memoranda.ContactManager;

public class ContactTextField extends JTextField{
	public ContactTextField() {
		super();
		AutoCompleteDecorator.decorate(this, _populateComboBox(), true);
		
	}
	
	private static Vector<ComparableKeyPair<String, Contact>> _populateComboBox() {
		ArrayList<Contact> contacts = ContactManager.getAllContacts();
		Vector<ComparableKeyPair<String, Contact>> comboContacts = new Vector<ComparableKeyPair<String, Contact>>();
		Iterator<Contact> contactI = contacts.iterator();
		
		comboContacts.sort(new Comparator<ComparableKeyPair>() {

			@SuppressWarnings("unchecked")
			@Override
			public int compare(ComparableKeyPair o1, ComparableKeyPair o2) {
				return o1.compareTo(o2);
			}
			
		});
		
		for(Contact contact; contactI.hasNext();) {
			contact = contactI.next();
			String name = contact.getFirstName() + " " + contact.getLastName();
			comboContacts.add(new ComparableKeyPair<String, Contact>(name, contact));
		}
		
		return comboContacts;
	}
}
