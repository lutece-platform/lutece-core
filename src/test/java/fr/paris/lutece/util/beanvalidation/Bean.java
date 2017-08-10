/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.util.beanvalidation;

import java.math.BigDecimal;

import java.sql.Date;

/**
 *
 * @author pierre
 */
public interface Bean
{
    /**
     * Returns the Age
     *
     * @return The Age
     */
    int getAge( );

    /**
     * @return the _strCurrency
     */
    String getCurrency( );

    /**
     * @return the _dateBirth
     */
    Date getDateBirth( );

    /**
     * @return the _dateEndOfWorld
     */
    Date getDateEndOfWorld( );

    /**
     * Returns the Description
     *
     * @return The Description
     */
    String getDescription( );

    /**
     * Returns the Email
     * 
     * @return The Email
     */
    String getEmail( );

    /**
     * Returns the IdObject
     *
     * @return The IdObject
     */
    int getIdObject( );

    /**
     * Returns the Name
     *
     * @return The Name
     */
    String getName( );

    /**
     * @return the _percent
     */
    BigDecimal getPercent( );

    /**
     * @return the _salary
     */
    BigDecimal getSalary( );

    /**
     * Sets the Age
     *
     * @param nAge
     *            The Age
     */
    void setAge( int nAge );

    /**
     * @param strCurrency
     *            the _strCurrency to set
     */
    void setCurrency( String strCurrency );

    /**
     * @param dateBirth
     *            the _dateBirth to set
     */
    void setDateBirth( Date dateBirth );

    /**
     * @param dateEndOfWorld
     *            the _dateEndOfWorld to set
     */
    void setDateEndOfWorld( Date dateEndOfWorld );

    /**
     * Sets the Description
     *
     * @param strDescription
     *            The Description
     */
    void setDescription( String strDescription );

    /**
     * Sets the Email
     * 
     * @param strEmail
     *            The Email
     */
    void setEmail( String strEmail );

    /**
     * Sets the IdObject
     *
     * @param nIdObject
     *            The IdObject
     */
    void setIdObject( int nIdObject );

    /**
     * Sets the Name
     *
     * @param strName
     *            The Name
     */
    void setName( String strName );

    /**
     * @param percent
     *            the _percent to set
     */
    void setPercent( BigDecimal percent );

    /**
     * @param salary
     *            the _salary to set
     */
    void setSalary( BigDecimal salary );

    void setUrl( String strUrl );

    String getUrl( );
}
