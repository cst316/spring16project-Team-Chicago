package net.sf.memoranda.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.memoranda.ui.EventsPanel;

public class EventsPanelTest {
	
	private EventsPanel _ep = new EventsPanel(null);
	
	private String _customDaysText1;
	private String _customDaysText2;
	private String _customDaysText3;
	private String _customDaysText4;
	private String _customDaysText5;
	private String _customDaysText6;
	private String _customDaysText7;
	private String _customDaysText8;
	

	@Before
	public void setUp() throws Exception {
		_customDaysText1 = "0";			
		_customDaysText2 = "100";		
		_customDaysText3 = "1000";		
		_customDaysText4 = "-15";		
		_customDaysText5 = "80p";		
		_customDaysText6 = "90";		
		_customDaysText7 = "abcd";		
		_customDaysText8 = "";			
		
	}

	@Test
	public void testGetDayRange() {
		assertEquals(-1, _ep.getDayRange(_customDaysText1, true));
		assertEquals(100, _ep.getDayRange(_customDaysText2, true));
		assertEquals(-1, _ep.getDayRange(_customDaysText3, true));
		assertEquals(-1, _ep.getDayRange(_customDaysText4, true));
		assertEquals(-1, _ep.getDayRange(_customDaysText5, true));
		assertEquals(90, _ep.getDayRange(_customDaysText6, true));
		assertEquals(-1, _ep.getDayRange(_customDaysText7, true));
		assertEquals(-1, _ep.getDayRange(_customDaysText8, true));
	}

}
