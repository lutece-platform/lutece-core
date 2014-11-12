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
package fr.paris.lutece.portal.business.right;

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for Level objects
 */
public final class LevelDAO implements ILevelDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_level ) FROM core_level_right ";
    private static final String SQL_QUERY_SELECT = " SELECT id_level, name FROM core_level_right WHERE id_level = ?";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_level_right ( id_level, name ) VALUES ( ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_level_right WHERE id_level = ?";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_level_right SET id_level = ?, name = ? WHERE id_level = ?";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_level , name FROM core_level_right ORDER BY id_level";

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
     * @param level The level object
     */
    public synchronized void insert( Level level )
    {
        level.setId( newPrimaryKey(  ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( 1, level.getId(  ) );
        daoUtil.setString( 2, level.getName(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of Level from the table
     * @param nIdLevel The indentifier of the object Level
     * @return The Instance of the object Level
     */
    public Level load( int nIdLevel )
    {
        Level level = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nIdLevel );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            level = new Level(  );
            level.setId( daoUtil.getInt( 1 ) );
            level.setName( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return level;
    }

    /**
     * Delete a record from the table
     * @param nIdLevel The indentifier of the object Level
     */
    public void delete( int nIdLevel )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nIdLevel );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param level The instance of the Level to update
     */
    public void store( Level level )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        int nLevelId = level.getId(  );
        daoUtil.setInt( 1, nLevelId );
        daoUtil.setString( 2, level.getName(  ) );
        daoUtil.setInt( 3, nLevelId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Returns a list of all the right level
     * @return A collection of right level objects
     */
    public Collection<Level> selectLevelsList(  )
    {
        Collection<Level> levelList = new ArrayList<Level>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Level level = new Level(  );
            level.setId( daoUtil.getInt( 1 ) );
            level.setName( daoUtil.getString( 2 ) );
            levelList.add( level );
        }

        daoUtil.free(  );

        return levelList;
    }
}
