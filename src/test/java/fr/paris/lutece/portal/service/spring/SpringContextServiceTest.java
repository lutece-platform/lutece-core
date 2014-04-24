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
package fr.paris.lutece.portal.service.spring;

import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminAuthentication;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.search.SearchEngine;
import fr.paris.lutece.test.LuteceTestCase;

import java.util.List;


/**
 * SpringContextService Test Class
 */
public class SpringContextServiceTest extends LuteceTestCase
{
    /**
     * Test of getBean method, of class fr.paris.lutece.portal.service.spring.SpringContextService.
     */
    public void testGetBean(  )
    {
        System.out.println( "getBean" );

        String strName = "adminAuthenticationModule";

        Object result = SpringContextService.getBean( strName );
        assertTrue( LuteceDefaultAdminAuthentication.class.isInstance( result ) );
    }

    public void testInit(  ) throws LuteceInitException
    {
        System.out.println( "init" );
        SpringContextService.init(  );

        for ( String name : SpringContextService.getContext(  ).getBeanDefinitionNames(  ) )
        {
            System.out.println( name );
        }
    }

    public void testGetBeanOfType(  )
    {
        System.out.println( "getBeanOfType" );

        List<SearchEngine> list = SpringContextService.getBeansOfType( SearchEngine.class );

        for ( SearchEngine engine : list )
        {
            System.out.println( engine.getClass(  ) );
        }
    }
}
