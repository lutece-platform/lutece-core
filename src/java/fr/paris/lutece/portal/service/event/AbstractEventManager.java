package fr.paris.lutece.portal.service.event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import fr.paris.lutece.portal.business.event.AbstractLuteceEvent;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * @deprecated Use CDI events and @observes listeners instead
 */
@Deprecated(forRemoval = true)
public  abstract class  AbstractEventManager <T extends AbstractLuteceEvent> 
{

	private Map<String, Consumer<T>> _listeners = new HashMap<>( );
   
    /**
     * Subscribe to this listener.
     *
     * @param listener
     *            the listener
     * @deprecated Use CDI events and @observes listeners instead
     */
	public void register( String strName, Consumer<T> consumerFunction )
    {
    	_listeners.put( strName, consumerFunction );
        AppLogService.info( "New Lutece event listener registered : {}", strName );
    }
    
    /**
     * Subscribe to this listener.
     *
     * @param listener
     *            the listener
     * @deprecated Use CDI events firing instead
     */
	public void notifyListeners( T event )
    {
        for ( String key : _listeners.keySet( ) )
        {
        	_listeners.get( key ).accept( event );            
        }
    }
}
