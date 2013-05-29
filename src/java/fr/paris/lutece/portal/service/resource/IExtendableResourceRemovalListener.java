package fr.paris.lutece.portal.service.resource;

/**
 * Listener of extendable resource removal
 */
public interface IExtendableResourceRemovalListener
{
    /**
     * Do remove every extensions information of a resource
     * @param strExtendableResourceType The extendable resource type of the
     *            removed resource
     * @param strIdExtendableResource The extendable resource id of the removed
     *            resource
     */
    void doRemoveResourceExtentions( String strExtendableResourceType, String strIdExtendableResource );
}
