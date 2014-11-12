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

import fr.paris.lutece.portal.service.mail.MailItem;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;
import fr.paris.lutece.util.sql.Transaction;
import fr.paris.lutece.util.sql.TransactionManager;

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
    private static final String SQL_QUERY_SELECT_NEXT_MAIL_ITEM_QUEUE_ID = "SELECT min(id_mail_queue) FROM core_mail_queue WHERE is_locked=0";
    private static final String SQL_QUERY_SELECT_COUNT = "SELECT COUNT(id_mail_queue) FROM core_mail_queue";
    private static final String SQL_QUERY_LOAD_MAIL_ITEM = "SELECT id_mail_queue,mail_item FROM core_mail_item WHERE id_mail_queue=? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_mail_queue( id_mail_queue ) VALUES(?) ";
    private static final String SQL_QUERY_INSERT_MAIL_ITEM = " INSERT INTO core_mail_item(id_mail_queue,mail_item) VALUES(?,?) ";
    private static final String SQL_QUERY_LOCK_MAIL_ITEM = " UPDATE core_mail_queue SET is_locked=1 WHERE id_mail_queue= ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_mail_queue WHERE id_mail_queue = ?";
    private static final String SQL_QUERY_DELETE_MAIL_ITEM = " DELETE FROM core_mail_item WHERE id_mail_queue = ?";

    /**
     * Generates a new primary key
     * @return The new primary key
     */
    private int newPrimaryKey(  )
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
     * return the next mail item queue id
     * @return the next mail item queue id
     */
    @Override
    public int nextMailItemQueueId(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEXT_MAIL_ITEM_QUEUE_ID );

        daoUtil.executeQuery(  );

        int nIdMailItemQueue = -1;

        if ( daoUtil.next(  ) )
        {
            nIdMailItemQueue = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nIdMailItemQueue;
    }

    /**
     * Lock the mail item
     * @param nIdMailItemQueue the id of the mail item to lock
     */
    @Override
    public void lockMailItemQueue( int nIdMailItemQueue )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOCK_MAIL_ITEM );
        daoUtil.setInt( 1, nIdMailItemQueue );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Insert a new mail item in the table.
     * @param mailItemQueue the mail item
     */
    @Override
    public synchronized void insert( MailItemQueue mailItemQueue )
    {
        try
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
            ObjectOutputStream objectOutputStream;
            objectOutputStream = new ObjectOutputStream( byteArrayOutputStream );
            objectOutputStream.writeObject( mailItemQueue.getMailItem(  ) );
            objectOutputStream.close(  );
            byteArrayOutputStream.close(  );

            TransactionManager.beginTransaction( null );

            int nNewPrimaryKey = newPrimaryKey(  );
            mailItemQueue.setIdMailItemQueue( nNewPrimaryKey );

            try
            {
                DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
                daoUtil.setInt( 1, nNewPrimaryKey );
                daoUtil.executeUpdate(  );
                daoUtil.free(  );
                daoUtil = new DAOUtil( SQL_QUERY_INSERT_MAIL_ITEM );
                daoUtil.setInt( 1, nNewPrimaryKey );
                daoUtil.setBytes( 2, byteArrayOutputStream.toByteArray(  ) );
                daoUtil.executeUpdate(  );
                daoUtil.free(  );
                TransactionManager.commitTransaction( null );
            }
            catch ( Exception e )
            {
                TransactionManager.rollBack( null );
                AppLogService.error( e );
            }
        }
        catch ( Exception e )
        {
            AppLogService.error( e );
        }
    }

    /**
     * return the first mail item in the table
     * @param nIdMailItemQueue the id of the mail item
     * @return the first mail item in the table
     */
    @Override
    public MailItemQueue load( int nIdMailItemQueue )
    {
        MailItemQueue mailItemQueue = null;
        MailItem mailItem = null;
        InputStream inputStream;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD_MAIL_ITEM );
        daoUtil.setInt( 1, nIdMailItemQueue );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            mailItemQueue = new MailItemQueue(  );
            mailItemQueue.setIdMailItemQueue( daoUtil.getInt( 1 ) );
            inputStream = daoUtil.getBinaryStream( 2 );

            try
            {
                ObjectInputStream objectInputStream = new ObjectInputStream( inputStream );
                mailItem = (MailItem) objectInputStream.readObject(  );
                objectInputStream.close(  );
            }
            catch ( IOException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
            catch ( ClassNotFoundException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
            finally
            {
                try
                {
                    inputStream.close(  );
                }
                catch ( IOException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }
            }

            mailItemQueue.setMailItem( mailItem );
        }

        daoUtil.free(  );

        return mailItemQueue;
    }

    /**
     * Delete the mail item record in the table
     * @param nIdMailItemQueue The identifier of the mail item to remove
     */
    @Override
    public void delete( int nIdMailItemQueue )
    {
        Transaction transaction = new Transaction(  );

        try
        {
            transaction.prepareStatement( SQL_QUERY_DELETE_MAIL_ITEM );
            transaction.getStatement(  ).setInt( 1, nIdMailItemQueue );
            transaction.executeStatement(  );
            transaction.prepareStatement( SQL_QUERY_DELETE );
            transaction.getStatement(  ).setInt( 1, nIdMailItemQueue );
            transaction.executeStatement(  );
            transaction.commit(  );
        }
        catch ( Exception e )
        {
            transaction.rollback( e );
            AppLogService.error( e );
        }
    }

    /**
     * @return the number of mail item present in the core_mail_queue
     */
    @Override
    public int getCountMailItem(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_COUNT );
        daoUtil.executeQuery(  );

        int nCount = 0;

        if ( daoUtil.next(  ) )
        {
            nCount = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nCount;
    }
}
