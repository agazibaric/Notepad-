package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Interface represents general form of manager of single document.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public interface SingleDocumentModel {

	/**
	 * Method returns {@link JTextArea} of this document.
	 * 
	 * @return {@link JTextArea} of this document
	 */
	JTextArea getTextComponent();

	/**
	 * Method returns path associated to this document.
	 * 
	 * @return path associated to this document
	 */
	Path getFilePath();

	/**
	 * Method sets path of this document to the given {@code path}.
	 * 
	 * @param path new path of this document
	 */
	void setFilePath(Path path);

	/**
	 * Method checks if this document is modified.
	 * 
	 * @return {@code true} if document is modified, otherwise {@code false}
	 */
	boolean isModified();

	/**
	 * Method sets modified status of this document to the given value of {@code modified}.
	 * 
	 * @param modified new modified status of document
	 */
	void setModified(boolean modified);

	/**
	 * Method adds given {@link SingleDocumentListener} to the list of listeners.
	 * 
	 * @param l listener that is added
	 */
	void addSingleDocumentListener(SingleDocumentListener l);

	/**
	 * Method removes given {@link SingleDocumentListener} from list of listeners.
	 * 
	 * @param l listener that is removed
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);

}
