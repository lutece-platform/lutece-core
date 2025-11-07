package fr.paris.lutece.portal.web.cdi.mvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.cdi.mvc.event.AfterProcessViewEvent;
import fr.paris.lutece.portal.web.cdi.mvc.event.BeforeControllerEvent;
import fr.paris.lutece.portal.web.cdi.mvc.event.ControllerRedirectEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.inject.spi.PassivationCapable;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ApplicationScoped
public class RedirectScopeManager {

	/**
     * Boolean property that when set to {@code true} indicates Krazo to use cookies instead of the default URL re-write
     * mechanism to implement redirect scope.
     */
	public static String REDIRECT_SCOPE_COOKIES = "fr.paris.lutece.redirectScopeCookies";
	 /**
     * String property that determines the name of the request attribute/parameter to be used in
     */
	public static String REDIRECT_SCOPE_QUERY_PARAM_NAME = "org.eclipse.krazo.redirectScopeQueryParamName";

    /**
     * String property that determines the redirect Cookie name to be used in
     */
	public static String REDIRECT_SCOPE_COOKIE_NAME = "org.eclipse.krazo.redirectScopeCookieName";
   
	public static final String DEFAULT_QUERY_PARAM_NAME = "fr.paris.lutece.redirect.param.ScopeId";
    public static final String DEFAULT_COOKIE_NAME = "fr.paris.lutece.redirect.Cookie";

    public static final String SCOPE_ID = "fr.paris.lutece.redirect.attribute.ScopeId";
    private static final String INSTANCE = "Instance-";
    private static final String CREATIONAL = "Creational-";
	/**
     * Stores the HTTP servlet request we are working for.
     */
    @Inject
    private HttpServletRequest request;

    /**
     * Destroy the instance.
     *
     * @param contextual the contextual.
     */
    public void destroy(Contextual contextual) {
        String scopeId = (String) request.getAttribute(SCOPE_ID);
        if (null != scopeId) {
            HttpSession session = request.getSession();
            if (!(contextual instanceof PassivationCapable)) {
                throw new RuntimeException("Unexpected type for contextual");
            }
            PassivationCapable pc = (PassivationCapable) contextual;
            final String sessionKey = SCOPE_ID + "-" + scopeId;
            Map<String, Object> scopeMap = (Map<String, Object>) session.getAttribute(sessionKey);
            if (null != scopeMap) {
                Object instance = scopeMap.get(INSTANCE + pc.getId());
                CreationalContext<?> creational = (CreationalContext<?>) scopeMap.get(CREATIONAL + pc.getId());
                if (null != instance && null != creational) {
                    contextual.destroy(instance, creational);
                    creational.release();
                }
            }
        }
    }
    
    /**
     * Get the instance.
     *
     * @param <T> the type.
     * @param contextual the contextual.
     * @return the instance, or null.
     */
    public <T> T get(Contextual<T> contextual) {
        T result = null;

        String scopeId = (String) request.getAttribute(SCOPE_ID);
        if (null != scopeId) {
            HttpSession session = request.getSession();
            if (contextual instanceof PassivationCapable == false) {
                throw new RuntimeException("Unexpected type for contextual");
            }
            PassivationCapable pc = (PassivationCapable) contextual;
            final String sessionKey = SCOPE_ID + "-" + scopeId;
            Map<String, Object> scopeMap = (Map<String, Object>) session.getAttribute(sessionKey);
            if (null != scopeMap) {
                result = (T) scopeMap.get(INSTANCE + pc.getId());
            } else {
                request.setAttribute(SCOPE_ID, null);       // old cookie, force new scope generation
            }
        }
        return result;
    }
    
