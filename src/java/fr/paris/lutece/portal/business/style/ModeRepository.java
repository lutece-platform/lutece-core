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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.transform.OutputKeys;

import org.eclipse.microprofile.config.Config;

import fr.paris.lutece.util.ReferenceList;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ModeRepository implements IModeRepository
{

    private static final String MODES_PROPERTY = "lutece.style.modes";
    private static final String MODE_PROPERTY_PREFIX = "lutece.style.mode.";

    @Inject
    private Config _config;
    private Map<Integer, Mode> _modes;

    @PostConstruct
    void init( )
    {
        initModes( );
    }

    private synchronized void initModes( )
    {
        List<Integer> modeIds = _config.getOptionalValues( MODES_PROPERTY, Integer.class ).orElse( new ArrayList<Integer>( ) );
        _modes = new TreeMap<Integer, Mode>( );

        for ( Integer id : modeIds )
        {
            Mode mode = new Mode( );
            String strDescription = _config.getOptionalValue( MODE_PROPERTY_PREFIX + id + ".description", String.class ).orElse( null );
            String strPath = _config.getOptionalValue( MODE_PROPERTY_PREFIX + id + ".path", String.class ).orElse( null );
            String strMethod = _config.getOptionalValue( MODE_PROPERTY_PREFIX + id + ".outputXslMethod", String.class ).orElse( "xml" );
            String strVersion = _config.getOptionalValue( MODE_PROPERTY_PREFIX + id + ".outputXslVersion", String.class ).orElse( "1.0" );
            String strEncoding = _config.getOptionalValue( MODE_PROPERTY_PREFIX + id + ".outputXslEncoding", String.class ).orElse( "UTF-8" );
            String strIndent = _config.getOptionalValue( MODE_PROPERTY_PREFIX + id + ".outputXslIndent", String.class ).orElse( "yes" );
            String strOmitXmlDeclaration = _config.getOptionalValue( MODE_PROPERTY_PREFIX + id + ".outputXslOmitXmlDec", String.class ).orElse( "yes" );
            String strMediaType = _config.getOptionalValue( MODE_PROPERTY_PREFIX + id + ".outputXslMediaType", String.class ).orElse( "text/xml" );
            String strStandalone = _config.getOptionalValue( MODE_PROPERTY_PREFIX + id + ".outputXslStandalone", String.class ).orElse( null );
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

    @Override
    public Mode load( int nId )
    {
        return _modes.get( nId );
    }

    @Override
    public ReferenceList findAllToReferenceList( )
    {
        ReferenceList modesList = new ReferenceList( );
        for ( Mode mode : _modes.values( ) )
        {
            modesList.addItem( mode.getId( ), mode.getDescription( ) );
        }
        return modesList;
    }

    @Override
    public Properties findOuputXslProperties( int nId )
    {
        Mode mode = load( nId );
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
