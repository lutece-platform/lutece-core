package fr.paris.lutece.portal.service.download;

import javax.servlet.http.HttpServletRequest;

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
    protected void checkUserDownloadRight( HttpServletRequest request, FileDownloadData fileDownloadData )
    {
    }
}
