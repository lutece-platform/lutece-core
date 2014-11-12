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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 *
 * Attribute
 *
 */
public abstract class AbstractAttribute implements IAttribute
{
    // MARKS
    private static final String MARK_ATTRIBUTE = "attribute";
    private static final String MARK_DEFAULT_VALUES_LIST = "default_values_list";
    private static final String MARK_USER_FIELD = "user_field";
    private int _nIdAttribute;
    private boolean _bMandatory;
    private String _strTitle;
    private String _strHelpMessage;
    private int _nPosition;
    private AttributeType _attributeType;
    private List<AttributeField> _listAttributeFields;
    private Plugin _plugin;
    private boolean _bIsShownInSearch;
    private boolean _bIsFieldInLine;
    private boolean _bIsAttributeImage;
    private boolean _bIsShownInResultList;
    private boolean _bAnonymize;

    /**
     * Constructor
     */
    public AbstractAttribute(  )
    {
    }

    /**
     * Get ID Attribute
     * @return ID attribute
     */
    @Override
    public int getIdAttribute(  )
    {
        return _nIdAttribute;
    }

    /**
     * Set ID Attribute
     * @param nIdAttribute ID Attribute
     */
    @Override
    public void setIdAttribute( int nIdAttribute )
    {
        _nIdAttribute = nIdAttribute;
    }

    /**
     * Get Mandatory
     * @return true if it's mandatory, false otherwise
     */
    @Override
    public boolean isMandatory(  )
    {
        return _bMandatory;
    }

    /**
     * Set mandatory
     * @param bMandatory true if it's mandatory, false otherwise
     */
    @Override
    public void setMandatory( boolean bMandatory )
    {
        _bMandatory = bMandatory;
    }

    /**
     * Get list fields
     * @return list fields
     */
    @Override
    public List<AttributeField> getListAttributeFields(  )
    {
        return _listAttributeFields;
    }

    /**
     * Set list fields
     * @param listAttributeFields list fields
     */
    @Override
    public void setListAttributeFields( List<AttributeField> listAttributeFields )
    {
        _listAttributeFields = listAttributeFields;
    }

    /**
     * Get title
     * @return title
     */
    @Override
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Set title
     * @param strTitle title
     */
    @Override
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Get help Message
     * @return help message
     */
    @Override
    public String getHelpMessage(  )
    {
        return _strHelpMessage;
    }

    /**
     * Set help message
     * @param strHelpMessage help message
     */
    @Override
    public void setHelpMessage( String strHelpMessage )
    {
        _strHelpMessage = strHelpMessage;
    }

    /**
     * Get position
     * @return position
     */
    @Override
    public int getPosition(  )
    {
        return _nPosition;
    }

    /**
     * Set position
     * @param nPosition position
     */
    @Override
    public void setPosition( int nPosition )
    {
        _nPosition = nPosition;
    }

    /**
     * Get attribute type
     * @return attribute type
     */
    @Override
    public AttributeType getAttributeType(  )
    {
        return _attributeType;
    }

    /**
     * Set attribute Type
     * @param attributeType attribute type
     */
    @Override
    public void setAttributeType( AttributeType attributeType )
    {
        _attributeType = attributeType;
    }

    /**
     * Get the anonymize status of the attribute
     * @return True if the attribute should be anonymize, false otherwise.
     */
    @Override
    public boolean getAnonymize(  )
    {
        return _bAnonymize;
    }

    /**
     * Set the anonymize status of the attribute
     * @param bAnonymize New anonymize status. True if the attribute should be
     *            anonymize, false otherwise.
     */
    @Override
    public void setAnonymize( boolean bAnonymize )
    {
        _bAnonymize = bAnonymize;
    }

