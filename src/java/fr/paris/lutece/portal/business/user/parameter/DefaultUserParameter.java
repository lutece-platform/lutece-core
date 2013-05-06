/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.portal.business.user.parameter;


/**
 *
 * Default parameters for AdminUsers
 *
 */
public class DefaultUserParameter
{
    private String _strParameterKey;
    private String _strParameterValue;

    /**
     * Constructor
     */
    public DefaultUserParameter(  )
    {
        _strParameterKey = "";
        _strParameterValue = "";
    }

    /**
     * Constructor
     * @param strParameterKey Key
     * @param strParameterValue Value
     */
    public DefaultUserParameter( String strParameterKey, String strParameterValue )
    {
        _strParameterKey = strParameterKey;
        _strParameterValue = strParameterValue;
    }

    /**
     * Getter
     * @return key
     */
    public String getParameterKey(  )
    {
        return _strParameterKey;
    }

    /**
     * Setter
     * @param strParameterKey Key
     */
    public void setParameterKey( String strParameterKey )
    {
        _strParameterKey = strParameterKey;
    }

    /**
     * Getter
     * @return Value
     */
    public String getParameterValue(  )
    {
        return _strParameterValue;
    }

    /**
     * Setter
     * @param strParameterValue Value
     */
    public void setParameterValue( String strParameterValue )
    {
        _strParameterValue = strParameterValue;
    }
}