    /**
     * Get the instance (create it if it does not exist).
     *
     * @param <T> the type.
     * @param contextual the contextual.
     * @param creational the creational.
     * @return the instance.
     */
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creational) {
        T result = get(contextual);

        if (result == null) {
            String scopeId = (String) request.getAttribute(SCOPE_ID);
            if (null == scopeId) {
                scopeId = generateScopeId();
            }
            HttpSession session = request.getSession();
            result = contextual.create(creational);
            if (!(contextual instanceof PassivationCapable)) {
                throw new RuntimeException("Unexpected type for contextual");
            }
            PassivationCapable pc = (PassivationCapable) contextual;
            final String sessionKey = SCOPE_ID + "-" + scopeId;
            Map<String, Object> scopeMap = (Map<String, Object>) session.getAttribute(sessionKey);
            if (null != scopeMap) {
                session.setAttribute(sessionKey, scopeMap);
                scopeMap.put(INSTANCE + pc.getId(), result);
                scopeMap.put(CREATIONAL + pc.getId(), creational);
            }
        }

        return result;
    }
    
    /**
     * Update scopeId request attribute based on either cookie or URL query param
     * information received in the request.
     *
     * @param event the event.
     */
    public void beforeProcessControllerEvent(@Observes @Priority(1) BeforeControllerEvent event) {
        if (usingCookies()) {
            final Cookie[] cookies = request.getCookies();
            if (null != cookies) {
            	String cookieName= AppPropertiesService.getProperty(REDIRECT_SCOPE_COOKIE_NAME,DEFAULT_COOKIE_NAME);

                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(cookieName)) {
                        request.setAttribute(SCOPE_ID, cookie.getValue());
                        return;     // we're done
                    }
                }
            }
        } else {
        	String paramName= AppPropertiesService.getProperty(REDIRECT_SCOPE_QUERY_PARAM_NAME,DEFAULT_QUERY_PARAM_NAME);
           // final String scopeId = event.getUriInfo().getQueryParameters().getFirst(krazoConfig.getRedirectScopeAttributeName());
        	final String scopeId = request.getParameter(paramName);
        	if (scopeId != null) {
                request.setAttribute(SCOPE_ID, scopeId);
            }
        }
    }
    /**
     * Perform the work we need to do at AfterProcessViewEvent time.
     *
     * @param event the event.
     */
    public void afterProcessViewEvent(@Observes AfterProcessViewEvent event) {
        if (request.getAttribute(SCOPE_ID) != null) {
            String scopeId = (String) request.getAttribute(SCOPE_ID);
            HttpSession session = request.getSession();
            final String sessionKey = SCOPE_ID + "-" + scopeId;
            Map<String, Object> scopeMap = (Map<String, Object>) session.getAttribute(sessionKey);
            if (null != scopeMap) {
                scopeMap.entrySet().forEach((entrySet) -> {
                    String key = entrySet.getKey();
                    Object value = entrySet.getValue();
                    if (key.startsWith(INSTANCE)) {
                        BeanManager beanManager =  CDI.current().getBeanManager();
                        Bean<?> bean = beanManager.resolve(beanManager.getBeans(value.getClass()));
                        destroy(bean);
                    }
                });
                scopeMap.clear();
                session.removeAttribute(sessionKey);
            }
        }
    }
    /**
     * Upon detecting a redirect, either add cookie to response or re-write URL of new
     * location to co-relate next request.
     *
     * @param event the event.
     */
    public void controllerRedirectEvent(@Observes @Priority(Interceptor.Priority.APPLICATION + 1000) ControllerRedirectEvent event) {
    	Object scopeId = request.getAttribute(SCOPE_ID);
        if (scopeId != null) {
            HttpServletResponse response = LocalVariables.getResponse( );
            if (usingCookies()) {            	
            	String cookieName= AppPropertiesService.getProperty(REDIRECT_SCOPE_COOKIE_NAME,DEFAULT_COOKIE_NAME);
                Cookie cookie = new Cookie(cookieName, scopeId.toString());
                cookie.setPath(request.getContextPath());
                cookie.setMaxAge(600);
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
                response.setIntHeader(cookieName, 0);
            } else {

            	String paramName=AppPropertiesService.getProperty(REDIRECT_SCOPE_QUERY_PARAM_NAME,DEFAULT_QUERY_PARAM_NAME );
                String encodedScopeId = URLEncoder.encode(scopeId.toString(), StandardCharsets.UTF_8);          	
                event.getLocation().addParameter(paramName, encodedScopeId);
             }
        }
    }
    /**
     * Generate the scope id.
     *
     * @return the scope id.
     */
    private String generateScopeId() {
        HttpSession session = request.getSession();
        String scopeId = UUID.randomUUID().toString();
        String sessionKey = SCOPE_ID + "-" + scopeId;
        synchronized (this) {
            while (session.getAttribute(sessionKey) != null) {
                scopeId = UUID.randomUUID().toString();
                sessionKey = SCOPE_ID + "-" + scopeId;
            }
            session.setAttribute(sessionKey, new HashMap<>());
            request.setAttribute(SCOPE_ID, scopeId);
        }
        return scopeId;
    }

    /**
     * Checks application configuration to see if cookies should be used.
     *
     * @return value of property.
     */
    private boolean usingCookies() {
        return AppPropertiesService.getPropertyBoolean(REDIRECT_SCOPE_COOKIES, false);
    }
}
