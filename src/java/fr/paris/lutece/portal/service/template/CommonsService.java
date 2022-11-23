/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.portal.service.template;

import fr.paris.lutece.portal.business.template.CommonsImport;
import fr.paris.lutece.portal.business.template.CommonsInclude;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import jakarta.enterprise.inject.spi.CDI;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * CommonsService
 */
public class CommonsService
{
    private static final String DSKEY_CURRENT_COMMONS_INCLUDE = "core.templates.currentCommonsInclude";

    private CommonsService( )
    {
        // Ctor
    }

    /**
     * Get the list of commons includes
     * 
     * @return The list
     */
    public static List<CommonsInclude> getCommonsIncludes( )
    {
        return CDI.current().select(CommonsInclude.class).stream().toList();
    }

    /**
     * Get the list of commons imports
     * 
     * @return The list
     */
    public static List<CommonsImport> getCommonsImports( )
    {
        return SpringContextService.getBeansOfType( CommonsImport.class );
    }

    /**
     * Activate a commons library
     * 
     * @param strKey
     *            The commons key
     */
    public static void activateCommons( String strKey )
    {
        CommonsInclude comIncNew = getCommonsInclude( strKey );
        activateCommonsInclude( comIncNew );
        CommonsImport comImpNew = getCommonsImport( strKey );
        activateCommonsImport( comImpNew );

        if ( comIncNew != null || comImpNew != null )
        {
            setNewCommonsKey( strKey );
        }
    }

    /**
     * Activate a commons include
     * 
     * @param ciNew
     *            The new commons include
     */
    private static void activateCommonsInclude( CommonsInclude ciNew )
    {
        if ( ciNew == null )
        {
            return;
        }

        IFreeMarkerTemplateService serviceFMT = FreeMarkerTemplateService.getInstance( );

        CommonsInclude ciCurrent = getCurrentCommonsInclude( );

        // Remove auto-include of the current commons include
        List<String> listAutoIncludes = serviceFMT.getAutoIncludes( );
        if ( ciCurrent != null )
        {
            for ( String strExclude : ciCurrent.getFiles( ) )
            {
                if ( ( listAutoIncludes != null ) && listAutoIncludes.contains( strExclude ) )
                {
                    serviceFMT.removeAutoInclude( strExclude );
                    AppLogService.info( "Existing Freemarker AutoInclude removed : {}", strExclude );
                }
            }
        }
        // Add auto-include that aren't already present
        for ( String strInclude : ciNew.getFiles( ) )
        {
            if ( ( listAutoIncludes != null ) && !listAutoIncludes.contains( strInclude ) )
            {
                serviceFMT.addAutoInclude( strInclude );
                AppLogService.info( "New Freemarker AutoInclude added : {}", strInclude );
            }
        }
    }

    /**
     * Activate a commons import
     * 
     * @param ciNew
     *            The new commons import
     */
    private static void activateCommonsImport( CommonsImport ciNew )
    {
        if ( ciNew == null )
        {
            return;
        }

        IFreeMarkerTemplateService serviceFMT = FreeMarkerTemplateService.getInstance( );

        CommonsImport ciCurrent = getCurrentCommonsImport( );

        // Remove auto-import of the current commons import
        Map<String,String> mapAutoImports = serviceFMT.getAutoImports( );
        if ( ciCurrent != null )
        {
            for ( Map.Entry<String, String> mapFilesEntry : ciCurrent.getMapFiles( ).entrySet( ) )
            {
                serviceFMT.removeAutoImport( mapFilesEntry.getKey( ) );
                AppLogService.info( "Existing Freemarker AutoImport removed : {} as {}", mapFilesEntry.getValue( ), mapFilesEntry.getKey( ) );
            }
        }
        // Add auto-import that aren't already present
        for ( Map.Entry<String, String> mapFilesEntry : ciNew.getMapFiles( ).entrySet( ) )
        {
            serviceFMT.addAutoImport( mapFilesEntry.getKey( ), mapFilesEntry.getValue( ) );
            AppLogService.info( "New Freemarker AutoImport added : {} as {}", mapFilesEntry.getValue( ), mapFilesEntry.getKey( ) );
        }
    }

