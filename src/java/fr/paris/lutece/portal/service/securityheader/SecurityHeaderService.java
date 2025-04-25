package fr.paris.lutece.portal.service.securityheader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.business.securityheader.SecurityHeader;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderHome;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderPageCategory;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderType;
import fr.paris.lutece.util.ReferenceList;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * This class provides a service that offers methods to manage security headers. 
 */
@ApplicationScoped
public class SecurityHeaderService 
{
	
	//This map contains the security headers loaded from the database. It prevents to make database calls when data are up to date.
	//It must be refreshed each time that an action is performed. Currently, These actions are creating a new header, modifying an 
	//existing header, removing a header and enabling or disabling a header
	private Map<String, SecurityHeader> _mapSecurityHeaders = new HashMap<String, SecurityHeader>( );
	
	Map<String, Map<String, List<SecurityHeader>>> _mapActiveSecurityHeadersForFilters = new HashMap<String, Map<String, List<SecurityHeader>>>( );
	
	//This boolean indicates if a refresh has been done on the security headers map. In other words, it tells if the map is up to date
	//with data from database
	private boolean _bMapRefreshDone = false;
	
	private Logger _logger = LogManager.getLogger( "lutece.securityHeader" );
	
	/**
	 * Returns all the security headers that match specified name, type and page category (if not null for the latter).
	 * If page category is null (REST api case), this criterion is ignored.
	 * 
	 * @param strName
	 *           The name of the security header
	 * @param strType
	 *           The type of the security header
	 * @param pageCategory
	 *           The page category of the security header
	 * @return list od security headers matching the criteria
	 */
	public List<SecurityHeader> find( String strName, String strType, String pageCategory )
	{
		ArrayList<SecurityHeader> securityHeadersResultList = new ArrayList<SecurityHeader>( );
		
		for( SecurityHeader securityHeader : findAll( ) )
		{
			if( securityHeader.getName( ).equals( strName ) && securityHeader.getType( ).equals( strType ) )
			{
				if( pageCategory == null || securityHeader.getPageCategory( ).equals( pageCategory ) )
				{
					securityHeadersResultList.add( securityHeader );
				}				
			}
		}
		
		return securityHeadersResultList;
	}
	
	/**
	 * Returns all security headers from the specified type and the specified category (if not null)
	 * that are active (attribute is_enable = true).
	 * 
	 * @param strType
	 * @param strPageCategory
	 * @return collection on security headers
	 */
	public Collection<SecurityHeader> findActive( String strType, String strPageCategory )
	{
		if( !_bMapRefreshDone )
		{
			refreshSecurityHeadersMap( SecurityHeaderHome.findAll() );
		}
		if( _mapActiveSecurityHeadersForFilters.get(strType) != null )
		{
			return _mapActiveSecurityHeadersForFilters.get( strType ).get( strPageCategory );
		}
		return null;
	}
	
	/**
	 * Returns a collection of all security headers.
	 * Security headers are fetched from database (if data are stale) or from security headers map in memory (if the data in map are still up to date).
	 * 
	 * @return collection of security headers
	 */
	public Collection<SecurityHeader> findAll( )
    {
		Collection<SecurityHeader> securityHeadersList = null;
		
		if( _bMapRefreshDone )
		{
			securityHeadersList = _mapSecurityHeaders.values( );
		} 
		else
		{			
			securityHeadersList = SecurityHeaderHome.findAll( );
			refreshSecurityHeadersMap( securityHeadersList );	
		}
		
		return securityHeadersList;
    }
	
	/**
	 * Returns a list of all security headers used in manage security headers page. They are sorted by type, page category and name.
	 * 
	 * @param locale
	 *           The locale
	 * @return list of all security headers
	 */
	public List<SecurityHeader> findAllSorted( Locale locale )
    {
		List<SecurityHeader> securityHeadersList = findAll( ).stream().collect( Collectors.toList( ) );   	
		Collections.sort( securityHeadersList, Comparator.comparing( SecurityHeader::getType )
				                                         .thenComparing( SecurityHeader::getPageCategory, Comparator.nullsLast( Comparator.naturalOrder( ) ) )
				                                         .thenComparing( SecurityHeader::getName ) );
    	
		return securityHeadersList;
    }
	
