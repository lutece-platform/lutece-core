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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * PortletTemplateHome Test Class
 */
public class PortletTemplateHomeTest extends LuteceTestCase
{
    private static final String PORTLET_TYPE = "PORTLET_TEMPLATE_TEST";
    private static final String OTHER_PORTLET_TYPE = "PORTLET_TEMPLATE_TEST_OTHER";
    private static final String DESCRIPTION = "Test template";
    private static final String DESCRIPTION_MODIFIED = "Test template modified";
    private static final String TEMPLATE_PATH = "skin/plugins/test/portlet_test.html";
    private static final String TEMPLATE_PATH_MODIFIED = "skin/plugins/test/portlet_test_modified.html";
    private static final String DEFAULT_TEMPLATE_PATH = "skin/plugins/test/portlet_default.html";

    @Test
    public void testCrud( )
    {
        PortletTemplate template = new PortletTemplate( );
        template.setPortletTypeId( PORTLET_TYPE );
        template.setDescription( DESCRIPTION );
        template.setTemplatePath( TEMPLATE_PATH );
        PortletTemplateHome.create( template );

        try
        {
            assertTrue( template.getId( ) > 0, "the generated identifier should be set" );

            PortletTemplate stored = PortletTemplateHome.findByPrimaryKey( template.getId( ) );
            assertNotNull( stored );
            assertEquals( PORTLET_TYPE, stored.getPortletTypeId( ) );
            assertEquals( DESCRIPTION, stored.getDescription( ) );
            assertEquals( TEMPLATE_PATH, stored.getTemplatePath( ) );
            assertEquals( PortletTemplate.RESOURCE_TYPE, stored.getResourceTypeCode( ) );
            assertEquals( String.valueOf( template.getId( ) ), stored.getResourceId( ) );
            assertFalse( PortletTemplateHome.isTemplateUsed( template.getId( ) ), "a new template is not used by any portlet" );

            assertEquals( 1, PortletTemplateHome.findByPortletType( PORTLET_TYPE ).size( ) );
            assertEquals( 1, PortletTemplateHome.findReferenceList( PORTLET_TYPE ).size( ) );
            assertEquals( String.valueOf( template.getId( ) ), PortletTemplateHome.findReferenceList( PORTLET_TYPE ).get( 0 ).getCode( ) );
            assertTrue( PortletTemplateHome.findByPortletType( OTHER_PORTLET_TYPE ).isEmpty( ), "a template is limited to its portlet type" );
            assertTrue( PortletTemplateHome.findAll( ).stream( ).anyMatch( t -> t.getId( ) == template.getId( ) ) );

            stored.setDescription( DESCRIPTION_MODIFIED );
            stored.setTemplatePath( TEMPLATE_PATH_MODIFIED );
            stored.setPortletTypeId( OTHER_PORTLET_TYPE );
            PortletTemplateHome.update( stored );

            PortletTemplate updated = PortletTemplateHome.findByPrimaryKey( template.getId( ) );
            assertEquals( OTHER_PORTLET_TYPE, updated.getPortletTypeId( ) );
            assertEquals( DESCRIPTION_MODIFIED, updated.getDescription( ) );
            assertEquals( TEMPLATE_PATH_MODIFIED, updated.getTemplatePath( ) );
        }
        finally
        {
            PortletTemplateHome.remove( template.getId( ) );
        }

        assertNull( PortletTemplateHome.findByPrimaryKey( template.getId( ) ), "the template should be deleted" );
    }

    @Test
    public void testGetTemplatePath( )
    {
        PortletTemplate template = new PortletTemplate( );
        template.setPortletTypeId( PORTLET_TYPE );
        template.setDescription( DESCRIPTION );
        template.setTemplatePath( TEMPLATE_PATH );
        PortletTemplateHome.create( template );

        try
        {
            PortletImpl portlet = new PortletImpl( );
            portlet.setPortletTypeId( PORTLET_TYPE );

            portlet.setIdTemplate( PortletTemplateHome.NO_TEMPLATE_ID );
            assertEquals( DEFAULT_TEMPLATE_PATH, PortletTemplateHome.getTemplatePath( portlet, DEFAULT_TEMPLATE_PATH ), "no template chosen : default" );

            portlet.setIdTemplate( template.getId( ) );
            assertEquals( TEMPLATE_PATH, PortletTemplateHome.getTemplatePath( portlet, DEFAULT_TEMPLATE_PATH ), "the chosen template" );

            portlet.setPortletTypeId( OTHER_PORTLET_TYPE );
            assertEquals( DEFAULT_TEMPLATE_PATH, PortletTemplateHome.getTemplatePath( portlet, DEFAULT_TEMPLATE_PATH ),
                    "a template of another portlet type is ignored" );

            portlet.setPortletTypeId( PORTLET_TYPE );
            portlet.setIdTemplate( Integer.MAX_VALUE );
            assertEquals( DEFAULT_TEMPLATE_PATH, PortletTemplateHome.getTemplatePath( portlet, DEFAULT_TEMPLATE_PATH ), "an unknown template : default" );
        }
        finally
        {
            PortletTemplateHome.remove( template.getId( ) );
        }
    }
}
