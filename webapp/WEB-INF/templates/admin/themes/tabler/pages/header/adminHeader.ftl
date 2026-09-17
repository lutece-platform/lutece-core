<#--
Macro: adminHeader
Description: Generates a header section for an administrative page, including a navigation menu and user account menu.
The markup follows the Tabler 1.5 page layouts (https://preview.tabler.io/layout-horizontal.html, layout-condensed.html,
layout-vertical.html, layout-folded.html and layout-folded-hover.html). The layout is driven by the site properties :
- portal.site.site_property.layout.menu.vertical.checkbox : vertical sidebar instead of the top navbar
- portal.site.site_property.layout.menu.sidebar.select : sidebar mode, 'default' (expanded, with a pin button letting the
  user fold it), 'folded' (icon-only rail with flyout submenus) or 'folded-hover' (rail that unfolds on hover / focus)
- portal.site.site_property.layout.menu.transparent.checkbox : transparent sidebar (dark otherwise)
- portal.site.site_property.layout.menu.condensed.checkbox : single row top navbar
- portal.site.site_property.layout.menu.sticky.checkbox : sticky top navbar
- portal.site.site_property.layout.menu.boxed.checkbox / portal.site.site_property.layout.fluid.checkbox : page width
- portal.site.site_property.layout.menu.home.checkbox : link to the front office

Parameters:
- site_name (string, required): the name of the website or application.

Snippet:

    Render the admin header with default site name:

    <@adminHeader />

    Render the admin header with a custom site name:

    <@adminHeader site_name='My Admin Portal' />

