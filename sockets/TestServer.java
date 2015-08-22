package sockets;

public class TestServer implements ServerMessageHandlerInterface {
	
	public static void main(String[] args)
	{
		ServerSocketManager ssm = new ServerSocketManager(4444, new TestServer());
		ssm.listen(1);
	}

	@Override
	public void handleMessage(ServerSocketConnection connection, ClientMessage message, String[] argument)
	{
		for(int i = 0; i < argument.length; i++)
		{
			if(i != 0)
			{
				System.out.print(" ");
			}
			System.out.print(argument[i]);
		}
		System.out.println();
		
		if(message == ClientMessage.C_DISCONNECT)
		{
			connection.sendMessage(ServerMessage.S_DISCONNECT, "Goodbye");
			connection.disconnect();
		}
		else if(message == ClientMessage.C_JOINGAME)
		{
			connection.sendMessage(ServerMessage.S_GAMEJOINED, argument[0]);
		}
		else if(message == ClientMessage.C_LOGIN)
		{
			connection.sendMessage(ServerMessage.S_LOGINACCEPTED, "");
		}
		else if(message == ClientMessage.C_RESUMEGAME)
		{
			connection.sendMessage(ServerMessage.S_GAMERESUMED, "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2");
			connection.sendMessage(ServerMessage.S_STATE, "1");
		}
		else
		{
			connection.sendMessage(ServerMessage.S_TEXT, message.toString() + " was recieved with " + argument.length + " arguments.");
		}
	}

}
