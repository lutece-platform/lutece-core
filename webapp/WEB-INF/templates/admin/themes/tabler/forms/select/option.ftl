<#--
Macro: option

Description: Generates an HTML option element for use in a select list.

Parameters:
- label (string): the text to be displayed for the option.
- value (string): the value to be assigned to the option.
- id (string, optional): the ID of the option element.
- class (string, optional): the CSS class of the option element.
- selected (boolean, optional): whether the option should be selected by default. Default is false.
- disabled (boolean, optional): whether the option should be disabled. Default is false.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Basic option element:

    <@option label='France' value='FR' />

    Selected and disabled options:

    <@option label='-- Select a country --' value='' disabled=true />
    <@option label='France' value='FR' selected=true />
    <@option label='Germany' value='DE' />

-->
<#macro option label value help='' id='' class='' selected=false disabled=false params={} deprecated...>
<@deprecatedWarning args=deprecated />
<option<@commonAttrs id=id class=class params=params /> value="${value!}"<#if selected> selected</#if><#if disabled> disabled</#if><#if help?has_content> label="${help}"</#if>>${label!} <#nested></option>
</#macro>