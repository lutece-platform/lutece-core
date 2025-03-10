<#-- Macro: cInputGroup

Description: permet de définir un groupe d'inputs.

Parameters:

@param - id - string - optional - l'ID du groupe
@param - class - string - optional - ajoute une classe CSS au groupe
@param - size - string - optional - permet d'ajouter un suffixe à la classe CSS 'form-control', valeur possible 'lg' ou 'sm'
@param - params - string - optional - permet d'ajouter des parametres HTML au groupe
-->

<#macro cInputGroup class='' size='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<div class="input-group<#if class!=''> ${class!}</#if><#if size!=''> input-group-${size!}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</div>
</#macro>