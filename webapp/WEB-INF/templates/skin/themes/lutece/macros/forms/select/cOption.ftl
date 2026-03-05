<#--
Macro: cOption

Description: Generates an option element for use inside a cSelect or cOptgroup dropdown.

Parameters:
- label (string, required): the display text of the option.
- value (string, required): the value of the option.
- id (string, optional): the ID of the option. Default: ''.
- class (string, optional): CSS class for the option. Default: ''.
- selected (boolean, optional): marks the option as selected. Default: false.
- disabled (boolean, optional): disables the option. Default: false.
- params (string, optional): additional HTML attributes. Default: ''.

Snippet:

    Basic options in a select:

    <@cSelect name='country'>
        <@cOption label='-- Select a country --' value='' />
        <@cOption label='France' value='FR' />
        <@cOption label='Germany' value='DE' />
        <@cOption label='Spain' value='ES' />
    </@cSelect>

    Pre-selected option:

    <@cOption label='France' value='FR' selected=true />

    Disabled option:

    <@cOption label='Not available' value='NA' disabled=true />

-->
<#macro cOption label value id='' class='' selected=false disabled=false params='' deprecated...>
<@deprecatedWarning args=deprecated />
<option <#if id!=''>id="${id}"</#if><#if class!=''> class="${class}"</#if> value="${value!}"<#if selected> selected</#if><#if disabled> disabled</#if><#if params!=''>${params}</#if>>${label!}</option>
</#macro>