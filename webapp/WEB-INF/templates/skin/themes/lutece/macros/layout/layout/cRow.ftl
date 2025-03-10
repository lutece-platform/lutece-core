<#-- Macro: cRow

Description: affiche une ligne.

Parameters:
@param - id - string - required - identifiant unique de la ligne
@param - class - string - optional - classe(s) css de la ligne
@param - params - string - optional - permet d'ajouter des paramètres HTML à la ligne
-->
<#macro cRow class='' id='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cSection type='div' class='row ${class}' id=id params=params>
<#nested>
</@cSection>
</#macro>