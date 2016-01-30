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

/**The ContactList object maintains an ordered list of Contacts by first name, last name, and nickname in the String format
 * "{firstName} {lastName}". To ensure preservation the ordering of the list, the list adds copies of input
 * contacts rather so the contact in the list cannot be modified. To edit a contact in the list, the contact must first be
 * removed by the ID given when the contact was added, and then re-copied into the list. The ContactList also contains
 * a method for searching the list by a string. The search returns a list of contacts where
 * "{firstName} {lastName}" starts with the input string (eg. instant search).
 */
public class ContactList{

	private TreeMap<ContactLocaleDecorator, Contact> contactList;
	private HashMap<String, ContactLocaleDecorator> idList;
	private Collator collator;
	
	
	/**Constructs an empty ContactList
	 */
	public ContactList() {
		collator = Collator.getInstance(Locale.US);
		collator.setStrength(Collator.PRIMARY);
		init();
	}
	
	/**Constructs an empty ContactList and sets the Collator to be used for
	 * alphabetical ordering of the ContactList
	 * @param collator A Collator to be used for alhpabetical ordering
	 * @see java.text.Collator
	 */
	public ContactList(Collator collator) {
		if(collator == null) throw new NullPointerException();
		this.collator = collator;
		init();
	}
	
	
	/**Constructs the ContactList using an XML representation of a ContactList
	 * @param doc A nu.xom.Document object containing an XML representation of a ContactList
	 * @see nu.xom.Document
	 */
	public ContactList(Document doc) {
		if(doc == null) throw new NullPointerException();
		collator = Collator.getInstance(Locale.US);
		collator.setStrength(Collator.PRIMARY);
		init();
		buildElements(doc);
	}
	
	
	/**Constructs the ContactList using an XML representation of a ContactList and sets the Collator to be used for
	 * alphabetical ordering of the ContactList
	 * @param doc A nu.xom.Document object containing an XML representation of a ContactList
	 * @param collator A Collator to be used for alhpabetical ordering
	 * @see nu.xom.Document
	 * @see java.text.Collator
	 */
	public ContactList(Document doc, Collator collator) {
		if(doc == null || collator == null) throw new NullPointerException();
		this.collator = collator;
		init();
		buildElements(doc);
	}
	
	
	private void init() {
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
	
	
	/**Sets the Collator used for alphabetical ordering of the ContactList
	 * @param collator The new Collator to be used
	 * @see java.text.Collator
	 */
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
	

	/**Adds a copy of the input Contact object to the list. This is so the list maintains its alphabetical order, because
	 * the passed in Contact object cannot be modified externally, potentially effecting the proper order of the list.
	 * The copy of the input Contact will always receive a new id.
	 * 
	 * @param contact The contact to be copied into the list
	 * @return The id of the created contact (to be used for removal)
	 */
	public String addCopy(Contact contact) {
		if(contact == null) throw new NullPointerException();
		Contact contactCopy = contact.copy();
		String id = contactCopy.obtainId();
		contactCopy.setId(id);
		ContactLocaleDecorator cLD = new ContactLocaleDecorator(contactCopy, this);
		contactList.put(cLD, cLD.contact);
		idList.put(id, cLD);
		return contactCopy.getId();
	}
	
	
	/**Removes the Contact with the associated ID.
	 * 
	 * @param id The id of the string as returned by the addCopy method
	 * @return The Contact with the associated input id
	 */
	public Contact removeContact(String id) {
		ContactLocaleDecorator cLD = idList.remove(id);
		if(cLD == null) return null;
		Contact removedContact = contactList.remove(cLD);
		return removedContact;
	}

	
	/**Gets a copy of the Contact with the associated ID. This does NOT give you the actual Contact object in the list,
	 * as the actual Contact in the list cannot be modified without removal from the list. The copied Contact will retain
 	 * the id associated with the actual Contact in the ContactList.
 	 * 
	 * @param id The id of the string as returned by the addCopy method
	 * @return A copy of the Contact with the associated input id
	 */
	public Contact getCopy(String id) {
		ContactLocaleDecorator cLD = idList.get(id);
		if(cLD == null) return null;
		Contact actualContact = contactList.get(cLD);
		return actualContact.copy();
	}
	
	
	/**A search method which returns an ArrayList of copied Contacts whose name starts with the input String. The format
	 * which the input string is matched against is "{firstName} {lastName}". The copied contacts will retain
	 * the ids associated with the actual Contacts in the ContactList.
	 * 
	 * @param searchString The String to be used to search for Contacts
	 * @return An ArrayList containing the Contacts which start with the input searchString.
	 */
	public ArrayList<Contact> searchByName(String searchString) {
		NavigableSet<ContactLocaleDecorator> contacts = contactList.navigableKeySet();
		final ArrayList<Contact> foundContacts = new ArrayList<Contact>();
		contacts.forEach(cLD -> {
			if(cLD.keyString.startsWith(searchString.toLowerCase())) {
				foundContacts.add(cLD.contact);
			}
		});
		return foundContacts;
	}

	
	/** Creates a nu.xom.Document object that can be used for translating the ContactList into an XML file
	 * @return A nu.xom.Document object
	 */
	public Document toDocument() {
		Element root = new Element("contactlist");
		Document doc = new Document(root);
		contactList.values().forEach(contact -> {
			root.appendChild(contact.toElement());
		});
		return doc;
	}

	
	/**Creates an ArrayList with an ordered copy of the ContactList
	 * @return An ArrayList with an ordered copy of the ContactList
	 */
	public ArrayList<Contact> toArrayList() {
		ArrayList<Contact> arrayContacts = new ArrayList<Contact>();
		Collection<Contact> collContacts = contactList.values();
		collContacts.forEach(contact -> {
			arrayContacts.add(contact.copy());
		});
		return arrayContacts;
	}
	
	
	/**Returns a copy of the Collator used for alphabetical ordering
	 * @return A copy of the Collator used for alphabetical ordering
	 * @see java.text.Collator
	 */
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
	
	
	/** Returns the size of the ContactList
	 * @return The size of the ContactList
	 */
	public int size() {
		return contactList.size();
	}
	
	
	/**Returns an array of Strings containing the IDs of all contacts in the ContactList
	 * @return An array of Strings containing the IDs of all contacts in the ContactList
	 */
	public String[] getAllIDs() {
		String[] ids = new String[idList.size()];
		return idList.keySet().toArray(ids);
	}
	
	
	private class ContactLocaleDecorator implements Comparable<ContactLocaleDecorator>{
		
		Contact contact;
		ContactList contactListImpl;
		CollationKey collationKey;
		String keyString;
		
		ContactLocaleDecorator(Contact contact, ContactList contactListImpl) {
			this.contact = contact;
			this.contactListImpl = contactListImpl;
			generateNewCollationKey();
		}

		boolean equals(ContactLocaleDecorator cLD) {
			if(this.collationKey.compareTo(cLD.collationKey) == 0) return true;
			return false;
		}
		
		@Override
		public int compareTo(ContactLocaleDecorator cLD) {
			int comparison = this.collationKey.compareTo(cLD.collationKey);
			return comparison;
		}
		
		void generateNewCollationKey() {
			String compName = "";
			String firstName = contact.getFirstName();
			String lastName = contact.getLastName();
			//String nickname = contact.getNickname();
			//if(firstName == null || lastName == null || nickname == null) throw new NullPointerException();
			if(firstName == null || lastName == null) throw new NullPointerException();
			if(firstName.length() > 0) compName += firstName + " ";
			if(lastName.length() > 0) compName += lastName + " ";
			//if(nickname.length() > 0) compName += nickname;
			//compName += contact.getId();
			this.keyString = compName.toLowerCase();
			this.collationKey = contactListImpl.getCollatorCopy().getCollationKey(compName+contact.getId());
		}
		
	}
	
}
