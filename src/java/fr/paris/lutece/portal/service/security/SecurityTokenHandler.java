/*
 * Copyright (c) 2002-2024, City of Paris
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
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.cdi.mvc.event.BeforeControllerEvent;
import fr.paris.lutece.portal.web.cdi.mvc.event.MvcEvent.ControllerInvocationType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
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
    private static final String SESSION_ATTRIBUTE_TOKENS = "tokens";
    private static final String PATTERN_FORM = "<form ([a-z]+)(?![^>]*\\\\/>)[^>]*>";
    private static final String TOKEN_FIELD_PATTERN = "<input type=\"hidden\" name=\"_csrftoken\" value=\"{0}\" >";
    private static final String PARAMETER_PAGE = "page";
    private static final String PATH_ADMIN = "/jsp/admin";
    private static final String ACTION_METHOD_PREFIX = "do";

    private Set<String> _actionMethods = new HashSet<String>( );
    private Map<String, HashSet<String>> _mapDisabledActionMethods = new HashMap<String, HashSet<String>>( );
    @Inject 
    HttpServletRequest request;
    @Inject
    private SecurityTokenService _securityTokenService;

    /**
     * Registers disabled action methods from XPage or MVCAdminJspBean
     */
    public void registerActions( String strName, Method [ ] methods )
    {
        if ( !_mapDisabledActionMethods.containsKey( strName ) )
        {
            _mapDisabledActionMethods.put( strName, new HashSet<String>( ) );
            HashSet<String> dis = _mapDisabledActionMethods.get( strName );
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
                }
                else if ( m.getName( ).startsWith( ACTION_METHOD_PREFIX ) && !m.isAnnotationPresent( Action.class ) && !m.isAnnotationPresent( View.class ) )
                {
                    // This XPage is not using the MVC model, so every 'do' action should be handled without token validation
                    dis.add( m.getName( ) );
                }
            }
        }
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
        String strPageName = request.getParameter( PARAMETER_PAGE );
        String strPath = request.getServletPath( ).substring( 1 );

        if ( null == strAction
                || ALLOWED_METHODS.contains( request.getMethod( ) ) )
        {
            return true;
        }

        if ( null == strPageName && !_actionMethods.contains( strAction ) )
        {
            return true;
        }
        
        if ( ( _mapDisabledActionMethods.containsKey( strPageName ) && _mapDisabledActionMethods.get( strPageName ).contains( strAction ) )
                || ( _mapDisabledActionMethods.containsKey( strPath ) && _mapDisabledActionMethods.get( strPath ).contains( strAction ) ) )
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
     * @param methods
     *            The MVC methods
     */
    public void handleToken( HttpServletRequest request, Method method )
    {
        if ( Boolean.TRUE.equals( request.getAttribute( FILTER_ENABLED ) ) )
        {
            String strView = MVCUtils.getView( request );
            if ( strView != null )
            {
                String secTokenAction = !"".equals( method.getAnnotation( View.class ).securityTokenAction( ) )
                        ? method.getAnnotation( View.class ).securityTokenAction( )
                        : method.getAnnotation( View.class ).value( );
                handleToken( request, secTokenAction );
                return;
            }
            
            String strAction = MVCUtils.getAction( request );
            if ( strAction != null )
            {
                String secTokenAction = !"".equals( method.getAnnotation( Action.class ).securityTokenAction( ) )
                        ? method.getAnnotation( Action.class ).securityTokenAction( )
                        : method.getAnnotation( Action.class ).value( );
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
     * Validates the token for the given request.
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
        HttpSession session = request.getSession( true );

        String strToken = request.getParameter( MARK_CSRF_TOKEN );

        if ( ( session.getAttribute( SESSION_ATTRIBUTE_TOKENS ) != null )
                && ( (Map<String, Set<String>>) session.getAttribute( SESSION_ATTRIBUTE_TOKENS ) ).containsKey( strAction )
                && ( (Map<String, Set<String>>) session.getAttribute( SESSION_ATTRIBUTE_TOKENS ) ).get( strAction ).contains( strToken ) )
        {
            ( (Map<String, Set<String>>) session.getAttribute( SESSION_ATTRIBUTE_TOKENS ) ).get( strAction ).remove( strToken );

            return true;
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

        if ( null != strSource && model instanceof HashMap)
        {
            HashMap<String, Object> rootmap = (HashMap<String, Object>) model;
            String strToken = (String) rootmap.get( MARK_CSRF_TOKEN );
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
     * Observes controller invocations and handles token validation
     * for selected invocation types (ACTION, VIEW, DEFAULT_VIEW).
     *
     * @param event the controller event
     */
    public void onControllerInvocation(@Observes BeforeControllerEvent event)
    {
        ControllerInvocationType invocationType = event.getInvocationType();
    	if(invocationType.equals(ControllerInvocationType.VIEW) 
    			|| invocationType.equals(ControllerInvocationType.ACTION)
    			|| invocationType.equals(ControllerInvocationType.DEFAULT_VIEW)) {
    		handleToken( request, event.getInvokedMethod( ));
    	}

    }
}
