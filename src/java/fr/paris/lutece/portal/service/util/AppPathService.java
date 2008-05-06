/*
 * Copyright (c) 2002-2008, Mairie de Paris
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
package fr.paris.lutece.portal.service.util;

import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.text.MessageFormat;

import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * this class provides services for locate repository or url
 */
public final class AppPathService
{
    private static final String MSG_LOG_PROPERTY_NOT_FOUND = "Property {0} not found in the properties file ";
    private static final int PORT_NUMBER_HTTP = 80;
    private static final String PROPERTY_BASE_URL = "lutece.base.url";
    private static final String PROPERTY_PORTAL_URL = "lutece.portal.path";
    private static final String PROPERTY_ADMIN_URL = "lutece.admin.path";
    private static final String PROPERTY_ADMIN_MENU_URL = "lutece.admin.menu.url";
    private static final String PROPERTY_VIRTUAL_HOST_KEYS = "virtualHostKeys";
    private static final String PROPERTY_VIRTUAL_HOST_KEY_PARAMETER = "virtualHostKey.parameterName";
    private static final String PROPERTY_VIRTUAL_HOST = "virtualHost.";
    private static final String PROPERTY_PREFIX_URL = "url.";
    private static final String SUFFIX_BASE_URL = ".baseUrl";
    private static final String SUFFIX_DESCRIPTION = ".description";
    private static String _strWebAppPath;

    /**
     * Creates a new AppPathService object.
     */
    private AppPathService(  )
    {
    }

    /**
     * Initialize The path service
     * 
     * @param context The servlet context
     */
    public static void init( ServletContext context )
    {
        String strRealPath = context.getRealPath( "/" );
        _strWebAppPath = normalizeWebappPath( strRealPath );
    }

    /**
     * Initialize The webapppath
     * 
     * @param strWebAppPath The Webapp path
     */
    public static void init( String strWebAppPath )
    {
        _strWebAppPath = strWebAppPath;
    }

    /**
     * Returns the absolute path of a repository from a relative definition in properties file
     *
     * @param strKey the repository key definied in properties file
     * @return the repository absolute path
     * @throws AppException
     */
    public static String getPath( String strKey )
    {
        // Adds relative path found from strKey
        String strDirectory = AppPropertiesService.getProperty( strKey );

        if ( strDirectory == null )
        {
            Object[] propertyMissing = { strKey };
            String strMsg = MessageFormat.format( MSG_LOG_PROPERTY_NOT_FOUND, propertyMissing );
            throw new AppException( strMsg );
        }

        return getWebAppPath(  ) + strDirectory;
    }

    /**
     * Returns the webapp path from the properties file
     *
     * @return the webapp path
     * @throws AppException
     */
    public static String getWebAppPath(  )
    {
        return _strWebAppPath;
    }

    /**
     * Returns the absolute path of file from its relative definition in properties file.
     *
     * @param strKey the repository key defined in properties file
     * @param strFilename The name of file
     * @return the absolute path of file
     */
    public static String getPath( String strKey, String strFilename )
    {
        return getPath( strKey ) + "/" + strFilename;
    }

    /**
     * Gets a file as stream
     * @param strPath the path
     * @param strFilename The name of file
     * @return a FileInput Stream object
     */
    public static FileInputStream getResourceAsStream( String strPath, String strFilename )
    {
        String strFilePath = getWebAppPath(  ) + strPath + strFilename;

        try
        {
            File file = new File( strFilePath );
            FileInputStream fis = new FileInputStream( file );

            return fis;
        }
        catch ( IOException e )
        {
            throw new AppException( "Unable to get file : " + strFilePath );
        }
    }

    /**
     * Returns the absolute path of a repository from a relative path
     *
     * @param strDirectory the relative path
     * @return the repository absolute path
     * @throws AppException
     */
    public static String getAbsolutePathFromRelativePath( String strDirectory )
    {
        return _strWebAppPath + strDirectory;
    }

    /**
     * Return the url of the webapp, built from rhe request
     * @param request The HttpServletRequest
     * @return strBase the webapp url
     */
    public static String getBaseUrl( HttpServletRequest request )
    {
        String strBase;

        // Search for a Virtual Host Base Url defined in the request
        strBase = getVirtualHostBaseUrl( request );

        // If not found, get the base url from the config.properties
        if ( ( strBase == null ) || ( strBase.equals( "" ) ) )
        {
            strBase = AppPropertiesService.getProperty( PROPERTY_BASE_URL );
        }

        if ( ( strBase == null ) || ( strBase.equals( "" ) ) )
        {
            // Dynamic base URL if not defined in the properties
            strBase = request.getScheme(  ) + "://" + request.getServerName(  );

            int nPort = request.getServerPort(  );

            if ( nPort != PORT_NUMBER_HTTP )
            {
                strBase += ( ":" + nPort );
            }

            strBase += request.getContextPath(  );
        }

        return strBase + "/";
    }

