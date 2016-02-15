package net.sf.memoranda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import net.sf.memoranda.util.CurrentStorage;
import nu.xom.*;

/**This class contains methods used for managing the ContactList, including methods to add, update and remove
 * contacts from the ContactList
 */
public class ContactManager {
	
	private static HashMap<String, Contact> _masterContactList;
	private static HashMap<String, HashMap<String, Contact>> _projectContactIDs;
	private static ArrayList<ContactManagerListener> _listeners;
	private static int MASTERLF = 50;
	private static int PROJECTLF = 20;
	
	
	static {
		Document doc = CurrentStorage.get().openMasterContactList();
		
		// Create an empty doc if the imported doc is null
		if(doc == null) {
			doc = new Document(new Element("contactlist"));
		}
		
		_initialize(doc);
	}
	
	
	/**Adds a Contact to the ContactList and returns a copy of the inserted contact with its newly generated
	 * id. The Contact that is returned can then be used to update or remove its copy within the ContactList
	 * @param contact The Contact object to be added
	 * @return A copy of the Contact object added to the ContactList with its newly generated id
	 */
	public static boolean addContact(Contact contact) {
		boolean addable = _addContact(contact);
		if(addable) {
			_notifyListeners();
		}
		return addable;
	}
	
	
	/**Updates a Contact within the ContactList. The Contact passed in must have the same id as the Contact
	 * returned from the addContact method or previous calls to the updateContact method. A new Contact copy
	 * is then returned with a newly generated id which can then be used to update or remove the updated Contact
	 * within the ContactList
	 * @param contact The Contact object to be updated.
	 * @return A copy of the updated Contact with a newly generated id
	 */
	public static boolean updateContact(Contact contact) {
		boolean updated = _updateContact(contact);
		if(updated) {
			_notifyListeners();
		}
		return updated;
	}
	
	
	/**Removes a Contact from the ContactList. The Contact passed in must have the same id as the Contact
	 * returned from the addContact or updateContact methods.
	 * @param contact The Contact object to be removed from the ContactList
	 */
	public static Contact removeContact(Contact contact) {
		Contact removed = _removeContact(contact);
		if(removed != null) {
			_notifyListeners();
		}
		return removed;
	}
	
	
	/**Returns an ArrayList containing copies of all the Contacts in the ContactList
	 * @return An ArrayList containing copies of all the Contacts in the ContactList
	 */
	public static ArrayList<Contact> getAllContacts() {
		ArrayList<Contact> returnContacts = new ArrayList<Contact>();
		Iterator<String> idIterator = _masterContactList.keySet().iterator();
		for(String id; idIterator.hasNext();) {
			id = idIterator.next();
			returnContacts.add(_masterContactList.get(id).copy());
		}
		return returnContacts;
	}
	
	
	/**Returns an ArrayList containing copies of all the Contacts that are associated with the 
	 * project given
	 * @param A project to use for searching
	 * @return An ArrayList containing copies of all the Contacts in the ContactList
	 * associated with the project given
	 */
	public static ArrayList<Contact> getContacts(Project project) {
		ArrayList<Contact> returnContacts = new ArrayList<Contact>();
		if(project != null) {
			HashMap<String, Contact> contacts = _projectContactIDs.get(project.getID());
			if(contacts != null) {
				Iterator<String> idIterator = contacts.keySet().iterator();
				for(String id; idIterator.hasNext();) {
					id = idIterator.next();
					Contact contact = contacts.get(id);
					if(contact != null) {
						returnContacts.add(contact.copy());
					}
				}
			}
		}
		return returnContacts;
	}
	

