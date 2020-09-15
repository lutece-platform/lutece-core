/*
 * Copyright (c) 2002-2020, City of Paris
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

import fr.paris.lutece.portal.business.template.CommonsInclude;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.portal.web.template.MockFreemarkerTemplateService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Pierre
 */
public class AppTemplateServiceTest extends LuteceTestCase
{

	private static String TEMPLATES_FOLDER = "templates/test";
	private static String REFERENCE_TEMPLATE = "reference.html";
	private static String EXPECTED_PREFIX = "expected_";
	private static String HTML_EXTENSION = ".html";

	@Test
	public void testCommonsTemplates() throws IOException, TemplateException {

		String classPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String templatesPath = "commons/templates/test/" ;


		for (CommonsInclude ci : CommonsService.getCommonsIncludes()) {
			try {
				CommonsService.activateCommons(ci.getKey());
				String ciName = ci.getName( );

				Map<String, Object> model = new HashMap<>();

				AppTemplateService.resetCache( );
				AppTemplateService.resetConfiguration( );
				AppTemplateService.initAutoIncludes( );

				String reference_template = readFile(classPath + templatesPath + REFERENCE_TEMPLATE, StandardCharsets.UTF_8);
				HtmlTemplate generated_template =  AppTemplateService.getTemplateFromStringFtl( reference_template, LocaleService.getDefault( ), model );
				String expected_template = readFile(classPath + templatesPath + EXPECTED_PREFIX + ciName + HTML_EXTENSION, StandardCharsets.UTF_8);

				assertEquals( generated_template.getHtml( ), expected_template );
			} catch ( Exception e ) {
				fail( e.getMessage());
			}
		}

	}
	static String readFile(String path, Charset encoding)
			throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}


}