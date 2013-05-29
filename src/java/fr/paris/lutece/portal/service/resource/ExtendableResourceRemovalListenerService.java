package fr.paris.lutece.portal.service.resource;

import java.util.ArrayList;
import java.util.List;


/**
 * @author vbroussard
 * 
 */
public class ExtendableResourceRemovalListenerService
{
    private static List<IExtendableResourceRemovalListener> _listListeners = new ArrayList<IExtendableResourceRemovalListener>( );

    /**
     * Register an extendable resource removal listener.
     * @param removalListener The listener to register.
     */
    public static synchronized void registerListener( IExtendableResourceRemovalListener removalListener )
    {
        _listListeners.add( removalListener );
    }

    /**
     * Notify listeners that they must remove extensions of a given resource.
     * @param strExtendableResourceType The extendable resource type of the
     *            resource to remove the extensions of.
     * @param strExtendableResourceId The id of the extendable resource to
     *            remove the extensions of.
     */
    public static void doRemoveResourceExtentions( String strExtendableResourceType, String strExtendableResourceId )
    {
        for ( IExtendableResourceRemovalListener removalListener : _listListeners )
        {
            removalListener.doRemoveResourceExtentions( strExtendableResourceType, strExtendableResourceId );
        }
    }

}
