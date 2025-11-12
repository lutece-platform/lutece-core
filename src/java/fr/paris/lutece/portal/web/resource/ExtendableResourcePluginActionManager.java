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
package fr.paris.lutece.portal.web.resource;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * This manager is used to fill the model with the actions that interact with extend. <br>
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
    private ExtendableResourcePluginActionManager( )
    {
    }

    /**
     * Fill the model with all actions and adds the list to the given marker. It also add the mapParameter to the model.
     *
     * @param request
     *            the request
     * @param adminUser
     *            the admin user
     * @param model
     *            the model
     * @param strIdExtendableResource
     *            the str id extendable resource
     * @param strExtendableResourceType
     *            the str extendable resource type
     */
    public static void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model, String strIdExtendableResource,
            String strExtendableResourceType )
    {
        fillModelInternal(request, adminUser, (Object) model, strIdExtendableResource, strExtendableResourceType);
    }
    /**
     * Fill the model with all actions and adds the list to the given marker. It also add the mapParameter to the models.
     *
     * @param request
     *            the request
     * @param adminUser
     *            the admin user
     * @param model
     *            the models object
     * @param strIdExtendableResource
     *            the str id extendable resource
     * @param strExtendableResourceType
     *            the str extendable resource type
     */
    public static void fillModel( HttpServletRequest request, AdminUser adminUser, Models model, String strIdExtendableResource,
            String strExtendableResourceType )
    {
        fillModelInternal(request, adminUser, (Object) model, strIdExtendableResource, strExtendableResourceType);
    }
    /**
     * Internal helper method used to populate the given model (either a {@link Map} or a {@link Models})
     * with the HTML corresponding to available extendable resource actions.
     * <p>
     * This method is shared by both {@code fillModel(..., Map, ...)} and {@code fillModel(..., Models, ...)}
     * to avoid code duplication.
     * </p>
     *
     * <p>
     * The method performs the following steps:
     * <ol>
     *   <li>Creates a temporary model containing the extendable resource identifiers.</li>
     *   <li>Delegates to {@link PluginActionManager#fillModel(HttpServletRequest, AdminUser, Map, Class, String)}
     *       to populate available plugin actions.</li>
     *   <li>Builds the HTML output using {@link AppTemplateService#getTemplate(String, java.util.Locale, Map)}.</li>
     *   <li>Injects the resulting HTML into the provided model under the key
     *       {@code MARK_EXTENDABLE_RESOURCE_ACTIONS_HTML}.</li>
     * </ol>
     * </p>
     *
     * @param request
     *            The current HTTP request (used for locale and plugin action context)
     * @param adminUser
     *            The current administrator user
     * @param model
     *            The target model to fill — may be a {@link Map} or a {@link Models} instance
     * @param strIdExtendableResource
     *            The identifier of the extendable resource
     * @param strExtendableResourceType
     *            The type of the extendable resource
     * @throws IllegalArgumentException
     *            If the provided {@code model} is not an instance of {@link Map} or {@link Models}
     */
    private static void fillModelInternal(HttpServletRequest request, AdminUser adminUser, Object model,
            String strIdExtendableResource, String strExtendableResourceType) {
		Map<String, Object> modelTmp = new HashMap<>();
		modelTmp.put(MARK_ID_EXTENDABLE_RESOURCE, strIdExtendableResource);
		modelTmp.put(MARK_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType);
		
		PluginActionManager.fillModel(request, adminUser, modelTmp,
		IExtendableResourcePluginAction.class, MARK_LIST_EXTENDABLE_RESOURCE_ACTIONS);
		
		HtmlTemplate template = AppTemplateService.getTemplate(TEMPLATE_EXTENDABLE_RESOURCE_ACTION,
		request.getLocale(), modelTmp);
		
		if (model instanceof Map) {
			((Map<String, Object>) model).put(MARK_EXTENDABLE_RESOURCE_ACTIONS_HTML, template.getHtml());
		} else if (model instanceof Models) {
			((Models) model).put(MARK_EXTENDABLE_RESOURCE_ACTIONS_HTML, template.getHtml());
		} else {
			throw new IllegalArgumentException("Unsupported model type: " + model.getClass());
		}
    }
}
