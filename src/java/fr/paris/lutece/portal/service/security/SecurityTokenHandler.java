/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.service.security;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.util.mvc.utils.ReflectionUtils;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.cdi.mvc.event.BeforeControllerEvent;
import fr.paris.lutece.portal.web.cdi.mvc.event.MvcEvent.ControllerInvocationType;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Handles the Security token related to the SecurityTokenFilter.
 */
@ApplicationScoped
public class SecurityTokenHandler
{

    public static final String MARK_CSRF_TOKEN = "_csrftoken";
    private static final HashSet<String> ALLOWED_METHODS = new HashSet<>( Arrays.asList( "GET", "HEAD", "TRACE", "OPTIONS" ) );
    private static final String CSRF_TOKEN = SecurityTokenHandler.class.getName( ) + "_CSRF_TOKEN";
    private static final String FILTER_ENABLED = "FILTER_ENABLED_" + SecurityTokenHandler.class.getName( );
    private static final String SESSION_ATTRIBUTE_SITE_TOKEN = "LUTECE_PORTAL_CSRF_TOKEN";
    private static final String SESSION_ATTRIBUTE_ADMIN_TOKEN = "LUTECE_ADMIN_CSRF_TOKEN";
    private static final String PATTERN_FORM = "<form\\s+([a-z]+)(?![^>]*\\\\/>)[^>]*>";
    private static final String TOKEN_FIELD_PATTERN = "<input type=\"hidden\" name=\"_csrftoken\" value=\"{0}\" >";
    private static final String PARAMETER_PAGE = "page";
    private static final String PATH_ADMIN = "/jsp/admin";
    private static final String ACTION_METHOD_PREFIX = "do";
    private static final String PROPERTY_CHECK_SAFE_METHODS = "lutece.security.token.checkGetActions";
    private static final String PROPERTY_GET_ACTION_POLICY = "lutece.mvc.action.getPolicy";
    private static final String PROPERTY_GET_ACTION_ALLOWED = "lutece.mvc.action.getPolicy.allowed";
    public static final String METHOD_POST = "POST";
    public static final String HEADER_ALLOW = "Allow";
    private static final String HEADER_REFERER = "Referer";
    private static final String SEPARATOR_ALLOWED = ":";
    private static final Logger _logger = LogManager.getLogger( "lutece.security" );
    private static final String ATTRIBUTE_GET_ACTION_REFUSED = SecurityTokenHandler.class.getName( ) + "_GET_ACTION_REFUSED";
    private static final String MESSAGE_GET_ACTION = "{} on @Action '{}' of {} (referer {}) : should be POST, policy {}";

    /**
     * What happens when an MVC {@code @Action} is requested with another method than POST (a link, a GET fetch, a HEAD request).
     */
    public enum GetActionPolicy
    {
        /** The action runs without security token check (behaviour before LUT-33977). */
        ALLOW,
        /** The action runs when its security token is valid. */
        TOKEN,
        /** Same as TOKEN, and a warning names the controller, the action and the referer, to find the plugins to migrate. */
        LOG,
        /** The request is answered by a 405 Method Not Allowed, with the header Allow: POST, and the same warning. */
        REFUSE
    }

    private Set<String> _actionMethods = ConcurrentHashMap.newKeySet( );
    private Map<String, Set<String>> _mapDisabledActionMethods = new ConcurrentHashMap<>( );
    private Set<String> _handledControllers = ConcurrentHashMap.newKeySet( );
    private Map<String, Map<String, String>> _mapActionTokenKeys = new ConcurrentHashMap<>( );
    private Map<String, Set<String>> _mapMvcActions = new ConcurrentHashMap<>( );
    private Map<String, Set<String>> _mapMvcViews = new ConcurrentHashMap<>( );
    private volatile GetActionPolicy _getActionPolicy = GetActionPolicy.LOG;
    @Inject 
    private HttpServletRequest request;
    @Inject
    private SecurityTokenService _securityTokenService;
    @Inject
    private BeanManager _beanManager;
    @Inject
    @ConfigProperty( name = PROPERTY_CHECK_SAFE_METHODS )
    private Optional<Boolean> _checkSafeMethods;
    @Inject
    @ConfigProperty( name = PROPERTY_GET_ACTION_POLICY )
    private Optional<String> _strGetActionPolicy;
    @Inject
    @ConfigProperty( name = PROPERTY_GET_ACTION_ALLOWED )
    private Optional<List<String>> _listGetActionAllowed;

