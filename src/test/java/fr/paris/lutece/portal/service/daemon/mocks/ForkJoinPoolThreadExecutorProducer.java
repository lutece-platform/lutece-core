package fr.paris.lutece.portal.service.daemon.mocks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import fr.paris.lutece.portal.service.daemon.DaemonExecutor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;

@Alternative
@ApplicationScoped
public class ForkJoinPoolThreadExecutorProducer
{
    @Produces
    @ApplicationScoped
    @DaemonExecutor
    @Alternative
    public ExecutorService executorServiceProduces()
    {
        return new ExecutorServiceForDaemonTests(() -> new TestExecutorService(runnable -> ForkJoinPool.commonPool().execute(runnable)));
    }
}