    /**
     * Returns the portal page relative url (jsp/site/Portal.jsp) defined in lutece.properties
     * @return the Portal Url
     */
    public static String getPortalUrl(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_PORTAL_URL );
    }

    /**
     * Returns the admin portal page relative url (jsp/admin/site/AdminSite.jsp) defined in lutece.properties
     * @return the Portal Url
     */
    public static String getAdminPortalUrl(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_ADMIN_URL );
    }

    /**
     * Returns the admin menu page relative url (jsp/admin/site/AdminMenu.jsp) defined in lutece.properties
     * @return the Admin Menu Url
     */
    public static String getAdminMenuUrl(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_ADMIN_MENU_URL );
    }

    private static String normalizeWebappPath( String strPath )
    {
        // convert Windows path separator if present
        String strNormalized = StringUtil.substitute( strPath, "/", "\\" );

        // remove the ending separator if present
        if ( strNormalized.endsWith( "/" ) )
        {
            strNormalized = strNormalized.substring( 0, strNormalized.length(  ) - 1 );
        }

        return strNormalized;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Multiple virtual hosts configuration management

    /**
     * Gets available virtual hosts defined in the config.properties
     * @return A reference list containing the key and the description of each
     * virtual host configuration. The list is empty if there is no configuration defined.
     */
    public static ReferenceList getAvailableVirtualHosts(  )
    {
        ReferenceList list = null;

        // Get keys list form config.properties
        String strKeysList = AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST_KEYS );

        if ( strKeysList != null )
        {
            list = new ReferenceList(  );

            // Extracts each key (separated by a comma)
            StringTokenizer strTokens = new StringTokenizer( strKeysList, "," );

            while ( strTokens.hasMoreTokens(  ) )
            {
                String strHostKey = strTokens.nextToken(  );
                String strHostKeyDescription = AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST + strHostKey +
                        SUFFIX_DESCRIPTION );
                list.addItem( strHostKey, strHostKeyDescription );
            }
        }

        return list;
    }

    /**
     * Gets a Virtual Host Key if the request contains a virtual host key
     * @param request The HTTP request
     * @return A Virtual Host Key if present, otherwise null.
     */
    public static String getVirtualHostKey( HttpServletRequest request )
    {
        String strVirtalHostKey = null;

        // Get from config.properties the parameter name for virtual host keys
        String strVirtualHostKeyParameter = AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST_KEY_PARAMETER );

        if ( ( request != null ) && ( strVirtualHostKeyParameter != null ) &&
                ( !strVirtualHostKeyParameter.equals( "" ) ) )
        {
            // Search for this parameter into the request
            strVirtalHostKey = request.getParameter( strVirtualHostKeyParameter );
        }

        return strVirtalHostKey;
    }

    /**
     * Gets a Base Url for a virtual host if the request contains a virtual host key
     * @param request The HTTP request
     * @return A virtual host base url if present, otherwise null.
     */
    private static String getVirtualHostBaseUrl( HttpServletRequest request )
    {
        String strBaseUrl = null;
        String strVirtalHostKey = getVirtualHostKey( request );

        if ( ( strVirtalHostKey != null ) && ( !strVirtalHostKey.equals( "" ) ) )
        {
            // If found gets the Base url for this virtual host by its key
            strBaseUrl = AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST + strVirtalHostKey + SUFFIX_BASE_URL );
        }

        return strBaseUrl;
    }

    /**
     * Build the url item to use for a url that includes the redirection parameter for reconnection.
     * @param strRootUrl the root part of the url, to build an absolute url
     * @param strUrlPropertySuffixKey The property suffix to retrieve the url
     * @return an absolute url, completed with the redirectUrl parameter (contains the relative part of the url), as an UrlItem
     */
    public static UrlItem buildRedirectUrlItem( String strRootUrl, String strUrlPropertySuffixKey )
    {
        String strUrl = AppPropertiesService.getProperty( PROPERTY_PREFIX_URL + strUrlPropertySuffixKey );
        UrlItem url = new UrlItem( strRootUrl + strUrl );
        url.addParameter( Parameters.REDIRECT_URL, strUrlPropertySuffixKey );

        return url;
    }

    /**
     * Retrieve the url to redirect to after login. It is given by the redirectUrl parameter if found.
     * The request parameters are copied (except the login and acces code).
     * This is to be used by the doLogin method of AdminLoginJspBean.
     * @param request the http request
     * @param strDefaultRedirectUrl the default url to go to after login
     * @return an UrlItem corresponding to the url to redirect to after login.
     */
    public static UrlItem resolveRedirectUrl( HttpServletRequest request, String strDefaultRedirectUrl )
    {
        String strUrl = strDefaultRedirectUrl;

        String strUrlKey = request.getParameter( Parameters.REDIRECT_URL );
        String strRedirectUrl = null;

        if ( strUrlKey != null )
        {
            strRedirectUrl = AppPropertiesService.getProperty( PROPERTY_PREFIX_URL + strUrlKey );
        }

        if ( strRedirectUrl != null )
        {
            strUrl = strRedirectUrl;
        }

        Enumeration enumParams = request.getParameterNames(  );
        UrlItem url = new UrlItem( getBaseUrl( request ) + strUrl );

        String strParamName;

        while ( enumParams.hasMoreElements(  ) )
        {
            strParamName = (String) enumParams.nextElement(  );

            if ( !strParamName.equals( Parameters.REDIRECT_URL ) && !strParamName.equals( Parameters.ACCESS_CODE ) &&
                    !strParamName.equals( Parameters.PASSWORD ) )
            {
                url.addParameter( strParamName, request.getParameter( strParamName ) );
            }
        }

        return url;
    }
}
