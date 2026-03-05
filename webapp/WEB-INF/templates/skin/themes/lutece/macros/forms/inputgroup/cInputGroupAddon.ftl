<#--
Macro: cInputGroupAddon

Description: Generates an addon element for an input group, optionally displaying text content via cInputGroupAddonText.

Parameters:
- append (boolean, optional): if true, appends the addon; if false, prepends it. Default: true.
- addonText (string, optional): text content for the addon. Default: ''.
- class (string, optional): adds a CSS class to the addon. Default: ''.
- id (string, optional): the ID of the addon. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

Snippet:

    Addon with text:

    <@cInputGroup>
        <@cInput name='amount' type='number' placeholder='0.00' />
        <@cInputGroupAddon addonText='EUR' />
    </@cInputGroup>

    Addon with nested content (icon):

    <@cInputGroup>
        <@cInput name='search' placeholder='Search...' />
        <@cInputGroupAddon>
            <button class="btn btn-primary" type="button">Search</button>
        </@cInputGroupAddon>
    </@cInputGroup>

-->
<#macro cInputGroupAddon append=true addonText='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if addonText !=''><@cInputGroupAddonText class=class id=id params=params >${addonText!}</@cInputGroupAddonText></#if>
<#nested>
</#macro>