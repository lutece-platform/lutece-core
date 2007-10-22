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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.LuteceTestCase;

import java.util.Locale;


/**
 * PortletType Test Class
 */
public class PortletTypeTest extends LuteceTestCase
{
    private static final String ID = "PORTLET_TEST";
    private static final String NAMEKEY = "name.key";
    private static final String CREATION_URL = "Create.jsp";
    private static final String UPDATE_URL = "Update.jsp";
    private static final String PLUGIN_NAME = "plugin";
    private static final String HOME_CLASS = "HomePortletClass";
    private static final String DO_CREATE_URL = "DoCreate.jsp";
    private static final String CREATE_SCRIPT = "/templates/create_script.html";
    private static final String CREATE_SPECIFIC = "/templates/create_specific.html";
    private static final String CREATE_SPECIFIC_FORM = "/templates/create_specific_form.html";
    private static final String DO_MODIFY_URL = "DoModify.jsp";
    private static final String MODIFY_SCRIPT = "/templates/modify_script.html";
    private static final String MODIFY_SPECIFIC = "/templates/modify_specific.html";
    private static final String MODIFY_SPECIFIC_FORM = "/templates/modify_specific_form.html";

    public void testBusinessPortletType(  )
    {
        // Create Test
        PortletType pt = new PortletType(  );
        pt.setId( ID );
        pt.setNameKey( NAMEKEY );
        pt.setUrlCreation( CREATION_URL );
        pt.setUrlUpdate( UPDATE_URL );
        pt.setPluginName( PLUGIN_NAME );
        pt.setHomeClass( HOME_CLASS );
        pt.setDoCreateUrl( DO_CREATE_URL );
        pt.setCreateScriptTemplate( CREATE_SCRIPT );
        pt.setCreateSpecificTemplate( CREATE_SPECIFIC );
        pt.setCreateSpecificFormTemplate( CREATE_SPECIFIC_FORM );
        pt.setDoModifyUrl( DO_MODIFY_URL );
        pt.setModifyScriptTemplate( MODIFY_SCRIPT );
        pt.setModifySpecificTemplate( MODIFY_SPECIFIC );
        pt.setModifySpecificFormTemplate( MODIFY_SPECIFIC_FORM );

        PortletTypeHome.create( pt );

        // Update test
        PortletType ptStored = PortletTypeHome.findByPrimaryKey( ID );
        assertEquals( ptStored.getId(  ), pt.getId(  ) );
        assertEquals( ptStored.getNameKey(  ), pt.getNameKey(  ) );
        assertEquals( ptStored.getUrlCreation(  ), pt.getUrlCreation(  ) );
        assertEquals( ptStored.getUrlUpdate(  ), pt.getUrlUpdate(  ) );
        assertEquals( ptStored.getPluginName(  ), pt.getPluginName(  ) );
        assertEquals( ptStored.getHomeClass(  ), pt.getHomeClass(  ) );
        assertEquals( ptStored.getDoCreateUrl(  ), pt.getDoCreateUrl(  ) );
        assertEquals( ptStored.getCreateScriptTemplate(  ), pt.getCreateScriptTemplate(  ) );
        assertEquals( ptStored.getCreateSpecificTemplate(  ), pt.getCreateSpecificTemplate(  ) );
        assertEquals( ptStored.getCreateSpecificFormTemplate(  ), pt.getCreateSpecificFormTemplate(  ) );
        assertEquals( ptStored.getDoModifyUrl(  ), pt.getDoModifyUrl(  ) );
        assertEquals( ptStored.getModifyScriptTemplate(  ), pt.getModifyScriptTemplate(  ) );
        assertEquals( ptStored.getModifySpecificTemplate(  ), pt.getModifySpecificTemplate(  ) );
        assertEquals( ptStored.getModifySpecificFormTemplate(  ), pt.getModifySpecificFormTemplate(  ) );

        // List test
        PortletTypeHome.getPortletTypesList( Locale.getDefault(  ) );

        // Delete Test
        PortletTypeHome.remove( ID );
    }
}
