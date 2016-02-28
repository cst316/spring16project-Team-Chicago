package net.sf.memoranda.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.memoranda.FrontSplitEvent;
import net.sf.memoranda.AbstractSplitEvent;
import net.sf.memoranda.date.CalendarDate;

/**
 * Runs tests on the core functions that determine the new dates for split events.
 *
 */
public class SplitEventTest {
	
	AbstractSplitEvent se1;	
	AbstractSplitEvent se2;
	AbstractSplitEvent se3;
	AbstractSplitEvent se4;
	AbstractSplitEvent se5;
	CalendarDate date1;
	CalendarDate date2;
	CalendarDate date3;
	CalendarDate date4;
	int period1 = 1;	// 1 day
	int period2 = 2;	// 2 days
	int period3 = 10;	// 10 days
	int period4 = 100;	// 100 days

	@Before
	public void setUp() throws Exception {
		date1 = new CalendarDate(1,1,2017);		// day/year edge 
		date2 = new CalendarDate(31,10,2017);	// month edge 
		date3 = new CalendarDate(31,12,2018);	// year edge 
		date4 = new CalendarDate(15,3,2018);	
		
		se1 = new FrontSplitEvent(1, date1, date2, date1, period1);	// tests date1
		se2 = new FrontSplitEvent(1, date1, date2, date2, period1);	// tests date2
		se3 = new FrontSplitEvent(1, date1, date2, date3, period2); // tests date3
		se4 = new FrontSplitEvent(1, date1, date2, date4, period3);	// tests date4
		se5 = new FrontSplitEvent(1, date1, date2, date4, period4);	// tests period4
		
	}

	@Test
	public void testIncrementDateDay() {
		assertTrue(new CalendarDate(2,1,2017).equals(se1.incrementDateDay(date1)));
		assertTrue(new CalendarDate(1,11,2017).equals(se2.incrementDateDay(date2)));
		assertEquals(2, se3.incrementDateDay(date3).getDay());
		assertEquals(25, se4.incrementDateDay(date4).getDay());
		assertEquals(6, se5.incrementDateDay(date4).getMonth());		
	}

	@Test
	public void testDecrementDateDay() {
		assertEquals(31, se1.decrementDateDay(date1).getDay());
		assertEquals(5, se5.decrementDateDay(date4).getDay());
	}

	@Test
	public void testIncrementDateYear() {
		assertEquals(2018, se1.incrementDateYear(date1).getYear());
		assertTrue(new CalendarDate(1,1,2018).equals(se1.incrementDateYear(date1)));
	}

	@Test
	public void testDecrementDateYear() {
		assertEquals(2017, se4.decrementDateYear(date4).getYear());
		assertTrue(new CalendarDate(31,12,2017).equals(se1.decrementDateYear(date3)));
	}

}
