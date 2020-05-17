package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Class represents exception happened during invalid situation while managing documents.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class DocumentModelException extends RuntimeException {

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = -8504316820010855232L;

	/**
	 * Constructor that creates new {@link DocumentModelException} object.
	 * 
	 * @param message message that describes invalid situation that happened.
	 */
	public DocumentModelException(String message) {
		super(message);
	}
	
	
}
