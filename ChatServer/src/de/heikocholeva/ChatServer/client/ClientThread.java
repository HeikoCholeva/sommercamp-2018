package de.heikocholeva.ChatServer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import de.heikocholeva.ChatServer.Main;
import de.heikocholeva.ChatServer.log.Log.LogLevel;
import de.heikocholeva.ChatServer.protocol.Protocol;

public class ClientThread extends Thread {
	
	private Socket socket;
	
	public ClientThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		while(!socket.isClosed()) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String protocol = br.readLine();
				Main.getLogger().write(LogLevel.DEBUG, "IN -> client=\"" + socket.getInetAddress().toString() + "\" protocol=\"" + protocol + "\"");
				//System.out.println("IN -> client=\"" + socket.getInetAddress().toString() + "\" protocol=\"" + protocol + "\"");
				Main.getServer().getProtocolHandler().fromClient(Main.getServer().getClient(socket), new Protocol(protocol));
			} catch(SocketException | NullPointerException e) {
				// TODO: disconnect socket
				Main.getServer().clientDisconnected(socket, Main.getServer().getClient(socket));
				Main.getServer().removeClient(socket);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
