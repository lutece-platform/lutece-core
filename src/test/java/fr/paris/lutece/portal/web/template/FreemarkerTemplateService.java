/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.portal.web.template;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AbstractFreeMarkerTemplateService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * FreemarkerTemplateService
 */
public class FreemarkerTemplateService extends AbstractFreeMarkerTemplateService
{

    private String _strAbsolutePath;

    /**
     * Constructor
     * @param strAbsolutePath 
     */
    public FreemarkerTemplateService( String strAbsolutePath )
    {
        _strAbsolutePath = strAbsolutePath;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getAbsolutePathFromRelativePath( String strPath )
    {
        return _strAbsolutePath + strPath;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getDefaultPattern( Locale locale )
    {
        return DateUtil.getDefaultPattern( locale );
    }



    /**
     * {@inheritDoc }
     */
    @Override
    public void init( String strTemplatePath )
    {
        super.init( strTemplatePath ); 
        getAutoIncludes( ); // force to initialize a cfg
    }
    
    /**
     * Write a file 
     * @param strTemplateFilename The template filename
     * @param strOutputFolder The output directory
     * @param model The model
     * @throws IOException if an error occurs
     */
    void write( String strTemplateFilename, String strOutputFolder, Map<String, Object> model ) throws IOException
    {
        Locale locale = Locale.getDefault();
        HtmlTemplate template = loadTemplate( "/", strTemplateFilename, locale, model );
        String strLocalized = I18nService.localize( template.getHtml(), locale );
        template = new HtmlTemplate( strLocalized );
        template = new HtmlTemplate( DatastoreService.replaceKeys( template.getHtml() ) );

        String strFileName = strOutputFolder + "/" + strTemplateFilename;
        FileWriter writer = new FileWriter( strFileName );
        writer.write( template.getHtml() );
        writer.close();
    }

}
