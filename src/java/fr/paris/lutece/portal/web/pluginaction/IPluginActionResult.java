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
package fr.paris.lutece.portal.web.pluginaction;


/**
 * Result of an action. <br>
 * Use :
 * <ul>
 * <li>{@link #setRedirect(String)} : if the action need a redirect
 * <li>{@link #setHtmlContent(String)} : if the action needs to display html content
 * <li>{@link #setNoop(boolean)} : if no post-action is required (mainly for exports writing directly to the response).
 * </ul>
 *
 */
public interface IPluginActionResult
{
    /**
     * Returns the redirect url, <code>null</code> otherwise.
     * @return the redirect url, <code>null</code> otherwise
     */
    String getRedirect(  );

    /**
     * Sets the redirect url, <code>null</code> otherwise.
     * @param strRedirect the redirect url, <code>null</code> otherwise.
     */
    void setRedirect( String strRedirect );

    /**
     * Returns <code>true</code> if no operation needed, <code>false</code> otherwise.
     * @return <code>true</code> if no operation needed, <code>false</code> otherwise.
     */
    boolean isNoop(  );

    /**
     * Set to <code>true</code> if no operation needed, <code>false</code> otherwise.
     * @param bNoop <code>true</code> if no operation needed, <code>false</code> otherwise.
     */
    void setNoop( boolean bNoop );

    /**
     * Returns the html content if any, <code>null</code> otherwise
     * @return the html content if any, <code>null</code> otherwise
     */
    String getHtmlContent(  );

    /**
     * Sets the html content if any, <code>null</code> otherwise
     * @param strHtmlContent the html content if any, <code>null</code> otherwise
     */
    void setHtmlContent( String strHtmlContent );
}
