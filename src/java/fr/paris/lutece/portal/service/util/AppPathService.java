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
package fr.paris.lutece.portal.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.resource.LuteceResource;
import fr.paris.lutece.plugins.resource.ResourceManager;
import fr.paris.lutece.plugins.resource.loader.FileResourceLoader;
import fr.paris.lutece.plugins.resource.loader.ResourceNotFoundException;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.stream.StreamUtil;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

/**
 * this class provides services for locate repository or url
 */
public final class AppPathService
{
    public static final String SESSION_BASE_URL = "base_url";
    private static final String MSG_LOG_PROPERTY_NOT_FOUND = "Property {0} not found in the properties file ";
    private static final int PORT_NUMBER_HTTP = 80;
    private static final String PROPERTY_BASE_URL = "lutece.base.url";
    private static final String PROPERTY_PORTAL_URL = "lutece.portal.path";
    private static final String PROPERTY_SITE_MESSAGE_URL = "lutece.siteMessage.path";
    private static final String PROPERTY_ADMIN_URL = "lutece.admin.path";
    private static final String PROPERTY_ADMIN_MENU_URL = "lutece.admin.menu.url";
    private static final String PROPERTY_PORTAL_REDIRECT_URL = "lutece.portal.redirect.url";
    private static final String PROPERTY_VIRTUAL_HOST_KEYS = "virtualHostKeys";
    private static final String PROPERTY_VIRTUAL_HOST_KEY_PARAMETER = "virtualHostKey.parameterName";
    private static final String PROPERTY_VIRTUAL_HOST = "virtualHost.";
    private static final String PROPERTY_PREFIX_URL = "url.";
    private static final String PROPERTY_PROD_BASE_URL = "lutece.prod.url";
    private static final String PROPERTY_INSTANCE = "lutece.webapp.instance";
    private static final String PROPERTY_EXPORT_PATH = "lutece.export.path";
    private static final String INSTANCE_DEFAULT = "default";
    private static final String SUFFIX_BASE_URL = ".baseUrl";
    private static final String SUFFIX_DESCRIPTION = ".description";
    private static final String SLASH = "/";
    private static final String DOUBLE_POINTS = ":";
    private static final String SYSTEM_TMP_DIR = "java.io.tmpdir";

    // Datastore keys
    private static final String KEY_ADMIN_HOME_URL = "portal.site.site_property.admin_home_url";
    private static final String KEY_PORTAL_HOME_URL = "portal.site.site_property.home_url";
    private static String _strWebAppPath = normalizeWebappPath(
           AppPathService.class.getProtectionDomain( ).getCodeSource( ).getLocation( ).getFile( ).split( "/WEB-INF/" ) [0] );
    private static ResourceManager _resourceManager;
    
    /**
     * Creates a new AppPathService object.
     */
    private AppPathService( )
    {
    }

    /**
     * Initialize The path service
     *
     * @param context
     *            The servlet context
     */
    public static void init( ServletContext context )
    {
        String strRealPath = context.getRealPath( "/" );
        initResourceManager( );
        //The real path is null when the deployed web application is an archive (.war file)
        if(strRealPath != null && !strRealPath.isEmpty( )) {
        	_strWebAppPath = normalizeWebappPath( strRealPath );
            _resourceManager.addSearchPath(FileResourceLoader.ID, _strWebAppPath);
        }
    }
    public static void initResourceManager( ) {
    	if( _resourceManager == null ) {
    		_resourceManager = CDI.current().select(ResourceManager.class).get( ); 
            _resourceManager.addSearchPath(FileResourceLoader.ID, _strWebAppPath);
    	}
    }

