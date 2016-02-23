/**
 * Exception class for illegal email format strings.
 * @author Jonathan Hinkle
 */

package net.sf.memoranda;

/**
 * This class is thrown when a string is not a valid email format.
 * 
 * @author Jonathan Hinkle
 *
 */
@SuppressWarnings("serial")
public class IllegalEmailException extends IllegalArgumentException {

	/**
	 * IllegalEmailException constructor.
	 */
	public IllegalEmailException() {}

	/**
	 * IllegalEmailException constructor.
	 * 
	 * @param message A message String
	 */
	public IllegalEmailException(String message) {
		super(message);
	}
}