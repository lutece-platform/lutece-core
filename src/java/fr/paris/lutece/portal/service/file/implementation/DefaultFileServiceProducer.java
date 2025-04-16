package fr.paris.lutece.portal.service.file.implementation;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.paris.lutece.portal.service.file.IFileDownloadUrlService;
import fr.paris.lutece.portal.service.file.IFileRBACService;
import fr.paris.lutece.portal.service.file.IFileStoreService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.util.CdiHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@ApplicationScoped
public class DefaultFileServiceProducer
{
    @Produces
    @ApplicationScoped
    @Named( "defaultFileServiceProvider" )
    public IFileStoreServiceProvider produceBean( @ConfigProperty( name = "lutece.defaultFileServiceProvider.fileStoreService" ) String fileStoreImplName,
            @ConfigProperty( name = "lutece.defaultFileServiceProvider.rbacService" ) String rbacImplName,
            @ConfigProperty( name = "lutece.defaultFileServiceProvider.downloadService" ) String dlImplName )
    {
        DefaultFileStoreServiceProvider provider = new DefaultFileStoreServiceProvider( "defaultFileServiceProvider",
        		CdiHelper.getReference( IFileStoreService.class, fileStoreImplName ),
        		CdiHelper.getReference( IFileDownloadUrlService.class, dlImplName ),
                CdiHelper.getReference( IFileRBACService.class, rbacImplName ),
                true );

        return provider;
    }
}
