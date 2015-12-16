package com.example.gc15.client;

import java.io.*;
import java.net.*;

public class Client {
	
	private String name;
	private boolean connected;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Thread listeningThread;
	
	public void setName(String name){
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
		connected = true;
		listeningThread = new Thread(new ListeningThread());
		listeningThread.start();
		return true;
	}
	
	public void disconnect() {
		try{
			connected = false;
			in = null;
			out = null;
			socket.close();
		}catch (IOException e){
			System.out.println(e+" : disconnect");
		}
	}
	
	public void send(String msg){
		out.println(msg);
		out.flush();
	}
	
	private class ListeningThread implements Runnable{
		@Override
		public void run() {
			String msg;
			try{
				while(connected){
					msg = in.readLine();
					
					//TODO add message handling logic
					
				}
			}catch (IOException e){
				System.out.println(e+" : ListeningThread");
			}		
		}
	}

}
