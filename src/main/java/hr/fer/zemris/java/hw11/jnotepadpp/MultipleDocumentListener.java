package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Interface represents general form of {@code MultipleDocumentModel}'s listener
 * that gets notified when changes to the documents happened.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public interface MultipleDocumentListener {

	/**
	 * Method that gets called when current documents changes. </br>
	 * {@code previousModel} or {@code currentModel} can be {@code null}, but not both.
	 * 
	 * @param previousModel previous document model
	 * @param currentModel  current document model
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);

	/**
	 * Method that gets called when new document is added.
	 * 
	 * @param model newly added document
	 */
	void documentAdded(SingleDocumentModel model);

	/**
	 * Method that gets called when document is removed.
	 * 
	 * @param model document that is removed
	 */
	void documentRemoved(SingleDocumentModel model);

}
