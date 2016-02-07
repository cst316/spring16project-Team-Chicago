package net.sf.memoranda;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;

import net.sf.memoranda.util.CurrentStorage;
import net.sf.memoranda.util.Local;
import nu.xom.*;

/**This class contains methods used for managing the ContactList, including methods to add, update and remove
 * contacts from the ContactList
 */
public class ContactManager {
	
	public static ContactList contactList;
	public static Document doc;
	public static Locale locale;
	
	static {
		CurrentStorage.get().openContactList();
		locale = Local.getCurrentLocale();
		initialize(locale);
	}
	
	
	private ContactManager() {}
	
	
	private static void initialize(Locale locale) {
		Collator collator = getAppropriateCollator(locale);
		if(doc == null) {
			contactList = new ContactList(collator);
		}
		else {
			contactList = new ContactList(doc, collator);
		}
		doc = contactList.toDocument();
	}
	
	
	
	/**Changes the language used for alphabetical ordering of the ContactList
	 * @param locale The locale to be used for alphabetical ordering of the ContactList.
	 */
	public static void changeLanguage(Locale locale) {
		Collator collator = getAppropriateCollator(locale);
		contactList.setCollator(collator);
	}
	
	private static Collator getAppropriateCollator(Locale locale) {
		if(locale == null) throw new NullPointerException();
		Collator collator = Collator.getInstance(locale);
		collator.setStrength(Collator.PRIMARY);
		return collator;
	}
	
	
	/**Returns the nu.xom.Document object containing an XML representation of the ContactList
	 * @return nu.xom.Document object containing an XML representation of the ContactList
	 */
	public static Document getDoc() {
		return contactList.toDocument();
	}
	
	
	/**Sets the nu.xom.Document object to be used for initializing the ContactList
	 * @param document
	 */
	public static void setDoc(Document document) {
		//TODO: add validation of the Document format
		doc = document;
	}
	
	
	/**Returns an ArrayList of Contacts which start with the search string passed in. If the search
	 * string is less than 3 characters, null is returned. This is to help with performance issues that
	 * may result from an instant search feature. The format being matched against is "{firstName} {lastName}"
	 * @param searchString The string used to search through the ContactList
	 * @return An ArrayList of Contacts that start with the search string passed in
	 */
	public static ArrayList<Contact> searchByName(String searchString) {
		if(searchString.length() <= 2) return null;
		return contactList.searchByName(searchString);
	}
	
	
	/**Adds a Contact to the ContactList and returns a copy of the inserted contact with its newly generated
	 * id. The Contact that is returned can then be used to update or remove its copy within the ContactList
	 * @param contact The Contact object to be added
	 * @return A copy of the Contact object added to the ContactList with its newly generated id
	 */
	public static Contact addContact(Contact contact) {
		String id = contactList.addCopy(contact);
		return contactList.getCopy(id);
	}
	
	
	/**Updates a Contact within the ContactList. The Contact passed in must have the same id as the Contact
	 * returned from the addContact method or previous calls to the updateContact method. A new Contact copy
	 * is then returned with a newly generated id which can then be used to update or remove the updated Contact
	 * within the ContactList
	 * @param contact The Contact object to be updated.
	 * @return A copy of the updated Contact with a newly generated id
	 */
	public static Contact updateContact(Contact contact) {
		contactList.removeContact(contact.getId());
		String id = contactList.addCopy(contact);
		return contactList.getCopy(id);
	}
	
	
	/**Removes a Contact from the ContactList. The Contact passed in must have the same id as the Contact
	 * returned from the addContact or updateContact methods.
	 * @param contact The Contact object to be removed from the ContactList
	 */
	public static void removeContact(Contact contact) {
		contactList.removeContact(contact.getId());
	}
	
	
	/**Returns an ArrayList containing copies of all the Contacts in the ContactList
	 * @return An ArrayList containing copies of all the Contacts in the ContactList
	 */
	public static ArrayList<Contact> getAllContacts() {
		return contactList.toArrayList();
	}
	
	
	/**Returns an ArrayList containing copies of all the Contacts in the ContactList
	 * that are associated with the project given
	 * @param A project to use for searching
	 * @return An ArrayList containing copies of all the Contacts in the ContactList
	 * associated with the project given
	 */
	public static ArrayList<Contact> getProjectContacts(Project project) {
		ArrayList<Contact> returnContacts = null;
		if(project != null) {
			ArrayList<Contact> projectContacts = new ArrayList<Contact>();
			getAllContacts().forEach(contact -> {
				String[] ids = contact.getProjectIDs();
				for(int i = 0; i < ids.length; i++) {
					if(ids[i].equals(project.getID())) {
						projectContacts.add(contact);
					}
				}
			});
			returnContacts = projectContacts;
		}
		return returnContacts;
	}
	
	
	/**Returns the number of Contacts in the ContactList
	 * @return The number of Contacts in the ContactList
	 */
	public static int numOfContacts() {
		return contactList.size();
	}
	
	
	/**Saves the ContactList to an XML document
	 * 
	 */
	public static void saveContactList() {
		CurrentStorage.get().storeContactList();
	}
}
