/*
 * Copyright (c) 2002-2026, City of Paris
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

import java.sql.Timestamp;

import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * The figures of the site, each one a count query
 */
@ApplicationScoped
public final class SiteStatisticsDAO implements ISiteStatisticsDAO
{
    private static final String SQL_QUERY_COUNT_PAGES = " SELECT COUNT(*) FROM core_page ";
    private static final String SQL_QUERY_COUNT_PAGES_RESTRICTED = " SELECT COUNT(*) FROM core_page WHERE role IS NOT NULL AND role <> ? AND role <> '' ";
    private static final String SQL_QUERY_COUNT_PAGES_UPDATED_SINCE = " SELECT COUNT(*) FROM core_page WHERE date_update >= ? ";
    private static final String SQL_QUERY_COUNT_PORTLETS = " SELECT COUNT(*) FROM core_portlet ";
    private static final String SQL_QUERY_COUNT_PORTLETS_WITH_STATUS = " SELECT COUNT(*) FROM core_portlet WHERE status = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public int countPages( )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_PAGES ) )
        {
            return count( daoUtil );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countPagesRestrictedToRole( )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_PAGES_RESTRICTED ) )
        {
            daoUtil.setString( 1, Page.ROLE_NONE );

            return count( daoUtil );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countPagesUpdatedSince( Timestamp dateSince )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_PAGES_UPDATED_SINCE ) )
        {
            daoUtil.setTimestamp( 1, dateSince );

            return count( daoUtil );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countPortlets( )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_PORTLETS ) )
        {
            return count( daoUtil );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countPortletsWithStatus( int nStatus )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_PORTLETS_WITH_STATUS ) )
        {
            daoUtil.setInt( 1, nStatus );

            return count( daoUtil );
        }
    }

    /**
     * Runs a count query
     *
     * @param daoUtil
     *            the query, its parameters set
     * @return the count
     */
    private static int count( DAOUtil daoUtil )
    {
        daoUtil.executeQuery( );

        return daoUtil.next( ) ? daoUtil.getInt( 1 ) : 0;
    }
}
