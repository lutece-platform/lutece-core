<#-- Macro: cBtnToolbar

Description: affiche un groupe de bouton toolbar.

Parameters:

@param - id - string - optional - l'ID du groupe de bouton toolbar
@param - label - string - required - définit l'aria-label du groupe de bouton toolbar
@param - class - string - optional - permet d'ajouter une classe CSS au groupe de bouton toolbar
@param - type - string - optional - permet de modifier le type de groupe de bouton toolbar (valeur possible: 'vertical')
@param - params - string - optional - permet d'ajouter des parametres HTML au groupe de bouton toolbar
 -->
<#macro cBtnToolbar label type='' class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local btnClass>btn-toolbar<#if type='vertical'>-vertical</#if><#if class!=''> ${class}</#if></#local>
<@cSection type='div' class=btnClass id=id params='${params} role="toolbar" aria-label="${label}"'>
    <#nested>
</@cSection>
</#macro>