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
package fr.paris.lutece.portal.business.mail;

import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * This class provides Data Access methods for MailItemQueue objects
 */
public final class MailItemQueueHome
{
    // Static variable pointed at the DAO instance
    private static IMailItemQueueDAO _dao = (IMailItemQueueDAO) SpringContextService.getBean( "mailItemQueueDAO" );

    /**
     * Creates a new MailItemQueueHome object.
     */
    private MailItemQueueHome(  )
    {
    }

    /**
     * Insert a new mail item in the database queue.
     * @param mailItemQueue the mail item to insert
     */
    public static void create( MailItemQueue mailItemQueue )
    {
        _dao.insert( mailItemQueue );
    }

    /**
     * Delete  the mail item record in the table
     * @param nIdMailItemQueue The indentifier of the mail item to remove
     */
    public static void delete( int nIdMailItemQueue )
    {
        _dao.delete( nIdMailItemQueue );
    }

    /**
     * Return the first mail item in the queue
     * @return the first mail item in the queue
     */
    public static MailItemQueue getNextMailItemQueue(  )
    {
        //get the id of the next mail item queue
        int nIdMailItemQueue = _dao.nextMailItemQueueId(  );

        if ( nIdMailItemQueue != -1 )
        {
            //lock the mail item queue before getting mailItemQueue Object
            _dao.lockMailItemQueue( nIdMailItemQueue );

            return _dao.load( nIdMailItemQueue );
        }

        return null;
    }

    /**
     *
     * @return the number of mail item present in the queue
     */
    public static int getMailItemNumber(  )
    {
        return _dao.getCountMailItem(  );
    }
}
