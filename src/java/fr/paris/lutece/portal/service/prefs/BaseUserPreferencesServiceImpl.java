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
package fr.paris.lutece.portal.service.prefs;

import fr.paris.lutece.portal.business.prefs.IPreferencesDAO;

import org.springframework.beans.factory.InitializingBean;

import java.util.List;


/**
 * Abstract User Preferences Service
 * @since 4.0
 */
public class BaseUserPreferencesServiceImpl implements IUserPreferencesService, InitializingBean
{
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static BaseUserPreferencesCacheService _cache;
    private IPreferencesDAO _dao;

    /**
     * Constructor
     */
    protected BaseUserPreferencesServiceImpl(  )
    {
    }

    /**
     * Sets the DAO
     * @param dao The DAO
     */
    public void setDao( IPreferencesDAO dao )
    {
        _dao = dao;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String get( String strUserId, String strKey, String strDefault )
    {
        String strCacheKey = _cache.getCacheKey( strUserId, strKey );
        String strValue = (String) _cache.getFromCache( strCacheKey );

        if ( strValue == null )
        {
            strValue = _dao.load( strUserId, strKey, strDefault );
            _cache.putInCache( strCacheKey, strValue );
        }

        return strValue;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getInt( String strUserId, String strKey, int nDefault )
    {
        return Integer.parseInt( get( strUserId, strKey, String.valueOf( nDefault ) ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean getBoolean( String strUserId, String strKey, boolean bDefault )
    {
        String strDefault = bDefault ? TRUE : FALSE;
        String strValue = get( strUserId, strKey, strDefault );

        return strValue.equals( TRUE );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void put( String strUserId, String strKey, String strValue )
    {
        _dao.store( strUserId, strKey, strValue );
        _cache.putInCache( _cache.getCacheKey( strUserId, strKey ), strValue );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void putInt( String strUserId, String strKey, int nValue )
    {
        put( strUserId, strKey, String.valueOf( nValue ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void putBoolean( String strUserId, String strKey, boolean bValue )
    {
        String strValue = bValue ? TRUE : FALSE;
        put( strUserId, strKey, strValue );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> keys( String strUserId )
    {
        return _dao.keys( strUserId );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void clear( String strUserId )
    {
        _dao.remove( strUserId );
        _cache.removeCacheValuesOfUser( strUserId );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void clearKey( String strUserId, String strKey )
    {
        _dao.removeKey( strUserId, strKey );
        _cache.removeCacheValuesOfUser( strUserId );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void clearKeyPrefix( String strUserId, String strPrefix )
    {
        _dao.removeKeyPrefix( strUserId, strPrefix );
        _cache.removeCacheValuesOfUser( strUserId );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean existsKey( String strUserId, String strKey )
    {
        return _dao.existsKey( strUserId, strKey );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getUsers( String strKey, String strValue )
    {
        return _dao.getUserId( strKey, strValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet(  ) throws Exception
    {
        synchronized ( BaseUserPreferencesServiceImpl.class )
        {
            if ( _cache == null )
            {
                _cache = new BaseUserPreferencesCacheService(  );
                _cache.initCache(  );
            }
        }
    }

	@Override
	public boolean existsValueForKey(String strKey, String strValue) {
		
		return _dao.existsValueForKey(strKey, strValue);
	}
}
