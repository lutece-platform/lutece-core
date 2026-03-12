<#--
Macro: inputGroup

Description: Wraps form elements inside an input group div, which adds styling and functionality such as prepend/append icons and buttons.

Parameters:
- id (string, optional): the ID of the input group div.
- class (string, optional): the CSS class of the input group div.
- size (string, optional): the size of the input group div.
- params (string, optional): additional parameters for the input group div.

Snippet:

    Input group with a prepended icon and text input:

    <@inputGroup>
        <@inputGroupItem type='icon'>
            <@icon style='mail' />
        </@inputGroupItem>
        <@input type='text' name='email' id='email' placeHolder='Email address' />
    </@inputGroup>

    Input group with appended button:

    <@inputGroup>
        <@input type='text' name='search' id='search' placeHolder='Search...' />
        <@inputGroupItem type='btn'>
            <@button type='submit' color='primary' buttonIcon='search' />
        </@inputGroupItem>
    </@inputGroup>

-->
<#macro inputGroup id='' class='' size='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="input-group<#if size!=''> input-group-${size}</#if><#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>