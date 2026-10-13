<#-- Macro: btnGroup

Description: Generates a button group container with customizable attributes.

Parameters:
- id (string, optional): the ID attribute of the container.
- class (string, optional): additional CSS classes to add to the container.
- align (string, optional): the alignment of the container's content.
- size (string, optional): the size of the container.
- params (string, optional): additional parameters to add to the container.
- attrs (hash, optional): additional HTML attributes as name/value pairs, e.g. {'aria-hidden': 'true'}. Prefer it to params: the macro writes the attribute and its value is escaped like any value of the page.
- ariaLabel (string, optional): the ARIA label of the container.
- hide (array, optional): an array of breakpoints to hide the container.
- deprecated (string, optional): a message indicating that the macro is deprecated.

Snippet:

    Basic button group with multiple buttons:

    <@btnGroup ariaLabel='User actions'>
        <@button title='Edit' buttonIcon='edit' color='primary' />
        <@button title='Copy' buttonIcon='copy' color='primary' />
        <@button title='Delete' buttonIcon='trash' color='danger' />
    </@btnGroup>

    Small right-aligned button group:

    <@btnGroup size='sm' align='right' ariaLabel='Pagination'>
        <@button title='Previous' buttonIcon='chevron-left' color='secondary' />
        <@button title='Next' buttonIcon='chevron-right' color='secondary' />
    </@btnGroup>

-->
<#macro btnGroup id='' class='' align='' size='' params='' ariaLabel='' hide=[] attrs={}   deprecated...>
<@deprecatedWarning args=deprecated />	
<#local classExtra = displaySettings( hide, 'inline-flex' ) + ' ' + alignmentSettings( align, '' ) />
<div<@commonAttrs id=id classBase='btn-group' + size?has_content?then( ' btn-group-' + size, '' ) class=class classExtra=classExtra params=params attrs=attrs /> role="group"<#if ariaLabel?has_content> aria-label="${ariaLabel}"</#if>>
	<#nested>
</div>
</#macro>