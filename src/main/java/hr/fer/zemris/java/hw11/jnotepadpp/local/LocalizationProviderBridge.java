package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Class represents bridge between localization provider and listeners.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * Localization provider.
	 */
	private ILocalizationProvider provider;
	/**
	 * Bridge localization listener
	 */
	private ILocalizationListener bridgeListener;
	/**
	 * Flag that shows is it connected to the provider
	 */
	private boolean isConnected;
	
	/**
	 * Constructor that creates new {@link LocalizationProviderBridge} object.
	 * 
	 * @param provider localization provider
	 */
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		this.provider = provider;
		bridgeListener = () -> fire();
	}
	
	/**
	 * Method disconnects bridge from localization provider.
	 */
	public void disconnect() {
		if (!isConnected)
			return;
		isConnected = true;
		provider.removeLocalizationListener(bridgeListener);
	}
	
	/**
	 * Method connects bridge to the localization provider.
	 */
	public void connect() {
		if (isConnected)
			return;
		isConnected = true;
		provider.addLocalizationListener(bridgeListener);
	}
	
	@Override
	public String getString(String key) {
		return provider.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return provider.getCurrentLanguage();
	}
	
}
