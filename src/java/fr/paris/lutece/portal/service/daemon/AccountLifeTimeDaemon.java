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
package fr.paris.lutece.portal.service.daemon;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameterHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.template.DatabaseTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Daemon to anonymize admin users
 */
public class AccountLifeTimeDaemon extends Daemon
{
    private static final String MESSAGE_DAEMON_NAME = "AccountLifeTimeDaemon - ";
    private static final String MESSAGE_NO_TEXT = "No next alert to send";
    private static final String MESSAGE_EXPIRED = "No expired passwords";
    private static final String MESSAGE_NO_NOTIF = "Expired passwords notification deactivated, skipping";
    private static final String MESSAGE_EXPIRED_USER = "No expired admin user found";
    private static final String PARAMETER_TIME_BEFORE_ALERT_ACCOUNT = "time_before_alert_account";
    private static final String PARAMETER_NB_ALERT_ACCOUNT = "nb_alert_account";
    private static final String PARAMETER_TIME_BETWEEN_ALERTS_ACCOUNT = "time_between_alerts_account";
    private static final String PARAMETER_NOTIFY_USER_PASSWORD_EXPIRED = "notify_user_password_expired";
    private static final String PARAMETER_EXPIRED_ALERT_MAIL_SENDER = "expired_alert_mail_sender";
    private static final String PARAMETER_EXPIRED_ALERT_MAIL_SUBJECT = "expired_alert_mail_subject";
    private static final String PARAMETER_FIRST_ALERT_MAIL_SENDER = "first_alert_mail_sender";
    private static final String PARAMETER_FIRST_ALERT_MAIL_SUBJECT = "first_alert_mail_subject";
    private static final String PARAMETER_OTHER_ALERT_MAIL_SENDER = "other_alert_mail_sender";
    private static final String PARAMETER_OTHER_ALERT_MAIL_SUBJECT = "other_alert_mail_subject";
    private static final String PARAMETER_PASSWORD_EXPIRED_MAIL_SENDER = "password_expired_mail_sender";
    private static final String PARAMETER_PASSWORD_EXPIRED_MAIL_SUBJECT = "password_expired_mail_subject";
    private static final String PARAMETER_CORE_EXPIRATION_MAIL = "core_expiration_mail";
    private static final String PARAMETER_CORE_FIRST_ALERT_MAIL = "core_first_alert_mail";
    private static final String PARAMETER_CORE_OTHER_ALERT_MAIL = "core_other_alert_mail";
    private static final String PARAMETER_CORE_PASSWORD_EXPIRED_ALERT_MAIL = "core_password_expired";
    private static final String MARK_LAST_NAME = "last_name";
    private static final String MARK_FIRST_NAME = "first_name";
    private static final String MARK_DATE_VALID = "date_valid";
    private static final String MARK_URL = "url";
    private static final String PROPERTY_PROD_URL = "init.webapp.prod.url";
    private static final String JSP_URL_REACTIVATE_ACCOUNT = "/jsp/admin/user/ReactivateAccount.jsp";

    /**
     * {@inheritDoc}
     */
    @Override
    public void run( )
    {
        StringBuilder sbResult = new StringBuilder( );
        Timestamp currentTimestamp = new Timestamp( new java.util.Date( ).getTime( ) );
       
        // We first set as expirated user that have reached their life time limit
        runSetExpiredUser( currentTimestamp, sbResult );

        // We send first alert to users
        runExpiringUserAlert( currentTimestamp, sbResult );

        // We send other alert to users
        runOtherUserAlert( currentTimestamp, sbResult );
        
        // We notify users with expired passwords
        runExpiredPassword( currentTimestamp, sbResult );

        setLastRunLogs( sbResult.toString( ) );
    }
    
    private void runSetExpiredUser( Timestamp currentTimestamp, StringBuilder sbResult )
    {
        List<Integer> accountsToSetAsExpired = AdminUserHome.getIdUsersWithExpiredLifeTimeList( currentTimestamp );
        if ( CollectionUtils.isNotEmpty( accountsToSetAsExpired ) )
        {
            int nbAccountToExpire = accountsToSetAsExpired.size( );
            String strBody = DatabaseTemplateService.getTemplateFromKey( PARAMETER_CORE_EXPIRATION_MAIL );

            String defaultUserParameter = DefaultUserParameterHome.findByKey( PARAMETER_EXPIRED_ALERT_MAIL_SENDER );
            String strSender = ( defaultUserParameter == null ) ? StringUtils.EMPTY : defaultUserParameter;

            defaultUserParameter = DefaultUserParameterHome.findByKey( PARAMETER_EXPIRED_ALERT_MAIL_SUBJECT );

            String strSubject = ( defaultUserParameter == null ) ? StringUtils.EMPTY : defaultUserParameter;
            
            sendMailAlerts( accountsToSetAsExpired, "expiration", strBody, strSender, strSubject );

            AdminUserHome.updateUserStatus( accountsToSetAsExpired, AdminUser.EXPIRED_CODE );

            StringBuilder sbLogs  = new StringBuilder( );
            sbLogs.append( MESSAGE_DAEMON_NAME );
            sbLogs.append( Integer.toString( nbAccountToExpire ) );
            sbLogs.append( " account(s) have expired" );
            AppLogService.info( sbLogs.toString( ) );
            sbResult.append( sbLogs.toString( ) );
            sbResult.append( "\n" );
        }
        else
        {
            AppLogService.info( MESSAGE_DAEMON_NAME + MESSAGE_EXPIRED_USER );
            sbResult.append( MESSAGE_DAEMON_NAME + MESSAGE_EXPIRED_USER );
        }
    }
    
