package fr.paris.lutece.portal.util.mvc.utils;

import java.lang.reflect.Parameter;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.util.mvc.commons.annotations.CookieValue;
import fr.paris.lutece.portal.util.mvc.commons.annotations.ModelAttribute;
import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestHeader;
import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestParam;

public class BindingUtils {
    	
	/**
     * Détermine le nom effectif à utiliser pour un paramètre en tenant compte des annotations
     * 
     * @param parameter Le paramètre à analyser
     * @return Le nom effectif du paramètre à utiliser dans l'application
     */
    public static String getEffectiveParameterName(Parameter parameter) {
        // Priorité à @RequestParam
        if (parameter.isAnnotationPresent(RequestParam.class)) {
            RequestParam annotation = parameter.getAnnotation(RequestParam.class);
            return annotation.value().isEmpty() ? parameter.getName() : annotation.value();
        }
     // Priorité à @RequestHeader
        if (parameter.isAnnotationPresent(RequestHeader.class)) {
        	RequestHeader annotation = parameter.getAnnotation(RequestHeader.class);
            return annotation.value().isEmpty() ? parameter.getName() : annotation.value();
        }
        // Puis @ModelAttribute
        if (parameter.isAnnotationPresent(ModelAttribute.class)) {
            ModelAttribute annotation = parameter.getAnnotation(ModelAttribute.class);
            return annotation.value().isEmpty() ? parameter.getName() : annotation.value();
        }
        // Puis @CookieValue
        if (parameter.isAnnotationPresent(CookieValue.class)) {
        	CookieValue annotation = parameter.getAnnotation(CookieValue.class);
            return annotation.value().isEmpty() ? parameter.getName() : annotation.value();
        }
        // Par défaut, utiliser le nom du paramètre
        return parameter.getName();
    }
    
  

}
