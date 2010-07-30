/*
 * Copyright (c) 2002-2010, Mairie de Paris
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

import java.util.Properties;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.annotation.AnnotationUtil;
import org.apache.log4j.Logger;

/**
 * 
 * JPAPersistenceUnitPostProcessor : adds classpath entities to the PersistenceUnitInfo
 */
public class JPAPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor
{
	private static final String CONSTANTE_DB_PROPERTIES_DRIVER = ".driver";
	private static final String CONSTANTE_DB_PROPERTIES_URL = ".url";
	private static final String CONSTANTE_DB_PROPERTIES_USER = ".user";
	private static final String CONSTANTE_DB_PROPERTIES_PASSWORD = ".password";
	
	private static final String CONSTANTE_PERSISTENCE_DRIVER = "hibernate.connection.driver_class";
	private static final String CONSTANTE_PERSISTENCE_URL = "hibernate.connection.url";
	private static final String CONSTANTE_PERSISTENCE_USER = "hibernate.connection.username";
	private static final String CONSTANTE_PERSISTENCE_PASSWORD = "hibernate.connection.password";

        private static Logger _log = Logger.getLogger( JPAConstants.JPA_LOGGER );
	
	/**
	 * 
	 *{@inheritDoc}
	 */
	public void postProcessPersistenceUnitInfo( MutablePersistenceUnitInfo pui )
	{
		String strPoolName = pui.getPersistenceUnitName(  );

		Properties props = pui.getProperties(  );
		props.put( CONSTANTE_PERSISTENCE_DRIVER,
					AppPropertiesService.getProperty( strPoolName + CONSTANTE_DB_PROPERTIES_DRIVER ) );
		
		props.put( CONSTANTE_PERSISTENCE_URL,
					AppPropertiesService.getProperty( strPoolName + CONSTANTE_DB_PROPERTIES_URL ) );

		props.put( CONSTANTE_PERSISTENCE_USER,
					AppPropertiesService.getProperty( strPoolName + CONSTANTE_DB_PROPERTIES_USER ) );
		
		props.put( CONSTANTE_PERSISTENCE_PASSWORD,
					AppPropertiesService.getProperty( strPoolName + CONSTANTE_DB_PROPERTIES_PASSWORD ) );

        Set<String> entityClasses = AnnotationUtil.find( Entity.class.getName(  ) );
        entityClasses.addAll( AnnotationUtil.find( Embeddable.class.getName(  ) ) );
        entityClasses.addAll( AnnotationUtil.find( MappedSuperclass.class.getName(  ) ) );

        for ( String strClass : entityClasses )
        {
        	_log.info( "Found entity class : " + strClass );
        	if ( !pui.getManagedClassNames(  ).contains( strClass ) )
        	{
        		pui.addManagedClassName( strClass );
        	}
        }
        
        if ( _log.isDebugEnabled(  ) )
        {
        	dumpPersistenceUnitInfo( pui );
        }
	}
	
	/**
	 * Show PUI infos
	 * @param pui PersistenceUnitInfo
	 */
	private void dumpPersistenceUnitInfo( MutablePersistenceUnitInfo pui )
	{
		_log.debug( "Dumping content for PersistenceUnitInfo of " + pui.getPersistenceUnitName(  ) );
		
		_log.debug( "** getTransactionType : " + pui.getTransactionType(  ) );
		_log.debug( "** getPersistenceProviderClassName : " + pui.getPersistenceProviderClassName(  ) );
		_log.debug( "** getPersistenceProviderPackageName : " + pui.getPersistenceProviderPackageName(  ) );
		_log.debug( "** getPersistenceUnitName : " + pui.getPersistenceUnitName(  ) );
		_log.debug( "** getPersistenceXMLSchemaVersion : " + pui.getPersistenceXMLSchemaVersion(  ) );
		_log.debug( "** getJtaDataSource : " + pui.getJtaDataSource(  ) );
		_log.debug( "** getManagedClassNames : " + pui.getManagedClassNames(  ) );
		_log.debug( "** getMappingFileNames : " + pui.getMappingFileNames(  ) );
		_log.debug( "** getNonJtaDataSource : " + pui.getNonJtaDataSource(  ) );
		_log.debug( "** getPersistenceUnitRootUrl :" + pui.getPersistenceUnitRootUrl(  ) );
		_log.debug( "** getProperties : " + pui.getProperties(  ) );
	}
}
