package fr.paris.lutece.portal.service.event;

import fr.paris.lutece.portal.business.event.AbstractLuteceEvent;
import fr.paris.lutece.portal.business.event.ILuteceEventListener;

public interface IEventManager<T extends AbstractLuteceEvent > {

	/**
	 * Subscribe to this listener.
	 *
	 * @param listener
	 *            the listener
	 */
	void register(ILuteceEventListener<T> listener);

	/**
	 * Subscribe to this listener.
	 *
	 * @param listener
	 *            the listener
	 */
	void notifyListeners(T event);
	
	 
	

}