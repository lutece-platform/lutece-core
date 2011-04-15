package fr.paris.lutece.portal.service.search;

import java.util.Locale;

import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

/**
 * 
 * RBAC Resource Id Service to allow roles on Search Services
 *
 */
public class SearchResourceIdService extends ResourceIdService
{
	public static final String PERMISSION_MANAGE_ADVANCED_PARAMETERS = "MANAGE_ADVANCED_PARAMETERS";
	private static final String PROPERTY_LABEL_RESOURCE_TYPE = "portal.search.searchService.resourceType";
	private static final String PROPERTY_LABEL_MANAGE_ADVANCED_PARAMETERS = "portal.search.searchService.permission.manageAdvancedParameters";

	/** {@inheritDoc} */
	@Override
	public void register(  )
	{
		ResourceType rt = new ResourceType(  );
		rt.setResourceIdServiceClass( SearchResourceIdService.class.getName(  ) );
		rt.setResourceTypeKey( SearchService.RESOURCE_TYPE );
		rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );
		
		Permission p = new Permission(  );
		p.setPermissionKey( PERMISSION_MANAGE_ADVANCED_PARAMETERS );
		p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE_ADVANCED_PARAMETERS );
		rt.registerPermission( p );
		
		ResourceTypeManager.registerResourceType( rt );
	}

	/** {@inheritDoc} */
	@Override
	public ReferenceList getResourceIdList( Locale locale )
	{
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String getTitle( String strId, Locale locale )
	{
		return "";
	}

}
