<#-- Macro: initToast

Description: Initializes a Polipop instance for displaying Bootstrap toast notifications.

Parameters:
@param - class - string - optionnel : Permet d'ajouter des classes CSS supplémentaires au conteneur de toasts.
@param - id - string - optionnel : Identifiant unique du conteneur de toasts.
@param - position - string - optionnel (par défaut : 'position-fixed') : Position CSS du conteneur de toasts. Valeurs possibles : 'position-fixed', 'position-static', etc.
@param - showAll - boolean - optionnel (par défaut : true) : Indique si tous les toasts doivent être affichés au chargement de la page.
@param - animation - boolean - optionnel (par défaut : true) : Active les animations des toasts si true.
@param - duration - number - optionnel (par défaut : 2000) : Durée d'affichage des toasts en millisecondes.
@param - triggerId - string - optionnel : Identifiant de l'élément déclencheur (par exemple un bouton) pour afficher les toasts.
@param - params - string - optionnel : Paramètres supplémentaires à ajouter au conteneur de toasts.
@param - deprecated - string - optionnel : Paramètres deprecated.
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
<#-- Macro: addToast

Description: Adds a Bootstrap toast notification to the page.

Parameters:
@param - title - string - optional : titre de la notification toast.
@param - content - string - optional : contenu de la notification toast.
@param - imgUrl - string - optional : url de l'image à afficher dans le head de la notification toast.
@param - type - string - optional : type de notification toast. Peut être "info", "success", "warning" ou "error".
@param - delay - number - required : durée de l'apparition de la notification toast.
@param - dismiss - boolean - required : affichage de l'icone de fermeture, à true par défaut.
@param - id - string - optional - l'ID de la notification toast
@param - class - string - optional - permet d'ajouter une classe CSS au tableau
@param - deprecated - string - optionnel : paramètres deprecated.
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