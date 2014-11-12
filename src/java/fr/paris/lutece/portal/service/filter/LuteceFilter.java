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
package fr.paris.lutece.portal.service.filter;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.Map;

import javax.servlet.Filter;


/**
 * LuteceFilter
 */
public class LuteceFilter implements Comparable<LuteceFilter>
{
    public static final int ORDER_NOT_DEFINED = -1;
    private String _strName;
    private Filter _filter;
    private String _strMapping;
    private Plugin _plugin;
    private int _nOrder = ORDER_NOT_DEFINED;
    private Map<String, String> _mapInitParameters;

    /**
     * Constructor
     * @param strName The name
     * @param filter The filter class
     * @param strMapping The mapping url pattern
     * @param plugin The plugin
     * @param mapInitParameters Init parameters as a map
     */
    public LuteceFilter( String strName, Filter filter, String strMapping, Plugin plugin,
        Map<String, String> mapInitParameters )
    {
        _strName = strName;
        _filter = filter;
        _strMapping = strMapping;
        _plugin = plugin;
        _mapInitParameters = mapInitParameters;
    }

    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the filter
     *
     * @return The filter
     */
    public Filter getFilter(  )
    {
        return _filter;
    }

    /**
     * Sets the filter
     *
     * @param filter The filter
     */
    public void setFilter( Filter filter )
    {
        _filter = filter;
    }

    /**
     * Returns the Mapping
     *
     * @return The Mapping
     */
    public String getMappingUrlPattern(  )
    {
        return _strMapping;
    }

    /**
     * Sets the Mapping
     *
     * @param strMapping The Mapping
     */
    public void setMappingUrlPattern( String strMapping )
    {
        _strMapping = strMapping;
    }

    /**
         * Returns the Plugin
         *
         * @return The Plugin
         */
    public Plugin getPlugin(  )
    {
        return _plugin;
    }

    /**
     * Sets the Plugin
     *
     * @param plugin The plugin
     */
    public void setPlugin( Plugin plugin )
    {
        _plugin = plugin;
    }

    /**
    * Returns init parameters
    * @return Init parameters in a map object
    */
    public Map<String, String> getInitParameters(  )
    {
        return _mapInitParameters;
    }

    /**
     * for debug purpose
     * @return The name of the filter
     */
    @Override
    public String toString(  )
    {
        return getName(  );
    }

    /**
     *
     * Returns the order.
     * {@link #getOrder()} == {@value #ORDER_NOT_DEFINED} means there is no priority set for the filter
     * @return the order
     */
    public int getOrder(  )
    {
        return _nOrder;
    }

    /**
     * nOrder ==  == {@value #ORDER_NOT_DEFINED} means there is no priority set for the filter
     * @param nOrder the order
     */
    public void setOrder( int nOrder )
    {
        _nOrder = nOrder;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int compareTo( LuteceFilter luteceFilter )
    {
        if ( luteceFilter == null )
        {
            return 1;
        }

        return luteceFilter.getOrder(  ) - this.getOrder(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        if ( !( o instanceof LuteceFilter ) )
        {
            return false;
        }

        return super.equals( o );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode(  )
    {
        return getOrder(  );
    }
}
