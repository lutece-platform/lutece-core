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

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for PageTemplate objects
 */
public final class PageTemplateDAO implements IPageTemplateDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_template ) FROM core_page_template";
    private static final String SQL_QUERY_SELECT = " SELECT id_template, description, file_name, picture FROM core_page_template WHERE id_template = ?";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_page_template ( id_template, description, file_name, picture ) VALUES ( ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_page_template WHERE id_template = ?";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_page_template SET id_template = ?, description = ?, file_name = ?, picture = ? " +
        " WHERE id_template = ?";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_template , description, file_name, picture FROM core_page_template ORDER BY id_template ";
    private static final String SQL_CHECK_PAGE_TEMPLATE_IS_USED = " SELECT id_template FROM core_page WHERE id_template = ? ";

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
     * Insert a new record in the table.
     * @param pageTemplate The Instance of the object PageTemplate
     */
    public synchronized void insert( PageTemplate pageTemplate )
    {
        pageTemplate.setId( newPrimaryKey(  ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setInt( 1, pageTemplate.getId(  ) );
        daoUtil.setString( 2, pageTemplate.getDescription(  ) );
        daoUtil.setString( 3, pageTemplate.getFile(  ) );
        daoUtil.setString( 4, pageTemplate.getPicture(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of PageTemplate from the table
     *
     * @param nPageTemplateId The indentifier of the object PageTemplate
     * @return The Instance of the object PageTemplate
     */
    public PageTemplate load( int nPageTemplateId )
    {
        PageTemplate pageTemplate = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nPageTemplateId );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            pageTemplate = new PageTemplate(  );
            pageTemplate.setId( daoUtil.getInt( 1 ) );
            pageTemplate.setDescription( daoUtil.getString( 2 ) );
            pageTemplate.setFile( daoUtil.getString( 3 ) );
            pageTemplate.setPicture( daoUtil.getString( 4 ) );
        }

        daoUtil.free(  );

        return pageTemplate;
    }

    /**
     * Delete a record from the table
     * @param nPageTemplateId The indentifier of the object PageTemplate
     */
    public void delete( int nPageTemplateId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPageTemplateId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param pageTemplate The instance of the PageTemplate to update
     */
    public void store( PageTemplate pageTemplate )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setInt( 1, pageTemplate.getId(  ) );
        daoUtil.setString( 2, pageTemplate.getDescription(  ) );
        daoUtil.setString( 3, pageTemplate.getFile(  ) );
        daoUtil.setString( 4, pageTemplate.getPicture(  ) );
        daoUtil.setInt( 5, pageTemplate.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Returns a list of all the page templates
     * @return A list of PageTemplates objects
     */
    public List<PageTemplate> selectPageTemplatesList(  )
    {
        List<PageTemplate> listPageTemplates = new ArrayList<PageTemplate>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            PageTemplate pageTemplate = new PageTemplate(  );

            pageTemplate.setId( daoUtil.getInt( 1 ) );
            pageTemplate.setDescription( daoUtil.getString( 2 ) );
            pageTemplate.setFile( daoUtil.getString( 3 ) );
            pageTemplate.setPicture( daoUtil.getString( 4 ) );
            listPageTemplates.add( pageTemplate );
        }

        daoUtil.free(  );

        return listPageTemplates;
    }

    /**
     * Checks if a page template has been used by a page
     * @param nPageTemplateId The identifier of the page template
     * @return true if a page template is used by a page, false otherwise
     */
    public boolean checkPageTemplateIsUsed( int nPageTemplateId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_CHECK_PAGE_TEMPLATE_IS_USED );

        daoUtil.setInt( 1, nPageTemplateId );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return true;
        }

        daoUtil.free(  );

        return false;
    }
}
