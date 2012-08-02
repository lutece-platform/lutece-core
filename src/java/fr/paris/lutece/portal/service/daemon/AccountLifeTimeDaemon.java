package fr.paris.lutece.portal.service.daemon;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameter;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameterHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.template.DatabaseTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * Daemon to anonymize admin users
 */
public class AccountLifeTimeDaemon extends Daemon
{

    private static final String PARAMETER_TIME_BEFORE_ALERT_ACCOUNT = "time_before_alert_account";
    private static final String PARAMETER_NB_ALERT_ACCOUNT = "nb_alert_account";
    private static final String PARAMETER_TIME_BETWEEN_ALERTS_ACCOUNT = "time_between_alerts_account";

    private static final String PARAMETER_EXPIRED_ALERT_MAIL_SENDER = "expired_alert_mail_sender";
    private static final String PARAMETER_EXPIRED_ALERT_MAIL_SUBJECT = "expired_alert_mail_subject";
    private static final String PARAMETER_FIRST_ALERT_MAIL_SENDER = "first_alert_mail_sender";
    private static final String PARAMETER_FIRST_ALERT_MAIL_SUBJECT = "first_alert_mail_subject";
    private static final String PARAMETER_OTHER_ALERT_MAIL_SENDER = "other_alert_mail_sender";
    private static final String PARAMETER_OTHER_ALERT_MAIL_SUBJECT = "other_alert_mail_subject";

    private static final String PARAMETER_CORE_EXPIRATION_MAIL = "core_expiration_mail";
    private static final String PARAMETER_CORE_FIRST_ALERT_MAIL = "core_first_alert_mail";
    private static final String PARAMETER_CORE_OTHER_ALERT_MAIL = "core_other_alert_mail";

    private static final String MARK_LAST_NAME = "last_name";
    private static final String MARK_FIRST_NAME = "first_name";
    private static final String MARK_DATE_VALID = "date_valid";
    private static final String MARK_URL = "url";

    private static final String PROPERTY_PROD_URL = "init.webapp.prod.url";

    private static final String JSP_URL_REACTIVATE_ACCOUNT = "/jsp/admin/user/ReactivateAccount.jsp";


    @SuppressWarnings( "deprecation" )
    @Override
    public void run( )
    {
        StringBuilder sbLogs = null;
        
        Timestamp currentTimestamp = new Timestamp( new java.util.Date( ).getTime( ) );
        List<Integer> accountsToSetAsExpired = AdminUserHome.getIdUsersWithExpiredLifeTimeList( currentTimestamp );

        // We first set as expirated user that have reached their life time limit
        if ( accountsToSetAsExpired != null && accountsToSetAsExpired.size( ) > 0 )
        {
            int nbAccountToExpire = accountsToSetAsExpired.size( );
            String strBody = DatabaseTemplateService.getTemplateFromKey( PARAMETER_CORE_EXPIRATION_MAIL );

            DefaultUserParameter defaultUserParameter = DefaultUserParameterHome
                    .findByKey( PARAMETER_EXPIRED_ALERT_MAIL_SENDER );
            String strSender = defaultUserParameter == null ? StringUtils.EMPTY : defaultUserParameter
                    .getParameterValue( );

            defaultUserParameter = DefaultUserParameterHome.findByKey( PARAMETER_EXPIRED_ALERT_MAIL_SUBJECT );
            String strSubject = defaultUserParameter == null ? StringUtils.EMPTY : defaultUserParameter
                    .getParameterValue( );

            for ( Integer nIdUser : accountsToSetAsExpired )
            {
                try
                {
                    AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );
                    String strUserMail = user.getEmail( );
                    if ( strUserMail != null && StringUtils.isNotBlank( strUserMail ) )
                    {
                        Map<String, String> model = new HashMap<String, String>( );
                        addParametersToModel( model, nIdUser );
                        HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( strBody,
                                user.getLocale( ), model );
                        MailService.sendMailHtml( strUserMail, strSender, strSender, strSubject, template.getHtml( ) );
                    }
                }
                catch ( Exception e )
                {
                    AppLogService.error(
                            "AccountLifeTimeDaemon - Error sending expiration alert to admin user : "
                                    + e.getMessage( ), e );
                }
            }
            AdminUserHome.updateUserStatus( accountsToSetAsExpired, AdminUser.EXPIRED_CODE );

            sbLogs = new StringBuilder( );
            sbLogs.append( "MyluteceAccountLifeTimeDaemon - " );
            sbLogs.append( Integer.toString( nbAccountToExpire ) );
            sbLogs.append( " account(s) have expired" );
            AppLogService.info( sbLogs.toString( ) );
            accountsToSetAsExpired = null;
        }
        else
        {
            AppLogService.info( "AccountLifeTimeDaemon - No expired admin user found" );
        }
        
