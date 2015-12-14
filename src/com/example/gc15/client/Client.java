package com.example.gc15.client;

import java.io.*;
import java.net.*;

public class Client {
	
	private String name;
	private boolean connected;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Thread listenThread;
	
	public void setName(String name){
		this.name = name;
	}
	
	public boolean connect(String host, int port){
		//TODO implement
		return false;
	}
	
	public void disconnect(){
		//TODO implement
	}
	
	public void send(String msg){
		//TODO implement
	}

}
