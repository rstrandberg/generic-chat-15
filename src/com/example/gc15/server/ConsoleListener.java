package com.example.gc15.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConsoleListener implements Runnable {
    private Server server;
    private volatile boolean checkLoop = false;
    private Scanner into;
    
    public ConsoleListener(Server server, Scanner input) {
    	this.server = server;
    	this.into = input;
    }
    
    public void run() {
    	String read;
    	System.out.println("Welcome...");
    	into.nextLine();
    	while(!checkLoop) {
    		try {
	            System.out.println("running1");
	            System.out.println("Type exit for terminating server: ");
	            read = into.nextLine();
	            System.out.println(read);
	            
	            if(read.equals("exit")) {
	                checkLoop = true;
	                server.initiateShutdown();
	            }else if(read.equals("users")){
	            	server.displayUsers();
	            }
	            Thread.sleep(500);
    		}
    		catch (InterruptedException ie) {
    			System.out.println(ie);
    			System.out.println("running3");
    		}
    	}
    	System.out.println("ConsoleListener thread died");
    }

	

}