<#--
Macro: cInputGroup

Description: Generates an input group container that wraps inputs and addons together for combined form controls.

Parameters:
- class (string, optional): adds a CSS class to the group. Default: ''.
- size (string, optional): adds a size suffix to the input-group class, possible values 'lg' or 'sm'. Default: ''.
- id (string, optional): the ID of the group. Default: ''.
- params (string, optional): additional HTML attributes. Default: ''.

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

<#macro cInputGroup class='' size='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="input-group<#if class!=''> ${class!}</#if><#if size!=''> input-group-${size!}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>