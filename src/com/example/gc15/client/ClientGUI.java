package com.example.gc15.client;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

public class ClientGUI implements Observer{

	private Client client;
	
	private JFrame chatFrame;
	
	private JTextArea chatArea;
	private JButton sendButton;
	private JButton nameButton;
	private JButton connectButton;
	private JButton disconnectButton;
	private JTextArea chatInput;
	private JTextField nameInput;
	private JTextField hostInput;
	private JTextField portInput;
	
	private JPanel mainPanel;
	private JPanel topPanel;
	
	private ActionListener buttonListener;
	private Action sendAction;
	
	public ClientGUI(Client client){
		this.client = client;
		this.client.addObserver(this);
		initialize();
	}
	
	private void initialize(){
		chatFrame = new JFrame();
		chatFrame.setTitle("Generic Chat 15");
		chatFrame.setResizable(false);
		chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = constructMainPanel();
		chatFrame.add(mainPanel);
		chatFrame.pack();
		createButtonListener();
		createSendAction();
		addListeners();
		chatFrame.setVisible(true);
	}
	
	private JPanel constructMainPanel(){
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		topPanel = new JPanel(new CardLayout());
		topPanel.add(constructConnectionPanel(), "connect");
		topPanel.add(constructNamePanel(), "name");
		p.add(topPanel, BorderLayout.NORTH);
		p.add(constructChatPanel(), BorderLayout.CENTER);
		p.add(constructInputPanel(), BorderLayout.SOUTH);
		return p;
	}
	
	private JPanel constructConnectionPanel(){
		JPanel p = new JPanel();
		JLabel host = new JLabel("Host");
		JLabel port = new JLabel("Port");
		hostInput = new JTextField();
		portInput = new JTextField();
		hostInput.setPreferredSize(new Dimension(100,25));
		portInput.setPreferredSize(new Dimension(50,25));
		connectButton = new JButton("Connect");
		connectButton.setActionCommand("connect");
		p.add(host);
		p.add(hostInput);
		p.add(port);
		p.add(portInput);
		p.add(connectButton);
		return p;
	}
	private JPanel constructNamePanel(){
		JPanel p = new JPanel();
		nameInput = new JTextField();
		nameInput.setPreferredSize(new Dimension(150,30));
		JLabel name = new JLabel("Name");
		name.setPreferredSize(new Dimension(40,30));
		nameButton = new JButton("Change");
		nameButton.setActionCommand("name");
		disconnectButton = new JButton("Disconnect");
		disconnectButton.setActionCommand("disconnect");
		p.add(name);
		p.add(nameInput);
		p.add(nameButton);
		p.add(disconnectButton);
		return p;
	}
	
	private JPanel constructInputPanel(){
		JPanel p = new JPanel();
		chatInput = new JTextArea();
		chatInput.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret)chatInput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scp = new JScrollPane(chatInput);
		scp.setPreferredSize(new Dimension(300,50));
		sendButton = new JButton("Send");
		sendButton.setActionCommand("send");
		sendButton.setPreferredSize(new Dimension(100,50));
		p.add(scp);
		p.add(sendButton);
		return p;
	}
	
	private JPanel constructChatPanel(){
		JPanel p = new JPanel();
		chatArea =  new JTextArea();
		chatArea.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret)chatArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatArea.setEditable(false);
		JScrollPane scp = new JScrollPane(chatArea);
		scp.setPreferredSize(new Dimension(400,400));
		p.add(scp);
		return p;
	}
	
	private void addListeners(){
		sendButton.addActionListener(buttonListener);
		nameButton.addActionListener(buttonListener);
		connectButton.addActionListener(buttonListener);
		disconnectButton.addActionListener(buttonListener);
		chatInput.getActionMap().put(chatInput.getInputMap().get(KeyStroke.getKeyStroke("ENTER")), sendAction);
	}
	
	private void createButtonListener(){
		buttonListener = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				switch(e.getActionCommand()){
					case "connect":
						String host = hostInput.getText();
						int port = Integer.parseInt(portInput.getText());
						if(client.connect(host, port)){
							((CardLayout)topPanel.getLayout()).show(topPanel, "name");
							System.out.println("test");
						}
						break;
					case "disconnect":
						((CardLayout)topPanel.getLayout()).show(topPanel, "connect");
						client.disconnect(true);
						break;
					case "send":
						if(client.isLoggedOn()){
							client.sendChatMessage(chatInput.getText());
							chatArea.append(client.getName()+": "+chatInput.getText()+"\n");
							chatInput.setText("");
						}
						break;
					case "name":
						client.setName(nameInput.getText());
						break;
				}
			}
		};
	}
	private void createSendAction(){
		sendAction = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(client.isLoggedOn()){
					client.sendChatMessage(chatInput.getText());
					chatArea.append(client.getName()+": "+chatInput.getText()+"\n");
					chatInput.setText("");
				}	
			}		
		};
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		chatArea.append(arg1.toString()+"\n");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI(new Client());
//					window.chatFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
