package de.heikocholeva.ChatClient.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import de.heikocholeva.ChatClient.Main;
import de.heikocholeva.ChatClient.log.Log.LogLevel;
import de.heikocholeva.ChatClient.protocol.Protocol;

public class IncomingMessageHandler implements Runnable {

	private BufferedReader bufferedReader;
	
	public IncomingMessageHandler(InputStreamReader input) {
		this.bufferedReader = new BufferedReader(input);
	}
	
	@Override
	public void run() {
		try {
			String message;
			while((message = this.bufferedReader.readLine()) != null) {
				protocolToMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void protocolToMessage(String prtcl) {
		Protocol protocol = new Protocol(prtcl);
		Main.getLogger().write(LogLevel.DEBUG, "Protocol in -> " + protocol.toString());
		switch (protocol.getAction()) {
		case ACCESS_KEY_IS_NOT_VALID:
			Main.textArea.append("[Error]: The appended access key is not valid\n");
			break;
		case ADD_MESSAGE:
			int idUsr = Integer.valueOf(protocol.getPayload().toArrayList().get(0).getValue().toString());
			String message = protocol.getPayload().toArrayList().get(2).getValue().toString().replaceAll("\"", "");
			String sName;
			if(idUsr == -1) {
				sName = "Server";
			} else {
				if(Main.usernames.containsKey(idUsr)) {
					sName = Main.usernames.get(idUsr);
				} else {
					sName = "Currently Unknown";
				}
			}
			String converted = MessageConverter.toMessage(message);
			if(converted.startsWith("%PRTCL_PRIVATE%")) {
				Main.textArea.append("[PRIVATE]: " + sName + " > " + converted.replaceFirst("%PRTCL_PRIVATE%", "") + "\n");
			} else {
				Main.textArea.append(sName + " > " + converted + "\n");	
			}
			break;
		case ADD_USER:
			String usernameAdd = protocol.getPayload().toArrayList().get(0).getValue().toString().replaceAll("\"", "");
			int idAdd = Integer.valueOf(protocol.getPayload().toArrayList().get(1).getValue().toString());
			Main.ids.put(usernameAdd, idAdd);
			Main.usernames.put(idAdd, usernameAdd);
			Main.textArea.append("[Info]: " + usernameAdd + "(" + idAdd + ") connected to this server\n");
			break;
		case REMOVE_USER:
			//String usernameRem = protocol.getPayload().toArrayList().get(0).getValue().toString();
			int idRem = Integer.valueOf(protocol.getPayload().toArrayList().get(0).getValue().toString());
			String usernameRem = Main.usernames.get(idRem);
			Main.ids.remove(usernameRem);
			Main.usernames.remove(idRem);
			Main.textArea.append("[Info]: " + usernameRem + "(" + idRem + ") disconnected from this server\n");
			break;
		case SHOW_ACCESS_KEY:
			Main.textArea.append("[Info]: Following you can see your private access key \"" + protocol.getPayload().toArrayList().get(0).getValue().toString() + "\"\n");
			break;
		case USERNAME_ALREADY_IN_USE:
			Main.textArea.append("[Error]: The appended username is already registered\n");
			break;
		default:
			break;
		}
	}

}
