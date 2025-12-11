<#-- Macro: button

Description: Generates a button with customizable attributes.

Parameters:
- name (string, optional): the name attribute of the button.
- id (string, optional): the ID attribute of the button.
- type (string, optional): the type of the button, defaults to 'button'.
- size (string, optional): the size of the button.
- color (string, optional): the color of the button.
- style (string, optional): the style of the button.
- class (string, optional): additional CSS classes to add to the button.
- params (string, optional): additional parameters to add to the button.
- value (string, optional): the value attribute of the button.
- title (string, optional): the title attribute of the button.
- tabIndex (string, optional): the tab index of the button.
- hideTitle (array, optional): an array of breakpoints to hide the button title.
- showTitle (boolean, optional): whether to show the button title.
- showTitleXs (boolean, optional): whether to show the button title on XS breakpoint.
- showTitleSm (boolean, optional): whether to show the button title on SM breakpoint.
- showTitleMd (boolean, optional): whether to show the button title on MD breakpoint.
- showTitleLg (boolean, optional): whether to show the button title on LG breakpoint.
- buttonIcon (string, optional): the icon to display in the button.
- disabled (boolean, optional): whether the button is disabled.
- iconPosition (string, optional): the position of the icon, either 'left' or 'right'.
- dropdownMenu (boolean, optional): whether the button is part of a dropdown menu.
- cancel (boolean, optional): whether the button cancels a form.
- formId (string, optional): the ID of the form the button belongs to.
- buttonTargetId (string, optional): the ID of the target element the button controls.
- deprecated (string, optional): a message indicating that the macro is deprecated.
-->
<#macro button name='' id='' type='button' size='' color='' style='' class='' value='' title='' tooltip='' tabIndex='' hideTitle=[] showTitle=true showTitleXs=true showTitleSm=true showTitleMd=true showTitleLg=true buttonIcon='' disabled=false iconPosition='left' dropdownMenu=false cancel=false formId='' buttonTargetId=''  params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local params = params />
	<#if cancel || color = 'default' || color='btn-default' || color='btn-secondary' || color='secondary'>
		<#local buttonColor = 'default' />
	<#elseif !cancel && color=''>
		<#local buttonColor = 'primary' />
	<#else>
		<#local buttonColor = color />
	</#if>
	<#-- Visibility of button title -->
	<#local displayTitleClass = displaySettings( hideTitle,'inline-flex') />
	<#-- Visibility of button title: backwards compatibility with Lutece v6, BS3 only -->
	<#local showTitleClass = '' />
	<#if style != ''>
		<#if style?contains('card-control')>
			<#if style?contains('collapse')>
				<#local widgetAction = 'collapse' />
				<script>
				document.addEventListener('DOMContentLoaded', function() {
					var targetEl = document.querySelector('${buttonTargetId}');
					if (targetEl) {
					<#if buttonIcon = 'minus'>
						targetEl.classList.add('show');
					<#else>
						targetEl.classList.add('collapse');
					</#if>
					}
				});
				</script>
			<#elseif style?contains('remove')>
				<#local widgetAction = 'remove' />
			</#if>
			<#local btnStyle = style?replace('collapse|remove', '', 'r')?trim />
		<#elseif style?contains('modal')>
			<#local widgetAction = 'modal' />
			<#local btnStyle = style?replace('modal', '', 'r')?trim />
		<#else>
			<#local btnStyle = style />
		</#if>
	</#if>
	<#-- Size class -->
	<#local buttonSize = '' />
	<#if size?starts_with('style') == true >
		<#local params = params + ' ' + size />
	<#else>
		<#local buttonSize = size />
	</#if>	
	<button class="<#if style!='close'>btn</#if><#if buttonSize!=''> btn-${buttonSize}</#if><#if buttonColor!='' && !dropdownMenu> btn-${buttonColor}</#if><#if btnStyle?? && btnStyle!=''> ${btnStyle}</#if><#if dropdownMenu> dropdown-toggle</#if><#if class!=''> ${class}</#if>" type="${type}"<#if tooltip!=''>title="${tooltip}"<#else><#if title!=''> title="${title}"</#if></#if><#if name!=''> name="${name}"</#if><#if id!=''> id="${id}"</#if><#if value!=''> value="${value}"</#if><#if params!=''> ${params}</#if><#if disabled> disabled</#if><#if dropdownMenu> data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"</#if><#if widgetAction?? && widgetAction!=''><#if widgetAction = 'collapse' || widgetAction = 'modal'> data-bs-toggle="${widgetAction}"<#elseif widgetAction = 'remove'> data-bs-dismiss="alert"</#if></#if><#if buttonTargetId!=''> data-bs-target="${buttonTargetId}"</#if><#if cancel> formnovalidate</#if><#if formId!=''> form="${formId}"</#if>>
		<#if buttonIcon!='' && iconPosition='left'>
			<#local buttonIcon = buttonIcon + ' me-1' />
			<@icon style=buttonIcon />
		</#if>
		<#local nestedContent><#nested /></#local>
		<#local nestedContent = nestedContent?trim />
		<#if nestedContent=''><#if displayTitleClass!=''><span class="${displayTitleClass}"></#if>${title}<#if displayTitleClass!=''></span></#if></#if>
		<#if nestedContent!='' && !dropdownMenu><#if displayTitleClass!=''><span class="${displayTitleClass}"></#if><#nested><#if displayTitleClass!=''></span></#if></#if>
		<#if buttonIcon!='' && iconPosition='right'>
			<#local buttonIcon = buttonIcon + ' ms-1' />
			<@icon style=buttonIcon />
		</#if>
	</button>
	<#if dropdownMenu>
		<ul class="dropdown-menu"<#if id!=''> id="${id}-content" aria-labelledby="${id}-content"</#if>>
			<#nested>
		</ul>
	</#if>
</#macro>