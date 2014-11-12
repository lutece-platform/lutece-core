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
package fr.paris.lutece.portal.service.regularexpression;

import fr.paris.lutece.portal.business.regularexpression.RegularExpression;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.List;


/**
 *
 * this class provides services for use regular expression
 */
public final class RegularExpressionService
{
    private static final String PLUGIN_REGULAR_EXPRESSION_NAME = "regularexpression";
    private static volatile RegularExpressionService _singleton;
    private boolean _bServiceAvailable = true;
    private IRegularExpressionService _service;

    /**
     * Private constructor
     */
    private RegularExpressionService(  )
    {
        try
        {
            _service = SpringContextService.getBean( "regularExpressionService" );
            _bServiceAvailable = _service != null;
        }
        catch ( BeanDefinitionStoreException e )
        {
            _bServiceAvailable = false;
        }
        catch ( NoSuchBeanDefinitionException e )
        {
            _bServiceAvailable = false;
        }
        catch ( CannotLoadBeanClassException e )
        {
            _bServiceAvailable = false;
        }
    }

    /**
    * Returns the unique instance of the service
    * @return The instance of the service
    */
    public static RegularExpressionService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new RegularExpressionService(  );
        }

        return _singleton;
    }

    /**
    *
    * @return true if the regular expression service is available
    */
    public boolean isAvailable(  )
    {
        return _bServiceAvailable && PluginService.isPluginEnable( PLUGIN_REGULAR_EXPRESSION_NAME );
    }

    /**
         * return false if the pattern is invalid
         * @param strPattern the pattern to test
         * @return  false if the pattern is invalid
         */
    boolean isPatternValide( String strPattern )
    {
        return isAvailable(  ) ? _service.isPatternValide( strPattern ) : false;
    }

    /**
     * return false if the expression's syntax is invalid
     * @param regularExpression the regular expression object to test
     * @return  false if the expression's syntax is invalid
     */
    boolean isPatternValide( RegularExpression regularExpression )
    {
        return isAvailable(  ) ? _service.isPatternValide( regularExpression ) : false;
    }

    /**
    * return true if the value in parameter verify the pattern
    * @param strValueToTest the value to test
    * @param strPattern the regular expression Pattern
    * @return true if the value in parameter verify the pattern
    */
    public boolean isMatches( String strValueToTest, String strPattern )
    {
        return isAvailable(  ) ? _service.isMatches( strValueToTest, strPattern ) : false;
    }

    /**
     * return true if the value in parameter verify the regular expression
     * @param strValueToTest the value to test
     * @param regularExpression the regular expression
     * @return true if the value verify the regular expression
     */
    public boolean isMatches( String strValueToTest, RegularExpression regularExpression )
    {
        return isAvailable(  ) ? _service.isMatches( strValueToTest, regularExpression ) : false;
    }

    /**
     * return the regular expression object  whose identifier is specified in parameter
     * @param nKey the regular expression key
     * @return the regular expression object  whose identifier is specified in parameter
     */
    public RegularExpression getRegularExpressionByKey( int nKey )
    {
        return isAvailable(  ) ? _service.getRegularExpressionByKey( nKey ) : null;
    }

    /**
     * return a list of regular expression
     * @return all regular expression
     */
    public List<RegularExpression> getAllRegularExpression(  )
    {
        return isAvailable(  ) ? _service.getAllRegularExpression(  ) : null;
    }
}
