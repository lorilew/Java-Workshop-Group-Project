package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

/**
 * Creates and manages the database system.
 * @author lxl667
 * @version 2015-02-27
 */
public class DatabaseManager implements DatabaseManagerInterface{
	private Connection connection = null;
	
	private Statement statement = null;
	private ResultSet resultSet = null;
	public static Properties settings = new Properties();
	
	/**
	 * Creates a new DatabaseManager objects and connects the the 
	 * lxl667_hongkongchess database. Checks for ClassNotFoundException and
	 * SQLException. If not connected to the database then connection = null.
	 */
	private void connectDatabase(){
		// Load properties
		try {
		    FileInputStream stream = new FileInputStream(".properties");
		    settings.load(stream);
		}
		catch (FileNotFoundException e) {
		    System.err.println("Please create a .properties file");
		    System.exit(1);
		}
		catch (IOException e) {
		    System.err.println("Could not read the properties file");
		    System.exit(1);
		}
		String driver = settings.getProperty("DBDriver");
		if (driver == null)
			driver = "org.postgresql.Driver";
		String protocol = settings.getProperty("DBProtocol");
		if (protocol == null)
			protocol = "postgresql";
		String server = settings.getProperty("DBServer");
		if (server == null)
			server = "dbteach2.cs.bham.ac.uk";
		String port = settings.getProperty("DBPort");
		if (port == null)
			port = "5432";
		String db = settings.getProperty("DBName");
		if (db == null)
			db = "dbteach2";
		String username = settings.getProperty("DBUser");
		if (username == null)
			username = "lxl667";
		String password = settings.getProperty("DBPassword");
		if (password == null)
			password = "konghong";
		try{
			Class.forName(driver);
			// Try to connect
			connection = DriverManager.getConnection("jdbc:" + protocol + "://" + server + ":" + port + "/" + db,username, password);
			statement = connection.createStatement();
		}catch(ClassNotFoundException e){
			System.out.println("Not connected to database - ClassNotFoundException.");
			e.printStackTrace();
		}catch(SQLException e){
			System.out.println("Not connected to database - SQLException.");
			e.printStackTrace();
		}
	}
	/**
	 * Try to close connection to database. 
	 * If an SQLException is thrown the method catches it and prints to console.
	 */
	private void closeConnection(){
		try {
			if(connection!=null)
				connection.close();      // if connection is not null - close.
	    }catch (SQLException e){
	    	System.out.println("Unable to close connection to database");
	    	e.printStackTrace();
		}
	}
	/**
	 * This method is called when the databaseManager object is destroyed.
	 * It tries to close the connection.
	 */
	public void finalize(){
		this.closeConnection();
        try {
			super.finalize();   // call the super.finalize after connection closed.
		} catch (Throwable e) {
			e.printStackTrace();
		}
     
	}
	/**
	 * Given a Player's username and password, the databaseManager will
	 * query the database for the username. 
	 * If the username does not exist, a new entry will be made with given
	 * username and password. Then returns the info as a Player object.
	 * If username and password do not match the method will return a null.
	 * If username and password do match the method will update the last_active
	 * variable and return the info as a Player object.
	 * Returns: the player's information as a Player object, or null.
	 */
	@Override
	public Player login(String username, String password) {
		Player player = null;
		PreparedStatement selectUsername = null;
		PreparedStatement insertNewUser = null; 
		String selectUsernameString = "SELECT * FROM chess_user WHERE username=?;";
		String selectMaxIDString = "SELECT max(user_id) FROM chess_user;";
		String insertNewUserString = "INSERT INTO chess_user VALUES (? ,? ,? , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); ";
		
		// Query for username.
		try{
			this.connectDatabase();
			if(!(connection==null || connection.isClosed())){
				connection.setAutoCommit(false);
				selectUsername = connection.prepareStatement(selectUsernameString);
				selectUsername.setString(1, username);
				resultSet = selectUsername.executeQuery();
				
				// Check username and password.
				if(!(resultSet.next())){
					// If username does not exist add username and password to database , create a new player object and return it.
					// Make a query.
					resultSet = statement.executeQuery(selectMaxIDString);
					resultSet.next();
					int user_id = resultSet.getInt(1) + 1;
					insertNewUser = connection.prepareStatement(insertNewUserString);
					insertNewUser.setInt(1, user_id);
					insertNewUser.setString(2, username);
					insertNewUser.setString(3, password);
					//System.out.println(insertNewUser.toString());
					insertNewUser.executeUpdate();
					connection.commit();
					return this.login(username, password);
				}else{
					
					if(!(resultSet.getString(3).equals(password))){
						//If username and password do not match database return null.
						System.out.println("PASSWORD INCORRECT");
						
					}else{
						//If username and password match update last_active parameter,create a new player object and return it.
						System.out.println("PASSWORD ACCEPTED");
						player = new Player(resultSet.getInt("user_id"), resultSet.getString("username"), resultSet.getString("password"),
								resultSet.getTimestamp("last_login"), resultSet.getTimestamp("reg_date"));
					}
				}
				connection.setAutoCommit(true);
			}
			this.closeConnection();
		}catch(SQLException e){
			System.out.println("SQLException whilst trying to login.");
			e.printStackTrace();
		}
		return player;
	}
	
