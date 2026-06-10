package fr.paris.lutece.util;
import java.util.Set;

import org.eclipse.microprofile.config.spi.ConfigSource;

import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * MicroProfile {@code ConfigSource} exposing the Lutece override configuration, that is the
 * {@code .properties} files located under the {@code override/} and {@code override/plugins} directories.
 *
 * <p>
 * This source declares a higher ordinal than {@link LuteceConfigSource} so that the override values take
 * precedence over the base ones when MicroProfile Config resolves a property.
 * </p>
 */
public class LuteceOverrideConfigSource implements ConfigSource{

    private static final String PATH_CONF = "WEB-INF/conf/";

	public LuteceOverrideConfigSource ( ){
    	AppInitPropertiesService.init(PATH_CONF);
	}
	@Override
    public int getOrdinal() {
        return 250;
    }

	@Override
	public String getName() {

		return LuteceOverrideConfigSource.class.getSimpleName( );
	}

	@Override
    public Set<String> getPropertyNames( )
    {
      return AppInitPropertiesService.getOverridePropertiesName( );
    }

	@Override
    public String getValue( String strProperty )
    {
        String strValue = null;
        try
        {
            strValue = AppInitPropertiesService.getOverrideProperty( strProperty );
        }
        catch( Exception e )
        {
            // This shouldn't happen. It happens only if Config API impl is calling getPropertyNames before CDI AppInitExtension (WildFly)
            AppLogService.error(
                    "LuteceOverrideConfigSource initialization error, due to Config API calling getPropertyNames and so getValue before CDI AppInitExtension", e );
        }
        return strValue;
    }
}
