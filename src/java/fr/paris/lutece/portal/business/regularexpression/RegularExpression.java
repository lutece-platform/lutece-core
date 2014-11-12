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
package fr.paris.lutece.portal.business.regularexpression;


/**
 *
 * RegularExpression
 *
 */
public class RegularExpression
{
    private int _nIdExpression;
    private String _strTitle;
    private String _strValue;
    private String _strInformationMessage;
    private String _strErrorMessage;
    private String _strValidExemple;

    /**
     *return the id of the regular expression
     * @return the id of the regular expression
     */
    public int getIdExpression(  )
    {
        return _nIdExpression;
    }

    /**
     * set the id of the regular expression
     * @param idExpression the id of the regular expression
     */
    public void setIdExpression( int idExpression )
    {
        _nIdExpression = idExpression;
    }

    /**
     * return the error message
     * @return the error message
     */
    public String getErrorMessage(  )
    {
        return _strErrorMessage;
    }

    /**
     * set the error message
     * @param errorMessage the error message
     */
    public void setErrorMessage( String errorMessage )
    {
        _strErrorMessage = errorMessage;
    }

    /**
     *
     * return a valid exemple which is validate by the regular expression
     * @return a valid exemple
     */
    public String getValidExemple(  )
    {
        return _strValidExemple;
    }

    /**
     * set a valid exemple which is validate by the regular expression
     * @param exempleValide a valid exemple
     */
    public void setValidExemple( String exempleValide )
    {
        _strValidExemple = exempleValide;
    }

    /**
     *return the information message
     * @return the information message
     */
    public String getInformationMessage(  )
    {
        return _strInformationMessage;
    }

    /**
     * set the information message
     * @param informationMessage the information message
     */
    public void setInformationMessage( String informationMessage )
    {
        _strInformationMessage = informationMessage;
    }

    /**
     * return the title of the regular expression
     * @return the title of the regular expression
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * set the title of the regular expression
     * @param title the title of the regular expression
     */
    public void setTitle( String title )
    {
        _strTitle = title;
    }

    /**
     * return the value of the regular expression
     * @return the value of the regular expression
     */
    public String getValue(  )
    {
        return _strValue;
    }

    /**
     * set the value of the regular expression
     * @param value the value of the regular expression
     */
    public void setValue( String value )
    {
        _strValue = value;
    }

    /**
     * @param obj the object to compare
     * @return true if the regularExpression in parameter is the same regularExpression
     */
    public boolean equals( Object obj )
    {
        if ( obj instanceof RegularExpression && ( ( (RegularExpression) obj ).getIdExpression(  ) == _nIdExpression ) )
        {
            return true;
        }

        return false;
    }

    /**
     * @return the id of the expression
     */
    public int hashCode(  )
    {
        return _nIdExpression;
    }
}