-->
<#macro adminHeader site_name=site_name!'Lutece' admin_url=admin_url deprecated...>
<@deprecatedWarning args=deprecated />
<#local userReadMode><#attempt>${dskey('portal.site.site_property.layout.user.readmode.show.checkbox')?number}<#recover>0</#attempt></#local>
<#local userDarkMode><#attempt>${dskey('portal.site.site_property.layout.user.darkmode.show.checkbox')?number}<#recover>0</#attempt></#local>
<#local adminDarkMode><#attempt>${dskey('portal.site.site_property.layout.darkmode.checkbox')?number}<#recover>0</#attempt></#local>
<#local readMode><#attempt><#if dskey('portal.site.site_property.layout.readmode.checkbox')?number = 1> dir="rtl"</#if><#recover></#attempt></#local>
<#local layoutBoxed><#attempt><#if dskey('portal.site.site_property.layout.menu.boxed.checkbox')?number==1> layout-boxed</#if><#recover></#attempt></#local>
<#local layoutFluid><#attempt><#if dskey('portal.site.site_property.layout.fluid.checkbox')?number==1> layout-fluid</#if><#recover></#attempt></#local>
<#local bodyClass><#if layoutBoxed!=''>${layoutBoxed!}</#if><#if layoutFluid!=''> ${layoutFluid!}</#if></#local>
<#local navbarSticky><#attempt><#if dskey('portal.site.site_property.layout.menu.sticky.checkbox')?number==1> sticky-top</#if><#recover></#attempt></#local>
<#local menuCondensed><#attempt><#if dskey('portal.site.site_property.layout.menu.condensed.checkbox')?number==1>condensed</#if><#recover></#attempt></#local>
<#local menuVertical><#attempt><#if dskey('portal.site.site_property.layout.menu.vertical.checkbox')?number==1>vertical</#if><#recover></#attempt></#local>
<#local menuTransparent><#attempt><#if dskey('portal.site.site_property.layout.menu.transparent.checkbox')?number==1> navbar-transparent</#if><#recover></#attempt></#local>
<#local menuHome><#attempt>${dskey('portal.site.site_property.layout.menu.home.checkbox')?number}<#recover>0</#attempt></#local>
<#local showSiteName><#attempt>${dskey('portal.site.site_property.show_site_name.checkbox')?number}<#recover>1</#attempt></#local>
<#local logoUrl = (dskey('portal.site.site_property.logo_url')!)?has_content?then(dskey('portal.site.site_property.logo_url')?trim, '')>
<#local logoSvg = (dskey('portal.site.site_property.logo_svg.textblock')!)?has_content?then(dskey('portal.site.site_property.logo_svg.textblock'), '')>
<#local logoWidth><#attempt>${dskey('portal.site.site_property.logo.width')}<#recover>24</#attempt></#local>
<#local logoHeight><#attempt>${dskey('portal.site.site_property.logo.height')}<#recover>24</#attempt></#local>
<#-- Sidebar mode (vertical menu only) : default (expanded + pin button) | folded | folded-hover -->
<#local sidebarMode><#attempt>${dskey('portal.site.site_property.layout.menu.sidebar.select')?trim}<#recover>default</#attempt></#local>
<#if sidebarMode != 'folded' && sidebarMode != 'folded-hover'><#local sidebarMode = 'default'></#if>
<#local sidebarClass><#if sidebarMode == 'folded'> navbar-folded<#elseif sidebarMode == 'folded-hover'> navbar-folded-hover</#if></#local>
<#local isVertical = (menuVertical == 'vertical')>
<#local isCondensed = (menuCondensed?trim == 'condensed')>
<script>
// Expose the current admin access code so per-user client storage keys can be namespaced.
// This keeps one user's dashboard widget layout from overwriting another's on a shared computer.
window.LuteceAdminUser = { accessCode: "${(user.accessCode!'')?js_string}" };
let localTheme = localStorage.getItem('lutece-tabler-theme');
<#if adminDarkMode?number==1>
<#if userDarkMode?number!=1>
localTheme = 'dark';
<#else>
if( localTheme === null ){
	localTheme = 'dark';
}
</#if>
</#if>
if( localTheme === null ){
	localTheme = 'light';
}
localStorage.setItem( 'lutece-tabler-theme', localTheme );
// Apply synchronously, before first paint, so the stored mode is loaded on every page (incl. right after login)
document.documentElement.dataset.bsTheme = localTheme;
// Read direction : apply the user's stored value on <html> before first paint (persists over login/logout)
if( localStorage.getItem('lutece-bo-readmode') === 'rtl' ){
	document.documentElement.setAttribute('dir','rtl');
}
<#if isVertical && sidebarMode == 'default'>
// Sidebar fold state : the pin button (Tabler [data-bs-toggle="sidebar-folded"]) stores the user's choice under
// 'tabler-sidebar'. Re-apply it on <html> before first paint so the rail does not flash expanded on each page load.
const storedSidebar = localStorage.getItem('tabler-sidebar');
if( storedSidebar === 'folded' || storedSidebar === 'folded-hover' ){
	document.documentElement.setAttribute('data-bs-sidebar', storedSidebar);
}
</#if>
</script>
<#-- Optional Jquery Inclusion for compat need the library-theme-jquery library https://github.com/lutece-platform/lutece-tech-library-theme-jquery -->
<#if jqueryHeader??>
<@jqueryHeader />
</#if>
<#-- End of Optional Jquery Inclusion -->
</head>
<body<#if bodyClass!=''> class="${bodyClass!}"</#if> ${readMode} data-bs-theme-base="neutral" data-bs-theme-radius="2">
<@adminSkipNav />
<div class="page" data-userdarkmode="${userDarkMode}">
<#if isVertical>
<!--  BEGIN SIDEBAR  -->
<aside class="navbar navbar-vertical navbar-expand-lg${sidebarClass}${menuTransparent}"<#if menuTransparent == ''> data-bs-theme="dark"</#if>>
	<div class="container-fluid">
		<!-- BEGIN NAVBAR TOGGLER -->
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#sidebar-menu" aria-controls="sidebar-menu" aria-expanded="false" aria-label="#i18n{portal.users.admin_header.labelMenuToggleV}">
			<span class="navbar-toggler-icon"></span>
		</button>
		<!-- END NAVBAR TOGGLER -->
		<!-- BEGIN NAVBAR LOGO -->
		<div class="navbar-brand navbar-brand-autodark">
			<@adminHeaderLogo site_name=site_name logoSvg=logoSvg logoUrl=logoUrl logoWidth=logoWidth logoHeight=logoHeight showSiteName=showSiteName vertical=true />
			<#if sidebarMode == 'default'>
			<#-- Runtime fold toggle : visible on hover / focus of the expanded sidebar, hidden while folded (Tabler CSS) -->
			<button type="button" class="btn btn-action btn-sm d-none d-lg-inline-flex" data-bs-toggle="sidebar-folded" aria-pressed="false" aria-label="#i18n{portal.users.admin_header.labelPinSidebar}" title="#i18n{portal.users.admin_header.labelPinSidebar}">
				<i class="ti ti-pin" aria-hidden="true"></i>
			</button>
			</#if>
		</div>
		<!-- END NAVBAR LOGO -->
		<!-- BEGIN SIDEBAR USER : before the collapse so it stays in the top bar row on mobile, pinned to the bottom on desktop -->
		<div class="navbar-footer">
			<ul class="navbar-nav">
				<li class="nav-item dropup">
					<a href="#" class="nav-link" data-bs-toggle="dropdown" aria-expanded="false" aria-label="#i18n{portal.users.admin_header.labelUserMenu}">
						<@adminHeaderAvatar />
						<span class="nav-link-title">
							${dashboard_zone_4!}
							<span class="d-block small text-secondary">${user.dateLastLogin!}</span>
						</span>
					</a>
					<div class="dropdown-menu">
						<@adminHeaderUserMenuItems vertical=true />
					</div>
				</li>
			</ul>
		</div>
		<!-- END SIDEBAR USER -->
		<nav class="collapse navbar-collapse" id="sidebar-menu" aria-label="#i18n{portal.users.admin_header.labelMenuV}">
			<!-- BEGIN NAVBAR MENU -->
			<@adminHeaderMenu admin_url=admin_url vertical=true />
			<!-- END NAVBAR MENU -->
			<!-- BEGIN NAVBAR SIDE : trailing tools, pinned to the bottom of the menu -->
			<div class="navbar-side">
				<ul class="navbar-nav">
					<@adminHeaderTools userDarkMode=userDarkMode userReadMode=userReadMode showHome=(menuHome?number == 1) vertical=true />
				</ul>
			</div>
			<!-- END NAVBAR SIDE -->
		</nav>
	</div>
