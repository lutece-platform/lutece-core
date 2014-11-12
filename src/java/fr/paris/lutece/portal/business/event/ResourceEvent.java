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
package fr.paris.lutece.portal.business.event;

import fr.paris.lutece.portal.service.search.IndexationService;


/**
*
* ResourceEvent
*
*/
public class ResourceEvent
{
    private String _strIdResource;
    private String _strTypeResource;
    private int _nIdPortlet = IndexationService.ALL_DOCUMENT;

    /**
     * default constructor
     */
    public ResourceEvent(  )
    {
    }

    /**
     * Constructor with fields
     * @param strIdDocument the resource identifier
     * @param strTypeResource the type of the resource
     * @param nIdPortlet the portlet identifier of the resource
     */
    public ResourceEvent( String strIdDocument, String strTypeResource, int nIdPortlet )
    {
        _strIdResource = strIdDocument;
        _strTypeResource = strTypeResource;
        _nIdPortlet = nIdPortlet;
    }

    /**
     * Gets the resource identifier
     * @return the resource identifier
     */
    public String getIdResource(  )
    {
        return _strIdResource;
    }

    /**
     * Sets the resource identifier
     * @param strIdDocument the resource identifier
     */
    public void setIdResource( String strIdDocument )
    {
        _strIdResource = strIdDocument;
    }

    /**
     * Gets the type of the resource
     * @return the type of the resource
     */
    public String getTypeResource(  )
    {
        return _strTypeResource;
    }

    /**
     * Sets the type of the resource
     * @param indexerName the type of the resource
     */
    public void setTypeResource( String indexerName )
    {
        _strTypeResource = indexerName;
    }

    /**
     * Gets the portlet identifier of the resource
     * @return the portlet identifier of the resource
     */
    public int getIdPortlet(  )
    {
        return _nIdPortlet;
    }

    /**
     * Sets the portlet identifier of the resource
     * @param nIdPortlet the portlet identifier of the resource
     */
    public void setIdPortlet( int nIdPortlet )
    {
        _nIdPortlet = nIdPortlet;
    }
}
