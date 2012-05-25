package fr.paris.lutece.portal.service.editor;

/**
 * 
 * This Interface Provides a parser BBCODE
 *
 */
public interface IEditorBbcodeService {

	/**
	 * Parse BBCODE text and return HTML text
	 * @param strValue the value of the text
	 * @return HTML Text
	 */
	String parse(String strValue);
	
}
