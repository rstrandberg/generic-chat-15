package com.example.gc15.server;

import java.io.*;
import java.net.*;

import com.example.gc15.message.*;

public class ClientHandler implements Runnable{
	
	private String userName = ""; //nullpointer in checkName if not initialized
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	private ClientHandler next;
	private Server server;
	private boolean firstAppearance;
	private volatile boolean connected;
	
	public ClientHandler(Socket clientSocket, Server server){
		this.clientSocket = clientSocket;
		this.server = server;
		this.firstAppearance = true;
		this.connected = true;
		try {
			in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
	        out = new PrintWriter(this.clientSocket.getOutputStream(), true);
		}
		catch(IOException ioEx) {
			ioEx.printStackTrace();
		}
	}
	
	public void send(String msg){
		out.println(msg);
		out.flush();
	}
	
	public void display() {
		System.out.print("This thread handles communication with " + this.userName);
	}
	
	public String getName() {
		return this.userName;
	}
	
	private void setName(String name) {
		this.userName = name;
	}
	
	public ClientHandler getNext() {
		return this.next;
	}
	
	public void setNext(ClientHandler next) {
		this.next = next;
	}
	
	public void serverShutdown() {
		//telling client that server is shutting down then removing the thread
		try {
			this.send(Header.SERVER_SHUTDOWN.getCode()+".OK");
			this.connected = false;
			this.clientSocket.close();
			this.server.removeClient(this);
		
		} 
		catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void handleMessage(String message){
		try{
			String data = "";
			if (this.firstAppearance) {
				switch(Message.getHeader(message)){
					case CLIENT_LOGON:
						data = Message.getData(message);
						
						if (server.checkName(data)) {
							this.setName(data);
							this.send(Header.NAME_CHANGE_RESPONSE.getCode()+".OK");
							this.server.handleMessage(this, Header.CLIENT_LOGON_BROADCAST.getCode() + "." + this.getName());
							this.firstAppearance = false;
						}
						else {
							this.send(Header.NAME_CHANGE_REQUEST.getCode()+".NOTOK");
						}						
						break;

					default:
						//Do nothing if client not proper
						break;	
				}
			}
			else {		
				
				switch(Message.getHeader(message)){
					case NAME_CHANGE_REQUEST:
						data = Message.getData(message);
						//receives a request to change or set username, check the used names and if its AN ok username is set and an NAME_CHANGE_REQUEST OK 
						//is sent back and its broadcasted that a certan username is changed to another
						//if name is already used a NAME_CHANGE_REQUEST NOTOK is sent back and client have to try finding another name
						
						if (server.checkName(data)) {
							String oldName = getName();
							this.setName(data);
							this.send(Header.NAME_CHANGE_RESPONSE.getCode()+".OK");
//							this.server.handleMessage(this, Header.NAME_CHANGE_BROADCAST.getCode()+ oldName + " changed name to " + this.getName());
							this.server.handleMessage(this, Header.NAME_CHANGE_BROADCAST.getCode()+ oldName + "." + this.getName());
						}
						else {
							this.send(Header.NAME_CHANGE_REQUEST.getCode()+".NOTOK");
						}
						break;
					case CHAT_MESSAGE:
						data = Message.getData(message);
						this.server.handleMessage(this, Header.CHAT_MESSAGE.getCode() + "." + this.getName() + " " + data);			
						break;					
					case CLIENT_DISCONNECT:
						//data = this.getName() + " is logged off";
						this.server.handleMessage(this, Header.CLIENT_DISCONNECT_BROADCAST.getCode() + "." + this.getName());
						this.server.removeClient(this);					
						break;			
					default:
						//Do nothing for non server relevant headers
						break;	
			
				}
			}
		}
		catch (MalformedMessageException e){
			//Silently drop messages with missing or unknown header
			e.printStackTrace();
		}
	}
	
	
	public void run() {
		
		String msg;
		try {
			while(connected){
				msg = in.readLine();
				this.handleMessage(msg);		
			}
		}
		catch(IOException ioEx) {
			System.out.println("Unable to disconnect!");
		}
	
	}
	
}
