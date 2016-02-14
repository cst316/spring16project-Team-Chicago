package net.sf.memoranda.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.TransformerException;

import biweekly.*;
import biweekly.component.VEvent;
import biweekly.util.ICalDate;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.*;

/**
 * Class contains methods to import an ics file as an event
 * and export project as ics file
 * @author michaelholmes
 *
 */
public class CalendarICS {

	@SuppressWarnings("deprecation")
	/**
	 * Converts .ics file of events to events in Memoranda 
	 * @param f .ics file to convert
	 */
	public static void importCalendar(File f){
		try {
			FileInputStream file = new FileInputStream(f);
			List<ICalendar> icals = Biweekly.parse(file).all();
			int eventCount = icals.get(0).getEvents().size();
			for(int i=0;i<eventCount;i++){
				VEvent event = icals.get(0).getEvents().get(i);
				ICalDate iDate = event.getDateStart().getValue();
				CalendarDate date = new CalendarDate(iDate.getDate(),iDate.getMonth(),iDate.getYear()+1900);
				int hour = iDate.getHours();
				int minute = iDate.getMinutes();
				Event mevent = EventsManager.createEvent(date,hour,minute,event.getSummary().getValue());
				String testDate = date.toString();
				System.out.println(testDate);
				
			} 
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
