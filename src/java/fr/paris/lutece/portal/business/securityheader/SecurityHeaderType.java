package fr.paris.lutece.portal.business.securityheader;

/**
 * security headers types
 * Security headers can be applied on 2 types of resources :
 * PAGE : html pages
 * REST_API : REST api endpoints
 */
public enum SecurityHeaderType
{
	PAGE( "page" ),
	REST_API( "rest_api" );

	private final String _code;
	
	/**
	 * Constructor
	 * 
	 * @param code
	 *           The code
	 */
	private SecurityHeaderType( String code ) 
	{
		this._code = code;
	}
	
	/**
	 * Returns code
	 * 
	 * @return code
	 */
	public String getCode()
	{
		return _code;
	}
}