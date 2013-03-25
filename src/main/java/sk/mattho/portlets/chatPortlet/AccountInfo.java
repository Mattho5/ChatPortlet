package sk.mattho.portlets.chatPortlet;

import java.io.Serializable;

import com.google.common.base.Objects.ToStringHelper;

import sk.mattho.portlets.chatPortlet.chat.ChatConfigurations;

public class AccountInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -111655575964971598L;
	private String userName;
	private String password;
	private ChatConfigurations protocol;
	private Integer port;
	private String server;
	private String domain;
	public Integer getPort() {
		return port;
	}
	
	public AccountInfo(String input){
		String[] temp=input.split("\\|");
		this.userName=temp[0];
		this.password=temp[1];
		//for(ChatConfigurations c:ChatConfigurations.va)
		this.protocol=ChatConfigurations.valueOf(temp[2]);
		if(this.protocol==ChatConfigurations.IRC || this.protocol== ChatConfigurations.XMPP){
			this.server=temp[3];
			this.port=new Integer(temp[4]);
			this.domain=temp[5];
		}
		else{
			this.server=this.protocol.getServer();
			this.port=this.protocol.getPort();
			this.domain=this.protocol.getDomain();
		}
				
		
	}
	private void init(){
		this.userName="";
		this.password="";
		this.server="";
		
		this.domain="";
		this.port=0;
	}
	public AccountInfo(String userName, String password,
			ChatConfigurations protocol) {
		init();
		this.userName = userName;
		this.password = password;
		this.protocol = protocol;
	}
	
	@Override
	
	
	public String toString(){
		
		String ret=userName+"|"+password+"|"+protocol.name()+"|"+server+"|"+port+"|"+domain+"|";
	
		
		return ret;
	}
	//----------------------------------------------------------
	// 			GETTERS and SETTERS
	//----------------------------------------------------------
	public void setPort(Integer port) {
		this.port = port;
	}
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ChatConfigurations getProtocol() {
		return protocol;
	}
	public void setProtocol(ChatConfigurations protocol) {
		this.protocol = protocol;
	}

}
