package sk.mattho.portlets.chatPortlet.chat.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PreDestroy;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import org.jivesoftware.smack.packet.Presence;

import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatInterface;
import sk.mattho.portlets.chatPortlet.chat.genericChat.Contact;
import sk.mattho.portlets.chatPortlet.chat.genericChat.ContactState;

public class XmppChat extends ChatInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1257959342464436632L;
	private XMPPConnection connection;
	private List<Contact> contacts;
	private String serviceName;
	private  RosterListener rosterListener;
	private ChatManagerListener chatManagerListener;

	public XmppChat(){
		super();
		this.init();
	}
	public String getServiceName() {
		return serviceName;
	}
	
	public void init(){
	
		this.chatManagerListener= new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {

				// notifyIncomingChat(null);
				System.out.println("Incoming chat!!!!"
						+ chat.getParticipant());
				XmppContact contact = (XmppContact) findContactByIdName(chat
						.getParticipant().split("/")[0]);
				contact.initChat(chat);
				notifyIncomingChat(contact);

			}
		};
		
	this.rosterListener= new RosterListener() {

		@Override
		public void presenceChanged(Presence presence) {
			Contact c = findContactByIdName(presence.getFrom().split(
					"/")[0]);

			if (presence.getType() == Presence.Type.available) {
				c.setOnline(true);
				c.setState(ContactState.AVAILABLE);

			} else
				c.setOnline(false);
			if (presence.getStatus() != "")
				c.setStatus(presence.getStatus());

			switch (presence.getMode()) {

			case available:
				c.setState(ContactState.AVAILABLE);
				break;
			case away:
				c.setState(ContactState.AWAY);
				break;
			case dnd:
				c.setState(ContactState.DND);

				break;
			case chat:
				c.setState(ContactState.AVAILABLE);
				break;
			case xa:
				c.setState(ContactState.AWAY);
				break;
			default:
				c.setState(ContactState.AVAILABLE);
			}

			notifyContactChange(c);
		}

		@Override
		public void entriesUpdated(Collection<String> arg0) {
			System.out.println("Entry " + arg0 + " was updated");
		}

		@Override
		public void entriesDeleted(Collection<String> arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void entriesAdded(Collection<String> arg0) {
			// TODO Auto-generated method stub
		}
	};
		
	
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public boolean connect(String name, String password) {
		// TODO Auto-generated method stub
		if (this.getServer().contains("facebook")) {
			SASLAuthentication.registerSASLMechanism("DIGEST-MD5",
					MySASLDigestMD5Mechanism.class);
			SASLAuthentication.supportSASLMechanism("DIGEST-MD5", 0);
		}
		ConnectionConfiguration connConfig = new ConnectionConfiguration(
				this.getServer(), this.getPort(), this.getServiceName());
		// ("talk.google.com", 5222, "gmail.com");

		this.connection = new XMPPConnection(connConfig);
	//	this.connection.DEBUG_ENABLED=true;
		try {
			connection.connect();

			System.out.println("Connected to " + connection.getHost());
		} catch (XMPPException ex) {
			// ex.printStackTrace();
			System.out.println("Failed to connect to " + connection.getHost());

			return false;

		}
		try {
			this.connection.login(name, password);
			System.out.println("Logged in as " + connection.getUser());
			// Thread.sleep(3000);
			this.InitContacts();
			this.notifyConnected();
		} catch (XMPPException ex) {
			// ex.printStackTrace();
			System.out.println("Failed to log in as "
					+ (connection.getUser().split("@"))[0]);
			System.exit(1);
			return false;
		}
		return true;

	}

	private void InitContacts() {
		this.contacts = new ArrayList<Contact>();
		if (this.connection != null && this.connection.isConnected()) {
			org.jivesoftware.smack.ChatManager chatManager = connection
					.getChatManager();
			chatManager.addChatListener(this.chatManagerListener);

			Roster roster = connection.getRoster();
			// RosterListenerImpl rostList = new RosterListenerImpl(this);
			// roster.removeRosterListener(rostList);
			// roster.setDefaultSubscriptionMode(SubscriptionMode.)
			roster.addRosterListener(this.rosterListener);
			// roster.
			Collection<RosterEntry> entries = roster.getEntries();

			System.out.println("\n\n" + entries.size() + " buddy(ies):");

			for (RosterEntry r : entries) {
				XmppContact c = new XmppContact(chatManager);
				c.setIdName(r.getUser());
				c.setName(r.getName());
				c.setMySession(this);

				c.setRosterEntry(r);

				// r.
				Presence presence = roster.getPresence(r.getUser());
				c.setPresenceFrom(presence.getFrom());
				if (presence.getType() == Presence.Type.available)
					c.setOnline(true);
				else
					c.setOnline(false);
				this.contacts.add(c);
				// /System.out.println(r.getGroups().size() + " "
				// + r.getUser().split("@")[0]);
			}
		} else
			System.out.println("You are not connected!");

	}

	@Override
	public List<Contact> getContacts() {

		return this.contacts;

	}

	@Override
	public void sendMessage(Contact to, String message) {
		// TODO Auto-generated method stub
		System.out.println("sprava pre " + to.getName() + ": " + message);
		// ((XmppContact)to).sendMessage(message);
	}

	@Override
	public void disconnect() {
		this.contacts.clear();
		this.connection.getRoster().removeRosterListener(this.rosterListener);
		this.connection.getChatManager().removeChatListener(chatManagerListener);
		// this.connection.disconnect(Presence.Type.unavailable);
		//this.connection.
		this.connection.disconnect();
		//thos/this.connection.shutdown();
		this.connection=null;

	}

	@PreDestroy
	public void preDestroy() {
		if (this.connection.isConnected())
			this.disconnect();
		
		this.connection=null;
	}

	@Override
	public boolean deleteContact() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addContact(Contact toAdd) {
		// TODO Auto-generated method stub
		return false;
	}

	public Contact findContact(String idName) {
		// Contact temp=null;
		for (Contact c : this.getContacts()) {
			if (c.getIdName().compareTo(idName) == 0)
				return c;
		}
		return null;
	}

	@Override
	public Contact findContactByIdName(String idName) {
		// Contact temp=null;
		for (Contact c : this.getContacts()) {
			// String[] contactName=
			if (((XmppContact) c).getPresenceFrom().compareTo(idName) == 0)
				return c;
		}
		return null;
	}

}
