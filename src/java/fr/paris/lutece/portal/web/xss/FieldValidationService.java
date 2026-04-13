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
package fr.paris.lutece.portal.web.xss;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Pattern;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import fr.paris.lutece.portal.service.html.XSSSanitizerException;
import fr.paris.lutece.portal.service.html.XSSSanitizerService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.string.StringUtil;

/**
 * Provides a per-type, mode-aware validation of user-editable field values.
 *
 * <p>
 * This service is feature-agnostic and can be reused by any form that bypasses the global XSS filter and needs to
 * secure values field by field. It mirrors the two modes of the global XSS filter (see
 * {@code fr.paris.lutece.portal.web.xss.SafeRequestFilter}), the mode being provided by the caller:
 * </p>
 * <ul>
 * <li><b>block mode</b> ({@code bSanitizeMode = false}) &mdash; equivalent to
 * {@code SecurityUtil.containsCleanParameters}: an {@link XSSSanitizerException} is thrown as soon as the value
 * contains one of the forbidden XSS characters, otherwise the value is returned unchanged;</li>
 * <li><b>sanitize mode</b> ({@code bSanitizeMode = true}) &mdash; the value is never rejected; it is checked according
 * to its {@link FieldType}, and when it is not valid a default placeholder is returned ({@code invalid_url},
 * {@code invalid_color}, {@code invalid_email}).</li>
 * </ul>
 *
 * <p>
 * Two distinct strategies are used, and the naming reflects them: URL, color and email values are <b>validated</b>
 * (they are accepted as-is or replaced by a placeholder, never rewritten), whereas HTML content is <b>sanitized</b>
 * (it is rewritten against an allowlist). {@link #sanitizeUrlParameters(String)} is the only URL rewriting operation
 * and it is deliberately kept independent: it is never applied by the type-driven validation, a caller that wants a
 * URL both validated and cleaned has to chain the two calls explicitly.
 * </p>
 *
 * <p>
 * The active mode and the list of forbidden characters are resolved from the admin XSS filter configuration
 * ({@code lutece.safe.request.admin.sanitizeFilterMode} and {@code lutece.safe.request.admin.xssCharacters}) by
 * {@link #validate(FieldType, String)}. A caller that manages the mode itself can use
 * {@link #validate(FieldType, String, boolean, String)} instead.
 * </p>
 */
public final class FieldValidationService
{
    private static final String PROPERTY_XSS_SANITIZE_MODE = "lutece.safe.request.admin.sanitizeFilterMode";
    private static final String PROPERTY_XSS_CHARACTERS = "lutece.safe.request.admin.xssCharacters";
    private static final boolean DEFAULT_XSS_SANITIZE_MODE = true;

    private static final String PROPERTY_COLOR_PATTERN = "portal.sanitizer.color.pattern";
    private static final String DEFAULT_COLOR_PATTERN = "^(#[0-9a-fA-F]{3,8}|rgba?\\([^)]+\\))?$";
    private static final String PROPERTY_URL_DANGEROUS_SCHEMES = "portal.sanitizer.url.dangerousSchemes";
    private static final String DEFAULT_URL_DANGEROUS_SCHEMES = "javascript:,data:,vbscript:";

    private static final String INVALID_URL = "invalid_url";
    private static final String INVALID_COLOR = "invalid_color";
    private static final String INVALID_EMAIL = "invalid_email";

    private static final String QUERY_SEPARATOR = "?";
    private static final String FRAGMENT_SEPARATOR = "#";
    private static final String PARAM_SEPARATOR = "&";
    private static final String KEY_VALUE_SEPARATOR = "=";
    private static final Pattern MARKUP_CHARACTERS = Pattern.compile( "[<>\"'`]" );
    private static final Pattern WHITESPACE = Pattern.compile( "\\s" );

    private static final PolicyFactory HTML_POLICY = Sanitizers.FORMATTING.and( Sanitizers.LINKS ).and( Sanitizers.BLOCKS )
            .and( Sanitizers.IMAGES ).and( Sanitizers.STYLES ).and( Sanitizers.TABLES );

    /**
     * Private constructor. Utility class.
     */
    private FieldValidationService( )
    {
    }

    /**
     * Validates a value according to a field type, resolving the active mode and the forbidden characters from the
     * admin XSS filter configuration ({@value #PROPERTY_XSS_SANITIZE_MODE} and {@value #PROPERTY_XSS_CHARACTERS}).
     *
     * @param type
     *            the field type
     * @param strValue
     *            the value to process, already XSS-bypass-decoded when the field type requires it
     * @return the value to use, validated or unchanged depending on the mode
     * @throws XSSSanitizerException
     *             in block mode, when the value contains a forbidden XSS character
     */
    public static String validate( FieldType type, String strValue ) throws XSSSanitizerException
    {
        boolean bSanitizeMode = AppPropertiesService.getPropertyBoolean( PROPERTY_XSS_SANITIZE_MODE, DEFAULT_XSS_SANITIZE_MODE );
        String strXssCharacters = AppPropertiesService.getProperty( PROPERTY_XSS_CHARACTERS );

        return validate( type, strValue, bSanitizeMode, strXssCharacters );
    }

