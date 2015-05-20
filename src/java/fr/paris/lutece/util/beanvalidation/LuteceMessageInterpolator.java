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
package fr.paris.lutece.util.beanvalidation;

import static fr.paris.lutece.portal.service.i18n.I18nService.getLocalizedString;
import fr.paris.lutece.portal.web.l10n.LocaleService;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;


/**
 * Lutece Message Interpolator
 */
public class LuteceMessageInterpolator implements MessageInterpolator
{
    private static final Pattern PATTERN_LOCALIZED_KEY = Pattern.compile( "#i18n\\{(.*?)\\}" );
    private static Locale _locale;
    private MessageInterpolator _interpolator;

    /**
     * Constructor;
     */
    public LuteceMessageInterpolator(  )
    {
        _interpolator = Validation.byDefaultProvider(  ).configure(  ).getDefaultMessageInterpolator(  );
        _locale = LocaleService.getDefault(  );
    }

    /**
     * Sets the locale for messages
     * @param locale The locale
     */
    public static void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String interpolate( String messageTemplate, Context context )
    {
        String strMessage = _interpolator.interpolate( messageTemplate, context );

        return interpolateMessage( strMessage, _locale );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String interpolate( String messageTemplate, Context context, Locale locale )
    {
        String strMessage = _interpolator.interpolate( messageTemplate, context );

        return interpolateMessage( strMessage, locale );
    }

    /**
     *
     * @param strMessage The message to transform
     * @param locale The Locale
     * @return The transformed message
     */
    protected static String interpolateMessage( String strMessage, Locale locale )
    {
        Matcher matcher = PATTERN_LOCALIZED_KEY.matcher( strMessage );

        if ( matcher.find(  ) )
        {
            StringBuffer sb = new StringBuffer(  );

            do
            {
                matcher.appendReplacement( sb, getLocalizedString( matcher.group( 1 ), locale ) );
            }
            while ( matcher.find(  ) );

            matcher.appendTail( sb );

            return sb.toString(  );
        }

        return strMessage;
    }
}
