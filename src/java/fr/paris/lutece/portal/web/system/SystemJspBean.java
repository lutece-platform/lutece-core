/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
package fr.paris.lutece.portal.web.system;

import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage system features ( manage logs, view system files, ... ).
 */
public class SystemJspBean extends AdminFeaturesPageJspBean
{
    // Right
    public static final String RIGHT_PROPERTIES_MANAGEMENT = "CORE_PROPERTIES_MANAGEMENT";
    public static final String RIGHT_CACHE_MANAGEMENT = "CORE_CACHE_MANAGEMENT";
    public static final String RIGHT_LOGS_VISUALISATION = "CORE_LOGS_VISUALISATION";

    // Jsp definition
    public static final String JSP_MANAGE_CACHES = "ManageCaches.jsp";
    public static final String JSP_MANAGE_PROPERTIES = "ManageProperties.jsp";

    // Markers
    private static final String MARK_SERVICES_LIST = "services_list";
    private static final String MARK_FILES_LIST = "files_list";
    private static final String MARK_FILES_SYSTEM_DIRECTORY = "files_system_directory";
    private static final String MARK_FILES_SYSTEM_NAME = "file_system_name";
    private static final String MARK_FILE_SYSTEM_DATA = "file_system_data";
    private static final String MARK_WEBMASTER_PROPERTIES = "webmaster_properties";
    private static final String MARK_PROPERTIES_LIST = "properties_list";

    // Template Files path
    private static final String TEMPLATE_MANAGE_FILES_SYSTEM = "admin/system/manage_files_system.html";
    private static final String TEMPLATE_VIEW_FILES_SYSTEM = "admin/system/view_files_system.html";
    private static final String TEMPLATE_VIEW_FILE = "admin/system/view_file.html";
    private static final String TEMPLATE_MANAGE_CACHES = "admin/system/manage_caches.html";
    private static final String TEMPLATE_MANAGE_PROPERTIES = "admin/system/manage_properties.html";
    private static final String TEMPLATE_MODIFY_PROPERTIES = "admin/system/modify_properties.html";

    // Parameters
    private static final String PARAMETER_FILE = "file";
    private static final String PARAMETER_DIRECTORY = "directory";
    private static final String PARAMETER_DIR = "dir";

    // Properties file definition
    private static final String PROPERTY_WEBMASTER_FILE = "file.webmaster.properties";
    private static final String PROPERTY_FILES_SYSTEM_LIST = "system.list";
    private static final String PROPERTY_TITLE_MANAGE_FILES_SYSTEM = "portal.system.manage_files_system.pageTitle";
    private static final String PROPERTY_FILE_DESCRIPTION = "portal.system.manage_files_system.description.";
    private static final String PROPERTY_FILE_NAME = "portal.system.manage_files_system.name.";
    private static final String PROPERTY_TITLE_VIEW_FILES_SYSTEM = "portal.system.view_files_system.pageTitle";
    private static final String PROPERTY_TITLE_VIEW_FILE = "portal.system.view_file.pageTitle";

    // Hashtable which contains the parameter to the webmaster.properties file
    private static Map<String, String> _mapProperties = new HashMap<String, String>(  );

    // Property object will contains all property necessary to the updated webmaster.properties treatment
    private static Properties _properties = new Properties(  );

    /**
     * Returns ViewLogs page
     *
     * @param request The HTTP request.
     * @return The HTML code
     */
    public String getManageFilesSystem( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_TITLE_MANAGE_FILES_SYSTEM );

        StringTokenizer st = new StringTokenizer( AppPropertiesService.getProperty( PROPERTY_FILES_SYSTEM_LIST ), "," );
        ArrayList<SystemFile> list = new ArrayList<SystemFile>(  );

