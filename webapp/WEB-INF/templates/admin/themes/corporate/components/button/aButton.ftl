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
-->
<#macro aButton name='' id='' href='' target='' size='' color='primary' style='btn' align='' class='' title='' tabIndex='' hideTitle=[] buttonIcon='' disabled=false iconPosition='left' dropdownMenu=false  params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#-- Visibility of button title -->
<#local displayTitleClass = displaySettings(hideTitle,'inline') />
<#if color = 'default' || color='btn-default' || color='btn-secondary' || color='secondary'>
	<#local buttonColor = 'btn-default' />
<#elseif color=''>
	<#local buttonColor = 'btn-primary' />
<#else>
	<#if color == 'primary' || color == 'secondary' || color == 'success' || color == 'info' || color == 'warning' || color == 'danger'>
		<#local buttonColor = 'btn-' + color />
	<#else>
		<#local buttonColor = 'bg-' + color />
	</#if>
</#if>
<#if style='card-control'>
	<#assign style='text-right btn-link' />
</#if>
<#local class += alignmentSettings(align,'') />
<#-- Size class -->
<#if size == 'xs'>
	<#local buttonSize = 'sm' />
<#elseif size == 'sm' || size == ''>
	<#local buttonSize = '' />
<#elseif size == 'lg'>
	<#local buttonSize = size />
<#else>
	<#local buttonSize = '' />
</#if>
<a class="${style}<#if buttonSize!=''> btn-${buttonSize}</#if><#if color!=''> ${buttonColor}</#if><#if class!=''> ${class}</#if>"<#if name!=''> name="${name}"</#if><#if id!=''> id="${id}"</#if> href="${href}" title="${title}"<#if target!=''> target="${target}"</#if><#if params!=''> ${params}</#if><#if disabled> disabled</#if><#if dropdownMenu> data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"</#if>>
	<#if buttonIcon!='' && iconPosition='left'>
		<#local buttonIcon = buttonIcon />
		<@icon style=buttonIcon />
	</#if>
	<span class="${displayTitleClass}">${title}</span>
	<#if buttonIcon!='' && iconPosition='right'>
		<#local buttonIcon = buttonIcon />
		<@icon style=buttonIcon />
	</#if>
	<#if !dropdownMenu>
	<#nested>
	</#if>
</a>
<#if dropdownMenu>
<div class="dropdown-menu"<#if id!=''> id="${id}" aria-labelledby="${id}"</#if>>
	<#nested>
</div>
</#if>
</#macro>