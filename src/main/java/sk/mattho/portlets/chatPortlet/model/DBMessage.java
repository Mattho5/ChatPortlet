package sk.mattho.portlets.chatPortlet.model;


import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import java.util.Date;

@Entity
public class DBMessage implements Serializable
{

   @Id
   private @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   Long id = null;
   @Version
   private @Column(name = "version")
   int version = 0;
   @Column
   private String message;
   
   @Column 
   private Date date;
   
   @Column
   private Boolean fromMe;
   
   public Boolean getFromMe() {
	return fromMe;
}

public void setFromMe(Boolean fromMe) {
	this.fromMe = fromMe;
}

public Date getDate() {
	return date;
}

public void setDate(Date date) {
	this.date = date;
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
         return id.equals(((DBMessage) that).id);
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

   public String getMessage()
   {
      return this.message;
   }

   public void setMessage(final String message)
   {
      this.message = message;
   }

   public String toString()
   {
      String result = "";
      if (message != null && !message.trim().isEmpty())
         result += message;
      return result;
   }
}