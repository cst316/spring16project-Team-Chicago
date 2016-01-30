package net.sf.memoranda.test;

import static org.junit.Assert.*;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import net.sf.memoranda.Contact;
import net.sf.memoranda.ContactList;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;


public class ContactListTest {
	
	private ContactList setupContactList;
	private Contact[] setupContacts;
	private String[] setupContactIDs;
	private char[] alphaChar;
	
	public void setupCharArray() {
		alphaChar = new char[26];
		char ch = 'a';
		for(int i = 0; i < 26; i++){
			alphaChar[i] = ch++;
		}
	}
	
	@Before
	public void setup() {
		setupContactList = new ContactList();
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
	public void contactListConstructorTest() {
		ContactList contactList = new ContactList();
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
		assertEquals(setupContacts.length-1,setupContactList.size());
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
			ContactList newContactList = new ContactList(newXMLDoc);
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
	
	@Test
	public void addRemoveEditAddTest() {
		Contact removedContact = setupContactList.removeContact(setupContactIDs[3]);
		String id = removedContact.getId();
		removedContact.setOrganization("ASU");
		String newID = setupContactList.addCopy(removedContact);
		assertTrue(id != newID);
		Contact editedContact = setupContactList.getCopy(newID);
		assertEquals(editedContact.getId(), newID);
		assertEquals(setupContacts.length, setupContactList.size());
	}
	
	@Test
	public void largeContactListTest() {
		setupCharArray();
		ContactList contactList = new ContactList();
		for(int i = 0; i < 1000; i++) {
			contactList.addCopy(new Contact(randomString(),randomString()));
		}
		assertEquals(contactList.getAllIDs().length,1000);
		
	}
	
	public String randomString() {
		String string = "";
		Random r = new Random();
		int size = r.nextInt(10);
		for(int i = 0; i < size; i++) {
			string += alphaChar[r.nextInt(alphaChar.length)];
		}
		return string;
	}
}
