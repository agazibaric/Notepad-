package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class represents singleton localization provider that offers localized names
 * that are associated to the specified key.
 * It offers static method for receiving {@link LocalizationProvider} object.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	/**
	 * Current language.
	 */
	private String language;
	/**
	 * Resource bundle object that provides support for 
	 * getting localized names from given key.
	 */
	private ResourceBundle bundle;
	/**
	 * Default language.
	 */
	private static final String DEFAULT_LANGUAGE = "en";
	/**
	 * Localization provider.
	 */
	private static LocalizationProvider provider;
	/**
	 * Base name of the file that has key=value localized content.
	 */
	private static final String baseName = "hr.fer.zemris.java.hw11.jnotepadpp.local.translations";
	
	/**
	 * Private constructor that creates new {@code LocalizationProvider} object.
	 */
	private LocalizationProvider() {
		language = DEFAULT_LANGUAGE;
		bundle = ResourceBundle.getBundle(baseName, Locale.forLanguageTag(language));
	}
	
	/**
	 * Method that returns instance of {@link LocalizationProvider}.
	 * 
	 * @return instance of {@link LocalizationProvider}
	 */
	public static LocalizationProvider getInstance() {
		if (provider == null) {
			provider = new LocalizationProvider();
		}
		return provider;
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return language;
	}
	
	/**
	 * Method sets current language of provider to the given {@code language}.
	 * 
	 * @param language new language
	 */
	public void setLanguage(String language) {
		this.language = language;
		bundle = ResourceBundle.getBundle(baseName, Locale.forLanguageTag(language));
		this.fire();
	}
	
}
