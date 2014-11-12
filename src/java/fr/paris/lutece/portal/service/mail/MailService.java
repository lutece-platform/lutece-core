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

import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.FileAttachment;
import fr.paris.lutece.util.mail.UrlAttachment;

import java.util.List;


/**
 * Application Mail Service
 */
public final class MailService
{
    private static final String KEY_NO_REPLY_EMAIL = "portal.site.site_property.noreply_email";
    private static final String PROPERTY_MAIL_NOREPLY_EMAIL = "mail.noreply.email";
    private static final String BEAN_MAIL_QUEUE = "mailQueue";

    /** Creates a new instance of AppMailService */
    private MailService(  )
    {
    }

    /**
     * Send a message asynchronously. The message is queued until a daemon
     * thread send all awaiting messages
     * @param strRecipient The recipient email.
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @deprecated Use
     *             {@link #sendMailText(String strRecipient, String strSenderName, String strSenderEmail, String strSubject, String strMessage)}
     *             instead
     */
    @Deprecated
    public static void sendMail( String strRecipient, String strSenderName, String strSenderEmail, String strSubject,
        String strMessage )
    {
        sendMailText( strRecipient, strSenderName, strSenderEmail, strSubject, strMessage );
    }

    /**
     * Send a HTML message asynchronously. The message is queued until a daemon
     * thread send all awaiting messages
     *
     * @param strRecipientsTo The list of the main recipients email.Every
     *            recipient
     *            must be separated by the mail separator define in
     *            config.properties
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     */
    public static void sendMailHtml( String strRecipientsTo, String strSenderName, String strSenderEmail,
        String strSubject, String strMessage )
    {
        sendMailHtml( strRecipientsTo, null, null, strSenderName, strSenderEmail, strSubject, strMessage );
    }

    /**
     * Send a HTML message asynchronously. The message is queued until a daemon
     * thread send all awaiting messages
     *
     * @param strRecipientsTo The list of the main recipients email.Every
     *            recipient
     *            must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     */
    public static void sendMailHtml( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage )
    {
        sendMailHtml( strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName, strSenderEmail, strSubject,
            strMessage, false );
    }

    /**
     * Send a HTML message asynchronously. The message is queued until a daemon
     * thread send all awaiting messages
     *
     * @param strRecipientsTo The list of the main recipients email.Every
     *            recipient
     *            must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param bUniqueRecipientTo true if the mail must be send unitarily for
     *            each recipient
     */
    public static void sendMailHtml( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage, boolean bUniqueRecipientTo )
    {
        MailItem item = new MailItem(  );
        item.setRecipientsTo( strRecipientsTo );
        item.setRecipientsCc( strRecipientsCc );
        item.setRecipientsBcc( strRecipientsBcc );
        item.setSenderName( strSenderName );
        item.setSenderEmail( strSenderEmail );
        item.setSubject( strSubject );
        item.setMessage( strMessage );
        item.setFormat( MailItem.FORMAT_HTML );
        item.setUniqueRecipientTo( bUniqueRecipientTo );

        IMailQueue queue = (IMailQueue) SpringContextService.getBean( BEAN_MAIL_QUEUE );
        queue.send( item );
    }

    /**
     * Send a HTML message asynchronously with the attachments associated to
     * the message . The message is queued until a daemon
     * thread send all awaiting messages
     *
     * @param strRecipientsTo The list of the main recipients email.Every
     *            recipient
     *            must be separated by the mail separator defined in
     *            config.properties
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param urlsAttachement The List of UrlAttachement Object, containing the
     *            URL of attachments associated with their content-location.
     */
    public static void sendMailMultipartHtml( String strRecipientsTo, String strSenderName, String strSenderEmail,
        String strSubject, String strMessage, List<UrlAttachment> urlsAttachement )
    {
        sendMailMultipartHtml( strRecipientsTo, null, null, strSenderName, strSenderEmail, strSubject, strMessage,
            urlsAttachement, null );
    }

