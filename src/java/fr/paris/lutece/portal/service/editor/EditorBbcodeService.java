package fr.paris.lutece.portal.service.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.editor.ParserComplexElement;
import fr.paris.lutece.portal.business.editor.ParserElement;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.parser.BbcodeUtil;

/**
 * 
 * This class Provides a parser BBCODE
 *
 */
public class EditorBbcodeService implements IEditorBbcodeService {
	
	
	/** Constants **/
    
    private static final String CONSTANT_EDITOR_BBCODE_ELEMENT_CODE = "code";
    private static final String CONSTANT_EDITOR_BBCODE_ELEMENT_VALUE = "value";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_TAG_NAME="tagName";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_OPEN_SUBST_WITH_PARAM="openSubstWithParam";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_CLOSE_SUBST_WITH_PARAM="closeSubstWithParam";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_OPEN_SUBST_WITHOUT_PARAM="openSubstWithoutParam";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_CLOSE_SUBST_WITHOUT_PARAM="closeSubstWithoutParam";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_INTERNAL_SUBST="internalSubst";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_PROCESS_INTERNAL_TAGS="processInternalTags";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_ACCEPT_PARAM="acceptParam";
    private static final String CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_REQUIRES_QUOTED_PARAM="requiresQuotedParam";
    
   
    /** Properties**/
    private static final String PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH="editors.parser.bbcode.complexElement";
    private static final String PROPERTY_EDITOR_BBCODE_ELEMENT_PATH = "editors.parser.bbcode.element";
    
    private static String PROPERTY_PARSER_ELEMENTS="editors.parser.bbcode.elements";
    private static String PROPERTY_PARSER_COMPLEX_ELEMENTS="editors.parser.bbcode.complexElements";
    
    private static final String SEPARATOR = ",";
	private static EditorBbcodeService _singleton;
	private static List<ParserElement> _listParserElement;
	private static List<ParserComplexElement> _listParserComplexElement;
	
	
	/*
	 * (non-Javadoc)
	 * @see fr.paris.lutece.portal.service.editor.IEditorBbcodeService#parse(java.lang.String)
	 */
	public String parse(String strValue)
	{
		
		return BbcodeUtil.parse(strValue, _listParserElement,_listParserComplexElement);
		
	}
    /**
	    * Returns the unique instance of the service
	    * @return The instance of the service
	    */
	public static EditorBbcodeService getInstance()
	{
		if ( _singleton == null )
        {
            _singleton = new EditorBbcodeService();
            _singleton.init();
        }

        return _singleton;
    }
	
	
	public void init()
	{
		 _listParserElement=new ArrayList<ParserElement>();
		 _listParserComplexElement=new ArrayList<ParserComplexElement>();
		//init simple elements
		 String strParserElements = AppPropertiesService.getProperty( PROPERTY_PARSER_ELEMENTS );
		
	        if ( StringUtils.isNotBlank( strParserElements ) )
	        {
	            String[] tabParserElements = strParserElements.split( SEPARATOR );
	            String strCodeElement;
	            String strValueElement;

	            for ( int i = 0; i < tabParserElements.length; i++ )
	            {
	                strCodeElement = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_ELEMENT_PATH+ "." +
	                		tabParserElements[i]+ "."+CONSTANT_EDITOR_BBCODE_ELEMENT_CODE);
	                strValueElement = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_ELEMENT_PATH+ "." +
	                		tabParserElements[i]+ "."+CONSTANT_EDITOR_BBCODE_ELEMENT_VALUE);
	                _listParserElement.add( new ParserElement(strCodeElement,strValueElement));
	               
	            }
	        }
	      //init complex elements
	        String strParserComplexElements = AppPropertiesService.getProperty( PROPERTY_PARSER_COMPLEX_ELEMENTS );
		    
	        if ( StringUtils.isNotBlank( strParserComplexElements ) )
	        {
	            String[] tabParserComplexElements = strParserComplexElements.split( SEPARATOR );
	            String strTagName;
	            String strOpenSubstWithParam;
	            String strCloseSubstWithParam;
	            String strOpenSubstWithoutParam;
	            String strCloseSubstWithoutParam;
	            String strInternalSubst;
	            boolean bProcessInternalTags;
	            boolean bAcceptParam;
	            boolean bRequiresQuotedParam;
	            

	            for ( int i = 0; i < tabParserComplexElements.length; i++ )
	            {
	                strTagName = AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH+ "." +
	                		tabParserComplexElements[i]+ "."+CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_TAG_NAME);
	                strOpenSubstWithParam= AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH+ "." +
	                		tabParserComplexElements[i]+ "."+CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_OPEN_SUBST_WITH_PARAM);
	                strCloseSubstWithParam= AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH+ "." +
	                		tabParserComplexElements[i]+ "."+CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_CLOSE_SUBST_WITH_PARAM);
	                strOpenSubstWithoutParam= AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH+ "." +
	                		tabParserComplexElements[i]+ "."+CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_OPEN_SUBST_WITHOUT_PARAM);
	                strCloseSubstWithoutParam= AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH+ "." +
	                		tabParserComplexElements[i]+ "."+CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_CLOSE_SUBST_WITHOUT_PARAM);
	                strInternalSubst=AppPropertiesService.getProperty( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH+ "." +
	                		tabParserComplexElements[i]+ "."+CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_INTERNAL_SUBST);
	                bProcessInternalTags= AppPropertiesService.getPropertyBoolean( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH+ "." +
	                		tabParserComplexElements[i]+ "."+CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_PROCESS_INTERNAL_TAGS,false);
	                bAcceptParam= AppPropertiesService.getPropertyBoolean( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH+ "." +
	                		tabParserComplexElements[i]+ "."+CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_ACCEPT_PARAM,false);
	                bRequiresQuotedParam= AppPropertiesService.getPropertyBoolean( PROPERTY_EDITOR_BBCODE_COMPLEX_ELEMENT_PATH+ "." +
	                		tabParserComplexElements[i]+ "."+CONSTANT_EDITOR_BBCODE_COMPLEX_ELEMENT_REQUIRES_QUOTED_PARAM,false);
	                
	               	_listParserComplexElement.add( new ParserComplexElement(strTagName, strOpenSubstWithParam, strCloseSubstWithParam, strOpenSubstWithoutParam, strCloseSubstWithoutParam, strInternalSubst, bProcessInternalTags, bAcceptParam, bRequiresQuotedParam));
	                	
	            }
	        }
  
		
	}
		
	
	
}
