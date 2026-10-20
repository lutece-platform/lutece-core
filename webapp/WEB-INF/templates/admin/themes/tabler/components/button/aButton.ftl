<#--
Macro: aButton
Description: Generates an HTML button or link with various customization options, including color, size, alignment, icon, and title.
Parameters:
- name (string, optional): the name attribute of the button element.
- id (string, optional): the ID attribute of the button element.
- href (string, optional): the URL to which the button will link.
- target (string, optional): the target of the link, such as "_blank".
- size (string, optional): the size of the button, such as "sm" or "lg".
- color (string, optional): the color of the button, such as "primary" or "warning".
- style (string, optional): the style of the button, such as "btn" or "card-control".
- align (string, optional): the alignment of the button, such as "left" or "right".
- class (string, optional): additional CSS classes to apply to the button.
- params (map, optional): additional HTML attributes as name/value pairs, e.g. {'aria-label': label}. Names out of the allow list of htmlAttributes (event handlers, URLs, styles...) are refused and every value is escaped. Default: {}.
- title (string, optional): the title of the button.
- tabIndex (string, optional): the tab index of the button.
- hideTitle (list, optional): a list of screen reader text to hide the title of the button.
- buttonIcon (string, optional): the icon to display inside the button.
- disabled (boolean, optional): whether or not the button is disabled.
- iconPosition (string, optional): the position of the icon inside the button, such as "left" or "right".
- dropdownMenu (boolean, optional): whether or not to include a dropdown menu inside the button.

Snippet:

    Primary link button with icon:

    <@aButton href='jsp/admin/ManageUsers.jsp' buttonIcon='users' title='Manage Users' color='primary' />

    Secondary small button opening in a new tab:

    <@aButton href='https://lutece.paris.fr' buttonIcon='external-link' title='Documentation' color='secondary' size='sm' target='_blank' />

    Link button with dropdown menu:

    <@aButton href='#' title='Export' buttonIcon='download' color='primary' id='export-menu' dropdownMenu=true>
        <a class="dropdown-item" href="export.jsp?format=csv">CSV</a>
        <a class="dropdown-item" href="export.jsp?format=pdf">PDF</a>
    </@aButton>

-->
<#macro aButton name='' id='' href='' target='' size='' color='primary' style='btn' align='' class='' title='' tabIndex='' hideTitle=[] buttonIcon='' disabled=false iconPosition='left' dropdownMenu=false  params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#-- Visibility of button title -->
<#local displayTitleClass = displaySettings( hideTitle, 'block' ) />
<#if color = 'default' || color='btn-default' || color='btn-secondary' || color='secondary'>
	<#local buttonColor = 'btn-default' />
<#elseif !color?has_content>
	<#local buttonColor = 'btn-primary' />
<#else>
	<#local buttonColor = 'btn-' + color />
</#if>
<#-- Size class, or a style attribute when size starts with 'style' -->
<#local buttonSize = '' />
<#local sizeStyle = '' />
<#if size?starts_with('style') == true ><#local sizeStyle = size /><#else><#local buttonSize = size /></#if>
<#local classBase = style + buttonSize?has_content?then( ' btn-' + buttonSize, '' ) + color?has_content?then( ' ' + buttonColor, '' ) + dropdownMenu?then( ' btn-action', '' ) />
<#if dropdownMenu><div class="dropdown"></#if>
<a<@commonAttrs id=id classBase=classBase class=class classExtra=alignmentSettings( align, '' ) params=params /><#if name?has_content> name="${name}"</#if> href="${href}" title="${title}"<#if target?has_content> target="${target}"</#if><#if sizeStyle?has_content> ${sizeStyle}</#if><#if disabled> aria-disabled="true" tabindex="-1"</#if><#if dropdownMenu> data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"</#if>>
	<#if buttonIcon?has_content && iconPosition='left'><@icon style=buttonIcon /></#if>
	<span class="${displayTitleClass}">${title}</span>
	<#if buttonIcon?has_content && iconPosition='right'><@icon style=buttonIcon /></#if>
	<#if !dropdownMenu>
	<#nested>
	</#if>
</a>
<#if dropdownMenu>
<div class="dropdown-menu"<#if id?has_content> id="${id}-content" aria-labelledby="${id}"</#if>>
<#nested>
</div>
</div>
</#if>
</#macro>