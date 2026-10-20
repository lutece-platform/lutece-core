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
package fr.paris.lutece.portal.service.html;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Sanitizes HTML with the OWASP Java HTML Sanitizer, as one policy: the formatting, links (rel="nofollow"), blocks,
 * images, styles and tables of the OWASP predefined policies, plus what rich text contents need (LUT-33153): the class,
 * role and aria-* attributes, the target of links (with rel="noopener noreferrer"), tel: links, the spans and scope of
 * table cells, the start of ordered lists, the elements pre, hr, dl, figure, section, article and details, and the
 * iframes of an allowed video host. Scripts, event handlers, the id and data-* attributes are never kept.
 */
@ApplicationScoped
public class OwaspXSSSanitizer implements IXSSSanitizer
{
    private static final String PROPERTY_IFRAME_HOSTS = "portal.sanitizer.iframe.allowedHosts";
    private static final String DEFAULT_IFRAME_HOSTS = "www.youtube-nocookie.com,www.youtube.com,www.dailymotion.com,player.vimeo.com";
    private static final Pattern CLASS_NAMES = Pattern.compile( "[A-Za-z0-9_\\- ]*" );
    private static final Pattern ROLE = Pattern.compile( "[a-z]+" );
    private static final Pattern NUMBER = Pattern.compile( "[0-9]{1,4}" );
    private static final Pattern SCOPE = Pattern.compile( "row|col|rowgroup|colgroup" );
    private static final Pattern TARGET = Pattern.compile( "_blank|_self|_parent|_top" );
    private static final String [ ] ARIA_ATTRIBUTES = {
            "aria-label", "aria-labelledby", "aria-describedby", "aria-hidden", "aria-expanded", "aria-controls", "aria-current", "aria-live",
            "aria-level", "aria-haspopup", "aria-pressed", "aria-selected", "aria-roledescription"
    };

    private static PolicyFactory _policy;

    /**
     * Sanitizes HTML.
     *
     * @param strSource
     *            the HTML
     * @return the sanitized HTML, with "@" and "+" unescaped for e-mail addresses
     */
    @Override
    public String sanitize( String strSource )
    {
        return _policy.sanitize( strSource ).replace( "&#64;", "@" ).replace( "&#43;", "+" );
    }

    /**
     * Builds the policy.
     */
    @Override
    public void init( )
    {
        _policy = new HtmlPolicyBuilder( ).allowCommonInlineFormattingElements( ).allowCommonBlockElements( ).allowStyling( )
                .allowElements( "a" ).allowAttributes( "href" ).onElements( "a" ).allowUrlProtocols( "https", "http", "mailto", "tel" )
                .requireRelNofollowOnLinks( ).allowAttributes( "target" ).matching( TARGET ).onElements( "a" ).requireRelsOnLinks( "noopener", "noreferrer" )
                .allowElements( "img" ).allowAttributes( "alt", "src" ).onElements( "img" ).allowAttributes( "border", "height", "width" ).matching( NUMBER )
                .onElements( "img" )
                .allowElements( "table", "tr", "td", "th", "colgroup", "caption", "col", "thead", "tbody", "tfoot" ).allowAttributes( "summary" )
                .onElements( "table" ).allowTextIn( "table" ).allowAttributes( "colspan", "rowspan" ).matching( NUMBER ).onElements( "td", "th" )
                .allowAttributes( "scope" ).matching( SCOPE ).onElements( "th" ).allowAttributes( "start" ).matching( NUMBER ).onElements( "ol" )
                .allowElements( "pre", "hr", "dl", "dt", "dd", "figure", "figcaption", "section", "article", "details", "summary" )
                .allowAttributes( "open" ).onElements( "details" ).allowAttributes( "class" ).matching( CLASS_NAMES ).globally( )
                .allowAttributes( "role" ).matching( ROLE ).globally( ).allowAttributes( ARIA_ATTRIBUTES ).globally( )
                .allowElements( ( strElementName, listAttributes ) -> listAttributes.contains( "src" ) ? strElementName : null, "iframe" )
                .allowAttributes( "src" ).matching( iframeSource( ) ).onElements( "iframe" )
                .allowAttributes( "title", "width", "height", "allowfullscreen", "loading" ).onElements( "iframe" ).toFactory( );
    }

    /**
     * Builds the pattern of the iframe sources: an https URL of an allowed video host (property
     * portal.sanitizer.iframe.allowedHosts, comma separated).
     *
     * @return the pattern
     */
    private static Pattern iframeSource( )
    {
        String strHosts = Arrays.stream( AppPropertiesService.getProperty( PROPERTY_IFRAME_HOSTS, DEFAULT_IFRAME_HOSTS ).split( "," ) )
                .map( String::trim ).filter( strHost -> !strHost.isEmpty( ) ).map( Pattern::quote ).collect( Collectors.joining( "|" ) );
        return Pattern.compile( "https://(" + strHosts + ")(/[^\\s\"'<>]*)?" );
    }
}
