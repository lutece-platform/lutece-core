/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.service.template;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;

import fr.paris.lutece.portal.service.util.AppLogService;
import freemarker.core.Environment;
import freemarker.core.HTMLOutputFormat;
import freemarker.core.TemplateMarkupOutputModel;
import freemarker.template.Template;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

/**
 * The <code>htmlAttributes( macroAttributes, params )</code> function of the templates: writes the attributes of a macro
 * element, as markup that starts with a space before each attribute. It is the single place where the attributes of the
 * macros are written (LUT-33387, LUT-33153):
 * <ul>
 * <li><code>macroAttributes</code> is the map the macro builds itself (id, class, role, data-*...): its names are only
 * checked for syntax;</li>
 * <li><code>params</code> is the map the caller of the macro passes: its names must be in the allow list (aria-*, data-*
 * and a fixed set of HTML attributes), so it never carries an event handler, a URL or a style;</li>
 * <li>the classes of both maps are concatenated, without duplicates; any other attribute given by both is the macro's;</li>
 * <li>every value is escaped, a markup value included: true writes the name alone, false and null write nothing, a
 * number is written as a computer number.</li>
 * </ul>
 * A refused attribute is left out and logged as a warning, with the attribute name and the template but never the value;
 * in strict mode (tests and continuous integration) it fails the rendering instead. A string or a markup given as
 * <code>params</code> always fails the rendering: params is a map.
 */
public class HtmlAttributesMethod implements TemplateMethodModelEx
{
    private static final String ATTRIBUTE_CLASS = "class";
    private static final Pattern NAME_SYNTAX = Pattern.compile( "[A-Za-z_][-A-Za-z0-9_.]*" );
    private static final Pattern NAME_ARIA = Pattern.compile( "aria-[a-z]+" );
    private static final Pattern NAME_DATA = Pattern.compile( "data-[a-z0-9]+(-[a-z0-9]+)*" );
    private static final Set<String> DATA_REFUSED = Set.of( "data-bs-sanitize", "data-bs-allow-list" );
    private static final Set<String> NAMES_ALLOWED = Set.of( "title", "name", "value", "id", "role", "tabindex", "hidden", "accesskey", "lang", "dir",
            "translate", "spellcheck", "draggable", "autocomplete", "autofocus", "autocapitalize", "inputmode", "enterkeyhint", "placeholder", "pattern",
            "required", "readonly", "disabled", "multiple", "min", "max", "step", "minlength", "maxlength", "size", "list", "accept", "accept-charset",
            "enctype", "method", "novalidate", "formnovalidate", "target", "rel", "download", "alt", "width", "height", "loading", "decoding", "colspan",
            "rowspan", "scope", "headers", "form", "open", "wrap", "rows", "cols", "selected", "checked" );

    private final boolean _bStrict;

    /**
     * Creates the function.
     *
     * @param bStrict
     *            true to fail the rendering on a refused attribute (tests, continuous integration), false to leave it out
     *            with a warning (production)
     */
    public HtmlAttributesMethod( boolean bStrict )
    {
        _bStrict = bStrict;
    }

    /**
     * Writes the attributes of the arguments: <code>( params )</code> or <code>( macroAttributes, params )</code>.
     *
     * @param arguments
     *            the maps of attributes
     * @return the attributes as HTML markup, empty when there is none
     * @throws TemplateModelException
     *             if an argument is not a map, or if an attribute is refused in strict mode
     */
    @Override
    public Object exec( @SuppressWarnings( "rawtypes" ) List arguments ) throws TemplateModelException
    {
        if ( arguments.isEmpty( ) || arguments.size( ) > 2 )
        {
            throw new TemplateModelException( "htmlAttributes( macroAttributes, params ) takes one or two maps" );
        }
        Map<String, TemplateModel> macroAttributes = arguments.size( ) == 2 ? toMap( (TemplateModel) arguments.get( 0 ), "macroAttributes" )
                : new LinkedHashMap<>( );
        Map<String, TemplateModel> params = toMap( (TemplateModel) arguments.get( arguments.size( ) - 1 ), "params" );

        Map<String, String> attributes = new LinkedHashMap<>( );
        Set<String> classes = new LinkedHashSet<>( );
        for ( Map.Entry<String, TemplateModel> entry : macroAttributes.entrySet( ) )
        {
            if ( isValidSyntax( entry.getKey( ) ) )
            {
                add( attributes, classes, entry.getKey( ), entry.getValue( ), true );
            }
            else
            {
                refuse( entry.getKey( ), "is not a valid attribute name" );
            }
        }
        Set<String> macroNames = new LinkedHashSet<>( attributes.keySet( ) );
        for ( Map.Entry<String, TemplateModel> entry : params.entrySet( ) )
        {
            String strName = entry.getKey( );
            if ( !isAllowed( strName ) )
            {
                refuse( strName, "is not an allowed attribute of params" );
            }
            else
                if ( !ATTRIBUTE_CLASS.equals( strName.toLowerCase( Locale.ROOT ) ) && macroNames.contains( strName.toLowerCase( Locale.ROOT ) ) )
                {
                    refuse( strName, "is already written by the macro" );
                }
                else
                {
                    add( attributes, classes, strName, entry.getValue( ), false );
                }
        }

        return HTMLOutputFormat.INSTANCE.fromMarkup( write( attributes, classes ) );
    }

