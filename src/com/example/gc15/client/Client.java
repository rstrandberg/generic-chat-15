package com.example.gc15.client;

import java.io.*;
import java.net.*;
import java.util.Observable;

import com.example.gc15.message.*;

public class Client extends Observable{
	
	private String name = "";
	private String oldName = "";
	private boolean connected = false;
	private boolean loggedOn = false;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Thread listeningThread;
	
	public void setName(String name){
		if(connected){
			if(loggedOn){
				if(!this.name.equals(""))
					this.oldName = this.name;
				send(Header.NAME_CHANGE_REQUEST.getCode()+"."+name);
			}
			else{
				send(Header.CLIENT_LOGON.getCode()+"."+name);
			}
		}
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	
	public boolean connect(String host, int port){
		try{
			socket = new Socket(host, port);
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch (IOException e){
			System.out.println(e+" : connect");
			return false;
		}
		loggedOn = false;
		connected = true;
		listeningThread = new Thread(new ListeningThread());
		listeningThread.start();
		return true;
	}
	
	public void disconnect(boolean notifyServer) {
		if(notifyServer)
			send(Header.CLIENT_DISCONNECT.getCode()+".QUIT");
		try{
			connected = false;
			in = null;
			out = null;
			socket.close();
		}catch (IOException e){
			System.out.println(e+" : disconnect");
		}
	}
	
	public boolean isLoggedOn(){
		return this.loggedOn;
	}
	
	private void send(String msg){
		out.println(msg);
		out.flush();
	}
	public void sendChatMessage(String msg){
		send(Header.CHAT_MESSAGE.getCode()+"."+msg);
	}
	
	private void handleMessage(String message){
		try{
			String data = "";
			switch(Message.getHeader(message)){
				case NAME_CHANGE_RESPONSE:
					if(Message.getData(message).equals("OK")){
						if(!loggedOn){
							loggedOn = true;
//							data = "Successfully logged on as "+this.name;
						}
					}
					setChanged();
					break;
				case NAME_CHANGE_REQUEST:
					if(Message.getData(message).equals("NOTOK"))
						this.name = this.oldName;
					setChanged();
					break;
				case CHAT_MESSAGE:
//					data = Message.getData(message);
					setChanged();
					break;
				case SERVER_SHUTDOWN:
					disconnect(false);
//					data = "Server shutting down";
					setChanged();
					break;
				case NAME_CHANGE_BROADCAST:
//					String[] name = Message.getData(message).split(".");
//					data = name[0]+" is now known as "+name[1];
					setChanged();
					break;
				case CLIENT_DISCONNECT_BROADCAST:
//					data = Message.getData(message) + "logged off";
					setChanged();
					break;
				case CLIENT_LOGON_BROADCAST:
//					data = Message.getData(message) + "logged on";
					setChanged();
					break;
				default:
					//Do nothing for non client relevant headers
					break;	
			}
			//Notification with message can be caught by an UI
			//implementing Observer.
			data = message;
			notifyObservers(data);
			
		}catch (MalformedMessageException e){
			//Silently drop messages with missing or unknown header
			e.printStackTrace();
		}
	}
	
	private class ListeningThread implements Runnable{
		@Override
		public void run() {
			String msg;
			try{
				while(connected){
					msg = in.readLine();
					handleMessage(msg);		
				}
			}catch (IOException e){
				System.out.println(e+" : ListeningThread");
			}		
		}
	}

}
