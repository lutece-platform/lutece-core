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
package fr.paris.lutece.portal.web.includes;

import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.init.AppInfo;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class includes metas into the head of the HTML page
 */
public class MetasInclude implements PageInclude
{
    private static final String KEY_WEBMASTER_META_AUTHOR = "portal.site.site_property.meta.author";
    private static final String KEY_WEBMASTER_META_COPYRIGHT = "portal.site.site_property.meta.copyright";
    private static final String KEY_WEBMASTER_META_KEYWORDS = "portal.site.site_property.meta.keywords";
    private static final String KEY_WEBMASTER_META_DESCRIPTION = "portal.site.site_property.meta.description";
    private static final String MARK_PAGE_HEAD_META_AUTHOR = "meta_author";
    private static final String MARK_PAGE_HEAD_META_COPYRIGHT = "meta_copyright";
    private static final String MARK_PAGE_HEAD_META_KEYWORDS = "meta_keywords";
    private static final String MARK_PAGE_HEAD_META_DESCRIPTION = "meta_description";
    private static final String MARK_PAGE_HEAD_META_GENERATOR = "meta_generator";

    /**
     * Substitue specific Freemarker markers in the page template.
     * @param rootModel the HashMap containing markers to substitute
     * @param data A PageData object containing applications data
     * @param nMode The current mode
     * @param request The HTTP request
     */
    @Override
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        String strMetaAuthor = ( data.getMetaAuthor(  ) != null ) ? data.getMetaAuthor(  )
                                                                  : DatastoreService.getDataValue( KEY_WEBMASTER_META_AUTHOR,
                "" );
        String strMetaCopyright = ( data.getMetaCopyright(  ) != null ) ? data.getMetaCopyright(  )
                                                                        : DatastoreService.getDataValue( KEY_WEBMASTER_META_COPYRIGHT,
                "" );
        String strMetaKeywords = ( ( data.getMetaKeywords(  ) != null ) && ( data.getMetaKeywords(  ).length(  ) > 0 ) )
            ? ( DatastoreService.getDataValue( KEY_WEBMASTER_META_KEYWORDS, "" ) + ", " + data.getMetaKeywords(  ) )
            : DatastoreService.getDataValue( KEY_WEBMASTER_META_KEYWORDS, "" );
        String strMetaDescription = ( ( data.getMetaDescription(  ) != null ) &&
            ( data.getMetaDescription(  ).length(  ) > 0 ) ) ? data.getMetaDescription(  )
                                                             : DatastoreService.getDataValue( KEY_WEBMASTER_META_DESCRIPTION,
                "" );

        rootModel.put( MARK_PAGE_HEAD_META_AUTHOR, strMetaAuthor );
        rootModel.put( MARK_PAGE_HEAD_META_COPYRIGHT, strMetaCopyright );
        rootModel.put( MARK_PAGE_HEAD_META_KEYWORDS, strMetaKeywords );
        rootModel.put( MARK_PAGE_HEAD_META_DESCRIPTION, strMetaDescription );
        rootModel.put( MARK_PAGE_HEAD_META_GENERATOR, "LUTECE " + AppInfo.getVersion(  ) );
    }
}
