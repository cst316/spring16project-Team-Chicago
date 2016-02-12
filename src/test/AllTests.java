package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ContactListTest.class,
				ContactTest.class, 
				EventsManagerTest.class,
				CalendarDateTest.class})
public class AllTests {

}