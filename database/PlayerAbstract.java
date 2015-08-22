package database;

import java.sql.Timestamp;

public abstract class PlayerAbstract {
	/**
	 * This method search database for all user's games and returns
	 * number of wins, losses, draws and total games in a string.
	 * @return A string containing user's stats.
	 */
	public abstract String getStats(DatabaseManager db);
	
	// Getters
	public abstract int getPlayerID();
	public abstract String getUsername();
	public abstract String getPassword();
	public abstract Timestamp getLastActive();
	public abstract Timestamp getRegDate();
}
