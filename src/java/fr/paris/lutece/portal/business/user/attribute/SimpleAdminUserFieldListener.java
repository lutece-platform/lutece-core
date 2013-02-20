package fr.paris.lutece.portal.business.user.attribute;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


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
    public abstract Plugin getPlugin( );

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
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getPluginAttributesWithoutFields(
                getPlugin( ).getName( ), locale );
        List<AdminUserField> listUserFields = new ArrayList<AdminUserField>( );

        for ( IAttribute attribute : listAttributes )
        {
            List<AdminUserField> userFields = attribute.getUserFieldsData( request, user );

            for ( AdminUserField userField : userFields )
            {
                if ( ( userField != null ) && StringUtils.isNotBlank( userField.getValue( ) ) )
                {
                    // Change the value of the user field
                    // Instead of having the ID of the attribute field, we put the attribute field title
                    // which represents the profile's ID
                    userField.setValue( userField.getAttributeField( ).getTitle( ) );
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
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getPluginAttributesWithoutFields(
                getPlugin( ).getName( ), locale );
        List<AdminUserField> listUserFields = new ArrayList<AdminUserField>( );

        for ( IAttribute attribute : listAttributes )
        {
            List<AdminUserField> userFields = attribute.getUserFieldsData( request, user );

            for ( AdminUserField userField : userFields )
            {
                if ( ( userField != null ) && StringUtils.isNotBlank( userField.getValue( ) ) )
                {
                    // Change the value of the user field
                    // Instead of having the ID of the attribute field, we put the attribute field title
                    // which represents the profile's ID
                    userField.setValue( userField.getAttributeField( ).getTitle( ) );
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
