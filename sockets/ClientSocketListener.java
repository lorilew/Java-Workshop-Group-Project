package sockets;

import java.io.*;

import engine.Game;
//import engine.ServerController;
import engine.ServerController;

public class ClientSocketListener extends Thread {

	private BufferedReader inputStream;
	private ClientSocketManager manager;
	private boolean running;
	private Game game;
	private GameListener controller;
	
	/**
	 * Constructor for the ClientSocketListener
	 * @param inputStream the BufferedReader which provides input
	 * @param inputQueue the LinkedBlockingQueue of InputMessage to place the input on
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-04
	 */
	public ClientSocketListener(BufferedReader inputStream, ClientSocketManager manager)
	{
		super("SocketListener");
		this.inputStream = inputStream;
		this.manager = manager;
		this.game = null;
		this.controller = null;
		
		this.running = false;
	}
	
	// Game game = new Game(response.getArgumentArray()[0]);
	@Override
	public void run()
	{
		this.running = true;
		
		try
		{
			while(running)
			{
				String input = inputStream.readLine();
				
				if(input == null)
				{
					running = false;
				}
				else
				{
					int firstBreak = input.indexOf(ServerMessage.BREAKCHAR);

					InputMessage im;
					if(firstBreak == -1)
					{
						im = new InputMessage(ServerMessage.valueOf(input), "");
					}
					else
					{
						im = new InputMessage(ServerMessage.valueOf(input.substring(0, firstBreak)),
								input.substring(firstBreak + 1, input.length()));
					}
					
					if(im.getMessage() == ServerMessage.S_GAMERESUMED && im.getArgumentArray() != null)
					{
						if(im.getArgumentArray().length >= 3)
						{
							this.game = new Game(im.getArgumentArray()[0]);
							this.game.setWhitePlayer(im.getArgumentArray()[1]);
							this.game.setBlackPlayer(im.getArgumentArray()[2]);
							// New test controller
							// TODO: Replace this with the actual controller
							this.controller = new ServerController(game);
						}
						
					}
					
					// Only pass the message to another thread if it's not one we should pass direct to the
					// game controller
					if(im.getMessage() == ServerMessage.S_FEN)
					{
						if(controller != null)
						{
							controller.makeMove(im.getArgumentArray()[0]);
						}
						else
						{
							System.out.println("Controller is null");
						}
					}
					else if(im.getMessage() == ServerMessage.S_STATE)
					{
						if(controller != null)
						{
							String state = im.getArgumentArray()[0];
							switch(state)
							{
							case "-1":
								controller.setLoss();
								break;
								
							case "0":
								controller.setDraw();
								break;
								
							case "1":
								controller.setWin();
								break;
								
							default:
								break;
							}
						}
					}
					else
					{
						synchronized(manager.getResponseMessage())
						{
							// Store the response, and inform the main thread that it has been received
							manager.getResponseMessage().setAll(im.getMessage(), im.getArgument());
							manager.getResponseMessage().notify();
						}
					}
				}
			}
		}
		catch(IOException e)
		{
			/* 
			 * Error catching here
			 * Currently has none, as throwing an exception by closing the input stream of the socket
			 * seems to be the only/best way of stopping the read loop.
			 * 
			 * If anyone comes up with a better method, let me know.
			 * 
			 * Oddly enough, this doesn't always seem to function the way it appears it should.
			 * In general, readLine() seems to return null rather than throwing an exception.
			 */
		}
		catch(NullPointerException n)
		{
			n.printStackTrace();
		}
		finally
		{
			this.running = false;
		}
	}
	
	/**
	 * Returns the current Game being handled.
	 * @return the current Game being handled.
	 * 
	 * @author Joseph Gent
	 * @version 2015-03-11
	 */
	public Game getGame()
	{
		return this.game;
	}
	
}
