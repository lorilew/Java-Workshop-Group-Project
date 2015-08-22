package client.other;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class ViewHelpers {

	/**
	 * Creates a center-aligned button with default settings that will be used throughout the program.
	 */
	
	public static JButton createCenterAlignedMainButton(String buttonSrc, int width, int height) {
		
		JButton button;
		
		ImageIcon icon = new ImageIcon(buttonSrc);
		
		button = new JButton(icon);
		
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBackground(new Color(0f,0f,0f,0f ));
		button.setBorder(null);
		button.setFocusable(false);
		button.setRolloverEnabled(false);
		button.setSelected(false);
		button.setBorder(null);
		button.setBorderPainted(false);
		button.setMargin(new Insets(0,0,0,0));
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
		button.setPreferredSize(new Dimension(width, height));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		return button;
		
	}
	
	/**
	 * Method handles all Interrupted Exceptions.
	 */

	public void handleInterruptedException() {

		//Program should show an error message and quit.

		showModal("Interrupted Exception. Quitting Program.");

		System.exit(1);

	}
	
	/**
	 * Shows a modal window to the user with any kind of notification.
	 */

	public void showModal(String message) {

		JOptionPane.showMessageDialog(null, message);

	}

}
