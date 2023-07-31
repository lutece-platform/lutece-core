package fr.paris.lutece.portal.service.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@ApplicationScoped
public class RemovalListenerServiceProducer {
	
	@Produces
    @Named("mailinglistRemovalService")
    @ApplicationScoped
    public RemovalListenerService mailingProducer() {
        return new RemovalListenerService( );
    }

	@Produces
    @Named("workgroupRemovalService")
    @ApplicationScoped
    public RemovalListenerService workGroupProducer() {
        return new RemovalListenerService( );
    }
	
	@Produces
    @Named("rbacRemovalService")
    @ApplicationScoped
    public RemovalListenerService rbacProducer() {
        return new RemovalListenerService( );
    }
	@Produces
    @Named("portletRemovalService")
    @ApplicationScoped
    public RemovalListenerService portletProducer() {
        return new RemovalListenerService( );
    }
	@Produces
    @Named("regularExpressionRemovalService")
    @ApplicationScoped
    public RemovalListenerService regularExpressionProducer() {
        return new RemovalListenerService( );
    }
	
	@Produces
    @Named("workflowRemovalService")
    @ApplicationScoped
    public RemovalListenerService workflowExpressionProducer() {
        return new RemovalListenerService( );
    }
	@Produces
    @Named("roleRemovalService")
    @ApplicationScoped
    public RemovalListenerService roleRemovalServiceProducer() {
        return new RemovalListenerService( );
    }

}
