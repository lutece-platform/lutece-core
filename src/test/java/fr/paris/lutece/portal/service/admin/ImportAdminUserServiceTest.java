package fr.paris.lutece.portal.service.admin;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.csv.CSVMessageDescriptor;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.test.LuteceTestCase;

public class ImportAdminUserServiceTest extends LuteceTestCase
{
    public void testReadLineOfCSVFileForPassword( )
    {
        String [ ] lineData = new String [ ] {
                "user" + new SecureRandom( ).nextLong( ), // accessCode
                "lastName", "firstName", "email", Integer.toString( AdminUser.ACTIVE_CODE ), "fr", Integer.toString( 3 ), // level
                "", // resetpassword
                "false", // accessibility mode
                "", // passwordmaxvaliddate
                "", // accountmaxvaliddate
                "", // datelastlogin

        };
        ImportAdminUserService importAdminuser = new ImportAdminUserService( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        List<CSVMessageDescriptor> messages = importAdminuser.readLineOfCSVFile( lineData, 1, AdminUserService.getLocale( request ),
                AppPathService.getBaseUrl( request ) );
        assertNotNull( messages );
        assertTrue( messages.isEmpty( ) );
        AdminUser user = AdminUserHome.findUserByLogin( lineData [0] );
        assertNotNull( user );
        try
        {
            LuteceDefaultAdminUser luteceDefaultAdminUser = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) );
            assertNotNull( luteceDefaultAdminUser );
            assertFalse( luteceDefaultAdminUser.getPassword( ).isLegacy( ) );
        }
        finally
        {
            AdminUserHome.remove( user.getUserId( ) );
        }
    }
}
