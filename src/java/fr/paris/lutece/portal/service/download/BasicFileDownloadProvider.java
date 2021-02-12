package fr.paris.lutece.portal.service.download;

import fr.paris.lutece.api.user.User;

/**
 * Basic {@link IFileDownloadProvider}  implementation with no user rights checks
 *
 */
public class BasicFileDownloadProvider extends AbstractFileDownloadProvider
{
    public static final String PROVIDER_NAME = "basicFileDownloadProvider";
    
    @Override
    public String getProviderName( )
    {
        return PROVIDER_NAME;
    }
    
    @Override
    protected void checkUserDownloadRight( User user, boolean bo, FileDownloadData fileDownloadData )
    {
    }
}
