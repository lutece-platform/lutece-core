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
    private static final Logger LOG = Logger.getLogger( JPAConstants.JPA_LOGGER );
    private Class<E> _entityClass;
    private EntityManager _defaultEM;

    /**
     * Constructor
     */
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
                    LOG.error( 
                        "getEM(  ) : no EntityManager found. Will use native entity manager factory [Transaction will not be supported]" );
                }
                else
                {
                    LOG.debug( "EntityManager found for the current transaction : " + em.toString(  ) +
                        " - using Factory : " + emf.toString(  ) );

                    return em;
                }
            }
            catch ( DataAccessResourceFailureException ex )
            {
                LOG.error( ex );
            }
        }

        LOG.error( 
            "getEM(  ) : no EntityManager found. Will use native entity manager factory [Transaction will not be supported]" );

        if( _defaultEM == null )
        {
            _defaultEM = emf.createEntityManager(  );
        }
        return _defaultEM;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void create( E entity )
    {
        LOG.debug( "Creating entity : " + entity.toString(  ) );

        EntityManager em = getEM(  );
        
        if( em == _defaultEM ) em.getTransaction().begin();
        em.persist( entity );
        if( em == _defaultEM ) em.getTransaction().commit();

        LOG.debug( "Entity created : " + entity.toString(  ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void remove( K key )
    {
        EntityManager em = getEM(  );
        E entity = em.find( _entityClass, key );
        LOG.debug( "Removing entity : " + entity.toString(  ) );
        if( em == _defaultEM ) em.getTransaction().begin();
        em.remove( entity );
        if( em == _defaultEM ) em.getTransaction().commit();
        LOG.debug( "Entity removed : " + entity.toString(  ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void update( E entity )
    {
        LOG.debug( "Updating entity : " + entity.toString(  ) );

        EntityManager em = getEM(  );
        if( em == _defaultEM ) em.getTransaction().begin();
        em.merge( entity );
        if( em == _defaultEM ) em.getTransaction().commit();
        LOG.debug( "Entity Updated : " + entity.toString(  ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public E findById( K key )
    {
        LOG.debug( "Selecting entity " + getEntityClassName(  ) + " by ID : " + key.toString(  ) );

        return (E) getEM(  ).find( _entityClass, key );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<E> findAll(  )
    {
        LOG.debug( "Selecting all entities of type : " + getEntityClassName(  ) );

        Query query = getEM(  ).createQuery( "SELECT e FROM " + getEntityClassName(  ) + " e " );

        return query.getResultList(  );
    }

    /**
     *
     *{@inheritDoc}
     */
    @Override
    public void flush(  )
    {
        getEM(  ).flush(  );
    }

    /**
     *
     *{@inheritDoc}
     */
    @Override
    public void detach( E entity )
    {
        getEM(  ).detach( entity );
    }
}
