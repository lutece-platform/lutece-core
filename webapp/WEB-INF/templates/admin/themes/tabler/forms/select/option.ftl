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
- params (string, optional): additional parameters for the option element.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Basic option element:

    <@option label='France' value='FR' />

    Selected and disabled options:

    <@option label='-- Select a country --' value='' disabled=true />
    <@option label='France' value='FR' selected=true />
    <@option label='Germany' value='DE' />

-->
<#macro option label value help='' id='' class='' selected=false disabled=false params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<option<#if id?has_content>id="${id}</#if><#if class?has_content> class="${class}"</#if> value="${value!}"<#if selected> selected</#if><#if disabled> disabled</#if><#if help?has_content> label="${help}"</#if><@commonAttrs params=params attrs=attrs />>${label!} <#nested></option>
</#macro>