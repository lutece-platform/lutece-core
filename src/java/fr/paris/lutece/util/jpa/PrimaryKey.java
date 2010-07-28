/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
package fr.paris.lutece.util.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * PrimaryKey : a value and a class name.
 * <br>
 * The value is the last inserted primary key for the class.
 */
@Entity
@Table( name="core_id_generator" )
public class PrimaryKey
{
	private String _strClassName;
	private int _nValue;
	
	/**
	 * "Getter method" for {@link #_strClassName}
	 * @return value of {@link #_strClassName}
	 */
	@Id
	@Column( name="class_name" )
	public String getClassName(  )
	{
		return _strClassName;
	}
	/**
	 * "Setter method" for {@link #_strClassName}
	 * @param className new value of {@link #_strClassName}
	 */
	public void setClassName( String className )
	{
		_strClassName = className;
	}
	/**
	 * "Getter method" for {@link #_nValue}
	 * @return value of {@link #_nValue}
	 */
	@Column( name="current_value" )
	public int getValue(  )
	{
		return _nValue;
	}
	/**
	 * "Setter method" for {@link #_nValue}
	 * @param value new value of {@link #_nValue}
	 */
	public void setValue( int value )
	{
		_nValue = value;
	}

	/**
	 * 
	 *{@inheritDoc}
	 */
	@Override
	public String toString(  )
	{
		return getClassName(  ) + " " + getValue(  );
	}
	
}
