package fr.paris.lutece.portal.service.daemon;

import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.List;
import java.util.Locale;


/**
 * Daemon to anonymize admin users
 */
public class AnonymizationDaemon extends Daemon
{


    private static final String CONSTANT_NO_EXPIRED_USER = "There is no expired admin user to anonymize";
    private static final String CONSTANT_FOUND_EXPIRED_USER_ANONYMIZED_START = "AnonymizationService - Expired admin users have been found. Begining anonymization...";

    @Override
    public void run( )
    {
        Locale locale = Locale.getDefault( );
        StringBuilder sbLogs = new StringBuilder( );
		StringBuilder sbResult = new StringBuilder( );
        List<Integer> expiredUserIdList = AdminUserService.getExpiredUserIdList( );
        if ( expiredUserIdList != null && expiredUserIdList.size( ) > 0 )
        {
            int nbUserFound = expiredUserIdList.size( );
            AppLogService.info( CONSTANT_FOUND_EXPIRED_USER_ANONYMIZED_START );
            
            for ( Integer nIdUser : expiredUserIdList )
            {
                AdminUserService.anonymizeUser( nIdUser, locale );
                AppLogService.info( "AnonymizationService - Admin user with id " + Integer.toString( nIdUser )
                        + " has been anonymized" );
            }
            
            sbLogs.append( "AnonymizationService - " );
            sbLogs.append( nbUserFound );
            sbLogs.append( " admin user(s) have been anonymized" );
            AppLogService.info( sbLogs.toString( ) );
			sbResult.append( sbLogs.toString( ) );
        }
        else
        {
            sbLogs.append( CONSTANT_NO_EXPIRED_USER );
            AppLogService.info( sbLogs.toString( ) );
			sbResult.append( sbLogs.toString( ) );
        }
		this.setLastRunLogs( sbLogs.toString( ) );
    }

}
