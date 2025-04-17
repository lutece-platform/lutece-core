package fr.paris.lutece.portal.web.cdi.mvc.event;


import fr.paris.lutece.util.url.UrlItem;
import jakarta.enterprise.context.Dependent;
import jakarta.servlet.http.HttpServletRequest;

@Dependent
public class ControllerRedirectEventImpl implements ControllerRedirectEvent {

	private UrlItem location;
	
	@Override
	public UrlItem getLocation( ) {
		return location;
	}
	public void setLocation( UrlItem location ) {
		 this.location= location;
	}

}
