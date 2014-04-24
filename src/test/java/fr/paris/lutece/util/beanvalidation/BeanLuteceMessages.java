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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

import java.sql.Date;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * BeanLuteceMessages class for bean validation tests
 */
public class BeanLuteceMessages implements Bean
{
    // Variables declarations 
    private int _nIdObject;
    @NotEmpty( message = "#i18n{portal.validation.message.notEmpty}" )
    @Pattern( regexp = "[a-z-A-Z]", message = "#i18n{portal.validation.message.pattern}" )
    @Size( max = 5, message = "#i18n{portal.validation.message.sizeMax}" )
    private String _strName;
    @Size( min = 10, max = 50, message = "#i18n{portal.validation.message.size}" )
    private String _strDescription;
    @Min( value = 5, message = "#i18n{portal.validation.message.min}" )
    private int _nAge;
    @Email( message = "#i18n{portal.validation.message.email}" )
    private String _strEmail;
    @Past( message = "#i18n{portal.validation.message.past}" )
    private Date _dateBirth;
    @Future( message = "#i18n{portal.validation.message.future}" )
    private Date _dateEndOfWorld;
    @DecimalMin( value = "1500.0", message = "#i18n{portal.validation.message.decimalMin}" )
    private BigDecimal _salary;
    @DecimalMax( value = "100.0", message = "#i18n{portal.validation.message.decimalMax}" )
    private BigDecimal _percent;
    @Digits( integer = 15, fraction = 2, message = "#i18n{portal.validation.message.digits}" )
    private String _strCurrency;
    @URL( message = "#i18n{portal.validation.message.url}" )
    private String _strUrl;

    /**
     * Returns the IdObject
     *
     * @return The IdObject
     */
    @Override
    public int getIdObject(  )
    {
        return _nIdObject;
    }

    /**
     * Sets the IdObject
     *
     * @param nIdObject The IdObject
     */
    @Override
    public void setIdObject( int nIdObject )
    {
        _nIdObject = nIdObject;
    }

    /**
     * Returns the Name
     *
     * @return The Name
     */
    @Override
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    @Override
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Description
     *
     * @return The Description
     */
    @Override
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     *
     * @param strDescription The Description
     */
    @Override
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the Age
     *
     * @return The Age
     */
    @Override
    public int getAge(  )
    {
        return _nAge;
    }

    /**
     * Sets the Age
     *
     * @param nAge The Age
     */
    @Override
    public void setAge( int nAge )
    {
        _nAge = nAge;
    }

    /**
     * Returns the Email
     *
     * @return The Email
     */
    @Override
    public String getEmail(  )
    {
        return _strEmail;
    }

    /**
     * Sets the Email
     *
     * @param strEmail The Email
     */
    @Override
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }

    /**
     * @return the _dateBirth
     */
    @Override
    public Date getDateBirth(  )
    {
        return _dateBirth;
    }

    /**
     * @param dateBirth the _dateBirth to set
     */
    @Override
    public void setDateBirth( Date dateBirth )
    {
        _dateBirth = dateBirth;
    }

    /**
     * @return the _dateEndOfWorld
     */
    @Override
    public Date getDateEndOfWorld(  )
    {
        return _dateEndOfWorld;
    }

    /**
     * @param dateEndOfWorld the _dateEndOfWorld to set
     */
    @Override
    public void setDateEndOfWorld( Date dateEndOfWorld )
    {
        _dateEndOfWorld = dateEndOfWorld;
    }

    /**
     * @return the _salary
     */
    @Override
    public BigDecimal getSalary(  )
    {
        return _salary;
    }

    /**
     * @param salary the _salary to set
     */
    @Override
    public void setSalary( BigDecimal salary )
    {
        _salary = salary;
    }

    /**
     * @return the _percent
     */
    @Override
    public BigDecimal getPercent(  )
    {
        return _percent;
    }

    /**
     * @param percent the _percent to set
     */
    @Override
    public void setPercent( BigDecimal percent )
    {
        _percent = percent;
    }

    /**
     * @return the _strCurrency
     */
    @Override
    public String getCurrency(  )
    {
        return _strCurrency;
    }

    /**
     * @param strCurrency the _strCurrency to set
     */
    @Override
    public void setCurrency( String strCurrency )
    {
        _strCurrency = strCurrency;
    }

    /**
     * @return the _strUrl
     */
    @Override
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * @param strUrl the _strUrl to set
     */
    @Override
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }
}
