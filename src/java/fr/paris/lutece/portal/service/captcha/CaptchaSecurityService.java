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
package fr.paris.lutece.portal.service.captcha;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.BeanUtils;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Legacy implementation providing access to the {@link ICaptchaService}.
 * <p>
 * This class is <strong>deprecated</strong> and will be removed in a future release.
 * It was originally responsible for managing access to the {@code ICaptchaService}.
 * </p>
 *
 * <p>
 * The recommended approach is now to use <b>CDI dependency injection</b> to obtain
 * the {@code ICaptchaService} directly, instead of instantiating or referencing this class.
 * </p>
 *
 * <p>Example of recommended usage:</p>
 * <pre>{@code
 * @Inject
 * @Named(BeanUtils.BEAN_CAPTCHA_SERVICE)
 * private Instance<ICaptchaService> _captchaService;
 * }</pre>
 *
 * @deprecated since 8.0 — use CDI injection of the {@code ICaptchaService} bean instead.
 */
@Deprecated(since = "8.0", forRemoval = true)
public class CaptchaSecurityService implements ICaptchaSecurityService
{
    private boolean _bActive = true;
    private boolean _bAvailable;
    private ICaptchaService _captchaService;

    /**
     * Default constructor.
     *
     * Gets the captchaValidator from the captcha plugin. If the validator is missing, sets available to false;
     */
    public CaptchaSecurityService( )
    {        
        // first check if captchaValidator bean is available in the jcaptcha plugin context    	
    	try
        {
    		Instance<ICaptchaService> instance= CDI.current()
    	            .select(ICaptchaService.class, NamedLiteral.of( BeanUtils.BEAN_CAPTCHA_SERVICE ));
    		if( instance.isResolvable()) {
    			_captchaService = instance.get();
    		}
    		_bAvailable = ( _captchaService != null );  
        }
        catch( IllegalArgumentException | IllegalStateException e )
        {	
        	AppLogService.debug("ICaptchaService Provider not found", e);
        	_bAvailable = false;
        }
    }
        	

    /**
     * {@inheritDoc}
     */
    @Override
    public String getActiveBlockHtml( )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHtmlCode( )
    {
        if ( isAvailable( ) && isActive( ) )
        {
            return _captchaService.getHtmlCode( );
        }
        else
        {
            return EMPTY_STRING;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActive( boolean isActive )
    {
        _bActive = isActive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate( HttpServletRequest request )
    {
        if ( isAvailable( ) && isActive( ) )
        {
            return _captchaService.validate( request );
        }
        else
        {
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive( )
    {
        return _bActive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAvailable( )
    {
        return _bAvailable;
    }
}
