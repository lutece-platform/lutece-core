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
package fr.paris.lutece.util.mail;

import java.io.Serializable;

import java.net.URL;


/**
 *
 * UrlAttachement Object
 *
 */
public class UrlAttachment implements Serializable
{
    private static final long serialVersionUID = 5638898463494932360L;
    private String _strContentLocation; //content location
    private URL _urlData; //the url data

    /**
    * Creates a new UrlAttachement object.
    *
    * @param strContentLocation the content location
    * @param urlData the url Data
    */
    public UrlAttachment( String strContentLocation, URL urlData )
    {
        _strContentLocation = strContentLocation;
        _urlData = urlData;
    }

    /**
     *
     * @return the content-location
     */
    public String getContentLocation(  )
    {
        return _strContentLocation;
    }

    /**
     * set the content-location
     * @param contentLocation the content-location
     */
    public void setContentLocation( String contentLocation )
    {
        _strContentLocation = contentLocation;
    }

    /**
     *
     * @return the URL of data
     */
    public URL getUrlData(  )
    {
        return _urlData;
    }

    /**
     * set the URL of data
     * @param data the URL of data
     */
    public void setUrlData( URL data )
    {
        _urlData = data;
    }
}
