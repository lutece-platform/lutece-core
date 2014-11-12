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

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.Properties;

import javax.xml.transform.OutputKeys;


/**
 * This class provides instances management methods (create, find, ...) for Mode objects
 */
public final class ModeHome
{
    // Static variable pointed at the DAO instance
    private static IModeDAO _dao = (IModeDAO) SpringContextService.getBean( "modeDAO" );

    /**
     * Creates a new ModeHome object.
     */
    private ModeHome(  )
    {
    }

    /**
     * Creation of an instance of a mode
     *
     * @param mode An instance of a mode which contains the informations to store
     * @return The instance of a mode which has been created with its primary key.
     */
    public static Mode create( Mode mode )
    {
        _dao.insert( mode );

        return mode;
    }

    /**
     * Update of the mode which is specified
     *
     * @param mode The instance of the mode which contains the data to store
     * @return The instance of the mode which has been updated
     */
    public static Mode update( Mode mode )
    {
        _dao.store( mode );

        return mode;
    }

    /**
     * Remove the mode whose identifier is specified in parameter
     *
     * @param nId The identifier of the mode to remove
     */
    public static void remove( int nId )
    {
        _dao.delete( nId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an mode whose identifier is specified in parameter
     *
     * @param nKey The mode primary key
     * @return an instance of a mode
     */
    public static Mode findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Return the list of all the modes
     *
     * @return A collection of modes objects
     */
    public static Collection<Mode> getModesList(  )
    {
        return _dao.selectModesList(  );
    }

    /**
     * Returns a reference list which contains all the modes
     *
     * @return a reference list
     */
    public static ReferenceList getModes(  )
    {
        return _dao.getModesList(  );
    }

    /**
     * Returns a set of properties used for xsl output
     *
     * @param nKey The mode primary key
     * @return the output properties to use for xsl transformation
     */
    public static Properties getOuputXslProperties( int nKey )
    {
        Mode mode = _dao.load( nKey );
        Properties ouputProperties = new Properties(  );

        String strMethod = mode.getOutputXslPropertyMethod(  );

        if ( ( strMethod != null ) && ( !strMethod.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.METHOD, strMethod );
        }

        String strVersion = mode.getOutputXslPropertyVersion(  );

        if ( ( strVersion != null ) && ( !strVersion.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.VERSION, strVersion );
        }

        String strEncoding = mode.getOutputXslPropertyEncoding(  );

        if ( ( strEncoding != null ) && ( !strEncoding.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.ENCODING, strEncoding );
        }

        String strIndent = mode.getOutputXslPropertyIndent(  );

        if ( ( strIndent != null ) && ( !strIndent.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.INDENT, strIndent );
        }

        String strOmitXmlDeclaration = mode.getOutputXslPropertyOmitXmlDeclaration(  );

        if ( ( strOmitXmlDeclaration != null ) && ( !strOmitXmlDeclaration.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.OMIT_XML_DECLARATION, strOmitXmlDeclaration );
        }

        String strMediaType = mode.getOutputXslPropertyMediaType(  );

        if ( ( strMediaType != null ) && ( !strMediaType.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.MEDIA_TYPE, strMediaType );
        }

        String strStandalone = mode.getOutputXslPropertyStandalone(  );

        if ( ( strStandalone != null ) && ( !strStandalone.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.STANDALONE, strStandalone );
        }

        return ouputProperties;
    }
}
