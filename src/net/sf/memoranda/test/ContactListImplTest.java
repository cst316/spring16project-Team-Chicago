package net.sf.memoranda.test;

import static org.junit.Assert.*;

import java.text.Collator;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import net.sf.memoranda.Contact;
import net.sf.memoranda.ContactListImpl;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;


public class ContactListImplTest {
	
	private ContactListImpl setupContactList;
	private Contact[] setupContacts;
	private String[] setupContactIDs;
	
	@Before
	public void setup() {
		setupContactList = new ContactListImpl();
		this.setupContacts = new Contact[]{
			new Contact("Tim","Johnson","111-999-9999","tim.johnson@fakeemail.com"),
			new Contact("Tim","Johnson","111-999-9999","tim.johnson@fakeemail.com"),
			new Contact("Tim","Johnson","111-999-9999","tim.johnson@fakeemail.com"),
			new Contact("Bob","Johnson","222-999-9999","bob.johnson@fakeemail.com"),
			new Contact("Michelle","Smith","333-999-9999","ms@fakeemail.com"),
			new Contact("Tim","Brown","444-999-9999","tb@fakeemail.com"),
			new Contact("Kelly","Rodriguez","555-999-9999","kr@fakeemail.com"),
			new Contact("Victoria","Williams","666-999-9999","vw@fakeemail.com"),
			new Contact("James","Moore","777-999-9999","jm@fakeemail.com"),
			new Contact("Mitch","Walker","888-999-9999","mw@fakeemail.com"),
			new Contact("Stacy","Hall","999-999-9999","sh@fakeemail.com"),
			new Contact("Jack","Gonzalez","999-111-9999","jg@fakeemail.com"),
			new Contact("Jake","Johnson","999-222-9999","jj@fakeemail.com"),
			new Contact("Jackie","Gonzalez","999-222-9999","jj@fakeemail.com"),
			new Contact("Jasmine","Johnson","999-222-9999","jj@fakeemail.com")
		};
		setupContactIDs = new String[setupContacts.length];
		for(int i = 0; i < setupContacts.length; i++) {
			String id = setupContactList.addCopy(setupContacts[i]);
			setupContactIDs[i] = id;
		}
	}

	@Test
	public void contactListConstructor1Test() {
		ContactListImpl contactList = new ContactListImpl();
		Document mockDoc = new Document(new Element("contactlist"));
		Document cLDoc = contactList.toDocument();
		assertEquals(mockDoc.toXML(), cLDoc.toXML());
	}
	
	@Test
	public void addCopyTest() {
		ArrayList<Contact> contacts = setupContactList.toArrayList();
		assertEquals(contacts.size(), setupContacts.length);
		Contact prevContact = contacts.get(0);
		Contact currentContact;
		Collator collator = setupContactList.getCollatorCopy();
		for(int i = 1; i < contacts.size(); i++) {
			currentContact = contacts.get(i);
			String prevName = prevContact.getFirstName() + prevContact.getLastName();
			String currentName = currentContact.getFirstName() + currentContact.getLastName();
			if(collator.compare(prevName, currentName) >= 0) {
				fail();
			}
		}
	}
	
	@Test
	public void removeContactTest() {
		Contact removedContact = setupContactList.removeContact(setupContactIDs[2]);
		assertTrue(removedContact != null);
		setupContactList.toArrayList().forEach(contact -> {
			if(contact.equals(removedContact)) fail();
		});
	}
	
	@Test
	public void toDocumentTest() {
		Document doc = setupContactList.toDocument();
		assertEquals(doc.getRootElement().getLocalName(), "contactlist");
		assertEquals(doc.getRootElement().getChildElements().size(), setupContacts.length);
	}
	
	@Test
	public void reconstructContactsFromXML() {
		String xmlDoc = setupContactList.toDocument().toXML();
		Builder builder = new Builder();
		try {
			Document newXMLDoc = builder.build(xmlDoc, "utf-8");
			ContactListImpl newContactList = new ContactListImpl(newXMLDoc);
			ArrayList<Contact> oldContacts = setupContactList.toArrayList();
			ArrayList<Contact> newContacts = newContactList.toArrayList();
			assertEquals(oldContacts.size(), newContacts.size());
			for(int i = 0; i < oldContacts.size(); i++) {
				Contact oldC = oldContacts.get(i);
				Contact newC = newContacts.get(i);
				String oldName = oldC.getFirstName()+oldC.getLastName()+oldC.getPhoneNumber()+oldC.getEmailAddress();
				String newName = newC.getFirstName()+newC.getLastName()+newC.getPhoneNumber()+newC.getEmailAddress();
				assertEquals(oldName, newName);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void searchByNameTest() {
		ArrayList<Contact> foundContacts = setupContactList.searchByName("ja");
		assertEquals(foundContacts.size(), 5);
		Collator collator = setupContactList.getCollatorCopy();
		Contact prevContact = null;
		Contact currentContact;
		for(int i = 0; i < foundContacts.size(); i++) {
			currentContact = foundContacts.get(i);
			if(prevContact != null) {
				String prevName = prevContact.getFirstName() + prevContact.getLastName();
				String currentName = currentContact.getFirstName() + currentContact.getLastName();
				assertTrue(collator.compare(prevName, currentName) <= 0);
			}
			prevContact = currentContact;
		}
	}
	
	
}
