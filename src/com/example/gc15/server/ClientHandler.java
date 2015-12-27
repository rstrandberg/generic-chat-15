package com.example.gc15.server;

import java.io.*;
import java.net.*;

import com.example.gc15.server.ClientHandler;
import com.example.main.Link;

public class ClientHandler implements Runnable{
	
	private String userName;
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	private Thread listeningThread;
	private ClientHandler next;
	
	
	public ClientHandler(Socket clientSocket){
		this.clientSocket = clientSocket;
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
	}
	
	public void display() {
		System.out.print("This thread handles communication with " + this.userName);
	}
	
	public ClientHandler getNext() {
		return this.next;
	}
	
	public void setNext(ClientHandler next) {
		this.next = next;
	}
	
	public void run() {
		
		String received;
		try {
			do {		
				received = in.readLine();
				out.println("ECHO: " + received);
				
			} while (!received.equals("QUIT"));
			
			if(this.clientSocket!=null) {
				System.out.println("Closing down connection...");
				this.clientSocket.close();
			}
		}
		catch(IOException ioEx) {
			System.out.println("Unable to disconnect!");
		}
	
	}
	
}
