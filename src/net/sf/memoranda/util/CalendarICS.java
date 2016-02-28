package net.sf.memoranda.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import biweekly.*;
import biweekly.component.VEvent;
import biweekly.property.Summary;
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
			final FileInputStream file = new FileInputStream(f);
			final List<ICalendar> icals = Biweekly.parse(file).all();
			final int eventCount = icals.get(0).getEvents().size();
			for(int i=0;i<eventCount;i++){
				VEvent event = icals.get(0).getEvents().get(i);
				ICalDate iDate = event.getDateStart().getValue();
				CalendarDate cdate = new CalendarDate(iDate.getDate(),iDate.getMonth(),iDate.getYear()+YEAR);
				int hour = iDate.getHours();
				int minute = iDate.getMinutes();
				Date date = new Date(iDate.getDate(),iDate.getMonth(),iDate.getYear()+1900);
				EventsManager.createEvent(cdate,hour,minute,event.getSummary().getValue(),date,"");
				CurrentStorage.get().storeEventsManager();
				
			} 
		}
		catch (IOException e) {
			new ExceptionDialog(e, "Failed to read from "+f, "Make sure that this file is a .ics file.");
		}
		
	}
	
	/**
	 * Exports a project's events and tasks to an ics file 
	 * @param proj project to be exported
	 * @param f name of file to save to
	 */
	public static void exportCal(Project proj,File f){
		
		 ICalendar ical = new ICalendar();
		 TaskList tasks = CurrentStorage.get().openTaskList(proj);
		 Vector<Task> vecTasks =  (Vector<Task>) tasks.getTopLevelTasks();
		 for(int i=0;i<vecTasks.size();i++){
			 VEvent event = new VEvent();
			 Summary summary = event.setSummary(vecTasks.get(i).getText());
			 summary.setLanguage("en-us");
			 
			 ICalDate sDate =new ICalDate(_convertDate(vecTasks.get(i).getStartDate()));
			 event.setDateStart(sDate);
			 
			 ICalDate eDate = new ICalDate(_convertDate(vecTasks.get(i).getEndDate()));
			 event.setDateEnd(eDate);
			 ical.addEvent(event);
		 }
		 if (f.getName().indexOf(".ics") ==-1){
			 f = new File(f.getPath()+".ics");
		 }
		 try {
			Biweekly.write(ical).go(f);
		} 
		catch (IOException e) {
			new ExceptionDialog(e, "Failed to write to "+f, "Select valid file");
		}
	}
	
	private static ICalDate _convertDate(CalendarDate d){
		 final ICalDate iDate =new ICalDate();
		 iDate.setDate(d.getDay());
		 iDate.setYear(d.getYear()-YEAR);
		 iDate.setMonth(d.getMonth());
		
		 return iDate;
	}
}
