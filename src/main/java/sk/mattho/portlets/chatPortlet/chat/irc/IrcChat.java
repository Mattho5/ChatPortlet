package sk.mattho.portlets.chatPortlet.chat.irc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PreDestroy;


import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.exception.IrcException;
import org.pircbotx.exception.NickAlreadyInUseException;
import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatInterface;
import sk.mattho.portlets.chatPortlet.chat.genericChat.Contact;
import sk.mattho.portlets.chatPortlet.chat.genericChat.ContactState;


public class IrcChat extends ChatInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7746873656307818275L;
	private PircBotX ircConnection;
	private String ircChannelName;
	private IrcChannel ircChannel;
	private static String CHANNEL_PREFIX="#";
	private IrcConnectionListener listener;
	private List<User> users;
	
	
	public IrcChat(){
		this.ircConnection= new PircBotX();
		this.users=new ArrayList<User>();
		//this.ircConnection.set
		this.listener=new IrcConnectionListener();
		this.listener.setChat(this);
		this.ircConnection.getListenerManager().addListener(listener);
		
		this.ircChannelName="";
		
	}
	public IrcChannel getIrcChannel() {
		return ircChannel;
	}
	public void setIrcChannel(IrcChannel ircChannel) {
		this.ircChannel = ircChannel;
	}
	
	public void setUsers(Set<User> users) {
		this.users.clear();
		this.users=new ArrayList<User>(users);
	}
	@Override
	public boolean connect(String name, String password) {
	
		try {
		this.ircConnection.changeNick(name);
		this.ircConnection.setAutoNickChange(true);
		this.ircConnection.setName(name);
		this.ircConnection.setLogin(name+"_");
		this.ircConnection.setVersion(name+"");
		if(getServer().contains("freenode"))
			;
		if(password==null || password.compareTo("")==0)
			
				this.ircConnection.connect(getServer(),getPort());
			
		else 
			this.ircConnection.connect(getServer(),getPort(),password);
		this.ircConnection.joinChannel(CHANNEL_PREFIX+this.ircChannelName);
		this.ircChannel= new IrcChannel();
		this.ircChannel.setMySession(this);
		this.ircChannel.setIdName(this.ircChannelName);
		this.ircChannel.setName(CHANNEL_PREFIX+this.ircChannelName);
		this.ircChannel.setOnline(true);
		
		
		//System.out.print
		return true;
		} catch (NickAlreadyInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return false;
	}
	
	public void initUsers(){
		if(users!=null)
			users.clear();
		
		this.users =new ArrayList<User>(ircConnection.getUsers());
		this.notifyContactChange(this.ircChannel);
	}
	
	public List<User> getUsers() {
		return users;
	}
	@Override
	//This actually returns connected ircChannel 
	public List<Contact> getContacts() {
		// TODO Auto-generated method stub
		ArrayList<Contact> channel = new ArrayList<Contact>();
		channel.add(ircChannel);
		
		return channel;
	}

	@Override
	public void sendMessage(Contact to, String message) {
		this.ircConnection.sendMessage(CHANNEL_PREFIX+this.ircChannelName, message);
	//	this.ircConnection.sendMessage(to.getIdName(), message);
		//this.ircConnection.send
		
	}

	@Override
	public void disconnect() {
		this.ircChannel=null;
		this.ircConnection.disconnect();
		
	}

	@Override
	public boolean deleteContact() {
		// Do nothing
		return false;
	}
	
	@Override
	public Contact findContactByIdName(String idName) {
		if(idName.compareTo(this.ircChannelName)==0)
			return this.ircChannel;
		return null;
	}

	@Override
	public boolean addContact(Contact toAdd) {
		// Do nothing;
		return false;
	}
	public ArrayList<String> ircContacts(){
		return null;
	}
	public String getIrcChannelName() {
		return ircChannelName;
	}
	public void setIrcChannelName(String ircChannelName) {
		this.ircChannelName = ircChannelName;
	}
	@PreDestroy
	public void preDestroy(){
		this.ircConnection.disconnect();
	}
	
	public void notifyMessage() {
		this.notifyMessage(this.ircChannel);
	}
	@Override
	public void setStatus(ContactState state, String textStatus) {
		// Do nothing
		
	} 
	

}
