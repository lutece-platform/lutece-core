<#-- Macro: cTab

Description: permet de gérer les onglets individuellement.

Parameters:

@param - id - string - optional - l'ID de l'onglet
@param - class - string - optional - ajoute une classe CSS à l'onglet
@param - url - string - required - utilisé pour définir l'id de l'onglet, et permet de définir le lien de l'onglet dans le cas où le parametre 'navigation' est true
@param - navigation - boolean - optional - permet d'intégrer l'onglet dans une balise HTML 'li'
@param - active - boolean - optional - permet de définir si l'onglet est actif au chargement de la page (par défaut: false)
@param - disabled - boolean - optional - permet de définir si l'onglet est désactivé (par défaut: false)
@param - params - string - optional - permet d'ajouter des parametres HTML à l'onglet
-->
<#macro cTab url id='' active=false navigation=false disabled=false class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if navigation>
<li class="nav-item" role="presentation">
	<a id="<#if id=''>tab_${url!?remove_beginning("#")}<#else>${id}</#if>" aria-controls="<#if id=''>${url!?remove_beginning("#")}<#else>${id}</#if>" class="nav-link<#if active> active</#if><#if disabled> disabled</#if><#if class!=''> ${class!}</#if>"<#if disabled> disabled tabindex="-1" aria-disabled="true"</#if><#if !active && !disabled> tabindex="-1"</#if> data-bs-toggle="tab" role="tab"<#if active> aria-selected="true"<#else> aria-selected="false"</#if> href="${url!}">
		<#nested>
	</a>
</li>
<#else>
<button type="button" id="<#if id=''>tab_${url!?remove_beginning("#")}<#else>${id}</#if>" aria-controls="<#if id=''>${url!?remove_beginning("#")}<#else>${id}</#if>" data-bs-toggle="tab" role="tab" data-bs-target="#<#if id=''>${url!?remove_beginning("#")}<#else>${id}</#if>"  class="nav-link<#if active> active</#if><#if disabled> disabled</#if><#if class!=''> ${class!}</#if>" <#if disabled> disabled tabindex="-1" aria-disabled="true"</#if><#if !active && !disabled> tabindex="-1"</#if><#if active> aria-selected="true"<#else> aria-selected="false"</#if>>
	<#nested>
</button>
</#if>
</#macro>