<#--
Macro: cInitToast

Description: Generates a Bootstrap toast container with initialization script for displaying toast notifications on skin pages.

Parameters:
- class (string, optional): Additional CSS classes for the toast container. Default: ''.
- id (string, optional): Unique identifier for the toast container. Default: ''.
- position (string, optional): CSS position class for the container. Default: 'position-fixed'.
- showAll (boolean, optional): Whether to show all toasts on page load. Default: true.
- animation (boolean, optional): Whether toast animations are enabled. Default: true.
- duration (number, optional): Display duration of toasts in milliseconds (0 for persistent). Default: 2000.
- triggerId (string, optional): ID of the trigger element (e.g., a button) to show toasts on click. Default: ''.
- params (string, optional): Additional HTML attributes for the container. Default: ''.

Snippet:

    Basic toast container with auto-show:

    <@cInitToast id='myToastContainer' showAll=true duration=3000>
        <@cToast title='Notification' content='Your request has been submitted.' />
    </@cInitToast>

    Toast container with a trigger button:

    <@cInitToast id='alertToasts' showAll=false triggerId='showToastsBtn' duration=5000>
        <@cToast title='Success' content='Operation completed successfully.' type='success' />
    </@cInitToast>

-->
<#macro cInitToast class='' id='' position='position-fixed' showAll=true animation=true duration=2000 triggerId='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#assign styleParams='' />
<#assign toastParams=params />
<#if duration?number == 0><#assign autohide=false ><#else><#assign autohide=true ></#if>
<#if position !=''><#assign styleParams> style="${position}"</#assign></#if>
<#if styleParams !=''><#assign toastParams += styleParams /></#if>
<div<#if id !=''> id="${id}"</#if> class="toast-container ${position} p-3<#if class !=''> ${class}</#if>"<#if toastParams !=''> ${toastParams}</#if>>
<#nested>
</div>
<script>
document.addEventListener("DOMContentLoaded", function() {
    // Récupérer le conteneur spécifique par son identifiant
    var container = document.getElementById('${id}');
    var toastElList = [].slice.call(container.querySelectorAll('.toast'));
    var option = { delay: ${duration}, animation: ${animation?c}, autohide: ${autohide?c} };
    var toastList = toastElList.map(function(toastEl) {
        return new bootstrap.Toast(toastEl, option);
    });

    // Afficher les toasts au chargement si showAll est vrai
    <#if showAll>
    toastList.forEach(function(toast) {
        toast.show();
    });
    </#if>

    // Ajouter un écouteur d'événement sur le bouton spécifié si triggerId est fourni
    <#if triggerId?has_content>
    var toastTrigger = document.getElementById('${triggerId}');
    if (toastTrigger) {
        toastTrigger.addEventListener('click', function() {
            toastList.forEach(function(toast) {
                toast.show();
            });
        });
    }
    </#if>
});
</script>
</#macro>
<#--
Macro: cToast

Description: Generates a single Bootstrap toast notification element to be placed inside a cInitToast container.

Parameters:
- title (string, optional): Title displayed in the toast header. Default: ''.
- content (string, optional): Text content displayed in the toast body. Default: ''.
- imgUrl (string, optional): URL of an image to display in the toast header. Default: ''.
- type (string, optional): Toast type for styling ('info', 'success', 'warning', 'error'). Default: ''.
- delay (number, optional): Auto-hide delay in milliseconds (0 for persistent). Default: 2000.
- dismiss (boolean, optional): Whether to display a close button. Default: true.
- class (string, optional): Additional CSS classes for the toast. Default: ''.
- id (string, optional): HTML id attribute. Default: ''.
- params (string, optional): Additional HTML attributes. Default: ''.

Snippet:

    Simple toast without header:

    <@cToast content='Your changes have been saved.' />

    Toast with title and type:

    <@cToast title='Success' content='Your account has been created.' type='success' delay=5000 />

    Persistent toast with image:

    <@cToast title='New message' content='You have a new notification.' imgUrl='images/avatar.png' delay=0 />

-->
<#macro cToast title='' content='' imgUrl='' type='' delay=2000 dismiss=true class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div<#if id !=''> id="${id}"</#if> class="toast<#if class !=''> ${class}</#if><#if type !=''> ${type}</#if>" role="alert" data-autohide="${(delay gt 0)?string('true','false')}"<#if delay gt 0> data-delay="${delay}"</#if> aria-live="assertive" aria-atomic="true"<#if params !=''> ${params}"</#if>>
  <#if title !=''>
    <div class="toast-header">
      <#if imgUrl !=''><img src="${imgUrl}" class="rounded me-2"></#if>
      <strong class="me-auto">${title}</strong>
      <#if delay = 0><button type="button" class="ms-auto btn-close" data-bs-dismiss="toast" aria-label="#i18n{portal.util.labelClose}">&times;</button></#if>
    </div>
    <div class="toast-body">
      ${content}
      <#nested>
    </div>
  <#else>
    <div class="d-flex align-items-center">
      <div class="toast-body me-auto<#if !dismiss> w-100</#if>">
        ${content}
        <#nested>
      </div>
      <#if dismiss><button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="#i18n{portal.util.labelClose}"></button></#if>
    </div>
  </#if>
</div>
</#macro>