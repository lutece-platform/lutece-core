package fr.paris.lutece.util.test;

import fr.paris.lutece.portal.service.init.AppInit;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.test.LuteceTestCaseInit;

/**
 * Initializes services.
 * This class is used for JUnit tests only.
 * 
 * This class MUST NOT be in the test library (circular dependency)
 * This class MUST NOT be moved to src/test (plugins depending on core need it)
 * 
 * launched by the test code, defined in src/java/META-INF/services/fr.paris.lutece.test.LuteceTestCaseInit
 * 
 */
public class LuteceTestCaseInitSupport implements LuteceTestCaseInit
{

    @Override
    public void initTests()
    {
        AppLogService.info("LuteceTestCaseInitSupport launching AppInit.initServices");
        AppInit.initServices(null);
    }

}
