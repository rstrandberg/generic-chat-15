package com.example.gc15.server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConsoleListener implements Runnable {
    private DataInputStream in;
    private Server server;
    private volatile boolean checkLoop = false;
    private Scanner into;
    
    public ConsoleListener(Server server, Scanner input) {
    	this.server = server;
    	this.into = input;
    }
    
    public void run() {
    	//Scanner into = new Scanner(System.in);
    	//BufferedReader into = new BufferedReader(new InputStreamReader(System.in));
    	String read;
    	System.out.println("Welcome...");
    	into.nextLine();
    	while(!checkLoop) {
    		try {
	            System.out.println("running1");
	            System.out.println("Type exit for terminating server: ");
	    //        read = into.readLine();
	            read = into.nextLine();
	            System.out.println(read);
	            
	            if(read.equals("exit")) {
	                checkLoop = true;
	                server.initiateShutdown();
	            }
	            Thread.sleep(500);
    		}
    		/*
    		catch(IOException e) {
    			System.out.println(e);
    			System.out.println("running2");
    		}
    		*/
    		catch (InterruptedException ie) {
    			System.out.println(ie);
    			System.out.println("running3");
    		}
    	}
    	System.out.println("running4");
    }

	

}