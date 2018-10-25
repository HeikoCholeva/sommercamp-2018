package de.heikocholeva.ChatServer;

import java.io.IOException;
import java.text.SimpleDateFormat;

import de.heikocholeva.ChatServer.log.Log;
import de.heikocholeva.ChatServer.log.Log.LogLevel;
import de.heikocholeva.ChatServer.mysql.MySQL;
import de.heikocholeva.ChatServer.protocol.ProtocolHandler;
import de.heikocholeva.ChatServer.server.Server;

public class Main {
	
	private static Server server;
	private static Log logger;
	
	public static void main(String[] args) {
		logger = new Log(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"), LogLevel.ALL);
		server = new Server(1234, new ProtocolHandler(), new MySQL("mysql.heikocholeva.de", 3306, "sommercamp", "sommercamp", "Fq8bGgStDxnT4KA4"));
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Server getServer() {
		return server;
	}
	
	public static Log getLogger() {
		return logger;
	}

}
