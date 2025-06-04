package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.util.bean.BeanUtil;
import jakarta.inject.Inject;

public abstract class AbstractParameterResolver implements ParameterResolver {
	private static final String PACKAGE = "org.apache.commons.beanutils.converters.";
    private static final String DEFAULT_CONFIG_MSG =
            "(N.B. Converters can be configured to use default values to avoid throwing exceptions)";
    /**
     * Should we return the default value on conversion errors?
     */
    private static boolean useDefault = true;
    /**
     * The default value specified to our Constructor, if any.
     */
    private static Object defaultValue = null;
    /**
     * The Logger for the MVC lutece framework.
	 */
    protected static final Logger _logger = MVCUtils.getLogger();

    @Inject
	protected BindingResultImpl bindingResult;
	
    
 // Constants for logging messages
    protected static final String LOG_RESOLVING_PARAMETER = "Resolving parameter: '{}' of type: '{}'";
    protected static final String LOG_NO_VALUES = "No values found for parameter '{}'";
    protected static final String LOG_REQUIRED_MISSING = "Required parameter '{}' of type '{}' is missing";
    protected static final String LOG_DEFAULT_VALUE = "Using default value '{}' for parameter '{}'";
    protected static final String LOG_TYPE_DEFAULT = "Returning type default value for parameter '{}': {}";
    protected static final String LOG_FOUND_VALUES = "Found {} values for parameter '{}': {}";
    protected static final String LOG_ARRAY_PARAM = "Resolving array parameter '{}' with component type '{}'";
    protected static final String LOG_COLLECTION_PARAM = "Resolving collection parameter '{}' of type '{}'";
    protected static final String LOG_MAP_PARAM = "Resolving map parameter '{}' of type '{}'";
    protected static final String LOG_SINGLE_VALUE = "Converting single value for parameter '{}'";
    protected static final String LOG_CONVERT_FAIL = "Failed to convert '{}' to '{}' for {}";
    protected static final String LOG_INSTANTIATION_FAIL = "Could not instantiate {} type '{}', falling back to default implementation";
    protected static final String LOG_CONVERT_VALUE = "Converting value [{}] '{}' to type '{}'";
    protected static final String LOG_CONVERT_SUCCESS = "Successfully converted to: {}";
    protected static final String LOG_DEFAULT_INSTEAD = "Using default value instead: {}";
    
    /**
     * Converts a raw value to the target type.
     * 
     * @param rawValue    the string value to convert
     * @param targetType  the type to convert to
     * @return the converted result
     */
    protected  <T> ResolverResult<T>  convertValue(String rawValue, Class<T> targetType, String paramName) {
    	
    	ConvertUtilsBean convertUtils = BeanUtil.getEnhancedBean(LocalVariables.getLocale( )).getConvertUtils( );
    	try {
    		T result= (T) convertUtils.convert(rawValue, targetType);
        	return ResolverResult.success(result);
    	}catch(ConversionException e) {
			_logger.debug("Error converting value '{}' to type '{}': {}", rawValue, targetType.getName(), e.getMessage(), e);
            T defaultValue = handleError(targetType, rawValue, e);
			return ResolverResult.failed(defaultValue, new BindingErrorImpl(paramName, e.getMessage(), rawValue));
		}
    }
    /**
     * Resolves an array parameter from a collection of string values.
     * 
     * @param parameter  the method parameter metadata
     * @param rawValues  the raw string values
     * @param paramName  the name of the parameter
     * @return a properly typed array with converted values
     */
	protected <T> ResolverResult<T> resolveArrayParameter(Parameter parameter, String[] rawValues, String paramName) {
	    Class<?> componentType = parameter.getType().getComponentType();
	    List<Object> convertedValues = new ArrayList<>();
	    List<BindingError> errors = new ArrayList<>();
	
	    _logger.debug("Processing {} raw values for array parameter '{}'", rawValues.length, paramName);
	
	    for (int i = 0; i < rawValues.length; i++) {
	        String rawValue = rawValues[i];
	        
	            _logger.debug(LOG_CONVERT_VALUE, i, rawValue, componentType.getName());
	            ResolverResult<?> result  = convertValue(rawValue, componentType, paramName);
	            if (result.getErrors() == null) {
		            convertedValues.add(result.getValue());
	                _logger.debug(LOG_CONVERT_SUCCESS, result.getValue());
	            }else {
	                _logger.warn(LOG_CONVERT_FAIL, rawValue, componentType.getName(), "array parameter '" + paramName + "'");
					errors.addAll(result.getErrors());
				} 
	        
	    }
	    @SuppressWarnings("unchecked")
		T resultArray = (T) createArrayFromValues(componentType, convertedValues);
	
	    return errors.isEmpty() ? 
	    		ResolverResult.success(resultArray) : 
	    			ResolverResult.failed(resultArray, errors); 
	}
	/**
     * Creates an array from a list of converted values.
     * 
     * @param componentType   the component type of the array
     * @param convertedValues the list of converted values
     * @return an array containing the values
     */
    protected Object createArrayFromValues(Class<?> componentType, List<Object> convertedValues) {
        // Create array of the proper component type and size
        Object resultArray = java.lang.reflect.Array.newInstance(componentType, convertedValues.size());
        
        // Copy elements to the array
        for (int i = 0; i < convertedValues.size(); i++) {
            java.lang.reflect.Array.set(resultArray, i, convertedValues.get(i));
        }
        
        _logger.debug("Created array of type '{}' with {} elements",
                     componentType.getName(), convertedValues.size());
        return resultArray;
    }
    /**
     * Resolves a collection parameter from string values.
     * 
     * @param parameter  the method parameter metadata
     * @param rawValues  the raw string values
     * @param paramName  the name of the parameter
     * @return a collection of converted values
     */
    @SuppressWarnings("unchecked")
	protected <T> ResolverResult<T> resolveCollectionParameter(Parameter parameter, String[] rawValues, String paramName) {
        Class<?> elementType = getCollectionElementType(parameter);
        Collection<Object> collection = createCollectionInstance(parameter.getType());
        List<BindingError> errors = new ArrayList<>();

        _logger.debug("Processing {} raw values for collection parameter '{}'", rawValues.length, paramName);

        for (int i = 0; i < rawValues.length; i++) {
            String rawValue = rawValues[i];
                _logger.debug(LOG_CONVERT_VALUE, i, rawValue, elementType.getName());
                ResolverResult<?> result = convertValue(rawValue, elementType, paramName);
                if (result.getErrors() == null) {
                    collection.add(result.getValue());
                    _logger.debug(LOG_CONVERT_SUCCESS, result.getValue());
                } else {
                    _logger.warn(LOG_CONVERT_FAIL, rawValue, elementType.getName(), "collection");
					errors.addAll(result.getErrors());
                }    
        }
        return errors.isEmpty() ? 
        		ResolverResult.success((T)collection) : 
        			ResolverResult.failed((T)collection, errors);
    }
    
