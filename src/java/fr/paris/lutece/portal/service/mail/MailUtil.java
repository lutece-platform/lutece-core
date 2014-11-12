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

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.mail.ByteArrayDataSource;
import fr.paris.lutece.util.mail.FileAttachment;
import fr.paris.lutece.util.mail.HtmlDocument;
import fr.paris.lutece.util.mail.UrlAttachment;

import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.CommandInfo;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.activation.MimetypesFileTypeMap;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
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
    // Properties
    private static final String PROPERTY_CHARSET = "mail.charset";
    private static final String PROPERTY_MAIL_LIST_SEPARATOR = "mail.list.separator";
    private static final String PROPERTY_MAIL_TYPE_HTML = "mail.type.html";
    private static final String PROPERTY_MAIL_TYPE_PLAIN = "mail.type.plain";
    private static final String PROPERTY_MAIL_TYPE_CALENDAR = "mail.type.calendar";
    private static final String PROPERTY_MAIL_SESSION_DEBUG = "mail.session.debug";
    private static final String PROPERTY_CALENDAR_SEPARATOR = "mail.type.calendar.separator";
    private static final String PROPERTY_CALENDAR_METHOD_CREATE = "mail.type.calendar.create";
    private static final String PROPERTY_CALENDAR_METHOD_CANCEL = "mail.type.calendar.cancel";

    // Javax.mail properties
    private static final String SMTP = "smtp";
    private static final String MAIL_HOST = "mail.host";
    private static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_PROPTOCOL_HOST = "mail." + SMTP + ".host";
    private static final String MAIL_PROPTOCOL_PORT = "mail." + SMTP + ".port";

    // Constants
    private static final String TRUE = "true";
    private static final String ENCODING = "Q";
    private static final String HEADER_NAME = "Content-Transfer-Encoding";
    private static final String HEADER_VALUE = "quoted-printable";
    private static final String HEADER_CONTENT_LOCATION = "Content-Location";
    private static final String CONTENT_HANDLER = "content-handler";
    private static final String MULTIPART_RELATED = "related";
    private static final String MSG_ATTACHMENT_NOT_FOUND = " not found, document ignored.";
    private static final int CONSTANTE_FILE_ATTACHMET_BUFFER = 4096;
    private static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
    private static final String MIME_TYPE_TEXT_CALENDAR = "text/calendar";
    private static final String CONSTANT_REGISTER_MIME_TYPE_HANDLER = ";; x-java-content-handler=";
    private static final String DEFAULT_PLAIN_TEXT_HANDLER = "com.sun.mail.handlers.text_plain";
    private static final String CONSTANT_DISPOSITION_ATTACHMENT = "attachment";
    private static final String CONSTANT_BASE64 = "base64";

    static
    {
        // We create the mime text/calendar mime type
        MimetypesFileTypeMap mimetypes = (MimetypesFileTypeMap) MimetypesFileTypeMap.getDefaultFileTypeMap(  );
        mimetypes.addMimeTypes( MIME_TYPE_TEXT_CALENDAR );

        // We register the handler for the text/calendar mime type
        MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap(  );

        // We try to get the default handler for plain text
        CommandInfo[] commandInfos = mailcap.getAllCommands( MIME_TYPE_TEXT_PLAIN );
        CommandInfo commandInfoText = null;

        if ( ( commandInfos != null ) && ( commandInfos.length > 0 ) )
        {
            for ( CommandInfo commandInfo : commandInfos )
            {
                if ( StringUtils.equals( commandInfo.getCommandName(  ), CONTENT_HANDLER ) )
                {
                    commandInfoText = commandInfo;

                    break;
                }
            }

            if ( commandInfoText == null )
            {
                commandInfoText = commandInfos[0];
            }
        }

        // If the default handler for plain text was not found, we just use the default one
        String strHandler = ( commandInfoText != null ) ? commandInfoText.getCommandClass(  ) : DEFAULT_PLAIN_TEXT_HANDLER;
        mailcap.addMailcap( MIME_TYPE_TEXT_CALENDAR + CONSTANT_REGISTER_MIME_TYPE_HANDLER + strHandler + "\n" );
    }

    /**
     * Creates a new MailUtil object
     */
    private MailUtil(  )
    {
    }

    /**
     * Send a text message.
     *
     * @param strRecipientsTo
     *            The list of the main recipients email.Every recipient must be
     *            separated by the mail separator defined in config.properties
     * @param strRecipientsCc
     *            The recipients list of the carbon copies .
     * @param strRecipientsBcc
     *            The recipients list of the blind carbon copies .
     * @param strSenderName
     *            The sender name.
     * @param strSenderEmail
     *            The sender email address.
     * @param strSubject
     *            The message subject.
     * @param strMessage
     *            The message.
     * @param transport
     *            the smtp transport object
     * @param session
     *            the smtp session object
     * @throws AddressException
     *             If invalid address
     * @throws SendFailedException
     *             If an error occured during sending
     * @throws MessagingException
     *             If a messaging error occured
     */
    protected static void sendMessageText( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage, Transport transport,
        Session session ) throws MessagingException, AddressException, SendFailedException
    {
        Message msg = prepareMessage( strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName,
                strSenderEmail, strSubject, session );
        msg.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_PLAIN ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );

        sendMessage( msg, transport );
    }

    /**
     * Send a HTML formated message.
     * @param strRecipientsTo
     *            The list of the main recipients email.Every recipient must be
     *            separated by the mail separator defined in config.properties
     * @param strRecipientsCc
     *            The recipients list of the carbon copies .
     * @param strRecipientsBcc
     *            The recipients list of the blind carbon copies .
     * @param strSenderName
     *            The sender name.
     * @param strSenderEmail
     *            The sender email address.
     * @param strSubject
     *            The message subject.
     * @param strMessage
     *            The message.
     * @param transport
     *            the smtp transport object
     * @param session
     *            the smtp session object
     * @throws AddressException
     *             If invalid address
     * @throws SendFailedException
     *             If an error occured during sending
     * @throws MessagingException
     *             If a messaging error occured
     */
    protected static void sendMessageHtml( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage, Transport transport,
        Session session ) throws MessagingException, AddressException, SendFailedException
    {
        Message msg = prepareMessage( strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName,
                strSenderEmail, strSubject, session );

        msg.setHeader( HEADER_NAME, HEADER_VALUE );
        // Message body formated in HTML
        msg.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_HTML ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );

        sendMessage( msg, transport );
    }

    /**
     * Send a Multipart HTML message with the attachements associated to the
     * message and attached files. FIXME: use prepareMessage method
     *
     * @param strRecipientsTo
     *            The list of the main recipients email.Every recipient must be
     *            separated by the mail separator defined in config.properties
     * @param strRecipientsCc
     *            The recipients list of the carbon copies .
     * @param strRecipientsBcc
     *            The recipients list of the blind carbon copies .
     * @param strSenderName
     *            The sender name.
     * @param strSenderEmail
     *            The sender email address.
     * @param strSubject
     *            The message subject.
     * @param strMessage
     *            The message.
     * @param urlAttachements
     *            The List of UrlAttachement Object, containing the URL of
     *            attachments associated with their content-location.
     * @param fileAttachements
     *            The list of files attached
     * @param transport
     *            the smtp transport object
     * @param session
     *            the smtp session object
     * @throws AddressException
     *             If invalid address
     * @throws SendFailedException
     *             If an error occured during sending
     * @throws MessagingException
     *             If a messaging error occurred
     */
    protected static void sendMultipartMessageHtml( String strRecipientsTo, String strRecipientsCc,
        String strRecipientsBcc, String strSenderName, String strSenderEmail, String strSubject, String strMessage,
        List<UrlAttachment> urlAttachements, List<FileAttachment> fileAttachements, Transport transport, Session session )
        throws MessagingException, AddressException, SendFailedException
    {
        Message msg = prepareMessage( strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName,
                strSenderEmail, strSubject, session );
        msg.setHeader( HEADER_NAME, HEADER_VALUE );

        // Creation of the root part containing all the elements of the message
        MimeMultipart multipart = ( ( fileAttachements == null ) || ( fileAttachements.isEmpty(  ) ) )
            ? new MimeMultipart( MULTIPART_RELATED ) : new MimeMultipart(  );

        // Creation of the html part, the "core" of the message
        BodyPart msgBodyPart = new MimeBodyPart(  );
        // msgBodyPart.setContent( strMessage, BODY_PART_MIME_TYPE );
        msgBodyPart.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_HTML ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );
        multipart.addBodyPart( msgBodyPart );

        if ( urlAttachements != null )
        {
            ByteArrayDataSource urlByteArrayDataSource;

            for ( UrlAttachment urlAttachement : urlAttachements )
            {
                urlByteArrayDataSource = convertUrlAttachmentDataSourceToByteArrayDataSource( urlAttachement );

                if ( urlByteArrayDataSource != null )
                {
                    msgBodyPart = new MimeBodyPart(  );
                    // Fill this part, then add it to the root part with the
                    // good headers
                    msgBodyPart.setDataHandler( new DataHandler( urlByteArrayDataSource ) );
                    msgBodyPart.setHeader( HEADER_CONTENT_LOCATION, urlAttachement.getContentLocation(  ) );
                    multipart.addBodyPart( msgBodyPart );
                }
            }
        }

        // add File Attachement
        if ( fileAttachements != null )
        {
            for ( FileAttachment fileAttachement : fileAttachements )
            {
                String strFileName = fileAttachement.getFileName(  );
                byte[] bContentFile = fileAttachement.getData(  );
                String strContentType = fileAttachement.getType(  );
                ByteArrayDataSource dataSource = new ByteArrayDataSource( bContentFile, strContentType );
                msgBodyPart = new MimeBodyPart(  );
                msgBodyPart.setDataHandler( new DataHandler( dataSource ) );
                msgBodyPart.setFileName( strFileName );
                msgBodyPart.setDisposition( CONSTANT_DISPOSITION_ATTACHMENT );
                multipart.addBodyPart( msgBodyPart );
            }
        }

        msg.setContent( multipart );

        sendMessage( msg, transport );
    }

    /**
     * Send a Multipart text message with attached files. FIXME: use
     * prepareMessage method
     *
     * @param strRecipientsTo
     *            The list of the main recipients email.Every recipient must be
     *            separated by the mail separator defined in config.properties
     * @param strRecipientsCc
     *            The recipients list of the carbon copies .
     * @param strRecipientsBcc
     *            The recipients list of the blind carbon copies .
     * @param strSenderName
     *            The sender name.
     * @param strSenderEmail
     *            The sender email address.
     * @param strSubject
     *            The message subject.
     * @param strMessage
     *            The message.
     * @param fileAttachements
     *            The list of attached files
     * @param transport
     *            the smtp transport object
     * @param session
     *            the smtp session object
     * @throws AddressException
     *             If invalid address
     * @throws SendFailedException
     *             If an error occured during sending
     * @throws MessagingException
     *             If a messaging error occured
     */
    protected static void sendMultipartMessageText( String strRecipientsTo, String strRecipientsCc,
        String strRecipientsBcc, String strSenderName, String strSenderEmail, String strSubject, String strMessage,
        List<FileAttachment> fileAttachements, Transport transport, Session session )
        throws MessagingException, AddressException, SendFailedException
    {
        Message msg = prepareMessage( strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName,
                strSenderEmail, strSubject, session );
        msg.setHeader( HEADER_NAME, HEADER_VALUE );

        // Creation of the root part containing all the elements of the message
        MimeMultipart multipart = new MimeMultipart(  );

        // Creation of the html part, the "core" of the message
        BodyPart msgBodyPart = new MimeBodyPart(  );
        // msgBodyPart.setContent( strMessage, BODY_PART_MIME_TYPE );
        msgBodyPart.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_PLAIN ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );
        multipart.addBodyPart( msgBodyPart );

        // add File Attachement
        if ( fileAttachements != null )
        {
            for ( FileAttachment fileAttachement : fileAttachements )
            {
                String strFileName = fileAttachement.getFileName(  );
                byte[] bContentFile = fileAttachement.getData(  );
                String strContentType = fileAttachement.getType(  );
                ByteArrayDataSource dataSource = new ByteArrayDataSource( bContentFile, strContentType );
                msgBodyPart = new MimeBodyPart(  );
                msgBodyPart.setDataHandler( new DataHandler( dataSource ) );
                msgBodyPart.setFileName( strFileName );
                msgBodyPart.setDisposition( CONSTANT_DISPOSITION_ATTACHMENT );
                multipart.addBodyPart( msgBodyPart );
            }
        }

        msg.setContent( multipart );

        sendMessage( msg, transport );
    }

    /**
     * Send a calendar message.
     * @param strRecipientsTo The list of the main recipients email. Every
     *            recipient must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The HTML message.
     * @param strCalendarMessage The calendar message.
     * @param bCreateEvent True to create the event, false to remove it
     * @param transport the smtp transport object
     * @param session the smtp session object
     * @throws AddressException If invalid address
     * @throws SendFailedException If an error occurred during sending
     * @throws MessagingException If a messaging error occurred
     */
    protected static void sendMessageCalendar( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, String strMessage, String strCalendarMessage,
        boolean bCreateEvent, Transport transport, Session session )
        throws MessagingException, AddressException, SendFailedException
    {
        Message msg = prepareMessage( strRecipientsTo, strRecipientsCc, strRecipientsBcc, strSenderName,
                strSenderEmail, strSubject, session );
        msg.setHeader( HEADER_NAME, HEADER_VALUE );

        MimeMultipart multipart = new MimeMultipart(  );
        BodyPart msgBodyPart = new MimeBodyPart(  );
        msgBodyPart.setDataHandler( new DataHandler( 
                new ByteArrayDataSource( strMessage,
                    AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_HTML ) +
                    AppPropertiesService.getProperty( PROPERTY_CHARSET ) ) ) );

        multipart.addBodyPart( msgBodyPart );

        BodyPart calendarBodyPart = new MimeBodyPart(  );
        //        calendarBodyPart.addHeader( "Content-Class", "urn:content-classes:calendarmessage" );
        calendarBodyPart.setContent( strCalendarMessage,
            AppPropertiesService.getProperty( PROPERTY_MAIL_TYPE_CALENDAR ) +
            AppPropertiesService.getProperty( PROPERTY_CHARSET ) +
            AppPropertiesService.getProperty( PROPERTY_CALENDAR_SEPARATOR ) +
            AppPropertiesService.getProperty( bCreateEvent ? PROPERTY_CALENDAR_METHOD_CREATE
                                                           : PROPERTY_CALENDAR_METHOD_CANCEL ) );
        calendarBodyPart.addHeader( HEADER_NAME, CONSTANT_BASE64 );
        multipart.addBodyPart( calendarBodyPart );

        msg.setContent( multipart );

        sendMessage( msg, transport );
    }

    /**
     * Send the message
     *
     * @param msg
     *            the message to send
     * @param transport
     *            the transport used to send the message
     * @throws MessagingException
     *             If a messaging error occured
     * @throws AddressException
     *             If invalid address
     */
    private static void sendMessage( Message msg, Transport transport )
        throws MessagingException, AddressException
    {
        if ( msg.getAllRecipients(  ) != null )
        {
            // Send the message
            transport.sendMessage( msg, msg.getAllRecipients(  ) );
        }
        else
        {
            throw new AddressException( "Mail adress is null" );
        }
    }

    /**
     * Extract a collection of elements to be attached to a mail from an HTML
     * string.
     * The collection contains the Url used for created DataHandler for each url
     * associated with an HTML tag img, script or link. Those urls must start
     * with the url strBaseUrl.
     * @param strHtml The HTML code.
     * @param strBaseUrl The base url, can be null in order to extract all urls.
     * @param useAbsoluteUrl Determine if we use absolute or relative url for
     *            attachement content-location
     * @return a collection of UrlAttachment Object for created DataHandler
     *         associated with attachment urls.
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
     * <li>initializes a mail session with the SMTP server</li>
     * <li>activates debugging</li>
     * <li>instantiates and initializes a mime message</li>
     * <li>sets the sent date, the from field, the subject field</li>
     * <li>sets the recipients</li>
     * </ul>
     *
     *
     * @return the message object initialized with the common settings
     * @param strRecipientsTo The list of the main recipients email.Every
     *            recipient must be separated by the mail separator defined in
     *            config.properties
     * @param strRecipientsCc The recipients list of the carbon copies .
     * @param strRecipientsBcc The recipients list of the blind carbon copies .
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param session The SMTP session object
     * @throws AddressException If invalid address
     * @throws MessagingException If a messaging error occurred
     */
    protected static Message prepareMessage( String strRecipientsTo, String strRecipientsCc, String strRecipientsBcc,
        String strSenderName, String strSenderEmail, String strSubject, Session session )
        throws MessagingException, AddressException
    {
        // Instantiate and initialize a mime message
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

        // Instantiation of the list of address
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
     * Open mail session with the SMTP server using the given credentials. Will
     * use no authentication if strUsername is null or blank.
     *
     * @param strHost The SMTP name or IP address.
     * @param nPort The port to use
     * @param strUsername the username
     * @param strPassword the password
     * @return the mails session object
     */
    protected static Session getMailSession( String strHost, int nPort, final String strUsername,
        final String strPassword )
    {
        String strDebug = AppPropertiesService.getProperty( PROPERTY_MAIL_SESSION_DEBUG, Boolean.FALSE.toString(  ) );
        boolean bSessionDebug = Boolean.parseBoolean( strDebug );

        // Initializes a mail session with the SMTP server
        Properties props = System.getProperties(  );
        props.put( MAIL_HOST, strHost );
        props.put( MAIL_TRANSPORT_PROTOCOL, SMTP );
        props.put( MAIL_PROPTOCOL_HOST, strHost );
        props.put( MAIL_PROPTOCOL_PORT, nPort );

        Authenticator auth;

        if ( StringUtils.isNotBlank( strUsername ) )
        {
            props.put( MAIL_SMTP_AUTH, TRUE );
            // using authenticator class that return a PasswordAuthentication
            auth = new Authenticator(  )
                    {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication(  )
                        {
                            return new PasswordAuthentication( strUsername, strPassword );
                        }
                    };
        }
        else
        {
            // no authentication data provided, no authenticator
            auth = null;
        }

        Session mailSession = Session.getDefaultInstance( props, auth );
        // Activate debugging
        mailSession.setDebug( bSessionDebug );

        return mailSession;
    }

    /**
     * return the transport object of the SMTP session
     *
     * @return the transport object of the SMTP session
     * @param session the SMTP session
     * @throws NoSuchProviderException If the provider is not found
     */
    protected static Transport getTransport( Session session )
        throws NoSuchProviderException
    {
        return session.getTransport( SMTP );
    }

    /**
     * extract The list of Internet Address content in the string strRecipients
     *
     * @return The list of Internet Address content in the string strRecipients
     * @param strRecipients
     *            The list of recipient separated by the mail separator defined
     *            in config.properties
     * @throws AddressException
     *             If invalid address
     */
    private static InternetAddress[] getAllAdressOfRecipients( String strRecipients )
        throws AddressException
    {
        List<String> listRecipients = getAllStringAdressOfRecipients( strRecipients );
        InternetAddress[] address = new InternetAddress[listRecipients.size(  )];

        // Initialization of the address array
        for ( int i = 0; i < listRecipients.size(  ); i++ )
        {
            address[i] = new InternetAddress( listRecipients.get( i ) );
        }

        return address;
    }

    /**
     * Extract The list of String Address content in the string strRecipients
     * @return The list of String Address content in the string strRecipients
     * @param strRecipients The list of recipient separated by the mail
     *            separator defined in config.properties
     *
     */
    public static List<String> getAllStringAdressOfRecipients( String strRecipients )
    {
        StringTokenizer st = new StringTokenizer( strRecipients,
                AppPropertiesService.getProperty( PROPERTY_MAIL_LIST_SEPARATOR, ";" ) );
        List<String> listRecipients = new ArrayList<String>(  );

        while ( st.hasMoreTokens(  ) )
        {
            listRecipients.add( st.nextToken(  ) );
        }

        return listRecipients;
    }

    /**
     * Return a String that contains a list of recipients separated with mail
     * separator
     * @param listRecipients a list of string recipients
     * @return A String that contains a list of recipients separated with mail
     *         separator
     */
    protected static String getStrRecipients( List<String> listRecipients )
    {
        String strMailListSeparator = AppPropertiesService.getProperty( PROPERTY_MAIL_LIST_SEPARATOR, ";" );
        StringBuilder strRecipients = new StringBuilder(  );
        int ncpt = 0;

        if ( listRecipients != null )
        {
            for ( String strRecipient : listRecipients )
            {
                strRecipients.append( strRecipient );

                if ( ++ncpt < listRecipients.size(  ) )
                {
                    strRecipients.append( strMailListSeparator );
                }
            }
        }

        return strRecipients.toString(  );
    }

    /**
     * This Method convert a UrlAttachmentDataSource to a ByteArrayDataSource
     * and used MailAttachmentCacheService for caching resource.
     * @param urlAttachement {@link UrlAttachment}
     * @return a {@link ByteArrayDataSource}
     */
    private static ByteArrayDataSource convertUrlAttachmentDataSourceToByteArrayDataSource( 
        UrlAttachment urlAttachement )
    {
        String strKey = MailAttachmentCacheService.getInstance(  ).getKey( urlAttachement.getUrlData(  ).toString(  ) );
        ByteArrayDataSource urlAttachmentDataSource = null;

        if ( !MailAttachmentCacheService.getInstance(  ).isCacheEnable(  ) ||
                ( MailAttachmentCacheService.getInstance(  ).getFromCache( strKey ) == null ) )
        {
            DataHandler handler = new DataHandler( urlAttachement.getUrlData(  ) );
            ByteArrayOutputStream bo = null;
            InputStream input = null;
            String strType = null;

            try
            {
                Object o = handler.getContent(  );
                strType = handler.getContentType(  );

                if ( o != null )
                {
                    if ( o instanceof InputStream )
                    {
                        input = (InputStream) o;
                        bo = new ByteArrayOutputStream(  );

                        int read;
                        byte[] tab = new byte[CONSTANTE_FILE_ATTACHMET_BUFFER];

                        do
                        {
                            read = input.read( tab );

                            if ( read > 0 )
                            {
                                bo.write( tab, 0, read );
                            }
                        }
                        while ( read > 0 );
                    }
                }
            }
            catch ( IOException e )
            {
                // Document is ignored
                AppLogService.info( urlAttachement.getContentLocation(  ) + MSG_ATTACHMENT_NOT_FOUND );
            }
            finally
            {
                //closed inputstream and outputstream 
                try
                {
                    if ( input != null )
                    {
                        input.close(  );
                    }

                    if ( bo != null )
                    {
                        bo.close(  );
                        urlAttachmentDataSource = new ByteArrayDataSource( bo.toByteArray(  ), strType );
                    }
                }
                catch ( IOException e )
                {
                    AppLogService.error( e );
                }
            }

            if ( MailAttachmentCacheService.getInstance(  ).isCacheEnable(  ) )
            {
                //add resource in cache
                MailAttachmentCacheService.getInstance(  ).putInCache( strKey, urlAttachmentDataSource );
            }
        }
        else
        {
            //used the resource store in cache
            urlAttachmentDataSource = (ByteArrayDataSource) MailAttachmentCacheService.getInstance(  )
                                                                                      .getFromCache( strKey );
        }

        return urlAttachmentDataSource;
    }
}
