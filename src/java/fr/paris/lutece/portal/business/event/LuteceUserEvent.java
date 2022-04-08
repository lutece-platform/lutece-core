package fr.paris.lutece.portal.business.event;

import fr.paris.lutece.portal.service.security.LuteceUser;

public class LuteceUserEvent extends AbstractLuteceEvent<LuteceUser> 
{
	/**
	 * event types
	 */
	public enum EventType {
		LOGIN_SUCCESSFUL,
		LOGIN_FAILED,
		USER_CREATED,
		USER_DELETED,
		PASSWORD_CHANGED_SUCCESSFUL,
		LOGOUT;
	}
	
	// user event type
	private EventType _type;
	
	/**
	 * Constuctor
	 * 
	 * @param user
	 * @param type
	 */
	public LuteceUserEvent(LuteceUser user, EventType type ) 
	{
		super( user );
		_type = type;
	}

	/**
	 * get type 
	 * @return the type 
	 */
	public EventType getType( ) 
	{
		return _type;
	}
}
