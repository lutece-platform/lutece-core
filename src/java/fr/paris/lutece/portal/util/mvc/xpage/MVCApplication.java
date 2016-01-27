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
package fr.paris.lutece.portal.util.mvc.xpage;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.utils.MVCMessage;
import fr.paris.lutece.portal.util.mvc.utils.MVCMessageBox;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.ErrorMessage;
import fr.paris.lutece.util.bean.BeanUtil;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.log4j.Logger;

import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.ConstraintViolation;


/**
 * MVC XPage Application
 */
public abstract class MVCApplication implements XPageApplication
{
    private static final long serialVersionUID = 6093635383465830355L;
    private static final String MARK_ERRORS = "errors";
    private static final String MARK_INFOS = "infos";
    private static final String MARK_MESSAGE_BOX = "messageBox";
    private static final String URL_PORTAL = "Portal.jsp";
    private static final String PATH_PORTAL = "jsp/site/";
    private static final String VIEW_MESSAGEBOX = "messageBox";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_XML = "application/xml";
    private static Logger _logger = MVCUtils.getLogger(  );
    private List<ErrorMessage> _listErrors = new ArrayList<ErrorMessage>(  );
    private List<ErrorMessage> _listInfos = new ArrayList<ErrorMessage>(  );
    private MVCMessageBox _messageBox;
    private Controller _controller = getClass(  ).getAnnotation( Controller.class );

