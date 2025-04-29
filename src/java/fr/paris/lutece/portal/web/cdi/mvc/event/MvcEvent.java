package fr.paris.lutece.portal.web.cdi.mvc.event;

/**
 * <p>Base type for all Lutece MVC events. Every Lutece MVC event type must extend this interface.</p>
 *
 * @since 8.0.0
 */
public interface MvcEvent {

	public enum ControllerInvocationType {
		ACTION,
        VIEW,
        DEFAULT_VIEW,
        MESSAGE_BOX_VIEW,
        OTHER
    }
	
}