	/**
	 * Returns the reference list of security headers types
	 * 
	 * @return ReferenceList object with security headers types
	 */
	public ReferenceList getTypeList( )
	{
		ReferenceList listTypes = new ReferenceList( );
		
		for (SecurityHeaderType type : SecurityHeaderType.values()) { 
			listTypes.addItem( type.getCode(), type.getCode() );
		}
		
		return listTypes;
	}
	
	/**
	 * Returns the reference list of security headers page categories
	 * 
	 * @return ReferenceList object with security headers page categories
	 */
	public ReferenceList getPageCategoryList( )
	{
		ReferenceList listPageCategory = new ReferenceList( );
		
		for (SecurityHeaderPageCategory pageCategory : SecurityHeaderPageCategory.values()) { 
			listPageCategory.addItem( pageCategory.getCode(), pageCategory.getCode() );
		}
		
		return listPageCategory;
	}
	
	/**
	 * Create the security header given in parameter. Like all the actions, this one invalidates the map containing the security headers.
	 * 
	 * @param securityHeader
	 *                The security header to create
	 */
	public void create( SecurityHeader securityHeader )
	{
		_logger.debug( "Security header to create : name : {}, value : {}, type : {}, page category : {}", securityHeader.getName( ), securityHeader.getValue( ), securityHeader.getType( ), securityHeader.getPageCategory( ) );
		SecurityHeaderHome.create( securityHeader );
		clearMapSecurityHeaders( );
		_logger.debug( "Security header created" );
	}

	/**
	 * Update the security header given in parameter. Like all the actions, this one invalidates the map containing the security headers.
	 * 
	 * @param securityHeader
	 *                The security header to update
	 */
	public void update( SecurityHeader securityHeader )
	{
		_logger.debug( "Security header to update : id : {}, name : {}, value : {}, type : {}, page category : {}", securityHeader.getId( ), securityHeader.getName( ), securityHeader.getValue( ), securityHeader.getType( ), securityHeader.getPageCategory( ) );
		SecurityHeaderHome.update( securityHeader );
		clearMapSecurityHeaders( );
		_logger.debug( "Security header updated" );
	}
	
	/**
	 * Remove the security header given in parameter. Like all the actions, this one invalidates the map containing the security headers.
	 * 
	 * @param securityHeader
	 *                The security header to remove
	 */
	public void remove( int nSecurityHeaderId )
	{
		SecurityHeader securityHeader = _mapSecurityHeaders.get( String.valueOf( nSecurityHeaderId ) );
		_logger.debug( "Security header to delete : id : {}, name : {}, value : {}, type : {}, page category : {}", securityHeader.getId( ), securityHeader.getName( ), securityHeader.getValue( ), securityHeader.getType( ), securityHeader.getPageCategory( ) );
		SecurityHeaderHome.remove( nSecurityHeaderId );
		clearMapSecurityHeaders( );
		_logger.debug( "Security header deleted" );
	}
	
	/**
	 * Enable the security header with the id given in parameter. Like all the actions, this one invalidates the map containing the security headers.
	 * 
	 * @param nSecurityHeaderId
	 *                The id of the security header to enable
	 */
	public void enable( int nSecurityHeaderId )
	{
		SecurityHeader securityHeader = _mapSecurityHeaders.get( String.valueOf( nSecurityHeaderId ) );
		_logger.debug( "Security header to enable : id : {}, name : {}, value : {}, type : {}, page category : {}", securityHeader.getId( ), securityHeader.getName( ), securityHeader.getValue( ), securityHeader.getType( ), securityHeader.getPageCategory( ) );
		SecurityHeaderHome.updateIsActive( nSecurityHeaderId, true );
		clearMapSecurityHeaders( );
		_logger.debug( "Security header enabled" );
	}
	
