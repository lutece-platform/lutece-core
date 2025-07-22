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
package fr.paris.lutece.portal.web.style;

import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import fr.paris.lutece.portal.business.style.IPageTemplateRepository;
import fr.paris.lutece.portal.business.style.PageTemplate;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class provides the user interface to manage page templates features ( manage, create, modify, remove)
 */
@RequestScoped
@Named
public class PageTemplatesJspBean extends AdminFeaturesPageJspBean
{
    // Right
    /**
     * Right to manage page templates
     */
    public static final String RIGHT_MANAGE_PAGE_TEMPLATES = "CORE_PAGE_TEMPLATE_MANAGEMENT";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -142214702397662732L;

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_PAGE_TEMPLATE_LIST = "portal.style.manage_page_templates.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PAGE_TEMPLATE = "portal.style.modify_page_template.pageTitle";

    // Markers
    private static final String MARK_PAGE_TEMPLATES_LIST = "page_templates_list";
    private static final String MARK_PAGE_TEMPLATE = "page_template";

    // Templates files path
    private static final String TEMPLATE_PAGE_TEMPLATES = "admin/style/manage_page_templates.html";
    private static final String TEMPLATE_MODIFY_PAGE_TEMPLATE = "admin/style/modify_page_template.html";

    // Messages
    private static final String MESSAGE_CONFIRM_DELETE_PAGE_TEMPLATE = "portal.style.message.pageTemplateConfirmDelete";
    private static final String MESSAGE_PAGE_TEMPLATE_IS_USED = "portal.style.message.pageTemplateIsUsed";

    // JSP
    private static final String JSP_DO_REMOVE_PAGE_TEMPLATE = "jsp/admin/style/DoRemovePageTemplate.jsp";

    @Inject
    private IPageTemplateRepository _repository;

    /**
     * Returns the list of page templates
     *
     * @param request
     *            The Http request
     * @return the html code for display the page templates list
     */
    public String getManagePageTemplate( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_PAGE_TEMPLATE_LIST );

        HashMap<String, Object> model = new HashMap<>( );
        model.put( MARK_PAGE_TEMPLATES_LIST, _repository.findAll( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_TEMPLATES, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Returns the page template form of update
     *
     * @param request
     *            The Http request
     * @return the html code of the page template form
     */
    public String getModifyPageTemplate( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_PAGE_TEMPLATE );

        String strId = request.getParameter( Parameters.PAGE_TEMPLATE_ID );

        HashMap<String, Object> model = new HashMap<>( );
        model.put( MARK_PAGE_TEMPLATE, _repository.load( Integer.parseInt( strId ) ).get( ) );
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, TEMPLATE_MODIFY_PAGE_TEMPLATE ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PAGE_TEMPLATE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Processes the updating form of a page template whose new parameters are stored in the http request
     *
     * @param request
     *            The http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doModifyPageTemplate( HttpServletRequest request ) throws AccessDeniedException
    {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        String strId = multipartRequest.getParameter( Parameters.PAGE_TEMPLATE_ID );
        String strDescription = multipartRequest.getParameter( Parameters.PAGE_TEMPLATE_DESCRIPTION );

        if ( !getSecurityTokenService( ).validate( multipartRequest, TEMPLATE_MODIFY_PAGE_TEMPLATE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        PageTemplate pageTemplate = _repository.load( Integer.parseInt( strId ) ).get( );
        pageTemplate.setDescription( strDescription );
        _repository.update( pageTemplate );

        // If the process is successful, redirects towards the page template management
        // page
        return getHomeUrl( request );
    }

    /**
     * Returns the confirm of removing the page_template whose identifier is in the http request
     * 
     * @param request
     *            The Http request
     * @return the html code for the remove confirmation page
     */
    public String getConfirmRemovePageTemplate( HttpServletRequest request )
    {
        String strId = request.getParameter( Parameters.PAGE_TEMPLATE_ID );
        int nId = Integer.parseInt( strId );

        boolean bIsUsed = _repository.isUsedByPage( nId );

        if ( !bIsUsed )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_PAGE_TEMPLATE_IS_USED, AdminMessage.TYPE_STOP );
        }

        Map<String, Object> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, strId );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, getSecurityTokenService( ).getToken( request, JSP_DO_REMOVE_PAGE_TEMPLATE ) );
        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_PAGE_TEMPLATE, new Object [ ] {}, null, JSP_DO_REMOVE_PAGE_TEMPLATE, null,
                AdminMessage.TYPE_CONFIRMATION, parameters );
    }

    /**
     * Processes the deletion of a page template
     * 
     * @param request
     *            the http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doRemovePageTemplate( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !getSecurityTokenService( ).validate( request, JSP_DO_REMOVE_PAGE_TEMPLATE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        String strId = request.getParameter( Parameters.PAGE_TEMPLATE_ID );
        int nId = Integer.parseInt( strId );

        _repository.remove( nId );

        return getHomeUrl( request );
    }
}
