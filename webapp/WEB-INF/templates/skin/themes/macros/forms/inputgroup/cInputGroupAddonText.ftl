<#--
Macro: cInputGroupAddonText

Description: Generates a text addon element (input-group-text) for an input group, using a configurable HTML tag.

Parameters:
- tag (string, optional): the HTML tag used for the text addon. Default: 'span'.
- class (string, optional): adds a CSS class to the addon. Default: ''.
- id (string, optional): the ID of the addon. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Snippet:

    Text addon with default span tag:

    <@cInputGroup>
        <@cInputGroupAddonText>$</@cInputGroupAddonText>
        <@cInput name='price' type='number' placeholder='0.00' />
    </@cInputGroup>

    Text addon with custom div tag:

    <@cInputGroup>
        <@cInput name='date' placeholder='Pick a date' />
        <@cInputGroupAddonText tag='div'>
            <i class="ti ti-calendar"></i>
        </@cInputGroupAddonText>
    </@cInputGroup>

-->
<#macro cInputGroupAddonText tag='span' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${tag} class="input-group-text<#if class!=''> ${class!}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</${tag}>
</#macro>