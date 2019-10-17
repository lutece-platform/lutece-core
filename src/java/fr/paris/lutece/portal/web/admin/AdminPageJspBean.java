/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.business.style.PageTemplate;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.content.ContentPostProcessorService;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.page.PageResourceIdService;
import fr.paris.lutece.portal.service.page.PageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.portal.ThemesService;
import fr.paris.lutece.portal.service.portlet.PortletResourceIdService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.resource.ExtendableResourcePluginActionManager;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the admin interface to manage administration of site
 */
public class AdminPageJspBean extends AdminFeaturesPageJspBean
{
    // //////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    // Properties for page titles
    public static final String PROPERTY_PAGE_TITLE_ADMIN_SITE = "portal.site.admin_page.pageTitle";

    // Markers
    private static final String MARK_PAGE = "page";
    private static final String MARK_PAGE_INIT_ID = "page_init_id";
    private static final String MARK_PAGE_MESSAGE = "page_message";
    private static final String MARK_PORTLET_TYPES_LIST = "portlet_types_list";
    private static final String MARK_PAGE_ORDER_LIST = "page_order_list";
    private static final String MARK_PAGE_ROLES_LIST = "page_roles_list";
    private static final String MARK_PAGE_THEMES_LIST = "page_themes_list";
    private static final String MARK_IMAGE_URL = "image_url";
    private static final String MARK_PAGE_TEMPLATES_LIST = "page_templates_list";
    private static final String MARK_PAGE_TEMPLATE = "page_template";
    private static final String MARK_INDEX_ROW = "index_row";
    private static final String MARK_AUTORIZATION = "authorization";
    private static final String MARK_PAGE_BLOCK = "page_block";
    private static final String MARK_PAGE_UPDATE_DATE = "page_update_date";

    // Parameters
    private static final String PARAMETER_IMAGE_CONTENT = "image_content";
    private static final String PARAMETER_NODE_STATUS = "node_status";
    private static final String PARAMETER_BLOCK = "param_block";
    private static final String PARAMETER_PAGE_TEMPLATE_UPDATE_IMAGE = "update_image";
    private static final int BLOCK_SEARCH = 1;
    private static final int BLOCK_PROPERTY = 2;
    private static final int BLOCK_CHILDPAGE = 5;

    // Templates
    private static final String TEMPLATE_PAGE_TEMPLATE_ROW = "admin/site/page_template_list_row.html";
    private static final String TEMPLATE_ADMIN_PAGE = "admin/site/admin_page.html";
    private static final String TEMPLATE_ADMIN_PAGE_BLOCK_SEARCH = "admin/site/admin_page_block_search.html";
    private static final String TEMPLATE_ADMIN_PAGE_BLOCK_PROPERTY = "admin/site/admin_page_block_property.html";
    private static final String TEMPLATE_ADMIN_PAGE_BLOCK_CHILDPAGE = "admin/site/admin_page_block_childpage.html";

    // Properties definition
    private static final String PROPERTY_MESSAGE_PAGE_INEXISTENT = "portal.site.admin_page.messagePageInexistent";
    private static final String PROPERTY_MESSAGE_PAGE_FORMAT = "portal.site.admin_page.messagePageFormat";
    private static final String PROPERTY_MESSAGE_CONFIRM_REMOVE_PAGE = "portal.site.message.confirmRemovePage";
    private static final String PROPERTY_LIST_ORDER_MAX = "list.order.max";

    // Jsp
    private static final String JSP_ADMIN_SITE = "AdminSite.jsp";
    private static final String JSP_PATH = "jsp/admin/site/";
    private static final String JSP_REMOVE_PAGE = JSP_PATH + "DoRemovePage.jsp";

    // Messages
    private static final String MESSAGE_TITLE_INVALID_CHARACTERS = "portal.site.message.invalidCharactersInTitleName";
    private static final String MESSAGE_DESCRIPTION_INVALID_CHARACTERS = "portal.site.message.invalidCharactersInDescription";
    private static final String MESSAGE_CANNOT_REMOVE_CHILDPAGE_EXISTS = "portal.site.message.cannotRemoveChildPageExists";
    private static final String MESSAGE_LENGTH_DESCRIPTION = "portal.site.message.pageLengthDescription";
    private static final String MESSAGE_ROOT_PAGE_FORBIDDEN = "portal.site.message.rootPageForbidden";
    private static final String MESSAGE_INVALID_PAGE_ID = "portal.site.message.pageIdInvalid";
    private static final String MESSAGE_PAGE_ID_CHILDPAGE = "portal.site.message.pageIdChildPage";
    private static final String MESSAGE_SAME_PAGE_ID = "portal.site.message.pageSameId";
    private static final String MESSAGE_MISSING_MANUAL_UPDATE_DATE = "portal.site.message.missingManualUpdateDate";
    private static IPageService _pageService = SpringContextService.getBean( "pageService" );

