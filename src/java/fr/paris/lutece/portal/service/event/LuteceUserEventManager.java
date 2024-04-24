package fr.paris.lutece.portal.service.event;

import fr.paris.lutece.portal.business.event.LuteceUserEvent;

/**
 * @deprecated Use CDI events and @observes listeners instead
 */
@Deprecated(forRemoval = true)
public class LuteceUserEventManager extends AbstractEventManager<LuteceUserEvent>
{

    private static LuteceUserEventManager _instance = null;

	/**
	 * get service
	 * @return the service
	 */
	public static LuteceUserEventManager getInstance( ) 
	{
		if ( _instance == null )
		{
			_instance = new LuteceUserEventManager( );
		}
		
		return _instance;
	}
	
	 
}



