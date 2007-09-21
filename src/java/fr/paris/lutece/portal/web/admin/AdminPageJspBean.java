/*
 * Copyright (c) 2002-2007, Mairie de Paris
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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.business.style.PageTemplate;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.page.PageResourceIdService;
import fr.paris.lutece.portal.service.page.PageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.portlet.PortletResourceIdService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.includes.ThemesInclude;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the admin interface to manage administration of site
 */
public class AdminPageJspBean extends AdminFeaturesPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    // Properties for page titles
    public final static String PROPERTY_PAGE_TITLE_ADMIN_SITE = "portal.site.admin_page.pageTitle";

    // Markers
    private final static String MARK_PAGE = "page";
    private final static String MARK_PAGE_INIT_ID = "page_init_id";
    private final static String MARK_PAGE_MESSAGE = "page_message";
    private final static String MARK_PORTLET_TYPES_LIST = "portlet_types_list";
    private final static String MARK_PAGE_ORDER_LIST = "page_order_list";
    private final static String MARK_PAGE_ROLES_LIST = "page_roles_list";
    private final static String MARK_PAGE_THEMES_LIST = "page_themes_list";
    private final static String MARK_IMAGE_URL = "image_url";
    private final static String MARK_PAGE_TEMPLATES_LIST = "page_templates_list";
    private final static String MARK_PAGE_TEMPLATE = "page_template";
    private final static String MARK_INDEX_ROW = "index_row";
    private final static String MARK_AUTORIZATION = "authorization";

    // Parameters
    private static final String PARAMETER_IMAGE_CONTENT = "image_content";
    private final static String PARAMETER_NODE_STATUS = "node_status";

    // Templates
    private static final String TEMPLATE_PAGE_TEMPLATE_ROW = "admin/site/page_template_list_row.html";
    private static final String TEMPLATE_ADMIN_PAGE = "admin/site/admin_page.html";

    // Properties definition
    private static final String PROPERTY_MESSAGE_PAGE_INEXISTENT = "portal.site.admin_page.messagePageInexistent";
    private static final String PROPERTY_MESSAGE_PAGE_FORMAT = "portal.site.admin_page.messagePageFormat";
    private static final String PROPERTY_MESSAGE_CONFIRM_REMOVE_PAGE = "portal.site.message.confirmRemovePage";
    private static final String PROPERTY_LIST_ORDER_MAX = "list.order.max";

    // Jsp
    private static final String JSP_ADMIN_SITE = "AdminSite.jsp";
    private static final String JSP_REMOVE_PAGE = "jsp/admin/site/DoRemovePage.jsp";

    //Messages
    private static final String MESSAGE_TITLE_INVALID_CHARACTERS = "portal.site.message.invalidCharactersInTitleName";
    private static final String MESSAGE_CANNOT_REMOVE_CHILDPAGE_EXISTS = "portal.site.message.cannotRemoveChildPageExists";
    private static final String MESSAGE_LENGTH_DESCRIPTION = "portal.site.message.pageLengthDescription";
    private static final String MESSAGE_ROOT_PAGE_FORBIDDEN = "portal.site.message.rootPageForbidden";
    private static final String MESSAGE_INVALID_PAGE_ID = "portal.site.message.pageIdInvalid";
    private static final String MESSAGE_PAGE_ID_CHILDPAGE = "portal.site.message.pageIdChildPage";
    private static final String MESSAGE_SAME_PAGE_ID = "portal.site.message.pageSameId";

    /**
     * Displays the page which contains the management forms of a skin page whose identifier is specified in parameter
     *
     * @param request The identifier of the page
     * @return The html code of the management forms of a page
     */
    public String getAdminPage( HttpServletRequest request )
    {
        String strPageId = request.getParameter( Parameters.PAGE_ID );

        if ( ( strPageId == null ) || strPageId.equals( "" ) )
        {
            strPageId = String.valueOf( PortalService.getRootPageId(  ) );
        }

        return getAdminPageBlock( strPageId );
    }

    /**
     * Returns the html code for displaying the page whose identifier is specified in parameter from the administration
     * unit. <br> That is useful to make a preview of the page.
     *
     * @param request The http request
     * @return The html code of the page
     * @throws UserNotSignedException The UserNotSignedException
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getAdminPagePreview( HttpServletRequest request )
        throws UserNotSignedException, SiteMessageException
    {
        ContentService cs = PortalService.getInvokedContentService( request );
        int nMode = 1;

        if ( cs != null )
        {
            return cs.getPage( request, nMode );
        }

        return PortalService.getDefaultPage( request, nMode );
    }

    /**
     * Processes of the modification of the page informations
     *
     * @param request The http request
     * @return The jsp url result of the process
     */
    public String doModifyPage( HttpServletRequest request )
    {
        int nPageId = Integer.parseInt( request.getParameter( Parameters.PAGE_ID ) );
        Page page = PageHome.getPage( nPageId );

        String strErrorUrl = getPageData( request, page );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        int nParentPageId = Integer.parseInt( request.getParameter( Parameters.PARENT_ID ) );

        if ( nParentPageId != page.getParentPageId(  ) )
        {
            strErrorUrl = getNewParentPageId( request, page, nParentPageId );

            if ( strErrorUrl != null )
            {
                return strErrorUrl;
            }
        }

        // Updates the page
        PageService.getInstance(  ).updatePage( page );

        // Displays again the current page with the modifications
        return getUrlPage( nPageId );
    }

    /**
     * Display the confirm page for the delete of a page
     *
     * @param request The http request
     * @return The html code of the confirm page
     */
    public String getRemovePage( HttpServletRequest request )
    {
        String strPageId = request.getParameter( Parameters.PAGE_ID );
        UrlItem url = new UrlItem( JSP_REMOVE_PAGE );
        url.addParameter( Parameters.PAGE_ID, strPageId );

        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_REMOVE_PAGE, url.getUrl(  ), "",
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Processes the deletion of a page
     *
     * @param request The http request
     * @return The jsp url result of the process
     */
    public String doRemovePage( HttpServletRequest request )
    {
        String strPageId = request.getParameter( Parameters.PAGE_ID );
        int nPageId = Integer.parseInt( strPageId );

        // Checks that the page has no child
        Collection list = PageHome.getChildPages( nPageId );

        if ( list.size(  ) > 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_REMOVE_CHILDPAGE_EXISTS,
                AdminMessage.TYPE_STOP );
        }

        int nParentPageId = PageHome.getPage( nPageId ).getParentPageId(  );

        // Delete the page
        PageService.getInstance(  ).removePage( nPageId );

        return getUrlPage( nParentPageId );
    }

    /**
     * Processes the creation of a child page to the page whose identifier is stored in the http request
     *
     * @param request The http request
     * @return The jsp url result of the process
     */
    public String doCreateChildPage( HttpServletRequest request )
    {
        String strParentPageId = request.getParameter( Parameters.PAGE_ID );
        int nParentPageId = Integer.parseInt( strParentPageId );

        Page page = new Page(  );
        page.setParentPageId( nParentPageId );

        String strErrorUrl = getPageData( request, page );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // Create the page
        PageService.getInstance(  ).createPage( page );

        // Displays again the current page with the modifications
        return getUrlPage( page.getId(  ) );
    }

    /**
     * Associate an image to the current page
     *
     * @param request The http request
     * @return The Jsp URL of the process result
     */
    public String doDefineImage( HttpServletRequest request )
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        String strPageId = mRequest.getParameter( Parameters.PAGE_ID );
        int nPageId = Integer.parseInt( strPageId );

        Page page = PageHome.getPage( nPageId );

        FileItem item = mRequest.getFile( PARAMETER_IMAGE_CONTENT );

        byte[] bytes = item.get(  );
        String strMimeType = item.getContentType(  );

        page.setImageContent( bytes );
        page.setMimeType( strMimeType );
        PageService.getInstance(  ).updatePage( page );

        // Displays again the current page
        return getUrlPage( nPageId );
    }

    /**
     * Management of the image associated to the page
     * @param page The Page Object
     * @param strPageId The page identifier
     */
    public String getResourceImagePage( Page page, String strPageId )
    {
        String strResourceType = PageService.getInstance(  ).getResourceTypeId(  );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, strPageId );

        return url.getUrlWithEntity(  );
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Private implementation

    /**
     * Displays the page which contains the management forms of a skin page whose identifier is specified in parameter
     *
     * @param strPageId The identifier of the page
     * @return The management page of a page
     */
    private String getAdminPageBlock( String strPageId )
    {
        HashMap model = new HashMap(  );

        Page page = null;
        int nPageId = 1;
        int nPageIdInit = 1;

        try
        {
            nPageId = Integer.parseInt( strPageId );
            nPageIdInit = nPageId;

            boolean bPageExist = PageHome.checkPageExist( nPageId );

            if ( bPageExist )
            {
                page = PageHome.getPage( nPageId );
                model.put( MARK_PAGE_MESSAGE, "" );
            }
            else
            {
                nPageId = PortalService.getRootPageId(  );
                page = PageHome.getPage( nPageId );
                model.put( MARK_PAGE_MESSAGE,
                    I18nService.getLocalizedString( PROPERTY_MESSAGE_PAGE_INEXISTENT, getLocale(  ) ) );
            }
        }
        catch ( NumberFormatException nfe )
        {
            nPageId = PortalService.getRootPageId(  );
            page = PageHome.getPage( nPageId );
            model.put( MARK_PAGE_MESSAGE, I18nService.getLocalizedString( PROPERTY_MESSAGE_PAGE_FORMAT, getLocale(  ) ) );
        }

        model.put( MARK_PAGE, page );
        model.put( MARK_PAGE_INIT_ID, Integer.toString( nPageIdInit ) );
        model.put( MARK_PAGE_ORDER_LIST, getOrdersList(  ) );
        model.put( MARK_PAGE_ROLES_LIST, RoleHome.getRolesList(  ) );
        model.put( MARK_PAGE_THEMES_LIST, ThemesInclude.getThemesList(  ) );
        model.put( MARK_IMAGE_URL, getResourceImagePage( page, strPageId ) );

        int nIndexRow = 1;
        StringBuffer strPageTemplatesRow = new StringBuffer(  );

        // Scan of the list
        for ( PageTemplate pageTemplate : PageTemplateHome.getPageTemplatesList(  ) )
        {
            strPageTemplatesRow.append( getTemplatesPageList( pageTemplate.getId(  ), page.getPageTemplateId(  ),
                    Integer.toString( nIndexRow ) ) );
            nIndexRow++;
        }

        model.put( MARK_PAGE_TEMPLATES_LIST, strPageTemplatesRow );
        model.put( MARK_PORTLET_TYPES_LIST, getPortletTypeList( getUser(  ) ) );

        // Add in v2.0
        String strAuthorizedPageId = Page.getAuthorizedPageId( page, nPageId );

        int nManageAuthorization = 0;

        if ( !RBACService.isAuthorized( Page.RESOURCE_TYPE, strAuthorizedPageId,
                    PageResourceIdService.PERMISSION_MANAGE, getUser(  ) ) )
        {
            nManageAuthorization = 1;
        }

        model.put( MARK_AUTORIZATION, Integer.toString( nManageAuthorization ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_PAGE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Provide page data
     * @param request The HttpServletRequest
     * @param page the Page Object
     * @return strErrorUrl
     */
    private String getPageData( HttpServletRequest request, Page page )
    {
        String strErrorUrl = null;

        String strPageId = request.getParameter( Parameters.PAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        String strName = request.getParameter( Parameters.PAGE_NAME );
        String strDescription = request.getParameter( Parameters.PAGE_DESCRIPTION );
        String strTemplatePageId = request.getParameter( Parameters.PAGE_TEMPLATE_ID );
        int nTemplatePageId = Integer.parseInt( strTemplatePageId );
        String strOrder = request.getParameter( Parameters.ORDER );
        String strRole = request.getParameter( Parameters.ROLE );
        String strTheme = request.getParameter( Parameters.THEME );

        /* Added in v2.0 */
        String strNodeStatus = request.getParameter( PARAMETER_NODE_STATUS );
        int nNodeStatus = Integer.parseInt( strNodeStatus );

        // Checks the description length (150 car. maximum)
        if ( strDescription.length(  ) > 150 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_LENGTH_DESCRIPTION, AdminMessage.TYPE_STOP );
        }

        // Checks if the mandatory field page name is found in the request
        if ( strName.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Checks if the page name contains HTML special characters
        else if ( StringUtil.containsHtmlSpecialCharacters( strName ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_INVALID_CHARACTERS, AdminMessage.TYPE_STOP );
        }
        else
        {
            page.setName( strName );
        }

        int nOrder = ( strOrder != null ) ? Integer.parseInt( strOrder ) : PageHome.getNewChildPageOrder( nPageId );

        page.setPageTemplateId( nTemplatePageId );
        page.setDescription( strDescription );
        page.setOrder( nOrder );
        page.setRole( strRole );
        page.setCodeTheme( strTheme );
        page.setNodeStatus( nNodeStatus );

        return strErrorUrl;
    }

    /**
     * Returns the list of the orders
     *
     * @return The list of the orders in form of a ReferenceList object
     */
    private ReferenceList getOrdersList(  )
    {
        ReferenceList list = new ReferenceList(  );
        int nOrderMax = AppPropertiesService.getPropertyInt( PROPERTY_LIST_ORDER_MAX, 15 );

        for ( int i = 1; i <= nOrderMax; i++ )
        {
            list.addItem( i, String.valueOf( i ) );
        }

        return list;
    }

    /**
     * Returns an html template containing the list of the portlet types
     * @param user The AdminUser
     * @return The html code
     */
    private Collection getPortletTypeList( AdminUser user )
    {
        List<PortletType> listPortletType = PortletTypeHome.getPortletTypesList( getLocale(  ) );

        return RBACService.getAuthorizedCollection( listPortletType, PortletResourceIdService.PERMISSION_CREATE, user );
    }

    /**
     * Gets an html template displaying the patterns list available in the portal for the layout
     *
     * @param nTemplatePageId The identifier of the layout to select in the list
     * @param nPageTemplatePageId The pafa templatepage id
     * @param nIndexRow the index row
     * @return The html code of the list
     */
    private String getTemplatesPageList( int nTemplatePageId, int nPageTemplatePageId, String nIndexRow )
    {
        HashMap model = new HashMap(  );

        PageTemplate pageTemplate = PageTemplateHome.findByPrimaryKey( nTemplatePageId );
        model.put( MARK_PAGE_TEMPLATE, pageTemplate );
        model.put( MARK_INDEX_ROW, nIndexRow );

        String strChecked = ( pageTemplate.getId(  ) == nPageTemplatePageId ) ? "checked=\"checked\"" : "";
        model.put( Markers.PAGE_TEMPLATE_CHECKED, strChecked );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_TEMPLATE_ROW, getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Return AdminSite Url
     * @param nId The PageId
     * @return url
     */
    private String getUrlPage( int nId )
    {
        UrlItem url = new UrlItem( JSP_ADMIN_SITE );
        url.addParameter( Parameters.PAGE_ID, nId );

        return url.getUrl(  );
    }

    /**
     *
     * @param request The HttpServletRequest
     * @param page The Page
     * @param nParentPageId
     * @return strParentPageId the new parent id
     */
    private String getNewParentPageId( HttpServletRequest request, Page page, int nParentPageId )
    {
        String strErrorUrl = null;

        if ( nParentPageId != 0 )
        {
            int nChildPagePageId = nParentPageId;
            boolean bPageExist = PageHome.checkPageExist( nChildPagePageId );

            if ( bPageExist )
            {
                if ( nChildPagePageId >= PortalService.getRootPageId(  ) )
                {
                    Page childPage = PageHome.getPage( nChildPagePageId );
                    int nParentChildPageId = childPage.getParentPageId(  );

                    while ( ( nChildPagePageId != page.getId(  ) ) && ( nChildPagePageId != 0 ) )
                    {
                        if ( nParentChildPageId != page.getId(  ) )
                        {
                            childPage = PageHome.getPage( nParentChildPageId );
                            nChildPagePageId = childPage.getId(  );
                            nParentChildPageId = childPage.getParentPageId(  );
                        }
                        else
                        {
                            return AdminMessageService.getMessageUrl( request, MESSAGE_PAGE_ID_CHILDPAGE,
                                AdminMessage.TYPE_STOP );
                        }
                    }

                    if ( nParentPageId != page.getId(  ) )
                    {
                        String strParentPageId = Integer.toString( nParentPageId );
                        page.setParentPageId( Integer.parseInt( strParentPageId ) );
                    }
                    else
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_SAME_PAGE_ID, AdminMessage.TYPE_STOP );
                    }
                }
                else
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_ROOT_PAGE_FORBIDDEN,
                        AdminMessage.TYPE_STOP );
                }
            }
            else
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_PAGE_ID, AdminMessage.TYPE_STOP );
            }
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_PAGE_ID, AdminMessage.TYPE_STOP );
        }

        return strErrorUrl;
    }
}
