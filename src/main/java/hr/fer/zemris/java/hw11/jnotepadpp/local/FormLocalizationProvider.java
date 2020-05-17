package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Class represents localization provider that extends {@link LocalizationProviderBridge}
 * It is associated to the given {@link JFrame}.
 *
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {
	
	/**
	 * Frame that is associated to this localization provider.
	 */
	private JFrame frame;
	
	/**
	 * Constructor that creates new {@link FormLocalizationProvider} object.
	 * 
	 * @param provider localization provider
	 * @param frame    {@link #frame}
	 */
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		this.frame = frame;
		addListeners();
	}
	
	/**
	 * Method adds window listener to the {@link #frame}.
	 */
	private void addListeners() {
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		});
		
	}
	
}