    /**
     * Displays the page which contains the management forms of a skin page whose identifier is specified in parameter
     *
     * @param request
     *            The identifier of the page
     * @return The html code of the management forms of a page
     */
    public String getAdminPage( HttpServletRequest request )
    {
        String strPageId = request.getParameter( Parameters.PAGE_ID );

        if ( ( strPageId == null ) || strPageId.equals( "" ) )
        {
            strPageId = String.valueOf( PortalService.getRootPageId( ) );
        }

        String strParamBlock = request.getParameter( PARAMETER_BLOCK );

        return getAdminPageBlock( strPageId, strParamBlock, request );
    }

    /**
     * Returns the html code for displaying the page whose identifier is specified in parameter from the administration unit. <br>
     * That is useful to make a preview of the page.
     *
     * @param request
     *            The http request
     * @return The html code of the page
     * @throws UserNotSignedException
     *             The UserNotSignedException
     * @throws SiteMessageException
     *             occurs when a site message need to be displayed
     */
    public String getAdminPagePreview( HttpServletRequest request ) throws UserNotSignedException, SiteMessageException
    {
        ContentService cs = PortalService.getInvokedContentService( request );
        int nMode = 1;

        String strContent;

        if ( cs != null )
        {
            strContent = cs.getPage( request, nMode );
        }
        else
        {
            strContent = PortalService.getDefaultPage( request, nMode );
        }

        if ( ContentPostProcessorService.hasProcessor( ) )
        {
            strContent = ContentPostProcessorService.process( request, strContent );
        }

        return strContent;
    }

    /**
     * Processes of the modification of the page informations
     *
     * @param request
     *            The http request
     * @return The jsp url result of the process
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doModifyPage( HttpServletRequest request ) throws AccessDeniedException
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        int nPageId = Integer.parseInt( mRequest.getParameter( Parameters.PAGE_ID ) );

        Page page = PageHome.getPage( nPageId );

        String strErrorUrl = getPageData( mRequest, page );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        strErrorUrl = setParentPageId( mRequest, page );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        strErrorUrl = updatePicture( mRequest, page );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        if ( !SecurityTokenService.getInstance( ).validate( mRequest, TEMPLATE_ADMIN_PAGE_BLOCK_PROPERTY ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        setAuthorizationNode( page );

        // Updates the page
        _pageService.updatePage( page );

        // Displays again the current page with the modifications
        return getUrlPage( nPageId );
    }

    /**
     * Update the page's picture
     * 
     * @param mRequest
     *            the request
     * @param page
     *            the page
     * @return an error URL, or <code>null</code>
     */
    private String updatePicture( MultipartHttpServletRequest mRequest, Page page )
    {
        String strUpdatePicture = mRequest.getParameter( PARAMETER_PAGE_TEMPLATE_UPDATE_IMAGE );
        if ( strUpdatePicture == null )
        {
            return null;
        }

        FileItem item = mRequest.getFile( PARAMETER_IMAGE_CONTENT );
        String strPictureName = FileUploadService.getFileNameOnly( item );

        if ( strPictureName.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( mRequest, Messages.MANDATORY_FILE, AdminMessage.TYPE_STOP );
        }
        byte [ ] bytes = item.get( );
        String strMimeType = item.getContentType( );
        page.setImageContent( bytes );
        page.setMimeType( strMimeType );
        return null;
    }

