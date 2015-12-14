package com.example.gc15.server;

import java.io.*;
import java.net.*;

public class ClientHandler {
	
	private String userName;
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	private Thread listenThread;
	
	public ClientHandler(Socket clientSocket, String userName){
		//TODO implement
	}
	
	public void send(String msg){
		//TODO implement
	}
	
}
