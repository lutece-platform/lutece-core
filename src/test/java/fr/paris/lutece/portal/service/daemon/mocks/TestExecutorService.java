package fr.paris.lutece.portal.service.daemon.mocks;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class TestExecutorService extends AbstractExecutorService
{
    private final Consumer<Runnable> _executor;

    public TestExecutorService( Consumer<Runnable> executor )
    {
        _executor = executor;
    }

    @Override
    public void execute( Runnable command )
    {
        _executor.accept( command );
    }

    @Override
    public List<Runnable> shutdownNow( )
    {
        return null;
    }

    @Override
    public void shutdown( )
    {
    }

    @Override
    public boolean isTerminated( )
    {
        return false;
    }

    @Override
    public boolean isShutdown( )
    {
        return false;
    }

    @Override
    public boolean awaitTermination( long timeout, TimeUnit unit ) throws InterruptedException
    {
        return true;
    }
}