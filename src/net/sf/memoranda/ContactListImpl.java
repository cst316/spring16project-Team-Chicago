package net.sf.memoranda;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.NavigableSet;
import java.util.TreeMap;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.NoSuchChildException;

//Sorry no methods have comments explaining them yet, I'll add them tomorrow.
public class ContactListImpl implements ContactList{

	private TreeMap<ContactLocaleDecorator, Contact> contactList;
	private HashMap<String, ContactLocaleDecorator> idList;
	private Collator collator;
	
	public ContactListImpl() {
		init();
	}
	
	public ContactListImpl(Document doc) {
		if(doc == null) throw new NullPointerException();
		init();
		buildElements(doc);
	}
	
	private void init() {
		collator = Collator.getInstance(Locale.US);
		collator.setStrength(Collator.PRIMARY);
		contactList = new TreeMap<ContactLocaleDecorator,Contact>();
		idList = new HashMap<String, ContactLocaleDecorator>(100);
	}
	
	private void buildElements(Document doc) {
		Element root = doc.getRootElement();
		if(!root.getLocalName().equals("contactlist")) throw new NoSuchChildException("contactlist");
		int size = root.getChildElements().size();
		for(int i = 0; i < size; i++) {
			Contact importedContact = new Contact(root.getChildElements().get(i));
			addCopy(importedContact);
		}
	}
	
	public void setCollator(Collator collator) {
		if(collator == null) throw new NullPointerException();
		this.collator = collator;
		rebuildListCollatorKeys();
		sort();
	}
	
	private void rebuildListCollatorKeys() {
		idList.forEach((id, cLD) -> {
			cLD.generateNewCollationKey();
		});
	}

	@Override
	public String addCopy(Contact contact) {
		if(contact == null) throw new NullPointerException();
		Contact contactCopy = contact.copy();
		String id = contactCopy.getId();
		contactCopy.setId(id);
		ContactLocaleDecorator cLD = new ContactLocaleDecorator(contactCopy, this);
		contactList.put(cLD, cLD.contact);
		idList.put(id, cLD);
		return contactCopy.getId();
	}
	
	@Override
	public Contact removeContact(String id) {
		ContactLocaleDecorator cLD = idList.remove(id);
		if(cLD == null) return null;
		Contact removedContact = contactList.remove(cLD);
		return removedContact;
	}

	@Override
	public Contact getCopy(String id) {
		ContactLocaleDecorator cLD = idList.get(id);
		if(cLD == null) return null;
		Contact actualContact = contactList.get(cLD);
		return actualContact.copy();
	}
	
	@Override
	public ArrayList<Contact> searchByName(String searchString) {
		NavigableSet<ContactLocaleDecorator> contacts = contactList.navigableKeySet();
		ArrayList<Contact> foundContacts = new ArrayList<Contact>();
		contacts.forEach(cLD -> {
			if(cLD.keyString.startsWith(searchString.toLowerCase())) {
				foundContacts.add(cLD.contact);
			}
		});
		return foundContacts;
	}

	@Override
	public Document toDocument() {
		Element root = new Element("contactlist");
		Document doc = new Document(root);
		contactList.values().forEach(contact -> {
			root.appendChild(contact.toElement());
		});
		return doc;
	}

	@Override
	public ArrayList<Contact> toArrayList() {
		ArrayList<Contact> arrayContacts = new ArrayList<Contact>();
		Collection<Contact> collContacts = contactList.values();
		collContacts.forEach(contact -> {
			arrayContacts.add(contact.copy());
		});
		return arrayContacts;
	}
	
	public Collator getCollatorCopy() {
		return (Collator) collator.clone();
	}
	
	private void sort() {
		TreeMap<ContactLocaleDecorator, Contact> newContactList = new TreeMap<ContactLocaleDecorator, Contact>();
		contactList.forEach((cLD, contact) -> {
			newContactList.put(cLD, contact);
		});
		this.contactList = newContactList;
	}
	
	class ContactLocaleDecorator implements Comparable<ContactLocaleDecorator>{
		
		Contact contact;
		ContactListImpl contactListImpl;
		CollationKey collationKey;
		String keyString;
		
		public ContactLocaleDecorator(Contact contact, ContactListImpl contactListImpl) {
			this.contact = contact;
			this.contactListImpl = contactListImpl;
			generateNewCollationKey();
		}

		public boolean equals(ContactLocaleDecorator cLD) {
			if(this.collationKey.compareTo(cLD.collationKey) == 0) return true;
			return false;
		}
		
		@Override
		public int compareTo(ContactLocaleDecorator cLD) {
			int comparison = this.collationKey.compareTo(cLD.collationKey);
			if(comparison == 0) comparison = 1;
			return comparison;
		}
		
		public void generateNewCollationKey() {
			String compName = "";
			String firstName = contact.getFirstName();
			String lastName = contact.getLastName();
			//String nickname = contact.getNickname();
			//if(firstName == null || lastName == null || nickname == null) throw new NullPointerException();
			if(firstName == null || lastName == null) throw new NullPointerException();
			if(firstName.length() > 0) compName += firstName + " ";
			if(lastName.length() > 0) compName += lastName + " ";
			//if(nickname.length() > 0) compName += nickname;
			compName.trim();
			this.keyString = compName.toLowerCase();
			this.collationKey = contactListImpl.getCollatorCopy().getCollationKey(compName);
		}
		
	}
	
}