    private void runExpiringUserAlert( Timestamp currentTimestamp, StringBuilder sbResult )
    {
        long nbDaysBeforeFirstAlert = AdminUserService.getIntegerSecurityParameter( PARAMETER_TIME_BEFORE_ALERT_ACCOUNT );

        if ( nbDaysBeforeFirstAlert <= 0 )
        {
            AppLogService.info( MESSAGE_DAEMON_NAME + MESSAGE_NO_NOTIF );
            sbResult.append( MESSAGE_DAEMON_NAME + MESSAGE_NO_NOTIF );
        }
        else
        {
            Timestamp firstAlertMaxDate = new Timestamp( currentTimestamp.getTime( ) + DateUtil.convertDaysInMiliseconds( nbDaysBeforeFirstAlert ) );
            List<Integer> userIdListToSendFirstAlert = AdminUserHome.getIdUsersToSendFirstAlert( firstAlertMaxDate );

            if ( CollectionUtils.isNotEmpty( userIdListToSendFirstAlert ) )
            {
                int nbFirstAlertSent = userIdListToSendFirstAlert.size( );
                String strBody = DatabaseTemplateService.getTemplateFromKey( PARAMETER_CORE_FIRST_ALERT_MAIL );

                String defaultUserParameter = DefaultUserParameterHome.findByKey( PARAMETER_FIRST_ALERT_MAIL_SENDER );
                String strSender = ( defaultUserParameter == null ) ? StringUtils.EMPTY : defaultUserParameter;

                defaultUserParameter = DefaultUserParameterHome.findByKey( PARAMETER_FIRST_ALERT_MAIL_SUBJECT );

                String strSubject = ( defaultUserParameter == null ) ? StringUtils.EMPTY : defaultUserParameter;
                
                sendMailAlerts( userIdListToSendFirstAlert, "first", strBody, strSender, strSubject );

                AdminUserHome.updateNbAlert( userIdListToSendFirstAlert );

                StringBuilder sbLogs = new StringBuilder( );
                sbLogs.append( MESSAGE_DAEMON_NAME );
                sbLogs.append( Integer.toString( nbFirstAlertSent ) );
                sbLogs.append( " first alert(s) have been sent" );
                AppLogService.info( sbLogs.toString( ) );
                sbResult.append( sbLogs.toString( ) );
                sbResult.append( "\n" );
            }
            else
            {
                AppLogService.info( "AccountLifeTimeDaemon - No first alert to send" );
                sbResult.append( "AccountLifeTimeDaemon - No first alert to send\n" );
            }
        }
    }
    
    private void runOtherUserAlert( Timestamp currentTimestamp, StringBuilder sbResult )
    {
        int maxNumberOfAlerts = AdminUserService.getIntegerSecurityParameter( PARAMETER_NB_ALERT_ACCOUNT );
        int nbDaysBetweenAlerts = AdminUserService.getIntegerSecurityParameter( PARAMETER_TIME_BETWEEN_ALERTS_ACCOUNT );
        long nbDaysBeforeFirstAlert = AdminUserService.getIntegerSecurityParameter( PARAMETER_TIME_BEFORE_ALERT_ACCOUNT );
        Timestamp timeBetweenAlerts = new Timestamp( DateUtil.convertDaysInMiliseconds( nbDaysBetweenAlerts ) );

        if ( ( maxNumberOfAlerts <= 0 ) || ( nbDaysBetweenAlerts <= 0 ) )
        {
            AppLogService.info( "AccountLifeTimeDaemon - Other alerts deactivated, skipping" );
            sbResult.append( "AccountLifeTimeDaemon - Other alerts deactivated, skipping\n" );
        }
        else
        {
            Timestamp firstAlertMaxDate = new Timestamp( currentTimestamp.getTime( ) + DateUtil.convertDaysInMiliseconds( nbDaysBeforeFirstAlert ) );
            List<Integer> userIdListToSendNextAlert = AdminUserHome.getIdUsersToSendOtherAlert( firstAlertMaxDate, timeBetweenAlerts, maxNumberOfAlerts );

            if ( CollectionUtils.isNotEmpty( userIdListToSendNextAlert ) )
            {
                int nbOtherAlertSent = userIdListToSendNextAlert.size( );
                String strBody = DatabaseTemplateService.getTemplateFromKey( PARAMETER_CORE_OTHER_ALERT_MAIL );

                String defaultUserParameter = DefaultUserParameterHome.findByKey( PARAMETER_OTHER_ALERT_MAIL_SENDER );
                String strSender = ( defaultUserParameter == null ) ? StringUtils.EMPTY : defaultUserParameter;

                defaultUserParameter = DefaultUserParameterHome.findByKey( PARAMETER_OTHER_ALERT_MAIL_SUBJECT );

                String strSubject = ( defaultUserParameter == null ) ? StringUtils.EMPTY : defaultUserParameter;
                
                sendMailAlerts( userIdListToSendNextAlert, "next", strBody, strSender, strSubject );

                AdminUserHome.updateNbAlert( userIdListToSendNextAlert );

                StringBuilder sbLogs = new StringBuilder( );
                sbLogs.append( MESSAGE_DAEMON_NAME );
                sbLogs.append( Integer.toString( nbOtherAlertSent ) );
                sbLogs.append( " next alert(s) have been sent" );
                AppLogService.info( sbLogs.toString( ) );
                sbResult.append( sbLogs.toString( ) );
            }
            else
            {
                AppLogService.info( MESSAGE_DAEMON_NAME + MESSAGE_NO_TEXT );
                sbResult.append( MESSAGE_DAEMON_NAME + MESSAGE_NO_TEXT );
            }
        }
    }
    
