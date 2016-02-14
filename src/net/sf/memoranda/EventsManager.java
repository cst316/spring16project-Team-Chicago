/**
 * EventsManager.java Created on 08.03.2003, 12:35:19 Alex Package:
 * net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003
 *         Memoranda Team. http://memoranda.sf.net
 */
package net.sf.memoranda;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JOptionPane;

import java.util.Map;
import java.util.Collections;
import java.util.Date;

import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.ui.DailyItemsPanel;
import net.sf.memoranda.util.CurrentStorage;
import net.sf.memoranda.util.Util;
import nu.xom.Attribute;
//import nu.xom.Comment;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParentNode;

/**
 *  
 */
/*$Id: EventsManager.java,v 1.11 2004/10/06 16:00:11 ivanrise Exp $*/
public class EventsManager {
/*	public static final String NS_JNEVENTS =
		"http://www.openmechanics.org/2003/jnotes-events-file";
*/
	public static final int NO_REPEAT = 0;
	public static final int REPEAT_DAILY = 1;
	public static final int REPEAT_WEEKLY = 2;
	public static final int REPEAT_MONTHLY = 3;
	public static final int REPEAT_YEARLY = 4;

	public static Document _doc = null;
	static Element _root = null;

	static {
		CurrentStorage.get().openEventsManager();
		if (_doc == null) {
			_root = new Element("eventslist");
/*			_root.addNamespaceDeclaration("jnevents", NS_JNEVENTS);
			_root.appendChild(
				new Comment("This is JNotes 2 data file. Do not modify.")); */
			_doc = new Document(_root);
		} else
			_root = _doc.getRootElement();

	}

	public static void createSticker(String text, int prior) {
		Element el = new Element("sticker");
		el.addAttribute(new Attribute("id", Util.generateId()));
		el.addAttribute(new Attribute("priority", prior+""));
		el.appendChild(text);
		_root.appendChild(el);
	}

	@SuppressWarnings("unchecked")
	public static Map getStickers() {
		Map m = new HashMap();
		Elements els = _root.getChildElements("sticker");
		for (int i = 0; i < els.size(); i++) {
			Element se = els.get(i);
			m.put(se.getAttribute("id").getValue(), se);
		}
		return m;
	}

	public static void removeSticker(String stickerId) {
		Elements els = _root.getChildElements("sticker");
		for (int i = 0; i < els.size(); i++) {
			Element se = els.get(i);
			if (se.getAttribute("id").getValue().equals(stickerId)) {
				_root.removeChild(se);
				break;
			}
		}
	}

	public static boolean isNREventsForDate(CalendarDate date) {
		Day d = getDay(date);
		if (d == null)
			return false;
		if (d.getElement().getChildElements("event").size() > 0)
			return true;
		return false;
	}

	public static Collection getEventsForDate(CalendarDate date) {
		Vector v = new Vector();
		Day d = getDay(date);
		if (d != null) {
			Elements els = d.getElement().getChildElements("event");
			for (int i = 0; i < els.size(); i++) {
				v.add(new EventImpl(els.get(i)));
			}
		}
		Collection r = getRepeatableEventsForDate(date);
		if (r.size() > 0)
			v.addAll(r);
		//EventsVectorSorter.sort(v);
		Collections.sort(v);
		return v;
	}
	
	/**
	 * Method: getEventsForWeek()
	 * Inputs: CalendarDate date
	 * Returns: Vector weekEvents
	 * 
	 * Description: Takes the current selected calendar day and makes a collection of all events
	 * of that day and following week. US-53.
	 */
	public static Collection getEventsForWeek(CalendarDate date) {  	
    	int nextDay = 0;
    	Vector weekEvents = new Vector();
    	
    	for (int j = 0; j < 7; j++) { 		
    		Vector events = (Vector) EventsManager.getEventsForDate(date);
    		nextDay++;
    		date = CalendarDate.nextDay(nextDay);
    		weekEvents.addAll(events);
    	}

		return weekEvents;
	}
	
	/**
	 * Method: getEventsForMonth()
	 * Inputs: CalendarDate date
	 * Returns: Vector monthEvents
	 * 
	 * Description: Takes the current selected calendar day and makes a collection of all events
	 * of that day and following 30 days. US-53.
	 */
	public static Collection getEventsForMonth(CalendarDate date) {  	
    	int nextDay = 0;
    	Vector monthEvents = new Vector();
    	
    	for (int j = 0; j < 30; j++) { 			
    		Vector events = (Vector) EventsManager.getEventsForDate(date);
    		nextDay++;
    		date = CalendarDate.nextDay(nextDay);
    		monthEvents.addAll(events);
    	}
    	
		return monthEvents;
	}
	