    /**
     * Sets the authorization node
     * 
     * @param page
     *            the page
     */
    private void setAuthorizationNode( Page page )
    {
        Integer nOldAutorisationNode = page.getIdAuthorizationNode( );

        if ( page.getNodeStatus( ) != 0 )
        {
            Page parentPage = PageHome.getPage( page.getParentPageId( ) );
            page.setIdAuthorizationNode( parentPage.getIdAuthorizationNode( ) );
        }
        else
        {
            page.setIdAuthorizationNode( page.getId( ) );
        }
        if ( ( page.getIdAuthorizationNode( ) == null ) || !page.getIdAuthorizationNode( ).equals( nOldAutorisationNode ) )
        {
            PageService.updateChildrenAuthorizationNode( page.getId( ), page.getIdAuthorizationNode( ) );
        }
    }

    /**
     * Sets the parent page ID
     * 
     * @param mRequest
     *            the requset
     * @param page
     *            the page
     * @return an error URL, or <code>null</code>
     */
    private String setParentPageId( MultipartHttpServletRequest mRequest, Page page )
    {
        int nParentPageId = Integer.parseInt( mRequest.getParameter( Parameters.PARENT_ID ) );

        if ( nParentPageId != page.getParentPageId( ) )
        {
            String strErrorUrl = getNewParentPageId( mRequest, page, nParentPageId );

            if ( strErrorUrl != null )
            {
                return strErrorUrl;
            }
        }
        return null;
    }

