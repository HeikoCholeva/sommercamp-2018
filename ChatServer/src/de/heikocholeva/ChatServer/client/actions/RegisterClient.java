package de.heikocholeva.ChatServer.client.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import de.heikocholeva.ChatServer.Main;
import de.heikocholeva.ChatServer.client.Client;
import de.heikocholeva.ChatServer.protocol.Action;
import de.heikocholeva.ChatServer.protocol.Content;
import de.heikocholeva.ChatServer.protocol.Payload;
import de.heikocholeva.ChatServer.protocol.Protocol;

public class RegisterClient {

	public static void handle(Client client, Protocol protocol) {
		String username = protocol.getPayload().toArrayList().get(0).getValue().toString().replaceAll("\"", "");
		if (!isRegistered(username)) {
			String accessKey = register(username);
			ArrayList<Content> list = new ArrayList<>();
			list.add(new Content().setProperty("senderId").setValue(-1));
			list.add(new Content().setProperty("targetId").setValue(client.getId()));
			list.add(new Content().setProperty("content").setValue("You have successfully been registered!"));
			list.add(new Content().setProperty("id").setValue(-1));
			Protocol p = new Protocol(Action.ADD_MESSAGE, new Payload(list));
			client.write(p);

			ArrayList<Content> list2 = new ArrayList<>();
			list2.add(new Content().setProperty("accessKey").setValue(accessKey));
			Protocol p2 = new Protocol(Action.SHOW_ACCESS_KEY, new Payload(list2));
			client.write(p2);
			client.setUsername(username);
			client.setAccessKey(accessKey);
			Main.getServer().clientConnected(client.getSocket(), client);
		} else {
			Protocol p = new Protocol(Action.USERNAME_ALREADY_IN_USE, new Payload(new ArrayList<>()));
			client.write(p);
		}
	}

	private static boolean isRegistered(String username) {
		try {
			PreparedStatement ps = Main.getServer().getMySQL().getConnection()
					.prepareStatement("SELECT id FROM users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = Main.getServer().getMySQL().query(ps);
			if (rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String register(String username) {
		String accessKey = generateAccessKey();
		try {
			PreparedStatement ps = Main.getServer().getMySQL().getConnection()
					.prepareStatement("INSERT INTO users (id,username,accesskey) VALUES (?,?,?)");
			ps.setInt(1, 0);
			ps.setString(2, username);
			ps.setString(3, accessKey);
			Main.getServer().getMySQL().update(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accessKey;
	}

	private static String generateAccessKey() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
