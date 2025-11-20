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

import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import fr.paris.lutece.portal.service.datastore.DatastoreService;


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
	    
	    private static final Logger logger = LogManager.getLogger(CacheConfigUtil.CACHE_LOGGER_NAME);

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
	                    "        <jsr107:defaults default-template=\""+defaultCacheConfig.getTemplateName()+"\">\n");

	            // Add cache configurations for each cache config
	            for (CacheConfig cacheConfig : cacheConfigs) {
	                writer.write("            <jsr107:cache name=\"" + cacheConfig.getName() + "\" template=\"" + cacheConfig.getTemplateName() + "\"/>\n");
	            }

	            writer.write("        </jsr107:defaults>\n" +
	                    "    </service>\n");
	            writer.write( "<persistence directory=\""+defaultCacheConfig.getPersistenceDirectory()+ "\" />\n"+
	            		
	            		"    <cache-template name=\"" + defaultCacheConfig.getTemplateName() + "\">\n" +
                        "        <expiry>\n" +
                        
                        (defaultCacheConfig.isEternal( ) ?
                        		"<none/>": "")+
                        (defaultCacheConfig.getTimeToLiveSeconds( ) > 0 ?
                        		"<ttl unit=\"seconds\">" + defaultCacheConfig.getTimeToLiveSeconds() + "</ttl>\n": "")+
                        (defaultCacheConfig.getTimeToLiveSeconds( ) == 0 && defaultCacheConfig.getTimeToIdleSeconds( ) > 0 ?
                        		"<tti unit=\"seconds\">" + defaultCacheConfig.getTimeToIdleSeconds() + "</tti>\n" : "") +
                        "        </expiry>\n" +
                        "        <resources>\n" +
                        (defaultCacheConfig.getHeapEntries() >0 ? "<heap unit=\"entries\">" + defaultCacheConfig.getHeapEntries() + "</heap>\n" : "") +
                        (defaultCacheConfig.getOffheapMB() > 0 ? "<offheap unit=\"MB\">" + defaultCacheConfig.getOffheapMB() + "</offheap>\n" : "") +
                        (defaultCacheConfig.getDiskMB() > 0 ? "<disk unit=\"MB\" persistent=\"true\">" + defaultCacheConfig.getDiskMB() + "</disk>\n" : "") +
                        "        </resources>\n" +
                        "        <jsr107:mbeans enable-management=\"" + defaultCacheConfig.isEnableManagement() + "\"  enable-statistics=\"" + defaultCacheConfig.isStatistics() + "\"/>\n"+
                        "    </cache-template>\n");
	            // Write cache templates
	            for (CacheConfig cacheConfig : cacheConfigs) {
	                writer.write("    <cache-template name=\"" + cacheConfig.getTemplateName() + "\">\n" +
	                		(cacheConfig.isExpiry( ) ?"<expiry>\n":"" )+
	                        (cacheConfig.isEternal( ) ?
	                        		"<none/>": "")+
	                        (cacheConfig.getTimeToLiveSeconds( ) > 0 ?
	                        		"<ttl unit=\"seconds\">" + cacheConfig.getTimeToLiveSeconds() + "</ttl>\n": "")+
	                        (cacheConfig.getTimeToLiveSeconds( ) == 0 && cacheConfig.getTimeToIdleSeconds( ) > 0 ?
	                        		"<tti unit=\"seconds\">" + cacheConfig.getTimeToIdleSeconds() + "</tti>\n" : "") +
	                        (cacheConfig.isExpiry( ) ?"</expiry>\n":"" )+
	                        (cacheConfig.isResource( ) ?"<resources>\n":"") +
	                        (cacheConfig.getHeapEntries() >0 ? "<heap unit=\"entries\">" + cacheConfig.getHeapEntries() + "</heap>\n" : "") +
	                        (cacheConfig.getOffheapMB() > 0 ? "<offheap unit=\"MB\">" + cacheConfig.getOffheapMB() + "</offheap>\n" : "") +
	                        (cacheConfig.getDiskMB() > 0 ? "<disk unit=\"MB\" persistent=\"true\">" + cacheConfig.getDiskMB() + "</disk>\n" : "") +
	                        (cacheConfig.isResource( ) ?"</resources>\n":"") +
	                        ((cacheConfig.isStatistics( ) || cacheConfig.isEnableManagement( )) ?"<jsr107:mbeans enable-management=\"" + cacheConfig.isEnableManagement() + "\"  enable-statistics=\"" + cacheConfig.isStatistics() + "\"/>\n":"")+
	                        "    </cache-template>\n");
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
    			.heapEntries(Optional.ofNullable(properties.get( Keyprefix+ PROPERTY_MAX_ELEMENTS))
                        .map(Integer::parseInt)
                        .orElse(0))
    			.offheapMB(Optional.ofNullable(properties.get(Keyprefix + PROPERTY_OFF_HEAP_MB))
                        .map(Integer::parseInt)
                        .orElse(0))
    			.diskMB(Optional.ofNullable(properties.get(Keyprefix + PROPERTY_MAX_ELEMENTS_DISK))
                        .map(Integer::parseInt)
                        .orElse(0))
    		    .timeToLiveSeconds(Optional.ofNullable(properties.get(Keyprefix + PROPERTY_TIME_TO_LIVE))
                        .map(Integer::parseInt)
                        .orElse(0))
                .timeToIdleSeconds(Optional.ofNullable(properties.get(Keyprefix + PROPERTY_TIME_TO_IDLE))
                        .map(Integer::parseInt)
                        .orElse(0))
                .eternal(Optional.ofNullable(properties.get(Keyprefix + PROPERTY_ETERNAL))
                        .map(Boolean::parseBoolean)
                        .orElse(false))
                .statistics(Optional.ofNullable(properties.get(Keyprefix + PROPERTY_STATISTICS))
                        .map(Boolean::parseBoolean)
                        .orElse(false))
                .enableManagement(Optional.ofNullable(properties.get(Keyprefix + PROPERTY_ENABLEMANAGEMENT))
                        .map(Boolean::parseBoolean)
                        .orElse(false))
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
	            if (key.startsWith(KEY_PREFIX) && !key.contains("enabled")) {
	                int endIndex = key.indexOf('.', startIndex);
	                if (endIndex != -1) {
	                	prefixes.add(key.substring(startIndex, endIndex));
	                }
	            }
	        }
	        return prefixes;
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
	    			.heapEntries(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_MAX_ELEMENTS, Integer.class).orElse(10000))
	    			.offheapMB(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_OFF_HEAP_MB,Integer.class).orElse(0))
	    			.diskMB(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_MAX_ELEMENTS_DISK, Integer.class).orElse(0))
	    		    .timeToLiveSeconds(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_TIME_TO_LIVE, Integer.class).orElse(1000))
                    .timeToIdleSeconds(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_TIME_TO_IDLE, Integer.class).orElse(1000))
                    .eternal(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_ETERNAL, Boolean.class).orElse(false))
                    .statistics(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_STATISTICS, Boolean.class).orElse(false))
                    .enableManagement(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_ENABLEMANAGEMENT, Boolean.class).orElse(false))
                    .persistenceDirectory(config.getOptionalValue(PREFIX_DEFAULT + PROPERTY_PERSISTENCEDIRECTORY, String.class).orElse("java.io.tmpdir/ehcache/"))
                    .build();
	    			
	    }
	    // Cache configuration class
	    static class CacheConfig {
	        private String name;
	        private String templateName;
	        private int heapEntries;
	        private int timeToLiveSeconds;
	        private int timeToIdleSeconds;
	        private boolean eternal;
	        private int offheapMB;
	        private int diskMB;
	        private boolean statistics;
	        private boolean enableManagement;
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

	        public static class ConfigBuilder {
	            // Required parameters
	            private String name;
	            private String templateName;

	            // Optional parameters - initialized to default values
	            private int heapEntries;
	            private int timeToLiveSeconds;
	            private int timeToIdleSeconds;
	            private boolean eternal;
	            private int offheapMB;
	            private int diskMB;
	            private boolean statistics;
	            private boolean enableManagement;
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

	            public ConfigBuilder eternal(boolean eternal) {
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

	            public ConfigBuilder statistics(boolean statistics) {
	                this.statistics = statistics;
	                return this;
	            }
	            public ConfigBuilder enableManagement(boolean enableManagement) {
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
	            return statistics;
	        }
	        public boolean isEnableManagement() {
	            return enableManagement;
	        }
	        public boolean isEternal() {
	            return eternal;
	        }
	        public boolean isExpiry() {
	            return eternal || timeToLiveSeconds > 0 || timeToIdleSeconds > 0;
	        }
	        public boolean isResource() {
	            return offheapMB >0 || diskMB > 0 || heapEntries > 0;
	        }
	        public String getPersistenceDirectory() {
				return persistenceDirectory;
			}

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
