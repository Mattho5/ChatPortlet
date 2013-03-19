package sk.mattho.portlets.chatPortlet.chat.genericChat;

public interface ChatEventsListener {
	public void connected(ChatInterface chatInterfaces);
	public void disconnected(ChatInterface chatInterface);
	public void processMessage(Contact c);
	public void processIncomingChat(Contact c);
	public void processContactStateChanged(Contact c);
}
