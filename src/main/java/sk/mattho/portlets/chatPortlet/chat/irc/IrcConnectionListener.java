package sk.mattho.portlets.chatPortlet.chat.irc;


import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.UserListEvent;

import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatMessage;

public class IrcConnectionListener extends ListenerAdapter{
	private IrcChat chat;
	
	public IrcChat getChat() {
		return chat;
	}
	public void setChat(IrcChat chat) {
		this.chat = chat;
	}
	@Override
	public void onConnect(ConnectEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onConnect(event);
	}
	@Override
	public void onDisconnect(DisconnectEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onDisconnect(event);
	}
	@Override
	public void onJoin(JoinEvent event) throws Exception {
		ChatMessage m = new ChatMessage();
    	m.setFrom("");
    	m.setMessage( event.getUser().getLogin()+ "has joined " +event.getChannel().getName());
		chat.getIrcChannel().addToHistory(m);
		chat.initUsers();
		System.out.print(m.getMessage());
		
		chat.notifyMessage();
	}
	@Override
	public void onUserList(UserListEvent event) throws Exception {
		chat.setUsers(event.getUsers());
		System.out.println("Userslist changed");
		
		//super.onUserList(event);
	}
	@Override
	public void onMessage(MessageEvent event) throws Exception {	
		ChatMessage m = new ChatMessage();
    	m.setFrom(event.getUser().getNick());
    	m.setMessage(event.getMessage());
		chat.getIrcChannel().addToHistory(m);
	//	chat.get
		System.out.print("Message "+event.getMessage());
		chat.notifyMessage();
	}
	@Override
	public void onQuit(QuitEvent event) throws Exception {
		ChatMessage m = new ChatMessage();
		chat.initUsers();
    	m.setFrom("");
    	m.setMessage( event.getUser().getLogin()+"("+event.getUser().getNick()+")"+ "has quit:"+event.getReason());
       System.out.println(m.getMessage() );
       chat.getIrcChannel().addToHistory(m);
       chat.notifyMessage();
	}
  
	
}