    /**
     * Initialize The webapppath
     *
     * @param strWebAppPath
     *            The Webapp path
     */
    public static void init( String strWebAppPath )
    {
        _strWebAppPath = normalizeWebappPath( strWebAppPath );
    }

   
    /**
     * Returns the webapp path from the properties file
     *
     * @return the webapp path
     */
    public static String getWebAppPath( )
    {
        return _strWebAppPath;
    }
    /**
     * Returns the absolute path of a repository from a relative definition in properties file
     *
     *
     * @return the repository absolute path else return null
     * @param strKey
     *            the repository key definied in properties file
     * @throws PathNotFoundException if the resource cannot be found
     */
    public static String getPath( String strKey ) 
    {
        String strDirectory = getRelativePath( strKey );
        try {
			return getRealPath( strDirectory );
		} catch (ResourceNotFoundException e) {
			AppLogService.debug("Path not found :{} ",strDirectory, e);
			throw new PathNotFoundException("Path not found: "+strDirectory,e);
		}
    }
    /**
     * Returns the absolute path of file from its relative definition in properties file.
     *
     * @param strKey
     *            the repository key defined in properties file
     * @param strFilename
     *            The name of file
     * @return the absolute path of file else null if file not found
     * @throws ResourceNotFoundException if the resource cannot be found
     */
    public static String getPath( String strKey, String strFilename ) 
    {
    	String strDirectory = getRelativePath( strKey );
        try {
			return getRealPath( strDirectory+ SLASH + strFilename );
		} catch (ResourceNotFoundException e) {
			AppLogService.debug("File not found :{}/{}",strDirectory, strFilename, e);
			throw new PathNotFoundException("File not found at path: "+strDirectory, e);
		}
    }
    /**
     * Returns the absolute path of a repository from a relative path
     *
     *
     * @return the repository absolute path
     * @param strDirectory
     *            the relative path
     * @throws ResourceNotFoundException  if the resource cannot be found
     */
    public static String getAbsolutePathFromRelativePath( String strDirectory ) 
    {
        try {
			return getRealPath( strDirectory );
		} catch (ResourceNotFoundException e) {
			AppLogService.debug("Path not found :{}",strDirectory, e);
			throw new PathNotFoundException("Absolute Path not found at the relative path: "+strDirectory, e);
		}
    }
    /**
     * Retrieves the resource paths for a given relative path within the web application.
     * <p>
     * The path provided should be relative to the web application's root, and the method 
     * will return a set of resource paths matching that location.
     * </p>
     *
     * @param relativePath The relative path to the resource(s) within the web application.
     * @return A set of resource paths as strings, or an empty set if no resources are found.
     * @throws ResourceNotFoundException if the resource cannot be found
     */
    public static Set<String> getResourcesPathsFromRelativePath( String relativePath ) throws ResourceNotFoundException
    {
    		Set<URL> listURL= getResourceURLFromRelativePath( relativePath );
    		return listURL.stream()
                    .map(URL::getPath) 
                    .collect(Collectors.toSet());
    }
    /**
     * Retrieves the resource paths as a Set<URL> from a given directory.
     * This method reads the files in the specified directory, converts their paths to URLs,
     * and returns them in a Set. If no resources are found, a {@link ResourceNotFoundException}
     * will be thrown.
     *
     * @param path The base path in the file system, which can be either relative or absolute.
     *                 This path is used to locate the directory containing the resources.
     *                 The path must start with a "/".
     * @return A Set containing the URLs of the resources found in the directory.
     * @throws ResourceNotFoundException If no resources are found or if an error occurs while reading
     *                                    the directory or converting paths to URLs.
     */
    public static Set<URL> getResourceURLFromRelativePath(String path) throws ResourceNotFoundException{
    	return _resourceManager.getResourceURL(path );
    }
	/**
	 * Retrieves a list of files for a given relative path within the web application.
	 * <p>
	 * This method constructs the full path to each file based on the relative path provided, 
	 * and adds the corresponding files to a set. It assumes that the resources are accessible 
	 * via the servlet context and converts them to file objects.
	 * </p>
	 * <p>
	 * Note: The paths used in this method are relative to the web application, and the method
	 * processes the paths using the servlet context.
	 * </p>
	 *
	 * @param relativePath The relative path to the resources within the web application.
	 * @return A set of {@link File} objects corresponding to the resources found at the specified path.
	 * @throws ResourceNotFoundException if the resource cannot be found
	 */
    public static Set<File> getlistFilesFromRelativePath( String relativePath ) throws ResourceNotFoundException
    {
    	Set<String> resourcePaths = null;
        Set<File> listFiles = new HashSet<>( ); 
		resourcePaths = getResourcesPathsFromRelativePath( relativePath );
    	if (resourcePaths != null) {
             for (String resourcePath : resourcePaths) {
                 File file;
				try {
					file = new File (_resourceManager.getResource(resourcePath).getURL().getPath());			
					listFiles.add(file);  
				} catch (IOException e) {
					AppLogService.error(e.getMessage(), e);
					throw new AppException( e.getMessage(), e );
				}         		         
              }
        }
        return  listFiles;
    }
    
   
    /**
     * Gets a file as stream
     * This method is deprecated use {@link #getResourceStream(String, String)}
     * @param strPath
     *            the path
     * @param strFilename
     *            The name of file
     * @return a FileInput Stream object
     * @throws ResourceNotFoundException if the resource cannot be found
     * @deprecatedUse {@link #getResourceStream(String, String)} instead
     */
    @Deprecated
    public static FileInputStream getResourceAsStream( String strPath, String strFilename )
    {
        FileInputStream fis = null;
        try
        {
        	File file = new File (_resourceManager.getResource(strPath + strFilename).getURL().getPath());			
            fis = new FileInputStream( file );

            return fis;
        }
        catch( IOException e )
        {
            StreamUtil.safeClose( fis );
            throw new AppException( "Unable to get file : " + strPath+ strFilename);
        } catch (ResourceNotFoundException e) {
            throw new AppException( "Unable to get file : " + strPath+ strFilename);
		}
    }
    /**
     * Retrieves an input stream for a resource based on a path and a file name.
     *
     * @param strPath     the relative path to the directory containing the resource
     * @param strFilename the name of the file to load
     * @return an input stream representing the requested resource
     * @throws ResourceNotFoundException if the resource cannot be found
     */
    public static InputStream getResourceStream(String strPath, String strFilename) throws ResourceNotFoundException {
        return getResourceStream(strPath + strFilename);
    }

