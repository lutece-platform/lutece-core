package fr.paris.lutece.util.file;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.commons.lang.StringUtils;


/**
 * Utility class for files
 */
public final class FileUtil
{
    private static final String PROPERTY_ALLOWED_IMAGES_EXTENSIONS = "portal.files.allowedImagesExtentions";
    private static final String PROPERTY_ALLOWED_HTML_EXTENSIONS = "portal.files.allowedHtmlExtentions";

    private static final String DEFAULT_IMAGES_EXTENSION = "gif,png,jpg,jpeg,bmp";
    private static final String DEFAULT_HTML_EXTENSION = "html,htm,xhtml";

    private static final String CONSTANT_POINT = ".";
    private static final String CONSTANT_COMMA = ",";

    /**
     * Private constructor
     */
    private FileUtil( )
    {
    }

    /**
     * Check if the extension of the file is an image extension
     * @param strImageFileName The file name to check
     * @return True if the extension is correct, false otherwise
     */
    public static boolean hasImageExtension( String strImageFileName )
    {
        String strImagesExtentions = AppPropertiesService.getProperty( PROPERTY_ALLOWED_IMAGES_EXTENSIONS,
                DEFAULT_IMAGES_EXTENSION );
        return hasExtension( strImageFileName, strImagesExtentions );
    }

    /**
     * Check if a file has a valid html extension
     * @param strFileName The file name to check
     * @return True if the file name is valid, false otherwise
     */
    public static boolean hasHtmlExtension( String strFileName )
    {
        String strImagesExtentions = AppPropertiesService.getProperty( PROPERTY_ALLOWED_HTML_EXTENSIONS,
                DEFAULT_HTML_EXTENSION );
        return hasExtension( strFileName, strImagesExtentions );

    }

    /**
     * Check if a file name match extensions in a given list
     * @param strFileName The file name to check
     * @param strAllowedExtensions The comma separated list of allowed
     *            extensions
     * @return True of the extension of the file exists in the extension list
     */
    private static boolean hasExtension( String strFileName, String strAllowedExtensions )
    {
        int nIndex = strFileName.lastIndexOf( CONSTANT_POINT );
        if ( nIndex >= 0 && strFileName.length( ) > nIndex + 2 )
        {
            String strExtension = strFileName.substring( nIndex + 1 );
            if ( StringUtils.isNotEmpty( strExtension ) )
            {
                for ( String strAllowedExtention : strAllowedExtensions.split( CONSTANT_COMMA ) )
                {
                    if ( StringUtils.equalsIgnoreCase( strExtension, strAllowedExtention ) )
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
