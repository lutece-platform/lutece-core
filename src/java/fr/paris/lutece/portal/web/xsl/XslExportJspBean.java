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
package fr.paris.lutece.portal.web.xsl;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.xsl.XslExport;
import fr.paris.lutece.portal.business.xsl.XslExportHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.xsl.XslExportResourceIdService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.filesystem.FileSystemUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 *
 * class XslExportJspBean
 *
 */
public class XslExportJspBean extends PluginAdminPageJspBean
{
    /**
     * Right to manage XSL Export
     */
    public static final String RIGHT_MANAGE_XSL_EXPORT = "CORE_XSL_EXPORT_MANAGEMENT";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -8697851692630602527L;

    // templates
    private static final String TEMPLATE_MANAGE_XSL_EXPORT = "admin/xsl/manage_xsl_export.html";
    private static final String TEMPLATE_CREATE_XSL_EXPORT = "admin/xsl/create_xsl_export.html";
    private static final String TEMPLATE_MODIFY_XSL_EXPORT = "admin/xsl/modify_xsl_export.html";

    // Markers
    private static final String MARK_XSL_EXPORT_LIST = "xsl_export_list";
    private static final String MARK_XSL_EXPORT = "xsl_export";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_PERMISSION_CREATE = "right_create";
    private static final String MARK_PERMISSION_MODIFY = "right_modify";
    private static final String MARK_PERMISSION_DELETE = "right_delete";
    private static final String MARK_LIST_PLUGINS = "list_plugins";

    // parameters form
    private static final String PARAMETER_ID_XSL_EXPORT = "id_xsl_export";
    private static final String PARAMETER_ID_FILE = "id_file";
    private static final String PARAMETER_TITLE = "title";
    private static final String PARAMETER_DESCRIPTION = "description";
    private static final String PARAMETER_EXTENSION = "extension";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_PLUGIN = "plugin";

    // other constants
    private static final String EMPTY_STRING = "";

    // message
    private static final String MESSAGE_CONFIRM_REMOVE_XSL_EXPORT = "portal.xsl.message.confirm_remove_xsl_export";
    private static final String MESSAGE_MANDATORY_FIELD = "portal.util.message.mandatoryField";

    // private static final String MESSAGE_CAN_NOT_REMOVE_XSL_EXPORT = "portal.xsl.message.can_not_remove_xsl_export";
    private static final String FIELD_TITLE = "portal.xsl.create_xsl_export.label_title";
    private static final String FIELD_DESCRIPTION = "portal.xsl.create_xsl_export.label_description";
    private static final String FIELD_EXTENSION = "portal.xsl.create_xsl_export.label_extension";

    // private static final String FIELD_EXTENSION = "portal.xsl.create_xsl_export.label_extension";
    private static final String FIELD_FILE = "portal.xsl.create_xsl_export.label_file";
    private static final String MESSAGE_XML_NOT_VALID = "portal.xsl.message.xml_not_valid";
    private static final String MESSAGE_PERMISSION_DENIED = "portal.xsl.message.permission_denied";

    // properties
    private static final String PROPERTY_ITEM_PER_PAGE = "paginator.xsl.itemsPerPage";
    private static final String PROPERTY_MANAGE_XSL_EXPORT_TITLE = "portal.xsl.manage_xsl_export.page_title";
    private static final String PROPERTY_MODIFY_XSL_EXPORT_TITLE = "portal.xsl.modify_xsl_export.title";
    private static final String PROPERTY_CREATE_XSL_EXPORT_TITLE = "portal.xsl.create_xsl_export.title";

    // Jsp Definition
    private static final String JSP_MANAGE_XSL_EXPORT = "jsp/admin/xsl/ManageXslExport.jsp";
    private static final String JSP_DO_REMOVE_XSL_EXPORT = "jsp/admin/xsl/DoRemoveXslExport.jsp";