    /**
     * Retrieves an input stream for a resource based solely on its relative path.
     *
     * @param strFilename the relative path of the file to load
     * @return an input stream representing the requested resource
     * @throws ResourceNotFoundException if the resource cannot be found
     */
    public static InputStream getResourceStream(String strFilename) throws ResourceNotFoundException {
        return _resourceManager.getResourceAsInputStream(strFilename);
    }

    /**
     * Retrieves a Lutece resource based on its relative path.
     *
     * @param relativePath the relative path of the resource to retrieve
     * @return an instance of {@link LuteceResource} representing the resource
     * @throws ResourceNotFoundException if the resource cannot be found
     */
    public static LuteceResource getLuteceResource(String relativePath) throws ResourceNotFoundException {
        return _resourceManager.getResource(relativePath);
    }

    /**
     * Return the url of the webapp, built from the request
     *
     * @param request
     *            The HttpServletRequest
     * @return strBase the webapp url
     */
    public static String getBaseUrl( HttpServletRequest request )
    {
        if ( request == null )
        {
            return getBaseUrl( );
        }

        String strBase;

        // Search for a Virtual Host Base Url defined in the request
        strBase = getVirtualHostBaseUrl( request );

        // If not found, get the base url from session
        if ( ( strBase == null ) || strBase.equals( StringUtils.EMPTY ) )
        {
            HttpSession session = request.getSession( false );

            if ( session != null )
            {
                Object oBase = session.getAttribute( SESSION_BASE_URL );

                if ( oBase != null )
                {
                    strBase = (String) oBase;
                }
            }
        }

        // If not found, get the base url from the config.properties
        if ( ( strBase == null ) || ( strBase.equals( StringUtils.EMPTY ) ) )
        {
            strBase = AppPropertiesService.getProperty( PROPERTY_BASE_URL );
        }

        if ( ( strBase == null ) || ( strBase.equals( StringUtils.EMPTY ) ) )
        {
            // Dynamic base URL if not defined in the properties
            strBase = request.getScheme( ) + DOUBLE_POINTS + SLASH + SLASH + request.getServerName( );

            int nPort = request.getServerPort( );

            if ( nPort != PORT_NUMBER_HTTP )
            {
                strBase += ( DOUBLE_POINTS + nPort );
            }

            strBase += request.getContextPath( );
        }

        if ( !strBase.endsWith( SLASH ) )
        {
            strBase += SLASH;
        }

        return strBase;
    }

