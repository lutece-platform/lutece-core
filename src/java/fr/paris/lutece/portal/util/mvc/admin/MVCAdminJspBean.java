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
package fr.paris.lutece.portal.util.mvc.admin;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.utils.MVCMessage;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.ErrorMessage;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.log4j.Logger;

import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * MVC Admin JspBean implementation let use MVC model to develop admin feature.
 */
public abstract class MVCAdminJspBean extends PluginAdminPageJspBean implements Serializable
{
    private static final String MARK_ERRORS = "errors";
    private static final String MARK_INFOS = "infos";
    private static Logger _logger = MVCUtils.getLogger(  );
    private List<ErrorMessage> _listErrors = new ArrayList<ErrorMessage>(  );
    private List<ErrorMessage> _listInfos = new ArrayList<ErrorMessage>(  );
    private Controller _controller = getClass(  ).getAnnotation( Controller.class );
    private HttpServletResponse _response;

    /**
     * Process request as a controller
     * @param request The HTTP request
     * @param response The HTTP response
     * @return The page content
     * @throws AccessDeniedException If the user's has no right
     */
    public String processController( HttpServletRequest request, HttpServletResponse response )
        throws AccessDeniedException
    {
        _response = response;
        init( request, _controller.right(  ) );

        Method[] methods = ReflectionUtils.getAllDeclaredMethods( getClass(  ) );

        try
        {
            // Process views
            Method m = MVCUtils.findViewAnnotedMethod( request, methods );

            if ( m != null )
            {
                return (String) m.invoke( this, request );
            }

            // Process actions
            m = MVCUtils.findActionAnnotedMethod( request, methods );

            if ( m != null )
            {
                return (String) m.invoke( this, request );
            }

            // No view or action found so display the default view
            m = MVCUtils.findDefaultViewMethod( methods );

            return (String) m.invoke( this, request );
        }
        catch ( InvocationTargetException e )
        {
            if ( e.getTargetException(  ) instanceof AccessDeniedException )
            {
                throw (AccessDeniedException) e.getTargetException(  );
            }

            throw new AppException( "MVC Error dispaching view and action ", e );
        }
        catch ( IllegalAccessException e )
        {
            throw new AppException( "MVC Error dispaching view and action ", e );
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Page utils 

    /**
     * Add an error message
     * @param strMessage The message
     */
    protected void addError( String strMessage )
    {
        _listErrors.add( new MVCMessage( strMessage ) );
    }

    /**
     * Add an error message
     * @param strMessageKey The message
     * @param locale The locale
     */
    protected void addError( String strMessageKey, Locale locale )
    {
        _listErrors.add( new MVCMessage( I18nService.getLocalizedString( strMessageKey, locale ) ) );
    }

    /**
     * Add an info message
     * @param strMessage The message
     */
    protected void addInfo( String strMessage )
    {
        _listInfos.add( new MVCMessage( strMessage ) );
    }

    /**
     * Add an info message
     * @param strMessageKey The message key
     * @param locale The locale
     */
    protected void addInfo( String strMessageKey, Locale locale )
    {
        _listInfos.add( new MVCMessage( I18nService.getLocalizedString( strMessageKey, locale ) ) );
    }

    /**
    * Fill the model with commons objects used in templates
    * @param model The model
    */
    protected void fillCommons( Map<String, Object> model )
    {
        List<ErrorMessage> listErrors = new ArrayList<ErrorMessage>( _listErrors );
        List<ErrorMessage> listInfos = new ArrayList<ErrorMessage>( _listInfos );
        model.put( MARK_ERRORS, listErrors );
        model.put( MARK_INFOS, listInfos );
        _listErrors.clear(  );
        _listInfos.clear(  );
    }

    /**
     * Get a model Object filled with default values
     * @return The model
     */
    protected Map<String, Object> getModel(  )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        fillCommons( model );

        return model;
    }

    /**
     * Return the page content
     * @param strTemplate The template
     * @return The page
     */
    protected String getPage( String strTemplate )
    {
        String strPageTitleProperty = _controller.pageTitleProperty(  );

        return getPage( strPageTitleProperty, strTemplate, getModel(  ) );
    }

    /**
     * Return the page content
     * @param strPageTitleProperty The page title property
     * @param strTemplate The template
     * @return The page
     */
    protected String getPage( String strPageTitleProperty, String strTemplate )
    {
        return getPage( strPageTitleProperty, strTemplate, getModel(  ) );
    }

    /**
     * Return the page content
     * @param strPageTitleProperty The page title property
     * @param strTemplate The template
     * @param model The model
     * @return The page
     */
    protected String getPage( String strPageTitleProperty, String strTemplate, Map<String, Object> model )
    {
        setPageTitleProperty( strPageTitleProperty );

        HtmlTemplate template = AppTemplateService.getTemplate( strTemplate, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Validate a bean
     * @param <T> The bean class
     * @param bean The bean
     * @param strPrefix The prefix
     * @return true if validated otherwise false
     */
    protected <T> boolean validateBean( T bean, String strPrefix )
    {
        List<ValidationError> errors = validate( bean, strPrefix );

        if ( errors.isEmpty(  ) )
        {
            return true;
        }

        for ( ValidationError errorValidation : errors )
        {
            MVCMessage error = new MVCMessage(  );
            error.setMessage( errorValidation.getMessage(  ) );
            _listErrors.add( error );
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Redirect utils

    /**
     * Return the JSP name used as controller
     * @return The JSP name
     */
    protected String getControllerJsp(  )
    {
        return _controller.controllerJsp(  );
    }

    /**
     * Return the path of the JSP used as controller
     * @return The controller path
     */
    protected String getControllerPath(  )
    {
        return _controller.controllerPath(  );
    }

    /**
     * Redirect to requested page
     *
     * @param request the http request
     * @param strTarget the targeted page
     * @return null. The page should be redirected
     */
    protected String redirect( HttpServletRequest request, String strTarget )
    {
        try
        {
            _logger.debug( "Redirect :" + strTarget );
            _response.sendRedirect( strTarget );
        }
        catch ( IOException e )
        {
            _logger.error( "Unable to redirect : " + strTarget + " : " + e.getMessage(  ), e );
        }

        return null;
    }

    /**
     * Redirect to an url defined by given parameters
     * @param request The HTTP request
     * @param strView The View name
     * @param strParameter The additional parameter
     * @param nValue The additional parameter's value
     * @return The redirection result
     */
    protected String redirect( HttpServletRequest request, String strView, String strParameter, int nValue )
    {
        UrlItem url = new UrlItem( getViewUrl( strView ) );
        url.addParameter( strParameter, nValue );

        return redirect( request, url.getUrl(  ) );
    }

    /**
     * Redirect to an url defined by given parameters
     * @param request The HTTP request
     * @param strView The View name
     * @param strParameter1 The first additional parameter
     * @param nValue1 The first additional parameter's value
     * @param strParameter2 The second additionnal parameter
     * @param nValue2 The second additionnal parameter's value
     * @return The redirection result
     */
    protected String redirect( HttpServletRequest request, String strView, String strParameter1, int nValue1,
        String strParameter2, int nValue2 )
    {
        UrlItem url = new UrlItem( getViewUrl( strView ) );
        url.addParameter( strParameter1, nValue1 );
        url.addParameter( strParameter2, nValue2 );

        return redirect( request, url.getUrl(  ) );
    }

    /**
     * Redirect to an url defined by given parameters
     * @param request The HTTP request
     * @param strView The View name
     * @param additionalParameters A map containing parameters to add to the
     *            URL. Keys of the map are parameters name, and values are
     *            parameters values
     * @return The XPage redirected
     */
    protected String redirect( HttpServletRequest request, String strView, Map<String, String> additionalParameters )
    {
        UrlItem url = new UrlItem( getViewUrl( strView ) );

        if ( additionalParameters != null )
        {
            for ( Entry<String, String> entry : additionalParameters.entrySet(  ) )
            {
                url.addParameter( entry.getKey(  ), entry.getValue(  ) );
            }
        }

        return redirect( request, url.getUrl(  ) );
    }

    /**
     * Redirect to requested view
     *
     * @param request the http request
     * @param strView the targeted view
     * @return The redirection result
     */
    protected String redirectView( HttpServletRequest request, String strView )
    {
        return redirect( request, getViewUrl( strView ) );
    }

    /**
     * Get a View URL
     * @param strView The view name
     * @return The URL
     */
    protected String getViewUrl( String strView )
    {
        UrlItem url = new UrlItem( getControllerJsp(  ) );
        url.addParameter( MVCUtils.PARAMETER_VIEW, strView );

        return url.getUrl(  );
    }

    /**
     * Gets the view URL with the JSP path
     * @param strView The view
     * @return The URL
     */
    protected String getViewFullUrl( String strView )
    {
        return getControllerPath(  ) + getViewUrl( strView );
    }

    /**
     * Get Action URL
     * @param strAction The view name
     * @return The URL
     */
    protected String getActionUrl( String strAction )
    {
        UrlItem url = new UrlItem( getControllerPath(  ) + getControllerJsp(  ) );
        url.addParameter( MVCUtils.PARAMETER_ACTION, strAction );

        return url.getUrl(  );
    }

    /**
     * Initiates a file download
     * @param strData Data of the file to download
     * @param strFilename Name of file
     * @param strContentType content type to set to the response
     */
    protected void download( String strData, String strFilename, String strContentType )
    {
        HttpServletResponse response = _response;
        PrintWriter out = null;
        response.setHeader( "Content-Disposition", "attachment; filename=\"" + strFilename + "\";" );
        MVCUtils.addDownloadHeaderToResponse( response, strFilename, strContentType );

        try
        {
            out = response.getWriter(  );
            out.print( strData );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getStackTrace(  ), e );
        }
        finally
        {
            if ( out != null )
            {
                out.close(  );
            }
        }
    }

    /**
     * Initiates a download of a byte array
     * @param data Data to download
     * @param strFilename Name of the downloaded file
     * @param strContentType Content type to set to the response
     */
    protected void download( byte[] data, String strFilename, String strContentType )
    {
        HttpServletResponse response = _response;
        OutputStream os;
        MVCUtils.addDownloadHeaderToResponse( response, strFilename, strContentType );

        try
        {
            os = response.getOutputStream(  );
            os.write( data );
            os.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getStackTrace(  ), e );
        }
    }
}
