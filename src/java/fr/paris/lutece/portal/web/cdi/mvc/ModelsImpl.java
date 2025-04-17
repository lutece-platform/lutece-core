package fr.paris.lutece.portal.web.cdi.mvc;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import jakarta.enterprise.context.RequestScoped;

/**
 * Implementation of {@link fr.paris.lutece.portal.web.cdi.mvc.Models} interface. A CDI class that delegates
 * to a {@link java.util.Map} implementation.
 *
 */
@RequestScoped
public class ModelsImpl implements Models {

	 private final Map<String, Object> map = new LinkedHashMap<>();

	    @Override
	    public Models put(String name, Object model) {
	        Objects.requireNonNull(name, "Name must not be null");
	        map.put(name, model);
	        return this;
	    }

	    @Override
	    public Object get(String name) {
	        return get(name, Object.class);
	    }

	    @Override
	    public <T> T get(String name, Class<T> type) {
	        Objects.requireNonNull(name, "Name must not be null");
	        Objects.requireNonNull(type, "Type must not be null");
	        return type.cast(map.get(name));
	    }

	    @Override
	    public Map<String, Object> asMap() {
	        return Collections.unmodifiableMap(new LinkedHashMap<>(map));
	    }

	    @Override
	    public Iterator<String> iterator() {
	        return map.keySet().iterator();
	    }
}
