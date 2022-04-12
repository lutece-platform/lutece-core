package fr.paris.lutece.portal.service.event;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.business.event.AbstractLuteceEvent;
import fr.paris.lutece.portal.business.event.ILuteceEventListener;
import fr.paris.lutece.portal.service.util.AppLogService;

public  abstract class  AbstractEventManager <T extends AbstractLuteceEvent> implements IEventManager<T> {

    private  List<ILuteceEventListener<T>> _listeners = new ArrayList<>( );
   
    
    /**
     * Subscribe to this listener.
     *
     * @param listener
     *            the listener
     */
    @Override
	public  void register( ILuteceEventListener<T> listener )
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
    @Override
	public  void notifyListeners( T event )
    {
        for ( ILuteceEventListener<T> listener : _listeners )
        {
        	
        	listener.onEvent( event );            
        }
    }

	public AbstractEventManager(List<ILuteceEventListener<T>> _listeners) {
		super();
		this._listeners = _listeners;
	}
       
   
    
    
    
}
