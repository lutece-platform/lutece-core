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
package fr.paris.lutece.portal.service.mail;

import fr.paris.lutece.util.mail.FileAttachment;
import fr.paris.lutece.util.mail.UrlAttachment;

import java.io.Serializable;

import java.util.List;


/**
 * MailIem
 */
public class MailItem implements Serializable
{
    public static final int FORMAT_HTML = 0; // Default
    public static final int FORMAT_TEXT = 1;
    public static final int FORMAT_MULTIPART_HTML = 2;
    public static final int FORMAT_MULTIPART_TEXT = 3;
    public static final int FORMAT_CALENDAR = 4;
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private String _strRecipientsTo;
    private String _strRecipientsCc;
    private String _strRecipientsBcc;
    private String _strSenderName;
    private String _strSenderEmail;
    private String _strSubject;
    private String _strMessage;
    private String _strCalendarMessage;
    private boolean _bCreateEvent;
    private int _nFormat;
    private List<UrlAttachment> _listUrlsAttachement;
    private List<FileAttachment> _listFilesAttachement;
    private boolean _bUniqueRecipientTo;

    /**
    * Returns the Recipient
    *
    * @return The Recipient
    */
    public String getRecipientsTo(  )
    {
        return _strRecipientsTo;
    }

    /**
     * Sets the Recipient
     *
     * @param strRecipient The Recipient
     */
    public void setRecipientsTo( String strRecipient )
    {
        _strRecipientsTo = strRecipient;
    }

    /**
     * Returns the Recipient
     *
     * @return The Recipient
     */
    public String getRecipientsCc(  )
    {
        return _strRecipientsCc;
    }

    /**
     * Sets the Recipient
     *
     * @param strRecipient The Recipient
     */
    public void setRecipientsCc( String strRecipient )
    {
        _strRecipientsCc = strRecipient;
    }

    /**
     * Returns the Recipient
     *
     * @return The Recipient
     */
    public String getRecipientsBcc(  )
    {
        return _strRecipientsBcc;
    }

    /**
     * Sets the Recipient
     *
     * @param strRecipient The Recipient
     */
    public void setRecipientsBcc( String strRecipient )
    {
        _strRecipientsBcc = strRecipient;
    }

    /**
     * Returns the SenderName
     *
     * @return The SenderName
     */
    public String getSenderName(  )
    {
        return _strSenderName;
    }

    /**
     * Sets the SenderName
     *
     * @param strSenderName The SenderName
     */
    public void setSenderName( String strSenderName )
    {
        _strSenderName = strSenderName;
    }

    /**
     * Returns the SenderEmail
     *
     * @return The SenderEmail
     */
    public String getSenderEmail(  )
    {
        return _strSenderEmail;
    }

    /**
     * Sets the SenderEmail
     *
     * @param strSenderEmail The SenderEmail
     */
    public void setSenderEmail( String strSenderEmail )
    {
        _strSenderEmail = strSenderEmail;
    }

    /**
     * Returns the Subject
     *
     * @return The Subject
     */
    public String getSubject(  )
    {
        return _strSubject;
    }

    /**
     * Sets the Subject
     *
     * @param strSubject The Subject
     */
    public void setSubject( String strSubject )
    {
        _strSubject = strSubject;
    }

    /**
     * Returns the Message
     *
     * @return The Message
     */
    public String getMessage(  )
    {
        return _strMessage;
    }

    /**
     * Sets the Message
     *
     * @param strMessage The Message
     */
    public void setMessage( String strMessage )
    {
        _strMessage = strMessage;
    }

    /**
     * Returns the calendar message
     *
     * @return The calendar message
     */
    public String getCalendarMessage(  )
    {
        return _strCalendarMessage;
    }

    /**
     * Sets the calendar message
     *
     * @param strCalendarMessage The calendar message
     */
    public void setCalendarMessage( String strCalendarMessage )
    {
        _strCalendarMessage = strCalendarMessage;
    }

    /**
     * Check if the calendar event of this mail item should be created or
     * removed
     * @return True if the event should be created, false if it should be
     *         removed
     */
    public boolean getCreateEvent(  )
    {
        return _bCreateEvent;
    }

    /**
     * Create or remove the event of this mail item
     * @param bCreateEvent True to create the event, false otherwise
     */
    public void setCreateEvent( boolean bCreateEvent )
    {
        this._bCreateEvent = bCreateEvent;
    }

    /**
     * Returns the Format
     *
     * @return The Format
     */
    public int getFormat(  )
    {
        return _nFormat;
    }

    /**
     * Sets the Format
     *
     * @param nFormat The Format
     */
    public void setFormat( int nFormat )
    {
        _nFormat = nFormat;
    }

    /**
     * Returns a collection of files attachement
     *
     * @return The Attachements Map
     */
    public List<FileAttachment> getFilesAttachement(  )
    {
        return _listFilesAttachement;
    }

    /**
     * Set a collection of files attachement
     *
     * @param fileAttachements  The collection of files attachement
     */
    public void setFilesAttachement( List<FileAttachment> fileAttachements )
    {
        _listFilesAttachement = fileAttachements;
    }

    /**
     * return the list of urls attachement
     * @return the list of urls attachement
     */
    public List<UrlAttachment> getUrlsAttachement(  )
    {
        return _listUrlsAttachement;
    }

    /**
     * set the list of urls attachement
     * @param urlsAttachement the list of urls attachement
     */
    public void setUrlsAttachement( List<UrlAttachment> urlsAttachement )
    {
        _listUrlsAttachement = urlsAttachement;
    }

    /**
     * set true if the mail must be send unitarily for each recipient
     * @param bUniqueRecipient true if the mail must be send unitarily for each recipient
     */
    public void setUniqueRecipientTo( boolean bUniqueRecipient )
    {
        _bUniqueRecipientTo = bUniqueRecipient;
    }

    /**
     *
     * @return if the mail must be send unitarily for each recipient
     */
    public boolean isUniqueRecipientTo(  )
    {
        return _bUniqueRecipientTo;
    }
}
