/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.business.stylesheet;

import java.util.Collection;

import fr.paris.lutece.data.dao.IGenericDAO;
import fr.paris.lutece.data.repository.DAORepository;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StyleSheetRepository extends DAORepository<StyleSheet, Integer> implements IStyleSheetRepository
{

    @Inject
    private IStyleSheetDAO _dao;

    @Override
    protected IGenericDAO<StyleSheet, Integer> getDAO( )
    {
        return _dao;
    }

    @Override
    public StyleSheet update( StyleSheet styleSheet )
    {
        super.update( styleSheet );
        XmlTransformerService.clearXslCache( );
        return styleSheet;
    }

    @Override
    public void remove( Integer paramID )
    {
        super.remove( paramID );
        XmlTransformerService.clearXslCache( );
    }

    @Override
    public Collection<StyleSheet> findByMode( int nModeId )
    {
        return _dao.selectStyleSheetList( nModeId );
    }

    @Override
    public int countPerStyleAndMode( int nStyleId, int nModeId )
    {
        return _dao.selectStyleSheetNbPerStyleMode( nStyleId, nModeId );
    }

}
