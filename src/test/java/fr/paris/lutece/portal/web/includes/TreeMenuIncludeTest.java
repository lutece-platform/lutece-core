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
package fr.paris.lutece.portal.web.includes;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.service.portal.PortalMenuService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;

/**
 * The tree menu lists the child pages of the current page
 */
public class TreeMenuIncludeTest extends LuteceTestCase
{
    private Page createPage( int nParentPageId, String strName )
    {
        Page page = new Page( );
        page.setParentPageId( nParentPageId );
        page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
        page.setName( strName );
        page.setDescription( strName );
        page.setMetaKeywords( "" );
        page.setMetaDescription( "" );
        page.setNodeStatus( 1 );
        page.setDateUpdate( new Timestamp( System.currentTimeMillis( ) ) );
        PageHome.create( page );
        return page;
    }

    @Test
    public void testChildPagesAreListed( )
    {
        Page parent = createPage( PortalService.getRootPageId( ), "junit tree parent" );
        Page child = createPage( parent.getId( ), "junit tree child" );
        try
        {
            String strHtml = new TreeMenuInclude( ).buildTreeMenuContentTemplate( parent.getId( ), PortalMenuService.MODE_ADMIN,
                    new MockHttpServletRequest( ) );

            assertTrue( strHtml.contains( "node-" + child.getId( ) ) );
            assertTrue( strHtml.contains( "junit tree child" ) );
            assertTrue( strHtml.contains( "#node-" + parent.getId( ) ) );
        }
        finally
        {
            PageHome.remove( child.getId( ) );
            PageHome.remove( parent.getId( ) );
        }
    }
}
