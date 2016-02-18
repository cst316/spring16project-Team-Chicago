package net.sf.memoranda.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.memoranda.EventsManager;

public class EventsManagerTest {
	String testDate1,
		   testDate2;

	@Before
	public void setUp() throws Exception {
		// format day/month/year
		testDate1 = "5/1/2016";			// test padding single digits, should convert to 20160205
		testDate2 = "10/11/2017";		// should not pad, should convert to 20171210
	}
	
	@Test
	public void testGetFormattedLocalTime() {
		int size = EventsManager.getFormattedLocalTime().toString().length();
		assertEquals(4, size);	// checks that the time formatting is functioning correctly
	}

	@Test 
	public void testEventDateConverter() {
		assertEquals("20160205", EventsManager.eventDateConverter(testDate1));
		assertEquals("20171210", EventsManager.eventDateConverter(testDate2));	
	}
}
