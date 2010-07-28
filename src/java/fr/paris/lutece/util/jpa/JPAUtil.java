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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import fr.paris.lutece.portal.service.jpa.EntityManagerService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * 
 * JPAUtil provides new primary key generation.
 * 
 */
public final class JPAUtil
{
	public static final String DEFAULT_PORTAL_PERSISTENCE_UNIT_NAME = "portal";
	
	/**
	 * 
	 * JPAUtil
	 * 
	 */
	private JPAUtil(  )
	{
		// nothing
	}
	
	/**
	 * Returns a new entity manager
	 * @return an entity manager
	 */
	private static EntityManager getNewEntityManager(  )
	{
		EntityManagerService ems = ( EntityManagerService ) SpringContextService.getBean( "entityManagerService" );
		EntityManagerFactory emf = ems.getEntityManagerFactory( DEFAULT_PORTAL_PERSISTENCE_UNIT_NAME );
		
		// no transaction needed. Actually, transaction will fail with 2 rows with same id
		return emf.createEntityManager(  );
	}

	/**
	 * Generates the new primary key value for the entity
	 * @param entityClass the entity class
	 * @return the new primary key, <code>null</code> if entityClass is not managed by the Primary Key Table, 
	 * or if an error occured.
	 */
	public static synchronized Integer newPrimaryKey( Class<?> entityClass )
	{
		// TODO synchronize on entityClass ?
		EntityManager em = getNewEntityManager(  );
		
		// find the pk value for the given class
		PrimaryKey pk = em.find( PrimaryKey.class, entityClass.getName(  ) );
		
		if ( pk == null )
		{
			AppLogService.error( "No primary key found for " + entityClass.getName(  ) );
			
			return null;
		}
		
		// get new primary key
		Integer nNewPrimaryKey = pk.getValue(  ) + 1;
		pk.setValue( nNewPrimaryKey );
		
		// update table
		EntityTransaction tx = null;
		try
		{
			tx = em.getTransaction(  );
			tx.begin(  );
			em.merge( pk );
			tx.commit(  );
		}
		finally
		{
			// rollback if transaction is in progress (i.e. not committed)
			if ( tx != null && tx.isActive(  ) )
			{
				AppLogService.error( "Rollback transaction for " + pk );
				tx.rollback(  );
				
				// the new value cannot be safely returned (might not be saved)
				nNewPrimaryKey = null;
			}

			// close entity manage
			if ( em.isOpen(  ) )
			{
				em.close(  );
			}
		}
		
		return nNewPrimaryKey;
	}
}
