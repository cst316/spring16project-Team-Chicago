package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CalendarDateNextDayTest.class, CalendarDateTest.class, ContactTest.class, EventsManagerTest.class })
public class AllTests {

}
