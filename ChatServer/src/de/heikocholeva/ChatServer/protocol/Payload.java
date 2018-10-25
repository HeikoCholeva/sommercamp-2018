package de.heikocholeva.ChatServer.protocol;

import java.util.ArrayList;

public class Payload {
	
	private String payloadString;
	private ArrayList<Content> payload;
	
	public Payload(String payloadString) {
		this.payloadString = payloadString;
		makeArrayList();
	}
	
	public Payload(ArrayList<Content> payload) {
		this.payload = payload;
		makeString();
	}
	
	public String toString() {
		return this.payloadString;
	}
	
	public ArrayList<Content> toArrayList() {
		return this.payload;
	}
	
	private void makeString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < this.payload.size(); i++) {
			if(i >= 1) {
				sb.append(",");
			}
			sb.append(payload.get(i).toString());
		}
		this.payloadString = sb.toString();
	}
	
	private void makeArrayList() {
		this.payload = new ArrayList<>();
		String[] str = this.payloadString.split(",");
		for(int i = 0; i < str.length; i++) {
			Object[] s = str[i].split(":");
			this.payload.add(new Content().setProperty(s[0].toString()).setValue(s[1]));
		}
	}

}
