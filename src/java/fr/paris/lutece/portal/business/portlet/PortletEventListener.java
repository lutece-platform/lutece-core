package fr.paris.lutece.portal.business.portlet;

/**
 * Portlet event listener
 *
 */
public interface PortletEventListener 
{
	/**
     * Process a page event
     * @param event The event to process
     */
    void processPageEvent( PortletEvent event );
}
