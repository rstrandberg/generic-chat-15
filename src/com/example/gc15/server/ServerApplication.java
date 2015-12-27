package com.example.gc15.server;

import java.util.Scanner;

public class ServerApplication {
	
	public static void main(String[] args){
		
		int port = 1234;
		final Scanner input;
		final ConsoleListener console;
		input = new Scanner(System.in);
		System.out.println("Set port: ");
		port = input.nextInt();
		System.out.println("Port: " + port);
		
		Server server = new Server(port);
		
		console = new ConsoleListener(server, input);
		Thread conThread = new Thread(console);
		conThread.start();	
			
	}
	
}
