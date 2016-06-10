/*
 * Copyright (c) 2015, Mairie de Paris
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.html.HtmlTemplate;

public class AppTemplateServiceTest extends LuteceTestCase
{

    public void testJadeTemplate(  )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( "variable", "value" );
        HtmlTemplate template = AppTemplateService.getTemplate( "../../../test-classes/AppTemplateServiceTest.jade", Locale.FRANCE, model );
        assertEquals( "<div class=\"test\"><p>variable=value</p></div>", template.getHtml(  ) );
    }

    public void testJadeInFreemarkerTemplate(  ) throws IOException
    {
        File templateFile = new File( getResourcesDir(  ), "../test-classes/AppTemplateServiceTest.html" );
        File dest = new File( getResourcesDir(  ), "WEB-INF/templates/AppTemplateServiceTest.html" );
        try
        {
            Files.copy( templateFile.toPath(  ), dest.toPath(  ), StandardCopyOption.REPLACE_EXISTING );
            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( "variable", "value" );
            HtmlTemplate template = AppTemplateService.getTemplate( "AppTemplateServiceTest.html", Locale.FRANCE, model );
            assertEquals( "value\n<div class=\"test\"><p>variable=value</p></div>", template.getHtml(  ) );
        } finally
        {
            dest.delete(  );
        }
    }

}
