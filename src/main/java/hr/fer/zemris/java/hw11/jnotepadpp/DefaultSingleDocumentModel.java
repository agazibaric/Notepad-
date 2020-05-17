package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Class represents implementation of {@link SingleDocumentModel}.
 * It is used for managing single opened document.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	/**
	 * Text area associated to this document model
	 */
	private JTextArea textArea;
	/**
	 * File path of document that this model represents
	 */
	private Path filePath;
	/**
	 * Flag that shows is this document modified
	 */
	private boolean isModified;
	/**
	 * List of listeners waiting on change of document
	 */
	private List<SingleDocumentListener> listeners;
	
	/**
	 * Constructor that creates new {@link DefaultSingleDocumentModel} object.
	 * 
	 * @param filePath    {@link #filePath}
	 * @param textContent text content that this document contains
	 */
	public DefaultSingleDocumentModel(Path filePath, String textContent) {
		this.filePath = filePath;
		textArea = new JTextArea(textContent);
		listeners = new ArrayList<>();
		addTextAreaListeners();
	}
	
	/**
	 * Method adds listeners that listens to changes of {@link #textArea}.
	 */
	private void addTextAreaListeners() {
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				modified();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				modified();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				modified();
			}
			
			private void modified() {
				isModified = true;
				fireModified();
			}
		});
	}
	
	/**
	 * Method notifies listeners about modification of the document.
	 */
	private void fireModified() {
		listeners.forEach(l -> {
			l.documentModifyStatusUpdated(this);
		});
	}
	
	/**
	 * Method notifies listeners about document's path change
	 */
	private void firePathChanged() {
		listeners.forEach(l -> {
			l.documentFilePathUpdated(this);
		});
	}

	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		Objects.requireNonNull(path, "Path must not be null");
		this.filePath = path;
		firePathChanged();
	}

	@Override
	public boolean isModified() {
		return isModified;
	}

	@Override
	public void setModified(boolean modified) {
		this.isModified = modified;
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}

}
