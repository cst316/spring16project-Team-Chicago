package net.sf.memoranda.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.xml.transform.TransformerException;

import biweekly.*;
import biweekly.component.VEvent;
import biweekly.util.ICalDate;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.ui.ExceptionDialog;
import net.sf.memoranda.*;

/**
 * Class contains methods to import an ics file as an event
 * and export project as ics file
 * @author michaelholmes
 *
 */
public class CalendarICS {

	private static final int YEAR =1900;
	
	/**
	 * Converts .ics file of events to events in Memoranda 
	 * @param f .ics file to convert
	 */
	@SuppressWarnings("deprecation")
	public static void importCalendar(File f){
		try {
			FileInputStream file = new FileInputStream(f);
			List<ICalendar> icals = Biweekly.parse(file).all();
			int eventCount = icals.get(0).getEvents().size();
			for(int i=0;i<eventCount;i++){
				VEvent event = icals.get(0).getEvents().get(i);
				ICalDate iDate = event.getDateStart().getValue();
				CalendarDate cdate = new CalendarDate(iDate.getDate(),iDate.getMonth(),iDate.getYear()+YEAR);
				int hour = iDate.getHours();
				int minute = iDate.getMinutes();
				Date date = new Date(iDate.getDate(),iDate.getMonth(),iDate.getYear()+1900);
				Event mevent = EventsManager.createEvent(cdate,hour,minute,event.getSummary().getValue(),date);
				CurrentStorage.get().storeEventsManager();
				
			} 
		}catch (IOException e) {
			new ExceptionDialog(e, "Failed to read from "+f, "Make sure that this file is a .ics file.");
		}
		
	}
	
	
}
