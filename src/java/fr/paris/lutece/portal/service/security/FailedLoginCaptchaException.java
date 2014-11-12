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
package fr.paris.lutece.portal.service.security;

import javax.security.auth.login.FailedLoginException;


/**
 * This Exception should be thrown when an IP failed to login several times, and the authentication service wants to enable Captcha.
 */
public final class FailedLoginCaptchaException extends FailedLoginException
{
    private static final long serialVersionUID = -6833180698556878514L;
    private final boolean _bEnableCaptcha;

    /**
     * Creates a new instance of FailedLoginCaptchaException
     * @param bEnableCaptcha True if the captcha should be enabled, false otherwise
     */
    public FailedLoginCaptchaException( boolean bEnableCaptcha )
    {
        super(  );
        _bEnableCaptcha = bEnableCaptcha;
    }

    /**
     * Creates a new instance of FailedLoginCaptchaException
     * @param strMessage The detail message
     * @param bEnableCaptcha True if the captcha should be enabled, false otherwise
     */
    public FailedLoginCaptchaException( String strMessage, boolean bEnableCaptcha )
    {
        super( strMessage );
        _bEnableCaptcha = bEnableCaptcha;
    }

    /**
     * Check if the captcha should be enabled
     * @return True if the captcha should be enabled, false otherwise
     */
    public boolean isCaptchaEnabled(  )
    {
        return _bEnableCaptcha;
    }
}
