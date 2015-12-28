package com.example.gc15.message;

public class Message {
	
	public static Header getHeader(String message)throws MalformedMessageException{
		int index = message.indexOf('.');
		if(index == -1)
			throw new MalformedMessageException("Missing header");
		Header h  = Header.fromCode(message.substring(0, index));
		if(h == null)
			throw new MalformedMessageException("Unknown header");
		return h;
	}
	public static String getData(String message){
		return message.substring(message.indexOf('.')+1, message.length());
	}

}
