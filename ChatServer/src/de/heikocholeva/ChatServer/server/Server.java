package de.heikocholeva.ChatServer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import de.heikocholeva.ChatServer.Main;
import de.heikocholeva.ChatServer.client.Client;
import de.heikocholeva.ChatServer.client.ClientThread;
import de.heikocholeva.ChatServer.log.Log.LogLevel;
import de.heikocholeva.ChatServer.mysql.MySQL;
import de.heikocholeva.ChatServer.protocol.Action;
import de.heikocholeva.ChatServer.protocol.Content;
import de.heikocholeva.ChatServer.protocol.Payload;
import de.heikocholeva.ChatServer.protocol.Protocol;
import de.heikocholeva.ChatServer.protocol.ProtocolHandler;

public class Server {
	
	private ServerSocket serverSocket;
	private int port;
	private ProtocolHandler protocolHandler;
	private MySQL mysql;
	private HashMap<Socket, Client> clients;
	private HashMap<Socket, Client> registeredClients;
	private HashMap<Integer, Client> clientById;
	private ArrayList<String> sockets;
	private int clientCount = 0;
	
	public Server(int port, ProtocolHandler protocolHandler, MySQL mysql) {
		this.port = port;
		this.protocolHandler = protocolHandler;
		this.clients = new HashMap<>();
		this.registeredClients = new HashMap<>();
		this.clientById = new HashMap<>();
		this.sockets = new ArrayList<>();
		this.mysql = mysql;
		Main.getLogger().write(LogLevel.DEBUG, "Initialized a new ChatServer on port " + this.port + ".");
	}
	
	public void start() throws IOException {
		Main.getLogger().write(LogLevel.DEBUG, "Starting the ChatServer...");
		mysql.connect();
		serverSocket = new ServerSocket(this.port);
		while(true) {
			Socket socket = serverSocket.accept();
			Client client = new Client(socket);
			addClient(socket, client);
			new ClientThread(socket).start();
		}
	}
	
	public int getPort() {
		return this.port;
	}
	
	public ProtocolHandler getProtocolHandler() {
		return this.protocolHandler;
	}

	public void addClient(Socket socket, Client client) {
		if(!sockets.contains(socket.getInetAddress().toString())) {
			clients.put(socket, client);
			clientById.put(Integer.valueOf(client.getId()), client);
			Main.getLogger().write(LogLevel.DEBUG, "A client was added (" + socket.getInetAddress().toString() + " : " + socket.getPort() + ") with id " + client.getId());
		} else {
			Main.getLogger().write(LogLevel.DEBUG, "An already existing client tried to connect (" + socket.getInetAddress().toString() + " : " + socket.getPort() + ")");
		}
	}
	
	public void clientConnected(Socket socket, Client client) {
		this.registeredClients.put(socket, client);
		ArrayList<Content> list = new ArrayList<>();
		list.add(new Content().setProperty("username").setValue(client.getUsername()));
		list.add(new Content().setProperty("id").setValue(client.getId()));
		Protocol protocol = new Protocol(Action.ADD_USER, new Payload(list));
		writeAll(protocol);
		informAboutCurrentlyConnected(client);
	}
	
	private void informAboutCurrentlyConnected(Client client) {
		for(Socket all : registeredClients.keySet()) {
			if(all != client.getSocket()) {
				ArrayList<Content> list = new ArrayList<>();
				list.add(new Content().setProperty("username").setValue(getClient(all).getUsername()));
				list.add(new Content().setProperty("id").setValue(getClient(all).getId()));
				Protocol protocol = new Protocol(Action.ADD_USER, new Payload(list));
				client.write(protocol);
			}
		}
	}
	
	public void clientDisconnected(Socket socket, Client client) {
		try {
			this.registeredClients.remove(socket);
			ArrayList<Content> list = new ArrayList<>();
			//list.add(new Content().setProperty("username").setValue(client.getUsername()));
			list.add(new Content().setProperty("id").setValue(client.getId()));
			Protocol protocol = new Protocol(Action.REMOVE_USER, new Payload(list));
			writeAll(protocol);	
		} catch(NullPointerException e) {
			Main.getLogger().write(LogLevel.DEBUG, "NullPointer @ clientDisconnect ~ Server.java");
		}
	}
	
	public void writeAll(Protocol protocol) {
		for(Socket all : registeredClients.keySet()) {
			getClient(all).write(protocol);
		}
	}
	
	public void removeClient(Socket socket) {
		Main.getLogger().write(LogLevel.DEBUG, "A client was removed (" + socket.getInetAddress().toString() + " : " + socket.getPort() + ")");
		sockets.remove(socket.getInetAddress().toString());
		clientById.remove(clients.get(socket).getId());
		clients.remove(socket);
	}
	
	public Client getClient(Socket socket) {
		return clients.get(socket);
	}
	
	public Client getClient(int id) {
		return clientById.get(id);
	}
	
	public int getClientCount() {
		return this.clientCount;
	}
	
	public void setClientCount(int count) {
		this.clientCount = count;
	}
	
	public Set<Socket> getConnectedSockets() {
		return clients.keySet();
	}
	
	public MySQL getMySQL() {
		return this.mysql;
	}
}
