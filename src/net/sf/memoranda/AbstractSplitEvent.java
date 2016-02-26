package net.sf.memoranda;
/**
 * File: AbstractSplitEvent.java
 *
 * Provides the functionality to split a repeating event into two new events.
 * A split event is divided into a Front event and a Back event, and the 
 * new dates for each are established. US-102.
 * 
 * @author lknaron 
 */

import java.util.Calendar;
import java.util.Date;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Util;

/**
 * The AbstractSplitEvent class provides the functions and abstract methods 
 * used by the concrete classes, FrontSplitEvent and BackSplitEvent, to split a 
 * repeating event into separate dates for new events, in order to isolate the 
 * individual selected event.
 */
public abstract class AbstractSplitEvent {
	
	public enum SplitPosition {
		FIRST_POSITION, MIDDLE_POSITION, LAST_POSITION
	}
	
	static final int YEARLY = 4;
	private final int _type;
	private final CalendarDate _origStartDate;			   
	private final CalendarDate _origEndDate;
	private final CalendarDate _selectedDate;
	private final int _periodOfDays;
	private final SplitPosition _position;
	private boolean _createRepeating;
	private CalendarDate _newStartDate;
	private CalendarDate _newEndDate;
	private Date _newSchedDate;

	/**
	 * Constructor for SplitEvent.
	 *
	 * @param type 		Indicates type of repeating event
	 * @param startDate Original event start date
	 * @param endDate 	Original event end date
	 * @param selected 	The date the user selected on the calendar
	 * @param period	Period of repeating days
	 */
	AbstractSplitEvent(int type, CalendarDate start, CalendarDate end, CalendarDate selected, int period) {
		_type = type;
		_origStartDate = start;
		_origEndDate = end;
		_selectedDate = selected;
		_periodOfDays = period;
		_position = _determineSplitPosition();
		_createRepeating = true;
		_newStartDate = null;
		_newEndDate = null;
		_newSchedDate = null;
	} 
	
	public boolean getCreateRepeating() {
		return this._createRepeating;
	}

	public CalendarDate getNewEndDate() {
		return this._newEndDate;
	}
	
	public CalendarDate getNewStartDate() {
		return this._newStartDate;
	}
	
	public CalendarDate getOrigEndDate() {
		return this._origEndDate;
	}
	
	public CalendarDate getOrigStartDate() {
		return this._origStartDate;
	}

	public SplitPosition getPosition() {
		return this._position;
	}

	public CalendarDate getSelectedDate() {
		return this._selectedDate;
	}
	
	public int getType() {
		return this._type;
	}
	
	public void setNewEndDate(CalendarDate endDate) {
		this._newEndDate = endDate;
	}
	
	protected void setNewSchedDate(Date newDate) {
		this._newSchedDate = newDate;
	}

	public void setNewStartDate(CalendarDate startDate) {
		this._newStartDate = startDate;
	}

	/**
	 * Modifies the date by increasing the day by the specified period.
	 *
	 * @param date 		The date to be incremented
	 * @return upDate 	The new date increased by the period
	 */
	@SuppressWarnings("deprecation")
	public CalendarDate incrementDateDay(CalendarDate date) {
		final Calendar cal = CalendarDate.convertCalendarDateToCalendar(date);
		cal.add(Calendar.DATE, this._periodOfDays);
		final int day = cal.getTime().getDate();
		final int month = cal.getTime().getMonth();
		final int year = cal.getTime().getYear() + 1900;
		final CalendarDate upDate = new CalendarDate(day, month, year);
		return upDate;
	}

	/**
	 * Modifies the date by decreasing the day by the specified period.
	 *
	 * @param date 			The date to be decremented
	 * @return downDate 	The new date decreased by the period
	 */
	@SuppressWarnings("deprecation")
	public CalendarDate decrementDateDay(CalendarDate date) {	
		final Calendar cal = CalendarDate.convertCalendarDateToCalendar(date);
		cal.add(Calendar.DATE, - this._periodOfDays);
		final int day = cal.getTime().getDate();
		final int month = cal.getTime().getMonth();
		final int year = cal.getTime().getYear() + 1900;
		final CalendarDate downDate = new CalendarDate(day, month, year);

		return downDate;
	}

