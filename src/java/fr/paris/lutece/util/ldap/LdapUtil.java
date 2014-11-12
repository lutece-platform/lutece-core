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
package fr.paris.lutece.util.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


/**
 * This class provides LDAP generic connection methods
 */
public final class LdapUtil
{
    /**
     * Constructor
     */
    private LdapUtil(  )
    {
        // empty contructor
    }

    /**
     * LDAP Context search
     * @param strLDAPContext the context
     * @param strLDAPUrl the url
     * @param strAdminDN the admin dn to use
     * @param strAdminPassword the admin password to use
     * @return a DirContext object
     * @throws NamingException in case of error
     */
    public static DirContext getContext( String strLDAPContext, String strLDAPUrl, String strAdminDN,
        String strAdminPassword ) throws NamingException
    {
        Hashtable<String, String> env = new Hashtable<String, String>(  );

        env.put( Context.INITIAL_CONTEXT_FACTORY, strLDAPContext );
        env.put( Context.PROVIDER_URL, strLDAPUrl );
        env.put( Context.SECURITY_PRINCIPAL, strAdminDN );
        env.put( Context.SECURITY_CREDENTIALS, strAdminPassword );

        DirContext context = new InitialDirContext( env );

        return context;
    }

    /**
     * Close the context given in parameter
     * @param context the LDAP context as a DirContext object
     * @throws NamingException in case of error
     */
    public static void freeContext( DirContext context )
        throws NamingException
    {
        if ( context != null )
        {
            context.close(  );
        }
    }

    /**
     *
     * @param strLDAPContext the context
     * @param strLDAPUrl the url
     * @param strDN the dn of the user to connect
     * @param strPassword the password of the user to connect
     * @return a DirContext object
     * @throws NamingException in case of error
     */
    public static DirContext bindUser( String strLDAPContext, String strLDAPUrl, String strDN, String strPassword )
        throws NamingException
    {
        DirContext context = null;

        Hashtable<String, String> env = new Hashtable<String, String>(  );

        env.put( Context.INITIAL_CONTEXT_FACTORY, strLDAPContext );
        env.put( Context.PROVIDER_URL, strLDAPUrl );
        env.put( Context.SECURITY_PRINCIPAL, strDN );
        env.put( Context.SECURITY_CREDENTIALS, strPassword );

        context = new InitialDirContext( env );

        return context;
    }

    /**
     *
     * @param context the context
     * @param strFilter the filter for the search
     * @param strUserDN the user dn
     * @param strDNBase the base dn
     * @param sc the SearchControls
     * @return an enumeration of the elements found
     * @throws NamingException in case of error
     */
    public static NamingEnumeration<SearchResult> searchUsers( DirContext context, String strFilter, String strUserDN,
        String strDNBase, SearchControls sc ) throws NamingException
    {
        NamingEnumeration<SearchResult> enumeration = context.search( strUserDN + strDNBase, strFilter, sc );

        return enumeration;
    }
}
