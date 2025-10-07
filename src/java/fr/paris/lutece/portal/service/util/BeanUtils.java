package fr.paris.lutece.portal.service.util;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

public class BeanUtils {

	
    public static final String BEAN_PORTLET_REMOVAL_SERVICE = "portletRemovalService";
    public static final String BEAN_MAILINGLIST_REMOVAL_SERVICE = "mailinglistRemovalService";
    public static final String BEAN_RBAC_REMOVAL_SERVICE ="rbacRemovalService";
    public static final String BEAN_REGULAR_EXPRESSION_REMOVAL_SERVICE = "regularExpressionRemovalService";
    public static final String BEAN_ROLE_REMOVAL_SERVICE = "roleRemovalService";
    public static final String BEAN_WORKFLOW_REMOVAL_SERVICE = "workflowRemovalService";
    public static final String BEAN_WORKGROUP_REMOVAL_SERVICE = "workgroupRemovalService";
    
    public static final String BEAN_CAPTCHA_SERVICE = "captcha.captchaService";

	 /**
     * Indicates if a bean, referenced by its name, is part of an enabled plugin.
     * 
     * Per Lutece convention, the plugin is determined from the bean's name prefix. If no prefix is present, the bean is considered enabled.
     * 
     * @param strBeanName
     *            The bean's name
     * @return <code>true</code> if the bean is part of an enabled plugin, <code>false</code> otherwise
     */
    public static boolean isBeanEnabled( String strBeanName )
    {
        String strPrefix = getPrefix( strBeanName );
        return strPrefix == null || isEnabled( strPrefix );
    }
	
	/**
     * Gets the prefix of the bean (supposed to be the plugin name)
     * 
     * @param strBeanName
     *            The bean name
     * @return The prefix
     */
    private static String getPrefix( String strBeanName )
    {
        int nPos = strBeanName.indexOf( '.' );

        if ( nPos > 0 )
        {
            return strBeanName.substring( 0, nPos );
        }

        return null;
    }

    /**
     * Analyze a bean prefix to tell if it matchs an activated plugin
     * 
     * @param strPrefix
     *            The prefix of a bean
     * @return True if the prefix matchs an activated plugin
     */
    private static boolean isEnabled( String strPrefix )
    {
        Plugin plugin = PluginService.getPlugin( strPrefix );

        return ( plugin != null ) && plugin.isInstalled( );
    }

}
