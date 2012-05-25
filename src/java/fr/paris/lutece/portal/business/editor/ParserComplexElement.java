package fr.paris.lutece.portal.business.editor;
/**
 * 
 * ParserComplexElementClass
 *
 */
public class ParserComplexElement {
	
	
    private String _strTagName;
    private String _strOpenSubstWithParam;
    private String _strCloseSubstWithParam;
    private String _strOpenSubstWithoutParam;
    private String _strCloseSubstWithoutParam;
    private String _strInternalSubst;
    private boolean _bProcessInternalTags;
    private boolean _bAcceptParam;
    private boolean _bRequiresQuotedParam;
    
    
    /**
     * 
     * @param strTagName
     * @param strOpenSubstWithParam
     * @param strCloseSubstWithParam
     * @param strOpenSubstWithoutParam
     * @param strCloseSubstWithoutParam
     * @param strInternalSubst
     * @param bProcessInternalTags
     * @param bAcceptParam
     * @param bRequiresQuotedParam
     */
    public ParserComplexElement(String strTagName,
			String strOpenSubstWithParam, String strCloseSubstWithParam,
			String strOpenSubstWithoutParam, String strCloseSubstWithoutParam,
			String strInternalSubst, boolean bProcessInternalTags,
			boolean bAcceptParam, boolean bRequiresQuotedParam) {
	
		_strTagName = strTagName;
		_strOpenSubstWithParam = strOpenSubstWithParam;
		_strCloseSubstWithParam = strCloseSubstWithParam;
		_strOpenSubstWithoutParam = strOpenSubstWithoutParam;
		_strCloseSubstWithoutParam = strCloseSubstWithoutParam;
		_strInternalSubst = strInternalSubst;
		_bProcessInternalTags = bProcessInternalTags;
		_bAcceptParam = bAcceptParam;
		_bRequiresQuotedParam = bRequiresQuotedParam;
	}
    
    /**
     * 
     * @return
     */
	public String getTagName() {
		return _strTagName;
	}
	/**
	 * 
	 * @param strTagName
	 */
	public void setTagName(String strTagName) {
		_strTagName = strTagName;
	}
	/**
	 * 
	 * @return
	 */
	public String getOpenSubstWithParam() {
		return _strOpenSubstWithParam;
	}
	/**
	 * 
	 * @param strOpenSubstWithParam
	 */
	public void setOpenSubstWithParam(String strOpenSubstWithParam) {
		_strOpenSubstWithParam = strOpenSubstWithParam;
	}
	/**
	 * 
	 * @return
	 */
	public String getCloseSubstWithParam() {
		return _strCloseSubstWithParam;
	}
	/**
	 * 
	 * @param strCloseSubstWithParam
	 */
	public void setCloseSubstWithParam(String strCloseSubstWithParam) {
		_strCloseSubstWithParam = strCloseSubstWithParam;
	}
	/**
	 * 
	 * @return
	 */
	public String getOpenSubstWithoutParam() {
		return _strOpenSubstWithoutParam;
	}
	/**
	 * 
	 * @param strOpenSubstWithoutParam
	 */
	public void setOpenSubstWithoutParam(String strOpenSubstWithoutParam) {
		_strOpenSubstWithoutParam = strOpenSubstWithoutParam;
	}
	/**
	 * 
	 * @return
	 */
	public String getCloseSubstWithoutParam() {
		return _strCloseSubstWithoutParam;
	}
	/**
	 * 
	 * @param strCloseSubstWithoutParam
	 */
	public void setCloseSubstWithoutParam(String strCloseSubstWithoutParam) {
		_strCloseSubstWithoutParam = strCloseSubstWithoutParam;
	}
	/**
	 * 
	 * @return
	 */
	public String getInternalSubst() {
		return _strInternalSubst;
	}
	/**
	 * 
	 * @param strInternalSubst
	 */
	public void setInternalSubst(String strInternalSubst) {
		_strInternalSubst = strInternalSubst;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isProcessInternalTags() {
		return _bProcessInternalTags;
	}
	/**
	 * 
	 * @param bProcessInternalTags
	 */
	public void setProcessInternalTags(boolean bProcessInternalTags) {
		_bProcessInternalTags = bProcessInternalTags;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isAcceptParam() {
		return _bAcceptParam;
	}
	/**
	 * 
	 * @param bAcceptParam
	 */
	public void setAcceptParam(boolean bAcceptParam) {
		_bAcceptParam = bAcceptParam;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isRequiresQuotedParam() {
		return _bRequiresQuotedParam;
	}
	/**
	 * 
	 * @param bRequiresQuotedParam
	 */
	public void setRequiresQuotedParam(boolean bRequiresQuotedParam) {
		_bRequiresQuotedParam = bRequiresQuotedParam;
	}
	

}
