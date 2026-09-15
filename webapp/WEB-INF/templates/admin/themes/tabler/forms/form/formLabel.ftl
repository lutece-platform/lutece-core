<#-- Macro: formLabel

Description: Generates a label for a Bootstrap form group.

Parameters:
- class (string, optional): additional CSS classes to add to the label element.
- labelFor (string, optional): the ID of the input field that the label is associated with.
- labelId (string, optional): the ID of the label element.
- labelKey (string, optional): the internationalization key for the label text.
- hideLabel (list, optional): a list of label properties to hide (e.g. "label", "i18nLabel").
- mandatory (boolean, optional): whether the input field is mandatory.
- deprecated: whether the macro is deprecated.

Snippet:

    Standard form label associated with an input:

    <@formLabel labelFor='email' labelKey='#i18n{portal.users.label.email}' />

    Mandatory label with custom class:

    <@formLabel labelFor='password' labelKey='#i18n{portal.users.label.password}' mandatory=true class='form-label fw-bold' />

-->
<#macro formLabel class='form-label' labelFor='' labelId='' labelKey='' labelKeyDesc='' hideLabel=[] mandatory=false deprecated...>
<#local labelKey = labelKey?is_markup_output?then(labelKey?markup_string, labelKey) />
<#local labelKeyDesc = labelKeyDesc?is_markup_output?then(labelKeyDesc?markup_string, labelKeyDesc) />
<@deprecatedWarning args=deprecated />	
<#local labelClass = ' ' + displaySettings(hideLabel,'') />
<label class="<#if class?has_content>${class?trim}</#if><#if hideLabel?seq_contains('all')> visually-hidden</#if>"<#if labelFor?has_content> for="${labelFor}"</#if><#if labelId?has_content> id="${labelId}"</#if>>
<#if labelKey?trim?has_content><#if labelClass?trim?has_content><span class="${labelClass}"></#if>${labelKey}<#if mandatory> <span class="text-danger">*</span></#if><#if labelClass?trim?has_content></span></#if><#if labelKeyDesc?trim?has_content><span class="form-label-description">${labelKeyDesc}</span></#if><#else><#nested></#if>
</label>
</#macro>