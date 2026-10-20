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

import jakarta.enterprise.inject.spi.CDI;

/**
 * The figures of the site shown on the admin home
 */
public final class SiteStatisticsHome
{
    private static ISiteStatisticsDAO _dao = CDI.current( ).select( ISiteStatisticsDAO.class ).get( );

    /**
     * Private constructor: static methods only
     */
    private SiteStatisticsHome( )
    {
    }

    /**
     * @return the number of pages of the site
     */
    public static int countPages( )
    {
        return _dao.countPages( );
    }

    /**
     * @return the number of pages restricted to a role
     */
    public static int countPagesRestrictedToRole( )
    {
        return _dao.countPagesRestrictedToRole( );
    }

    /**
     * @param dateSince
     *            the date from which the changes count
     * @return the number of pages changed since that date
     */
    public static int countPagesUpdatedSince( Timestamp dateSince )
    {
        return _dao.countPagesUpdatedSince( dateSince );
    }

    /**
     * @return the number of portlets of the site
     */
    public static int countPortlets( )
    {
        return _dao.countPortlets( );
    }

    /**
     * @param nStatus
     *            the status, Portlet.STATUS_PUBLISHED or Portlet.STATUS_UNPUBLISHED
     * @return the number of portlets of that status
     */
    public static int countPortletsWithStatus( int nStatus )
    {
        return _dao.countPortletsWithStatus( nStatus );
    }
}
