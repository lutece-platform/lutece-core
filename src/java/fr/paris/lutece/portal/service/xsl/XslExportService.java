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
public class XslExportService
{
	private static final String XSL_UNIQUE_PREFIX_ID = UniqueIDGenerator.getNewId( ) + "core-";

	/**
	 * Transform an XML with an XSL
	 * @param nIdXslExport Id of the Xsl export to use for the XML transformation.
	 * @param strXml XML to transform
	 * @return The given XML transformed with the given XSL
	 */
	public static String exportXMLWithXSL( int nIdXslExport, String strXml )
	{
		XslExport xslExport = XslExportHome.findByPrimaryKey( nIdXslExport );
		PhysicalFile xslPhysicalFile = PhysicalFileHome.findByPrimaryKey( xslExport.getFile( ).getPhysicalFile( ).getIdPhysicalFile( ) );
		XmlTransformerService xmlTransformerService = new XmlTransformerService( );

		String strXslId = XSL_UNIQUE_PREFIX_ID + xslPhysicalFile.getIdPhysicalFile( );
		return xmlTransformerService.transformBySourceWithXslCache( strXml, xslPhysicalFile.getValue( ), strXslId, null );
	}
}
