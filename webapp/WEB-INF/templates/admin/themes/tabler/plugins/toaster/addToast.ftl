<#-- Macro: addToast
Description: Adds a Bootstrap toast notification to the page.
Parameters:
- title (string): the title of the notification.
- content (string): the content of the notification.
- type (string, optional): the type of the notification
-->
<#macro addToast title='' content='' titleImg='' titleInfo='' dismiss=true class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div<#if id !=''> id="${id}"</#if> class="toast<#if class !=''> ${class}</#if>" role="alert" aria-live="assertive" aria-atomic="true"<#if params !=''> ${params}"</#if>>
  <#if title !=''>
    <div class="toast-header">
      <#if titleImg !=''><img src="${titleImg}" class="rounded me-2" alt="..."></#if>
      <strong class="me-auto">${title}</strong>
      <#if titleInfo !=''><small>${titleInfo}</small></#if>
      <#if dismiss><button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="#i18n{portal.util.labelClose}"></button></#if>
    </div>
    <div class="toast-body">
      ${content}
    </div>
  <#else>
    <div class="d-flex align-items-center">
      <div class="toast-body me-auto">
        ${content}
      </div>
      <#if dismiss><button type="button" class="btn-close me-2" data-bs-dismiss="toast" aria-label="#i18n{portal.util.labelClose}"></button></#if>
    </div>
  </#if>
</div>
</#macro>