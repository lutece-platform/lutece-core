<#--
Macro: cMainNav

Description: Generates the main site navigation bar with logo, menu items, optional search field, login button, and sidebar mode support.

Parameters:
- title (string, required): Site title displayed in the navbar. Default: favourite.
- logoImg (string, optional): URL of the logo image. Default: ''.
- href (string, optional): URL linked to the logo/title. Default: '.'.
- hasMenu (boolean, optional): If true, displays the site menu. Default: hasDefaultMenu?boolean.
- hasUserThemeSwitch (boolean, optional): If true, displays a light/dark theme toggle. Default: hasUserThemeSwitch?boolean.
- hasNestedMenu (boolean, optional): If true, renders nested content as additional menu items. Default: true.
- isSidebar (boolean, optional): If true, renders a vertical sidebar navigation. Default: isMainSidebarMenu?boolean.
- isSibebarCollapsible (boolean, optional): If true, the sidebar menu can be collapsed. Default: isMainSidebarMenuCollapse?boolean.
- sidebarMenuClass (string, optional): Additional CSS class for the sidebar menu. Default: ''.
- isOnlyHome (boolean, optional): If true, banner is only shown on the home page. Default: isBannerOnlyHome.
- showDefaultMenu (boolean, optional): If true, renders the default page menu. Default: true.
- hasSearchMenu (boolean, optional): If true, adds a search feature to the menu. Default: false.
- typeSearch (string, optional): Search display type: 'field' for an input form, 'icon' for a search link. Default: 'field'.
- searchUrl (string, optional): URL for the search icon link. Default: urlDefaultSearch.
- searchAction (string, optional): Form action URL when typeSearch is 'field'. Default: 'jsp/site/Portal.jsp'.
- searchSolr (boolean, optional): If true, searches via Solr instead of the default engine. Default: false.
- searchParams (string, optional): Additional hidden fields for the search form. Default: ''.
- isFixed (boolean, optional): If true, the navbar is fixed at the top of the page. Default: isFixedMenu?boolean.
- hasLogin (boolean, optional): If true, displays a login button. Default: false.
- loginClass (string, optional): CSS class for the login button. Default: ''.
- mainClass (string, optional): CSS class for the main element. Default: ''.
- id (string, optional): Unique identifier for the navbar. Default: ''.
- class (string, optional): Additional CSS class(es) for the navbar. Default: ''.
- role (string, optional): ARIA role attribute for the navbar. Default: ''.
- params (string, optional): Additional HTML attributes for the navbar. Default: ''.

Showcase:
- desc: "Navigation principale - @cMainNav"
- guide: navigation
- newFeature: false

Snippet:

    Basic navigation bar:

    <@cMainNav title='My Portal' />

    Navigation with search and login:

    <@cMainNav title='City Services' hasSearchMenu=true typeSearch='field' hasLogin=true>
        <@cMainNavItem title='About' url='jsp/site/Portal.jsp?page=about' />
        <@cMainNavItem title='Contact' url='jsp/site/Portal.jsp?page=contact' />
    </@cMainNav>

    Fixed sidebar navigation:

    <@cMainNav title='Admin Portal' isSidebar=true isSibebarCollapsible=true isFixed=true hasSearchMenu=true />

-->
<#macro cMainNav title=favourite logoImg='' href='.' hasMenu=hasDefaultMenu?boolean hasUserThemeSwitch=hasUserThemeSwitch?boolean hasNestedMenu=true isSidebar=isMainSidebarMenu?boolean isSibebarCollapsible=isMainSidebarMenuCollapse?boolean sidebarMenuClass='' isOnlyHome=isBannerOnlyHome showDefaultMenu=true hasSearchMenu=hasSearchMenu?boolean typeSearch='field' searchUrl=urlDefaultSearch searchAction='jsp/site/Portal.jsp' searchSolr=false searchParams='' isFixed=isFixedMenu?boolean hasLogin=false loginClass='' mainClass='' id='' class='' role='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#assign pageId><#if page_id??>${page_id!'1'}<#else>0</#if></#assign>
<#if isSidebar>
<#assign isMainSidebarMenu=isSidebar >
<#else>
<header class="sticky-top navigation<#if isFixed> is-fixed</#if><#if hasBanner?boolean><#if isOnlyHome><#if pageId?number = 1> has-banner</#if><#else> has-banner</#if></#if>" id="main-banner-${page_id!'theme'}" role="banner">
</#if>
<#local logoAltDS=dskey('portal.theme.site_property.menu.logo.alt')! />
<#if logoAltDS?has_content && !logoAltDS?starts_with('DS')><#local logoAlt=logoAltDS /></#if>
<div class="container main-header<#if class !=''> ${class!}</#if><#if isSidebar> is-sidebar<#if sidebarMenuClass!=''> ${sidebarMenuClass}</#if><#if isFixed> is-fixed</#if><#if hasBanner?boolean><#if isOnlyHome><#if pageId?number = 1> has-banner</#if><#else> has-banner</#if></#if></#if>"<#if role !=''> role='${role!}'</#if><#if id !=''> id="${id!}"</#if><#if params!=''> ${params}</#if>>
<nav class="navbar navbar-expand-lg navbar-light topnav lutece-ds-topbar" aria-labelledby="main-nav-title">
    <a class="navbar-brand topnav__brand" href="${href!'.'}">
       <#if logoHeader !=''><img src="${logoHeader!}" class="logo" alt="${logoAlt!}" aria-hidden="true"></#if>
        <span class="main-service-title visually-hidden">${title}</span>
    </a>
    <button class="navbar-toggler" data-bs-toggle="collapse" data-bs-target="#navbarMainMenu" aria-label="#i18n{portal.theme.showmenu}" aria-controls="navbarMainMenu">
        <span class="navbar-toggler-icon"><span class="visually-hidden-focusable">#i18n{portal.theme.showmenu}</span></span>
    </button>
    <div class="collapse navbar-collapse text-center" aria-label="#i18n{portal.theme.mainMenu} #i18n{portal.theme.showmenu}" role="navigation" id="navbarMainMenu">
        <ul class="navbar-nav w-100 align-items-center lutece-ds-tw-menu" aria-label="#i18n{portal.theme.mainMenu}">
            <li class="nav-item<#if pageId = '1'> active</#if>">
                <a class="nav-link text-capitalize" title="${title}" href="." target="_top" aria-current="<#if pageId = '1'>page<#else>false</#if>">#i18n{portal.site.page_home.label}</a>
            </li>
            <#if hasMenu>
                <#assign menu=page_main_menu!>
                <#if showDefaultMenu>${page_main_menu_html!menu}</#if>
                <#if customMenuMainPage??>${customMenuMainPage}</#if>
                <#if customMenuInternalPage??>${customMenuInternalPage!customMenuMainPage}</#if>
            </#if>
            <#if hasNestedMenu>
            <#nested>
            </#if>
            <#if hasMenu && hasSearchMenu && typeSearch='icon'>
                <@cMainNavItem title='' class='ms-auto' url=searchUrl >
                    <@cIcon name='search' class='main-color' />
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
                                    <@cIcon name='search' title='#i18n{portal.site.page_menu_tools.labelSearch}' />
                                </@cBtn >
                            </@cInputGroupAddon>
                        </@cInputGroup>
                    </@cForm>
                </@cMainNavItem>
                <@cMainNavItem title='' class='ms-auto d-none d-md-flex d-lg-none ' urlClass='border-0' url=searchUrl>
                    <@cIcon name='search' class='main-color' />
                    <@cInline class='visually-hidden'>#i18n{portal.util.labelSearch}</@cInline>
                </@cMainNavItem>
            </#if>
            <#if hasLogin>
                <li class="nav-item navbar-user<#if loginClass !='' > ${loginClass!}</#if> ms-auto" aria-label="#i18n{portal.theme.labelAccount}">
                    ${pageinclude_userlogin?default("")}
                </li>
            </#if>
            <@translationMenu />
        </ul>
    </div>
