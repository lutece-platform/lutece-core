package fr.paris.lutece.portal.business.user.attribute;

import fr.paris.lutece.portal.business.user.AdminUser;

import java.util.List;


/**
 * Interface of attributes that contains only interger, boolean or string
 * values.
 */
public interface ISimpleValuesAttributes extends IAttribute
{
    /**
     * Get the data of the user fields
     * @param strValues Values
     * @param user user
     * @return user field data
     */
    List<AdminUserField> getUserFieldsData( String[] strValues, AdminUser user );
}
