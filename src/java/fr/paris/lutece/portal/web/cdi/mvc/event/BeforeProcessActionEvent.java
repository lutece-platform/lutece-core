package fr.paris.lutece.portal.web.cdi.mvc.event;

import java.lang.reflect.Method;

public interface BeforeProcessActionEvent extends MvcEvent {

	Method getMethode();
}

