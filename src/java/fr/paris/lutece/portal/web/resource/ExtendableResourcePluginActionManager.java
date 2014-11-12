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
package fr.paris.lutece.portal.web.resource;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * This manager is used to fill the model with the actions
 * that interact with extend.
 * <br />
 * It uses the method fillModel of the manager {@link PluginActionManager}.
 *
 */
public final class ExtendableResourcePluginActionManager
{
    // MARKS
    public static final String MARK_ID_EXTENDABLE_RESOURCE = "idExtendableResource";
    public static final String MARK_EXTENDABLE_RESOURCE_TYPE = "extendableResourceType";
    private static final String MARK_EXTENDABLE_RESOURCE_ACTIONS_HTML = "extendableResourceActionsHtml";
    private static final String MARK_LIST_EXTENDABLE_RESOURCE_ACTIONS = "listExtendableResourceActions";

    // TEMPLATES
    private static final String TEMPLATE_EXTENDABLE_RESOURCE_ACTION = "admin/resource/extendable_resource_actions.html";

    /**
     * Instantiates a new extendable resource plugin action manager.
     */
    private ExtendableResourcePluginActionManager(  )
    {
    }

    /**
     * Fill the model with all actions and adds the list to the given marker.
     * It also add the mapParameter to the model.
     *
     * @param request the request
     * @param adminUser the admin user
     * @param model the model
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     */
    public static void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model,
        String strIdExtendableResource, String strExtendableResourceType )
    {
        Map<String, Object> modelTmp = new HashMap<String, Object>(  );
        modelTmp.put( MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        modelTmp.put( MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
        PluginActionManager.fillModel( request, adminUser, modelTmp, IExtendableResourcePluginAction.class,
            MARK_LIST_EXTENDABLE_RESOURCE_ACTIONS );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EXTENDABLE_RESOURCE_ACTION,
                request.getLocale(  ), modelTmp );

        model.put( MARK_EXTENDABLE_RESOURCE_ACTIONS_HTML, template.getHtml(  ) );
    }
}
