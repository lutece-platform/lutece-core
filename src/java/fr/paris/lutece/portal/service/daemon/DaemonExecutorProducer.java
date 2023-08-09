package fr.paris.lutece.portal.service.daemon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.config.Config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class DaemonExecutorProducer {
	@Inject 
	DaemonThreadFactory _daemonThreadFactory;
	@Inject
	private Config _config;
	
	
	@Produces
	@ApplicationScoped
	@DaemonExecutor
	public ExecutorService executorServiceProduces() {		
		return new ThreadPoolExecutor (
				_config.getOptionalValue("daemon.ScheduledThreadCorePoolSize", Integer.class).orElse(1),
				_config.getOptionalValue("daemon.maximumPoolSize", Integer.class).orElse(30),
				_config.getOptionalValue("daemon.keepAliveTime", Long.class).orElse(60L),
				_config.getOptionalValue("daemon.timeUnit", TimeUnit.class).orElse(TimeUnit.SECONDS),
				new SynchronousQueue<>(),
				_daemonThreadFactory			    
		);
	} 

}
