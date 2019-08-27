package fr.paris.lutece.portal.business.event;

/**
 * IEventParam
 * @param <T> type of param.
 */
public interface IEventParam<T> {
	
	/**
	 * Get the value of the parameter.
	 * @return
	 */
	T getValue( );
}
