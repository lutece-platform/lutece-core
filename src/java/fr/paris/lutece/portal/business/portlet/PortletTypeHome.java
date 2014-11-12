/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;
import java.util.Locale;


/**
 * This class provides instances management methods (create, find, ...) for PortletType objects
 */
public final class PortletTypeHome
{
    // Static variable pointed at the DAO instance
    private static IPortletTypeDAO _dao = (IPortletTypeDAO) SpringContextService.getBean( "portletTypeDAO" );

    /**
     * Creates a new PortletTypeHome object.
     */
    private PortletTypeHome(  )
    {
    }

    /**
     * Creation of an instance of an PortletType
     *
     * @param portletType An instance of the PortletType which contains the informations to store
     * @return The  instance of PortletType which has been created with its primary key.
     */
    public static PortletType create( PortletType portletType )
    {
        _dao.insert( portletType );

        return portletType;
    }

    /**
     * Remove the portlet type whose identifier is specified in parameter
     *
     * @param strPortletTypeId The identifier of the portlet type to remove
     */
    public static void remove( String strPortletTypeId )
    {
        _dao.delete( strPortletTypeId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an portletType whose identifier is specified in parameter
     *
     * @param strKey The Primary Key of the portletType
     * @return an instance of portletType
     */
    public static PortletType findByPrimaryKey( String strKey )
    {
        return _dao.load( strKey );
    }

    /**
     * Return the id of a portlet type
     *
     * @param strPluginHomeClass The identifier of the portlet
     * @return The order max
     */
    public static String getPortletTypeId( String strPluginHomeClass )
    {
        return _dao.selectPortletTypeId( strPluginHomeClass );
    }

    /**
     * Search the number of portlet type for a portlet
     *
     * @param strPortletTypeId The Type Id of portlet
     * @return int
     */
    public static int getNbPortletTypeByPortlet( String strPortletTypeId )
    {
        return _dao.selectNbPortletTypeByPortlet( strPortletTypeId );
    }

    /**
     * Returns the list of the portlet types
     *
     * @param locale The locale
     * @return the list of portlet types
     */
    public static ReferenceList getPortletsTypesList( Locale locale )
    {
        return _dao.selectPortletsTypesList( locale );
    }

    /**
     * Returns the list of the portlet types
     *
     * @param locale The locale
     * @return the list of portlet types
     */
    public static List<PortletType> getPortletTypesList( Locale locale )
    {
        List<PortletType> list = _dao.selectPortletTypesList(  );
        I18nService.localizeCollection( list, locale );

        return list;
    }
}
