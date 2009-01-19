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
package fr.paris.lutece.portal.business.style;

import fr.paris.lutece.test.LuteceTestCase;


public class StyleTest extends LuteceTestCase
{
    private static final int STYLEID = -99;
    private final static int PORTALCOMPONENTID1 = 1;
    private final static int PORTALCOMPONENTID2 = 2;
    private final static int TEST_PORTAL_COMPONENT_ID = 0; // portlet
    private final static String PORTLETTYPEID1 = "PortletTypeId 1";
    private final static String PORTLETTYPEID2 = "PortletTypeId 2";
    private final static String DESCRIPTION1 = "Description 1";
    private final static String DESCRIPTION2 = "Description 2";
    private final static String PORTLETTYPENAME1 = "PortletTypeName 1";
    private final static String PORTALCOMPONENTNAME1 = "PortalComponentName 1";

    public void testBusinessStyle(  )
    {
        // Initialize an object
        Style style = new Style(  );
        style.setId( STYLEID );
        style.setPortalComponentId( PORTALCOMPONENTID1 );
        style.setPortletTypeId( PORTLETTYPEID1 );
        style.setDescription( DESCRIPTION1 );
        style.setPortletTypeName( PORTLETTYPENAME1 );
        style.setPortalComponentName( PORTALCOMPONENTNAME1 );

        // Create test
        StyleHome.create( style );

        Style styleStored = StyleHome.findByPrimaryKey( style.getId(  ) );
        assertEquals( styleStored.getPortalComponentId(  ), style.getPortalComponentId(  ) );
        assertEquals( styleStored.getPortletTypeId(  ), style.getPortletTypeId(  ) );
        assertEquals( styleStored.getDescription(  ), style.getDescription(  ) );

        // Update test
        style.setPortalComponentId( PORTALCOMPONENTID2 );
        style.setPortletTypeId( PORTLETTYPEID2 );
        style.setDescription( DESCRIPTION2 );
        StyleHome.update( style );
        styleStored = StyleHome.findByPrimaryKey( style.getId(  ) );
        assertEquals( styleStored.getPortalComponentId(  ), style.getPortalComponentId(  ) );
        assertEquals( styleStored.getPortletTypeId(  ), style.getPortletTypeId(  ) );
        assertEquals( styleStored.getDescription(  ), style.getDescription(  ) );

        // List test
        StyleHome.getPortalComponentList(  );
        StyleHome.getStylesList(  );
        StyleHome.getStyleSheetList( style.getId(  ) );
        StyleHome.checkStylePortalComponent( TEST_PORTAL_COMPONENT_ID );

        // Delete test
        StyleHome.remove( style.getId(  ) );
        styleStored = StyleHome.findByPrimaryKey( style.getId(  ) );
        assertNull( styleStored );
    }
}
