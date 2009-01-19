/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.portal.business;

import fr.paris.lutece.portal.business.features.FeaturesTest;
import fr.paris.lutece.portal.business.group.GroupRoleTest;
import fr.paris.lutece.portal.business.group.GroupTest;
import fr.paris.lutece.portal.business.portalcomponent.PortalComponentTest;
import fr.paris.lutece.portal.business.portlet.AliasPortletTest;
import fr.paris.lutece.portal.business.portlet.PortletTypeTest;
import fr.paris.lutece.portal.business.rbac.AdminRoleTest;
import fr.paris.lutece.portal.business.rbac.RBACTest;
import fr.paris.lutece.portal.business.right.FeatureGroupTest;
import fr.paris.lutece.portal.business.right.RightTest;
import fr.paris.lutece.portal.business.role.RoleTest;
import fr.paris.lutece.portal.business.style.ModeTest;
import fr.paris.lutece.portal.business.style.PageTemplateTest;
import fr.paris.lutece.portal.business.style.StyleTest;
import fr.paris.lutece.portal.business.stylesheet.StyleSheetTest;
import fr.paris.lutece.portal.business.user.AdminUserTest;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupTest;

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
        suite.addTest( new TestSuite( FeaturesTest.class ) );
        suite.addTest( new TestSuite( GroupTest.class ) );
        suite.addTest( new TestSuite( GroupRoleTest.class ) );
        suite.addTest( new TestSuite( PortalComponentTest.class ) );
        suite.addTest( new TestSuite( AliasPortletTest.class ) );
        suite.addTest( new TestSuite( PortletTypeTest.class ) );
        suite.addTest( new TestSuite( AdminRoleTest.class ) );
        suite.addTest( new TestSuite( RBACTest.class ) );
        suite.addTest( new TestSuite( FeatureGroupTest.class ) );
        suite.addTest( new TestSuite( RightTest.class ) );
        suite.addTest( new TestSuite( RoleTest.class ) );
        suite.addTest( new TestSuite( ModeTest.class ) );
        suite.addTest( new TestSuite( PageTemplateTest.class ) );
        suite.addTest( new TestSuite( StyleTest.class ) );
        suite.addTest( new TestSuite( StyleSheetTest.class ) );
        suite.addTest( new TestSuite( AdminUserTest.class ) );
        suite.addTest( new TestSuite( AdminWorkgroupTest.class ) );

        //$JUnit-END$
        return suite;
    }
}
