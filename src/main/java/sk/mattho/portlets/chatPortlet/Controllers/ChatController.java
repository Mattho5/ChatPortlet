/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the 
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sk.mattho.portlets.chatPortlet.Controllers;

import java.io.IOException;
import java.io.Serializable;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.portlet.ActionRequest;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

import sk.mattho.portlets.chatPortlet.AccountInfo;
import sk.mattho.portlets.chatPortlet.PreferencesAccounts;
import sk.mattho.portlets.chatPortlet.chat.ChatManager;
import sk.mattho.portlets.chatPortlet.chat.ChatConfigurations;
import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatEventsListener;
import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatInterface;

import sk.mattho.portlets.chatPortlet.chat.genericChat.Contact;

/**
 * {@link ChatController} is the JSF backing bean for the application, holding
 * the input data to be re-displayed.
 */
@Named
@SessionScoped
public class ChatController implements Serializable, ChatEventsListener {
	private boolean connected;
	private boolean pushWorking;

	private Contact currentContact;
	private String username;
	private String service;
	private String password;
	private String port;
	private String server;
	private ChatConfigurations[] selectedConf;
	private ChatConfigurations toSelect;
	private final String userIdentifier = UUID.randomUUID().toString()
			.replace("-", "");

	public ChatConfigurations[] getSelectedConf() {
		return selectedConf;
	}

	public void setSelectedConf(ChatConfigurations[] selectedConf) {
		this.selectedConf = selectedConf;
	}

	public ChatConfigurations getToSelect() {
		return toSelect;
	}

	public void setToSelect(ChatConfigurations toSelect) {
		this.toSelect = toSelect;
	}

	private String actualTab;

	private TopicsContext topicsContext;
	private static final String CDI_PUSH_TOPIC = "imService";

	public String getUserIdentifier() {
		return userIdentifier;
	}

	@Inject
	private ChatManager manager;

	private static final long serialVersionUID = -6239437588285327644L;

	@PostConstruct
	public void postContruct() {
		this.connected = false;
		// this.userIdentifier=
		this.selectedConf = ChatConfigurations.values();
	//this.initFromPreferences();
	}
	public boolean isConnected() {

		return this.connected;
	}

	public void initFromPreferences(){
		Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (request instanceof ActionRequest) {
			ActionRequest actionReq = (ActionRequest) request; 
			PortletPreferences p=	actionReq.getPreferences();
			//=p.getMap()
		//	Map<String,String[]> pref=p.getMap();
		//	String[] temp=pref.get("accounts");
		//String[] acc=	actionReq.getParameter ("accounts");
			String temp = "";
			temp= p.getValue("accounts",temp);
			System.out.println("temp"+ temp);
			PreferencesAccounts pr= new PreferencesAccounts(temp);
			//boolean tmp= false;
		for(AccountInfo a:pr.accounts()){
			if( this.manager.addAccount(a.getUserName(),a.getPassword(),this, a.getProtocol()));
			 	//	if(this.addAccount())
					this.connected=true;
			}
			
		
		}
		
	}
	
