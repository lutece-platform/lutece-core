package fr.paris.lutece.portal.service.securityheader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.business.securityheader.SecurityHeader;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderConfigHome;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderHome;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderPageCategory;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderType;
import fr.paris.lutece.portal.service.cache.Lutece107Cache;
import fr.paris.lutece.portal.service.cache.LuteceCache;
import fr.paris.lutece.util.ReferenceList;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

/**
 * This class provides a service that offers methods to manage security headers. 
 */
@ApplicationScoped
public class SecurityHeaderService 
{
	// Rebuilt as a whole then swapped: the filters read it on every request while another thread may refresh it
	volatile Map<String, Map<String, List<SecurityHeader>>> _mapActiveSecurityHeadersByType = new HashMap<String, Map<String, List<SecurityHeader>>>( );
	
	private static final String CACHE_SERVICE_NAME = "SecurityHeaderCacheService";
	// Cache entry written after the security headers: its value identifies the load. Reading one key is served by the near cache of a
	// distributed cache, whereas testing that the cache is empty iterated all of its entries on every request.
	static final String KEY_LOADED = "securityHeaders.loaded";
	// Load of the cache that _mapActiveSecurityHeadersByType reflects: another value means another instance reloaded the headers
	private volatile String _strLoadedStamp;
	
	@Inject
	@LuteceCache( cacheName = CACHE_SERVICE_NAME, keyType = String.class, valueType = SecurityHeader.class, enable = true )
	private Lutece107Cache<String, SecurityHeader> _securityHeaderCache;
	
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
	 * @return list of security headers matching the criteria
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
		if( !_securityHeaderCache.isCacheEnable( ) )
		{		
			refreshActiveSecurityHeadersMapByType( SecurityHeaderHome.findAll( ) );
		} 
		else
		{
			SecurityHeader loaded = _securityHeaderCache.get( KEY_LOADED );
			if( loaded == null )
			{
				Collection<SecurityHeader> securityHeadersList = SecurityHeaderHome.findAll( );
				String strStamp = refreshCache( securityHeadersList );
				refreshActiveSecurityHeadersMapByType( securityHeadersList );
				_strLoadedStamp = strStamp;
			}
			else if( !loaded.getValue( ).equals( _strLoadedStamp ) )
			{
				// Headers reloaded by another instance after a change
				Collection<SecurityHeader> securityHeadersList = SecurityHeaderHome.findAll( );
				securityHeadersList.forEach( securityHeader -> securityHeader
						.setSecurityHeaderConfigItemList( SecurityHeaderConfigHome.findBySecurityHeaderId( securityHeader.getId( ) ) ) );
				refreshActiveSecurityHeadersMapByType( securityHeadersList );
				_strLoadedStamp = loaded.getValue( );
			}
		}
		
