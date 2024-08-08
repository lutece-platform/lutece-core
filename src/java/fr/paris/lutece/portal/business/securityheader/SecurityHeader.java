package fr.paris.lutece.portal.business.securityheader;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * This class represents a HTTP security header. It is mainly defined by a name and a value. The type is the resource on which the security 
 * is added to the HTTP request (html page or rest api). When the type is equals to page, it is mandatory to specify on which page (category)
 * the header must be applied. The different categories are all pages, Back Office admin authenticated pages, Back Office logout page, Front 
 * Office admin authenticated pages and Front Office logout page 
 */
public class SecurityHeader 
{
	// Variables declarations
	private int _nId;
	
	@NotEmpty( message = "portal.securityheader.validation.securityheader.Name.notEmpty" )
	@Size( max = 60 , message = "portal.securityheader.validation.securityheader.Name.size" )
	private String _strName;
	
	@NotEmpty( message = "portal.securityheader.validation.securityheader.Value.notEmpty" )
	@Size( max = 1024 , message = "portal.securityheader.validation.securityheader.Value.size" )
	private String _strValue;
	
	@Size( max = 1024 , message = "portal.securityheader.validation.securityheader.Description.size" )
	private String _strDescription;
	
	@NotEmpty( message = "portal.securityheader.validation.securityheader.Type.notEmpty" )
	@Size( max = 10 , message = "portal.securityheader.validation.securityheader.Type.size" )
	private String _strType;
	
	@Size( max = 25 , message = "portal.securityheader.validation.securityheader.PageCategory.size" )
	private String _strPageCategory;	
	
	private boolean _bIsActive;
	
	private String _strTypeWording;
	
	private String _strPageCategoryWording;

	/**
     * Returns the Id
     *
     * @return The Id
     */
	public int getId( ) 
	{
		return _nId;
	}

	/**
     * Sets the Id
     *
     * @param nId
     *            The Id
     */
	public void setId( int nId ) 
	{
		this._nId = nId;
	}
	
	/**
     * Returns the name of the security header
     *
     * @return the security header name as a String
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * Sets the name of the security header
     *
     * @param strName
     *            The security header name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }
    
    /**
     * Returns the value of the security header
     *
     * @return the security header value as a String
     */
    public String getValue( )
    {
        return _strValue;
    }

    /**
     * Sets the value of the security header
     *
     * @param strValue
     *            The security header value
     */
    public void setValue( String strValue )
    {
        _strValue = strValue;
    }
    
    /**
     * Returns the description of the security header
     *
     * @return the security header description as a String
     */
    public String getDescription( ) 
    {
		return _strDescription;
	}

    /**
     * Sets the description of the security header
     *
     * @param strDescription
     *            The security header description
     */
	public void setDescription( String strDescription ) 
	{
		this._strDescription = strDescription;
	}
    
	/**
     * Returns the type of the security header
     *
     * @return the security header type as a String
     */
    public String getType( ) 
    {
		return _strType;
	}

    /**
     * Sets the type of the security header
     *
     * @param strType
     *            The security header type
     */
	public void setType( String strType ) 
	{
		this._strType = strType;
	}

	/**
     * Returns the page category of the security header
     *
     * @return the security header page category as a String
     */
	public String getPageCategory( ) 
	{
		return _strPageCategory;
	}

	/**
     * Sets the page category of the security header
     *
     * @param strPageCategory
     *            The security header page category
     */
	public void setPageCategory( String strPageCategory ) 
	{
		this._strPageCategory = strPageCategory;
	}
	
	/**
     * Indicates if the security is active or not
     *
     * @return true if the security header is active, false otherwise
     */
	public boolean isActive( )
	{
		return _bIsActive;
	}

	/**
     * Activate or deactivate the security header
     *
     * @param isActive
     *            true if the security header is active, false otherwise
     */
	public void setActive( boolean isActive )
	{
		this._bIsActive = isActive;
	}
	
	/**
     * Returns the security header type on a more user-friendly form (wording) used to be displayed in manage security headers page
     *
     * @return the security header type wording as a String
     */
	public String getTypeWording( ) 
	{
		return _strTypeWording;
	}

	/**
     * Sets the type wording of the security header
     *
     * @param strTypeWording
     *            The security header type wording
     */
	public void setTypeWording( String strTypeWording ) 
	{
		this._strTypeWording = strTypeWording;
	}
	
	/**
     * Returns the security header page category on a more user-friendly form (wording) used to be displayed in manage security headers page
     *
     * @return the security header page category wording as a String
     */
	public String getPageCategoryWording( ) 
	{
		return _strPageCategoryWording;
	}

	/**
     * Sets the page category wording of the security header
     *
     * @param strTypeWording
     *            The security header type wording
     */
	public void setPageCategoryWording( String strPageCategoryWording ) 
	{
		this._strPageCategoryWording = strPageCategoryWording;
	}
}