    /**
     * Display the confirm page for the delete of a page
     *
     * @param request
     *            The http request
     * @return The url of the confirm page
     */
    public String getRemovePage( HttpServletRequest request )
    {
        String strPageId = request.getParameter( Parameters.PAGE_ID );
        if ( !StringUtils.isNumeric( strPageId ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_PAGE_ID, AdminMessage.TYPE_ERROR );
        }
        int nPageId = Integer.parseInt( strPageId );
        Page page = PageHome.getPage( nPageId );
        if ( page == null || page.getId( ) == 0 || page.getId( ) != nPageId )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_PAGE_ID, AdminMessage.TYPE_ERROR );
        }
        Map<String, Object> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_ID, strPageId );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, JSP_REMOVE_PAGE ) );

        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_REMOVE_PAGE, new Object [ ] {
            page.getName( )
        }, null, JSP_REMOVE_PAGE, null, AdminMessage.TYPE_CONFIRMATION, parameters );
    }

    /**
     * Processes the deletion of a page
     *
     * @param request
     *            The http request
     * @return The jsp url result of the process
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doRemovePage( HttpServletRequest request ) throws AccessDeniedException
    {
        String strPageId = request.getParameter( Parameters.PAGE_ID );
        if ( !StringUtils.isNumeric( strPageId ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_PAGE_ID, AdminMessage.TYPE_ERROR );
        }
        int nPageId = Integer.parseInt( strPageId );
        Page page = PageHome.getPage( nPageId );
        if ( page == null || page.getId( ) == 0 || page.getId( ) != nPageId )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_PAGE_ID, AdminMessage.TYPE_ERROR );
        }
        // Checks that the page has no child
        Collection<Page> list = PageHome.getChildPagesMinimalData( nPageId );

        if ( CollectionUtils.isNotEmpty( list ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_REMOVE_CHILDPAGE_EXISTS, new Object [ ] {
                    page.getName( ), list.size( )
            }, JSP_PATH + getUrlPage( nPageId ), AdminMessage.TYPE_STOP );
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, JSP_REMOVE_PAGE ) )
        {
            throw new AccessDeniedException( "Invalid CSRF token" );
        }

        int nParentPageId = page.getParentPageId( );

        // Delete the page
        _pageService.removePage( nPageId );

        return getUrlPage( nParentPageId );
    }

    /**
     * Processes the creation of a child page to the page whose identifier is stored in the http request
     *
     * @param request
     *            The http request
     * @return The jsp url result of the process
     * @throws AccessDeniedException
     *             If the security token is invalid
     */
    public String doCreateChildPage( HttpServletRequest request ) throws AccessDeniedException
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;

        String strParentPageId = mRequest.getParameter( Parameters.PAGE_ID );
        int nParentPageId = Integer.parseInt( strParentPageId );

        Page page = new Page( );
        page.setParentPageId( nParentPageId );

        String strErrorUrl = getPageData( mRequest, page );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }
        if ( !SecurityTokenService.getInstance( ).validate( mRequest, TEMPLATE_ADMIN_PAGE_BLOCK_CHILDPAGE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        // Create the page
        _pageService.createPage( page );

        // set the authorization node
        if ( page.getNodeStatus( ) != 0 )
        {
            Page parentPage = PageHome.getPage( page.getParentPageId( ) );
            page.setIdAuthorizationNode( parentPage.getIdAuthorizationNode( ) );
        }
        else
        {
            page.setIdAuthorizationNode( page.getId( ) );
        }

        FileItem item = mRequest.getFile( PARAMETER_IMAGE_CONTENT );

        byte [ ] bytes = item.get( );
        String strMimeType = item.getContentType( );

        page.setImageContent( bytes );
        page.setMimeType( strMimeType );

        _pageService.updatePage( page );

        // Displays again the current page with the modifications
        return getUrlPage( page.getId( ) );
    }

    /**
     * Management of the image associated to the page
     * 
     * @param page
     *            The Page Object
     * @param strPageId
     *            The page identifier
     * @return The url
     */
    public String getResourceImagePage( Page page, String strPageId )
    {
        String strResourceType = _pageService.getResourceTypeId( );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, strPageId );

        return url.getUrlWithEntity( );
    }

    // ////////////////////////////////////////////////////////////////////////////////
    // Private implementation

    /**
     * Displays the page which contains the management forms of a skin page whose identifier is specified in parameter.
     *
     * @param strPageId
     *            The identifier of the page
     * @param strParamBlock
     *            The block parameter to display
     * @param request
     *            The request
     * @return The management page of a page
     */
    private String getAdminPageBlock( String strPageId, String strParamBlock, HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        Page page = null;
        int nPageId = 1;
        int nPageIdInit = 1;
        int nParamBlock = 0;

        try
        {
            nPageId = Integer.parseInt( strPageId );
            nPageIdInit = nPageId;
            nParamBlock = ( strParamBlock != null ) ? Integer.parseInt( strParamBlock ) : 0;

            boolean bPageExist = PageHome.checkPageExist( nPageId );

            if ( bPageExist )
            {
                page = PageHome.getPage( nPageId );
                model.put( MARK_PAGE_MESSAGE, "" );
            }
            else
            {
                nPageId = PortalService.getRootPageId( );
                page = PageHome.getPage( nPageId );
                model.put( MARK_PAGE_MESSAGE, I18nService.getLocalizedString( PROPERTY_MESSAGE_PAGE_INEXISTENT, getLocale( ) ) );
            }
        }
        catch( NumberFormatException nfe )
        {
            nPageId = PortalService.getRootPageId( );
            page = PageHome.getPage( nPageId );
            model.put( MARK_PAGE_MESSAGE, I18nService.getLocalizedString( PROPERTY_MESSAGE_PAGE_FORMAT, getLocale( ) ) );
        }

        switch( nParamBlock )
        {
            case BLOCK_SEARCH:
                model.put( MARK_PAGE_BLOCK, getAdminPageBlockSearch( nPageIdInit, model ) );

                break;

            case BLOCK_PROPERTY:
            case BLOCK_CHILDPAGE:
                model.put( MARK_PAGE_BLOCK, getAdminPageBlockProperty( page, nParamBlock, model, request ) );

                break;

            default:
                model.put( MARK_PAGE_BLOCK, "" );

                break;
        }

        model.put( MARK_PORTLET_TYPES_LIST, getPortletTypeList( getUser( ) ) );
        model.put( MARK_PAGE, page );
        ExtendableResourcePluginActionManager.fillModel( request, getUser( ), model, strPageId, Page.RESOURCE_TYPE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_PAGE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Displays the page which contains the management forms of a skin page whose identifier is specified in parameter
     *
     * @param page
     *            The page object
     * @param nParamBlock
     *            The id parameter to display
     * @param model
     *            The model
     * @param request
     *            The request
     * @return The management page of a page
     */
    private String getAdminPageBlockProperty( Page page, int nParamBlock, Map<String, Object> model, HttpServletRequest request )
    {
        model.put( MARK_PAGE, page );
        model.put( MARK_PAGE_INIT_ID, page.getId( ) );
        model.put( MARK_PAGE_ORDER_LIST, getOrdersList( ) );
        model.put( MARK_PAGE_ROLES_LIST, RoleHome.getRolesList( getUser( ) ) );
        model.put( MARK_PAGE_THEMES_LIST, ThemesService.getPageThemes( getLocale( ) ) );
        model.put( MARK_IMAGE_URL, getResourceImagePage( page, Integer.toString( page.getId( ) ) ) );
        model.put( MARK_PAGE_UPDATE_DATE, DateUtil.getDateString( page.getDateUpdate( ), request.getLocale( ) ) );

        int nIndexRow = 1;
        StringBuilder strPageTemplatesRow = new StringBuilder( );

        // Scan of the list
        for ( PageTemplate pageTemplate : PageTemplateHome.getPageTemplatesList( ) )
        {
            strPageTemplatesRow.append( getTemplatesPageList( pageTemplate.getId( ), page.getPageTemplateId( ), Integer.toString( nIndexRow ) ) );
            nIndexRow++;
        }

        model.put( MARK_PAGE_TEMPLATES_LIST, strPageTemplatesRow );

        // Add in v2.0
        int nManageAuthorization = 1;

        if ( _pageService.isAuthorizedAdminPage( page.getId( ), PageResourceIdService.PERMISSION_MANAGE, getUser( ) ) )
        {
            nManageAuthorization = 0;
        }

        model.put( MARK_AUTORIZATION, Integer.toString( nManageAuthorization ) );

        String strTemplate = TEMPLATE_ADMIN_PAGE_BLOCK_PROPERTY;

        if ( nParamBlock == BLOCK_CHILDPAGE )
        {
            strTemplate = TEMPLATE_ADMIN_PAGE_BLOCK_CHILDPAGE;
        }
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, strTemplate ) );

        HtmlTemplate template = AppTemplateService.getTemplate( strTemplate, getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * Displays the page which contains the management forms of a skin page whose identifier is specified in parameter
     *
     * @param nPageIdInit
     *            The identifier of the init page
     * @param model
     *            The HashMap
     * @return The management page of a page
     */
    private String getAdminPageBlockSearch( int nPageIdInit, Map<String, Object> model )
    {
        model.put( MARK_PAGE_INIT_ID, Integer.toString( nPageIdInit ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_PAGE_BLOCK_SEARCH, getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * Provide page data
     * 
     * @param request
     *            The HttpServletRequest
     * @param page
     *            the Page Object
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
        String strMetaKeywords = request.getParameter( Parameters.META_KEYWORDS ).trim( );
        String strMetaDescription = request.getParameter( Parameters.META_DESCRIPTION ).trim( );

        /* Added in v2.0 */
        String strNodeStatus = request.getParameter( PARAMETER_NODE_STATUS );
        int nNodeStatus = Integer.parseInt( strNodeStatus );

        boolean bDisplayDateUpdate = StringUtils.isNotEmpty( request.getParameter( Parameters.PARAMETER_DISPLAY_UPDATE_DATE ) );

        boolean bIsManualDateUpdate = StringUtils.isNotEmpty( request.getParameter( Parameters.PARAMETER_ENABLE_MANUAL_UPDATE_DATE ) );

        String strManualDateUpdate = request.getParameter( Parameters.PARAMETER_MANUAL_UPDATE_DATE );

        page.setDateUpdate( new Timestamp( new java.util.Date( ).getTime( ) ) );

        if ( bDisplayDateUpdate && bIsManualDateUpdate )
        {
            if ( StringUtils.isNotBlank( strManualDateUpdate ) )
            {
                Timestamp tsManualDate = DateUtil.formatTimestamp( strManualDateUpdate, request.getLocale( ) );
                page.setDateUpdate( tsManualDate );
            }
            else
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_MISSING_MANUAL_UPDATE_DATE, AdminMessage.TYPE_STOP );
            }
        }

        // Checks the description length (150 car. maximum)
        if ( strDescription.length( ) > 150 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_LENGTH_DESCRIPTION, AdminMessage.TYPE_STOP );
        }

        // Checks if the mandatory field page name is found in the request
        if ( strName.trim( ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Checks if the page name contains HTML special characters
        else
            if ( StringUtil.containsHtmlSpecialCharacters( strName ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_TITLE_INVALID_CHARACTERS, AdminMessage.TYPE_STOP );
            }

            // Checks if the page description contains HTML special characters
            else
                if ( StringUtil.containsHtmlSpecialCharacters( strDescription ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_DESCRIPTION_INVALID_CHARACTERS, AdminMessage.TYPE_STOP );
                }

                // Checks if the META name of the page contains HTML special characters
                else
                    if ( StringUtil.containsHtmlSpecialCharacters( strMetaKeywords ) )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_DESCRIPTION_INVALID_CHARACTERS, AdminMessage.TYPE_STOP );
                    }

                    // Checks if the META description of the page description contains HTML special characters
                    else
                        if ( StringUtil.containsHtmlSpecialCharacters( strMetaDescription ) )
                        {
                            return AdminMessageService.getMessageUrl( request, MESSAGE_DESCRIPTION_INVALID_CHARACTERS, AdminMessage.TYPE_STOP );
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
        page.setMetaKeywords( strMetaKeywords );
        page.setMetaDescription( strMetaDescription );
        page.setDisplayDateUpdate( bDisplayDateUpdate );
        page.setIsManualDateUpdate( bIsManualDateUpdate );

        return strErrorUrl;
    }

    /**
     * Returns the list of the orders
     *
     * @return The list of the orders in form of a ReferenceList object
     */
    private ReferenceList getOrdersList( )
    {
        ReferenceList list = new ReferenceList( );
        int nOrderMax = AppPropertiesService.getPropertyInt( PROPERTY_LIST_ORDER_MAX, 15 );

        for ( int i = 1; i <= nOrderMax; i++ )
        {
            list.addItem( i, String.valueOf( i ) );
        }

        return list;
    }

    /**
     * Returns an html template containing the list of the portlet types
     * 
     * @param user
     *            The AdminUser
     * @return The html code
     */
    private Collection<PortletType> getPortletTypeList( AdminUser user )
    {
        List<PortletType> listPortletType = PortletTypeHome.getPortletTypesList( getLocale( ) );

        return RBACService.getAuthorizedCollection( listPortletType, PortletResourceIdService.PERMISSION_CREATE, user );
    }

    /**
     * Gets an html template displaying the patterns list available in the portal for the layout
     *
     * @param nTemplatePageId
     *            The identifier of the layout to select in the list
     * @param nPageTemplatePageId
     *            The pafa templatepage id
     * @param nIndexRow
     *            the index row
     * @return The html code of the list
     */
    private String getTemplatesPageList( int nTemplatePageId, int nPageTemplatePageId, String nIndexRow )
    {
        Map<String, Object> model = new HashMap<>( );

        PageTemplate pageTemplate = PageTemplateHome.findByPrimaryKey( nTemplatePageId );
        model.put( MARK_PAGE_TEMPLATE, pageTemplate );
        model.put( MARK_INDEX_ROW, nIndexRow );

        String strChecked = ( pageTemplate.getId( ) == nPageTemplatePageId ) ? "checked=\"checked\"" : "";
        model.put( Markers.PAGE_TEMPLATE_CHECKED, strChecked );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_TEMPLATE_ROW, getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * Return AdminSite Url
     * 
     * @param nId
     *            The PageId
     * @return url
     */
    private String getUrlPage( int nId )
    {
        UrlItem url = new UrlItem( JSP_ADMIN_SITE );
        url.addParameter( Parameters.PAGE_ID, nId );

        return url.getUrl( );
    }

    /**
     *
     * @param request
     *            The HttpServletRequest
     * @param page
     *            The Page
     * @param nParentPageId
     *            The page parent Id
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
                if ( nChildPagePageId >= PortalService.getRootPageId( ) )
                {
                    Page childPage = PageHome.getPage( nChildPagePageId );
                    int nParentChildPageId = childPage.getParentPageId( );

                    while ( ( nChildPagePageId != page.getId( ) ) && ( nChildPagePageId != 0 ) )
                    {
                        if ( nParentChildPageId != page.getId( ) )
                        {
                            childPage = PageHome.getPage( nParentChildPageId );
                            nChildPagePageId = childPage.getId( );
                            nParentChildPageId = childPage.getParentPageId( );
                        }
                        else
                        {
                            return AdminMessageService.getMessageUrl( request, MESSAGE_PAGE_ID_CHILDPAGE, AdminMessage.TYPE_STOP );
                        }
                    }

                    if ( nParentPageId != page.getId( ) )
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
                    return AdminMessageService.getMessageUrl( request, MESSAGE_ROOT_PAGE_FORBIDDEN, AdminMessage.TYPE_STOP );
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
