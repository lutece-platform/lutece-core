<#--
Macro: alert

Description: Generates an HTML alert element with optional title, icon, and dismiss button.

Parameters:
- class (string, optional): the CSS class of the alert element.
- color (string, optional): the background color of the alert element.
- titleLevel (string, optional): the level of the title element (h1, h2, h3, h4, h5, or h6).
- title (string, optional): the title of the alert element.
- titleClass (string, optional): the CSS class of the title element.
- iconTitle (string, optional): the name of the icon to include in the alert element.
- dismissible (boolean, optional): whether the alert should be dismissible.
- id (string, optional): the ID of the alert element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the alert element.
-->
<#macro alert class='' color='info' titleLevel='h4' title='' titleClass='' iconTitle='' dismissible=false id='' params=''>
<div class="alert<#if color!=''> alert-${color}</#if><#if class!=''> ${class}</#if><#if dismissible> alert-dismissible</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if> role="alert">
<#if iconTitle!=''> <div class="alert-icon"><@icon style=iconTitle /></div></#if>
<div>
<#if title!=''><${titleLevel} class="alert-heading ${titleClass}">${title}</${titleLevel}></#if>
<#nested>
</div>
<#if dismissible><a class="btn-close" data-bs-dismiss="alert" aria-label="#i18n{portal.util.labelClose}"></a></#if>
</div>
</#macro>