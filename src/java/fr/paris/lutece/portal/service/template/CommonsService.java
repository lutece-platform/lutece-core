/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import fr.paris.lutece.portal.business.template.CommonsInclude;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import java.util.List;

/**
 * CommonsService
 */
public class CommonsService
{
    private static final String DSKEY_CURRENT_COMMONS_INCLUDE = "core.templates.currentCommonsInclude";
    private static final String DEFAULT_COMMONS_INCLUDE_KEY = "default";

    /**
     * Get the list of commons includes
     * 
     * @return The list
     */
    public static List<CommonsInclude> getCommonsIncludes( )
    {
        return SpringContextService.getBeansOfType( CommonsInclude.class );
    }

    /**
     * Activate a commons library
     * 
     * @param strKey The commons key
     */
    public static void activateCommons( String strKey )
    {
        IFreeMarkerTemplateService serviceFMT = FreeMarkerTemplateService.getInstance( );

        CommonsInclude ciNew = getCommonsInclude( strKey );

        if ( ciNew == null )
        {
            return;
        }

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
                    AppLogService.info( "Existing Freemarker AutoInclude removed : " + strExclude );
                }
            }
        }
        // Add auto-include that aren't already present
        for ( String strInclude : ciNew.getFiles( ) )
        {
            if ( ( listAutoIncludes != null ) && !listAutoIncludes.contains( strInclude ) )
            {
                serviceFMT.addAutoInclude( strInclude );
                AppLogService.info( "New Freemarker AutoInclude added : " + strInclude );
            }
        }

        setNewCommonsInclude( ciNew );
    }

    /**
     * Get the commons list
     * 
     * @return The list
     */
    public static ReferenceList getCommonsIncludeList( )
    {
        ReferenceList list = new ReferenceList( );
        for ( CommonsInclude ci : getCommonsIncludes( ) )
        {
            list.addItem( ci.getKey( ), ci.getName( ) );
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
        CommonsInclude ciCurrent = getCurrentCommonsInclude( );
        if ( ciCurrent != null )
        {
            return ciCurrent.getKey( );
        }
        return DEFAULT_COMMONS_INCLUDE_KEY;
    }

    /**
     * Get a commons include by its key
     * 
     * @param strKey The key
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
     * Get the current commons include
     * 
     * @return The commons include object
     */
    public static CommonsInclude getCurrentCommonsInclude( )
    {
        String strCurrentCommonsIncludeKey = DatastoreService.getInstanceDataValue( DSKEY_CURRENT_COMMONS_INCLUDE,
                DEFAULT_COMMONS_INCLUDE_KEY );
        return getCommonsInclude( strCurrentCommonsIncludeKey );
    }

    /**
     * Define the new commons include
     * 
     * @param ciNew the new commons include
     */
    private static void setNewCommonsInclude( CommonsInclude ciNew )
    {
        DatastoreService.setDataValue( DSKEY_CURRENT_COMMONS_INCLUDE, ciNew.getKey( ) );
    }

}
