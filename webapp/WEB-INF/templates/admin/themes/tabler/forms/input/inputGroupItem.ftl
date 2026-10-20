<#--
Macro: inputGroupItem

Description: Generates an input group item, which can be used to add buttons, text, or other form elements to an input group.

Parameters:
- id (string, optional): the ID of the input group item.
- pos (string, optional): the position of the input group item relative to the input field. Can be 'append' or 'prepend'. Default is 'append'.
- type (string, optional): the type of the input group item. Can be 'btn', 'icon' or 'text'. Default is 'text'.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.

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
<#macro inputGroupItem id='' class='' pos='append' type='text'  params={} deprecated...>
<@deprecatedWarning args=deprecated />
<span class="<#if type='icon'>input-icon-addon<#else>input-group-${type}</#if><#if class?has_content> ${class}</#if>"<@commonAttrs id=id params=params />>
<#nested>
</span>
</#macro>