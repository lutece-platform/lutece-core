<#-- Macro: cInputGroupAddonText

Description: permet de définir un Addon de texte au groupe d'inputs.

Parameters:

@param - id - string - optional - l'ID de l'addon
@param - class - string - optional - ajoute une classe CSS à l'addon
@param - tag - string - optional - définit la balise du texte ajouté au groupe (par défaut: 'span')
@param - params - string - optional - permet d'ajouter des parametres HTML l'addon
-->
<#macro cInputGroupAddonText tag='span' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<${tag} class="input-group-text<#if class!=''> ${class!}</#if>"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
<#nested>
</${tag}>
</#macro>