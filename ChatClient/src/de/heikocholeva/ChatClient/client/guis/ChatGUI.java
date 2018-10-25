package de.heikocholeva.ChatClient.client.guis;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.heikocholeva.ChatClient.Main;

public class ChatGUI extends JFrame implements ActionListener {

	JTextArea textArea;
	JTextField textField;
	JButton button;
	JScrollPane scroll;
	
	public ChatGUI() {
		textField = new JTextField(20);
		button = new JButton("Submit");
		textArea = new JTextArea(10, 10);
		scroll = new JScrollPane(textArea);
		
		scroll.setBounds(5, 5, 840, 400);
		textField.setBounds(5, 410, 680, 25);
		button.setBounds(690, 410, 155, 25);
		
		textArea.setMaximumSize(new Dimension(840, 400));
		textField.addActionListener(this);
		button.addActionListener(this);
		scroll.setAutoscrolls(true);
		
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textField.setText("Your message here...");
		textField.selectAll();
		
		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
		});
		
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				textField.selectAll();
			}
		});
		
		add(scroll);
		//add(textArea);
		add(textField);
		add(button);
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		
		setResizable(false);
		setTitle("Chat Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(850, 480);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(false); // true
		
		textField.requestFocus();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String msg = textField.getText();
		if(msg.startsWith("@")) {
			String targetStr = msg.split(" ")[0].replaceFirst("@", "");
			if(isNumeric(targetStr)) {
				int targetId = Integer.valueOf(targetStr);
				textField.setText("");
				Main.getClient().sendMessage(targetId, "%PRTCL_PRIVATE%" + msg.replaceFirst("@" + targetId + " ", ""));
			} else {
				if(Main.ids.containsKey(targetStr)) {
					int targetId = Integer.valueOf(Main.ids.get(targetStr));
					textField.setText("");
					Main.getClient().sendMessage(targetId, "%PRTCL_PRIVATE%" + msg.replaceFirst("@" + targetStr + " ", ""));
				} else {
					Main.textArea.append("[Info] > The requested user \"" + targetStr + "\" is not available! \n");
				}
			}
		} else {
			textField.setText("");
			Main.getClient().sendMessage(-1, msg);
		}
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