    /**
     * Get Html form
     * @param locale locale
     * @return html form
     */
    public String getHtmlFormAttribute( Locale locale )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ATTRIBUTE, this );

        HtmlTemplate template = AppTemplateService.getTemplate( getTemplateHtmlFormAttribute(  ), locale, model );

        return template.getHtml(  );
    }

    /**
     * Get Html form
     * @param locale locale
     * @param listDefaultValues the list of default values
     * @return html form
     */
    public String getHtmlFormAttribute( Locale locale, Object listDefaultValues )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_ATTRIBUTE, this );
        model.put( MARK_DEFAULT_VALUES_LIST, listDefaultValues );

        HtmlTemplate template = AppTemplateService.getTemplate( getTemplateHtmlFormAttribute(  ), locale, model );

        return template.getHtml(  );
    }

    /**
     * Get Html form
     * @param auFieldFilter The admin user field filter
     * @param locale locale
     * @return html form
     */
    public String getHtmlFormSearchAttribute( AdminUserFieldFilter auFieldFilter, Locale locale )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        List<AdminUserField> listUserFields = auFieldFilter.getListUserFields(  );
        List<AdminUserField> selectedUserFields = null;

        if ( ( listUserFields != null ) && ( !listUserFields.isEmpty(  ) ) )
        {
            selectedUserFields = new ArrayList<AdminUserField>(  );

            for ( AdminUserField userField : listUserFields )
            {
                if ( userField.getAttribute(  ).getIdAttribute(  ) == _nIdAttribute )
                {
                    selectedUserFields.add( userField );
                }
            }
        }

        model.put( MARK_DEFAULT_VALUES_LIST, selectedUserFields );
        model.put( MARK_ATTRIBUTE, this );

        HtmlTemplate template = AppTemplateService.getTemplate( getTemplateHtmlFormSearchAttribute(  ), locale, model );

        return template.getHtml(  );
    }

    /**
     * Get Html value
     * @param locale Locale
     * @param userField User field
     * @return the html
     */
    public String getHtmlValue( Locale locale, AdminUserField userField )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_ATTRIBUTE, this );
        model.put( MARK_USER_FIELD, userField );

        HtmlTemplate template = AppTemplateService.getTemplate( getTemplateHtmlValue(  ), locale, model );

        return template.getHtml(  );
    }

    /**
     * Get plugin
     * @return plugin
     */
    @Override
    public Plugin getPlugin(  )
    {
        return _plugin;
    }

    /**
     * Set plugin
     * @param plugin plugin
     */
    @Override
    public void setPlugin( Plugin plugin )
    {
        _plugin = plugin;
    }

    /**
     * Check if the attribute is shown in search
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isShownInSearch(  )
    {
        return _bIsShownInSearch;
    }

    /**
     * Set isShownInSearch
     * @param bIsShownInSearch shown in search
     */
    @Override
    public void setShownInSearch( boolean bIsShownInSearch )
    {
        _bIsShownInSearch = bIsShownInSearch;
    }

    /**
     * Check if the attribute is to be shown in line
     * @return true if it is shown in line, false otherwise
     */
    @Override
    public boolean isFieldInLine(  )
    {
        return _bIsFieldInLine;
    }

    /**
     * Set isFieldInLine
     * @param bIsFieldInLine shown in line
     */
    @Override
    public void setFieldInLine( boolean bIsFieldInLine )
    {
        _bIsFieldInLine = bIsFieldInLine;
    }

    /**
     * Check if it is an attribute image
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isAttributeImage(  )
    {
        return _bIsAttributeImage;
    }

    /**
     * Set the attribute as an attribute image
     * @param bIsAttributeImage true if it is an image, false otherwise
     */
    @Override
    public void setAttributeImage( boolean bIsAttributeImage )
    {
        _bIsAttributeImage = bIsAttributeImage;
    }

    /**
     * Check if the attribute is shown in result list
     * @return true if it is, false otherwise
     */
    @Override
    public boolean isShownInResultList(  )
    {
        return _bIsShownInResultList;
    }

    /**
     * Set isShownInResultList
     * @param bIsShownInResultList shown in result list
     */
    @Override
    public void setShownInResultList( boolean bIsShownInResultList )
    {
        _bIsShownInResultList = bIsShownInResultList;
    }
}
