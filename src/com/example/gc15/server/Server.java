package com.example.gc15.server;

import java.net.*;
import java.io.IOException;
import java.net.*;

public class Server implements Runnable {
	
	private static int PORT;
	
	private LinkList clientList;
	private volatile boolean running;
	private static ServerSocket serverSocket;
	private Thread serverThread;

	public Server(int port){
		this.PORT = port;
		this.clientList = new LinkList();	
	}
	
	public void initiateShutdown() {
		this.running = false;
		
	}
	
	/*
	public void setPort(int port){
		//TODO implement
	}
	
	public void sendToAll(String msg){
		//TODO implement
	}
	
	public void start(){
		//TODO implement
	}
	
	public void stop(){
		//TODO implement
	}
	
	public void handleMessage(String sender, String msg){
		//TODO implement
	}
	
	public void removeClient(String name){
		//TODO implement
	}
	*/
	
	public void run() {
		try {
			serverSocket = new ServerSocket(PORT);
			running = true;
		}
		catch (IOException ioEx) {
			System.out.println("\nUnable to set up port!");
			System.exit(1);
		}
		
		do {
			
			try {
				Socket client = serverSocket.accept();
				System.out.println("\nNew client accepted.\n");
				ClientHandler handler = new ClientHandler(client);
				clientList.insertFirst(handler);
				Thread thread = new Thread(handler);
				thread.start();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		} while(running);
		System.out.println("\nServer shutdown!\n");
	}
	
}
