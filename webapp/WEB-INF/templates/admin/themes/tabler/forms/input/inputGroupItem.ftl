<#--
Macro: inputGroupItem

Description: Generates an input group item, which can be used to add buttons, text, or other form elements to an input group.

Parameters:
- id (string, optional): the ID of the input group item.
- pos (string, optional): the position of the input group item relative to the input field. Can be 'append' or 'prepend'. Default is 'append'.
- type (string, optional): the type of the input group item. Can be 'btn', 'icon' or 'text'. Default is 'text'.
- params (string, optional): additional parameters for the input group item.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.

Snippet:

    Icon addon prepended to an input:

    <@inputGroupItem type='icon'>
        <@icon style='user' />
    </@inputGroupItem>

    Text addon appended to an input:

    <@inputGroupItem type='text'>
        @example.com
    </@inputGroupItem>

-->
<#macro inputGroupItem id='' class='' pos='append' type='text'  params='' attrs={} deprecated...>
<@deprecatedWarning args=deprecated />
<span class="<#if type='icon'>input-icon-addon<#else>input-group-${type}</#if><#if class!=''> ${class}</#if>"<@commonAttrs id=id params=params attrs=attrs />>
<#nested>
</span>
</#macro>