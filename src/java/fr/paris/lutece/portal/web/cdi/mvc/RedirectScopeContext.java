package fr.paris.lutece.portal.web.cdi.mvc;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import jakarta.enterprise.context.spi.AlterableContext;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.CDI;

public class RedirectScopeContext implements AlterableContext, Serializable {

	/**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Stores the manager.
     */
    private transient RedirectScopeManager manager;

	
    @Override
	public Class<? extends Annotation> getScope() {
		return RedirectScoped.class;
	}

	@Override
	public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        return getManager().get(contextual, creationalContext);	}

	@Override
	public <T> T get(Contextual<T> contextual) {
        return getManager().get(contextual);
    }

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void destroy(Contextual<?> contextual) {
        getManager().destroy(contextual);
    }
	 /**
     * Get the manager.
     *
     * @return the manager.
     */
    public RedirectScopeManager getManager() {
        if (manager == null) {
            manager = CDI.current().select(RedirectScopeManager.class).get();
        }
        return manager;
    }
}
