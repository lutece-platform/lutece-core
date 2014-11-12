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
 * A feed resource.
 * <ul>
 * <li>title
 * <li>link
 * <li>description
 * <li>language
 * <li>date
 * <li>image
 * <li>items
 * </ul>
 */
public interface IFeedResource
{
    /**
     * Gets the feed title
     * @return the title
     */
    String getTitle(  );

    /**
     * Sets the feed title
     * @param strTitle the title
     */
    void setTitle( String strTitle );

    /**
     * Gets the feed link - usually the site url.
     * @return the link
     */
    String getLink(  );

    /**
     * Sets the feed link
     * @param strLink the link -  usually the site url.
     */
    void setLink( String strLink );

    /**
     * Gets the feed description
     * @return the description
     */
    String getDescription(  );

    /**
     * Sets the description
     * @param strDescription the description
     */
    void setDescription( String strDescription );

    /**
     * Gets the feed language
     * @return the language
     */
    String getLanguage(  );

    /**
     * Sets the language
     * @param strLanguage the language
     */
    void setLanguage( String strLanguage );

    /**
     * Gets the feed items
     * @return the items
     */
    List<IFeedResourceItem> getItems(  );

    /**
     * Sets the feed items
     * @param listItems the items
     */
    void setItems( List<IFeedResourceItem> listItems );

    /**
     * Gets the image
     * @return the image
     */
    IFeedResourceImage getImage(  );

    /**
     * Sets the image
     * @param image the image
     */
    void setImage( IFeedResourceImage image );

    /**
     * Gets the item publishing date
     * @return the date
     */
    Date getDate(  );

    /**
     * Set the item publishing date
     * @param date the date
     */
    void setDate( Date date );
}
