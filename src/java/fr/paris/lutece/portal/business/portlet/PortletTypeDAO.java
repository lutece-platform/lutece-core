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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class provides Data Access methods for PortletType objects
 */
public final class PortletTypeDAO implements IPortletTypeDAO
{
    // sql queries
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_portlet_type ( id_portlet_type , name , url_creation , "
            + " url_update , home_class , plugin_name , " + " url_docreate , create_script , create_specific , create_specific_form , "
            + " url_domodify , modify_script , modify_specific , modify_specific_form )" + " VALUES ( ?, ? ,? ,?, ?, ?, ?, ? ,? ,?, ?, ?, ? , ? )";
    private static final String SQL_QUERY_SELECT = " SELECT id_portlet_type , name , url_creation , url_update , home_class, plugin_name, "
            + " url_docreate , create_script , create_specific , create_specific_form , "
            + " url_domodify , modify_script , modify_specific , modify_specific_form " + " FROM core_portlet_type WHERE id_portlet_type = ? ORDER BY name ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_portlet_type WHERE id_portlet_type = ?";
    private static final String SQL_QUERY_SELECT_PORTLET_TYPE_ID = "SELECT id_portlet_type FROM core_portlet_type WHERE home_class = ?";
    private static final String SQL_QUERY_SELECT_NB_PORTLET_TYPE_BY_PORTLET = " SELECT count(*) FROM core_portlet WHERE id_portlet_type = ? ";
    private static final String SQL_QUERY_SELECT_PORTLETS_TYPE_LIST = " SELECT id_portlet_type , name FROM core_portlet_type ORDER BY name ";
    private static final String SQL_QUERY_SELECT_PORTLET_TYPE_LIST = "SELECT id_portlet_type , name , url_creation, url_update FROM core_portlet_type ORDER BY name";

    // /////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data

    /**
     * Insert a new record in the table PortletType
     * 
     * @param portletType
     *            The portlet Type object
     */
    public void insert( PortletType portletType )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            daoUtil.setString( 1, portletType.getId( ) );
            daoUtil.setString( 2, portletType.getNameKey( ) );
            daoUtil.setString( 3, portletType.getUrlCreation( ) );
            daoUtil.setString( 4, portletType.getUrlUpdate( ) );
            daoUtil.setString( 5, portletType.getHomeClass( ) );
            daoUtil.setString( 6, portletType.getPluginName( ) );
            daoUtil.setString( 7, portletType.getDoCreateUrl( ) );
            daoUtil.setString( 8, portletType.getCreateScriptTemplate( ) );
            daoUtil.setString( 9, portletType.getCreateSpecificTemplate( ) );
            daoUtil.setString( 10, portletType.getCreateSpecificFormTemplate( ) );
            daoUtil.setString( 11, portletType.getDoModifyUrl( ) );
            daoUtil.setString( 12, portletType.getModifyScriptTemplate( ) );
            daoUtil.setString( 13, portletType.getModifySpecificTemplate( ) );
            daoUtil.setString( 14, portletType.getModifySpecificFormTemplate( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * Load the data of PortletType from the table
     * 
     * @param strPortletTypeId
     *            The identifier of PortletType
     * @return The instance of the PortletType
     */
    public PortletType load( String strPortletTypeId )
    {
        PortletType portletType = new PortletType( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setString( 1, strPortletTypeId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                portletType.setId( daoUtil.getString( 1 ) );
                portletType.setNameKey( daoUtil.getString( 2 ) );
                portletType.setUrlCreation( daoUtil.getString( 3 ) );
                portletType.setUrlUpdate( daoUtil.getString( 4 ) );
                portletType.setHomeClass( daoUtil.getString( 5 ) );
                portletType.setPluginName( daoUtil.getString( 6 ) );
                portletType.setDoCreateUrl( daoUtil.getString( 7 ) );
                portletType.setCreateScriptTemplate( daoUtil.getString( 8 ) );
                portletType.setCreateSpecificTemplate( daoUtil.getString( 9 ) );
                portletType.setCreateSpecificFormTemplate( daoUtil.getString( 10 ) );
                portletType.setDoModifyUrl( daoUtil.getString( 11 ) );
                portletType.setModifyScriptTemplate( daoUtil.getString( 12 ) );
                portletType.setModifySpecificTemplate( daoUtil.getString( 13 ) );
                portletType.setModifySpecificFormTemplate( daoUtil.getString( 14 ) );
            }

        }

        return portletType;
    }

    /**
     * Delete a record from the table
     * 
     * @param strPortletTypeId
     *            The POrtletTYpe identifier
     */
    public void delete( String strPortletTypeId )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setString( 1, strPortletTypeId );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * Returns the portlet type identifier
     * 
     * @param strPluginHomeClass
     *            the name of the portlet type
     * @return the identifier of the portlet type
     */
    public String selectPortletTypeId( String strPluginHomeClass )
    {
        String strPortletTypeId = "";
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_TYPE_ID ) )
        {
            daoUtil.setString( 1, strPluginHomeClass );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                strPortletTypeId = daoUtil.getString( 1 );
            }

        }

        return strPortletTypeId;
    }

    /**
     * Returns the portlet count for a given provider
     * 
     * @param strPortletTypeId
     *            The provider's identifier
     * @return nCount
     */
    public int selectNbPortletTypeByPortlet( String strPortletTypeId )
    {
        int nCount = 0;
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NB_PORTLET_TYPE_BY_PORTLET ) )
        {
            daoUtil.setString( 1, strPortletTypeId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nCount = daoUtil.getInt( 1 );
            }

        }

        return nCount;
    }

    /**
     * Return a Reference List of portletType
     * 
     * @param locale
     *            The current locale
     * @return list The reference List
     */
    public ReferenceList selectPortletsTypesList( Locale locale )
    {
        ReferenceList list = new ReferenceList( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLETS_TYPE_LIST ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                PortletType portletType = new PortletType( );
                portletType.setId( daoUtil.getString( 1 ) );
                portletType.setNameKey( daoUtil.getString( 2 ) );
                // Localize the portlet type
                portletType.setLocale( locale );
                list.addItem( portletType.getId( ), portletType.getName( ) );
            }

        }

        return list;
    }

    /**
     * Returns the list of the portlet types
     * 
     * @return the list of the portlet types
     */
    public List<PortletType> selectPortletTypesList( )
    {
        List<PortletType> list = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_TYPE_LIST ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                PortletType portletType = new PortletType( );
                portletType.setId( daoUtil.getString( 1 ) );
                portletType.setNameKey( daoUtil.getString( 2 ) );
                portletType.setUrlCreation( daoUtil.getString( 3 ) );
                portletType.setUrlUpdate( daoUtil.getString( 4 ) );
                list.add( portletType );
            }

        }

        return list;
    }
}
