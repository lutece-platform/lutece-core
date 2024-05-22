package fr.paris.lutece.portal.mocks;

import java.util.concurrent.ExecutorService;

import org.jboss.weld.junit5.auto.EnableAlternativeStereotypes;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import fr.paris.lutece.portal.business.user.IAdminUserDAO;
import fr.paris.lutece.portal.service.daemon.DaemonExecutor;
import fr.paris.lutece.portal.service.daemon.mocks.SingleThreadExecutorProducer;
import fr.paris.lutece.portal.service.mail.IMailQueue;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MyInterface;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.inject.Inject;

@EnableAlternativeStereotypes({ TestAlternative.class/* , SingleThreadExecutorProducer.StereotypeAlternative.class */ })
@EnableAlternatives(SingleThreadExecutorProducer.class)
public class TestSandbox extends LuteceTestCase
{
    @Inject
    private IAdminUserDAO adminUserDAO;
    @Inject
    private MyInterface my;
    private @Inject IMailQueue mailqueue;
    private @Inject ManagedThreadFactory threadFactory;
    @Inject
    @DaemonExecutor
    private ExecutorService _executor;

    private void log(String msg)
    {
        AppLogService.info(msg);
    }

    public void testStuff()
    {
        log("-- testStuff started");
        log("MyInterface class : " + my);
        log("getParentContext : " + SpringContextService.getParentContext());
        log("getContext : " + SpringContextService.getContext());
        log("mailqueue class : " + mailqueue);
        log("threadFactory class : " + threadFactory);
        log("_executor class : " + _executor);
        log("-- testStuff ended");
    }

    @Disabled
    public void testStuff2()
    {
        log("we can have several test methods, including a disabled one");
    }

    public void testStuff3()
    {
        log("we can have several test methods");
    }

    @BeforeEach
    public void setUp()
    {
        log("BEFORE");
    }

    @AfterEach
    public void titi()
    {
        log("AFTER");

    }
}
