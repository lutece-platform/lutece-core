<#-- Macro: btnToolbar

Description: Generates a button toolbar container with customizable attributes.

Parameters:
- id (string, optional): the ID attribute of the container.
- class (string, optional): additional CSS classes to add to the container.
- align (string, optional): the alignment of the container's content.
- ariaLabel (string, optional): the ARIA label of the container.
- params (string, optional): additional parameters to add to the container.

Snippet:

    Horizontal toolbar with multiple button groups:

    <@btnToolbar ariaLabel='Main toolbar'>
        <@btnGroup ariaLabel='Edit actions' class='me-2'>
            <@button title='Cut' buttonIcon='cut' color='secondary' />
            <@button title='Copy' buttonIcon='copy' color='secondary' />
            <@button title='Paste' buttonIcon='clipboard' color='secondary' />
        </@btnGroup>
        <@btnGroup ariaLabel='Format actions'>
            <@button title='Bold' buttonIcon='bold' color='secondary' />
            <@button title='Italic' buttonIcon='italic' color='secondary' />
        </@btnGroup>
    </@btnToolbar>

    Vertical toolbar:

    <@btnToolbar vertical=true ariaLabel='Vertical toolbar'>
        <@button title='Up' buttonIcon='arrow-up' color='secondary' />
        <@button title='Down' buttonIcon='arrow-down' color='secondary' />
    </@btnToolbar>

-->
<#macro btnToolbar id='' class='' vertical=false align='' ariaLabel='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if align!=''><#local class += ' ' + alignmentSettings(align,'') /></#if>
<div class="btn-group<#if vertical>-vertical</#if><#if class!=''> ${class?trim}</#if>" role="toolbar"<#if ariaLabel!=''> aria-label="${ariaLabel}"</#if><#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>