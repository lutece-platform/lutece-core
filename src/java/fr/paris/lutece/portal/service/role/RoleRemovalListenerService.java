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
package fr.paris.lutece.portal.service.role;

import fr.paris.lutece.portal.service.util.BeanUtils;
import fr.paris.lutece.portal.service.util.RemovalListenerService;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.CDI;

/**
 * RoleRemovalListenerService
 */
@Deprecated(since = "8.0", forRemoval = true)
public final class RoleRemovalListenerService
{
    /**
     * Private constructor
     */
    private RoleRemovalListenerService( )
    {
    }
    
    /**
     * Returns the {@link RemovalListenerService} instance.
     * <p>
     * This static accessor is <strong>deprecated</strong> and will be removed in a future release.
     * Instead of calling this method directly, you should use <b>CDI dependency injection</b>
     * to obtain an instance of {@code RemovalListenerService}.
     * </p>
     *
     * <pre>{@code
     * @Inject
     * @Named(BeanUtils.BEAN_ROLE_REMOVAL_SERVICE)
     * private RemovalListenerService removalListenerService;
     * }</pre>
     *
     * @deprecated since 8.0 — use CDI injection instead of this static method.
     * @return the {@link RemovalListenerService} instance
     */
    public static RemovalListenerService getService( )
    {
    	return CDI.current()
                .select(RemovalListenerService.class, NamedLiteral.of(BeanUtils.BEAN_ROLE_REMOVAL_SERVICE))
                .get();
    }
}
