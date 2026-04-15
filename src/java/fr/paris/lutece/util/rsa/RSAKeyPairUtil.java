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
package fr.paris.lutece.util.rsa;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

/**
 * Application-scoped holder for the RSA key pair used across the portal.
 * <p>
 * Managed as a CDI {@link ApplicationScoped} bean so that initialization is
 * thread-safe (guaranteed by the container) and the keys are resolved via an
 * {@link IRSAKeyProvider} that can be swapped with a CDI {@code @Alternative}
 * (e.g. {@link RSAKeyEnvironmentProvider}).
 * </p>
 * <p>
 * In a multi-instance deployment, all instances resolve the same keys provided
 * that the selected {@link IRSAKeyProvider} is cluster-aware (shared datastore
 * or identical environment variables).
 * </p>
 */
@ApplicationScoped
public class RSAKeyPairUtil
{
    @Inject
    private IRSAKeyProvider _rsaKeyProvider;

    private PrivateKey _privateKey;
    private PublicKey _publicKey;

    /**
     * CDI no-arg constructor.
     */
    public RSAKeyPairUtil( )
    {
        // Managed by CDI — keys are loaded in {@link #init()}.
    }

    /**
     * Loads the RSA key pair from the configured provider.
     *
     * @throws IllegalStateException
     *             if the key provider fails to deliver the keys
     */
    @PostConstruct
    void init( )
    {
        try
        {
            _publicKey = _rsaKeyProvider.getPublicKey( );
            _privateKey = _rsaKeyProvider.getPrivateKey( );
        }
        catch ( GeneralSecurityException e )
        {
            throw new IllegalStateException( "Failed to load RSA keys from provider", e );
        }
    }

    /**
     * @return the privateKey
     */
    public PrivateKey getPrivateKey( )
    {
        return _privateKey;
    }

    /**
     * @return the publicKey
     */
    public PublicKey getPublicKey( )
    {
        return _publicKey;
    }

    /**
     * Backward-compatible accessor for legacy static callers.
     * <p>
     * New code must use CDI injection (e.g. {@code @Inject RSAKeyPairUtil}).
     * This method will be removed in a future version.
     * </p>
     *
     * @return the CDI-managed instance
     * @throws GeneralSecurityException
     *             kept on the signature for backward compatibility; never thrown
     *             by this implementation
     * @deprecated use CDI injection instead
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static RSAKeyPairUtil getInstance( ) throws GeneralSecurityException
    {
        return CDI.current( ).select( RSAKeyPairUtil.class ).get( );
    }
}
