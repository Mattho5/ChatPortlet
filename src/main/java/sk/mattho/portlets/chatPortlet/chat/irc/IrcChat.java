package sk.mattho.portlets.chatPortlet.chat.irc;

import java.util.List;

import org.jibble.pircbot.PircBot;

import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatInterface;
import sk.mattho.portlets.chatPortlet.chat.genericChat.Contact;


public class IrcChat extends ChatInterface{

	private PircBot ircConnection;
	
	@Override
	public boolean connect(String name, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Contact> getContacts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMessage(Contact to, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteContact() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Contact findContactByIdName(String idName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addContact(Contact toAdd) {
		// TODO Auto-generated method stub
		return false;
	}

}