    /**
     * Reads a map argument. An empty string is read as an empty map, so a macro whose params default is '' writes
     * nothing; any other string, and any markup, is refused.
     *
     * @param model
     *            the argument
     * @param strArgument
     *            the name of the argument, for the error message
     * @return the entries of the map, in their order
     * @throws TemplateModelException
     *             if the argument is not a map
     */
    private static Map<String, TemplateModel> toMap( TemplateModel model, String strArgument ) throws TemplateModelException
    {
        Map<String, TemplateModel> map = new LinkedHashMap<>( );
        if ( model == null || ( model instanceof TemplateScalarModel && ( (TemplateScalarModel) model ).getAsString( ).isEmpty( ) ) )
        {
            return map;
        }
        if ( !( model instanceof TemplateHashModelEx ) || model instanceof TemplateScalarModel || model instanceof TemplateMarkupOutputModel )
        {
            throw new TemplateModelException( "htmlAttributes: " + strArgument + " must be a map such as {'aria-label': label}, not a "
                    + model.getClass( ).getSimpleName( ) + templateSuffix( ) );
        }
        TemplateHashModelEx hash = (TemplateHashModelEx) model;
        TemplateModelIterator keys = hash.keys( ).iterator( );
        while ( keys.hasNext( ) )
        {
            String strKey = ( (TemplateScalarModel) keys.next( ) ).getAsString( );
            map.put( strKey, hash.get( strKey ) );
        }
        return map;
    }

    /**
     * Adds an attribute: a class is gathered with the others, any other attribute keeps its first value.
     *
     * @param attributes
     *            the attributes written so far, by lower-case name, valued with their escaped value or null for a name
     *            written alone
     * @param classes
     *            the classes gathered so far
     * @param strName
     *            the attribute name
     * @param value
     *            the attribute value
     * @param bFromMacro
     *            true for an attribute of the macro, whose empty value means its parameter was not given and writes
     *            nothing; an empty value of params is written (alt="")
     * @throws TemplateModelException
     *             if the value cannot be written and the mode is strict
     */
    private void add( Map<String, String> attributes, Set<String> classes, String strName, TemplateModel value, boolean bFromMacro )
            throws TemplateModelException
    {
        String strKey = strName.toLowerCase( Locale.ROOT );
        if ( ATTRIBUTE_CLASS.equals( strKey ) )
        {
            String strClasses = toText( strName, value );
            if ( strClasses != null )
            {
                for ( String strClass : strClasses.trim( ).split( "\\s+" ) )
                {
                    if ( !strClass.isEmpty( ) )
                    {
                        classes.add( strClass );
                    }
                }
            }
            return;
        }
        if ( attributes.containsKey( strKey ) )
        {
            return;
        }
        if ( value instanceof TemplateBooleanModel )
        {
            if ( ( (TemplateBooleanModel) value ).getAsBoolean( ) )
            {
                attributes.put( strKey, null );
            }
            return;
        }
        String strValue = toText( strName, value );
        if ( strValue != null && !( bFromMacro && strValue.isEmpty( ) ) )
        {
            attributes.put( strKey, strValue );
        }
    }

    /**
     * Converts a value to the text of an attribute, before escaping.
     *
     * @param strName
     *            the attribute name, for the message of a refused value
     * @param value
     *            the value: a string, a markup (read back as text), a number, or null
     * @return the text, or null when nothing is written
     * @throws TemplateModelException
     *             if the value cannot be written and the mode is strict
     */
    private String toText( String strName, TemplateModel value ) throws TemplateModelException
    {
        if ( value == null )
        {
            return null;
        }
        if ( value instanceof TemplateMarkupOutputModel )
        {
            return StringEscapeUtils.unescapeHtml4( markupString( (TemplateMarkupOutputModel<?>) value ) );
        }
        if ( value instanceof TemplateNumberModel )
        {
            return toComputerNumber( ( (TemplateNumberModel) value ).getAsNumber( ) );
        }
        if ( value instanceof TemplateScalarModel )
        {
            return ( (TemplateScalarModel) value ).getAsString( );
        }
        refuse( strName, "has a value that is neither a string, a number nor a boolean" );
        return null;
    }

