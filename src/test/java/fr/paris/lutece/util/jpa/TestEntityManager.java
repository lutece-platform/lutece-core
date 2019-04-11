/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

final class TestEntityManager implements EntityManager
{
    @Override
    public <T> T unwrap( Class<T> cls )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setProperty( String propertyName, Object value )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFlushMode( FlushModeType flushMode )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove( Object entity )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh( Object entity, LockModeType lockMode, Map<String, Object> properties )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh( Object entity, LockModeType lockMode )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh( Object entity, Map<String, Object> properties )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh( Object entity )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void persist( Object entity )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> T merge( T entity )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void lock( Object entity, LockModeType lockMode, Map<String, Object> properties )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void lock( Object entity, LockModeType lockMode )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void joinTransaction( )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isOpen( )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public EntityTransaction getTransaction( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getReference( Class<T> entityClass, Object primaryKey )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> getProperties( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Metamodel getMetamodel( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LockModeType getLockMode( Object entity )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FlushModeType getFlushMode( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getDelegate( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void flush( )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> T find( Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T find( Class<T> entityClass, Object primaryKey, LockModeType lockMode )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T find( Class<T> entityClass, Object primaryKey, Map<String, Object> properties )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T find( Class<T> entityClass, Object primaryKey )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void detach( Object entity )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> TypedQuery<T> createQuery( String qlString, Class<T> resultClass )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery( CriteriaQuery<T> criteriaQuery )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query createQuery( String qlString )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query createNativeQuery( String sqlString, String resultSetMapping )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query createNativeQuery( String sqlString, Class resultClass )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query createNativeQuery( String sqlString )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery( String name, Class<T> resultClass )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query createNamedQuery( String name )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean contains( Object entity )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void close( )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void clear( )
    {
        // TODO Auto-generated method stub

    }
}
