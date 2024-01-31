<#--
Macro: alert

Description: Generates an HTML alert element with optional title, icon, and dismiss button.

Parameters:
- class (string, optional): the CSS class of the alert element.
- color (string, optional): the background color of the alert element.
- titleLevel (string, optional): the level of the title element (h1, h2, h3, h4, h5, or h6).
- title (string, optional): the title of the alert element.
- iconTitle (string, optional): the name of the icon to include in the alert element.
- dismissible (boolean, optional): whether the alert should be dismissible.
- id (string, optional): the ID of the alert element. If not provided, a default ID will be generated.
- params (string, optional): additional HTML attributes to include in the alert element.
-->
<#macro alert class='d-flex align-items-center' color='' titleLevel='h3' title='' iconTitle='' dismissible=false id='' params=''>
<div class="alert rounded <#if color!=''>bg-body text-${color} border-${color}</#if><#if class!=''> ${class}</#if><#if dismissible> alert-dismissible</#if> shadow"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#if color!=''><#assign txtColor> text-${color}</#assign></#if>
<#if iconTitle!=''><@icon style=iconTitle class='mx-2${txtColor}' /></#if>
<#if title!=''><${titleLevel} class="alert-title ms-2">${title}</${titleLevel}></#if>
<#nested>
<#if dismissible><a class="btn-close mt-2" data-bs-dismiss="alert" aria-label="#i18n{portal.util.labelClose}"></a></#if>
</div>
</#macro>