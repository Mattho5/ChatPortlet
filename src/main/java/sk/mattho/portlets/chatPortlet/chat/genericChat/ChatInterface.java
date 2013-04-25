package sk.mattho.portlets.chatPortlet.chat.genericChat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sk.mattho.portlets.chatPortlet.chat.xmpp.XmppChat;

public abstract class ChatInterface implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2715664303692245371L;
	private String server;
	private int port;
	private String username;

	private List<ChatEventsListener> eventsListeners;
	
	public abstract boolean connect(String name, String password); 
	public abstract List<Contact> getContacts();
	public abstract void sendMessage(Contact to,String message);
	public abstract void disconnect();
	public abstract boolean deleteContact();
	public abstract Contact findContactByIdName(String idName);
	public abstract boolean addContact(Contact toAdd);
	public abstract void setStatus(ContactState state,String textStatus);
	public void addListener(ChatEventsListener listener){
		if(this.eventsListeners==null)
			this.eventsListeners= new ArrayList<ChatEventsListener>();
		eventsListeners.add(listener);
	}
	public void removeListener(ChatEventsListener listener){
		this.eventsListeners.remove(listener);
	}
	
	
	//----------------------------------------------------------
	//  OBSERVABLE METHODS
	//-------------------------------------------------------
	public void notifyContactChange(Contact c){
		//System.out.println("Contact changing");
		for(ChatEventsListener l:this.eventsListeners){
			l.processContactStateChanged(c);
		}
	}
	public void notifyMessage(Contact c){
	//	System.out.println("Notifying about message");
		for(ChatEventsListener l:this.eventsListeners){
			l.processMessage(c);
		}
	}
	public void notifyIncomingChat(Contact c){
	
		for(ChatEventsListener l:this.eventsListeners){
			l.processIncomingChat(c);
		}
	}
	
	public void notifyConnected(){
		for(ChatEventsListener l:this.eventsListeners){
			l.connected(this);
		}
		
	}
	public void notifyDisconnected(){
		for(ChatEventsListener l:this.eventsListeners){
			l.disconnected(this);
		}
		
//-------------------------------------------------
//        GETTERS AND SETTERS
//------------------------------------------------
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((server == null) ? 0 : server.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatInterface other = (ChatInterface) obj;
		if (server == null) {
			if (other.server != null)
				return false;
		} else if (!server.equals(other.server))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	

	
	
	
 
}
