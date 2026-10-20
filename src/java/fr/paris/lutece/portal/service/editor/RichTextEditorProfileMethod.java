/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.portal.service.editor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.portal.business.editor.EditorProfile;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.util.AppLogService;
import freemarker.core.Environment;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateScalarModel;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Template function richTextEditorProfile( [plugin] ): the editor of the back office and its profile for the rendered template and the connected user, as a
 * hash { plugin, name, editor, config }. editor is the name of the editor of the back office, name and config the ones of its profile (config is {} when
 * the editor has no profile). Without argument, the plugin is the one of the rendered template, or of the requested JSP when the template belongs to the core.
 */
public class RichTextEditorProfileMethod implements TemplateMethodModelEx
{
    private static final String KEY_NAME = "name";
    private static final String KEY_EDITOR = "editor";
    private static final String KEY_CONFIG = "config";
    private static final String KEY_PLUGIN = "plugin";

    /**
     * {@inheritDoc}
     */
    @Override
    public Object exec( List arguments ) throws TemplateModelException
    {
        HttpServletRequest request = getRequest( );
        String strPlugin = getPlugin( arguments, request );
        AdminUser user = ( request != null ) ? AdminUserService.getAdminUser( request ) : null;
        String strEditorName = RichTextEditorService.getBackOfficeDefaultEditor( );
        EditorProfile profile = CDI.current( ).select( EditorProfileService.class ).get( ).getProfile( strEditorName, strPlugin, user );

        Map<String, String> mapProfile = new HashMap<>( );
        mapProfile.put( KEY_PLUGIN, strPlugin );
        mapProfile.put( KEY_NAME, ( profile != null ) ? profile.getName( ) : StringUtils.EMPTY );
        mapProfile.put( KEY_EDITOR, StringUtils.defaultString( strEditorName ) );
        mapProfile.put( KEY_CONFIG, ( profile != null && StringUtils.isNotBlank( profile.getConfig( ) ) ) ? profile.getConfig( ) : EditorProfile.EMPTY_CONFIG );

        return mapProfile;
    }

    /**
     * The plugin given as argument, or the one of the rendered template, or the one of the request
     *
     * @param arguments
     *            the arguments of the function
     * @param request
     *            the request, may be null
     * @return the name of the plugin
     * @throws TemplateModelException
     *             if the argument is not a string
     */
    private static String getPlugin( List arguments, HttpServletRequest request ) throws TemplateModelException
    {
        if ( !arguments.isEmpty( ) && arguments.get( 0 ) instanceof TemplateScalarModel )
        {
            String strPlugin = ( (TemplateScalarModel) arguments.get( 0 ) ).getAsString( );

            if ( StringUtils.isNotBlank( strPlugin ) )
            {
                return strPlugin.trim( );
            }
        }

        Environment environment = Environment.getCurrentEnvironment( );

        return EditorProfileService.getPlugin( ( environment != null ) ? environment.getMainTemplate( ).getName( ) : null,
                ( request != null ) ? request.getRequestURI( ) : null );
    }

    /**
     * @return the current request, null when the template is rendered outside of a request (daemon, mail)
     */
    private static HttpServletRequest getRequest( )
    {
        try
        {
            HttpServletRequest request = CDI.current( ).select( HttpServletRequest.class ).get( );
            // the proxy only fails when used
            request.getRequestURI( );

            return request;
        }
        catch( ContextNotActiveException | IllegalStateException e )
        {
            AppLogService.debug( "No request to choose the editor profile", e );

            return null;
        }
    }
}
