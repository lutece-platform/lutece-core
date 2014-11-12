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
package fr.paris.lutece.portal.service.csv;

import org.apache.commons.lang.StringUtils;


/**
 * Describe an error that occurred during the reading of a CSV file.
 */
public class CSVMessageDescriptor implements Comparable<CSVMessageDescriptor>
{
    private CSVMessageLevel _messageLevel;
    private int _nLineNumber;
    private String _strMessageContent;

    /**
     * Default constructor
     */
    public CSVMessageDescriptor(  )
    {
    }

    /**
     * Creates a new <i>CSVMessageDescriptor</i> with every attributes
     * initialized
     * @param messageLevel The level of the message
     * @param nLineNumber The number of the line associated with the message
     * @param strMessageContent The content of the message
     */
    public CSVMessageDescriptor( CSVMessageLevel messageLevel, int nLineNumber, String strMessageContent )
    {
        this._messageLevel = messageLevel;
        this._nLineNumber = nLineNumber;
        this._strMessageContent = strMessageContent;
    }

    /**
     * Get the level of the message
     * @return The level of the message
     */
    public CSVMessageLevel getMessageLevel(  )
    {
        return _messageLevel;
    }

    /**
     * Set the level of the message
     * @param messageLevel the level of the message
     */
    public void setMessageLevel( CSVMessageLevel messageLevel )
    {
        this._messageLevel = messageLevel;
    }

    /**
     * Get the number of the line of the CSV file associated with this message.
     * @return The number of the line of the CSV file associated with this
     *         message.
     */
    public int getLineNumber(  )
    {
        return _nLineNumber;
    }

    /**
     * Set the number of the line of the CSV file associated with this error.
     * @param nLineNumber The number of the line of the CSV file associated with
     *            this error.
     */
    public void setLineNumber( int nLineNumber )
    {
        this._nLineNumber = nLineNumber;
    }

    /**
     * Get the description of the message
     * @return The description of the message
     */
    public String getMessageContent(  )
    {
        return _strMessageContent;
    }

    /**
     * Set the description of the message
     * @param strMessageContent The description of the message
     */
    public void setMessageContent( String strMessageContent )
    {
        this._strMessageContent = strMessageContent;
    }

    /**
     * compare this CSVMessageDescriptor with another.<br />
     * <b>This method returns 0 for objects that are not equals ! Use with care
     * in collections !</b>
     * @param o Object to compare to
     * @return Returns :<br />
     *         <ul>
     *         <li>
     *         -1 if the line number of this object is greater than the line
     *         number of the other object, or if this object has an
     *         {@link CSVMessageLevel#INFO INFO} level and the other one an
     *         {@link CSVMessageLevel#ERROR ERROR} level if they have the same
     *         line number.</li>
     *         <li>
     *         0 if they both have the same line number and level, regardless of
     *         their description</li>
     *         <li>1 if the other object is null, if its line number if greater
     *         than the line number of the current object, or if this object has
     *         a {@link CSVMessageLevel#ERROR ERROR} level whereas the other has
     *         the {@link CSVMessageLevel#INFO INFO} level if they have the same
     *         line number.</li>
     *         </ul>
     */
    @Override
    public int compareTo( CSVMessageDescriptor o )
    {
        if ( null == o )
        {
            return 1;
        }

        if ( this.getLineNumber(  ) == o.getLineNumber(  ) )
        {
            if ( this.getMessageLevel(  ) == CSVMessageLevel.ERROR )
            {
                if ( o.getMessageLevel(  ) == CSVMessageLevel.ERROR )
                {
                    return getMessageContent(  ).compareTo( o.getMessageContent(  ) );
                }

                return 1;
            }

            if ( o.getMessageLevel(  ) == CSVMessageLevel.INFO )
            {
                return getMessageContent(  ).compareTo( o.getMessageContent(  ) );
            }

            return -1;
        }

        if ( this.getLineNumber(  ) > o.getLineNumber(  ) )
        {
            return 1;
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        if ( !( o instanceof CSVMessageDescriptor ) )
        {
            return false;
        }

        CSVMessageDescriptor other = (CSVMessageDescriptor) o;

        return ( getLineNumber(  ) == other.getLineNumber(  ) ) &&
        ( ( ( getMessageLevel(  ) == null ) && ( other.getMessageLevel(  ) == null ) ) ||
        getMessageLevel(  ).equals( other.getMessageLevel(  ) ) ) &&
        StringUtils.equals( getMessageContent(  ), other.getMessageContent(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode(  )
    {
        return ( this.getLineNumber(  ) * 1000 ) + this.getMessageLevel(  ).hashCode(  );
    }
}
