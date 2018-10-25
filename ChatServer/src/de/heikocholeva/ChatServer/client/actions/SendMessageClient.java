package de.heikocholeva.ChatServer.client.actions;

import java.net.Socket;
import java.util.ArrayList;

import de.heikocholeva.ChatServer.Main;
import de.heikocholeva.ChatServer.client.Client;
import de.heikocholeva.ChatServer.log.Log.LogLevel;
import de.heikocholeva.ChatServer.protocol.Action;
import de.heikocholeva.ChatServer.protocol.Content;
import de.heikocholeva.ChatServer.protocol.Payload;
import de.heikocholeva.ChatServer.protocol.Protocol;

public class SendMessageClient {

	public static void handle(Client client, Protocol protocol) {
		System.out.println("VALUE: " + protocol.getPayload().toArrayList().get(0).getValue());
		int targetId = Integer.valueOf(protocol.getPayload().toArrayList().get(0).getValue().toString());
		String message = protocol.getPayload().toArrayList().get(1).getValue().toString();
		if(targetId == -1) {
			Main.getLogger().write(LogLevel.DEBUG, "Broadcasting message to all connected users...");
			for(Socket all : Main.getServer().getConnectedSockets()) {
				Client target = Main.getServer().getClient(all);
				Main.getLogger().write(LogLevel.DEBUG, "Broadcast-Receiver: username=\"" + target.getUsername() + "\" id=\"" + target.getId() + "\"");
				//if(target.getId() == client.getId()) continue;
				ArrayList<Content> list = new ArrayList<>();
				list.add(new Content().setProperty("senderId").setValue(client.getId()));
				list.add(new Content().setProperty("targetId").setValue(target.getId()));
				list.add(new Content().setProperty("content").setValue(message.replaceAll("\"", "")));
				list.add(new Content().setProperty("id").setValue(-1));
				Protocol p = new Protocol(Action.ADD_MESSAGE, new Payload(list));
				target.write(p);
			}
		} else if(Main.getServer().getClient(targetId) != null) {
			Client target = Main.getServer().getClient(targetId);
			
			ArrayList<Content> list = new ArrayList<>();
			list.add(new Content().setProperty("senderId").setValue(client.getId()));
			list.add(new Content().setProperty("targetId").setValue(targetId));
			list.add(new Content().setProperty("content").setValue("[%PRTCL_PRIVATE%]" + message.replaceAll("\"", "")));
			list.add(new Content().setProperty("id").setValue(-1));
			Protocol p = new Protocol(Action.ADD_MESSAGE, new Payload(list));
			target.write(p);

			ArrayList<Content> list1 = new ArrayList<>();
			list1.add(new Content().setProperty("senderId").setValue(client.getId()));
			list1.add(new Content().setProperty("targetId").setValue(client.getId()));
			list1.add(new Content().setProperty("content").setValue("[%PRTCL_PRIVATE%]" + message.replaceAll("\"", "")));
			list1.add(new Content().setProperty("id").setValue(-1));
			Protocol p1 = new Protocol(Action.ADD_MESSAGE, new Payload(list1));
			client.write(p1);
		} else {
			ArrayList<Content> list = new ArrayList<>();
			list.add(new Content().setProperty("senderId").setValue(-1));
			list.add(new Content().setProperty("targetId").setValue(client.getId()));
			list.add(new Content().setProperty("content").setValue("The specified user is not online!"));
			list.add(new Content().setProperty("id").setValue(-1));
			Protocol p = new Protocol(Action.ADD_MESSAGE, new Payload(list));
			client.write(p);
		}
	}
}
