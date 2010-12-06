/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import fr.paris.lutece.util.jpa.IGenericDAO;
import fr.paris.lutece.util.jpa.IGenericHome;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public abstract class AbstractLuteceHome<K, E, DAO extends IGenericDAO<K, E>> implements IGenericHome<K, E>
{
    private DAO _dao;

    /**
     *{@inheritDoc}
     */
    public void setDao( DAO dao )
    {
        _dao = dao;
    }

    /**
     *{@inheritDoc}
     */
    public DAO getDao(  )
    {
        return _dao;
    }

    /**
     *{@inheritDoc}
     */
    @Transactional
    public void create( E entityBean )
    {
        getDao(  ).create( entityBean );
    }

    /**
     *{@inheritDoc}
     */
    @Transactional
    public void remove( K key )
    {
        getDao(  ).remove( key );
    }

    /**
     *{@inheritDoc}
     */
    public E findByPrimaryKey( K key )
    {
        return getDao(  ).findById( key );
    }

    /**
     *{@inheritDoc}
     */
    @Transactional
    public void update( E entityBean )
    {
        getDao(  ).update( entityBean );
    }

    /**
     *{@inheritDoc}
     */
    public List<E> findAll(  )
    {
        return getDao(  ).findAll(  );
    }
}
