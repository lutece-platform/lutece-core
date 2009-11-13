/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.portal.business.rss;


/*
 * ResourceRss
 */
public abstract class ResourceRss implements IResourceRss
{
    private int _nId;
    private IResourceRssType _taskType;
    private String _strName;
    private String _strDescription;

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRss#getId()
     */
    public int getId(  )
    {
        return _nId;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRss#setId(int)
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRss#getResourceRssType()
     */
    public IResourceRssType getResourceRssType(  )
    {
        return _taskType;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRss#setResourceRssType(fr.paris.lutece.portal.business.rss.IResourceRssType)
     */
    public void setResourceRssType( IResourceRssType taskType )
    {
        _taskType = taskType;
    }
    
    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRss#getDescription()
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRss#setName(String)
     */
    public void setName( String strName )
    {
        _strName = strName;
    }
    
    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRss#setDescription(String)
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRss#getName()
     */
    public String getName(  )
    {
        return _strName;
    }
}
