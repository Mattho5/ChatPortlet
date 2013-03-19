package sk.mattho.portlets.chatPortlet;

import java.io.Serializable;

import com.google.common.base.Objects.ToStringHelper;

import sk.mattho.portlets.chatPortlet.chat.ChatConfigurations;

public class AccountInfo implements Serializable{
	private String userName;
	private String password;
	private ChatConfigurations protocol;
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
	public AccountInfo(String input){
		String[] temp=input.split("\\|");
		this.userName=temp[0];
		this.password=temp[1];
		//for(ChatConfigurations c:ChatConfigurations.va)
		this.protocol=ChatConfigurations.valueOf(temp[2]);
				
		
	}
	public AccountInfo(String userName, String password,
			ChatConfigurations protocol) {
		super();
		this.userName = userName;
		this.password = password;
		this.protocol = protocol;
	}
	@Override
	
	
	public String toString(){
		
		String ret=userName+"|"+password+"|"+protocol.name();
	
		
		return ret;
	}
	

}
