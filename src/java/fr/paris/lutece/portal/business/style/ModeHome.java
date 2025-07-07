/*
 * Copyright (c) 2002-2025, City of Paris
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.transform.OutputKeys;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * This class provides instances management methods (create, find, ...) for Mode objects
 */
public final class ModeHome
{
    private static final Config _config = ConfigProvider.getConfig( );
    private static Map<Integer, Mode> _modes;
    
    /**
     * Creates a new ModeHome object.
     */
    private ModeHome( )
    {
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an mode whose identifier is specified in parameter
     *
     * @param nKey
     *            The mode primary key
     * @return an instance of a mode
     */
    public static Mode findByPrimaryKey( int nKey )
    {
        if (null == _modes)
        {
            initModes();
        }
        return _modes.get( nKey );
    }

    private static synchronized void initModes( )
    {
        List<Integer> modeIds = _config.getOptionalValues( "lutece.style.modes", Integer.class ).orElse( new ArrayList<Integer>( ) );
        _modes = new TreeMap<Integer, Mode>( );

        for ( Integer id : modeIds )
        {
            Mode mode = new Mode( );
            String strDescription = _config.getOptionalValue( "lutece.style.mode." + id + ".description", String.class ).orElse( null );
            String strPath = _config.getOptionalValue( "lutece.style.mode." + id + ".path", String.class ).orElse( null );
            String strMethod = _config.getOptionalValue( "lutece.style.mode." + id + ".outputXslMethod", String.class ).orElse( "xml" );
            String strVersion = _config.getOptionalValue( "lutece.style.mode." + id + ".outputXslVersion", String.class ).orElse( "1.0" );
            String strEncoding = _config.getOptionalValue( "lutece.style.mode." + id + ".outputXslEncoding", String.class ).orElse( "UTF-8" );
            String strIndent = _config.getOptionalValue( "lutece.style.mode." + id + ".outputXslIndent", String.class ).orElse( "yes" );
            String strOmitXmlDeclaration = _config.getOptionalValue( "lutece.style.mode." + id + ".outputXslOmitXmlDec", String.class ).orElse( "yes" );
            String strMediaType = _config.getOptionalValue( "lutece.style.mode." + id + ".outputXslMediaType", String.class ).orElse( "text/xml" );
            String strStandalone = _config.getOptionalValue( "lutece.style.mode." + id + ".outputXslStandalone", String.class ).orElse( null );
            mode.setId( id );
            mode.setDescription( strDescription );
            mode.setPath( strPath );
            mode.setOutputXslPropertyMethod( strMethod );
            mode.setOutputXslPropertyVersion( strVersion );
            mode.setOutputXslPropertyEncoding( strEncoding );
            mode.setOutputXslPropertyIndent( strIndent );
            mode.setOutputXslPropertyOmitXmlDeclaration( strOmitXmlDeclaration );
            mode.setOutputXslPropertyMediaType( strMediaType );
            mode.setOutputXslPropertyStandalone( strStandalone );
            _modes.put( id, mode );
        }
    }

    /**
     * Returns a reference list which contains all the modes
     *
     * @return a reference list
     */
    public static ReferenceList getModes( )
    {
        if (null == _modes)
        {
            initModes();
        }
        
        ReferenceList modesList = new ReferenceList( );
        for ( Mode mode : _modes.values( ) )
        {
            modesList.addItem( mode.getId( ), mode.getDescription( ) );
        }
        return modesList;
    }

    /**
     * Returns a set of properties used for xsl output
     *
     * @param nKey
     *            The mode primary key
     * @return the output properties to use for xsl transformation
     */
    public static Properties getOuputXslProperties( int nKey )
    {
        Mode mode = findByPrimaryKey( nKey );
        Properties ouputProperties = new Properties( );

        String strMethod = mode.getOutputXslPropertyMethod( );

        if ( ( strMethod != null ) && ( !strMethod.trim( ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.METHOD, strMethod );
        }

        String strVersion = mode.getOutputXslPropertyVersion( );

        if ( ( strVersion != null ) && ( !strVersion.trim( ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.VERSION, strVersion );
        }

        String strEncoding = mode.getOutputXslPropertyEncoding( );

        if ( ( strEncoding != null ) && ( !strEncoding.trim( ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.ENCODING, strEncoding );
        }

        String strIndent = mode.getOutputXslPropertyIndent( );

        if ( ( strIndent != null ) && ( !strIndent.trim( ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.INDENT, strIndent );
        }

        String strOmitXmlDeclaration = mode.getOutputXslPropertyOmitXmlDeclaration( );

        if ( ( strOmitXmlDeclaration != null ) && ( !strOmitXmlDeclaration.trim( ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.OMIT_XML_DECLARATION, strOmitXmlDeclaration );
        }

        String strMediaType = mode.getOutputXslPropertyMediaType( );

        if ( ( strMediaType != null ) && ( !strMediaType.trim( ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.MEDIA_TYPE, strMediaType );
        }

        String strStandalone = mode.getOutputXslPropertyStandalone( );

        if ( ( strStandalone != null ) && ( !strStandalone.trim( ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.STANDALONE, strStandalone );
        }

        return ouputProperties;
    }
}
