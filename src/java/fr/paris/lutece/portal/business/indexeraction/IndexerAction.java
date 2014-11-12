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
package fr.paris.lutece.portal.business.indexeraction;


/**
 *
 * IndexerAction
 *
 */
public class IndexerAction
{
    public static final int TASK_CREATE = 1;
    public static final int TASK_MODIFY = 2;
    public static final int TASK_DELETE = 3;
    private int _nIdAction;
    private int _nIdTask;
    private String _strIdDocument;
    private int _nIdPortlet = -1;
    private String _strIndexerName;

    /**
     *
     * @return the indexer name
     */
    public String getIndexerName(  )
    {
        return _strIndexerName;
    }

    /**
     * set the indexer name
     * @param indexerName the indexer name
     */
    public void setIndexerName( String indexerName )
    {
        _strIndexerName = indexerName;
    }

    /**
    *
    * @return the action id
    */
    public int getIdAction(  )
    {
        return _nIdAction;
    }

    /**
     * set the action id
     * @param idAction idAction
     */
    public void setIdAction( int idAction )
    {
        _nIdAction = idAction;
    }

    /**
    *
    * @return the  portlet id
    */
    public int getIdPortlet(  )
    {
        return _nIdPortlet;
    }

    /**
     * set the  portlet id
     * @param idPortlet the  portlet id
     */
    public void setIdPortlet( int idPortlet )
    {
        _nIdPortlet = idPortlet;
    }

    /**
     *
     * @return the document Id
     */
    public String getIdDocument(  )
    {
        return _strIdDocument;
    }

    /**
     * set the DocumentId
     * @param strIdDocument  document id
     */
    public void setIdDocument( String strIdDocument )
    {
        _strIdDocument = strIdDocument;
    }

    /**
     * get the task id
     * @return the task id
     */
    public int getIdTask(  )
    {
        return _nIdTask;
    }

    /**
     * set the task id
     * @param idTask the task id
     */
    public void setIdTask( int idTask )
    {
        _nIdTask = idTask;
    }
}
