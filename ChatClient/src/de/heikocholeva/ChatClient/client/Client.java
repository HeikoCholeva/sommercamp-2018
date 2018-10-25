package de.heikocholeva.ChatClient.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextArea;

import de.heikocholeva.ChatClient.Main;
import de.heikocholeva.ChatClient.log.Log.LogLevel;
import de.heikocholeva.ChatClient.protocol.Action;
import de.heikocholeva.ChatClient.protocol.Content;
import de.heikocholeva.ChatClient.protocol.Payload;
import de.heikocholeva.ChatClient.protocol.Protocol;

public class Client {
	
	private String username, accessKey, address;
	private int port;
	private boolean register;
	private Socket socket;
	private InputStream inputStream;
	private InputStreamReader inputStreamReader;
	private OutputStream outputStream;
	private PrintWriter writer;
	private JTextArea textArea;
	
	public Client(String username, String address, int port) {
		this.username = username;
		this.address = address;
		this.port = port;
		this.accessKey = null;
		this.register = true;
	}
	
	public Client(String username, String accessKey, String address, int port) {
		this.username = username;
		this.accessKey = accessKey;
		this.address = address;
		this.port = port;
		this.register = false;
	}
	
	public void connect() {
		System.out.println("address=" + this.address + ", port=" + this.port);
		try {
			this.socket = new Socket(this.address, this.port);
			this.inputStream = this.socket.getInputStream();
			this.inputStreamReader = new InputStreamReader(this.inputStream);
			this.outputStream = this.socket.getOutputStream();
			this.writer = new PrintWriter(getOutputStream());
			new Thread(new IncomingMessageHandler(getInputStreamReader())).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(this.register) {
			ArrayList<Content> list = new ArrayList<>();
			list.add(new Content().setProperty("username").setValue(getUsername()));
			Protocol protocol = new Protocol(Action.REGISTER, new Payload(list));
			write(protocol);
		} else {
			ArrayList<Content> list = new ArrayList<>();
			list.add(new Content().setProperty("accessKey").setValue(getAccessKey() + "-" + getUsername()));
			Protocol protocol = new Protocol(Action.LOGIN, new Payload(list));
			write(protocol);
		}
	}
	
	public void sendMessage(int targetId, String message) {
		ArrayList<Content> list = new ArrayList<>();
		list.add(new Content().setProperty("targetId").setValue(targetId));
		list.add(new Content().setProperty("content").setValue(MessageConverter.toConverted(message)));
		Protocol protocol = new Protocol(Action.SEND_MESSAGE, new Payload(list));
		write(protocol);
	}
	
	public void write(Protocol protocol) {
		Main.getLogger().write(LogLevel.CHAT_EVENTS, "Protocol out -> " + protocol.toString());
		try {
			this.writer.println(protocol.toString());
			this.writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getAccessKey() {
		return this.accessKey;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public InputStream getInputStream() {
		return this.inputStream;
	}
	
	public InputStreamReader getInputStreamReader() {
		return this.inputStreamReader;
	}
	
	public OutputStream getOutputStream() {
		return this.outputStream;
	}
	
	/*public void setTextArea(JTextArea ta) {
		this.textArea = ta;
	}
	
	public JTextArea getTextArea() {
		return this.textArea;
	}
	 */
}
