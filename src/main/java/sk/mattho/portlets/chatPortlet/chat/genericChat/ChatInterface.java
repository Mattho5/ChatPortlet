package sk.mattho.portlets.chatPortlet.chat.genericChat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ChatInterface implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2715664303692245371L;
	private String server;
	private int port;
	private String username;

	private List<ChatEventsListener> eventsListeners;
	private ContactState myState;
	private String status;
	
	public abstract boolean connect(String name, String password); 
	public abstract List<Contact> getContacts();
	public abstract void sendMessage(Contact to,String message);
	public abstract void disconnect();
	public abstract boolean deleteContact();
	public abstract Contact findContactByIdName(String idName);
	public abstract boolean addContact(Contact toAdd);
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
		System.out.println("Contact changing");
		for(ChatEventsListener l:this.eventsListeners){
			l.processContactStateChanged(c);
		}
	}
	public void notifyMessage(Contact c){
		System.out.println("Notifying about message");
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
			l.connected();
		}
		
	}
	public void notifyDisconnected(){
		for(ChatEventsListener l:this.eventsListeners){
			l.disconnected();
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
	

	
	
	
 
}
