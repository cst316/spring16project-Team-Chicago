package net.sf.memoranda.tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.sf.memoranda.Contact;
import net.sf.memoranda.IllegalEmailException;
import net.sf.memoranda.IllegalPhoneNumberException;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectManager;
import net.sf.memoranda.date.CalendarDate;
import nu.xom.Document;
import nu.xom.Elements;

@SuppressWarnings("checkstyle:javadocmethod")
public class ContactTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private Contact[] _testContacts;

	@Before
	public void setUp() throws Exception {
		this._testContacts = new Contact[] { new Contact("Tom", "Jones", "256-345-1211", "tjones@att.net"),
				new Contact("Melissa", "Little", "602-286-3421", "hr@acmeco.com"), new Contact("Bill", "James"),
				new Contact("Beth", "Parker") };

	}

	@Test
	public final void testContactStringStringStringString() {
		final Contact tom = new Contact("Tom", "Jones", "256-345-1211", "tjones@att.net");
		assertEquals("Tom", tom.getFirstName());
		assertEquals(tom.getEmailAddress(), "tjones@att.net");
		assertEquals(tom.getOrganization(), "");
		assertEquals(_testContacts[1].getLastName(), "Little");

	}

	@Test
	public final void testContactStringString() {
		final Contact jordan = new Contact("Michael", "Jordan");
		assertEquals(_testContacts[2].getLastName(), "James");
		assertEquals(_testContacts[3].getPhoneNumber(), "");
		assertEquals(jordan.getFirstName(), "Michael");
		assertTrue(jordan.getOrganization().equals("Bulls") != true);
		assertTrue(jordan.getOrganization().equals("") == true);
	}

	@Test
	public final void testContactElement() {
		final Contact test = new Contact(_testContacts[1].toElement());
		assertEquals(test.getFirstName(), "Melissa");
		assertEquals(test.getPhoneNumber(), "+1 602-286-3421");
		assertTrue(test.getID() != null);

	}

	@Test
	public final void testGetId() {
		assertTrue(_testContacts[3].getID() != null);
		assertTrue(_testContacts[3].getID().equals("") != true);
		assertTrue(_testContacts[0].getID() != null);
		assertTrue(_testContacts[0].getID().equals("") != true);
	}

	@Test
	public final void testCopy() {
		final Contact test1 = _testContacts[1].copy();
		assertEquals(test1.getFirstName(), _testContacts[1].getFirstName());
		assertEquals(test1.getLastName(), _testContacts[1].getLastName());
		assertEquals(test1.getPhoneNumber(), _testContacts[1].getPhoneNumber());
		assertEquals(test1.getID(), _testContacts[1].getID());

	}

	@Test
	public final void testToElement() {
		final Project project1 = ProjectManager.createProject("project1", new CalendarDate(), new CalendarDate());
		final Project project2 = ProjectManager.createProject("project2", new CalendarDate(), new CalendarDate());
		final Project project3 = ProjectManager.createProject("project3", new CalendarDate(), new CalendarDate());
		_testContacts[0].addProjectID(project1.getID());
		_testContacts[0].addProjectID(project2.getID());
		_testContacts[0].addProjectID(project3.getID());
		final Document doc1 = new Document(_testContacts[0].toElement());
		assertEquals(doc1.getRootElement().getLocalName(), "Contact");
		assertEquals(doc1.getRootElement().getChildCount(), 8);
		assertEquals(doc1.getRootElement().getChild(0).getValue(), "Tom");
		final Elements projects = doc1.getRootElement().getFirstChildElement("projects").getChildElements();
		final Set<String> ids = new HashSet<String>(3);
		ids.add(projects.get(0).getValue());
		ids.add(projects.get(1).getValue());
		ids.add(projects.get(2).getValue());
		assertTrue(ids.contains(project1.getID()));
		assertTrue(ids.contains(project2.getID()));
		assertTrue(ids.contains(project3.getID()));
		ProjectManager.removeProject(project1.getID());
		ProjectManager.removeProject(project2.getID());
		ProjectManager.removeProject(project3.getID());
	}

	@Test
	public final void testAddProject() {
		final Project project1 = ProjectManager.createProject("project1", new CalendarDate(), new CalendarDate());
		final Project project2 = ProjectManager.createProject("project2", new CalendarDate(), new CalendarDate());
		final boolean add1 = _testContacts[0].addProjectID(project1.getID());
		final boolean add2 = _testContacts[0].addProjectID(project2.getID());
		final boolean add3 = _testContacts[0].addProjectID(project2.getID());
		assertTrue(add1);
		assertTrue(add2);
		assertTrue(!add3);
		final Set<String> ids = _testContacts[0].getProjectIDs();
		assertEquals(2, ids.size());
		final Iterator<String> idIterator = ids.iterator();
		int count = 0;
		for (String id; idIterator.hasNext();) {
			id = idIterator.next();
			if (id.equals(project1.getID()) || id.equals(project2.getID())) {
				count++;
			}
		}
		assertEquals(2, count++);
		ProjectManager.removeProject(project1.getID());
		ProjectManager.removeProject(project2.getID());
	}

	@Test
	public final void testRemoveProject() {
		final Project project1 = ProjectManager.createProject("project1", new CalendarDate(), new CalendarDate());
		final Project project2 = ProjectManager.createProject("project2", new CalendarDate(), new CalendarDate());
		final Project project3 = ProjectManager.createProject("project3", new CalendarDate(), new CalendarDate());
		_testContacts[0].addProjectID(project1.getID());
		_testContacts[0].addProjectID(project2.getID());
		final boolean remove1 = _testContacts[0].removeProjectID(project1.getID());
		final boolean remove2 = _testContacts[0].removeProjectID(project3.getID());
		assertTrue(remove1);
		assertTrue(!remove2);
		final Set<String> ids = _testContacts[0].getProjectIDs();
		assertEquals(1, ids.size());
		final Iterator<String> idIterator = ids.iterator();
		for (String id; idIterator.hasNext();) {
			id = idIterator.next();
			if (id.equals(project1.getID())) {
				fail();
			}
		}
		ProjectManager.removeProject(project1.getID());
		ProjectManager.removeProject(project2.getID());
		ProjectManager.removeProject(project3.getID());
	}

	@Test
	public final void testInProject() {
		final Project project1 = ProjectManager.createProject("project1", new CalendarDate(), new CalendarDate());
		final Project project2 = ProjectManager.createProject("project2", new CalendarDate(), new CalendarDate());
		final Project project3 = ProjectManager.createProject("project3", new CalendarDate(), new CalendarDate());
		_testContacts[0].addProjectID(project1.getID());
		_testContacts[0].addProjectID(project2.getID());
		final Set<String> ids = _testContacts[0].getProjectIDs();
		assertTrue(ids.contains(project1.getID()));
		assertTrue(ids.contains(project2.getID()));
		assertTrue(!ids.contains(project3.getID()));
		assertTrue(!ids.contains(null));
		ProjectManager.removeProject(project1.getID());
		ProjectManager.removeProject(project2.getID());
		ProjectManager.removeProject(project3.getID());
	}

	@Test
	public final void testClearProjectIDs() {
		final Project project1 = ProjectManager.createProject("project1", new CalendarDate(), new CalendarDate());
		final Project project2 = ProjectManager.createProject("project2", new CalendarDate(), new CalendarDate());
		_testContacts[0].addProjectID(project1.getID());
		_testContacts[0].addProjectID(project2.getID());
		assertEquals(2, _testContacts[0].getProjectIDs().size());
		_testContacts[0].clearIDs();
		assertEquals(0, _testContacts[0].getProjectIDs().size());
		ProjectManager.removeProject(project1.getID());
		ProjectManager.removeProject(project2.getID());
	}

	@Test
	public final void testAddAllProjectIDs() {
		final Project project1 = ProjectManager.createProject("project1", new CalendarDate(), new CalendarDate());
		final Project project2 = ProjectManager.createProject("project2", new CalendarDate(), new CalendarDate());
		final Project project3 = ProjectManager.createProject("project3", new CalendarDate(), new CalendarDate());
		Set<String> ids = new HashSet<String>();
		ids.add(project1.getID());
		ids.add(project2.getID());
		ids.add(project3.getID());
		_testContacts[0].addAllProjectID(ids);
		ids = _testContacts[0].getProjectIDs();
		assertTrue(ids.contains(project1.getID()));
		assertTrue(ids.contains(project2.getID()));
		assertTrue(ids.contains(project3.getID()));
		assertEquals(3, ids.size());
		ProjectManager.removeProject(project1.getID());
		ProjectManager.removeProject(project2.getID());
		ProjectManager.removeProject(project3.getID());
	}
	
	@Test
	public final void testInvalidFirstName() {
		thrown.expect(IllegalArgumentException.class);
		new Contact(" ", "Smith");
	}

	@Test
	public final void testValidEmail() {
		final String email1 = "TLDemail@tld"; // TLD valid email
		final String email2 = "testemail@testemail.com";
		assertTrue(Contact.isValidEmail(email1));
		assertTrue(Contact.isValidEmail(email2));
		_testContacts[0].setEmailAddress(email1);
		_testContacts[1].setEmailAddress(email2);
		assertEquals(email1, _testContacts[0].getEmailAddress());
		assertEquals(email2, _testContacts[1].getEmailAddress());
	}

	@Test
	public final void testInvalidEmail() {
		thrown.expect(IllegalEmailException.class);
		final String email1 = "bademail@_.com";
		assertTrue(!Contact.isValidEmail(email1)); // Email1 should not be valid
		_testContacts[0].setEmailAddress(email1);
	}

	@Test
	public final void testValidPhone() {
		final String phone1 = "8552785080"; // Arizona State University
		final String phone2 = "+498921800"; // University of Munich
		assertTrue(Contact.isValidPhoneNumber(phone1));
		assertTrue(Contact.isValidPhoneNumber(phone2));
		_testContacts[0].setPhoneNumber(phone1);
		_testContacts[1].setPhoneNumber(phone2);
		assertEquals("+1 855-278-5080", _testContacts[0].getPhoneNumber());
		assertEquals("+49 89 21800", _testContacts[1].getPhoneNumber());
	}

	@Test
	public final void testInvalidPhoneUS() {
		thrown.expect(IllegalPhoneNumberException.class);
		final String phone = "9998765432"; // Invalid area code
		assertTrue(!Contact.isValidPhoneNumber(phone));
		_testContacts[0].setPhoneNumber(phone);
	}

	@Test
	public final void testInvalidPhoneInternational() {
		thrown.expect(IllegalPhoneNumberException.class);
		final String phone = "+44186527000"; // Invalid international number
		assertTrue(!Contact.isValidPhoneNumber(phone));
		_testContacts[0].setPhoneNumber(phone);
	}

	@Test
	public final void testValidParseNumber() {
		final String phone1 = "8552785080"; // Arizona State University
		final String phone2 = "+498921800"; // University of Munich
		assertEquals("+1 855-278-5080", Contact.parsePhoneNumber(phone1));
		assertEquals("+49 89 21800", Contact.parsePhoneNumber(phone2));
	}

	@Test
	public final void testInvalidParseNumber() {
		thrown.expect(IllegalPhoneNumberException.class);
		final String phone = "+44186527000"; // Invalid international number
		Contact.parsePhoneNumber(phone);
	}
}
