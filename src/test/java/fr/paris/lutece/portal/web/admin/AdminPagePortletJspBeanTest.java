package fr.paris.lutece.portal.web.admin;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.portlet.PortletResourceIdService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;

/**
 * Test the AdminPagePortletJspBean class
 */
public class AdminPagePortletJspBeanTest extends LuteceTestCase
{

    /** status request parameter */
    private static final String PORTLET_STATUS = "status";

    /**
     * Test when no parameter given
     * @throws AccessDeniedException should not happen
     */
    public void testGetModifyPortletStatusNoParam(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        String url = bean.getModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when no status parameter given
     * @throws AccessDeniedException should not happen
     */
    public void testGetModifyPortletStatusNoStatusParam(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        request.addMokeParameters( Parameters.PORTLET_ID, "1" );
        String url = bean.getModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when no portlet_id parameter given
     * @throws AccessDeniedException should not happen
     */
    public void testGetModifyPortletStatusNoPortletParam(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        request.addMokeParameters( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
        String url = bean.getModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the status is invalid
     * @throws AccessDeniedException should not happen
     */
    public void testGetModifyPortletStatusInvalidStatus(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addMokeParameters( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addMokeParameters( PORTLET_STATUS, "999999999" );
            String url = bean.getModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        } finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the portlet_id is invalid
     * @throws AccessDeniedException should not happen
     */
    public void testGetModifyPortletStatusInvalidPortletID(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        request.addMokeParameters( Parameters.PORTLET_ID, "NOT_NUMERIC" );
        request.addMokeParameters( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
        String url = bean.getModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the portlet does not exist
     * @throws AccessDeniedException should not happen
     */
    public void testGetModifyPortletStatusInexistantPortletID(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addMokeParameters( Parameters.PORTLET_ID, "31415925" );
            request.addMokeParameters( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
            String url = bean.getModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        } finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the user does not have the right ro manage portlets
     */
    public void testGetModifyPortletStatusNoRight(  )
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addMokeParameters( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addMokeParameters( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
            request.registerAdminUser( new AdminUser( ) );
            bean.getModifyPortletStatus( request );
            fail("Should not have been able to modify the portlet");
        } catch (AccessDeniedException e)
        {
        } finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when all conditions are met
     * @throws AccessDeniedException should not happen
     */
    public void testGetModifyPortletStatus(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            user = getAdminUser( );
            request.addMokeParameters( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addMokeParameters( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
            request.registerAdminUser( user );
            String url = bean.getModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_CONFIRMATION );
        } finally
        {
            if ( portlet != null )
            {
                removePortlet( portlet );
            }
            if ( user != null )
            {
                removeUser( user );
            }
        }
    }

    /**
     * Test when no parameter given
     * @throws AccessDeniedException should not happen
     */
    public void testDoModifyPortletStatusNoParam(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        String url = bean.doModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when no status parameter given
     * @throws AccessDeniedException should not happen
     */
    public void testDoModifyPortletStatusNoStatusParam(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        request.addMokeParameters( Parameters.PORTLET_ID, "1" );
        String url = bean.doModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when no portlet_id parameter given
     * @throws AccessDeniedException should not happen
     */
    public void testDoModifyPortletStatusNoPortletParam(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        request.addMokeParameters( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
        String url = bean.doModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the status is invalid
     * @throws AccessDeniedException should not happen
     */
    public void testDoModifyPortletStatusInvalidStatus(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addMokeParameters( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addMokeParameters( PORTLET_STATUS, "999999999" );
            String url = bean.doModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        } finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the portlet_id is invalid
     * @throws AccessDeniedException should not happen
     */
    public void testDoModifyPortletStatusInvalidPortletID(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        request.addMokeParameters( Parameters.PORTLET_ID, "NOT_NUMERIC" );
        request.addMokeParameters( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
        String url = bean.doModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the portlet does not exist
     * @throws AccessDeniedException should not happen
     */
    public void testDoModifyPortletStatusInexistantPortletID(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addMokeParameters( Parameters.PORTLET_ID, "31415925" );
            request.addMokeParameters( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
            String url = bean.doModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        } finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the user does not have the right ro manage portlets
     */
    public void testDoModifyPortletStatusNoRight(  )
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addMokeParameters( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addMokeParameters( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
            request.registerAdminUser( new AdminUser( ) );
            bean.doModifyPortletStatus( request );
            fail("Should not have been able to modify the portlet");
        } catch (AccessDeniedException e)
        {
        } finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when all conditions are met
     * @throws AccessDeniedException should not happen
     */
    public void testDoModifyPortletStatus(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            int nStatus = portlet.getStatus( );
            int nNewStatus = nStatus == Portlet.STATUS_PUBLISHED ? Portlet.STATUS_UNPUBLISHED : Portlet.STATUS_PUBLISHED;
            user = getAdminUser( );
            request.addMokeParameters( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addMokeParameters( PORTLET_STATUS, Integer.toString( nNewStatus ) );
            request.registerAdminUser( user );
            String url = bean.doModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNull( message );
            Portlet storedPortlet = PortletHome.findByPrimaryKey( portlet.getId( ) );
            assertNotNull( storedPortlet );
            assertEquals( nNewStatus, storedPortlet.getStatus( ) );
        } finally
        {
            if ( portlet != null )
            {
                removePortlet( portlet );
            }
            if ( user != null )
            {
                removeUser( user );
            }
        }
    }

    /**
     * Test when no parameter given
     * @throws AccessDeniedException should not happen
     */
    public void testGetRemovePortletNoParam(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        String url = bean.getRemovePortlet( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the portlet_id is invalid
     * @throws AccessDeniedException should not happen
     */
    public void testGetRemovePortletInvalidPortletID(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        request.addMokeParameters( Parameters.PORTLET_ID, "NOT_NUMERIC" );
        String url = bean.getRemovePortlet( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the portlet does not exist
     * @throws AccessDeniedException should not happen
     */
    public void testGetRemovePortletInexistantPortletID(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addMokeParameters( Parameters.PORTLET_ID, "31415925" );
            String url = bean.getRemovePortlet( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        } finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the user does not have the right ro manage portlets
     */
    public void testGetRemovePortletNoRight(  )
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addMokeParameters( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.registerAdminUser( new AdminUser( ) );
            bean.getRemovePortlet( request );
            fail("Should not have been able to modify the portlet");
        } catch (AccessDeniedException e)
        {
        } finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when all conditions are met
     * @throws AccessDeniedException should not happen
     */
    public void testGetRemovePortlet(  ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            user = getAdminUser( );
            request.addMokeParameters( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.registerAdminUser( user );
            String url = bean.getRemovePortlet( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_CONFIRMATION );
        } finally
        {
            if ( portlet != null )
            {
                removePortlet( portlet );
            }
            if ( user != null )
            {
                removeUser( user );
            }
        }
    }

    /**
     * Get an admin user with a Role which can manage portlets
     * @return the admin user
     */
    private AdminUser getAdminUser( )
    {
        String strRoleKey = "ROLE_" + new BigInteger(40, new SecureRandom()).toString(32); 
        RBAC rbac = new RBAC( );
        rbac.setResourceTypeKey( PortletType.RESOURCE_TYPE );
        rbac.setPermissionKey( PortletResourceIdService.PERMISSION_MANAGE );
        rbac.setResourceId( RBAC.WILDCARD_RESOURCES_ID );
        rbac.setRoleKey( strRoleKey );
        RBACHome.create( rbac );
        AdminRole role = new AdminRole( );
        role.setKey( strRoleKey );
        role.setDescription( strRoleKey );
        AdminUser user = new AdminUser( );
        Map<String, AdminRole> roles = new HashMap<>( );
        roles.put( strRoleKey, role );
        user.setRoles( roles );
        return user;
    }

    /**
     * Remove objects persisted with the test user
     * @param user the test user
     */
    private void removeUser( AdminUser user )
    {
        Map<String, AdminRole> roles = user.getRoles( );
        for ( String roleKey : roles.keySet( ) )
        {
            RBACHome.removeForRoleKey( roleKey );
        }
    }

    /**
     * Remove the test portlet
     * @param portlet the test portlet
     */
    private void removePortlet( Portlet portlet )
    {
        PortletType portletType = PortletTypeHome.findByPrimaryKey( portlet.getPortletTypeId( ) );
        PortletHome portletHome = new TestPortletHome( );
        portletHome.remove( portlet );
        PortletTypeHome.remove( portletType.getId( ) );
    }

    /**
     * Get a test portlet
     * @return a test portlet
     */
    private Portlet getPortlet( )
    {
        PortletType portletType = new PortletType( );
        String strPortletTypeID = "TEST_" + new BigInteger(40, new SecureRandom()).toString(32); 
        portletType.setId( strPortletTypeID );
        portletType.setHomeClass( TestPortletHome.class.getName( ) );
        PortletTypeHome.create( portletType );
        Portlet portlet = new TestPortlet( portletType );
        portlet.setStatus( Portlet.STATUS_UNPUBLISHED );
        PortletHome portletHome = new TestPortletHome( );
        portletHome.create( portlet );
        return portlet;
    }

    /**
     * Test portlet
     */
    private static final class TestPortlet extends Portlet
    {

        /**
         * Constructor
         * @param type the portlet type
         */
        TestPortlet( PortletType type )
        {
            setPortletTypeId( type.getId(  ) );
        }

        @Override
        public String getXml( HttpServletRequest request ) throws SiteMessageException
        {
            return null;
        }

        @Override
        public String getXmlDocument( HttpServletRequest request ) throws SiteMessageException
        {
            return null;
        }

        @Override
        public void remove( )
        {
        }

    }

    /**
     * The test portlet home
     */
    public static final class TestPortletHome extends PortletHome
    {

        @Override
        public IPortletInterfaceDAO getDAO( )
        {
            return new TestPortletInterfaceDAO(  );
        }

        @Override
        public String getPortletTypeId( )
        {
            throw new UnsupportedOperationException( );
        }
    }

    /**
     * The test portlet DAO
     */
    private static final class TestPortletInterfaceDAO implements IPortletInterfaceDAO
    {

        private static final Map<Integer, Portlet> _storage = new HashMap<>( );

        @Override
        public void insert( Portlet portlet )
        {
            _storage.put( portlet.getId( ), portlet );
        }

        @Override
        public void delete( int nPortletId )
        {
            _storage.remove( nPortletId );
        }

        @Override
        public Portlet load( int nPortletId )
        {
            return _storage.get( nPortletId );
        }

        @Override
        public void store( Portlet portlet )
        {
            _storage.put( portlet.getId( ), portlet );
        }

    }

}
