package com.example.gc15.server;

import java.net.*;
import java.io.IOException;

public class Server implements Runnable {
	
	private final int port;
	
	private LinkList clientList;
	private volatile boolean running;
	private ServerSocket serverSocket;

	public Server(int port){
		this.port = port;
		this.clientList = new LinkList();	
	}
	
	public synchronized void initiateShutdown() {
		ClientHandler current = clientList.getFirst();
		while (current!=null) {
			current.serverShutdown();
			current = current.getNext();
		}	
		this.running = false;
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void handleMessage(ClientHandler handler, String msg){
		ClientHandler current = clientList.getFirst();
		while (current!=null) {
			if (current!=handler) {
				current.send(msg);
			}
			current = current.getNext();
		}
	}
	
	public synchronized boolean checkName(String name) {
		ClientHandler current = clientList.getFirst();
		boolean changeOk = true;
		while (current!=null) {
			if (current.getName().equals(name)) {
				//found a matching name and returns false
				return false;
			}
			current = current.getNext();
		}
		//no match is found and returns an ok to change to that name
		return changeOk;
	}
	
	public synchronized void removeClient(ClientHandler handler){
		this.clientList.removeLink(handler);
		System.out.println("Client removed: "+handler.getName());
	}
	
	public synchronized void displayUsers(){
		this.clientList.displayList();
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			running = true;
			System.out.println("\nServer thread running!");
		}
		catch (IOException ioEx) {
			System.out.println("\nUnable to set up port!");
			System.exit(1);
		}
		
		do {
			
			try {
				Socket client = serverSocket.accept();
				System.out.println("\nNew client accepted.\n");
				ClientHandler handler = new ClientHandler(client, this);
				clientList.insertFirst(handler);
				Thread thread = new Thread(handler);
				thread.start();
			}
			catch (IOException e) {
				//e.printStackTrace();
			}

		} while(running);
		System.out.println("\nServer shutdown!\n");
	}
	
}
