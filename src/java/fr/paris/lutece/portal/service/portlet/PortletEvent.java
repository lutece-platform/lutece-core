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
package fr.paris.lutece.portal.service.portlet;


/**
 * Portlet event
 *
 */
public class PortletEvent
{
    public static final int INVALIDATE = 1;
    private int _nType;
    private int _nPortletId;
    private int _nPageId;

    /**
     * Default empty constructor
     */
    public PortletEvent(  )
    {
        // nothing
    }

    /**
     * PortletEvent
     * @param nType the type
     * @param nPortletId the portlet id
     * @param nPageId the page id
     */
    public PortletEvent( int nType, int nPortletId, int nPageId )
    {
        _nType = nType;
        _nPortletId = nPortletId;
        _nPageId = nPageId;
    }

    /**
     * Event type
     * @return event type
     */
    public int getType(  )
    {
        return _nType;
    }

    /**
     * Sets the event type
     * @param nType event type
     */
    public void setType( int nType )
    {
        this._nType = nType;
    }

    /**
     * Gets the portlet id
     * @return the portlet id
     */
    public Integer getPortletId(  )
    {
        return _nPortletId;
    }

    /**
     * Sets the portlet id
     * @param nPortletId the portlet id
     */
    public void setPortletId( int nPortletId )
    {
        this._nPortletId = nPortletId;
    }

    /**
     * Gets the page id
     * @return the page id
     */
    public int getPageId(  )
    {
        return _nPageId;
    }

    /**
     * Sets the page id
     * @param nPageId the page id
     */
    public void setPageId( int nPageId )
    {
        _nPageId = nPageId;
    }
}
