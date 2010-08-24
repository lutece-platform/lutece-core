package fr.paris.lutece.portal.business.user.attribute;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * 
 * AdminUserFieldFilter
 *
 */
public class AdminUserFieldFilter 
{
	// CONSTANTS
	private static final String EMPTY_STRING = "";
	private static final String CONSTANT_ESPERLUETTE = "&";
	private static final String CONSTANT_EQUAL = "=";
	
	// PARAMETERS
	private static final String PARAMETER_SEARCH_IS_SEARCH = "search_is_search";
	private static final String PARAMETER_ATTRIBUTE = "attribute_";
	
	// PROPERTIES
	private static final String PROPERTY_ENCODING_URL = "lutece.encoding.url";
	
	private List<AdminUserField> _listUserFields;
	
	/**
	 * Get list user fields
	 * @return list user fields
	 */
	public List<AdminUserField> getListUserFields(  )
	{
		return _listUserFields;
	}
	
	/**
	 * Set list user fields
	 * @param listUserFields list user fields
	 */
	public void setListUserFields( List<AdminUserField> listUserFields )
	{
		_listUserFields = listUserFields;
	}
	
	/**
	 * Set admin user field filter
	 * @param request HttpServletRequest
	 * @param locale locale
	 */
	public void setAdminUserFieldFilter( HttpServletRequest request, Locale locale )
	{
		_listUserFields = new ArrayList<AdminUserField>(  );
		String strIsSearch = request.getParameter( PARAMETER_SEARCH_IS_SEARCH );
		
		if ( strIsSearch != null )
		{
			// Attributes created in the Back-Office
			List<AbstractAttribute> listAttributes = AttributeHome.findCoreAttributes( locale );
	        for ( AbstractAttribute attribute : listAttributes )
	        {
	        	for ( AdminUserField userField : attribute.getUserFieldsData( request, null ) )
	        	{
	        		if ( userField != null && !userField.getValue(  ).equals( EMPTY_STRING ) )
	        		{
	        			_listUserFields.add( userField );
	        		}
	        	}
	        }
	        
		}
	}

	/**
     * Build url attributes
     * @param the url
     */
    public void setUrlAttributes( UrlItem url )
    {
    	for ( AdminUserField userField : _listUserFields )
    	{
    		try
        	{
        		url.addParameter( PARAMETER_ATTRIBUTE + userField.getAttribute(  ).getIdAttribute(  ), 
        				URLEncoder.encode( userField.getValue(  ), AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
        	}
        	catch( UnsupportedEncodingException e )
        	{
        		e.printStackTrace(  );
        	}
    	}
    }
    
    /**
     * Build url attributes
     * @return the url attributes
     */
    public String getUrlAttributes(  )
    {
    	StringBuilder sbUrlAttributes = new StringBuilder(  );
    	
    	for ( AdminUserField userField : _listUserFields )
    	{
    		try
        	{
        		sbUrlAttributes.append( CONSTANT_ESPERLUETTE + PARAMETER_ATTRIBUTE + userField.getAttribute(  ).getIdAttribute(  ) + CONSTANT_EQUAL +
        				URLEncoder.encode( userField.getValue(  ), AppPropertiesService.getProperty( PROPERTY_ENCODING_URL ) ) );
        	}
        	catch( UnsupportedEncodingException e )
        	{
        		e.printStackTrace(  );
        	}
    	}
    	
    	return sbUrlAttributes.toString(  );
    }
}
