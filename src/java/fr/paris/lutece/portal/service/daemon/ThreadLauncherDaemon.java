package fr.paris.lutece.portal.service.daemon;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Daemon that manage a pool of threads to launch runnables.
 */
public class ThreadLauncherDaemon extends Daemon
{
    private static final String PROPERTY_MAX_NUMBER_THREAD = "daemon.threadLauncherDaemon.maxNumberOfThread";

    private static Deque<RunnableQueueItem> _stackItems = new ArrayDeque<RunnableQueueItem>( );
    private Map<String, Thread> _mapThreadByKey = new HashMap<String, Thread>( );
    private List<Thread> _listThread = new ArrayList<Thread>( );

    /**
     * {@inheritDoc}
     */
    @Override
    public void run( )
    {
        int nMaxNumberThread = AppPropertiesService.getPropertyInt( PROPERTY_MAX_NUMBER_THREAD, 5 );

        // We remove dead threads from running thread collections
        RunnableQueueItem item = null;
        List<Thread> listDeadThreads = new ArrayList<Thread>( );
        for ( Thread thread : _mapThreadByKey.values( ) )
        {
            if ( !thread.isAlive( ) )
            {
                listDeadThreads.add( thread );
            }
        }
        for ( Thread thread : listDeadThreads )
        {
            _mapThreadByKey.remove( thread );
        }
        listDeadThreads = new ArrayList<Thread>( );
        for ( Thread thread : _listThread )
        {
            if ( !thread.isAlive( ) )
            {
                listDeadThreads.add( thread );
            }
        }
        for ( Thread thread : listDeadThreads )
        {
            _listThread.remove( thread );
        }
        int nCurrentNumberRunningThreads = _mapThreadByKey.size( ) + _listThread.size( );

        List<RunnableQueueItem> listLockedItems = new ArrayList<RunnableQueueItem>( );
        while ( nCurrentNumberRunningThreads < nMaxNumberThread && ( item = popItemFromQueue( ) ) != null )
        {
            // If the item has a key, then we must make sure that another thread with the same key and plugin is not running
            if ( item.getKey( ) != null && item.getPlugin( ) != null )
            {
                Thread thread = _mapThreadByKey.get( item.computeKey( ) );
                if ( thread != null )
                {
                    if ( thread.isAlive( ) )
                    {
                        // The thread is already running. We declare this item as locked for this run of the daemon.
                        listLockedItems.add( item );
                    }
                    else
                    {
                        // Dead threads are removed from collections at the beginning of the run of the daemon
                        // We still check again that the thread is alive just in case it died during the run
                        thread = new Thread( item.getRunnable( ) );
                        thread.start( );
                        _mapThreadByKey.put( item.computeKey( ), thread );
                        // We do not increase the number of running threads, because we removed and add one
                    }
                }
                else
                {
                    // We start a new thread, and increase the current number of running threads
                    thread = new Thread( item.getRunnable( ) );
                    thread.start( );
                    _mapThreadByKey.put( item.computeKey( ), thread );
                    nCurrentNumberRunningThreads++;
                }
            }
            else
            {
                // If it has no key, or if the plugin has not been set, we create a thread in the keyless collection
                Thread thread = new Thread( item.getRunnable( ) );
                thread.start( );
                _mapThreadByKey.put( item.computeKey( ), thread );
                nCurrentNumberRunningThreads++;
            }
        }

        // We replace in the queue items that was locked
        for ( RunnableQueueItem itemQueue : listLockedItems )
        {
            addItemToQueue( itemQueue );
        }

        // If the maximum number of running threads has been reached, we end this run
        if ( nCurrentNumberRunningThreads >= nMaxNumberThread )
        {
            setLastRunLogs( "Every threads are running. Daemon execution ending." );
            return;
        }
        setLastRunLogs( "There is no more runnable to launch." );
    }

    /**
     * Add a runnable to the launch queue. It will be launched as soon as a
     * thread is available.
     * @param runnable The runnable to execute
     * @param strKey The key of the runnable. Runnables of a given plugin are
     *            ensured that they will not be executed at the same time if
     *            they have the same key.
     * @param plugin The plugin the runnable is associated with
     */
    public static void addItemToQueue( Runnable runnable, String strKey, Plugin plugin )
    {
        RunnableQueueItem runnableItem = new RunnableQueueItem( runnable, strKey, plugin );
        synchronized ( ThreadLauncherDaemon.class )
        {
            _stackItems.addLast( runnableItem );
        }
    }

    /**
     * Add a runnable item to the queue
     * @param runnableItem The runnable item to add to the queue
     */
    private synchronized static void addItemToQueue( RunnableQueueItem runnableItem )
    {
        _stackItems.addLast( runnableItem );
    }

    /**
     * Pop the first item of the queue. The item is removed from the queue.
     * @return The first item of the queue, or null if the queue is empty
     */
    private synchronized static RunnableQueueItem popItemFromQueue( )
    {
        if ( _stackItems.size( ) == 0 )
        {
            return null;
        }
        return _stackItems.pop( );
    }

    /**
     * Count the number of items in the queue.
     * @return The current number of items in the queue
     */
    public synchronized static Integer countItemsInQueue( )
    {
        return _stackItems.size( );
    }
}
