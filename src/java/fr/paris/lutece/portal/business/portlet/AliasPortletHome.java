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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;


/**
 * This class provides instances management methods for AliasPortlet objects
 */
public class AliasPortletHome extends PortletHome
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    // Static variable pointed at the DAO instance
    private static IAliasPortletDAO _dao = (IAliasPortletDAO) SpringContextService.getBean( "aliasPortletDAO" );

    /** This class implements the Singleton design pattern. */
    private static volatile AliasPortletHome _singleton;

    /**
     * Constructor
     */
    public AliasPortletHome(  )
    {
        if ( _singleton == null )
        {
            _singleton = this;
        }
    }

    /**
     * Returns the identifier of the alias portlet type
     *
     * @return the portlet type identifier
     */
    public String getPortletTypeId(  )
    {
        String strCurrentClassName = this.getClass(  ).getName(  );
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( strCurrentClassName );

        return strPortletTypeId;
    }

    /**
     * Returns the instance of AliasPortletHome
     *
     * @return the AliasPortletHome instance
     */
    public static PortletHome getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new AliasPortletHome(  );
        }

        return _singleton;
    }

    /**
     * Returns the instance of the FolderListingPortletDAO singleton
     *
     * @return the instance of the FolderListingPortletDAO
     */
    public IPortletInterfaceDAO getDAO(  )
    {
        return _dao;
    }

    /**
     * Returns the portlet alias identifier of the portlet whose identifier is specified in parameter
     *
     * @param nIdPortlet the identifier of the portlet
     * @return the identifier of the alias portlet
     */
    public static int getAliasId( int nIdPortlet )
    {
        return _dao.selectAliasId( nIdPortlet );
    }

    /**
     * Returns the list of the portlets which accept an alias
     *
     * @return the list of the portlets in form of ReferenceList
     */
    public static ReferenceList getAcceptAliasPortletList(  )
    {
        return _dao.selectAcceptAliasPortletList(  );
    }

    /**
     * Loads the list of the portlets whose type is the same as the one specified in parameter
     *
     * @param strPortletTypeId the portlet type identifier
     * @return the list of the portlets in form of a ReferenceList
     */
    public static ReferenceList getPortletsByTypeList( String strPortletTypeId )
    {
        return _dao.selectPortletsByTypeList( strPortletTypeId );
    }
}
