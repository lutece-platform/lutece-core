package fr.paris.lutece.portal.util.mvc.binding;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.paris.lutece.portal.util.mvc.binding.validate.ValidationError;
import jakarta.enterprise.inject.Vetoed;

/**
 * Default implementation of the {@link BindingResult} interface.
 * This class is used to collect and manage binding and validation errors
 * that occur during the processing of MVC requests in the Lutece framework.
 * It provides methods to check for errors, retrieve error messages, and access
 * detailed information about binding and validation errors.
 */
@Vetoed // produced by BindingResultManager
public class BindingResultImpl implements BindingResult {

		private final Set<BindingError> bindingErrors = new LinkedHashSet<>();

	    private final Set<ValidationError> validationErrors = new LinkedHashSet<>();

	    private boolean consumed;

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public boolean isFailed() {
	        this.consumed = true;
	        return validationErrors.size() > 0 || bindingErrors.size() > 0;
	    }
	    /**
	    * {@inheritDoc}
	    */
	    @Override
	    public List<String> getAllMessages() {
	        this.consumed = true;
	        return Stream.concat(bindingErrors.stream(), validationErrors.stream())
	                .map( ParamError::getMessage )
	                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
	    }
	    /**
	    * {@inheritDoc}
	    */
	    @Override
	    public Set<ParamError> getAllErrors() {
	        this.consumed = true;
	        return Stream.concat(bindingErrors.stream(), validationErrors.stream())
	                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
	    }
	    /**
	    * {@inheritDoc}
	    */
	    @Override
	    public Set<ParamError> getErrors(String param) {
	        Objects.requireNonNull(param, "Parameter name is required");
	        this.consumed = true;
	        return Stream.concat(bindingErrors.stream(), validationErrors.stream())
	                .filter(paramError -> Objects.equals(paramError.getParamName(), param))
	                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
	    }
	    
	    /**
	     * Adds a binding error to the result.
	     */
	    public void addValidationErrors(Set<ValidationError> validationErrors) {
	        this.validationErrors.addAll(validationErrors);
	    }
	    /**
	     * Adds a single validation error to the result.
	     */
	    public void addBindingError(BindingError bindingError) {
	        this.bindingErrors.add(bindingError);
	    }
	    /**
	     * Adds a list of binding errors to the result.
	     */
	    public void addBindingError(List<BindingError> listbindingError) {
	        this.bindingErrors.addAll(listbindingError);
	    }
	   
	    /** 
	    * Check if has Unconsumed Errors
	    */
	    public boolean hasUnconsumedErrors() {
	        return !consumed && (!bindingErrors.isEmpty() || !validationErrors.isEmpty());
	    }
}
