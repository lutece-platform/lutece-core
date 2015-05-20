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
package fr.paris.lutece.portal.service.daemon;

import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.l10n.LocaleService;

import java.util.List;
import java.util.Locale;


/**
 * Daemon to anonymize admin users
 */
public class AnonymizationDaemon extends Daemon
{
    private static final String CONSTANT_NO_EXPIRED_USER = "There is no expired admin user to anonymize";
    private static final String CONSTANT_FOUND_EXPIRED_USER_ANONYMIZED_START = "AnonymizationService - Expired admin users have been found. Begining anonymization...";

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(  )
    {
        Locale locale = LocaleService.getDefault(  );
        StringBuilder sbLogs = new StringBuilder(  );
        StringBuilder sbResult = new StringBuilder(  );
        List<Integer> expiredUserIdList = AdminUserService.getExpiredUserIdList(  );

        if ( ( expiredUserIdList != null ) && ( expiredUserIdList.size(  ) > 0 ) )
        {
            int nbUserFound = expiredUserIdList.size(  );
            AppLogService.info( CONSTANT_FOUND_EXPIRED_USER_ANONYMIZED_START );

            for ( Integer nIdUser : expiredUserIdList )
            {
                AdminUserService.anonymizeUser( nIdUser, locale );
                AppLogService.info( "AnonymizationService - Admin user with id " + Integer.toString( nIdUser ) +
                    " has been anonymized" );
            }

            sbLogs.append( "AnonymizationService - " );
            sbLogs.append( nbUserFound );
            sbLogs.append( " admin user(s) have been anonymized" );
            AppLogService.info( sbLogs.toString(  ) );
            sbResult.append( sbLogs.toString(  ) );
        }
        else
        {
            sbLogs.append( CONSTANT_NO_EXPIRED_USER );
            AppLogService.info( sbLogs.toString(  ) );
            sbResult.append( sbLogs.toString(  ) );
        }

        setLastRunLogs( sbLogs.toString(  ) );
    }
}
