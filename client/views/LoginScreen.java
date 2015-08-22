package client.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


import client.Controller;
import client.other.PasswordFieldWithPrompt;
import client.other.TextFieldWithPrompt;
import client.other.ViewHelpers;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class LoginScreen extends JPanel{

	public static final long serialVersionUID = 1L;

	public LoginScreen(final Controller controller) {

		//Create the "cards".
		JPanel panel = new JPanel();

		JPanel contentArea = new JPanel();

		contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.PAGE_AXIS));

		panel.setSize(766,577);
		
		panel.setLayout(new BorderLayout());

		JLabel container = new JLabel(new ImageIcon("src/res/bg.jpg"));

		panel.add(container);

		JButton loginButton = ViewHelpers.createCenterAlignedMainButton("src/res/buttons/signin.png", 400, 72);

		contentArea.setOpaque(false);

		contentArea.add(Box.createRigidArea(new Dimension(50,150)));

		final TextFieldWithPrompt usernameField = new TextFieldWithPrompt();

		Font bigFont = usernameField.getFont().deriveFont(Font.PLAIN, 40f); //Get the current font to make it larger. Only using it once for both username and password since they're the same.

		usernameField.setFont(bigFont);

		contentArea.add(usernameField);

		contentArea.add(Box.createRigidArea(new Dimension(50,30)));

		final PasswordFieldWithPrompt passwordField = new PasswordFieldWithPrompt();

		passwordField.setFont(bigFont);

		contentArea.add(passwordField);

		contentArea.add(Box.createRigidArea(new Dimension(50,30)));

		contentArea.add(loginButton);	

		contentArea.setPreferredSize(new Dimension(390,400));

		container.add(contentArea);

		loginButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation") //Don't really need to use getPassword
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.login(usernameField.getText(), passwordField.getText());
			}
		});

		container.setLayout(new FlowLayout());

		this.add(container);

	}

}
