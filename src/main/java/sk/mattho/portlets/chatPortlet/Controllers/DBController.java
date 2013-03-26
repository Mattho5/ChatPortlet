package sk.mattho.portlets.chatPortlet.Controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import javax.persistence.Query;

import sk.mattho.portlets.chatPortlet.chat.genericChat.ChatMessage;
import sk.mattho.portlets.chatPortlet.chat.genericChat.Contact;
import sk.mattho.portlets.chatPortlet.chat.xmpp.XmppContact;
import sk.mattho.portlets.chatPortlet.model.DBContact;
import sk.mattho.portlets.chatPortlet.model.DBMessage;

@Stateless
public class DBController implements Serializable{
//@PersistenceContext (unitName="chatportlet-db")
	//private EntityManagerFactory emf;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2298313998574008269L;
	@Inject
	private EntityManager em;
	
	public void initContact(XmppContact contact){
		DBContact c = new DBContact();
		String acc = contact.getMySession().getUsername() + "@"
				+ contact.getMySession().getServer();
		// if(contact.get)
		c = getDBContact(acc, contact.getIdName());
		if (c == null) {
			c = new DBContact();
			c.setAccount(acc);
			c.setIdName(contact.getIdName());
			saveDbContact(c);
		}
		//((XmppContact) contact).setDbContact(c);
	}
	
	public void saveHistory(XmppContact contact){
		DBContact c = new DBContact();
		String acc = contact.getMySession().getUsername() + "@"
				+ contact.getMySession().getServer();
		// if(contact.get)
		c = getDBContact(acc, contact.getIdName());
		if (c == null) {
			c = new DBContact();
			c.setAccount(acc);
			c.setIdName(contact.getIdName());
			saveDbContact(c);
		}
		
		for(ChatMessage m:contact.getMessagesHistory()){
			if(!m.isSaved()){
			DBMessage dm= new DBMessage();
			dm.setDate(m.getDate());
			dm.setMessage(m.getMessage());
			if(m.getFrom().compareTo("Me")!=0)
				dm.setFromMe(false);
			
			else dm.setFromMe(true);
			c.getConversation().add(dm);
			}
		
			
		}
	//em.persist(c);
		em.merge(c);
	//em.flush();
	contact.getMessagesHistory().clear();
	}
	
	public void loadHistory(XmppContact contact){
		String acc = contact.getMySession().getUsername() + "@"
				+ contact.getMySession().getServer();
		DBContact c= getDBContact(acc,contact.getIdName());
		if(c!=null){
			List<ChatMessage> m= new ArrayList<ChatMessage>( contact.getMessagesHistory());
			contact.getMessagesHistory().clear();
			for(DBMessage dbm:c.getConversation()){
				ChatMessage newChatMessage= new ChatMessage((dbm.getFromMe()? "Me":contact.getIdName()), dbm.getMessage(),dbm.getDate());
				newChatMessage.setSaved(true);
				contact.getMessagesHistory().add(newChatMessage);
			}			
			contact.getMessagesHistory().addAll(m);
		}
	}
	
	
	public DBContact getDBContact(String account,String idname){
	//	em= emf.createEntityManager();
		em.isOpen();
		Query q= em.createQuery("SELECT c from DBContact c where c.idName =:name and c.account =:acc");
		q.setParameter("name",idname);
		q.setParameter("acc", account);
		try{
		return (DBContact) q.getSingleResult();
		}
		catch(NoResultException e){
			return null;
		}
	}
	
	public void saveDbContact(DBContact contact){
	//	em.
		em.persist(contact);
		//em.r
	}
	//public void saveDBContactChan

}
