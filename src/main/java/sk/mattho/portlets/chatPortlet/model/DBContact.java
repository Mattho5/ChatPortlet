package sk.mattho.portlets.chatPortlet.model;

import javax.persistence.Entity;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.OrderBy;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import javax.persistence.Version;

import java.lang.Override;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "account", "idName" }))
public class DBContact implements Serializable
{

@Id
   private @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   Long id = null;
   @Version
   private @Column(name = "version")
   int version = 0;

   @Column (name="idname")
   private String idName;

   @Column
   private String account;

   @Column
   private String userId;

   @OrderBy("date")
   @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL )
   private Set<DBMessage> conversation = new HashSet<DBMessage>();
   
   

   public Set<DBMessage> getConversation() {
	return conversation;
}

public void setConversations(Set<DBMessage> conversations) {
	this.conversation = conversations;
}

public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Override
   public boolean equals(Object that)
   {
      if (this == that)
      {
         return true;
      }
      if (that == null)
      {
         return false;
      }
      if (getClass() != that.getClass())
      {
         return false;
      }
      if (id != null)
      {
         return id.equals(((DBContact) that).id);
      }
      return super.equals(that);
   }

   @Override
   public int hashCode()
   {
      if (id != null)
      {
         return id.hashCode();
      }
      return super.hashCode();
   }

   public String getIdName()
   {
      return this.idName;
   }

   public void setIdName(final String idName)
   {
      this.idName = idName;
   }

   public String getAccount()
   {
      return this.account;
   }

   public void setAccount(final String account)
   {
      this.account = account;
   }

   public String getUserId()
   {
      return this.userId;
   }

   public void setUserId(final String userId)
   {
      this.userId = userId;
   }

   public String toString()
   {
      String result = "";
      if (idName != null && !idName.trim().isEmpty())
         result += idName;
      if (account != null && !account.trim().isEmpty())
         result += " " + account;
      if (userId != null && !userId.trim().isEmpty())
         result += " " + userId;
      return result;
   }
}