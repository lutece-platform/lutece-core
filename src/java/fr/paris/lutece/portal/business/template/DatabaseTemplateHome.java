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
package fr.paris.lutece.portal.business.template;

import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides methods to access templates stored in the database.
 */
public final class DatabaseTemplateHome
{
    // Static variable pointed to the DAO instance
    private static IDatabaseTemplateDAO _dao = SpringContextService.getBean( "databaseTemplateDAO" );

    /**
     * Instantiates a new database template home.
     */
    private DatabaseTemplateHome( )
    {
    }

    /**
     * Get a template in the database from its key
     * 
     * @param strKey
     *            The key of the template to get
     * @return The template loaded from the database
     */
    public static String getTemplateFromKey( String strKey )
    {
        return _dao.getTemplateFromKey( strKey );
    }

    /**
     * Update a template in the database
     * 
     * @param strKey
     *            The key of the template
     * @param strValue
     *            The new value of the template
     */
    public static void updateTemplate( String strKey, String strValue )
    {
        _dao.updateTemplate( strKey, strValue );
    }
}
