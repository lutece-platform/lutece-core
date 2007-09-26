/*
 * Copyright (c) 2002-2007, Mairie de Paris
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
package fr.paris.lutece.portal.service.captcha;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Default captcha security service implementation
 */
public class CaptchaSecurityService implements ICaptchaSecurityService
{
    private static final String SPRING_CONTEXT_NAME = "jcaptcha";
    private boolean _bActive = true;
    private boolean _bAvailable;
    private ICaptchaService captchaService;

    /**
     * Default constructor.
     *
     * Gets the captchaValidator from the jcaptcha plugin.
     * If the validator is missing, sets available to false;
     */
    public CaptchaSecurityService(  )
    {
        try
        {
        	// first check if captchaValidator bean is available in the jcaptcha plugin context
        	captchaService = (ICaptchaService) SpringContextService.getPluginBean( SPRING_CONTEXT_NAME,
                    "captchaService" );
            _bAvailable = captchaService != null;
        }
        catch ( NoSuchBeanDefinitionException e )
        {
            _bAvailable = false;
        }
        catch ( CannotLoadBeanClassException e )
        {
        	_bAvailable = false;
        }
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.jcaptcha.service.ICaptchaSecurityService#getActiveBlockHtml()
     */
    public String getActiveBlockHtml(  )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.jcaptcha.service.ICaptchaSecurityService#getHtmlCode()
     */
    public String getHtmlCode(  )
    {
        if ( isAvailable(  ) && isActive(  ) )
        {
        	return captchaService.getHtmlCode();
        }
        else
        {
            return EMPTY_STRING;
        }
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.jcaptcha.service.ICaptchaSecurityService#setActive(boolean)
     */
    public void setActive( boolean isActive )
    {
        _bActive = isActive;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.jcaptcha.service.ICaptchaSecurityService#validate(javax.servlet.http.HttpServletRequest)
     */
    public boolean validate( HttpServletRequest request )
    {
        if ( isAvailable(  ) && isActive(  ) )
        {
            return captchaService.validate( request );
        }
        else
        {
            return true;
        }
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.jcaptcha.service.ICaptchaSecurityService#isActive()
     */
    public boolean isActive(  )
    {
        return _bActive;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.jcaptcha.service.ICaptchaSecurityService#isAvailable()
     */
    public boolean isAvailable(  )
    {
        return _bAvailable;
    }
}
