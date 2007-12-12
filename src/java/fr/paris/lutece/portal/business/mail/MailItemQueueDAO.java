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
package fr.paris.lutece.portal.business.mail;

import fr.paris.lutece.portal.service.mail.MailItem;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * This class provides Data Access methods for MailItemQueue objects
 */
public class MailItemQueueDAO implements IMailItemQueueDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max(id_mail_queue) FROM core_mail_queue";
    private static final String SQL_QUERY_SELECT = "SELECT min(id_mail_queue) FROM core_mail_queue";
    private static final String SQL_QUERY_LOAD = "SELECT id_mail_queue,mail_item FROM core_mail_queue WHERE id_mail_queue=?";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_mail_queue( id_mail_queue ,mail_item) " +
        " VALUES ( ?, ?)";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_mail_queue WHERE id_mail_queue = ?";

    /**
     * Generates a new primary key
     * @return The new primary key
     */
    int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new mail item in the table.
     * @param mailItemQueue the mail item
     */
    public void insert( MailItemQueue mailItemQueue )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        int nNewPrimaryKey = newPrimaryKey(  );
        mailItemQueue.setIdMailItemQueue( nNewPrimaryKey );
        daoUtil.setInt( 1, mailItemQueue.getIdMailItemQueue(  ) );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
        ObjectOutputStream objectOutputStream;

        try
        {
            objectOutputStream = new ObjectOutputStream( byteArrayOutputStream );
            objectOutputStream.writeObject( mailItemQueue.getMailItem(  ) );
            objectOutputStream.close(  );
            byteArrayOutputStream.close(  );
            daoUtil.setBytes( 2, byteArrayOutputStream.toByteArray(  ) );
            daoUtil.executeUpdate(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }

        daoUtil.free(  );
    }

    /**
     * return the first mail item  in the table
     * @return the first mail item in the table
     */
    public MailItemQueue select(  )
    {
        MailItemQueue mailItemQueue = null;
        MailItem mailItem = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        InputStream inputStream;
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            mailItemQueue = new MailItemQueue(  );
            mailItemQueue.setIdMailItemQueue( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        if ( mailItemQueue != null )
        {
            daoUtil = new DAOUtil( SQL_QUERY_LOAD );
            daoUtil.setInt( 1, mailItemQueue.getIdMailItemQueue(  ) );
            daoUtil.executeQuery(  );

            if ( daoUtil.next(  ) )
            {
                inputStream = daoUtil.getBinaryStream( 2 );

                try
                {
                    ObjectInputStream objectInputStream = new ObjectInputStream( inputStream );
                    mailItem = (MailItem) objectInputStream.readObject(  );
                    objectInputStream.close(  );
                    inputStream.close(  );
                }
                catch ( IOException e )
                {
                    // TODO Auto-generated catch block
                    AppLogService.error( e );
                }
                catch ( ClassNotFoundException e )
                {
                    // TODO Auto-generated catch block
                    AppLogService.error( e );
                }

                mailItemQueue.setMailItem( mailItem );
            }

            daoUtil.free(  );
        }

        return mailItemQueue;
    }

    /**
     * Delete  the mail item record in the table
     * @param nIdMailItemQueue The indentifier of the mail item to remove
     */
    public void delete( int nIdMailItemQueue )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nIdMailItemQueue );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
