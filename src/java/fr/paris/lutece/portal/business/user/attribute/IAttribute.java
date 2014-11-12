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

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * IAttribute
 */
public interface IAttribute
{
    /**
     * Get ID Attribute
     * @return ID attribute
     */
    int getIdAttribute(  );

    /**
     * Set ID Attribute
     * @param nIdAttribute ID Attribute
     */
    void setIdAttribute( int nIdAttribute );

    /**
     * Get Mandatory
     * @return true if it's mandatory, false otherwise
     */
    boolean isMandatory(  );

    /**
     * Set mandatory
     * @param bMandatory true if it's mandatory, false otherwise
     */
    void setMandatory( boolean bMandatory );

    /**
     * Get list fields
     * @return list fields
     */
    List<AttributeField> getListAttributeFields(  );

    /**
     * Set list fields
     * @param listAttributeFields list fields
     */
    void setListAttributeFields( List<AttributeField> listAttributeFields );

    /**
     * Get title
     * @return title
     */
    String getTitle(  );

    /**
     * Set title
     * @param strTitle title
     */
    void setTitle( String strTitle );

    /**
     * Get help Message
     * @return help message
     */
    String getHelpMessage(  );

    /**
     * Set help message
     * @param strHelpMessage help message
     */
    void setHelpMessage( String strHelpMessage );

    /**
     * Get position
     * @return position
     */
    int getPosition(  );

    /**
     * Set position
     * @param nPosition position
     */
    void setPosition( int nPosition );

    /**
     * Get attribute type
     * @return attribute type
     */
    AttributeType getAttributeType(  );

    /**
     * Set attribute Type
     * @param attributeType attribute type
     */
    void setAttributeType( AttributeType attributeType );

    /**
     * Get plugin
     * @return plugin
     */
    Plugin getPlugin(  );

    /**
     * Set plugin
     * @param plugin plugin
     */
    void setPlugin( Plugin plugin );

    /**
     * Check if the attribute is shown in search
     * @return true if it is, false otherwise
     */
    boolean isShownInSearch(  );

    /**
     * Set isShownInSearch
     * @param bIsShownInSearch shown in search
     */
    void setShownInSearch( boolean bIsShownInSearch );

    /**
     * Check if the attribute is shown in result list
     * @return true if it is, false otherwise
     */
    boolean isShownInResultList(  );

    /**
     * Set isShownInResultList
     * @param bIsShownInResultList shown in result list
     */
    void setShownInResultList( boolean bIsShownInResultList );

    /**
     * Check if the attribute is to be shown in line
     * @return true if it is shown in line, false otherwise
     */
    boolean isFieldInLine(  );

    /**
     * Set isFieldInLine
     * @param bIsFieldInLine shown in line
     */
    void setFieldInLine( boolean bIsFieldInLine );

    /**
     * Get the anonymize status of the attribute
     * @return True if the attribute should be anonymize, false otherwise.
     */
    boolean getAnonymize(  );

    /**
     * Set the anonymize status of the attribute
     * @param bAnonymize New anonymize status. True if the attribute should be
     *            anonymize, false otherwise.
     */
    void setAnonymize( boolean bAnonymize );

    /**
     * Get the data of the user fields
     * @param request HttpServletRequest
     * @param user user
     * @return user field data
     */
    List<AdminUserField> getUserFieldsData( HttpServletRequest request, AdminUser user );

    /**
     * Set attribute type
     * @param locale locale
     */
    void setAttributeType( Locale locale );

    /**
     * Get page title for create page
     * @return page title
     */
    String getPropertyCreatePageTitle(  );

    /**
     * Get page title for modify page
     * @return page title
     */
    String getPropertyModifyPageTitle(  );

    /**
     * Get the template create an attribute
     * @return The URL of the template
     */
    String getTemplateCreateAttribute(  );

    /**
     * Get the template modify an attribute
     * @return The URL of the template
     */
    String getTemplateModifyAttribute(  );

    /**
     * Get the template html form attribute
     * @return the template
     */
    String getTemplateHtmlFormAttribute(  );

    /**
     * Get the template html form search attribute
     * @return the template
     */
    String getTemplateHtmlFormSearchAttribute(  );

    /**
     * Get the template html for the value of the attribute
     * @return the template
     */
    String getTemplateHtmlValue(  );

    /**
     * Set the data of the attribute
     * @param request HttpServletRequest
     * @return null if there are no errors
     */
    String setAttributeData( HttpServletRequest request );

    /**
     * Check if it is an attribute image
     * @return true if it is, false otherwise
     */
    boolean isAttributeImage(  );

    /**
     * Set the attribute as an attribute image
     * @param bIsAttributeImage true if it is an image, false otherwise
     */
    void setAttributeImage( boolean bIsAttributeImage );

    /**
     * Get whether the attribute is anonymizable.
     * @return True if the attribute can be anonymized, false otherwise.
     */
    boolean isAnonymizable(  );
}
