package fr.paris.lutece.util.pool.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * 
 * LuteceConnectionProxy : proxy implementation of {@link LuteceConnection}
 * This class should be removed when java5 support is dropped.
 *
 */
public class LuteceConnectionProxy implements InvocationHandler 
{
	/**
	 * Close method name
	 */
	private static final String METHOD_CLOSE = "close";
	/**
	 * Close connection method name (from {@link LuteceConnection}
	 */
	private static final String METHOD_CLOSE_CONNECTION = "closeConnection";
	
	private ConnectionPool _pool;
	private Connection _connection;
	
	/**
	 * Private constructor
	 * @param pool the pool to use
	 * @param connection the actual connection
	 */
	LuteceConnectionProxy( ConnectionPool pool, Connection connection )
	{
		_pool = pool;
		_connection = connection;
	}
	
	
	/**
	 * Catches close() and closeConnection() method.
	 * close() calls _pool.freeConnection, and closeConnection() calls _connection.close().
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable 
	{
		Object oReturn;
		
		if ( METHOD_CLOSE.equals( method.getName() ) )
		{
			_pool.freeConnection( (Connection) proxy );
			oReturn = null;
		} 
		else if ( METHOD_CLOSE_CONNECTION.equals( method.getName() ) )
		{
			_connection.close();
			oReturn = null;
		}
		else
		{
			oReturn = method.invoke( _connection, args );
		}
		
		return oReturn;
	}
}
