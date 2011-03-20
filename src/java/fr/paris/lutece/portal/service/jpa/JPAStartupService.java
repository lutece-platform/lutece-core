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
package fr.paris.lutece.portal.service.jpa;

import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.init.StartUpService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.jpa.JPAConstants;
import fr.paris.lutece.util.jpa.JPAPersistenceUnitPostProcessor;
import fr.paris.lutece.util.jpa.transaction.ChainedTransactionManager;

import org.apache.log4j.Logger;

import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import javax.sql.DataSource;


/**
 * JPAStartupService
 */
public class JPAStartupService implements StartUpService
{
    private static Logger _log = Logger.getLogger( JPAConstants.JPA_LOGGER );

    /**
     * Initialize JPA objects (Datasource, Persistence Unit Manager, Entity Manager Factory,
     * Transaction Manager) for each pool.
     */
    public void process(  )
    {
        ReferenceList list = new ReferenceList(  );
        AppConnectionService.getPoolList( list );

        HashMap<String, EntityManagerFactory> mapFactories = new HashMap(  );
        List<PlatformTransactionManager> listTransactionManagers = new ArrayList<PlatformTransactionManager>(  );
        _log.info( "JPA Startup Service : Initializing JPA objects ..." );

        for ( ReferenceItem poolItem : list )
        {
            String strPoolname = poolItem.getCode(  );

            DataSource ds = AppConnectionService.getPoolManager(  ).getDataSource( strPoolname );
            _log.info( "JPA Startup Service : DataSource retrieved for pool : " + strPoolname );
            _log.debug( "> DS : " + ds.toString(  ) );

            DefaultPersistenceUnitManager pum = new DefaultPersistenceUnitManager(  );
            pum.setDefaultDataSource( ds );

            PersistenceUnitPostProcessor[] postProcessors = { new JPAPersistenceUnitPostProcessor(  ) };
            pum.setPersistenceUnitPostProcessors( postProcessors );

            pum.afterPropertiesSet(  );

            _log.info( "JPA Startup Service : Persistence Unit Manager for pool : " + strPoolname );
            _log.debug( "> PUM : " + pum.toString(  ) );

            LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean(  );
            lcemfb.setDataSource( ds );
            lcemfb.setPersistenceUnitManager( pum );
            lcemfb.setPersistenceUnitName( "jpaLuteceUnit" );

            JpaDialect jpaDialect = (JpaDialect) SpringContextService.getBean( "jpaDialect" );
            lcemfb.setJpaDialect( jpaDialect );

            Map mapJpaProperties = (Map) SpringContextService.getBean( "jpaPropertiesMap" );
            lcemfb.setJpaPropertyMap( mapJpaProperties );

            JpaVendorAdapter jpaVendorAdapter = (JpaVendorAdapter) SpringContextService.getBean( "jpaVendorAdapter" );
            lcemfb.setJpaVendorAdapter( jpaVendorAdapter );

            lcemfb.afterPropertiesSet(  );

            EntityManagerFactory emf = lcemfb.getNativeEntityManagerFactory(  );
            _log.info( "JPA Startup Service : EntityManagerFactory created for pool : " + strPoolname );
            _log.debug( "> EMF : " + emf.toString(  ) );

            JpaTransactionManager tm = new JpaTransactionManager(  );
            tm.setEntityManagerFactory( emf );
            tm.afterPropertiesSet(  );
            _log.info( "JPA Startup Service : JPA TransactionManager created for pool : " + strPoolname );
            _log.debug( "> TM : " + tm.toString(  ) );

            mapFactories.put( strPoolname, emf );
            listTransactionManagers.add( tm );
        }

        EntityManagerService ems = (EntityManagerService) SpringContextService.getBean( "entityManagerService" );
        ems.setMapFactories( mapFactories );

        ChainedTransactionManager ctm = (ChainedTransactionManager) SpringContextService.getBean( "transactionManager" );
        ctm.setTransactionManagers( listTransactionManagers );
        _log.info( "JPA Startup Service : completed successfully" );
    }

    /**
     * {@inheritDoc }
     * @return
     */
    public String getName(  )
    {
        return ( "JPA Startup Service" );
    }
}
