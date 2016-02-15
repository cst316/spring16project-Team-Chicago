package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import net.sf.memoranda.Contact;

import nu.xom.Document;

public class StickerTextOutput	 {


	
	
	
	@Before
	public void setUp() throws Exception {
		EventsManager.createSticker("Test 1", 0);
		EventsManager.createSticker("Test 2", 1);
		EventsManager.createSticker("Test 3", 2);
		};
		
	}

	@Test
	public final void testContactStringStringStringString() {
		  String test = String getStickerForTxt();
		  System.out.println(test);

}
