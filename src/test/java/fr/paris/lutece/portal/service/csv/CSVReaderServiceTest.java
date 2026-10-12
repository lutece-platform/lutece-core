/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.service.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Tests of {@link CSVReaderService#readCSVFile(PhysicalFile, int, boolean, boolean, boolean, Locale, String)}
 */
public class CSVReaderServiceTest extends LuteceTestCase
{
    private static final byte [ ] CSV_CONTENT = "a;b\nc;d\n".getBytes( StandardCharsets.UTF_8 );

    private static class RecordingCSVReaderService extends CSVReaderService
    {
        private final List<String> _listReadLines = new ArrayList<>( );

        @Override
        protected List<CSVMessageDescriptor> readLineOfCSVFile( String [ ] strLineDataArray, int nLineNumber, Locale locale, String strBaseUrl )
        {
            _listReadLines.add( String.join( ",", strLineDataArray ) );
            return new ArrayList<>( );
        }

        @Override
        protected List<CSVMessageDescriptor> checkLineOfCSVFile( String [ ] strLineDataArray, int nLineNumber, Locale locale )
        {
            return new ArrayList<>( );
        }

        @Override
        protected List<CSVMessageDescriptor> getEndOfProcessMessages( int nNbLineParses, int nNbLinesWithoutErrors, Locale locale )
        {
            return new ArrayList<>( );
        }
    }

    private List<CSVMessageDescriptor> read( RecordingCSVReaderService service, PhysicalFile physicalFile )
    {
        return service.readCSVFile( physicalFile, 2, false, false, false, Locale.FRENCH, "" );
    }

    private void assertNoFileFound( List<CSVMessageDescriptor> listMessages )
    {
        assertEquals( 1, listMessages.size( ) );
        assertEquals( CSVMessageLevel.ERROR, listMessages.get( 0 ).getMessageLevel( ) );
    }

    @Test
    void testReadNullPhysicalFile( )
    {
        RecordingCSVReaderService service = new RecordingCSVReaderService( );

        assertNoFileFound( read( service, null ) );
        assertTrue( service._listReadLines.isEmpty( ) );
    }

    @Test
    void testReadPhysicalFileWithContent( )
    {
        RecordingCSVReaderService service = new RecordingCSVReaderService( );
        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setValue( CSV_CONTENT );

        assertTrue( read( service, physicalFile ).isEmpty( ) );
        assertEquals( List.of( "a,b", "c,d" ), service._listReadLines );
    }

    @Test
    void testReadPhysicalFileLoadedFromDatabase( )
    {
        PhysicalFile storedFile = new PhysicalFile( );
        storedFile.setValue( CSV_CONTENT );
        int nId = PhysicalFileHome.create( storedFile );

        try
        {
            RecordingCSVReaderService service = new RecordingCSVReaderService( );
            PhysicalFile physicalFile = new PhysicalFile( );
            physicalFile.setIdPhysicalFile( nId );

            assertTrue( read( service, physicalFile ).isEmpty( ) );
            assertEquals( List.of( "a,b", "c,d" ), service._listReadLines );
        }
        finally
        {
            PhysicalFileHome.remove( nId );
        }
    }

    @Test
    void testReadUnknownPhysicalFile( )
    {
        RecordingCSVReaderService service = new RecordingCSVReaderService( );
        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setIdPhysicalFile( -1 );

        assertNoFileFound( read( service, physicalFile ) );
        assertTrue( service._listReadLines.isEmpty( ) );
    }
}