        // We send first alert to users
        long nbDaysBeforeFirstAlert = AdminUserService
                .getIntegerSecurityParameter( PARAMETER_TIME_BEFORE_ALERT_ACCOUNT );

        Timestamp firstAlertMaxDate = new Timestamp( currentTimestamp.getTime( )
                + DateUtil.convertDaysInMiliseconds( nbDaysBeforeFirstAlert ) );
        if ( nbDaysBeforeFirstAlert <= 0 )
        {
            AppLogService.info( "AccountLifeTimeDaemon - First alert deactivated, skipping" );
        }
        else
        {
            List<Integer> userIdListToSendFirstAlert = AdminUserHome.getIdUsersToSendFirstAlert( firstAlertMaxDate );
            if ( userIdListToSendFirstAlert != null && userIdListToSendFirstAlert.size( ) > 0 )
            {
                int nbFirstAlertSent = userIdListToSendFirstAlert.size( );
                String strBody = DatabaseTemplateService.getTemplateFromKey( PARAMETER_CORE_FIRST_ALERT_MAIL );

                DefaultUserParameter defaultUserParameter = DefaultUserParameterHome
                        .findByKey( PARAMETER_FIRST_ALERT_MAIL_SENDER );
                String strSender = defaultUserParameter == null ? StringUtils.EMPTY : defaultUserParameter
                        .getParameterValue( );

                defaultUserParameter = DefaultUserParameterHome.findByKey( PARAMETER_FIRST_ALERT_MAIL_SUBJECT );
                String strSubject = defaultUserParameter == null ? StringUtils.EMPTY : defaultUserParameter
                        .getParameterValue( );

                for ( Integer nIdUser : userIdListToSendFirstAlert )
                {
                    try
                    {
                        AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );
                        String strUserMail = user.getEmail( );
                        if ( strUserMail != null && StringUtils.isNotBlank( strUserMail ) )
                        {
                            Map<String, String> model = new HashMap<String, String>( );
                            addParametersToModel( model, nIdUser );
                            HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( strBody,
                                    user.getLocale( ), model );
                            MailService
                                    .sendMailHtml( strUserMail, strSender, strSender, strSubject, template.getHtml( ) );
                        }
                    }
                    catch ( Exception e )
                    {
                        AppLogService.error( "AccountLifeTimeDaemon - Error sending first alert to admin user : " + e.getMessage( ), e );
                    }
                }

                AdminUserHome.updateNbAlert( userIdListToSendFirstAlert );

                sbLogs = new StringBuilder( );
                sbLogs.append( "AccountLifeTimeDaemon - " );
                sbLogs.append( Integer.toString( nbFirstAlertSent ) );
                sbLogs.append( " first alert(s) have been sent" );
                AppLogService.info( sbLogs.toString( ) );

                userIdListToSendFirstAlert = null;
            }
            else
            {
                AppLogService.info( "AccountLifeTimeDaemon - No first alert to send" );
            }
        }
        
        // We send other alert to users
        int maxNumberOfAlerts = AdminUserService.getIntegerSecurityParameter( PARAMETER_NB_ALERT_ACCOUNT );
        int nbDaysBetweenAlerts = AdminUserService.getIntegerSecurityParameter( PARAMETER_TIME_BETWEEN_ALERTS_ACCOUNT );
        Timestamp timeBetweenAlerts = new Timestamp( DateUtil.convertDaysInMiliseconds( nbDaysBetweenAlerts ) );

        if ( maxNumberOfAlerts <= 0 || nbDaysBetweenAlerts <= 0 )
        {
            AppLogService.info( "AccountLifeTimeDaemon - Other alerts deactivated, skipping" );
        }
        else
        {
            List<Integer> userIdListToSendNextAlert = AdminUserHome.getIdUsersToSendOtherAlert( firstAlertMaxDate,
                    timeBetweenAlerts, maxNumberOfAlerts );
            if ( userIdListToSendNextAlert != null && userIdListToSendNextAlert.size( ) > 0 )
            {
                int nbOtherAlertSent = userIdListToSendNextAlert.size( );
                String strBody = DatabaseTemplateService.getTemplateFromKey( PARAMETER_CORE_OTHER_ALERT_MAIL );

                DefaultUserParameter defaultUserParameter = DefaultUserParameterHome
                        .findByKey( PARAMETER_OTHER_ALERT_MAIL_SENDER );
                String strSender = defaultUserParameter == null ? StringUtils.EMPTY : defaultUserParameter
                        .getParameterValue( );

                defaultUserParameter = DefaultUserParameterHome.findByKey( PARAMETER_OTHER_ALERT_MAIL_SUBJECT );
                String strSubject = defaultUserParameter == null ? StringUtils.EMPTY : defaultUserParameter
                        .getParameterValue( );

                for ( Integer nIdUser : userIdListToSendNextAlert )
                {
                    try
                    {
                        AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );
                        String strUserMail = user.getEmail( );
                        if ( strUserMail != null && StringUtils.isNotBlank( strUserMail ) )
                        {
                            Map<String, String> model = new HashMap<String, String>( );
                            addParametersToModel( model, nIdUser );
                            HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( strBody,
                                    user.getLocale( ), model );
                            MailService
                                    .sendMailHtml( strUserMail, strSender, strSender, strSubject, template.getHtml( ) );
                        }
                    }
                    catch ( Exception e )
                    {
                        AppLogService.error( "AccountLifeTimeDaemon - Error sending next alert to admin user : " + e.getMessage( ), e );
                    }
                }

                AdminUserHome.updateNbAlert( userIdListToSendNextAlert );

                sbLogs = new StringBuilder( );
                sbLogs.append( "MyluteceAccountLifeTimeDaemon - " );
                sbLogs.append( Integer.toString( nbOtherAlertSent ) );
                sbLogs.append( " next alert(s) have been sent" );
                AppLogService.info( sbLogs.toString( ) );

                userIdListToSendNextAlert = null;
            }
            else
            {
                AppLogService.info( "AccountLifeTimeDaemon - No next alert to send" );
            }
        }
    }

    protected void addParametersToModel( Map<String, String> model, Integer nIdUser )
    {
        AdminUser user = AdminUserHome.findByPrimaryKey( nIdUser );
        DateFormat dateFormat = SimpleDateFormat.getDateInstance( DateFormat.SHORT, Locale.getDefault( ) );

        String accountMaxValidDate = dateFormat.format( new Date( user.getAccountMaxValidDate( ).getTime( ) ) );

        String activationURL = AppPropertiesService.getProperty( PROPERTY_PROD_URL ) + JSP_URL_REACTIVATE_ACCOUNT;

        model.put( MARK_DATE_VALID, accountMaxValidDate );
        model.put( MARK_URL, activationURL );
        model.put( MARK_LAST_NAME, user.getLastName( ) );
        model.put( MARK_FIRST_NAME, user.getFirstName( ) );
    }
}
