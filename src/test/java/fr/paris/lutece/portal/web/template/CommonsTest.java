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
package fr.paris.lutece.portal.web.template;

import fr.paris.lutece.portal.business.template.CommonsInclude;
import fr.paris.lutece.portal.service.template.CommonsService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * CommonsTest
 */
public class CommonsTest extends LuteceTestCase
{
    private static final String RESSOURCES_PATH = "commons/templates/";
    private static final String TEMPLATES_FOLDER = "/" + RESSOURCES_PATH;

    private static final String MARK_TEMPLATE = "template";
    private static final String MARK_MOCK_OBJECT = "mockObject";
    private static final String MARK_FOREIGN_KEYS_LIST = "id_foreigns_list";
    private static final String[] CHARTERS_FOLDERS =
    { "css", "fonts", "js" };

    @Test
    public void testCommonsTemplates( ) throws IOException, TemplateException
    {
        try
        {
            String strPath = getClass( ).getResource( TEMPLATES_FOLDER ).getPath( );
            File fileTemplatesFolder = new File( strPath );

            copyCommonsFiles( strPath );

            String strOutput = strPath + "/output";
            File fileOutputFolder = new File( strOutput );
            fileOutputFolder.mkdir( );

            for ( CommonsInclude ci : CommonsService.getCommonsIncludes( ) )
            {
                CommonsService.activateCommons( ci.getKey( ) );
                String strCommonsOutput = strOutput + "/" + ci.getKey( );
                File fileOutputCommonsFolder = new File( strCommonsOutput );
                fileOutputCommonsFolder.mkdir( );

                copyChartersFolders( strPath, strCommonsOutput );

                FreemarkerTemplateService templateService = new FreemarkerTemplateService( strPath );
                templateService.init( "/" );
                for ( String strAutoIncludes : ci.getFiles( ) )
                {
                    templateService.addAutoInclude( "commons/" + strAutoIncludes );
                }

                for ( File file : fileTemplatesFolder.listFiles( ) )
                {
                    if ( !file.isDirectory( ) )
                    {
                        System.out.println( file.getName( ) );
                        Map<String, Object> model = new HashMap<>( );
                        MockObject mockObject = new MockObject( );

                        model.put( MARK_TEMPLATE, file.getName( ) );
                        model.put( MARK_MOCK_OBJECT, mockObject );
                        model.put( MARK_FOREIGN_KEYS_LIST, getForeignKeysList( ) );

                        templateService.write( file.getName( ), fileOutputCommonsFolder.getPath( ), model );

                    }
                }

            }
        }
        catch ( Exception e )
        {
            fail( );
        }
    }

    private ReferenceList getForeignKeysList( )
    {
        ReferenceList listForeignKeys = new ReferenceList( );
        listForeignKeys.addItem( 1, "Item 1" );
        listForeignKeys.addItem( 2, "Item 2" );
        return listForeignKeys;

    }

    private String getSourcePath( String strPath, String strFolder )
    {
        int nPos = strPath.indexOf( "target" );
        return strPath.substring( 0, nPos ) + "target/lutece/" + strFolder;
    }

    private void copyChartersFolders( String strRootPath, String strCommonsOutput ) throws IOException
    {
        for ( String strFolder : CHARTERS_FOLDERS )
        {
            String strSourcePath = getSourcePath( strRootPath, strFolder );
            File fileSourceFolder = new File( strSourcePath );

            String strCommonsFolder = strCommonsOutput + "/" + strFolder;
            File fileOutputCommonsFolder = new File( strCommonsFolder );
            fileOutputCommonsFolder.mkdir( );
            FileUtils.copyDirectory( fileSourceFolder, fileOutputCommonsFolder );
        }
    }

    private void copyCommonsFiles( String strRootPath ) throws IOException
    {
        String strSourcePath = getSourcePath( strRootPath, "WEB-INF/templates" );
        File fileSourceFolder = new File( strSourcePath );
        String strDestPath = strRootPath + "/commons";
        File fileCommonsFolder = new File( strDestPath );
        fileCommonsFolder.mkdir( );
        FileUtils.copyDirectory( fileSourceFolder, fileCommonsFolder, new CommonsFileFilter( ) );
    }

    class CommonsFileFilter implements FileFilter
    {

        @Override
        public boolean accept( File file )
        {
            if ( file.isFile( ) && file.getName( ).startsWith( "commons" ) )
            {
                return true;
            }
            return false;
        }

    }

}
