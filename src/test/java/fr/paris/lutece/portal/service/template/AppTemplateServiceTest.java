package fr.paris.lutece.portal.service.template;

import fr.paris.lutece.portal.business.template.CommonsInclude;
import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.html.HtmlTemplate;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

/**
 *
 * @author Pierre
 */
public class AppTemplateServiceTest extends LuteceTestCase
{

	private static final String REFERENCE_TEMPLATE = "reference.html";
	private static final String HTML_EXTENSION = ".html";
	final private static String TEST_TEMPLATES_PATH = "commons/templates/test/";

	@Test
	public void testCommonsTemplates( ) throws IOException, TemplateException 
	{

		String classPath = getClass( ).getProtectionDomain( ).getCodeSource( ).getLocation( ).getPath( );


		for (CommonsInclude ci : CommonsService.getCommonsIncludes( ) ) 
		{
			try 
			{
				CommonsService.activateCommons( ci.getKey( ) );
				String ciKey = ci.getKey( );
				Map<String, Object> model = new HashMap<>( );
				
				AppTemplateService.resetCache( );

				String strReferenceTemplate = readFile( classPath + TEST_TEMPLATES_PATH + REFERENCE_TEMPLATE, StandardCharsets.UTF_8 );
				HtmlTemplate generated_template =  AppTemplateService.getTemplateFromStringFtl( strReferenceTemplate, LocaleService.getDefault( ), model );
				
				assertNotNull( "AppTemplateServiceTest freemarker lib :  "  + ciKey, generated_template.getHtml( ) );
			}
			catch ( IOException e )
			{
				fail( e.getMessage( ) );
			}
		}

	}

	/**
	 * read file
	 *
	 * @param path
	 * @param encoding
	 * @return the file as string
	 * @throws IOException
	 */
	static String readFile( String path, Charset encoding )
			throws IOException
	{
		byte[] encoded = Files.readAllBytes( Paths.get( path ) );
		return new String( encoded, encoding );
	}


}
