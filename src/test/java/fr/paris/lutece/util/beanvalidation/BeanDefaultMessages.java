/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author pierre
 */
public class BeanDefaultMessages implements Bean
{
    // Variables declarations 
    private int _nIdObject;
    @NotEmpty(  )
    @Pattern( regexp = "[a-z-A-Z]" )
    @Size( max = 5 )
    private String _strName;
    @Size( min = 10 )
    private String _strDescription;
    @Min( value = 5 )
    private int _nAge;
    @Email(  )
    private String _strEmail;
    @Past(  )
    private Date _dateBirth;
    @Future(  )
    private Date _dateEndOfWorld;
    @DecimalMin( value = "1500.0" )
    private BigDecimal _salary;
    @DecimalMax( value = "100.0" )
    private BigDecimal _percent;
    @Digits( integer = 15, fraction = 2 )
    private String _strCurrency;
    @URL(  )
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
