<#-- Macro: cBadge

Description: affiche un badge ou tag.

Parameters:
@param - id - string - optional - l'ID du badge
@param - label - string - optional - le titre du badge
@param - class - string - optional - permet d'ajouter une classe CSS au badge
@param - hasp - boolean - optional - permet de retirer le paragraphe qui entoure le label (par défaut: true)
@param - dismissible - boolean - optional - permet d'activer la fermeture du badge avec l'affichage d'une croix (par défaut: false)
@param - params - string - optional - permet d'ajouter des parametres HTML au badge
-->
<#macro cBadge label='' class='' hasp=true dismissible=false id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="badge <#if class !=''> ${class}</#if><#if dismissible> dismissible</#if>"<#if id!=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
<#if dismissible> 
	<div class="badge-content">
		<#if hasp><p class="badge-label">${label!}</p><#else>${label!}</#if>
		<#nested>
		<button type="button" class="btn-close" onclick="this.parentElement.style.display='none';" aria-label="Fermer"></button>
	</div>	
<#else>
	<#if hasp><p class="badge-label">${label!}</p><#else>${label!}</#if>
	<#nested>
</#if>
</div>
</#macro>