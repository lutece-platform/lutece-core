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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.template.AppTemplateService;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Base class of the portlets whose content is HTML, rendered with a FreeMarker template rather than through an XML/XSL transformation.
 * <p>
 * The template is the one chosen for the portlet among the {@link PortletTemplate} registered for its type in the back office; a portlet without a valid
 * template falls back on the default template given by the subclass. Subclasses build their model with {@link #createPortletModel()} and render it with
 * {@link #renderTemplate(HttpServletRequest, String, Map)}.
 */
public abstract class PortletHtmlContent extends Portlet
{
    /** Mark of the portlet itself */
    public static final String MARK_PORTLET = "portlet";
    /** Mark of the portlet identifier */
    public static final String MARK_PORTLET_ID = "portlet_id";
    /** Mark of the portlet name, present only when the title has to be displayed */
    public static final String MARK_PORTLET_NAME = "portlet_name";
    /** Mark of the CSS classes hiding the portlet on the devices it must not be displayed on */
    public static final String MARK_DEVICE_CLASS = "device_class";

    private static final String CLASS_HIDDEN = "d-none";
    private static final String CLASS_DISPLAY_PREFIX = "d-";
    private static final String CLASS_DISPLAY_NONE_SUFFIX = "-none";
    private static final String CLASS_DISPLAY_BLOCK_SUFFIX = "-block";
    private static final String [ ] BREAKPOINTS = {
            "md", "lg", "xl"
    };
    private static final int [ ] BREAKPOINT_FLAGS = {
            FLAG_DISPLAY_ON_NORMAL_DEVICE, FLAG_DISPLAY_ON_LARGE_DEVICE, FLAG_DISPLAY_ON_XLARGE_DEVICE
    };

    /**
     * Returns the HTML content of the portlet
     *
     * @param request
     *            The HTTP request
     * @return the HTML content
     */
    @Override
    public abstract String getHtmlContent( HttpServletRequest request );

    /**
     * Returns the path of the FreeMarker template to render this portlet with : the template chosen for the portlet when it is valid, the given default
     * otherwise
     *
     * @param strDefaultTemplatePath
     *            the default template of the portlet type
     * @return the template path, relative to the WEB-INF/templates directory
     * @see PortletTemplateHome#getTemplatePath(Portlet, String)
     */
    public String getTemplatePath( String strDefaultTemplatePath )
    {
        return PortletTemplateHome.getTemplatePath( this, strDefaultTemplatePath );
    }

    /**
     * Returns the Bootstrap display classes hiding the portlet on the devices it must not be displayed on, computed from the device display flags
     *
     * @return the CSS classes, an empty string when the portlet is displayed on every device
     */
    public String getDeviceDisplayClass( )
    {
        StringBuilder sbClass = new StringBuilder( );
        boolean bVisible = hasDeviceDisplayFlag( FLAG_DISPLAY_ON_SMALL_DEVICE );

        if ( !bVisible )
        {
            sbClass.append( CLASS_HIDDEN );
        }

        for ( int i = 0; i < BREAKPOINTS.length; i++ )
        {
            boolean bVisibleAtBreakpoint = hasDeviceDisplayFlag( BREAKPOINT_FLAGS [i] );

            if ( bVisibleAtBreakpoint != bVisible )
            {
                sbClass.append( ' ' ).append( CLASS_DISPLAY_PREFIX ).append( BREAKPOINTS [i] )
                        .append( bVisibleAtBreakpoint ? CLASS_DISPLAY_BLOCK_SUFFIX : CLASS_DISPLAY_NONE_SUFFIX );
                bVisible = bVisibleAtBreakpoint;
            }
        }

        return sbClass.toString( ).trim( );
    }

    /**
     * Creates the model shared by every portlet template : the portlet, its identifier, its device display classes and, when the title has to be displayed,
     * its name
     *
     * @return the model, to complete with the marks of the portlet type
     */
    protected Map<String, Object> createPortletModel( )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_PORTLET, this );
        model.put( MARK_PORTLET_ID, getId( ) );
        model.put( MARK_DEVICE_CLASS, getDeviceDisplayClass( ) );

        if ( getDisplayPortletTitle( ) == 0 )
        {
            model.put( MARK_PORTLET_NAME, getName( ) );
        }

        return model;
    }

    /**
     * Renders the portlet with the template chosen for it, or with the given default template when the portlet has no valid template
     *
     * @param request
     *            the HTTP request, used for the locale
     * @param strDefaultTemplatePath
     *            the default template of the portlet type
     * @param model
     *            the model
     * @return the rendered HTML
     */
    protected String renderTemplate( HttpServletRequest request, String strDefaultTemplatePath, Map<String, Object> model )
    {
        return AppTemplateService.getTemplate( getTemplatePath( strDefaultTemplatePath ), getLocale( request ), model ).getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXml( HttpServletRequest request ) throws SiteMessageException
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXmlDocument( HttpServletRequest request ) throws SiteMessageException
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isContentGeneratedByXmlAndXsl( )
    {
        return false;
    }
}
