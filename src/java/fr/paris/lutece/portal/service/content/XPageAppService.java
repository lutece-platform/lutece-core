/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
package fr.paris.lutece.portal.service.content;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class delivers Extra pages (xpages) to web components. An XPage is a page where the content is provided by a
 * specific class, but should be integrated into the portal struture and design. XPageApps are identified by a key
 * name.
 * To display an XPage into the portal just call the following url :<br><code>
 * Portal.jsp?page=<i>keyname</i>&amp;param1=value1&amp; ...&amp;paramN=valueN </code>
 *
 * @see fr.paris.lutece.portal.web.xpages.XPage
 */
public class XPageAppService extends ContentService
{
    public static final String PARAM_XPAGE_APP = "page";
    private static final String CONTENT_SERVICE_NAME = "XPageAppService";
    private static final String MESSAGE_ERROR_APP_BODY = "portal.util.message.errorXpageApp";
    private static final String ATTRIBUTE_XPAGE = "LUTECE_XPAGE_";
    private static Map<String, XPageApplicationEntry> _mapApplications = new HashMap<String, XPageApplicationEntry>(  );

    /**
     * Register an application by its entry defined in the plugin xml file
     * @param entry The application entry
     * @throws LuteceInitException If an error occured
     */
    public static void registerXPageApplication( XPageApplicationEntry entry )
        throws LuteceInitException
    {
        try
        {
            if ( entry.getClassName(  ) == null )
            {
                String applicationBeanName = entry.getPluginName(  ) + ".xpage." + entry.getId(  );

                if ( !SpringContextService.getContext(  ).containsBean( applicationBeanName ) )
                {
                    throw new LuteceInitException( "Error instantiating XPageApplication : " + entry.getId(  ) +
                        " - Could not find bean named " + applicationBeanName,
                        new NoSuchBeanDefinitionException( applicationBeanName ) );
                }
            }
            else
            {
                // check that the class can be found
                Class.forName( entry.getClassName(  ) ).newInstance(  );
            }

            _mapApplications.put( entry.getId(  ), entry );
            AppLogService.info( "New XPage application registered : " + entry.getId(  )
                    + ( entry.isEnabled(  ) ? "" : " (disabled)" ) );
        }
        catch ( ClassNotFoundException e )
        {
            throw new LuteceInitException( "Error instantiating XPageApplication : " + entry.getId(  ) + " - " +
                e.getCause(  ), e );
        }
        catch ( InstantiationException e )
        {
            throw new LuteceInitException( "Error instantiating XPageApplication : " + entry.getId(  ) + " - " +
                e.getCause(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            throw new LuteceInitException( "Error instantiating XPageApplication : " + entry.getId(  ) + " - " +
                e.getCause(  ), e );
        }
    }

    /**
     * Returns the Content Service name
     *
     * @return The name as a String
     */
    @Override
    public String getName(  )
    {
        return CONTENT_SERVICE_NAME;
    }

    /**
     * Analyzes request parameters to see if the request should be handled by the current Content Service
     *
     * @param request The HTTP request
     * @return true if this ContentService should handle this request
     */
    @Override
    public boolean isInvoked( HttpServletRequest request )
    {
        String strXPage = request.getParameter( PARAM_XPAGE_APP );

        if ( ( strXPage != null ) && ( strXPage.length(  ) > 0 ) )
        {
            return true;
        }

        return false;
    }

    /**
     * Enable or disable the cache feature.
     *
     * @param bCache true to enable the cache, false to disable
     */
    public void setCache( boolean bCache )
    {
    }

    /**
     * Gets the current cache status.
     *
     * @return true if enable, otherwise false
     */
    @Override
    public boolean isCacheEnable(  )
    {
        return false;
    }

    /**
     * Reset the cache.
     */
    @Override
    public void resetCache(  )
    {
    }

    /**
     * Gets the number of item currently in the cache.
     *
     * @return the number of item currently in the cache.
     */
    @Override
    public int getCacheSize(  )
    {
        return 0;
    }

    /**
     * Build the XPage content.
     *
     * @param request The HTTP request.
     * @param nMode The current mode.
     * @return The HTML code of the page.
     * @throws UserNotSignedException The User Not Signed Exception
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    @Override
    public String getPage( HttpServletRequest request, int nMode )
        throws UserNotSignedException, SiteMessageException
    {
        // Gets XPage info from the lutece.properties
        String strName = request.getParameter( PARAM_XPAGE_APP );

        XPageApplicationEntry entry = getApplicationEntry( strName );

        // TODO : Handle entry == null
        if ( ( entry != null ) && ( entry.isEnable(  ) ) )
        {
            XPage page = null;
            List<String> listRoles = entry.getRoles(  );

            if ( SecurityService.isAuthenticationEnable(  ) && ( listRoles.size(  ) > 0 ) )
            {
                LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

                if ( user != null )
                {
                    boolean bAutorized = false;

                    for ( String strRole : listRoles )
                    {
                        if ( SecurityService.getInstance(  ).isUserInRole( request, strRole ) )
                        {
                            bAutorized = true;
                        }
                    }

                    if ( bAutorized )
                    {
                        XPageApplication application = getXPageSessionInstance( request, entry );
                        page = application.getPage( request, nMode, entry.getPlugin(  ) );
                    }
                    else
                    {
                        // The user doesn't have the correct role
                        String strAccessDeniedTemplate = SecurityService.getInstance(  ).getAccessDeniedTemplate(  );
                        HtmlTemplate tAccessDenied = AppTemplateService.getTemplate( strAccessDeniedTemplate );
                        page = new XPage(  );
                        page.setContent( tAccessDenied.getHtml(  ) );
                    }
                }
                else
                {
                    throw new UserNotSignedException(  );
                }
            }
            else
            {
                XPageApplication application = getXPageSessionInstance( request, entry );
                page = application.getPage( request, nMode, entry.getPlugin(  ) );
            }

            if ( page.isStandalone(  ) )
            {
                return page.getContent(  );
            }

            PageData data = new PageData(  );

            data.setContent( page.getContent(  ) );
            data.setName( page.getTitle(  ) );

            // set the page path. Done by adding the extra-path information to the pathLabel.
            String strXml = page.getXmlExtendedPathLabel(  );

            if ( strXml == null )
            {
                data.setPagePath( PortalService.getXPagePathContent( page.getPathLabel(  ), 0, request ) );
            }
            else
            {
                data.setPagePath( PortalService.getXPagePathContent( page.getPathLabel(  ), 0, strXml, request ) );
            }

            return PortalService.buildPageContent( data, nMode, request );
        }
        else
        {
            AppLogService.error( "The specified Xpage '" + strName +
                "' cannot be retrieved. Check installation of your Xpage application." );
            SiteMessageService.setMessage( request, MESSAGE_ERROR_APP_BODY, SiteMessage.TYPE_ERROR );

            return null; // unreachable because SiteMessageService.setMessage throws
        }
    }

    /**
     * Gets Application entry by name
     * @param strName The application's name
     * @return The entry
     */
    public static XPageApplicationEntry getApplicationEntry( String strName )
    {
        return _mapApplications.get( strName );
    }

    /**
     * Gets applications list
     * @return A collection of applications
     */
    public static Collection<XPageApplicationEntry> getXPageApplicationsList(  )
    {
        return _mapApplications.values(  );
    }

    /**
     * Return an instance of the XPage attached to the current Http Session
     * @param request The HTTP request
     * @param entry The XPage entry
     * @return The XPage instance
     */
    private static XPageApplication getXPageSessionInstance( HttpServletRequest request, XPageApplicationEntry entry )
    {
        HttpSession session = request.getSession( true );
        String strAttribute = ATTRIBUTE_XPAGE + entry.getId(  );
        XPageApplication application = (XPageApplication) session.getAttribute( strAttribute );

        if ( application == null )
        {
            application = getApplicationInstance( entry );
            session.setAttribute( strAttribute, application );
            AppLogService.debug( "New XPage instance of " + entry.getClassName(  ) +
                " created and attached to session " + session );
        }

        return application;
    }

    /**
     * Get an XPage instance
     * @param entry The Xpage entry
     * @return An instance of a given XPage
     */
    public static XPageApplication getApplicationInstance( XPageApplicationEntry entry )
    {
        XPageApplication application = null;

        try
        {
            if ( entry.getClassName(  ) == null )
            {
                application = SpringContextService.getBean( entry.getPluginName(  ) + ".xpage." + entry.getId(  ) );
            }
            else
            {
                application = (XPageApplication) Class.forName( entry.getClassName(  ) ).newInstance(  );
            }
        }
        catch ( Exception e )
        {
            throw new AppException( "Error instantiating XPageApplication : " + entry.getId(  ) + " - " +
                e.getCause(  ), e );
        }

        return application;
    }
}
