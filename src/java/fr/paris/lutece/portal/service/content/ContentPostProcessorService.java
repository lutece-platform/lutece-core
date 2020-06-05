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
package fr.paris.lutece.portal.service.content;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import javax.servlet.http.HttpServletRequest;

/**
 * Content Post Processor Service
 */
public final class ContentPostProcessorService
{
    /**
     * Instantiates a new content post processor service.
     */
    private ContentPostProcessorService( )
    {
    }

    /**
     * Initialize the service
     */
    public static void init( )
    {
        for ( ContentPostProcessor processor : SpringContextService.getBeansOfType( ContentPostProcessor.class ) )
        {
            AppLogService.info( "New Content Post Processor registered : " + processor.getName( ) );
        }
    }

    /**
     * Has processor registered. Use this method before processing for performance issue.
     * 
     * @return True if there at least one processor registered
     */
    public static boolean hasProcessor( )
    {
        return !SpringContextService.getBeansOfType( ContentPostProcessor.class ).isEmpty( );
    }

    /**
     * Process a content
     * 
     * @param request
     *            The HTTP Request
     * @param strContent
     *            The content to process
     * @return The processed content
     */
    public static String process( HttpServletRequest request, String strContent )
    {
        String strProcessed = strContent;

        for ( ContentPostProcessor processor : SpringContextService.getBeansOfType( ContentPostProcessor.class ) )
        {
            strProcessed = processor.process( request, strProcessed );
        }

        return strProcessed;
    }
}
