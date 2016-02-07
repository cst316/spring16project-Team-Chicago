package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import net.sf.memoranda.Contact;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectImpl;
import net.sf.memoranda.ProjectManager;
import net.sf.memoranda.date.CalendarDate;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

public class ContactTest {

	private Contact[] testContacts;
	
	
	
	@Before
	public void setUp() throws Exception {
		this.testContacts=new Contact[]{
				new Contact("Tom","Jones","256-345-1211","tjones@att.net"),
				new Contact("Melissa", "Little","286-3421","hr@acmeco.com"),
				new Contact("Bill", "James"),
				new Contact("Beth","Parker")
		};
		
	}

	@Test
	public final void testContactStringStringStringString() {
		Contact tom = new Contact("Tom","Jones","256-345-1211","tjones@att.net");
		assertEquals("Tom",tom.getFirstName());
		assertEquals(tom.getEmailAddress(),"tjones@att.net");
		assertEquals(tom.getOrganization(),"");
		assertEquals(testContacts[1].getLastName(),"Little");
	    
	}

	@Test
	public final void testContactStringString() {
		Contact jordan = new Contact("Michael","Jordan");
		assertEquals(testContacts[2].getLastName(),"James");
		assertEquals(testContacts[3].getPhoneNumber(),"");
		assertEquals(jordan.getFirstName(),"Michael");
		assertTrue(jordan.getOrganization().equals("Bulls")!=true);
		assertTrue(jordan.getOrganization().equals("")==true);
	}

	@Test
	public final void testContactElement() {
		Contact test = new Contact(testContacts[1].toElement());
		assertEquals(test.getFirstName(),"Melissa");
		assertEquals(test.getPhoneNumber(),"286-3421");
		assertTrue(test.getId() != null);
		
	}

	@Test
	public final void testGetId() {
		assertTrue(testContacts[3].getId() != null);
		assertTrue(testContacts[3].getId().equals("") !=true);
		assertTrue(testContacts[0].getId() != null);
		assertTrue(testContacts[0].getId().equals("") !=true);
	}

	@Test
	public final void testCopy() {
		Contact test1 = testContacts[1].copy();
		assertEquals(test1.getFirstName(),testContacts[1].getFirstName());
		assertEquals(test1.getLastName(),testContacts[1].getLastName());
		assertEquals(test1.getPhoneNumber(),testContacts[1].getPhoneNumber());
		assertEquals(test1.getId(),testContacts[1].getId());

	}

	@Test
	public final void testToElement() {
		Project project1 = ProjectManager.createProject("project1", new CalendarDate(), new CalendarDate());
		Project project2 = ProjectManager.createProject("project2", new CalendarDate(), new CalendarDate());
		Project project3 = ProjectManager.createProject("project3", new CalendarDate(), new CalendarDate());
		testContacts[0].addProject(project1);
		testContacts[0].addProject(project2);
		testContacts[0].addProject(project3);
		Document doc1 = new Document(testContacts[0].toElement());
		assertEquals(doc1.getRootElement().getLocalName(),"Contact");
		assertEquals(doc1.getRootElement().getChildCount(),7);
		assertEquals(doc1.getRootElement().getChild(0).getValue(),"Tom");
		Elements projects = doc1.getRootElement().getFirstChildElement("projects").getChildElements();
		assertEquals(projects.get(0).getValue(), project1.getID());
		assertEquals(projects.get(1).getValue(), project2.getID());
		assertEquals(projects.get(2).getValue(), project3.getID());
		ProjectManager.removeProject(project1.getID());
		ProjectManager.removeProject(project2.getID());
		ProjectManager.removeProject(project3.getID());
	}
	
	@Test
	public final void testAddProject() {
		Project project1 = ProjectManager.createProject("project1", new CalendarDate(), new CalendarDate());
		Project project2 = ProjectManager.createProject("project2", new CalendarDate(), new CalendarDate());
		boolean add1 = testContacts[0].addProject(project1);
		boolean add2 = testContacts[0].addProject(project2);
		boolean add3 = testContacts[0].addProject(project2);
		assertTrue(add1);
		assertTrue(add2);
		assertTrue(!add3);
		String[] ids = testContacts[0].getProjectIDs();
		assertEquals(2, ids.length);
		int count = 0;
		for(int i = 0; i < ids.length; i++) {
			if(ids[i].equals(project1.getID()) ||
				ids[i].equals(project2.getID())) {
					count++;
			}
		}
		assertEquals(2, count++);
		ProjectManager.removeProject(project1.getID());
		ProjectManager.removeProject(project2.getID());
	}
	
	@Test
	public final void testRemoveProject() {
		Project project1 = ProjectManager.createProject("project1", new CalendarDate(), new CalendarDate());
		Project project2 = ProjectManager.createProject("project2", new CalendarDate(), new CalendarDate());
		Project project3 = ProjectManager.createProject("project3", new CalendarDate(), new CalendarDate());
		testContacts[0].addProject(project1);
		testContacts[0].addProject(project2);
		boolean remove1 = testContacts[0].removeProject(project1);
		boolean remove2 = testContacts[0].removeProject(project3);
		assertTrue(remove1);
		assertTrue(!remove2);
		String[] ids = testContacts[0].getProjectIDs();
		assertEquals(1, ids.length);
		for(int i = 0; i < ids.length; i++) {
			if(ids[i].equals(project1.getID())) {
				fail();
			}
		}
		ProjectManager.removeProject(project1.getID());
		ProjectManager.removeProject(project2.getID());
		ProjectManager.removeProject(project3.getID());
	}
	
	@Test
	public final void testInProject() {
		Project project1 = ProjectManager.createProject("project1", new CalendarDate(), new CalendarDate());
		Project project2 = ProjectManager.createProject("project2", new CalendarDate(), new CalendarDate());
		Project project3 = ProjectManager.createProject("project3", new CalendarDate(), new CalendarDate());
		testContacts[0].addProject(project1);
		testContacts[0].addProject(project2);
		assertTrue(testContacts[0].inProject(project1));
		assertTrue(testContacts[0].inProject(project2));
		assertTrue(!testContacts[0].inProject(project3));
		assertTrue(!testContacts[0].inProject(null));
	}
}
