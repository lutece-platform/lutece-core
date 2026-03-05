<#--
Macro: skipNav

Description: Generates an accessible skip navigation menu, allowing keyboard users to jump directly to the main content or the site menu.

Parameters:
- target (string, optional): ID of the main content element to skip to. Default: 'main'.
- skipMenu (boolean, optional): If true, displays an additional link to skip to the site menu. Default: true.

Snippet:

    Basic usage at the top of the page layout:

    <@skipNav />

    Skip navigation targeting a custom content area without menu link:

    <@skipNav target='page-content' skipMenu=false />

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