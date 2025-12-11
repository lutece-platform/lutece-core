<#-- Macro: empty

Description: Generates an empty state message with an optional icon, image, title, subtitle, and action button.

Parameters:
- title (string, optional): the main title of the empty state message.
- subtitle (string, optional): the subtitle of the empty state message.
- iconName (string, optional): the name of the icon to use, using the Themify Icon font (e.g. "mood-empty", "heart", "star", etc.).
- img (string, optional): the URL of the image to display.
- actionTitle (string, optional): the title of the action button.
- actionBtn (string, optional): the color of the action button, using a Bootstrap color class (e.g. "primary", "secondary", "success", "info", "warning", "danger", "light", or "dark").
- actionIcon (string, optional): the name of the icon to use for the action button, using the Themify Icon font.
- actionUrl (string, optional): the URL to link to when the action button is clicked.
-->
<#macro empty title='' subtitle='' id='' class='' iconName='mood-empty' iconClass='' img='' imgClass='' actionTitle='' actionBtn='primary' actionIcon='plus' actionClass='' actionUrl='#' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="empty<#if class!=''> ${class}</#if>"<#if id!=''> id="${id}"</#if>>
<#if img=''>
	<div class="empty-icon<#if iconClass!=''> ${iconClass}</#if>"><@icon prefix='ti ti-' style='${iconName}' params='style="font-size:48px"' /></div>
<#else>
	<div class="empty-img<#if imgClass!=''> ${imgClass}</#if>"><img src="${img}" height="128" alt=""></div>
</#if>
	<p class="empty-title"><#if title=''>#i18n{portal.util.message.emptyTitle}	<#else>${title}</#if></p>
<#if subtitle !=''>
	<p class="empty-subtitle text-muted">${subtitle}</p>
<#else>
	<p class="empty-subtitle text-muted">#i18n{portal.util.message.emptySubTitle}</p>
</#if>
<#nested>
<#if actionTitle !=''>
	<div class="empty-action<#if actionClass!=''> ${actionClass}</#if>">
		<a href="${actionUrl}" class="btn btn-${actionBtn}"><#if actionIcon !=''><@icon prefix='ti ti-' style='${actionIcon}' /></#if> ${actionTitle}</a>
	</div>
</#if>
</div>
</#macro>