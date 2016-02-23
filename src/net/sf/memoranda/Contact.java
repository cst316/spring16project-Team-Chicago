package net.sf.memoranda;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.validator.routines.EmailValidator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import net.sf.memoranda.util.Util;
import nu.xom.Element;
import nu.xom.Elements;

/**
 * Contact class.
 * 
 * Defines attributes and methods for a contact
 */
public class Contact {

	private static EmailValidator _emailValidator = EmailValidator.getInstance(true, true);
	private static PhoneNumberUtil _phoneNumberUtil = PhoneNumberUtil.getInstance();
	private String _firstName;
	private String _lastName;
	private String _phoneNumber;
	private String _emailAddress;
	private String _organization;
	private String _id;
	private long _timestamp;
	private HashSet<String> _projectIDs = new HashSet<String>(20, 20);

	/**
	 * Constructor requiring all attributes except organization and id. An ID
	 * and timestamp will automatically be generated.
	 * 
	 * @param fname First name
	 * @param lname Last name
	 * @param phoneNumber Phone number
	 * @param emailAddress Email Address
	 * @throws IllegalArgumentException If the first name is empty
	 * @throws IllegalEmailException If the email address is in an invalid
	 *             format
	 * @throws IllegalPhoneNumberException If the phone number is in an invalid
	 *             format
	 */
	public Contact(String fname, String lname, String phoneNumber, String emailAddress)
			throws IllegalArgumentException, IllegalEmailException, IllegalPhoneNumberException {
		this.setFirstName(fname);
		this._lastName = lname;
		this.setPhoneNumber(phoneNumber);
		this.setEmailAddress(emailAddress);
		this._id = obtainID();
		this._organization = "";
		this.resetTimestamp();
	}

	/**
	 * Constructor requiring just first and last name. An ID and timestamp will
	 * be automatically generated.
	 * 
	 * @param fname First name
	 * @param lname Last name
	 * @throws IllegalArgumentException If the first name is empty
	 */
	public Contact(String fname, String lname) throws IllegalArgumentException {
		this.setFirstName(fname);
		this._lastName = lname;
		this.setPhoneNumber("");
		this.setEmailAddress("");
		this._id = obtainID();
		this._organization = "";
		this.resetTimestamp();
	}

	/**
	 * Reads in an XML element and constructs a Contact Sets empty fields to an
	 * Empty String If id exists uses it else generates new id.
	 * 
	 * @param element An Element representation of a Contact
	 * @throws IllegalArgumentException If the first name is empty
	 * @throws IllegalEmailException If the email address is in an invalid
	 *             format
	 * @throws IllegalPhoneNumberException If the phone number is in an invalid
	 *             format
	 */
	public Contact(Element element)
			throws IllegalArgumentException, IllegalEmailException, IllegalPhoneNumberException {
		final Elements children = element.getChildElements();
		final int numElems = element.getChildCount();

		for (int i = 0; i < numElems; i++) {
			if (children.get(i).getLocalName().equalsIgnoreCase("FirstName")) {
				this.setFirstName(children.get(i).getValue());

			}
			else if (children.get(i).getLocalName().equalsIgnoreCase("LastName")) {
				this._lastName = children.get(i).getValue();
			}
			else if (children.get(i).getLocalName().equalsIgnoreCase("PhoneNumber")) {
				final String phoneNumber = children.get(i).getValue();
				if (phoneNumber == null) {
					this.setPhoneNumber("");
				}
				else {
					this.setPhoneNumber(phoneNumber);
				}
			}
			else if (children.get(i).getLocalName().equalsIgnoreCase("E-Mail")) {
				final String email = children.get(i).getValue();
				if (email == null) {
					this.setEmailAddress("");
				}
				else {
					this.setEmailAddress(email);
				}
			}
			else if (children.get(i).getLocalName().equalsIgnoreCase("Organization")) {
				if (children.get(i).getValue() == null) {
					this._organization = "";
				}
				else {
					this._organization = children.get(i).getValue();
				}
			}
			else if (children.get(i).getLocalName().equalsIgnoreCase("ID")) {
				if (children.get(i).getValue() == null) {
					this._id = obtainID();
				}
				else {
					this._id = children.get(i).getValue();
				}
			}
			else if (children.get(i).getLocalName().equalsIgnoreCase("Timestamp")) {
				if (children.get(i).getValue() == null) {
					this.resetTimestamp();
				}
				else {
					try {
						final String time = children.get(i).getValue();
						this._timestamp = Long.parseLong(time);
					}
					catch (NumberFormatException nfe) {
						this.resetTimestamp();
					}
				}
			}
			else if (children.get(i).getLocalName().equalsIgnoreCase("Projects")) {
				final Elements projects = children.get(i).getChildElements();
				final int size = projects.size();
				for (int j = 0; j < size; j++) {
					if (projects.get(j).getLocalName().equalsIgnoreCase("project")) {
						_projectIDs.add(projects.get(j).getValue());
					}
				}
			}
		}
		if (this._timestamp == 0) {
			resetTimestamp();
		}
	}

