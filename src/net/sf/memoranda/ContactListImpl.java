package net.sf.memoranda;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

import nu.xom.Document;
import nu.xom.Element;

//Sorry no methods have comments explaining them yet, I'll add them tomorrow.
public class ContactListImpl implements ContactList{

	private Document _doc;
	private Element _root;
	private TreeMap<ContactLocaleDecorator, Contact> contactList;
	private HashMap<String, ContactLocaleDecorator> idList;
	private Collator collator;
	
	public ContactListImpl() {
		_root = new Element("contactlist");
		_doc = new Document(_root);
		init();
	}
	
	public ContactListImpl(Document _doc) {
		this._doc = _doc;
		this._root = _doc.getRootElement();
		init();
		buildElements(_root);
	}
	
	private void init() {
		collator = Collator.getInstance(Locale.US);
		collator.setStrength(Collator.PRIMARY);
		contactList = new TreeMap<ContactLocaleDecorator,Contact>();
		idList = new HashMap<String, ContactLocaleDecorator>(100);
	}
	
	private void buildElements(Element parent) {
		//TODO add XML parser for rebuilding the Contact Tree
	}
	
	public void setCollator(Collator collator) {
		if(collator == null) throw new NullPointerException();
		this.collator = collator;
		rebuildListCollatorKeys();
	}
	
	private void rebuildListCollatorKeys() {
		idList.forEach((id, cLD) -> {
			cLD.generateNewCollationKey();
		});
	}

	@Override
	public String addCopy(Contact contact) {
		Contact contactCopy = contact.copy();
		ContactLocaleDecorator cLD = new ContactLocaleDecorator(contactCopy, this);
		contactList.put(cLD, cLD.contact);
		Element el = contactCopy.toElement();
		_root.appendChild(el);
		String id = contactCopy.getId();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document toDocument() {
		return _doc;
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
	
	private Collator getCollator() {
		return collator;
	}
	
	class ContactLocaleDecorator {
		
		Contact contact;
		ContactListImpl contactListImpl;
		CollationKey collationKey;
		
		public ContactLocaleDecorator(Contact contact, ContactListImpl contactListImpl) {
			this.contact = contact;
			this.contactListImpl = contactListImpl;
			this.collationKey = createComparableNameKey(contact);
		}

		public boolean equals(ContactLocaleDecorator cLD) {
			if(this.collationKey.compareTo(cLD.collationKey) == 0) return true;
			return false;
		}
		
		public int compareTo(ContactLocaleDecorator cLD) {
			return this.collationKey.compareTo(cLD.collationKey);
		}
		
		public void generateNewCollationKey() {
			this.collationKey = createComparableNameKey(this.contact);
		}
		
		private CollationKey createComparableNameKey(Contact contact) {
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
			return contactListImpl.getCollator().getCollationKey("compName");
		}
		
	}
	
}
