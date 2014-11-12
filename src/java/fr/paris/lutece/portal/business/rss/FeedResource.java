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
package fr.paris.lutece.portal.business.rss;

import java.util.Date;
import java.util.List;


/**
 *
 * FeedResource - {@link IFeedResource} implementation.
 */
public class FeedResource implements IFeedResource
{
    private IFeedResourceImage _image;
    private List<IFeedResourceItem> _listItems;
    private String _strLanguage;
    private String _strLink;
    private String _strDescription;
    private String _strTitle;
    private Date _date;

    /**
     *
     * {@inheritDoc}
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     *
     * {@inheritDoc}
     */
    public void setDescription( String description )
    {
        this._strDescription = description;
    }

    /**
     *
     * {@inheritDoc}
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     *
     * {@inheritDoc}
     */
    public void setTitle( String title )
    {
        this._strTitle = title;
    }

    /**
     *
     * {@inheritDoc}
     */
    public IFeedResourceImage getImage(  )
    {
        return _image;
    }

    /**
     * Sets the image
     * @param image the image
     */
    public void setImage( IFeedResourceImage image )
    {
        this._image = image;
    }

    /**
     *
     * {@inheritDoc}
     */
    public List<IFeedResourceItem> getItems(  )
    {
        return _listItems;
    }

    /**
     * Sets the items
     * @param listItems the items
     */
    public void setItems( List<IFeedResourceItem> listItems )
    {
        this._listItems = listItems;
    }

    /**
     *
     * {@inheritDoc}
     */
    public String getLanguage(  )
    {
        return _strLanguage;
    }

    /**
     * Sets the language
     * @param strLanguage the language
     */
    public void setLanguage( String strLanguage )
    {
        this._strLanguage = strLanguage;
    }

    /**
     *
     * {@inheritDoc}
     */
    public String getLink(  )
    {
        return _strLink;
    }

    /**
     * Sets the link
     * @param strLink the link
     */
    public void setLink( String strLink )
    {
        this._strLink = strLink;
    }

    /**
     *
     * {@inheritDoc}
     */
    public Date getDate(  )
    {
        return _date;
    }

    /**
     *
     * {@inheritDoc}
     */
    public void setDate( Date date )
    {
        this._date = date;
    }
}
