package sk.mattho.portlets.chatPortlet.chat.xmpp;

import java.util.Date;


import javax.inject.Named;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.richfaces.application.push.MessageException;
import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatMessage;
import sk.mattho.portlets.chatPortlet.chat.genericChat.Contact;
import sk.mattho.portlets.chatPortlet.model.DBContact;

@Named
public class XmppContact extends Contact {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6175580327513361664L;
	private Chat chat;
	private ChatManager manager;
	private String presenceFrom;
	private RosterEntry rosterEntry;

	
	public RosterEntry getRosterEntry() {
		return rosterEntry;
	}

	public void setRosterEntry(RosterEntry rosterEntry) {
		this.rosterEntry = rosterEntry;
	}

	public String getPresenceFrom() {
		return presenceFrom;
	}

	public void setPresenceFrom(String presenceFrom) {
		this.presenceFrom = presenceFrom;
	}

	public XmppContact(ChatManager chatManager) {
		this.manager = chatManager;
		this.chat = null;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	private void initChat() {
		if (this.chat == null) {
			this.chat = this.manager.createChat(this.getIdName(),
					new MessageListener() {
						@Override
						public void processMessage(Chat arg0, Message arg1) {
					//		System.out.println("Created chat:");
					//		noticeMessage(arg0, arg1);
						}
					});
		}
	}

	public void initChat(Chat incomingChat) {
		if (this.chat == null) {
			//this.manager.
		//	this.chat.getListeners().clear();
		//	this.chat = null;
		//}
		this.chat = incomingChat;
		if (this.chat.getListeners().size() < 1)
			this.chat.addMessageListener(new MessageListener() {

				@Override
				public void processMessage(Chat arg0, Message arg1) {
					System.out.println("Init chat:");
					noticeMessage(arg0, arg1);

				}
			});
	}
		// this.chat=incomingChat;

	}

	@SuppressWarnings("unused")
	private void closeChat() {
		this.chat = null;
	}

	@Override
	public void sendMessage() throws MessageException {
		if (this.chat == null)
			initChat();
		if (this.getActualMessage() != "")
			try {
				ChatMessage m = new ChatMessage();
				m.setDate(new Date());
				m.setFrom("Me");
				m.setMessage(this.getActualMessage());

				chat.sendMessage(this.getActualMessage());
				this.addToHistory(m);
				this.setHasUnreadedMessage(false);
				this.pushMessage();
				this.setActualMessage("");
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	public Chat getChat() {
		return chat;
	}

	private void noticeMessage(Chat arg0, Message arg1) {
		System.out.println("message from " + getIdName() + ":  "
				+ arg1.getBody());
		if (arg1.getBody() != null) {

			ChatMessage m = new ChatMessage();
			m.setDate(new Date());
			m.setFrom(getIdName());
			m.setMessage(arg1.getBody());
			addToHistory(m);
			setHasUnreadedMessage(true);
			this.pushMessage();

		}
	}

	@Override
	public void beginConversation() {
	//	if(this.db==null)
		//	this.db= new DBController();
		
		// TODO Auto-generated method stub
	
	//	}		
		if (this.chat == null)
			this.initChat();

	}
	@Override
	public void endConversation() {
	//	this.chat.getListeners().clear();
		//this.chat = null;
		// TODO Auto-generated method stub

	}

}
