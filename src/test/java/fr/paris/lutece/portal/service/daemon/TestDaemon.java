package fr.paris.lutece.portal.service.daemon;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class TestDaemon extends Daemon
{
    CyclicBarrier startBarrier = new CyclicBarrier( 2 );
    CyclicBarrier completionBarrier = new CyclicBarrier( 2 );
    boolean hasRun;
    boolean shouldThrow;

    public void setRunThrows( boolean shouldThrow )
    {
        this.shouldThrow = shouldThrow;
    }

    @Override
    public void run( )
    {
        try
        {
            hasRun = false;
            startBarrier.await( 10, TimeUnit.SECONDS );
            hasRun = true;
            completionBarrier.await( 10, TimeUnit.SECONDS );
        }
        catch ( InterruptedException | BrokenBarrierException | TimeoutException e )
        {
            e.printStackTrace( );
        }
        if ( shouldThrow )
        {
            throw new RuntimeException( "I'm a bad daemon" );
        }
    }

    public boolean hasRun( )
    {
        return hasRun;
    }

    public void resetGo( )
    {
        startBarrier.reset( );
    }

    public void go( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        this.go( 10, TimeUnit.SECONDS );
    }

    public void go( long timeout, TimeUnit unit )
            throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        startBarrier.await( timeout, unit );
    }

    public void waitForCompletion( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        this.waitForCompletion( 10, TimeUnit.SECONDS );
    }

    public void waitForCompletion( long timeout, TimeUnit unit )
            throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        completionBarrier.await( timeout, unit );
    }

}