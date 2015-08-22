package server;

import sockets.ClientSocketManager;
import sockets.GameListener;
import sockets.GameListing;

public class TestClient implements GameListener {
	public static void main(String[] args)
	{
		ClientSocketManager csm = new ClientSocketManager();
		csm.connect("localhost", 4444);
		
		// Test connectivity
		if(!csm.isConnected())
		{
			return;
		}
		
		try
		{
			System.out.println("Joe_Test_1, veg : " + csm.login("Joe_Test_1", "veg"));
			System.out.println("Joe_Test_1, cake : " + csm.login("Joe_Test_1", "cake"));
			System.out.println("Resuming.");
			csm.resumeGame("19");
			csm.makeMove(new util.Cell(0, 1), new util.Cell(0, 3));
			csm.leaveGame();
			GameListing[] ogl = csm.getOpenGames();
			if(ogl != null)
			{
				for(GameListing g : ogl)
				{
					System.out.println(g.getGameID() + ": " + g.getWhitePlayer() + ", " + g.getBlackPlayer());
				}
			}
			else
			{
				System.out.println("Connection fault in open games.");
			}
			
			Thread.sleep(500);
			
			GameListing[] agl = csm.getMyGames();
			if(agl != null)
			{
				for(GameListing g : agl)
				{
					System.out.println(g.getGameID() + ": " + g.getWhitePlayer() + ", " + g.getBlackPlayer());
				}
			}
			else
			{
				System.out.println("Connection fault in active games.");
			}
			csm.disconnect();
		}
		catch(InterruptedException i)
		{
			
		}
	}

	@Override
	public void makeMove(String fen) {
		System.out.println("A move was made!");
		System.out.println(fen);
	}

	@Override
	public void setWin() {
		System.out.println("You win!");
		
	}

	@Override
	public void setLoss() {
		System.out.println("You lose!");
		
	}

	@Override
	public void setDraw() {
		System.out.println("You draw!");
		
	}
}
