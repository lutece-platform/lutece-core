/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.portal.service.resource;

import org.apache.commons.lang.StringUtils;

/**
 *
 * Enumeration of the resource types for ResourceEnhancer
 *
 */
public enum ResourceTypeEnum
{PAGE( "page", "Page Content Service" ),
	XPAGE( "xpage", "XPageAppService" ),
	CONTENT_SERVICE( "contentservice", "" );

	private String _strResourceType;
	private String _strName;

	/**
	 * Instantiates a new resource type enum.
	 *
	 * @param strResourceType the str resource type
	 */
	private ResourceTypeEnum( String strResourceType, String strName )
	{
		_strResourceType = strResourceType;
		_strName = strName;
	}

	/**
	 * Get name
	 * @return the name
	 */
	public String getName(  )
	{
		return _strName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(  )
	{
		return _strResourceType;
	}

	/**
	 * Find.
	 *
	 * @param strName the str name
	 * @return the resource type enum
	 */
	public static ResourceTypeEnum find( String strName )
	{
		if ( StringUtils.isNotBlank( strName ) )
		{
			for ( ResourceTypeEnum resourceTypeEnum : ResourceTypeEnum.values( ) )
			{
				if ( resourceTypeEnum.getName( ).equals( strName ) )
				{
					return resourceTypeEnum;
				}
			}
		}
		return ResourceTypeEnum.CONTENT_SERVICE;
	}
}
