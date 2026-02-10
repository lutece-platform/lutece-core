/*
 * Copyright (c) 2002-2025, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
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
import fr.paris.lutece.portal.service.init.StartUpService;
import fr.paris.lutece.util.ReferenceList;

/**
 * This class provides a service that offers methods to manage security headers. 
 */
public class SecurityHeaderService implements StartUpService
{

    /**
     * Cache of security headers
     */
    private static final class Cache
    {
        private final Collection<SecurityHeader> _securityHeaders;

        private final Map<String, Map<String, List<SecurityHeader>>> _mapActiveSecurityHeadersForFilters;

        private Cache( )
        {
            _securityHeaders = SecurityHeaderHome.findAll( );

            _mapActiveSecurityHeadersForFilters = new HashMap<String, Map<String, List<SecurityHeader>>>( );
            _securityHeaders.stream( ).filter( SecurityHeader::isActive ).forEach( securityHeader -> {
                _mapActiveSecurityHeadersForFilters
                        .computeIfAbsent( securityHeader.getType( ), t -> new HashMap<>( ) )
                        .computeIfAbsent( SecurityHeaderType.PAGE.getCode( ).equals( securityHeader.getType( ) )
                                ? securityHeader.getPageCategory( )
                                : null, c -> new ArrayList<>( ) )
                        .add( securityHeader );
            } );
        }

        /**
         * All cached security headers
         * 
         * @return all security headers
         */
        private Collection<SecurityHeader> getSecurityHeaders( )
        {
            return _securityHeaders;
        }

        /**
         * Cached active security headers, by type and page category
         * 
         * @return active security headers, by type and page category
         */
        private Map<String, Map<String, List<SecurityHeader>>> getMapActiveSecurityHeadersForFilters( )
        {
            return _mapActiveSecurityHeadersForFilters;
        }
    }

    /**
     * The current cache. Marked <code>volatile</code> so that if can be swapped
     * atomically with a fresher cache.
     * 
     * This field should be read only once for a coherent interaction group of actions.
     */
    private volatile Cache _cache;
	
	private final Logger _logger = LogManager.getLogger( "lutece.securityHeader" );
	
	/**
	 * Returns all the security headers that match specified name, type and page category.
	 * If page category is <code>null</code> (REST API case), this criterion is ignored.
	 * 
	 * @param strName
	 *           The name of the security header
	 * @param strType
	 *           The type of the security header
	 * @param strPageCategory
	 *           The page category of the security header
	 * @return list of security headers matching the criteria
	 */
	public List<SecurityHeader> find( String strName, String strType, String strPageCategory )
	{
		ArrayList<SecurityHeader> securityHeadersResultList = new ArrayList<SecurityHeader>( );
		
		for( SecurityHeader securityHeader : findAll( ) )
		{
			if( securityHeader.getName( ).equals( strName ) && securityHeader.getType( ).equals( strType ) )
			{
				if( strPageCategory == null || securityHeader.getPageCategory( ).equals( strPageCategory ) )
				{
					securityHeadersResultList.add( securityHeader );
				}				
			}
		}
		
		return securityHeadersResultList;
	}
	
    /**
     * Returns all security headers of the specified type and the specified
     * category (if not <code>null</code>) that are active (attribute is_enable
     * = true).
     * 
     * @param strType
     *            the type of security header to find
     * @param strPageCategory
     *            the page category of security header to find
     * @return collection on security headers
     */
    public Collection<SecurityHeader> findActive( String strType, String strPageCategory )
    {
        Map<String, Map<String, List<SecurityHeader>>> map = _cache.getMapActiveSecurityHeadersForFilters( );
        if ( map.get( strType ) == null )
        {
            return null;
        }
        return Collections.unmodifiableCollection( map.get( strType ).get( strPageCategory ) );
    }
	
