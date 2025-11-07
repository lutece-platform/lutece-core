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
package fr.paris.lutece.portal.util.mvc.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.SecurityTokenHandler;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.binding.ParamError;
import fr.paris.lutece.portal.util.mvc.binding.ServletParameterBinder;
import fr.paris.lutece.portal.util.mvc.binding.validate.ValidationService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.ResponseBody;
import fr.paris.lutece.portal.util.mvc.utils.MVCMessage;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.util.mvc.utils.ReflectionUtils;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.cdi.mvc.event.ControllerRedirectEvent;
import fr.paris.lutece.portal.web.cdi.mvc.event.EventDispatcher;
import fr.paris.lutece.portal.web.cdi.mvc.event.MvcEvent;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.ErrorMessage;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.json.JsonUtil;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.xml.XmlMarshaller;
import fr.paris.lutece.util.xml.XmlUtil;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

/**
 * MVC Admin JspBean implementation let use MVC model to develop admin feature.
 */
public abstract class MVCAdminJspBean extends PluginAdminPageJspBean
{
    private static final long serialVersionUID = 278165302545398831L;

    // instance vars
    private static Logger _logger = MVCUtils.getLogger( );
    private Controller _controller = getClass( ).getAnnotation( Controller.class );
    private transient HttpServletResponse _response;
    @Inject
    private SecurityTokenHandler _securityTokenHandler;
    @Inject 
    private EventDispatcher _eventDispatcher;
    @Inject
    private ServletParameterBinder _servletParameterBinder;
    @Inject
    private ValidationService _validationService;
    @Inject 
    private Models models;
    
