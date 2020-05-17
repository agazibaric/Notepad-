package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Interface represents general form of localization provider.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public interface ILocalizationProvider {

	/**
	 * Method adds given {@link ILocalizationListener} to the list of localization change listeners.
	 * 
	 * @param l listener that is added
	 */
	void addLocalizationListener(ILocalizationListener l);
		
	/**
	 * Method removes given {@link ILocalizationListener} from list of localization change listeners.
	 * 
	 * @param l listener that is removed
	 */
	void removeLocalizationListener(ILocalizationListener l);
	
	/**
	 * Method returns String value associated to the given {@code key}.
	 * 
	 * @param key key to which String value is associated
	 * @return    String value associated to the given key
	 */
	String getString(String key);
	
	/**
	 * Method returns current language.
	 * 
	 * @return current language
	 */
	String getCurrentLanguage();
	
}