    /**
     * Get the commons list
     * 
     * @return The list
     */
    public static ReferenceList getCommonsList( )
    {
        ReferenceList list = new ReferenceList( );
        for ( CommonsInclude ci : getCommonsIncludes( ) )
        {
            list.addItem( ci.getKey( ), ci.getName( ) );
        }
        for ( CommonsImport ci : getCommonsImports( ) )
        {
            if ( !list.stream( ).anyMatch( item -> StringUtils.equals( item.getCode( ), ci.getKey( ) ) ) )
            {
                list.addItem( ci.getKey( ), ci.getName( ) );
            }
        }
        return list;
    }

    /**
     * Get the current commons key
     * 
     * @return The key
     */
    public static String getCurrentCommonsKey( )
    {
        String strCurrentKey = null;

        CommonsInclude comIncCurrent = getCurrentCommonsInclude( );

        if ( comIncCurrent != null )
        {
            strCurrentKey = comIncCurrent.getKey( );
        }

        CommonsImport comImpCurrent = getCurrentCommonsImport( );

        if ( comImpCurrent != null )
        {
            strCurrentKey = comImpCurrent.getKey( );
        }

        return strCurrentKey;
    }

    /**
     * Get a commons include by its key
     * 
     * @param strKey
     *            The key
     * @return The commons include object
     */
    public static CommonsInclude getCommonsInclude( String strKey )
    {
        for ( CommonsInclude ci : getCommonsIncludes( ) )
        {
            if ( ci.getKey( ).equals( strKey ) )
            {
                return ci;
            }
        }
        return null;
    }

    /**
     * Get a commons import by its key
     * 
     * @param strKey
     *            The key
     * @return The commons import object
     */
    public static CommonsImport getCommonsImport( String strKey )
    {
        for ( CommonsImport ci : getCommonsImports( ) )
        {
            if ( ci.getKey( ).equals( strKey ) )
            {
                return ci;
            }
        }
        return null;
    }

    /**
     * Get the default commons include
     * 
     * @return The commons include object
     */
    public static CommonsInclude getDefaultCommonsInclude( )
    {
        // get default commons include
        for ( CommonsInclude ci : getCommonsIncludes( ) )
        {
            if ( ci.isDefault( ) )
            {
                return ci;
            }
        }

        // if there's no default, returns the first one
        if ( getCommonsIncludes( ).size( ) > 0 )
        {
            return getCommonsIncludes( ).get( 0 );
        }

        return null;
    }

    /**
     * Get the default commons import
     * 
     * @return The commons import object
     */
    public static CommonsImport getDefaultCommonsImport( )
    {
        // get default commons import
        for ( CommonsImport ci : getCommonsImports( ) )
        {
            if ( ci.isDefault( ) )
            {
                return ci;
            }
        }

        // if there's no default, returns the first one
        if ( getCommonsImports( ).size( ) > 0 )
        {
            return getCommonsImports( ).get( 0 );
        }

        return null;
    }

    /**
     * Get the current commons include
     * 
     * @return The commons include object
     */
    public static CommonsInclude getCurrentCommonsInclude( )
    {
        String strCurrentCommonsKey = DatastoreService.getInstanceDataValue( DSKEY_CURRENT_COMMONS_INCLUDE, null );

        if ( strCurrentCommonsKey != null )
        {
            CommonsInclude ci = getCommonsInclude( strCurrentCommonsKey );
            if ( ci != null )
            {
                return ci;
            }
        }

        CommonsInclude ci = getDefaultCommonsInclude( );
        if ( ci != null )
        {
            setNewCommonsKey( ci.getKey( ) );
            return ci;
        }

        return null;
    }

    /**
     * Get the current commons import
     * 
     * @return The commons import object
     */
    public static CommonsImport getCurrentCommonsImport( )
    {
        String strCurrentCommonsKey = DatastoreService.getInstanceDataValue( DSKEY_CURRENT_COMMONS_INCLUDE, null );

        if ( strCurrentCommonsKey != null )
        {
            CommonsImport ci = getCommonsImport( strCurrentCommonsKey );
            if ( ci != null )
            {
                return ci;
            }
        }

        CommonsImport ci = getDefaultCommonsImport( );
        if ( ci != null )
        {
            setNewCommonsKey( ci.getKey( ) );
            return ci;
        }

        return null;
    }

    /**
     * Define the new commons key
     * 
     * @param strNewKey
     *            the new commons key
     */
    private static void setNewCommonsKey( String strNewKey )
    {
        DatastoreService.setDataValue( DSKEY_CURRENT_COMMONS_INCLUDE, strNewKey );
    }

}