    /**
     * Determines the component type of a collection parameter.
     * This method extracts the generic type information from a collection parameter
     * to determine what type of elements it should contain.
     *
     * @param parameter The method parameter representing a collection
     * @return The class representing the component type of the collection
     */
    protected Class<?> getCollectionElementType(Parameter parameter) {
        Type type = parameter.getParameterizedType();
        
        // Handle ParameterizedType (like List<Integer>)
        if (type instanceof ParameterizedType) {
            Type[] args = ((ParameterizedType) type).getActualTypeArguments();
            if (args.length > 0) {
                Type arg = args[0];
                
                // Direct Class reference
                if (arg instanceof Class) {
                    return (Class<?>) arg;
                }
                // Handle TypeVariable (like T in List<T>)
                else if (arg instanceof TypeVariable) {
                    // Try to resolve the actual type if possible
                    // This is more complex and might require class hierarchy analysis
                    TypeVariable<?> typeVar = (TypeVariable<?>) arg;
                    // For simple cases, we can check bounds
                    Type[] bounds = typeVar.getBounds();
                    if (bounds.length > 0 && bounds[0] instanceof Class) {
                        return (Class<?>) bounds[0];
                    }
                }
                // Handle WildcardType (like ? in List<? extends Number>)
                else if (arg instanceof WildcardType) {
                    WildcardType wildcardType = (WildcardType) arg;
                    Type[] upperBounds = wildcardType.getUpperBounds();
                    if (upperBounds.length > 0 && upperBounds[0] instanceof Class) {
                        return (Class<?>) upperBounds[0];
                    }
                }
                // Handle ParameterizedType (like List<List<String>>)
                else if (arg instanceof ParameterizedType) {
                    ParameterizedType parameterizedArg = (ParameterizedType) arg;
                    return (Class<?>) parameterizedArg.getRawType();
                }
            }
        }
        // If type is a Class, it might be a raw type (like List without generics)
        else if (type instanceof Class) {
            // For raw types, try to infer from generic superclass or interfaces
            // This is a best-effort approach
            Class<?> clazz = (Class<?>) type;
            if (Collection.class.isAssignableFrom(clazz)) {
                // Check if there's any type information in the class hierarchy
                Type genericSuperclass = clazz.getGenericSuperclass();
                if (genericSuperclass instanceof ParameterizedType) {
                    ParameterizedType paramType = (ParameterizedType) genericSuperclass;
                    Type[] args = paramType.getActualTypeArguments();
                    if (args.length > 0 && args[0] instanceof Class) {
                        return (Class<?>) args[0];
                    }
                }
            }
        }
        
        // Default to Object.class which is more appropriate than String.class
        // as it won't cause unnecessary type conversions
        return Object.class;
    }
    /**
     * Creates an instance of a Collection based on the specified type.
     * 
     * @param collectionType the class representing the collection type
     * @return an instance of the collection
     */
    @SuppressWarnings("unchecked")
    protected Collection<Object> createCollectionInstance(Class<?> collectionType) {
        if (!collectionType.isInterface()) {
            try {
                return (Collection<Object>) collectionType.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(LOG_INSTANTIATION_FAIL, "collection", collectionType.getName());
                }
            }
        }
        return getCollectionImplementation(collectionType);
    }
    
    /**
     * Gets an appropriate collection implementation for the given collection interface.
     * 
     * @param collectionType the collection interface class
     * @return an appropriate implementation instance
     */
    protected Collection<Object> getCollectionImplementation(Class<?> collectionType) {
        if (List.class.isAssignableFrom(collectionType)) return new ArrayList<>();
        if (SortedSet.class.isAssignableFrom(collectionType)) return new TreeSet<>();
        if (Set.class.isAssignableFrom(collectionType)) return new HashSet<>();
        if (Queue.class.isAssignableFrom(collectionType)) return new LinkedList<>();
        if (Deque.class.isAssignableFrom(collectionType)) return new ArrayDeque<>();
        
        // Default to ArrayList if no specific match
        return new ArrayList<>();
    }
    
    /**
     * Creates an instance of a Map based on the specified type.
     * 
     * @param mapType the class representing the map type
     * @return an instance of the map
     */
    @SuppressWarnings("unchecked")
    protected Map<Object, Object> createMapInstance(Class<?> mapType) {
        if (!mapType.isInterface()) {
            try {
                return (Map<Object, Object>) mapType.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                if (_logger.isDebugEnabled()) {
                    _logger.debug(LOG_INSTANTIATION_FAIL, "map", mapType.getName());
                }
            }
        }
        return getMapImplementation(mapType);
    }
    
    /**
     * Gets an appropriate map implementation for the given map interface.
     * 
     * @param mapType the map interface class
     * @return an appropriate implementation instance
     */
    protected Map<Object, Object> getMapImplementation(Class<?> mapType) {
        if (SortedMap.class.isAssignableFrom(mapType)) return new TreeMap<>();
        if (ConcurrentMap.class.isAssignableFrom(mapType)) return new ConcurrentHashMap<>();
        if (LinkedHashMap.class.isAssignableFrom(mapType)) return new LinkedHashMap<>();
        
        // Default to HashMap if no specific match
        return new HashMap<>();
    }
    
    /**
     * Determines the key and value types of a Map based on the generic parameter metadata.
     * 
     * @param parameter the method parameter to inspect
     * @return an array where index 0 is the key type and index 1 is the value type
     */
    protected Class<?>[] getMapKeyValueTypes(Parameter parameter) {
        Type type = parameter.getParameterizedType();
        Class<?> keyType = String.class;
        Class<?> valueType = String.class;
        
        if (type instanceof ParameterizedType) {
            Type[] args = ((ParameterizedType) type).getActualTypeArguments();
            if (args.length > 1) {
                if (args[0] instanceof Class) keyType = (Class<?>) args[0];
                if (args[1] instanceof Class) valueType = (Class<?>) args[1];
            }
        }
        
        return new Class<?>[]{ keyType, valueType };
    }
    protected   <T> ResolverResult<T> getDefaultValue(Class<T> type) {
	       /* if (type == boolean.class || type == Boolean.class) return false;
	        if (type == int.class || type == Integer.class) return 0;
	        if (type == long.class || type == Long.class) return 0L;
	        if (type == double.class || type == Double.class) return 0.0;
	        if (type == float.class || type == Float.class) return 0f;
	        if (type == short.class || type == Short.class) return (short) 0;
	        if (type == byte.class || type == Byte.class) return (byte) 0;
	        if (type == char.class || type == Character.class) return '\u0000';
	        if (type == String.class) return "";
	        if (type == java.math.BigDecimal.class) return java.math.BigDecimal.ZERO;
	        if (type == java.math.BigInteger.class) return java.math.BigInteger.ZERO;
	        if (type == java.util.Date.class) return new java.util.Date(0);
	        if (type == java.sql.Date.class) return new java.sql.Date(0);
	        if (type == java.sql.Timestamp.class) return new java.sql.Timestamp(0);
	        if (type == java.sql.Time.class) return new java.sql.Time(0);
	        if (type == java.util.Calendar.class) return java.util.Calendar.getInstance();
	        if (type == java.util.UUID.class) return java.util.UUID.randomUUID();
	        if (type.isArray()) {
	            Class<?> componentType = type.getComponentType();
	            return java.lang.reflect.Array.newInstance(componentType, 0);
	        }
	        if (java.util.List.class.isAssignableFrom(type)) return new java.util.ArrayList<>();
	        if (java.util.Set.class.isAssignableFrom(type)) return new java.util.HashSet<>();
	        if (java.util.Map.class.isAssignableFrom(type)) return new java.util.HashMap<>();
	        return null; 
	        */
	    	Converter converter = ConvertUtils.lookup( type );
	        if (converter == null) {
	            return null;
	        }
	        return  ResolverResult.success(converter.convert(type, null));
	    }
	    /**
	     * Handle Conversion Errors.
	     * <p>
	     * If a default value has been specified then it is returned
	     * otherwise a ConversionException is thrown.
	     *
	     * @param <T> Target type of the conversion.
	     * @param type Data type to which this value should be converted.
	     * @param value The input value to be converted
	     * @param cause The exception thrown by the <code>convert</code> method
	     * @return The default value.
	     * @throws ConversionException if no default value has been
	     * specified for this {@link Converter}.
	     */
	    protected  <T> T handleError(final Class<T> type, final Object value, final Throwable cause) {
	    	if (_logger.isDebugEnabled()) {
	            if (cause instanceof ConversionException) {
	            	_logger.debug(" Conversion threw ConversionException: {}", cause.getMessage());
	            } else {
	            	_logger.debug(" Conversion threw {}",  cause);
	            }
	        }

	        if (useDefault) {
	            //bindingResult.addBindingError(new BindingErrorImpl(paramName, cause.getMessage(), value.toString( )));
	            return handleMissing(type);
	        }

	        ConversionException cex = null;
	        if (cause instanceof ConversionException) {
	            cex = (ConversionException)cause;
	            if (_logger.isDebugEnabled()) {
	            	_logger.debug("    Re-throwing ConversionException: {}", cex.getMessage());
	            }
	        } else {
	            final String msg = "Error converting from '" + toString(value.getClass()) +
	                    "' to '" + toString(type) + "' " + cause.getMessage();
	            cex = new ConversionException(msg, cause);
	            if (_logger.isDebugEnabled()) {
	            	_logger.debug("    Throwing ConversionException: {}",  msg);
	            	_logger.debug("    {}",  DEFAULT_CONFIG_MSG);
	            }
	        }

	        throw cex;
	    }
	    /**
	     * Handle missing values.
	     * <p>
	     * If a default value has been specified, then it is returned (after a cast
	     * to the desired target class); otherwise a ConversionException is thrown.
	     *
	     * @param <T> the desired target type
	     * @param type Data type to which this value should be converted.
	     * @return The default value.
	     * @throws ConversionException if no default value has been
	     * specified for this {@link Converter}.
	     */
	    protected <T> T handleMissing(final Class<T> type) {

	        if (useDefault || type.equals(String.class)) {
	            Object value = getDefaultValue(type).getValue( );
	            
	            if (_logger.isDebugEnabled()) {
	            	_logger.debug("    Using default {} value {}",
	                        (value == null ? "" : toString(value.getClass())), 
	                         defaultValue );
	            }
	            // value is now either null or of the desired target type
	           // return type.cast(value);
	            return (T) value;
	        }

	        final ConversionException cex =  new ConversionException("No value specified for '" +
	                toString(type) + "'");
	        if (_logger.isDebugEnabled()) {
	        	_logger.debug("    Throwing ConversionException: " + cex.getMessage());
	        	_logger.debug("    " + DEFAULT_CONFIG_MSG);
	        }
	        throw cex;

	    }
	    /**
	     * Provide a String representation of a <code>java.lang.Class</code>.
	     * @param type The <code>java.lang.Class</code>.
	     * @return The String representation.
	     */
	    String toString(final Class<?> type) {
	        String typeName = null;
	        if (type == null) {
	            typeName = "null";
	        } else if (type.isArray()) {
	            Class<?> elementType = type.getComponentType();
	            int count = 1;
	            while (elementType.isArray()) {
	                elementType = elementType .getComponentType();
	                count++;
	            }
	            typeName = elementType.getName();
	            for (int i = 0; i < count; i++) {
	                typeName += "[]";
	            }
	        } else {
	            typeName = type.getName();
	        }
	        if (typeName.startsWith("java.lang.") ||
	            typeName.startsWith("java.util.") ||
	            typeName.startsWith("java.math.")) {
	            typeName = typeName.substring("java.lang.".length());
	        } else if (typeName.startsWith(PACKAGE)) {
	            typeName = typeName.substring(PACKAGE.length());
	        }
	        return typeName;
	    }
}
