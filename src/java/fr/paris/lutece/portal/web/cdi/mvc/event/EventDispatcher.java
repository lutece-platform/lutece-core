package fr.paris.lutece.portal.web.cdi.mvc.event;

import java.lang.reflect.Method;

import fr.paris.lutece.util.url.UrlItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
/**
 * Central dispatcher for firing CDI MVC-related events during the request lifecycle.
 * <p>
 * This class allows external components to observe and react to different moments
 * in the MVC controller flow, such as before the controller is executed,
 * after the view is processed, or just before a redirection occurs.
 * </p>
 *
 * <p>
 * Events dispatched include:
 * <ul>
 *   <li>{@link BeforeControllerEvent} – fired before the controller view and action has been processed.</li>
 *   <li>{@link AfterProcessViewEvent} – fired after the view has been processed.</li>
 *   <li>{@link ControllerRedirectEvent} – fired just before a redirect operation.</li>
 * </ul>
 * </p>
 * 
 */
@ApplicationScoped
public class EventDispatcher {


    @Inject
    private Event<MvcEvent> mvcEventDispatcher;
   
    /**
     * Fires a {@link BeforeControllerEvent} before the controller view and action has been processed.
     * <p>
     * This allows observers to modify request context, set attributes, or
     * perform validation before the controller method runs.
     * </p>
     *
     * @param  invokedMethod controller method that is about to be invoked
     * @param stIsBackOffice Indicates whether the current request is in the back-office (BO) context or in the FO
     * @return the dispatched {@link BeforeControllerEvent} instance
     */
    public BeforeControllerEvent fireBeforeControllerEvent( Method invokedMethod, boolean isBackOffice, BeforeControllerEvent.ControllerInvocationType controllerInvocationType  ) {
    	final BeforeControllerEventImpl event = new BeforeControllerEventImpl(invokedMethod, isBackOffice, controllerInvocationType);
    	
        mvcEventDispatcher.fire(event);  
        return event;
    }
    /**
     * Fires an {@link AfterProcessViewEvent} after the view has been processed.
     * <p>
     * Observers may use this hook to inject additional content, perform logging,
     * or clean up temporary data.
     * </p>
     *
     * @return the dispatched {@link AfterProcessViewEvent} instance
     */
    public AfterProcessViewEvent fireAfterProcessViewEvent( ) {
        final AfterProcessViewEventImpl event = new AfterProcessViewEventImpl();
        mvcEventDispatcher.fire(event);
        return event;
    }
    /**
     * Fires a {@link ControllerRedirectEvent} just before a redirect is performed.
     * <p>
     * This allows observers to modify the redirect target URL or perform logic
     * based on the target destination.
     * </p>
     *
     * @param uri the {@link UrlItem} representing the redirect destination
     * @return the dispatched {@link ControllerRedirectEvent} instance
     */
    public ControllerRedirectEvent fireControllerRedirectEvent( UrlItem uri ) {
    	final ControllerRedirectEventImpl event = new ControllerRedirectEventImpl();        
        event.setLocation(uri);
        mvcEventDispatcher.fire(event);
        return event; 
	}
}
