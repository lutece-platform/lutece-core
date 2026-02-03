<#--
Macro: adminHeader
Description: Generates a header section for an administrative page, including a navigation menu and user account menu.
Parameters:
- site_name (string, required): the name of the website or application.
-->
<#macro adminHeader site_name=site_name!'Lutece' admin_url=admin_url deprecated...>
<@deprecatedWarning args=deprecated />
<#local userReadMode><#attempt>${dskey('portal.site.site_property.layout.user.readmode.show.checkbox')?number}<#recover>0</#attempt></#local>
<#local userDarkMode><#attempt>${dskey('portal.site.site_property.layout.user.darkmode.show.checkbox')?number}<#recover>0</#attempt></#local>
<#local adminDarkMode><#attempt>${dskey('portal.site.site_property.layout.darkmode.checkbox')?number}<#recover>0</#attempt></#local>
<#--  <#local userMenuMode><#attempt>${dskey('portal.site.site_property.layout.user.menumode.show.checkbox')?number}<#recover>0</#attempt></#local>  -->
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
<script>
document.documentElement.classList.add('loading');
document.documentElement.classList.add('loaded');
let localTheme=localStorage.getItem('lutece-tabler-theme')
<#if adminDarkMode?number==1>
<#if userDarkMode?number!=1>
localStorage.setItem( 'lutece-tabler-theme','dark');
<#else>
if( localTheme === null ){
	localTheme = 'dark';
}
localStorage.setItem( 'lutece-tabler-theme',localTheme );
</#if>
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
<#--  <div class="page" data-userdarkmode="${userDarkMode}" data-usermenu="${userMenuMode}">  -->
<div class="page" data-userdarkmode="${userDarkMode}">
<#if menuVertical == 'vertical'>
<!--  BEGIN SIDEBAR  -->
      <aside class="navbar navbar-vertical navbar-expand-lg<#if menuTransparent!=''>${menuTransparent}</#if>"<#if menuTransparent=''> data-bs-theme="dark"</#if> >
      <div class="container-fluid">
<#else>
	<!-- BEGIN NAVBAR  -->
	<#if navbarSticky !=''><div class="${navbarSticky!}"></#if>
    <header class="navbar navbar-expand-md<#if navbarSticky !=''> ${navbarSticky!}</#if> d-print-none">
    	<div class="container-xl">
