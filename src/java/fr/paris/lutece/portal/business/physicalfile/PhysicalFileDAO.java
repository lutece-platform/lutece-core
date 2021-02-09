/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.portal.business.physicalfile;

import java.sql.Statement;

import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Field objects
 */
public final class PhysicalFileDAO implements IPhysicalFileDAO
{
    // Constants
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_physical_file,file_value" + " FROM core_physical_file WHERE id_physical_file = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_physical_file(file_value)" + " VALUES(?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_physical_file WHERE id_physical_file = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE  core_physical_file SET " + "id_physical_file=?,file_value=? WHERE id_physical_file = ?";

    /**
     * {@inheritDoc}
     */
    @Override
    public int insert( PhysicalFile physicalFile )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            daoUtil.setBytes( 1, physicalFile.getValue( ) );
            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                physicalFile.setIdPhysicalFile( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        return physicalFile.getIdPhysicalFile( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PhysicalFile load( int nId )
    {
        PhysicalFile physicalFile = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                int nIndex = 1;
                physicalFile = new PhysicalFile( );
                physicalFile.setIdPhysicalFile( daoUtil.getInt( nIndex++ ) );
                physicalFile.setValue( daoUtil.getBytes( nIndex ) );
            }

        }

        return physicalFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdPhysicalFile )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nIdPhysicalFile );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( PhysicalFile physicalFile )
    {
        int nIndex = 1;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            daoUtil.setInt( nIndex++, physicalFile.getIdPhysicalFile( ) );
            daoUtil.setBytes( nIndex++, physicalFile.getValue( ) );
            daoUtil.setInt( nIndex, physicalFile.getIdPhysicalFile( ) );
            daoUtil.executeUpdate( );
        }
    }
}
