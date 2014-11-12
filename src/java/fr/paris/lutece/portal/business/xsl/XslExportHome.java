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
package fr.paris.lutece.portal.business.xsl;

import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for ExportFormat objects
 */
public final class XslExportHome
{
    // Static variable pointed at the DAO instance
    private static IXslExportDAO _dao = SpringContextService.getBean( "xslExportDAO" );

    /**
     * Private constructor - this class do not need to be instantiated
     */
    private XslExportHome(  )
    {
    }

    /**
     * Creation of an instance of Xsl Export
     *
     * @param xslExport The instance of the xslExport which contains the informations to store
     *
     */
    public static void create( XslExport xslExport )
    {
        _dao.insert( xslExport );
    }

    /**
     * Update of the XslExport which is specified in parameter
     *
     * @param xslExport The instance of the xslExport which contains the informations to update
     */
    public static void update( XslExport xslExport )
    {
        _dao.store( xslExport );
        XmlTransformerService.clearXslCache(  );
    }

    /**
     * Remove the XslExport whose identifier is specified in parameter
     *
     * @param nIdXslExport The XslExport Id
     */
    public static void remove( int nIdXslExport )
    {
        _dao.delete( nIdXslExport );
        XmlTransformerService.clearXslCache(  );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a XslExport whose identifier is specified in parameter
     *
     * @param nKey The xslExport primary key
     * @return an instance of XslExport
     */
    public static XslExport findByPrimaryKey( int nKey )
    {
        XslExport xslExport = _dao.load( nKey );

        if ( ( xslExport != null ) && ( xslExport.getFile(  ) != null ) )
        {
            xslExport.setFile( FileHome.findByPrimaryKey( xslExport.getFile(  ).getIdFile(  ) ) );
        }

        return xslExport;
    }

    /**
     * Loads the data of all the XslExport and returns them in a list
     * @return the list which contains the data of every Xsl export items
     */
    public static List<XslExport> getList(  )
    {
        return _dao.selectList(  );
    }

    /**
     * Loads the data of XslExport associated with a given plugin and returns
     * them in a list
     * @param plugin The plugin
     * @return the list which contains the data of Xsl export items
     */
    public static List<XslExport> getListByPlugin( Plugin plugin )
    {
        return _dao.selectListByPlugin( plugin );
    }

    /**
     * Loads in the reference list the data of all the XslExport and returns
     * them in a list
     * @return the list which contains the data of every Xsl Export items
     */
    public static ReferenceList getRefList(  )
    {
        ReferenceList refList = new ReferenceList(  );

        List<XslExport> xslList = getList(  );

        for ( XslExport xslExport : xslList )
        {
            refList.addItem( xslExport.getIdXslExport(  ), xslExport.getTitle(  ) );
        }

        return refList;
    }

    /**
     * Loads the data of XslExport associated with a given plugin and returns
     * them in a list
     * @param plugin The plugin
     * @return the list which contains the data of Xsl export items
     */
    public static ReferenceList getRefListByPlugin( Plugin plugin )
    {
        ReferenceList refList = new ReferenceList(  );

        List<XslExport> xslList = getListByPlugin( plugin );

        for ( XslExport xslExport : xslList )
        {
            refList.addItem( xslExport.getIdXslExport(  ), xslExport.getTitle(  ) );
        }

        return refList;
    }
}
