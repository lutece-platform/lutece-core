<#-- Macro: cInputGroupAddon

Description: permet de définir un Addon au groupe d'inputs.

Parameters:

@param - id - string - optional - l'ID de l'addon
@param - class - string - optional - ajoute une classe CSS à l'addon
@param - append - string - optional - si true, ajoute un suffixe 'append' à la classe CSS du groupe. Si false, ajoute le suffixe prepend (par défaut: true)
@param - addonText - string - optional - ajoute un texte à l'addon
@param - params - string - optional - permet d'ajouter des parametres HTML à l'addon
-->
<#macro cInputGroupAddon append=true addonText='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#if addonText !=''><@cInputGroupAddonText class=class id=id params=params >${addonText!}</@cInputGroupAddonText></#if>
<#nested>
</#macro>