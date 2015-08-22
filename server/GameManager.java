package server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import database.DatabaseManagerInterface;
import database.DatabaseManager;
import engine.AbstractBoardController;
import engine.BoardController;
import engine.Game;
import sockets.ClientMessage;
import sockets.ServerMessage;
import sockets.ServerMessageHandlerInterface;
import sockets.ServerSocketConnection;
import util.Cell;

public class GameManager implements ServerMessageHandlerInterface {
	
	private Map<ServerSocketConnection, ConnectionData> players;
	private Map<String, GameData> games;
	private DatabaseManagerInterface database;
	
	/**
	 * Standard constructor
	 */
	public GameManager()
	{
		players = new ConcurrentHashMap<ServerSocketConnection, ConnectionData>();
		games = new ConcurrentHashMap<String, GameData>();
		database = new DatabaseManager();
	}
	
	/**
	 * Stored information related to a client connection
	 * @author Joseph Gent
	 *
	 */
	class ConnectionData
	{
		private String username;
		private GameData gameData;
		
		public ConnectionData(String u)
		{
			this.username = u;
			this.gameData = null;
		}
		
		public String getUsername()
		{
			return username;
		}
		
		public GameData getGameData()
		{
			return gameData;
		}
		
		public void setGameData(GameData g)
		{
			this.gameData = g;
		}
	}
	
	/**
	 * Stores data related to a game with a viewer
	 * @author Joseph Gent
	 *
	 */
	class GameData
	{
		private AbstractBoardController game;
		private ServerSocketConnection white;
		private ServerSocketConnection black;
		private String whiteName;
		private String blackName;
		private String gameID;
		private Game gameInstance;
		
		public GameData(String gameID)
		{
			// Get the game data from the database
			database.Game dbGame;
			synchronized(database)
			{
				dbGame = database.getGameInfo(Integer.parseInt(gameID));
			}
			gameInstance = new Game(dbGame.getGameInfo());
			game = new BoardController(gameInstance);
			whiteName = dbGame.getWhite();
			blackName = dbGame.getBlack();
			this.gameID = gameID;
		}
		
		public GameData(String gameID, database.Game dbGame)
		{
			// Gets the game data from a result from the database
			System.out.println(dbGame.getGameInfo());
			gameInstance = new Game(dbGame.getGameInfo());
			game = new BoardController(gameInstance);
			whiteName = dbGame.getWhite();
			blackName = dbGame.getBlack();
			this.gameID = gameID;
		}
		
		public AbstractBoardController getController()
		{
			return this.game;
		}
		
		public ServerSocketConnection getWhite()
		{
			return this.white;
		}
		
		public String getWhiteName()
		{
			return this.whiteName;
		}
		
		public ServerSocketConnection getBlack()
		{
			return this.black;
		}
		
		public String getBlackName()
		{
			return this.blackName;
		}
		
		public String getGameID()
		{
			return this.gameID;
		}
		
		public String getGameFEN()
		{
			/*String fen = util.Fen.arrayToFenBoard(gameInstance.getBoard());
			if(gameInstance.isWhiteTurn())
			{
				fen = fen + " w";
			}
			else
			{
				fen = fen + " b";
			}
			
			gameInstance.toFen()
			*/
			return gameInstance.toFen();
		}
		
		public void setWhite(ServerSocketConnection w)
		{
			this.white = w;
		}
		
		public void setWhiteName(String w)
		{
			this.whiteName = w;
		}
		
		public void setBlack(ServerSocketConnection b)
		{
			this.black = b;
		}
		
		public void setBlackName(String b)
		{
			this.blackName = b;
		}
	}

