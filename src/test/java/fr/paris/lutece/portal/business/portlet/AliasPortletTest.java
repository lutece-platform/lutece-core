/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.portal.business.portlet;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;

/**
 * AliasPortletTest Test Class
 *
 */
public class AliasPortletTest extends LuteceTestCase
{
    private static final int TEST_PORTLET_ID = 1; // portlet
    private static final int TEST_PORTLET_ALIAS_ID = 2; // portlet alias
    private static final String TEST_PORTLET_TYPE_ID = "HTML_PORTLET"; // html portlet

    @Test
    public void testBusinessAliasPortlet( )
    {
        // Initialize an object
        AliasPortlet aliasPortlet = new AliasPortlet( );

        aliasPortlet.setAliasId( TEST_PORTLET_ALIAS_ID );
        aliasPortlet.setId( TEST_PORTLET_ID );
        aliasPortlet.setAcceptAlias( 1 );

        aliasPortlet.update( );
        aliasPortlet.remove( );

        AliasPortletHome instance = (AliasPortletHome) AliasPortletHome.getInstance( );
        instance.getPortletTypeId( );

        AliasPortletHome.getAliasId( TEST_PORTLET_ID );
        AliasPortletHome.getAcceptAliasPortletRefList( );
        AliasPortletHome.getAcceptAliasPortletList( );
        AliasPortletHome.getPortletsByTypeList( TEST_PORTLET_TYPE_ID );
    }

    /**
     * An alias of itself, or of a missing portlet, shows nothing instead of failing
     */
    @Test
    public void testAliasLoopAndMissingTarget( ) throws SiteMessageException
    {
        AliasPortlet alias = new AliasPortlet( );
        alias.setName( "junit alias" );
        alias.setPageId( PortalService.getRootPageId( ) );
        alias.setColumn( 1 );
        alias.setOrder( 1 );
        AliasPortletHome.getInstance( ).create( alias );
        try
        {
            for ( int nTargetId : new int [ ] {
                    alias.getId( ), 999999
            } )
            {
                alias.setAliasId( nTargetId );
                AliasPortletHome.getInstance( ).update( alias );

                Portlet portlet = PortletHome.findByPrimaryKey( alias.getId( ) );
                assertFalse( portlet.isContentGeneratedByXmlAndXsl( ) );
                assertEquals( "", portlet.getHtmlContent( new MockHttpServletRequest( ) ) );
                assertTrue( portlet.getXml( new MockHttpServletRequest( ) ).contains( "junit alias" ) );
                assertTrue( portlet.getXslParams( ).isEmpty( ) );
            }
        }
        finally
        {
            AliasPortletHome.getInstance( ).remove( alias );
        }
    }
}
