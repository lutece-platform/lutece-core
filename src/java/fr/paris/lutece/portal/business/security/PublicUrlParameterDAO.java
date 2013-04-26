/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.portal.business.security;

import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;


/**
*
* Class PublicUrlParameterDAO
* Provides data access to public url  parameters in MySQL database
*
*/
public class PublicUrlParameterDAO implements IPublicUrlParameterDAO
{
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_public_url_parameter(parameter_key, parameter_value) VALUES( ? , ? ) ";
    private static final String SQL_QUERY_SELECT = " SELECT parameter_value FROM core_public_url_parameter WHERE parameter_key = ?  ORDER BY parameter_value ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_public_url_parameter SET parameter_value = ? WHERE parameter_key = ? ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT parameter_key, parameter_value FROM core_public_url_parameter ORDER BY parameter_key ";
    private static final String SQL_QUERY_DELETE = " DELETE  FROM core_public_url_parameter WHERE parameter_key = ? ";

    /** {@inheritDoc} */
    @Override
    public void insert( ReferenceItem param )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setString( 1, param.getCode(  ) );
        daoUtil.setString( 2, param.getName(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /** {@inheritDoc} */
    @Override
    public ReferenceList findAllByKey( String strParameterKey )
    {
        ReferenceList parametersList = new ReferenceList(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setString( 1, strParameterKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            parametersList.addItem( strParameterKey, daoUtil.getString( 1 ) );
        }

        daoUtil.free(  );

        return parametersList;
    }

    /** {@inheritDoc} */
    @Override
    public ReferenceItem load( String strParameterKey )
    {
        ReferenceItem param = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setString( 1, strParameterKey );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            param = new ReferenceItem(  );
            param.setCode( strParameterKey );
            param.setName( daoUtil.getString( 1 ) );
        }

        daoUtil.free(  );

        return param;
    }

    /** {@inheritDoc} */
    @Override
    public void store( ReferenceItem param )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setString( 1, param.getName(  ) );
        daoUtil.setString( 2, param.getCode(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /** {@inheritDoc} */
    @Override
    public void delete( String strParameterKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setString( 1, strParameterKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /** {@inheritDoc} */
    @Override
    public ReferenceList findAll(  )
    {
        ReferenceList parametersList = new ReferenceList(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            parametersList.addItem( daoUtil.getString( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return parametersList;
    }
}
