package fr.paris.lutece.portal.service.util;

import javax.servlet.http.HttpServletRequest;

public class AppHTTPSService
{
	private static final String PROPERTY_HTTPS = "lutece.https.";
	private static final String PROPERTY_HTTPS_SUPPORT = PROPERTY_HTTPS + "support";
	private static final String PROPERTY_HTTPS_URL = PROPERTY_HTTPS + "url";
	
	/**
	 * Returns <b>true</b> if HTTPS is supported, <b>false</b> otherwise.
	 * @return <b>true</b> if HTTPS is supported, <b>false</b> otherwise.
	 */
	public static boolean isHTTPSSupportEnabled(  )
	{
		return "1".equals( AppPropertiesService.getProperty( PROPERTY_HTTPS_SUPPORT ) );
	}
	
	/**
	 * Returns the HTTPS base url for the site.
	 * @param request the request
	 * @return the HTTPS base url for the site.
	 */
	public static String getHTTPSUrl( HttpServletRequest request )
	{
		return AppPropertiesService.getProperty( PROPERTY_HTTPS_URL );
	}
}
