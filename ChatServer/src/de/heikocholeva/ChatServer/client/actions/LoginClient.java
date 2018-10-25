package de.heikocholeva.ChatServer.client.actions;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.heikocholeva.ChatServer.Main;
import de.heikocholeva.ChatServer.client.Client;
import de.heikocholeva.ChatServer.log.Log.LogLevel;
import de.heikocholeva.ChatServer.protocol.Action;
import de.heikocholeva.ChatServer.protocol.Content;
import de.heikocholeva.ChatServer.protocol.Payload;
import de.heikocholeva.ChatServer.protocol.Protocol;

public class LoginClient {
	
	public static void handle(Client client, Protocol protocol) {
		ArrayList<Content> payload = protocol.getPayload().toArrayList();
		String accessKey = payload.get(0).getValue().toString().split("-")[0].replaceAll("\"", "");
		String username = payload.get(0).getValue().toString().split("-")[1].replaceAll("\"", "");
		Main.getLogger().write(LogLevel.DEBUG, "Checking accesskey \"" + accessKey + "\" for user \"" + username + "\"");
		if(valid(username, accessKey)) {
			client.setAccessKey(accessKey);
			client.setUsername(username);
			ArrayList<Content> list = new ArrayList<>();
			list.add(new Content().setProperty("senderId").setValue(-1));
			list.add(new Content().setProperty("targetId").setValue(client.getId()));
			list.add(new Content().setProperty("content").setValue("You successfully logged in!"));
			list.add(new Content().setProperty("id").setValue(-1));
			Protocol p = new Protocol(Action.ADD_MESSAGE, new Payload(list));
			client.write(p);
			Main.getServer().clientConnected(client.getSocket(), client);
		} else {
			Protocol p = new Protocol(Action.ACCESS_KEY_IS_NOT_VALID, new Payload(new ArrayList<>()));
			client.write(p);
			try {
				Main.getServer().clientDisconnected(client.getSocket(), client);
				Main.getServer().removeClient(client.getSocket());
				client.getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	private static boolean valid(String username, String accesskey) {
		try {
			PreparedStatement statement = Main.getServer().getMySQL().getConnection().prepareStatement("SELECT id FROM users WHERE username = ? AND accessKey = ?");
			statement.setString(1, username);
			statement.setString(2, accesskey);
			ResultSet rs = Main.getServer().getMySQL().query(statement);
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
