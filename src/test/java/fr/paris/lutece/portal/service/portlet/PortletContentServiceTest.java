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
package fr.paris.lutece.portal.service.portlet;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Tests of {@link PortletContentService}
 */
public class PortletContentServiceTest extends LuteceTestCase
{
    private static final String CONTENT = "junitportletcontent";

    @Inject
    private PortletContentService _service;

    private static final class TestPortlet extends Portlet
    {
        @Override
        public void remove( )
        {
        }

        @Override
        public boolean isContentGeneratedByXmlAndXsl( )
        {
            return false;
        }

        @Override
        public String getHtmlContent( HttpServletRequest request )
        {
            return CONTENT;
        }

        @Override
        public String getXml( HttpServletRequest request )
        {
            return null;
        }

        @Override
        public String getXmlDocument( HttpServletRequest request )
        {
            return null;
        }
    }

    /**
     * An unpublished portlet is not part of the page rendered without request (the page content indexed by the search)
     */
    @Test
    public void testUnpublishedPortletWithoutRequest( ) throws Exception
    {
        TestPortlet portlet = new TestPortlet( );
        portlet.setId( -1 );
        portlet.setRole( Page.ROLE_NONE );
        portlet.setStatus( Portlet.STATUS_UNPUBLISHED );

        assertEquals( "", _service.getPortletContent( null, portlet, new HashMap<>( ), 0 ) );
    }
}
