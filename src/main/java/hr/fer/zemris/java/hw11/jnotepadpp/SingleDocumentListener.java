package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Interface represents general form of listener
 * that is notified when change happens in {@link SingleDocumentModel}.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public interface SingleDocumentListener {

	/**
	 * Method that is called when modify status of document is changed.
	 * 
	 * @param model document model that is changed
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);

	/**
	 * Method that is called when path of document is changed.
	 * 
	 * @param model document model whose path is changed
	 */
	void documentFilePathUpdated(SingleDocumentModel model);

}
