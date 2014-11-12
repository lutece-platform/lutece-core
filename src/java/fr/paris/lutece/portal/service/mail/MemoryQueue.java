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
package fr.paris.lutece.portal.service.mail;

import java.util.ArrayList;
import java.util.List;


/**
 * MemoryQueue
 */
public class MemoryQueue implements IMailQueue
{
    private List<MailItem> _listMails = new ArrayList<MailItem>(  );

    /**
     * Put a mail item into the list of the queue
     * @param item The mail item to add to the queue
     */
    public void send( MailItem item )
    {
        synchronized ( _listMails )
        {
            _listMails.add( item );
        }
    }

    /**
     * Get a mail item from the list and remove it from the queue
     * @return The older mail item of the queue
     */
    public MailItem consume(  )
    {
        MailItem item = null;

        synchronized ( _listMails )
        {
            if ( _listMails.size(  ) > 0 )
            {
                item = _listMails.get( 0 );
                _listMails.remove( 0 );
            }
        }

        return item;
    }

    /**
     * get the MemoryQueue size
     * @return the MemoryQueue size
     */
    public int size(  )
    {
        return _listMails.size(  );
    }
}
