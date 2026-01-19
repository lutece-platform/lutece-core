package fr.paris.lutece.portal.service.init;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import fr.paris.lutece.portal.service.util.FileSorterUtil;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;
import nonapi.io.github.classgraph.scanspec.AcceptReject;

/**
 * The {@code WebConfResourceLocator} class provides utility methods to scan and retrieve resources from 
 * the /WEB-INF/conf/ directory and its subdirectories. This class focuses on gathering resources 
 * related to plugin configurations, core overrides, and logging configurations.
 * 
 * The methods in this class support filtering resources based on file extensions, patterns, or 
 * wildcard strings, making it useful for locating and handling specific configuration files 
 * in a web application.
 */
public class WebConfResourceLocator {

    // Resource list to store the scanned resources.
    protected static final ResourceList resources = new ResourceList();
    private static final String pattern = ".*/log4j2[^/]*\\.(xml|json|jsn|yaml|yml|properties)$";

    /**
     * Scans the /WEB-INF/conf/ directories (including plugins, themes and overrides) to collect resources.
     * This method is invoked automatically when resources are needed.
     */
    private static void scanWebInfConfDirectory() {
        try (ScanResult scanResult = new ClassGraph()
                .acceptPathsNonRecursive(FileSorterUtil.PATH_PLUGINS,FileSorterUtil.PATH_THEMES, FileSorterUtil.PATH_OVERRIDE_CORE, FileSorterUtil.PATH_OVERRIDE_PLUGINS)
                .disableNestedJarScanning().scan()) {
            resources.addAll(scanResult.getAllResources());
        }
    }
    /**
     * Returns all resources found in the /WEB-INF/conf/ directories.
     * If the resources have not been scanned yet, it will trigger a scan of the directories.
     * 
     * @return A list of resources found in the specified directories.
     */
    public static ResourceList getAllResources() {
        if (resources.isEmpty()) {
            scanWebInfConfDirectory();
        }
        return resources;    
    }

    /**
     * Retrieves all resources with the specified file extension from the scanned directories.
     * The extension is case-insensitive.
     * 
     * @param extension the file extension to match (e.g., "xml").
     * @return A list of resources that match the specified extension.
     */
    public static ResourceList getResourcesWithExtension(final String extension) {
        final ResourceList allAcceptedResources = getAllResources();
        if (allAcceptedResources.isEmpty()) {
            return ResourceList.emptyList();
        } else {
            String bareExtension = extension;
            while (bareExtension.startsWith(".")) {
                bareExtension = bareExtension.substring(1);
            }
            final ResourceList filteredResources = new ResourceList();
            for (final Resource classpathResource : allAcceptedResources) {
                final String relativePath = classpathResource.getPath();
                final int lastSlashIdx = relativePath.lastIndexOf('/');
                final int lastDotIdx = relativePath.lastIndexOf('.');
                if (lastDotIdx > lastSlashIdx && relativePath.substring(lastDotIdx + 1).equalsIgnoreCase(bareExtension)) {
                    filteredResources.add(classpathResource);
                }
            }
            return filteredResources;
        }
    }

    /**
     * Retrieves all resources that match the provided regular expression pattern in their paths.
     * 
     * @param pattern The regex pattern to match against resource paths.
     * @return A list of resources whose paths match the provided pattern.
     */
    public static ResourceList getResourcesMatchingPattern(final Pattern pattern) {
        final ResourceList allAcceptedResources = getAllResources();
        if (allAcceptedResources.isEmpty()) {
            return ResourceList.emptyList();
        } else {
            final ResourceList filteredResources = new ResourceList();
            for (final Resource classpathResource : allAcceptedResources) {
                final String relativePath = classpathResource.getPath();
                if (pattern.matcher(relativePath).matches()) {
                    filteredResources.add(classpathResource);
                }
            }
            return filteredResources;
        }
    }

    /**
     * Retrieves all resources that match the provided wildcard pattern in their paths.
     * The wildcard pattern can include:
     * <ul>
     *     <li>Single asterisks (*) to match any sequence of characters excluding '/'.</li>
     *     <li>Double asterisks (**) to match any sequence of characters including '/'.</li>
     *     <li>Question marks (?) to match any single character.</li>
     * </ul>
     * 
     * @param wildcardString The wildcard pattern to match against resource paths.
     * @return A list of resources whose paths match the provided wildcard pattern.
     */
    public static ResourceList getResourcesMatchingWildcard(final String wildcardString) {
        return getResourcesMatchingPattern(AcceptReject.globToPattern(wildcardString, false));
    }

    /**
     * Retrieves a set of paths to configuration log files (e.g., "_log.xml") found in the scanned directories.
     * 
     * @return A set of paths to the log configuration files.
     */
    public static Set<String> getPathConfLog() {
        return new LinkedHashSet<>(FileSorterUtil.sortedByPathAndExtensionList(getResourcesMatchingPattern(Pattern.compile(pattern)).getPathsRelativeToClasspathElement()));
    }

    /**
     * Retrieves a set of paths to properties files found in the scanned directories.
     * 
     * @return A set of paths to properties files.
     */
    public static Set<String> getPathPropertiesFile() {
        return new LinkedHashSet<>(FileSorterUtil.sortByPathPriorityDescending(getResourcesWithExtension("properties").getPathsRelativeToClasspathElement()));
    }
    
    /**
     * Retrieves a set of paths to xml files found in the scanned directories.
     * 
     * @return A set of paths to xml files.
     */
    public static Set<String> getPathXmlFile() {
        return new LinkedHashSet<>(FileSorterUtil.sortByPathPriorityDescending(getResourcesWithExtension("xml").getPathsRelativeToClasspathElement()));
    }
}
