/***
 * This Class facilitates messaging exception between modules of this application.
 * Share the MessageStore object between provider and consumer
 * @author Vimal
 */
package model;

public class MessageStore {
	private Exception exception;
	private String message;
	public MessageStore(Exception e){
		exception = e;
	}
	public MessageStore(Exception e, String m){
		exception = e;
		message = m;
	}
	public MessageStore(String m){
		message = m;
	}
	public String getMessage(){
		if(message.isEmpty() || message == null ){
			return exception.getMessage();
		}else{
			return message;
		}
	}
	public Exception getExcp(){
		return exception;
	}
	
}
