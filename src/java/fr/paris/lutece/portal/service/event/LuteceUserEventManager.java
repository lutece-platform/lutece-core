package fr.paris.lutece.portal.service.event;

import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.business.event.ILuteceEventListener;
import fr.paris.lutece.portal.business.event.LuteceUserEvent;

public class LuteceUserEventManager extends AbstractEventManager{

	private static LuteceUserEventManager _instance=null;

	private LuteceUserEventManager(List<ILuteceEventListener<LuteceUserEvent>> listeners) {
		super(listeners);
		
	}

	
	
	public static LuteceUserEventManager getService() {
		
		if(_instance==null)
		{
			
			List<ILuteceEventListener<LuteceUserEvent>> listeners=new ArrayList<>();
			_instance=new LuteceUserEventManager(new ArrayList<>());
			
		}
		return _instance;
	}

    
    
    
}



