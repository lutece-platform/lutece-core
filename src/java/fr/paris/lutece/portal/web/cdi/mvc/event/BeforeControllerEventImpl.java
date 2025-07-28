package fr.paris.lutece.portal.web.cdi.mvc.event;

import java.lang.reflect.Method;

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

	private final Method invokedMethod;
	private final boolean isBackOffice;
    private final ControllerInvocationType invocationType;
    private final boolean isSecurityTokenEnabled;

    /**
     * Constructs a new {@code BeforeControllerEventImpl} with the given parameters.
     *
     * @param invokedMethod the method that will be invoked
     * @param isBackOffice {@code true} if the context is back-office (BO), {@code false} if front-office (FO)
     * @param invocationType the type of controller call (e.g., VIEW, ACTION, etc.)
     */
    public BeforeControllerEventImpl(Method invokedMethod, boolean isBackOffice, ControllerInvocationType invocationType, boolean isSecurityTokenEnabled) {
        this.invokedMethod = invokedMethod;
        this.isBackOffice = isBackOffice;
        this.invocationType = invocationType;
        this.isSecurityTokenEnabled = isSecurityTokenEnabled;
    }

	@Override
	public Method getInvokedMethod() {
		return invokedMethod;
	}
	
	@Override
	public boolean isBackOffice() {
		return isBackOffice;
	}
	
	@Override
    public ControllerInvocationType getInvocationType() {
        return invocationType;
    }

    @Override
    public boolean isSecurityTokenEnabled( )
    {
        return isSecurityTokenEnabled;
    }

}
