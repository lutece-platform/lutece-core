package fr.paris.lutece.portal.web.cdi.mvc;

import java.io.Serializable;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.web.cdi.mvc.event.EventDispatcher;
import fr.paris.lutece.portal.web.cdi.mvc.event.MvcEvent.ControllerInvocationType;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.url.UrlItem;
import jakarta.inject.Inject;

public class RedirectScopeTest extends LuteceTestCase{

	/**
     * Bean injected with the {@code @RedirectScoped} scope.
     * This bean is expected to retain its state during a redirect.
     */
	@Inject
	RedirectScopedBean bean;
	 /**
     * Component responsible for managing controller lifecycle events.
     * Used to simulate different phases of an HTTP request in the test.
     */
	@Inject 
	private EventDispatcher _eventDispatcher;
	   
	/**
     * Tests the behavior of the {@code @RedirectScoped} scope during a redirect.
     * <p>
     * The test simulates the following steps:
     * <ol>
     *   <li>Initialization of the context before the controller execution.</li>
     *   <li>Modification of the {@code @RedirectScoped} bean's value.</li>
     *   <li>Triggering of a redirect event.</li>
     *   <li>Verification that the value is retained after the redirect.</li>
     *   <li>Triggering of the post-view processing event.</li>
     *   <li>Verification that the bean is reset after the view is processed.</li>
     * </ol>
     * </p>
     */
	@Test
    public void testGetPage( )
    {
         UrlItem url = new UrlItem( "/Portal.jsp" );
         // Simulate the start of controller processing
         try
         {
             _eventDispatcher.fireBeforeControllerEvent( this.getClass( ).getMethod( "mockControllerViewMethod" ), true, ControllerInvocationType.VIEW );
         }
         catch( Exception e )
         {
             // This case should not happen
         }

        // Set a value in the bean
        bean.setValue("test-through-redirect");
        // Simulate a redirect
        _eventDispatcher.fireControllerRedirectEvent(url);
        // Verify that the value is retained after the redirect
        String valueAfterRedirect= bean.getValue();
        // Simulate post-view processing
        _eventDispatcher.fireAfterProcessViewEvent();
        String valueAfterProcessView=bean.getValue();

        assertEquals(valueAfterRedirect,"test-through-redirect");  
        assertEquals(valueAfterProcessView,"init");        
    }
	/**
     * Test bean with the {@code @RedirectScoped} scope.
     * <p>
     * This bean simulates temporary state that should be retained during a redirect
     * and reset after the view is processed.
     * </p>
     */
	@RedirectScoped
	public static class  RedirectScopedBean implements Serializable{
	    private static final long serialVersionUID = 1L;

		  private String value = "init";

		  public String getValue() {
		       return value;
		  }

		  public void setValue(String value) {
		      this.value = value;
		  }
	}

	
	public void mockControllerViewMethod() 
	{
	    // Nothing to do
	}
}
