<#-- Macro: cMainNav

Description: affiche une barre de navigation.

Parameters:
@param - id - string - optional - identifiant unique de la barre de navigation
@param - class - string - optional - classe(s) css de la barre de navigation
@param - title - string - required - Titre du site. Par défaut récupère le nom de la variable "favorite".
@param - logoImg - string - required - url de l'image utilisée comme logo pour le titre du site
@param - href - string - required - url de redirection sur le logo du titre du site
@param - hasMenu - boolean - required -  si true, le site possède un menu
@param - hasNestedMenu - boolean - optionnal -  si true, le site possède un menu
@param - isSidebar - boolean - required -  si true, le menu sera vertical calé à gauche
@param - isOnlyHome - boolean - required -  si true, le menu ne sera affiché que sur la page d'accueil
@param - isSibebarCollapsible - boolean - required -  si true, le menu pourra être masqué
@param - sidebarMenuClass - string - optionnal -  Ajoute une classe au menu vertical. Par défaut vide.
@param - showDefaultMenu - boolean - required -  si true, affiche le menu
@param - hasSearchMenu - boolean - required -  si true, ajoute la rercherche au menu
@param - typeSearch - string - required -  Si Field, ajoute le champs de rercherche sinon un lien paramètre sur l'url de recherche précisée dans les paramètres les propriétés du site. Si la valeur est button une icone de recherche sera positionnée après le menu.
@param - searchUrl - string - required -  Url pour l'icone de recherche. Par défaut récupère la valeur de l'url de recherche précisée dans les paramètres les propriétés du site.
@param - searchAction - string - required -  Url d'action pour le formulaire nécessite typeSearch='field'
@param - searchSolr - boolean - required - si "true" fait la recherche sur Solr et non sur le moteur de recherche par défaut.
@param - searchParams - string - optional - tous champs à ajouter au formulaire nécessitent typeSearch='field'
@param - hasLogin - boolean - required - si true, le site est authentifié et propose un bouton de login (connexion Mon Paris)
@param - loginClass - string - optional - classe CSS pour le bouton de login
@param - mainClass - string - optional - classe CSS pour la balise main
@param - role - string - required - role aria par défaut
@param - params - string - optional - permet d'ajouter des paramètres HTML à la barre de navigation
-->
<#macro cMainNav title=favourite logoImg='' href='.' hasMenu=hasDefaultMenu?boolean hasNestedMenu=true isSidebar=isMainSidebarMenu?boolean isSibebarCollapsible=isMainSidebarMenuCollapse?boolean sidebarMenuClass='' isOnlyHome=isBannerOnlyHome showDefaultMenu=true hasSearchMenu=false typeSearch='field' searchUrl=urlDefaultSearch searchAction='jsp/site/Portal.jsp' searchSolr=false searchParams='' isFixed=isFixedMenu?boolean hasLogin=false loginClass='' mainClass='' id='' class='' role='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#assign pageId><#if page_id??>${page_id!'1'}<#else>0</#if></#assign>
<#if isSidebar>
<#assign isMainSidebarMenu=isSidebar >
<#else>
<header class="sticky-top navigation<#if isFixed> is-fixed</#if><#if hasBanner?boolean><#if isOnlyHome><#if pageId?number = 1> has-banner</#if><#else> has-banner</#if></#if>" id="main-banner-${page_id!'theme'}" role="banner">
</#if>
<div class="container main-header<#if class !=''> ${class!}</#if><#if isSidebar> is-sidebar<#if sidebarMenuClass!=''> ${sidebarMenuClass}</#if><#if isFixed> is-fixed</#if><#if hasBanner?boolean><#if isOnlyHome><#if pageId?number = 1> has-banner</#if><#else> has-banner</#if></#if></#if>"<#if role !=''> role='${role!}'</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params}</#if>>
<nav class="navbar navbar-expand-lg navbar-light " aria-labelledby="main-nav-title">
    <a class="navbar-brand" href="${href!'.'}">
        <#if logoImg !=''><img src="${logoImg}" class="img-fluid main-logo" alt="${title}" aria-hidden="true"></#if> 
        <span id="main-nav-title" class="visually-hidden-focusable">
            ${title} 
        </span>
    </a>
    <button class="navbar-toggler" data-bs-toggle="collapse" data-bs-target="#navbarMainMenu" aria-label="#i18n{theme.showmenu}" aria-controls="navbarMainMenu">
        <span class="navbar-toggler-icon"><span class="visually-hidden-focusable">#i18n{theme.showmenu}</span></span>
    </button>
    <div class="collapse navbar-collapse text-center" aria-label="#i18n{theme.mainMenu} #i18n{theme.showmenu}" role="navigation">
        <ul class="navbar-nav mx-auto align-items-center" aria-label="#i18n{theme.mainMenu}">
            <li class="nav-item<#if pageId = '1'> active</#if>">
                <a class="nav-link text-capitalize" title="${title}" href="." target="_top" aria-current="<#if pageId = '1'>page<#else>false</#if>">#i18n{portal.site.page_home.label}</a>
            </li>
            <#if hasMenu>
                <#assign menu=page_main_menu!>
                <#if showDefaultMenu>${page_main_menu_html!menu}</#if>
            </#if>
            <#if hasNestedMenu>
            <#nested>
            </#if>
            <#if hasMenu && hasSearchMenu && typeSearch='icon'>
                <@cMainNavItem title='' class='ms-auto' url=searchUrl >
                    <@parisIcon name='search' class='main-color' />
                    <@cInline class='visually-hidden'>#i18n{portal.util.labelSearch}</@cInline>
                </@cMainNavItem>
            </#if>
            <#if hasMenu && hasSearchMenu && typeSearch='field'>
                <#assign formSearchAction><#if searchAction=''>${urlSearch!}<#else>${searchAction!}</#if></#assign>
                <@cMainNavItem title='' url='' class='ms-auto' >
                    <@cForm action=formSearchAction class='d-none d-md-none d-lg-block p-sm' params='role="search"'>
                        <input type="hidden" name="page" value="search<#if searchSolr>-solr</#if>">
                        <#if searchParams !=''>${searchParams!}</#if>
                        <@cLabel for='header-query-top' class='visually-hidden' label='#i18n{portal.util.labelSearch}' />
                        <@cInputGroup class='mt-0'>
                            <@cInput name='query' id='header-query-top' placeholder='#i18n{portal.site.page_menu_tools.labelSearch}' autocomplete='on' />
                            <@cInputGroupAddon>
                                <@cBtn label='' id='button-main-search' class='secondary' params='aria-label="#i18n{portal.site.page_menu_tools.labelSearch}"'>
                                <@parisIcon name='search' title='#i18n{portal.site.page_menu_tools.labelSearch}' />
                                </@cBtn >
                            </@cInputGroupAddon>
                        </@cInputGroup>
                    </@cForm>
                </@cMainNavItem>
                <@cMainNavItem title='' class='ms-auto d-none d-md-flex d-lg-none ' urlClass='border-0' url=searchUrl>
                    <@parisIcon name='search' class='main-color' />
                    <@cInline class='visually-hidden'>#i18n{portal.util.labelSearch}</@cInline>
                </@cMainNavItem>
            </#if>
            <#if hasLogin>
                <li class="nav-item navbar-user<#if loginClass !='' > ${loginClass!}</#if> ms-auto" aria-label="#i18n{theme.labelMyAcount}">
                    ${pageinclude_userlogin?default("")}
                </li>
            </#if>
        </ul>
        <#if pageId = '1'>
        <a href="${footerLinkContact!}" class="btn btn-sm btn-primary ms-lg-4">${footerLinkContactLabel!}</a>
        <#else>
        <form class="form-inline search-wrapper d-none d-lg-block" action="jsp/site/Portal.jsp">
          <input name="page" type="hidden" value="search">
          <input id="search-by" name="query" type="search" class="form-control" placeholder="Search here ...">
          <button class="border-0 bg-white" type="submit"><i class="ti ti-search"></i></button>
        </form>
        </#if>
    </div>
