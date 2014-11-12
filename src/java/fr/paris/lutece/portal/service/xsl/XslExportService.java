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
package fr.paris.lutece.portal.service.xsl;

import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.business.xsl.XslExport;
import fr.paris.lutece.portal.business.xsl.XslExportHome;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.util.UniqueIDGenerator;


/**
 *
 * Class to export XML datas with a Xsl file.
 *
 */
public final class XslExportService
{
    private static final String XSL_UNIQUE_PREFIX_ID = UniqueIDGenerator.getNewId(  ) + "core-";

    /**
     * Instantiates a new xsl export service.
     */
    private XslExportService(  )
    {
    }

    /**
     * Transform an XML with an XSL
     * @param nIdXslExport Id of the Xsl export to use for the XML transformation.
     * @param strXml XML to transform
     * @return The given XML transformed with the given XSL
     */
    public static String exportXMLWithXSL( int nIdXslExport, String strXml )
    {
        XslExport xslExport = XslExportHome.findByPrimaryKey( nIdXslExport );
        PhysicalFile xslPhysicalFile = PhysicalFileHome.findByPrimaryKey( xslExport.getFile(  ).getPhysicalFile(  )
                                                                                   .getIdPhysicalFile(  ) );
        XmlTransformerService xmlTransformerService = new XmlTransformerService(  );

        String strXslId = XSL_UNIQUE_PREFIX_ID + xslPhysicalFile.getIdPhysicalFile(  );

        return xmlTransformerService.transformBySourceWithXslCache( strXml, xslPhysicalFile.getValue(  ), strXslId, null );
    }
}