    /**
     * Return the url of the webapp. The method should only be used out of request context (by daemons for example). If there is a request context, use
     * {@link AppPathService#getBaseUrl(HttpServletRequest)} instead.
     *
     * @return The webapp url, or null if the 'lutece.base.url' property has not been set.
     * @deprecated Use {@link AppPathService#getBaseUrl(HttpServletRequest)} instead
     */
    @Deprecated
    public static String getBaseUrl( )
    {
        HttpServletRequest request = LocalVariables.getRequest( );

        if ( request != null )
        {
            return getBaseUrl( request );
        }

        // FIXME : lutece.base.url is only set when using WSSO
        String strBaseUrl = AppPropertiesService.getProperty( PROPERTY_BASE_URL );

        if ( strBaseUrl == null )
        {
            strBaseUrl = StringUtils.EMPTY;
        }
        else
        {
            if ( !strBaseUrl.endsWith( SLASH ) )
            {
                strBaseUrl += SLASH;
            }
        }

        return strBaseUrl;
    }

    /**
     * Return the webapp prod url (or the base url if no prod url has been definied).
     *
     * @param request
     *            The HTTP request
     * @return The prod url
     */
    public static String getProdUrl( HttpServletRequest request )
    {
        String strBaseUrl = AppPropertiesService.getProperty( PROPERTY_PROD_BASE_URL );

        if ( StringUtils.isBlank( strBaseUrl ) )
        {
            strBaseUrl = getBaseUrl( request );
        }

        if ( !strBaseUrl.endsWith( SLASH ) )
        {
            strBaseUrl += SLASH;
        }

        return strBaseUrl;
    }

    /**
     * Return the webapp prod url. If no prod URL has been defined, then the base URL is returned
     *
     * @param strBaseUrl
     *            The base URL
     * @return The prod url
     */
    public static String getProdUrl( String strBaseUrl )
    {
        String strProdUrl = AppPropertiesService.getProperty( PROPERTY_PROD_BASE_URL );

        if ( StringUtils.isBlank( strProdUrl ) )
        {
            strProdUrl = strBaseUrl;
        }

        if ( ( strProdUrl != null ) && !strProdUrl.endsWith( SLASH ) )
        {
            strProdUrl += SLASH;
        }

        return strProdUrl;
    }

    /**
     * Return the url of the webapp, built from the request
     *
     * @param request
     *            The HttpServletRequest
     * @return strBase the webapp url
     */
    public static String getSiteMessageUrl( HttpServletRequest request )
    {
        // Set the site message url
        return SiteMessageService.setSiteMessageUrl( getBaseUrl( request ) + getSiteMessageUrl( ) );
    }

    /**
     * Returns the portal page relative url (jsp/site/Portal.jsp) defined in lutece.properties
     *
     * @return the Portal Url
     */
    public static String getPortalUrl( )
    {
        return AppPropertiesService.getProperty( PROPERTY_PORTAL_URL );
    }

    /**
     * Returns the forward URL for webapp's root path. Default is (jsp/site/Portal.jsp) defined in lutece.properties
     *
     * @return the Portal Root forward Url
     */
    public static String getRootForwardUrl( )
    {
        return DatastoreService.getDataValue( KEY_PORTAL_HOME_URL, AppPropertiesService.getProperty( PROPERTY_PORTAL_REDIRECT_URL ) );
    }

