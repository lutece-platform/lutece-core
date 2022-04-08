package fr.paris.lutece.portal.business.event;

import fr.paris.lutece.portal.service.security.LuteceUser;

public class LuteceUserEvent extends AbstractLuteceEvent<LuteceUser> 
{

	// user event type
	private LuteceUserEventType _type;
	
	/**
	 * Constuctor
	 * 
	 * @param user
	 * @param type
	 */
	public LuteceUserEvent(LuteceUser user, LuteceUserEventType type ) 
	{
		super( user );
		_type = type;
	}

	/**
	 * get type 
	 * @return the type 
	 */
	public LuteceUserEventType getType( ) 
	{
		return _type;
	}
}
