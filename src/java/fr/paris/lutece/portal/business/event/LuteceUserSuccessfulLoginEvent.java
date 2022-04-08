package fr.paris.lutece.portal.business.event;

import fr.paris.lutece.portal.service.security.LuteceUser;

public class LuteceUserSuccessfulLoginEvent extends AbstractLuteceEvent<LuteceUser> {

	public LuteceUserSuccessfulLoginEvent(LuteceUser user) {
		super( user );
	}

}
