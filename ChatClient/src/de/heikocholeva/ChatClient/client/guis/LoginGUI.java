package de.heikocholeva.ChatClient.client.guis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.heikocholeva.ChatClient.Main;
import de.heikocholeva.ChatClient.client.Client;

public class LoginGUI extends JFrame implements ActionListener {

	JLabel labelAddress, labelUsername, labelPassword;
	JTextField textAddress, textUsername, textPassword;
	JButton buttonLogin, buttonRegister;
	ChatGUI chatGui;
	
	public LoginGUI() {
		chatGui = new ChatGUI();
		Main.textArea = chatGui.textArea;
		//Main.getClient().setTextArea(chatGui.textArea);
		/* LABELS */
		labelAddress = new JLabel("Please enter the destination address with port (IP_ADDRESS:PORT)");
		labelUsername = new JLabel("Please enter your Username");
		labelPassword = new JLabel("Please enter your Access Key, if you are already registered");
		
		/* TEXTFIELDS */
		textAddress = new JTextField(20);
		textUsername = new JTextField(20);
		textPassword = new JTextField(20);
		
		/* BUTTONS */
		buttonLogin = new JButton("Login");
		buttonRegister = new JButton("Register");
		
		/* ADDING ACTION LISTENER */
		textAddress.addActionListener(this);
		textUsername.addActionListener(this);
		textPassword.addActionListener(this);
		buttonLogin.addActionListener(this);
		buttonRegister.addActionListener(this);
		
		/* SETTING BOUNDS */
		labelAddress.setBounds(10, 10, 500, 30);
		textAddress.setBounds(10, 50, 500, 30);
		
		labelUsername.setBounds(10, 90, 500, 30);
		textUsername.setBounds(10, 130, 500, 30);
		
		labelPassword.setBounds(10, 170, 500, 30);
		textPassword.setBounds(10, 210, 500, 30);
		
		buttonLogin.setBounds(10, 250, 500, 30);
		buttonRegister.setBounds(10, 290, 500, 30);
		
		/* SETTINGS INPUT DEFAULTS */
		textAddress.setText("heikocholeva.de:1234");
		textUsername.setText("Guest");
		textPassword.setText("");
		
		/* INPUT FOCUS LISTENER s*/
		textAddress.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			@Override
			public void focusGained(FocusEvent e) {textAddress.selectAll();}
		});
		textUsername.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			@Override
			public void focusGained(FocusEvent e) {textUsername.selectAll();}
		});
		textPassword.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {}
			@Override
			public void focusGained(FocusEvent e) {textPassword.selectAll();}
		});
		
		/* ADDING COMPONENTS */
		add(labelAddress);
		add(labelUsername);
		add(labelPassword);
		add(textAddress);
		add(textUsername);
		add(textPassword);
		add(buttonLogin);
		add(buttonRegister);
		
		/* WINDOW LISTENER */
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		
		/* SETTINGS */
		setResizable(false);
		setTitle("Chat Client");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(520, 370);
		setLayout(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == textAddress || e.getSource() == textUsername || e.getSource() == textAddress) {
			System.out.println("1");
			if(isValid(textAddress.getText()) && textUsername.getText().length() >= 3 && textUsername.getText().length() <= 25 && textPassword.getText() != "") {
				System.out.println("2");
				/* LOGIN */
				login(textAddress.getText(), textUsername.getText(), textPassword.getText());
			} else if(isValid(textAddress.getText()) && textUsername.getText().length() >= 3 && textUsername.getText().length() <= 25 && textPassword.getText() == "") {
				System.out.println("3");
				/* REGISTER */
				register(textAddress.getText(), textUsername.getText());
			}
		} else if(e.getActionCommand().equals("Login")) {
			System.out.println("4");
			if(isValid(textAddress.getText()) && textUsername.getText().length() >= 3 && textUsername.getText().length() <= 25 && textPassword.getText() != "") {
				/* LOGIN */
				System.out.println("5");
				login(textAddress.getText(), textUsername.getText(), textPassword.getText());
			}
		} else if(e.getActionCommand().equals("Register")) {
			System.out.println("6");
			if(isValid(textAddress.getText()) && textUsername.getText().length() >= 3 && textUsername.getText().length() <= 25) {
				/* REGISTER */
				System.out.println("7");
				register(textAddress.getText(), textUsername.getText());
			}
		}
	}
	
	private void register(String fullAddress, String username) {
		System.out.println("8");
		Main.setClient(new Client(username, fullAddress.split(":")[0], Integer.valueOf(fullAddress.split(":")[1])));
		Main.getClient().connect();
		setVisible(false);
		//new ChatGUI();
		chatGui.setVisible(true);
	}
	
	private void login(String fullAddress, String username, String accessKey) {
		System.out.println("9");
		Main.setClient(new Client(username, accessKey, fullAddress.split(":")[0], Integer.valueOf(fullAddress.split(":")[1])));
		Main.getClient().connect();
		setVisible(false);
		//new ChatGUI();
		chatGui.setVisible(true);
	}
	
	private boolean isValid(String address) {
		if(address.contains(":") && address.split(":").length > 1 && address.split(":").length < 3 && isNumeric(address.split(":")[1]) && String.valueOf(address).split(":")[0] != "") 
			return true;
		return false;
	}
	
	private boolean isNumeric(String str) {  
		try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(str);  
		} catch(NumberFormatException e) {  
			return false;
		}  
		return true;  
	}
}