    // session fields
    private int _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_ITEM_PER_PAGE, 15 );
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

    /**
     * Return the xsl export management page ( list of export format )
     * @param request The Http request
     * @return Html of the xsl export management page
     */
    public String getManageXslExport( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        List<XslExport> listXslExport = XslExportHome.getList(  );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        model.put( MARK_PERMISSION_CREATE,
            RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                XslExportResourceIdService.PERMISSION_CREATE, getUser(  ) ) );
        model.put( MARK_PERMISSION_MODIFY,
            RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                XslExportResourceIdService.PERMISSION_MODIFY, getUser(  ) ) );
        model.put( MARK_PERMISSION_DELETE,
            RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                XslExportResourceIdService.PERMISSION_DELETE, getUser(  ) ) );

        LocalizedPaginator<XslExport> paginator = new LocalizedPaginator<XslExport>( listXslExport, _nItemsPerPage,
                getJspManageXslExport( request ), PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale(  ) );

        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, EMPTY_STRING + _nItemsPerPage );
        model.put( MARK_XSL_EXPORT_LIST, paginator.getPageItems(  ) );
        setPageTitleProperty( PROPERTY_MANAGE_XSL_EXPORT_TITLE );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_XSL_EXPORT, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Gets the xsl export creation page
     * @param request The HTTP request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The xsl export creation page
     */
    public String getCreateXslExport( HttpServletRequest request )
        throws AccessDeniedException
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        Collection<Plugin> listPlugins = PluginService.getPluginList(  );
        ReferenceList refListPlugins = new ReferenceList(  );
        ReferenceItem refItem = new ReferenceItem(  );
        Plugin pluginCore = PluginService.getCore(  );
        refItem.setCode( pluginCore.getName(  ) );
        refItem.setName( pluginCore.getName(  ) );
        refListPlugins.add( refItem );

        for ( Plugin plugin : listPlugins )
        {
            if ( plugin != null )
            {
                refItem = new ReferenceItem(  );
                refItem.setCode( plugin.getName(  ) );
                refItem.setName( plugin.getName(  ) );
                refListPlugins.add( refItem );
            }
        }

        model.put( MARK_LIST_PLUGINS, refListPlugins );

        if ( !RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    XslExportResourceIdService.PERMISSION_CREATE, getUser(  ) ) )
        {
            throw new AccessDeniedException( MESSAGE_PERMISSION_DENIED );
        }

        setPageTitleProperty( PROPERTY_CREATE_XSL_EXPORT_TITLE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_XSL_EXPORT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Perform the xsl export creation
     * @param request The HTTP request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The URL to go after performing the action
     */
    public String doCreateXslExport( HttpServletRequest request )
        throws AccessDeniedException
    {
        XslExport xslExport = new XslExport(  );
        String strError = getXslExportData( request, xslExport );

        if ( !RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    XslExportResourceIdService.PERMISSION_CREATE, getUser(  ) ) )
        {
            throw new AccessDeniedException( MESSAGE_PERMISSION_DENIED );
        }

        if ( strError != null )
        {
            return strError;
        }

        if ( xslExport.getFile(  ) != null )
        {
            xslExport.getFile(  ).setIdFile( FileHome.create( xslExport.getFile(  ) ) );
        }

        XslExportHome.create( xslExport );

        return getJspManageXslExport( request );
    }

    /**
     * Gets the export format modification page
     * @param request The HTTP request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The export format creation page
     */
    public String getModifyXslExport( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    XslExportResourceIdService.PERMISSION_MODIFY, getUser(  ) ) )
        {
            throw new AccessDeniedException( MESSAGE_PERMISSION_DENIED );
        }

        XslExport xslExport;
        String strIdXslExport = request.getParameter( PARAMETER_ID_XSL_EXPORT );
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        int nIdXslExport = Integer.parseInt( strIdXslExport );
        xslExport = XslExportHome.findByPrimaryKey( nIdXslExport );
        model.put( MARK_XSL_EXPORT, xslExport );

        Collection<Plugin> listPlugins = PluginService.getPluginList(  );
        ReferenceList refListPlugins = new ReferenceList(  );
        ReferenceItem refItem = new ReferenceItem(  );
        Plugin pluginCore = PluginService.getCore(  );
        refItem.setCode( pluginCore.getName(  ) );
        refItem.setName( pluginCore.getName(  ) );
        refListPlugins.add( refItem );

        for ( Plugin plugin : listPlugins )
        {
            if ( plugin != null )
            {
                refItem = new ReferenceItem(  );
                refItem.setCode( plugin.getName(  ) );
                refItem.setName( plugin.getName(  ) );
                refListPlugins.add( refItem );
            }
        }

        model.put( MARK_LIST_PLUGINS, refListPlugins );

        setPageTitleProperty( PROPERTY_MODIFY_XSL_EXPORT_TITLE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_XSL_EXPORT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Perform the xsl export modification
     * @param request The HTTP request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The URL to go after performing the action
     */
    public String doModifyXslExport( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    XslExportResourceIdService.PERMISSION_MODIFY, getUser(  ) ) )
        {
            throw new AccessDeniedException( MESSAGE_PERMISSION_DENIED );
        }

        XslExport xslExport;
        String strIdXslExport = request.getParameter( PARAMETER_ID_XSL_EXPORT );
        int nIdXslExport = Integer.parseInt( strIdXslExport );
        xslExport = XslExportHome.findByPrimaryKey( nIdXslExport );

        String strError = getXslExportData( request, xslExport );

        if ( strError != null )
        {
            return strError;
        }

        // if xslExport
        File fileStore = XslExportHome.findByPrimaryKey( nIdXslExport ).getFile(  );

        if ( xslExport.getFile(  ) != null )
        {
            // the file has been modified
            File fileSource = xslExport.getFile(  );
            // init id file source and id physical file before update
            fileSource.setIdFile( fileStore.getIdFile(  ) );

            if ( fileStore.getPhysicalFile(  ) != null )
            {
                fileSource.getPhysicalFile(  ).setIdPhysicalFile( fileStore.getPhysicalFile(  ).getIdPhysicalFile(  ) );
            }

            FileHome.update( fileSource );
        }
        else
        {
            xslExport.setFile( fileStore );
        }

        XslExportHome.update( xslExport );

        return getJspManageXslExport( request );
    }

    /**
     * Gets the confirmation page of delete xsl export
     * @param request The HTTP request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return the confirmation page of delete xsl export
     */
    public String getConfirmRemoveXslExport( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    XslExportResourceIdService.PERMISSION_DELETE, getUser(  ) ) )
        {
            throw new AccessDeniedException( MESSAGE_PERMISSION_DENIED );
        }

        String strIdXslExport = request.getParameter( PARAMETER_ID_XSL_EXPORT );

        UrlItem url = new UrlItem( JSP_DO_REMOVE_XSL_EXPORT );
        url.addParameter( PARAMETER_ID_XSL_EXPORT, strIdXslExport );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_XSL_EXPORT, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Perform the export format supression
     * @param request The HTTP request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return The URL to go after performing the action
     */
    public String doRemoveXslExport( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    XslExportResourceIdService.PERMISSION_DELETE, getUser(  ) ) )
        {
            throw new AccessDeniedException( MESSAGE_PERMISSION_DENIED );
        }

        // ArrayList<String> listErrors = new ArrayList<String>( );
        String strIdXslExport = request.getParameter( PARAMETER_ID_XSL_EXPORT );
        int nIdXslExport = Integer.parseInt( strIdXslExport );
        XslExport xslExport = XslExportHome.findByPrimaryKey( nIdXslExport );

        // if ( !XslExportRemovalListenerService.getService( ).checkForRemoval( strIdXslExport, listErrors, getLocale( ) ) )
        // {
        // String strCause = AdminMessageService.getFormattedList( listErrors, getLocale( ) );
        // Object[] args =
        // { strCause };
        //
        // return AdminMessageService.getMessageUrl( request, MESSAGE_CAN_NOT_REMOVE_XSL_EXPORT, args, AdminMessage.TYPE_STOP );
        // }
        XslExportHome.remove( nIdXslExport );

        if ( xslExport.getFile(  ) != null )
        {
            FileHome.remove( xslExport.getFile(  ).getIdFile(  ) );
        }

        return getJspManageXslExport( request );
    }

    /**
     * Initiate a download of a XSL file
     * @param request The request
     * @param response The response
     * @throws IOException Throw an exception if the outputstream has error.
     */
    public void doDownloadXslExport( HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        String strXslExportId = request.getParameter( PARAMETER_ID_XSL_EXPORT );

        if ( strXslExportId != null )
        {
            int nXslExportId = Integer.parseInt( strXslExportId );
            XslExport xslExport = XslExportHome.findByPrimaryKey( nXslExportId );

            String strMimetype = xslExport.getFile(  ).getMimeType(  );
            response.setContentType( ( strMimetype != null ) ? strMimetype : "application/octet-stream" );
            response.setHeader( "Content-Disposition",
                "attachement; filename=\"" + xslExport.getFile(  ).getTitle(  ) + "\"" );

            OutputStream out = response.getOutputStream(  );
            PhysicalFile physicalFile = PhysicalFileHome.findByPrimaryKey( xslExport.getFile(  ).getPhysicalFile(  )
                                                                                    .getIdPhysicalFile(  ) );
            out.write( physicalFile.getValue(  ) );
            out.flush(  );
            out.close(  );
        }
    }

    /**
     * Get the request data and if there is no error insert the data in the
     * exportFormat object specified in parameter. return null if there is no
     * error or else return the error page url
     * @param request the request
     * @param xslExport the exportFormat Object
     * @return null if there is no error or else return the error page url
     */
    private String getXslExportData( HttpServletRequest request, XslExport xslExport )
    {
        String strError = StringUtils.EMPTY;
        String strTitle = request.getParameter( PARAMETER_TITLE );
        String strDescription = request.getParameter( PARAMETER_DESCRIPTION );
        String strExtension = request.getParameter( PARAMETER_EXTENSION );
        String strPlugin = request.getParameter( PARAMETER_PLUGIN );
        File fileSource = getFileData( PARAMETER_ID_FILE, request );

        if ( ( strTitle == null ) || strTitle.trim(  ).equals( EMPTY_STRING ) )
        {
            strError = FIELD_TITLE;
        }

        else if ( ( strDescription == null ) || strDescription.trim(  ).equals( EMPTY_STRING ) )
        {
            strError = FIELD_DESCRIPTION;
        }

        else if ( StringUtils.isBlank( strExtension ) )
        {
            strError = FIELD_EXTENSION;
        }

        else if ( ( xslExport.getFile(  ) == null ) && ( fileSource == null ) )
        {
            strError = FIELD_FILE;
        }

        if ( strPlugin == null )
        {
            strPlugin = StringUtils.EMPTY;
        }

        // Mandatory fields
        if ( !strError.equals( EMPTY_STRING ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( strError, getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields,
                AdminMessage.TYPE_STOP );
        }

        // Check the XML validity of the XSL stylesheet
        if ( fileSource != null )
        {
            strError = isValid( fileSource.getPhysicalFile(  ).getValue(  ) );

            if ( strError != null )
            {
                Object[] args = { strError };

                return AdminMessageService.getMessageUrl( request, MESSAGE_XML_NOT_VALID, args, AdminMessage.TYPE_STOP );
            }
        }

        xslExport.setTitle( strTitle );
        xslExport.setDescription( strDescription );
        xslExport.setExtension( strExtension );
        xslExport.setPlugin( strPlugin );

        xslExport.setFile( fileSource );

        return null;
    }

    /**
     * Use parsing for validate the modify xsl file
     * @param baXslSource the xsl source
     * @return the message exception when the validation is false
     */
    private String isValid( byte[] baXslSource )
    {
        String strError = null;

        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance(  );
            SAXParser analyzer = factory.newSAXParser(  );
            InputSource is = new InputSource( new ByteArrayInputStream( baXslSource ) );
            analyzer.getXMLReader(  ).parse( is );
        }
        catch ( Exception e )
        {
            strError = e.getMessage(  );
        }

        return strError;
    }

    /**
     * return the url of manage export format
     * @param request the request
     * @return the url of manage export format
     */
    private String getJspManageXslExport( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + JSP_MANAGE_XSL_EXPORT;
    }

    /**
     * Get a file contained in the request from the name of the parameter
     *
     * @param strFileInputName name of the file parameter of the request
     * @param request the request
     * @return file the file contained in the request with the given parameter
     *         key
     */
    private static File getFileData( String strFileInputName, HttpServletRequest request )
    {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        FileItem fileItem = multipartRequest.getFile( strFileInputName );

        if ( ( fileItem != null ) && ( fileItem.getName(  ) != null ) && !EMPTY_STRING.equals( fileItem.getName(  ) ) )
        {
            File file = new File(  );
            PhysicalFile physicalFile = new PhysicalFile(  );
            physicalFile.setValue( fileItem.get(  ) );
            file.setTitle( FileUploadService.getFileNameOnly( fileItem ) );
            file.setSize( (int) fileItem.getSize(  ) );
            file.setPhysicalFile( physicalFile );
            file.setMimeType( FileSystemUtil.getMIMEType( FileUploadService.getFileNameOnly( fileItem ) ) );

            return file;
        }

        return null;
    }
}