	// Getters and Setters for the attributes - No Public method to set the ID -
	// ensures can't be changed

	public String getID() {
		return _id;
	}

	public void setID(String id) {
		this.resetTimestamp();
		this._id = id;
	}

	public String getFirstName() {
		return _firstName;
	}

	public void setFirstName(String name) throws IllegalArgumentException {
		name = name.trim();
		if (name.length() > 0) {
			this.resetTimestamp();
			this._firstName = name;
		}
		else {
			throw new IllegalArgumentException("Empty first name");
		}
	}

	public String getLastName() {
		return _lastName;
	}

	public void setLastName(String lastName) {
		this.resetTimestamp();
		this._lastName = lastName;
	}

	public String getPhoneNumber() {
		return _phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) throws IllegalPhoneNumberException {
		if (phoneNumber.length() > 0) {
			this._phoneNumber = parsePhoneNumber(phoneNumber);
			this.resetTimestamp();
		}
		else {
			this._phoneNumber = "";
			this.resetTimestamp();
		}
	}

	public String getEmailAddress() {
		return _emailAddress;
	}

	public void setEmailAddress(String emailAddress) throws IllegalEmailException {
		if (!_emailValidator.isValid(emailAddress) && emailAddress.length() > 0) {
			throw new IllegalEmailException("Not a valid email format: " + emailAddress);
		}
		else {
			this.resetTimestamp();
			this._emailAddress = emailAddress;
		}
	}

	public String getOrganization() {
		return _organization;
	}

	public void setOrganization(String organization) {
		this.resetTimestamp();
		this._organization = organization;
	}

	/**
	 * Checks if the phone number entered is a valid phone number. This does not
	 * check to see if the phone number is active, only if it is the correct
	 * format for a possible number.
	 * 
	 * @param phone The phone number to be tested
	 * @return True if the phone number is valid, false if not
	 */
	public static boolean isValidPhoneNumber(String phone) {
		boolean valid = false;
		if (phone.length() > 0) {
			try {
				final PhoneNumber number = _phoneNumberUtil.parse(phone, "US");
				if (_phoneNumberUtil.isValidNumber(number)) {
					valid = true;
				}
			}
			catch (NumberParseException e) {
				return false;
			}
		}
		return valid;
	}

	/**
	 * Parses a phone number and returns a formatted version. If the phoneNumber
	 * string is not a valid phone number, an IllegalPhoneNumberException is
	 * thrown.
	 * 
	 * @param phoneNumber The phone number to be parsed.
	 * @return A formatted String of the parsed phone number.
	 * @throws IllegalPhoneNumberException Thrown if the phone number entered is
	 *             not valid
	 */
	public static String parsePhoneNumber(String phoneNumber) throws IllegalPhoneNumberException {
		String parsedNumber = "";
		if (isValidPhoneNumber(phoneNumber)) {
			try {
				final PhoneNumber number = _phoneNumberUtil.parse(phoneNumber, "US");
				parsedNumber = _phoneNumberUtil.format(number, PhoneNumberFormat.INTERNATIONAL);
			}
			catch (NumberParseException e) {
				throw new IllegalPhoneNumberException("Not a valid phone number format: " + phoneNumber);
			}
		}
		else {
			throw new IllegalPhoneNumberException("Not a valid phone number format: " + phoneNumber);
		}
		return parsedNumber;
	}

	/**
	 * Tests if the input string is a valid email. Valid emails are
	 * case-insensitive and allow the domain name to be an ip address or a
	 * Top-level domain. Examples of valid email addresses are as follows:
	 * <p>
	 * john.doe@email.com kelly@localhost paul@192.168.42.10
	 * </p>
	 * . This does not check if the email address is active, but only checks if
	 * it is a valid format.
	 * 
	 * @param email The email address to be tested
	 * @return True if the email is valid, false if not.
	 */
	public static boolean isValidEmail(String email) {
		boolean valid = false;
		if (email.length() > 0) {
			if (_emailValidator.isValid(email)) {
				valid = true;
			}
		}
		return valid;
	}

