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
package fr.paris.lutece.portal.business.right;

import fr.paris.lutece.util.ReferenceItem;

/**
 * This class reprsents business objects Mode
 */
public class Level
{
    private int _nId;
    private String _strName;

    /**
     * Returns the level right identifier
     *
     * @return the level right identifier
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the level right identifier
     *
     * @param nId the level right identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the name of the level right
     *
     * @return the level right name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of the level right
     *
     * @param strName the level right name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }
    
    /**
     * Return a reference Item for this level
     *
     * @return a reference item for this level
     */
    public ReferenceItem getReferenceItem(  )
    {
        ReferenceItem referenceItem = new ReferenceItem(  );
        referenceItem.setCode( Integer.toString( _nId ) );
        referenceItem.setName( _strName );
        referenceItem.setChecked( true );
        return referenceItem;
    }
}
