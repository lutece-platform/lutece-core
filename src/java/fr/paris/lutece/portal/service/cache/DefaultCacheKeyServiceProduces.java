package fr.paris.lutece.portal.service.cache;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@ApplicationScoped
public class DefaultCacheKeyServiceProduces {

	@Produces
    @Named("pageCacheKeyService")
    @ApplicationScoped
    public ICacheKeyService pageCacheKeyServiceProducer() {
        // theme (chosen by the user) and locale (language of the request) are added by PageService
        return new DefaultCacheKeyService( List.of("page_id","base_url","theme","locale"), null );
    }

    @Produces
    @Named("portletCacheKeyService")
    @ApplicationScoped
    public ICacheKeyService portletCacheKeyServiceProducer() {
        return new DefaultCacheKeyService( null, List.of("page_id","site-path") );
    }
}
