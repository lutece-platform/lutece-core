/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.portal.service.panel;

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * PanelService
 * @param <T> Abstract type of panels to display. Every class extending the class T will create a panel.
 */
public final class LutecePanelService<T extends LutecePanel>
{
    private static List<LutecePanelService<?extends LutecePanel>> _listSingletons = new ArrayList<LutecePanelService<?extends LutecePanel>>(  );
    private List<T> _listPanels;
    private Class<T> _genericTypeClass;

    /**
     * Instantiates a new lutece panel service.
     */
    private LutecePanelService(  )
    {
    }

    /**
     * Instantiates a new lutece panel service.
     *
     * @param clazz the clazz
     */
    private LutecePanelService( Class<T> clazz )
    {
        _listPanels = SpringContextService.getBeansOfType( clazz );
        Collections.sort( _listPanels, new PanelComparator(  ) );
        _genericTypeClass = clazz;
    }

    /**
     * Gets the generic type class.
     *
     * @return the generic type class
     */
    private Class<T> getGenericTypeClass(  )
    {
        return _genericTypeClass;
    }

    /**
     * Get the instance of a PanelService for a given type.
     * @param <T> Specialized type of the PanelService
     * @param clazz Class associated to the type A.
     * @return The instance of the PanelService with the generic type A. This instance is unique.
     */
    public static synchronized <T extends LutecePanel> LutecePanelService<T> instance( Class<T> clazz )
    {
        for ( LutecePanelService<?extends LutecePanel> storedPanelService : _listSingletons )
        {
            if ( storedPanelService.getGenericTypeClass(  ).equals( clazz ) )
            {
                return (LutecePanelService<T>) storedPanelService;
            }
        }

        LutecePanelService<T> panelService = new LutecePanelService<T>( clazz );
        _listSingletons.add( panelService );

        return panelService;
    }

    /**
     * Get the index of a panel from its key
     * @param strPanelKey Key of the panel to get the index.
     * @return The index of the panel with the given key, or -1 if the panel could not be found.
     */
    public int getIndex( String strPanelKey )
    {
        int nIndex = 1;

        for ( LutecePanel panel : getPanels(  ) )
        {
            if ( panel.getPanelKey(  ).equals( strPanelKey ) )
            {
                return nIndex;
            }

            nIndex++;
        }

        return -1;
    }

    /**
     * Get the list of panels associated to this PanelService. One panel is used for every class extending the class T.
     * @return The list of panels associated to this PanelService.
     */
    public List<T> getPanels(  )
    {
        return _listPanels;
    }

    /**
     * PanelComparator
     */
    private class PanelComparator implements Comparator<T>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare( T p1, T p2 )
        {
            return p1.getPanelOrder(  ) - p2.getPanelOrder(  );
        }
    }
}