    /**
     * Reads the source of a markup value.
     *
     * @param model
     *            the markup
     * @return its markup string
     * @throws TemplateModelException
     *             if the markup cannot be read
     */
    @SuppressWarnings( {
            "unchecked", "rawtypes"
    } )
    private static String markupString( TemplateMarkupOutputModel model ) throws TemplateModelException
    {
        return model.getOutputFormat( ).getMarkupString( model );
    }

    /**
     * Writes a number the way the ?c built-in does: no grouping, a dot as decimal separator, no trailing zero.
     *
     * @param number
     *            the number
     * @return its computer representation
     */
    private static String toComputerNumber( Number number )
    {
        try
        {
            return new BigDecimal( number.toString( ) ).stripTrailingZeros( ).toPlainString( );
        }
        catch( NumberFormatException e )
        {
            return number.toString( );
        }
    }

    /**
     * Writes the attributes, the class first, each one preceded by a space, with escaped values.
     *
     * @param attributes
     *            the attributes by name, valued with their text or null for a name written alone
     * @param classes
     *            the classes
     * @return the HTML of the attributes
     */
    private static String write( Map<String, String> attributes, Set<String> classes )
    {
        StringBuilder sbHtml = new StringBuilder( );
        String strId = attributes.get( "id" );
        if ( strId != null )
        {
            sbHtml.append( " id=\"" ).append( escape( strId ) ).append( '"' );
        }
        if ( !classes.isEmpty( ) )
        {
            sbHtml.append( " class=\"" ).append( escape( String.join( " ", classes ) ) ).append( '"' );
        }
        for ( Map.Entry<String, String> attribute : attributes.entrySet( ) )
        {
            if ( "id".equals( attribute.getKey( ) ) )
            {
                continue;
            }
            sbHtml.append( ' ' ).append( attribute.getKey( ) );
            if ( attribute.getValue( ) != null )
            {
                sbHtml.append( "=\"" ).append( escape( attribute.getValue( ) ) ).append( '"' );
            }
        }
        return sbHtml.toString( );
    }

    /**
     * Escapes a text for an attribute value, as the HTML auto-escaping does.
     *
     * @param strText
     *            the text
     * @return the escaped text
     */
    private static String escape( String strText )
    {
        return HTMLOutputFormat.INSTANCE.escapePlainText( strText );
    }

    /**
     * Tells whether a name has the syntax of an attribute name.
     *
     * @param strName
     *            the name
     * @return true if it is a valid name
     */
    private static boolean isValidSyntax( String strName )
    {
        return NAME_SYNTAX.matcher( strName ).matches( );
    }

    /**
     * Tells whether a caller may give an attribute through params: aria-*, data-* (but the ones that turn off the
     * Bootstrap sanitizer), the class and the fixed set of HTML attributes. Event handlers, URLs and styles never are.
     *
     * @param strName
     *            the name
     * @return true if it is allowed
     */
    static boolean isAllowed( String strName )
    {
        String strKey = strName.toLowerCase( Locale.ROOT );
        if ( !isValidSyntax( strName ) )
        {
            return false;
        }
        if ( NAME_DATA.matcher( strKey ).matches( ) )
        {
            return !DATA_REFUSED.contains( strKey );
        }
        return ATTRIBUTE_CLASS.equals( strKey ) || NAME_ARIA.matcher( strKey ).matches( ) || NAMES_ALLOWED.contains( strKey );
    }

    /**
     * Refuses an attribute: fails the rendering in strict mode, logs a warning otherwise. The value is never logged.
     *
     * @param strName
     *            the attribute name
     * @param strReason
     *            why it is refused
     * @throws TemplateModelException
     *             in strict mode
     */
    private void refuse( String strName, String strReason ) throws TemplateModelException
    {
        if ( _bStrict )
        {
            throw new TemplateModelException( "htmlAttributes: the attribute '" + strName + "' " + strReason + templateSuffix( ) );
        }
        AppLogService.getLogger( ).warn( "htmlAttributes: the attribute '{}' {}{}", strName, strReason, templateSuffix( ) );
    }

    /**
     * Names the template being rendered, for the messages.
     *
     * @return the suffix naming the template, empty outside a rendering
     */
    private static String templateSuffix( )
    {
        Environment environment = Environment.getCurrentEnvironment( );
        if ( environment == null )
        {
            return "";
        }
        Template template = environment.getMainTemplate( );
        return template == null ? "" : " (template " + template.getSourceName( ) + ")";
    }
}
