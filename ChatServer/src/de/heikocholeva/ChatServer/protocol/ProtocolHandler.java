package de.heikocholeva.ChatServer.protocol;

import de.heikocholeva.ChatServer.Main;
import de.heikocholeva.ChatServer.client.Client;
import de.heikocholeva.ChatServer.client.actions.LoginClient;
import de.heikocholeva.ChatServer.client.actions.RegisterClient;
import de.heikocholeva.ChatServer.client.actions.SendMessageClient;
import de.heikocholeva.ChatServer.log.Log.LogLevel;

public class ProtocolHandler {
	
	public void fromClient(Client client, Protocol protocol) {
		Main.getLogger().write(LogLevel.DEBUG, "Incoming protocol from client "
				+ client.getSocket().getInetAddress().toString() + ": " + protocol.toString());
		switch (protocol.getAction()) {
		case REGISTER:
			RegisterClient.handle(client, protocol);
			break;
		case LOGIN:
			LoginClient.handle(client, protocol);
			break;
		case SEND_MESSAGE:
			SendMessageClient.handle(client, protocol);
			break;
		default:
			break;
		}
	}
	
	public void toClient(Client client, Protocol protocol) {
		Main.getLogger().write(LogLevel.DEBUG, "Writing protocol to client "
				+ client.getSocket().getInetAddress().toString() + ": " + protocol.toString());
		client.write(protocol);
	}

}