        while ( st.hasMoreElements(  ) )
        {
            String strFileSystemName = st.nextToken(  ).trim(  );
            SystemFile file = new SystemFile(  );
            file.setName( I18nService.getLocalizedString( PROPERTY_FILE_NAME + strFileSystemName, request.getLocale(  ) ) );
            file.setDescription( I18nService.getLocalizedString( PROPERTY_FILE_DESCRIPTION + strFileSystemName,
                    request.getLocale(  ) ) );
            file.setDirectory( AppPropertiesService.getProperty( "system." + strFileSystemName + ".directory" ) );
            list.add( file );
        }

        HashMap<String, Collection<SystemFile>> model = new HashMap<String, Collection<SystemFile>>(  );
        model.put( MARK_FILES_LIST, list );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_FILES_SYSTEM, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Get directory list
     * @param request The request
     * @return The html code
     */
    public String getManageFilesSystemDir( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_TITLE_VIEW_FILES_SYSTEM );

        String strDir = request.getParameter( PARAMETER_DIR );
        String strDirectory = AppPathService.getWebAppPath(  ) + strDir;
        File directoryLog = new File( strDirectory );
        File[] fileLog = directoryLog.listFiles(  );

        // Creating names array
        int nLogNumber = fileLog.length;
        String[] arrayLog = new String[nLogNumber];
        ArrayList<SystemFile> list = new ArrayList<SystemFile>(  );

        for ( int i = 0; i < nLogNumber; i++ )
        {
            arrayLog[i] = fileLog[i].getName(  );
        }

        //defines the order of the logs
        File[] screenArrayLog = new File[nLogNumber];
        String name;

        for ( int i = 0; i < nLogNumber; i++ )
        {
            int numero = i;
            name = arrayLog[i];

            for ( int a = 0; a < nLogNumber; a++ )
            {
                String strNameTemp = arrayLog[a];

                if ( name.toLowerCase(  ).trim(  ).compareTo( strNameTemp.toLowerCase(  ).trim(  ) ) > 0 )
                {
                    name = strNameTemp;
                    numero = a;
                }
            }

            screenArrayLog[i] = fileLog[numero];
            arrayLog[numero] = "{";
        }

        for ( int i = 0; i < nLogNumber; i++ )
        {
            File file = (File) screenArrayLog[i];
            SystemFile f = new SystemFile(  );
            f.setName( file.getName(  ) );
            f.setDirectory( strDir );
            f.setSize( (int) ( file.length(  ) / 1000 ) + 1 );
            f.setDate( new Date( file.lastModified(  ) ) );
            list.add( f );
        }

        Map<String, Serializable> model = new HashMap<String, Serializable>(  );
        model.put( MARK_FILES_LIST, list );
        model.put( MARK_FILES_SYSTEM_DIRECTORY, strDir );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_FILES_SYSTEM, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods to display a system file

