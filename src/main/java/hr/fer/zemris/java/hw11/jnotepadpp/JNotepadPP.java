package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.swing.LJMenu;

/**
 * Program JNotepadPP is text editor program that offers basic functionality for text editing. </br>
 * It allows user create new empty document, open existing document, save written document.
 * </p>
 * 
 * Functionalities for text editing that it supports are: </br>
 *    <li>	copy text</li>
 *    <li>	cut text</li>
 *    <li>	paste text</li>
 * </p>
 * 
 * Tools for text manipulation that it supports are: </br>
 *    <li>	To upper case</li>
 *    <li>	To lower case</li>
 *    <li>	Invert case</li>
 *    <li>	Sort ascending</li>
 *    <li>	Sort descending</li>
 *    <li>	Unique lines</li>
 * </p>
 *    
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class JNotepadPP extends JFrame {
	
	/**
	 * Serial number of frame
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Location of the frame
	 */
	private static final Point LOCATION_POINT = new Point(10, 10);
	/**
	 * Frame size
	 */
	private static final Dimension FRAME_SIZE = new Dimension(500, 500);
	/**
	 * Frame's title
	 */
	private static final String FRAME_NAME = "JNotepad++";
	/**
	 * Document model used to manage multiple documents at once
	 */
	private MultipleDocumentModel documentModel;
	/**
	 * Document's panel
	 */
	private JPanel documentPanel;
	/**
	 * Clipboard used for storing values from copy and cut actions
	 */
	private String clipboard = "";
	/**
	 * File menu
	 */
	private LJMenu fileMenu;
	/**
	 * Edit menu
	 */
	private LJMenu editMenu;
	/**
	 * Languages menu
	 */
	private LJMenu langMenu;
	/**
	 * Tools menu
	 */
	private LJMenu toolsMenu;
	/**
	 * Change case menu
	 */
	private LJMenu changeCaseMenu;
	/**
	 * Sort menu
	 */
	private LJMenu sortMenu;
	/**
	 * Status bar that shows info about current document
	 */
	private JLabel statusBar;
	/**
	 * Label that shows length of current document
	 */
	private JLabel lengthLabel;
	/**
	 * Label that shows current line position of caret
	 */
	private JLabel lnLabel;
	/**
	 * Label that shows current column position of caret
	 */
	private JLabel colLabel;
	/**
	 * Label that shows how many characters are selected
	 */
	private JLabel selLabel;
	/**
	 * Label that shows current time
	 */
	private JLabel clockLabel;
	/**
	 * Date format for clock
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	/**
	 * String format for length of document
	 */
	private static final String LENGTH_FORMAT = "length: %d  ";
	/**
	 * String format for current line position of the caret
	 */
	private static final String LN_FORMAT = "Ln: %d  ";
	/**
	 * String format for current column position of the caret
	 */
	private static final String COL_FORMAT = "Col: %d  ";
	/**
	 * String format for number of selected characters
	 */
	private static final String SEL_FORMAT = "Sel: %d  ";
	/**
	 * Name of tab for empty document
	 */
	private static final String EMPTY_DOC_NAME = "new";
	/**
	 * Localization provider for this frame
	 */
	private FormLocalizationProvider formLocProvider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	
	/**
	 * Main method. Accepts no arguments.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadPP().setVisible(true);
		});
	}
	
	/**
	 * Constructor that creates new {@link JNotepadPP} object.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocation(LOCATION_POINT);
		setSize(FRAME_SIZE);
		setTitle(FRAME_NAME);
		initGUI();
	}
	
	/**
	 * Method initializes {@link JNotepadPP} object.
	 */
	private void initGUI() {
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		documentPanel = new JPanel();
		documentPanel.setLayout(new BorderLayout());
		cp.add(documentPanel, BorderLayout.CENTER);
		
		documentModel = new DefaultMultipleDocumentModel();
		documentPanel.add((Component) documentModel, BorderLayout.CENTER);
		
		createActions();
		createMenus();
		createToolBars();
		createStatusBar();
		addListeners();
		
	}
	
	/**
	 * Method adds listeners.
	 */
	private void addListeners() {
		// Window closing listener
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitProgram();
			}
		});
		
		documentModel.addMultipleDocumentListener(new MultipleDocumentListener() {
			@Override
			public void documentRemoved(SingleDocumentModel model) {
				// Not needed
			}
			@Override
			public void documentAdded(SingleDocumentModel model) {
				replaceActions(model.getTextComponent());
			}
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if (currentModel != null) {
					JTextArea textArea = currentModel.getTextComponent();
					updateStatusBar(textArea);
					setEnabledActions(textArea);
					setTitleForModel(currentModel);
				}
			}
		});
	}
	
	/**
	 * Method updates status bar according to given {@code textArea}.
	 * 
	 * @param textArea text area that is used to update the status bar
	 */
	private void updateStatusBar(JTextArea textArea) {
		try {
			Caret caret = textArea.getCaret();
			Document doc = textArea.getDocument();

			int docLength = doc.getLength();
			lengthLabel.setText(String.format(LENGTH_FORMAT, docLength));

			int caretIndex = textArea.getCaretPosition();
			int currentLn = textArea.getLineOfOffset(caretIndex);
			int currentCol = caretIndex - textArea.getLineStartOffset(currentLn);

			lnLabel.setText(String.format(LN_FORMAT, currentLn + 1));
			colLabel.setText(String.format(COL_FORMAT, currentCol + 1));

			int selectedLen = Math.abs(caret.getDot() - caret.getMark());
			selLabel.setText(String.format(SEL_FORMAT, selectedLen));
		} catch (BadLocationException ignorable) {
		}
	}
	
	/**
	 * Method creates actions.
	 */
	private void createActions() {
		
		newDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		
		closeDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		closeDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		
		openExistingDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openExistingDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		
		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		
		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
		
		cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		
		copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		
		pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
		
		statInfoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		statInfoAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		
		enLangAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift E"));
		enLangAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		
		hrLangAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift H"));
		hrLangAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_H);
		
		deLangAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift G"));
		deLangAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);
		
		uppercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift U"));
		uppercaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		uppercaseAction.setEnabled(false);
		
		lowercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift L"));
		lowercaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		lowercaseAction.setEnabled(false);
		
		invertcaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift I"));
		invertcaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		invertcaseAction.setEnabled(false);
		
		ascendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift A"));
		ascendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		ascendingAction.setEnabled(false);
		
		descendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift D"));
		descendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		descendingAction.setEnabled(false);
		
		uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift U"));
		uniqueAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		uniqueAction.setEnabled(false);
		
	}
	
	/**
	 * Method creates menu bar.
	 */
	private void createMenus() {
		
		JMenuBar menuBar = new JMenuBar();
		
		fileMenu = new LJMenu(LocalizationKeys.FILE_KEY, formLocProvider);
		fileMenu.add(newDocumentAction);
		fileMenu.add(openExistingDocumentAction);
		fileMenu.add(closeDocumentAction);
		fileMenu.addSeparator();
		fileMenu.add(saveDocumentAction);
		fileMenu.add(saveAsDocumentAction);
		fileMenu.addSeparator();
		fileMenu.add(statInfoAction);
		fileMenu.addSeparator();
		fileMenu.add(exitAction);
		
		editMenu = new LJMenu(LocalizationKeys.EDIT_KEY, formLocProvider);
		editMenu.add(copyAction);
		editMenu.add(cutAction);
		editMenu.add(pasteAction);
		
		langMenu = new LJMenu(LocalizationKeys.LANG_KEY, formLocProvider);
		langMenu.add(enLangAction);
		langMenu.add(hrLangAction);
		langMenu.add(deLangAction);
		
		toolsMenu = new LJMenu(LocalizationKeys.TOOLS_KEY, formLocProvider);
		changeCaseMenu = new LJMenu(LocalizationKeys.CHANGECASE_KEY, formLocProvider);
		changeCaseMenu.add(uppercaseAction);
		changeCaseMenu.add(lowercaseAction);
		changeCaseMenu.add(invertcaseAction);
		
		sortMenu = new LJMenu(LocalizationKeys.SORT_KEY, formLocProvider);
		sortMenu.add(ascendingAction);
		sortMenu.add(descendingAction);
		sortMenu.add(uniqueAction);
		
		toolsMenu.add(changeCaseMenu);
		toolsMenu.add(sortMenu);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(langMenu);
		menuBar.add(toolsMenu);
		
		this.setJMenuBar(menuBar);
		
	}
	
	/**
	 * Method creates tool bar.
	 */
	private void createToolBars() {
		
		JToolBar toolBar = new JToolBar();
		
		toolBar.add(new JButton(newDocumentAction));
		toolBar.add(new JButton(openExistingDocumentAction));
		toolBar.add(new JButton(closeDocumentAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(saveAsDocumentAction));
		toolBar.addSeparator();
		toolBar.add(copyAction);
		toolBar.add(cutAction);
		toolBar.add(pasteAction);
		
		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
		
	}
	
	
	
	/**
	 * Method creates status bar
	 */
	private void createStatusBar() {
		
		statusBar = new JLabel();
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		statusBar.setPreferredSize(new Dimension(getWidth(), 20));
		
		lengthLabel = new JLabel("length: 0 ");
		lnLabel = new JLabel("Ln: 0 ");
		colLabel = new JLabel("Col: 0 ");
		selLabel = new JLabel("Sel: 0 ");
		clockLabel = new JLabel();
		activateClock();
		
		
		statusBar.add(lengthLabel);
		statusBar.add(Box.createGlue());
		statusBar.add(lnLabel);
		statusBar.add(colLabel);
		statusBar.add(selLabel);
		statusBar.add(Box.createGlue());
		statusBar.add(clockLabel);
		
		statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		documentPanel.add(statusBar, BorderLayout.SOUTH);
		
	}
	
	/**
	 * Method activates clock by starting new thread
	 * which refreshes value every 500 milis.
	 */
	private void activateClock() {
		Thread clockThread = new Thread(() -> {
			while (true) {
				try {
					clockLabel.setText(dateFormat.format(new Date()));
					Thread.sleep(500);
				} catch (InterruptedException ignorable) {
				}
			}
		});
		clockThread.setDaemon(true);
		clockThread.start();
	}
	
	/**
	 * Action creates new empty document.
	 */
	private final Action newDocumentAction = new LocalizableAction(LocalizationKeys.NEW_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			documentModel.createNewDocument();
		}
	};
	
	/**
	 * Action closes current document.
	 */
	private final Action closeDocumentAction = new LocalizableAction(LocalizationKeys.CLOSE_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (documentModel.getNumberOfDocuments() == 0)
				return;
			
			closeDocument(documentModel.getCurrentDocument());
		}
	};
	
	/**
	 * Method closes given {@code document}.
	 * If document is modified, it asks user whether he wants to save it first.
	 * 
	 * @param document document that is closed
	 */
	private void closeDocument(SingleDocumentModel document) {
		if (document.isModified()) {
			String name = getFileName(document);
			int value = JOptionPane.showConfirmDialog(JNotepadPP.this,
					"Document '" + name + "' is not saved!\n" + 
					"Do you want to save it first?", "Warning", 
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (value == JOptionPane.CANCEL_OPTION)
				return;
			if (value == JOptionPane.YES_OPTION) {
				if(!saveDocument(document, document.getFilePath()))
					return;
			}
		}
		documentModel.closeDocument(document);
	}
	
	/**
	 * Action opens existing file from disk. </br>
	 * For file choosing it uses {@link JFileChooser} object.
	 */
	private final Action openExistingDocumentAction = new LocalizableAction(LocalizationKeys.OPEN_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Open document");
			if (chooser.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			File fileName = chooser.getSelectedFile();
			Path filePath = fileName.toPath();
			if (!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(JNotepadPP.this, "File " + fileName.getAbsolutePath() + " does not exit",
						"Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			documentModel.loadDocument(filePath);
		}
	};
	
	/**
	 * Actions save current document.
	 */
	private final Action saveDocumentAction = new LocalizableAction(LocalizationKeys.SAVE_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (documentModel.getNumberOfDocuments() == 0)
				return;
			SingleDocumentModel document = documentModel.getCurrentDocument();
			saveDocument(document, document.getFilePath());
		}
	};
	
	/**
	 * Method runs through all documents, if document is modified 
	 * it saves it using {@link #saveDocument(SingleDocumentModel, Path)},
	 * and then it closes document.
	 */
	private void saveAllAndCloseDocuments() {
		while (documentModel.getNumberOfDocuments() > 0) {
			SingleDocumentModel document = documentModel.getCurrentDocument();
			if (document.isModified()) {
				saveDocument(document, document.getFilePath());
			}
			documentModel.closeDocument(document);
		}
	}
	
	/**
	 * Method saves given {@code document}. 
	 * If given {@code documentPath} is {@code null} it asks user to save it using {@link JFileChooser}.
	 * Otherwise it saves document to the given path.
	 * 
	 * @param document     document that is saved
	 * @param documentPath path that represents location on disk where document is saved
	 * @return             {@code true} if document is successfully saved, otherwise {@code false}
	 */
	private boolean saveDocument(SingleDocumentModel document, Path documentPath) {
		// If path == null let user choose the path
		if (documentPath == null) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save document");

			if (chooser.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepadPP.this, "File is not saved", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
			documentPath = chooser.getSelectedFile().toPath();
		}
		
		try {
			documentModel.saveDocument(document, documentPath);
			return true;
		} catch (DocumentModelException ex) {
			JOptionPane.showMessageDialog(JNotepadPP.this, ex.getMessage(), 
					"ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	/**
	 * Action represents save as action that offers user to choose new path for current document.
	 */
	private final Action saveAsDocumentAction = new LocalizableAction(LocalizationKeys.SAVEAS_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (documentModel.getNumberOfDocuments() == 0)
				return;

			saveDocument(documentModel.getCurrentDocument(), null);
		}
	};
	
	/**
	 * Action exits program.
	 */
	private final Action exitAction = new LocalizableAction(LocalizationKeys.EXIT_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			exitProgram();
		}
	};
	
	/**
	 * Action cuts selected text from current document
	 * and stores it to the {@link #clipboard}.
	 */
	private final Action cutAction = new LocalizableAction(LocalizationKeys.CUT_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel model = documentModel.getCurrentDocument();
			if (model == null)
				return;
			JTextArea textArea = model.getTextComponent();
			Document doc = textArea.getDocument();
			int length = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
			if (length == 0)
				return;
			
			int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
			
			try {
				setClipboard(doc.getText(offset, length));
				doc.remove(offset, length);
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
	};
	
	/**
	 * Action copies selected text from current document
	 * and stores it to the {@link #clipboard}.
	 */
	private final Action copyAction = new LocalizableAction(LocalizationKeys.COPY_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel model = documentModel.getCurrentDocument();
			if (model == null)
				return;
			JTextArea textArea = model.getTextComponent();
			Document doc = textArea.getDocument();
			int length = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
			if (length == 0)
				return;
			
			int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
			
			try {
				setClipboard(doc.getText(offset, length));
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
	};
	
	/**
	 * Action pastes content from {@link #clipboard}
	 * to the current location of the caret in current document.
	 */
	private final Action pasteAction = new LocalizableAction(LocalizationKeys.PASTE_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (isClipboardEmpty())
				return;
			
			SingleDocumentModel model = documentModel.getCurrentDocument();
			if (model == null)
				return;
			
			JTextArea textArea = model.getTextComponent();
			Document doc = textArea.getDocument();
			int offset = textArea.getCaret().getDot();
			
			try {
				doc.insertString(offset, getClipboard(), null);
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
	};
	
	/**
	 * Action calculates statistical info of current document </br>
	 * and shows it to the user using {@link JOptionPane#showMessageDialog(Component, Object, String, int)}.
	 */
	private final Action statInfoAction = new LocalizableAction(LocalizationKeys.STATINFO_KEY, formLocProvider) {

		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (documentModel.getNumberOfDocuments() == 0)
				return;
			
			String statInfo = getStatInfo(documentModel.getCurrentDocument());
			JOptionPane.showMessageDialog(null, statInfo, "Statistical informations",
					JOptionPane.INFORMATION_MESSAGE);
		}
		
	};
	
	/**
	 * Action switches program's language to the Croatian language.
	 */
	private final Action hrLangAction = new LocalizableAction(LocalizationKeys.HRLANG_KEY, formLocProvider) {

		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
		
	};
	
	/**
	 * Action switches program's language to the English language.
	 */
	private final Action enLangAction = new LocalizableAction(LocalizationKeys.ENLANG_KEY, formLocProvider) {

		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
		
	};
	
	/**
	 * Action switches program's language to the German language.
	 */
	private final Action deLangAction = new LocalizableAction(LocalizationKeys.DELANG_KEY, formLocProvider) {

		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
		
	};
	
	/**
	 * Action switches selected text to uppercase letters.
	 */
	private final Action uppercaseAction = new LocalizableAction(LocalizationKeys.UPPERCASE_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel model = documentModel.getCurrentDocument();
			if (model == null)
				return;
			JTextArea textArea = model.getTextComponent();
			Document doc = model.getTextComponent().getDocument();
			int selectedLen = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
			if (selectedLen == 0)
				return;
			
			int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
			try {
				String text = doc.getText(offset, selectedLen).toUpperCase();
				doc.remove(offset, selectedLen);
				doc.insertString(offset, text, null);
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
		
	};
	
	/**
	 * Action switches selected text to lower case letters.
	 */
	private final Action lowercaseAction = new LocalizableAction(LocalizationKeys.LOWERCASE_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel model = documentModel.getCurrentDocument();
			if (model == null)
				return;
			JTextArea textArea = model.getTextComponent();
			Document doc = model.getTextComponent().getDocument();
			int selectedLen = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
			if (selectedLen == 0)
				return;
			
			int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
			try {
				String text = doc.getText(offset, selectedLen).toLowerCase();
				doc.remove(offset, selectedLen);
				doc.insertString(offset, text, null);
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
	};
	
	/**
	 * Action toggles case of letters in selected text.
	 */
	private final Action invertcaseAction = new LocalizableAction(LocalizationKeys.INVERTCASE_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel model = documentModel.getCurrentDocument();
			if (model == null)
				return;
			JTextArea textArea = model.getTextComponent();
			Document doc = model.getTextComponent().getDocument();
			int selectedLen = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
			if (selectedLen == 0)
				return;
			
			int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
			try {
				String text = doc.getText(offset, selectedLen);
				text = invertCase(text);
				doc.remove(offset, selectedLen);
				doc.insertString(offset, text, null);
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}

		/**
		 * Method inverts case for every letter of given {@code text}.
		 * 
		 * @param text text whose casing is inverted
		 * @return     text with inverted casing
		 */
		private String invertCase(String text) {
			char[] chars = text.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
				if (Character.isLowerCase(c)) {
					chars[i] = Character.toUpperCase(c);
				} else {
					chars[i] = Character.toLowerCase(c);
				}
			}
			return new String(chars);
		}
	};
	
	/**
	 * Action sorts selected lines in ascending order.
	 */
	private final Action ascendingAction = new LocalizableAction(LocalizationKeys.ASC_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel model = documentModel.getCurrentDocument();
			if (model == null)
				return;
			sortSelectedLines(model.getTextComponent(), true);
		}
	};
	
	/**
	 * Action sorts selected lines in descending order.
	 */
	private final Action descendingAction = new LocalizableAction(LocalizationKeys.DESC_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel model = documentModel.getCurrentDocument();
			if (model == null)
				return;
			sortSelectedLines(model.getTextComponent(), false);
		}
	};
	
	/**
	 * Action sorts selected lines in descending order.
	 */
	private final Action uniqueAction = new LocalizableAction(LocalizationKeys.UNIQUE_KEY, formLocProvider) {
		
		/**
		 * Serial number
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel model = documentModel.getCurrentDocument();
			if (model == null)
				return;
			removeEqualLines(model.getTextComponent());
		}
	};
	
	/**
	 * Method sorts selected lines from given {@code textArea}.
	 * If {@code isAscending} is {@code true} it sorts lines in ascending order,
	 * otherwise in descending order.
	 * 
	 * @param textArea    text area whose selected lines are sorted
	 * @param isAscending flag that shows will method sort in ascending 
	 * 					  or descending order
	 */
	private void sortSelectedLines(JTextArea textArea, boolean isAscending) {
		try {
			Document document = textArea.getDocument();
			String currentLang = formLocProvider.getCurrentLanguage();
			Locale currentLocale = Locale.forLanguageTag(currentLang);
			Collator collator = Collator.getInstance(currentLocale);
			Caret caret = textArea.getCaret();
			
			int length = Math.abs(textArea.getCaret().getDot() - caret.getMark());
			int offset = Math.min(caret.getDot(), caret.getMark());
			
			offset = textArea.getLineStartOffset(textArea.getLineOfOffset(offset));
			length = textArea.getLineEndOffset(textArea.getLineOfOffset(length + offset));
			String text = document.getText(offset, length - offset);

			List<String> lines = Arrays.asList(text.split("\r\n|\r|\n"));
			Collections.sort(lines, isAscending ? collator : collator.reversed());
			document.remove(offset, length - offset);

			for (String line : lines) {
				document.insertString(offset, line + "\n", null);
				offset += line.length() + 1;
			}
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Method removes equal lines in given {@code textArea}.
	 * 
	 * @param textArea text area whose equal lines are removed
	 */
	private void removeEqualLines(JTextArea textArea) {
		try {
			Document document = textArea.getDocument();
			Caret caret = textArea.getCaret();
			
			int length = Math.abs(textArea.getCaret().getDot() - caret.getMark());
			int offset = Math.min(caret.getDot(), caret.getMark());
			
			offset = textArea.getLineStartOffset(textArea.getLineOfOffset(offset));
			length = textArea.getLineEndOffset(textArea.getLineOfOffset(length + offset));
			String text = document.getText(offset, length - offset);
			
			List<String> lines = Arrays.asList(text.split("\r\n|\r|\n"));
			Set<String> uniqueLines = new LinkedHashSet<>(lines);
			document.remove(offset, length - offset);

			for (String line : uniqueLines) {
				document.insertString(offset, line + "\n", null);
				offset += line.length() + 1;
			}
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Method calculates statistical info of given {@code document} and returns result as String.
	 * That includes number of characters, non-black characters
	 * and number of lines in given document.
	 * 
	 * @param document
	 * @return statistical info of given {@code document}
	 */
	private String getStatInfo(SingleDocumentModel document) {
		JTextArea textArea = document.getTextComponent();
		String documentText = document.getTextComponent().getText();
		String name = getFileName(document);
		
		int numOfChars = documentText.length();
		int numOfNonBlankChars = documentText.replaceAll("\\s+", "").length();
		int numOfLines = textArea.getLineCount();
		return String.format(
				"File: %s%n" +
				"- Number of characters: %d%n" + 
				"- Number of non-blank characters: %d%n" + 
				"- Number of lines: %d",
				name, numOfChars, numOfNonBlankChars, numOfLines);
	}
	
	/**
	 * Method enables or disables actions that work with selected text
	 * according to the given {@code isSelected}.
	 * 
	 * @param isSelected flag that shows if actions should be enabled
	 */
	private void setEnabledActions(JTextArea textArea) {
		Caret caret = textArea.getCaret();
		int selectedLen = Math.abs(caret.getDot() - caret.getMark());
		boolean isEnabled = selectedLen != 0;
		
		uppercaseAction.setEnabled(isEnabled);
		lowercaseAction.setEnabled(isEnabled);
		invertcaseAction.setEnabled(isEnabled);
		ascendingAction.setEnabled(isEnabled);
		descendingAction.setEnabled(isEnabled);
		uniqueAction.setEnabled(isEnabled);
	}
	
	/**
	 * Method replaces copy, paste and cut action 
	 * for given {@code textArea} with notepad's actions.
	 * 
	 * @param textArea text area whose actions are replaced
	 */
	public void replaceActions(JTextArea textArea) {
		ActionMap actionMap = textArea.getActionMap();
		actionMap.put(DefaultEditorKit.copyAction, copyAction);
		actionMap.put(DefaultEditorKit.pasteAction, pasteAction);
		actionMap.put(DefaultEditorKit.cutAction, cutAction);
	}
	
	/**
	 * Method sets title of the frame according to given {@code model}.
	 * 
	 * @param model model used for determining the title of the frame
	 */
	private void setTitleForModel(SingleDocumentModel model) {
		Path path = model.getFilePath();
		String name = path == null ? EMPTY_DOC_NAME : path.toAbsolutePath().toString();
		setTitle(name.concat(" - ").concat(FRAME_NAME));
	}
	
	/**
	 * Method exits program by disposing {@link JNotepadPP} frame. </br>
	 * It also checks if there's unsaved documents and offers user to save those.
	 */
	private void exitProgram() {
		for (SingleDocumentModel document : documentModel) {
			if (document.isModified()) {
				int value = JOptionPane.showConfirmDialog(JNotepadPP.this, 
						"Some documents are not saved!\n" + 
						"Do you want to save them first?", "Warning", 
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (value == JOptionPane.CANCEL_OPTION)
					return;
				if (value == JOptionPane.YES_OPTION) {
					saveAllAndCloseDocuments();
				}
				break;
			}
		}
		dispose();
	}
	
	/**
	 * Method returns documents name which is got from document's path. </br>
	 * If path is {@code null} method returns String '{@code new}'.
	 * 
	 * @param document document whose name is returned
	 * @return         name of document
	 */
	private String getFileName(SingleDocumentModel document) {
		Path path = document.getFilePath();
		return path == null ? EMPTY_DOC_NAME : path.getFileName().toString();
	}
	
	/**
	 * Method sets {@link #clipboard} value to the given {@code text}.
	 * 
	 * @param text new clipboard value
	 */
	private void setClipboard(String text) {
		this.clipboard = text;
	}
	
	/**
	 * Method checks if {@link #clipboard} is {@code null}.
	 * 
	 * @return
	 */
	private boolean isClipboardEmpty() {
		return this.clipboard == null;
	}
	
	/**
	 * Method returns {@link #clipboard} value.
	 * 
	 * @return clipboard value
	 */
	private String getClipboard() {
		return clipboard;
	}

}
