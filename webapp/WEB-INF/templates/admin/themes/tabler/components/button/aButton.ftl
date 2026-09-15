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
- params (string, optional): additional HTML parameters to apply to the button element.
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
<#macro aButton name='' id='' href='' target='' size='' color='primary' style='btn' align='' class='' title='' tabIndex='' hideTitle=[] buttonIcon='' disabled=false iconPosition='left' dropdownMenu=false  params='' deprecated...>
<#local size = size?is_markup_output?then(size?markup_string, size) />
<#local class = class?is_markup_output?then(class?markup_string, class) />
<@deprecatedWarning args=deprecated />
<#local params = params />
<#-- Visibility of button title -->
<#local displayTitleClass = displaySettings( hideTitle, 'block' ) />
<#if color = 'default' || color='btn-default' || color='btn-secondary' || color='secondary'>
	<#local buttonColor = 'btn-default' />
<#elseif !color?has_content>
	<#local buttonColor = 'btn-primary' />
<#else>
	<#local buttonColor = 'btn-' + color />
</#if>
<#if style='card-control'>
	<#assign style='text-right btn-link' />
</#if>
<#local class += alignmentSettings(align,'') />
<#-- Size class -->
<#local buttonSize = '' />
<#local paramsExtra = '' />
<#if size?starts_with('style') == true ><#local paramsExtra = ' ' + size /><#else><#local buttonSize = size /></#if>	
<a class="${style}<#if buttonSize?has_content> btn-${buttonSize}</#if><#if color?has_content> ${buttonColor}</#if><#if class?has_content> ${class}</#if>"<#if name?has_content> name="${name}"</#if><#if id?has_content> id="${id}"</#if> href="${href}" title="${title}"<#if target?has_content> target="${target}"</#if><#if params?has_content> ${params}</#if>${paramsExtra}<#if disabled> disabled</#if><#if dropdownMenu> data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"</#if>>
	<#if buttonIcon?has_content && iconPosition='left'><#local buttonIcon = buttonIcon  /><@icon style=buttonIcon /></#if>
	<span class="${displayTitleClass}">${title}</span>
	<#if buttonIcon?has_content && iconPosition='right'><#local buttonIcon = buttonIcon  /><@icon style=buttonIcon /></#if>
	<#if !dropdownMenu>
	<#nested>
	</#if>
</a>
<#if dropdownMenu>
<div class="dropdown-menu"<#if id?has_content> id="${id}" aria-labelledby="${id}"</#if>>
<#nested>
</div>
</#if>
</#macro>