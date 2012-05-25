package fr.paris.lutece.portal.business.editor;
/**
 * 
 * ParserElement Class
 *
 */
public class ParserElement {
	//the element to search
	private String _strCode;
	//the replacement value 
	private String _strValue;
	
	/**
	 * 
	 * @param strCode
	 * @param strValue
	 */
	public ParserElement(String strCode,String strValue)
	{
		_strCode=strCode;
		_strValue=strValue;
	}
	
	
	/**
	 * 
	 * @return code
	 */
	public String getCode() {
		return _strCode;
	}
	/**
	 * 
	 * @param strCode the code
	 */
	public void setCode(String strCode) {
		_strCode = strCode;
	}
	/**
	 * 
	 * @return the value
	 */
	public String getValue() {
		return _strValue;
	}
	/**
	 * 
	 * @param strValue the value
	 */
	public void setValue(String strValue) {
		_strValue = strValue;
	}
	

}
