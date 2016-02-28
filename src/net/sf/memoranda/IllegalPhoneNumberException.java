/**
 * Exception class for illegal phone number format strings.
 * @author Jonathan Hinkle
 */

package net.sf.memoranda;

/**
 * This class is thrown when a string is not a valid phone number format.
 * 
 * @author Jonathan Hinkle
 *
 */
@SuppressWarnings("serial")
public class IllegalPhoneNumberException extends IllegalArgumentException {

	/**
	 * IllegalPhoneNumberException constructor.
	 */
	public IllegalPhoneNumberException() {}

	/**
	 * IllegalPhoneNumberException constructor.
	 * 
	 * @param message A message String
	 */
	public IllegalPhoneNumberException(String message) {
		super(message);
	}
}
