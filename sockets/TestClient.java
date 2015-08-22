package sockets;


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
			csm.login("Cheese", "cake");
			System.out.println("Login Accepted");
			csm.createGame();
			System.out.println("Game Create Sent");
			csm.joinGame("100");
			System.out.println("Game Join Sent");
			csm.resumeGame("100");
			System.out.println("Game Resume Sent");
			csm.sendMessage(ClientMessage.C_DISCONNECT, "");
			Thread.sleep(1000);
			System.out.println("Disconnecting");
		}
		catch(InterruptedException i)
		{
			
		}
	}

	@Override
	public void makeMove(String fen) {
		System.out.println("A move was made!");
		
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
