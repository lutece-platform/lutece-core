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

import java.util.List;

import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.test.LuteceTestCase;

public class TemporaryFileBusinessTest extends LuteceTestCase
{

    private AdminUser _user1;
    private AdminUser _user2;
    
    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _user1 = new AdminUser( );
        _user1.setUserId( 1 );
        
        _user2 = new AdminUser( );
        _user2.setUserId( 2 );
    }
    
    public void testCRUD( )
    {
        TemporaryFile file = new TemporaryFile( );
        file.setUser( _user1 );
        TemporaryFileHome.create( file );
        
        TemporaryFile loaded = TemporaryFileHome.findByPrimaryKey( file.getIdFile( ) );
        
        assertNotNull( loaded.getDateCreation( ) );
        
        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setValue( "hello".getBytes( ) );
        
        file.setTitle( "Title" );
        file.setPhysicalFile( physicalFile );
        
        TemporaryFileHome.update( file );
        
        loaded = TemporaryFileHome.findByPrimaryKey( file.getIdFile( ) );
        
        assertNotNull( loaded.getPhysicalFile( ) );
        assertEquals( "Title", loaded.getTitle( ) );
        
        TemporaryFileHome.remove( file.getIdFile( ) );
        
        loaded = TemporaryFileHome.findByPrimaryKey( file.getIdFile( ) );
        assertNull( loaded );
    }
    
    public void testFindByUser( )
    {
        TemporaryFile file1 = new TemporaryFile( );
        file1.setUser( _user1 );
        TemporaryFileHome.create( file1 );
        
        TemporaryFile file2 = new TemporaryFile( );
        file2.setUser( _user2 );
        TemporaryFileHome.create( file2 );
        
        List<TemporaryFile> files = TemporaryFileHome.findByUser( _user1 );
        assertEquals( 1, files.size( ) );
        assertEquals( _user1.getUserId( ), files.get( 0 ).getUser( ).getUserId( ) );
        
        TemporaryFileHome.remove( file1.getIdFile( ) );
        TemporaryFileHome.remove( file2.getIdFile( ) );
    }
    
    public void testDeleteFilesOlderThan( )
    {
        TemporaryFile file = new TemporaryFile( );
        file.setUser( _user1 );
        TemporaryFileHome.create( file );
        
        TemporaryFile loaded = TemporaryFileHome.findByPrimaryKey( file.getIdFile( ) );
        
        assertNotNull( loaded );
        
        TemporaryFileHome.deleteFilesOlderThan( -1 );
        
        loaded = TemporaryFileHome.findByPrimaryKey( file.getIdFile( ) );
        
        assertNull( loaded );
    }
}