	/**Returns an HashMap containing copies of all the Contacts that are associated with the 
	 * project given
	 * @param A project to use for searching
	 * @return A HashMap containing copies of all the Contacts in the ContactList
	 * associated with the project given
	 */
	public static HashMap<String, Contact> getContactsHashMap(Project project) {
		HashMap<String, Contact> returnContacts = new HashMap<String, Contact>();
		if(project != null) {
			HashMap<String, Contact> contacts = _projectContactIDs.get(project.getID());
			Iterator<String> idIterator = contacts.keySet().iterator();
			returnContacts = new HashMap<String, Contact>(contacts.size());
			for(String id; idIterator.hasNext();) {
				id = idIterator.next();
				Contact contact = contacts.get(id);
				if(contact != null) {
					returnContacts.put(id, contact.copy());
				}
			}
		}
		return returnContacts;
	}
	
	
	/**Returns the number of Contacts in the ContactList
	 * @return The number of Contacts in the ContactList
	 */
	public static int numOfContacts() {
		return _masterContactList.size();
	}
	
	
	/**Saves the master and project contact lists to their representative XML documents
	 * 
	 */
	public static void saveContactList() {
		CurrentStorage.get().storeMasterContactList();
		Vector<Project> projects = ProjectManager.getAllProjects();
		projects.forEach(project -> {
			CurrentStorage.get().storeProjectContactList(project);
		});
	}
	
	
	/**Imports a <code>Document</code> object that contains a contact list representation based on
	 * the input <code>Project</code> . If a contact id in the document matches a contact id in the 
	 * master list, then the contact with the newest timestamp will overwrite the older.
	 * 
	 * @param document A document object containing a representation of a contact list
	 * @param project The project that the document is associated with
	 * @See Contact
	 */
	public static void importProjectContacts(Document document, Project project) {
		if(document != null && project != null) {
			if (!_projectContactIDs.containsKey(project.getID())) {
				_projectContactIDs.put(project.getID(), new HashMap<String, Contact>(PROJECTLF, PROJECTLF));
			}
			_mergeImportContacts(document, project);
			saveContactList();
			_notifyListeners();
		}
	}
	
	
	/**Returns the nu.xom.Document object containing an XML representation of the master contact list
	 * @return nu.xom.Document object containing an XML representation of the master contact list
	 */
	public static Document toDocument() {
		Element root = new Element("contactlist");
		Document doc = new Document(root);
		Iterator<String> idIterator = _masterContactList.keySet().iterator();
		for(String id; idIterator.hasNext();) {
			id = idIterator.next();
			Contact contact = _masterContactList.get(id);
			root.appendChild(contact.toElement());
		}
		return doc;
	}
	
	
	/**Returns the nu.xom.Document object containing an XML representation of the project contact list
	 * @return nu.xom.Document object containing an XML representation of the project contact list
	 */
	public static Document toProjectDocument(Project project) {
		Element rootElement = new Element("contactlist");
		ArrayList<Contact> contacts = getContacts(project);
		contacts.forEach((contact) -> {
			// Don't save any projects the contact is associated with since it's not needed.
			contact.clearIDs();
			rootElement.appendChild(contact.toElement());
		});
		return new Document(rootElement);
	}
	
	
	/** Adds a <code>ContactManagerListener</code> which is notified when a contact has
	 * been added, removed, updated, or when a contact list has been imported
	 * @param listener The listener object to be added
	 * @return Returns true if the listener could be added and false if it could not
	 */
	public static boolean addContactManagerListener(ContactManagerListener listener) {
		return _listeners.add(listener);
	}
	
	
	/** Removes a <code>ContactManagerListener</code>
	 * @param listener The listener object to be removed
	 * @return Returns true if the listener could be removed and false if it could not
	 */
	public static boolean removeContactManagerListener(ContactManagerListener listener) {
		return _listeners.remove(listener);
	}
	
	
	private ContactManager() {}
	
	
	private static void _buildElements(Document doc) {
		Element root = doc.getRootElement();
		if(!root.getLocalName().equals("contactlist")) throw new NoSuchChildException("contactlist");
		int size = root.getChildElements().size();
		
		// Master contacts hashmap size should be a minimum of 100 and increase in MASTERLF intervals
		int initHashSize = MASTERLF * ((size / MASTERLF) + 1);
		if(initHashSize < 100) {
			initHashSize = 100;
		}
		_masterContactList = new HashMap<String, Contact>(initHashSize, MASTERLF);
		
		// Add contacts to list
		for(int i = 0; i < size; i++) {
			Contact importedContact = new Contact(root.getChildElements().get(i));
			_addContact(importedContact);
		}
	}
	

