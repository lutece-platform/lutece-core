/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.portal.business.file;

import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class provides Data Access methods for Field objects
 */
public final class TemporaryFileDAO implements ITemporaryFileDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT_ALL = "SELECT id_file,id_user,title,description,id_physical_file,file_size,mime_type,date_creation"
            + " FROM core_temporary_file ";
    private static final String SQL_QUERY_FIND_BY_USER = SQL_QUERY_SELECT_ALL + " WHERE id_user = ? ORDER BY date_creation desc";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = SQL_QUERY_SELECT_ALL + " WHERE id_file = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_temporary_file(id_user,title,description,id_physical_file,file_size,mime_type,date_creation)"
            + " VALUES(?,?,?,?,?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_temporary_file WHERE id_file = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_temporary_file SET id_file=?,id_user=?,title=?,description=?,id_physical_file=?,file_size=?,mime_type=? WHERE id_file = ?";

    /**
     * Insert a new record in the table.
     *
     * @param file instance of the File object to insert
     * @return the id of the new file
     */

    @Override
    public int insert( TemporaryFile file )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, file.getUser( ).getUserId( ) );
            daoUtil.setString( nIndex++, file.getTitle( ) );
            daoUtil.setString( nIndex++, file.getDescription( ) );

            if ( file.getPhysicalFile( ) != null )
            {
                daoUtil.setInt( nIndex++, file.getPhysicalFile( ).getIdPhysicalFile( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            daoUtil.setInt( nIndex++, file.getSize( ) );
            daoUtil.setString( nIndex++, file.getMimeType( ) );
            daoUtil.setTimestamp( nIndex, new Timestamp( new Date( ).getTime( ) ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                file.setIdFile( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }

        return file.getIdFile( );
    }

    /**
     * Load the data of the File from the table
     *
     * @param nId The identifier of the file
     * @return the instance of the File
     */
    @Override
    public TemporaryFile load( int nId )
    {
        TemporaryFile file = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                file = dataToObject( daoUtil );
            }

        }
        return file;
    }

    @Override
    public List<TemporaryFile> findByUser( AdminUser user )
    {
        List<TemporaryFile> fileList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_USER ) )
        {
            daoUtil.setInt( 1, user.getUserId( ) );
            daoUtil.executeQuery( );
            
            while ( daoUtil.next( ) )
            {
                fileList.add( dataToObject( daoUtil ) );
            }
        }

        return fileList;
    }

    /**
     * Delete a record from the table
     *
     * @param nIdFile The identifier of the file
     */
    @Override
    public void delete( int nIdFile )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nIdFile );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * Update the file in the table
     *
     * @param file instance of the File object to update
     */
    @Override
    public void store( TemporaryFile file )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, file.getIdFile( ) );
            daoUtil.setInt( nIndex++, file.getUser( ).getUserId( ) );
            daoUtil.setString( nIndex++, file.getTitle( ) );
            daoUtil.setString( nIndex++, file.getDescription( ) );

            if ( file.getPhysicalFile( ) != null )
            {
                daoUtil.setInt( nIndex++, file.getPhysicalFile( ).getIdPhysicalFile( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            daoUtil.setInt( nIndex++, file.getSize( ) );
            daoUtil.setString( nIndex++, file.getMimeType( ) );
            daoUtil.setInt( nIndex, file.getIdFile( ) );
            daoUtil.executeUpdate( );
        }
    }

    private TemporaryFile dataToObject( DAOUtil daoUtil )
    {
        int nIndex = 1;
        TemporaryFile file = new TemporaryFile( );
        file.setIdFile( daoUtil.getInt( nIndex++ ) );

        AdminUser user = new AdminUser( );
        user.setUserId( daoUtil.getInt( nIndex++ ) );
        file.setUser( user );

        file.setTitle( daoUtil.getString( nIndex++ ) );
        file.setDescription( daoUtil.getString( nIndex++ ) );

        if ( daoUtil.getObject( nIndex ) != null )
        {
            PhysicalFile physicalFile = new PhysicalFile( );
            physicalFile.setIdPhysicalFile( daoUtil.getInt( nIndex ) );
            file.setPhysicalFile( physicalFile );
        }
        nIndex++;

        file.setSize( daoUtil.getInt( nIndex++ ) );
        file.setMimeType( daoUtil.getString( nIndex++ ) );
        file.setDateCreation( daoUtil.getTimestamp( nIndex ) );

        return file;
    }
}
