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
package fr.paris.lutece.portal.business.globalmanagement;


/**
 * Rich text editor configuration
 */
public class RichTextEditor
{
    private String _strEditorName;
    private String _strDescription;
    private boolean _bBackOffice;

    /**
     * Get the name of the editor
     * @return The name of the editor
     */
    public String getEditorName(  )
    {
        return _strEditorName;
    }

    /**
     * Set the name of the editor
     * @param strEditorName The name of the editor
     */
    public void setEditorName( String strEditorName )
    {
        _strEditorName = strEditorName;
    }

    /**
     * Get the description of the editor
     * @return The description of the editor
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Set the description of the editor
     * @param strDescription The description of the editor
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Get a boolean describing of this text editor should be used for back of front office
     * @return True if this editor should be used for back office, false if it should be used for front office.
     */
    public boolean getBackOffice(  )
    {
        return _bBackOffice;
    }

    /**
     * Set a boolean describing of this text editor should be used for back of front office
     * @param bBackOffice True if this editor should be used for back office, false if it should be used for front office.
     */
    public void setBackOffice( boolean bBackOffice )
    {
        _bBackOffice = bBackOffice;
    }
}
