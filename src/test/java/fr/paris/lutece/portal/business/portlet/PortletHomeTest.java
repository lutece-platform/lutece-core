/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.portal.business.portlet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServletRequest;

/**
 * PortletHome Test Class: the place of a portlet in the columns of its page, on creation, move and modification
 */
public class PortletHomeTest extends LuteceTestCase
{
    // A page without portlets: core_portlet has no foreign key on core_page
    private static final int TEST_PAGE_ID = 987654;
    private static final int OTHER_PAGE_ID = 987655;
    private static final int COLUMN_1 = 1;
    private static final int COLUMN_2 = 2;

    // Created once the test container runs: PortletHome looks up its DAO in CDI when it loads
    private PortletHome _home;
    private final List<Portlet> _listCreated = new ArrayList<>( );
    private PortletType _portletType;

    @BeforeEach
    public void createPortletType( )
    {
        _home = new TestPortletHome( );
        _portletType = new PortletType( );
        _portletType.setId( "TEST_" + new BigInteger( 40, new SecureRandom( ) ).toString( 32 ) );
        _portletType.setHomeClass( TestPortletHome.class.getName( ) );
        PortletTypeHome.create( _portletType );
    }

    @AfterEach
    public void removePortlets( )
    {
        _listCreated.forEach( _home::remove );
        PortletTypeHome.remove( _portletType.getId( ) );
    }

    @Test
    public void testCreateTakesItsPlace( )
    {
        Portlet a = createPortlet( COLUMN_1, 1 );
        Portlet b = createPortlet( COLUMN_1, 2 );
        Portlet c = createPortlet( COLUMN_1, 3 );
        assertColumn( COLUMN_1, a, b, c );

        // Created at the place of b: b and c move one step down
        Portlet d = createPortlet( COLUMN_1, 2 );
        assertColumn( COLUMN_1, a, d, b, c );
        assertEquals( 2, d.getOrder( ), "the created portlet carries its order" );

        // Beyond the last portlet: last, without a gap
        Portlet e = createPortlet( COLUMN_1, 20 );
        assertColumn( COLUMN_1, a, d, b, c, e );

        // Before the first one
        Portlet f = createPortlet( COLUMN_1, 0 );
        assertColumn( COLUMN_1, f, a, d, b, c, e );
    }

    @Test
    public void testRemoveClosesTheGap( )
    {
        Portlet a = createPortlet( COLUMN_1, 1 );
        Portlet b = createPortlet( COLUMN_1, 2 );
        Portlet c = createPortlet( COLUMN_1, 3 );

        _home.remove( a );
        _listCreated.remove( a );
        assertColumn( COLUMN_1, b, c );

        _home.remove( c );
        _listCreated.remove( c );
        assertColumn( COLUMN_1, b );
    }

    @Test
    public void testPlaceInTheSameColumn( )
    {
        Portlet a = createPortlet( COLUMN_1, 1 );
        Portlet b = createPortlet( COLUMN_1, 2 );
        Portlet c = createPortlet( COLUMN_1, 3 );

        PortletHome.placeInColumn( c, COLUMN_1, 1 );
        assertColumn( COLUMN_1, c, a, b );

        PortletHome.placeInColumn( c, COLUMN_1, 3 );
        assertColumn( COLUMN_1, a, b, c );

        PortletHome.placeInColumn( a, COLUMN_1, 2 );
        assertColumn( COLUMN_1, b, a, c );
        assertEquals( COLUMN_1, a.getColumn( ) );
        assertEquals( 2, a.getOrder( ), "the moved portlet carries its new order" );
    }

    @Test
    public void testPlaceInAnotherColumn( )
    {
        Portlet a = createPortlet( COLUMN_1, 1 );
        Portlet b = createPortlet( COLUMN_1, 2 );
        Portlet c = createPortlet( COLUMN_1, 3 );
        Portlet x = createPortlet( COLUMN_2, 1 );

        // Into the column 2, before x; the column 1 closes the gap
        PortletHome.placeInColumn( b, COLUMN_2, 1 );
        assertColumn( COLUMN_1, a, c );
        assertColumn( COLUMN_2, b, x );
        assertEquals( COLUMN_2, b.getColumn( ) );
        assertEquals( 1, b.getOrder( ) );

        // Into a place beyond the end of the column 2: last
        PortletHome.placeInColumn( a, COLUMN_2, 9 );
        assertColumn( COLUMN_1, c );
        assertColumn( COLUMN_2, b, x, a );

        // The last portlet of the column 1 leaves it empty
        PortletHome.placeInColumn( c, COLUMN_2, 2 );
        assertColumn( COLUMN_1 );
        assertColumn( COLUMN_2, b, c, x, a );
    }

    @Test
    public void testPlaceRenumbersAColumnWithGaps( )
    {
        Portlet a = createPortlet( COLUMN_1, 1 );
        Portlet b = createPortlet( COLUMN_1, 2 );
        Portlet c = createPortlet( COLUMN_1, 3 );

        // Orders left with gaps and a tie, as an older version could leave them
        PortletHome.updatePosition( a, COLUMN_1, 2 );
        PortletHome.updatePosition( b, COLUMN_1, 5 );
        PortletHome.updatePosition( c, COLUMN_1, 5 );

        Portlet d = createPortlet( COLUMN_1, 2 );
        assertColumn( COLUMN_1, a, d, b, c );
    }

