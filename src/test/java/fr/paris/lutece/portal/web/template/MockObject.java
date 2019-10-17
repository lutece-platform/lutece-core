/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.portal.web.template;

import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * This is the business class for the object MockObject
 */ 
public class MockObject
{
        // Variables declarations 
        private int _nIdObject;
        private String _strName;
        private String _strDescription;
        private boolean _bEnable;
        private int _nStatus;
        private double _dRatio;
        private String _strPassword;
        private long _lElapsedTime;
        private int _nIdForeign;
        private String _strEmail;
        private String _strText;
        private List _listAttributes = new ArrayList();
        private Map _mapParameters = new HashMap();
        private Date _dateDateCreated;
        private Timestamp _dateDateUpdated;
    
    
       /**
        * Returns the IdObject
        * @return The IdObject
        */ 
        public int getIdObject()
        {
            return _nIdObject;
        }
    
       /**
        * Sets the IdObject
        * @param nIdObject The IdObject
        */ 
        public void setIdObject( int nIdObject )
        {
            _nIdObject = nIdObject;
        }
    
       /**
        * Returns the Name
        * @return The Name
        */ 
        public String getName()
        {
            return _strName;
        }
    
       /**
        * Sets the Name
        * @param strName The Name
        */ 
        public void setName( String strName )
        {
            _strName = strName;
        }
    
       /**
        * Returns the Description
        * @return The Description
        */ 
        public String getDescription()
        {
            return _strDescription;
        }
    
       /**
        * Sets the Description
        * @param strDescription The Description
        */ 
        public void setDescription( String strDescription )
        {
            _strDescription = strDescription;
        }
    
       /**
        * Returns the Enable
        * @return The Enable
        */ 
        public boolean getEnable()
        {
            return _bEnable;
        }
    
       /**
        * Sets the Enable
        * @param bEnable The Enable
        */ 
        public void setEnable( boolean bEnable )
        {
            _bEnable = bEnable;
        }
    
       /**
        * Returns the Status
        * @return The Status
        */ 
        public int getStatus()
        {
            return _nStatus;
        }
    
       /**
        * Sets the Status
        * @param nStatus The Status
        */ 
        public void setStatus( int nStatus )
        {
            _nStatus = nStatus;
        }
    
       /**
        * Returns the Ratio
        * @return The Ratio
        */ 
        public double getRatio()
        {
            return _dRatio;
        }
    
       /**
        * Sets the Ratio
        * @param dRatio The Ratio
        */ 
        public void setRatio( double dRatio )
        {
            _dRatio = dRatio;
        }
    
       /**
        * Returns the Password
        * @return The Password
        */ 
        public String getPassword()
        {
            return _strPassword;
        }
    
       /**
        * Sets the Password
        * @param strPassword The Password
        */ 
        public void setPassword( String strPassword )
        {
            _strPassword = strPassword;
        }
    
       /**
        * Returns the ElapsedTime
        * @return The ElapsedTime
        */ 
        public long getElapsedTime()
        {
            return _lElapsedTime;
        }
    
       /**
        * Sets the ElapsedTime
        * @param lElapsedTime The ElapsedTime
        */ 
        public void setElapsedTime( long lElapsedTime )
        {
            _lElapsedTime = lElapsedTime;
        }
    
       /**
        * Returns the IdForeign
        * @return The IdForeign
        */ 
        public int getIdForeign()
        {
            return _nIdForeign;
        }
    
       /**
        * Sets the IdForeign
        * @param nIdForeign The IdForeign
        */ 
        public void setIdForeign( int nIdForeign )
        {
            _nIdForeign = nIdForeign;
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
    
       /**
        * Returns the Text
        * @return The Text
        */ 
        public String getText()
        {
            return _strText;
        }
    
       /**
        * Sets the Text
        * @param strText The Text
        */ 
        public void setText( String strText )
        {
            _strText = strText;
        }
    
       /**
        * Returns the Attributes
        * @return The Attributes
        */ 
        public List getAttributes()
        {
            return _listAttributes;
        }
    
       /**
        * Sets the Attributes
        * @param listAttributes The Attributes
        */ 
        public void setAttributes( List listAttributes )
        {
            _listAttributes = listAttributes;
        }
    
       /**
        * Returns the Parameters
        * @return The Parameters
        */ 
        public Map getParameters()
        {
            return _mapParameters;
        }
    
       /**
        * Sets the Parameters
        * @param mapParameters The Parameters
        */ 
        public void setParameters( Map mapParameters )
        {
            _mapParameters = mapParameters;
        }
    
       /**
        * Returns the DateCreated
        * @return The DateCreated
        */ 
        public Date getDateCreated()
        {
            return _dateDateCreated;
        }
    
       /**
        * Sets the DateCreated
        * @param dateDateCreated The DateCreated
        */ 
        public void setDateCreated( Date dateDateCreated )
        {
            _dateDateCreated = dateDateCreated;
        }
    
       /**
        * Returns the DateUpdated
        * @return The DateUpdated
        */ 
        public Timestamp getDateUpdated()
        {
            return _dateDateUpdated;
        }
    
       /**
        * Sets the DateUpdated
        * @param dateDateUpdated The DateUpdated
        */ 
        public void setDateUpdated( Timestamp dateDateUpdated )
        {
            _dateDateUpdated = dateDateUpdated;
        }
 }