</aside>
<!--  END SIDEBAR  -->
<#else>
<!-- BEGIN NAVBAR  -->
<#if navbarSticky != ''><div class="sticky-top"></#if>
<header class="navbar navbar-expand-md d-print-none">
	<div class="container-xl">
		<!-- BEGIN NAVBAR TOGGLER -->
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar-menu" aria-controls="navbar-menu" aria-expanded="false" aria-label="#i18n{portal.users.admin_header.labelMenuToggleH}">
			<span class="navbar-toggler-icon"></span>
		</button>
		<!-- END NAVBAR TOGGLER -->
		<!-- BEGIN NAVBAR LOGO -->
		<div class="navbar-brand navbar-brand-autodark pe-0 pe-md-3">
			<@adminHeaderLogo site_name=site_name logoSvg=logoSvg logoUrl=logoUrl logoWidth=logoWidth logoHeight=logoHeight showSiteName=showSiteName />
		</div>
		<!-- END NAVBAR LOGO -->
		<!-- BEGIN NAVBAR SIDE -->
		<div id="main-nav" class="navbar-nav flex-row order-md-last">
			<div class="d-none d-md-flex">
				<@adminHeaderTools userDarkMode=userDarkMode userReadMode=userReadMode showHome=(menuHome?number == 1 && isCondensed) />
			</div>
			<div class="nav-item dropdown" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-original-title="#i18n{portal.users.admin_header.labelUserMenu}">
				<a href="#" class="nav-link d-flex lh-1 p-0 px-2" data-bs-toggle="dropdown" aria-expanded="false" aria-label="#i18n{portal.users.admin_header.labelUserMenu}">
					<@adminHeaderAvatar />
				</a>
				<div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
					<@adminHeaderUserMenuItems />
				</div>
			</div>
		</div>
		<!-- END NAVBAR SIDE -->
		<#if isCondensed>
		<div class="collapse navbar-collapse" id="navbar-menu">
			<div class="d-flex flex-column flex-md-row flex-fill align-items-stretch align-items-md-center">
				<!-- BEGIN NAVBAR MENU -->
				<@adminHeaderMenu admin_url=admin_url />
				<!-- END NAVBAR MENU -->
			</div>
		</div>
		</#if>
	</div>
</header>
<#if !isCondensed>
<div class="navbar-expand-md">
	<div class="collapse navbar-collapse" id="navbar-menu">
		<div class="navbar">
			<div class="container-xl">
				<div class="row flex-column flex-md-row flex-fill align-items-center">
					<div class="col">
						<!-- BEGIN NAVBAR MENU -->
						<@adminHeaderMenu admin_url=admin_url />
						<!-- END NAVBAR MENU -->
					</div>
					<#if menuHome?number == 1>
					<div class="col col-md-auto">
						<ul class="navbar-nav">
							<li class="nav-item">
								<a class="nav-link" href="." target="_blank" title="#i18n{portal.users.admin_header.title.viewSite}">
									<span class="nav-link-icon"><@adminHeaderIconExternalLink /></span>
									<span class="nav-link-title">#i18n{portal.users.admin_header.homePage}</span>
								</a>
							</li>
						</ul>
					</div>
					</#if>
				</div>
			</div>
		</div>
	</div>
