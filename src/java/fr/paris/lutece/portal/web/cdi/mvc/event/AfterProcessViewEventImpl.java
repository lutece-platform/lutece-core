package fr.paris.lutece.portal.web.cdi.mvc.event;

/**
 * Default implementation of the {@link AfterProcessViewEvent} interface.
 * <p>
 * This class serves as a concrete event object that can be fired and observed
 * after the MVC view has been processed. It contains no additional data by default,
 * but can be extended to include contextual information if needed.
 * </p>
 *
 * <p>
 * Typically fired by the {@link fr.paris.lutece.portal.web.cdi.mvc.event.EventDispatcher}
 * at the end of the MVC request lifecycle, after the view has been rendered.
 * </p>
 *
 * @see AfterProcessViewEvent
 */
public class AfterProcessViewEventImpl implements AfterProcessViewEvent {

}
