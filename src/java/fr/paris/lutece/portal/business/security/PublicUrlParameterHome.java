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
package fr.paris.lutece.portal.business.security;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;


/**
 * This class provides instances management methods (update, find, ...) for public url parameters
 */
public final class PublicUrlParameterHome
{
    //Static variable pointed at the DAO instance
    private static IPublicUrlParameterDAO _dao = (IPublicUrlParameterDAO) SpringContextService.getBean( 
            "publicUrlParameterDAO" );

    /**
     * Instantiates a new security service parameter home.
     */
    private PublicUrlParameterHome(  )
    {
    }

    /**
     * Loads a  public url  parameter
     * @param strParameterKey the parameter key
     * @return the found parameter
     */
    public static ReferenceItem findByKey( String strParameterKey )
    {
        return _dao.load( strParameterKey );
    }

    /**
     * Updates a public url parameter
     * @param param the parameter to update
     */
    public static void update( ReferenceItem param )
    {
        _dao.store( param );
    }

    /**
     * Add  a new  public url parameter
     * @param param the parameter to update
     */
    public static void create( ReferenceItem param )
    {
        _dao.insert( param );
    }

    /**
     * Loads all public url parameter link with the parameter key
     * @param strParameterKey the parameter key
     * @return List of public url parameter link with the parameter key
     */
    public static ReferenceList findAllByKey( String strParameterKey )
    {
        return _dao.findAllByKey( strParameterKey );
    }

    /**
     *  Loads all public url parameter
     *  @return all public url parameter
     */
    public static ReferenceList findAll(  )
    {
        return _dao.findAll(  );
    }

    /**
     * Remove a security parameter by key
     * @param strParameterKey the parameter key
     */
    public static void remove( String strParameterKey )
    {
        _dao.delete( strParameterKey );
    }
}
