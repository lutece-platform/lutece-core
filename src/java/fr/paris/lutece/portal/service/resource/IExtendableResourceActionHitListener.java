package fr.paris.lutece.portal.service.resource;

import java.util.Map;


/**
 * Listener for extendable resource action hit
 */
public interface IExtendableResourceActionHitListener
{
    /**
     * Get the total number of hit associated to a given action name and
     * resource type
     * @param strActionName The name of the action to get the number of hit of
     * @param strExtendableResourceType The resource type to get the hit of
     * @return The number of hit, or 0 if this action has no hit for this
     *         resource type
     */
    int getActionHit( String strActionName, String strExtendableResourceType );

    /**
     * Get the list of action names associated with a number of hit for a given
     * resource
     * @param strExtendableResourceId The id of the resource
     * @param strExtendableResourceType The type of the resource
     * @return A map containing associations between action names and hit number
     */
    Map<String, Integer> getResourceHit( String strExtendableResourceId, String strExtendableResourceType );

    /**
     * Get the number of hit associated with a resource and an action name
     * @param strExtendableResourceId The id of the resource
     * @param strExtendableResourceType The type of the resource
     * @param strActionName The name of the action
     * @return The number of hit, or 0 if the resource has no hit for this
     *         action
     */
    int getResourceActionHit( String strExtendableResourceId, String strExtendableResourceType, String strActionName );

    /**
     * Notify this listener that an action has been performed on a resource
     * @param strExtendableResourceId The id of the resource the action was
     *            performed on
     * @param strExtendableResourceType The type of the resource the action was
     *            performed on
     * @param strActionName The name of the action that was performed on the
     *            resource
     */
    void notifyActionOnResource( String strExtendableResourceId, String strExtendableResourceType, String strActionName );
}
