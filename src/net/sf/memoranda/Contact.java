package net.sf.memoranda;

import java.util.ArrayList;

import net.sf.memoranda.util.Util;
import nu.xom.Element;
import nu.xom.Elements;
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
	private ArrayList<String> projectIDs = new ArrayList<String>();
	
	//public method to set the unique id - Called in both constructors
	//Utilizes the generateId method in net.sf.memoranda.util.Util
	public String obtainId(){
		String id = Util.generateId();
		return id;
	}
	
	//Constructor requiring all attributes except organization and id
	//id will automatically generate using the generateId method in net.sf.memoranda.util.Util
	public Contact(String fname, String lname, String phoneNumber, String emailAddress) {
		
		this.firstName = fname;
		this.lastName = lname;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.id = obtainId();
		this.organization = "";
	}

	//Constructor requiring just first and last name
	//id will automatically generate using the generateId method in net.sf.memoranda.util.Util
	public Contact(String fname, String lname) {
		
		this.firstName = fname;
		this.lastName = lname;
		this.phoneNumber = "";
		this.emailAddress = "";
		this.id = obtainId();
		this.organization ="";
	}
	
	/**
	 * Reads in an XML element and constructs a Contact
	 * Sets empty fields to an Empty String
	 * If id exists uses it else generates new id
	 * @param element
	 */
	public Contact(Element element) {
		
		Elements children = element.getChildElements();
		int numelems = element.getChildCount();
		
		for(int i=0;i<numelems;i++){
			if(children.get(i).getLocalName().equalsIgnoreCase("FirstName")){
				this.firstName = children.get(i).getValue();
			}else if(children.get(i).getLocalName().equalsIgnoreCase("LastName")){
				this.lastName = children.get(i).getValue();
			}else if(children.get(i).getLocalName().equalsIgnoreCase("PhoneNumber")){
				if(children.get(i).getValue() == null){
					this.phoneNumber = "";
				}else{
					this.phoneNumber = children.get(i).getValue();
				}
			}else if(children.get(i).getLocalName().equalsIgnoreCase("E-Mail")){
				if(children.get(i).getValue() == null){
					this.emailAddress = "";
				}else{
					this.emailAddress = children.get(i).getValue();
				}
			}else if(children.get(i).getLocalName().equalsIgnoreCase("Organization")){
				if(children.get(i).getValue() == null){
					this.organization = "";
				}else{
					this.organization = children.get(i).getValue();
				}
			}else if(children.get(i).getLocalName().equalsIgnoreCase("ID")){
				if(children.get(i).getValue() == null){
					this.id = obtainId();
				}else{
					this.id = children.get(i).getValue();
				}
			}else if(children.get(i).getLocalName().equalsIgnoreCase("Projects")) {
				Elements projects = children.get(i).getChildElements();
				int size = projects.size();
				for(int j = 0; j < size; j++) {
					if(projects.get(j).getLocalName().equalsIgnoreCase("project")) {
						projectIDs.add(projects.get(j).getValue());
					}
				}
			}
		}// end for loop
	}

// Getters and Setters for the attributes - No Public method to set the ID - ensures can't be changed
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String name) {
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
		
		String contact = "Name: " +this.getFirstName() + " " +this.getLastName();
		contact += ", Phone Number: " +this.getPhoneNumber();
		contact += ", Email Address: "  +this.getEmailAddress();
		return contact;
	}
	
	//Copies the contact in to a new contact and returns copy
	public Contact copy(){
		Contact copy = new Contact(this.getFirstName(),this.getLastName(), this.getPhoneNumber(),this.getEmailAddress());
		copy.organization = this.organization;
		copy.id = this.id;
		projectIDs.forEach(projectID -> {
			copy.projectIDs.add(projectID);
		});
		return copy;
	}
	
	/**
	 * Converts A Contact Object to XML Element 
	 * uses nu.xom
	 * 
	 * @return XML Element of Contact
	 */
	public Element toElement() {
		Element root = new Element("Contact");
		Element firstName = new Element("FirstName");
		Element lastName = new Element("LastName");
		Element phone = new Element("PhoneNumber");
		Element email = new Element("E-Mail");
		Element org = new Element("Organization");
		Element id = new Element("ID");
		Element projects = new Element("projects");
		
		firstName.appendChild(this.getFirstName());
		lastName.appendChild(this.getLastName());
		phone.appendChild(this.getPhoneNumber());
		email.appendChild(this.getEmailAddress());
		org.appendChild(this.getOrganization());
		id.appendChild(this.getId());
		this.projectIDs.forEach(projectID -> {
			Element project = new Element("project");
			project.appendChild(projectID);
			projects.appendChild(project);
		});

		root.appendChild(firstName);
		root.appendChild(lastName);
		root.appendChild(phone);
		root.appendChild(email);
		root.appendChild(org);
		root.appendChild(id);
		root.appendChild(projects);
		
		return root;
	}

	public boolean addProject(Project project) {
		boolean success = false;
		if(!projectIDs.contains(project.getID())) {
			projectIDs.add(project.getID());
			success = true;
		}
		return success;
	}

	public boolean removeProject(Project project) {
		return projectIDs.remove(project.getID());
	}
	
	public String[] getProjectIDs() {
		String[] projects = new String[projectIDs.size()];
		projectIDs.toArray(projects);
		return projects;
	}

	public boolean inProject(Project project) {
		boolean exists = false;
		if(project != null) {
			for(int i = 0; i < projectIDs.size(); i++) {
				String id = projectIDs.get(i);
				if(id.equals(project.getID())) {
					exists = true;
				}
			}
		}
		return exists;
	}
	
}
