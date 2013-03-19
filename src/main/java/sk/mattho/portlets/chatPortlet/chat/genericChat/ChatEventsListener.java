package sk.mattho.portlets.chatPortlet.chat.genericChat;

public interface ChatEventsListener {
	public void connected();
	public void disconnected();
	public void processMessage(Contact c);
	public void processIncomingChat(Contact c);
	public void processContactStateChanged(Contact c);
}
