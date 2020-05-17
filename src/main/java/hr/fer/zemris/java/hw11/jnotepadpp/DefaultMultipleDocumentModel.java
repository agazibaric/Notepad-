package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Class represents implementation of {@link MultipleDocumentModel} that also extends {@link JTabbedPane}. </br>
 * It is used for managing multiple opened documents.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	/**
	 * List of opened documents
	 */
	private List<SingleDocumentModel> documents;
	/**
	 * Currently shown document
	 */
	private SingleDocumentModel currentDocument;
	/**
	 * Listeners waiting on change of the {@link DefaultMultipleDocumentModel}.
	 */
	private List<MultipleDocumentListener> listeners;
	/**
	 * Name for the empty document
	 */
	private static final String EMPTY_DOC_NAME = "new";
	/**
	 * Icon that is shown when document is not modified
	 */
	private ImageIcon unmodifiedDocumentIcon;
	/**
	 * Icon that is shown when document is modified
	 */
	private ImageIcon modifiedDocumentIcon;
	/**
	 * Size of image icons
	 */
	private static final int IMAGE_SIZE = 16;
	
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 6761883173472735006L;

	/**
	 * Constructor that creates new {@link DefaultMultipleDocumentModel} object.
	 */
	public DefaultMultipleDocumentModel() {
		documents = new ArrayList<>();
		listeners = new LinkedList<>();
		initModel();
	}
	
	/**
	 * Method initializes {@link DefaultMultipleDocumentModel} object.
	 */
	private void initModel() {
		this.addChangeListener(l -> {
			int index = this.getSelectedIndex();
			if (index == -1)
				return;
			currentDocument = documents.get(this.getSelectedIndex());
			fireDocumentChanged(null, currentDocument);
		});
		
		initIcons();
	}
	
	/**
	 * Method initializes icons.
	 */
	private void initIcons() {
		try {
			modifiedDocumentIcon = resizeIcon(getIconForPath("./icons/ModifiedDocumentIcon.png"));
			unmodifiedDocumentIcon = resizeIcon(getIconForPath("./icons/UnmodifiedDocumentIcon.png"));
		} catch (IOException ex) {
			throw new DocumentModelException("Icons can not be loaded.");
		}
	}
	
	/**
	 * Method resizes given {@code imageIcon}
	 * to the size with width and height of {@link #IMAGE_SIZE}.
	 * 
	 * @param imageIcon image icon that is resized
	 * @return          resized image icon
	 */
	private ImageIcon resizeIcon(ImageIcon imageIcon) {
		Image image = imageIcon.getImage(); 
		Image resizedImage = image.getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH); 
		return new ImageIcon(resizedImage);
	}
	
	/**
	 * Method loads image icon from given {@code path} in resources folder.
	 * 
	 * @param path         path from which icon is loaded
	 * @return             image icon got from given path
	 * @throws IOException if reading from given path failed
	 */
	private ImageIcon getIconForPath(String path) throws IOException {
		try (InputStream is = this.getClass().getResourceAsStream(path)) {
			if (is == null)
				throw new DocumentModelException("Can not load image. Path was: " + path);
			byte[] bytes;
			bytes = is.readAllBytes();
			return new ImageIcon(bytes);
		}
	}
	
	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel document = new DefaultSingleDocumentModel(null, "");
		currentDocument = document;
		documents.add(document);
		addNewDocumentTab(null, document.getTextComponent());
		document.addSingleDocumentListener(getSingleDocumentListener());
		fireDocumentAdded(document);
		return document;
	}
	
	/**
	 * Method creates new document tab.
	 * 
	 * @param name     name of document
	 * @param textArea text area associated to the new document
	 */
	private void addNewDocumentTab(Path path, JTextArea textArea) {
		JScrollPane scrollPane = new JScrollPane(textArea);
		String name = path == null ? EMPTY_DOC_NAME : path.getFileName().toString();
		String pathName = path == null ? EMPTY_DOC_NAME : path.toAbsolutePath().toString();
		
		this.add(name, scrollPane);
		int index = getNumberOfDocuments() - 1;
		this.setSelectedIndex(index);
		this.setToolTipTextAt(index, pathName);
		this.setIconAt(index, unmodifiedDocumentIcon);
		textArea.addCaretListener(l -> {
			fireDocumentChanged(null, getCurrentDocument());
		});
	}
	
	/**
	 * Method notifies listeners about newly added document.
	 * 
	 * @param model document that is added
	 */
	private void fireDocumentAdded(SingleDocumentModel model) {
		listeners.forEach(l -> {
			l.documentAdded(model);
		});
	}
	
	/**
	 * Method notifies listeners about removed document.
	 * 
	 * @param model document that is removed
	 */
	private void fireDocumentRemoved(SingleDocumentModel model) {
		listeners.forEach(l -> {
			l.documentRemoved(model);
		});
	}
	
	/**
	 * Method notifies listeners about document change.
	 * 
	 * @param previousModel previous document
	 * @param currentModel  current document
	 */
	private void fireDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		listeners.forEach(l -> {
			l.currentDocumentChanged(previousModel, currentModel);
		});
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path, "Path must not be null");
		
		// Check if there's already document with given path
		for (SingleDocumentModel document : documents) {
			Path documentPath = document.getFilePath();
			if (documentPath == null)
				continue;
			if (document.getFilePath().equals(path)) {
				currentDocument = document;
				return document;
			}
		}
		
		// Add new document
		String textContent;
		try {
			byte[] content = Files.readAllBytes(path);
			textContent = new String(content, "UTF-8");
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Pogreška prilikom učitavanja datoteke " + path,
					"Pogreška", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		SingleDocumentModel document = new DefaultSingleDocumentModel(path, textContent);
		documents.add(document);
		addNewDocumentTab(path, document.getTextComponent());
		currentDocument = document;
		document.addSingleDocumentListener(getSingleDocumentListener());
		fireDocumentAdded(document);
		
		return document;
	}
	
	/**
	 * Method returns new implementation of {@link SingleDocumentListener}.
	 * 
	 * @return {@link SingleDocumentListener} object
	 */
	private SingleDocumentListener getSingleDocumentListener() {
		return new SingleDocumentListener() {
			
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				setModifiedIcon(model);
				fireDocumentChanged(null, model);
			}
			
			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				getSelectedComponent().setName(model.getFilePath().getFileName().toString());
			}
		};
	}
	
	/**
	 * Method sets modified icon to the given {@code model}.
	 * 
	 * @param model document model to which modified icon is set
	 */
	private void setModifiedIcon(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		this.setIconAt(index, modifiedDocumentIcon);
	}
	
	/**
	 * Method sets unmodified icon to the given {@code model}.
	 * 
	 * @param model document model to which unmodified icon is set
	 */
	private void setUnmodified(SingleDocumentModel model) {
		model.setModified(false);
		int index = documents.indexOf(model);
		this.setIconAt(index, unmodifiedDocumentIcon);
	}
	
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		// If newPath = null use model's path
		if (newPath == null) {
			byte[] bytes = model.getTextComponent().getText().getBytes();
			try {
				Files.write(model.getFilePath(), bytes);
				setUnmodified(model);
				return;
			} catch (IOException ex) {
				System.err.println("Greška save document");
				return;
			}
		}
		
		// Check if newPath already exists
		for (SingleDocumentModel document : documents) {
			Path documentPath = document.getFilePath();
			if (document == model || documentPath == null)
				continue;
			if (document.getFilePath().equals(newPath)) {
				throw new DocumentModelException("Specified path is already opened");
			}
		}
			
		// Save document to the newPath
		byte[] bytes = model.getTextComponent().getText().getBytes();
		try {
			Files.write(newPath, bytes);
			currentDocument.setFilePath(newPath);
			this.setTitleAt(this.getSelectedIndex(), newPath.getFileName().toString());
			setUnmodified(model);
		} catch (IOException ex) {
		}
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		documents.remove(model);
		this.remove(index);
		int selectedIndex = getSelectedIndex();
		if (selectedIndex == -1) {
			currentDocument = null;
		} else {
			currentDocument = documents.get(selectedIndex);
		}
		fireDocumentRemoved(model);
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		if (index >= getNumberOfDocuments()) 
			throw new IndexOutOfBoundsException("Was: " + index);
		return documents.get(index);
	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return new DocumentIterator();
	}
	
	/**
	 * Class represents {@code DefaultMultipleDocumentModel}'s implementation of {@link Iterator}.
	 * 
	 * @author Ante Gazibaric
	 * @version 1.0
	 *
	 */
	private class DocumentIterator implements Iterator<SingleDocumentModel> {

		/** current document index */
		private int currentIndex;
		
		@Override
		public boolean hasNext() {
			return currentIndex < getNumberOfDocuments();
		}

		@Override
		public SingleDocumentModel next() {
			if (!hasNext())
				throw new NoSuchElementException("There's no more elements.");
			return documents.get(currentIndex++);
		}		
	}

}