    /**
     * Returns the content of the page
     *
     * @param request The http request
     * @param nMode The current mode
     * @param plugin The plugin object
     * @return The XPage
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException
     *             Message displayed if an exception occurs
     * @throws UserNotSignedException if an authentication is required by a view
     */
    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
        throws SiteMessageException, UserNotSignedException
    {
        return processController( request );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Controller

    /**
     * XPage controller
     * @param request The HTTP request
     * @return The XPage
     * @throws UserNotSignedException if an authentication is required by a view
     * @throws SiteMessageException if a message is thrown by an action
     */
    private XPage processController( HttpServletRequest request )
        throws UserNotSignedException, SiteMessageException
    {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods( getClass(  ) );

        try
        {
            if ( isMessageBox( request ) )
            {
                return messageBox( request );
            }

            // Process views
            Method m = MVCUtils.findViewAnnotedMethod( request, methods );

            if ( m != null )
            {
                return (XPage) m.invoke( this, request );
            }

            // Process actions
            m = MVCUtils.findActionAnnotedMethod( request, methods );

            if ( m != null )
            {
                return (XPage) m.invoke( this, request );
            }

            // No view or action found so display the default view
            m = MVCUtils.findDefaultViewMethod( methods );

            return (XPage) m.invoke( this, request );
        }
        catch ( InvocationTargetException e )
        {
            if ( e.getTargetException(  ) instanceof UserNotSignedException )
            {
                throw (UserNotSignedException) e.getTargetException(  );
            }

            if ( e.getTargetException(  ) instanceof SiteMessageException )
            {
                throw (SiteMessageException) e.getTargetException(  );
            }

            throw new AppException( "MVC Error dispaching view and action ", e );
        }
        catch ( IllegalAccessException e )
        {
            throw new AppException( "MVC Error dispaching view and action ", e );
        }
    }

    /**
     * Returns the XPage name
     * @return The XPage name
     */
    protected String getXPageName(  )
    {
        return _controller.xpageName(  );
    }

    /**
     * Returns the default page title
     * @param locale The locale
     * @return The default page title
     */
    protected String getDefaultPageTitle( Locale locale )
    {
        if ( !_controller.pageTitleProperty(  ).equals( "" ) )
        {
            return AppPropertiesService.getProperty( _controller.pageTitleProperty(  ) );
        }
        else if ( !_controller.pageTitleI18nKey(  ).equals( "" ) )
        {
            return I18nService.getLocalizedString( _controller.pageTitleI18nKey(  ), locale );
        }

        return _controller.xpageName(  );
    }

    /**
     * Returns the default page path
     * @param locale The locale
     * @return The default pagepath
     */
    protected String getDefaultPagePath( Locale locale )
    {
        if ( !_controller.pagePathProperty(  ).equals( "" ) )
        {
            return AppPropertiesService.getProperty( _controller.pagePathProperty(  ) );
        }
        else if ( !_controller.pagePathI18nKey(  ).equals( "" ) )
        {
            return I18nService.getLocalizedString( _controller.pagePathI18nKey(  ), locale );
        }

        return _controller.xpageName(  );
    }

    ////////////////////////////////////////////////////////////////////////////
    // XPage utils

    /**
     * Returns a new XPage object with default values
     * @return An XPage Object
     */
    protected XPage getXPage(  )
    {
        XPage page = new XPage(  );

        page.setTitle( getDefaultPageTitle( LocaleService.getDefault(  ) ) );
        page.setPathLabel( getDefaultPagePath( LocaleService.getDefault(  ) ) );

        return page;
    }

    /**
     * Returns a new XPage object with default values and the content filled
     * by a template
     * @param strTemplate The template
     * @return An XPage Object
     */
    protected XPage getXPage( String strTemplate )
    {
        XPage page = getXPage(  );

        HtmlTemplate t = AppTemplateService.getTemplate( strTemplate );
        page.setContent( t.getHtml(  ) );

        return page;
    }

    /**
     * Returns a new XPage object with default values and the content filled
     * by a template using a default model and for a given locale
     * @param strTemplate The template
     * @param locale The locale
     * @return An XPage Object
     */
    protected XPage getXPage( String strTemplate, Locale locale )
    {
        return getXPage( strTemplate, locale, getModel(  ) );
    }

    /**
     * Returns a new XPage object with default values and the content filled
     * by a template using a given model and for a given locale
     * @param strTemplate The template
     * @param locale The locale
     * @param model The model
     *
     * @return An XPage Object
     */
    protected XPage getXPage( String strTemplate, Locale locale, Map<String, Object> model )
    {
        XPage page = getXPage(  );

        HtmlTemplate t = AppTemplateService.getTemplate( strTemplate, locale, model );
        page.setContent( t.getHtml(  ) );
        page.setTitle( getDefaultPageTitle( locale ) );
        page.setPathLabel( getDefaultPagePath( locale ) );

        return page;
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

    ////////////////////////////////////////////////////////////////////////////
    // Bean processing

    /**
     * Populate a bean using parameters in http request
     * @param bean bean to populate
     * @param request http request
     */
    protected void populate( Object bean, HttpServletRequest request )
    {
        BeanUtil.populate( bean, request );
    }

    /**
     * Validate a bean. If the validation failed, error messages of this
     * MVCApplication are updated.< br/>
     * This method should be used only if error messages of
     * constraints of the bean are NOT i18n Keys. If they are I18n keys, the
     * method {@link #validateBean(Object, Locale)} should be used instead.
     * @param <T> The bean class
     * @param bean The bean
     * @return true if validated otherwise false
     */
    protected <T> boolean validateBean( T bean )
    {
        Set<ConstraintViolation<T>> errors = BeanValidationUtil.validate( bean );

        if ( errors.isEmpty(  ) )
        {
            return true;
        }

        for ( ConstraintViolation<T> constraint : errors )
        {
            MVCMessage error = new MVCMessage(  );
            error.setMessage( constraint.getMessage(  ) );
            _listErrors.add( error );
        }

        return false;
    }

    /**
     * Validate a bean. If the validation failed, error messages of this
     * MVCApplication are updated.< br/>
     * This method should be used only if error messages of constraints of the
     * bean are i18n Keys. If they are not I18n keys, the method
     * {@link #validateBean(Object)} should be used instead.
     * @param <T> The bean class
     * @param bean The bean
     * @param locale The locale
     * @return true if validated otherwise false
     */
    protected <T> boolean validateBean( T bean, Locale locale )
    {
        Set<ConstraintViolation<T>> errors = BeanValidationUtil.validate( bean );

        if ( errors.isEmpty(  ) )
        {
            return true;
        }

        for ( ConstraintViolation<T> constraint : errors )
        {
            MVCMessage error = new MVCMessage(  );
            error.setMessage( I18nService.getLocalizedString( constraint.getMessage(  ), locale ) );
            _listErrors.add( error );
        }

        return false;
    }

    /**
     * Add an error message. The error message must NOT be an I18n key.
     * @param strMessage The message
     */
    protected void addError( String strMessage )
    {
        _listErrors.add( new MVCMessage( strMessage ) );
    }

    /**
     * Add an error message. The error message must be an I18n key.
     * @param strMessageKey The message
     * @param locale The locale to display the message in
     */
    protected void addError( String strMessageKey, Locale locale )
    {
        _listErrors.add( new MVCMessage( I18nService.getLocalizedString( strMessageKey, locale ) ) );
    }

    /**
     * Add an info message. The info message must NOT be an I18n key.
     * @param strMessage The message
     */
    protected void addInfo( String strMessage )
    {
        _listInfos.add( new MVCMessage( strMessage ) );
    }

    /**
     * Add an info message. The info message must be an I18n key.
     * @param strMessageKey The message key
     * @param locale The locale to display the message in
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

    ////////////////////////////////////////////////////////////////////////////
    // Redirect utils

    /**
     * Redirect to requested page
     *
     * @param request the http request
     * @param strTarget the targeted page
     * @return the page requested
     */
    protected XPage redirect( HttpServletRequest request, String strTarget )
    {
        HttpServletResponse response = LocalVariables.getResponse(  );

        try
        {
            _logger.debug( "Redirect :" + strTarget );
            response.sendRedirect( strTarget );
        }
        catch ( IOException e )
        {
            _logger.error( "Unable to redirect : " + strTarget + " : " + e.getMessage(  ), e );
        }

        return new XPage(  );
    }

    /**
     * Redirect to an url defined by given parameters
     * @param request The HTTP request
     * @param strView The View name
     * @param strParameter The additional parameter
     * @param nValue The additional parameter's value
     * @return The XPage redirected
     */
    protected XPage redirect( HttpServletRequest request, String strView, String strParameter, int nValue )
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
     * @return The XPage redirected
     */
    protected XPage redirect( HttpServletRequest request, String strView, String strParameter1, int nValue1,
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
    protected XPage redirect( HttpServletRequest request, String strView, Map<String, String> additionalParameters )
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
     * @return the page requested
     */
    protected XPage redirectView( HttpServletRequest request, String strView )
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
        UrlItem url = new UrlItem( URL_PORTAL );
        url.addParameter( MVCUtils.PARAMETER_PAGE, getXPageName(  ) );
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
        return PATH_PORTAL + getViewUrl( strView );
    }

    /**
     * Get Action URL
     * @param strAction The view name
     * @return The URL
     */
    protected String getActionUrl( String strAction )
    {
        UrlItem url = new UrlItem( URL_PORTAL );
        url.addParameter( MVCUtils.PARAMETER_PAGE, getXPageName(  ) );
        url.addParameter( MVCUtils.PARAMETER_ACTION, strAction );

        return url.getUrl(  );
    }

    /**
     * Get Action URL
     * @param strAction The view name
     * @return The URL
     */
    protected String getActionFullUrl( String strAction )
    {
        return PATH_PORTAL + getActionUrl( strAction );
    }

    /**
     * Initiates a file download
     * @param strData Data of the file to download
     * @param strFilename Name of file
     * @param strContentType content type to set to the response
     * @return The page requested
     */
    protected XPage download( String strData, String strFilename, String strContentType )
    {
        HttpServletResponse response = LocalVariables.getResponse(  );
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

        return new XPage(  );
    }

    /**
     * Initiates a download of a byte array
     * @param data Data to download
     * @param strFilename Name of the downloaded file
     * @param strContentType Content type to set to the response
     * @return The page requested
     */
    protected XPage download( byte[] data, String strFilename, String strContentType )
    {
        HttpServletResponse response = LocalVariables.getResponse(  );
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

        return new XPage(  );
    }

    /**
     * Return a response as JSON content
     * @param strJSON The JSON
     * @return An unused XPage
     */
    protected XPage responseJSON( String strJSON )
    {
        HttpServletResponse response = LocalVariables.getResponse(  );
        response.setContentType( CONTENT_TYPE_JSON );

        try
        {
            PrintWriter out = response.getWriter(  );
            out.print( strJSON );
            out.flush(  );
            out.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getStackTrace(  ), e );
        }

        return new XPage(  );
    }

    /**
     * Return a response as XML content
     * @param strXML The XML
     * @return An unused XPage
     */
    protected XPage responseXML( String strXML )
    {
        HttpServletResponse response = LocalVariables.getResponse(  );
        response.setContentType( CONTENT_TYPE_XML );

        try
        {
            PrintWriter out = response.getWriter(  );
            out.print( strXML );
            out.flush(  );
            out.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getStackTrace(  ), e );
        }

        return new XPage(  );
    }

