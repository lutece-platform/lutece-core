<#-- Macro: cFormHelp

Description: permet de définir le message d'aide à la saisie.

Parameters:

@param - id - string - required - l'ID du message d'aide correspondant au champs afférent
@param - class - string - optional - ajoute une classe CSS au message d'aide
@param - label - string - required - définit le libellé du message d'aide
@param - params - string - optional - permet d'ajouter des parametres HTML au message d'aide
-->
<#macro cFormHelp id label class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cText id='help_${id!}' class='form-text ${class!}' params=params>${label!}</@cText>
</#macro>