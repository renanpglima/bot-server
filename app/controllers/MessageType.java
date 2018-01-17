package controllers;

public enum MessageType {
	STORE,
	MOVIE,
	UNKNOWN;

	public static MessageType getTypeByText(String message) {
		
		if (message.contains("filme")) {
			return MOVIE;
		}else if (message.contains("loja")) {
			return STORE;
		}
		
		return UNKNOWN;
	}
	
	
}
