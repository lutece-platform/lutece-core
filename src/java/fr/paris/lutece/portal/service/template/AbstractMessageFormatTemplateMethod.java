/*
 * Copyright (c) 2002-2021, City of Paris
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

import freemarker.core.Environment;

import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;

import java.text.MessageFormat;

import java.util.List;
import java.util.Locale;

public abstract class AbstractMessageFormatTemplateMethod implements TemplateMethodModelEx
{
    @Override
    public Object exec( @SuppressWarnings( "rawtypes" ) List arguments ) throws TemplateModelException
    {
        int argsSize = arguments.size( );

        if ( argsSize < 1 )
        {
            throw new TemplateModelException( "Must be called with at least one argument (the message key)" );
        }

        String key = ( (TemplateScalarModel) arguments.get( 0 ) ).getAsString( );
        Locale locale = Environment.getCurrentEnvironment( ).getLocale( );

        if ( argsSize == 1 )
        {
            // no arguments, the message is not a MessageFormat
            return getPattern( key, locale );
        }

        Object [ ] args = new Object [ argsSize - 1];

        for ( int i = 1; i < argsSize; i++ )
        {
            TemplateModel arg = (TemplateModel) arguments.get( i );

            if ( arg instanceof TemplateScalarModel )
            {
                args [i - 1] = ( (TemplateScalarModel) arg ).getAsString( );
            }
            else
                if ( arg instanceof TemplateNumberModel )
                {
                    args [i - 1] = ( (TemplateNumberModel) arg ).getAsNumber( );
                }
                else
                    if ( arg instanceof TemplateDateModel )
                    {
                        args [i - 1] = ( (TemplateDateModel) arg ).getAsDate( );
                    }
                    else
                    {
                        throw new TemplateModelException( "Unsupported argument type : " + arg );
                    }
        }

        return new MessageFormat( getPattern( key, locale ), locale ).format( args, new StringBuffer( ), null ).toString( );
    }

    protected abstract String getPattern( String key, Locale locale );
}
