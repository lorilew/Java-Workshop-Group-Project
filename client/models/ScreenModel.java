package client.models;

import javax.swing.JOptionPane;

import client.User;
import client.other.ScreenNames;
import sockets.ClientSocketManager;
import util.Pswd;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class ScreenModel extends java.util.Observable {	

	ClientSocketManager csm;
	
	User user;

	public ScreenModel(ClientSocketManager csm, User user)	{

		this.csm = csm;
		this.user = user;

	}

	/**
	 * Handles what happens after the user clicks on the login button.
	 */

	public void handleLogin(String username, String password) {

		try {
			
			if (Pswd.isValidPassword(password) && Pswd.isValidUsername(username)) {
				
				String hashedPassword = Pswd.hashPassword(password);
				
				if (csm.login(username, hashedPassword)) {
					
					user.setUsername(username);
					user.setPassword(hashedPassword);
					
					setChanged();
					notifyObservers(ScreenNames.MAINMENUSCREEN.toString());
					
				} else {
					
					showModal("Invalid username or password");
					
				}
				
				
			} else {
				
				showModal("Problem with the password");
				
			}
			
		} catch (InterruptedException e) {

			handleInterruptedException();

		}

	}

	/**
	 * Handles the creation of the game, from the main menu or the join game / my current games screen.
	 */

	public void handleJoinGameScreen() {

		setChanged();

		notifyObservers(ScreenNames.CHESSBOARDSCREEN.toString());

	}

	public void handleCreateGame() {

		try {

			if (csm.createGame().isEmpty()) {

				showModal("There was an error or you have already created a game.");
				
				setChanged();

				notifyObservers(ScreenNames.MAINMENUSCREEN.toString());

			} else {
				
				setChanged();

				notifyObservers(ScreenNames.JOINGAMESCREEN.toString());

			}

		} catch (InterruptedException e) {

			handleInterruptedException();

		}


	}

	/**
	 * Handles the cancel button from the Join Game screen or the Games in Progress screen. Should just send the user back to the main menu.
	 */

	public void handleCancel() {

		setChanged();
		notifyObservers(ScreenNames.MAINMENUSCREEN.toString());

	}

	/**
	 * Handles what happens when the user clicks on the Join Game button from the menu.
	 */

	public void handleJoinGameButton() {

		setChanged();
		notifyObservers(ScreenNames.JOINGAMESCREEN.toString());

	}

	/**
	 * Handles showing the list of games in progress.
	 */

	public void handleMyGamesInProgress() {

		setChanged();
		notifyObservers(ScreenNames.MYGAMESINPROGRESSSCREN.toString());

	}

	/**
	 * Handles what happens when the user clicks on the back button inside the Chess Board screen.
	 */

	public void handleBack() {

		setChanged();
		notifyObservers(ScreenNames.MAINMENUSCREEN.toString());

	}
	
	/**
	 * Handles what happens after the user resigns.
	 */

	public void handleAfterResign() {

		setChanged();
		notifyObservers(ScreenNames.MAINMENUSCREEN.toString());

	}

	/**
	 * Shows a modal window to the user with any kind of notification.
	 */

	public void showModal(String message) {

		JOptionPane.showMessageDialog(null, message);

	}
	
	/**
	 * Handles what occurs when the application is closed.
	 */
	
	public void handleExitApplication() {
		
		csm.disconnect(); //Disconnect from server.
	    System.exit(0);
		
	}
	
	/**
	 * Handles join game error
	 */
	
	public void handleJoinGameError() {
		
		showModal("There was an error joining this game.");
		
	}

	/**
	 * Method handles all Interrupted Exceptions.
	 */

	private void handleInterruptedException() {

		//Program should show an error message and quit.

		showModal("Interrupted Exception. Quitting Program.");

		System.exit(1);

	}

}