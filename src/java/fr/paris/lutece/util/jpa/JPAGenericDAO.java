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

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 * Class JPAGenericDAO
 * @param <K> Type of the entity's key
 * @param <E> Type of the entity
 */
public abstract class JPAGenericDAO<K, E> implements IGenericDAO<K, E>
{
    private Class<E> _entityClass;
    private EntityManagerFactory _emf;
    private EntityManager _em;
    private static final Logger _log = Logger.getLogger(JPAGenericDAO.class.getName());

    /**
     * Constructor
     */
    public JPAGenericDAO()
    {
        _entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * Inherit classes should provide their Entity Manager Factory
     * @return The Entity Manager Factory that will create Entity Manager for this DAO
     */
    public abstract EntityManagerFactory getEntityManagerFactory();

    /**
     * Returns the entity class name
     * @return The entity class name
     */
    public String getEntityClassName()
    {
        return _entityClass.getName();
    }


    /**
     * Return the Entity Manager
     * @return The Entity Manager
     */
    public EntityManager getEM()
    {
        if (_emf == null)
        {
            _emf = getEntityManagerFactory();
            _em = _emf.createEntityManager();
        }
        return _em;
    }

    /**
     * {@inheritDoc }
     */
    public void create(E entity)
    {
        _log.debug( "Creating entity : " + entity);
        getEM().persist(entity);
    }

    /**
     * {@inheritDoc }
     */
    public void remove(K key)
    {
        E entity = findById(key);
        _log.debug( "Removing entity : " + entity);
        getEM().remove(entity);
    }

    /**
     * {@inheritDoc }
     */
    public void update(E entity)
    {
       _log.debug( "Updating entity : " + entity);
       getEM().merge(entity);

    }

    /**
     * {@inheritDoc }
     */
    public E findById(K key)
    {
        return (E) getEM().find(_entityClass, key);
    }

    /**
     * {@inheritDoc }
     */
    public List<E> findAll()
    {
        Query query = getEM().createQuery("SELECT e FROM " + getEntityClassName() + " e ");
        return query.getResultList();
    }
}
