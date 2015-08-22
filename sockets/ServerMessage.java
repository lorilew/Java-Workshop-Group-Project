package sockets;

/**
 * The type of message sent from the server to the client.
 * 
 * @author Joseph Gent
 * @version 2015-03-04
 */
public enum ServerMessage {
	S_TEXT,				// Argument represents a text string. Used for testing
	S_ERROR,			// Argument represents what error has occurred. Not always fatal (eg, invalid move)
	S_LOGINACCEPTED,	// Argument is empty
	S_LOGINREJECTED,	// Argument is empty
	S_GAMECREATED,		// Argument represents the game_id of the created game
	S_GAMEJOINED,		// Argument is empty
	S_GAMERESUMED,		// Argument is fen, white username, black username
	S_GAMELEFT,			// Argument is empty
	S_GAMEABANDONED,	// Argument is empty
	S_OPENGAMES,		// Argument represents the game_id, opponent name and created time of all games looking for a player
	//S_YOUROPENGAMES,	// Argument represents the game_id and creation time of all the current user's open games
	S_YOURACTIVEGAMES,	// Argument represents the game_id, opponent name and last move time of all the current user's active games
	//S_YOURENDEDGAMES,	// Argument represents the game_id, opponent name and last move time of all the current user's ended games
	S_STATE,			// Argument represents the game_state of a game
	S_FEN,				// Argument represents the fen of a game
	//S_OFFEREDDRAW,		// Argument represents the game_id
	//S_OPPONENTNAME,		// Argument represents the game_id and opponent's name
	S_DISCONNECT;		/* Argument is an optional String with extra information. This is a courtesy to
						 * inform the client that the server is terminating their side of the connection
						 * for some reason. To be expected if the client has already sent C_DISCONNECT.
						 */
	
	public static final char BREAKCHAR = '#';
}
