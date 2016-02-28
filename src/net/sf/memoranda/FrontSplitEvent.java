package net.sf.memoranda;
/**
 * File: FrontSplitEvent.java
 *
 * Establishes the new start and end dates for the front half of a 
 * split repeatable event. US-102.
 *
 * @author lknaron
 */

import java.util.Date;

import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Util;

/**
 * Establishes the new start and end dates of the first half of a split 
 * event based on where the split occurs within the series.
 */
public class FrontSplitEvent extends AbstractSplitEvent {
	
	/**
	 * Constructor for FrontSplitEvent. Calls the method to split at the 
	 * appropriate position.
	 *
	 * @param type 		Indicates type of repeating event
	 * @param startDate Original event start date
	 * @param endDate 	Original event end date
	 * @param selected 	The date the user selected on the calendar
	 * @param period	Period of repeating days
	 */
	public FrontSplitEvent(int type, CalendarDate startDate, CalendarDate endDate,
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
	}

	/**
	 * Sets the new start and end dates of the front half of the split event
	 * to null when the split occurs at the first event of the series. This
	 * indicates the first instance of the event should be deleted.
	 */ 
	public void splitAtFirstPosition() {
		this.setNewStartDate(null);
		this.setNewEndDate(null);
	}

	/**
	 * Determines the new start and end dates of the front half of the
	 * split event when the split occurs at the middle event of the series.
	 */
	public void splitAtMiddlePosition() {
		if (this.getType() == this.YEARLY) {
			this.setNewStartDate(getOrigStartDate());
			this.setNewEndDate(decrementDateYear(this.getSelectedDate()));
		}
		else {
			this.setNewStartDate(getOrigStartDate());
			this.setNewEndDate(decrementDateDay(this.getSelectedDate()));
		}
		
		this.checkForSameDate();
	}
	
	/**
	 * Determines the new start and end dates of the front half of the
	 * split event when the split occurs at the last event of the series.
	 */
	public void splitAtLastPosition() {
		if (this.getType() == this.YEARLY) {
			this.setNewStartDate(getOrigStartDate());
			this.setNewEndDate(this.decrementDateYear(getSelectedDate()));
		}
		else {
			this.setNewStartDate(getOrigStartDate());
			this.setNewEndDate(this.decrementDateDay(getSelectedDate()));
		}
		
		this.checkForSameDate();
	}
}
