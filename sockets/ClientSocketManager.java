package sockets;

import java.io.*;
import java.net.*;

import util.Cell;
import engine.Game;

public class ClientSocketManager implements SocketClientInterface {

	private boolean connected;
	private int port;
	private String ip;
	private Socket socket;
	private PrintWriter outputStream;
	private ClientSocketListener listener;
	private boolean inGame;
	private InputMessage responseMessage;
	
	private static final int TIMEOUT_TIME = 5000;
	
	/**
	 * Constructor for the ClientSocketManager
	 */
	public ClientSocketManager()
	{
		this.responseMessage = new InputMessage(null, "");
		this.ip = "localhost";
		this.port = 4444; // Default for now, will update later
		this.connected = false;
		this.inGame = false;
	}
	
	/**
	 * Returns true if connected to the server (as far as it knows).
	 */
	@Override
	public boolean isConnected() {
		return connected;
	}
	
	/**
	 * Connects to the server.
	 */
	@Override
	public void connect(String ip, int port) {
		
		this.ip = ip;
		this.port = port;
		
		try
		{
			this.socket = new Socket(this.ip, this.port);
			this.outputStream = new PrintWriter(socket.getOutputStream(), true);
			this.listener = new ClientSocketListener(new BufferedReader(
					new InputStreamReader(socket.getInputStream())), this);
			this.listener.start();
			this.connected = true; // Everything has gone well, so we are connected.
		}
		catch(UnknownHostException e)
		{
			System.out.println("Could not locate the server at the specified ip.");
			this.connected = false;
		}
		catch(IOException e)
		{
			System.out.println("There was a failure with the Socket or IO streams.");
			this.connected = false;
		}
	}

	/**
	 * Sends a message to the server if connected.
	 */
	@Override
	public void sendMessage(ClientMessage msg, String argument) {
		if(this.connected)
		{
			outputStream.println(msg.toString() + ServerMessage.BREAKCHAR + argument);
		}
	}
	
	/**
	 * Returns a reference to the InputMessage instance that will be used to contain all responses from
	 * the server
	 * @return a reference to the InputMessage that stores server responses
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-12
	 */
	public InputMessage getResponseMessage()
	{
		return this.responseMessage;
	}
	
	/**
	 * Awaits a response from the server when a message is sent.
	 * @return the resulting message. The message is null if no response is recieved in time
	 * @throws InterruptedException
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-12
	 */
	private InputMessage awaitResponse() throws InterruptedException
	{
		synchronized(responseMessage)
		{
			if(responseMessage.getMessage() == null)
			{
				responseMessage.wait(TIMEOUT_TIME);
			}
			
			InputMessage result = new InputMessage(responseMessage.getMessage(), responseMessage.getArgument());
			responseMessage.setAll(null, "");
			return result;
		}
	}

	/*
	 * Returns a reference to the message queue, a LinkedBlockingQueue of InputMessage.
	 *
	@Override
	public LinkedBlockingQueue<InputMessage> getMessageQueue() {
		return responseQueue;
	}
	*/
	
	/**
	 * Disconnects from the server.
	 */
	@Override
	public void disconnect()
	{
		if(this.connected)
		{
			try
			{
				sendMessage(ClientMessage.C_DISCONNECT, "");
				socket.getInputStream().close();

				outputStream.close();
				socket.close();
			}
			catch(IOException e)
			{
				System.out.println("There was a failure while disconnecting the ClientSocketManager.");
			}

			this.connected = false;
		}
	}
	
	public void finalize()
	{
		if(this.connected)
		{
			disconnect();
		}
	}

	@Override
	public String createGame() throws InterruptedException {
		if(this.connected)
		{
			sendMessage(ClientMessage.C_CREATEGAME, "");
			InputMessage response = awaitResponse();
			if(response.getMessage() == ServerMessage.S_GAMECREATED)
			{
				return response.getArgumentArray()[0];
			}
			return "";
		}
		else
		{
			return "";
		}
	}

	@Override
	public boolean joinGame(String game_id) throws InterruptedException {
		if(this.connected)
		{
			sendMessage(ClientMessage.C_JOINGAME, game_id);
			InputMessage response = awaitResponse();
			if(response.getMessage() == ServerMessage.S_GAMEJOINED)
			{
				return true;
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean login(String username, String password) throws InterruptedException {
		if(this.connected)
		{
			sendMessage(ClientMessage.C_LOGIN, username + ServerMessage.BREAKCHAR + password);
			InputMessage response = awaitResponse();
			if(response.getMessage() == ServerMessage.S_LOGINACCEPTED)
			{
				return true;
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	@Override
	public GameListing[] getOpenGames() throws InterruptedException {
		if(this.connected)
		{
			sendMessage(ClientMessage.C_GETOPENGAMES, "");
			InputMessage response = awaitResponse();
			if(response.getMessage() == ServerMessage.S_OPENGAMES)
			{
				GameListing output[] = new GameListing[response.getArgumentArray().length/4];
				for(int i = 0; i < output.length; i++)
				{
					output[i] = new GameListing(response.getArgumentArray()[4*i],
							response.getArgumentArray()[(4*i) + 1],
							response.getArgumentArray()[(4*i) + 2],
							response.getArgumentArray()[(4*i) + 3]);
				}
				
				return output;
			}
			return null;
		}
		else
		{
			return null;
		}
	}

	@Override
	public GameListing[] getMyGames() throws InterruptedException {
		if(this.connected)
		{
			sendMessage(ClientMessage.C_GETMYACTIVEGAMES, "");
			InputMessage response = awaitResponse();
			if(response.getMessage() == ServerMessage.S_YOURACTIVEGAMES)
			{
				GameListing output[] = new GameListing[response.getArgumentArray().length/4];
				for(int i = 0; i < output.length; i++)
				{
					output[i] = new GameListing(response.getArgumentArray()[4*i],
							response.getArgumentArray()[(4*i) + 1],
							response.getArgumentArray()[(4*i) + 2],
							response.getArgumentArray()[(4*i) + 3]);
				}
				
				return output;
			}
			return null;
		}
		else
		{
			return null;
		}
	}

	@Override
	public Game resumeGame(String game_id) throws InterruptedException {
		if(this.connected)
		{
			sendMessage(ClientMessage.C_RESUMEGAME, game_id);
			InputMessage response = awaitResponse();
			if(response.getMessage() == ServerMessage.S_GAMERESUMED)
			{				
				this.inGame = true;
				return this.listener.getGame();
			}
			return null;
		}
		else
		{
			return null;
		}
	}

	@Override
	public boolean leaveGame() throws InterruptedException {
		if(this.connected)
		{
			sendMessage(ClientMessage.C_LEAVEGAME, "");
			InputMessage response = awaitResponse();
			if(response.getMessage() == ServerMessage.S_GAMELEFT)
			{
				return true;
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean abandonGame() throws InterruptedException {
		if(this.connected)
		{
			sendMessage(ClientMessage.C_ABANDONGAME, "");
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean makeMove(Cell from, Cell to) throws InterruptedException {
		if(this.connected)
		{
			StringBuilder str = new StringBuilder();
			str.append(from.x);
			str.append(ServerMessage.BREAKCHAR);
			str.append(from.y);
			str.append(ServerMessage.BREAKCHAR);
			str.append(to.x);
			str.append(ServerMessage.BREAKCHAR);
			str.append(to.y);
			sendMessage(ClientMessage.C_MAKEMOVE, str.toString());
			return true;
		}
		else
		{
			return false;
		}
	}

}
