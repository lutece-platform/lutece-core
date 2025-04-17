package fr.paris.lutece.portal.web.cdi.mvc.event;

/**
 * Event triggered after the MVC view has been processed and rendered.
 * <p>
 * This event can be observed to perform post-processing tasks such as:
 * <ul>
 *   <li>Modifying the rendered response</li>
 *   <li>Injecting additional data or metadata</li>
 *   <li>Logging or auditing</li>
 *   <li>Cleaning up session or request-scoped resources</li>
 * </ul>
 * </p>
 *
 * <p>
 * It is part of the MVC event lifecycle and is typically fired by the framework
 * once the view rendering is completed.
 * </p>
 *
 * @see MvcEvent
 */
public interface AfterProcessViewEvent extends MvcEvent {

}
