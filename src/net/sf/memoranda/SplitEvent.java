package net.sf.memoranda;

import net.sf.memoranda.date.CalendarDate;

public abstract class SplitEvent {
	
	public enum SplitPosition {
		FIRST_POSITION, MIDDLE_POSITION, LAST_POSITION
	};
	
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

	SplitEvent(int type, CalendarDate start, CalendarDate end, CalendarDate selected, int period) {
		_type = type;
		_origStartDate = start;
		_origEndDate = end;
		_selectedDate = selected;
		_periodOfDays = period;
		_position = _determineSplitPosition();
		_newStartDate = null;
		_newEndDate = null;
		_createRepeating = true;
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

	public void setNewStartDate(CalendarDate startDate) {
		this._newStartDate = startDate;
	}

	public CalendarDate incrementDateDay(CalendarDate date) {	
		int upDay = date.getDay() + this._periodOfDays;
		CalendarDate upDate = new CalendarDate(upDay, date.getMonth(), date.getYear());
		return upDate;
	}

	public CalendarDate decrementDateDay(CalendarDate date) {
		int downDay = date.getDay() - this._periodOfDays;
		CalendarDate downDate = new CalendarDate(downDay, date.getMonth(), date.getYear());
		return downDate;
	}

	public CalendarDate incrementDateYear(CalendarDate date) {
		int upYear = date.getYear() + 1;
		CalendarDate upDate = new CalendarDate(date.getDay(), date.getMonth(), upYear);
		return upDate;
	}
	
	public CalendarDate decrementDateYear(CalendarDate date) {
		int downYear = date.getYear() - 1;
		CalendarDate downDate = new CalendarDate(date.getDay(), date.getMonth(), downYear);
		return downDate;
	}
	
	public void checkForSameDate() {		// indicates whether to make a rep or single event
		if (this.getNewStartDate().equals(getNewEndDate())) {
			_setCreateRepeating(false);
		}
	}

	public abstract void splitAtFirstPosition();

	public abstract void splitAtMiddlePosition();

	public abstract void splitAtLastPosition();
	
	private void _setCreateRepeating(boolean makeSingle) {
		this._createRepeating = makeSingle;
	}
	
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