</#if>
        <!-- BEGIN NAVBAR TOGGLER -->
        	<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar-menu" aria-controls="navbar-menu" aria-expanded="false" aria-label="Toggle navigation">
            	<span class="navbar-toggler-icon"></span>
          	</button>
          	<!-- END NAVBAR TOGGLER -->
          	<!-- BEGIN NAVBAR LOGO -->
          	<div class="navbar-brand navbar-brand-autodark<#if menuVertical != 'vertical'> navbar-horizontal pe-0 pe-md-3</#if>">
            	<a href="jsp/admin/AdminMenu.jsp" aria-label="${site_name}"> 
					<#if logoSvg?trim !=''>
						${logoSvg!} 
					<#elseif logoUrl?trim!=''>
            			<img src="${logoUrl}" class="me-1" height="32" width="80" alt="Logo ${site_name}" aria-hidden="true">
					<#else>
						<#if showSiteName?number == 1><span class="fs-4 me-2">${site_name}</span></#if>
					</#if>
				</a>
          	</div>
          	<!-- END NAVBAR LOGO -->
		  	<div id="main-nav"class="navbar-nav flex-row <#if menuVertical == 'vertical'>order-last d-flex flex-column align-items-center<#else>order-md-last</#if>">
          		<div class="d-none<#if menuVertical == 'vertical'> d-lg-flex me-3<#else> d-md-flex</#if>">
					<#if menuHome?number==1 && menuCondensed?trim = 'condensed'>
					<div class="nav-item">
						<a class="nav-link" href="." target="_blank">
							<svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-external-link"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 6h-6a2 2 0 0 0 -2 2v10a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-6" /><path d="M11 13l9 -9" /><path d="M15 4h5v5" /></svg>
							<span class="nav-link-title ms-1">#i18n{portal.users.admin_header.homePage}</span>
						</a>
					</div>
					</#if>
					<#if userDarkMode?number = 1>
					<div class="nav-item">
						<a href="?theme=dark" class="nav-link px-0 hide-theme-dark" data-bs-toggle="tooltip" data-bs-placement="bottom" aria-label="Enable dark mode" data-bs-original-title="Enable dark mode">
							<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-1">
								<path d="M12 3c.132 0 .263 0 .393 0a7.5 7.5 0 0 0 7.92 12.446a9 9 0 1 1 -8.313 -12.454z"></path>
							</svg>
						</a>
						<a href="?theme=light" class="nav-link px-0 hide-theme-light" data-bs-toggle="tooltip" data-bs-placement="bottom" aria-label="Enable light mode" data-bs-original-title="Enable light mode">
							<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-1">
								<path d="M12 12m-4 0a4 4 0 1 0 8 0a4 4 0 1 0 -8 0"></path>
								<path d="M3 12h1m8 -9v1m8 8h1m-9 8v1m-6.4 -15.4l.7 .7m12.1 -.7l-.7 .7m0 11.4l.7 .7m-12.1 -.7l-.7 .7"></path>
							</svg>
						</a>
					</div>
					</#if>
					<#if userReadMode?number = 1><@adminReadMode /></#if>
					<#if user.userLevel == 0>
					<#if listLoggersInfo??>
					<#assign listLogDebug = listLoggersInfo?filter( logInfo -> ( logInfo.level = 'DEBUG' || logInfo.level = 'TRACE' ) ) />
					<#if listLogDebug?has_content>
					<div class="nav-item dropdown d-none d-md-flex">
						<a href="#" class="nav-link px-0" data-bs-toggle="dropdown" tabindex="-1" aria-label="#i18n{portal.admin.notification.labelShow}" data-bs-auto-close="outside" aria-expanded="false">
							<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon icon-1">
								<path d="M10 5a2 2 0 1 1 4 0a7 7 0 0 1 4 6v3a4 4 0 0 0 2 3h-16a4 4 0 0 0 2 -3v-3a7 7 0 0 1 4 -6"></path>
								<path d="M9 17v1a3 3 0 0 0 6 0v-1"></path>
							</svg>
							<span class="badge <#if listLoggersInfo?has_content>bg-red</#if>"></span>
						</a>
						<div class="dropdown-menu dropdown-menu-arrow dropdown-menu-end dropdown-menu-card">
							<div class="card">
								<div class="card-header d-flex">
									<h3 class="card-title">#i18n{portal.admin.notification.title}</h3>
									<#--  <div class="btn-close ms-auto" data-bs-dismiss="dropdown"></div>  -->
								</div>
								<div class="list-group list-group-flush list-group-hoverable">
									<div class="list-group-item">
										<div class="row align-items-center">
											<div class="col-auto align-self-baseline"><span class="status-dot status-dot-animated <#if listLoggersInfo?has_content>bg-red</#if> d-block"></span></div>
											<div class="col text-truncate" style="max-height:80vh;overflow-y:auto;">
												<a href="#" class="text-body d-block">#i18n{portal.util.log.warningLevel}</a>
											<#list listLogDebug as logInfo>
												<div class="d-block text-secondary text-truncate mt-n1">
													<#if logInfo?size gt 0><div class="d-block text-truncate mt-1" title="${logInfo.path!}"><strong>${logInfo.name!}  - ${logInfo.level!}</strong> ${logInfo.path!}</div></#if>
												</div>
											</#list>
											</div>
											<div class="col-auto align-self-baseline visually-hidden">
												<a href="#" class="list-group-item-actions">
												<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="icon text-muted icon-2">
													<path d="M12 17.75l-6.172 3.245l1.179 -6.873l-5 -4.867l6.9 -1l3.086 -6.253l3.086 6.253l6.9 1l-5 4.867l1.179 6.873z"></path>
												</svg>
												</a>
											</div>
										</div>
									</div>
								</div>
								<div class="card-body">
									<#--  <div class="row">
										<div class="col">
											<a href="#" class="btn btn-2 w-100"> Archive all </a>
										</div>
										<div class="col">
											<a href="#" class="btn btn-2 w-100"> Mark all as read </a>
										</div>
									</div>  -->
								</div>
							</div>
						</div>
					</div>
					</#if>
					</#if>
					<div class="nav-item">
						<a class="nav-link" href="jsp/admin/ManageProperties.jsp" title="#i18n{portal.site.adminFeature.properties_management.name}" >
							<svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-home-cog"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M9 21v-6a2 2 0 0 1 2 -2h1.6" /><path d="M20 11l-8 -8l-9 9h2v7a2 2 0 0 0 2 2h4.159" /><path d="M18 18m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" /><path d="M18 14.5v1.5" /><path d="M18 20v1.5" /><path d="M21.032 16.25l-1.299 .75" /><path d="M16.27 19l-1.3 .75" /><path d="M14.97 16.25l1.3 .75" /><path d="M19.733 19l1.3 .75" /></svg>
							<span class="visually-hidden">#i18n{portal.site.adminFeature.properties_management.name}</span>
						</a>
					</div>
					<div class="nav-item">
						<a class="nav-link" href="jsp/admin/AdminTechnicalMenu.jsp" title="#i18n{portal.admindashboard.view_dashboards.title}">
							<svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-adjustments-horizontal"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M14 6m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" /><path d="M4 6l8 0" /><path d="M16 6l4 0" /><path d="M8 12m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" /><path d="M4 12l2 0" /><path d="M10 12l10 0" /><path d="M17 18m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" /><path d="M4 18l11 0" /><path d="M19 18l1 0" /></svg>
							<span class="visually-hidden">#i18n{portal.admindashboard.view_dashboards.title}</span>
						</a>
					</div>
					</#if>
				</div>
				<div class="nav-item dropdown<#if menuVertical == 'vertical'> d-block position-relative</#if>"<#if menuVertical == 'vertical'> style="left:-100px;"</#if>>
					<a href="#" class="nav-link d-flex lh-1 p-0 px-2<#if menuVertical == 'vertical'> d-none</#if>" data-bs-toggle="dropdown" aria-label="Open user menu">
						<#local hasAdminAvatar=false />
						<#if hasAdminAvatar>
						<span class="avatar avatar-sm" style="background-image:url(<#if adminAvatar>servlet/plugins/adminavatar/avatar?id_user=${user.userId}<#else>#dskey{portal.site.site_property.avatar_default}</#if>); "></span>
						<#else>
						 <span class="avatar avatar-sm user-initials" data-username="${dashboard_zone_4}"></span>
						</#if>
					</a>
					<div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow <#if menuVertical == 'vertical'>border-0 show</#if>">
						<div class="dropdown-item text-muted">${dashboard_zone_4!}</div>
						<div class="dropdown-item text-muted">${user.dateLastLogin!}</div>
						<#if userMenuItems?has_content><#list userMenuItems as item>${item.content}</#list></#if>
						<div class="dropdown-divider"></div>
						<#if user.userLevel == 0>
						<a href="jsp/admin/AdminTechnicalMenu.jsp" class="dropdown-item">#i18n{portal.admindashboard.view_dashboards.title}</a>
						</#if>
						<#if admin_logout_url?has_content>
						<a href="${admin_logout_url}" class="dropdown-item">#i18n{portal.users.admin_header.deconnectionLink}</a>
						</#if>
					</div>
				</div>
			</div>
