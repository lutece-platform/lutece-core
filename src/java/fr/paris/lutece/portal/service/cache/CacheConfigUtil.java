package fr.paris.lutece.portal.service.cache;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.EternalExpiryPolicy;
import javax.cache.expiry.TouchedExpiryPolicy;
import javax.cache.spi.CachingProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;


/**
 * Utility class for configuring and managing cache settings.
 */
public class CacheConfigUtil {
	
		public static final String CACHE_LOGGER_NAME = "lutece.cache";

	    private static final String ENABLED = "1";
		private static final String DISABLED = "0";
		private static final String PROPERTY_IS_ENABLED = ".enabled";


     	private static final String HAZELCAST_PROVIDER = "com.hazelcast.cache.HazelcastCachingProvider";
	    private static final String HAZELCAST_MEMBER_PROVIDER = "com.hazelcast.cache.HazelcastMemberCachingProvider";
	    private static final String HAZELCAST_SERVER_IMPL_PROVIDER = "com.hazelcast.cache.impl.HazelcastServerCachingProvider";
	    private static final String HAZELCAST_CLIENT_PROVIDER = "com.hazelcast.client.cache.HazelcastClientCachingProvider";
	    private static final String HAZELCAST_CLIENT_IMPL_PROVIDER = "com.hazelcast.client.cache.impl.HazelcastClientCachingProvider";

	    private static final String INFINISPAN_EMBEDDED_PROVIDER = "org.infinispan.jcache.embedded.JCachingProvider";
	    private static final String INFINISPAN_REMOTE_PROVIDER = "org.infinispan.jcache.remote.JCachingProvider";

	    private final static String EHCAHE_JSR107_CACHING_PROVIDER="org.ehcache.jsr107.EhcacheCachingProvider";
	    private static final String KEY_PREFIX = "core.cache.status.";
	    private static final String PREFIX_DEFAULT = "lutece.cache.default";
	    
	    private static final String PROPERTY_MAX_ELEMENTS = ".maxElementsInMemory";
	    private static final String PROPERTY_ETERNAL = ".eternal";
	    private static final String PROPERTY_TIME_TO_IDLE = ".timeToIdleSeconds";
	    private static final String PROPERTY_TIME_TO_LIVE = ".timeToLiveSeconds";
	    private static final String PROPERTY_OFF_HEAP_MB = ".offheap";
	    private static final String PROPERTY_MAX_ELEMENTS_DISK = ".maxElementsOnDisk";
	    private static final String PROPERTY_STATISTICS = ".statistics";
	    private static final String PROPERTY_ENABLEMANAGEMENT = ".enableManagement";
	    private static final String PROPERTY_PERSISTENCEDIRECTORY =".persistenceDirectory";
	    private static final String [ ] CONFIG_PROPERTIES = { PROPERTY_MAX_ELEMENTS, PROPERTY_ETERNAL, PROPERTY_TIME_TO_IDLE, PROPERTY_TIME_TO_LIVE,
	    		PROPERTY_OFF_HEAP_MB, PROPERTY_MAX_ELEMENTS_DISK, PROPERTY_STATISTICS, PROPERTY_ENABLEMANAGEMENT };
	    private static final String TMPDIR = "java.io.tmpdir";
	    private static final int DEFAULT_HEAP_ENTRIES = 10000;
	    /** Value of a numeric setting that is not configured */
	    static final int UNSET = -1;
	    
	    private static final Logger logger = LogManager.getLogger(CacheConfigUtil.CACHE_LOGGER_NAME);
	    // Unknown settings already reported
	    private static final Set<String> _setWarnedKeys = ConcurrentHashMap.newKeySet( );

