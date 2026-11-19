package fr.paris.lutece.portal.service.util;

import fr.paris.lutece.portal.business.mailinglist.MailingListWorkgroupRemovalListener;
import fr.paris.lutece.portal.business.user.parameter.EmailPatternRegularExpressionRemovalListener;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@ApplicationScoped
public class RemovalListenerServiceProducer {

	@Produces
    @Named( BeanUtils.BEAN_MAILINGLIST_REMOVAL_SERVICE )
    @ApplicationScoped
    public RemovalListenerService mailingProducer() {
        return new RemovalListenerService( );
    }

	@Produces
    @Named(BeanUtils.BEAN_WORKGROUP_REMOVAL_SERVICE)
    @ApplicationScoped
    public RemovalListenerService workGroupProducer() {
		RemovalListenerService service =new RemovalListenerService( );
		service.registerListener(new MailingListWorkgroupRemovalListener( ));
        return service;
    }
	
	@Produces
    @Named( BeanUtils.BEAN_RBAC_REMOVAL_SERVICE )
    @ApplicationScoped
    public RemovalListenerService rbacProducer() {
        return new RemovalListenerService( );
    }
	@Produces
    @Named( BeanUtils.BEAN_PORTLET_REMOVAL_SERVICE )
    @ApplicationScoped
    public RemovalListenerService portletProducer() {
        return new RemovalListenerService( );
    }
	@Produces
    @Named( BeanUtils.BEAN_REGULAR_EXPRESSION_REMOVAL_SERVICE )
    @ApplicationScoped
    public RemovalListenerService regularExpressionProducer() {
		RemovalListenerService service= new RemovalListenerService( );
		service.registerListener(new EmailPatternRegularExpressionRemovalListener());
        return service;
    }
	
	@Produces
    @Named(BeanUtils.BEAN_WORKFLOW_REMOVAL_SERVICE)
    @ApplicationScoped
    public RemovalListenerService workflowExpressionProducer() {
        return new RemovalListenerService( );
    }
	@Produces
    @Named( BeanUtils.BEAN_ROLE_REMOVAL_SERVICE )
    @ApplicationScoped
    public RemovalListenerService roleRemovalServiceProducer() {
        return new RemovalListenerService( );
    }

	/**
	 * The removal service of the back office users: a plugin registers a listener to refuse the deletion of a user it still references (for example
	 * a user to whom resources are assigned); the listeners receive the id of the user.
	 *
	 * @return the removal service of the back office users
	 */
	@Produces
    @Named( BeanUtils.BEAN_ADMIN_USER_REMOVAL_SERVICE )
    @ApplicationScoped
    public RemovalListenerService adminUserRemovalServiceProducer( )
    {
        return new RemovalListenerService( );
    }

}
