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
- id (string, optional): the ID of the empty state container.
- class (string, optional): additional CSS classes for the empty state container.
- iconClass (string, optional): additional CSS classes for the icon container.
- imgClass (string, optional): additional CSS classes for the image container.
- actionClass (string, optional): additional CSS classes for the action button container.

Snippet:

    Basic empty state with default icon and messages:

    <@empty />

    Empty state with custom title, subtitle, and action button:

    <@empty title='No users found' subtitle='Get started by creating your first user' actionTitle='Create User' actionUrl='/admin/users/create' actionIcon='user-plus' />

    Empty state with a custom image instead of an icon:

    <@empty title='No results' subtitle='Try different search terms' img='images/empty-search.svg' />

    Empty state with nested custom content:

    <@empty title='Your inbox is empty' iconName='mail'>
        <div class="empty-action">
            <a class="btn btn-outline-primary" href="/admin/mail/compose">Compose Message</a>
        </div>
    </@empty>

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