    /**
     * Reads the policy applied to the MVC actions requested with another method than POST: lutece.mvc.action.getPolicy (allow, token, log or refuse,
     * log by default), or the former lutece.security.token.checkGetActions when only this one is set (false is allow, true is token).
     */
    @PostConstruct
    void initGetActionPolicy( )
    {
        if ( _strGetActionPolicy != null && _strGetActionPolicy.isPresent( ) )
        {
            try
            {
                _getActionPolicy = GetActionPolicy.valueOf( _strGetActionPolicy.get( ).trim( ).toUpperCase( Locale.ROOT ) );
            }
            catch( IllegalArgumentException e )
            {
                AppLogService.error( "Invalid value '{}' for {} : allow, token, log or refuse expected, log used", _strGetActionPolicy.get( ),
                        PROPERTY_GET_ACTION_POLICY );
                _getActionPolicy = GetActionPolicy.LOG;
            }
        }
        else
            if ( _checkSafeMethods != null && _checkSafeMethods.isPresent( ) )
            {
                _getActionPolicy = _checkSafeMethods.get( ) ? GetActionPolicy.TOKEN : GetActionPolicy.ALLOW;
                AppLogService.info( "{} is deprecated, replaced by {}={}", PROPERTY_CHECK_SAFE_METHODS, PROPERTY_GET_ACTION_POLICY,
                        _getActionPolicy.name( ).toLowerCase( Locale.ROOT ) );
            }
    }

    /**
     * Gets the policy applied to the MVC actions requested with another method than POST.
     *
     * @return The policy
     */
    public GetActionPolicy getGetActionPolicy( )
    {
        return _getActionPolicy;
    }

    /**
     * Sets the policy applied to the MVC actions requested with another method than POST.
     *
     * @param policy
     *            The policy
     */
    public void setGetActionPolicy( GetActionPolicy policy )
    {
        _getActionPolicy = policy;
    }

    /**
     * Registers the actions of every back office MVC controller with the security token enabled, at application startup, so that the token is checked
     * from the first request, before the controller has ever been displayed.
     *
     * @param context
     *            The servlet context
     */
    public void registerAdminControllers( @Observes @Initialized( ApplicationScoped.class ) ServletContext context )
    {
        for ( Bean<?> bean : _beanManager.getBeans( MVCAdminJspBean.class, Any.Literal.INSTANCE ) )
        {
            Class<?> clazz = bean.getBeanClass( );
            Controller controller = clazz.getAnnotation( Controller.class );
            if ( null != controller )
            {
                String strName = controller.controllerPath( ) + controller.controllerJsp( );
                Method [ ] methods = ReflectionUtils.getAllDeclaredMethods( clazz );
                registerMvcController( strName, methods );
                if ( controller.securityTokenEnabled( ) )
                {
                    registerActions( strName, methods );
                }
            }
        }
    }

    /**
     * Registers the views and the actions of an MVC controller (XPage or MVCAdminJspBean), whether its security token is enabled or not, so that the
     * actions requested with another method than POST are found.
     *
     * @param strName
     *            The controller name: the XPage name in front office, the controller path and JSP in back office
     * @param methods
     *            The methods of the controller
     */
    public void registerMvcController( String strName, Method [ ] methods )
    {
        _mapMvcActions.computeIfAbsent( strName, name -> {
            Set<String> views = new HashSet<>( );
            Set<String> actions = new HashSet<>( );
            for ( Method m : methods )
            {
                if ( m.isAnnotationPresent( Action.class ) )
                {
                    actions.add( m.getAnnotation( Action.class ).value( ) );
                }
                else
                    if ( m.isAnnotationPresent( View.class ) )
                    {
                        views.add( m.getAnnotation( View.class ).value( ) );
                    }
            }
            _mapMvcViews.put( name, views );
            return actions;
        } );
    }

    /**
     * Checks whether the request calls an MVC {@code @Action} with another method than POST: the action parameter names an action of the registered
     * controller of the request, and no view of this controller is requested (a view takes precedence over an action in the dispatch). The requests
     * of the controllers that do not use the MVC model (a page reading its own action parameter) are never concerned.
     *
     * @param request
     *            The request
     * @return true if an MVC action is requested with another method than POST
     */
    public boolean isMvcActionWithoutPost( HttpServletRequest request )
    {
        if ( METHOD_POST.equals( request.getMethod( ) ) )
        {
            return false;
        }
        String strAction = MVCUtils.getAction( request );
        if ( null == strAction )
        {
            return false;
        }
        String strController = getControllerName( request );
        Set<String> actions = _mapMvcActions.get( strController );
        if ( null == actions || !actions.contains( strAction ) )
        {
            return false;
        }
        String strView = MVCUtils.getView( request );
        Set<String> views = _mapMvcViews.get( strController );
        return null == strView || null == views || !views.contains( strView );
    }

