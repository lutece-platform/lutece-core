package fr.paris.lutece.portal.service.daemon.mocks;

import java.util.concurrent.ExecutorService;

import fr.paris.lutece.portal.service.daemon.DaemonExecutor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;

@Alternative
@ApplicationScoped
public class ImmediateExecutorProducer
{
    @Produces
    @ApplicationScoped
    @DaemonExecutor
    @Alternative
    public ExecutorService executorServiceProduces()
    {
        return new ExecutorServiceForDaemonTests(() -> new TestExecutorService(Runnable::run));
    }
}
