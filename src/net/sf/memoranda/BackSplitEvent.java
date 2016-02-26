/**
 * File: BackSplitEvent.java
 *
 * Establishes the new start and end dates for the back half of a 
 * split repeatable event. US-102.
 *
 * @author lknaron
 */
package net.sf.memoranda;

import java.util.Date;

import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Util;

/**
 * Establishes the new start and end dates of the latter half of a split 
 * event based on where the split occurs within the series.
 */
public class BackSplitEvent extends SplitEvent {
	
	/**
	 * Constructor for BackSplitEvent. Calls the method to split at the 
	 * appropriate position.
	 *
	 * @param type 		Indicates type of repeating event
	 * @param startDate Original event start date
	 * @param endDate 	Original event end date
	 * @param selected 	The date the user selected on the calendar
	 * @param period	Period of repeating days
	 */
	public BackSplitEvent(int type, CalendarDate startDate, CalendarDate endDate,
					  CalendarDate selected, int period) {

		super(type, startDate, endDate, selected, period);

		if (this.getPosition() == SplitPosition.FIRST_POSITION) {
			this.splitAtFirstPosition();
		}
		else if (this.getPosition() == SplitPosition.MIDDLE_POSITION) {
			this.splitAtMiddlePosition();
		}
		else if (this.getPosition() == SplitPosition.LAST_POSITION) {
			this.splitAtLastPosition();
		}
		
		createNewSchedDate();
	}

	/**
	 * Determines the new start and end dates of the back half of the
	 * split event when the split occurs at the first event of the series.
	 */
	public void splitAtFirstPosition() {
		if (this.getType() == this.YEARLY) {
			this.setNewStartDate(this.incrementDateYear(getSelectedDate()));
		}
		else {
			this.setNewStartDate(this.incrementDateDay(getSelectedDate()));
		}
		
		if (this.getOrigEndDate() == null) {
			this.setNewEndDate(null);
		}
		else  {
			this.setNewEndDate(this.getOrigEndDate());
		}
		
		this.checkForSameDate();
	}

	/**
	 * Determines the new start and end dates of the back half of the
	 * split event when the split occurs at the middle event of the series.
	 */ 
	public void splitAtMiddlePosition() {
		if (this.getType() == this.YEARLY) {
			this.setNewStartDate(this.incrementDateYear(getSelectedDate()));
		}
		else {
			this.setNewStartDate(this.incrementDateDay(getSelectedDate()));
		}
		
		if (this.getOrigEndDate() == null) {
			this.setNewEndDate(null);
		}
		else  {
			this.setNewEndDate(this.getOrigEndDate());
		}
		
		this.checkForSameDate();
	}
	
	/**
	 * Sets the new start and end dates of the back half of the split event
	 * to null when the split occurs at the last event of the series. This
	 * indicates the last instance of the event should be deleted.
	 */ 
	public void splitAtLastPosition() {
		this.setNewStartDate(null);
		this.setNewEndDate(null);
	}
	
	/**
	 * Creates the new scheduled date for an event. Since this is for the back split,
	 * the date is created from the selected date on the calendar.
	 */
	public void createNewSchedDate() {
		if (this.getNewStartDate() != null) {
		Date newSchedDate = CalendarDate.toDate(this.getSelectedDate().getDay(), 
				this.getSelectedDate().getMonth(), this.getSelectedDate().getYear());
		Util.debug(newSchedDate.toString());
		this.setNewSchedDate(newSchedDate);
		}
	}

}