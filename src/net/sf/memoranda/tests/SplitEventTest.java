package net.sf.memoranda.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.memoranda.FrontSplitEvent;
import net.sf.memoranda.SplitEvent;
import net.sf.memoranda.date.CalendarDate;

/**
 * Runs tests on the core functions that determine the new dates for split events.
 *
 */
public class SplitEventTest {
	
	SplitEvent se1;	
	SplitEvent se2;
	SplitEvent se3;
	SplitEvent se4;
	SplitEvent se5;
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
		date1 = new CalendarDate(1,1,2017);		// day edge   - decrement(1 day) to previous year
		date2 = new CalendarDate(30,4,2017);	// month edge - increment(1 day) to following month
		date3 = new CalendarDate(30,12,2018);	// year edge  - increment(2 days) to following year
		date4 = new CalendarDate(15,3,2016);	
		
		se1 = new FrontSplitEvent(1, date1, date2, date1, period1);	// tests date1
		se2 = new FrontSplitEvent(1, date1, date2, date2, period1);	// tests date2
		se3 = new FrontSplitEvent(1, date1, date2, date3, period2);   // tests date3
		se4 = new FrontSplitEvent(1, date1, date2, date4, period3);	// tests date4
		se5 = new FrontSplitEvent(1, date1, date2, date4, period4);	// tests period4
		
	}

	@Test
	public void testIncrementDateDay() {
		assertEquals("1/2/17", se1.incrementDateDay(date1).getShortDateString());
		assertEquals("5/1/17", se2.incrementDateDay(date2).getShortDateString());
		assertEquals("1/1/19", se3.incrementDateDay(date3).getShortDateString());
		assertEquals("3/25/16", se4.incrementDateDay(date4).getShortDateString());
		assertEquals("6/23/16", se5.incrementDateDay(date4).getShortDateString());		
	}

	@Test
	public void testDecrementDateDay() {
		assertEquals("12/31/16", se1.decrementDateDay(date1).getShortDateString());
	}

	@Test
	public void testIncrementDateYear() {
		//fail("Not yet implemented");
	}

	@Test
	public void testDecrementDateYear() {
		//fail("Not yet implemented");
	}

}