    /**
     * Send a HTML message asynchronously with the attachments associated to
     * the message and attached files . The message is queued until a daemon
     * thread send all awaiting messages
     *
     * @param strRecipientsTo The list of the main recipients email.Every
     *            recipient
     *            must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param urlsAttachement The List of UrlAttachement Object, containing the
     *            URL of attachments associated with their content-location
     * @param filesAttachement The list of attached files.
     */
    public static void sendMailMultipartHtml( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage,
        List<UrlAttachment> urlsAttachement, List<FileAttachment> filesAttachement )
    {
        sendMailMultipartHtml( strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName, strSenderEmail,
            strSubject, strMessage, urlsAttachement, filesAttachement, false );
    }

    /**
     * Send a HTML message asynchronously with the attachments associated to
     * the message and attached files . The message is queued until a daemon
     * thread send all awaiting messages
     *
     * @param strRecipientsTo The list of the main recipients email.Every
     *            recipient
     *            must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param urlsAttachement The List of UrlAttachement Object, containing the
     *            URL of attachments associated with their content-location
     * @param filesAttachement The list of attached files.
     * @param bUniqueRecipientTo true if the mail must be send unitarily for
     *            each recipient
     */
    public static void sendMailMultipartHtml( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage,
        List<UrlAttachment> urlsAttachement, List<FileAttachment> filesAttachement, boolean bUniqueRecipientTo )
    {
        MailItem item = new MailItem(  );
        item.setRecipientsTo( strRecipientsTo );
        item.setRecipientsCc( strRecipientsCc );
        item.setRecipientsBcc( strRecipientsBcc );
        item.setSenderName( strSenderName );
        item.setSenderEmail( strSenderEmail );
        item.setSubject( strSubject );
        item.setMessage( strMessage );
        item.setFormat( MailItem.FORMAT_MULTIPART_HTML );
        item.setUrlsAttachement( urlsAttachement );
        item.setFilesAttachement( filesAttachement );
        item.setUniqueRecipientTo( bUniqueRecipientTo );

        IMailQueue queue = (IMailQueue) SpringContextService.getBean( BEAN_MAIL_QUEUE );
        queue.send( item );
    }

    /**
     * Send a calendar message asynchronously. The message is queued until a
     * daemon thread send all awaiting messages
     * @param strRecipientsTo The list of the main recipients email. Every
     *            recipient must be separated by the mail separator defined in
     *            config.properties
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param strCalendarMessage The calendar message
     * @param bCreateEvent True to create the calendar event, false to remove it
     */
    public static void sendMailCalendar( String strRecipientsTo, String strSenderName, String strSenderEmail,
        String strSubject, String strMessage, String strCalendarMessage, boolean bCreateEvent )
    {
        sendMailCalendar( strRecipientsTo, null, null, strSenderName, strSenderEmail, strSubject, strMessage,
            strCalendarMessage, bCreateEvent );
    }

    /**
     * Send a calendar message asynchronously. The message is queued until a
     * daemon thread send all awaiting messages
     * @param strRecipientsTo The list of the main recipients email. Every
     *            recipient must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param strCalendarMessage The calendar message
     * @param bCreateEvent True to create the calendar event, false to remove it
     */
    public static void sendMailCalendar( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage, String strCalendarMessage,
        boolean bCreateEvent )
    {
        sendMailCalendar( strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName, strSenderEmail,
            strSubject, strMessage, strCalendarMessage, bCreateEvent, false );
    }

