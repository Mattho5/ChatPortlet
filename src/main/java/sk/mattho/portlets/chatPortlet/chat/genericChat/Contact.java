package sk.mattho.portlets.chatPortlet.chat.genericChat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.richfaces.application.push.MessageException;

@RequestScoped
public abstract class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private ChatInterface mySession;
	private String idName;
	private List<ChatMessage> messagesHistory;
	private String name;
	private ContactState state;
	private boolean online;
	private String status;
	private boolean typing;
	private boolean hasUnreadedMessage;

	public boolean isHasUnreadedMessage() {
		return hasUnreadedMessage;
	}

	public void setHasUnreadedMessage(boolean hasUnreadedMessage) {
		this.hasUnreadedMessage = hasUnreadedMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ContactState getState() {
		return state;
	}

	public void setState(ContactState state) {
		if (!state.equals(this.state))
		this.state = state;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	private String actualMessage;
	private String messageName;

	public Contact() {
		this.messagesHistory = new ArrayList<ChatMessage>();
	}

	public String getActualMessage() {
		return actualMessage;
	}

	public List<ChatMessage> getMessagesHistory() {
		return this.messagesHistory;
	}

	public void setMessagesHistory(List<ChatMessage> m) {
		// do nothing
	}

	public String getMessageName() {
		return messageName;
	}

	public void addToHistory(ChatMessage m) {
		if (this.messagesHistory == null)
			this.messagesHistory = new ArrayList<ChatMessage>();
		this.messagesHistory.add(m);
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public void setActualMessage(String actualMessage) {
		this.actualMessage = actualMessage;
	}

	public ChatInterface getMySession() {
		return mySession;
	}

	public void setMySession(ChatInterface mySession) {
		this.mySession = mySession;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getName() {
		//if(this.name!=null )
		return name;
	//	else return this.idName.split("@")[0];
	}

	public void setName(String name) {
		this.name = name;
	}

	protected void pushMessage() {
		this.mySession.notifyMessage(this);
	}

	public abstract void sendMessage() throws MessageException;
	public abstract void beginConversation();
	public abstract void endConversation();
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idName == null) ? 0 : idName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		Contact other = (Contact) obj;
		if (idName == null) {
			if (other.idName != null)
				return false;
		} else if (!idName.equals(other.idName))
			return false;
		return true;
	}
	public boolean isFrom(ChatInterface chat){
		return this.getMySession().equals(chat);
	}
}
