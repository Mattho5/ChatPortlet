package sk.mattho.portlets.chatPortlet.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import sk.mattho.portlets.chatPortlet.chat.ChatConfigurations;
import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatEventsListener;
import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatInterface;
import sk.mattho.portlets.chatPortlet.chat.genericChat.Contact;
import sk.mattho.portlets.chatPortlet.chat.xmpp.XmppChat;

public class ChatManager implements ChatEventsListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ChatInterface> accounts;
	private List<Contact> conversations;
	private List<Contact> friends;
	

	@PostConstruct
	public void init() {
	//	System.out.println("iniing");
		this.accounts = new ArrayList<ChatInterface>();
		this.conversations = new ArrayList<Contact>();
		this.friends = new ArrayList<Contact>();
	}

	public void addChatListener(ChatEventsListener listener) {
		for (ChatInterface ch : accounts)
			ch.addListener(listener);
	}

	public void removeListener(ChatEventsListener listener) {
		for (ChatInterface ch : accounts)
			ch.removeListener(listener);
	}

	/**
	 * This method adding contacts in conversation list.
	 * 
	 * @param contact
	 *            - contact to conversation start with
	 */
	public void newConversation(Contact contact) {
		if (this.conversations.contains(contact))
			System.out.println(contact + "already exist!");
		else {
			this.conversations.add(contact);
			contact.beginConversation();
			System.out.println("" + contact.getIdName()
					+ " added succesfully into conversations");
		}
	}

	public void exitConversation(String convName) {

		Contact c = this.findContactByIdName(convName);
		if (c != null) {
			c.endConversation();
			this.conversations.remove(c);
			System.out.print("Removed from conversations: " + convName);
		}
	}

	private Contact findContactByIdName(String convName) {
		Contact c = null;
		for (ChatInterface i : this.accounts) {
			c = i.findContactByIdName(convName);
			if (c != null)
				return c;
		}
		return c;
	}

	public boolean addAccount(String userName, String password,
			ChatEventsListener listener, ChatConfigurations con) {
		if (con == null) {
			System.out.println("connection is null");
			return false;
		} else
			return this.addAccount(userName, password, con.getServer(),
					con.getPort(), con.getDomain(), listener);
	}

	public boolean addAccount(String userName, String password, String server,
			int port, String serviceName, ChatEventsListener listener) {
		XmppChat chat = new XmppChat();
		chat.setServer(server);
		chat.setServiceName(serviceName);
		chat.setPort(port);
		chat.setUsername(userName);
		chat.addListener(this);
		chat.addListener(listener);
		if (chat.connect(userName, password)) {
			this.accounts.add(chat);
			this.friends.addAll(chat.getContacts());
			return true;
		}
		return false;
	}

	public void disconnect() {
	//	System
		for (ChatInterface c : this.accounts) {
			System.out.println("Disconnecting");
			c.disconnect();
		}
		this.accounts.clear();
		this.friends.clear();
		this.conversations.clear();
	}

	public void disconnect(String server, String username) {
			ChatInterface ch=this.searchAccount(server, username);
			if(ch!=null)
				{ch.disconnect();
					this.accounts.remove(ch);
				}
			
	}

	public ChatInterface searchAccount(String server, String username) {
		for (ChatInterface ch : this.accounts)
			if (ch.getServer().compareTo(server) == 0
					&& ch.getUsername().compareTo(username) == 0)
				return ch;
		return null;
	}

	// -----------------------------------------------------------
	// IMPLEMENTS OF chateventsListener
	// -----------------------------------------------------------
	@Override
	public void processMessage(Contact c) {
		if (this.conversations.size() < 1) {
			this.conversations.add(c);

		} else if (!this.conversations.contains(c))
			this.conversations.add(c);
	}

	@Override
	public void processIncomingChat(Contact c) {
		if (!this.conversations.contains(c))
			this.conversations.add(c);
	}

	@Override
	public void connected(ChatInterface chatInterface) {
		// TODO Auto-generatedt method stub
		
		System.out.println("Connected to "+ chatInterface.getServer());
	}

	@Override
	public void disconnected(ChatInterface chatInterface) {
		// TODO Auto-generated method stub
		// for(int i;i<this.friends.size();i++){
		// if(c.)
		List<Contact> contacts= new ArrayList<Contact>();
		
		for (Contact c : this.friends) {
			if (c.isFrom(chatInterface))
				contacts.add(c);
		}
		
		for (Contact c :contacts) {
			if(this.friends.contains(c))
				this.friends.remove(c);
			if(this.conversations.contains(c))
				this.conversations.remove(c);
		}
		
		
		contacts.clear();
	//	contacts=null;
	//	this.accounts.remove(chatInterface);

	}

	@Override
	public void processContactStateChanged(Contact c) {
		// do nothing
	}

	// -----------------------------------------------------------
	// GETTERS AND SETTERS
	// -----------------------------------------------------------
	public List<Contact> getConversations() {
		return conversations;
	}

	public List<Contact> getFriends() {
		return friends;
	}

	public void setFriends(List<Contact> friends) {
		// this.friends = friends;
	}

	public List<ChatInterface> getAccounts() {
		return accounts;
	}

}