    /**
     * Send a calendar message asynchronously. The message is queued until a
     * daemon thread send all awaiting messages
     * @param strRecipientsTo The list of the main recipients email. Every
     *            recipient must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param strCalendarMessage The calendar message
     * @param bCreateEvent True to create the calendar event, false to remove it
     * @param bUniqueRecipientTo true if the mail must be send unitarily for
     *            each recipient
     */
    public static void sendMailCalendar( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage, String strCalendarMessage,
        boolean bCreateEvent, boolean bUniqueRecipientTo )
    {
        MailItem item = new MailItem(  );
        item.setRecipientsTo( strRecipientsTo );
        item.setRecipientsCc( strRecipientsCc );
        item.setRecipientsBcc( strRecipientsBcc );
        item.setSenderName( strSenderName );
        item.setSenderEmail( strSenderEmail );
        item.setSubject( strSubject );
        item.setMessage( strMessage );
        item.setCalendarMessage( strCalendarMessage );
        item.setCreateEvent( bCreateEvent );
        item.setFormat( MailItem.FORMAT_CALENDAR );
        item.setUniqueRecipientTo( bUniqueRecipientTo );

        IMailQueue queue = (IMailQueue) SpringContextService.getBean( BEAN_MAIL_QUEUE );
        queue.send( item );
    }

    /**
     * Send a text message asynchronously. The message is queued until a daemon
     * thread send all awaiting messages
     * @param strRecipientsTo The list of the main recipients email. Every
     *            recipient must be separated by the mail separator defined in
     *            config.properties
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     */
    public static void sendMailText( String strRecipientsTo, String strSenderName, String strSenderEmail,
        String strSubject, String strMessage )
    {
        sendMailText( strRecipientsTo, null, null, strSenderName, strSenderEmail, strSubject, strMessage );
    }

    /**
     * Send a text message asynchronously. The message is queued until a daemon
     * thread send all awaiting messages
     * @param strRecipientsTo The list of the main recipients email. Every
     *            recipient must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     */
    public static void sendMailText( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage )
    {
        sendMailText( strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName, strSenderEmail, strSubject,
            strMessage, false );
    }

    /**
     * Send a text message asynchronously. The message is queued until a daemon
     * thread send all awaiting messages
     * @param strRecipientsTo The list of the main recipients email. Every
     *            recipient must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param bUniqueRecipientTo true if the mail must be send unitarily for
     *            each recipient
     */
    public static void sendMailText( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage, boolean bUniqueRecipientTo )
    {
        MailItem item = new MailItem(  );
        item.setRecipientsTo( strRecipientsTo );
        item.setRecipientsCc( strRecipientsCc );
        item.setRecipientsBcc( strRecipientsBcc );
        item.setSenderName( strSenderName );
        item.setSenderEmail( strSenderEmail );
        item.setSubject( strSubject );
        item.setMessage( strMessage );
        item.setFormat( MailItem.FORMAT_TEXT );
        item.setUniqueRecipientTo( bUniqueRecipientTo );

        IMailQueue queue = (IMailQueue) SpringContextService.getBean( BEAN_MAIL_QUEUE );
        queue.send( item );
    }

    /**
     * Send a text message asynchronously with attached files. The message is
     * queued until a daemon
     * thread send all awaiting messages
     *
     * @param strRecipientsTo The list of the main recipients email.Every
     *            recipient
     *            must be separated by the mail separator defined in
     *            config.properties
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param filesAttachement The list of attached files.
     */
    public static void sendMailMultipartText( String strRecipientsTo, String strSenderName, String strSenderEmail,
        String strSubject, String strMessage, List<FileAttachment> filesAttachement )
    {
        sendMailMultipartText( strRecipientsTo, null, null, strSenderName, strSenderEmail, strSubject, strMessage,
            filesAttachement );
    }

    /**
     * Send a text message asynchronously with attached files. The message is
     * queued until a daemon
     * thread send all awaiting messages
     * @param strRecipientsTo The list of the main recipients email.Every
     *            recipient
     *            must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param filesAttachement The list of attached files.
     */
    public static void sendMailMultipartText( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage,
        List<FileAttachment> filesAttachement )
    {
        sendMailMultipartText( strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName, strSenderEmail,
            strSubject, strMessage, filesAttachement, false );
    }