</nav>
</div>
<#if isSidebar>
<#assign menu=page_main_menu!>
<nav id="sidebar-brand">
    <a class="navbar-brand navbar-brand-sidebar" href="${href!'.'}">
        <#if logoHeader !=''><img src="${logoHeader!}" class="img-fluid" width="120" alt="${logoAlt!}" aria-hidden="true"></#if>
        <span class="main-service-title visually-hidden">${title}</span>
    </a>
    <#if isSibebarCollapsible>
    <button id="main-sidebar-collapse" type="button" class="btn btn-link p-0 border-0 text-decoration-none" aria-label="#i18n{portal.util.labelShow} / #i18n{portal.util.labelHide} #i18n{portal.theme.mainMenu}" aria-expanded="true" aria-controls="main-menu-sidebar">
        <@cIcon name='layout-sidebar-left-collapse' class='fs-ml' />
    </button>
    </#if>
</nav>
<div id="layout-sidebar-wrapper">
    <header class="theme-main-header<#if sidebarMenuClass!=''> ${sidebarMenuClass}</#if>" id="main-banner-${pageId!'theme'}" role="banner">
        <div id="sidebar-main-menu">
            <nav class="navbar-main sidebar-nav lutece-ds-topbar" id="main-menu-sidebar" aria-label="#i18n{portal.theme.mainMenu}" role="navigation">
                <#--
                <a class="navbar-brand sidebar-brand" href="${href!'.'}">
                <#if logoHeader !=''><img src="${logoHeader!}" class="logo" alt="${logoAlt!}" aria-hidden="true"></#if>
                    <span class="main-service-title visually-hidden">${title}</span>
                </a>
                -->
                <ul class="navbar-nav navbar-main flex-column me-auto  lutece-ds-tw-menu" aria-label="#i18n{portal.theme.mainMenu}">
                    <#if hasMenu>
                        <#if showDefaultMenu>${page_main_menu_html!menu}</#if>
                        <#if customMenuSideBar??>${customMenuSideBar!}</#if>
                    </#if>
                    <#if hasNestedMenu>
                    <#nested>
                    </#if>
                    <#if hasMenu && hasSearchMenu>
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
                                            <@cIcon name='search' title='#i18n{portal.site.page_menu_tools.labelSearch}' />
                                        </@cBtn >
                                    </@cInputGroupAddon>
                                </@cInputGroup>
                            </@cForm>
                        </@cMainNavItem>
                    </#if>
                    <#if hasLogin>
                        <li class="nav-item navbar-user<#if loginClass !='' > ${loginClass!}</#if>" aria-label="#i18n{portal.theme.labelAccount}">
                            ${pageinclude_userlogin?default("")}
                        </li>
                    </#if>
                    <@translationMenu />
                </ul>
            </nav>
        </div>
    </header>
    <main id="main"<#if mainClass !=''> class="${mainClass!}"</#if> role="main">
        <#if hasBanner?boolean>
            <#assign optMainBanner=.get_optional_template('../../../../site/theme_frameset_main_banner.html')>
            <#if optMainBanner.exists><@optMainBanner.include /></#if>
        </#if>
    <#else>
        </header>
        <main id="main"<#if mainClass !=''> class="${mainClass!}"</#if> role="main">
        <#if hasBanner?boolean>
            <#assign optMainBanner=.get_optional_template('../../../../site/theme_frameset_main_banner.html')>
            <#if optMainBanner.exists><@optMainBanner.include /></#if>
        </#if>
    </#if>
</#macro>