    ////////////////////////////////////////////////////////////////////////////
    // MESSAGE BOX MANAGEMENT

    /**
     * Redirect to a Message Box page
     * @param request The HTTP request
     * @param messageBox The MessageBox infos
     * @return A redirect XPage
     */
    protected XPage redirectMessageBox( HttpServletRequest request, MVCMessageBox messageBox )
    {
        _messageBox = messageBox;

        return redirectView( request, VIEW_MESSAGEBOX );
    }

    /**
     * Check if a message box is asked for
     * @param request The HTTP request
     * @return If a message box is asked
     */
    private boolean isMessageBox( HttpServletRequest request )
    {
        String strView = request.getParameter( MVCUtils.PARAMETER_VIEW );

        return ( ( strView != null ) && ( strView.equals( VIEW_MESSAGEBOX ) ) );
    }

    /**
     * Default getLocale() implementation. Could be overriden
     * @param request The HTTP request
     * @return The Locale
     */
    protected Locale getLocale( HttpServletRequest request )
    {
        return request.getLocale(  );
    }

    /**
     * Display the Message BOX
     * @param request The HTTP request
     * @return The message box
     */
    private XPage messageBox( HttpServletRequest request )
    {
        _messageBox.localize( getLocale( request ) );

        Map<String, Object> model = getModel(  );
        model.put( MARK_MESSAGE_BOX, _messageBox );

        return getXPage( _messageBox.getTemplate(  ), getLocale( request ), model );
    }
}
