package fr.paris.lutece.portal.util.mvc.binding.validate;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;

/**
 * Utility class for handling constraint violations in the context of MVC validation.
 * This class provides methods to extract metadata from constraint violations,
 * including annotations on properties and method parameters.
 * It is used to enhance error reporting and provide additional context
 * for validation errors in the MVC lutece framework.
 */
public class ConstraintViolations {

        private static final Logger _logger = MVCUtils.getLogger( );
 
	    private ConstraintViolations() {
	        // utility class
	    }
	    /**
	     * Extracts metadata from a ConstraintViolation, including annotations
	     * on the property or method parameter that caused the violation.
	     *
	     * @param violation the ConstraintViolation to extract metadata from
	     * @return a ConstraintViolationMetadata object containing the violation and its annotations
	     */
	    public static ConstraintViolationMetadata getMetadata(ConstraintViolation<?> violation) {

	        Annotation[] annotations = getAnnotations(violation);

	        return new ConstraintViolationMetadata(violation, annotations);

	    }

	    /**
	     * Retrieves the annotations associated with the property or method parameter
	     * that caused the given ConstraintViolation.
	     *
	     * @param violation the ConstraintViolation to analyze
	     * @return an array of annotations present on the property or method parameter
	     */
	    private static Annotation[] getAnnotations(ConstraintViolation<?> violation) {


	        // create a simple list of nodes from the path
	        List<Path.Node> nodes = new ArrayList<>();
	        for (Path.Node node : violation.getPropertyPath()) {
	            nodes.add(node);
	        }
	        Path.Node lastNode = nodes.get(nodes.size() - 1);

	        // the path refers to some property of the leaf bean
	        if (lastNode.getKind() == ElementKind.PROPERTY) {

	            Path.PropertyNode propertyNode = lastNode.as(Path.PropertyNode.class);
	            return getPropertyAnnotations(violation, propertyNode);

	        }

	        // The path refers to a method parameter
	        else if (lastNode.getKind() == ElementKind.PARAMETER && nodes.size() == 2) {

	            Path.MethodNode methodNode = nodes.get(0).as(Path.MethodNode.class);
	            Path.ParameterNode parameterNode = nodes.get(1).as(Path.ParameterNode.class);

	            return getParameterAnnotations(violation, methodNode, parameterNode);
	        }
	        _logger.warn("Could not read annotations for path: {}",  violation.getPropertyPath().toString());
	        return new Annotation[0];

	    }

	    /**
	     * Retrieves the annotations associated with the property node of the given ConstraintViolation.
	     *
	     * @param violation the ConstraintViolation to analyze
	     * @param node      the PropertyNode representing the property in the violation's path
	     * @return an array of annotations present on the property
	     */
	    private static Annotation[] getPropertyAnnotations(ConstraintViolation<?> violation, Path.PropertyNode node) {

	        Class<?> leafBeanClass = violation.getLeafBean().getClass();
	        Set<Annotation> allAnnotations = new HashSet<>();
	        try {

	            Field field = leafBeanClass.getDeclaredField(node.getName());
	            allAnnotations.addAll(Arrays.asList(field.getAnnotations()));

	        } catch (NoSuchFieldException e) {
	            // ignore for now
	        }

	        allAnnotations.addAll(readAndWriteMethodAnnotationsForField(leafBeanClass, node.getName()));

	        return allAnnotations.toArray(new Annotation[0]);
	    }
	    /* * Retrieves the annotations associated with the method parameter node of the given ConstraintViolation.
	     *
	     * @param violation     the ConstraintViolation to analyze
	     * @param methodNode    the MethodNode representing the method in the violation's path
	     * @param parameterNode the ParameterNode representing the parameter in the violation's path
	     * @return an array of annotations present on the method parameter
	     */	
	    private static Annotation[] getParameterAnnotations(ConstraintViolation<?> violation, Path.MethodNode methodNode,
	                                                        Path.ParameterNode parameterNode) {

	        try {

	            String methodName = methodNode.getName();

	            int paramCount = methodNode.getParameterTypes().size();
	            Class[] paramTypes = methodNode.getParameterTypes().toArray(new Class[paramCount]);

	            Class<?> rootBeanClass = violation.getRootBean().getClass();
	            Method method = rootBeanClass.getMethod(methodName, paramTypes);

	            int parameterIndex = parameterNode.getParameterIndex();
	            return method.getParameterAnnotations()[parameterIndex];

	        } catch (NoSuchMethodException e) {
	            throw new IllegalStateException(e);
	        }

	    }

	    /**
	     * Returns a set of all annotations present on the getter and setter methods
	     * for field fieldName in class beanClass. The bean class must be a valid
	     * java bean.
	     *
	     * @param beanClass the bean class
	     * @param fieldName the field in the bean class
	     * @return a set of all annotations on the read and write methods for the
	     * field, or an empty set if none are found
	     */
	    private static Set<Annotation> readAndWriteMethodAnnotationsForField(Class<?> beanClass, String fieldName) {
	        Set<Annotation> annotationsSet = new HashSet<>();

	        try {

	            BeanInfo info = Introspector.getBeanInfo(beanClass);

	            Optional<PropertyDescriptor> descriptorOpt = Arrays.stream(info.getPropertyDescriptors())
	                .filter(desc -> desc.getName().equals(fieldName))
	                .findFirst();

	            if(descriptorOpt.isPresent()) {

	                Method getter = descriptorOpt.get().getReadMethod();
	                if(getter != null) {
	                    annotationsSet.addAll(Arrays.asList(getter.getAnnotations()));
	                }

	                Method setter = descriptorOpt.get().getWriteMethod();
	                if(setter != null) {
	                    annotationsSet.addAll(Arrays.asList(setter.getAnnotations()));
	                }

	            }
	        }
	        catch(IntrospectionException e) {
	        	_logger.warn("Unable to introspect read and write methods for field '{}' on bean class '{}': {}",
	                    fieldName, beanClass.getName(), e.getMessage()
	                );
	        }

	        return annotationsSet;
	    }
}
