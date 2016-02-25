package net.sf.memoranda;

import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Util;

public class BackSplitEvent extends SplitEvent {
	
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
		else {
			Util.debug("Error assigning split position");
		}
	}

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
	
	public void splitAtLastPosition() {
		this.setNewStartDate(null);
		this.setNewEndDate(null);
	}

}