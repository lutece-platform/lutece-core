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

import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class provides Data Access methods for Portlet objects
 */
public final class PortletDAO implements IPortletDAO
{
    // queries
    private static final String SQL_QUERY_NEW_PK = "SELECT max(id_portlet) FROM core_portlet ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_portlet SET name = ?, date_update = ?, column_no = ?, " +
        " portlet_order = ? , id_style = ? , id_page = ?, accept_alias = ? , display_portlet_title = ?, role = ?, device_display_flags = ? " +
        " WHERE id_portlet = ?";
    private static final String SQL_QUERY_SELECT = " SELECT b.id_portlet_type, a.id_page, a.id_style, a.name , b.name, " +
        " b.url_creation, b.url_update, a.date_update, a.column_no, a.portlet_order, " +
        " b.home_class, a.accept_alias , a.role , b.plugin_name , a.display_portlet_title, a.status, a.device_display_flags " +
        " FROM core_portlet a , core_portlet_type b WHERE a.id_portlet_type = b.id_portlet_type AND a.id_portlet = ?";
    private static final String SQL_QUERY_SELECT_ALIAS = " SELECT a.id_portlet FROM core_portlet a, core_portlet_alias b " +
        " WHERE a.id_portlet = b.id_portlet AND b.id_alias= ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_portlet WHERE id_portlet = ?";
    private static final String SQL_QUERY_UPDATE_STATUS = " UPDATE core_portlet SET status = ?, date_update = ? WHERE id_portlet = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_portlet ( id_portlet, id_portlet_type, id_page, id_style, name, " +
        " date_creation, date_update, status, column_no, portlet_order, accept_alias, display_portlet_title, role, device_display_flags ) " +
        " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?)";
    private static final String SQL_QUERY_SELECT_PORTLET_LIST_BY_STYLE = "SELECT id_portlet, name, id_page FROM core_portlet WHERE id_style=?";
    private static final String SQL_QUERY_SELECT_PORTLET_LIST_BY_ROLE = "SELECT id_portlet, name, id_page FROM core_portlet WHERE role=?";
    private static final String SQL_QUERY_SELECT_XSL_FILE = " SELECT a.id_stylesheet , a.description , a.file_name, a.source " +
        " FROM core_stylesheet a, core_portlet b, core_style_mode_stylesheet c " +
        " WHERE a.id_stylesheet = c.id_stylesheet " +
        " AND b.id_style = c.id_style AND b.id_portlet = ? AND c.id_mode = ? ";
    private static final String SQL_QUERY_SELECT_STYLE_LIST = " SELECT distinct a.id_style , a.description_style " +
        " FROM core_style a , core_style_mode_stylesheet b " + " WHERE  a.id_style = b.id_style " +
        " AND a.id_portlet_type = ? ORDER BY a.description_style";
    private static final String SQL_QUERY_SELECT_PORTLET_TYPE = " SELECT id_portlet_type , name , url_creation, url_update, plugin_name " +
        " FROM core_portlet_type WHERE id_portlet_type = ? ORDER BY id_portlet_type ";
    private static final String SQL_QUERY_SELECT_PORTLET_ALIAS = " SELECT a.id_portlet FROM core_portlet a , core_portlet_alias b" +
        " WHERE a.id_portlet = b.id_portlet " + " AND b.id_alias= ? ";
    private static final String SQL_QUERY_SELECT_ALIASES_FOR_PORTLET = "SELECT p.id_portlet, p.id_page, p.name " +
        "FROM core_portlet_alias a JOIN core_portlet p ON p.id_portlet = a.id_portlet WHERE a.id_alias = ? ";
    private static final String SQL_QUERY_SELECT_PORTLET_LIST_BY_NAME = " SELECT id_portlet , id_page , name FROM core_portlet WHERE name LIKE ? ";
    private static final String SQL_QUERY_SELECT_PORTLET_LIST_BY_TYPE = " SELECT a.id_portlet, a.id_portlet_type, a.id_page, a.name, " +
        "a.date_update, a.status, a.portlet_order, a.column_no, a.id_style, a.accept_alias, a.date_creation, a.display_portlet_title, a.role, a.device_display_flags " +
        " FROM core_portlet a, core_page b  WHERE a.id_page = b.id_page " + " AND a.id_portlet_type = ? ";
    private static final String SQL_QUERY_SELECT_LAST_MODIFIED_PORTLET = " SELECT a.id_portlet, b.id_portlet_type, a.id_page, a.id_style, a.name , b.name, " +
        " b.url_creation, b.url_update, a.date_update, a.column_no, a.portlet_order, " +
        " b.home_class, a.accept_alias , a.role , b.plugin_name , a.display_portlet_title, a.status , a.device_display_flags " +
        " FROM core_portlet a , core_portlet_type b WHERE a.id_portlet_type = b.id_portlet_type ORDER BY a.date_update DESC LIMIT 1 ";
    private static final String SQL_QUERY_SELECT_ORDER_FROM_PAGE_AND_COLUMN = " SELECT portlet_order FROM core_portlet WHERE column_no = ? AND id_page = ?  ORDER BY portlet_order";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * {@inheritDoc}
     */
    public void insert( Portlet portlet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setInt( 1, portlet.getId(  ) );
        daoUtil.setString( 2, portlet.getPortletTypeId(  ) );
        daoUtil.setInt( 3, portlet.getPageId(  ) );
        daoUtil.setInt( 4, portlet.getStyleId(  ) );
        daoUtil.setString( 5, portlet.getName(  ) );
        daoUtil.setTimestamp( 6, new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setTimestamp( 7, new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setInt( 8, portlet.getStatus(  ) );
        daoUtil.setInt( 9, portlet.getColumn(  ) );
        daoUtil.setInt( 10, portlet.getOrder(  ) );
        daoUtil.setInt( 11, portlet.getAcceptAlias(  ) );
        daoUtil.setInt( 12, portlet.getDisplayPortletTitle(  ) );
        daoUtil.setString( 13, portlet.getRole(  ) );
        daoUtil.setInt( 14, portlet.getDeviceDisplayFlags(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void delete( int nPortletId )
    {
        // we recover the alias of the portlet parent to delete
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALIAS );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AliasPortletHome.getInstance(  ).remove( PortletHome.findByPrimaryKey( daoUtil.getInt( 1 ) ) );
        }

        daoUtil.free(  );

        // we delete the portlet
        daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPortletId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public Portlet load( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        PortletImpl portlet = new PortletImpl(  );

        if ( daoUtil.next(  ) )
        {
            portlet.setId( nPortletId );
            portlet.setPortletTypeId( daoUtil.getString( 1 ) );
            portlet.setPageId( daoUtil.getInt( 2 ) );
            portlet.setStyleId( daoUtil.getInt( 3 ) );
            portlet.setName( daoUtil.getString( 4 ) );
            portlet.setPortletTypeName( daoUtil.getString( 5 ) );
            portlet.setUrlCreation( daoUtil.getString( 6 ) );
            portlet.setUrlUpdate( daoUtil.getString( 7 ) );
            portlet.setDateUpdate( daoUtil.getTimestamp( 8 ) );
            portlet.setColumn( daoUtil.getInt( 9 ) );
            portlet.setOrder( daoUtil.getInt( 10 ) );
            portlet.setHomeClassName( daoUtil.getString( 11 ) );
            portlet.setAcceptAlias( daoUtil.getInt( 12 ) );
            portlet.setRole( daoUtil.getString( 13 ) );
            portlet.setPluginName( daoUtil.getString( 14 ) );
            portlet.setDisplayPortletTitle( daoUtil.getInt( 15 ) );
            portlet.setStatus( daoUtil.getInt( 16 ) );
            portlet.setDeviceDisplayFlags( daoUtil.getInt( 17 ) );
        }

        daoUtil.free(  );

        return portlet;
    }

    /**
     * {@inheritDoc}
     */
    public void store( Portlet portlet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( 1, portlet.getName(  ) );
        daoUtil.setTimestamp( 2, new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setInt( 3, portlet.getColumn(  ) );
        daoUtil.setInt( 4, portlet.getOrder(  ) );
        daoUtil.setInt( 5, portlet.getStyleId(  ) );
        daoUtil.setInt( 6, portlet.getPageId(  ) );
        daoUtil.setInt( 7, portlet.getAcceptAlias(  ) );
        daoUtil.setInt( 8, portlet.getDisplayPortletTitle(  ) );
        daoUtil.setString( 9, portlet.getRole(  ) );
        daoUtil.setInt( 10, portlet.getDeviceDisplayFlags(  ) );
        daoUtil.setInt( 11, portlet.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Returns a new primary key which will be used to add a new portlet
     *
     * @return The new key.
     */
    public int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            nKey = 1; // if the table is empty
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    public void updateStatus( Portlet portlet, int nStatus )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_STATUS );

        daoUtil.setInt( 1, nStatus );
        daoUtil.setTimestamp( 2, new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setInt( 3, portlet.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public StyleSheet selectXslFile( int nPortletId, int nIdMode )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_XSL_FILE );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nIdMode );
        daoUtil.executeQuery(  );

        StyleSheet stylesheet = new StyleSheet(  );

        if ( daoUtil.next(  ) )
        {
            stylesheet.setId( daoUtil.getInt( 1 ) );
            stylesheet.setDescription( daoUtil.getString( 2 ) );
            stylesheet.setFile( daoUtil.getString( 3 ) );
            stylesheet.setSource( daoUtil.getBytes( 4 ) );
        }

        daoUtil.free(  );

        return stylesheet;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<PortletImpl> selectPortletsListbyName( String strPortletName )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_LIST_BY_NAME );
        daoUtil.setString( 1, '%' + strPortletName + '%' );
        daoUtil.executeQuery(  );

        List<PortletImpl> list = new ArrayList<PortletImpl>(  );

        while ( daoUtil.next(  ) )
        {
            PortletImpl portlet = new PortletImpl(  );
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setPageId( daoUtil.getInt( 2 ) );
            portlet.setName( daoUtil.getString( 3 ) );

            list.add( portlet );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Portlet> selectAliasesForPortlet( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALIASES_FOR_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        List<Portlet> list = new ArrayList<Portlet>(  );

        while ( daoUtil.next(  ) )
        {
            PortletImpl portlet = new PortletImpl(  );
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setPageId( daoUtil.getInt( 2 ) );
            portlet.setName( daoUtil.getString( 3 ) );

            list.add( portlet );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * {@inheritDoc}
     */
    public List<Portlet> selectPortletsByType( String strPortletTypeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_LIST_BY_TYPE );
        daoUtil.setString( 1, strPortletTypeId );
        daoUtil.executeQuery(  );

        List<Portlet> list = new ArrayList<Portlet>(  );

        while ( daoUtil.next(  ) )
        {
            PortletImpl portlet = new PortletImpl(  );
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setPortletTypeId( daoUtil.getString( 2 ) );
            portlet.setPageId( daoUtil.getInt( 3 ) );
            portlet.setName( daoUtil.getString( 4 ) );
            portlet.setDateUpdate( daoUtil.getTimestamp( 5 ) );
            portlet.setStatus( daoUtil.getInt( 6 ) );
            portlet.setOrder( daoUtil.getInt( 7 ) );
            portlet.setColumn( daoUtil.getInt( 8 ) );
            portlet.setStyleId( daoUtil.getInt( 9 ) );
            portlet.setAcceptAlias( daoUtil.getInt( 10 ) );
            portlet.setDateUpdate( daoUtil.getTimestamp( 11 ) );
            portlet.setDisplayPortletTitle( daoUtil.getInt( 12 ) );
            portlet.setRole( daoUtil.getString( 13 ) );
            portlet.setDeviceDisplayFlags( daoUtil.getInt( 14 ) );

            //FIXME Theses attributes concerns PortletType :
            //            portlet.setHomeClassName(daoUtil.getString( 1 ));
            //            portlet.setPluginName(daoUtil.getString( 1 ));
            //            portlet.setPortletTypeName(daoUtil.getString( 1 ));
            //            portlet.setUrlCreation(daoUtil.getString( 1 ));
            //            portlet.setUrlUpdate(daoUtil.getString( 1 ));
            list.add( portlet );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * {@inheritDoc}
     */
    public ReferenceList selectStylesList( String strPortletTypeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_STYLE_LIST );
        daoUtil.setString( 1, strPortletTypeId );
        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasAlias( int nPortletId )
    {
        boolean bHasAlias = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_ALIAS );
        daoUtil.setInt( 1, nPortletId );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bHasAlias = true;
        }

        daoUtil.free(  );

        return bHasAlias;
    }

    /**
     * {@inheritDoc}
     */
    public PortletType selectPortletType( String strPortletTypeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_TYPE );

        daoUtil.setString( 1, strPortletTypeId );
        daoUtil.executeQuery(  );

        PortletType portletType = new PortletType(  );

        if ( daoUtil.next(  ) )
        {
            portletType.setId( daoUtil.getString( 1 ) );
            portletType.setNameKey( daoUtil.getString( 2 ) );
            portletType.setUrlCreation( daoUtil.getString( 3 ) );
            portletType.setUrlUpdate( daoUtil.getString( 4 ) );
            portletType.setPluginName( daoUtil.getString( 5 ) );
        }

        daoUtil.free(  );

        return portletType;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<PortletImpl> selectPortletListByStyle( int nStyleId )
    {
        Collection<PortletImpl> portletList = new ArrayList<PortletImpl>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_LIST_BY_STYLE );

        daoUtil.setInt( 1, nStyleId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            PortletImpl portlet = new PortletImpl(  );

            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setName( daoUtil.getString( 2 ) );
            portlet.setPageId( daoUtil.getInt( 3 ) );

            portletList.add( portlet );
        }

        daoUtil.free(  );

        return portletList;
    }

    /**
     * {@inheritDoc }
     */
    public Collection<Portlet> selectPortletsByRole( String strRole )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_LIST_BY_ROLE );
        daoUtil.setString( 1, strRole );
        daoUtil.executeQuery(  );

        List<Portlet> list = new ArrayList<Portlet>(  );

        while ( daoUtil.next(  ) )
        {
            PortletImpl portlet = new PortletImpl(  );
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setPortletTypeId( daoUtil.getString( 2 ) );
            portlet.setPageId( daoUtil.getInt( 3 ) );

            list.add( portlet );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * {@inheritDoc}
     */
    public Portlet loadLastModifiedPortlet(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_MODIFIED_PORTLET );
        daoUtil.executeQuery(  );

        PortletImpl portlet = null;

        if ( daoUtil.next(  ) )
        {
            portlet = new PortletImpl(  );

            int nIndex = 1;
            portlet.setId( daoUtil.getInt( nIndex++ ) );
            portlet.setPortletTypeId( daoUtil.getString( nIndex++ ) );
            portlet.setPageId( daoUtil.getInt( nIndex++ ) );
            portlet.setStyleId( daoUtil.getInt( nIndex++ ) );
            portlet.setName( daoUtil.getString( nIndex++ ) );
            portlet.setPortletTypeName( daoUtil.getString( nIndex++ ) );
            portlet.setUrlCreation( daoUtil.getString( nIndex++ ) );
            portlet.setUrlUpdate( daoUtil.getString( nIndex++ ) );
            portlet.setDateUpdate( daoUtil.getTimestamp( nIndex++ ) );
            portlet.setColumn( daoUtil.getInt( nIndex++ ) );
            portlet.setOrder( daoUtil.getInt( nIndex++ ) );
            portlet.setHomeClassName( daoUtil.getString( nIndex++ ) );
            portlet.setAcceptAlias( daoUtil.getInt( nIndex++ ) );
            portlet.setRole( daoUtil.getString( nIndex++ ) );
            portlet.setPluginName( daoUtil.getString( nIndex++ ) );
            portlet.setDisplayPortletTitle( daoUtil.getInt( nIndex++ ) );
            portlet.setStatus( daoUtil.getInt( nIndex++ ) );
            portlet.setDeviceDisplayFlags( daoUtil.getInt( nIndex++ ) );
        }

        daoUtil.free(  );

        return portlet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getUsedOrdersForColumns( int pageId, int columnId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ORDER_FROM_PAGE_AND_COLUMN );
        daoUtil.setInt( 1, columnId );
        daoUtil.setInt( 2, pageId );

        List<Integer> result = new ArrayList<Integer>(  );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            result.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return result;
    }
}
