package fr.paris.lutece.portal.service.cache;


import jakarta.enterprise.util.AnnotationLiteral;

/**
 * The {@code LuteceCacheLiteral} class is an implementation of the {@code LuteceCache} annotation
 * that is used in the context of dependency injection frameworks. This class provides a way to create
 * literal instances of the {@code LuteceCache} annotation which can be used for various purposes
 * such as programmatic configuration or injection in a framework that requires annotation literals.
 * 
 * <p>This class extends {@code AnnotationLiteral<LuteceCache>} and implements the {@code LuteceCache}
 * interface, allowing it to serve as a concrete representation of the {@code LuteceCache} annotation.</p>
 * 
 * <p>Constructor:</p>
 * <pre>
 * {@code
 * LuteceCacheLiteral(String cacheName, Class<?> keyType, Class<?> valueType, boolean enable)
 * }
 * </pre>
 * 
 * <p>Parameters:</p>
 * <ul>
 *     <li>{@code cacheName}: The name of the cache to be used. This is a required parameter.</li>
 *     <li>{@code keyType}: The class type of the cache keys. Defaults to {@code Object.class} if not specified.</li>
 *     <li>{@code valueType}: The class type of the cache values. Defaults to {@code Object.class} if not specified.</li>
 *     <li>{@code enable}: A boolean flag indicating whether the cache injection should be enabled. Defaults to {@code false}.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 *     <li>{@code cacheName()}: Returns the name of the cache.</li>
 *     <li>{@code keyType()}: Returns the class type of the cache keys.</li>
 *     <li>{@code valueType()}: Returns the class type of the cache values.</li>
 *     <li>{@code enable()}: Returns whether the cache injection is enabled.</li>
 * </ul>
 * 
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * LuteceCacheLiteral literal = new LuteceCacheLiteral("myCache", Integer.class, String.class, true);
 * String cacheName = literal.cacheName();  // "myCache"
 * Class<?> keyType = literal.keyType();    // Integer.class
 * Class<?> valueType = literal.valueType(); // String.class
 * boolean isEnabled = literal.enable();    // true
 * }
 * </pre>
 * 
 * <p>This class is typically used in environments where annotations need to be programmatically created
 * and used, such as in custom CDI (Contexts and Dependency Injection) implementations or configuration
 * setups.</p>
 */
public class LuteceCacheLiteral extends AnnotationLiteral<LuteceCache> implements LuteceCache {
	private final String cacheName;
    private final Class<?> keyType;
    private final Class<?> valueType;
    private final boolean enable;
    private final boolean preventGlobalReset;

    /**
     * Constructs a new {@code LuteceCacheLiteral} with the specified parameters.
     * 
     * @param cacheName the name of the cache
     * @param keyType the class type of the cache keys
     * @param valueType the class type of the cache values
     * @param enable whether cache injection is enabled
     * @param preventGlobalReset whether the cache can be reset by global resets
     */
    public LuteceCacheLiteral(String cacheName, Class<?> keyType, Class<?> valueType, boolean enable, boolean preventGlobalReset) {
        this.cacheName = cacheName;
        this.keyType = keyType;
        this.valueType = valueType;
        this.enable = enable;
        this.preventGlobalReset = preventGlobalReset;
    }

    @Override
    public String cacheName() {
        return cacheName;
    }

    @Override
    public Class<?> keyType() {
        return keyType;
    }

    @Override
    public Class<?> valueType() {
        return valueType;
    }

	@Override
	public boolean enable() {
		return enable;
	}

    @Override
    public boolean preventGlobalReset( )
    {
        return preventGlobalReset;
    }
}
