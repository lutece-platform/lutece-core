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
package fr.paris.lutece.portal.service.daemon;

import org.junit.Test;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.test.LuteceTestCase;

public class AppDaemonServiceTest extends LuteceTestCase
{
    private static final String JUNIT_DAEMON = "JUNIT";
    private static final String DAEMON_INTERVAL_DSKEY = "core.daemon." + JUNIT_DAEMON + ".interval";

    private DaemonEntry _entry;

    @Override
    protected void setUp( )
    {
        try
        {
            super.setUp( );
        }
        catch (Exception e)
        {
            throw new AppException( e.getMessage( ), e );
        }
        _entry = new DaemonEntry( );
        _entry.setId( JUNIT_DAEMON );
        _entry.setNameKey( JUNIT_DAEMON );
        _entry.setDescriptionKey( JUNIT_DAEMON );
        _entry.setClassName( TestDaemon.class.getName( ) );
        _entry.setPluginName( "core" );
        _entry.setOnStartUp( true );
        
        DatastoreService.setInstanceDataValue( DAEMON_INTERVAL_DSKEY, "1" );
        try
        {
            AppDaemonService.registerDaemon( _entry );
        }
        catch (LuteceInitException e)
        {
            throw new AppException( e.getMessage( ), e );
        }
        AppDaemonService.startDaemon( JUNIT_DAEMON );
    }

    @Override
    protected void tearDown( ) 
    {
        DatastoreService.removeInstanceData( DAEMON_INTERVAL_DSKEY );
        AppDaemonService.unregisterDaemon( JUNIT_DAEMON );
        
        try
        {
            super.tearDown( );
        }
        catch (Exception e)
        {
            throw new AppException( e.getMessage( ), e );
        }
    }

    @Test
    public void testRegisterDaemonTwiceIgnored( )
    {
        assertTrue( AppDaemonService.getDaemonEntries( ).contains( _entry ) );
        DaemonEntry duplicate = new DaemonEntry( );
        duplicate.setId( JUNIT_DAEMON + "duplicate" );
        duplicate.setNameKey( JUNIT_DAEMON + "duplicate" );
        duplicate.setDescriptionKey( JUNIT_DAEMON + "duplicate" );
        duplicate.setClassName( TestDaemon.class.getName( ) );
        duplicate.setPluginName( "core" );
        DatastoreService.setInstanceDataValue( DAEMON_INTERVAL_DSKEY, "1" );
        

        try
        {
            AppDaemonService.registerDaemon( duplicate );
        }
        catch (LuteceInitException e)
        {
            throw new AppException( e.getMessage( ), e );
        }
        AppDaemonService.startDaemon( JUNIT_DAEMON + "duplicate" );

        assertTrue( AppDaemonService.getDaemonEntries( ).contains( duplicate ) );
        assertTrue( AppDaemonService.getDaemonEntries( ).contains( _entry ) );
        DaemonEntry registeredEntry = AppDaemonService.getDaemonEntries( ).stream( ).filter( entry -> JUNIT_DAEMON.equals( entry.getId( ) ) ).findFirst( )
                .orElseThrow( AssertionError::new );
        assertEquals( JUNIT_DAEMON, registeredEntry.getDescriptionKey( ) );
    }
}
