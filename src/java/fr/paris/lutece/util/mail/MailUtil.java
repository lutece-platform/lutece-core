/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.util.mail;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URL;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


/**
 * This class provides mail utils.
 * @deprecated
 */
public final class MailUtil
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
     * Send a message.
     *
     * @param strHost The SMTP name or IP address.
     * @param strRecipient The recipient email.
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @throws MessagingException The messaging exception
     */
    public static void sendMessage( String strHost, String strRecipient, String strSenderName, String strSenderEmail,
        String strSubject, String strMessage ) throws MessagingException
    {
        Message msg = prepareMessage( strHost, strRecipient, strSenderName, strSenderEmail, strSubject );
        msg.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_PLAIN ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );

        // Send the message
        Transport.send( msg );
    }

    /**
     * Send a HTML formated message.
     *
     * @param strHost The SMTP name or IP address.
     * @param strRecipient The recipient email.
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @throws MessagingException The messaging exception
     */
    public static void sendMessageHtml( String strHost, String strRecipient, String strSenderName,
        String strSenderEmail, String strSubject, String strMessage )
        throws MessagingException
    {
        Message msg = prepareMessage( strHost, strRecipient, strSenderName, strSenderEmail, strSubject );

        msg.setHeader( HEADER_NAME, HEADER_VALUE );
        // Message body formated in HTML
        msg.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_HTML ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );

        // Send the message
        Transport.send( msg );
    }

    /**
     * Send a HTML formated message.
     *
     * @param strHost The SMTP name or IP address.
     * @param strRecipient The recipient email.
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param strFileName The name of the attachment.
     * @throws MessagingException The messaging exception
     */
    public static void sendMessageHtmlWithAttachment( String strHost, String strRecipient, String strSenderName,
        String strSenderEmail, String strSubject, String strMessage, String strFileName )
        throws MessagingException
    {
        Message msg = prepareMessage( strHost, strRecipient, strSenderName, strSenderEmail, strSubject );
        msg.setHeader( HEADER_NAME, HEADER_VALUE );

        // Message body formated in HTML
        msg.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_HTML ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );
        msg.setFileName( strFileName );

        // Send the message
        Transport.send( msg );
    }

    /**
     * Send a message.
     * FIXME: use prepareMessage method
     * @param strHost The SMTP name or IP address.
     * @param strRecipient The recipient email.
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     * @param mapAttachments The map containing the attachments associated with their content-location.
     * @throws MessagingException The messaging exception
     */
    public static void sendMessageHtml( String strHost, String strRecipient, String strSenderName,
        String strSenderEmail, String strSubject, String strMessage, Map mapAttachments )
        throws MessagingException
    {
        Message msg = prepareMessage( strHost, strRecipient, strSenderName, strSenderEmail, strSubject );
        msg.setHeader( HEADER_NAME, HEADER_VALUE );

        // Creation of the root part containing all the elements of the message 
        MimeMultipart multipart = new MimeMultipart( MULTIPART_RELATED );

        // Creation of the html part, the "core" of the message
        BodyPart msgBodyPart = new MimeBodyPart(  );
        msgBodyPart.setContent( strMessage, BODY_PART_MIME_TYPE );

        multipart.addBodyPart( msgBodyPart );

        if ( mapAttachments != null )
        {
            // Add the attachments
            Iterator attachementList = mapAttachments.keySet(  ).iterator(  );

            while ( attachementList.hasNext(  ) )
            {
                msgBodyPart = new MimeBodyPart(  );

                String strContentLocation = (String) attachementList.next(  );
                DataHandler handler = new DataHandler( (URL) mapAttachments.get( strContentLocation ) );

                try
                {
                    handler.getContent(  );
                }
                catch ( IOException e )
                {
                    // Document is ignored
                    AppLogService.info( strContentLocation + MSG_ATTACHMENT_NOT_FOUND );

                    continue;
                }

                // Fill this part, then add it to the root part with the good headers
                msgBodyPart.setDataHandler( handler );
                msgBodyPart.setHeader( HEADER_CONTENT_LOCATION, strContentLocation );
                multipart.addBodyPart( msgBodyPart );
            }
        }

        msg.setContent( multipart );

        // Send the message
        Transport.send( msg );
    }

    /**
     * Extract a collection of elements to be attached to a mail from an HTML string.
     *
     * The collection contains the DataHandler for each url associated with
     * an HTML tag img, script or link. Those urls must start with the url strBaseUrl.
     *
     * @param strHtml The HTML code.
     * @param strBaseUrl The base url, can be null in order to extract all urls.
     * @param useAbsoluteUrl Determine if we use absolute or relative url for attachement content-location
     * @return a collection of DataHandler associated with attachment urls.
     */
    public static Map<String, URL> getAttachmentList( String strHtml, String strBaseUrl, boolean useAbsoluteUrl )
    {
        Map<String, URL> map = new HashMap<String, URL>(  );
        HtmlDocument doc = new HtmlDocument( strHtml, strBaseUrl, useAbsoluteUrl );
        map.putAll( doc.getAllUrls( HtmlDocument.ELEMENT_IMG ) );
        map.putAll( doc.getAllUrls( HtmlDocument.ELEMENT_CSS ) );
        map.putAll( doc.getAllUrls( HtmlDocument.ELEMENT_JAVASCRIPT ) );

        return map;
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
     * @param strRecipient The recipient email.
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @return the message object initialised with the common settings
     * @throws MessagingException The messaging exception
     */
    private static Message prepareMessage( String strHost, String strRecipient, String strSenderName,
        String strSenderEmail, String strSubject ) throws MessagingException
    {
        boolean sessionDebug = false;

        // Initializes a mail session with the SMTP server
        Properties props = System.getProperties(  );
        props.put( MAIL_HOST, strHost );
        props.put( MAIL_TRANSPORT_PROTOCOL, SMTP );

        Session mailSession = Session.getDefaultInstance( props, null );

        // Activate debugging
        mailSession.setDebug( sessionDebug );

        // Instanciate and initialize a mime message
        Message msg = new MimeMessage( mailSession );
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
        StringTokenizer st = new StringTokenizer( strRecipient,
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

        msg.setRecipients( Message.RecipientType.TO, address );

        return msg;
    }
}
