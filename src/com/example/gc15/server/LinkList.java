package com.example.gc15.server;

public class LinkList {
	
	private ClientHandler first;
	
	public LinkList() {
		this.first = null;
	}
	
	public ClientHandler getFirst() {
		return this.first;
	}
	
	public boolean isEmpty() {
		return (this.first == null);
	}
	
	public void insertFirst(ClientHandler handler) {
		handler.setNext(first);
		first = handler;
	}
	
	public void removeLink(ClientHandler handler) {
		//assumes list is not empty since there is a reference to an active ClientHandler
		ClientHandler previous, current;
		previous = first;
		current = first;
		while (current!=null) {
			if (current.equals(handler)) {
				if(current.equals(first))
					first = first.getNext();
				else
					previous.setNext(current.getNext());
				break;
			}
			previous = previous.getNext();
			current = current.getNext();
		}		
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