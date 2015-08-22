package server;

import static org.junit.Assert.*;

import org.junit.Test;

import database.DatabaseManager;
import engine.Game;
import sockets.ClientSocketManager;
import sockets.GameListing;
import util.Cell;

public class ServerTests {
	
	/*
	 * Player Test 1
	 * Attempt to login using both an incorrect and correct password
	 */
	@Test
	public void PlayerTest1()
	{
		Thread t = new ServerTestThread(1);
		t.start();
		
		try
		{
			// Sleep to give the server thread time to start
			Thread.sleep(1000);
			ClientSocketManager csm = new ClientSocketManager();
			csm.connect("localhost", 4444);
			assertTrue(csm.isConnected());
			assertFalse(csm.login("Joe_Test_1", "veg"));
			assertTrue(csm.login("Joe_Test_1", "cake"));
			csm.disconnect();
		}
		catch(InterruptedException i)
		{
			i.printStackTrace();
		}
	}
	
	/*
	 * Game Test 1
	 * Attempt to login and resume an active game
	 */
	@Test
	public void GameTest1()
	{
		Thread t = new ServerTestThread(1);
		t.start();
		
		try
		{
			// Sleep to give the server thread time to start
			Thread.sleep(1000);
			ClientSocketManager csm = new ClientSocketManager();
			csm.connect("localhost", 4444);
			assertTrue(csm.isConnected());
			assertTrue(csm.login("Joe_Test_1", "cake"));
			Game game = csm.resumeGame("19");
			assertTrue(game.toFen().equals("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 1 1"));
			csm.disconnect();
		}
		catch(InterruptedException i)
		{
			i.printStackTrace();
		}
	}
	
	/*
	 * Game Test 2
	 * Attempt to login and resume an active game with two users at once
	 */
	@Test
	public void GameTest2()
	{
		Thread t = new ServerTestThread(2);
		t.start();
		
		try
		{
			// Sleep to give the server thread time to start
			Thread.sleep(1000);
			ClientSocketManager csm1 = new ClientSocketManager();
			ClientSocketManager csm2 = new ClientSocketManager();
			csm1.connect("localhost", 4444);
			csm2.connect("localhost", 4444);
			assertTrue(csm1.isConnected());
			assertTrue(csm2.isConnected());
			assertTrue(csm1.login("Joe_Test_1", "cake"));
			assertTrue(csm2.login("Joe_Test_2", "cake"));
			Game game1 = csm1.resumeGame("19");
			Game game2 = csm2.resumeGame("19");
			assertTrue(game1.toFen().equals("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 1 1"));
			assertTrue(game2.toFen().equals("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 1 1"));
			csm1.disconnect();
			csm2.disconnect();
		}
		catch(InterruptedException i)
		{
			i.printStackTrace();
		}
	}
	
	/*
	 * Game Test 3
	 * Create and play a new game
	 * Requires game controller to be completed
	 */
	/*@Test
	public void GameTest3()
	{
		DatabaseManager database = new DatabaseManager();
		Thread t = new ServerTestThread(2);
		t.start();
		String gID = "empty string";
		
		try
		{
			// Sleep to give the server thread time to start
			Thread.sleep(1000);
			ClientSocketManager csm1 = new ClientSocketManager();
			ClientSocketManager csm2 = new ClientSocketManager();
			csm1.connect("localhost", 4444);
			csm2.connect("localhost", 4444);
			assertTrue(csm1.isConnected());
			assertTrue(csm2.isConnected());
			assertTrue(csm1.login("Joe_Test_1", "cake"));
			assertTrue(csm2.login("Joe_Test_2", "cake"));
			gID = csm1.createGame();
			csm2.joinGame(gID);
			Thread.sleep(500);
			Game game1 = csm1.resumeGame(gID);
			Game game2 = csm2.resumeGame(gID);
			assertTrue(game1.toFen().equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
			assertTrue(game2.toFen().equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
			ClientSocketManager white;
			ClientSocketManager black;
			if(game1.isPlayerWhite())
			{
				white = csm1;
				black = csm2;
			}
			else
			{
				white = csm2;
				black = csm1;
			}
			white.makeMove(new Cell(0, 1), new Cell(0, 3));
			Thread.sleep(500);
			System.out.println(game1.toFen());
			System.out.println("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 1 1");
			assertTrue(game1.toFen().equals("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 1 1"));
			assertTrue(game2.toFen().equals("rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 1 1"));
			csm1.disconnect();
			csm2.disconnect();
		}
		catch(InterruptedException i)
		{
			i.printStackTrace();
		}
		
		database.removeGame(Integer.parseInt(gID));
	}*/

}
