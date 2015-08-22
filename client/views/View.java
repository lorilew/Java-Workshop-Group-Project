package client.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.Controller;
import client.other.ScreenNames;


/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class View extends JFrame  implements java.util.Observer {


	private static final long serialVersionUID = 1L;

	private Controller controller;
	private ChessBoardScreen chessBoardScreen;
	private JoinGameScreen joinGameScreen;
	MyGamesInProgressScreen myGamesInProgressScreen;
	private JPanel cards;


	public View(Controller controller, JoinGameScreen joinGameScreen, MyGamesInProgressScreen myGamesInProgressScreen, ChessBoardScreen chessBoardScreen) {

		this.controller = controller;

		this.joinGameScreen = joinGameScreen;

		this.myGamesInProgressScreen = myGamesInProgressScreen;
		
		this.chessBoardScreen = chessBoardScreen;

		prepareGUI();


	}

	/**
	 * Initialize the default JFrame using the Card Layout. 
	 */

	private void prepareGUI(){

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
	    addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent event) {
	            controller.exitApplication();
	        }
	    });

		addContentToJFrame();

		//Display the window.

		setTitle("Hong Kong Chess");

		setMinimumSize(new Dimension (766, 577));

		setLocationRelativeTo(null); //Center the frame on the screen;

		setResizable(false);

		pack();

		setVisible(true);

	}

	/**
	 * updates the current screen being displayed.
	 */

	public void update(Observable obs, Object obj) {

		String newScreen = ((String)obj);

		changeScreen(newScreen);

	}

	/**
	 * Adds all the cards (screens) to the frame.
	 * @param pane   The frame to add the cards to.
	 */

	public void addContentToJFrame() { 

		//Creates the panel and add all the views.
		cards = new JPanel(new CardLayout());

		cards.add(new LoginScreen(controller), ScreenNames.LOGINSCREEN.toString()); //This is the default screen.

		cards.add(new MainMenuScreen(controller), ScreenNames.MAINMENUSCREEN.toString());

		cards.add(chessBoardScreen, ScreenNames.CHESSBOARDSCREEN.toString());

		cards.add(joinGameScreen, ScreenNames.JOINGAMESCREEN.toString());

		cards.add(myGamesInProgressScreen, ScreenNames.MYGAMESINPROGRESSSCREN.toString());

		add(cards, BorderLayout.CENTER);

		//CardLayout layout = (CardLayout)(cards.getLayout());
		//layout.show(cards, ScreenNames.CHESSBOARDSCREEN.toString()); //Screen that I'm working on. Remove when finished.

	}

	/**
	 * Method changes current working card, to a new one.
	 * @param newScreen   The String of the screen to change to.
	 */

	public void changeScreen(String newScreen) {

		CardLayout layout = (CardLayout)(cards.getLayout());

		layout.show(cards, newScreen);

	}
	
}