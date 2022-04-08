package fr.paris.lutece.portal.service.event;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.business.event.AbstractLuteceEvent;
import fr.paris.lutece.portal.business.event.ILuteceEventListener;
import fr.paris.lutece.portal.service.util.AppLogService;

public class LuteceEventManager {

    protected static List<ILuteceEventListener<? extends AbstractLuteceEvent>> _listeners = new ArrayList<>( );
    
    /**
     * Subscribe to this listener.
     *
     * @param listener
     *            the listener
     */
    public static void register( ILuteceEventListener<? extends AbstractLuteceEvent> listener )
    {
    	_listeners.add( listener );
        AppLogService.info( "New Lutece event listener registered : {}", listener.getName( ) );
    }
    
    /**
     * Subscribe to this listener.
     *
     * @param listener
     *            the listener
     */
    public static void notifyListeners( AbstractLuteceEvent event )
    {
        for ( ILuteceEventListener<? extends AbstractLuteceEvent> listener : _listeners )
        {
        	
        	listener.onEvent( event );            
        }
    }
    
    
    
}
