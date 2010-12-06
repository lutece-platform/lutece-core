/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
    protected int _nIdAttribute;
    protected boolean _bMandatory;
    protected String _strTitle;
    protected String _strHelpMessage;
    protected int _nPosition;
    protected AttributeType _attributeType;
    protected List<AttributeField> _listAttributeFields;
    protected Plugin _plugin;
    protected boolean _bIsShownInSearch;

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
    public int getIdAttribute(  )
    {
        return _nIdAttribute;
    }

    /**
     * Set ID Attribute
     * @param nIdAttribute ID Attribute
     */
    public void setIdAttribute( int nIdAttribute )
    {
        _nIdAttribute = nIdAttribute;
    }

    /**
     * Get Mandatory
     * @return true if it's mandatory, false otherwise
     */
    public boolean isMandatory(  )
    {
        return _bMandatory;
    }

    /**
     * Set mandatory
     * @param bMandatory true if it's mandatory, false otherwise
     */
    public void setMandatory( boolean bMandatory )
    {
        _bMandatory = bMandatory;
    }

    /**
     * Get list fields
     * @return list fields
     */
    public List<AttributeField> getListAttributeFields(  )
    {
        return _listAttributeFields;
    }

    /**
     * Set list fields
     * @param listAttributeFields list fields
     */
    public void setListAttributeFields( List<AttributeField> listAttributeFields )
    {
        _listAttributeFields = listAttributeFields;
    }

    /**
     * Get title
     * @return title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Set title
     * @param strTitle title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Get help Message
     * @return help message
     */
    public String getHelpMessage(  )
    {
        return _strHelpMessage;
    }

    /**
     * Set help message
     * @param strHelpMessage help message
     */
    public void setHelpMessage( String strHelpMessage )
    {
        _strHelpMessage = strHelpMessage;
    }

    /**
     * Get position
     * @return position
     */
    public int getPosition(  )
    {
        return _nPosition;
    }

    /**
     * Set position
     * @param nPosition position
     */
    public void setPosition( int nPosition )
    {
        _nPosition = nPosition;
    }

    /**
     * Get attribute type
     * @return attribute type
     */
    public AttributeType getAttributeType(  )
    {
        return _attributeType;
    }

    /**
     * Set attribute Type
     * @param attributeType attribute type
     */
    public void setAttributeType( AttributeType attributeType )
    {
        _attributeType = attributeType;
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
    public String getHtmlFormAttribute( Locale locale, List<AdminUserField> listDefaultValues )
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

        if ( ( listUserFields != null ) && ( listUserFields.size(  ) != 0 ) )
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
     * Get plugin
     * @return plugin
     */
    public Plugin getPlugin(  )
    {
        return _plugin;
    }

    /**
     * Set plugin
     * @param plugin plugin
     */
    public void setPlugin( Plugin plugin )
    {
        _plugin = plugin;
    }

    /**
     * Check if the attribute is shown in search
     * @return true if it is, false otherwise
     */
    public boolean isShownInSearch(  )
    {
        return _bIsShownInSearch;
    }

    /**
     * Set isShownInSearch
     * @param bIsShownInSearch shown in search
     */
    public void setShownInSearch( boolean bIsShownInSearch )
    {
        _bIsShownInSearch = bIsShownInSearch;
    }
}
