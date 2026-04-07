/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.web.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a menu item in the portal menu structure. Each MenuItem can have child menu items, forming a tree structure.
 * <p>
 * This class provides information about the menu item such as its page ID, parent ID, name, description, level, menu index, sub-menu index, current page ID,
 * role, and its children. It also provides a builder for easy construction and a utility to build a tree from a flat list.
 */
public class MenuItem
{
    private final int _nPageId;
    private final int _nParentId;
    private final String _strPageName;
    private final String _strPageDescription;
    private final Integer _nLevel;
    private final Integer _nMenuIndex;
    private final Integer _nSubMenuIndex;
    private final Integer _nCurrentPageId;
    private final String _strRole;
    private final List<MenuItem> _listChildren = new ArrayList<>( );

    /**
     * Constructs a MenuItem with the specified properties.
     *
     * @param nPageId
     *            the page ID of the menu item
     * @param nParentId
     *            the parent page ID (0 if root)
     * @param strPageName
     *            the name of the page
     * @param strPageDescription
     *            the description of the page
     * @param nLevel
     *            the level in the menu hierarchy
     * @param nMenuIndex
     *            the index in the menu
     * @param nSubMenuIndex
     *            the index in the submenu
     * @param nCurrentPageId
     *            the current page ID
     * @param strRole
     *            the role required to view this menu item
     */
    public MenuItem( int nPageId, int nParentId, String strPageName, String strPageDescription, Integer nLevel, Integer nMenuIndex, Integer nSubMenuIndex,
            Integer nCurrentPageId, String strRole )
    {
        _nPageId = nPageId;
        _nParentId = nParentId;
        _strPageName = strPageName;
        _strPageDescription = strPageDescription;
        _nLevel = nLevel;
        _nMenuIndex = nMenuIndex;
        _nSubMenuIndex = nSubMenuIndex;
        _nCurrentPageId = nCurrentPageId;
        _strRole = strRole;
    }

    /**
     * Gets the page ID of this menu item.
     * 
     * @return the page ID
     */
    public int getPageId( )
    {
        return _nPageId;
    }

    /**
     * Gets the parent page ID of this menu item.
     * 
     * @return the parent page ID (0 if root)
     */
    public int getParentId( )
    {
        return _nParentId;
    }

    /**
     * Gets the name of the page for this menu item.
     * 
     * @return the page name
     */
    public String getPageName( )
    {
        return _strPageName;
    }

    /**
     * Gets the description of the page for this menu item.
     * 
     * @return the page description
     */
    public String getPageDescription( )
    {
        return _strPageDescription;
    }

    /**
     * Gets the level of this menu item in the menu hierarchy.
     * 
     * @return the level (may be null)
     */
    public Integer getLevel( )
    {
        return _nLevel;
    }

    /**
     * Gets the index of this menu item in the menu.
     * 
     * @return the menu index (may be null)
     */
    public Integer getMenuIndex( )
    {
        return _nMenuIndex;
    }

    /**
     * Gets the index of this menu item in the submenu.
     * 
     * @return the submenu index (may be null)
     */
    public Integer getSubMenuIndex( )
    {
        return _nSubMenuIndex;
    }

    /**
     * Gets the current page ID associated with this menu item.
     * 
     * @return the current page ID (may be null)
     */
    public Integer getCurrentPageId( )
    {
        return _nCurrentPageId;
    }

    /**
     * Gets the role required to view this menu item.
     * 
     * @return the role string
     */
    public String getRole( )
    {
        return _strRole;
    }

    /**
     * Gets the list of child menu items.
     * 
     * @return the list of children
     */
    public List<MenuItem> getChildren( )
    {
        return _listChildren;
    }

    public static class Builder
    {
        private int _nPageId;
        private int _nParentId;
        private String _strPageName;
        private String _strPageDescription;
        private Integer _nLevel;
        private Integer _nMenuIndex;
        private Integer _nSubMenuIndex;
        private Integer _nCurrentPageId;
        private String _strRole;
        private List<MenuItem> children = new ArrayList<>( );

        public Builder pageId( int nPageId )
        {
            this._nPageId = nPageId;
            return this;
        }

        public Builder parentId( int nParentId )
        {
            this._nParentId = nParentId;
            return this;
        }

        public Builder name( String strPageName )
        {
            _strPageName = strPageName;
            return this;
        }

        public Builder description( String strPageDescription )
        {
            _strPageDescription = strPageDescription;
            return this;
        }

        public Builder level( Integer nLevel )
        {
            _nLevel = nLevel;
            return this;
        }

        public Builder menuIndex( Integer menuIndex )
        {
            _nMenuIndex = menuIndex;
            return this;
        }

        public Builder subMenuIndex( Integer subMenuIndex )
        {
            _nSubMenuIndex = subMenuIndex;
            return this;
        }

        public Builder currentPageId( Integer currentPageId )
        {
            _nCurrentPageId = currentPageId;
            return this;
        }

        public Builder role( String strRole )
        {
            _strRole = strRole;
            return this;
        }

        public Builder addChild( MenuItem child )
        {
            this.children.add( child );
            return this;
        }

        public Builder children( List<MenuItem> children )
        {
            this.children = new ArrayList<>( children );
            return this;
        }

        public MenuItem build( )
        {
            MenuItem item = new MenuItem( _nPageId, _nParentId, _strPageName, _strPageDescription, _nLevel, _nMenuIndex, _nSubMenuIndex, _nCurrentPageId,
                    _strRole );
            item._listChildren.addAll( this.children );
            return item;
        }
    }

    public static Builder builder( )
    {
        return new Builder( );
    }

    public static class MenuTreeBuilder
    {

        public static List<MenuItem> buildTree( List<MenuItem> nodes )
        {
            Map<Integer, MenuItem> nodeMap = new HashMap<>( );
            List<MenuItem> roots = new ArrayList<>( );
            int root = 0;
            
            for ( MenuItem node : nodes )
            {
                nodeMap.put( node.getPageId( ), node );
            }
            if ( !nodeMap.isEmpty( ) && !nodeMap.containsKey( root ) )
            {
                root = Collections.min( nodeMap.entrySet( ), Map.Entry.comparingByKey( ) ).getKey( );
            }

            for ( MenuItem node : nodes )
            {
                if ( root == node.getParentId( ) )
                {
                    roots.add( node );
                }
                else
                {
                    MenuItem parent = nodeMap.get( node.getParentId( ) );
                    if ( parent != null )
                    {
                        parent.getChildren( ).add( node );
                    }
                    else
                    {
                        roots.add( node );
                    }
                }
            }
            return roots;
        }
    }

}