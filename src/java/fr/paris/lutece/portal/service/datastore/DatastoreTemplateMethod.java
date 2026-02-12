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
package fr.paris.lutece.portal.service.datastore;

import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AbstractMessageFormatTemplateMethod;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

import java.util.List;
import java.util.Locale;

public class DatastoreTemplateMethod extends AbstractMessageFormatTemplateMethod
{
    private static final String PREFIX_PLUGIN = "plugin:";
    @Override
    public Object exec( List arguments ) throws TemplateModelException
    {
        int argsSize = arguments.size( );

        if ( argsSize == 2 )
        {
            Object arg2 = arguments.get( 1 );

            if ( arg2 instanceof TemplateScalarModel )
            {
                String strArg = ( (TemplateScalarModel) arg2 ).getAsString( );

                if ( strArg.startsWith( PREFIX_PLUGIN ) )
                {
                    String strPluginName = strArg.substring( PREFIX_PLUGIN.length( ) );

                    if ( PluginService.isPluginEnable( strPluginName ) )
                    {
                        String key = ( (TemplateScalarModel) arguments.get( 0 ) ).getAsString( );
                        return DatastoreService.getDataValue( key, DatastoreService.VALUE_MISSING );
                    }

                    return "";
                }
            }
        }
        return super.exec( arguments );
    }
    @Override
    protected String getPattern( String key, Locale locale )
    {
        return DatastoreService.getDataValue( key, DatastoreService.VALUE_MISSING );
    }
}
