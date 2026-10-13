import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.core.TemplateClassResolver;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Renders every macro call of a call file against a templates tree, with the configuration
 * AbstractFreeMarkerTemplateService builds (2.3.0 compatibility, core auto-includes, optional
 * auto-escaping). Usage: MacroHarness <templatesDir> <calls.jsonl> <outDir> <autoescape>
 */
public class MacroHarness
{
    private static final Pattern FIELD = Pattern.compile( "\"(macro|variant|ftl)\": \"((?:[^\"\\\\]|\\\\.)*)\"" );

    public static void main( String [ ] args ) throws Exception
    {
        System.setProperty( "org.freemarker.loggerLibrary", "none" );
        File templates = new File( args [0] );
        Path out = Path.of( args [2] );
        boolean autoEscape = Boolean.parseBoolean( args [3] );

        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setNewBuiltinClassResolver( TemplateClassResolver.ALLOWS_NOTHING_RESOLVER );
        cfg.setAPIBuiltinEnabled( false );
        cfg.setDefaultEncoding( "UTF-8" );
        cfg.setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );
        cfg.setLogTemplateExceptions( false );
        List<String> includes = System.getenv( "HARNESS_INCLUDES" ) != null ? List.of( System.getenv( "HARNESS_INCLUDES" ).split( "," ) )
                : List.of( "commons_site.html", "skin/themes/global_theme_commons.ftl", "commons_bs5_tabler.html", "commons_backport.html",
                        "admin/util/calendar/macro_datetimepicker.html" );
        for ( String inc : includes )
        {
            cfg.addAutoInclude( inc );
        }
        TemplateMethodModelEx dskey = a -> "DS Value Missing";
        TemplateMethodModelEx i18n = a -> "#i18n{" + a.get( 0 ) + "}";
        cfg.setSharedVariable( "dskey", dskey );
        cfg.setSharedVariable( "i18n", i18n );
        cfg.setLazyAutoImports( true );
        cfg.setLocalizedLookup( false );
        cfg.setNumberFormat( "0.######" );
        if ( autoEscape )
        {
            cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
            cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        }
        StringTemplateLoader strings = new StringTemplateLoader( );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] { new FileTemplateLoader( templates ), strings } ) );

        Map<String, StringBuilder> results = new LinkedHashMap<>( );
        int n = 0, errors = 0;
        for ( String line : Files.readAllLines( Path.of( args [1] ), StandardCharsets.UTF_8 ) )
        {
            Map<String, String> call = new HashMap<>( );
            Matcher m = FIELD.matcher( line );
            while ( m.find( ) )
            {
                call.put( m.group( 1 ), m.group( 2 ).replace( "\\\"", "\"" ).replace( "\\\\", "\\" ) );
            }
            String name = "call_" + ( n++ ) + ".ftl";
            strings.putTemplate( name, call.get( "ftl" ) );
            String rendered;
            try
            {
                Template t = cfg.getTemplate( name );
                StringWriter w = new StringWriter( );
                t.process( new HashMap<String, Object>( ), w );
                rendered = w.toString( );
            }
            catch( Exception e )
            {
                errors++;
                String msg = String.valueOf( e.getMessage( ) );
                int cut = msg.indexOf( "----" );
                msg = ( cut > 0 ? msg.substring( 0, cut ) : msg ).replaceAll( "(?s)in template \"[^\"]*\".*", "" )
                        .replaceAll( "line \\d+, column \\d+", "L" ).replaceAll( "(baseDir|canonicalBasePath)=\"[^\"]*\"", "$1" ).replaceAll( "\\s+", " " ).trim( );
                rendered = "!! " + e.getClass( ).getSimpleName( ) + ": " + msg;
            }
            results.computeIfAbsent( call.get( "macro" ), k -> new StringBuilder( ) ).append( "### " ).append( call.get( "variant" ) ).append( '\n' )
                    .append( "# " ).append( call.get( "ftl" ) ).append( '\n' ).append( rendered ).append( "\n\n" );
        }
        Files.createDirectories( out );
        for ( Map.Entry<String, StringBuilder> e : results.entrySet( ) )
        {
            Files.writeString( out.resolve( e.getKey( ) + ".txt" ), e.getValue( ).toString( ), StandardCharsets.UTF_8 );
        }
        System.out.println( n + " calls, " + errors + " errors -> " + out );
    }
}
