package sk.mattho.portlets.chatPortlet.chat;

public enum ChatConfigurations {
	NONE("",0,""),
	GTALK("talk.google.com",5222,"gmail.com"),
	FACEBOOK_CHAT("chat.facebook.com",5222,"chat.facebook.com");
	
	private String server;
	private String domain;
	private int port;
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
private ChatConfigurations(String server,int port, String domain){
	this.server=server;
	this.port=port;
	this.domain=domain;
}
	

}
