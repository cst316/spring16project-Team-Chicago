package net.sf.memoranda.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ CalendarDateNextDayTest.class, 
				CalendarDateTest.class, 
				ContactTest.class, 
				EventsManagerTest.class,
				EventsPanelTest.class,
				SplitEventTest.class})
public class AllTests {

}
