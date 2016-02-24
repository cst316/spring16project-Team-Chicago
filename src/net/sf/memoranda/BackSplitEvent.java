package net.sf.memoranda;

import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Util;

public class BackSplitEvent extends SplitEvent {
	
	public BackSplitEvent(int type, CalendarDate startDate, CalendarDate endDate,
					  CalendarDate selected) {

		super(type, startDate, endDate, selected);

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
		//TODO
	}

	public void splitAtMiddlePosition() {
		//TODO
	}
	
	public void splitAtLastPosition() {
		//TODO
	}

}