    /**
     * Validates a value according to a field type and to a caller-provided XSS filter mode.
     *
     * @param type
     *            the field type
     * @param strValue
     *            the value to process, already XSS-bypass-decoded when the field type requires it
     * @param bSanitizeMode
     *            {@code true} to validate the value against its type (never blocks), {@code false} to block on
     *            forbidden characters
     * @param strXssCharacters
     *            the list of forbidden XSS characters, used in block mode
     * @return the value to use, validated or unchanged depending on the mode
     * @throws XSSSanitizerException
     *             in block mode, when the value contains a forbidden XSS character
     */
    public static String validate( FieldType type, String strValue, boolean bSanitizeMode, String strXssCharacters ) throws XSSSanitizerException
    {
        if ( strValue == null || strValue.isEmpty( ) )
        {
            return strValue;
        }

        if ( !bSanitizeMode )
        {
            if ( StringUtil.containsXssCharacters( strValue, strXssCharacters ) )
            {
                throw new XSSSanitizerException( "Value rejected: it contains a forbidden XSS character" );
            }

            return strValue;
        }

        return validateValue( type, strValue );
    }

    /**
     * Applies the strategy dedicated to a field type, returning a default placeholder when the value is not valid.
     * Used in sanitize mode only.
     *
     * @param type
     *            the field type
     * @param strValue
     *            the non-empty value to check
     * @return the validated value, the sanitized HTML, or a default placeholder
     */
    private static String validateValue( FieldType type, String strValue )
    {
        switch( type )
        {
            case URL:
                return validateUrl( strValue );
            case COLOR:
                return validateColor( strValue );
            case EMAIL:
                return validateEmail( strValue );
            case HTML:
                return sanitizeHtml( strValue );
            default:
                return strValue;
        }
    }

    /**
     * Validates a URL: the value is rejected when it uses a dangerous scheme (see
     * {@value #PROPERTY_URL_DANGEROUS_SCHEMES}), when it is not a structurally valid URI, or when one of its query
     * parameter values carries a markup character once URL-decoded. The value is never rewritten: it is returned
     * trimmed when it is valid, and replaced by {@value #INVALID_URL} otherwise. Use
     * {@link #sanitizeUrlParameters(String)} to clean the parameters of a URL instead of rejecting it.
     *
     * @param strValue
     *            the URL value
     * @return the trimmed URL, or {@value #INVALID_URL}
     */
    public static String validateUrl( String strValue )
    {
        if ( strValue == null || strValue.isEmpty( ) )
        {
            return strValue;
        }

        String strTrimmed = strValue.trim( );
        String strNormalized = WHITESPACE.matcher( strTrimmed.toLowerCase( Locale.ROOT ) ).replaceAll( "" );

        for ( String strScheme : AppPropertiesService.getProperty( PROPERTY_URL_DANGEROUS_SCHEMES, DEFAULT_URL_DANGEROUS_SCHEMES ).split( "," ) )
        {
            String strTrimmedScheme = strScheme.trim( ).toLowerCase( Locale.ROOT );

            if ( !strTrimmedScheme.isEmpty( ) && strNormalized.startsWith( strTrimmedScheme ) )
            {
                AppLogService.error( "FieldValidationService: rejected URL with dangerous scheme '{}'", strValue );
                return INVALID_URL;
            }
        }

        try
        {
            new URI( strTrimmed );
        }
        catch( URISyntaxException | IllegalArgumentException e )
        {
            AppLogService.error( "FieldValidationService: rejected malformed URL '{}'", strValue, e );
            return INVALID_URL;
        }

        if ( containsMarkupInParameters( strTrimmed ) )
        {
            AppLogService.error( "FieldValidationService: rejected URL with markup in a query parameter '{}'", strValue );
            return INVALID_URL;
        }

        return strTrimmed;
    }

    /**
     * Rebuilds a complete URL by cleaning the values of its query parameters, leaving the base path, the parameter
     * names and the fragment untouched. Each value is URL-decoded, stripped of its markup characters and re-encoded,
     * so that the URL structure ({@code ?}, {@code &}, {@code =}, fragment) is preserved.
     *
     * <p>
     * This method performs no structural nor scheme validation: it is independent from
     * {@link #validateUrl(String)} and can be applied to any URL, valid or not.
     * </p>
     *
     * @param strUrl
     *            the complete URL
     * @return the URL with cleaned query parameter values, unchanged when it carries no query string
     */
    public static String sanitizeUrlParameters( String strUrl )
    {
        if ( strUrl == null || strUrl.isEmpty( ) )
        {
            return strUrl;
        }

        int nQueryIndex = strUrl.indexOf( QUERY_SEPARATOR );

        if ( nQueryIndex < 0 )
        {
            return strUrl;
        }

        String strBase = strUrl.substring( 0, nQueryIndex );
        String strQueryAndFragment = strUrl.substring( nQueryIndex + 1 );

        int nFragmentIndex = strQueryAndFragment.indexOf( FRAGMENT_SEPARATOR );
        String strFragment = nFragmentIndex >= 0 ? strQueryAndFragment.substring( nFragmentIndex ) : "";
        String strQuery = nFragmentIndex >= 0 ? strQueryAndFragment.substring( 0, nFragmentIndex ) : strQueryAndFragment;

        StringBuilder sbQuery = new StringBuilder( );

        for ( String strPair : strQuery.split( Pattern.quote( PARAM_SEPARATOR ) ) )
        {
            if ( sbQuery.length( ) > 0 )
            {
                sbQuery.append( PARAM_SEPARATOR );
            }

            int nEqualIndex = strPair.indexOf( KEY_VALUE_SEPARATOR );

            if ( nEqualIndex < 0 )
            {
                sbQuery.append( strPair );
            }
            else
            {
                sbQuery.append( strPair, 0, nEqualIndex + 1 ).append( sanitizeParameterValue( strPair.substring( nEqualIndex + 1 ) ) );
            }
        }

        return strBase + QUERY_SEPARATOR + sbQuery + strFragment;
    }

