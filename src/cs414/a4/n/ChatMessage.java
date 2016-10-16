package cs414.a4.n;

public class ChatMessage {

	private String sender;
	
	private String message;

	public ChatMessage(String sender, String message) {
		this.sender = sender;
		this.message = message;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getMessage() {
		return message;
	}
}