	private static void _initialize(Document doc) {
		_projectContactIDs = new HashMap<String, HashMap<String, Contact>>(PROJECTLF, PROJECTLF);
		_listeners = new ArrayList<ContactManagerListener>();
		_buildElements(doc);
		
		// Build project/contact maps
		Vector<Project> projects = ProjectManager.getAllProjects();
		projects.forEach(project -> {
			Document projectDoc = CurrentStorage.get().openProjectContactList(project);
			importProjectContacts(projectDoc, project);
		});
	}
	
	
	private static void _notifyListeners() {
		_listeners.forEach(listener -> {
			listener.contactManagerChanged();
		});
	}
	
	
	private static boolean _addContact(Contact contact) {
		boolean addable = !_masterContactList.containsKey(contact.getID());
		if(addable) {
			Contact newContact = contact.copy();
			_masterContactList.put(contact.getID(), contact);
			_addContactToProjects(newContact);
		}
		return addable;
	}
	
	
	private static boolean _updateContact(Contact contact) {
		boolean updatable = false;
		Contact removed = _removeContact(contact);
		if(removed != null) {
			_addContact(contact);
			updatable = true;
		}
		return updatable;
	}
	
	
	private static Contact _removeContact(Contact contact) {
		Contact removed = _masterContactList.remove(contact.getID());
		if(removed != null) {
			_removeContactFromProjects(contact);
		}
		return removed;
	}
	
	
	private static void _addContactToProjects(Contact contact) {
		Set<String> projectIDs = contact.getProjectIDs();
		Iterator<String> idIterator = projectIDs.iterator();
		for(String id; idIterator.hasNext();) {
			id = idIterator.next();
			if(id != null) {
				HashMap<String, Contact> contactHash = _projectContactIDs.get(id);
				if(contactHash == null) {
					contactHash = new HashMap<String, Contact>(PROJECTLF, PROJECTLF);
					_projectContactIDs.put(id, contactHash);
				}
				boolean addable = !contactHash.containsKey(contact.getID());
				if(addable) {
					contactHash.put(contact.getID(), contact);
				}
			}
		}
	}
	
	private static void _removeContactFromProjects(Contact contact) {
		Set<String> projectIDs = contact.getProjectIDs();
		Iterator<String> idIterator = projectIDs.iterator();
		for(String id; idIterator.hasNext();) {
			id = idIterator.next();
			HashMap<String, Contact> contactHash = _projectContactIDs.get(id);
			if(contactHash != null) {
				contactHash.remove(contact.getID());
			}
		}
	}
	
	private static void _mergeImportContacts(Document doc, Project prj) {
		HashMap<String, Contact> projectContacts = getContactsHashMap(prj);
		HashMap<String, Contact> importedContacts = new HashMap<String, Contact>();
		
		// If the project contact list doesnt exist, make a new one
		if(projectContacts == null) {
			projectContacts = new HashMap<String, Contact>();
			_projectContactIDs.put(prj.getID(), projectContacts);
		}
		
		// Parse the Document to get a list of contact elements
		Element root = doc.getRootElement();
		Elements contactListElements = root.getChildElements();
		int size = contactListElements.size();
		Contact importedContact;
		
		// Process each contact element in the document
		for(int i = 0; i < size; i++) {
			importedContact = new Contact(contactListElements.get(i));
			importedContacts.put(importedContact.getID(), importedContact);
			
			// Check if the contact is in the master list. If it exists, determine how to merge the contact.
			// If it doesn't, then just add the new contact.
			if(_masterContactList.containsKey(importedContact.getID())) {
				Contact masterContact = _masterContactList.get(importedContact.getID());
				
				// If the imported contact is newer than the master contact, replace the master contact info.
				// If not, then just add the project ID to the master contact.
				if(masterContact.getTimestamp() < importedContact.getTimestamp()) {
					Set<String> projectIDs = masterContact.getProjectIDs();
					importedContact.addAllProjectID(projectIDs);
					importedContact.addProjectID(prj.getID());
					_updateContact(importedContact);
				}
				else {
					masterContact.addProjectID(prj.getID());
				}
			}
			else {
				importedContact.addProjectID(prj.getID());
				_addContact(importedContact);
			}
		}
		
		size = projectContacts.size();
		Iterator<String> idIterator = projectContacts.keySet().iterator();
		
		// Get each contact currently associated with the project id and if it is not associated with
		// the imported contact, remove the current project id from the contact.
		for(String id; idIterator.hasNext();) {
			id = idIterator.next();
			Contact projectContact = projectContacts.get(id);
			
			if(projectContact != null && !importedContacts.containsKey(projectContact.getID())) {
				Contact masterContact = _masterContactList.get(projectContact.getID());
				masterContact.removeProjectID(prj.getID());
				_updateContact(masterContact);
			}
		}
	}
}
