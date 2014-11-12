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


/**
 * Default ValidationError Config implementation
 */
public class DefaultValidationErrorConfig implements ValidationErrorConfig
{
    private static final String VALUE1_ATTRIBUTES = "value,regexp,min,integer";
    private static final String VALUE2_ATTRIBUTES = "max,fraction";
    private static final String[] VARIABLES_PREFIX = { "_str", "_n", "_l", "_" };
    private static final String DEFAULT_WRAPPER_BEGIN = "<strong>";
    private static final String DEFAULT_WRAPPER_END = "</strong>";
    private String _strFieldKeysPrefix;

    /**
     * {@inheritDoc }
     */
    @Override
    public String getValue1Attributes(  )
    {
        return VALUE1_ATTRIBUTES;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getValue2Attributes(  )
    {
        return VALUE2_ATTRIBUTES;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String[] getVariablesPrefix(  )
    {
        return VARIABLES_PREFIX;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getFieldKeysPrefix(  )
    {
        return _strFieldKeysPrefix;
    }

    /**
     * @param strFieldKeysPrefix the _strFieldKeysPrefix to set
     */
    public void setFieldKeysPrefix( String strFieldKeysPrefix )
    {
        _strFieldKeysPrefix = strFieldKeysPrefix;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getFieldWrapperBegin(  )
    {
        return DEFAULT_WRAPPER_BEGIN;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getFieldWrapperEnd(  )
    {
        return DEFAULT_WRAPPER_END;
    }
}
