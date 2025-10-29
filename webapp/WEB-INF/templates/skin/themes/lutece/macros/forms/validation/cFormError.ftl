<#-- Macro: cFormError

Description: permet de définir le message d'erreur d'un champs.

Parameters:

@param - id - string - required - l'ID du message d'erreur correspondant au champs afférent
@param - class - string - optional - ajoute une classe CSS au message d'erreur
@param - label - string - required - définit le libellé du message d'erreur
@param - params - string - optional - permet d'ajouter des parametres HTML au message d'erreur
-->
<#macro cFormError id label class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local cClass>invalid-feedback<#if class!=''> ${class!}</#if></#local>
<#local cParam><#if params!=''> ${params!}  role="status"</#if></#local>
<@cText class=cClass id="error_${id!}" params=cParam><@parisIcon name='alert-error' class='main-danger-color' />${label!}</@cText>
</#macro>