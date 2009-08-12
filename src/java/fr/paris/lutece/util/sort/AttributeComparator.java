package fr.paris.lutece.util.sort;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import fr.paris.lutece.portal.service.util.AppLogService;

public class AttributeComparator implements Comparator<Object>
{
	private String _strSortedAttribute;
	private boolean _bIsASC;
	
	/**
	 * Constructor
	 * @param strSortedAttribute the name of the attribute on which the sort will be made
	 * @param bIsAsc true for the ASC order, false for the DESC order
	 */
    public AttributeComparator( String strSortedAttribute, boolean bIsASC ) 
    {
        this._strSortedAttribute = strSortedAttribute;
        this._bIsASC = bIsASC;
	}
    
	/**
	 * Constructor
	 * @param strSortedAttribute the name of the attribute on which the sort will be made
	 * @param bIsAsc true for the ASC order, false for the DESC order
	 */
    public AttributeComparator( String strSortedAttribute ) 
    {
        this._strSortedAttribute = strSortedAttribute;
        this._bIsASC = true;
	}

    /**
     * Compare two objects o1 and o2.
     * @param o1 Object
     * @param o2 Object
     * @return < 0 if o1 is before o2 in the alphabetical order
     *           0 if o1 equals o2
     *         > 0 if o1 is after o2
     */
	public int compare(Object o1, Object o2) 
	{
		int nStatus = 0;
		
		Method method1 = getMethod( o1 );
		Method method2 = getMethod( o2 );
		
		if ( method1 != null && method2 != null && method1.getReturnType( ) == method2.getReturnType( ) )
		{			
			try 
			{
				String strReturnType = method1.getReturnType( ).getName().toString();
				if ( strReturnType.equals( "java.lang.String" ) )
				{
					nStatus = ( (String) method1.invoke( o1 ) ).toLowerCase().compareTo( 
						((String) method2.invoke( o2 ) ).toLowerCase() );
				}
				else if ( strReturnType.equals( "int" ) )
				{
					nStatus = ( (Integer) method1.invoke( o1 ) ).compareTo( 
							(Integer) method2.invoke( o2 ) );
				}
			} 
			catch ( IllegalArgumentException e ) 
			{
				AppLogService.error( e );
			} 
			catch ( IllegalAccessException e ) 
			{
				AppLogService.error( e );
			} 
			catch ( InvocationTargetException e ) 
			{
				AppLogService.error( e );
			}
		}
		if ( !_bIsASC )
		{
			nStatus = nStatus * (-1);
		}
		return nStatus;
	}
	
    /**
     * Return the getter method of the object obj for the attribute _strSortedAttribute
     * @param obj the object
     * @return method Method of the object obj for the attribute _strSortedAttribute
     */
    private Method getMethod (Object obj)
    {
      Method method = null;
      String strFirstLetter = _strSortedAttribute.substring( 0, 1 ).toUpperCase();
      
      String strMethodName = "get" + strFirstLetter + _strSortedAttribute.substring( 1, _strSortedAttribute.length() );

     try 
     {
    	 method = obj.getClass( ).getMethod( strMethodName );
     }
     catch(Exception e)
     {
    	AppLogService.error( e );
     }
      return method;
   }
}