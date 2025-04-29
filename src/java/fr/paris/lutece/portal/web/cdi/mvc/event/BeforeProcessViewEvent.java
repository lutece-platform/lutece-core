package fr.paris.lutece.portal.web.cdi.mvc.event;

import java.lang.reflect.Method;

public interface BeforeProcessViewEvent extends MvcEvent {

	Method getMethode();
}
