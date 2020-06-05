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

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

final class TestEntityManagerFactory implements EntityManagerFactory
{
    @Override
    public boolean isOpen( )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Map<String, Object> getProperties( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil( )
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
    public CriteriaBuilder getCriteriaBuilder( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cache getCache( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntityManager createEntityManager( Map map )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntityManager createEntityManager( )
    {
        return new TestEntityManager( );
    }

    @Override
    public void close( )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public EntityManager createEntityManager( SynchronizationType st )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntityManager createEntityManager( SynchronizationType st, Map map )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addNamedQuery( String string, Query query )
    {
        // TODO Auto-generated method stub
    }

    @Override
    public <T> T unwrap( Class<T> type )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> void addNamedEntityGraph( String string, EntityGraph<T> eg )
    {
        // TODO Auto-generated method stub
    }
}
