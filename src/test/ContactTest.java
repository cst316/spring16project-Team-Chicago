package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import net.sf.memoranda.Contact;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Document;

public class ContactTest {

	private Contact[] testContacts;
	private Element[] testXML;
	
	
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
		Document doc1 = new Document(testContacts[0].toElement());
		assertEquals(doc1.getRootElement().getLocalName(),"Contact");
		assertEquals(doc1.getRootElement().getChildCount(),6);
		assertEquals(doc1.getRootElement().getChild(0).getValue(),"Tom");
		
		//fail("Not yet implemented"); // TODO
	}

}
