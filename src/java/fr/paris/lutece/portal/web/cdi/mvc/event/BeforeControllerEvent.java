package fr.paris.lutece.portal.web.cdi.mvc.event;

import java.lang.reflect.Method;

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

	/**
     * Returns the controller method that is about to be invoked.
     *
     * @return a {@link Method} object representing the target controller method
     */
	Method getInvokedMethod();
	
	/**
     * Indicates whether the current request is in the back-office (BO) context.
     *
     * @return {@code true} if the request is part of the back-office, {@code false} otherwise 
     */
    boolean isBackOffice();
    
    /**
     * Indicates the type of controller invocation (e.g., view, action, messageBox).
     *
     * @return a {@link ControllerInvocationType} enum describing the nature of the method being invoked
     */
    ControllerInvocationType getInvocationType();

    /**
     * Returns the status of the automatic security token generation and validation.
     * 
     * @return {@code true} if the controller uses the automatic security token process, {@code false} otherwise
     */
    boolean isSecurityTokenEnabled( );
}
