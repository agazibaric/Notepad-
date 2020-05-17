package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.LinkedList;
import java.util.List;

/**
 * Class represents abstract localization provider that implements {@link ILocalizationProvider}.
 * It defines methods for adding and removing {@link ILocalizationListener} objects 
 * and fire method for notifying listeners about localization change.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/**
	 * List of listeners waiting on localization change
	 */
	private List<ILocalizationListener> listeners = new LinkedList<>();
	
	/**
	 * Method adds given listener to the localization change list of listeners.
	 */
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(l);
	}
	
	/**
	 * Method removes given listener from localization change list of listeners.
	 */
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Method notifies listener about localization change.
	 */
	public void fire() {
		listeners.forEach(l -> {
			l.localizationChanged();
		});
	}
	
}
