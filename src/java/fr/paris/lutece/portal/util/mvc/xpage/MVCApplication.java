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
package fr.paris.lutece.portal.util.mvc.xpage;

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

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;

import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.SecurityTokenHandler;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.utils.MVCMessage;
import fr.paris.lutece.portal.util.mvc.utils.MVCMessageBox;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.util.mvc.utils.ReflectionUtils;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.cdi.mvc.event.ControllerRedirectEvent;
import fr.paris.lutece.portal.web.cdi.mvc.event.EventDispatcher;
import fr.paris.lutece.portal.web.cdi.mvc.event.MvcEvent;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.ErrorMessage;
import fr.paris.lutece.util.bean.BeanUtil;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * MVC XPage Application
 */
public abstract class MVCApplication implements XPageApplication
{
    private static final long serialVersionUID = 6093635383465830355L;

    // markers
    private static final String MARK_ERRORS = "errors";
    private static final String MARK_INFOS = "infos";
    private static final String MARK_WARNINGS = "warnings";
    private static final String MARK_MESSAGE_BOX = "messageBox";

    // constants
    private static final String URL_PORTAL = "Portal.jsp";
    private static final String PATH_PORTAL = "jsp/site/";
    private static final String VIEW_MESSAGEBOX = "messageBox";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_XML = "application/xml";

    // instance vars
    private static Logger _logger = MVCUtils.getLogger( );
    private List<ErrorMessage> _listErrors = new ArrayList<>( );
    private List<ErrorMessage> _listInfos = new ArrayList<>( );
    private List<ErrorMessage> _listWarnings = new ArrayList<>( );
    private MVCMessageBox _messageBox;
    private Controller _controller = getClass( ).getAnnotation( Controller.class );
    @Inject
    private transient SecurityTokenHandler _securityTokenHandler;
    @Inject 
    private transient EventDispatcher _eventDispatcher;
    
