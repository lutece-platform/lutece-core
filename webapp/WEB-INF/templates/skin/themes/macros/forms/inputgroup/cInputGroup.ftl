<#--
Macro: cInputGroup

Description: Generates an input group container that wraps inputs and addons together for combined form controls.

Parameters:
- class (string, optional): adds a CSS class to the group. Default: ''.
- size (string, optional): adds a size suffix to the input-group class, possible values 'lg' or 'sm'. Default: ''.
- id (string, optional): the ID of the group. Default: ''.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

Showcase:
- desc: "Groupe de champs - @cInputGroup"
- bs: "forms/input-group"
- newFeature: false

Snippet:

    Basic input group with text addon:

    <@cInputGroup>
        <@cInputGroupAddonText>@</@cInputGroupAddonText>
        <@cInput name='email' placeholder='Email address' />
    </@cInputGroup>

    Input group with size:

    <@cInputGroup size='lg'>
        <@cInput name='search' placeholder='Search...' />
        <@cInputGroupAddon addonText='Go' />
    </@cInputGroup>

-->

<#macro cInputGroup class='' size='' id='' params={} deprecated...>
<@deprecatedWarning args=deprecated />
<div class="input-group<#if class?has_content> ${class!}</#if><#if size?has_content> input-group-${size!}</#if>"<@commonAttrs id=id params=params />>
<#nested>
</div>
</#macro>