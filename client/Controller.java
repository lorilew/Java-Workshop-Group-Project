package client;

import util.Cell;
import client.models.ChessBoardModel;
import client.models.ChessBoardScreenModel;
import client.models.JoinGameModel;
import client.models.MyGamesInProgressModel;
import client.models.ScreenModel;
import client.views.View;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class Controller {

	ScreenModel screenModel;
	ChessBoardModel chessBoardModel;
	JoinGameModel joinGameModel;
	MyGamesInProgressModel myGamesInProgressModel;
	ChessBoardScreenModel chessBoardScreenModel;
	View view;
	
	/**
	 * Called when the user logs in.
	 */

	public Controller() {
		

	}
	
	public void login(String username, String password) {
		
		//Create userdata object upon login.
		
		screenModel.handleLogin(username, password);
		
	}
	
	/**
	 * Called when the user creates a game (from the main menu or the join game/games in progress screens)
	 */
	
	public void createGame() {
				
		screenModel.handleCreateGame();
		
		joinGameModel.updateJoinGame();
		
	}
	
	/**
	 * Called when the user clicks on the Join Game button in the main menu.
	 */
	
	public void joinGameButton() {
		
		screenModel.handleJoinGameButton();
		
		joinGameModel.updateJoinGame();
		
	}
	
	/**
	 * Called when the user chooses a game clicks on the "join" button. Can be games in progress or join game.
	 */
	
	public void joinGame(String activeId) {
		
		if (chessBoardModel.handleJoinGame(activeId)) {
			
			screenModel.handleJoinGameScreen();
			
		}
		
	}
	
	public void resumeGame(String activeId) {

		if (chessBoardModel.handleResumeGame(activeId)) {
			
			screenModel.handleJoinGameScreen();
			
			chessBoardScreenModel.handleAddChessBoardTurnIcon(chessBoardModel.getCurrentGamePlayerTurn(), false);
			
		} else {
			
			screenModel.handleJoinGameError();;
			
		}
		
	}
	
	/**
	 * Called when the user clicks on the "My Games in Progress" button.
	 */
	
	public void myGamesInProgress() {
		
		screenModel.handleMyGamesInProgress();
		
		myGamesInProgressModel.updateMyGamesInProgress();
		
	}
	
	/**
	 * Called when user cancels joining a new game or joining a game in progress.
	 */
	
	public void cancel() {
		
		screenModel.handleCancel();
		
	}
	
	/**
	 * Handles making a move from the chess board.
	 * @param from   The non-empty from square.
	 * @param to     The non-empty to square.
	 */
	
	public void makeMove(Cell from, Cell to) {
		
		chessBoardModel.handleMakeMove(from,to);
		chessBoardScreenModel.handleAddChessBoardTurnIcon(chessBoardModel.getCurrentGamePlayerTurn(), chessBoardModel.getFlipStatus());
		
	}
	
	/**
	 * Methods related to the Chess Board game in progress
	 */
	
	/**
	 * Called when the user clicks on the "back" button inside the Chess Board screen.
	 */
	
	public void back() {
		
		chessBoardModel.handleLeaveGame();
		screenModel.handleBack();
		
	}
	
	/**
	 * Chess Board Screen methods below
	 */
	
	public void offerDraw() {
		
		//chessBoardModel.handleOfferDraw();
		
	}
	
	public void resign() {
		
		if (chessBoardModel.handleResign()) {
			
			//If user resigns successfully, take the user to the main menu.
			
			screenModel.handleAfterResign();
			
		}
		
	}
	
	public void exitApplication() {
		
		screenModel.handleExitApplication();
		
	}
	
	/**
	 * Handles flipping the board from the Chess Board Screen
	 */
	
	public void flipBoard() {
		
		chessBoardModel.handleFlipBoard();
		chessBoardScreenModel.handleAddChessBoardTurnIcon(chessBoardModel.getCurrentGamePlayerTurn(), chessBoardModel.getFlipStatus());
		
	}
	
	/**
	 * Methods for adding models and views.
	 * @param m
	 */
	
	public void addScreenModel(ScreenModel c){
		
		this.screenModel = c;
		
	}
	
	public void addChessBoardModel(ChessBoardModel c) {
		
		this.chessBoardModel = c;
		
	}
	
	public void addJoinGameModel(JoinGameModel c) {
		
		this.joinGameModel = c;
		
	}
	
	public void addGamesInProgressModel(MyGamesInProgressModel c) {
		
		this.myGamesInProgressModel = c;
		
	}
	
	public void addChessBoardScreenModel(ChessBoardScreenModel c) {
		
		this.chessBoardScreenModel = c;
		
	}

	public void addView(View v){
		
		this.view = v;
		
	}


}