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
package fr.paris.lutece.util.jpa;

import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.annotation.AnnotationUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import org.apache.log4j.Logger;

import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

import java.io.File;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;


/**
 *
 * JPAPersistenceUnitPostProcessor : adds classpath entities to the PersistenceUnitInfo and finds *.orm.xml.
 */
public class JPAPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor
{
    private static final Logger _Log = Logger.getLogger( JPAConstants.JPA_LOGGER );
    private static final String PATH_CONF = "/WEB-INF/classes/";
    private static final String SUFFIX_ORM_XML = ".orm.xml";
    private static final String CLASSPATH_PATH_IDENTIFIER = "fr" + File.separator + "paris";

    /**
     * Scans for *.orm.xml and adds Entites from classpath.
     *
     * @param pui the pui
     */
    @Override
    public void postProcessPersistenceUnitInfo( MutablePersistenceUnitInfo pui )
    {
        _Log.info( "Scanning for JPA orm.xml files" );

        for ( File ormFile : getListORMFiles(  ) )
        {
            String ormAbsolutePath = ormFile.getAbsolutePath(  );
            _Log.info( "Found ORM file : " + ormAbsolutePath );
            pui.addMappingFileName( ormAbsolutePath.substring( ormAbsolutePath.indexOf( CLASSPATH_PATH_IDENTIFIER ) ) );
        }

        _Log.info( "Scanning for JPA entities..." );

        Set<String> entityClasses = AnnotationUtil.find( Entity.class.getName(  ) );
        entityClasses.addAll( AnnotationUtil.find( Embeddable.class.getName(  ) ) );
        entityClasses.addAll( AnnotationUtil.find( MappedSuperclass.class.getName(  ) ) );

        for ( String strClass : entityClasses )
        {
            _Log.info( "Found entity class : " + strClass );

            if ( !pui.getManagedClassNames(  ).contains( strClass ) )
            {
                pui.addManagedClassName( strClass );
            }
        }

        if ( _Log.isDebugEnabled(  ) )
        {
            dumpPersistenceUnitInfo( pui );
        }
    }

    /**
     * Search for <code>WEB-INF/conf/plugins/*.orm.xml</code>.
     * @return list of files found
     */
    private Collection<File> getListORMFiles(  )
    {
        String strConfPath = AppPathService.getAbsolutePathFromRelativePath( PATH_CONF );
        File dirConfPlugins = new File( strConfPath );

        return FileUtils.listFiles( dirConfPlugins, FileFilterUtils.suffixFileFilter( SUFFIX_ORM_XML ),
            TrueFileFilter.INSTANCE );
    }

    /**
     * Show PUI infos
     * @param pui PersistenceUnitInfo
     */
    private void dumpPersistenceUnitInfo( MutablePersistenceUnitInfo pui )
    {
        _Log.debug( "Dumping content for PersistenceUnitInfo of " + pui.getPersistenceUnitName(  ) );

        _Log.debug( "** getTransactionType : " + pui.getTransactionType(  ) );
        _Log.debug( "** getPersistenceProviderClassName : " + pui.getPersistenceProviderClassName(  ) );
        _Log.debug( "** getPersistenceProviderPackageName : " + pui.getPersistenceProviderPackageName(  ) );
        _Log.debug( "** getPersistenceUnitName : " + pui.getPersistenceUnitName(  ) );
        _Log.debug( "** getPersistenceXMLSchemaVersion : " + pui.getPersistenceXMLSchemaVersion(  ) );
        _Log.debug( "** getJtaDataSource : " + pui.getJtaDataSource(  ) );
        _Log.debug( "** getManagedClassNames : " + pui.getManagedClassNames(  ) );
        _Log.debug( "** getMappingFileNames : " + pui.getMappingFileNames(  ) );
        _Log.debug( "** getNonJtaDataSource : " + pui.getNonJtaDataSource(  ) );
        _Log.debug( "** getPersistenceUnitRootUrl :" + pui.getPersistenceUnitRootUrl(  ) );
        _Log.debug( "** getProperties : " + pui.getProperties(  ) );
    }
}
