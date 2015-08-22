package database;

import java.sql.Timestamp;

public abstract class GameAbstract {
	/**
	 * Updates the game object and the database with the given boardinfo.
	 * @param boardInfo A fen record.
	 */
	public abstract void updateBoard(String boardInfo, DatabaseManager db);
	/**
	 * If a game has ended this method should be called.
	 * The database will be updated with the winner given.
	 * If the game is a draw, winner = null;
	 * @param winner The player object of the winner of the game, or null if draw.
	 */
	public abstract void gameEnded(String winner, DatabaseManager db);
	// Getters
	public abstract int getGameID();
	public abstract String getWhite();
	public abstract String getBlack();
	public abstract Timestamp getStart();
	public abstract Timestamp getEnd();
	public abstract Timestamp getLastActive();
	public abstract String getGameInfo();
	public abstract String getState();
	
	
	
}
