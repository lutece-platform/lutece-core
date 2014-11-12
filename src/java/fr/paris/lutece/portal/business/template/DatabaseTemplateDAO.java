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
package fr.paris.lutece.portal.business.template;

import fr.paris.lutece.util.sql.DAOUtil;

import org.apache.commons.lang.StringUtils;


/**
 * Implementation of interface IDatabaseTemplateDAO
 * @author vbroussard
 *
 */
public class DatabaseTemplateDAO implements IDatabaseTemplateDAO
{
    private static final String SQL_QUERY_SELECT_TEMPLATE_FROM_KEY = "SELECT template_value FROM core_template WHERE template_name = ? ";
    private static final String SQL_UPDATE_TEMPLATE = "UPDATE core_template SET template_value = ?  WHERE template_name = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplateFromKey( String strKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_TEMPLATE_FROM_KEY );
        daoUtil.setString( 1, strKey );

        String strTemplate = StringUtils.EMPTY;

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            strTemplate = daoUtil.getString( 1 );
        }

        daoUtil.free(  );

        return strTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTemplate( String strKey, String strValue )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_UPDATE_TEMPLATE );
        daoUtil.setString( 1, strValue );
        daoUtil.setString( 2, strKey );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }
}