	/**
	 * Handles the message from the client
	 */
	@Override
	public void handleMessage(ServerSocketConnection connection, ClientMessage message, String[] arguments)
	{
		switch(message)
		{
		case C_LOGIN:
			// Handle login.
			// For now, we will simply accept if the name isn't already logged in.
			if(arguments.length < 2 || players.containsKey(connection))
			{
				connection.sendMessage(ServerMessage.S_LOGINREJECTED, "");
				break;
			}
			
			database.Player p;
			
			synchronized(database)
			{
				p = database.login(arguments[0], arguments[1]);
			}
			
			if(p == null)
			{
				connection.sendMessage(ServerMessage.S_LOGINREJECTED, "");
			}
			else
			{
				players.put(connection, new ConnectionData(p.getUsername()));
				connection.sendMessage(ServerMessage.S_LOGINACCEPTED, "");
			}
			break;
			
		case C_DISCONNECT:
			// Handle disconnecting
			// Simply close the connection.
			leaveGame(connection);
			if(players.containsKey(connection))
			{
				players.remove(connection);
			}
			connection.disconnect();
			break;
			
		case C_GETOPENGAMES:
			// Handle open game list acquisition
			String openGames;
			synchronized(database)
			{
				openGames = database.getGamesInWaiting();
			}
			connection.sendMessage(ServerMessage.S_OPENGAMES, openGames);
			break;
			
		case C_GETMYACTIVEGAMES:
			// Handle active game list acquisition
			String joinedGames;
			synchronized(database)
			{
				joinedGames = database.getCurrentGames(players.get(connection).getUsername());
			}
			connection.sendMessage(ServerMessage.S_YOURACTIVEGAMES, joinedGames);
			break;
			
		case C_CREATEGAME:
			// Handle game creation with the database
			if(players.containsKey(connection))
			{
				int gameID;
				synchronized(database)
				{
					gameID = database.newGame(players.get(connection).getUsername());
				}
				connection.sendMessage(ServerMessage.S_GAMECREATED, gameID + "");
			}
			else
			{
				connection.sendMessage(ServerMessage.S_ERROR, "Not logged in.");
			}
			break;
			
		case C_JOINGAME:
			// Handle game joining with the database
			if(players.containsKey(connection))
			{
				synchronized(database)
				{
					database.joinGame(players.get(connection).getUsername(), Integer.valueOf(arguments[0]));
					database.updateGame(Integer.valueOf(arguments[0]), "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
				}
				System.out.println("Game joined.");
				connection.sendMessage(ServerMessage.S_GAMEJOINED, "");
			}
			else
			{
				connection.sendMessage(ServerMessage.S_ERROR, "Not logged in.");
			}
			break;
			
		case C_RESUMEGAME:
			// Handle resuming a game
			if(players.containsKey(connection))
			{
				String id = arguments[0];
				String name = players.get(connection).getUsername();
				
				database.Game dbGame;
				synchronized(database)
				{
					dbGame = database.getGameInfo(Integer.valueOf(arguments[0]));
				}
				if(dbGame == null)
				{
					connection.sendMessage(ServerMessage.S_ERROR, "Game does not exist.");
					break;
				}
				else if(dbGame.getState().equals("INWAITING"))
				{
					connection.sendMessage(ServerMessage.S_ERROR, "Game does not yet have two players.");
					break;
				}
				
				if(!games.containsKey(id))
				{
					GameData joinedGame = new GameData(id, dbGame);
					joinedGame.setWhiteName(dbGame.getWhite());
					joinedGame.setBlackName(dbGame.getBlack());
					games.put(id, joinedGame);
				}
				
				if(games.containsKey(id))
				{
					// If the game is running, link to it
					if(games.get(id).getWhiteName().equals(name))
					{
						if(games.get(id).getWhite() == null)
						{
							games.get(id).setWhite(connection);
							players.get(connection).setGameData(games.get(id));
							connection.sendMessage(ServerMessage.S_GAMERESUMED, games.get(id).getGameFEN() +
									ServerMessage.BREAKCHAR + games.get(id).getWhiteName() +
									ServerMessage.BREAKCHAR + games.get(id).getBlackName());
						}
						else
						{
							connection.sendMessage(ServerMessage.S_ERROR, "Player already in game.");
						}
					}
					else if(games.get(id).getBlackName().equals(name))
					{
						if(games.get(id).getBlack() == null)
						{
							games.get(id).setBlack(connection);
							players.get(connection).setGameData(games.get(id));
							connection.sendMessage(ServerMessage.S_GAMERESUMED, games.get(id).getGameFEN() +
									ServerMessage.BREAKCHAR + games.get(id).getWhiteName() +
									ServerMessage.BREAKCHAR + games.get(id).getBlackName());
						}
						else
						{
							connection.sendMessage(ServerMessage.S_ERROR, "Player already in game.");
						}
					}
					else
					{
						connection.sendMessage(ServerMessage.S_ERROR, "Not your game.");
					}
				}
				else
				{
					connection.sendMessage(ServerMessage.S_ERROR, "Unknown error. Game could not be retrieved.");
				}
			}
			else
			{
				connection.sendMessage(ServerMessage.S_ERROR, "Not logged in.");
			}
			break;
			
		case C_LEAVEGAME:
			// Handle game leaving
			// Remove the player from the current game
			leaveGame(connection);
			connection.sendMessage(ServerMessage.S_GAMELEFT, "");
			break;
			
		case C_ABANDONGAME:
			// Handle game abandon
			// For now, do nothing
			//connection.sendMessage(ServerMessage.S_GAMEABANDONED, "");
			// TODO: Deal with abandons
			break;
			
		case C_MAKEMOVE:
			if(players.containsKey(connection))
			{
				if(players.get(connection).getGameData() != null  && arguments.length >= 4)
				{
					AbstractBoardController c = players.get(connection).getGameData().getController();
					Cell from = new Cell(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
					Cell to = new Cell(Integer.parseInt(arguments[2]), Integer.parseInt(arguments[3]));
					// Synchronise
					synchronized(c)
					{
						if(c.isValidMove(from, to))
						{
							c.makeMove(from, to);
							// Transmit game state
							synchronized(database)
							{
								database.updateGame(Integer.parseInt(players.get(connection).getGameData().getGameID()), players.get(connection).getGameData().getGameFEN());
							}
							//connection.sendMessage(ServerMessage.S_FEN, players.get(connection).getGameData().getGameFEN());
                                                        
                                                        String id = players.get(connection).getGameData().getGameID();
                                                        ServerSocketConnection black = games.get(id).getBlack();
                                                        ServerSocketConnection white = games.get(id).getWhite();
                                                        String fen = players.get(connection).getGameData().getGameFEN();
                                                        
                                                        if (black != null){
                                                            black.sendMessage(ServerMessage.S_FEN, fen);
                                                        }
                                                        if (white != null){
                                                            white.sendMessage(ServerMessage.S_FEN, fen);
                                                        }
                                                        
						}
					}
				}
			}
			break;
			
		default:
			break;
		}
		
	}
	
	/**
	 * Removed a player from the game they are currently in.
	 * @param connection the connection of the player to remove
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-14
	 */
	private void leaveGame(ServerSocketConnection connection)
	{
		if(players.containsKey(connection))
		{
			GameData gd = players.get(connection).getGameData();
			if(gd != null)
			{
				if(gd.getWhite() == connection)
				{
					gd.setWhite(null);
				}
				else if(gd.getBlack() == connection)
				{
					gd.setBlack(null);
				}
				
				if(gd.getWhite() == null &
						gd.getBlack() == null)
				{
					games.remove(gd.getGameID());
				}
			}

			players.get(connection).setGameData(null);
		}
	}

}
