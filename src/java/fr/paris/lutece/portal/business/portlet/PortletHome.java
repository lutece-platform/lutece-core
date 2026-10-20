/*
 * Copyright (c) 2002-2022, City of Paris
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

import fr.paris.lutece.portal.service.portlet.PortletEvent;
import fr.paris.lutece.portal.service.portlet.PortletEventListener;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import jakarta.enterprise.inject.spi.CDI;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Portlet objects
 */
public abstract class PortletHome implements PortletHomeInterface
{
    private static final String PROPERTY_PORTLET_CREATION_STATUS = "lutece.portlet.creation.status";
    private static final int CONSTANT_DEFAULT_STATUS = Portlet.STATUS_PUBLISHED;

    // Static variable pointed at the DAO instance
    private static IPortletDAO _dao = CDI.current( ).select( IPortletDAO.class ).get( );
    private static IPortletStyleDAO _styleDAO = CDI.current( ).select( IPortletStyleDAO.class ).get( );

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns the Portlet whose primary key is specified in parameter
     *
     * @param nKey
     *            the portlet identifier
     * @return The portlet object, or null if the portlet does not exist
     */
    public static Portlet findByPrimaryKey( int nKey )
    {
        Portlet portlet = _dao.load( nKey );

        if ( ( portlet == null ) || ( portlet.getHomeClassName( ) == null ) )
        {
            // unknown portlet
            return null;
        }

        String strHomeClass = portlet.getHomeClassName( );
        Portlet p = null;

        try
        {
            PortletHomeInterface home = (PortletHomeInterface) Class.forName( strHomeClass ).getDeclaredConstructor().newInstance( );
            p = home.getDAO( ).load( nKey );
            p.copy( portlet );
        }
        catch( IllegalAccessException | InstantiationException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

        return p;
    }

    /**
     * Returns a collection of portlets according to the selected type
     *
     * @param strIdPortletType
     *            the portlet type
     * @return the portlets in form of Collection
     */
    public static List<Portlet> findByType( String strIdPortletType )
    {
        return _dao.selectPortletsByType( strIdPortletType );
    }

    /**
     * Returns the list of portlets for the search on publishing
     *
     * @param strPortletName
     *            STh name of the portlet
     * @return the list in form of Collection
     */
    public static Collection<PortletImpl> getPortletsListbyName( String strPortletName )
    {
        return _dao.selectPortletsListbyName( strPortletName );
    }

    /**
     * Returns the stylesheet content of the portlet according to the mode
     *
     * @param nIdPortlet
     *            the identifier of the portlet
     * @param nIdMode
     *            the selected mode
     * @return the stylesheet content
     */
    public static String getXslFile( int nIdPortlet, int nIdMode )
    {
       return _styleDAO.selectXslFile( nIdPortlet, nIdMode );
    }

    /**
     * Returns the stylesheet binary content of the portlet according to the mode
     *
     * @param nIdPortlet
     *            the identifier of the portlet
     * @param nIdMode
     *            the selected mode
     * @return the stylesheet binary content
     */
    public static byte [ ] getXslSource( int nIdPortlet, int nIdMode )
    {
       return _styleDAO.selectXslSource( nIdPortlet, nIdMode );
    }

    /**
     * Returns all the styles corresponding to a portlet typeun type de portlet
     *
     * @param strIdPortletType
     *            the identifier of the portlet type
     * @return the list of styles in form of ReferenceList
     */
    public static ReferenceList getStylesList( String strIdPortletType )
    {
        return _styleDAO.selectStylesList( strIdPortletType );
    }

    /**
     * Gets a collection of portlets associated to a given role
     * 
     * @param strRole
     *            The role
     * @return The collection
     */
    public static Collection<Portlet> getPortletsByRoleKey( String strRole )
    {
        return _dao.selectPortletsByRole( strRole );
    }

    /**
     * Creates a new portlet in the database
     *
     * @param portlet
     *            An instance of the portlet to create
     * @return the Portlet instance created
     */
    public synchronized Portlet create( Portlet portlet )
    {
        portlet.setStatus( AppPropertiesService.getPropertyInt( PROPERTY_PORTLET_CREATION_STATUS, CONSTANT_DEFAULT_STATUS ) );

        // Creation of the portlet parent
        _dao.insert( portlet );

        // Creation of the portlet child
        getDAO( ).insert( portlet );

        // Its order is a place in its column: the portlets from that place down move one step, the column keeps the orders 1 to n
        placeInColumn( portlet, portlet.getPageId( ), portlet.getColumn( ), portlet.getColumn( ), portlet.getOrder( ) );

        // Invalidate the portlet
        invalidate( portlet );

        return portlet;
    }

    /**
     * Deletes the portlet in the database. The column it leaves closes the gap: it keeps the orders 1 to n, which the preview sends back as
     * places
     *
     * @param portlet
     *            the portlet to remove
     */
    public synchronized void remove( Portlet portlet )
    {
        Portlet former = _dao.load( portlet.getId( ) );

        // Deleting of the portlet child
        getDAO( ).delete( portlet.getId( ) );

        // Deleting of the portlet parent and its alias if exist
        _dao.delete( portlet.getId( ) );

        if ( former.getId( ) == portlet.getId( ) )
        {
            renumberColumn( getColumnWithout( former.getPageId( ), former.getColumn( ), portlet ), former.getColumn( ) );
        }

        // Invalidate the portlet
        invalidate( portlet );
    }

    /**
     * Updates a portlet with the values of the specified portlet instance. When it moves to another page, that page is invalidated too, as
     * the page it joins
     *
     * @param portlet
     *            portlet to update
     */
    public void update( Portlet portlet )
    {
        // The position stored before, to tell whether the portlet moves
        Portlet former = _dao.load( portlet.getId( ) );

        getDAO( ).store( portlet );
        _dao.store( portlet );

        // A new page, column or order is a place in the column, as on creation: the column it leaves closes the gap
        if ( former.getId( ) == portlet.getId( ) && ( former.getPageId( ) != portlet.getPageId( ) || former.getColumn( ) != portlet.getColumn( )
                || former.getOrder( ) != portlet.getOrder( ) ) )
        {
            placeInColumn( portlet, former.getPageId( ), former.getColumn( ), portlet.getColumn( ), portlet.getOrder( ) );
        }

        // Invalidate the portlet
        invalidate( portlet );

        if ( former.getId( ) == portlet.getId( ) && former.getPageId( ) != portlet.getPageId( ) )
        {
            notifyListeners( new PortletEvent( PortletEvent.INVALIDATE, portlet.getId( ), former.getPageId( ) ) );
        }
    }

    /**
     * Invalidate the portlet which is specified in parameter Invalidates the alias portlets connected to this portlet too.
     *
     * @param portlet
     *            the portlet instance
     */
    public static void invalidate( Portlet portlet )
    {
        PortletEvent event = new PortletEvent( PortletEvent.INVALIDATE, portlet.getId( ), portlet.getPageId( ) );
        notifyListeners( event );

        // invalidate aliases
        Collection<Portlet> listAliases = getAliasList( portlet.getId( ) );

        for ( Portlet alias : listAliases )
        {
            PortletEvent eventAlias = new PortletEvent( PortletEvent.INVALIDATE, alias.getId( ), alias.getPageId( ) );
            notifyListeners( eventAlias );
        }
    }

    /**
     * Invalidate a portlet whose identifier is specified in paramaeter
     *
     * @param nIdPortlet
     *            the portlet identifier
     */
    public static void invalidate( int nIdPortlet )
    {
        Portlet portlet = findByPrimaryKey( nIdPortlet );
        if ( portlet != null )
        {
            invalidate( portlet );
        }
    }

    /**
     * Indicates if the portlet has alias
     *
     * @param nIdPortlet
     *            the portlet identifier
     * @return true if the portlet has alias, false if not.
     */
    public static boolean hasAlias( int nIdPortlet )
    {
        return _dao.hasAlias( nIdPortlet );
    }

    /**
     * Update the status of portlet
     *
     * @param portlet
     *            the portlet to remove
     * @param nStatus
     *            The status to update
     */
    public static void updateStatus( Portlet portlet, int nStatus )
    {
        // Deleting of the portlet child
        _dao.updateStatus( portlet, nStatus );

        // Invalidate the portlet
        invalidate( portlet );
    }
    /**
     * Update the status of portlet
     *
     * @param portlet
     *            the portlet to remove
     * @param nStatus
     *            The status to update
     */
    public static void updatePosition( Portlet portlet, int nColumn, int nOrder )
    {
        // Deleting of the portlet child
        _dao.updatePosition( portlet, nColumn, nOrder );

        // Invalidate the portlet
        invalidate( portlet );
    }

    /**
     * Puts a portlet at a place of a column of its page. The portlets of that column keep their order around it and take the orders 1
     * to n; the column it leaves, if it changes column, closes the gap. Only the portlets whose position changes are updated, and the page
     * is invalidated once
     *
     * @param portlet
     *            the portlet, with its current column; its column and its order are set to the new ones
     * @param nColumn
     *            the column
     * @param nOrder
     *            the place in the column, from 1; beyond the last portlet, the portlet goes last
     */
    public static void placeInColumn( Portlet portlet, int nColumn, int nOrder )
    {
        placeInColumn( portlet, portlet.getPageId( ), portlet.getColumn( ), nColumn, nOrder );

        invalidate( portlet );
    }

    /**
     * The portlets of a column of a page, in their order, with only their identifier, name, page, column and order loaded
     *
     * @param nPageId
     *            the page
     * @param nColumn
     *            the column
     * @return the portlets of the column
     */
    public static List<Portlet> getColumnPositions( int nPageId, int nColumn )
    {
        return _dao.selectColumnPositions( nPageId, nColumn );
    }

    /**
     * The portlets of a page, by column and in their order, with only their identifier, type, name, page, column and order loaded
     *
     * @param nPageId
     *            the page
     * @return the portlets of the page
     */
    public static List<Portlet> getPagePositions( int nPageId )
    {
        return _dao.selectPagePositions( nPageId );
    }

    /**
     * Puts a portlet at a place of a column of its page, coming from another column, possibly of another page, or from the same one. It
     * invalidates nothing: the caller invalidates the portlet, and so its page, once
     *
     * @param portlet
     *            the portlet, with its new page; its column and its order are set to the new ones
     * @param nFormerPageId
     *            the page the portlet comes from
     * @param nFormerColumn
     *            the column the portlet comes from, which closes the gap when it is not the new one
     * @param nColumn
     *            the column
     * @param nOrder
     *            the place in the column, from 1; beyond the last portlet, the portlet goes last
     */
    private static void placeInColumn( Portlet portlet, int nFormerPageId, int nFormerColumn, int nColumn, int nOrder )
    {
        List<Portlet> listColumn = getColumnWithout( portlet.getPageId( ), nColumn, portlet );
        listColumn.add( Math.max( 0, Math.min( nOrder - 1, listColumn.size( ) ) ), portlet );
        renumberColumn( listColumn, nColumn );

        if ( nFormerPageId != portlet.getPageId( ) || nFormerColumn != nColumn )
        {
            renumberColumn( getColumnWithout( nFormerPageId, nFormerColumn, portlet ), nFormerColumn );
        }
    }

    /**
     * The portlets of a column of a page, in their order, without a portlet
     *
     * @param nPageId
     *            the page
     * @param nColumn
     *            the column
     * @param portlet
     *            the portlet left out
     * @return the portlets of the column
     */
    private static List<Portlet> getColumnWithout( int nPageId, int nColumn, Portlet portlet )
    {
        List<Portlet> listColumn = _dao.selectColumnPositions( nPageId, nColumn );
        listColumn.removeIf( columnPortlet -> columnPortlet.getId( ) == portlet.getId( ) );

        return listColumn;
    }

    /**
     * Gives the portlets of a column the orders 1 to n, in the order of the list, updating only the portlets whose position changes. A
     * position is not a content: it invalidates nothing, the page of the column is invalidated once by the caller
     *
     * @param listColumn
     *            the portlets of the column, in their new order
     * @param nColumn
     *            the column
     */
    private static void renumberColumn( List<Portlet> listColumn, int nColumn )
    {
        for ( int i = 0; i < listColumn.size( ); i++ )
        {
            Portlet columnPortlet = listColumn.get( i );

            if ( columnPortlet.getColumn( ) != nColumn || columnPortlet.getOrder( ) != i + 1 )
            {
                _dao.updatePosition( columnPortlet, nColumn, i + 1 );
                columnPortlet.setColumn( nColumn );
                columnPortlet.setOrder( i + 1 );
            }
        }
    }

    /**
     * Returns the instance of the PortletType whose identifier is specified in parameter
     *
     * @param strPortletTypeId
     *            the identifier of the portlet type
     * @return the instance of the portlet type
     */
    public static PortletType getPortletType( String strPortletTypeId )
    {
        return _dao.selectPortletType( strPortletTypeId );
    }

    /**
     * Returns the collection of the StyleSheet objects associated to the Style
     *
     * @param nStyleId
     *            identifier of the style
     * @return A collection of styles
     */
    public static Collection<PortletImpl> getPortletListByStyle( int nStyleId )
    {
        return _dao.selectPortletListByStyle( nStyleId );
    }

    /**
     * Returns the collection of the StyleSheet objects associated to the Style
     *
     * @param nPortletId
     *            identifier of the portlet
     * @return A collection of styles
     */
    public static Collection<Portlet> getAliasList( int nPortletId )
    {
        return _dao.selectAliasesForPortlet( nPortletId );
    }

    /**
     * Get the last modified portlet
     * 
     * @return the last modified portlet
     */
    public static Portlet getLastModifiedPortlet( )
    {
        return _dao.loadLastModifiedPortlet( );
    }

    /**
     * Notifies listeners
     * 
     * @param event
     *            the event
     */
    public static void notifyListeners( PortletEvent event )
    {
        CDI.current( ).getBeanManager( ).getEvent( ).fire( event );

        // The following lines are for backward compatibility with PortletEventListener mechanism and will be removed in next release
        CDI.current().select(PortletEventListener.class).forEach(
            listener -> listener.processPortletEvent( event )
            );
    }

    /**
     * Get list of used orders for a column
     * 
     * @param pageId
     *            the page id
     * @param columnId
     *            the column id
     * @return list of orders used for this column
     */
    public static List<Integer> getUsedOrdersForColumns( int pageId, int columnId )
    {
        return _dao.getUsedOrdersForColumns( pageId, columnId );
    }
}
