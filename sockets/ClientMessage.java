package sockets;

/**
 * The type of message sent from the client to the server.
 * 
 * Most of these either:
 * 	(a) Request data from the server, which is then sent via a SocketInputMessage.
 * 	(b) Inform the server of a change on the client, which it can act upon.
 * 
 * GUI messages always require a single response. Never send a GUI response from the server
 * except when a GUI message has been received.
 * 
 * Game messages can be sent at any time. They currently include:
 * - C_ABANDONGAME
 * - C_MAKEMOVE
 * 
 * @author Joseph Gent
 * @version 2015-03-04
 */
public enum ClientMessage {
	C_TEXT,				// Argument represents a text string. Used for testing
	C_LOGIN,			// Argument represents the username then the password
	C_DISCONNECT,		// Argument is empty
	C_GETOPENGAMES,		// Argument is empty
	//C_GETMYOPENGAMES,	// Argument is empty
	C_GETMYACTIVEGAMES,	// Argument is empty
	//C_GETMYENDEDGAMES,	// Argument is empty
	C_CREATEGAME,		// Argument is empty
	C_JOINGAME,			// Argument represents the game_id of an open game to join
	C_RESUMEGAME,		// Argument represents the game_id of a started game to continue (sets as current game)
	C_LEAVEGAME,		// Argument is empty. Leaves the current game, informing the server to not update you on it's progress until you resume it.
	C_ABANDONGAME,		// Argument is empty
	C_MAKEMOVE,			// Argument is the x, y coordinates of the starting square, and x, y coordinates of the target square
	//C_GETSTATE,			// Argument is empty
	//C_GETFEN,			// Argument is empty
	//C_OFFERDRAW,		// Argument is empty
	//C_GETGAMENAME,		// Argument is empty
	//C_GETOPPONENTNAME	// Argument is empty
}
