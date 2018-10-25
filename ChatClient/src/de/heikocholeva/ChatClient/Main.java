package de.heikocholeva.ChatClient;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.swing.JTextArea;

import de.heikocholeva.ChatClient.client.Client;
import de.heikocholeva.ChatClient.client.guis.LoginGUI;
import de.heikocholeva.ChatClient.log.Log;
import de.heikocholeva.ChatClient.log.Log.LogLevel;

public class Main {
	
	private static Log logger;
	private static Client client;
	public static JTextArea textArea;
	public static HashMap<String, Integer> ids = new HashMap<>();
	public static HashMap<Integer, String> usernames = new HashMap<>();
	
	public static void main(String[] args) {
		logger = new Log(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"), LogLevel.ALL);
		new LoginGUI();
	}
	
	public static Log getLogger() {
		return logger;
	}
	
	public static Client getClient() {
		return client;
	}
	
	public static void setClient(Client c) {
		client = c;
	}
}
