/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import org.apache.log4j.Logger;

import org.springframework.dao.DataAccessResourceFailureException;

import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.ParameterizedType;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;


/**
 * Class JPAGenericDAO
 * @param <K> Type of the entity's key
 * @param <E> Type of the entity
 */
public abstract class JPAGenericDAO<K, E> implements IGenericDAO<K, E>
{
    private static final Logger _log = Logger.getLogger( JPAConstants.JPA_LOGGER );
    private Class<E> _entityClass;

    /**
     * Constructor
     */
    @SuppressWarnings( "unchecked" )
    public JPAGenericDAO(  )
    {
        _entityClass = (Class<E>) ( (ParameterizedType) getClass(  ).getGenericSuperclass(  ) ).getActualTypeArguments(  )[1];
    }

    /**
     * Inherit classes should provide their Entity Manager Factory
     * @return The Entity Manager Factory that will create Entity Manager for this DAO
     */
    public abstract EntityManagerFactory getEntityManagerFactory(  );

    /**
     * Returns the entity class name
     * @return The entity class name
     */
    public String getEntityClassName(  )
    {
        return _entityClass.getName(  );
    }

    /**
     * Gets the entity class
     * @return the entity class
     */
    public Class<E> getEntityClass(  )
    {
        return _entityClass;
    }

    /**
     * Return the Entity Manager
     * @return The Entity Manager
     */
    public EntityManager getEM(  )
    {
        EntityManagerFactory emf = getEntityManagerFactory(  );

        if ( TransactionSynchronizationManager.isSynchronizationActive(  ) )
        {
            // first, get Spring entitymanager (if available)
            try
            {
                EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager( emf );

                if ( em == null )
                {
                    _log.error( 
                        "getEM(  ) : no EntityManager found. Will use native entity manager factory [Transaction will not be supported]" );
                }
                else
                {
                    _log.debug( "EntityManager found for the current transaction : " + em.toString(  ) +
                        " - using Factory : " + emf.toString(  ) );

                    return em;
                }
            }
            catch ( DataAccessResourceFailureException ex )
            {
                _log.error( ex );
            }
        }

        _log.error( 
            "getEM(  ) : no EntityManager found. Will use native entity manager factory [Transaction will not be supported]" );

        return emf.createEntityManager(  );
    }

    /**
     * {@inheritDoc }
     */
    public void create( E entity )
    {
        _log.debug( "Creating entity : " + entity.toString(  ) );

        EntityManager em = getEM(  );
        em.persist( entity );
        _log.debug( "Entity created : " + entity.toString(  ) );
    }

    /**
     * {@inheritDoc }
     */
    public void remove( K key )
    {
        EntityManager em = getEM(  );
        E entity = em.find( _entityClass, key );
        _log.debug( "Removing entity : " + entity.toString(  ) );
        em.remove( entity );
        _log.debug( "Entity removed : " + entity.toString(  ) );
    }

    /**
     * {@inheritDoc }
     */
    public void update( E entity )
    {
        _log.debug( "Updating entity : " + entity.toString(  ) );

        EntityManager em = getEM(  );
        em.merge( entity );
        _log.debug( "Entity Updated : " + entity.toString(  ) );
    }

    /**
     * {@inheritDoc }
     */
    public E findById( K key )
    {
        _log.debug( "Selecting entity " + getEntityClassName(  ) + " by ID : " + key.toString(  ) );

        return (E) getEM(  ).find( _entityClass, key );
    }

    /**
     * {@inheritDoc }
     */
    public List<E> findAll(  )
    {
        _log.debug( "Selecting all entities of type : " + getEntityClassName(  ) );

        Query query = getEM(  ).createQuery( "SELECT e FROM " + getEntityClassName(  ) + " e " );

        return query.getResultList(  );
    }

    /**
     *
     *{@inheritDoc}
     */
    public void flush(  )
    {
        getEM(  ).flush(  );
    }

    /**
     *
     *{@inheritDoc}
     */
    public void detach( E entity )
    {
        getEM(  ).detach( entity );
    }
}
