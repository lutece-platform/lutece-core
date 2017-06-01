package fr.paris.lutece.portal.web.admin;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.portlet.AliasPortlet;
import fr.paris.lutece.portal.business.portlet.AliasPortletHome;
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
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.portlet.PortletRemovalListenerService;
import fr.paris.lutece.portal.service.portlet.PortletResourceIdService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.RemovalListener;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

/**
 * Test the AdminPagePortletJspBean class
 */
public class AdminPagePortletJspBeanTest extends LuteceTestCase
{

    /** status request parameter */
    private static final String PORTLET_STATUS = "status";

    /**
     * Test when no parameter given
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetModifyPortletStatusNoParam( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String url = bean.getModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when no status parameter given
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetModifyPortletStatusNoStatusParam( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PORTLET_ID, "1" );
        String url = bean.getModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when no portlet_id parameter given
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetModifyPortletStatusNoPortletParam( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
        String url = bean.getModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the status is invalid
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetModifyPortletStatusInvalidStatus( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( PORTLET_STATUS, "999999999" );
            String url = bean.getModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        }
        finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the portlet_id is invalid
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetModifyPortletStatusInvalidPortletID( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PORTLET_ID, "NOT_NUMERIC" );
        request.addParameter( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
        String url = bean.getModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the portlet does not exist
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetModifyPortletStatusInexistantPortletID( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addParameter( Parameters.PORTLET_ID, "31415925" );
            request.addParameter( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
            String url = bean.getModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        }
        finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the user does not have the right ro manage portlets
     */
    public void testGetModifyPortletStatusNoRight( )
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
            Utils.registerAdminUser( request, new AdminUser( ) );
            bean.getModifyPortletStatus( request );
            fail( "Should not have been able to modify the portlet" );
        }
        catch( AccessDeniedException e )
        {
        }
        finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when all conditions are met
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetModifyPortletStatus( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            user = getAdminUser( );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
            Utils.registerAdminUser( request, user );
            String url = bean.getModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertNotNull( message.getRequestParameters( ).get( SecurityTokenService.PARAMETER_TOKEN ) );
            assertEquals( message.getType( ), AdminMessage.TYPE_CONFIRMATION );
        }
        finally
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
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoModifyPortletStatusNoParam( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoModifyPortletStatus.jsp" ) );
        String url = bean.doModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when no status parameter given
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoModifyPortletStatusNoStatusParam( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoModifyPortletStatus.jsp" ) );
        request.addParameter( Parameters.PORTLET_ID, "1" );
        String url = bean.doModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when no portlet_id parameter given
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoModifyPortletStatusNoPortletParam( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoModifyPortletStatus.jsp" ) );
        String url = bean.doModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the status is invalid
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoModifyPortletStatusInvalidStatus( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoModifyPortletStatus.jsp" ) );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( PORTLET_STATUS, "999999999" );
            String url = bean.doModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        }
        finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the portlet_id is invalid
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoModifyPortletStatusInvalidPortletID( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoModifyPortletStatus.jsp" ) );
        request.addParameter( Parameters.PORTLET_ID, "NOT_NUMERIC" );
        request.addParameter( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
        String url = bean.doModifyPortletStatus( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the portlet does not exist
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoModifyPortletStatusInexistantPortletID( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoModifyPortletStatus.jsp" ) );
            request.addParameter( Parameters.PORTLET_ID, "31415925" );
            request.addParameter( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
            String url = bean.doModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        }
        finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the user does not have the right ro manage portlets
     */
    public void testDoModifyPortletStatusNoRight( )
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoModifyPortletStatus.jsp" ) );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( PORTLET_STATUS, Integer.toString( Portlet.STATUS_PUBLISHED ) );
            Utils.registerAdminUser( request, new AdminUser( ) );
            bean.doModifyPortletStatus( request );
            fail( "Should not have been able to modify the portlet" );
        }
        catch( AccessDeniedException e )
        {
        }
        finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when all conditions are met
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoModifyPortletStatus( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            int nStatus = portlet.getStatus( );
            int nNewStatus = nStatus == Portlet.STATUS_PUBLISHED ? Portlet.STATUS_UNPUBLISHED : Portlet.STATUS_PUBLISHED;
            user = getAdminUser( );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoModifyPortletStatus.jsp" ) );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( PORTLET_STATUS, Integer.toString( nNewStatus ) );
            Utils.registerAdminUser( request, user );
            String url = bean.doModifyPortletStatus( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNull( message );
            Portlet storedPortlet = PortletHome.findByPrimaryKey( portlet.getId( ) );
            assertNotNull( storedPortlet );
            assertEquals( nNewStatus, storedPortlet.getStatus( ) );
        }
        finally
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
     * Test when no token
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoModifyPortletStatusNoCSRFToken( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        int nStatus = 0;
        try
        {
            portlet = getPortlet( );
            nStatus = portlet.getStatus( );
            int nNewStatus = nStatus == Portlet.STATUS_PUBLISHED ? Portlet.STATUS_UNPUBLISHED : Portlet.STATUS_PUBLISHED;
            user = getAdminUser( );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( PORTLET_STATUS, Integer.toString( nNewStatus ) );
            Utils.registerAdminUser( request, user );
            bean.doModifyPortletStatus( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            Portlet storedPortlet = PortletHome.findByPrimaryKey( portlet.getId( ) );
            assertNotNull( storedPortlet );
            assertEquals( nStatus, storedPortlet.getStatus( ) );
        }
        finally
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
     * Test when invalid token
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoModifyPortletStatusInvalidCSRFToken( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        int nStatus = 0;
        try
        {
            portlet = getPortlet( );
            nStatus = portlet.getStatus( );
            int nNewStatus = nStatus == Portlet.STATUS_PUBLISHED ? Portlet.STATUS_UNPUBLISHED : Portlet.STATUS_PUBLISHED;
            user = getAdminUser( );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoModifyPortletStatus.jsp" ) + "b" );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( PORTLET_STATUS, Integer.toString( nNewStatus ) );
            Utils.registerAdminUser( request, user );
            bean.doModifyPortletStatus( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            Portlet storedPortlet = PortletHome.findByPrimaryKey( portlet.getId( ) );
            assertNotNull( storedPortlet );
            assertEquals( nStatus, storedPortlet.getStatus( ) );
        }
        finally
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
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetRemovePortletNoParam( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String url = bean.getRemovePortlet( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the portlet_id is invalid
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetRemovePortletInvalidPortletID( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PORTLET_ID, "NOT_NUMERIC" );
        String url = bean.getRemovePortlet( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the portlet does not exist
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetRemovePortletInexistantPortletID( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addParameter( Parameters.PORTLET_ID, "31415925" );
            String url = bean.getRemovePortlet( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        }
        finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the user does not have the right ro manage portlets
     */
    public void testGetRemovePortletNoRight( )
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            Utils.registerAdminUser( request, new AdminUser( ) );
            bean.getRemovePortlet( request );
            fail( "Should not have been able to modify the portlet" );
        }
        catch( AccessDeniedException e )
        {
        }
        finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when all conditions are met
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetRemovePortlet( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            user = getAdminUser( );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            Utils.registerAdminUser( request, user );
            String url = bean.getRemovePortlet( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_CONFIRMATION );
            assertNotNull( message.getRequestParameters( ).get( SecurityTokenService.PARAMETER_TOKEN ) );
            ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
            for ( ReferenceItem lang : listLanguages )
            {
                assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( portlet.getName( ) ) );
            }
        }
        finally
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
     * Test when all conditions are met
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetRemovePortletWithAlias( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            getAlias( portlet );
            user = getAdminUser( );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            Utils.registerAdminUser( request, user );
            String url = bean.getRemovePortlet( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertNotNull( message.getRequestParameters( ).get( SecurityTokenService.PARAMETER_TOKEN ) );
            assertEquals( message.getType( ), AdminMessage.TYPE_CONFIRMATION );
            ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
            for ( ReferenceItem lang : listLanguages )
            {
                assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( portlet.getName( ) ) );
            }
        }
        finally
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
     * Test when all conditions are met
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testGetRemovePortletWithPortletRemovalListener( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            user = getAdminUser( );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            Utils.registerAdminUser( request, user );
            final int nPortletId = portlet.getId( );
            final String removalRefusedMessage = "REMOVAL_REFUSED_" + nPortletId;
            PortletRemovalListenerService.getService( ).registerListener( new RemovalListener( )
            {
                // removalListener cannot be unregistered. Try not to interfere with other tests
                private boolean first = true;

                @Override
                public String getRemovalRefusedMessage( String id, Locale locale )
                {
                    return removalRefusedMessage;
                }

                @Override
                public boolean canBeRemoved( String id )
                {
                    // always allow removal after first use
                    boolean res = !first || !id.equals( Integer.toString( nPortletId ) );
                    first = false;
                    return res;
                }
            } );
            String url = bean.getRemovePortlet( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertNotNull( message.getRequestParameters( ).get( SecurityTokenService.PARAMETER_TOKEN ) );
            assertEquals( message.getType( ), AdminMessage.TYPE_STOP );
            ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
            for ( ReferenceItem lang : listLanguages )
            {
                assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( portlet.getName( ) ) );
                assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( removalRefusedMessage ) );
            }
        }
        finally
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
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoRemovePortletNoParam( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( )
                .getToken( request, "jsp/admin/site/DoRemovePortlet.jsp" ) );
        String url = bean.doRemovePortlet( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the portlet_id is invalid
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoRemovePortletInvalidPortletID( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( )
                .getToken( request, "jsp/admin/site/DoRemovePortlet.jsp" ) );
        request.addParameter( Parameters.PORTLET_ID, "NOT_NUMERIC" );
        String url = bean.doRemovePortlet( request );
        assertNotNull( url );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
    }

    /**
     * Test when the portlet does not exist
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoRemovePortletInexistantPortletID( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addParameter( Parameters.PORTLET_ID, "31415925" );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoRemovePortlet.jsp" ) );
            String url = bean.doRemovePortlet( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( message.getType( ), AdminMessage.TYPE_ERROR );
        }
        finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when the user does not have the right ro manage portlets
     */
    public void testDoRemovePortletNoRight( )
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = getPortlet( );
        try
        {
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoRemovePortlet.jsp" ) );
            Utils.registerAdminUser( request, new AdminUser( ) );
            bean.doRemovePortlet( request );
            fail( "Should not have been able to modify the portlet" );
        }
        catch( AccessDeniedException e )
        {
        }
        finally
        {
            removePortlet( portlet );
        }
    }

    /**
     * Test when all conditions are met
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoRemovePortlet( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            user = getAdminUser( );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoRemovePortlet.jsp" ) );
            Utils.registerAdminUser( request, user );
            String url = bean.doRemovePortlet( request );
            assertNotNull( url );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNull( message );
            try
            {
                portlet = PortletHome.findByPrimaryKey( portlet.getId( ) );
                fail( "Portlet was not removed" );
            }
            catch( NullPointerException e )
            {
                portlet = null;
            }
        }
        finally
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
     * Test when no CSRF token
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoRemovePortletNoCSRFToken( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            user = getAdminUser( );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            Utils.registerAdminUser( request, user );
            bean.doRemovePortlet( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( PortletHome.findByPrimaryKey( portlet.getId( ) ) );
        }
        finally
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
     * Test when invalid CSRF token
     * 
     * @throws AccessDeniedException
     *             should not happen
     */
    public void testDoRemovePortletInvalidCSRFToken( ) throws AccessDeniedException
    {
        AdminPagePortletJspBean bean = new AdminPagePortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Portlet portlet = null;
        AdminUser user = null;
        try
        {
            portlet = getPortlet( );
            user = getAdminUser( );
            request.addParameter( Parameters.PORTLET_ID, Integer.toString( portlet.getId( ) ) );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoRemovePortlet.jsp" ) + "b" );
            Utils.registerAdminUser( request, user );
            bean.doRemovePortlet( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( PortletHome.findByPrimaryKey( portlet.getId( ) ) );
        }
        finally
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
     * 
     * @return the admin user
     */
    private AdminUser getAdminUser( )
    {
        String strRoleKey = "ROLE_" + new BigInteger( 40, new SecureRandom( ) ).toString( 32 );
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
     * 
     * @param user
     *            the test user
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
     * 
     * @param portlet
     *            the test portlet
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
     * 
     * @return a test portlet
     */
    private Portlet getPortlet( )
    {
        PortletType portletType = new PortletType( );
        String strPortletTypeID = "TEST_" + new BigInteger( 40, new SecureRandom( ) ).toString( 32 );
        portletType.setId( strPortletTypeID );
        portletType.setHomeClass( TestPortletHome.class.getName( ) );
        PortletTypeHome.create( portletType );
        Portlet portlet = new TestPortlet( portletType );
        portlet.setStatus( Portlet.STATUS_UNPUBLISHED );
        portlet.setName( strPortletTypeID );
        portlet.setAcceptAlias( 1 );
        PortletHome portletHome = new TestPortletHome( );
        portletHome.create( portlet );
        return portlet;
    }

    private Portlet getAlias( Portlet portlet )
    {
        AliasPortlet aliasPortlet = new AliasPortlet( );
        aliasPortlet.setPageId( portlet.getPageId( ) );
        aliasPortlet.setName( "ALIAS_" + portlet.getName( ) );
        aliasPortlet.setAliasId( portlet.getId( ) );
        aliasPortlet.setStyleId( portlet.getStyleId( ) );
        AliasPortletHome.getInstance( ).create( aliasPortlet );
        return aliasPortlet;
    }

    /**
     * Test portlet
     */
    private static final class TestPortlet extends Portlet
    {

        /**
         * Constructor
         * 
         * @param type
         *            the portlet type
         */
        TestPortlet( PortletType type )
        {
            setPortletTypeId( type.getId( ) );
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
            new TestPortletHome( ).remove( this );
            ;
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
            return new TestPortletInterfaceDAO( );
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
