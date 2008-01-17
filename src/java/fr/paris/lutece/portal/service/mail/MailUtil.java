/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.ByteArrayDataSource;
import fr.paris.lutece.util.mail.FileAttachment;
import fr.paris.lutece.util.mail.HtmlDocument;
import fr.paris.lutece.util.mail.UrlAttachment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


/**
 * This class provides mail utils.
 */
final class MailUtil
{
    private static final String PROPERTY_CHARSET = "mail.charset";
    private static final String PROPERTY_MAIL_LIST_SEPARATOR = "mail.list.separator";
    private static final String PROPERTY_MAIL_TYPE_HTML = "mail.type.html";
    private static final String PROPERTY_MAIL_TYPE_PLAIN = "mail.type.plain";
    private static final String MAIL_HOST = "mail.host";
    private static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    private static final String SMTP = "smtp";
    private static final String ENCODING = "Q";
    private static final String HEADER_NAME = "Content-Transfer-Encoding";
    private static final String HEADER_VALUE = "quoted-printable";
    private static final String HEADER_CONTENT_LOCATION = "Content-Location";
    private static final String BODY_PART_MIME_TYPE = "text/html";
    private static final String MULTIPART_RELATED = "related";
    private static final String MSG_ATTACHMENT_NOT_FOUND = " not found, document ignored.";

    /**
     * Creates a new MailUtil object
     */
    private MailUtil(  )
    {
    }

    /**
     * Send a text message.
     *
     * @param strHost The SMTP name or IP address.
     * @param strRecipientsTo The list of the main recipients email.Every recipient
    *                   must be separated by the mail separator defined in config.properties
    * @param strRecipientsCc The recipients list of the carbon copies .
    * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param transport the smtp transport object
     * @param session the smtp session object
     * @throws MessagingException The messaging exception
     */
    protected static void sendMessageText( String strHost, String strRecipientsTo, String strRecipientsCc,
        String strRecipientsBcc, String strSenderName, String strSenderEmail, String strSubject, String strMessage,
        Transport transport, Session session ) throws MessagingException, AddressException, SendFailedException
    {
        Message msg = prepareMessage( strHost, strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName,
                strSenderEmail, strSubject, session );
        msg.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_PLAIN ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );

