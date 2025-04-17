package fr.paris.lutece.portal.web.cdi.mvc.event;


import fr.paris.lutece.util.url.UrlItem;

/**
 * <p>An event is triggered when a lutece controller MVC  performs an HTTP redirect 
 *    using the HttpServletResponse.sendRedirect(...) method..</p>
 *
 * <p>For example:
 * <pre><code>    public class EventObserver {
 *         public void onControllerRedirect(&#64;Observes ControllerRedirectEvent e) {
 *            ...
 *        } 
 *    }</code></pre>
 *
 * @see jakarta.enterprise.event.Observes
 * @since 8.0.0
 */
public interface ControllerRedirectEvent extends MvcEvent{

    /**
     * The target of the redirection.
     *
     * @return URL target of redirection.
     */
	UrlItem getLocation();
}