</div>
</#if>
<#if navbarSticky != ''></div></#if>
<!-- END NAVBAR  -->
</#if>
<#if user.userLevel == 0>
<script>
document.addEventListener( 'DOMContentLoaded', () => {	
	const loggers = document.querySelector('.logger') ;
	if( loggers != null && loggers.childElementCount > 0 && document.getElementById('adminModal') != null ){
		if( !sessionStorage.getItem('lutece-debug-modal') ){
			sessionStorage.setItem('lutece-debug-modal', false )
		}
		var modalContent = '<h3>#i18n{portal.util.log.warningLevel}</h3><p><strong>#i18n{portal.util.log.modalWarningMessage}</strong></p><p class="text-center"><button class="btn btn-sm btn-danger" data-toggle="collapse" data-target="#info-log" aria-expanded="false" aria-controls="info-log" type="button">#i18n{portal.util.log.modalLabelButton}</button></p><blockquote class="collapse" id="info-log">' + loggers.querySelector('.col.text-truncate').innerHTML + '</blockquote>';
		var adminModal = document.getElementById('adminModal');
		var adminModalLabel = document.getElementById('adminModalLabel');
		var adminModalBody = document.querySelector('#adminModal .modal-body');
		if( adminModalBody != null){
			adminModalBody.insertAdjacentHTML( 'beforeEnd', modalContent);
		}
		var adminModalHeader = document.querySelector('#adminModal .modal-header');
		var adminModalHeaderBtn = document.querySelector('#adminModal .modal-header button');
		var myAdminModal = new bootstrap.Modal( adminModal, {} );
		adminModalLabel.insertAdjacentHTML( 'beforeEnd', '#i18n{portal.util.log.modalWarningTitle}' );
		adminModalHeader.classList.add('text-white');
		adminModalHeaderBtn.setAttribute( 'style', 'background: transparent url("data:image/svg+xml,%3csvg xmlns=\'http://www.w3.org/2000/svg\' viewBox=\'0 0 16 16\' fill=\'%23ffffff\'%3e%3cpath d=\'M.293.293a1 1 0 011.414 0L8 6.586 14.293.293a1 1 0 111.414 1.414L9.414 8l6.293 6.293a1 1 0 01-1.414 1.414L8 9.414l-6.293 6.293a1 1 0 01-1.414-1.414L6.586 8 .293 1.707a1 1 0 010-1.414z\'/%3e%3c/svg%3e") center/.75rem auto no-repeat;');
		adminModalHeader.classList.add('bg-danger');
		adminModalBody.classList.add('text-danger');
		adminModal.addEventListener('hide.bs.modal', function () {
			sessionStorage.setItem('lutece-debug-modal',true);
		})	
		if( sessionStorage.getItem('lutece-debug-modal') === 'false' ){
			myAdminModal.show();
		}
	}
	
}); 
</script>
</#if>
<div class="page-wrapper">
</#macro>
<#--
Macro: adminHeaderLogo
Description: Brand link of the admin header (logo and optional site name). Internal helper of adminHeader.
In the vertical sidebar the site name gets the nav-link-title class so it collapses with the link titles when the sidebar is folded.
-->
<#macro adminHeaderLogo site_name logoSvg logoUrl logoWidth logoHeight showSiteName vertical=false>
<a href="jsp/admin/AdminMenu.jsp" aria-label="${site_name}">
	<#if logoSvg?trim != ''>
	${logoSvg!}
	<#else>
	<img src="${logoUrl}" class="me-1" height="${logoHeight}" width="${logoWidth}" alt="Logo ${site_name}" aria-hidden="true">
	</#if>
	<#if showSiteName?number == 1><span class="fs-4 me-2<#if vertical> nav-link-title</#if>">${site_name}</span></#if>
