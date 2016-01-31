/**
 * 
 */
package net.sf.memoranda;

import net.sf.memoranda.ui.EventNotificationDialog;

/**
 * OverdueEventNotifier.java creates an event overdue notice popup.
 * @author Larry Naron
 */
public class OverdueEventNotifier extends DefaultEventNotifier {
	
 	/** 
 	 * Constructor for OverdueEventNotifier. 
 	 */ 
 	public OverdueEventNotifier() { 
 		super(); 
 	} 
 
 	/** 
	 * Creates a popup notification stating an event scheduled for the current day is past due.
	 * @param ev
 	 */ 
	public void eventIsOccured(Event ev) {		
 		new EventNotificationDialog( 
 			"Memoranda event", 
 			ev.getTimeString(), 
 			"OVERDUE: " + ev.getText(),
 			ev); 
 	}

}
