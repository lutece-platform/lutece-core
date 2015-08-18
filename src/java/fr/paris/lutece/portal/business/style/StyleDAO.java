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
package fr.paris.lutece.portal.business.style;

import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for style objects
 */
public final class StyleDAO implements IStyleDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = " SELECT DISTINCT a.id_portlet_type , a.id_portal_component, a.description_style , " +
        " b.name, c.name , id_style FROM core_style a " +
        " INNER JOIN core_portal_component c ON a.id_portal_component = c.id_portal_component " +
        " LEFT JOIN core_portlet_type b ON a.id_portlet_type = b.id_portlet_type WHERE a.id_style = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_style ( id_style , id_portlet_type , id_portal_component, description_style ) " +
        " VALUES ( ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_style WHERE id_style = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_style SET  id_portlet_type = ?, id_portal_component = ?, description_style = ? " +
        " WHERE id_style = ?";
    private static final String SQL_QUERY_SELECTALL = " SELECT a.id_style , a.id_portlet_type , a.id_portal_component, a.description_style , " +
        " b.name, c.name FROM core_style a " +
        " INNER JOIN core_portal_component c ON a.id_portal_component = c.id_portal_component " +
        " LEFT JOIN core_portlet_type b ON a.id_portlet_type = b.id_portlet_type " +
        " ORDER BY a.id_portal_component, a.id_style ";
    private static final String SQL_QUERY_SELECT_STYLESHEET = " SELECT a.id_stylesheet, a.description, a.file_name " +
        " FROM  core_stylesheet a , core_style_mode_stylesheet b" + " WHERE b.id_stylesheet = a.id_stylesheet " +
        " AND b.id_style = ? ";
    private static final String SQL_CHECK_STYLE_PORTLETCOMPONENT = " SELECT id_style FROM core_style WHERE id_portal_component = ? ";
    private static final String SQL_QUERY_SELECT_PORTLETCOMPONENT = " SELECT id_portal_component , name FROM core_portal_component ORDER BY name ";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table.
     * @param style The Instance of the object Style
     */
    public void insert( Style style )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setInt( 1, style.getId(  ) );
        daoUtil.setString( 2, style.getPortletTypeId(  ) );
        daoUtil.setInt( 3, style.getPortalComponentId(  ) );
        daoUtil.setString( 4, style.getDescription(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of the Style whose identifier is specified in parameter from the table
     * @param nStyleId The identifier of the Style
     * @return an instance of the Style which has been created
     */
    public Style load( int nStyleId )
    {
        Style style = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nStyleId );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            style = new Style(  );
            style.setId( nStyleId );
            style.setPortletTypeId( daoUtil.getString( 1 ) );
            style.setPortalComponentId( daoUtil.getInt( 2 ) );
            style.setDescription( daoUtil.getString( 3 ) );
            style.setPortletTypeName( daoUtil.getString( 4 ) );
            style.setPortalComponentName( daoUtil.getString( 5 ) );
        }

        daoUtil.free(  );

        return style;
    }

    /**
     * Delete a record from the table
     * @param nStyleId the identifier of the style to delete
     */
    public void delete( int nStyleId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nStyleId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param style The instance of the Style to update
     */
    public void store( Style style )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( 1, style.getPortletTypeId(  ) );
        daoUtil.setInt( 2, style.getPortalComponentId(  ) );
        daoUtil.setString( 3, style.getDescription(  ) );
        daoUtil.setInt( 4, style.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the list of styles stored in the database
     * @return The styles list in form of a Collection object
     */
    public Collection<Style> selectStylesList(  )
    {
        Collection<Style> styleList = new ArrayList<Style>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Style style = new Style(  );

            style.setId( daoUtil.getInt( 1 ) );
            style.setPortletTypeId( daoUtil.getString( 2 ) );
            style.setPortalComponentId( daoUtil.getInt( 3 ) );
            style.setDescription( daoUtil.getString( 4 ) );
            style.setPortletTypeName( daoUtil.getString( 5 ) );
            style.setPortalComponentName( daoUtil.getString( 6 ) );

            styleList.add( style );
        }

        daoUtil.free(  );

        return styleList;
    }

    /**
     * Returns the list of the portal component in form of a ReferenceList
     * @return the list of the portal component
     */
    public ReferenceList selectPortalComponentList(  )
    {
        ReferenceList portletComponentList = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLETCOMPONENT );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            portletComponentList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return portletComponentList;
    }

    /**
     * load the data of the StyleSheet which re associated to the given style
     *
     * @param nStyleId The identifier of the Style
     * @return an instance of the Style which has been created
     */
    public Collection<StyleSheet> selectStyleSheetList( int nStyleId )
    {
        Collection<StyleSheet> stylesheetList = new ArrayList<StyleSheet>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_STYLESHEET );

        daoUtil.setInt( 1, nStyleId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            StyleSheet stylesheet = new StyleSheet(  );

            stylesheet.setId( daoUtil.getInt( 1 ) );
            stylesheet.setDescription( daoUtil.getString( 2 ) );
            stylesheet.setFile( daoUtil.getString( 3 ) );

            stylesheetList.add( stylesheet );
        }

        daoUtil.free(  );

        return stylesheetList;
    }

    /**
     * Checks if a style has been created in the database with the given portal componenet
     * @param nPortalComponentId The identifier of the portal component
     * @return true if a style has been created for this portal component, false otherwise
     */
    public boolean checkStylePortalComponent( int nPortalComponentId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_CHECK_STYLE_PORTLETCOMPONENT );

        daoUtil.setInt( 1, nPortalComponentId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return true;
        }

        daoUtil.free(  );

        return false;
    }
}
