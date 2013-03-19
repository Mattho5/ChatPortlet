package sk.mattho.portlets.chatPortlet.chat.genericChat;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class ChatMessage implements Serializable{
	private String from;
	private String message;
	private Date date;
	
	public ChatMessage(){
		
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public ChatMessage(String from, String message, Date date) {
		super();
		this.from = from;
		this.message = message;
		this.date = date;
	}
	@Override
	public String toString(){
		return date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+" "+this.from+": "+this.message;
	}
	public String getTime(){
		return date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
	public void setTime(String s){
		
	}
	

}