    /**
     * Indicates whether one of the query parameter values of a URL carries a markup character once URL-decoded.
     *
     * @param strUrl
     *            the URL to inspect
     * @return {@code true} when at least one parameter value carries a markup character
     */
    private static boolean containsMarkupInParameters( String strUrl )
    {
        int nQueryIndex = strUrl.indexOf( QUERY_SEPARATOR );

        if ( nQueryIndex < 0 )
        {
            return false;
        }

        String strQueryAndFragment = strUrl.substring( nQueryIndex + 1 );
        int nFragmentIndex = strQueryAndFragment.indexOf( FRAGMENT_SEPARATOR );
        String strQuery = nFragmentIndex >= 0 ? strQueryAndFragment.substring( 0, nFragmentIndex ) : strQueryAndFragment;

        for ( String strPair : strQuery.split( Pattern.quote( PARAM_SEPARATOR ) ) )
        {
            int nEqualIndex = strPair.indexOf( KEY_VALUE_SEPARATOR );

            if ( nEqualIndex >= 0 && MARKUP_CHARACTERS.matcher( decodeParameterValue( strPair.substring( nEqualIndex + 1 ) ) ).find( ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Cleans a single query parameter value by decoding it, stripping any markup character and re-encoding it.
     *
     * @param strValue
     *            the raw (URL-encoded) parameter value
     * @return the cleaned and re-encoded parameter value
     */
    private static String sanitizeParameterValue( String strValue )
    {
        String strCleaned = MARKUP_CHARACTERS.matcher( decodeParameterValue( strValue ) ).replaceAll( "" );

        return URLEncoder.encode( strCleaned, StandardCharsets.UTF_8 );
    }

    /**
     * URL-decodes a query parameter value, falling back to the raw value when the encoding is malformed.
     *
     * @param strValue
     *            the raw (URL-encoded) parameter value
     * @return the decoded value, or the raw value when it cannot be decoded
     */
    private static String decodeParameterValue( String strValue )
    {
        try
        {
            return URLDecoder.decode( strValue, StandardCharsets.UTF_8 );
        }
        catch( IllegalArgumentException e )
        {
            AppLogService.error( "FieldValidationService: malformed URL encoding in a query parameter '{}'", strValue, e );
            return strValue;
        }
    }

    /**
     * Validates a CSS color value against the configured pattern. Returns {@value #INVALID_COLOR} when the format is
     * not recognized.
     *
     * @param strValue
     *            the color value
     * @return the color value, or {@value #INVALID_COLOR}
     */
    public static String validateColor( String strValue )
    {
        if ( strValue == null || strValue.isEmpty( ) )
        {
            return strValue;
        }

        Pattern pattern = Pattern.compile( AppPropertiesService.getProperty( PROPERTY_COLOR_PATTERN, DEFAULT_COLOR_PATTERN ) );

        return pattern.matcher( strValue ).matches( ) ? strValue : INVALID_COLOR;
    }

    /**
     * Validates an email value against the email format. Returns {@value #INVALID_EMAIL} when the value is not a valid
     * email.
     *
     * @param strValue
     *            the email value
     * @return the email value, or {@value #INVALID_EMAIL}
     */
    public static String validateEmail( String strValue )
    {
        if ( strValue == null || strValue.isEmpty( ) )
        {
            return strValue;
        }

        return StringUtil.checkEmail( strValue ) ? strValue : INVALID_EMAIL;
    }

    /**
     * Sanitizes an HTML content with the XSS sanitizer allowlist, falling back to a local allowlist when the advanced
     * sanitizer is unavailable or fails.
     *
     * @param strValue
     *            the HTML value
     * @return the sanitized HTML
     */
    public static String sanitizeHtml( String strValue )
    {
        if ( strValue == null || strValue.isEmpty( ) )
        {
            return strValue;
        }

        try
        {
            return XSSSanitizerService.sanitize( strValue );
        }
        catch( XSSSanitizerException e )
        {
            AppLogService.error( "FieldValidationService: HTML sanitization failed, falling back to the local allowlist", e );
            return HTML_POLICY.sanitize( strValue ).replace( "&#64;", "@" ).replace( "&#43;", "+" );
        }
    }
}
