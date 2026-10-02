package fr.paris.lutece.portal.service.util;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileSorterUtil {

	  // Constants representing the paths to different configuration directories.
    public static final String PATH_CONF = "/WEB-INF/conf/";
    public static final String PATH_PLUGINS = PATH_CONF + "plugins/";
    public static final String PATH_THEMES = PATH_CONF + "themes/";
    public static final String PATH_OVERRIDE_CORE = PATH_CONF + "override/";
    public static final String PATH_OVERRIDE_PLUGINS = PATH_CONF + "override/plugins";
    
    /**
     * Sorts properties file paths into their loading order, from the weakest location to the strongest: the plugins,
     * the themes, the override directory, then the override/plugins directory, each one in alphabetical order. The
     * files are loaded in this order into one set of properties, so a key set in several files takes the value of the
     * last one, as in Lutece 7. A path may be relative, the way the classpath scan returns it, or start with a slash.
     * <p>
     * The configuration is exposed to MicroProfile Config through two {@code ConfigSource}s, so the four ranks never
     * appear together in one list: the base source is given the plugins and the themes only, the override source the
     * two override directories only. This order therefore arbitrates <b>inside</b> a source. Between the two sources,
     * it is their {@code ConfigSource} ordinal that makes the override values win, not this sort.
     * </p>
     *
     * @param set The list of properties file paths
     * @return The paths in the order they must be loaded
     */
    public static List<String> sortByPropertiesPrecedence( List<String> set )
    {
        return set.stream( )
                .sorted( Comparator.comparingInt( FileSorterUtil::getPropertiesPrecedence ).thenComparing( Comparator.naturalOrder( ) ) )
                .collect( Collectors.toList( ) );
    }

    /**
     * Returns the rank of a properties file in the loading order: the higher the rank, the later the file is loaded
     * and the stronger its values.
     *
     * @param path The file path, relative or starting with a slash
     * @return The rank of its directory
     */
    private static int getPropertiesPrecedence( String path )
    {
        String strPath = path.startsWith( "/" ) ? path : "/" + path;
        if ( strPath.startsWith( PATH_OVERRIDE_PLUGINS + "/" ) )
        {
            return 4;
        }
        if ( strPath.startsWith( PATH_OVERRIDE_CORE ) )
        {
            return 3;
        }
        if ( strPath.startsWith( PATH_THEMES ) )
        {
            return 2;
        }
        return strPath.startsWith( PATH_PLUGINS ) ? 1 : 0;
    }

    /**
     * Sorts the list of file paths by both path priority and extension priority.
     * First, the list is sorted by path priority, and then by extension priority.
     * 
     * @param set The list of file paths to be sorted
     * @return The sorted list of file paths
     */
    public static List<String> sortedByPathAndExtensionList(List<String> set) {
        List<String> pathSorted = sortByPathPriorityDescending(set);
        return sortByExtensionPriority(pathSorted);
    }

    /**
     * Sorts the list of file paths in descending order based on path priority.
     * Paths with higher priority values will appear later in the list.
     * 
     * @param set The list of file paths to sort in descending path priority
     * @return The list of file paths sorted in descending path priority
     */
    public static List<String> sortByPathPriority(List<String> set) {
        return set.stream()
                .sorted((s1, s2) -> {
                    int pathPriority1 = getPathPriority(s1);
                    int pathPriority2 = getPathPriority(s2);
                    if (pathPriority1 != pathPriority2) {
                        return Integer.compare(pathPriority1, pathPriority2);
                    }
                    // If path priorities are equal, sort alphabetically
                    return s1.compareTo(s2);
                })
                .collect(Collectors.toList());
    }  
    /**
     * Sorts the list of file paths based on path priority.
     * Paths are prioritized based on specific prefixes (e.g., PATH_OVERRIDE_PLUGINS, PATH_OVERRIDE_CORE, etc.).
     * 
     * @param set The list of file paths to sort by path priority
     * @return The list of file paths sorted by path priority
     */
    public static List<String> sortByPathPriorityDescending(List<String> set) {
    	 return set.stream()
                 .sorted((s1, s2) -> {
                     int pathPriority1 = getPathPriority(s1);
                     int pathPriority2 = getPathPriority(s2);
                     if (pathPriority1 != pathPriority2) {
                         return Integer.compare(pathPriority2, pathPriority1);
                     }
                     // If path priorities are equal, sort alphabetically
                     return s2.compareTo(s1);
                 })
                 .collect(Collectors.toList());
    }

    /**
     * Sorts the list of file paths based on extension priority.
     * Extensions are prioritized (e.g., .xml, .json, .properties, etc.). If two files have the same extension priority,
     * they are sorted alphabetically.
     * 
     * @param set The list of file paths to sort by extension priority
     * @return The list of file paths sorted by extension priority
     */
    public static List<String> sortByExtensionPriority(List<String> set) {
        return set.stream()
                .sorted((s1, s2) -> {
                    int extensionPriority1 = getExtensionPriority(s1);
                    int extensionPriority2 = getExtensionPriority(s2);
                    if (extensionPriority1 != extensionPriority2) {
                        // Sort by extension priority in descending order
                        return Integer.compare(extensionPriority2, extensionPriority1);
                    }
                    // If extension priorities are equal, sort alphabetically
                    return s1.compareTo(s2);
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns the priority of the given path based on its prefix.
     * Higher priority is given to paths that match specific prefixes.
     * 
     * @param path The file path
     * @return The priority level of the path
     */
    private static int getPathPriority(String path) {
        if (path.startsWith(PATH_OVERRIDE_PLUGINS)) {
            return 3;
        } else if (path.startsWith(PATH_OVERRIDE_CORE)) {
            return 2;
        } else if (path.startsWith(PATH_PLUGINS)) {
            return 1;
        }
        return -1;
    }

    /**
     * Returns the priority of the file based on its extension.
     * A higher priority is assigned to specific extensions (e.g., .xml, .json).
     * 
     * @param filename The filename or file path
     * @return The priority level of the file's extension
     */
    private static int getExtensionPriority(String filename) {
        if (filename.endsWith(".xml")) {
            return 5;
        } else if (filename.endsWith(".json") || filename.endsWith(".jsn")) {
            return 6;
        } else if (filename.endsWith(".yaml") || filename.endsWith(".yml")) {
            return 7;
        } else if (filename.endsWith(".properties")) {
            return 8;
        }
        return Integer.MAX_VALUE; // Lowest priority for unknown extensions
    }
}

