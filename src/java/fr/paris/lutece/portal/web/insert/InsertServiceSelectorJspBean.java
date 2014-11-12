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
package fr.paris.lutece.portal.web.insert;

import fr.paris.lutece.portal.service.html.EncodingService;
import fr.paris.lutece.portal.service.insert.InsertResourceIdService;
import fr.paris.lutece.portal.service.insert.InsertService;
import fr.paris.lutece.portal.service.insert.InsertServiceManager;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Provides the generic interface to choose a particular link service
 */
public class InsertServiceSelectorJspBean extends AdminFeaturesPageJspBean
{
    /**
     * Right to manage this feature
     */
    public static final String RIGHT_MANAGE_LINK_SERVICE = "CORE_LINK_SERVICE_MANAGEMENT";

    /**
     * Session key to set the content to insert
     */
    public static final String SESSION_INSERT = "portal.session.insertServiceSelector.contentToInsert";
    private static final long serialVersionUID = 3395846045509139922L;

    // Constants
    private static final String TEMPLATE_INSERT_TYPE_PAGE = "admin/insert/page_insertservice.html";
    private static final String TEMPLATE_INSERT_INTO_ELEMENT = "admin/insert/insert_into_element.html";
    private static final String TEMPLATE_INSERT_INTO_ELEMENT2 = "admin/insert/insert_into_element2.html";
    private static final String MSG_NO_SERVICE_AVAILABLE = "portal.insert.message.noServiceAvailable";

    /** name of the links type parameter */
    private static final String PARAMETER_INSERT_SERVICE_TYPE = "insert_service_type";
    private static final String PARAMETER_MODE = "mode";
    private static final String PARAMETER_INPUT = "input";
    private static final String PARAMETER_INSERT = "insert";
    private static final String PARAMETER_SELECTED_TEXT = "selected_text";
    private static final String MARK_INSERT_SERVICES_LIST = "insert_services_list";
    private static final String MARK_SELECTED_TEXT = "selected_text";
    private static final String MARK_INPUT = "input";
    private static final String MARK_INSERT = "insert";

    /**
     * build the insert service selection page
     *
     * @param request HTTP request
     * @return HTML code of the page as String
     */
    public String getServicesListPage( HttpServletRequest request )
    {
        String strInput = request.getParameter( PARAMETER_INPUT );
        String strText = request.getParameter( PARAMETER_SELECTED_TEXT );

        // Encode the HTML code to insert
        strText = EncodingService.encodeUrl( strText );

        Collection<InsertService> listServices = InsertServiceManager.getInsertServicesList(  );

        // building from a template
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_INSERT_SERVICES_LIST,
            RBACService.getAuthorizedCollection( listServices, InsertResourceIdService.PERMISSION_USE, getUser(  ) ) );
        model.put( MARK_SELECTED_TEXT, strText );
        model.put( MARK_INPUT, strInput );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_INSERT_TYPE_PAGE, getLocale(  ), model );

        return t.getHtml(  );
    }

    /**
     * Call the selected service selection UI
     *
     * @param request HTTP request
     * @return the HTML code of the selection page for selected service
     */
    public String displayService( HttpServletRequest request )
    {
        String strInsertService = request.getParameter( PARAMETER_INSERT_SERVICE_TYPE );

        if ( ( strInsertService != null ) && !strInsertService.equals( "" ) )
        {
            InsertService is = InsertServiceManager.getInsertService( strInsertService );

            return is.getSelectorUI( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MSG_NO_SERVICE_AVAILABLE, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * Build the JSP that provides the HTML code insertion into the Editor via a javascript call
     *
     * @param request HTTP request
     * @return the HTML code of the page that insert code into the editor
     */
    public String doInsertIntoElement( HttpServletRequest request )
    {
        String strMode = request.getParameter( PARAMETER_MODE );
        String strInput = request.getParameter( PARAMETER_INPUT );
        String strInsert = (String) request.getSession(  ).getAttribute( SESSION_INSERT );

        if ( strInsert == null )
        {
            strInsert = request.getParameter( PARAMETER_INSERT );
        }

        request.getSession(  ).removeAttribute( SESSION_INSERT );

        Map<String, String> model = new HashMap<String, String>(  );
        model.put( MARK_INPUT, strInput );
        model.put( MARK_INSERT, strInsert );

        HtmlTemplate template;

        if ( strMode.compareTo( "2" ) == 0 )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_INSERT_INTO_ELEMENT2, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( TEMPLATE_INSERT_INTO_ELEMENT, getLocale(  ), model );
        }

        return template.getHtml(  );
    }
}