</nav>
</div>
<#if isSidebar>
<#assign menu=page_main_menu!>
<div id="layout-sidebar-wrapper">
    <header class="theme-main-header<#if sidebarMenuClass!=''> ${sidebarMenuClass}</#if>" id="main-banner-${pageId!'theme'}" role="banner">
        <div id="sidebar-main-menu">
             <#if isSibebarCollapsible>
            <button id="main-sidebar-collapse" type="button" class="btn btn-outline-primary btn-mini" aria-label="#i18n{portal.util.labelShow} / #i18n{portal.util.labelHide} #i18n{theme.mainMenu}" aria-expanded="true" aria-controls="main-menu-sidebar">
                <@parisIcon name='burger' />
            </button>
            </#if>
            <nav class="navbar-main sidebar-nav " id="main-menu-sidebar" aria-label="#i18n{theme.mainMenu}" role="navigation">
                <ul class="navbar-nav navbar-main flex-column me-auto" aria-label="#i18n{theme.mainMenu}">
                    <#if showDefaultMenu>${page_main_menu_html!menu}</#if>
                    <#if hasSearchMenu && typeSearch='icon'>
                        <@cMainNavItem title='' url=searchUrl >
                            <svg class="paris-icon paris-icon-search main-color" role="img" aria-hidden="true" focusable="false">
                                <use xlink:href="#paris-icon-search"></use>
                            </svg>
                            <@cInline class='visually-hidden'>#i18n{portal.util.labelSearch}</@cInline>
                        </@cMainNavItem>
                    </#if>
                    <#nested>
                </ul>
                <#if hasSearchMenu && typeSearch='field'>
                    <#assign formSearchAction><#if searchAction=''>${urlSearch!}<#else>${searchAction!}</#if></#assign>
                    <@cForm action=formSearchAction class='mx-m' params='role="search"'>
                        <input type="hidden" name="page" value="search<#if searchSolr>-solr</#if>">
                        <#if searchParams !=''>${searchParams!}</#if>
                        <@cLabel for='header-query' class='visually-hidden' label='#i18n{portal.util.labelSearch}' />
                        <@cInputGroup>
                            <@cInput name='query' id='header-query' placeholder='#i18n{portal.site.page_menu_tools.labelSearch}' />
                            <@cInputGroupAddon>
                                <@cBtn label='' id='button-main-search-top' class='secondary' params='aria-label="#i18n{portal.site.page_menu_tools.labelSearch}"'>
                                    <@parisIcon name='search' title='#i18n{portal.site.page_menu_tools.labelSearch}' />
                                </@cBtn >
                            </@cInputGroupAddon>
                        </@cInputGroup>
                    </@cForm>
                </#if>
            </nav>
        </div>
    </header>
    <div id="layout-sidebar">
    <main id="main"<#if mainClass !=''> class="${mainClass!}"</#if> role="main">
        <#if hasBanner?boolean>
            <#assign optMainBanner=.get_optional_template('../../../../site/theme_frameset_main_banner.html')>
            <#if optMainBanner.exists><@optMainBanner.include /></#if>
        </#if>
    <#else>
        <#if hasBanner?boolean>
            <#assign optMainBanner=.get_optional_template('../../../../site/theme_frameset_main_banner.html')>
            <#if optMainBanner.exists><@optMainBanner.include /></#if>
        </#if>
        </header>
        <main id="main"<#if mainClass !=''> class="${mainClass!}"</#if> role="main">
    </#if>
</#macro>