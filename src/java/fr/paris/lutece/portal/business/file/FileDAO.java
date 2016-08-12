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
package fr.paris.lutece.portal.business.file;

import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Timestamp;
import java.util.Date;

/**
 * This class provides Data Access methods for Field objects
 */
public final class FileDAO implements IFileDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_file ) FROM core_file";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_file,title,id_physical_file,file_size,mime_type,date_creation" +
        " FROM core_file WHERE id_file = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_file(id_file,title,id_physical_file,file_size,mime_type,date_creation)" +
        " VALUES(?,?,?,?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_file WHERE id_file = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE  core_file SET " +
        "id_file=?,title=?,id_physical_file=?,file_size=?,mime_type=? WHERE id_file = ?";

    /**
     * Generates a new primary key
     *
     * @return The new primary key
     */
    @Override
    public int newPrimaryKey(  )
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
     *
     * @param file instance of the File object to insert
     * @return the id of the new file
     */
    
    @Override
    public synchronized int insert( File file )
    {
        file.setIdFile( newPrimaryKey(  ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setString( 2, file.getTitle(  ) );

        if ( file.getPhysicalFile(  ) != null )
        {
            daoUtil.setInt( 3, file.getPhysicalFile(  ).getIdPhysicalFile(  ) );
        }
        else
        {
            daoUtil.setIntNull( 3 );
        }

        daoUtil.setInt( 4, file.getSize(  ) );
        daoUtil.setString( 5, file.getMimeType(  ) );
        daoUtil.setTimestamp( 6, new Timestamp( new Date(  ).getTime(  ) ) );
        daoUtil.setInt( 1, file.getIdFile(  ) );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );

        return file.getIdFile(  );
    }

    /**
     * Load the data of the File from the table
     *
     * @param nId The identifier of the file
     * @return the instance of the File
     */
    @Override
    public File load( int nId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        File file = null;
        PhysicalFile physicalFile = null;

        if ( daoUtil.next(  ) )
        {
            file = new File(  );
            file.setIdFile( daoUtil.getInt( 1 ) );
            file.setTitle( daoUtil.getString( 2 ) );

            if ( daoUtil.getObject( 3 ) != null )
            {
                physicalFile = new PhysicalFile(  );
                physicalFile.setIdPhysicalFile( daoUtil.getInt( 3 ) );
                file.setPhysicalFile( physicalFile );
            }

            file.setSize( daoUtil.getInt( 4 ) );
            file.setMimeType( daoUtil.getString( 5 ) );
            file.setDateCreation( daoUtil.getTimestamp( 6 ));
        }

        daoUtil.free(  );

        return file;
    }

    /**
     * Delete a record from the table
     *
     * @param nIdFile The identifier of the file
     */
    @Override
    public void delete( int nIdFile )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nIdFile );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the file in the table
     *
     * @param file instance of the File object to update
     */
    @Override
    public void store( File file )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, file.getIdFile(  ) );
        daoUtil.setString( 2, file.getTitle(  ) );

        if ( file.getPhysicalFile(  ) != null )
        {
            daoUtil.setInt( 3, file.getPhysicalFile(  ).getIdPhysicalFile(  ) );
        }
        else
        {
            daoUtil.setIntNull( 3 );
        }

        daoUtil.setInt( 4, file.getSize(  ) );
        daoUtil.setString( 5, file.getMimeType(  ) );
        daoUtil.setInt( 6, file.getIdFile(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
