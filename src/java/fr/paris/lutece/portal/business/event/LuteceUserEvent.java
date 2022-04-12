package fr.paris.lutece.portal.business.event;

import fr.paris.lutece.portal.service.security.LuteceUser;

public class LuteceUserEvent extends AbstractLuteceEvent<LuteceUser> {

	private LuteceUserEventType _type;
	
	public LuteceUserEvent(LuteceUser user,LuteceUserEventType type) {
		super( user );
		_type=type;
	}

	public LuteceUserEventType getType() {
		return _type;
	}


}
