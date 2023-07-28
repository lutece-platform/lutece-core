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
    public ICacheKeyService fooProducer() {
        return new DefaultCacheKeyService( List.of("page_id","base_url"), null );
    }

    @Produces
    @Named("portletCacheKeyService")
    @ApplicationScoped
    public ICacheKeyService hooProducer() {
        return new DefaultCacheKeyService( null, List.of("page_id","site-path") );
    }
}
