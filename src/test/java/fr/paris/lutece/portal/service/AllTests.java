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
package fr.paris.lutece.portal.service;

import fr.paris.lutece.portal.service.html.EncodingServiceTest;
import fr.paris.lutece.portal.service.html.HtmlCleanerServiceTest;
import fr.paris.lutece.portal.service.i18n.I18nServiceTest;
import fr.paris.lutece.portal.service.insert.InsertServiceManagerTest;
import fr.paris.lutece.portal.service.message.AdminMessageServiceTest;
import fr.paris.lutece.portal.service.plugin.PluginFileTest;
import fr.paris.lutece.portal.service.portal.PortalMenuServiceTest;
import fr.paris.lutece.portal.service.security.LuteceUserTest;
import fr.paris.lutece.portal.service.template.FreeMarkerTemplateServiceTest;
import fr.paris.lutece.portal.service.util.AppLogServiceTest;
import fr.paris.lutece.portal.service.util.AppPathServiceTest;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * This Class is the main Test Suite class to launch all the unit tests of the current package
 */
public final class AllTests
{
    /**
     * Constructor
     *
     */
    private AllTests(  )
    {
    }

    /**
     * A set of tests
     * @return Test the tests
     */
    public static Test suite(  )
    {
        TestSuite suite = new TestSuite( "Test for test.fr.paris.lutece.portal.service" );

        //$JUnit-BEGIN$
        suite.addTest( new TestSuite( HtmlCleanerServiceTest.class ) );
        suite.addTest( new TestSuite( EncodingServiceTest.class ) );
        suite.addTest( new TestSuite( I18nServiceTest.class ) );
        suite.addTest( new TestSuite( InsertServiceManagerTest.class ) );
        suite.addTest( new TestSuite( AdminMessageServiceTest.class ) );
        suite.addTest( new TestSuite( PluginFileTest.class ) );
        suite.addTest( new TestSuite( LuteceUserTest.class ) );
        suite.addTest( new TestSuite( FreeMarkerTemplateServiceTest.class ) );
        suite.addTest( new TestSuite( AppLogServiceTest.class ) );
        suite.addTest( new TestSuite( AppPathServiceTest.class ) );
        suite.addTest( new TestSuite( PortalServiceTest.class ) );
        suite.addTest( new TestSuite( PortalMenuServiceTest.class ) );
        suite.addTest( new TestSuite( PageIncludeServiceTest.class ) );
        suite.addTest( new TestSuite( SecurityServiceTest.class ) );

        //        suite.addTest( new TestSuite( ImageResourceTest.class ) );

        //$JUnit-END$
        return suite;
    }
}
