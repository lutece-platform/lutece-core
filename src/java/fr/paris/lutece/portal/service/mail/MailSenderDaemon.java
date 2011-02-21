/*
 * Copyright (c) 2002-2010, Mairie de Paris
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

import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.util.Date;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;

/**
 * MailSender Daemon
 */
public class MailSenderDaemon extends Daemon
{

    private static final String PROPERTY_MAIL_HOST = "mail.server";
    private static final String PROPERTY_MAIL_DEAMON_WAITTIME = "mail.daemon.waittime";
    private static final String PROPERTY_MAIL_DEAMON_COUNT = "mail.daemon.count";
    private static final String PROPERTY_MAIL_USERNAME = "mail.username";
    private static final String PROPERTY_MAIL_PASSWORD = "mail.password";


    /**
     * Implements Runable interface
     */
    public synchronized void run()
    {
        String strHost = AppPropertiesService.getProperty( PROPERTY_MAIL_HOST );
        String strUsername = AppPropertiesService.getProperty( PROPERTY_MAIL_USERNAME, null );
        String strPassword = AppPropertiesService.getProperty( PROPERTY_MAIL_PASSWORD, null );
        int nWaitTime = AppPropertiesService.getPropertyInt( PROPERTY_MAIL_DEAMON_WAITTIME , 1 );
        int nCount = AppPropertiesService.getPropertyInt( PROPERTY_MAIL_DEAMON_COUNT , 1000 );

        // Initializes a mail session with the SMTP server
        StringBuilder sbLogs = new StringBuilder();
        IMailQueue queue = MailService.getQueue();

        if ( queue.size() != 0 )
        {
            sbLogs.append( new Date().toString() );

            Session session = MailUtil.getMailSession( strHost , strUsername, strPassword );
            Transport transportSmtp = null;

            try
            {
                transportSmtp = MailUtil.getTransport( session );
            }
            catch ( NoSuchProviderException e )
            {
                AppLogService.error( e );
            }

            if ( transportSmtp != null )
            {
                try
                {
                    transportSmtp.connect();

                    MailItem mail = queue.consume();
                    int count = 0;
                    while ( mail != null )
                    {
                        try
                        {
                            sbLogs.append( "\r\n - To " );
                            sbLogs.append( ( ( mail.getRecipientsTo() != null ) ? mail.getRecipientsTo() : "" ) );
                            sbLogs.append( " - Cc " );
                            sbLogs.append( ( mail.getRecipientsCc() != null ) ? mail.getRecipientsCc() : "" );
                            sbLogs.append( " - Bcc " );
                            sbLogs.append( ( mail.getRecipientsBcc() != null ) ? mail.getRecipientsBcc() : "" );
                            sbLogs.append( " - Subject : " );
                            sbLogs.append( mail.getSubject() );

                            switch ( mail.getFormat() )
                            {
                                case MailItem.FORMAT_HTML:
                                    MailUtil.sendMessageHtml( strHost, mail.getRecipientsTo(),
                                            mail.getRecipientsCc(), mail.getRecipientsBcc(), mail.getSenderName(),
                                            mail.getSenderEmail(), mail.getSubject(), mail.getMessage(),
                                            transportSmtp, session );

                                    break;

                                case MailItem.FORMAT_TEXT:
                                    MailUtil.sendMessageText( strHost, mail.getRecipientsTo(),
                                            mail.getRecipientsCc(), mail.getRecipientsBcc(), mail.getSenderName(),
                                            mail.getSenderEmail(), mail.getSubject(), mail.getMessage(),
                                            transportSmtp, session );

                                    break;

                                case MailItem.FORMAT_MULTIPART_HTML:
                                    MailUtil.sendMultipartMessageHtml( strHost, mail.getRecipientsTo(),
                                            mail.getRecipientsCc(), mail.getRecipientsBcc(), mail.getSenderName(),
                                            mail.getSenderEmail(), mail.getSubject(), mail.getMessage(),
                                            mail.getUrlsAttachement(), mail.getFilesAttachement(), transportSmtp,
                                            session );

                                    break;

                                case MailItem.FORMAT_MULTIPART_TEXT:
                                    MailUtil.sendMultipartMessageText( strHost, mail.getRecipientsTo(),
                                            mail.getRecipientsCc(), mail.getRecipientsBcc(), mail.getSenderName(),
                                            mail.getSenderEmail(), mail.getSubject(), mail.getMessage(),
                                            mail.getFilesAttachement(), transportSmtp, session );

                                    break;

                                default:
                                    break;
                            }

                            sbLogs.append( " - Status [ OK ]" );
                        }
                        catch ( AddressException e )
                        {
                            //a wrongly formatted address is encountered in the list of recipients
                            sbLogs.append( " - Status [ Failed ] : " );
                            sbLogs.append( e.getMessage() );
                            AppLogService.error( "MailService - Error sending mail : " + e.getMessage(), e );
                        }
                        catch ( SendFailedException e )
                        {
                            //the send failed because of invalid addresses.
                            sbLogs.append( " - Status [ Failed ] : " );
                            sbLogs.append( e.getMessage() );
                            AppLogService.error( "MailService - Error sending mail : " + e.getMessage(), e );
                        }
                        catch ( MessagingException e )
                        {
                            //if the connection is dead or not in the connected state
                            //we put the mail in the queue before end process
                            sbLogs.append( " - Status [ Failed ] : " );
                            sbLogs.append( e.getMessage() );
                            AppLogService.error( "MailService - Error sending mail : " + e.getMessage(), e );
                            queue.send( mail );

                            break;
                        }

                        mail = queue.consume();
                        
                        // Tempo
                        count++;
                        if( count % nCount ==  0)
                        {
                            transportSmtp.close();
                            wait( nWaitTime );
                            transportSmtp.connect();
                        }
                    }

                    transportSmtp.close();
                }
                catch ( MessagingException e )
                {
                    sbLogs.append( "MailService - Error sending mail (MessagingException): " );
                    sbLogs.append( e.getMessage() );
                    AppLogService.error( "MailService - Error sending mail (MessagingException): " + e.getMessage(), e );
                }
                catch ( Exception e )
                {
                    sbLogs.append( "MailService - Error sending mail : " );
                    sbLogs.append( e.getMessage() );
                    AppLogService.error( "MailService - Error sending mail : " + e.getMessage(), e );
                }
            }
        }
        else
        {
            sbLogs.append( "\r\nNo mail to send " );
            sbLogs.append( new Date().toString() );
        }

        Logger logger = Logger.getLogger( "lutece.mail" );
        logger.setAdditivity( false );
        logger.info( sbLogs.toString() );
        setLastRunLogs( sbLogs.toString() );
    }
}
