package fr.paris.lutece.portal.util.mvc.binding;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.portal.service.upload.MultipartItem;
import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestParam;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Resolver for parameters of type {@link MultipartItem}, {@code MultipartItem[]}, or {@code List<MultipartItem>}.
 * <p>
 * This class is responsible for resolving multipart file uploads in the context of an MVC request.
 * It supports single file resolution, arrays of files, and lists of files, using the {@link MultipartHttpServletRequest}.
 * </p>
 * 
 * <p>
 * The resolver checks for the presence of the {@link RequestParam} annotation to determine the parameter name
 * and whether the parameter is required. If the required parameter is missing, a {@link ResolverResult#failed}
 * is returned with an appropriate error message.
 * </p>
 * 
 * <p>
 * This class is annotated with {@link ApplicationScoped}, meaning it is managed by the CDI container and shared
 * across the application.
 * </p>
 * 
 * @see AbstractParameterResolver
 * @see MultipartHttpServletRequest
 * @see RequestParam
 * @see ResolverResult
 */
@ApplicationScoped
public class MultipartItemResolver extends AbstractParameterResolver {

	 /**
     * Determines whether this resolver supports the given parameter.
     * <p>
     * This method checks if the parameter type is {@link MultipartItem}, {@code MultipartItem[]}, or {@code List<MultipartItem>}.
     * </p>
     * 
     * @param parameter The method parameter to check.
     * @return {@code true} if the parameter type is supported, {@code false} otherwise.
     */
	@Override
	public boolean supports(Parameter parameter) {
		 // Check if the parameter type is MultipartItem, MultipartItem[], or List<MultipartItem>
        if (MultipartItem.class.isAssignableFrom(parameter.getType())) {
            return true;
        }
        if (parameter.getType().isArray() && MultipartItem.class.isAssignableFrom(parameter.getType().getComponentType())) {
            return true;
        }
        if (List.class.isAssignableFrom(parameter.getType())) {
            Type genericType = parameter.getParameterizedType();
            if (genericType instanceof ParameterizedType) {
                Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
                if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                    return MultipartItem.class.isAssignableFrom((Class<?>) typeArguments[0]);
                }
            }
        }
        return false;
    }

	/**
     * Resolves the value of the given parameter from the {@link HttpServletRequest}.
     * <p>
     * This method retrieves the parameter name from the {@link RequestParam} annotation if present,
     * and resolves the value based on the parameter type. Supported types include:
     * <ul>
     * <li>{@link MultipartItem} - Single file upload.</li>
     * <li>{@code MultipartItem[]} - Array of file uploads.</li>
     * <li>{@code List<MultipartItem>} - List of file uploads.</li>
     * </ul>
     * </p>
     * 
     * @param request   The HTTP request containing the multipart data.
     * @param parameter The method parameter to resolve.
     * @return A {@link ResolverResult} containing the resolved value or an error if resolution fails.
     */
	@Override
	public ResolverResult<?> resolve(HttpServletRequest request, Parameter parameter) {
		
		if (!(request instanceof MultipartHttpServletRequest)) {
            // Return a failed result if the request is not MultipartHttpServletRequest
            return ResolverResult.failed(null, new BindingErrorImpl(parameter.getName(),
                "Request is not MultipartHttpServletRequest", null));
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        
     // Retrieve parameter name from @RequestParam annotation if present
        String paramName = parameter.isAnnotationPresent(RequestParam.class)
            ? parameter.getAnnotation(RequestParam.class).value()
            : parameter.getName();
        RequestParam annotation = parameter.getAnnotation(RequestParam.class);

        if (MultipartItem.class.isAssignableFrom(parameter.getType())) {
            // Resolve single MultipartItem
            MultipartItem file = multipartRequest.getFile(paramName);
            if (file == null && annotation != null && annotation.required()) {
                return ResolverResult.failed(null, new BindingErrorImpl(paramName, "No file found for parameter", null));
            } else {
            	return ResolverResult.success(file);
            }
        } else if (parameter.getType().isArray() && MultipartItem.class.isAssignableFrom(parameter.getType().getComponentType())) {
            // Resolve array of MultipartItem
            List<MultipartItem> fileList = multipartRequest.getFileList(paramName);
            if ( CollectionUtils.isEmpty(fileList) && annotation != null && annotation.required()) {
                return ResolverResult.failed(null, new BindingErrorImpl(paramName, "No files found for parameter", null));

            } else if(CollectionUtils.isEmpty(fileList)) {
                return ResolverResult.success(null);
            }else {
            	MultipartItem[] fileArray = fileList.toArray(new MultipartItem[0]);
                return ResolverResult.success(fileArray);
            }
        }else if (List.class.isAssignableFrom(parameter.getType())) {
            // Resolve List<MultipartItem>
            List<MultipartItem> fileList = multipartRequest.getFileList(paramName);
            if ( CollectionUtils.isEmpty(fileList) && annotation != null && annotation.required()) {
                return ResolverResult.failed(null, new BindingErrorImpl(paramName, "No files found for parameter", null));
            } else {
                return ResolverResult.success(fileList);
            }
        }

        return ResolverResult.failed(null, new BindingErrorImpl(paramName, "Unsupported parameter type", null));
    }
}
