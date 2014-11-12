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
package fr.paris.lutece.portal.business.search;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.Map;


/**
 * This class provides instances management methods (update, find, ...) for search parameters
 */
public final class SearchParameterHome
{
    //Static variable pointed at the DAO instance
    private static ISearchParameterDAO _dao = (ISearchParameterDAO) SpringContextService.getBean( "searchParameterDAO" );

    /**
     * Instantiates a new search parameter home.
     */
    private SearchParameterHome(  )
    {
    }

    /**
     * Loads a search parameter
     * @param strParameterKey the parameter key
     * @return the found parameter
     */
    public static ReferenceItem findByKey( String strParameterKey )
    {
        return _dao.load( strParameterKey );
    }

    /**
     * Updates a search parameter
     * @param param the parameter to update
     */
    public static void update( ReferenceItem param )
    {
        _dao.store( param );
    }

    /**
     * Finds all search parameters
     * @return all the parameter as a Map
     */
    public static Map<String, String> findAll(  )
    {
        return _dao.findAll(  );
    }

    /**
     * Finds all search parameters
     * @return all the parameter as a ReferenceList
     */
    public static ReferenceList findParametersList(  )
    {
        return _dao.selectParametersList(  );
    }
}
