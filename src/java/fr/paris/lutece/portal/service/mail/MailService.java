/*
 * Copyright (c) 2002-2007, Mairie de Paris
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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * Application Mail Service
 */
public final class MailService
{
    private static final String PROPERTY_MAIL_NOREPLY_EMAIL = "mail.noreply.email";
    private static IMailQueue _queue = (IMailQueue) SpringContextService.getBean( "mailQueue" );
    
    /** Creates a new instance of AppMailService */
    private MailService(  )
    {
    }
    
    /**
     * Send a message asynchronously. The message is queued until a daemon
     * thread send all awaiting messages
     *
     * @param strRecipient The recipient email.
     * @param strSenderName The sender name.
     * @param strSenderEmail The sender email address.
     * @param strSubject The message subject.
     * @param strMessage The message.
     */
    public static void sendMail( String strRecipient, String strSenderName, String strSenderEmail, String strSubject,
            String strMessage )
    {
        MailItem item = new MailItem(  );
        item.setRecipient( strRecipient );
        item.setSenderName( strSenderName );
        item.setSenderEmail( strSenderEmail );
        item.setSubject( strSubject );
        item.setMessage( strMessage );
        _queue.send( item );
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
        return AppPropertiesService.getProperty( PROPERTY_MAIL_NOREPLY_EMAIL );
    }
    
    /**
     * Returns the mail queue
     * @return the mail queue
     */
    public static IMailQueue getQueue()
    {
        return _queue;
    }
}
