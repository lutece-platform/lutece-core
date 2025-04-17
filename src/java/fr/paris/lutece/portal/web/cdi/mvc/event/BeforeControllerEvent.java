package fr.paris.lutece.portal.web.cdi.mvc.event;

/**
 * Event triggered before the execution of the MVC controller logic(view and action).
 * <p>
 * This event allows observers to intercept the request at an early stage,
 * typically to perform tasks such as:
 * <ul>
 *   <li>Modifying request parameters or attributes</li>
 *   <li>Injecting contextual data</li>
 *   <li>Performing pre-processing or validation</li>
 *   <li>Short-circuiting the controller execution if needed</li>
 * </ul>
 * </p>
 *
 * <p>
 * It is part of the MVC event lifecycle and is fired before any controller-specific (view or action) is run.
 * </p>
 *
 * @see MvcEvent
 */
public interface BeforeControllerEvent extends MvcEvent{

}
