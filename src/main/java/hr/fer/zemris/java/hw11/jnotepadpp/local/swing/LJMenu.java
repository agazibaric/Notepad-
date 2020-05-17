package hr.fer.zemris.java.hw11.jnotepadpp.local.swing;

import java.util.Objects;

import javax.swing.JMenu;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * Class represents localizable JMenu.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class LJMenu extends JMenu {

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Key to which localized String value is associated
	 */
	private String key;
	/**
	 * Localization provider
	 */
	private ILocalizationProvider provider;
	
	/**
	 * Constructor that creates new {@link LJMenu} object.
	 * 
	 * @param key      {@link #key}
	 * @param provider {@link #provider}
	 */
	public LJMenu(String key, ILocalizationProvider provider) {
		this.key = Objects.requireNonNull(key, "Key must not be null");
		this.provider = Objects.requireNonNull(provider, "Provider must not be null");
		provider.addLocalizationListener(() -> {
			updateLJMenu();
		});
		updateLJMenu();
	}
	
	/**
	 * Method updates {@code LJMenu}'s name.
	 */
	private void updateLJMenu() {
		this.setText(provider.getString(key));
	}

}