    /**
     * Dispatches the HTTP request to the appropriate controller method based on annotations,
     * and returns the path to the view to render (typically a JSP).
     * <p>
     * This method is part of the MVC controller logic for administrative back-office. It performs:
     * <ul>
     *   <li>Initialization and permission checks</li>
     *   <li>Registration of actions for security token validation</li>
     *   <li>Invocation of view or action methods based on request parameters</li>
     *   <li>Fallback to the default view if none is explicitly matched</li>
     * </ul>
     * </p>
     *
     * <p>
     * A {@code BeforeControllerEvent} is fired at the beginning of execution, allowing
     * CDI observers to modify behavior or intercept early.
     * </p>
     *
     * @param request the {@link HttpServletRequest} to process
     * @param response the {@link HttpServletResponse} associated with the request
     * @return a {@link String} representing the path to the view (e.g., JSP page)
     *
     * @throws AccessDeniedException if the invoked method explicitly denies access
     * @throws AppException if any reflection-related errors occur during method invocation
     */
    public String processController( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException
    {
        _response = response;
        init( request, _controller.right( ) );

        Method [ ] methods = ReflectionUtils.getAllDeclaredMethods( getClass( ) );

        if ( _controller.securityTokenEnabled( ) )
        {
            getSecurityTokenHandler( ).registerActions( _controller.controllerPath( ) + _controller.controllerJsp( ), methods );
        }

        try
        {
            // Process views
            Method m = MVCUtils.findViewAnnotedMethod( request, methods );

            if ( m != null )
            {
            	getEventDispatcher().fireBeforeControllerEvent( m, true, MvcEvent.ControllerInvocationType.VIEW, _controller.securityTokenEnabled( )); 
            	return (String) processView(m, request);
            }

            // Process actions
            m = MVCUtils.findActionAnnotedMethod(request, methods);

            if ( m != null )
            {
            	getEventDispatcher().fireBeforeControllerEvent( m, true, MvcEvent.ControllerInvocationType.ACTION, _controller.securityTokenEnabled( ));
            	// Check for @ResponseBody annotation
                if (m.isAnnotationPresent(ResponseBody.class)) {
                    processResponseBody(m, request);
                    return null; // No page needed
                }
            	return (String) processAction(m, request);
            }

            // No view or action found so display the default view
            m = MVCUtils.findDefaultViewMethod( methods );
        	getEventDispatcher().fireBeforeControllerEvent( m, true, MvcEvent.ControllerInvocationType.DEFAULT_VIEW, _controller.securityTokenEnabled( ));
            return (String) processView(m, request);
        }
        catch( InvocationTargetException e )
        {
            if ( e.getTargetException( ) instanceof AccessDeniedException )
            {
                throw (AccessDeniedException) e.getTargetException( );
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
    private String processView( Method m, HttpServletRequest request  ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
    	try {
    		Object[] args = getServletParameterBinder( ).bindParameters( request, m);
    		getValidationService( ).validateParameters(this, m, args);
        	return (String) m.invoke( this, args );
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
    private String processAction( Method m, HttpServletRequest request  ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
    	Object[] args = getServletParameterBinder( ).bindParameters( request, m);
    	getValidationService( ).validateParameters(this, m, args);
		return (String) m.invoke( this, args ); 
    }
    
    /**
     * Processes the response body of a controller method annotated with @ResponseBody.
     * <p>
     * This method:
     * <ul>
     *     <li>Binds HTTP request parameters to method arguments.</li>
     *     <li>Validates the method arguments.</li>
     *     <li>Invokes the controller method via reflection.</li>
     *     <li>Serializes the return value to JSON or XML based on the "Accept" header.</li>
     *     <li>Writes the serialized content to the HTTP response.</li>
     * </ul>
     *
     * @param m       the controller method to invoke
     * @param request the current HTTP request
     * @throws IllegalAccessException    if the method cannot be accessed
     * @throws IllegalArgumentException  if the method arguments are invalid
     * @throws InvocationTargetException if the underlying method throws an exception
     */
     private void processResponseBody(Method m, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
         HttpServletResponse response = _response;
         Object[] args = getServletParameterBinder().bindParameters(request, m);
         getValidationService().validateParameters(this, m, args);

         Object result = m.invoke(this, args);

         try {
         	String acceptHeader = request.getHeader("Accept");
             boolean isXml = acceptHeader != null && acceptHeader.contains(XmlUtil.CONTENT_TYPE_XML);
             response.setContentType(isXml ? XmlUtil.CONTENT_TYPE_XML : JsonUtil.CONTENT_TYPE_JSON);

             PrintWriter out = response.getWriter();

             if (result instanceof String || result instanceof Number || result instanceof Boolean || result instanceof Character) {
                 // Write primitive types and their wrappers directly
                 out.print(result.toString());
             } else {
             	if (isXml) {
                     // Serialize to XML
                     String xmlResult = XmlMarshaller.serialize(result);
                     out.print(xmlResult);
                 } else {
 	                // Serialize non-primitive objects using Jackson
 	                String jsonResult = JsonUtil.serialize(result);
 	                out.print(jsonResult);
                 }
             }

             out.flush();
             out.close( );
         } catch (IOException e) {
             AppLogService.error("Error writing @ResponseBody content to response", e);
         }
     }
    // //////////////////////////////////////////////////////////////////////////
    // Page utils
     /**
      * Adds a message of a given type (error, warning, info) to the model.
      *
      * @param key      the model key (e.g. {@code MVCUtils.MARK_ERRORS})
      * @param message  the message text
      */
     private void addMessage(String key, String message) {
         Models models = getModels();
         Set<String> messages = models.get(key, Set.class);
         if (messages == null) {
             messages = new LinkedHashSet<>();
         }
         messages.add(message);
         models.put(key, messages);
     }
     /**
      * Adds a ParamError to the model.
      *
      * @param paramError  the paramError
      */
     private void addError(ParamError paramError) {
         Models models = getModels();
         Set<ParamError> messages = models.get(MVCUtils.MARK_ERRORS, Set.class);
         if (messages == null) {
             messages = new LinkedHashSet<>();
         }
         messages.add(paramError);
         models.put(MVCUtils.MARK_ERRORS, messages);
     }

     /**
      * Adds a localized message of a given type (error, warning, info) to the model.
      *
      * @param key         the model key (e.g. {@code MVCUtils.MARK_ERRORS})
      * @param messageKey  the i18n key
      * @param locale      the locale to use for translation
      */
     private void addMessage(String key, String messageKey, Locale locale) {
         addMessage(key, I18nService.getLocalizedString(messageKey, locale));
     }

    /**
     * Add an error message
     * 
     * @param strMessage
     *            The message
     */
    protected void addError( String strMessage )
    {
    	addError( new MVCMessage( strMessage ) );
    }

    /**
     * Add an error message
     * 
     * @param strMessageKey
     *            The message
     * @param locale
     *            The locale
     */
    protected void addError( String strMessageKey, Locale locale )
    {
    	addError( new MVCMessage( I18nService.getLocalizedString( strMessageKey, locale ) ) );
    }

    /**
     * Add a warning message
     * 
     * @param strMessage
     *            The message
     */
    protected void addWarning( String strMessage )
    {
        addMessage(MVCUtils.MARK_WARNINGS, strMessage);
    }

    /**
     * Add an warning message
     * 
     * @param strMessageKey
     *            The message
     * @param locale
     *            The locale
     */
    protected void addWarning( String strMessageKey, Locale locale )
    {
        addMessage(MVCUtils.MARK_WARNINGS, strMessageKey, locale);
    }

    /**
     * Add an info message
     * 
     * @param strMessage
     *            The message
     */
    protected void addInfo( String strMessage )
    {
        addMessage(MVCUtils.MARK_INFOS, strMessage);
    }

    /**
     * Add an info message
     * 
     * @param strMessageKey
     *            The message key
     * @param locale
     *            The locale
     */
    protected void addInfo( String strMessageKey, Locale locale )
    {
        addMessage(MVCUtils.MARK_INFOS, strMessageKey, locale);
    }

    /**
     * Fill the model with commons objects used in templates
     
     * @param model
     *            The model
     * <p>
	 * This method is deprecated. It is recommended to use <b>dependency injection</b> 
	 * to obtain an instance of {@link Models} directly, instead of calling this method.
	 * </p>
	 *
	 * @deprecated Use {@link Models} injection directly to access or populate model data.
     */
    @Deprecated
    protected void fillCommons( Map<String, Object> model )
    {
    	Models models= getModels();
        model.putAll(models.asMap());  
    }

    /**
     * Get a model Object filled with default values
     * <p>
	 * This method is deprecated. It is recommended to use <b>dependency injection</b> 
	 * to obtain an instance of {@link Models} directly, instead of calling this method.
	 * </p>
	 *
	 * @deprecated Use {@link Models} injection directly to access or populate model data.
	 * @return the model as a {@link Map} containing default values.
	 */
    @Deprecated
    protected Map<String, Object> getModel( )
    {
    	Models model= getModels();
        fillSecurityToken( model );        
        return new HashMap<>(model.asMap());
    }

    /**
     * Return the page content
     * 
     * @param strTemplate
     *            The template
     * @return The page
     */
    protected String getPage( String strTemplate )
    {
        String strPageTitleProperty = _controller.pageTitleProperty( );

        return getPage( strPageTitleProperty, strTemplate, getModelsWithSecurityToken( ) );
    }

    /**
     * Return the page content
     * 
     * @param strPageTitleProperty
     *            The page title property
     * @param strTemplate
     *            The template
     * @return The page
     */
    protected String getPage( String strPageTitleProperty, String strTemplate )
    {
        return getPage( strPageTitleProperty, strTemplate, getModelsWithSecurityToken( ) );
    }

    /**
     * Return the page content
     * 
     * @param strPageTitleProperty
     *            The page title property
     * @param strTemplate
     *            The template
     * @param model
     *            The model
     * @return The page
     */
    protected String getPage( String strPageTitleProperty, String strTemplate, Map<String, Object> model )
    {
        setPageTitleProperty( strPageTitleProperty );

        HtmlTemplate template = AppTemplateService.getTemplate( strTemplate, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }
    /**
	 * Return the page content
	 * 
	 * @param strPageTitleProperty
	 *            The page title property
	 * @param strTemplate
	 *            The template
	 * @param model
	 *            The model
	 * @return The page
	 */
    protected String getPage( String strPageTitleProperty, String strTemplate, Models model )
    {
        setPageTitleProperty( strPageTitleProperty );

        HtmlTemplate template = AppTemplateService.getTemplate( strTemplate, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Validate a bean
     * 
     * @param <T>
     *            The bean class
     * @param bean
     *            The bean
     * @param strPrefix
     *            The prefix
     * @return true if validated otherwise false
     */
    protected <T> boolean validateBean( T bean, String strPrefix )
    {
        List<ValidationError> errors = validate( bean, strPrefix );

        if ( errors.isEmpty( ) )
        {
            return true;
        }

        for ( ValidationError errorValidation : errors )
        {
            MVCMessage error = new MVCMessage( );
            error.setMessage( errorValidation.getMessage( ) );
            error.setFieldName( errorValidation.getFieldName( ) );
            addError( error );
        }

        return false;
    }

    // //////////////////////////////////////////////////////////////////////////
    // Redirect utils

    /**
     * Return the JSP name used as controller
     * 
     * @return The JSP name
     */
    protected String getControllerJsp( )
    {
        return _controller.controllerJsp( );
    }

    /**
     * Return the path of the JSP used as controller
     * 
     * @return The controller path
     */
    protected String getControllerPath( )
    {
        return _controller.controllerPath( );
    }

    /**
     * Redirect to requested page
     *
     * @param request
     *            the http request
     * @param strTarget
     *            the targeted page
     * @return null. The page should be redirected
     */
    protected String redirect( HttpServletRequest request, String strTarget )
    {
        try
        {
            _logger.debug( "Redirect : {}", strTarget );
            UrlItem url = new UrlItem( strTarget );
            ControllerRedirectEvent event= getEventDispatcher().fireControllerRedirectEvent( url );        
            String finalUrl = event.getLocation( ).toString();
            _logger.debug("Redirecting to final target: {}", finalUrl);        
            
            _response.sendRedirect( finalUrl );
        }
        catch( IOException e )
        {
            _logger.error( "Unable to redirect : {} : {}", strTarget, e.getMessage( ), e );
        }

        return null;
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
     * @return The redirection result
     */
    protected String redirect( HttpServletRequest request, String strView, String strParameter, int nValue )
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
     * @return The redirection result
     */
    protected String redirect( HttpServletRequest request, String strView, String strParameter1, int nValue1, String strParameter2, int nValue2 )
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
    protected String redirect( HttpServletRequest request, String strView, Map<String, String> additionalParameters )
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
     * @return The redirection result
     */
    protected String redirectView( HttpServletRequest request, String strView )
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
        UrlItem url = new UrlItem( getControllerJsp( ) );
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
        return getControllerPath( ) + getViewUrl( strView );
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
        UrlItem url = new UrlItem( getControllerPath( ) + getControllerJsp( ) );
        url.addParameter( MVCUtils.PARAMETER_ACTION, strAction );

        return url.getUrl( );
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
     */
    protected void download( String strData, String strFilename, String strContentType )
    {
        HttpServletResponse response = _response;
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
     */
    protected void download( byte [ ] data, String strFilename, String strContentType )
    {
        HttpServletResponse response = _response;
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
	 * Returns the ServletParameterBinder instance by privileging direct injection. Used during complete transition do CDI XPages.
	 * 
	 * @return the ServletParameterBinder instance
	 */
    private ServletParameterBinder getServletParameterBinder( )
    {
        return null != _servletParameterBinder ? _servletParameterBinder : CDI.current( ).select( ServletParameterBinder.class ).get( );
    }
    /**
	 * Returns the ValidationService instance by privileging direct injection. Used during complete transition do CDI XPages.
	 * 
	 * @return the ValidationService instance
	 */
    private ValidationService getValidationService( )
    {
        return null != _validationService ? _validationService : CDI.current( ).select( ValidationService.class ).get( );
    }
    /**
     * Get Models
     * @return models object
     */
    private Models getModels( )
    {
        return null != models ? models : CDI.current().select(Models.class).get();
    }
    /**
     * Get a models Object filled with default values
     * 
     * @return The models
     */
    private Models getModelsWithSecurityToken( )
    {
    	Models model= getModels( );
        fillSecurityToken( model );        
        return model;
    }
    /**
     * Fill the model with security token
     * 
     * @param model
     *            The model
     */
    private void fillSecurityToken( Models model )
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