    @Test
    public void testUpdateWithANewPosition( )
    {
        Portlet a = createPortlet( COLUMN_1, 1 );
        Portlet b = createPortlet( COLUMN_1, 2 );
        Portlet c = createPortlet( COLUMN_1, 3 );
        Portlet x = createPortlet( COLUMN_2, 1 );

        // The modify form: a new order in the same column
        c.setOrder( 1 );
        _home.update( c );
        assertColumn( COLUMN_1, c, a, b );

        // The modify form: another column; the column left closes the gap
        a.setColumn( COLUMN_2 );
        a.setOrder( 1 );
        _home.update( a );
        assertColumn( COLUMN_1, c, b );
        assertColumn( COLUMN_2, a, x );

        // The same position: nothing moves
        b.setName( "renamed" );
        _home.update( b );
        assertColumn( COLUMN_1, c, b );
        assertColumn( COLUMN_2, a, x );
    }

    @Test
    public void testUpdateToAnotherPage( )
    {
        Portlet a = createPortlet( COLUMN_1, 1 );
        Portlet b = createPortlet( COLUMN_1, 2 );
        Portlet c = createPortlet( COLUMN_1, 3 );
        Portlet y = createPortlet( OTHER_PAGE_ID, COLUMN_2, 1 );

        // The modify form: another page; the column it leaves on the first page closes the gap
        a.setPageId( OTHER_PAGE_ID );
        a.setColumn( COLUMN_2 );
        a.setOrder( 9999 );
        _home.update( a );
        assertColumn( COLUMN_1, b, c );
        assertColumn( OTHER_PAGE_ID, COLUMN_2, y, a );

        // The same column number on another page is another column
        b.setPageId( OTHER_PAGE_ID );
        b.setOrder( 1 );
        _home.update( b );
        assertColumn( COLUMN_1, c );
        assertColumn( OTHER_PAGE_ID, COLUMN_1, b );
        assertColumn( OTHER_PAGE_ID, COLUMN_2, y, a );
    }

    /**
     * Creates a test portlet on the test page
     *
     * @param nColumn
     *            the column
     * @param nOrder
     *            the order
     * @return the portlet
     */
    private Portlet createPortlet( int nColumn, int nOrder )
    {
        return createPortlet( TEST_PAGE_ID, nColumn, nOrder );
    }

    /**
     * Creates a test portlet
     *
     * @param nPageId
     *            the page
     * @param nColumn
     *            the column
     * @param nOrder
     *            the order
     * @return the portlet
     */
    private Portlet createPortlet( int nPageId, int nColumn, int nOrder )
    {
        Portlet portlet = new TestPortlet( _portletType );
        portlet.setPageId( nPageId );
        portlet.setName( _portletType.getId( ) + "_" + _listCreated.size( ) );
        portlet.setColumn( nColumn );
        portlet.setOrder( nOrder );
        _home.create( portlet );
        _listCreated.add( portlet );

        return portlet;
    }

    /**
     * Checks the portlets stored in a column of the test page: these ones, in this order, with the orders 1 to n
     *
     * @param nColumn
     *            the column
     * @param expected
     *            the portlets expected, in their order
     */
    private static void assertColumn( int nColumn, Portlet... expected )
    {
        assertColumn( TEST_PAGE_ID, nColumn, expected );
    }

    /**
     * Checks the portlets stored in a column of a page: these ones, in this order, with the orders 1 to n
     *
     * @param nPageId
     *            the page
     * @param nColumn
     *            the column
     * @param expected
     *            the portlets expected, in their order
     */
    private static void assertColumn( int nPageId, int nColumn, Portlet... expected )
    {
        List<Portlet> listStored = CDI.current( ).select( IPortletDAO.class ).get( ).selectColumnPositions( nPageId, nColumn );

        List<String> listExpected = new ArrayList<>( );
        for ( int i = 0; i < expected.length; i++ )
        {
            listExpected.add( expected [i].getId( ) + "@" + ( i + 1 ) );
        }
        List<String> listActual = new ArrayList<>( );
        for ( Portlet stored : listStored )
        {
            listActual.add( stored.getId( ) + "@" + stored.getOrder( ) );
        }

        assertEquals( listExpected, listActual, "portlet id@order of the column " + nColumn + " of the page " + nPageId );
    }

    /**
     * Test portlet
     */
    private static final class TestPortlet extends Portlet
    {
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
        }
    }

    /**
     * The test portlet home, its own data kept in memory
     */
    public static final class TestPortletHome extends PortletHome
    {
        private static final Map<Integer, Portlet> _mapStorage = new HashMap<>( );

        @Override
        public IPortletInterfaceDAO getDAO( )
        {
            return new IPortletInterfaceDAO( )
            {
                @Override
                public void insert( Portlet portlet )
                {
                    _mapStorage.put( portlet.getId( ), portlet );
                }

                @Override
                public void delete( int nPortletId )
                {
                    _mapStorage.remove( nPortletId );
                }

                @Override
                public Portlet load( int nPortletId )
                {
                    return _mapStorage.get( nPortletId );
                }

                @Override
                public void store( Portlet portlet )
                {
                    _mapStorage.put( portlet.getId( ), portlet );
                }
            };
        }

        @Override
        public String getPortletTypeId( )
        {
            throw new UnsupportedOperationException( );
        }
    }
}
