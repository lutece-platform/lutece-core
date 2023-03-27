package fr.paris.lutece.util;
import java.util.Set;

import org.eclipse.microprofile.config.spi.ConfigSource;

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
	public Set<String> getPropertyNames() {
		
		return AppInitPropertiesService.getPropertiesAsMap().keySet();
	}

	@Override
	public String getValue(String strProperty) {
		
		return AppInitPropertiesService.getProperty(strProperty);
	}

}
