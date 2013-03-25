package sk.mattho.portlets.chatPortlet.chat.irc;

import java.util.Date;
import java.util.List;

import org.pircbotx.User;
import org.richfaces.application.push.MessageException;

import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatMessage;
import sk.mattho.portlets.chatPortlet.chat.genericChat.Contact;

public class IrcChannel extends Contact{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7205732572532333706L;

	@Override
	public void sendMessage() throws MessageException {
	
		this.getMySession().sendMessage(this,this.getActualMessage());
		ChatMessage m=new ChatMessage();
		m.setDate(new Date());
		m.setMessage(getActualMessage());
		m.setFrom(this.getMySession().getUsername());
		this.addToHistory(m);
		this.setActualMessage("");
		this.pushMessage();
	}

	public void addToHistory(String name,String message){
		ChatMessage m=new ChatMessage();
		m.setDate(new Date());
		m.setMessage(message);
		m.setFrom(name);
		this.addToHistory(m);
		this.pushMessage();
		
	}
	
	public List<User> getconnectedUsers(){
		return ((IrcChat)getMySession()).getUsers();
	}
	
	
	@Override
	public void beginConversation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endConversation() {
		// TODO Auto-generated method stub
		
	}

}
