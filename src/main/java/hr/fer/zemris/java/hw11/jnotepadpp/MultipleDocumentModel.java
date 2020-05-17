package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * Interface represents general form of manager of multiple documents.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {

	/**
	 * Method creates new empty document.
	 * 
	 * @return created document
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * Method returns current opened document.
	 * 
	 * @return current opened document
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Method loads new document from given {@code path}.
	 * 
	 * @param path path from which document is loaded
	 * @return     loaded document
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Method saves given document {@code model} to the location of {@code newPath}. </br>
	 * If {@code newPath} is {@code null}, {@code model}'s path is used.
	 * 
	 * @param model   document model that is saved
	 * @param newPath path to which document is saved
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Method closes given document {@code model}.
	 * 
	 * @param model document that is closed
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Method adds given {@code MultipleDocumentListener} {@code l} 
	 * to the list of listeners that are notified when changes happened.
	 * 
	 * @param l listeners that is added
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Method removes given {@code MultipleDocumentListener} {@code l} from list of listeners.
	 * 
	 * @param l listener that is removed
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Method returns number of opened documents.
	 * 
	 * @return number of opened documents
	 */
	int getNumberOfDocuments();

	/**
	 * Method returns opened document at given {@code index}.
	 * 
	 * @param index index of document that is returned
	 * @return      document at given {@code index}
	 */
	SingleDocumentModel getDocument(int index);

}
