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
package fr.paris.lutece.portal.service.datastore;


/**
 * This is the business class for the object SiteProperty
 */
public class LocalizedData
{
    // Variables declarations 
    private String _strKey;
    private String _strLabel;
    private String _strValue;
    private String _strHelp;

    /**
     * Returns the Key
     *
     * @return The Key
     */
    public String getKey(  )
    {
        return _strKey;
    }

    /**
     * Sets the Key
     *
     * @param strKey The Key
     */
    public void setKey( String strKey )
    {
        _strKey = strKey;
    }

    /**
     * Returns the Label
     *
     * @return The Label
     */
    public String getLabel(  )
    {
        return _strLabel;
    }

    /**
     * Sets the Label
     *
     * @param strLabel The Label
     */
    public void setLabel( String strLabel )
    {
        _strLabel = strLabel;
    }

    /**
     * Returns the Value
     *
     * @return The Value
     */
    public String getValue(  )
    {
        return _strValue;
    }

    /**
     * Sets the Value
     *
     * @param strValue The Value
     */
    public void setValue( String strValue )
    {
        _strValue = strValue;
    }

    /**
     * Returns the Help
     *
     * @return The Help
     */
    public String getHelp(  )
    {
        return _strHelp;
    }

    /**
     * Sets the Help
     *
     * @param strHelp The Help
     */
    public void setHelp( String strHelp )
    {
        _strHelp = strHelp;
    }
}
