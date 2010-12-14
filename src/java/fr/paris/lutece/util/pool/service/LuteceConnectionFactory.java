package fr.paris.lutece.util.pool.service;

import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * 
 * Factory to create LuteceConnection
 */
public class LuteceConnectionFactory 
{
	/**
	 * Supported interfaces for the proxy
	 */
	@SuppressWarnings( "rawtypes" )
	private static final Class[] PROXY_INTERFACES = {LuteceConnection.class};
	
	/**
	 * Builds a new proxied instance of the connection 
	 * so we can use {@link ConnectionPool} with third party libraries.
	 * @param pool the connection pool to use when closing connection
	 * @param connection the actual connection
	 * @return a proxy implementing {@link LuteceConnection}
	 */
	public static LuteceConnection newInstance( ConnectionPool pool, Connection connection )
	{
		return (LuteceConnection) Proxy.newProxyInstance( LuteceConnection.class.getClassLoader(), 
				PROXY_INTERFACES, new LuteceConnectionProxy( pool, connection ) );
	}
}
