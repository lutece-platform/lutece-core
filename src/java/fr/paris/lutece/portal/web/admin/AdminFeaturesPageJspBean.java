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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.bean.BeanUtil;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.beanvalidation.ValidationError;
import fr.paris.lutece.util.beanvalidation.ValidationErrorConfig;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolation;


/**
 * Provides generic methods for jspBeans
 */
public abstract class AdminFeaturesPageJspBean implements Serializable
{
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -7952383741759547934L;

    // template for all admin pages
    private static final String TEMPLATE_MAIN = "/admin/feature_frameset.html";

    // bookmarks
    private static final String MARK_FEATURE_URL = "feature_url";
    private static final String MARK_FEATURE_TITLE = "feature_title";
    private static final String MARK_FEATURE_ICON = "feature_icon";
    private static final String MARK_FEATURE_DOCUMENTATION = "feature_documentation";
    private static final String MARK_FEATURE_GROUP = "feature_group";
    private static final String MARK_PAGE_TITLE = "page_title";
    private static final String MARK_PAGE_CONTENT = "page_content";

    // Properties
    private static final String PROPERTY_DEFAULT_FEATURE_ICON = "lutece.admin.feature.default.icon";
    private static final String PROPERTY_RESET_EXCEPTION_MESSAGE = "User must reset his password.";

    // private fields
    private String _strFeatureLabel;
    private String _strFeatureUrl;
    private String _strFeatureIcon;
    private String _strFeatureDocumentation;
    private String _strFeatureGroup;
    private String _strPageTitleKey;
    private Locale _locale;
    private AdminUser _user;

    /**
     * Initialize the jspbean data
     * Allows to set the feature url and feature title associated
     * @param request the HTTP request
     * @param strRight The right
     * @throws AccessDeniedException Access denied exception
     * @throws PasswordResetException Password reset exception
     */
    public void init( HttpServletRequest request, String strRight )
        throws AccessDeniedException, PasswordResetException
    {
        _user = AdminUserService.getAdminUser( request );

        if ( !_user.checkRight( strRight ) )
        {
            throw new AccessDeniedException( "User " + _user.getAccessCode(  ) + " does not have " + strRight +
                " right." );
        }

        if ( _user.isPasswordReset(  ) )
        {
            throw new PasswordResetException( PROPERTY_RESET_EXCEPTION_MESSAGE );
        }

        // get the locale
        _locale = _user.getLocale(  );

        Right right = RightHome.findByPrimaryKey( strRight );
        right.setLocale( _locale );
        _strFeatureLabel = right.getName(  );
        _strFeatureUrl = right.getUrl(  );
        _strFeatureIcon = right.getIconUrl(  );
        _strFeatureDocumentation = right.getDocumentationUrl(  );
        _strFeatureGroup = right.getFeatureGroup(  );
    }

    /**
     * Set the page title property
     * @param strPageTitleKey The page title property
     */
    public void setPageTitleProperty( String strPageTitleKey )
    {
        _strPageTitleKey = strPageTitleKey;
    }

    /**
     * Get the page title
     * @return The page title
     */
    public String getPageTitle(  )
    {
        return ( _strPageTitleKey != null ) ? I18nService.getLocalizedString( _strPageTitleKey, getLocale(  ) ) : "";
    }

    /**
     * Returns the Locale
     *
     * @return The Locale
     */
    public Locale getLocale(  )
    {
        return _locale;
    }

    /**
     * Returns the AdminUser
     *
     * @return The AdminUser
     */
    public AdminUser getUser(  )
    {
        return _user;
    }

    /**
     * Returns the feature home Url
     * @param request The HTTP request
     * @return The feature home Url
     */
    public String getHomeUrl( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + _strFeatureUrl;
    }

    /**
     * Returns the feature icon Url
     * @return The feature icon Url
     */
    public String getFeatureIcon(  )
    {
        return _strFeatureIcon;
    }

    /**
     * Sets the feature icon url
     * @param strFeatureIcon the feature icon url
     */
    public void setFeatureIcon( String strFeatureIcon )
    {
        _strFeatureIcon = strFeatureIcon;
    }

    /**
     * Sets the feature group
     * @param strFeatureGroup the feature group
     */
    public void setFeatureGroup( String strFeatureGroup )
    {
        _strFeatureGroup = strFeatureGroup;
    }

    /**
     * Get the admin page from a content data
     *
     * @return the html code for the admin page for the given content
     * @param strContent the data to load in the admin page
     */
    public String getAdminPage( String strContent )
    {
        Map<String, String> rootModel = new HashMap<String, String>(  );

        rootModel.put( MARK_FEATURE_URL, _strFeatureUrl );
        rootModel.put( MARK_FEATURE_TITLE, _strFeatureLabel );

        String strIconUrl = ( _strFeatureIcon != null ) ? _strFeatureIcon
                                                        : AppPropertiesService.getProperty( PROPERTY_DEFAULT_FEATURE_ICON );
        rootModel.put( MARK_FEATURE_ICON, strIconUrl );

        String strDocumentationUrl = null;

        if ( _strFeatureDocumentation != null )
        {
            strDocumentationUrl = _strFeatureDocumentation;
        }

        rootModel.put( MARK_FEATURE_DOCUMENTATION, strDocumentationUrl );
        rootModel.put( MARK_FEATURE_GROUP, _strFeatureGroup );

        rootModel.put( MARK_PAGE_TITLE, getPageTitle(  ) );
        rootModel.put( MARK_PAGE_CONTENT, strContent );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MAIN, getLocale(  ), rootModel );

        return template.getHtml(  );
    }

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
     * Validates a bean.
     *
     * @param <T> the bean type
     * @param bean the bean to validate
     * @return the sets of constraints that has been violated
     */
    public <T> Set<ConstraintViolation<T>> validate( T bean )
    {
        return BeanValidationUtil.validate( bean );
    }

    /**
     * Validates a bean
     * @param <T> The bean type
     * @param bean The bean to validate
     * @param strFieldsKeyPrefix The fields keys prefix in resources files
     * @return The error list
     */
    public <T> List<ValidationError> validate( T bean, String strFieldsKeyPrefix )
    {
        return BeanValidationUtil.validate( bean, getLocale(  ), strFieldsKeyPrefix );
    }

    /**
     * Validates a bean
     * @param <T> The bean type
     * @param bean The bean to validate
     * @param config  The config for Error validation rendering
     * @return The error list
     */
    public <T> List<ValidationError> validate( T bean, ValidationErrorConfig config )
    {
        return BeanValidationUtil.validate( bean, getLocale(  ), config );
    }
}
