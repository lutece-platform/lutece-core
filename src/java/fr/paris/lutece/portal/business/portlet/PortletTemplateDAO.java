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

import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Data access of the portlet templates
 */
@ApplicationScoped
public final class PortletTemplateDAO implements IPortletTemplateDAO
{
    private static final String SQL_QUERY_SELECT_COLUMNS = "SELECT id_template, id_portlet_type, description, template_path FROM core_portlet_template";
    private static final String SQL_QUERY_SELECTALL = SQL_QUERY_SELECT_COLUMNS + " ORDER BY id_portlet_type, id_template";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECT_COLUMNS + " WHERE id_template = ?";
    private static final String SQL_QUERY_SELECT_BY_PORTLET_TYPE = SQL_QUERY_SELECT_COLUMNS + " WHERE id_portlet_type = ? ORDER BY id_template";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_portlet_template ( id_portlet_type, description, template_path ) VALUES ( ?, ?, ? )";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_portlet_template SET id_portlet_type = ?, description = ?, template_path = ? WHERE id_template = ?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_portlet_template WHERE id_template = ?";
    private static final String SQL_QUERY_CHECK_IS_USED = "SELECT COUNT(*) FROM core_portlet WHERE id_template = ?";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( PortletTemplate template )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, template.getPortletTypeId( ) );
            daoUtil.setString( nIndex++, template.getDescription( ) );
            daoUtil.setString( nIndex++, template.getTemplatePath( ) );
            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                template.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( PortletTemplate template )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, template.getPortletTypeId( ) );
            daoUtil.setString( nIndex++, template.getDescription( ) );
            daoUtil.setString( nIndex++, template.getTemplatePath( ) );
            daoUtil.setInt( nIndex++, template.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nIdTemplate )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nIdTemplate );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public PortletTemplate load( int nIdTemplate )
    {
        PortletTemplate template = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nIdTemplate );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                template = dataToObject( daoUtil );
            }
        }

        return template;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<PortletTemplate> selectAll( )
    {
        List<PortletTemplate> listTemplates = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                listTemplates.add( dataToObject( daoUtil ) );
            }
        }

        return listTemplates;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<PortletTemplate> selectByPortletType( String strPortletTypeId )
    {
        List<PortletTemplate> listTemplates = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PORTLET_TYPE ) )
        {
            daoUtil.setString( 1, strPortletTypeId );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                listTemplates.add( dataToObject( daoUtil ) );
            }
        }

        return listTemplates;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean checkTemplateIsUsed( int nIdTemplate )
    {
        boolean bIsUsed = false;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_IS_USED ) )
        {
            daoUtil.setInt( 1, nIdTemplate );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                bIsUsed = daoUtil.getInt( 1 ) > 0;
            }
        }

        return bIsUsed;
    }

    /**
     * Builds a template from the current row
     *
     * @param daoUtil
     *            the positioned DAOUtil
     * @return the template
     */
    private PortletTemplate dataToObject( DAOUtil daoUtil )
    {
        int nIndex = 1;
        PortletTemplate template = new PortletTemplate( );
        template.setId( daoUtil.getInt( nIndex++ ) );
        template.setPortletTypeId( daoUtil.getString( nIndex++ ) );
        template.setDescription( daoUtil.getString( nIndex++ ) );
        template.setTemplatePath( daoUtil.getString( nIndex++ ) );

        return template;
    }
}
