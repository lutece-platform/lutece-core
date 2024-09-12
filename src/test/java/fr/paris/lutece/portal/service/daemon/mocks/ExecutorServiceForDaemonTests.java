package fr.paris.lutece.portal.service.daemon.mocks;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * Test class specifically made for Daemon Tests.
 * 
 * This class expects to be used from DaemonScheduler, notably from its shutdown() method
 */
public class ExecutorServiceForDaemonTests implements ExecutorService
{
    private final Supplier<ExecutorService> factory;
    public ExecutorServiceForDaemonTests(Supplier<ExecutorService> factory)
    {
        this.factory = factory;
        activeExecutor = factory.get();
    }

    private ExecutorService activeExecutor;

    @Override
    public void execute(Runnable command)
    {
        activeExecutor.execute(command);
    }

    @Override
    public void shutdown()
    {
        activeExecutor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow()
    {
        return activeExecutor.shutdownNow();
    }

    @Override
    public boolean isShutdown()
    {
        return activeExecutor.isShutdown();
    }

    @Override
    public boolean isTerminated()
    {
        return activeExecutor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
    {
        boolean awaitTermination = activeExecutor.awaitTermination(timeout, unit);
        if (awaitTermination)
        {
            activeExecutor = factory.get();
        }
        return awaitTermination;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task)
    {
        return activeExecutor.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result)
    {
        return activeExecutor.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task)
    {
        return activeExecutor.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException
    {
        return activeExecutor.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException
    {
        return activeExecutor.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException
    {
        return activeExecutor.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException
    {
        return activeExecutor.invokeAny(tasks, timeout, unit);
    }

}