package com.example.gc15.message;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.function.Function;

public enum Header {

		NAME_CHANGE_RESPONSE("0"),
		NAME_CHANGE_REQUEST("1"),
		CHAT_MESSAGE("2"),
		CLIENT_DISCONNECT("3"),
		SERVER_SHUTDOWN("4"),
		NAME_CHANGE_BROADCAST("5"),
		CLIENT_DISCONNECT_BROADCAST("6");
		
		private final String code;
		private final static Map<String, Header> map = stream(Header.values()).collect(toMap(Header::getCode, Function.identity()));
		
		private Header(String code){
			this.code = code;
		}
		public String getCode(){
			return this.code;
		}
		public static Header fromCode(String code){
			return map.get(code);
		}
}
