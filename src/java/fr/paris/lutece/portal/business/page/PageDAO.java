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
package fr.paris.lutece.portal.business.page;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class porvides Data Access methods for Page objects
 */
public final class PageDAO implements IPageDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max(id_page) FROM core_page";
    private static final String SQL_QUERY_SELECT = "SELECT a.id_parent, a.name, a.description, a.id_template, b.file_name, " +
        " a.page_order, a.status, a.role , a.code_theme , a.node_status , a.image_content, a.mime_type, " +
        "  a.date_update, a.meta_keywords, a.meta_description, a.id_authorization_node FROM core_page a, core_page_template b WHERE a.id_template = b.id_template AND a.id_page = ? ";
    private static final String SQL_QUERY_SELECT_WITHOUT_IMAGE_CONTENT = "SELECT a.id_parent, a.name, a.description, a.id_template, b.file_name, " +
        " a.page_order, a.status, a.role , a.code_theme , a.node_status , a.mime_type, " +
        "  a.date_update, a.meta_keywords, a.meta_description FROM core_page a INNER JOIN " +
        " core_page_template b ON (a.id_template = b.id_template) WHERE a.id_page = ? ";
    private static final String SQL_QUERY_SELECT_BY_ID_PORTLET = "SELECT a.id_page, a.id_parent, a.name, a.description, a.id_template, " +
        " a.page_order, a.status, a.role , a.code_theme , a.node_status , a.image_content, a.mime_type, " +
        "  a.meta_keywords, a.meta_description,a.id_authorization_node FROM core_page a,core_portlet b WHERE a.id_page = b.id_page AND b.id_portlet = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_page ( id_page , id_parent , name , description, date_update, " +
        " id_template,  page_order, status, role, date_creation, code_theme , node_status, image_content , mime_type ,  " +
        " meta_keywords, meta_description,id_authorization_node ) " +
        " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_page WHERE id_page = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_page SET id_parent = ?,  name = ?, description = ? , date_update = ? , " +
        " id_template = ? , page_order = ? , status = ? , role = ? , code_theme = ? , node_status = ? , " +
        " image_content = ? , mime_type = ? , meta_keywords = ?, meta_description = ? ,id_authorization_node=?" +
        " WHERE id_page = ?";
    private static final String SQL_QUERY_CHECKPK = "SELECT id_page FROM core_page WHERE id_page = ?";
    private static final String SQL_QUERY_CHILDPAGE = "SELECT id_page , id_parent, name, description, " +
        " page_order , status , role, code_theme, image_content, mime_type , meta_keywords, meta_description, date_update,id_authorization_node " +
        " FROM core_page WHERE id_parent = ? ORDER BY page_order";
    private static final String SQL_QUERY_CHILDPAGE_MINIMAL_DATA = "SELECT id_page ,id_parent, name, description, role FROM core_page " +
        " WHERE id_parent = ? ORDER BY page_order";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_page , id_parent,  name, description, date_update, " +
        " page_order, status, role, code_theme, image_content, mime_type , meta_keywords, meta_description,id_authorization_node  FROM core_page ";
    private static final String SQL_QUERY_BY_ROLE_KEY = "SELECT id_page , id_parent,  name, description, date_update, " +
        " page_order, status, role, code_theme, image_content, mime_type , meta_keywords, meta_description,id_authorization_node  FROM core_page WHERE role = ? ";
    private static final String SQL_QUERY_SELECT_PORTLET = "SELECT id_portlet FROM core_portlet WHERE id_page = ? ORDER BY portlet_order";
    private static final String SQL_QUERY_UPDATE_PAGE_DATE = "UPDATE core_page SET date_update = ? WHERE id_page = ?";
    private static final String SQL_QUERY_SELECTALL_NODE_PAGE = "SELECT id_page, name FROM core_page WHERE node_status = 0";
    private static final String SQL_QUERY_NEW_CHILD_PAGE_ORDER = "SELECT max(page_order) FROM core_page WHERE id_parent = ?";
    private static final String SQL_QUERY_CHECK_PAGE_EXIST = "SELECT id_page FROM core_page " + " WHERE id_page = ? ";
    private static final String SQL_QUERY_SELECT_LAST_MODIFIED_PAGE = "SELECT id_page, id_parent, name, description, id_template, " +
        " page_order, status, role , code_theme , node_status , mime_type, " +
        "  date_update, meta_keywords, meta_description,id_authorization_node FROM core_page " +
        " ORDER BY date_update DESC LIMIT 1";

    // ImageResource queries
    private static final String SQL_QUERY_SELECT_RESOURCE_IMAGE = " SELECT image_content , mime_type FROM core_page " +
        " WHERE id_page = ? ";
    private static final String SQL_QUERY_SELECT_CHILD_PAGE_FOR_MODIFY_AUTORISATION_NODE = "  SELECT id_page FROM core_page  " +
        "WHERE id_parent=? AND( id_authorization_node IS NULL OR id_page != id_authorization_node ) ";
    private static final String SQL_QUERY_UPDATE_AUTORISATION_NODE = " UPDATE core_page SET id_authorization_node = ? WHERE id_page=? ";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Generates a new primary key
     * @return The new primary key
     */
    int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void insert( Page page )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        int nNewPrimaryKey = newPrimaryKey(  );
        page.setId( nNewPrimaryKey );
        page.setOrigParentPageId( page.getParentPageId(  ) );

        daoUtil.setInt( 1, page.getId(  ) );
        daoUtil.setInt( 2, page.getParentPageId(  ) );
        daoUtil.setString( 3, page.getName(  ) );
        daoUtil.setString( 4, page.getDescription(  ) );
        page.setDateUpdate( new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setTimestamp( 5, page.getDateUpdate(  ) );
        daoUtil.setInt( 6, page.getPageTemplateId(  ) );
        daoUtil.setInt( 7, page.getOrder(  ) );
        daoUtil.setInt( 8, page.getStatus(  ) );
        daoUtil.setString( 9, page.getRole(  ) );

        // For a new object, update time = creation time
        daoUtil.setTimestamp( 10, page.getDateUpdate(  ) );
        daoUtil.setString( 11, page.getCodeTheme(  ) );
        daoUtil.setInt( 12, page.getNodeStatus(  ) );
        daoUtil.setBytes( 13, page.getImageContent(  ) );
        daoUtil.setString( 14, page.getMimeType(  ) );

        if ( ( page.getMetaKeywords(  ) != null ) && ( page.getMetaKeywords(  ).length(  ) > 0 ) )
        {
            daoUtil.setString( 15, page.getMetaKeywords(  ) );
        }
        else
        {
            daoUtil.setString( 15, null );
        }

        if ( ( page.getMetaDescription(  ) != null ) && ( page.getMetaDescription(  ).length(  ) > 0 ) )
        {
            daoUtil.setString( 16, page.getMetaDescription(  ) );
        }
        else
        {
            daoUtil.setString( 16, null );
        }

        if ( page.getIdAuthorizationNode(  ) != null )
        {
            daoUtil.setInt( 17, page.getIdAuthorizationNode(  ) );
        }
        else
        {
            daoUtil.setIntNull( 17 );
        }

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public Page load( int nPageId, boolean bPortlets )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nPageId );

        daoUtil.executeQuery(  );

        Page page = new Page(  );

        if ( daoUtil.next(  ) )
        {
            page.setId( nPageId );
            page.setParentPageId( daoUtil.getInt( 1 ) );
            page.setOrigParentPageId( daoUtil.getInt( 1 ) );
            page.setName( daoUtil.getString( 2 ) );
            page.setDescription( daoUtil.getString( 3 ) );
            page.setPageTemplateId( daoUtil.getInt( 4 ) );
            page.setTemplate( daoUtil.getString( 5 ) );
            page.setOrder( daoUtil.getInt( 6 ) );
            page.setStatus( daoUtil.getInt( 7 ) );
            page.setRole( daoUtil.getString( 8 ) );
            page.setCodeTheme( daoUtil.getString( 9 ) );
            page.setNodeStatus( daoUtil.getInt( 10 ) );
            page.setImageContent( daoUtil.getBytes( 11 ) );
            page.setMimeType( daoUtil.getString( 12 ) );
            page.setDateUpdate( daoUtil.getTimestamp( 13 ) );
            page.setMetaKeywords( daoUtil.getString( 14 ) );
            page.setMetaDescription( daoUtil.getString( 15 ) );

            if ( daoUtil.getObject( 16 ) != null )
            {
                page.setIdAuthorizationNode( daoUtil.getInt( 16 ) );
            }

            // Patch perfs : close connection before loadPortlets
            daoUtil.free(  );

            // Loads the portlets contained into the page
            if ( bPortlets )
            {
                loadPortlets( page );
            }
        }

        daoUtil.free(  );

        return page;
    }

    /**
     * {@inheritDoc}
     */
    public Page loadWithoutImageContent( int nPageId, boolean bPortlets )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_WITHOUT_IMAGE_CONTENT );
        daoUtil.setInt( 1, nPageId );

        daoUtil.executeQuery(  );

        Page page = new Page(  );

        if ( daoUtil.next(  ) )
        {
            page.setId( nPageId );
            page.setParentPageId( daoUtil.getInt( 1 ) );
            page.setOrigParentPageId( daoUtil.getInt( 1 ) );
            page.setName( daoUtil.getString( 2 ) );
            page.setDescription( daoUtil.getString( 3 ) );
            page.setPageTemplateId( daoUtil.getInt( 4 ) );
            page.setTemplate( daoUtil.getString( 5 ) );
            page.setOrder( daoUtil.getInt( 6 ) );
            page.setStatus( daoUtil.getInt( 7 ) );
            page.setRole( daoUtil.getString( 8 ) );
            page.setCodeTheme( daoUtil.getString( 9 ) );
            page.setNodeStatus( daoUtil.getInt( 10 ) );
            page.setMimeType( daoUtil.getString( 11 ) );
            page.setDateUpdate( daoUtil.getTimestamp( 12 ) );
            page.setMetaKeywords( daoUtil.getString( 13 ) );
            page.setMetaDescription( daoUtil.getString( 14 ) );

            if ( daoUtil.getObject( 15 ) != null )
            {
                page.setIdAuthorizationNode( daoUtil.getInt( 15 ) );
            }

            // Patch perfs : close connection before loadPortlets
            daoUtil.free(  );

            // Loads the portlets contained into the page
            if ( bPortlets )
            {
                loadPortlets( page );
            }
        }

        daoUtil.free(  );

        return page;
    }

    /**
     * {@inheritDoc}
     */
    public Page loadPageByIdPortlet( int nPorletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ID_PORTLET );
        daoUtil.setInt( 1, nPorletId );

        daoUtil.executeQuery(  );

        Page page = new Page(  );

        if ( daoUtil.next(  ) )
        {
            page.setId( daoUtil.getInt( 1 ) );
            page.setParentPageId( daoUtil.getInt( 2 ) );
            page.setOrigParentPageId( daoUtil.getInt( 2 ) );
            page.setName( daoUtil.getString( 3 ) );
            page.setDescription( daoUtil.getString( 4 ) );
            page.setPageTemplateId( daoUtil.getInt( 5 ) );
            page.setOrder( daoUtil.getInt( 6 ) );
            page.setStatus( daoUtil.getInt( 7 ) );
            page.setRole( daoUtil.getString( 8 ) );
            page.setCodeTheme( daoUtil.getString( 9 ) );
            page.setNodeStatus( daoUtil.getInt( 10 ) );
            page.setImageContent( daoUtil.getBytes( 11 ) );
            page.setMimeType( daoUtil.getString( 12 ) );
            page.setMetaKeywords( daoUtil.getString( 13 ) );
            page.setMetaDescription( daoUtil.getString( 14 ) );
            page.setIdAuthorizationNode( daoUtil.getInt( 15 ) );
        }

        daoUtil.free(  );

        return page;
    }

    /**
     * {@inheritDoc}
     */
    public void delete( int nPageId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPageId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void store( Page page )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setInt( 1, page.getParentPageId(  ) );
        daoUtil.setString( 2, page.getName(  ) );
        daoUtil.setString( 3, page.getDescription(  ) );
        page.setDateUpdate( new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setTimestamp( 4, page.getDateUpdate(  ) );
        daoUtil.setInt( 5, page.getPageTemplateId(  ) );
        daoUtil.setInt( 6, page.getOrder(  ) );
        daoUtil.setInt( 7, page.getStatus(  ) );
        daoUtil.setString( 8, page.getRole(  ) );
        daoUtil.setString( 9, page.getCodeTheme(  ) );
        daoUtil.setInt( 10, page.getNodeStatus(  ) );
        daoUtil.setBytes( 11, page.getImageContent(  ) );
        daoUtil.setString( 12, page.getMimeType(  ) );

        if ( ( page.getMetaKeywords(  ) != null ) && ( page.getMetaKeywords(  ).length(  ) > 0 ) )
        {
            daoUtil.setString( 13, page.getMetaKeywords(  ) );
        }
        else
        {
            daoUtil.setString( 13, null );
        }

        if ( ( page.getMetaDescription(  ) != null ) && ( page.getMetaDescription(  ).length(  ) > 0 ) )
        {
            daoUtil.setString( 14, page.getMetaDescription(  ) );
        }
        else
        {
            daoUtil.setString( 14, null );
        }

        if ( page.getIdAuthorizationNode(  ) != null )
        {
            daoUtil.setInt( 15, page.getIdAuthorizationNode(  ) );
        }
        else
        {
            daoUtil.setIntNull( 15 );
        }

        daoUtil.setInt( 16, page.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Checks if the page identifier exists
     * @param nKey  The page identifier
     * @return true if the identifier exists, false if not
     */
    boolean checkPrimaryKey( int nKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECKPK );

        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return false;
        }

        daoUtil.free(  );

        return true;
    }

    /**
     * loads the portlets list contained into the page
     *
     * @param page The object page
     */
    void loadPortlets( Page page )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET );
        daoUtil.setInt( 1, page.getId(  ) );

        daoUtil.executeQuery(  );

        // Patch perfs :  get query responses and close connection before getting portlet
        ArrayList<Integer> portletIds = new ArrayList<Integer>(  );

        while ( daoUtil.next(  ) )
        {
            portletIds.add( Integer.valueOf( daoUtil.getInt( 1 ) ) );
        }

        daoUtil.free(  );

        ArrayList<Portlet> pageColl = new ArrayList<Portlet>(  );

        for ( Integer nPortletId : portletIds )
        {
            Portlet portlet = PortletHome.findByPrimaryKey( nPortletId );
            pageColl.add( portlet );
        }

        page.setPortlets( pageColl );
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Page> selectChildPages( int nParentPageId )
    {
        Collection<Page> pageList = new ArrayList<Page>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHILDPAGE );
        daoUtil.setInt( 1, nParentPageId );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Page page = new Page(  );

            page.setId( daoUtil.getInt( 1 ) );
            page.setParentPageId( daoUtil.getInt( 2 ) );
            page.setOrigParentPageId( daoUtil.getInt( 2 ) );
            page.setName( daoUtil.getString( 3 ) );
            page.setDescription( daoUtil.getString( 4 ) );
            page.setOrder( daoUtil.getInt( 5 ) );
            page.setStatus( daoUtil.getInt( 6 ) );
            page.setRole( daoUtil.getString( 7 ) );
            page.setCodeTheme( daoUtil.getString( 8 ) );
            page.setImageContent( daoUtil.getBytes( 9 ) );
            page.setMimeType( daoUtil.getString( 10 ) );
            page.setMetaKeywords( daoUtil.getString( 11 ) );
            page.setMetaDescription( daoUtil.getString( 12 ) );
            page.setDateUpdate( daoUtil.getTimestamp( 13 ) );

            if ( daoUtil.getObject( 14 ) != null )
            {
                page.setIdAuthorizationNode( daoUtil.getInt( 14 ) );
            }

            pageList.add( page );
        }

        daoUtil.free(  );

        return pageList;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Page> selectChildPagesMinimalData( int nParentPageId )
    {
        Collection<Page> pageList = new ArrayList<Page>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHILDPAGE_MINIMAL_DATA );
        daoUtil.setInt( 1, nParentPageId );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Page page = new Page(  );
            page.setId( daoUtil.getInt( 1 ) );
            page.setParentPageId( daoUtil.getInt( 2 ) );
            page.setOrigParentPageId( daoUtil.getInt( 2 ) );
            page.setName( daoUtil.getString( 3 ) );
            page.setDescription( daoUtil.getString( 4 ) );
            page.setRole( daoUtil.getString( 5 ) );
            pageList.add( page );
        }

        daoUtil.free(  );

        return pageList;
    }

    /**
     * {@inheritDoc}
     */
    public List<Page> selectAllPages(  )
    {
        List<Page> pageList = new ArrayList<Page>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Page page = new Page(  );

            page.setId( daoUtil.getInt( 1 ) );
            page.setParentPageId( daoUtil.getInt( 2 ) );
            page.setOrigParentPageId( daoUtil.getInt( 2 ) );
            page.setName( daoUtil.getString( 3 ) );
            page.setDescription( daoUtil.getString( 4 ) );
            page.setDateUpdate( daoUtil.getTimestamp( 5 ) );
            page.setOrder( daoUtil.getInt( 6 ) );
            page.setStatus( daoUtil.getInt( 7 ) );
            page.setRole( daoUtil.getString( 8 ) );
            page.setCodeTheme( daoUtil.getString( 9 ) );
            page.setImageContent( daoUtil.getBytes( 10 ) );
            page.setMimeType( daoUtil.getString( 11 ) );
            page.setMetaKeywords( daoUtil.getString( 12 ) );
            page.setMetaDescription( daoUtil.getString( 13 ) );

            if ( daoUtil.getObject( 14 ) != null )
            {
                page.setIdAuthorizationNode( daoUtil.getInt( 14 ) );
            }

            pageList.add( page );
        }

        daoUtil.free(  );

        return pageList;
    }

    /**
     * {@inheritDoc}
     */
    public void invalidatePage( int nPageId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_PAGE_DATE );

        daoUtil.setTimestamp( 1, new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setInt( 2, nPageId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public ReferenceList getPagesList(  )
    {
        ReferenceList listPages = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_NODE_PAGE );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Page page = new Page(  );
            page.setId( daoUtil.getInt( 1 ) );
            page.setName( daoUtil.getString( 2 ) );
            listPages.addItem( page.getId(  ), page.getName(  ) + " ( " + page.getId(  ) + " )" );
        }

        daoUtil.free(  );

        return listPages;
    }

    /**
     * Return the list of all the pages filtered by Lutece Role specified in parameter
     *
     * @param strRoleKey The Lutece Role key
     * @return a collection of pages
     */
    public Collection<Page> getPagesByRoleKey( String strRoleKey )
    {
        Collection<Page> pageList = new ArrayList<Page>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_BY_ROLE_KEY );
        daoUtil.setString( 1, strRoleKey );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Page page = new Page(  );

            page.setId( daoUtil.getInt( 1 ) );
            page.setParentPageId( daoUtil.getInt( 2 ) );
            page.setOrigParentPageId( daoUtil.getInt( 2 ) );
            page.setName( daoUtil.getString( 3 ) );
            page.setDescription( daoUtil.getString( 4 ) );
            page.setDateUpdate( daoUtil.getTimestamp( 5 ) );
            page.setOrder( daoUtil.getInt( 6 ) );
            page.setStatus( daoUtil.getInt( 7 ) );
            page.setRole( daoUtil.getString( 8 ) );
            page.setCodeTheme( daoUtil.getString( 9 ) );
            page.setImageContent( daoUtil.getBytes( 10 ) );
            page.setMimeType( daoUtil.getString( 11 ) );
            page.setMetaKeywords( daoUtil.getString( 12 ) );
            page.setMetaDescription( daoUtil.getString( 13 ) );

            if ( daoUtil.getObject( 14 ) != null )
            {
                page.setIdAuthorizationNode( daoUtil.getInt( 14 ) );
            }

            pageList.add( page );
        }

        daoUtil.free(  );

        return pageList;
    }

    /**
     * {@inheritDoc}
     */
    public int selectNewChildPageOrder( int nParentPageId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_CHILD_PAGE_ORDER );
        daoUtil.setInt( 1, nParentPageId );
        daoUtil.executeQuery(  );

        int nPageOrder;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nPageOrder = 1;
        }

        nPageOrder = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nPageOrder;
    }

    /**
     * {@inheritDoc}
     */
    public ImageResource loadImageResource( int nIdPage )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_RESOURCE_IMAGE );
        daoUtil.setInt( 1, nIdPage );
        daoUtil.executeQuery(  );

        ImageResource image = null;

        if ( daoUtil.next(  ) )
        {
            image = new ImageResource(  );
            image.setImage( daoUtil.getBytes( 1 ) );
            image.setMimeType( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return image;
    }

    /**
     * Tests if page exist
     *
     * @param nPageId The identifier of the document
     * @return true if the page existed, false otherwise
     */
    public boolean checkPageExist( int nPageId )
    {
        boolean bPageExisted = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_PAGE_EXIST );

        daoUtil.setInt( 1, nPageId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bPageExisted = true;
        }

        daoUtil.free(  );

        return bPageExisted;
    }

    /**
     * {@inheritDoc}
     */
    public Page loadLastModifiedPage(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_MODIFIED_PAGE );

        daoUtil.executeQuery(  );

        Page page = null;

        if ( daoUtil.next(  ) )
        {
            page = new Page(  );

            int nIndex = 1;
            page.setId( daoUtil.getInt( nIndex++ ) );
            page.setParentPageId( daoUtil.getInt( nIndex++ ) );
            page.setOrigParentPageId( page.getParentPageId(  ) );
            page.setName( daoUtil.getString( nIndex++ ) );
            page.setDescription( daoUtil.getString( nIndex++ ) );
            page.setPageTemplateId( daoUtil.getInt( nIndex++ ) );
            page.setOrder( daoUtil.getInt( nIndex++ ) );
            page.setStatus( daoUtil.getInt( nIndex++ ) );
            page.setRole( daoUtil.getString( nIndex++ ) );
            page.setCodeTheme( daoUtil.getString( nIndex++ ) );
            page.setNodeStatus( daoUtil.getInt( nIndex++ ) );
            page.setMimeType( daoUtil.getString( nIndex++ ) );
            page.setDateUpdate( daoUtil.getTimestamp( nIndex++ ) );
            page.setMetaKeywords( daoUtil.getString( nIndex++ ) );
            page.setMetaDescription( daoUtil.getString( nIndex++ ) );

            if ( daoUtil.getObject( nIndex ) != null )
            {
                page.setIdAuthorizationNode( daoUtil.getInt( nIndex ) );
            }
        }

        daoUtil.free(  );

        return page;
    }

    /**
     * {@inheritDoc }
     */
    public void updateAutorisationNode( int nIdPage, Integer nIdAutorisationNode )
    {
        StringBuilder strSQl = new StringBuilder(  );
        strSQl.append( SQL_QUERY_UPDATE_AUTORISATION_NODE );

        DAOUtil daoUtil = new DAOUtil( strSQl.toString(  ) );

        if ( nIdAutorisationNode != null )
        {
            daoUtil.setInt( 1, nIdAutorisationNode );
        }
        else
        {
            daoUtil.setIntNull( 1 );
        }

        daoUtil.setInt( 2, nIdPage );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc }
     */
    public List<Integer> selectPageForChangeAutorisationNode( int nIdParentPage )
    {
        List<Integer> listIdPage = new ArrayList<Integer>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CHILD_PAGE_FOR_MODIFY_AUTORISATION_NODE );

        daoUtil.setInt( 1, nIdParentPage );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            listIdPage.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return listIdPage;
    }
}