	/**
	 * Given a username search the database all the games that the user is either black or white player
	 * and the game in INPROGRESS. 
	 * Add game information to a string in the format game_id#white_username#black_username#last_active#
	 * and add each game info to the string.  
	 * list is order by last_active, in descending order.
	 * Returns: A String containing information about the current games.
	 */
	@Override
	public String getGamesInWaiting(){
		
		String selectGamesInWaiting = "SELECT game_id, white, black, last_active FROM chess_game WHERE state = 'INWAITING' ORDER BY last_active DESC;";
		// Search game table for game_id#user_id/null#user_id/null#last_active
		StringBuffer info = new StringBuffer(); 
		try{
			this.connectDatabase();
			if(this.isConnected()){
				resultSet = statement.executeQuery(selectGamesInWaiting);
				
				while(resultSet.next()){
					info.append(resultSet.getInt("game_id") + "#");
					info.append(resultSet.getString("white") + "#");
					info.append(resultSet.getString("black") + "#");
					info.append(resultSet.getTimestamp("last_active") + "#");
				}
			}
			this.closeConnection();
		}catch(SQLException e){
			System.out.println("SQLException whilst trying to getGamesINWAITING.");
			e.printStackTrace();
		}
		return info.toString();
	}
	/**
	 * When a user wants to create a new game and wait for an opponent.
	 * If the user already has an open game, this replaces this game - only one open game per user.
	 * Creates a new game with state INWAITING, the user is randomly assigned black or white
	 * @param player_id
	 * @return int game_id or 0 if method has failed.
	 */
	@Override
	public int newGame(String username) {
		int game_id=0;
		PreparedStatement addNewGame = null;
		PreparedStatement findUsersGames = null;
		PreparedStatement updateGame = null;
		String selectMaxIDString = "SELECT max(game_id) FROM chess_game";
		String addNewGameString = "INSERT INTO chess_game VALUES (?,?,?,NULL,NULL,CURRENT_TIMESTAMP,NULL,'INWAITING');";
		String findUsersGamesString = "SELECT game_id FROM chess_game WHERE (white = ? OR black = ?) AND state = 'INWAITING';";
		String updateGameString = "UPDATE chess_game SET last_active=CURRENT_TIMESTAMP WHERE game_id=?;";
		try{
			this.connectDatabase();
			if(!(connection==null || connection.isClosed())){
				connection.setAutoCommit(false);
				
				// check if user has a open game already.
				findUsersGames = connection.prepareStatement(findUsersGamesString);
				findUsersGames.setString(1, username);
				findUsersGames.setString(2, username);
				resultSet = findUsersGames.executeQuery();
				if(resultSet.next()){
					System.out.println("Open game already exists for " + username + " - updating last_active");
					//edit current entry
					game_id = resultSet.getInt(1);
					updateGame = connection.prepareStatement(updateGameString);
					updateGame.setInt(1, game_id);
					updateGame.executeUpdate();
				}
				else{
					// Add the new game entry from scratch.
					System.out.println("Adding a new open game for " + username);
					resultSet = statement.executeQuery(selectMaxIDString);
					resultSet.next();
					
					// Randomly assign player to black or white.
					game_id = resultSet.getInt(1) + 1;
					int rand =(new Random()).nextInt(2);
					addNewGame = connection.prepareStatement(addNewGameString);
					addNewGame.setInt(1, game_id);
					if(rand<1){
						addNewGame.setString(2, username);
						addNewGame.setNull(3, 12);
					}else{
						addNewGame.setNull(2, 12);
						addNewGame.setString(3, username);
					}
					addNewGame.executeUpdate();
				}
				connection.commit();
				connection.setAutoCommit(true);
				this.closeConnection();
			}
		}catch(SQLException e){
			System.out.println("SQLException whilst trying to login.");
			e.printStackTrace();
		}
		return game_id;
	}
	/**
	 * Given a username and password, this method checks that the game_id is available for
	 * username. If available, it will determine whether the new player is black or white and
	 * add their username to the chess_game table. It will update the the game_start and last_active attributes.
	 * If the game is unavailable for given user and game_id this method returns null.
	 * Returns: A Game object containing all the information for the updated game with game_id given.
	 */
	@Override
	public Game joinGame(String username, int game_id) {
		Game game = null;
		PreparedStatement getJoinGame = null;
		PreparedStatement addWhite = null;
		PreparedStatement addBlack = null;
		String getJoinGameString = "SELECT * FROM chess_game WHERE game_id=?  AND state = 'INWAITING' ;";
		String addWhiteString ="UPDATE chess_game SET white=?, state = 'INPROGRESS', game_start=CURRENT_TIMESTAMP  WHERE game_id=?;";
		String addBlackString ="UPDATE chess_game SET black=?, state = 'INPROGRESS', game_start=CURRENT_TIMESTAMP  WHERE game_id=?;";
		
		this.connectDatabase();
		if(this.isConnected()){
			try{
				connection.setAutoCommit(false);
				getJoinGame = connection.prepareStatement(getJoinGameString);
				getJoinGame.setInt(1, game_id);
				
				
				// Get the game info for the game the player wants to join
				resultSet = getJoinGame.executeQuery();
				
				// If game exists continue, add player to game and start game.
				if(resultSet.next()){
					// Check if the new player is white or black.
					if(resultSet.getString("white")==null && !(resultSet.getString("black").equals(username))){
						addWhite = connection.prepareStatement(addWhiteString);
						addWhite.setString(1, username);
						addWhite.setInt(2, game_id);
						addWhite.executeUpdate();
						connection.commit();
						game = this.getGameInfo(game_id);
					}else if(resultSet.getString("black")==null && !(resultSet.getString("white").equals(username))){
						addBlack = connection.prepareStatement(addBlackString);
						addBlack.setString(1, username);
						addBlack.setInt(2, game_id);
						addBlack.executeUpdate();
						connection.commit();
						game = this.getGameInfo(game_id);
					}
				}else{
					System.out.println("Game unavailable for game_id:" + game_id + " username: "+ username);
				}
				connection.setAutoCommit(true);
				this.closeConnection();
			}catch(SQLException e){
				System.out.println("SQLException whilst trying to login.");
				e.printStackTrace();
			}
		}
		return game;
	}
	/**
	 * Searches the database for all games that are INWAITING and only have one player. 
	 * The String contains user_id#white_username#black_username#last_active# for each game.
	 * Example: 3#null#bodum#2015-03-05 14:33:14.284556#
	 * List is ordered last_active in descending order.
	 * Returns: an String of all games in waiting info. 
	 */
	@Override
	public String getCurrentGames(String username) {
		PreparedStatement selectGamesInProgress= null;
		String selectGamesInProgressString = "SELECT game_id, white, black, last_active FROM chess_game WHERE state='INPROGRESS' AND (white=? OR black=?) ORDER BY last_active DESC";
		// Search game table for game_id#user_id/null#user_id/null#last_active
		StringBuffer info = new StringBuffer();
		try{
			this.connectDatabase();
			if(this.isConnected()){
				selectGamesInProgress = connection.prepareStatement(selectGamesInProgressString);
				selectGamesInProgress.setString(1, username);
				selectGamesInProgress.setString(2, username);
				resultSet = selectGamesInProgress.executeQuery();
				while(resultSet.next()){
					info.append(resultSet.getInt("game_id") + "#");
					info.append(resultSet.getString("white") + "#");
					info.append(resultSet.getString("black") + "#");
					info.append(resultSet.getTimestamp("last_active")+"#");
				}
			}
			this.closeConnection();
		}catch(SQLException e){
			System.out.println("SQLException whilst trying to getGames INPROGRESS.");
			e.printStackTrace();
		}
		return info.toString();
	}
	/**
	 * When a user wants to continue a game, first check that the username is a player in game, game_id.
	 * If valid game then update the last_active attribute for the game and return the information
	 * as a Game Object.
	 * Returns: A Game object for the game with game_id.
	 */
	@Override
	public Game continueGame(String player_id, int game_id) {
		Game game = this.getGameInfo(game_id);
		String updateGameLastActiveString = "UPDATE chess_game SET last_active=CURRENT_TIMESTAMP  where game_id=?;";
		PreparedStatement updateGameLastActive = null;
		if(game.getBlack().equals(player_id)|| game.getWhite().equals(player_id)){
			try{
				this.connectDatabase();
				connection.setAutoCommit(false);
				updateGameLastActive = connection.prepareStatement(updateGameLastActiveString);
				updateGameLastActive.setInt(1, game_id);
				updateGameLastActive.executeUpdate();
				connection.commit();
				
				connection.setAutoCommit(true);
				this.closeConnection();
			}catch(SQLException e){
				System.out.println("Exception thrown in getCurrentGame for game_id: " + game_id);
				e.printStackTrace();
			}
		}
		return this.getGameInfo(game_id);
	}
	/**
	 * Gets the all the information from the database for a given username and 
	 * returns the information as a Player Object.
	 * @param username The user's username.
	 * @return A player object or null if username does not exist.
	 */
	public Player getPlayerInfo(String username){
		PreparedStatement getUserInfo = null;
		String getUserInfoString = "SELECT * FROM chess_user WHERE username = ?;";
		this.connectDatabase();
		if(this.isConnected()){
			try{
				getUserInfo = connection.prepareStatement(getUserInfoString);
				getUserInfo.setString(1, username);
				resultSet = getUserInfo.executeQuery();
				if(resultSet.next()){
					return new Player(resultSet.getInt("user_id"), resultSet.getString("username"), 
							resultSet.getString("password"), resultSet.getTimestamp("last_login"), resultSet.getTimestamp("reg_date"));
				}else{
					return null;
				}
			}catch(SQLException e){
				System.out.println("SQLException whilst trying to get user info.");
				e.printStackTrace();
			}
		}
		this.closeConnection();
		return null;
	}
	/**
	 * Gets the all the information from the database for a given game_id and 
	 * returns the information as a Game Object.
	 * @param game_id An int value referring to a game entry in the database.
	 * @return A Game object or null if game_id does not exist.
	 */
	public Game getGameInfo(int game_id){
		PreparedStatement getGameInfo = null;
		String getGameInfoString = "SELECT * FROM chess_game WHERE game_id = ?;";
		this.connectDatabase();
		if(this.isConnected()){
			try{
				getGameInfo = connection.prepareStatement(getGameInfoString);
				getGameInfo.setInt(1, game_id);
				resultSet = getGameInfo.executeQuery();
				if(resultSet.next()){
					return new Game(resultSet.getInt("game_id"), resultSet.getString("white"), resultSet.getString("black"), 
							resultSet.getTimestamp("game_start"), resultSet.getTimestamp("game_end"), 
							resultSet.getTimestamp("last_active"), resultSet.getString("board"), resultSet.getString("state"));
				}else{
					return null;
				}
			}catch(SQLException e){
				System.out.println("SQLException whilst trying to get user info.");
				e.printStackTrace();
			}
		}
		this.closeConnection();
		return null;
	}
	/**
	 * Returns a boolean indicating whether the connection is 
	 * connected to the database.
	 */
	public boolean isConnected() {
		if(this.connection==null)
			return false;
		try{
			if(this.connection.isClosed())
				return false;
		}catch(SQLException e){
			return false;
		}
		return true;
	}
	/**
	 * When a move is made this method should be called to update the board_info 
	 * in the chess_game table.
	 * @param game_id An int game_id.
	 * @param fen A String containing the fen of the updated board.
	 * @return The updated game object.
	 */
	@Override
	public Game updateGame(int game_id, String fen) {
		PreparedStatement updatechessboard = null;
		String updatechessboardString = "UPDATE chess_game SET board = ?, last_active=CURRENT_TIMESTAMP WHERE game_id = ?;";
		try{
			this.connectDatabase();
			connection.setAutoCommit(false);
			updatechessboard = connection.prepareStatement(updatechessboardString);
			updatechessboard.setString(1, fen);
			updatechessboard.setInt(2, game_id);
			updatechessboard.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
			this.closeConnection();
		}catch(SQLException e){
			System.out.println("Exception thrown when trying to update game");
			e.printStackTrace();
		}
		
		return this.getGameInfo(game_id);
	}
	/**
	 * When a game has finished call this method to update the database. 
	 * Arguments include the game_id and the result in format:
	 * WHITEWIN - white player wins the game.
	 * BLACKWIN - black player wins the game.
	 * DRAW - game ends in a draw.
	 * If result format is incorrect this method does returns 0,
	 * if successful returns 1.
	 * @param game_id Int representing the game_id.
	 * @param result A string as specified above.
	 */
	@Override
	public int endGame(int game_id, String result){
		System.out.println("Ending game " + game_id + ". Result = " + result);
		// check result is
		if(!(result.equals("WHITEWIN") || result.equals("BLACKWIN") || result.equals("DRAW")))
			return 0;
		String endGameString ="UPDATE chess_game SET game_end=CURRENT_TIMESTAMP, "
				+ "last_active=CURRENT_TIMESTAMP, state=? WHERE game_id=?;";
		PreparedStatement endGame = null;
		try{
			this.connectDatabase();
			connection.setAutoCommit(false);
			endGame = connection.prepareStatement(endGameString);
			endGame.setString(1, result);
			endGame.setInt(2, game_id);
			endGame.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
			this.closeConnection();
			return 1;
		}catch(SQLException e){
			System.out.println("Exception thrown when trying to end game");
			e.printStackTrace();
			
		}
		return 0;
	}
	/**
	 * DO NOT USE - FOR TESTING PURPOSES ONLY.
	 * @param username
	 */
	public void removeUser(String username, String password){
		PreparedStatement removeUser = null;
		String removeUserString = "DELETE FROM chess_user WHERE username = ? AND password = ?;";
		try{
			this.connectDatabase();
			connection.setAutoCommit(false);
			removeUser = connection.prepareStatement(removeUserString);
			removeUser.setString(1, username);
			removeUser.setString(2, password);
			//System.out.println(removeUser.toString());
			removeUser.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
			this.closeConnection();
		}catch(SQLException e){
			System.out.println("Exception thrown when trying to update remove user");
			e.printStackTrace();
		}
	}
	/**
	 * DO NOT USE - FOR TESTING PURPOSES ONLY.
	 * @param game_id
	 */
	public void removeGame(int game_id){
		PreparedStatement removeGame = null;
		String removeGameString = "DELETE FROM chess_game WHERE game_id = ?;";
		try{
			this.connectDatabase();
			connection.setAutoCommit(false);
			removeGame = connection.prepareStatement(removeGameString);
			removeGame.setInt(1, game_id);
			//System.out.println(removeGame.toString());
			removeGame.executeUpdate();
			connection.commit();
			connection.setAutoCommit(true);
			this.closeConnection();
		}catch(SQLException e){
			System.out.println("Exception thrown when trying to update remove game");
			e.printStackTrace();
		}
	}
	/**
	 * Searches database for number of games, wins, losses, draws for a given username.
	 * Returns a string in the format numOfGamesTotal#numOfWins#numOfLosses#numOfDraws#
	 * Example: 3#1#1#1#
	 * If username does not exist returns 0#0#0#0#.
	 * 
	 * @param username A String, the username to search for.
	 * @return a String representing the stats for the given username.
	 */
	@Override
	public String getUserStats(String username){
		String getStatsString = "SELECT COUNT(*) total,"
				+ "SUM(CASE WHEN ((state='WHITEWIN' AND white=?) OR (state='BLACKWIN' AND black=?)) THEN 1 ELSE 0 END) wins ,"
				+ "SUM(CASE WHEN state='DRAW' THEN 1 ELSE 0 END) draws  "
				+ "FROM (SELECT * FROM chess_game WHERE (black=? OR white=?) AND game_end IS NOT NULL) AS allGames;";
		PreparedStatement getStats = null;
		try{
			this.connectDatabase();
			getStats = connection.prepareStatement(getStatsString);
			for(int i=0; i<4; i++)
				getStats.setString(i+1, username);
			resultSet = getStats.executeQuery();
			resultSet.next();
			StringBuffer buffer = new StringBuffer();
			buffer.append(resultSet.getInt("total")+"#"); // total games
			buffer.append(resultSet.getInt("wins")+"#"); // total wins
			buffer.append(resultSet.getInt("total") - resultSet.getInt("wins") -resultSet.getInt("draws") +"#"); // total losses
			buffer.append(resultSet.getInt("draws") + "#"); // total draws
			this.closeConnection();
			return buffer.toString();
		}catch(SQLException e){
			System.out.println("Exception thrown when trying to update remove game");
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * ############
	 *     MAIN
	 * ############
	 */
	public static void main(String[] args) {
		DatabaseManager dbmanager = new DatabaseManager();
		System.out.println(dbmanager.getGamesInWaiting());
//		dbmanager.endGame(9, "WHITEWIN");
//		dbmanager.endGame(10, "WHITEWIN");
//		dbmanager.endGame(11, "DRAW");
//		dbmanager.endGame(12, "DRAW");
//		dbmanager.endGame(13, "BLACKWIN");
//		dbmanager.endGame(14, "BLACKWIN");
//		dbmanager.login("test5", "password");
//		dbmanager.newGame("test5");
//		dbmanager.joinGame("test3", 4);
//		dbmanager.endGame(4, "BLACKWIN");
//		dbmanager.login("dlewis", "stone");
//		dbmanager.login("meerkat", "coffee2");
//		//dbmanager.newGame("meerkat");
//		//dbmanager.joinGame("meerkat", 3);
//		//ArrayList<String> list = dbmanager.getCurrentGames("lorilew");
//		//System.out.println(list.toString());
//		//dbmanager.newGame("bodum");
//		dbmanager.updateGame(1, "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2");
		
	}
}