		Map<String, List<SecurityHeader>> mapHeadersForType = _mapActiveSecurityHeadersByType.get( strType );
		if( mapHeadersForType != null )
		{
			return mapHeadersForType.get( strPageCategory );
		}
		return null;
	}
	
	/**
	 * Returns a collection of all security headers.
	 * Security headers are fetched from database (if data are stale) or from security headers map in memory (if the data in map are still up to date).
	 * 
	 * @return collection of security headers
	 */
	private Collection<SecurityHeader> findAll( )
    {
		Collection<SecurityHeader> securityHeadersList = null;
		
		if( _securityHeaderCache.isCacheEnable( ) )
		{
			securityHeadersList = new ArrayList<SecurityHeader>( );
			for( String key : _securityHeaderCache.getKeys( ) )
			{
				if( KEY_LOADED.equals( key ) )
				{
					continue;
				}
				SecurityHeader securityHeader = _securityHeaderCache.get( key );
				if( securityHeader != null )
				{
					securityHeadersList.add( securityHeader );
				}				
			}
		} 
		else
		{			
			securityHeadersList = SecurityHeaderHome.findAll( );
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
		List<SecurityHeader> securityHeadersList = findAll( ).stream( ).collect( Collectors.toList( ) );   	
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
		
		for ( SecurityHeaderType type : SecurityHeaderType.values( ) ) 
		{ 
			listTypes.addItem( type.getCode( ), type.getCode( ) );
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
		
		for (SecurityHeaderPageCategory pageCategory : SecurityHeaderPageCategory.values( ) ) 
		{ 
			listPageCategory.addItem( pageCategory.getCode( ), pageCategory.getCode( ) );
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
		_logger.debug( "Security header created" );
		
		if( _securityHeaderCache.isCacheEnable( ) )
		{		
			_securityHeaderCache.resetCache( );
			_logger.debug( "Security header cache cleared" );
		}	
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
		_logger.debug( "Security header updated" );
		
		if( _securityHeaderCache.isCacheEnable( ) )
		{		
			_securityHeaderCache.resetCache( );
			_logger.debug( "Security header cache cleared" );
		}
	}
	
	/**
	 * Remove the security header given in parameter. Like all the actions, this one invalidates the map containing the security headers.
	 * 
	 * @param securityHeader
	 *                The security header to remove
	 */
	public void remove( int nSecurityHeaderId )
	{
		SecurityHeader securityHeader = getSecurityHeader( nSecurityHeaderId );
		_logger.debug( "Security header to delete : id : {}, name : {}, value : {}, type : {}, page category : {}", securityHeader.getId( ), securityHeader.getName( ), securityHeader.getValue( ), securityHeader.getType( ), securityHeader.getPageCategory( ) );
		
		SecurityHeaderHome.remove( nSecurityHeaderId );
		_logger.debug( "Security header deleted" );
		
		if( _securityHeaderCache.isCacheEnable( ) )
		{		
			_securityHeaderCache.resetCache( );
			_logger.debug( "Security header cache cleared" );
		}
	}
	
	/**
	 * Enable the security header with the id given in parameter. Like all the actions, this one invalidates the map containing the security headers.
	 * 
	 * @param nSecurityHeaderId
	 *                The id of the security header to enable
	 */
	public void enable( int nSecurityHeaderId )
	{
		SecurityHeader securityHeader = getSecurityHeader( nSecurityHeaderId );
		_logger.debug( "Security header to enable : id : {}, name : {}, value : {}, type : {}, page category : {}", securityHeader.getId( ), securityHeader.getName( ), securityHeader.getValue( ), securityHeader.getType( ), securityHeader.getPageCategory( ) );
		
		SecurityHeaderHome.updateIsActive( nSecurityHeaderId, true );
		_logger.debug( "Security header enabled" );
		
		if( _securityHeaderCache.isCacheEnable( ) )
		{		
			_securityHeaderCache.resetCache( );
			_logger.debug( "Security header cache cleared" );
		}
	}
	
	/**
	 * Disable the security header with the id given in parameter. Like all the actions, this one invalidates the map containing the security headers.
	 * 
	 * @param nSecurityHeaderId
	 *                The id of the security header to disable
	 */
	public void disable( int nSecurityHeaderId )
	{
		SecurityHeader securityHeader = getSecurityHeader( nSecurityHeaderId );
		_logger.debug( "Security header to disable : id : {}, name : {}, value : {}, type : {}, page category : {}", securityHeader.getId( ), securityHeader.getName( ), securityHeader.getValue( ), securityHeader.getType( ), securityHeader.getPageCategory( ) );
		
		SecurityHeaderHome.updateIsActive( nSecurityHeaderId, false );
		_logger.debug( "Security header disabled" );
		
		if( _securityHeaderCache.isCacheEnable( ) )
		{		
			_securityHeaderCache.resetCache( );
			_logger.debug( "Security header cache cleared" );
		}
	}
	
	/**
	 * Refreshes the security headers cache with the list given in parameter. After a call to this method, data of the cache are up to date with database data.
	 * 
	 * @param securityHeadersList
	 *                 The security headers collection
	 * @return the value of the load marker written in the cache
	 */
	private String refreshCache( Collection<SecurityHeader> securityHeadersList )
	{
		for( SecurityHeader securityHeader : securityHeadersList )
		{
			securityHeader.setSecurityHeaderConfigItemList( SecurityHeaderConfigHome.findBySecurityHeaderId( securityHeader.getId( ) ) );
			_securityHeaderCache.put( String.valueOf( securityHeader.getId( ) ), securityHeader );				
		}
		SecurityHeader loaded = new SecurityHeader( );
		loaded.setId( -1 );
		loaded.setName( KEY_LOADED );
		loaded.setValue( UUID.randomUUID( ).toString( ) );
		loaded.setActive( false );
		_securityHeaderCache.put( KEY_LOADED, loaded );
		_logger.debug( "Security header cache refreshed" );
		return loaded.getValue( );
	}
	
	/**
	 * Refreshes the map of security headers with the list given in parameter. After a call to this method, data of the map are up to date with database data.
	 * 
	 * @param securityHeadersList
	 *                 The security headers collection
	 */
	private void refreshActiveSecurityHeadersMapByType( Collection<SecurityHeader> securityHeadersList )
	{
		Map<String, Map<String, List<SecurityHeader>>> mapActiveSecurityHeadersByType = new HashMap<>( );
		for( SecurityHeader securityHeader : securityHeadersList )
		{			
			if( securityHeader.isActive( ) )
			{
				mapActiveSecurityHeadersByType.put( securityHeader.getType( ), addHeaderToTypeMap( mapActiveSecurityHeadersByType, securityHeader ) );
			}					
		}
		_mapActiveSecurityHeadersByType = mapActiveSecurityHeadersByType;
		_logger.debug( "Security header map refreshed" );
	}
	
	/**
	 * Adds a security header in the map of active security headers of the same type as the security header passed in parameter
	 * 
	 * @param mapActiveSecurityHeadersByType Map of active security headers being built
	 * @param securityHeader Security header to add to the map
	 * @return map of active security headers of the same type as the security header passed in parameter updated with this security header
	 */
	private Map<String, List<SecurityHeader>> addHeaderToTypeMap( Map<String, Map<String, List<SecurityHeader>>> mapActiveSecurityHeadersByType,
			SecurityHeader securityHeader )
	{
		//In _mapActiveSecurityHeadersByType, 2 keys are necessary to retrieve a list of security headers.
		//For Page headers, those keys are respectively type and page category
		//For Rest api headers, those keys are respectively type and null value. 
		//As page category is irrelevant for rest api headers, they are grouped using the null key. 
		String firstKey = securityHeader.getType( );
		Map<String, List<SecurityHeader>> mapHeadersForType = mapActiveSecurityHeadersByType.get( firstKey );
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
	
	/**
	 * Get security header from cache if cache is enabled, from database otherwise
	 * 
	 * @param nSecurityHeaderId
	 * @return securityHeader
	 */
	private SecurityHeader getSecurityHeader( int nSecurityHeaderId )
	{
		SecurityHeader securityHeader = null;
		if( _securityHeaderCache.isCacheEnable( ) )
		{
			securityHeader = (SecurityHeader) _securityHeaderCache.get( String.valueOf( nSecurityHeaderId ) );
		}
		if( securityHeader == null )
		{
			// Not cached: the cache is reset by every change, on any instance
			securityHeader = SecurityHeaderHome.findByPrimaryKey( nSecurityHeaderId );
		}
		return securityHeader;
	}
	
	/**
	 * Get security header cache
	 * 
	 * @return security header cache
	 */
	public Lutece107Cache<String, SecurityHeader> getSecurityHeaderCache( )
	{
		return _securityHeaderCache;
	}
	
	/**
     * This method observes the initialization of the {@link ApplicationScoped} context.
     * It ensures that this CDI beans are instantiated at the application startup.
     *
     * <p>This method is triggered automatically by CDI when the {@link ApplicationScoped} context is initialized,
     * which typically occurs during the startup of the application server.</p>
     *
     * @param context the {@link ServletContext} that is initialized. This parameter is observed
     *                and injected automatically by CDI when the {@link ApplicationScoped} context is initialized.
     */
    public void initializedService(@Observes @Initialized(ApplicationScoped.class) ServletContext context) {
        // This method is intentionally left empty to trigger CDI bean instantiation
    }
}