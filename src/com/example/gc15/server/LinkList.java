package com.example.gc15.server;

import com.example.gc15.server.ClientHandler;

public class LinkList {
	
	private ClientHandler first;
	
	public LinkList() {
		this.first = null;
	}
	
	public boolean isEmpty() {
		return (this.first == null);
	}
	
	public void insertFirst(ClientHandler handler) {
		handler.setNext(first);
		first = handler;
	}
		
	// assumes LinkList isn't empty!!!!
	public ClientHandler deleteFirst() {
		ClientHandler temp = first;
		first = first.getNext();
		return temp;
	}
	
	public void displayList() {
		System.out.println("First --> Last");
		ClientHandler current = first;
		while(current != null) {
			current.display();
			current = current.getNext();
		}
		System.out.println("");
	}
		
	// clear the list, leave all links to garbage collector
	public void clearList() {
		this.first = null;
	}	
	
}