	public static String getFormattedLocalTime() {
		// sets sysTime date and time values to check		  
		Calendar time = Calendar.getInstance();           
		int timeHour = time.get(Calendar.HOUR_OF_DAY);
		int timeMinute = time.get(Calendar.MINUTE);
		
		// concatenates system hour and minute for comparison with user scheduled event time
		String hour = String.format("%02d", timeHour);
		String minute = String.format("%02d", timeMinute);
		String sysTime = hour + minute;
		
		return sysTime;
	}
	
	/**
	 * Method: checkEventSchedule
	 * Inputs: schedTime - the time trying to be scheduled
	 * @param schedTime
	 * Returns: void
	 * Description: Checks if the non-repeated event being created is scheduled for a time that
	 * has already past.
	 */
	public static void checkEventScheduleTime(String schedTime) {
		Calendar time = Calendar.getInstance(); 
		
		// gets scheduled event date		
		String schedDate = DailyItemsPanel.getCurrentDate();
		schedDate = eventDateConverter(schedDate);
		
		// gets current system time and date
		String sysTime = getFormattedLocalTime();
		String sysDate = new SimpleDateFormat("yyyyMMdd").format(time.getTime());		
		
		// compare dates and times
		if (sysDate.equals(schedDate)) {
			if (sysTime.compareTo(schedTime) > 0) {		
				JOptionPane.showMessageDialog(null, "Time selected for event has already past!", 
									  			    "Notice", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	/**
	 * Method: eventDateConver.java
	 * Input: String toConvert - current date from calendar
	 * Returns: convertedDate - converted date format
	 * @param toConvert
	 * @return convertDate
	 * Description: Custom date formatter. Converts "dd/mm/yyyy" to "yyyyMMdd".
	 */
	public static String eventDateConverter(String toConvert) {
		String convertedDate = "";
		String[] dateElements;
		
		// gets day, month, year from date string
		try {
			dateElements = toConvert.split("/");
		}
		catch (Exception ex) {
			throw ex;
		}
		
		// stores each part of the date
		String day = dateElements[0];
		String month = dateElements[1];
		int correctMonth = Integer.parseInt(month) + 1;
		month = Integer.toString(correctMonth);
		String year = dateElements[2];
		
		// pads days and months with zeroes if needed
		if (day.length() < 2) {
			day = "0" + day;
		}
		if (month.length() < 2) {
			month = "0" + month;
		}
		
		// assembles date parts to new date format
		convertedDate = year + month + day;
		
		return convertedDate;
	}

	/**
	 * Checks if current day events are past their user-set notification date. Called in App.java
	 * at startup. Occurs during app initialization.
	 * @author Larry Naron
	 */
	public static void checkOverdueEvents() {	
		// sets date and time values to check
		CalendarDate date = new CalendarDate();			  
		Calendar time = Calendar .getInstance();           
		int currentHour = time.get(Calendar.HOUR_OF_DAY);
		int currentMinute = time.get(Calendar.MINUTE);
 
		Vector events = new Vector();  // new vector to hold today's events
		events = (Vector) getEventsForDate(date); // store events, cast to vector from collection
		
		// performs check of each event
		for (int j = 0; j < events.size(); j ++) {			
			// checks event hour and minute against current system time
			if (((EventImpl)events.elementAt(j)).getHour() < currentHour ) {
				new OverdueEventNotifier().eventIsOccured(((EventImpl)events.elementAt(j)));
			} 	
			else if (((EventImpl)events.elementAt(j)).getHour() == currentHour &&
					       ((EventImpl)events.elementAt(j)).getMinute() < currentMinute) {
				new OverdueEventNotifier().eventIsOccured(((EventImpl)events.elementAt(j)));
			} 
			
		} // end for	
		
	} // end checkOverdueEvents

	public static Event createEvent(
		CalendarDate date,
		int hh,
		int mm,
		String text,
		Date schedDate) { 
		
		// US-53 gets the scheduled date of the events, converts to string, then adds it
		String dateText;
		DateFormat sdf = new SimpleDateFormat("M/dd/yy");
		dateText = sdf.format(schedDate);
				
		
		Element el = new Element("event");
		el.addAttribute(new Attribute("id", Util.generateId()));
		el.addAttribute(new Attribute("hour", String.valueOf(hh)));
		el.addAttribute(new Attribute("min", String.valueOf(mm)));
		el.appendChild(text);
		el.addAttribute(new Attribute("schedDate", String.valueOf(dateText)));
		Day d = getDay(date);
		if (d == null)
			d = createDay(date);
		d.getElement().appendChild(el);
		return new EventImpl(el);
	}

	public static Event createRepeatableEvent(
		int type,
		CalendarDate startDate,
		CalendarDate endDate,
		int period,
		int hh,
		int mm,
		String text,
		boolean workDays) {
		Element el = new Element("event");
		Element rep = _root.getFirstChildElement("repeatable");
		if (rep == null) {
			rep = new Element("repeatable");
			_root.appendChild(rep);
		}
		
		el.addAttribute(new Attribute("repeat-type", String.valueOf(type)));
		el.addAttribute(new Attribute("id", Util.generateId()));
		el.addAttribute(new Attribute("hour", String.valueOf(hh)));
		el.addAttribute(new Attribute("min", String.valueOf(mm)));
		el.addAttribute(new Attribute("startDate", startDate.toString()));
		if (endDate != null)
			el.addAttribute(new Attribute("endDate", endDate.toString()));
		el.addAttribute(new Attribute("period", String.valueOf(period)));
		// new attribute for wrkin days - ivanrise
		el.addAttribute(new Attribute("workingDays",String.valueOf(workDays)));
		el.appendChild(text);
		rep.appendChild(el);
		return new EventImpl(el);
	}

	public static Collection getRepeatableEvents() {
		Vector v = new Vector();
		Element rep = _root.getFirstChildElement("repeatable");
		if (rep == null)
			return v;
		Elements els = rep.getChildElements("event");
		for (int i = 0; i < els.size(); i++)
			v.add(new EventImpl(els.get(i)));
		return v;
	}

	public static Collection getRepeatableEventsForDate(CalendarDate date) {
		Vector reps = (Vector) getRepeatableEvents();
		Vector v = new Vector();
		for (int i = 0; i < reps.size(); i++) {
			Event ev = (Event) reps.get(i);
			
			// --- ivanrise
			// ignore this event if it's a 'only working days' event and today is weekend.
			if(ev.getWorkingDays() && (date.getCalendar().get(Calendar.DAY_OF_WEEK) == 1 ||
				date.getCalendar().get(Calendar.DAY_OF_WEEK) == 7)) continue;
			// ---
			/*
			 * /if ( ((date.after(ev.getStartDate())) &&
			 * (date.before(ev.getEndDate()))) ||
			 * (date.equals(ev.getStartDate()))
			 */
			//System.out.println(date.inPeriod(ev.getStartDate(),
			// ev.getEndDate()));
			if (date.inPeriod(ev.getStartDate(), ev.getEndDate())) {
				
				ev.setRepSchedDate(date);	// sets the date of the repeating event US-53
				
				if (ev.getRepeat() == REPEAT_DAILY) {
					int n = date.getCalendar().get(Calendar.DAY_OF_YEAR);
					int ns =
						ev.getStartDate().getCalendar().get(
							Calendar.DAY_OF_YEAR);
					//System.out.println((n - ns) % ev.getPeriod());
					if ((n - ns) % ev.getPeriod() == 0)
						v.add(ev);
				} else if (ev.getRepeat() == REPEAT_WEEKLY) {
					if (date.getCalendar().get(Calendar.DAY_OF_WEEK)
						== ev.getPeriod())
						v.add(ev);
				} else if (ev.getRepeat() == REPEAT_MONTHLY) {
					if (date.getCalendar().get(Calendar.DAY_OF_MONTH)
						== ev.getPeriod())
						v.add(ev);
				} else if (ev.getRepeat() == REPEAT_YEARLY) {
					int period = ev.getPeriod();
					//System.out.println(date.getCalendar().get(Calendar.DAY_OF_YEAR));
					if ((date.getYear() % 4) == 0
						&& date.getCalendar().get(Calendar.DAY_OF_YEAR) > 60)
						period++;

					if (date.getCalendar().get(Calendar.DAY_OF_YEAR) == period)
						v.add(ev);
				}
			}
		}
		return v;
	}

	public static Collection getActiveEvents() {
		return getEventsForDate(CalendarDate.today());
	}

	public static Event getEvent(CalendarDate date, int hh, int mm) {
		Day d = getDay(date);
		if (d == null)
			return null;
		Elements els = d.getElement().getChildElements("event");
		for (int i = 0; i < els.size(); i++) {
			Element el = els.get(i);
			if ((new Integer(el.getAttribute("hour").getValue()).intValue()
				== hh)
				&& (new Integer(el.getAttribute("min").getValue()).intValue()
					== mm))
				return new EventImpl(el);
		}
		return null;
	}

	public static void removeEvent(CalendarDate date, int hh, int mm) {
		Day d = getDay(date);
		if (d == null)
			d.getElement().removeChild(getEvent(date, hh, mm).getContent());
	}

	public static void removeEvent(Event ev) {
		ParentNode parent = ev.getContent().getParent();
		parent.removeChild(ev.getContent());
	}

	private static Day createDay(CalendarDate date) {
		Year y = getYear(date.getYear());
		if (y == null)
			y = createYear(date.getYear());
		Month m = y.getMonth(date.getMonth());
		if (m == null)
			m = y.createMonth(date.getMonth());
		Day d = m.getDay(date.getDay());
		if (d == null)
			d = m.createDay(date.getDay());
		return d;
	}

	private static Year createYear(int y) {
		Element el = new Element("year");
		el.addAttribute(new Attribute("year", new Integer(y).toString()));
		_root.appendChild(el);
		return new Year(el);
	}

	private static Year getYear(int y) {
		Elements yrs = _root.getChildElements("year");
		String yy = new Integer(y).toString();
		for (int i = 0; i < yrs.size(); i++)
			if (yrs.get(i).getAttribute("year").getValue().equals(yy))
				return new Year(yrs.get(i));
		//return createYear(y);
		return null;
	}

	private static Day getDay(CalendarDate date) {
		Year y = getYear(date.getYear());
		if (y == null)
			return null;
		Month m = y.getMonth(date.getMonth());
		if (m == null)
			return null;
		return m.getDay(date.getDay());
	}

	static class Year {
		Element yearElement = null;

		public Year(Element el) {
			yearElement = el;
		}

		public int getValue() {
			return new Integer(yearElement.getAttribute("year").getValue())
				.intValue();
		}

		public Month getMonth(int m) {
			Elements ms = yearElement.getChildElements("month");
			String mm = new Integer(m).toString();
			for (int i = 0; i < ms.size(); i++)
				if (ms.get(i).getAttribute("month").getValue().equals(mm))
					return new Month(ms.get(i));
			//return createMonth(m);
			return null;
		}

		private Month createMonth(int m) {
			Element el = new Element("month");
			el.addAttribute(new Attribute("month", new Integer(m).toString()));
			yearElement.appendChild(el);
			return new Month(el);
		}

		public Vector getMonths() {
			Vector v = new Vector();
			Elements ms = yearElement.getChildElements("month");
			for (int i = 0; i < ms.size(); i++)
				v.add(new Month(ms.get(i)));
			return v;
		}

		public Element getElement() {
			return yearElement;
		}

	}

	static class Month {
		Element mElement = null;

		public Month(Element el) {
			mElement = el;
		}

		public int getValue() {
			return new Integer(mElement.getAttribute("month").getValue())
				.intValue();
		}

		public Day getDay(int d) {
			if (mElement == null)
				return null;
			Elements ds = mElement.getChildElements("day");
			String dd = new Integer(d).toString();
			for (int i = 0; i < ds.size(); i++)
				if (ds.get(i).getAttribute("day").getValue().equals(dd))
					return new Day(ds.get(i));
			//return createDay(d);
			return null;
		}

		private Day createDay(int d) {
			Element el = new Element("day");
			el.addAttribute(new Attribute("day", new Integer(d).toString()));
			el.addAttribute(
				new Attribute(
					"date",
					new CalendarDate(
						d,
						getValue(),
						new Integer(
							((Element) mElement.getParent())
								.getAttribute("year")
								.getValue())
							.intValue())
						.toString()));

			mElement.appendChild(el);
			return new Day(el);
		}

		public Vector getDays() {
			if (mElement == null)
				return null;
			Vector v = new Vector();
			Elements ds = mElement.getChildElements("day");
			for (int i = 0; i < ds.size(); i++)
				v.add(new Day(ds.get(i)));
			return v;
		}

		public Element getElement() {
			return mElement;
		}

	}

	static class Day {
		Element dEl = null;

		public Day(Element el) {
			dEl = el;
		}

		public int getValue() {
			return new Integer(dEl.getAttribute("day").getValue()).intValue();
		}

		/*
		 * public Note getNote() { return new NoteImpl(dEl);
		 */

		public Element getElement() {
			return dEl;
		}
	}
/*
	static class EventsVectorSorter {

		private static Vector keys = null;

		private static int toMinutes(Object obj) {
			Event ev = (Event) obj;
			return ev.getHour() * 60 + ev.getMinute();
		}

		private static void doSort(int L, int R) { // Hoar's QuickSort
			int i = L;
			int j = R;
			int x = toMinutes(keys.get((L + R) / 2));
			Object w = null;
			do {
				while (toMinutes(keys.get(i)) < x) {
					i++;
				}
				while (x < toMinutes(keys.get(j))) {
					j--;
				}
				if (i <= j) {
					w = keys.get(i);
					keys.set(i, keys.get(j));
					keys.set(j, w);
					i++;
					j--;
				}
			}
			while (i <= j);
			if (L < j) {
				doSort(L, j);
			}
			if (i < R) {
				doSort(i, R);
			}
		}

		public static void sort(Vector theKeys) {
			if (theKeys == null)
				return;
			if (theKeys.size() <= 0)
				return;
			keys = theKeys;
			doSort(0, keys.size() - 1);
		}

	}
*/
}