	    /**
	     * Pre-configures a cache manager with the given URI, caching provider, and properties.
	     *
	     * @param uriValue        The URI value for the cache configuration.
	     * @param cachingProvider The caching provider.
	     * @param properties      The properties to configure the cache.
	     * @return The configured URI.
	     * @throws IOException If an I/O error occurs.
	     */
	    public static URI preConfigureCacheManager(String uriValue, CachingProvider cachingProvider, Properties properties) throws IOException {
		    
	    	logger.info("Pre-configuring cache manager with URI: {} and caching provider: {}", uriValue, cachingProvider.getClass().getName());
	        URI uriToReturn = null;
	        final URI configuredUri;
	        if (uriValue != null && !uriValue.isEmpty()) {
	            try {
	                configuredUri = new URI(uriValue);           
	            } catch (URISyntaxException e) {
	                logger.error("Incorrect URI syntax: {}", uriValue, e);
	                throw new IllegalArgumentException("INCORRECT_URI_SYNTAX", e);
	            }
	        } else {
	            configuredUri = null;
	        }
	
	        switch (cachingProvider.getClass().getName()) {
	        	case EHCAHE_JSR107_CACHING_PROVIDER:
	        		uriToReturn =(configuredUri==null)?generateOrUpdateEhCacheConfig():configuredUri;
	        		break;
	        	case HAZELCAST_PROVIDER:
	            case HAZELCAST_MEMBER_PROVIDER:
	            case HAZELCAST_SERVER_IMPL_PROVIDER:
	            case HAZELCAST_CLIENT_PROVIDER:
	            case HAZELCAST_CLIENT_IMPL_PROVIDER:
	                if (uriValue != null) {
	                    properties.setProperty("hazelcast.config.location", uriValue);
	                    //uriToReturn= configuredUri;
	                }
	                break;
	
	            case INFINISPAN_EMBEDDED_PROVIDER:
	            case INFINISPAN_REMOTE_PROVIDER:
	                uriToReturn = configuredUri;
	                break;
	
	            default:
	                uriToReturn = configuredUri;
	                break;
	         }
	         logger.info("Configured URI: {}", uriToReturn);
	         return uriToReturn;
       }	    
	    /**
	     * Generates or updates the Ehcache configuration.
	     *
	     * @return The URI of the generated or updated Ehcache configuration.
	     * @throws IOException If an I/O error occurs.
	     */ 
	    public static URI generateOrUpdateEhCacheConfig() throws IOException {
	        logger.info("Generating or updating Ehcache configuration");
	    	return createTempEhcacheConfigFile(getCahesConfigs(), loadDefaults());
	    }
	    /**
	     * Creates a temporary Ehcache configuration file.
	     *
	     * @param cacheConfigs       The list of cache configurations.
	     * @param defaultCacheConfig The default cache configuration.
	     * @return The URI of the created configuration file.
	     * @throws IOException If an I/O error occurs.
	     */
	    public static URI createTempEhcacheConfigFile(List<CacheConfig> cacheConfigs, CacheConfig defaultCacheConfig) throws IOException {
	        logger.info("Creating temporary Ehcache configuration file");
	    	Path ehcacheXmlPath = Files.createTempFile("ehcache", ".xml");
	    	boolean bDisk = defaultCacheConfig.getDiskMB( ) > 0 || cacheConfigs.stream( ).anyMatch( c -> c.getDiskMB( ) > 0 );
	        try (FileWriter writer = new FileWriter(ehcacheXmlPath.toFile())) {
	            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
	                    "<config xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n" +
	                    "        xmlns='http://www.ehcache.org/v3'\n" +
	                    "        xmlns:jsr107='http://www.ehcache.org/v3/jsr107'\n" +
	                    "        xsi:schemaLocation=\"\n" +
	                    "            http://www.ehcache.org/v3 http://www.ehcache.org/schema/ehcache-core.xsd\n" +
	                    "            http://www.ehcache.org/v3/jsr107 https://www.ehcache.org/schema/ehcache-107-ext.xsd\">\n" +
	                    "\n" +
	                    "    <service>\n" +
	                    "        <jsr107:defaults default-template=\""+escape( defaultCacheConfig.getTemplateName())+"\">\n");

	            // Add cache configurations for each cache config
	            for (CacheConfig cacheConfig : cacheConfigs) {
	                writer.write("            <jsr107:cache name=\"" + escape( cacheConfig.getName()) + "\" template=\"" + escape( cacheConfig.getTemplateName()) + "\"/>\n");
	            }

	            writer.write("        </jsr107:defaults>\n" +
	                    "    </service>\n");
	            if ( bDisk ) {
	            	writer.write( "    <persistence directory=\""+escape( resolveDirectory( defaultCacheConfig.getPersistenceDirectory()))+ "\" />\n" );
	            }
	            writeTemplate( writer, defaultCacheConfig );
	            // A cache configured by name gets the default settings it does not override
	            for (CacheConfig cacheConfig : cacheConfigs) {
	                writeTemplate( writer, cacheConfig.completedBy( defaultCacheConfig ) );
	            }
	            writer.write("</config>\n");
	            logger.info("Ehcache configuration file created at: {}", ehcacheXmlPath);
	        }catch (IOException e) {
	            logger.error("Error while writing Ehcache configuration file", e);
	            throw e;
	        }
	        return ehcacheXmlPath.toUri();
	    }

	    /**
	     * Writes the Ehcache template of a complete cache configuration
	     *
	     * @param writer the writer of the configuration file
	     * @param cacheConfig the configuration, every setting resolved
	     * @throws IOException If an I/O error occurs.
	     */
	    private static void writeTemplate( FileWriter writer, CacheConfig cacheConfig ) throws IOException
	    {
	    	StringBuilder sbTemplate = new StringBuilder( "    <cache-template name=\"" ).append( escape( cacheConfig.getTemplateName( ) ) ).append( "\">\n" );
	    	sbTemplate.append( "        <expiry>" );
	    	if ( cacheConfig.isEternal( ) || ( cacheConfig.getTimeToLiveSeconds( ) <= 0 && cacheConfig.getTimeToIdleSeconds( ) <= 0 ) )
	    	{
	    		sbTemplate.append( "<none/>" );
	    	}
	    	else if ( cacheConfig.getTimeToLiveSeconds( ) > 0 )
	    	{
	    		sbTemplate.append( "<ttl unit=\"seconds\">" ).append( cacheConfig.getTimeToLiveSeconds( ) ).append( "</ttl>" );
	    	}
	    	else
	    	{
	    		sbTemplate.append( "<tti unit=\"seconds\">" ).append( cacheConfig.getTimeToIdleSeconds( ) ).append( "</tti>" );
	    	}
	    	sbTemplate.append( "</expiry>\n        <resources>\n" );
	    	// A heap tier is mandatory: an unbounded cache is not allowed
	    	sbTemplate.append( "            <heap unit=\"entries\">" ).append( cacheConfig.getHeapEntries( ) > 0 ? cacheConfig.getHeapEntries( ) : DEFAULT_HEAP_ENTRIES ).append( "</heap>\n" );
	    	if ( cacheConfig.getOffheapMB( ) > 0 )
	    	{
	    		sbTemplate.append( "            <offheap unit=\"MB\">" ).append( cacheConfig.getOffheapMB( ) ).append( "</offheap>\n" );
	    	}
	    	if ( cacheConfig.getDiskMB( ) > 0 )
	    	{
	    		sbTemplate.append( "            <disk unit=\"MB\" persistent=\"true\">" ).append( cacheConfig.getDiskMB( ) ).append( "</disk>\n" );
	    	}
	    	sbTemplate.append( "        </resources>\n" );
	    	sbTemplate.append( "        <jsr107:mbeans enable-management=\"" ).append( cacheConfig.isEnableManagement( ) ).append( "\" enable-statistics=\"" ).append( cacheConfig.isStatistics( ) ).append( "\"/>\n" );
	    	sbTemplate.append( "    </cache-template>\n" );
	    	writer.write( sbTemplate.toString( ) );
	    }

	    /**
	     * Resolves a persistence directory starting with java.io.tmpdir to the temporary directory of the JVM
	     *
	     * @param strDirectory the configured directory
	     * @return the directory
	     */
	    static String resolveDirectory( String strDirectory )
	    {
	    	if ( strDirectory != null && strDirectory.startsWith( TMPDIR ) )
	    	{
	    		return Path.of( System.getProperty( TMPDIR ), strDirectory.substring( TMPDIR.length( ) ) ).toString( );
	    	}
	    	return strDirectory;
	    }

	    /**
	     * Escapes a value written in an XML attribute
	     *
	     * @param strValue the value
	     * @return the escaped value
	     */
	    private static String escape( String strValue )
	    {
	    	return strValue.replace( "&", "&amp;" ).replace( "\"", "&quot;" ).replace( "<", "&lt;" ).replace( ">", "&gt;" );
	    }

	    /**
	     * Retrieves cache configurations.
	     *
	     * @return A list of cache configurations.
	     */
	    public static  List<CacheConfig> getCahesConfigs(){	    	
	    	return buildCacheConfigs( getConfigPropertiesWithPrefix());
	    }
	    /**
	     * Builds the cache configuration map.
	     *
	     * @return A map of cache configurations.
	     */
	    public static Map<String, String> getConfigPropertiesWithPrefix() {
	        Config config = ConfigProvider.getConfig();
	        Set<String> propertyNamesWithPrefix  = StreamSupport.stream(config.getPropertyNames().spliterator(), false)
	                .filter(propertyName -> propertyName.startsWith(KEY_PREFIX))
	                .collect(Collectors.toSet());
	        Map<String, String> configPropertiesWithPrefix = new HashMap<>();
	        propertyNamesWithPrefix.forEach(propertyName -> {
	            String propertyValue = config.getOptionalValue(propertyName, String.class)
	                    .orElse("");
	            configPropertiesWithPrefix.put(propertyName, propertyValue);
	        });
	        DatastoreService.getDataByPrefix(KEY_PREFIX).forEach(refItem ->{
	        	configPropertiesWithPrefix.put(refItem.getCode(), refItem.getName());
	        });
	        
	        return configPropertiesWithPrefix;
	    }
	    /**
	     * Builds a list of cache configurations from the given properties map.
	     *
	     * @param properties A map of properties where keys represent cache configuration parameters.
	     * @return A list of {@link CacheConfig} objects configured according to the given properties.
	     */
	    public static List<CacheConfig> buildCacheConfigs(Map<String, String> properties) {
	      
	    	List<CacheConfig> cacheConfigs = new ArrayList<>();
	        Set<String> prefixes = determineCaheName(properties.keySet());

	        for (String prefix : prefixes) {
	        	
	        	String Keyprefix= KEY_PREFIX+prefix;
	        	CacheConfig cacheConfig =new CacheConfig.ConfigBuilder(prefix)
    			.heapEntries(intValue(properties, Keyprefix + PROPERTY_MAX_ELEMENTS))
    			.offheapMB(intValue(properties, Keyprefix + PROPERTY_OFF_HEAP_MB))
    			.diskMB(intValue(properties, Keyprefix + PROPERTY_MAX_ELEMENTS_DISK))
    		    .timeToLiveSeconds(intValue(properties, Keyprefix + PROPERTY_TIME_TO_LIVE))
                .timeToIdleSeconds(intValue(properties, Keyprefix + PROPERTY_TIME_TO_IDLE))
                .eternal(booleanValue(properties, Keyprefix + PROPERTY_ETERNAL))
                .statistics(booleanValue(properties, Keyprefix + PROPERTY_STATISTICS))
                .enableManagement(booleanValue(properties, Keyprefix + PROPERTY_ENABLEMANAGEMENT))
                .build();
	                  
	            cacheConfigs.add(cacheConfig);
	        }
	        return cacheConfigs;
	    }
		/**
		 * Determines the unique cache name prefixes from a set of property keys.
		 *
		 * @param keys A set of property keys.
		 * @return A set of unique cache name prefixes extracted from the keys.
		 */
	    public static Set<String> determineCaheName(Set<String> keys) {
	        Set<String> prefixes = new HashSet<>();
            int startIndex = KEY_PREFIX.length();
	        for (String key : keys) {
	            if (key.startsWith(KEY_PREFIX)) {
	            	boolean bKnown = key.endsWith( PROPERTY_IS_ENABLED );
	            	for ( String strProperty : CONFIG_PROPERTIES )
	            	{
	            		if ( key.endsWith( strProperty ) && key.length( ) > startIndex + strProperty.length( ) )
	            		{
	            			prefixes.add( key.substring( startIndex, key.length( ) - strProperty.length( ) ) );
	            			bKnown = true;
	            		}
	            	}
	            	if ( !bKnown && _setWarnedKeys.add( key ) )
	            	{
	            		// Ehcache 2 settings (overflowToDisk, diskPersistent...) have no effect
	            		logger.warn( "Unknown cache setting ignored: {}", key );
	            	}
	            }
	        }
	        return prefixes;
	    }

	    /**
	     * Reads an integer setting
	     *
	     * @param properties the settings
	     * @param strKey the key
	     * @return the value, or UNSET when the setting is absent or not a number
	     */
	    private static int intValue( Map<String, String> properties, String strKey )
	    {
	    	String strValue = properties.get( strKey );
	    	if ( strValue == null || strValue.isBlank( ) )
	    	{
	    		return UNSET;
	    	}
	    	try
	    	{
	    		return Integer.parseInt( strValue.trim( ) );
	    	}
	    	catch( NumberFormatException e )
	    	{
	    		logger.warn( "The cache setting {} is not a number: {}", strKey, strValue );
	    		return UNSET;
	    	}
	    }

	    /**
	     * Reads a boolean setting
	     *
	     * @param properties the settings
	     * @param strKey the key
	     * @return the value, or null when the setting is absent
	     */
	    private static Boolean booleanValue( Map<String, String> properties, String strKey )
	    {
	    	String strValue = properties.get( strKey );
	    	return ( strValue == null || strValue.isBlank( ) ) ? null : Boolean.valueOf( strValue.trim( ) );
	    }

	    /**
	     * Loads the default cache configuration.
	     *
	     * @return The default cache configuration.
	     */
	    private static CacheConfig loadDefaults( )
	    {
	    	Config config = ConfigProvider.getConfig();
	    	return new CacheConfig.ConfigBuilder("luteceCache")
	    			.heapEntries(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_MAX_ELEMENTS, Integer.class).orElse(DEFAULT_HEAP_ENTRIES))
	    			.offheapMB(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_OFF_HEAP_MB,Integer.class).orElse(0))
	    			.diskMB(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_MAX_ELEMENTS_DISK, Integer.class).orElse(0))
	    		    .timeToLiveSeconds(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_TIME_TO_LIVE, Integer.class).orElse(1000))
                    .timeToIdleSeconds(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_TIME_TO_IDLE, Integer.class).orElse(1000))
                    .eternal(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_ETERNAL, Boolean.class).orElse(false))
                    .statistics(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_STATISTICS, Boolean.class).orElse(false))
                    .enableManagement(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_ENABLEMANAGEMENT, Boolean.class).orElse(false))
                    .persistenceDirectory(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_PERSISTENCEDIRECTORY, String.class).orElse(TMPDIR + "/ehcache/"))
                    .build();
	    			
	    }
	    // Cache configuration class
	    static class CacheConfig {
	        private String name;
	        private String templateName;
	        private int heapEntries;
	        private int timeToLiveSeconds;
	        private int timeToIdleSeconds;
	        private Boolean eternal;
	        private int offheapMB;
	        private int diskMB;
	        private Boolean statistics;
	        private Boolean enableManagement;
	        private String persistenceDirectory;

			public CacheConfig(ConfigBuilder builder) {
	            this.name = builder.name;
	            this.templateName = builder.templateName;
	            this.heapEntries = builder.heapEntries;
	            this.offheapMB = builder.offheapMB;
	            this.diskMB = builder.diskMB;
	            this.timeToLiveSeconds = builder.timeToLiveSeconds;
	            this.timeToIdleSeconds = builder.timeToIdleSeconds;
	            this.eternal = builder.eternal;
	            this.statistics = builder.statistics;
	            this.enableManagement = builder.enableManagement;
	            this.persistenceDirectory= builder.persistenceDirectory;
	        }

	        /**
	         * Returns this configuration with every setting it leaves unset taken from another one. The expiry is taken
	         * as a whole: a cache that sets its time to live, time to idle or eternal flag does not inherit the others.
	         *
	         * @param defaults the configuration that provides the missing settings
	         * @return the complete configuration
	         */
	        public CacheConfig completedBy( CacheConfig defaults )
	        {
	        	boolean bOwnExpiry = eternal != null || timeToLiveSeconds != UNSET || timeToIdleSeconds != UNSET;
	        	return new ConfigBuilder( name )
	        			.heapEntries( heapEntries != UNSET ? heapEntries : defaults.heapEntries )
	        			.offheapMB( offheapMB != UNSET ? offheapMB : defaults.offheapMB )
	        			.diskMB( diskMB != UNSET ? diskMB : defaults.diskMB )
	        			.timeToLiveSeconds( bOwnExpiry ? timeToLiveSeconds : defaults.timeToLiveSeconds )
	        			.timeToIdleSeconds( bOwnExpiry ? timeToIdleSeconds : defaults.timeToIdleSeconds )
	        			.eternal( bOwnExpiry ? Boolean.TRUE.equals( eternal ) : defaults.isEternal( ) )
	        			.statistics( statistics != null ? statistics : defaults.isStatistics( ) )
	        			.enableManagement( enableManagement != null ? enableManagement : defaults.isEnableManagement( ) )
	        			.persistenceDirectory( persistenceDirectory != null ? persistenceDirectory : defaults.persistenceDirectory )
	        			.build( );
	        }

	        public static class ConfigBuilder {
	            // Required parameters
	            private String name;
	            private String templateName;

	            // Optional parameters - unset until given
	            private int heapEntries = UNSET;
	            private int timeToLiveSeconds = UNSET;
	            private int timeToIdleSeconds = UNSET;
	            private Boolean eternal;
	            private int offheapMB = UNSET;
	            private int diskMB = UNSET;
	            private Boolean statistics;
	            private Boolean enableManagement;
	            private String persistenceDirectory;

	            public ConfigBuilder(String name) {
	                this.name = name;
	                this.templateName = name+"_template";
	            }

	            public ConfigBuilder heapEntries(int heapEntries) {
	                this.heapEntries = heapEntries;
	                return this;
	            }

	            public ConfigBuilder timeToLiveSeconds(int timeToLiveSeconds) {
	                this.timeToLiveSeconds = timeToLiveSeconds;
	                return this;
	            }

	            public ConfigBuilder timeToIdleSeconds(int timeToIdleSeconds) {
	                this.timeToIdleSeconds = timeToIdleSeconds;
	                return this;
	            }

	            public ConfigBuilder eternal(Boolean eternal) {
	                this.eternal = eternal;
	                return this;
	            }

	            public ConfigBuilder offheapMB(int offheapMB) {
	                this.offheapMB = offheapMB;
	                return this;
	            }

	            public ConfigBuilder diskMB(int diskMB) {
	                this.diskMB = diskMB;
	                return this;
	            }

	            public ConfigBuilder statistics(Boolean statistics) {
	                this.statistics = statistics;
	                return this;
	            }
	            public ConfigBuilder enableManagement(Boolean enableManagement) {
	                this.enableManagement = enableManagement;
	                return this;
	            }
				public ConfigBuilder persistenceDirectory(String persistenceDirectory) {
					this.persistenceDirectory = persistenceDirectory;
					return this;
				}
	            public CacheConfig build() {
	                return new CacheConfig(this);
	            }
	        }
	        public String getName() {
	            return name;
	        }

	        public String getTemplateName() {
	            return templateName;
	        }

	        public int getHeapEntries() {
	            return heapEntries;
	        }


	        public int getTimeToLiveSeconds() {
	            return timeToLiveSeconds;
	        }

	        public int getTimeToIdleSeconds() {
	            return timeToIdleSeconds;
	        }

	        public int getOffheapMB() {
	            return offheapMB;
	        }

	        public int getDiskMB() {
	            return diskMB;
	        }

	        public boolean isStatistics() {
	            return Boolean.TRUE.equals( statistics );
	        }
	        public boolean isEnableManagement() {
	            return Boolean.TRUE.equals( enableManagement );
	        }
	        public boolean isEternal() {
	            return Boolean.TRUE.equals( eternal );
	        }
	        public boolean isExpiry() {
	            return isEternal( ) || timeToLiveSeconds > 0 || timeToIdleSeconds > 0;
	        }
	        public boolean isResource() {
	            return offheapMB >0 || diskMB > 0 || heapEntries > 0;
	        }
	        public String getPersistenceDirectory() {
				return persistenceDirectory;
			}

	        /**
	         * Returns the public view of this configuration
	         *
	         * @return the settings
	         */
	        public CacheSettings toSettings( )
	        {
	        	return new CacheSettings( heapEntries, timeToLiveSeconds, timeToIdleSeconds, isEternal( ), isStatistics( ), isEnableManagement( ) );
	        }

	    }
	    /**
	     * Returns the name under which a cache is created in the JCache manager: the cache name without spaces, the
	     * name its settings are read under.
	     *
	     * @param strCacheName the name of the cacheable service
	     * @return the JCache name
	     */
	    public static String getJCacheName( String strCacheName )
	    {
	    	return normalizeName( strCacheName );
	    }

	    /**
	     * Returns the settings of a cache: its own settings, completed by the default ones
	     *
	     * @param strCacheName the cache name
	     * @return the complete settings
	     */
	    static CacheConfig getCacheSettings( String strCacheName )
	    {
	    	String strName = normalizeName( strCacheName );
	    	CacheConfig defaults = loadDefaults( );
	    	return getCahesConfigs( ).stream( ).filter( c -> c.getName( ).equals( strName ) ).findFirst( )
	    			.map( c -> c.completedBy( defaults ) ).orElse( defaults );
	    }

	    /**
	     * Completes the JCache configuration of a cache with the expiry of its Lutece settings, so that a provider
	     * without a Lutece generated configuration (Hazelcast, Infinispan) expires the entries too. Ehcache templates
	     * override this expiry with the same settings. A configuration that sets its own expiry is kept. Immutable values
	     * are stored by reference.
	     *
	     * @param strCacheName the cache name
	     * @param configuration the configuration given by the cacheable service
	     * @return the configuration to create the cache with
	     */
	    @SuppressWarnings( "unchecked" )
	    public static <K, V> Configuration<K, V> completeConfiguration( String strCacheName, Configuration<K, V> configuration )
	    {
	    	if ( !( configuration instanceof CompleteConfiguration ) )
	    	{
	    		return configuration;
	    	}
	    	MutableConfiguration<K, V> mutable = new MutableConfiguration<>( (CompleteConfiguration<K, V>) configuration );
	    	Class<V> valueType = mutable.getValueType( );
	    	if ( String.class.equals( valueType ) || Number.class.isAssignableFrom( valueType ) || Boolean.class.equals( valueType ) )
	    	{
	    		// An immutable value needs no copy on each read and write
	    		mutable.setStoreByValue( false );
	    	}
	    	if ( EternalExpiryPolicy.factoryOf( ).equals( mutable.getExpiryPolicyFactory( ) ) )
	    	{
	    		CacheConfig settings = getCacheSettings( strCacheName );
	    		if ( !settings.isEternal( ) && settings.getTimeToLiveSeconds( ) > 0 )
	    		{
	    			mutable.setExpiryPolicyFactory( CreatedExpiryPolicy.factoryOf( new Duration( TimeUnit.SECONDS, settings.getTimeToLiveSeconds( ) ) ) );
	    		}
	    		else if ( !settings.isEternal( ) && settings.getTimeToIdleSeconds( ) > 0 )
	    		{
	    			mutable.setExpiryPolicyFactory( TouchedExpiryPolicy.factoryOf( new Duration( TimeUnit.SECONDS, settings.getTimeToIdleSeconds( ) ) ) );
	    		}
	    	}
	    	return mutable;
	    }

	    /**
	     * Lets the CDI beans implementing {@link ICacheConfigurationCustomizer} adapt the configuration of a cache to the
	     * caching provider, for the settings that JCache cannot carry. A customizer that fails is skipped and logged.
	     *
	     * @param strCacheName
	     *            the cache name
	     * @param configuration
	     *            the configuration built by the core
	     * @param provider
	     *            the caching provider of the cache manager
	     * @return the configuration to create the cache with
	     */
	    public static <K, V> Configuration<K, V> customizeConfiguration( String strCacheName, Configuration<K, V> configuration, CachingProvider provider )
	    {
	    	Instance<ICacheConfigurationCustomizer> customizers = CDI.current( ).select( ICacheConfigurationCustomizer.class );
	    	if ( customizers.isUnsatisfied( ) )
	    	{
	    		return configuration;
	    	}
	    	String strJCacheName = getJCacheName( strCacheName );
	    	CacheSettings settings = getCacheSettings( strCacheName ).toSettings( );
	    	Configuration<K, V> result = configuration;
	    	for ( ICacheConfigurationCustomizer customizer : customizers )
	    	{
	    		if ( customizer.supports( provider ) )
	    		{
	    			try
	    			{
	    				result = customizer.customize( strJCacheName, result, settings );
	    				logger.debug( "Configuration of the cache {} customized by {}", strJCacheName, customizer.getClass( ).getName( ) );
	    			}
	    			catch( RuntimeException | LinkageError e )
	    			{
	    				logger.warn( "The cache configuration customizer {} failed for the cache {}: configuration kept", customizer.getClass( ).getName( ),
	    						strJCacheName, e );
	    			}
	    		}
	    	}
	    	return result;
	    }

	    /**
	     * Tells whether a cache is shared by several instances of the webapp (any provider but Ehcache)
	     *
	     * @param cache the cache
	     * @return true when the cache is not local to this instance
	     */
	    public static boolean isDistributed( javax.cache.Cache<?, ?> cache )
	    {
	    	return !cache.getCacheManager( ).getCachingProvider( ).getClass( ).getName( ).startsWith( "org.ehcache." );
	    }

	    /**
	     * Returns the cache status froms database
	     *
	     * @param cs
	     *            The cacheable service
	     * @return The status
	     */
	    public static boolean getStatusFromDataBase( String cacheName )
	    {
	        String strEnabled = DatastoreService.getInstanceDataValue( CacheConfigUtil.getDSKey( cacheName, PROPERTY_IS_ENABLED ), DISABLED );
	        return strEnabled.equals( ENABLED );
	    }

	    /**
	     * Returns the status of the cache saved in the database, or the given default when no status is saved yet (a
	     * cache created for the first time). A status chosen in the back office is thus kept across restarts.
	     *
	     * @param cacheName
	     *            the cache name
	     * @param bDefault
	     *            the status of a cache that has no saved status
	     * @return true if the cache is enabled
	     */
	    public static boolean getStatusFromDataBase( String cacheName, boolean bDefault )
	    {
	        String strEnabled = DatastoreService.getInstanceDataValue( CacheConfigUtil.getDSKey( cacheName, PROPERTY_IS_ENABLED ), null );
	        return ( strEnabled == null ) ? bDefault : strEnabled.equals( ENABLED );
	    }
	    /**
	     * Return the key of a datastore property
	     * 
	     * @param strCacheName
	     *            The cacheable service
	     * @param strProperty
	     *            The property
	     * @return The DS key
	     */
	    public static String getDSKey( String strCacheName, String strProperty )
	    {
	        return KEY_PREFIX + normalizeName( strCacheName ) + strProperty;
	    }
	    
	    /**
	     * Normalize name (remove spaces)
	     *
	     * @param strName
	     *            The name to normalize
	     * @return The normalized name
	     */
	    private static String normalizeName( String strName )
	    {
	        return strName.replace( " ", "" );
	    }
	    /**
	     * Returns cache config
	     *
	     * @param cache
	     *            The cache
	     * @return Cache infos
	     */
	     public static <K,V>String getInfos( Configuration<K,V> conf )
	    {
	    	 if( conf == null) {
	    		 return new StringBuilder("The cache is desabled").toString();
	    	 }
	        StringBuilder sbInfos = new StringBuilder( conf.toString()).append( "\n" );
	           
	        sbInfos.append( "key type" ).append( "=" ).append( conf.getKeyType( ) ).append( "\n" );
	        sbInfos.append( "Value type" ).append( "=" ).append( conf.getValueType( ) ).append( "\n" );
	        sbInfos.append( "Whether storeByValue (true) or storeByReference (false)" ).append( "=" ).append( conf.isStoreByValue( ) ).append( "\n" );
	        logger.info("cache config info {}", sbInfos.toString( ));	            
	        return sbInfos.toString( );
	    }
}
