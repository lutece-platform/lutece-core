package fr.paris.lutece.portal.business.securityheader;

/**
 * security headers pages categories
 * Determines on which pages the security headers must be applied :
 * ALL : all pages
 * LOGOUT_BO : Back office (admin) logout page
 * LOGOUT_FO : Front office (portal) logout page
 * AUTHENTICATED_ADMIN_BACK_OFFICE : Back office (admin) pages with authentication
 * AUTHENTICATED_ADMIN_FRONT_OFFICE : Front office (portal) pages with authentication
 * 
 */
public enum SecurityHeaderPageCategory
{
	ALL( "all" ),
	LOGOUT_BO( "logout_BO" ),
	LOGOUT_FO( "logout_FO" ),
	AUTHENTICATED_ADMIN_BACK_OFFICE( "auth_admin_BO" ),
	AUTHENTICATED_ADMIN_FRONT_OFFICE( "auth_admin_FO" );

	private final String _code;
	
	/**
	 * Constructor
	 * 
	 * @param code
	 *           The code
	 */
	private SecurityHeaderPageCategory( String code ) 
	{
		this._code = code;
	}
	
	/**
	 * Returns code
	 * 
	 * @return code
	 */
	public String getCode( )
	{
		return _code;
	}
}