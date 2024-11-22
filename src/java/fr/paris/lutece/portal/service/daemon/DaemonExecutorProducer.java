package fr.paris.lutece.portal.service.daemon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class DaemonExecutorProducer {
	@Inject 
	DaemonThreadFactory _daemonThreadFactory;
	
	@Inject
    @ConfigProperty(name = "daemon.ScheduledThreadCorePoolSize", defaultValue = "1")
    private int corePoolSize;
    @Inject
    @ConfigProperty(name = "daemon.maximumPoolSize", defaultValue = "30")
    private int maximumPoolSize;
    @Inject
    @ConfigProperty(name = "daemon.keepAliveTime", defaultValue = "60")
    private long keepAliveTime;
    @Inject
    @ConfigProperty(name = "daemon.timeUnit", defaultValue = "SECONDS")
    private String timeUnitString;

    private TimeUnit timeUnit;
	
    @PostConstruct
    private void init() {
        // Convert the injected string to TimeUnit enum
        timeUnit = TimeUnit.valueOf(timeUnitString);
    }
	
	@Produces
	@ApplicationScoped
	@DaemonExecutor
	public ExecutorService executorServiceProduces() {		
		return new ThreadPoolExecutor (
				corePoolSize,
				maximumPoolSize,
				keepAliveTime,
				timeUnit,
				new SynchronousQueue<>(),
				_daemonThreadFactory			    
		);
	} 
}
