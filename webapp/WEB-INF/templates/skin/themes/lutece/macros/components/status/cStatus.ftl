<#-- Macro: cStatus

Description: affiche un tag de status.

Parameters:
@param - level - string - optional - permet de définir le niveau de status du tag (par défaut: 'forthcoming'). Liste des Statuts : forthcoming = A venir / tobecompleted = A compléter / inprogress = En cours / over = Terminé / bygone = Passé
@param - class - string - optional - permet d'ajouter une classe CSS au tag
@param - id - string - optional - l'ID du tag
@param - label - string - optional - permet de définir le libellé du tag
@param - labelClass - string - optional - permet d'ajouter une classe CSS au libellé du tag
@param - params - string - optional - permet d'ajouter des parametres HTML au tag
-->  
<#macro cStatus level='forthcoming' class='' id='' label='' labelClass='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="status ${level}<#if class !=''> ${class}</#if>"<#if id!=''> id="${id}"</#if><#if params !=''> ${params}</#if>>
<#if level='tobecompleted'><svg width="10" height="10" viewBox="0 0 10 10" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="5" cy="5" r="5" fill="currentColor"/></svg></#if>
<span class="status-label fw-bold<#if labelClass !=''> ${labelClass}</#if>"><#if label!=''>${label!}<#else>#i18n{portal.theme.status.level.${level}.label}</#if></span>
<#nested>
</div>
</#macro>