	/**
	 * Returns a collection of all security headers.
	 * 
	 * @return collection of security headers
	 */
	public Collection<SecurityHeader> findAll( )
    {
		return Collections.unmodifiableCollection( _cache.getSecurityHeaders( ) );
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
	 * Create the security header given in parameter.
	 * 
	 * @param securityHeader
	 *                The security header to create
	 */
	public void create( SecurityHeader securityHeader )
	{
		_logger.debug( "Security header to create : name : {}, value : {}, type : {}, page category : {}", securityHeader.getName( ), securityHeader.getValue( ), securityHeader.getType( ), securityHeader.getPageCategory( ) );
        SecurityHeaderHome.create( securityHeader );
        _logger.debug( "Security header created" );
        refreshSecurityHeadersCache( );
	}

	/**
	 * Update the security header given in parameter.
	 * 
	 * @param securityHeader
	 *                The security header to update
	 */
	public void update( SecurityHeader securityHeader )
	{
		_logger.debug( "Security header to update : id : {}, name : {}, value : {}, type : {}, page category : {}", securityHeader.getId( ), securityHeader.getName( ), securityHeader.getValue( ), securityHeader.getType( ), securityHeader.getPageCategory( ) );
        SecurityHeaderHome.update( securityHeader );
        _logger.debug( "Security header updated" );
        refreshSecurityHeadersCache( );
	}

    /**
     * Remove the security header given in parameter.
     * 
     * @param securityHeader
     *            The security header to remove
     */
    public void remove( int nSecurityHeaderId )
    {
        logOperation( nSecurityHeaderId, "remove" );
        SecurityHeaderHome.remove( nSecurityHeaderId );
        _logger.debug( "Security header deleted" );
        refreshSecurityHeadersCache( );
    }

    /**
     * Enable the security header with the id given in parameter.
     * 
     * @param nSecurityHeaderId
     *            The id of the security header to enable
     */
    public void enable( int nSecurityHeaderId )
    {
        logOperation( nSecurityHeaderId, "enable" );
        SecurityHeaderHome.updateIsActive( nSecurityHeaderId, true );
        _logger.debug( "Security header enabled" );
        refreshSecurityHeadersCache( );
    }

    /**
     * Disable the security header with the id given in parameter.
     * 
     * @param nSecurityHeaderId
     *            The id of the security header to disable
     */
    public void disable( int nSecurityHeaderId )
    {
        logOperation( nSecurityHeaderId, "disable" );
        SecurityHeaderHome.updateIsActive( nSecurityHeaderId, false );
        _logger.debug( "Security header disabled" );
        refreshSecurityHeadersCache( );
    }

    private void logOperation( int nSecurityHeaderId, String strOperation )
    {
        if ( _logger.isDebugEnabled( ) )
        {
            SecurityHeader securityHeader = SecurityHeaderHome.findByPrimaryKey( nSecurityHeaderId );
            if ( securityHeader != null )
            {
                _logger.debug( "Security header to {} : id : {}, name : {}, value : {}, type : {}, page category : {}",
                        strOperation, securityHeader.getId( ), securityHeader.getName( ), securityHeader.getValue( ),
                        securityHeader.getType( ), securityHeader.getPageCategory( ) );
            }
            else
            {
                _logger.debug( "Non existent security header to disable : id : {}", nSecurityHeaderId );
            }
        }
    }

    /**
     * Refreshes the cache of security headers. After a call to this method, data
     * in the cache is up to date with database data.
     */
    private void refreshSecurityHeadersCache( )
    {
        _cache = new Cache( );
        _logger.debug( "Security headers cache refreshed" );
    }

    @Override
    public String getName( )
    {
        return "SecurityHeaderService";
    }

    /**
     * {@inheritDoc}
     * 
     * <p>This initialize the cache after the Spring context has been initialized,
     * so that all necessary infrastructure is available
     */
    @Override
    public void process( )
    {
        refreshSecurityHeadersCache( );
    }
}