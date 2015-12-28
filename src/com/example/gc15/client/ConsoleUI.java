package com.example.gc15.client;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class ConsoleUI implements Observer, Runnable{
	
	private Client client;
	private Scanner in;
	private boolean running;

	public ConsoleUI(Client client) {
		this.client = client;
		client.addObserver(this);
		this.in = new Scanner(System.in);
	}
	
	private void parseCommand(String[] arg){
		switch (arg[0]){
			case "/quit":
				running = false;
				break;
			case "/connect":
				System.out.println(arg[1]+" "+arg[2]);
				if(client.connect(arg[1], Integer.parseInt(arg[2])))
					System.out.println("Connected to server");
				else
					System.out.println("Failed to connect");
				break;
			case "/disconnect":
				client.disconnect(true);
				break;
			case "/setname":
				client.setName(arg[1]);
				break;
			case "/getname":
				System.out.println(client.getName());
				break;
			default:
				System.out.println("Unknown command");
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(arg.toString());	
	}

	@Override
	public void run() {
		String line = "";
		System.out.println("Welcome to Generic Chat 15");
		running = true;
		do{
			line = in.nextLine();
			if(line.charAt(0) == '/')
				parseCommand(line.split(" "));
			else
				client.sendChatMessage(line);
		}while(running);
	}
	
	public static void main(String[] args) {
		ConsoleUI ui = new ConsoleUI(new Client());
		ui.run();
	}

	/* Command format
	 * 
	 * 		/connect ip port
	 * 		/setname name
	 * 		/getname
	 * 		/disconnect
	 * 		/quit
	 */
}
