package net.sf.memoranda;

import net.sf.memoranda.util.Util;
/**
 * Contact class 
 * 
 * Defines attributes and methods for a contact
 */
public class Contact {
	
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String emailAddress;
	private String organization;
	private String id;
	
	//private method to set the unique id - Called in both constructors
	//Utilizes the generateId method in net.sf.memoranda.util.Util
	private String obtainId(){
		String id = Util.generateId();
		return id;
	}
	
	//Constructor requiring all attributes except organization and id
	//id will automatically generate using the generateId method in net.sf.memoranda.util.Util
	public Contact(String fname, String lname, String phoneNumber, String emailAddress) {
		
		this.firstName = fname;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.id = obtainId();
	}

	//Constructor requiring just first and last name
	//id will automatically generate using the generateId method in net.sf.memoranda.util.Util
	public Contact(String fname, String lname) {
		
		this.firstName = fname;
		this.lastName = lname;
		this.phoneNumber = "";
		this.emailAddress = "";
		this.id = obtainId();
	}

// Getters and Setters for the attributes - No Public method to set the ID - ensures can't be changed
	
	public String getId() {
		return id;
	}


	public String getfirstName() {
		return firstName;
	}


	public void setfirstName(String name) {
		this.firstName = name;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getOrganization() {
		return organization;
	}


	public void setOrganization(String organization) {
		this.organization = organization;
	}


	// Returns a string of the Contact
	public String toString(){
		
		String contact = "Name: " +this.getfirstName() + " " +this.getLastName();
		contact += ", Phone Number: " +this.getPhoneNumber();
		contact += ", Email Address: "  +this.getEmailAddress();
		return contact;
	}
	
	//Copies the contact in to a new contact and returns copy
	public Contact copy(){
		Contact copy = new Contact(this.getfirstName(),this.getLastName(), this.getPhoneNumber(),this.getEmailAddress());
		copy.id = this.id;
		return copy;
	}
	
}