</a>
</#macro>
<#--
Macro: adminHeaderAvatar
Description: Avatar of the connected admin user. Internal helper of adminHeader, the initials are filled by admin.js from data-username.
-->
<#macro adminHeaderAvatar>
<#local hasAdminAvatar=false />
<#if hasAdminAvatar>
<span class="avatar avatar-sm" style="background-image:url(<#if adminAvatar>servlet/plugins/adminavatar/avatar?id_user=${user.userId}<#else>#dskey{portal.site.site_property.avatar_default}</#if>); "></span>
<#else>
<span class="avatar avatar-sm user-initials" data-username="${dashboard_zone_4!}"></span>
</#if>
</#macro>
<#--
Macro: adminHeaderUserMenuItems
Description: Items of the connected user dropdown menu. Internal helper of adminHeader.
Parameters:
- vertical (boolean, optional): true in the sidebar, where the user name and last login are already shown on the nav link.
-->
<#macro adminHeaderUserMenuItems vertical=false>
<#if !vertical>
<div class="dropdown-item text-muted">${dashboard_zone_4!}</div>
<div class="dropdown-item text-muted">${user.dateLastLogin!}</div>
</#if>
<#if userMenuItems?has_content><#list userMenuItems as item>${item.content}</#list></#if>
<#if !vertical || userMenuItems?has_content><div class="dropdown-divider"></div></#if>
<#if user.userLevel == 0>
<a href="jsp/admin/AdminTechnicalMenu.jsp" class="dropdown-item">#i18n{portal.admindashboard.view_dashboards.title}</a>
</#if>
<#if admin_logout_url?has_content>
<a href="${admin_logout_url}" id="lutece-admin-logout" class="dropdown-item">#i18n{portal.users.admin_header.deconnectionLink}</a>
</#if>
</#macro>
<#--
Macro: adminHeaderMenu
Description: Main features menu (feature groups and their features). Internal helper of adminHeader.
Parameters:
- admin_url (string, required): the admin home url.
- vertical (boolean, optional): true in the sidebar. Submenus then stay open on outside clicks (data-bs-auto-close="false"),
  Tabler closes the flyouts itself when the sidebar is folded.
-->
<#macro adminHeaderMenu admin_url vertical=false>
<#local autoClose = vertical?then('false', 'outside')>
<ul id="main-menu" class="navbar-nav<#if vertical> pt-lg-3</#if>">
<#list feature_group_list as feature_group>
	<#local icon_class = (feature_group.icon?length < 1)?then('ti ti-mood-empty', feature_group.icon)>
	<#if feature_group.features?size gt 1>
	<li class="nav-item dropdown">
		<a class="nav-link dropdown-toggle" id="dLabel${feature_group.id}Header" href="${admin_url}#${feature_group.id}" data-bs-toggle="dropdown" data-bs-auto-close="${autoClose}" role="button" aria-haspopup="true" aria-expanded="false">
			<span class="nav-link-icon"><i class="${icon_class}"></i></span>
			<span class="nav-link-title">${feature_group.label}</span>
		</a>
		<div class="dropdown-menu" aria-labelledby="dLabel${feature_group.id}Header">
			<div class="dropdown-menu-columns">
				<div class="dropdown-menu-column">
				<#list feature_group.features as feature>
					<#if !feature.externalFeature>
					<a class="dropdown-item" id="feature-${feature.id?lower_case}" href="${feature.url}?plugin_name=${feature.pluginName}" title="${feature.name}">
						<#if feature.iconUrl?has_content><i class="${feature.iconUrl} icon-inline me-1"></i></#if>${feature.name}
					</a>
					<#else>
					<a class="dropdown-item" id="feature-${feature.id?lower_case}" href="${feature.url}" title="${feature.name}">
						<#if feature.iconUrl?has_content><i class="${feature.iconUrl} icon-inline me-1"></i></#if>${feature.name}
					</a>
					</#if>
				</#list>
				</div>
			</div>
		</div>
	</li>
	<#else>
	<#list feature_group.features as feature>
	<li class="nav-item">
		<a class="nav-link" id="feature-${feature.id?lower_case}" href="<#if !feature.externalFeature>${feature.url}?plugin_name=${feature.pluginName}<#else>${feature.url}</#if>">
			<#if feature.iconUrl?has_content><span class="nav-link-icon"><i class="${feature.iconUrl}"></i></span></#if>
			<span class="nav-link-title">${feature.name}</span>
		</a>
	</li>
	</#list>
	</#if>