<#if menuVertical != 'vertical'>
<#if menuCondensed?trim != 'condensed'>
	</div>
</header>
<header class="navbar-expand-md">
</#if>
<div class="collapse navbar-collapse" id="navbar-menu">
	<div class="navbar<#if menuCondensed?trim = 'condensed'>-nav</#if>">
		<div class="container-xl">
		<div class="row flex-column flex-md-row flex-fill align-items-center">
			<div class="col">
<#else>			
			<div class="collapse navbar-collapse flex-grow-0 mb-5" id="navbar-menu">
</#if>
            	<!-- BEGIN NAVBAR MENU -->
            	<ul id="main-menu" class="navbar-nav">
				<#list feature_group_list as feature_group>
					<#if feature_group.icon?length < 1><#assign icon_class = "ti ti-mood-empty"><#else><#assign icon_class = feature_group.icon></#if>
					<#if feature_group.features?size gt 1>
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle" id="dLabel${feature_group.id}Header" role="button" data-bs-toggle="dropdown" role="button" data-bs-auto-close="outside" aria-expanded="false" href="${admin_url}#${feature_group.id}">
							<span class="nav-link-icon d-md-none d-lg-inline-block"><i class="${icon_class} me-1"></i></span>
							<span class="nav-link-title">${feature_group.label}</span>
						</a>
						<div class="dropdown-menu" aria-labelledby="dLabel${feature_group.id}Header">
							<div class="dropdown-menu-columns">
								<div class="dropdown-menu-column">
								<#list feature_group.features as feature>
									<#if !feature.externalFeature>
										<a class="dropdown-item" id="feature-${feature.id?lower_case}" href="${feature.url}?plugin_name=${feature.pluginName}" title="${feature.name}">
											<span class="nav-link-icon d-md-none d-lg-inline-block"><i class="${feature.iconUrl} me-1"></i> </span>
											<span class="nav-link-title">${feature.name} </span>
										</a>
									<#else>
										<a class="dropdown-item" href="${feature.url}" title="${feature.name}">
											<#if feature.iconUrl?has_content><span class="nav-link-icon d-md-none d-lg-inline-block"><i class="${feature.iconUrl} me-1"></i> </span></#if> 
											<<span class="nav-link-title">${feature.name}</span>
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
					<#if !feature.externalFeature>
						<a class="nav-link" id="feature-${feature.id?lower_case}" href="${feature.url}?plugin_name=${feature.pluginName}"><#if feature.iconUrl?has_content>
							<span class="nav-link-icon d-md-none d-lg-inline-block"><i class="${feature.iconUrl} me-1"></i></span></#if> 
							<span class="nav-link-title">${feature.name}</span>
						</a>
					<#else>
						<a class="nav-link" id="feature-${feature.id?lower_case}" href="${feature.url}">
							<#if feature.iconUrl?has_content><span class="nav-link-icon d-md-none d-lg-inline-block"><i class="${feature.iconUrl} me-1"></i></span></#if> 
							<span class="nav-link-title">${feature.name}</span>
						</a>
					</#if>
					</li>
					</#list>
					</#if>
				</#list>
				</ul>
			<!-- END NAVBAR MENU -->
<#if menuCondensed?trim != 'condensed'>
	<#if menuVertical == 'vertical'>
		</div>
	</aside>
	<!--  END SIDEBAR  -->
	<#else>
		</div>
		<#if menuHome?number==1>
		<div class="col col-md-auto">
			<ul class="navbar-nav">
			<li class="nav-item">
				<a class="nav-link" href="." target="_blank">
				<svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-external-link"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M12 6h-6a2 2 0 0 0 -2 2v10a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-6" /><path d="M11 13l9 -9" /><path d="M15 4h5v5" /></svg>
				<span class="nav-link-title ms-1">#i18n{portal.users.admin_header.homePage}</span>
				</a>
			</li>
			</ul>
		</div>
		</#if>
		</div>
			</div>
		</div>
	</div>
	</header>
	<#if navbarSticky !=''></div></#if>
	</#if>
<#else>
	<#if menuVertical == 'vertical'>
		</div>
	</aside>
	<!--  END SIDEBAR  -->
	<#else>
			</div>
        </div>
      </header>
</#if>
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
<!-- END NAVBAR  -->
<div class="page-wrapper">
</#macro>	