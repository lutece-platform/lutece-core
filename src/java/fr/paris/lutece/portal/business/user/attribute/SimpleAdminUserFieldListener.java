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
package fr.paris.lutece.portal.business.user.attribute;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * Implementation of AdminUserFieldListener that allow attributes to be
 * exported.
 */
public abstract class SimpleAdminUserFieldListener implements AdminUserFieldListener
{
    /**
     * Get the plugin
     * @return The plugin
     */
    public abstract Plugin getPlugin(  );

    /**
     * Create user fields
     * @param user Adminuser
     * @param listUserFields The list of user fields to create
     * @param locale locale
     */
    public abstract void doCreateUserFields( AdminUser user, List<AdminUserField> listUserFields, Locale locale );

    /**
     * Modify user fields
     * @param user Adminuser
     * @param listUserFields The list of user fields to modify
     * @param locale locale
     * @param currentUser current user
     */
    public abstract void doModifyUserFields( AdminUser user, List<AdminUserField> listUserFields, Locale locale,
        AdminUser currentUser );

    /**
     * Remove user fields
     * @param user Adminuser
     * @param locale locale
     */
    public abstract void doRemoveUserFields( AdminUser user, Locale locale );

    /**
     * {@inheritDoc}
     */
    @Override
    public void doCreateUserFields( AdminUser user, HttpServletRequest request, Locale locale )
    {
        List<IAttribute> listAttributes = AttributeService.getInstance(  )
                                                          .getPluginAttributesWithoutFields( getPlugin(  ).getName(  ),
                locale );
        List<AdminUserField> listUserFields = new ArrayList<AdminUserField>(  );

        for ( IAttribute attribute : listAttributes )
        {
            List<AdminUserField> userFields = attribute.getUserFieldsData( request, user );

            for ( AdminUserField userField : userFields )
            {
                if ( ( userField != null ) && StringUtils.isNotBlank( userField.getValue(  ) ) )
                {
                    // Change the value of the user field
                    // Instead of having the ID of the attribute field, we put the attribute field title
                    // which represents the profile's ID
                    userField.setValue( userField.getAttributeField(  ).getTitle(  ) );
                    AdminUserFieldHome.create( userField );
                    listUserFields.add( userField );
                }
            }
        }

        doCreateUserFields( user, listUserFields, locale );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doModifyUserFields( AdminUser user, HttpServletRequest request, Locale locale, AdminUser currentUser )
    {
        List<IAttribute> listAttributes = AttributeService.getInstance(  )
                                                          .getPluginAttributesWithoutFields( getPlugin(  ).getName(  ),
                locale );
        List<AdminUserField> listUserFields = new ArrayList<AdminUserField>(  );

        for ( IAttribute attribute : listAttributes )
        {
            List<AdminUserField> userFields = attribute.getUserFieldsData( request, user );

            for ( AdminUserField userField : userFields )
            {
                if ( ( userField != null ) && StringUtils.isNotBlank( userField.getValue(  ) ) )
                {
                    // Change the value of the user field
                    // Instead of having the ID of the attribute field, we put the attribute field title
                    // which represents the profile's ID
                    userField.setValue( userField.getAttributeField(  ).getTitle(  ) );
                    AdminUserFieldHome.create( userField );
                    listUserFields.add( userField );
                }
            }
        }

        doModifyUserFields( user, listUserFields, locale, currentUser );
    }

    /**
     * Remove user fields
     * @param user Adminuser
     * @param request HttpServletRequest
     * @param locale locale
     */
    public void doRemoveUserFields( AdminUser user, HttpServletRequest request, Locale locale )
    {
        doRemoveUserFields( user, locale );
    }
}