    /**
     * Send a text message asynchronously with attached files. The message is
     * queued until a daemon
     * thread send all awaiting messages
     * @param strRecipientsTo The list of the main recipients email.Every
     *            recipient
     *            must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param filesAttachement The list of attached files.
     * @param bUniqueRecipientTo true if the mail must be send unitarily for
     *            each recipient
     */
    public static void sendMailMultipartText( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage,
        List<FileAttachment> filesAttachement, boolean bUniqueRecipientTo )
    {
        MailItem item = new MailItem(  );
        item.setRecipientsTo( strRecipientsTo );
        item.setRecipientsCc( strRecipientsCc );
        item.setRecipientsBcc( strRecipientsBcc );
        item.setSenderName( strSenderName );
        item.setSenderEmail( strSenderEmail );
        item.setSubject( strSubject );
        item.setMessage( strMessage );
        item.setFormat( MailItem.FORMAT_MULTIPART_TEXT );
        item.setFilesAttachement( filesAttachement );
        item.setUniqueRecipientTo( bUniqueRecipientTo );

        IMailQueue queue = (IMailQueue) SpringContextService.getBean( BEAN_MAIL_QUEUE );
        queue.send( item );
    }

    /**
     * Shutdown the service
     */
    public static void shutdown(  )
    {
        // if there is mails that have not been sent call the daemon to flush the list
        Daemon daemon = AppDaemonService.getDaemon( "mailSender" );

        if ( daemon != null )
        {
            daemon.run(  );
        }
    }

    /**
     * Returns a no reply email address defined in config.properties
     * @return A no reply email
     */
    public static String getNoReplyEmail(  )
    {
        String strDefault = AppPropertiesService.getProperty( PROPERTY_MAIL_NOREPLY_EMAIL );

        return DatastoreService.getDataValue( KEY_NO_REPLY_EMAIL, strDefault );
    }

    /**
     * Returns the mail queue
     * @return the mail queue
     */
    public static IMailQueue getQueue(  )
    {
        IMailQueue queue = (IMailQueue) SpringContextService.getBean( BEAN_MAIL_QUEUE );

        return queue;
    }

    /**
     * Extract a collection of elements to be attached to a mail from an HTML
     * string.
     *
     * The collection contains the Url used for created DataHandler for each url
     * associated with
     * an HTML tag img, script or link. Those urls must start with the url
     * strBaseUrl.
     *
     * @param strHtml The HTML code.
     * @param strBaseUrl The base url, can be null in order to extract all urls.
     * @param useAbsoluteUrl Determine if we use absolute or relative url for
     *            attachement content-location
     * @return a collection of UrlAttachment Object for created DataHandler
     *         associated with attachment urls.
     */
    public static List<UrlAttachment> getUrlAttachmentList( String strHtml, String strBaseUrl, boolean useAbsoluteUrl )
    {
        return MailUtil.getUrlAttachmentList( strHtml, strBaseUrl, useAbsoluteUrl );
    }

    /**
     * Return a String that contains a list of recipients separated with mail
     * separator
     * @param listRecipients a list of string recipients
     * @return a String that contains a list of recipients separated with mail
     *         separator
     */
    public static String getStrRecipients( List<String> listRecipients )
    {
        return MailUtil.getStrRecipients( listRecipients );
    }

    /**
     * Get a string that contains an html link to the site back office or front
     * office.
     * @param strBaseUrl The base url of the site
     * @param linkToFrontOffice True if the link should be directed to the front
     *            office, false if it should be directed to the back office.
     * @return A string containing an html link.
     */
    public static String getSiteLink( String strBaseUrl, boolean linkToFrontOffice )
    {
        StringBuilder sb = new StringBuilder(  );
        String strSiteName = PortalService.getSiteName(  );

        if ( strSiteName != null )
        {
            sb.append( "<a title=\"" );
            sb.append( strSiteName );
            sb.append( "\" href=\"" );
            sb.append( strBaseUrl );

            String strUrl;

            if ( linkToFrontOffice )
            {
                strUrl = AppPathService.getPortalUrl(  );
            }
            else
            {
                strUrl = AppPathService.getAdminMenuUrl(  );
            }

            if ( strUrl != null )
            {
                sb.append( strUrl );
            }

            sb.append( "\" >" );
            sb.append( strSiteName );
            sb.append( "</a>" );
        }

        return sb.toString(  );
    }
}
