package fr.paris.lutece.portal.service.html;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OwaspXSSSanitizer implements IXSSSanitizer {

	private static PolicyFactory _policy ;
	
	@Override
	public String sanitize( String strSource ) 
	{
	    // sanitize, but unescape "@" and "+" for emails
	    return _policy.sanitize( strSource ).replace ( "&#64;", "@").replace( "&#43;", "+" );
	}
	    
    public void init( )
    {
    	_policy = Sanitizers.FORMATTING
	            .and(Sanitizers.LINKS)
	            .and(Sanitizers.BLOCKS)
	            .and(Sanitizers.IMAGES)
	            .and(Sanitizers.STYLES)
	            .and ( Sanitizers.TABLES); 
    }
}
