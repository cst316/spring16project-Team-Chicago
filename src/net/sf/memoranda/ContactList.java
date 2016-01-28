package net.sf.memoranda;

import java.util.ArrayList;

import nu.xom.Document;

//The ContactList object maintains an ordered list of Contacts by first name, last name, and nickname in the String format
//"{firstName} {lastName} {nickname}". To ensure preservation the ordering of the list, the list adds copies of input
//contacts rather so the contact in the list cannot be modified. To edit a contact in the list, the contact must first be
//removed by the ID given when the contact was added, and then re-copied into the list. The ContactList also contains
//a method for searching the list by a string. The search returns a list of contacts where
//"{firstName} {lastName} {nickname}" starts with the input string (eg. instant search).
public interface ContactList {
	
	//General constructor with an empty list
	//
	//public ContactList();
	
	
	//Constructor that takes in a Document object, and parses through it to create the contact list, such as from a save
	//file.
	//
	//public ContactList(Document doc);
	
	
	//Adds a copy of the input Contact object to the list. This is so the list maintains its alphabetical order, because
	//the passed in Contact object cannot be modified externally, potentially effecting the proper order of the list.
	//The copy of the input Contact will always receive a new id.
	//
	//@param Contact The contact to be copied into the list
	//@return The id of the created contact (to be used for removal)
	public String addCopy(Contact contact);
	
	
	//Removes the Contact with the associated ID.
	//
	//@param id The id of the string as returned by the addCopy method
	//@return The Contact with the associated input id
	public Contact removeContact(String id);
	
	
	//Gets a copy of the Contact with the associated ID. This does NOT give you the actual Contact object in the list,
	//as the actual Contact in the list cannot be modified without removal from the list. The copied Contact will retain
	//the id associated with the actual Contact in the ContactList.
	//
	//@param id The id of the string as returned by the addCopy method
	//@return A copy of the Contact with the associated input id
	public Contact getCopy(String id);
	
	
	//A search method which returns an ArrayList of copied Contacts whose name starts with the input String. The format
	//which the input string is matched against is "{firstName} {lastName} {nickname}". The copied contacts will retain
	//the ids associated with the actual Contacts in the ContactList.
	//
	//@param searchString The String to be used to search for Contacts
	//@return An ArrayList containing the Contacts which start with the input searchString.
	public ArrayList<Contact> searchByName(String searchString);
	
	
	//Creates a nu.xom.Document object that can be used for translating the ContactList into an XML file
	public Document toDocument();
	
	
	//Creates an ArrayList with an ordered copy of the ContactList
	public ArrayList<Contact> toArrayList();
	
}
