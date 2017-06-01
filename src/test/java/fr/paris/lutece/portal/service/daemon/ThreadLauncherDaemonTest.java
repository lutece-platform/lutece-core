package fr.paris.lutece.portal.service.daemon;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;

public class ThreadLauncherDaemonTest extends LuteceTestCase
{
    private boolean _runnableTimedOut;

    public void testAddItemToQueue( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        CyclicBarrier barrier = new CyclicBarrier( 2 );
        _runnableTimedOut = false;
        ThreadLauncherDaemon.addItemToQueue( ( ) -> {
            try
            {
                barrier.await( 10L, TimeUnit.SECONDS );
            }
            catch( InterruptedException | BrokenBarrierException | TimeoutException e )
            {
                _runnableTimedOut = true;
            }
        }, "key", PluginService.getCore( ) );
        barrier.await( 250L, TimeUnit.MILLISECONDS );
        assertFalse( _runnableTimedOut );
    }
}