    /**
     * Returns the Site Message relative url (jsp/site/SiteMessage.jsp) defined in lutece.properties
     *
     * @return the SiteMessage Url
     */
    public static String getSiteMessageUrl( )
    {
        return AppPropertiesService.getProperty( PROPERTY_SITE_MESSAGE_URL );
    }

    /**
     * Returns the admin portal page relative url (jsp/admin/site/AdminSite.jsp) defined in lutece.properties
     *
     * @return the Portal Url
     */
    public static String getAdminPortalUrl( )
    {
        return AppPropertiesService.getProperty( PROPERTY_ADMIN_URL );
    }

    /**
     * Returns the admin menu page relative url (jsp/admin/site/AdminMenu.jsp) defined in lutece.properties
     *
     * @return the Admin Menu Url
     */
    public static String getAdminMenuUrl( )
    {
        return DatastoreService.getDataValue( KEY_ADMIN_HOME_URL, AppPropertiesService.getProperty( PROPERTY_ADMIN_MENU_URL ) );
    }

    /**
     * Normalizes the Webapp Path
     *
     * @param strPath
     *            The path to normalize
     * @return The normalized path
     */
    private static String normalizeWebappPath( String strPath )
    {
        String strNormalized = strPath;

        // For windows, remove the leading \
        if ( ( strNormalized.length( ) > 3 ) && ( strNormalized.indexOf( ':' ) == 2 ) )
        {
            strNormalized = strNormalized.substring( 1 );
        }

        // convert Windows path separator if present
        strNormalized = StringUtil.substitute( strNormalized, "/", "\\" );

        // remove the ending separator if present
        if ( strNormalized.endsWith( "/" ) )
        {
            strNormalized = strNormalized.substring( 0, strNormalized.length( ) - 1 );
        }

        return strNormalized;
    }

    // //////////////////////////////////////////////////////////////////////////
    // Multiple virtual hosts configuration management
    /**
     * Gets available virtual hosts defined in the config.properties
     *
     * @return A reference list containing the key and the description of each virtual host configuration. The list is empty if there is no configuration
     *         defined.
     */
    public static ReferenceList getAvailableVirtualHosts( )
    {
        ReferenceList list = null;

        // Get keys list form config.properties
        String strKeysList = AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST_KEYS );