    private void runExpiredPassword( Timestamp currentTimestamp, StringBuilder sbResult )
    {
        if ( AdminUserService.getBooleanSecurityParameter( PARAMETER_NOTIFY_USER_PASSWORD_EXPIRED ) )
        {
            // We notify users with expired passwords
            List<Integer> accountsWithPasswordsExpired = AdminUserHome.getIdUsersWithExpiredPasswordsList( currentTimestamp );

            if ( CollectionUtils.isNotEmpty( accountsWithPasswordsExpired ) )
            {
                String strSender = AdminUserService.getSecurityParameter( PARAMETER_PASSWORD_EXPIRED_MAIL_SENDER );
                String strSubject = AdminUserService.getSecurityParameter( PARAMETER_PASSWORD_EXPIRED_MAIL_SUBJECT );
                String strBody = DatabaseTemplateService.getTemplateFromKey( PARAMETER_CORE_PASSWORD_EXPIRED_ALERT_MAIL );

                if ( StringUtils.isNotBlank( strBody ) )
                {
                    sendMailAlerts( accountsWithPasswordsExpired, "password expiration", strBody, strSender, strSubject );
                }

                AdminUserHome.updateChangePassword( accountsWithPasswordsExpired );
                StringBuilder sbLogs = new StringBuilder( );
                sbLogs.append( MESSAGE_DAEMON_NAME );
                sbLogs.append( Integer.toString( accountsWithPasswordsExpired.size( ) ) );
                sbLogs.append( " user(s) have been notified their password has expired" );
                AppLogService.info( sbLogs.toString( ) );
                sbResult.append( sbLogs.toString( ) );
                sbResult.append( "\n" );
            }
            else
            {
                AppLogService.info( MESSAGE_DAEMON_NAME + MESSAGE_EXPIRED );
                sbResult.append( MESSAGE_DAEMON_NAME + MESSAGE_EXPIRED );
            }
        }
        else
        {
            AppLogService.info( "AccountLifeTimeDaemon - Expired passwords notification deactivated, skipping" );
            sbResult.append( "AccountLifeTimeDaemon - Expired passwords notification deactivated, skipping" );
        }
    }
    
    
    private void sendMailAlerts( List<Integer> userIdList, String type, String strBody, String strSender, String strSubject )
    {
        for ( Integer nIdUser : userIdList )
        {
            try
            {
                AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );
                String strUserMail = user.getEmail( );

                if ( StringUtils.isNotBlank( strUserMail ) )
                {
                    Map<String, String> model = new HashMap<>( );
                    addParametersToModel( model, nIdUser );

                    HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( strBody, user.getLocale( ), model );
                    MailService.sendMailHtml( strUserMail, strSender, strSender, strSubject, template.getHtml( ) );
                }
            }
            catch( Exception e )
            {
                AppLogService.error( "AccountLifeTimeDaemon - Error sending " + type + " alert to admin user : " + e.getMessage( ), e );
            }
        }
    }

    /**
     * Adds the parameters to model.
     *
     * @param model
     *            the model
     * @param nIdUser
     *            the n id user
     */
    protected void addParametersToModel( Map<String, String> model, Integer nIdUser )
    {
        AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );

        if ( user.getAccountMaxValidDate( ) != null )
        {
            DateFormat dateFormat = DateFormat.getDateInstance( DateFormat.SHORT, LocaleService.getDefault( ) );

            String accountMaxValidDate = dateFormat.format( new Date( user.getAccountMaxValidDate( ).getTime( ) ) );

            String activationURL = AppPropertiesService.getProperty( PROPERTY_PROD_URL ) + JSP_URL_REACTIVATE_ACCOUNT;

            model.put( MARK_DATE_VALID, accountMaxValidDate );
            model.put( MARK_URL, activationURL );
        }

        model.put( MARK_LAST_NAME, user.getLastName( ) );
        model.put( MARK_FIRST_NAME, user.getFirstName( ) );
    }
}