	/**
	 * Returns a new ID String.
	 * 
	 * @return A new ID String.
	 */
	public String obtainID() {
		return Util.generateId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String contact = "Name: " + this.getFirstName() + " " + this.getLastName();
		contact += ", Phone Number: " + this.getPhoneNumber();
		contact += ", Email Address: " + this.getEmailAddress();
		return contact;
	}

	/**
	 * Copies the contact in to a new contact and returns copy.
	 * 
	 * @return A copy of the Contact
	 */
	public Contact copy() {
		final Contact copy = new Contact(this.getFirstName(), this.getLastName(), this.getPhoneNumber(),
				this.getEmailAddress());
		copy._organization = this._organization;
		copy._id = this._id;
		_projectIDs.forEach(projectID -> {
			copy._projectIDs.add(projectID);
		});
		copy._timestamp = this._timestamp;
		return copy;
	}

	/**
	 * Converts A Contact Object to XML Element uses nu.xom
	 * 
	 * @return XML Element of Contact
	 */
	public Element toElement() {
		final Element root = new Element("Contact");
		final Element firstName = new Element("FirstName");
		final Element lastName = new Element("LastName");
		final Element phone = new Element("PhoneNumber");
		final Element email = new Element("E-Mail");
		final Element org = new Element("Organization");
		final Element id = new Element("ID");
		final Element projects = new Element("projects");
		final Element timestamp = new Element("Timestamp");

		firstName.appendChild(this.getFirstName());
		lastName.appendChild(this.getLastName());
		phone.appendChild(this.getPhoneNumber());
		email.appendChild(this.getEmailAddress());
		org.appendChild(this.getOrganization());
		id.appendChild(this.getID());
		timestamp.appendChild(String.valueOf(this.getTimestamp()));
		this._projectIDs.forEach(projectID -> {
			final Element project = new Element("project");
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
		root.appendChild(timestamp);

		return root;
	}

	/**
	 * Adds the project ID to the Contact's project ID set.
	 * 
	 * @param id The project ID to be added
	 * @return Returns true if the ID could be added and false if it could not,
	 *         such as for duplicate IDs or a null input
	 */
	public boolean addProjectID(String id) {
		boolean added = false;
		if (id != null) {
			added = _projectIDs.add(id);
		}
		return added;
	}

	/**
	 * Adds all the project IDs in the set to the Contact's project ID set.
	 * Duplicates are ignored.
	 * 
	 * @param ids A set of ID strings to be added.
	 */
	public void addAllProjectID(Set<String> ids) {
		final Iterator<String> idIterator = ids.iterator();
		for (String id; idIterator.hasNext();) {
			id = idIterator.next();
			if (id != null) {
				_projectIDs.add(id);
			}
		}
	}

	/**
	 * Removes the project ID from the Contact's project ID set.
	 * 
	 * @param id The project ID to be removed
	 * @return Returns true if the project id was removed and false if not
	 */
	public boolean removeProjectID(String id) {
		return _projectIDs.remove(id);
	}

	public Set<String> getProjectIDs() {
		final HashSet<String> ids = new HashSet<String>();
		final Iterator<String> idIterator = _projectIDs.iterator();
		for (String id; idIterator.hasNext();) {
			id = idIterator.next();
			ids.add(id);
		}
		return ids;
	}

	/**
	 * Resets the timestamp to the current system time in milliseconds.
	 * 
	 */
	public void resetTimestamp() {
		_timestamp = System.currentTimeMillis();
	}

	public void setTimestamp(long time) {
		_timestamp = time;
	}

	public long getTimestamp() {
		return _timestamp;
	}

	/**
	 * Clears all project IDs from this Contact.
	 * 
	 */
	public void clearIDs() {
		_projectIDs.clear();
	}

	/**
	 * Evaluates all the fields of the input Contact object and checks them
	 * against this instance. The timestamp, projectIDs, and contact ID are
	 * excluded from this check.
	 * 
	 * @param contact The Contact to be checked against.
	 * @return true if information is equal, false if not.
	 */
	public boolean hasSameInfo(Contact contact) {
		boolean same = true;
		if (contact == null || !_emailAddress.equals(contact._emailAddress) || !_firstName.equals(contact._firstName)
				|| !_lastName.equals(contact._lastName) || !_phoneNumber.equals(contact._phoneNumber)
				|| !_organization.equals(contact._organization)) {
			same = false;
		}
		return same;
	}
}
