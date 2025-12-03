<#-- Macro: skipNav

Description: Menu de navigation pour sauter les liens de navigation

Parameters:
@param - target - string - optional - Default: "main", target du contenu principal 
@param - skipMenu - string - optional - Default: true, si true affiche le lien vers le menu; 
-->
<#macro skipNav target='main' skipMenu=true deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="#i18n{portal.theme.skipNavLabel}" role="navigation">
    <ul class="skip-links visually-hidden visually-hidden-focusable">
        <li><a href="#${target}">#i18n{portal.theme.skipNavMain}</a></li>
        <#if skipMenu><li><a id="skip-nav" href="#main-site-menu">#i18n{portal.theme.skipNavMenu}</a></li></#if>
    </ul>
    <div id="top" class="visually-hidden visually-hidden-focusable">#i18n{portal.theme.gohome}</div>
</nav>
</#macro>