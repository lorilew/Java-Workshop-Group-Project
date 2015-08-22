package client.models;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import client.User;
import sockets.ClientSocketManager;
import util.Cell;
import engine.BoardController;
import engine.Game;

/**
 * Handles the chess board and updates it from Game object.
 * @author Joaquin de la Sierra
 * Haven't tested this yet.
 *
 */

public class ChessBoardModel extends Observable implements Observer {

	Game game;
	
	User user;
	
	ClientSocketManager csm;
	
	BoardController boardController;
	
	private boolean flipStatus = false;
	
	public ChessBoardModel(ClientSocketManager csm, User user)	{

		this.csm = csm;
		this.user = user;

	}
	
	/**
	 * Joins the game, sets a controller.
	 */
	
	public boolean handleJoinGame(String gameId) {
	
		try {
			
			if (csm.joinGame(gameId)) {
			
				if (handleResumeGame(gameId)) {
					
					return true;
					
				} else {
					
					return false;
					
				}
				
			} else {
				
				return false;
				
			}
			
		} catch (InterruptedException e) {
			
			handleInterruptedException();
			
			return false;
	
		}
	
	}

	
	public boolean handleResumeGame(String gameId) {
		
		try {

			game = csm.resumeGame(gameId);
		
			if (game != null) {
			
				boardController = new BoardController(game);
				
				game.addObserver(this);
				
				setChanged();
				
				notifyObservers(game);
				
				return true;
			
			} else {
				
				return false;
				
			}
		
		} catch (InterruptedException e) {

			handleInterruptedException();
			
			return false;
			
		}
		
	}
	
	public void handleBoardUpdate() {
		
		setChanged();
		notifyObservers(game);
		
	}
	
	/**
	 * Makes a move on the chessboard.
	 * @param from Non-empty from cell object.
	 * @param to   Non-empty to cell object.
	 */
	
	public void handleMakeMove(Cell from, Cell to) {
		
		if (boardController.isValidMove(from, to)) {
			
			boardController.makeMove(from, to);
			
			setChanged();
            notifyObservers(this.game);
			
			try {
				if (csm.makeMove(from, to)) {
					//Success
					
				} else {
					
					//Failure.
					setChanged();
					notifyObservers(this.game); //Resets Board.
					
				}
			} catch (InterruptedException e) {
				
				handleInterruptedException();
				
			}
			
		}else{
                    setChanged();
                    notifyObservers(this.game);
                }
		
	}	
	
	public void handleFlipBoard() {
		
		flipStatus = !flipStatus;
		setChanged();
		notifyObservers("Flip Board");
		
	}
	
	public boolean handleResign() {
		
		try {
			
			if (csm.abandonGame()) {
				
				showModal("You have resigned");
				
				return true;
				
			} else {
				
				return false;
				
			}
			
		} catch (InterruptedException e) {

			handleInterruptedException();
			
			return false;
			
		}
		
	}
	
	public void handleLeaveGame() {
		
		try {
			
			csm.leaveGame();
			
		} catch (InterruptedException e) {
			
			handleInterruptedException();
			
		}
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		setChanged();
		notifyObservers(this.game);
		
		if (game.isDraw()) {
			
			showModal("You have drawn this game");
			endGame();
			
		}
		
		if (game.isLoss()) {
			
			showModal("You have lost this game");
			endGame();
			
		}
		
		if (game.isWin()) {
			
			showModal("You have won this game");
			endGame();
			
		}
		
	}
	
	private void endGame() {
		
		if (csm.isConnected()) {
			
			try {
				
				csm.leaveGame();
				
			} catch (InterruptedException e) {
				
				handleInterruptedException();
				
			}
			
		}
		 
	}
	
	
	public void showModal(String message) {

		JOptionPane.showMessageDialog(null, message);

	}

	/**
	 * Method handles all Interrupted Exceptions.
	 */

	private void handleInterruptedException() {

		//Program should show an error message and quit.

		showModal("Interrupted Exception. Quitting Program.");

		System.exit(1);

	}
	
	/**
	 * Gets whether the current user's turn is white or not. Should only be called when there's a game already in progress.
	 * @return Boolean, true if it's white's turn, false otherwise.
	 */
	
	public boolean getCurrentGamePlayerTurn() {
		
		return game.isWhiteTurn();
		
	}
	
	public boolean getFlipStatus() {
		
		return flipStatus;
		
	}
	
}
