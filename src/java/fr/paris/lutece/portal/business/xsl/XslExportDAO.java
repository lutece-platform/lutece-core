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
package fr.paris.lutece.portal.business.xsl;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for XSL objects
 */
public final class XslExportDAO implements IXslExportDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_xsl_export ) FROM core_xsl_export";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_xsl_export,title,description,extension,id_file,plugin FROM core_xsl_export WHERE id_xsl_export = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_xsl_export( id_xsl_export,title,description,extension,id_file,plugin) VALUES(?,?,?,?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_xsl_export WHERE id_xsl_export = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_xsl_export SET id_xsl_export=?,title=?,description=?,extension=?,id_file=?,plugin=? WHERE id_xsl_export = ? ";
    private static final String SQL_QUERY_SELECT = "SELECT id_xsl_export,title,description,extension,id_file,plugin FROM core_xsl_export ";
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_FILTER_PLUGIN = " plugin = ? ";

    /**
     * {@inheritDoc}
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
        else
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( XslExport xslExport )
    {
        xslExport.setIdXslExport( newPrimaryKey(  ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( 1, xslExport.getIdXslExport(  ) );
        daoUtil.setString( 2, xslExport.getTitle(  ) );
        daoUtil.setString( 3, xslExport.getDescription(  ) );
        daoUtil.setString( 4, xslExport.getExtension(  ) );

        if ( xslExport.getFile(  ) != null )
        {
            daoUtil.setInt( 5, xslExport.getFile(  ).getIdFile(  ) );
        }
        else
        {
            daoUtil.setIntNull( 5 );
        }

        daoUtil.setString( 6, xslExport.getPlugin(  ) );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XslExport load( int nId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        XslExport xslExport = null;
        File file = null;

        if ( daoUtil.next(  ) )
        {
            xslExport = new XslExport(  );
            xslExport.setIdXslExport( daoUtil.getInt( 1 ) );
            xslExport.setTitle( daoUtil.getString( 2 ) );
            xslExport.setDescription( daoUtil.getString( 3 ) );
            xslExport.setExtension( daoUtil.getString( 4 ) );

            if ( daoUtil.getObject( 5 ) != null )
            {
                file = new File(  );
                file.setIdFile( daoUtil.getInt( 5 ) );
                xslExport.setFile( file );
            }

            xslExport.setPlugin( daoUtil.getString( 6 ) );
        }

        daoUtil.free(  );

        return xslExport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdXslExport )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nIdXslExport );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( XslExport xslExport )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, xslExport.getIdXslExport(  ) );
        daoUtil.setString( 2, xslExport.getTitle(  ) );
        daoUtil.setString( 3, xslExport.getDescription(  ) );
        daoUtil.setString( 4, xslExport.getExtension(  ) );

        if ( xslExport.getFile(  ) != null )
        {
            daoUtil.setInt( 5, xslExport.getFile(  ).getIdFile(  ) );
        }
        else
        {
            daoUtil.setIntNull( 5 );
        }

        daoUtil.setString( 6, xslExport.getPlugin(  ) );

        daoUtil.setInt( 7, xslExport.getIdXslExport(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<XslExport> selectList(  )
    {
        List<XslExport> listXslExport = new ArrayList<XslExport>(  );
        XslExport xslExport = null;
        File file = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            xslExport = new XslExport(  );
            xslExport.setIdXslExport( daoUtil.getInt( 1 ) );
            xslExport.setTitle( daoUtil.getString( 2 ) );
            xslExport.setDescription( daoUtil.getString( 3 ) );
            xslExport.setExtension( daoUtil.getString( 4 ) );

            if ( daoUtil.getObject( 5 ) != null )
            {
                file = new File(  );
                file.setIdFile( daoUtil.getInt( 5 ) );
                xslExport.setFile( file );
            }

            xslExport.setPlugin( daoUtil.getString( 6 ) );

            listXslExport.add( xslExport );
        }

        daoUtil.free(  );

        return listXslExport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<XslExport> selectListByPlugin( Plugin plugin )
    {
        List<XslExport> listXslExport = new ArrayList<XslExport>(  );
        XslExport xslExport = null;
        File file = null;

        StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT );
        sbSql.append( SQL_WHERE );
        sbSql.append( SQL_FILTER_PLUGIN );

        DAOUtil daoUtil = new DAOUtil( sbSql.toString(  ) );
        daoUtil.setString( 1, plugin.getName(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            xslExport = new XslExport(  );
            xslExport.setIdXslExport( daoUtil.getInt( 1 ) );
            xslExport.setTitle( daoUtil.getString( 2 ) );
            xslExport.setDescription( daoUtil.getString( 3 ) );
            xslExport.setExtension( daoUtil.getString( 4 ) );

            if ( daoUtil.getObject( 5 ) != null )
            {
                file = new File(  );
                file.setIdFile( daoUtil.getInt( 5 ) );
                xslExport.setFile( file );
            }

            xslExport.setPlugin( daoUtil.getString( 6 ) );

            listXslExport.add( xslExport );
        }

        daoUtil.free(  );

        return listXslExport;
    }
}
