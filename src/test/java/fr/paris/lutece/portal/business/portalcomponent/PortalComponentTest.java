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
package fr.paris.lutece.portal.business.portalcomponent;

import fr.paris.lutece.test.LuteceTestCase;


public class PortalComponentTest extends LuteceTestCase
{
    private static final int PORTAL_COMPONENT_ID = -99;
    private static final int TEST_PORTALCOMPONENT_ID = 0; // Portlets
    private final static int INTMODE = 0;
    private final static String STRNAME1 = "StrName 1";
    private final static String STRNAME2 = "StrName 2";

    public void testBusinessPortalComponent(  )
    {
        // Initialize an object
        PortalComponent portalComponent = new PortalComponent(  );
        portalComponent.setId( PORTAL_COMPONENT_ID );
        portalComponent.setName( STRNAME1 );

        // Create test
        PortalComponentHome.create( portalComponent );

        PortalComponent portalComponentStored = PortalComponentHome.findByPrimaryKey( portalComponent.getId(  ) );
        assertEquals( portalComponentStored.getName(  ), portalComponent.getName(  ) );

        // Update test
        portalComponent.setName( STRNAME2 );

        PortalComponentHome.update( portalComponent );
        portalComponentStored = PortalComponentHome.findByPrimaryKey( portalComponent.getId(  ) );
        assertEquals( portalComponentStored.getName(  ), portalComponent.getName(  ) );

        // Delete test
        PortalComponentHome.remove( portalComponent.getId(  ) );
        portalComponentStored = PortalComponentHome.findByPrimaryKey( portalComponent.getId(  ) );
        assertNull( portalComponentStored );

        // getXsl test
        PortalComponentHome.getXsl( TEST_PORTALCOMPONENT_ID, INTMODE );
    }
}
