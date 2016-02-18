package net.sf.memoranda.tests;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import net.sf.memoranda.date.CalendarDate;

/**
 * This class was originally named CalendarDateTest, but an add to our repository for
 * Travis CI added a CalendarDateTest before this was merged into master.
 */
public class CalendarDateNextDayTest {
	
	private Calendar cal;
	private String sysDate;

	@Before
	public void setUp() throws Exception {
		cal = Calendar.getInstance();		
	}

	@Test
	public void testNextDay() {
		
		for (int i = 1; i < 31; i++) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
			sysDate = new SimpleDateFormat("M/d/yy").format(cal.getTime());
			//System.out.println("cal " + CalendarDate.nextDay(i).getShortDateString());
			//System.out.println("sys " + sysDate);
			assertEquals(CalendarDate.nextDay(i).getShortDateString(), sysDate);
		}
	}

}
