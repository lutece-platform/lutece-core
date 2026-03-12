<#--
Macro: messages
Description: Generates HTML alert elements for displaying error, info, and warning messages.
Parameters:
- errors (list, optional): a list of error messages to display.
- infos (list, optional): a list of info messages to display.
- warnings (list, optional): a list of warning messages to display.
- errors_class (string, optional): the CSS class of the alert element for error messages.
- infos_class (string, optional): the CSS class of the alert element for info messages.
- warnings_class (string, optional): the CSS class of the alert element for warning messages.

Snippet:

    Simple box with nested content:

    <@box>
        <@boxBody>
            <p>This is the content of the box.</p>
        </@boxBody>
    </@box>

    Box with title and color:

    <@box title='User List' color='primary'>
        <p>Content is automatically wrapped in a box body when a title is provided.</p>
    </@box>

    Collapsible box with custom class:

    <@box title='Advanced Settings' color='info' collapsed=true class='my-custom-class' id='settings-box'>
        <p>This content is initially collapsed.</p>
    </@box>

-->
<#macro box color='' id='' style='' class='' title=''  collapsed=false  params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="card mb-3<#if color!=''> card-${color}<#else> card-transparent</#if> card-outline<#if style!=''> text-${style}</#if><#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#if title =''>
<#nested>
<#else>
<@boxHeader title=title collapsed=isCollapsed params=params skipHeader=true />
<@boxBody collapsed = collapsed>
<#nested>
</@boxBody>
</#if>
</div>
</#macro>