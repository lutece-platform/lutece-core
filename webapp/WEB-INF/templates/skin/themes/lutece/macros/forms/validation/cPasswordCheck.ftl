<#-- Macro: cPasswordCheck

Description: permet de définir le message d'erreur d'un champs.

Parameters:

@param - id - string - required - l'ID du champs password
-->
<#macro cPasswordCheck id >
<#local checkMsg><@cInline class='charlength'><@parisIcon name='check' /> #i18n{theme.labelNbChars}</@cInline> <@cInline class='uppercase'><@parisIcon name='check' /> #i18n{theme.labelNbUppercase} </@cInline> <@cInline class='digit'><@parisIcon name='check' /> #i18n{theme.labelNbDigit}</@cInline></#local>
<@cFormHelp id checkMsg />
</#macro>