package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Class stores {@link JNotepadPP}'s localizations keys.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class LocalizationKeys {

	/**
	 * Key for new document action.
	 */
	public static final String NEW_KEY = "new";
	/**
	 * Key for open action.
	 */
	public static final String OPEN_KEY = "open";
	/**
	 * Key for save action.
	 */
	public static final String SAVE_KEY = "save";
	/**
	 * Key for save as action.
	 */
	public static final String SAVEAS_KEY = "saveas";
	/**
	 * Key for close action.
	 */
	public static final String CLOSE_KEY = "close";
	/**
	 * Key for exit action.
	 */
	public static final String EXIT_KEY = "exit";
	/**
	 * Key for cut action.
	 */
	public static final String CUT_KEY = "cut";
	/**
	 * Key for copy action.
	 */
	public static final String COPY_KEY = "copy";
	/**
	 * Key for paste action.
	 */
	public static final String PASTE_KEY = "paste";
	/**
	 * Key for statistical info action.
	 */
	public static final String STATINFO_KEY = "statinfo";
	/**
	 * Key for Croatian language change action.
	 */
	public static final String HRLANG_KEY = "hrlang";
	/**
	 * Key for English language change action.
	 */
	public static final String ENLANG_KEY = "enlang";
	/**
	 * Key for German language change action.
	 */
	public static final String DELANG_KEY = "delang";
	/**
	 * Key for file menu.
	 */
	public static final String FILE_KEY = "file";
	/**
	 * Key for edit menu.
	 */
	public static final String EDIT_KEY = "edit";
	/**
	 * Key for language menu.
	 */
	public static final String LANG_KEY = "lang";
	/**
	 * Key for tools menu.
	 */
	public static final String TOOLS_KEY = "tools";
	/**
	 * Key for upper case action.
	 */
	public static final String UPPERCASE_KEY = "uppercase";
	/**
	 * Key for lower case action.
	 */
	public static final String LOWERCASE_KEY = "lowercase";
	/**
	 * Key for invert case action.
	 */
	public static final String INVERTCASE_KEY = "invertcase";
	/**
	 * Key for change case menu.
	 */
	public static final String CHANGECASE_KEY = "changecase";
	/**
	 * Key for sort menu.
	 */
	public static final String SORT_KEY = "sort";
	/**
	 * Key for ascending action.
	 */
	public static final String ASC_KEY = "asc";
	/**
	 * Key for descending action.
	 */
	public static final String DESC_KEY = "desc";
	/**
	 * Key for unique action.
	 */
	public static final String UNIQUE_KEY = "unique";
	/**
	 * Suffix for short description key of all actions.
	 */
	public static final String DESCRIPTION_SUFFIX = ".desc";
	
	/**
	 * Method returns proper short description key
	 * of actions represented by given {@code key}.
	 * 
	 * @param key action's key
	 * @return    short description key of action
	 */
	public static String getDescritpionKey(String key) {
		return key.concat(DESCRIPTION_SUFFIX);
	}
	
}
