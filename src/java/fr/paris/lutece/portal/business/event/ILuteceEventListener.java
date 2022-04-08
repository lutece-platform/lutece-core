package fr.paris.lutece.portal.business.event;


import java.util.EventListener;

/**
*
* EventRessourceListener
*
*/
public interface ILuteceEventListener<T extends AbstractLuteceEvent> extends EventListener
{
   /**
    * Return the listener service name.
    *
    * @return the name
    */
   String getName( );

   /**
    * Warn subscriber that a resource has been added
    * 
    * @param event
    *            the event for the added resource
    */
   void onEvent( AbstractLuteceEvent event );

}
