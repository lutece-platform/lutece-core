package fr.paris.lutece.portal.business.template;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

@ApplicationScoped
public class CommonsIncludeProducer {

    private static final String TEMPLATE_COMMONS_BS5_TABLER = "commons_bs5_tabler.html";
    private static final String TEMPLATE_COMMONS_BACKPORT = "commons_backport.html";
    private static final String TEMPLATE_MACRO_DATETIMEPICKER = "admin/util/calendar/macro_datetimepicker.html";

	@Produces
    @Named("commonsBoostrap5Tabler")
	@Singleton
    public CommonsInclude commonsBoostrap5TablerProduces() {
        return new CommonsInclude.CommonsIncludeBuilder("commonsBoostrap5Tabler")
        		.setDefault(true)
        		.setName("Bootstrap 5.1 + Tabler 1.0 + Backport file (v6.x compatible) (Default)")
        		.setFiles(List.of( TEMPLATE_COMMONS_BS5_TABLER,TEMPLATE_COMMONS_BACKPORT, TEMPLATE_MACRO_DATETIMEPICKER))
        		.build();
    }
}