    /**
     * Returns a FileView page
     *
     * @param request The HTTP request.
     * @return The HTML code
     */
    public String getFileView( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        setPageTitleProperty( PROPERTY_TITLE_VIEW_FILE );

        String strFilePath = null;
        String strFileData = null;
        String strFile = request.getParameter( PARAMETER_FILE );
        String strDirectory = request.getParameter( PARAMETER_DIRECTORY );

        if ( strFile == null )
        {
            strFilePath = "ERROR";
            strFileData = "No file selected !";
        }
        else
        {
            strFilePath = AppPathService.getWebAppPath(  );

            if ( strFilePath == null )
            {
                strFilePath = "ERROR";
                strFileData = strFile + " not found !";
            }
            else
            {
                strFileData = getFileData( strFilePath + strDirectory + strFile );
            }
        }

        model.put( MARK_FILES_SYSTEM_NAME, strFilePath + strDirectory + strFile );
        model.put( MARK_FILE_SYSTEM_DATA, strFileData );
        model.put( MARK_FILES_SYSTEM_DIRECTORY, strDirectory );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_FILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the page to manage caches
     * @param request The HttpServletRequest
     * @return The HTML code.
     */
    public String getManageCaches( HttpServletRequest request )
    {
        HashMap<String, Collection<CacheableService>> model = new HashMap<String, Collection<CacheableService>>(  );
        model.put( MARK_SERVICES_LIST, PortalService.getCacheableServicesList(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_CACHES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process cache resetting
     *
     * @return The URL to display when the process is done.
     */
    public static String doResetCaches(  )
    {
        PortalService.resetCache(  );
        AppTemplateService.resetCache(  );

        return JSP_MANAGE_CACHES;
    }

    /**
     * Reload all properties files of the application
     *
     * @return The URL to display when the process is done.
     */
    public String doReloadProperties(  )
    {
        AppPropertiesService.reloadAll(  );

        return JSP_MANAGE_CACHES;
    }

    /**
     * Returns the page to manage webmaster.properties
     *
     * @return The HTML code.
     */
    public String getManageProperties(  )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_WEBMASTER_PROPERTIES, getFileProperties(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_PROPERTIES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the form to update info about the webmaster.properties
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    public String getModifyProperties( HttpServletRequest request )
    {
        HashMap<String, ReferenceList> model = new HashMap<String, ReferenceList>(  );
        ReferenceList updatePropertiesList = new ReferenceList(  );

        File file = new File( AppPropertiesService.getProperty( PROPERTY_WEBMASTER_FILE ) );

        try
        {
            FileInputStream is = new FileInputStream( file );
            _properties.load( is );

            // Storage of the parameters in the hashtable
            Enumeration updateProperties = _properties.keys(  );

            while ( updateProperties.hasMoreElements(  ) )
            {
                String key = (String) updateProperties.nextElement(  );
                _mapProperties.put( key, _properties.getProperty( key ) );

                updatePropertiesList.addItem( key, _properties.getProperty( key ) );
            }
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        // Insert the rows in the list
        model.put( MARK_PROPERTIES_LIST, updatePropertiesList );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PROPERTIES, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Process the change form of the webmaster.properties
     *
     * @param request The Http request
     * @param context The context
     * @return The Jsp URL of the process result
     */
    public static String doModifyProperties( HttpServletRequest request, ServletContext context )
    {
        File file = new File( AppPropertiesService.getProperty( PROPERTY_WEBMASTER_FILE ) );
        String strProperties = "";
        FileWriter fw = null;

        try
        {
            FileInputStream is = new FileInputStream( file );
            _properties.load( is );

            // Storage of the parameters in the hashtable
            Enumeration enumKey = request.getParameterNames(  );

            while ( enumKey.hasMoreElements(  ) )
            {
                String key = (String) enumKey.nextElement(  );
                String strValue = request.getParameter( key );
                _mapProperties.put( key, strValue );
                strProperties += ( key + "=" + strValue + "\n" );
            }

            fw = new FileWriter( file );
            fw.write( strProperties );
            fw.close(  );
            is.close(  );
            AppPropertiesService.reload( "webmaster.properties" );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        // if the operation occurred well, redirects towards the view of the Properties
        return JSP_MANAGE_PROPERTIES;
    }

    ///////////////////////////////////////////////////////////////////////////////
    // Private Implementation

    /**
     * Returns the FileProperties page
     *
     * @return The HTML code
     */
    private static String getFileProperties(  )
    {
        String strFileData = getFileData( AppPropertiesService.getProperty( PROPERTY_WEBMASTER_FILE ) );

        return strFileData;
    }

    /**
     * Returns the content of a file.
     *
     * @param strFilePath The file Path
     * @return The content of the file.
     */
    private static String getFileData( String strFilePath )
    {
        StringBuffer sbData = new StringBuffer(  );

        try
        {
            FileInputStream is = new FileInputStream( strFilePath );
            int chr = 0;

            while ( chr != -1 )
            {
                chr = is.read(  );
                sbData.append( (char) chr );
            }

            //we delete the end of file caracter
            sbData.setLength( sbData.length(  ) - 1 );
        }
        catch ( FileNotFoundException e )
        {
            sbData.append( "File " + strFilePath + " not found" );
        }
        catch ( IOException e )
        {
            sbData.append( "Error reading the file : " );
            sbData.append( strFilePath );
        }

        return sbData.toString(  );
    }
}
