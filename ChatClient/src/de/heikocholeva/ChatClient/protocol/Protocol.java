package de.heikocholeva.ChatClient.protocol;

import java.util.ArrayList;

public class Protocol {
	
	private Action action;
	private String protocol;
	private Payload payload;
	
	public Protocol(Action addMessage, Payload payload) {
		this.action = addMessage;
		this.payload = payload;
		makeString();
	}
	
	public Protocol(String protocol) {
		this.protocol = protocol;
		makeProtocol();
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public Payload getPayload() {
		return this.payload;
	}
	
	public String toString() {
		return this.protocol;
	}
	
	private void makeString() {
		this.protocol = "{\"type\":\"" + getAction().name() + "\",\"payload\"[" + getPayload().toString() + "]}";
	}
	
	private void makeProtocol() {
		System.out.println("Creating protocol from " + this.protocol);
		String[] str = this.protocol.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\[", "").replaceAll("\\]", "").split("payload");
		String action = str[0].replaceAll(",", "").replaceAll("\"", "").replaceAll("type:", "").replace("action:", "");
		this.action = Action.valueOf(action);
		System.out.println("str[1]: " + str[1]);
		if(str[1].equals("\"")) {
			System.out.println("PAYLOAD_EMPTY");
			this.payload = new Payload(new ArrayList<>());
		} else {
			String payload = str[1].replaceFirst("\"", "");
			System.out.println("PAYLOAD: \"" + payload + "\"");
			this.payload = new Payload(payload);
		}
	}

}
