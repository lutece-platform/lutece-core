<#-- Macro: cFormRow

Description: permet de définir une ligne de formulaire.

Parameters:

@param - id - string - optional - l'ID de la ligne de formulaire
@param - class - string - optional - ajoute une classe CSS à la ligne de formulaire
@param - params - string - optional - permet d'ajouter des parametres HTML à la ligne de formulaire
-->
<#macro cFormRow class='' id='' params=''>
<@cRow class=class id=id params=params >
<#nested>
</@cRow>
</#macro>