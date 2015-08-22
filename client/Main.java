package client;

import sockets.ClientSocketManager;
import client.models.ChessBoardModel;
import client.models.ChessBoardScreenModel;
import client.models.JoinGameModel;
import client.models.MyGamesInProgressModel;
import client.models.ScreenModel;
import client.views.ChessBoard;
import client.views.ChessBoardScreen;
import client.views.JoinGameScreen;
import client.views.MyGamesInProgressScreen;
import client.views.View;

/**
 * 
 * @author Joaquin de la Sierra
 * 
 * Launches the client-side application by creating the models, views and controller.
 *
 */

public class Main {

	public static void main(String[] args) {
		
		boolean production = false;
		
		ClientSocketManager csm;	
		
		csm = new ClientSocketManager(); //Create client socket manager object
		
		if (args == null || args.length < 1) {
			
			csm.connect("localhost", 4444); //Connect to the socket 
			
		} else {
			
			csm.connect(args[0], 4444); //Connect to the socket 
			
		}
		
		if (csm.isConnected() || !production) {
			
			User user = new User(); //Set the user
			
			Controller myController = new Controller(); //Main Controller
				
			ScreenModel screenModel = new ScreenModel(csm, user); //Screen model.
				
			myController.addScreenModel(screenModel);
			
			JoinGameModel joinGameModel = new JoinGameModel(csm); //Join Game Model (for the Join Game Screen).
			
			ChessBoardScreenModel chessBoardScreenModel = new ChessBoardScreenModel();
			
			myController.addChessBoardScreenModel(chessBoardScreenModel);
				
			myController.addJoinGameModel(joinGameModel);
			
			MyGamesInProgressModel myGamesInProgressModel = new MyGamesInProgressModel(csm); //Join Game Model (for the Join Game Screen).
			
			myController.addGamesInProgressModel(myGamesInProgressModel);
				
			ChessBoardModel chessBoardModel = new ChessBoardModel(csm, user); //Model that implements ObservableBoardData
				
			myController.addChessBoardModel(chessBoardModel);
	
			ChessBoard chessBoardView = new ChessBoard(myController, user);
				
			JoinGameScreen joinGameScreen = new JoinGameScreen(myController);
			
			ChessBoardScreen chessBoardScreen = new ChessBoardScreen(myController, chessBoardView);
			
			MyGamesInProgressScreen myGamesInProgressScreen = new MyGamesInProgressScreen(myController);
				
			View myView = new View(myController, joinGameScreen,myGamesInProgressScreen, chessBoardScreen); //Pass the chessBoardView to view so it can be an observer of chessBoardView.
				
			chessBoardModel.addObserver(chessBoardView);
				
			screenModel.addObserver(myView);
				
			joinGameModel.addObserver(joinGameScreen);
			
			myGamesInProgressModel.addObserver(myGamesInProgressScreen);
			
			chessBoardScreenModel.addObserver(chessBoardScreen);
	
			myController.addView(myView);
			
			
		} else {
			
			System.out.println("Connection cannot be established");
			
		}

	}

}