	/**
	 * Modifies the date by increasing the year by one.
	 *
	 * @param date 		The date to be incremented
	 * @return upDate 	The new date increased by one year
	 */
	public CalendarDate incrementDateYear(CalendarDate date) {
		final int upYear = date.getYear() + 1;
		final CalendarDate upDate = new CalendarDate(date.getDay(), date.getMonth(), upYear);
		return upDate;
	}
	
	/**
	 * Modifies the date by decreasing the year by one.
	 *
	 * @param date 			The date to be decremented
	 * @return downDate 	The new date decreased by one year
	 */
	public CalendarDate decrementDateYear(CalendarDate date) {
		final int downYear = date.getYear() - 1;
		final CalendarDate downDate = new CalendarDate(date.getDay(), date.getMonth(), downYear);
		return downDate;
	}
	
	/**
	 * Checks equivalence of the newly created start and end dates. If the
	 * dates are the same, then the new event will be non-repeating.
	 */
	public void checkForSameDate() {		
		if (this.getNewStartDate().equals(getNewEndDate())) {
			_setCreateRepeating(false);
		}
	}
	
	/**
	 * Creates a new event with the new start and end dates based on the position 
	 * of the split, and copies over the unmodified attributes of the original
	 * event.
	 * 
	 * @param ev	The original event 
	 */
	public void newSplitEvent(Event ev) {
		if (!(this.getNewStartDate() == null && this.getNewEndDate() == null)) {
			
			if (this.getCreateRepeating()) {
				EventsManager.createRepeatableEvent(ev.getRepeat(), this.getNewStartDate(), 
						this.getNewEndDate(), ev.getPeriod(), ev.getHour(), ev.getMinute(), 
						ev.getText(), ev.getWorkingDays());
			}
			else {	
				if (/*this.getNewStartDate() == null || */this.getNewStartDate().equals(this.getNewEndDate())) {
					this._createNewSchedDate();
				}
				
				EventsManager.createEvent(this.getNewStartDate(), ev.getHour(), ev.getMinute(),
						ev.getText(), this._newSchedDate);
			}
		}
	}

	/**
	 * Abstract method to split the repeating event at the first position.
	 */
	public abstract void splitAtFirstPosition();

	/**
	 * Abstract method to split the repeating event at the middle position.
	 */
	public abstract void splitAtMiddlePosition();
	
	/**
	 * Abstract method to split the repeating event at the last position.
	 */
	public abstract void splitAtLastPosition();
	
	/**
	 * Creates the new scheduled date for creating a single event.
	 */
	private void _createNewSchedDate() {
		Date newSchedDate = CalendarDate.toDate(this.getNewStartDate().getDay(), 
					this.getNewStartDate().getMonth(), this.getNewStartDate().getYear());

		this.setNewSchedDate(newSchedDate);
	}
	
	/**
	 * Flags if an event should be created as non-repeating or repeating.
	 * 
	 * @param makeSingle	Flags repeating type, whether single or repeating
	 */
	private void _setCreateRepeating(boolean makeSingle) {
		this._createRepeating = makeSingle;
	}
	
	/**
	 * Determines at which position to split the repeating event based on the
	 * original start and end dates.
	 *
	 * @return SplitPosition The enumerator value indicating position
	 */
	private SplitPosition _determineSplitPosition() {		
		if (this._origStartDate.equals(this._selectedDate)) {
			return SplitPosition.FIRST_POSITION;
		}
		else if (this._origEndDate == null) {
			return SplitPosition.MIDDLE_POSITION;
		}
		else if (this._origEndDate.equals(this._selectedDate)) {
			return SplitPosition.LAST_POSITION;
		}
		else {
			return SplitPosition.MIDDLE_POSITION;
		}
	}
}