    /**
     * Gets the policy applied to the action of the request: allow when the controller and the action are listed in lutece.mvc.action.getPolicy.allowed
     * (entries controller:action, the controller being the XPage name or the back office path such as jsp/admin/plugins/x/ManageX.jsp), the
     * configured policy otherwise.
     *
     * @param request
     *            The request
     * @return The policy
     */
    public GetActionPolicy getGetActionPolicy( HttpServletRequest request )
    {
        if ( _listGetActionAllowed != null && _listGetActionAllowed.isPresent( ) )
        {
            String strEntry = getControllerName( request ) + SEPARATOR_ALLOWED + MVCUtils.getAction( request );
            for ( String strAllowed : _listGetActionAllowed.get( ) )
            {
                if ( strEntry.equals( strAllowed.trim( ) ) )
                {
                    return GetActionPolicy.ALLOW;
                }
            }
        }
        return _getActionPolicy;
    }

    /**
     * Applies the policy to a request reaching the security token filter: when it calls a registered MVC action with another method than POST, writes
     * the warning of the log and refuse policies.
     *
     * @param request
     *            The request
     * @return true if the request must be refused with a 405 Method Not Allowed
     */
    public boolean isRefusedMvcActionWithoutPost( HttpServletRequest request )
    {
        return isMvcActionWithoutPost( request ) && isRefusedActionWithoutPost( request );
    }

    /**
     * Applies the policy to a request whose dispatch has selected an MVC action: when it is requested with another method than POST, writes the
     * warning of the log and refuse policies. The result is kept in the request, so that the filter and the dispatch write a single warning.
     *
     * @param request
     *            The request
     * @return true if the request must be refused with a 405 Method Not Allowed
     */
    public boolean isRefusedActionWithoutPost( HttpServletRequest request )
    {
        if ( METHOD_POST.equals( request.getMethod( ) ) )
        {
            return false;
        }
        Object checked = request.getAttribute( ATTRIBUTE_GET_ACTION_REFUSED );
        if ( null != checked )
        {
            return Boolean.TRUE.equals( checked );
        }
        GetActionPolicy policy = getGetActionPolicy( request );
        if ( GetActionPolicy.LOG.equals( policy ) || GetActionPolicy.REFUSE.equals( policy ) )
        {
            _logger.warn( MESSAGE_GET_ACTION, request.getMethod( ), MVCUtils.getAction( request ), getControllerName( request ),
                    request.getHeader( HEADER_REFERER ), policy.name( ).toLowerCase( Locale.ROOT ) );
        }
        boolean bRefused = GetActionPolicy.REFUSE.equals( policy );
        request.setAttribute( ATTRIBUTE_GET_ACTION_REFUSED, bRefused );
        return bRefused;
    }

    /**
     * Registers disabled action methods from XPage or MVCAdminJspBean, and the token key of the actions that share their token with other actions
     * (securityTokenAction).
     */
    public void registerActions( String strName, Method [ ] methods )
    {
        _mapDisabledActionMethods.computeIfAbsent( strName, name -> {
            Set<String> dis = new HashSet<>( );
            Map<String, String> tokenKeys = new HashMap<>( );
            for ( Method m : methods )
            {
                if ( m.isAnnotationPresent( Action.class ) )
                {
                    String strActionMethod = m.getAnnotation( Action.class ).value( ); 
                    _actionMethods.add( strActionMethod );
                    if (m.getAnnotation( Action.class ).securityTokenDisabled( ) )
                    {
                        dis.add( strActionMethod );
                    }
                    String strTokenKey = m.getAnnotation( Action.class ).securityTokenAction( );
                    if ( !strTokenKey.isEmpty( ) )
                    {
                        tokenKeys.put( strActionMethod, strTokenKey );
                    }
                }
                else if ( m.getName( ).startsWith( ACTION_METHOD_PREFIX ) && !m.isAnnotationPresent( Action.class ) && !m.isAnnotationPresent( View.class ) )
                {
                    // This XPage is not using the MVC model, so every 'do' action should be handled without token validation
                    dis.add( m.getName( ) );
                }
            }
            _mapActionTokenKeys.put( name, tokenKeys );
            _handledControllers.add( name );
            return dis;
        } );
    }

