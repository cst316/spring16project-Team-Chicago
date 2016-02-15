package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({ ContactTest.class, 
				EventsManagerTest.class,
				CalendarDateNextDayTest.class})

public class AllTests {

}
