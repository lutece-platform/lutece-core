/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;

/**
 * MailSender Daemon
 */
public class MailSenderDaemon extends Daemon
{
    protected static final String DAEMON_ID = "mailSender";

    private static final String PROPERTY_MAIL_HOST = "mail.server";
    private static final String PROPERTY_MAIL_PORT = "mail.server.port";
    private static final String PROPERTY_MAIL_DEAMON_WAITTIME = "mail.daemon.waittime";
    private static final String PROPERTY_MAIL_DEAMON_COUNT = "mail.daemon.count";
    private static final String PROPERTY_MAIL_USERNAME = "mail.username";
    private static final String PROPERTY_MAIL_PASSWORD = "mail.password";
    private static final String PROPERTY_MAIL_DAEMON_RETRYONERROR_WAITTIME = "mail.daemon.retryonerror.waittime";
    private static final String PROPERTY_MAIL_DAEMON_RETRYONERROR_WAITTIME_UNIT = "mail.daemon.retryonerror.waittime.unit";
    private static final int DEFAULT_SMTP_PORT = 25;

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void run( )
    {
        Logger logger = Logger.getLogger( "lutece.mail" );
        logger.setAdditivity( false );

        String strHost = AppPropertiesService.getProperty( PROPERTY_MAIL_HOST );
        String strUsername = AppPropertiesService.getProperty( PROPERTY_MAIL_USERNAME, null );
        String strPassword = AppPropertiesService.getProperty( PROPERTY_MAIL_PASSWORD, null );
        int nStmpPort = AppPropertiesService.getPropertyInt( PROPERTY_MAIL_PORT, DEFAULT_SMTP_PORT );

        // Initializes a mail session with the SMTP server
        StringBuilder sbLogs = new StringBuilder( );
        IMailQueue queue = MailService.getQueue( );

        if ( queue.size( ) != 0 )
        {
            sbLogs.append( new Date( ).toString( ) );

            Session session = MailUtil.getMailSession( strHost, nStmpPort, strUsername, strPassword );
            Transport transportSmtp = null;

            try
            {
                transportSmtp = MailUtil.getTransport( session );
            }
            catch( NoSuchProviderException e )
            {
                AppLogService.error( e );
            }

            if ( transportSmtp != null )
            {
                try
                {
                    transportSmtp.connect( strHost, nStmpPort, strUsername, strPassword );
                    
                    sendMails( transportSmtp, session, queue, logger, sbLogs );

                    transportSmtp.close( );
                }
                catch( MessagingException e )
                {
                    sbLogs.append( "MailService - Error sending mail (MessagingException): " );
                    sbLogs.append( e.getMessage( ) );
                    AppLogService.error( "MailService - Error sending mail (MessagingException): " + e.getMessage( ), e );
                }
                catch( Exception e )
                {
                    sbLogs.append( "MailService - Error sending mail : " );
                    sbLogs.append( e.getMessage( ) );
                    AppLogService.error( "MailService - Error sending mail : " + e.getMessage( ), e );
                }
            }

            // reset all resource stored in MailAttachmentCacheService
            MailAttachmentCacheService.getInstance( ).resetCache( );
            setLastRunLogs( sbLogs.toString( ) );
        }
        else
        {
            sbLogs.append( "\r\nNo mail to send " );
            sbLogs.append( new Date( ).toString( ) );
            logger.debug( sbLogs.toString( ) );
        }
    }
    
    private void sendMails( Transport transportSmtp, Session session, IMailQueue queue, Logger logger,
            StringBuilder sbLogs ) throws MessagingException
    {
        int nWaitTime = AppPropertiesService.getPropertyInt( PROPERTY_MAIL_DEAMON_WAITTIME, 1 );
        int nCount = AppPropertiesService.getPropertyInt( PROPERTY_MAIL_DEAMON_COUNT, 1000 );
        long nRetryWaitTime = AppPropertiesService.getPropertyLong( PROPERTY_MAIL_DAEMON_RETRYONERROR_WAITTIME, 60L );
        TimeUnit retryWaitTimeUnit = TimeUnit.valueOf(
                AppPropertiesService.getProperty( PROPERTY_MAIL_DAEMON_RETRYONERROR_WAITTIME_UNIT, "SECONDS" ) );

        MailItem mail = queue.consume( );
        int count = 0;

        while ( mail != null )
        {
            try
            {
                if ( mail.isUniqueRecipientTo( ) )
                {
                    List<String> listAdressTo = MailUtil.getAllStringAdressOfRecipients( mail.getRecipientsTo( ) );

                    for ( String strAdressTo : listAdressTo )
                    {
                        StringBuilder sbLogsLine = new StringBuilder( );
                        // just one recipient by mail
                        mail.setRecipientsTo( strAdressTo );
                        sendMail( mail, transportSmtp, session, sbLogsLine );
                        logger.info( sbLogsLine.toString( ) );
                        sbLogs.append( "\r\n" );
                        sbLogs.append( sbLogsLine );
                    }
                }
                else
                {
                    StringBuilder sbLogsLine = new StringBuilder( );
                    sendMail( mail, transportSmtp, session, sbLogsLine );
                    logger.info( sbLogsLine.toString( ) );
                    sbLogs.append( "\r\n" );
                    sbLogs.append( sbLogsLine );
                }
            }
            catch ( MessagingException e )
            {
                // if the connection is dead or not in the connected state
                // we put the mail in the queue before end process
                queue.send( mail );
                AppLogService.error( "Error while sending a message. Will schedule a retry", e );
                AppDaemonService.signalDaemon( DAEMON_ID, nRetryWaitTime, retryWaitTimeUnit );
                break;
            }
            // Tempo
            count++;

            if ( ( count % nCount ) == 0 )
            {
                transportSmtp.close( );
                AppDaemonService.signalDaemon( DAEMON_ID, nWaitTime, TimeUnit.MILLISECONDS );
                break;
            }
        }
    }

