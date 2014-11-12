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


/**
 *
 * An feed Item used by Rss resources.
 * <ul>
 * <li>title
 * <li>link
 * <li>description
 * <li>guid
 * <li>pubDate
 * </ul>
 */
public interface IFeedResourceItem
{
    /**
     * Gets the item title
     * @return the item title
     */
    String getTitle(  );

    /**
     * Sets the item title
     * @param strTitle the item title
     */
    void setTitle( String strTitle );

    /**
     * Gets the item link
     * @return the link
     */
    String getLink(  );

    /**
     * Sets the item link
     * @param strLink the item link
     */
    void setLink( String strLink );

    /**
     * Gets the item description
     * @return the description
     */
    String getDescription(  );

    /**
     * Sets the description
     * @param strDescription the description
     */
    void setDescription( String strDescription );

    /**
     * Gets the item id
     * @return the item id
     */
    String getGUID(  );

    /**
     * Sets the item guid
     * @param strGUID the item guid
     */
    void setGUID( String strGUID );

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
