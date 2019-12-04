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
package fr.paris.lutece.portal.service.tempfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import fr.paris.lutece.portal.business.file.TemporaryFile;
import fr.paris.lutece.portal.business.file.TemporaryFileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.file.FileUtil;

/**
 * Service tha generates and saves Temporary Files.
 */
public class TemporaryFileGeneratorService
{
    private static final TemporaryFileGeneratorService INSTANCE = new TemporaryFileGeneratorService( );
    
    private static final Object LOCK = new Object( );
    
    public static TemporaryFileGeneratorService getInstance( )
    {
        return INSTANCE;
    }

    public void generateFile( IFileGenerator generator, AdminUser user )
    {
        CompletableFuture.runAsync( new GenerateFileRunnable( generator, user ) );
    }

    private static final class GenerateFileRunnable implements Runnable
    {

        private IFileGenerator _generator;
        private AdminUser _user;

        /**
         * Constructor.
         * 
         * @param _generator
         * @param _user
         */
        public GenerateFileRunnable( IFileGenerator generator, AdminUser user )
        {
            _generator = generator;
            _user = user;
        }

        @Override
        public void run( )
        {
            int idFile = initTemporaryFile( );
            synchronized ( LOCK )
            {
                Path generatedFile = null;
                try
                {
                    generatedFile = _generator.generateFile( );
                }
                catch ( IOException e )
                {
                    AppLogService.error( "Error generating temporary file with id " + idFile, e );
                }
                updateTemporaryFile( generatedFile, idFile );
            }
        }

        private int initTemporaryFile( )
        {
            TemporaryFile file = new TemporaryFile( );
            file.setUser( _user );
            file.setDescription( _generator.getDescription( ) );
            TemporaryFileHome.create( file );
            return file.getIdFile( );
        }

        private void updateTemporaryFile( Path generatedFile, int idFile )
        {
            TemporaryFile file = TemporaryFileHome.findByPrimaryKey( idFile );
            if ( generatedFile != null )
            {
                try
                {
                    PhysicalFile physicalFile = createPhysicalFile( generatedFile );
                    file.setSize( physicalFile.getValue( ).length );
                    file.setTitle( _generator.getFileName( ) );
                    file.setMimeType( _generator.getMimeType( ) );
                    file.setDescription( _generator.getDescription( ) );
                    file.setPhysicalFile( physicalFile );
                }
                catch ( IOException e )
                {
                    AppLogService.error( "Error storing temporary file with id " + idFile );
                    file.setSize( -1 );
                }
                finally
                {
                    FileUtil.deleteFile( generatedFile.toFile( ) );
                }
            }
            else
            {
                file.setSize( -1 );
            }

            TemporaryFileHome.update( file );
        }

        private PhysicalFile createPhysicalFile( Path generatedFile ) throws IOException
        {
            PhysicalFile physicalFile = new PhysicalFile( );
            if ( _generator.isZippable( ) )
            {
                Path zipFile = Paths.get( generatedFile.getParent( ).toString( ), _generator.getFileName( ) );
                try {
                    FileUtil.zipFiles( zipFile, generatedFile );
                    physicalFile.setValue( Files.readAllBytes( zipFile ) );
                }
                finally
                {
                    FileUtil.deleteFile( zipFile.toFile( ) );
                }
            }
            else
            {
                physicalFile.setValue( Files.readAllBytes( generatedFile ) );
            }
            return physicalFile;
        }
    }
}
