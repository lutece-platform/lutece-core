<#-- SkipNav -->
<#-- Params
    - title   : Title shown over the banner
    - logoImg : Default: Empty string, show logo instead of text title, title is set as title html attribute for the logo image.
    - hasMenu : Default: true; 
    Nested content : Shows default page menu, but can other item can be add using @mainNavItem macro.
-->
<#macro adminSkipNav target='main-menu' skipNav=true skipContent=false deprecated...>
<@deprecatedWarning args=deprecated />
<nav aria-label="#i18n{portal.theme.skipNavLabel}" role="navigation" class="visually-hidden visually-hidden-focusable">
    <ul class="skip-links">
        <#if skipNav><li><a href="jsp/admin/AdminMenu.jsp#${target}">#i18n{portal.theme.skipNavLabel}</a></li></#if>
        <#if skipContent><li><a id="skip-nav" href="jsp/admin/AdminMenu.jsp#lutece-main">#i18n{portal.util.labelSkipContent}</a></li></#if>
    </ul>
    <div id="top" >#i18n{portal.util.labelHome}</div>
</nav>
</#macro> 