    /**
     * Resolves the key under which the token of the requested action is stored: the securityTokenAction of the action when it declares one, so that
     * several actions posted from the same form (save a step, summary, save...) accept the token generated for that form, the action name otherwise.
     * 
     * @param request
     *            The request
     * @param strAction
     *            The MVC action
     * @return The token key
     */
    public String resolveTokenAction( HttpServletRequest request, String strAction )
    {
        Map<String, String> tokenKeys = _mapActionTokenKeys.get( getControllerName( request ) );
        if ( null != tokenKeys && tokenKeys.containsKey( strAction ) )
        {
            return tokenKeys.get( strAction );
        }
        return strAction;
    }

    /**
     * Gets the name under which the controller of the request is registered: the XPage name in front office, the requested path in back office.
     * 
     * @param request
     *            The request
     * @return The controller name
     */
    private String getControllerName( HttpServletRequest request )
    {
        String strPageName = request.getParameter( PARAMETER_PAGE );
        if ( null != strPageName && !request.getServletPath( ).startsWith( PATH_ADMIN ) )
        {
            return strPageName;
        }
        return AppPathService.getRequestedPath( request );
    }

    /**
     * Activates the token handling on the request.
     * 
     * @param request
     *            The request
     */
    public void handle( HttpServletRequest request )
    {
        request.setAttribute( FILTER_ENABLED, Boolean.TRUE );
    }

    /**
     * Checks if the request should validate the security token.
     * 
     * @param request
     *            The request
     * @return
     *         True if the request should be skipped from the SecurityTokenFilter validation, false otherwise.
     */
    public boolean shouldNotFilter( HttpServletRequest request )
    {
        String strAction = MVCUtils.getAction( request );

        if ( null == strAction
                || ( ALLOWED_METHODS.contains( request.getMethod( ) ) && GetActionPolicy.ALLOW.equals( getGetActionPolicy( request ) ) ) )
        {
            return true;
        }

        String strPageName = request.getParameter( PARAMETER_PAGE );
        String strPath = AppPathService.getRequestedPath( request );
        if ( null != strPageName && !request.getServletPath( ).startsWith( PATH_ADMIN ) )
        {
            return shouldNotFilterXPageAction( request, strPageName, strAction );
        }
        return shouldNotFilterPath( request, strPath, strAction );
    }

    /**
     * Checks if the request should validate the security token for the given Admin feature.
     * 
     * @param request
     *            The request
     * @return
     *         True if the request should be skipped from the SecurityTokenFilter validation, false otherwise.
     */
    private boolean shouldNotFilterPath( HttpServletRequest request2, String strPath, String strAction )
    {
        if ( !_actionMethods.contains( strAction ) )
        {
            return true;
        }
        if ( null != strPath && !_handledControllers.contains( strPath ) )
        {
            return true;
        }
        if ( _mapDisabledActionMethods.containsKey( strPath ) && _mapDisabledActionMethods.get( strPath ).contains( strAction ) )
        {
            return true;
        }
        return false;
    }

    /**
     * Checks if the request should validate the security token for the given XPage.
     * 
     * @param request
     *            The request
     * @return
     *         True if the request should be skipped from the SecurityTokenFilter validation, false otherwise.
     */
    private boolean shouldNotFilterXPageAction( HttpServletRequest request, String strPageName, String strAction )
    {
        if ( !_handledControllers.contains( strPageName ) )
        {
            return true;
        }
        if ( _mapDisabledActionMethods.containsKey( strPageName ) && _mapDisabledActionMethods.get( strPageName ).contains( strAction ) )
        {
            return true;
        }
        return false;
    }
    
    /**
     * Handles the generation and storage of the security token.
     * 
     * @param request
     *            The request
     * @param invocationType
     *            The invocation type
     * @param methods
     *            The MVC methods
     */
    public void handleToken( HttpServletRequest request, ControllerInvocationType invocationType, Method method )
    {
        if ( Boolean.TRUE.equals( request.getAttribute( FILTER_ENABLED ) ) )
        {
            if ( invocationType.equals( ControllerInvocationType.ACTION ) )
            {
                String strAction = MVCUtils.getAction( request );
                if ( strAction != null )
                {
                    String secTokenAction = !"".equals( method.getAnnotation( Action.class ).securityTokenAction( ) )
                            ? method.getAnnotation( Action.class ).securityTokenAction( )
                            : method.getAnnotation( Action.class ).value( );
                    handleToken( request, secTokenAction );
                }
            } else {
                String secTokenAction = !"".equals( method.getAnnotation( View.class ).securityTokenAction( ) )
                        ? method.getAnnotation( View.class ).securityTokenAction( )
                        : method.getAnnotation( View.class ).value( );
                handleToken( request, secTokenAction );
            }
        }
    }
    