</#list>
</ul>
</#macro>
<#--
Macro: adminHeaderTools
Description: Header tools (front office link, dark mode toggle, read direction toggle, and for level 0 users the debug loggers
notification, the site properties and the technical menu). Internal helper of adminHeader.
Parameters:
- userDarkMode (number, required): 1 when users can switch the dark mode.
- userReadMode (number, required): 1 when users can switch the read direction.
- showHome (boolean, required): whether to render the front office link.
- vertical (boolean, optional): true in the sidebar, items are then li with icon + title so they fold with the sidebar.
-->
<#macro adminHeaderTools userDarkMode userReadMode showHome vertical=false>
<#local tag = vertical?then('li', 'div')>
<#if showHome>
<${tag} class="nav-item">
	<a class="nav-link" href="." target="_blank" title="#i18n{portal.users.admin_header.title.viewSite}">
		<#if vertical><span class="nav-link-icon"></#if><@adminHeaderIconExternalLink /><#if vertical></span></#if>
		<span class="nav-link-title<#if !vertical> ms-1</#if>">#i18n{portal.users.admin_header.homePage}</span>
	</a>
</${tag}>
</#if>
<#if userDarkMode?number == 1>
<${tag} class="nav-item">
	<a href="?theme=dark" class="nav-link hide-theme-dark<#if !vertical> px-0</#if>" aria-label="#i18n{portal.users.admin_header.labelEnableDarkMode}"<#if !vertical> data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-original-title="#i18n{portal.users.admin_header.labelEnableDarkMode}"</#if>>
		<#if vertical><span class="nav-link-icon"></#if>
		<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-1">
			<path d="M12 3c.132 0 .263 0 .393 0a7.5 7.5 0 0 0 7.92 12.446a9 9 0 1 1 -8.313 -12.454z"></path>
		</svg>
		<#if vertical></span><span class="nav-link-title">#i18n{portal.users.admin_header.labelEnableDarkMode}</span></#if>
	</a>
	<a href="?theme=light" class="nav-link hide-theme-light<#if !vertical> px-0</#if>" aria-label="#i18n{portal.users.admin_header.labelEnableLightMode}"<#if !vertical> data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-original-title="#i18n{portal.users.admin_header.labelEnableLightMode}"</#if>>
		<#if vertical><span class="nav-link-icon"></#if>
		<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-1">
			<path d="M12 12m-4 0a4 4 0 1 0 8 0a4 4 0 1 0 -8 0"></path>
			<path d="M3 12h1m8 -9v1m8 8h1m-9 8v1m-6.4 -15.4l.7 .7m12.1 -.7l-.7 .7m0 11.4l.7 .7m-12.1 -.7l-.7 .7"></path>
		</svg>
		<#if vertical></span><span class="nav-link-title">#i18n{portal.users.admin_header.labelEnableLightMode}</span></#if>
	</a>
</${tag}>
</#if>
<#if userReadMode?number == 1>
	<#if vertical>
	<li class="nav-item" id="lutece-rtl">
		<button type="button" class="nav-link" title="#i18n{portal.site.site_property.layout.readmode.checkbox}">
			<span class="nav-link-icon"><i class="ti ti-text-direction-rtl"></i></span>
			<span class="nav-link-title">#i18n{portal.site.site_property.layout.readmode.checkbox}</span>
		</button>
	</li>
	<#else>
	<@adminReadMode />
	</#if>
