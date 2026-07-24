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
package fr.paris.lutece.portal.business.physicalfile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.util.AppException;

/**
 * Unit tests for {@link PhysicalFileDAO}.
 *
 * The guard checked here runs before any database access, so no Lutece runtime nor database is required : {@code store} must refuse a physical file whose content
 * is {@code null} rather than erase the stored content with a NULL, symmetrically to the guards on {@code insert} and on the {@code LocalDatabaseFileService}
 * store methods.
 */
public class PhysicalFileDAOTest
{
    /**
     * {@code store} must reject a null content : it would otherwise overwrite the stored file value with NULL, reproducing the "physical file without content"
     * symptom on the update path.
     */
    @Test
    void testStoreRefusesNullContent( )
    {
        PhysicalFileDAO dao = new PhysicalFileDAO( );

        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setIdPhysicalFile( 42 );
        // no value set : getValue( ) is null

        AppException exception = assertThrows( AppException.class, ( ) -> dao.store( physicalFile ) );
        assertTrue( exception.getMessage( ).contains( "null content" ), "The exception must mention the null content" );
    }
}