        // Send the message
        transport.sendMessage( msg, msg.getAllRecipients(  ) );
    }

    /**
     * Send a HTML formated message.
     *
     * @param strHost The SMTP name or IP address.
     * @param strRecipientsTo The list of the main recipients email.Every recipient
    *                   must be separated by the mail separator defined in config.properties
    * @param strRecipientsCc The recipients list of the carbon copies .
    * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param transport the smtp transport object
     * @param session the smtp session object
     * @throws MessagingException The messaging exception
     */
    protected static void sendMessageHtml( String strHost, String strRecipientsTo, String strRecipientsCc,
        String strRecipientsBcc, String strSenderName, String strSenderEmail, String strSubject, String strMessage,
        Transport transport, Session session ) throws MessagingException, AddressException, SendFailedException
    {
        Message msg = prepareMessage( strHost, strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName,
                strSenderEmail, strSubject, session );

        msg.setHeader( HEADER_NAME, HEADER_VALUE );
        // Message body formated in HTML
        msg.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_HTML ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );

        // Send the message
        transport.sendMessage( msg, msg.getAllRecipients(  ) );
    }

    /**
     * Send a Multipart HTML message with the attachements associated to the message and attached files.
     * FIXME: use prepareMessage method
     * @param strHost The SMTP name or IP address.
     * @param strRecipientsTo The list of the main recipients email.Every recipient
    *                   must be separated by the mail separator defined in config.properties
    * @param strRecipientsCc The recipients list of the carbon copies .
    * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param urlAttachements The List of UrlAttachement Object, containing the URL of attachments associated with their content-location.
     * @param  fileAttachement The list of attached files
     * @param transport the smtp transport object
     * @param session the smtp session object
     * @throws MessagingException The messaging exception
     */
    protected static void sendMultipartMessageHtml( String strHost, String strRecipientsTo, String strRecipientsCc,
        String strRecipientsBcc, String strSenderName, String strSenderEmail, String strSubject, String strMessage,
        List<UrlAttachment> urlAttachements, List<FileAttachment> fileAttachements, Transport transport, Session session )
        throws MessagingException, AddressException, SendFailedException
    {
        Message msg = prepareMessage( strHost, strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName,
                strSenderEmail, strSubject, session );
        msg.setHeader( HEADER_NAME, HEADER_VALUE );

        // Creation of the root part containing all the elements of the message 
        MimeMultipart multipart = new MimeMultipart( MULTIPART_RELATED );

        // Creation of the html part, the "core" of the message
        BodyPart msgBodyPart = new MimeBodyPart(  );
        //msgBodyPart.setContent( strMessage, BODY_PART_MIME_TYPE );
        msgBodyPart.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_HTML ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );
        multipart.addBodyPart( msgBodyPart );

        if ( urlAttachements != null )
        {
            for ( UrlAttachment urlAttachement : urlAttachements )
            {
                msgBodyPart = new MimeBodyPart(  );

                DataHandler handler = new DataHandler( urlAttachement.getUrlData(  ) );

                try
                {
                    handler.getContent(  );
                }
                catch ( IOException e )
                {
                    // Document is ignored
                    AppLogService.info( urlAttachement.getContentLocation(  ) + MSG_ATTACHMENT_NOT_FOUND );

                    continue;
                }

                // Fill this part, then add it to the root part with the good headers
                msgBodyPart.setDataHandler( handler );
                msgBodyPart.setHeader( HEADER_CONTENT_LOCATION, urlAttachement.getContentLocation(  ) );
                multipart.addBodyPart( msgBodyPart );
            }
        }

        //add File Attachement
        if ( fileAttachements != null )
        {
            for ( FileAttachment fileAttachement : fileAttachements )
            {
                String strFileName = fileAttachement.getFileName(  );
                byte[] bContentFile = (byte[]) fileAttachement.getData(  );
                String strContentType = fileAttachement.getType(  );
                ByteArrayDataSource dataSource = new ByteArrayDataSource( bContentFile, strContentType );
                msgBodyPart = new MimeBodyPart(  );
                msgBodyPart.setDataHandler( new DataHandler( dataSource ) );
                msgBodyPart.setFileName( strFileName );
                msgBodyPart.setDisposition( "attachment" );
                multipart.addBodyPart( msgBodyPart );
            }
        }

        msg.setContent( multipart );

        // Send the message
        transport.sendMessage( msg, msg.getAllRecipients(  ) );
    }

    /**
     * Send a Multipart text message with attached files.
     * FIXME: use prepareMessage method
     * @param strHost The SMTP name or IP address.
     * @param strRecipientsTo The list of the main recipients email.Every recipient
    *                   must be separated by the mail separator defined in config.properties
    * @param strRecipientsCc The recipients list of the carbon copies .
    * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param  fileAttachements The list of attached files
     * @param transport the smtp transport object
     * @param session the smtp session object
     * @throws MessagingException The messaging exception
     */
    protected static void sendMultipartMessageText( String strHost, String strRecipientsTo, String strRecipientsCc,
        String strRecipientsBcc, String strSenderName, String strSenderEmail, String strSubject, String strMessage,
        List<FileAttachment> fileAttachements, Transport transport, Session session )
        throws MessagingException, AddressException, SendFailedException
    {
        Message msg = prepareMessage( strHost, strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName,
                strSenderEmail, strSubject, session );
        msg.setHeader( HEADER_NAME, HEADER_VALUE );

        // Creation of the root part containing all the elements of the message 
        MimeMultipart multipart = new MimeMultipart(  );

        // Creation of the html part, the "core" of the message
        BodyPart msgBodyPart = new MimeBodyPart(  );
        //msgBodyPart.setContent( strMessage, BODY_PART_MIME_TYPE );
        msgBodyPart.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_PLAIN ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );
        multipart.addBodyPart( msgBodyPart );

        //add File Attachement
        //add File Attachement
        if ( fileAttachements != null )
        {
            for ( FileAttachment fileAttachement : fileAttachements )
            {
                String strFileName = fileAttachement.getFileName(  );
                byte[] bContentFile = (byte[]) fileAttachement.getData(  );
                String strContentType = fileAttachement.getType(  );
                ByteArrayDataSource dataSource = new ByteArrayDataSource( bContentFile, strContentType );
                msgBodyPart = new MimeBodyPart(  );
                msgBodyPart.setDataHandler( new DataHandler( dataSource ) );
                msgBodyPart.setFileName( strFileName );
                msgBodyPart.setDisposition( "attachment" );
                multipart.addBodyPart( msgBodyPart );
            }
        }

        msg.setContent( multipart );

        // Send the message
        transport.sendMessage( msg, msg.getAllRecipients(  ) );
    }

    /**
     * Extract a collection of elements to be attached to a mail from an HTML string.
     *
     * The collection contains the Url used for created DataHandler  for each url associated with
     * an HTML tag img, script or link. Those urls must start with the url strBaseUrl.
     *
     * @param strHtml The HTML code.
     * @param strBaseUrl The base url, can be null in order to extract all urls.
     * @param useAbsoluteUrl Determine if we use absolute or relative url for attachement content-location
     * @return a collection of UrlAttachment Object  for created  DataHandler associated with attachment urls.
     */
    protected static List<UrlAttachment> getUrlAttachmentList( String strHtml, String strBaseUrl, boolean useAbsoluteUrl )
    {
        List<UrlAttachment> listUrlAttachement = new ArrayList<UrlAttachment>(  );
        HtmlDocument doc = new HtmlDocument( strHtml, strBaseUrl, useAbsoluteUrl );
        listUrlAttachement.addAll( doc.getAllUrlsAttachement( HtmlDocument.ELEMENT_IMG ) );
        listUrlAttachement.addAll( doc.getAllUrlsAttachement( HtmlDocument.ELEMENT_CSS ) );
        listUrlAttachement.addAll( doc.getAllUrlsAttachement( HtmlDocument.ELEMENT_JAVASCRIPT ) );

        return listUrlAttachement;
    }

    /**
     * Common part for sending message process :
     * <ul>
     *         <li>initializes a mail session with the smtp server</li>
     *         <li>activates debugging</li>
     *         <li>instanciates and initializes a mime message</li>
     *  <li>sets the sent date, the from field, the subject field</li>
     *  <li>sets the recipients</li>
     * </ul>
     *
     * @param strHost The SMTP name or IP address.
     * @param strRecipientsTo The list of the main recipients email.Every recipient
    *                   must be separated by the mail separator defined in config.properties
    * @param strRecipientsCc The recipients list of the carbon copies .
    * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param session the smtp session object
     * @return the message object initialised with the common settings
     * @throws MessagingException The messaging exception
     */
    protected static Message prepareMessage( String strHost, String strRecipientsTo, String strRecipientsCc,
        String strRecipientsBcc, String strSenderName, String strSenderEmail, String strSubject, Session session )
        throws MessagingException, AddressException
    {
        // Instanciate and initialize a mime message
        Message msg = new MimeMessage( session );
        msg.setSentDate( new Date(  ) );

        try
        {
            msg.setFrom( new InternetAddress( strSenderEmail, strSenderName,
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) );
            msg.setSubject( MimeUtility.encodeText( strSubject, AppPropertiesService.getProperty( PROPERTY_CHARSET ),
                    ENCODING ) );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new AppException( e.toString(  ) );
        }

        // Instanciation of the list of address
        if ( strRecipientsTo != null )
        {
            msg.setRecipients( Message.RecipientType.TO, getAllAdressOfRecipients( strRecipientsTo ) );
        }

        if ( strRecipientsCc != null )
        {
            msg.setRecipients( Message.RecipientType.CC, getAllAdressOfRecipients( strRecipientsCc ) );
        }

        if ( strRecipientsBcc != null )
        {
            msg.setRecipients( Message.RecipientType.BCC, getAllAdressOfRecipients( strRecipientsBcc ) );
        }

        return msg;
    }

    /**
     * Open mail session with the SMTP server
     * @param strHost The SMTP name or IP address.
     * @return the mails session object
     */
    protected static Session getMailSession( String strHost )
    {
        boolean sessionDebug = false;

        // Initializes a mail session with the SMTP server
        Properties props = System.getProperties(  );
        props.put( MAIL_HOST, strHost );
        props.put( MAIL_TRANSPORT_PROTOCOL, SMTP );

        Session mailSession = Session.getDefaultInstance( props, null );
        // Activate debugging
        mailSession.setDebug( sessionDebug );

        return mailSession;
    }

    /**
     * return the transport object of the smtp session
     * @param session the smtp session
     * @return the transport object of the smtp session
     *
     */
    protected static Transport getTransport( Session session )
        throws NoSuchProviderException
    {
        return session.getTransport( SMTP );
    }

    /**
     * extract The list of Internet Adress content in the string strRecipients
     * @param strRecipients The list of recipient separated by the mail separator defined in config.properties
     * @return The list of Internet Adress content in the string strRecipients
     * @throws MessagingException
     */
    private static InternetAddress[] getAllAdressOfRecipients( String strRecipients )
        throws AddressException
    {
        StringTokenizer st = new StringTokenizer( strRecipients,
                AppPropertiesService.getProperty( PROPERTY_MAIL_LIST_SEPARATOR, ";" ) );
        List<String> listRecipients = new ArrayList<String>(  );

        while ( st.hasMoreTokens(  ) )
        {
            listRecipients.add( st.nextToken(  ) );
        }

        InternetAddress[] address = new InternetAddress[listRecipients.size(  )];

        // Initialization of the address array
        for ( int i = 0; i < listRecipients.size(  ); i++ )
        {
            address[i] = new InternetAddress( listRecipients.get( i ) );
        }

        return address;
    }
}
