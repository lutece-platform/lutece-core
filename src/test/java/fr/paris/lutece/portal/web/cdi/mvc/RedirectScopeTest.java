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
     *   <li>Request that redirects: modification of the {@code @RedirectScoped} bean's value, then a redirect event.</li>
     *   <li>The view that redirected ends: the value is retained, the next request needs it.</li>
     *   <li>Request that follows the redirect: the value is retained during the controller processing.</li>
     *   <li>Its view ends: the bean is reset.</li>
     * </ol>
     * </p>
     */
	@Test
    public void testGetPage( )
    {
        UrlItem url = new UrlItem( "/Portal.jsp" );
        // Request that redirects: a view sets a value, then redirects
        fireBeforeControllerEvent( );
        bean.setValue( "test-through-redirect" );
        _eventDispatcher.fireControllerRedirectEvent( url );
        String valueAfterRedirect = bean.getValue( );
        // The view that redirected ends: the scope must survive until the next request
        _eventDispatcher.fireAfterProcessViewEvent( );
        String valueAfterRedirectingView = bean.getValue( );

        // Request that follows the redirect
        fireBeforeControllerEvent( );
        String valueInNextRequest = bean.getValue( );
        _eventDispatcher.fireAfterProcessViewEvent( );
        String valueAfterProcessView = bean.getValue( );

        assertEquals( "test-through-redirect", valueAfterRedirect );
        assertEquals( "test-through-redirect", valueAfterRedirectingView );
        assertEquals( "test-through-redirect", valueInNextRequest );
        assertEquals( "init", valueAfterProcessView );
    }

	/**
	 * The scope parameter is appended to a redirect within the webapp only: an external or protocol-relative
	 * location is left as it is.
	 */
	@Test
	public void testScopeParameterOnlyOnInternalRedirect( )
	{
	    assertTrue( redirectTo( "jsp/admin/plugins/forms/ManageForms.jsp?view=manageForms" ).contains( RedirectScopeManager.DEFAULT_QUERY_PARAM_NAME ) );
	    assertTrue( redirectTo( "Portal.jsp?page=z&back_url=https://return.example.org/x" ).contains( RedirectScopeManager.DEFAULT_QUERY_PARAM_NAME ) );
	    assertEquals( "https://return.example.org/back", redirectTo( "https://return.example.org/back" ) );
	    assertEquals( "//return.example.org/back", redirectTo( "//return.example.org/back" ) );
	    assertEquals( "/\\return.example.org/back", redirectTo( "/\\return.example.org/back" ) );
	    assertEquals( "\\\\return.example.org/back", redirectTo( "\\\\return.example.org/back" ) );
	}

	/**
	 * Runs a request that sets a value of the redirect scope then redirects to a location
	 *
	 * @param strLocation
	 *            the redirect location
	 * @return the location after the redirect event
	 */
	private String redirectTo( String strLocation )
	{
	    UrlItem url = new UrlItem( strLocation );
	    fireBeforeControllerEvent( );
	    bean.setValue( "test-through-redirect" );
	    _eventDispatcher.fireControllerRedirectEvent( url );
	    _eventDispatcher.fireAfterProcessViewEvent( );
	    fireBeforeControllerEvent( );
	    _eventDispatcher.fireAfterProcessViewEvent( );
	    return url.getUrl( );
	}

	/**
	 * Simulate the start of controller processing
	 */
	private void fireBeforeControllerEvent( )
	{
        try
        {
            _eventDispatcher.fireBeforeControllerEvent( this.getClass( ).getMethod( "mockControllerViewMethod" ), true, ControllerInvocationType.VIEW, false );
        }
        catch( Exception e )
        {
            // This case should not happen
        }
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