        if ( strKeysList != null )
        {
            list = new ReferenceList( );

            // Extracts each key (separated by a comma)
            StringTokenizer strTokens = new StringTokenizer( strKeysList, "," );

            while ( strTokens.hasMoreTokens( ) )
            {
                String strHostKey = strTokens.nextToken( );
                String strHostKeyDescription = AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST + strHostKey + SUFFIX_DESCRIPTION );
                list.addItem( strHostKey, strHostKeyDescription );
            }
        }

        return list;
    }

    /**
     * Gets a Virtual Host Key if the request contains a virtual host key
     *
     * @param request
     *            The HTTP request
     * @return A Virtual Host Key if present, otherwise null.
     */
    public static String getVirtualHostKey( HttpServletRequest request )
    {
        String strVirtalHostKey = null;

        // Get from config.properties the parameter name for virtual host keys
        String strVirtualHostKeyParameter = AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST_KEY_PARAMETER );

        if ( ( request != null ) && ( strVirtualHostKeyParameter != null ) && ( !strVirtualHostKeyParameter.equals( "" ) ) )
        {
            // Search for this parameter into the request
            strVirtalHostKey = request.getParameter( strVirtualHostKeyParameter );
        }

        return strVirtalHostKey;
    }

    
    /**
     * Build the url item to use for a url that includes the redirection parameter for reconnection.
     *
     * @param strRootUrl
     *            the root part of the url, to build an absolute url
     * @param strUrlPropertySuffixKey
     *            The property suffix to retrieve the url
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
     * Retrieve the url to redirect to after login. It is given by the redirectUrl parameter if found. The request parameters are copied (except the login and
     * acces code and token). This is to be used by the doLogin method of AdminLoginJspBean.
     *
     * @param request
     *            the http request
     * @param strDefaultRedirectUrl
     *            the default url to go to after login
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

        Enumeration<String> enumParams = request.getParameterNames( );
        UrlItem url = new UrlItem( getBaseUrl( request ) + strUrl );

        String strParamName;

        while ( enumParams.hasMoreElements( ) )
        {
            strParamName = (String) enumParams.nextElement( );

            if ( !strParamName.equals( Parameters.REDIRECT_URL ) && !strParamName.equals( Parameters.ACCESS_CODE )
                    && !strParamName.equals( Parameters.PASSWORD ) && !strParamName.equals( SecurityTokenService.PARAMETER_TOKEN ) )
            {
                url.addParameter( strParamName, request.getParameter( strParamName ) );
            }
        }

        return url;
    }

    /**
     * Returns the absolute url corresponding to the given one, if the later was found to be relative. An url starting with "http://" is absolute. A relative
     * url should be given relatively to the webapp root.
     *
     * @param request
     *            the http request (provides the base path if needed)
     * @param strUrl
     *            the url to transform
     * @return the corresonding absolute url
     *
     *
     */
    public static String getAbsoluteUrl( HttpServletRequest request, String strUrl )
    {
        if ( ( strUrl != null ) && !strUrl.startsWith( "http://" ) && !strUrl.startsWith( "https://" ) )
        {
            return AppPathService.getBaseUrl( request ) + strUrl;
        }

        return strUrl;
    }

    /**
     * Gets the webapp instance defined in the config.properties file with the key lutece.webapp.instance
     *
     * @return The instance name
     * @since 4.1
     */
    public static String getWebappInstance( )
    {
        String strInstance = AppPropertiesService.getProperty( PROPERTY_INSTANCE );

        if ( ( strInstance != null ) && ( !strInstance.equals( StringUtils.EMPTY ) ) )
        {
            return strInstance;
        }

        return INSTANCE_DEFAULT;
    }

    /**
     * Returns whether the current instance is the default webapp instance
     *
     * @return true if default, otherwise false
     */
    public static boolean isDefaultWebappInstance( )
    {
        return INSTANCE_DEFAULT.equals( getWebappInstance( ) );
    }
    /**
     * Gets a Base Url for a virtual host if the request contains a virtual host key
     *
     * @param request
     *            The HTTP request
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
     * Returns the absolute path of a repository from a relative definition in properties file
     *
     *
     * @return the repository absolute path
     * @param strKey
     *            the repository key definied in properties file
     */
    private static String getRelativePath( String strKey )
    {
        // Adds relative path found from strKey
        String strDirectory = AppPropertiesService.getProperty( strKey );

        if ( strDirectory == null )
        {
            Object [ ] propertyMissing = {
                    strKey
            };
            String strMsg = MessageFormat.format( MSG_LOG_PROPERTY_NOT_FOUND, propertyMissing );
            throw new AppException( strMsg );
        }

        return strDirectory;
    }

    private static String getRealPath( String relativePath ) throws ResourceNotFoundException
    {
    	try {
			return _resourceManager.getResource(relativePath).getURL().getPath();
		} catch (IOException e) {			
			AppLogService.error(e.getMessage(), e);
			throw new PathNotFoundException("Get url exception form relativePath: "+relativePath,e);
		}
    }

    /**
     * Returns the lutece global export path, defined in lutece.export.path property
     * 
     * @return The export path
     */
    public static String getExportPath( )
    {
        String strPath = null;
        String p = AppPropertiesService.getProperty( PROPERTY_EXPORT_PATH, SYSTEM_TMP_DIR );
        if ( p.startsWith( SYSTEM_TMP_DIR ) )
        {
            String strTmpPath = p.replace( SYSTEM_TMP_DIR, "" );
            File f = new File( System.getProperty( SYSTEM_TMP_DIR ), strTmpPath );
            f.mkdir( );
            strPath = f.getAbsolutePath( );
        }
        return strPath;
    }

}
