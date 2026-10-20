<#--
Macro: cOptgroup

Description: Generates an optgroup element to group options within a select dropdown.

Parameters:
- label (string, required): the label of the option group.
- class (string, optional): CSS class for the optgroup. Default: ''.
- disabled (boolean, optional): disables the option group. Default: false.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Snippet:

    Option group inside a select:

    <@cSelect name='city'>
        <@cOptgroup label='Ile-de-France'>
            <@cOption label='Paris' value='75' />
            <@cOption label='Nanterre' value='92' />
        </@cOptgroup>
        <@cOptgroup label='Provence-Alpes-Cote d Azur'>
            <@cOption label='Marseille' value='13' />
            <@cOption label='Nice' value='06' />
        </@cOptgroup>
    </@cSelect>

-->
<#macro cOptgroup label class='' disabled=false params={} deprecated...>
<@deprecatedWarning args=deprecated />
<optgroup label="${label!}" <#if disabled> disabled</#if><@commonAttrs class=class params=params />>
<#nested>
</optgroup>
</#macro>