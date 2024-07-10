package fr.paris.lutece.util;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.microprofile.config.spi.ConfigSource;

import fr.paris.lutece.portal.service.util.AppLogService;

public class LuteceConfigSource implements ConfigSource{

	@Override
    public int getOrdinal() {
        return 100;
    }
	
	@Override
	public String getName() {

		return LuteceConfigSource.class.getSimpleName( );
	}

	@Override
    public Set<String> getPropertyNames( )
    {
        Set<String> propertyNames = new HashSet<String>( );
        try
        {
            propertyNames = AppInitPropertiesService.getPropertiesAsMap( ).keySet( );
        }
        catch( Exception e )
        {
            // This shouldn't happen. It happens only if Config API impl is calling getPropertyNames before CDI AppInitExtension (WildFly)
            AppLogService.error( "LuteceConfigSource initialization error, due to Config API calling getPropertyNames before CDI AppInitExtension", e );
        }
        return propertyNames;
    }

	@Override
    public String getValue( String strProperty )
    {
        String strValue = null;
        try
        {
            strValue = AppInitPropertiesService.getProperty( strProperty );
        }
        catch( Exception e )
        {
            // This shouldn't happen. It happens only if Config API impl is calling getPropertyNames before CDI AppInitExtension (WildFly)
            AppLogService.error(
                    "LuteceConfigSource initialization error, due to Config API calling getPropertyNames and so getValue before CDI AppInitExtension", e );
        }
        return strValue;
    }

}
