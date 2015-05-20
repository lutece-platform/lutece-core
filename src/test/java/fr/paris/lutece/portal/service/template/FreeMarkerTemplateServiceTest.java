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
package fr.paris.lutece.portal.service.template;

import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.test.LuteceTestCase;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Pierre
 */
public class FreeMarkerTemplateServiceTest extends LuteceTestCase
{
    /**
     * Test of init method, of class fr.paris.lutece.portal.service.template.FreeMarkerTemplateService.
     */
    public void testInit(  )
    {
        System.out.println( "init" );

        String strTemplatePath = "WEB-INF/templates/";

        IFreeMarkerTemplateService freeMarkerTemplateService = FreeMarkerTemplateService.getInstance(  );

        freeMarkerTemplateService.init( strTemplatePath );
    }

    /**
     * Test of loadTemplate method, of class fr.paris.lutece.portal.service.template.FreeMarkerTemplateService.
     */
    public void testLoadTemplate(  )
    {
        System.out.println( "loadTemplate" );

        String strPath = "WEB-INF/templates/";
        String strTemplate = "skin/site/portal_footer.html";

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( "web_mail", "lutece@paris.fr" );
        model.put( Markers.PAGE_MAIN_MENU, "menu" );

        IFreeMarkerTemplateService freeMarkerTemplateService = FreeMarkerTemplateService.getInstance(  );
        freeMarkerTemplateService.loadTemplate( strPath, strTemplate, LocaleService.getDefault(  ), model );
    }

    /**
     * Test of resetCache method, of class fr.paris.lutece.portal.service.template.FreeMarkerTemplateService.
     */
    public void testResetCache(  )
    {
        System.out.println( "resetCache" );

        IFreeMarkerTemplateService freeMarkerTemplateService = FreeMarkerTemplateService.getInstance(  );
        freeMarkerTemplateService.resetCache(  );
    }
}
