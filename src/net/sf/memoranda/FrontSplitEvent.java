package net.sf.memoranda;

import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Util;

public class FrontSplitEvent extends SplitEvent {
	
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

	public void splitAtFirstPosition() {
		this.setNewStartDate(null);
		this.setNewEndDate(null);
	}

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
