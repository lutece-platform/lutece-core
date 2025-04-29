package fr.paris.lutece.portal.web.cdi.mvc.event;

import java.lang.reflect.Method;

public class BeforeProcessActionEventImpl implements BeforeProcessActionEvent {

	private Method method;
	
	@Override
	public Method getMethode() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}
