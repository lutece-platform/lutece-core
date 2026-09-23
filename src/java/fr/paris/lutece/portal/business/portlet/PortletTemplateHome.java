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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.util.ReferenceList;

import java.util.List;

import jakarta.enterprise.inject.spi.CDI;

/**
 * Static facade of the portlet templates : the FreeMarker templates a portlet of a given type can be rendered with
 */
public final class PortletTemplateHome
{
    /** The template identifier meaning "no template chosen" : the portlet renders with the default template of its type */
    public static final int NO_TEMPLATE_ID = 0;

    private static IPortletTemplateDAO _dao = CDI.current( ).select( IPortletTemplateDAO.class ).get( );

    /**
     * Private constructor
     */
    private PortletTemplateHome( )
    {
    }

    /**
     * Creates a template
     *
     * @param template
     *            the template
     * @return the template, with its identifier
     */
    public static PortletTemplate create( PortletTemplate template )
    {
        _dao.insert( template );

        return template;
    }

    /**
     * Updates a template
     *
     * @param template
     *            the template
     * @return the template
     */
    public static PortletTemplate update( PortletTemplate template )
    {
        _dao.store( template );

        return template;
    }

    /**
     * Removes a template
     *
     * @param nKey
     *            the template identifier
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey );
    }

    /**
     * Finds a template
     *
     * @param nKey
     *            the template identifier
     * @return the template, null when it does not exist
     */
    public static PortletTemplate findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Returns every template, ordered by portlet type
     *
     * @return the templates
     */
    public static List<PortletTemplate> findAll( )
    {
        return _dao.selectAll( );
    }

    /**
     * Returns the templates registered for a portlet type
     *
     * @param strPortletTypeId
     *            the portlet type identifier
     * @return the templates, ordered by identifier
     */
    public static List<PortletTemplate> findByPortletType( String strPortletTypeId )
    {
        return _dao.selectByPortletType( strPortletTypeId );
    }

    /**
     * Returns the templates whose identifiers are given. It is serializable as a method reference, which lets a session pager load a page of templates
     * from their identifiers.
     *
     * @param listIdTemplates
     *            the template identifiers
     * @return the templates, ordered by portlet type then identifier
     */
    public static List<PortletTemplate> findByPrimaryKeyList( List<Integer> listIdTemplates )
    {
        return _dao.selectByPrimaryKeyList( listIdTemplates );
    }

    /**
     * Returns the identifiers of every template
     *
     * @return the identifiers, ordered by portlet type then identifier
     */
    public static List<Integer> findAllIds( )
    {
        return _dao.selectAllIds( );
    }

    /**
     * Returns the identifiers of the templates registered for a portlet type
     *
     * @param strPortletTypeId
     *            the portlet type identifier
     * @return the identifiers, ordered by identifier
     */
    public static List<Integer> findIdsByPortletType( String strPortletTypeId )
    {
        return _dao.selectIdsByPortletType( strPortletTypeId );
    }

    /**
     * Returns the templates registered for a portlet type as a reference list for a select
     *
     * @param strPortletTypeId
     *            the portlet type identifier
     * @return the reference list (template identifier, description)
     */
    public static ReferenceList findReferenceList( String strPortletTypeId )
    {
        ReferenceList list = new ReferenceList( );

        for ( PortletTemplate template : findByPortletType( strPortletTypeId ) )
        {
            list.addItem( template.getId( ), template.getDescription( ) );
        }

        return list;
    }

    /**
     * Returns the templates as a reference list for a select, whatever their portlet type
     *
     * @return the reference list (template identifier, description)
     */
    public static ReferenceList findReferenceList( )
    {
        ReferenceList list = new ReferenceList( );

        for ( PortletTemplate template : findAll( ) )
        {
            list.addItem( template.getId( ), template.getDescription( ) );
        }

        return list;
    }

    /**
     * Returns the path of the template chosen for a portlet, or a default path when the portlet has no valid template.
     * <p>
     * A template is valid when it exists, has a path and applies to the portlet type : a template removed from the back office, or a template of another
     * type, never breaks a page.
     *
     * @param portlet
     *            the portlet
     * @param strDefaultTemplatePath
     *            the path used when the portlet has no valid template
     * @return the template path, relative to the WEB-INF/templates directory
     */
    public static String getTemplatePath( Portlet portlet, String strDefaultTemplatePath )
    {
        if ( portlet.getIdTemplate( ) != NO_TEMPLATE_ID )
        {
            PortletTemplate template = findByPrimaryKey( portlet.getIdTemplate( ) );

            if ( template != null && template.getTemplatePath( ) != null && !template.getTemplatePath( ).trim( ).isEmpty( )
                    && ( template.getPortletTypeId( ) == null || template.getPortletTypeId( ).equals( portlet.getPortletTypeId( ) ) ) )
            {
                return template.getTemplatePath( );
            }
        }

        return strDefaultTemplatePath;
    }

    /**
     * Tells whether at least one portlet uses a template
     *
     * @param nKey
     *            the template identifier
     * @return true when a portlet uses the template
     */
    public static boolean isTemplateUsed( int nKey )
    {
        return _dao.checkTemplateIsUsed( nKey );
    }
}
