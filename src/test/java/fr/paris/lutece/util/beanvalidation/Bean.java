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
package fr.paris.lutece.util.beanvalidation;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Bean class for bean validation tests
 */
public class Bean
{
// Variables declarations 

    private int _nIdObject;
    @NotEmpty(message = "Invalid value {1}. The field {0} cannot be empty.")
    @Pattern(regexp = "[a-z-A-Z]" , message = "#i18n{portal.util.validation.regexp}" )
    private String _strName;
    @Size( min = 10 , max = 50 , message = "#i18n{portal.util.validation.size}" )
    private String _strDescription;
    @Min( value=5 , message = "#i18n{portal.util.validation.min}" )
    private int _nAge;
    @Email( message = "#i18n{portal.util.validation.email}")
    private String _strEmail;

    /**
     * Returns the IdObject
     *
     * @return The IdObject
     */
    public int getIdObject()
    {
        return _nIdObject;
    }

    /**
     * Sets the IdObject
     *
     * @param nIdObject The IdObject
     */
    public void setIdObject(int nIdObject)
    {
        _nIdObject = nIdObject;
    }

    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName()
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    public void setName(String strName)
    {
        _strName = strName;
    }

    /**
     * Returns the Description
     *
     * @return The Description
     */
    public String getDescription()
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     *
     * @param strDescription The Description
     */
    public void setDescription(String strDescription)
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the Age
     *
     * @return The Age
     */
    public int getAge()
    {
        return _nAge;
    }

    /**
     * Sets the Age
     *
     * @param nAge The Age
     */
    public void setAge(int nAge)
    {
        _nAge = nAge;
    }
    
          /**
        * Returns the Email
        * @return The Email
        */ 
    public String getEmail()
    {
        return _strEmail;
    }
    
       /**
        * Sets the Email
        * @param strEmail The Email
        */ 
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }

}