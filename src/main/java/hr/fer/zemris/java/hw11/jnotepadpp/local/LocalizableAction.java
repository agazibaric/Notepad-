package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Objects;

import javax.swing.AbstractAction;

import hr.fer.zemris.java.hw11.jnotepadpp.LocalizationKeys;

/**
 * Class represents action that is localizable.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public abstract class LocalizableAction extends AbstractAction {

	/**
	 * Serial number.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Key to which String value is associated.
	 */
	private String key;
	/**
	 * Localization provider.
	 */
	private ILocalizationProvider lp;
	
	/**
	 * Constructor that creates new {@link LocalizableAction}.
	 * 
	 * @param key {@link #key}
	 * @param lp  {@link #lp}
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		this.key = Objects.requireNonNull(key, "Key must not be null");
		this.lp = Objects.requireNonNull(lp, "Localization provider must not be null");
		updateActions();
		addListeners();
	}
	
	/**
	 * Method initializes action.
	 */
	private void updateActions() {
		putValue(NAME, lp.getString(key));
		putValue(SHORT_DESCRIPTION, lp.getString(LocalizationKeys.getDescritpionKey(key)));
	}
	
	/**
	 * Method adds localization provider listener.
	 */
	private void addListeners() {
		lp.addLocalizationListener(() -> {
			updateActions();
		});
	}
	
}