	private boolean saveToPreferences(){
		Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (request instanceof ActionRequest) {
			ActionRequest actionReq = (ActionRequest) request; 
			
				try {
					AccountInfo acc= new AccountInfo(this.username,this.password,this.toSelect);
					PreferencesAccounts pref= new PreferencesAccounts();
					pref.addAccount(acc);
				PortletPreferences p=	actionReq.getPreferences();
				p.setValue("accounts", pref.getAccounts());
						actionReq.getPreferences().store();
						System.out.println("preferences stored");
					}
				catch (ValidatorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					
				} catch (ReadOnlyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
		
		}
		return false;
	}

	public List<Contact> getContacts() {
		List<Contact> temp = this.manager.getFriends();
		Comparator<Contact> c = new Comparator<Contact>() {

			@Override
			public int compare(Contact o1, Contact o2) {
				if (o1.getState() == null && o2.getState() == null)
					return 0;
				else if (o1.getState() == null && o2.getState() != null)
					return -1;
				else if (o1.getState() != null && o2.getState() == null)
					return 1;

				return o1.getState().compareTo(o2.getState());
			}
		};
		Collections.sort(temp, c);
		return temp;

	}

	/**
	 * Resets {@link #name} to the default value {@code "World"} and
	 * {@link #greeting} with the default value {@code "Hello"}.
	 * 
	 * @param ae
	 *            ignored
	 */

	public void connect() {
		if (this.toSelect != ChatConfigurations.NONE) {
			this.connected = this.manager.addAccount(this.username,
					this.password, this, this.toSelect);
		} else {
			if (this.manager.addAccount(this.username, this.password,
					this.server, 5222, this.service, this))
				this.connected = true;
		}
		
		//this.saveToPreferences();
		

	}

	public void disconnect() {
		this.connected = false;

		this.manager.disconnect();

	}

	public boolean addAccount() {
		boolean result=false;
		if (this.toSelect != ChatConfigurations.NONE)
		result=	this.manager.addAccount(this.username, this.password, this,
					this.toSelect);
	//	else
		//	this.connected = true;
		//TODO add other account
return result;
	}

	public void setCurrentConversation(String idname) {
		this.setActualTab(idname);
	}

	public void newConversation(Contact contact) {
		this.setActualTab(contact.getIdName());
		this.currentContact = contact;
		this.manager.newConversation(contact);
		messageWindowRefresh();
	}

	private TopicsContext getTopicsContext() {

		if (topicsContext == null) {
			topicsContext = TopicsContext.lookup();
		}
		return topicsContext;
	}

	public void exitConversation() {
		String conv = (FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("current"));
		this.manager.exitConversation(conv);
		if (this.manager.getConversations().size() > 0) {
			this.currentContact = this.manager.getConversations().get(0);
			this.actualTab = currentContact.getIdName();
		} else {
			this.currentContact = null;
			this.actualTab = "";
		}
	}

	public Date getDate() {
		return new Date();
	}

	public void setActualTab(String actualTab) {
		// find in conversations
		// Contact c=null;
		this.actualTab = actualTab;
		for (Contact con : this.manager.getConversations()) {
			if (con.getIdName().compareTo(actualTab) == 0) {
				this.currentContact = con;
				this.currentContact.setHasUnreadedMessage(false);
				System.out.println("setting actual tab" + con);
				break;
			}
		}
		// this.conversations.g

	}

	/**
	 * Escaping input string and adding smilies
	 * 
	 * @param input
	 *            input string
	 * @return String with escaped emoticons
	 */
	public String escapeString(String input) {
		String ret = input;
		ret = ret.replaceAll("&", "&amp;");
		ret = ret.replaceAll("<", "&lt;");
		ret = ret.replaceAll(">", "&gt;");
		// smile
		ret = ret.replaceAll(":\\)",
				"<img src=\"/chatportlet/pages/emoticons/smile.png\" />");
		ret = ret.replaceAll(":-\\)",
				"<img src=\"/chatportlet/pages/emoticons/smile.png\" />");
		// laugh
		ret = ret.replaceAll(":D",
				"<img src=\"/chatportlet/pages/emoticons/laugh.png\" />");
		ret = ret.replaceAll(":-D",
				"<img src=\"/chatportlet/pages/emoticons/laugh.png\" />");
		// angel
		ret = ret.replaceAll("O:-\\)",
				"<img src=\"/chatportlet/pages/emoticons/angel.png\" />");
		ret = ret.replaceAll("O:\\)",
				"<img src=\"/chatportlet/pages/emoticons/angel.png\" />");
		ret = ret.replaceAll("o:-\\)",
				"<img src=\"/chatportlet/pages/emoticons/angel.png\" />");
		ret = ret.replaceAll("o:\\)",
				"<img src=\"/chatportlet/pages/emoticons/angel.png\" />");
		// cool
		ret = ret.replaceAll("8\\)",
				"<img src=\"/chatportlet/pages/emoticons/cool.png\" />");
		ret = ret.replaceAll("8-\\)",
				"<img src=\"/chatportlet/pages/emoticons/cool.png\" />");
		ret = ret.replaceAll("B\\)",
				"<img src=\"/chatportlet/pages/emoticons/cool.png\" />");
		ret = ret.replaceAll("B-\\)",
				"<img src=\"/chatportlet/pages/emoticons/cool.png\" />");
		// sad
		ret = ret.replaceAll(":\\(",
				"<img src=\"/chatportlet/pages/emoticons/sad.png\" />");
		ret = ret.replaceAll(":-\\(",
				"<img src=\"/chatportlet/pages/emoticons/sad.png\" />");
		// silly
		ret = ret.replaceAll(":P",
				"<img src=\"/chatportlet/pages/emoticons/silly.png\" />");
		ret = ret.replaceAll(":p",
				"<img src=\"/chatportlet/pages/emoticons/silly.png\" />");
		ret = ret.replaceAll(":-P",
				"<img src=\"/chatportlet/pages/emoticons/silly.png\" />");
		ret = ret.replaceAll(":-p",
				"<img src=\"/chatportlet/pages/emoticons/silly.png\" />");

		// kissy
		ret = ret.replaceAll(":\\*",
				"<img src=\"/chatportlet/pages/emoticons/kissy.png\" />");
		ret = ret.replaceAll(":-\\*",
				"<img src=\"/chatportlet/pages/emoticons/kissy.png\" />");
		return ret;
	}

	public String subString(int lenght, String inputString) {
		if (inputString.length() > lenght) {
			StringBuilder sb = new StringBuilder();
			sb.append(inputString.substring(0, lenght));
			sb.append("...");
			return sb.toString();
		} else
			return inputString;
	}

	// ------------------------------------------------------------------
	// Implementation of ChatEventsListener
	// --------- ---------------------------------------------------------
	@Override
	public void processMessage(Contact c) {
		// this.renderId=new String("message");

		System.out.println("new message from contact :" + c.getName());
		if (this.manager.getConversations().size() < 1) {
			this.currentContact = c;
			c.setHasUnreadedMessage(false);
			conversationsWindowRefresh();
			messageWindowRefresh();

		} else if (this.currentContact != null
				&& this.currentContact.getIdName().compareTo(c.getIdName()) == 0) {
			messageWindowRefresh();
		}

		else
			conversationsWindowRefresh();
	}

	@Override
	public void processIncomingChat(Contact c) {
		// System.out.println("Incoming chat from: ");

		if (this.manager.getConversations().size() < 1) {
			this.currentContact = c;
			messageWindowRefresh();
		}
		conversationsWindowRefresh();

	}

	@Override
	public void processContactStateChanged(Contact c) {
		System.out.println("Status changed: " + c.getIdName());
		contactWindowRefresh();
	}

	@Override
	public void connected() {
		// TODO Auto-generated method stub
		this.contactWindowRefresh();

	}

	@Override
	public void disconnected() {
		// TODO Auto-generated method stub

	}

	public void checkPush() {
		this.pushWorking = false;

		this.pushTest();
	}

	// ==================================================================

	private void pushTest() {
		// this.pushWorking=true;
		try {
			getTopicsContext().publish(
					new TopicKey(CDI_PUSH_TOPIC, this.getUserIdentifier()
							+ "pushTest"), "message");
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void messageWindowRefresh() {
		try {
			getTopicsContext().publish(
					new TopicKey(CDI_PUSH_TOPIC, this.getUserIdentifier()
							+ "newmessage"), "sprava");
			System.out.println("fireMessage ");
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void contactWindowRefresh() {
		try {
			getTopicsContext().publish(
					new TopicKey(CDI_PUSH_TOPIC, this.getUserIdentifier()
							+ "contact"), "sprava");
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void conversationsWindowRefresh() {
		try {
			getTopicsContext().publish(
					new TopicKey(CDI_PUSH_TOPIC, this.getUserIdentifier()
							+ "conversations"), "sprava");
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ============================================================================
	// GETTERS and SETTERS
	// ============================================================================

	public boolean isPushWorking() {
		return pushWorking;
	}

	public void setPushWorking(boolean pushWorking) {

		this.pushWorking = pushWorking;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Contact getCurrentContact() {
		return currentContact;
	}

	public void setCurrentContact(Contact currentContact) {
		this.currentContact = currentContact;
	}

	public String getActualTab() {
		return actualTab;
	}

	public List<Contact> getConversations() {
		return this.manager.getConversations();
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
	
	public List<ChatInterface> getAccounts(){
		return this.manager.getAccounts();
		
	}

}
