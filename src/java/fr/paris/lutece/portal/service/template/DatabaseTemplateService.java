package fr.paris.lutece.portal.service.template;

import fr.paris.lutece.portal.business.template.DatabaseTemplateHome;


/**
 * This class provides methods to access templates stored in the database.
 */
public class DatabaseTemplateService
{
    /**
     * Get a template in the database from its key
     * @param strKey The key of the template to get
     * @return The template loaded from the database
     */
    public static String getTemplateFromKey( String strKey )
    {
        return DatabaseTemplateHome.getTemplateFromKey( strKey );
    }

    /**
     * Update a template in the database
     * @param strKey The key of the template
     * @param strValue The new value of the template
     */
    public static void updateTemplate( String strKey, String strValue )
    {
        DatabaseTemplateHome.updateTemplate( strKey, strValue );
    }
}
