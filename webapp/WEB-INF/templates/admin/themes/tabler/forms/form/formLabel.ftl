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
<@deprecatedWarning args=deprecated />	
<#local labelClass = ' ' + displaySettings(hideLabel,'') />
<#-- hideLabel 'all' already puts visually-hidden on the label: a d-none on the text would take it out of the accessibility tree, which is the opposite of the intent -->
<#if hideLabel?seq_contains('all')><#local labelClass = '' /></#if>
<label class="<#if class !=''>${class?trim}</#if><#if hideLabel?seq_contains('all')> visually-hidden</#if>"<#if labelFor!=''> for="${labelFor}"</#if><#if labelId!=''> id="${labelId}"</#if>>
<#if labelKey?trim !=''><#if labelClass?trim !=''><span class="${labelClass}"></#if>${labelKey}<#if mandatory> <span class="text-danger">*</span></#if><#if labelClass?trim !=''></span></#if><#if labelKeyDesc?trim !=''><span class="form-label-description">${labelKeyDesc}</span></#if><#else><#nested></#if>
</label>
</#macro>