</#if>
<#if user.userLevel == 0>
	<#if listLoggersInfo??>
	<#local listLogDebug = listLoggersInfo?filter( logInfo -> ( logInfo.level = 'DEBUG' || logInfo.level = 'TRACE' ) ) />
	<#if listLogDebug?has_content>
	<#if vertical>
	<li class="nav-item dropdown">
		<a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown" data-bs-auto-close="outside" role="button" aria-haspopup="true" aria-expanded="false" aria-label="#i18n{portal.admin.notification.labelShow}">
			<span class="nav-link-icon"><@adminHeaderIconBell /></span>
			<span class="nav-link-title">#i18n{portal.admin.notification.title}</span>
			<span class="badge bg-red"></span>
		</a>
		<div class="dropdown-menu">
			<span class="dropdown-header">#i18n{portal.util.log.warningLevel}</span>
			<#list listLogDebug as logInfo>
			<span class="dropdown-item text-truncate" title="${logInfo.path!}"><strong>${logInfo.name!} - ${logInfo.level!}</strong></span>
			</#list>
		</div>
	</li>
	<#else>
	<div class="nav-item dropdown d-none d-md-flex">
		<a href="#" class="nav-link px-0" data-bs-toggle="dropdown" tabindex="-1" aria-label="#i18n{portal.admin.notification.labelShow}" data-bs-auto-close="outside" aria-expanded="false">
			<@adminHeaderIconBell />
			<span class="badge bg-red"></span>
		</a>
		<div class="dropdown-menu dropdown-menu-arrow dropdown-menu-end dropdown-menu-card">
			<div class="card">
				<div class="card-header d-flex">
					<h3 class="card-title">#i18n{portal.admin.notification.title}</h3>
				</div>
				<div class="list-group list-group-flush list-group-hoverable">
					<div class="list-group-item">
						<div class="row align-items-center">
							<div class="col-auto align-self-baseline"><span class="status-dot status-dot-animated bg-red d-block"></span></div>
							<div class="col text-truncate" style="max-height:80vh;overflow-y:auto;">
								<a href="#" class="text-body d-block">#i18n{portal.util.log.warningLevel}</a>
							<#list listLogDebug as logInfo>
								<div class="d-block text-secondary text-truncate mt-1" title="${logInfo.path!}"><strong>${logInfo.name!} - ${logInfo.level!}</strong> ${logInfo.path!}</div>
							</#list>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</#if>
	</#if>
	</#if>
<${tag} class="nav-item">
	<a class="nav-link" href="jsp/admin/ManageProperties.jsp" title="#i18n{portal.site.adminFeature.properties_management.name}" aria-label="#i18n{portal.site.adminFeature.properties_management.name}"<#if !vertical> data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-original-title="#i18n{portal.site.adminFeature.properties_management.name}"</#if>>
		<#if vertical><span class="nav-link-icon"></#if>
		<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-tabler icons-tabler-outline icon-tabler-home-cog"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M9 21v-6a2 2 0 0 1 2 -2h1.6" /><path d="M20 11l-8 -8l-9 9h2v7a2 2 0 0 0 2 2h4.159" /><path d="M18 18m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" /><path d="M18 14.5v1.5" /><path d="M18 20v1.5" /><path d="M21.032 16.25l-1.299 .75" /><path d="M16.27 19l-1.3 .75" /><path d="M14.97 16.25l1.3 .75" /><path d="M19.733 19l1.3 .75" /></svg>
		<#if vertical></span></#if>
		<span class="<#if vertical>nav-link-title<#else>visually-hidden</#if>">#i18n{portal.site.adminFeature.properties_management.name}</span>
	</a>
</${tag}>
<${tag} class="nav-item">
	<a class="nav-link" href="jsp/admin/AdminTechnicalMenu.jsp" title="#i18n{portal.admindashboard.view_dashboards.title}" aria-label="#i18n{portal.admindashboard.view_dashboards.title}"<#if !vertical> data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-original-title="#i18n{portal.admindashboard.view_dashboards.title}"</#if>>
		<#if vertical><span class="nav-link-icon"></#if>
		<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-tabler icons-tabler-outline icon-tabler-adjustments-horizontal"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M14 6m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" /><path d="M4 6l8 0" /><path d="M16 6l4 0" /><path d="M8 12m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" /><path d="M4 12l2 0" /><path d="M10 12l10 0" /><path d="M17 18m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" /><path d="M4 18l11 0" /><path d="M19 18l1 0" /></svg>
		<#if vertical></span></#if>
		<span class="<#if vertical>nav-link-title<#else>visually-hidden</#if>">#i18n{portal.admindashboard.view_dashboards.title}</span>
	</a>
</${tag}>
</#if>
</#macro>
<#-- Icons shared by the header helpers -->
<#macro adminHeaderIconExternalLink>
<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-tabler icons-tabler-outline icon-tabler-external-link"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 6h-6a2 2 0 0 0 -2 2v10a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-6" /><path d="M11 13l9 -9" /><path d="M15 4h5v5" /></svg>
</#macro>
<#macro adminHeaderIconBell>
<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-1">
	<path d="M10 5a2 2 0 1 1 4 0a7 7 0 0 1 4 6v3a4 4 0 0 0 2 3h-16a4 4 0 0 0 2 -3v-3a7 7 0 0 1 4 -6"></path>
	<path d="M9 17v1a3 3 0 0 0 6 0v-1"></path>
</svg>
</#macro>
