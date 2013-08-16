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
    int getAge(  );

    /**
     * @return the _strCurrency
     */
    String getCurrency(  );

    /**
     * @return the _dateBirth
     */
    Date getDateBirth(  );

    /**
     * @return the _dateEndOfWorld
     */
    Date getDateEndOfWorld(  );

    /**
     * Returns the Description
     *
     * @return The Description
     */
    String getDescription(  );

    /**
     * Returns the Email
     * @return The Email
     */
    String getEmail(  );

    /**
     * Returns the IdObject
     *
     * @return The IdObject
     */
    int getIdObject(  );

    /**
     * Returns the Name
     *
     * @return The Name
     */
    String getName(  );

    /**
     * @return the _percent
     */
    BigDecimal getPercent(  );

    /**
     * @return the _salary
     */
    BigDecimal getSalary(  );

    /**
     * Sets the Age
     *
     * @param nAge The Age
     */
    void setAge( int nAge );

    /**
     * @param strCurrency the _strCurrency to set
     */
    void setCurrency( String strCurrency );

    /**
     * @param dateBirth the _dateBirth to set
     */
    void setDateBirth( Date dateBirth );

    /**
     * @param dateEndOfWorld the _dateEndOfWorld to set
     */
    void setDateEndOfWorld( Date dateEndOfWorld );

    /**
     * Sets the Description
     *
     * @param strDescription The Description
     */
    void setDescription( String strDescription );

    /**
     * Sets the Email
     * @param strEmail The Email
     */
    void setEmail( String strEmail );

    /**
     * Sets the IdObject
     *
     * @param nIdObject The IdObject
     */
    void setIdObject( int nIdObject );

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    void setName( String strName );

    /**
     * @param percent the _percent to set
     */
    void setPercent( BigDecimal percent );

    /**
     * @param salary the _salary to set
     */
    void setSalary( BigDecimal salary );

    void setUrl( String strUrl );

    String getUrl(  );
}
