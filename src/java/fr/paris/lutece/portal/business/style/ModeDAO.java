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
package fr.paris.lutece.portal.business.style;

import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for Mode objects
 */
public final class ModeDAO implements IModeDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_mode ) FROM core_mode";
    private static final String SQL_QUERY_SELECT = " SELECT id_mode, description_mode, path, output_xsl_method, output_xsl_version, " +
        " output_xsl_media_type, output_xsl_encoding, output_xsl_indent, output_xsl_omit_xml_dec, " +
        " output_xsl_standalone FROM core_mode WHERE id_mode = ?";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_mode ( id_mode, description_mode, path, output_xsl_method, output_xsl_version, output_xsl_media_type, output_xsl_encoding, output_xsl_indent, output_xsl_omit_xml_dec, output_xsl_standalone ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_mode WHERE id_mode = ?";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_mode SET description_mode = ?, path = ?, " +
        " output_xsl_method = ? , output_xsl_version = ?, output_xsl_media_type = ?, output_xsl_encoding = ?, " +
        " output_xsl_indent = ?, output_xsl_omit_xml_dec = ?, output_xsl_standalone = ?" + " WHERE id_mode = ?";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_mode, description_mode, path, output_xsl_method, output_xsl_version, output_xsl_media_type, " +
        " output_xsl_encoding, output_xsl_indent, output_xsl_omit_xml_dec, output_xsl_standalone " +
        " FROM core_mode ORDER BY id_mode";
    private static final String SQL_QUERY_SELECT_MODES = " SELECT id_mode , description_mode FROM core_mode";

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
     * @param mode The mode object
     */
    public synchronized void insert( Mode mode )
    {
        mode.setId( newPrimaryKey(  ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setInt( 1, mode.getId(  ) );
        daoUtil.setString( 2, mode.getDescription(  ) );
        daoUtil.setString( 3, mode.getPath(  ) );
        daoUtil.setString( 4, mode.getOutputXslPropertyMethod(  ) );
        daoUtil.setString( 5, mode.getOutputXslPropertyVersion(  ) );
        daoUtil.setString( 6, mode.getOutputXslPropertyMediaType(  ) );
        daoUtil.setString( 7, mode.getOutputXslPropertyEncoding(  ) );
        daoUtil.setString( 8, mode.getOutputXslPropertyIndent(  ) );
        daoUtil.setString( 9, mode.getOutputXslPropertyOmitXmlDeclaration(  ) );
        daoUtil.setString( 10, mode.getOutputXslPropertyStandalone(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of Level from the table
     *
     * @param nIdMode The indentifier of the object Mode
     * @return The Instance of the object Mode
     */
    public Mode load( int nIdMode )
    {
        Mode mode = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nIdMode );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            mode = new Mode(  );
            mode.setId( daoUtil.getInt( 1 ) );
            mode.setDescription( daoUtil.getString( 2 ) );
            mode.setPath( daoUtil.getString( 3 ) );
            mode.setOutputXslPropertyMethod( daoUtil.getString( 4 ) );
            mode.setOutputXslPropertyVersion( daoUtil.getString( 5 ) );
            mode.setOutputXslPropertyMediaType( daoUtil.getString( 6 ) );
            mode.setOutputXslPropertyEncoding( daoUtil.getString( 7 ) );
            mode.setOutputXslPropertyIndent( daoUtil.getString( 8 ) );
            mode.setOutputXslPropertyOmitXmlDeclaration( daoUtil.getString( 9 ) );
            mode.setOutputXslPropertyStandalone( daoUtil.getString( 10 ) );
        }

        daoUtil.free(  );

        return mode;
    }

    /**
     * Delete a record from the table
     * @param nModeId The indentifier of the object Mode
     */
    public void delete( int nModeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nModeId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param mode The instance of the Mode to update
     */
    public void store( Mode mode )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( 1, mode.getDescription(  ) );
        daoUtil.setString( 2, mode.getPath(  ) );
        daoUtil.setString( 3, mode.getOutputXslPropertyMethod(  ) );
        daoUtil.setString( 4, mode.getOutputXslPropertyVersion(  ) );
        daoUtil.setString( 5, mode.getOutputXslPropertyMediaType(  ) );
        daoUtil.setString( 6, mode.getOutputXslPropertyEncoding(  ) );
        daoUtil.setString( 7, mode.getOutputXslPropertyIndent(  ) );
        daoUtil.setString( 8, mode.getOutputXslPropertyOmitXmlDeclaration(  ) );
        daoUtil.setString( 9, mode.getOutputXslPropertyStandalone(  ) );
        daoUtil.setInt( 10, mode.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Returns a list of all the modes
     * @return A collection of modes objects
     */
    public Collection<Mode> selectModesList(  )
    {
        Collection<Mode> modeList = new ArrayList<Mode>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Mode mode = new Mode(  );

            mode.setId( daoUtil.getInt( 1 ) );
            mode.setDescription( daoUtil.getString( 2 ) );
            mode.setPath( daoUtil.getString( 3 ) );
            mode.setOutputXslPropertyMethod( daoUtil.getString( 4 ) );
            mode.setOutputXslPropertyVersion( daoUtil.getString( 5 ) );
            mode.setOutputXslPropertyMediaType( daoUtil.getString( 6 ) );
            mode.setOutputXslPropertyEncoding( daoUtil.getString( 7 ) );
            mode.setOutputXslPropertyIndent( daoUtil.getString( 8 ) );
            mode.setOutputXslPropertyOmitXmlDeclaration( daoUtil.getString( 9 ) );
            mode.setOutputXslPropertyStandalone( daoUtil.getString( 10 ) );

            modeList.add( mode );
        }

        daoUtil.free(  );

        return modeList;
    }

    /**
     * Returns the list of the modes in form of a reference list
     *
     * @return the modes list in form of a ReferenceList object
     */
    public ReferenceList getModesList(  )
    {
        ReferenceList modesList = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MODES );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            modesList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return modesList;
    }
}
