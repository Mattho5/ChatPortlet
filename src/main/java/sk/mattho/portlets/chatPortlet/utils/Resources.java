package sk.mattho.portlets.chatPortlet.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

public class Resources {
	
		@Produces
	   @PersistenceContext(unitName="chatportlet-db", type=PersistenceContextType.EXTENDED)
	   private EntityManager em;
	
}