	/**
	 * Disable the security header with the id given in parameter. Like all the actions, this one invalidates the map containing the security headers.
	 * 
	 * @param nSecurityHeaderId
	 *                The id of the security header to disable
	 */
	public void disable( int nSecurityHeaderId )
	{
		SecurityHeader securityHeader = _mapSecurityHeaders.get( String.valueOf( nSecurityHeaderId ) );
		_logger.debug( "Security header to disable : id : {}, name : {}, value : {}, type : {}, page category : {}", securityHeader.getId( ), securityHeader.getName( ), securityHeader.getValue( ), securityHeader.getType( ), securityHeader.getPageCategory( ) );
		SecurityHeaderHome.updateIsActive( nSecurityHeaderId, false );
		clearMapSecurityHeaders( );
		_logger.debug( "Security header disabled" );
	}
	
	/**
	 * Clears the map containing the security headers
	 * 
	 */
	private void clearMapSecurityHeaders( )
	{
		_mapSecurityHeaders.clear( );
		_mapActiveSecurityHeadersForFilters.clear( );
		_bMapRefreshDone = false;
		_logger.debug( "Security header maps cleared" );
	}
	
	/**
	 * Refreshes the map of security headers with the list given in parameter. After a call to this method, data of the map are up to date with database data.
	 * 
	 * @param securityHeadersList
	 *                 The security headers collection
	 */
	private void refreshSecurityHeadersMap( Collection<SecurityHeader> securityHeadersList )
	{
		for( SecurityHeader securityHeader : securityHeadersList )
		{
			_mapSecurityHeaders.put( String.valueOf( securityHeader.getId( ) ), securityHeader );
			
			if( securityHeader.isActive() )
			{
				_mapActiveSecurityHeadersForFilters.put( securityHeader.getType( ), addHeaderToTypeMap( securityHeader ) );
			}					
		}
		_bMapRefreshDone = true;
		_logger.debug( "Security header map refreshed" );
	}
	
	/**
	 * Adds a security header in the map of active security headers of the same type as the security header passed in parameter
	 * 
	 * @param securityHeader Security header to add to the map
	 * @return map of active security headers of the same type as the security header passed in parameter updated with this security header
	 */
	private Map<String, List<SecurityHeader>> addHeaderToTypeMap( SecurityHeader securityHeader )
	{
		//In _mapActiveSecurityHeadersForFilters, 2 keys are necessary to retrieve a list of security headers.
		//For Page headers, those keys are respectively type and page category
		//For Rest api headers, those keys are respectively type and null value. 
		//As page category is not relevant for rest api headers, they are grouped using the null key. 
		String firstKey = securityHeader.getType( );
		Map<String, List<SecurityHeader>> mapHeadersForType = _mapActiveSecurityHeadersForFilters.get( firstKey );
		List<SecurityHeader> headersListToUpdate = null;
		
		String secondKey = null;					
		if( securityHeader.getType( ).equals( SecurityHeaderType.PAGE.getCode( ) ) )
		{
			secondKey = securityHeader.getPageCategory( );
		}
		
		if( mapHeadersForType == null )
		{
			mapHeadersForType = new HashMap<String, List<SecurityHeader>>( );
			headersListToUpdate = new ArrayList<SecurityHeader>( );					
		}
		else
		{					
			headersListToUpdate = mapHeadersForType.get( secondKey );
			if( headersListToUpdate == null )
			{
				headersListToUpdate = new ArrayList<SecurityHeader>( );							
			}
		}
		headersListToUpdate.add( securityHeader );
		mapHeadersForType.put( secondKey, headersListToUpdate );
		
		return mapHeadersForType;
	}
}