    /**
     * Handles the generation and storage of the security token for the given action.
     * 
     * @param request
     *            The request
     * @param tokenAction
     *            The token action
     */
    private void handleToken( HttpServletRequest request, String tokenAction )
    {
        String s = _securityTokenService.getToken( request, tokenAction );
        request.setAttribute( CSRF_TOKEN, s );
        HttpSession session = request.getSession( true );
        if ( !request.getServletPath( ).startsWith( PATH_ADMIN ) )
        {
            session.setAttribute( SESSION_ATTRIBUTE_SITE_TOKEN, s );
        }
        else
        {
            session.setAttribute( SESSION_ATTRIBUTE_ADMIN_TOKEN, s );
        }
    }

    /**
     * Resolves security token value from the request.
     * 
     * @param request
     *            The request
     * @return
     *         The security token value
     */
    public String resolveTokenValue( HttpServletRequest request )
    {
        return (String) request.getAttribute( CSRF_TOKEN );
    }

    /**
     * Adds the session security token for confirm type actions (SiteMessageHandler / AdminMessageJspBean).
     * 
     * @param request
     *            The request
     * @param model
     *            The model
     */
    public void addSessionTokenValue( HttpServletRequest request, Map<String, Object> model )
    {
        HttpSession session = request.getSession( true );
        String strTokenAttribute = SESSION_ATTRIBUTE_SITE_TOKEN;
        if ( request.getServletPath( ).startsWith( PATH_ADMIN ) )
        {
            strTokenAttribute = SESSION_ATTRIBUTE_ADMIN_TOKEN;
        }
        String s = (String) session.getAttribute( strTokenAttribute );
        model.put( MARK_CSRF_TOKEN, s );
        session.removeAttribute( strTokenAttribute );
    }

    /**
     * Validates the token for the given request. A form may carry several tokens: the token of the page, added to every form, and the token of the
     * action it posts when this action is not the one of the page (a button of a list posting an action); the action is accepted when one of them is
     * valid for it, and only this one is consumed.
     *
     * @param request
     *            The request
     * @param strAction
     *            The MVC action
     * @return
     *         True if the token was valid.
     */
    public boolean validate( HttpServletRequest request, String strAction )
    {
        String [ ] tokens = request.getParameterValues( MARK_CSRF_TOKEN );
        if ( null == tokens )
        {
            return false;
        }
        for ( String strToken : tokens )
        {
            if ( _securityTokenService.consumeToken( request, strAction, strToken ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the hidden field to forms located in the given template data.
     * 
     * @param strSource
     *            The template data.
     * @param model
     *            The model
     * @return
     *         The updated template data.
     */
    public static String addSecurityToken( String strSource, Object model )
    {
        String result = strSource;

        if ( null != strSource  && model != null) {
        	String strToken = null;
            
            // Extraire le token selon le type de model
            if (model instanceof HashMap) {
                HashMap<String, Object> map = (HashMap<String, Object>) model;
                strToken = (String) map.get(MARK_CSRF_TOKEN);
            } else if (model instanceof Models) {
                Models modelObj = (Models) model;
                strToken = (String) modelObj.get(MARK_CSRF_TOKEN);
            }        
            if ( null != strToken )
            {
                Pattern p = Pattern.compile( PATTERN_FORM );
                Matcher matcher = p.matcher( strSource );

                if ( matcher.find( ) )
                {
                    StringBuffer sb = new StringBuffer( );

                    do
                    {
                        matcher.appendReplacement( sb, matcher.group( 0 ) + MessageFormat.format( TOKEN_FIELD_PATTERN, strToken ) );
                    }
                    while ( matcher.find( ) );

                    matcher.appendTail( sb );
                    result = sb.toString( );
                }
            }
        }

        return result;
    }

    /**
     * Observes controller invocations and handles token validation for selected invocation types (ACTION, VIEW, DEFAULT_VIEW).
     *
     * @param event
     *            the controller event
     */
    public void onControllerInvocation( @Observes BeforeControllerEvent event )
    {
        ControllerInvocationType invocationType = event.getInvocationType( );
        if ( ( invocationType.equals( ControllerInvocationType.VIEW ) || invocationType.equals( ControllerInvocationType.ACTION )
                || invocationType.equals( ControllerInvocationType.DEFAULT_VIEW ) ) && event.isSecurityTokenEnabled( ) )
        {
            handleToken( request, invocationType, event.getInvokedMethod( ) );
        }
    }
}
