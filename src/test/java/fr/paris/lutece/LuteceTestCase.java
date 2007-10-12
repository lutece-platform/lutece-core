/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece;

import fr.paris.lutece.portal.service.init.AppInit;
import fr.paris.lutece.portal.service.util.AppPathService;

import junit.framework.*;


/**
 * This class is the base class for Lutece test case.
 * It provides all services initialization.
 */
public class LuteceTestCase extends TestCase
{
    protected static boolean _bInit = false;
    private String _strResourcesDir;
    
    public LuteceTestCase()
    {
        super();
        _strResourcesDir = getClass().getResource("/").toString().replaceFirst("file:/", "").replaceFirst("test-classes/", "lutece/");

    }

    public LuteceTestCase( String strTestName )
    {
        super( strTestName );
        _strResourcesDir = getClass().getResource("/").toString().replaceFirst("file:/", "").replaceFirst("test-classes/", "lutece/");

    }
    
    public String getResourcesDir ( )
    {
    	return _strResourcesDir;
    }
    
    public void setResourcesDir ( String strResourcesDir )
    {
    	_strResourcesDir = strResourcesDir;
    }
    
    protected void setUp(  ) throws Exception
    {
        super.setUp(  );

        
        // Initializes Lutece services
        if ( !_bInit )
        {
        	System.out.println("-------------resourcesDir------------"+_strResourcesDir);
            AppPathService.init( _strResourcesDir );
            AppInit.initServices( "/WEB-INF/conf/" );
            
            _bInit = true;
            System.out.println( "Lutece services initialized");
            
        }
        System.out.println( this.getName());
    }

    protected void tearDown(  ) throws Exception
    {
        super.tearDown(  );
    }
}
