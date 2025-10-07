<#-- Macro: cTab

Description: permet de gérer le container du contenu des onglets.

Parameters:

@param - id - required - optional - l'ID du container
@param - class - string - optional - ajoute une classe CSS au container
@param - params - string - optional - permet d'ajouter des parametres HTML au container
-->
<#macro cTabContent id class='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<@cBlock class='tab-content ${class!}' id=id params=params>
<#nested>
</@cBlock>
<#if themeTabsIsLoaded?? && themeTabsIsLoaded>
<#else>
<script src="${commonsSiteThemePath}${commonsSiteJsPath}theme-tabs.min.js"></script>
<#assign themeTabsIsLoaded = true />
</#if>
</#macro>