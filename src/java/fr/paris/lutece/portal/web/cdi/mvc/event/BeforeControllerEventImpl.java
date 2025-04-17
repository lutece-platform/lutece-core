package fr.paris.lutece.portal.web.cdi.mvc.event;

/**
 * Default implementation of the {@link BeforeControllerEvent} interface.
 * <p>
 * This class represents the event fired just before the MVC controller is executed.
 * It can be extended to carry additional contextual data (e.g., request parameters,
 * user session info) if needed by observers.
 * </p>
 *
 * <p>
 * Typically fired by the {@link fr.paris.lutece.portal.web.cdi.mvc.event.EventDispatcher}
 * at the beginning of the request lifecycle.
 * </p>
 *
 * @see BeforeControllerEvent
 */
public class BeforeControllerEventImpl implements BeforeControllerEvent{

}
