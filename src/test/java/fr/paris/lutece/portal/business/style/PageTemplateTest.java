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
package fr.paris.lutece.portal.business.style;

import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class PageTemplateTest extends LuteceTestCase
{
    private final static String DESCRIPTION1 = "Description 1";
    private final static String DESCRIPTION2 = "Description 2";
    private final static String FILE1 = "File 1";
    private final static String FILE2 = "File 2";
    private final static String PICTURE1 = "Picture 1";
    private final static String PICTURE2 = "Picture 2";

    @Inject
    private IPageTemplateRepository _repository;
    
    @Test
    public void testBusinessPageTemplate( )
    {
        // Initialize an object
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( DESCRIPTION1 );
        pageTemplate.setFile( FILE1 );
        pageTemplate.setPicture( PICTURE1 );

        // Create test
        _repository.create( pageTemplate );

        PageTemplate pageTemplateStored = _repository.load( pageTemplate.getId( ) ).get( );
        assertEquals( pageTemplateStored.getDescription( ), pageTemplate.getDescription( ) );
        assertEquals( pageTemplateStored.getFile( ), pageTemplate.getFile( ) );
        assertEquals( pageTemplateStored.getPicture( ), pageTemplate.getPicture( ) );

        // Update test
        pageTemplate.setDescription( DESCRIPTION2 );
        pageTemplate.setFile( FILE2 );
        pageTemplate.setPicture( PICTURE2 );
        _repository.update( pageTemplate );
        pageTemplateStored = _repository.load( pageTemplate.getId( ) ).get( );
        assertEquals( pageTemplateStored.getDescription( ), pageTemplate.getDescription( ) );
        assertEquals( pageTemplateStored.getFile( ), pageTemplate.getFile( ) );
        assertEquals( pageTemplateStored.getPicture( ), pageTemplate.getPicture( ) );

        // List test
        Collection<PageTemplate> listPageTemplates = _repository.findAll( );
        assertTrue( listPageTemplates.size( ) > 0 );

        // Delete test
        _repository.remove( pageTemplate.getId( ) );
        assertThrows( NoSuchElementException.class, ( ) -> _repository.load( pageTemplate.getId( ) ).get( ) );
    }
}
