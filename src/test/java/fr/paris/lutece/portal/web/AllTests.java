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
package fr.paris.lutece.portal.web;

import fr.paris.lutece.portal.web.admin.AdminMapJspBeanTest;
import fr.paris.lutece.portal.web.admin.AdminMenuJspBeanTest;
import fr.paris.lutece.portal.web.admin.AdminMessageJspBeanTest;
import fr.paris.lutece.portal.web.features.FeaturesGroupJspBeanTest;
import fr.paris.lutece.portal.web.features.LevelsJspBeanTest;
import fr.paris.lutece.portal.web.insert.InsertServiceJspBeanTest;
import fr.paris.lutece.portal.web.insert.InsertServiceSelectorJspBeanTest;
import fr.paris.lutece.portal.web.rbac.RoleManagementJspBeanTest;
import fr.paris.lutece.portal.web.search.SearchAppTest;
import fr.paris.lutece.portal.web.search.SearchIndexationJspBeanTest;
import fr.paris.lutece.portal.web.style.ModesJspBeanTest;
import fr.paris.lutece.portal.web.style.PageTemplatesJspBeanTest;
import fr.paris.lutece.portal.web.style.StylesJspBeanTest;
import fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBeanTest;
import fr.paris.lutece.portal.web.system.PluginJspBeanTest;
import fr.paris.lutece.portal.web.system.SystemJspBeanTest;
import fr.paris.lutece.portal.web.xpages.SiteMapAppTest;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * This class is the main test suite for the package fr.paris.lutece.util.date
 */
public final class AllTests
{
    /**
     * A set of tests
     * @return Test the tests
     */
    public static Test suite(  )
    {
        TestSuite suite = new TestSuite( "*** Tests Lutece core " );

        //$JUnit-BEGIN$
        //        suite.addTest( new TestSuite( AdminPageJspBeanTest.class ) );
        suite.addTest( new TestSuite( AdminMapJspBeanTest.class ) );
        suite.addTest( new TestSuite( AdminMenuJspBeanTest.class ) );
        suite.addTest( new TestSuite( AdminMessageJspBeanTest.class ) );
        suite.addTest( new TestSuite( FeaturesGroupJspBeanTest.class ) );
        suite.addTest( new TestSuite( LevelsJspBeanTest.class ) );
        suite.addTest( new TestSuite( InsertServiceJspBeanTest.class ) );
        suite.addTest( new TestSuite( InsertServiceSelectorJspBeanTest.class ) );
        suite.addTest( new TestSuite( RoleManagementJspBeanTest.class ) );
        suite.addTest( new TestSuite( SearchAppTest.class ) );
        suite.addTest( new TestSuite( SearchIndexationJspBeanTest.class ) );
        suite.addTest( new TestSuite( ModesJspBeanTest.class ) );
        suite.addTest( new TestSuite( PageTemplatesJspBeanTest.class ) );
        suite.addTest( new TestSuite( StylesJspBeanTest.class ) );
        suite.addTest( new TestSuite( StyleSheetJspBeanTest.class ) );
        suite.addTest( new TestSuite( PluginJspBeanTest.class ) );
        suite.addTest( new TestSuite( SystemJspBeanTest.class ) );
        suite.addTest( new TestSuite( SiteMapAppTest.class ) );
        suite.addTest( new TestSuite( PortalJspBeanTest.class ) );
        suite.addTest( new TestSuite( StandaloneAppJspBeanTest.class ) );

        //$JUnit-END$
        return suite;
    }
}
