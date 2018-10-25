package de.heikocholeva.ChatServer.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import de.heikocholeva.ChatServer.Main;
import de.heikocholeva.ChatServer.log.Log.LogLevel;
import de.heikocholeva.ChatServer.protocol.Protocol;

public class Client {
	
	private Socket socket;
	boolean loggedin;
	private int id;
	private String username, accessKey;
	
	public Client(Socket socket) {
		this.username = "UnnamedGuest";
		this.accessKey = "";
		this.socket = socket;
		this.loggedin = false;
		this.id = Main.getServer().getClientCount()+1;
		Main.getServer().setClientCount(this.id);
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public boolean isLoggedin() {
		return this.loggedin;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getAccesskey() {
		return this.accessKey;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void write(Protocol protocol) {
		PrintWriter pw;
		try {
			Main.getLogger().write(LogLevel.DEBUG, "OUT -> to: " + getSocket().getInetAddress().toString() + " protocol:" + protocol.toString());
			//System.out.println("OUT -> to: " + getSocket().getInetAddress().toString() + " protocol:" + protocol.toString());
			pw = new PrintWriter(getSocket().getOutputStream());
			pw.println(protocol.toString());
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