    /**
     * Returns the content of the XPage based on the current HTTP request.
     * <p>
     * This method is the entry point for the Lutece MVCXPage system. It delegates
     * the request to the appropriate controller method (action or view) using
     * {@code processController}, which handles annotation-based dispatching.
     * </p>
     *
     * @param request the incoming {@link HttpServletRequest}
     * @param nMode the current portal mode (e.g., normal, admin)
     * @param plugin the plugin from which this XPage originates
     * @return the resolved {@link XPage} object to be rendered
     *
     * @throws SiteMessageException if a site message needs to be displayed
     *         instead of the standard page content
     * @throws UserNotSignedException if a view requires authentication and the
     *         user is not currently signed in
     */
    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin ) throws SiteMessageException, UserNotSignedException
    {
       	 return processController( request );
    }

    // //////////////////////////////////////////////////////////////////////////
    // Controller

    /**
     * Handles the execution of the appropriate controller method (view or action)
     * based on the incoming HTTP request and the annotations present in the class.
     * <p>
     * The flow of this method is as follows:
     * <ul>
     *   <li>Fires a {@code BeforeControllerEvent} to allow CDI observers to perform pre-processing</li>
     *   <li>If the request matches a message box context, it returns the message box page</li>
     *   <li>Attempts to resolve a view method based on the request and invokes it</li>
     *   <li>If no view is found, attempts to resolve an action method and invokes it</li>
     *   <li>If neither is found, falls back to the default view method</li>
     * </ul>
     * </p>
     *
     * <p>
     * Exceptions are unwrapped and rethrown to preserve the original cause,
     * including application-specific exceptions like {@code UserNotSignedException}
     * and {@code SiteMessageException}.
     * </p>
     *
     * @param request the current {@link HttpServletRequest}
     * @return the resulting {@link XPage} from the invoked controller method
     * @throws UserNotSignedException if the controller method requires authentication and the user is not signed in
     * @throws SiteMessageException if the controller method triggers a site message display
     * @throws AppException if any other exception occurs during method resolution or invocation
     */
    private XPage processController( HttpServletRequest request ) throws UserNotSignedException, SiteMessageException
    {
        Method [ ] methods = ReflectionUtils.getAllDeclaredMethods( getClass( ) );

        try
        {
            if ( isMessageBox( request ) )
            {
            	Method m= getMessageBoxMethod( methods );
            	getEventDispatcher().fireBeforeControllerEvent( m, false, MvcEvent.ControllerInvocationType.MESSAGE_BOX_VIEW );
                return messageBox( request );
            }
            // Process views
            Method m = MVCUtils.findViewAnnotedMethod( request, methods );
            if ( m != null )
            {
            	getEventDispatcher().fireBeforeControllerEvent( m, false, MvcEvent.ControllerInvocationType.VIEW );
                 return processView(  m,  request  );
            }

            // Process actions
            m = MVCUtils.findActionAnnotedMethod( request, methods );
            if ( m != null )
            {
            	getEventDispatcher().fireBeforeControllerEvent( m, false, MvcEvent.ControllerInvocationType.ACTION );
                return processAction( m, request );
            }

            // No view or action found so display the default view
            m = MVCUtils.findDefaultViewMethod( methods );
        	getEventDispatcher().fireBeforeControllerEvent( m, false, MvcEvent.ControllerInvocationType.DEFAULT_VIEW  );
            return processView( m,  request  );
        }
        catch( InvocationTargetException e )
        {
            if ( e.getTargetException( ) instanceof UserNotSignedException )
            {
                throw (UserNotSignedException) e.getTargetException( );
            }

            if ( e.getTargetException( ) instanceof SiteMessageException )
            {
                throw (SiteMessageException) e.getTargetException( );
            }

            if ( e.getTargetException( ) instanceof RuntimeException )
            {
                throw new AppException( "MVC Error dispaching view and action ", (RuntimeException) e.getTargetException( ) );
            }

            throw new AppException( "MVC Error dispaching view and action ", e );
        }
        catch( IllegalAccessException e )
        {
            throw new AppException( "MVC Error dispaching view and action ", e );
        }
    }
    /**
     * Processes the execution of a view method in the MVC lifecycle.
     * <p>
     * This method performs the following steps:
     * <ul>
     *   <li>Logs the access using the {@code AccessLogService}</li>
     *   <li>Performs security token handling (CSRF protection, etc.)</li>
     *   <li>Invokes the controller method that returns the view</li>
     *   <li>Fires the {@code AfterProcessViewEvent} once processing is complete</li>
     * </ul>
     * </p>
     *
     * <p>
     * Observers of the {@code AfterProcessViewEvent} can use it to perform any cleanup,
     * post-render logic, or additional decorations to the view.
     * </p>
     *
     * @param m the controller method to invoke
     * @param request the current {@link HttpServletRequest}
     * @return the resulting {@link XPage} object from the view method
     * @throws IllegalAccessException if the method is inaccessible
     * @throws IllegalArgumentException if the arguments are invalid
     * @throws InvocationTargetException if the invoked method throws an exception
     */
    private XPage processView( Method m, HttpServletRequest request  ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	try {
        	return (XPage) m.invoke( this, request );
        } finally {
        	getEventDispatcher( ).fireAfterProcessViewEvent();
        }
    }
    /**
     * Processes the execution of an action method in the MVC lifecycle.
     * <p>
     * This method is typically used for handling form submissions or state-changing
     * operations. It performs:
     * <ul>
     *   <li>Access logging for audit or monitoring purposes</li>
     *   <li>Security token validation (e.g., CSRF protection)</li>
     *   <li>Invocation of the controller method associated with the action</li>
     * </ul>
     * </p>
     *
     * @param m the controller method to invoke
     * @param request the current {@link HttpServletRequest}
     * @return the resulting {@link XPage} object from the action method
     * @throws IllegalAccessException if the method is inaccessible
     * @throws IllegalArgumentException if the arguments are invalid
     * @throws InvocationTargetException if the invoked method throws an exception
     */
    private XPage processAction( Method m, HttpServletRequest request  ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	
    	return (XPage) m.invoke( this, request );        
    }
    /**
     * Searches for the {@code messageBox} method within the given list of declared methods.
     * <p>
     * The method must be named {@code "messageBox"} and take exactly one parameter (typically
     * an {@link javax.servlet.http.HttpServletRequest}).
     * </p>
     * 
     * @param methods the array of {@link Method} objects to search through, typically from {@code getAllDeclaredMethods()}
     * @return the matching {@code messageBox} method, or {@code null} if not found
     */
    private Method getMessageBoxMethod( Method [ ] methods )
    {
        for ( Method m : methods )
        {
            if ( "messageBox".equals(m.getName( )) && m.getParameterCount() == 1 )
            {
                return m;
            }
        }

        _logger.error( "MVC controller - No messageBox method found" );

        return null;
    }
    /**
     * Returns the XPage name
     * 
     * @return The XPage name
     */
    protected String getXPageName( )
    {
        return _controller.xpageName( );
    }

    /**
     * Returns the default page title
     * 
     * @param locale
     *            The locale
     * @return The default page title
     */
    protected String getDefaultPageTitle( Locale locale )
    {
        if ( !_controller.pageTitleProperty( ).equals( "" ) )
        {
            return AppPropertiesService.getProperty( _controller.pageTitleProperty( ) );
        }
        else
            if ( !_controller.pageTitleI18nKey( ).equals( "" ) )
            {
                return I18nService.getLocalizedString( _controller.pageTitleI18nKey( ), locale );
            }

        return _controller.xpageName( );
    }

    /**
     * Returns the default page path
     * 
     * @param locale
     *            The locale
     * @return The default pagepath
     */
    protected String getDefaultPagePath( Locale locale )
    {
        if ( !_controller.pagePathProperty( ).equals( "" ) )
        {
            return AppPropertiesService.getProperty( _controller.pagePathProperty( ) );
        }
        else
            if ( !_controller.pagePathI18nKey( ).equals( "" ) )
            {
                return I18nService.getLocalizedString( _controller.pagePathI18nKey( ), locale );
            }

        return _controller.xpageName( );
    }

    // //////////////////////////////////////////////////////////////////////////
    // XPage utils

    /**
     * Returns a new XPage object with default values
     * 
     * @return An XPage Object
     */
    protected XPage getXPage( )
    {
        XPage page = new XPage( );

        page.setTitle( getDefaultPageTitle( LocaleService.getDefault( ) ) );
        page.setPathLabel( getDefaultPagePath( LocaleService.getDefault( ) ) );

        return page;
    }

    /**
     * Returns a new XPage object with default values and the content filled by a template
     * 
     * @param strTemplate
     *            The template
     * @return An XPage Object
     */
    protected XPage getXPage( String strTemplate )
    {
        XPage page = getXPage( );

        HtmlTemplate t = AppTemplateService.getTemplate( strTemplate );
        page.setContent( t.getHtml( ) );

        return page;
    }

    /**
     * Returns a new XPage object with default values and the content filled by a template using a default model and for a given locale
     * 
     * @param strTemplate
     *            The template
     * @param locale
     *            The locale
     * @return An XPage Object
     */
    protected XPage getXPage( String strTemplate, Locale locale )
    {
        return getXPage( strTemplate, locale, getModel( ) );
    }

    /**
     * Returns a new XPage object with default values and the content filled by a template using a given model and for a given locale
     * 
     * @param strTemplate
     *            The template
     * @param locale
     *            The locale
     * @param model
     *            The model
     *
     * @return An XPage Object
     */
    protected XPage getXPage( String strTemplate, Locale locale, Map<String, Object> model )
    {
        XPage page = getXPage( );

        HtmlTemplate t = AppTemplateService.getTemplate( strTemplate, locale, model );
        page.setContent( t.getHtml( ) );
        page.setTitle( getDefaultPageTitle( locale ) );
        page.setPathLabel( getDefaultPagePath( locale ) );

        return page;
    }

    /**
     * Get a model Object filled with default values
     * 
     * @return The model
     */
    protected Map<String, Object> getModel( )
    {
        Map<String, Object> model = new HashMap<>( );
        fillCommons( model );
        fillSecurityToken( model );
        
        return model;
    }
   

    // //////////////////////////////////////////////////////////////////////////
    // Bean processing

    /**
     * Populate a bean using parameters in http request
     * 
     * @param bean
     *            bean to populate
     * @param request
     *            http request
     */
    protected void populate( Object bean, HttpServletRequest request )
    {
        BeanUtil.populate( bean, request, null );
    }

    /**
     * Populate a bean using parameters in http request, with locale date format controls
     * 
     * @param bean
     *            bean to populate
     * @param request
     *            http request
     * @param locale
     *            the locale
     */
    protected void populate( Object bean, HttpServletRequest request, Locale locale )
    {
        BeanUtil.populate( bean, request, locale );
    }

    /**
     * Validate a bean. If the validation failed, error messages of this MVCApplication are updated.<br>
     * This method should be used only if error messages of constraints of the bean are NOT i18n Keys. If they are I18n keys, the method
     * {@link #validateBean(Object, Locale)} should be used instead.
     * 
     * @param <T>
     *            The bean class
     * @param bean
     *            The bean
     * @return true if validated otherwise false
     */
    protected <T> boolean validateBean( T bean )
    {
        Set<ConstraintViolation<T>> errors = BeanValidationUtil.validate( bean );

        if ( errors.isEmpty( ) )
        {
            return true;
        }

        for ( ConstraintViolation<T> constraint : errors )
        {
            MVCMessage error = new MVCMessage( );
            error.setMessage( constraint.getMessage( ) );
            _listErrors.add( error );
        }

        return false;
    }

    /**
     * Validate a bean. If the validation failed, error messages of this MVCApplication are updated.<br>
     * This method should be used only if error messages of constraints of the bean are i18n Keys. If they are not I18n keys, the method
     * {@link #validateBean(Object)} should be used instead.
     * 
     * @param <T>
     *            The bean class
     * @param bean
     *            The bean
     * @param locale
     *            The locale
     * @return true if validated otherwise false
     */
    protected <T> boolean validateBean( T bean, Locale locale )
    {
        Set<ConstraintViolation<T>> errors = BeanValidationUtil.validate( bean );

        if ( errors.isEmpty( ) )
        {
            return true;
        }

        for ( ConstraintViolation<T> constraint : errors )
        {
            MVCMessage error = new MVCMessage( );
            error.setMessage( I18nService.getLocalizedString( constraint.getMessage( ), locale ) );
            _listErrors.add( error );
        }

        return false;
    }

    /**
     * Add an error message. The error message must NOT be an I18n key.
     * 
     * @param strMessage
     *            The message
     */
    protected void addError( String strMessage )
    {
        _listErrors.add( new MVCMessage( strMessage ) );
    }
    
    /**
     * Add an error message. The error message must NOT be an I18n key.
     * 
     * @param strMessage
     *            The message
     * @param  strFieldName the field name           
     */
    protected void addError( String strMessage, String strFieldName )
    {
        _listErrors.add( new MVCMessage( strMessage,strFieldName ) );
    }

    /**
     * Add an error message. The error message must be an I18n key.
     * 
     * @param strMessageKey
     *            The message
     * @param locale
     *            The locale to display the message in
     */
    protected void addError( String strMessageKey, Locale locale )
    {
        _listErrors.add( new MVCMessage( I18nService.getLocalizedString( strMessageKey, locale ) ) );
    }

    
    /**
     * Add an warning message. The warning message must NOT be an I18n key.
     * 
     * @param strMessage
     *            The message
     */
    protected void addWarning( String strMessage )
    {
        _listWarnings.add( new MVCMessage( strMessage ) );
    }
    
    /**
     * Add an warning message. The warning message must NOT be an I18n key.
     * 
     * @param strMessage
     *            The message
     *            
     * @param  strFieldName the field name            
     *            
     */
    protected void addWarning( String strMessage, String strFieldName  )
    {
        _listWarnings.add( new MVCMessage( strMessage,strFieldName ) );
    }
    


    /**
     * Add an warning message. The warning message must be an I18n key.
     * 
     * @param strMessageKey
     *            The message
     * @param locale
     *            The locale to display the message in
     */
    protected void addWarning( String strMessageKey, Locale locale )
    {
        _listWarnings.add( new MVCMessage( I18nService.getLocalizedString( strMessageKey, locale ) ) );
    }

    /**
     * Add an info message. The info message must NOT be an I18n key.
     * 
     * @param strMessage
     *            The message
     */
    protected void addInfo( String strMessage )
    {
        _listInfos.add( new MVCMessage( strMessage ) );
    }
    
    /**
     * Add an info message. The info message must NOT be an I18n key.
     * 
     * @param strMessage
     *            The message
     * @param  strFieldName the field name             
     */
    protected void addInfo( String strMessage, String strFieldName )
    {
        _listInfos.add( new MVCMessage( strMessage,strFieldName )  );
    }

    /**
     * Add an info message. The info message must be an I18n key.
     * 
     * @param strMessageKey
     *            The message key
     * @param locale
     *            The locale to display the message in
     */
    protected void addInfo( String strMessageKey, Locale locale )
    {
        _listInfos.add( new MVCMessage( I18nService.getLocalizedString( strMessageKey, locale ) ) );
    }

    /**
     * Fill the model with commons objects used in templates
     * 
     * @param model
     *            The model
     */
    protected void fillCommons( Map<String, Object> model )
    {
        List<ErrorMessage> listErrors = new ArrayList<>( _listErrors );
        List<ErrorMessage> listInfos = new ArrayList<>( _listInfos );
        List<ErrorMessage> listWarnings = new ArrayList<>( _listWarnings );
        model.put( MARK_ERRORS, listErrors );
        model.put( MARK_INFOS, listInfos );
        model.put( MARK_WARNINGS, listWarnings );
        _listErrors.clear( );
        _listInfos.clear( );
        _listWarnings.clear( );
    }
    
    // //////////////////////////////////////////////////////////////////////////
    // Redirect utils

    /**
     * Redirect to requested page
     *
     * @param request
     *            the http request
     * @param strTarget
     *            the targeted page
     * @return the page requested
     */
    protected XPage redirect( HttpServletRequest request, String strTarget )
    {
        HttpServletResponse response = LocalVariables.getResponse( );
        try
        {
            _logger.debug( "Redirect :{}", strTarget );
            UrlItem url = new UrlItem( strTarget );
            ControllerRedirectEvent event= getEventDispatcher().fireControllerRedirectEvent( url );        
            String finalUrl = event.getLocation( ).toString();
            _logger.debug("Redirecting to final target: {}", finalUrl);
         
            response.sendRedirect( finalUrl );
        }
        catch( IOException e )
        {
            _logger.error( "Unable to redirect : {} : {}", strTarget, e.getMessage( ), e );
        }
        XPage xpage=new XPage();
        xpage.setSendRedirect(true);
        return xpage;
    }

    /**
     * Redirect to an url defined by given parameters
     * 
     * @param request
     *            The HTTP request
     * @param strView
     *            The View name
     * @param strParameter
     *            The additional parameter
     * @param nValue
     *            The additional parameter's value
     * @return The XPage redirected
     */
    protected XPage redirect( HttpServletRequest request, String strView, String strParameter, int nValue )
    {
        UrlItem url = new UrlItem( getViewUrl( strView ) );
        url.addParameter( strParameter, nValue );

        return redirect( request, url.getUrl( ) );
    }

    /**
     * Redirect to an url defined by given parameters
     * 
     * @param request
     *            The HTTP request
     * @param strView
     *            The View name
     * @param strParameter1
     *            The first additional parameter
     * @param nValue1
     *            The first additional parameter's value
     * @param strParameter2
     *            The second additionnal parameter
     * @param nValue2
     *            The second additionnal parameter's value
     * @return The XPage redirected
     */
    protected XPage redirect( HttpServletRequest request, String strView, String strParameter1, int nValue1, String strParameter2, int nValue2 )
    {
        UrlItem url = new UrlItem( getViewUrl( strView ) );
        url.addParameter( strParameter1, nValue1 );
        url.addParameter( strParameter2, nValue2 );

        return redirect( request, url.getUrl( ) );
    }

    /**
     * Redirect to an url defined by given parameters
     * 
     * @param request
     *            The HTTP request
     * @param strView
     *            The View name
     * @param additionalParameters
     *            A map containing parameters to add to the URL. Keys of the map are parameters name, and values are parameters values
     * @return The XPage redirected
     */
    protected XPage redirect( HttpServletRequest request, String strView, Map<String, String> additionalParameters )
    {
        UrlItem url = new UrlItem( getViewUrl( strView ) );

        if ( additionalParameters != null )
        {
            for ( Entry<String, String> entry : additionalParameters.entrySet( ) )
            {
                url.addParameter( entry.getKey( ), entry.getValue( ) );
            }
        }

        return redirect( request, url.getUrl( ) );
    }

    /**
     * Redirect to requested view
     *
     * @param request
     *            the http request
     * @param strView
     *            the targeted view
     * @return the page requested
     */
    protected XPage redirectView( HttpServletRequest request, String strView )
    {
        return redirect( request, getViewUrl( strView ) );
    }

    /**
     * Get a View URL
     * 
     * @param strView
     *            The view name
     * @return The URL
     */
    protected String getViewUrl( String strView )
    {
        UrlItem url = new UrlItem( URL_PORTAL );
        url.addParameter( MVCUtils.PARAMETER_PAGE, getXPageName( ) );
        url.addParameter( MVCUtils.PARAMETER_VIEW, strView );

        return url.getUrl( );
    }
   
    /**
     * Gets the view URL with the JSP path
     * 
     * @param strView
     *            The view
     * @return The URL
     */
    protected String getViewFullUrl( String strView )
    {
        return PATH_PORTAL + getViewUrl( strView );
    }

    /**
     * Get Action URL
     * 
     * @param strAction
     *            The view name
     * @return The URL
     */
    protected String getActionUrl( String strAction )
    {
        UrlItem url = new UrlItem( URL_PORTAL );
        url.addParameter( MVCUtils.PARAMETER_PAGE, getXPageName( ) );
        url.addParameter( MVCUtils.PARAMETER_ACTION, strAction );

        return url.getUrl( );
    }

    /**
     * Get Action URL
     * 
     * @param strAction
     *            The view name
     * @return The URL
     */
    protected String getActionFullUrl( String strAction )
    {
        return PATH_PORTAL + getActionUrl( strAction );
    }

    /**
     * Initiates a file download
     * 
     * @param strData
     *            Data of the file to download
     * @param strFilename
     *            Name of file
     * @param strContentType
     *            content type to set to the response
     * @return The page requested
     */
    protected XPage download( String strData, String strFilename, String strContentType )
    {
        HttpServletResponse response = LocalVariables.getResponse( );
        PrintWriter out = null;
        response.setHeader( "Content-Disposition", "attachment; filename=\"" + strFilename + "\";" );
        MVCUtils.addDownloadHeaderToResponse( response, strFilename, strContentType );

        try
        {
            out = response.getWriter( );
            out.print( strData );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getStackTrace( ), e );
        }
        finally
        {
            if ( out != null )
            {
                out.close( );
            }
        }

        return new XPage( );
    }

    /**
     * Initiates a download of a byte array
     * 
     * @param data
     *            Data to download
     * @param strFilename
     *            Name of the downloaded file
     * @param strContentType
     *            Content type to set to the response
     * @return The page requested
     */
    protected XPage download( byte [ ] data, String strFilename, String strContentType )
    {
        HttpServletResponse response = LocalVariables.getResponse( );
        OutputStream os;
        MVCUtils.addDownloadHeaderToResponse( response, strFilename, strContentType );

        try
        {
            os = response.getOutputStream( );
            os.write( data );
            os.close( );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getStackTrace( ), e );
        }

        return new XPage( );
    }

    /**
     * Return a response as JSON content
     * 
     * @param strJSON
     *            The JSON
     * @return An unused XPage
     */
    protected XPage responseJSON( String strJSON )
    {
        HttpServletResponse response = LocalVariables.getResponse( );
        response.setContentType( CONTENT_TYPE_JSON );

        try
        {
            PrintWriter out = response.getWriter( );
            out.print( strJSON );
            out.flush( );
            out.close( );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getStackTrace( ), e );
        }

        return new XPage( );
    }

    /**
     * Return a response as XML content
     * 
     * @param strXML
     *            The XML
     * @return An unused XPage
     */
    protected XPage responseXML( String strXML )
    {
        HttpServletResponse response = LocalVariables.getResponse( );
        response.setContentType( CONTENT_TYPE_XML );

        try
        {
            PrintWriter out = response.getWriter( );
            out.print( strXML );
            out.flush( );
            out.close( );
        }
        catch( IOException e )
        {
            AppLogService.error( e.getStackTrace( ), e );
        }

        return new XPage( );
    }

    // //////////////////////////////////////////////////////////////////////////
    // MESSAGE BOX MANAGEMENT

    /**
     * Redirect to a Message Box page
     * 
     * @param request
     *            The HTTP request
     * @param messageBox
     *            The MessageBox infos
     * @return A redirect XPage
     */
    protected XPage redirectMessageBox( HttpServletRequest request, MVCMessageBox messageBox )
    {
        _messageBox = messageBox;

        return redirectView( request, VIEW_MESSAGEBOX );
    }

    /**
     * Check if a message box is asked for
     * 
     * @param request
     *            The HTTP request
     * @return If a message box is asked
     */
    private boolean isMessageBox( HttpServletRequest request )
    {
        String strView = request.getParameter( MVCUtils.PARAMETER_VIEW );

        return ( ( strView != null ) && ( strView.equals( VIEW_MESSAGEBOX ) ) );
    }

    /**
     * Default getLocale() implementation. Could be overriden
     * 
     * @param request
     *            The HTTP request
     * @return The Locale
     */
    protected Locale getLocale( HttpServletRequest request )
    {
        return LocaleService.getContextUserLocale( request );
    }

    /**
     * Display the Message BOX
     * 
     * @param request
     *            The HTTP request
     * @return The message box
     */
    private XPage messageBox( HttpServletRequest request)
    {
	    try {
	        _messageBox.localize( getLocale( request ) );
	        Map<String, Object> model = getModel( );
	        model.put( MARK_MESSAGE_BOX, _messageBox );
	        return getXPage( _messageBox.getTemplate( ), getLocale( request ), model );
	    } finally {
	    	getEventDispatcher( ).fireAfterProcessViewEvent();
	    }
    }

    /**
     * get the registred User
     * 
     * @param request
     * @return the lutece user if registred, null otherwise
     */
    protected LuteceUser getRegistredUser( HttpServletRequest request )
    {
        // get authenticated user if authentication is enable
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser luteceUser = SecurityService.getInstance( ).getRegisteredUser( request );
            if ( luteceUser != null )
            {
                return luteceUser;
            }
        }

        return null;
    }

    /**
     * Returns the SecurityTokenHandler instance by privileging direct injection. Used during complete transition do CDI XPages.
     * 
     * @return the SecurityTokenHandler instance
     */
    private SecurityTokenHandler getSecurityTokenHandler( )
    {
        return null != _securityTokenHandler ? _securityTokenHandler : CDI.current( ).select( SecurityTokenHandler.class ).get( );
    }
    /**
     * Returns the EventDispatcher instance by privileging direct injection. Used during complete transition do CDI XPages.
     * 
     * @return the EventDispatcher instance
     */
    private EventDispatcher getEventDispatcher( )
    {
        return null != _eventDispatcher ? _eventDispatcher : CDI.current( ).select( EventDispatcher.class ).get( );
    }
    
    

    /**
     * Fill the model with security token
     * 
     * @param model
     *            The model
     */
    private void fillSecurityToken( Map<String, Object> model )
    {
        if ( null != LocalVariables.getRequest( ) )
        {
            String strToken = getSecurityTokenHandler( ).resolveTokenValue( LocalVariables.getRequest( ) );
            if ( null != strToken )
            {
                model.put( SecurityTokenHandler.MARK_CSRF_TOKEN, strToken );
            }
        }
    }
}
