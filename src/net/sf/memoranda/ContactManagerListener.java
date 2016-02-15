/**Listener for the ContactManager class
 * @author Jonathan Hinkle
 *
 */
package net.sf.memoranda;

import java.util.EventListener;

/**Listener for the ContactManager class
 *
 */
public abstract class ContactManagerListener implements EventListener{
	
	/**Called when the ContactManager has changed
	 * 
	 */
	public abstract void contactManagerChanged();
	
}