    /**
     * send mail
     * 
     * @param mail
     *            the mail item
     * @param transportSmtp
     *            the smtp transport
     * @param session
     *            the session smtp
     * @param sbLogsLine
     *            the log line
     * @throws MessagingException
     *             See {@link MessagingException}
     */
    private void sendMail( MailItem mail, Transport transportSmtp, Session session, StringBuilder sbLogsLine ) throws MessagingException
    {
        try
        {
            sbLogsLine.append( " - To " );
            sbLogsLine.append( ( ( mail.getRecipientsTo( ) != null ) ? mail.getRecipientsTo( ) : "" ) );
            sbLogsLine.append( " - Cc " );
            sbLogsLine.append( ( mail.getRecipientsCc( ) != null ) ? mail.getRecipientsCc( ) : "" );
            sbLogsLine.append( " - Bcc " );
            sbLogsLine.append( ( mail.getRecipientsBcc( ) != null ) ? mail.getRecipientsBcc( ) : "" );
            sbLogsLine.append( " - Subject : " );
            sbLogsLine.append( mail.getSubject( ) );

            switch( mail.getFormat( ) )
            {
                case MailItem.FORMAT_HTML:
                    MailUtil.sendMessageHtml( mail.getRecipientsTo( ), mail.getRecipientsCc( ), mail.getRecipientsBcc( ), mail.getSenderName( ),
                            mail.getSenderEmail( ), mail.getSubject( ), mail.getMessage( ), transportSmtp, session );

                    break;

                case MailItem.FORMAT_TEXT:
                    MailUtil.sendMessageText( mail.getRecipientsTo( ), mail.getRecipientsCc( ), mail.getRecipientsBcc( ), mail.getSenderName( ),
                            mail.getSenderEmail( ), mail.getSubject( ), mail.getMessage( ), transportSmtp, session );

                    break;

                case MailItem.FORMAT_MULTIPART_HTML:
                    MailUtil.sendMultipartMessageHtml( mail.getRecipientsTo( ), mail.getRecipientsCc( ), mail.getRecipientsBcc( ), mail.getSenderName( ),
                            mail.getSenderEmail( ), mail.getSubject( ), mail.getMessage( ), mail.getUrlsAttachement( ), mail.getFilesAttachement( ),
                            transportSmtp, session );

                    break;

                case MailItem.FORMAT_MULTIPART_TEXT:
                    MailUtil.sendMultipartMessageText( mail.getRecipientsTo( ), mail.getRecipientsCc( ), mail.getRecipientsBcc( ), mail.getSenderName( ),
                            mail.getSenderEmail( ), mail.getSubject( ), mail.getMessage( ), mail.getFilesAttachement( ), transportSmtp, session );

                    break;

                case MailItem.FORMAT_CALENDAR:
                    MailUtil.sendMessageCalendar( mail.getRecipientsTo( ), mail.getRecipientsCc( ), mail.getRecipientsBcc( ), mail.getSenderName( ),
                            mail.getSenderEmail( ), mail.getSubject( ), mail.getMessage( ), mail.getCalendarMessage( ), mail.getCreateEvent( ), transportSmtp,
                            session );

                    break;

                default:
                    break;
            }

            sbLogsLine.append( " - Status [ OK ]" );
        }
        catch( SendFailedException | AddressException e )
        {
            // a wrongly formatted address is encountered in the list of recipients
            sbLogsLine.append( " - Status [ Failed ] : " );
            sbLogsLine.append( e.getMessage( ) );
            AppLogService.error( "MailService - Error sending mail : " + e.getMessage( ), e );
        }
        catch( MessagingException e )
        {
            // if the connection is dead or not in the connected state
            // we put the mail in the queue before end process
            sbLogsLine.append( " - Status [ Failed ] : " );
            sbLogsLine.append( e.getMessage( ) );
            AppLogService.error( "MailService - Error sending mail : " + e.getMessage( ), e );
            throw e;
        }
    }
}
