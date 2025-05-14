<#--
Macro: adminHeader
Description: Generates a header section for an administrative page, including a navigation menu and user account menu.
Parameters:
- site_name (string, required): the name of the website or application.
-->
<#macro adminHeader site_name=site_name!'Lutece' admin_url=admin_url >
<#local userReadMode><#attempt>${dskey('portal.site.site_property.layout.user.readmode.show.checkbox')?number}<#recover>0</#attempt></#local>
<#local userDarkMode><#attempt>${dskey('portal.site.site_property.layout.user.darkmode.show.checkbox')?number}<#recover>0</#attempt></#local>
<#local userMenuMode><#attempt>${dskey('portal.site.site_property.layout.user.menumode.show.checkbox')?number}<#recover>0</#attempt></#local>
<#local readMode><#attempt><#if dskey('portal.site.site_property.layout.readmode.checkbox')?number = 1> dir="rtl"</#if><#recover></#attempt></#local>
<#local darkMode><#attempt><#if dskey('portal.site.site_property.layout.darkmode.checkbox')?number==1> theme-dark</#if><#recover></#attempt></#local>
<#local layout><#attempt><#if dskey('portal.site.site_property.layout.menu.checkbox')?number==1>aside<#else>header</#if><#recover>header</#attempt></#local>
<#local logoUrl = (dskey('portal.site.site_property.logo_url')!)?has_content?then(dskey('portal.site.site_property.logo_url'), 'themes/admin/shared/images/logo-header-icon.svg')>
</head>
<body class="antialiased"${readMode}>
<@adminSkipNav />
<div id="lutece-layout-wrapper" class="${layout!}" data-userdarkmode="${userDarkMode}" data-usermenu="${userMenuMode}">
<header class="lutece-header" > 
	<nav class="lutece-nav navbar navbar-expand-lg d-print-none" data-bs-theme="dark">
		<div class="container-fluid">
			<a class="lutece-brand navbar-brand navbar-brand-autodark d-none-navbar-horizontal pe-0" href="${dskey('portal.site.site_property.home_url')}" title="#i18n{portal.users.admin_header.title.viewSite} ${site_name}" target="_blank" title="#i18n{portal.site.portal_footer.newWindow}">
				<img src="${logoUrl}" height="30" width="30" alt="Logo ${site_name}" aria-hidden="true">
				<span class="ml-2 ms-2">${site_name}</span>
			</a>
			<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar-menu">
				<span class="navbar-toggler-icon"></span>
				<span class="visually-hidden">#i18n{portal.util.labelShow} / #i18n{portal.util.labelHide}</span>
			</button>
			<div class="lutece-collapse collapse navbar-collapse" id="navbar-menu">
				<ul class="navbar-nav">
					<#list feature_group_list as feature_group>
						<#if feature_group.icon?length < 1>
							<#assign icon_class = "ti ti-mood-empty">
						<#else>
							<#assign icon_class = feature_group.icon>
						</#if>
						<#if feature_group.features?size &gt; 1>
							<li class="nav-item dropdown">
								<a class="nav-link dropdown-toggle" id="dLabel${feature_group.id}Header" role="button" data-bs-toggle="dropdown" role="button" aria-expanded="false" href="${admin_url}#${feature_group.id}">
									<i class="${icon_class} me-1"></i> <span>${feature_group.label}</span>
								</a>
								<div class="dropdown-menu" aria-labelledby="dLabel${feature_group.id}Header">
									<div class="dropdown-menu-columns">
										<div class="dropdown-menu-column">
										<#list feature_group.features as feature>
											<#if !feature.externalFeature>
												<a class="dropdown-item" href="${feature.url}?plugin_name=${feature.pluginName}" title="${feature.name}">
													<i class="${feature.iconUrl} me-1"></i> 
													<span>${feature.name} </span>
												</a>
											<#else>
												<a class="dropdown-item" href="${feature.url}" title="${feature.name}">
													<#if feature.iconUrl?has_content><i class="${feature.iconUrl} me-1"></i></#if> 
													<span>${feature.name}</span>
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
									<a class="nav-link" href="${feature.url}?plugin_name=${feature.pluginName}"><#if feature.iconUrl?has_content>
										<i class="${feature.iconUrl} me-1"></i></#if> 
										<span>${feature.name}</span>
									</a>
								<#else>
									<a class="nav-link" href="${feature.url}"><#if feature.iconUrl?has_content><i class="${feature.iconUrl} me-1"></i></#if> 
										<span>${feature.name}</span>
									</a>
								</#if>
								</li>
							</#list>
						</#if>
					</#list>
					<li class="nav-item d-none d-lg-flex ms-auto">
						<a class="nav-link"  href="${admin_url}" title="#i18n{portal.users.admin_header.homePage}" id="go-home">
							<i class="ti ti-home"></i><span class="visually-hidden">#i18n{portal.users.admin_header.homePage}</span>
						</a>
					</li>
					<li class="nav-item d-flex d-lg-none mt-2">
						<a class="nav-link"  href="${admin_url}" title="#i18n{portal.users.admin_header.homePage}" id="go-home">
							<i class="ti ti-home me-2"></i> <span>#i18n{portal.users.admin_header.homePage}</span>
						</a>
					</li>
					<#if userMenuMode?number = 1>
					<li class="nav-item d-none d-lg-flex">
						<div class="nav-link" title="#i18n{portal.users.admin_header.labelMenuV} / #i18n{portal.users.admin_header.labelMenuH}" id="switch-menu" tabindex="0" role="button" data-bs-toggle="tooltip" data-bs-animation="false" data-bs-placement="bottom" data-bs-original-title="#i18n{portal.users.admin_header.labelMenuV} / #i18n{portal.users.admin_header.labelMenuH}">
							<i class="ti ti-layout-navbar-collapse menu-rotate-icon"></i><span class="visually-hidden">#i18n{portal.users.admin_header.labelMenuV} / #i18n{portal.users.admin_header.labelMenuH}</span>
						</div> 
					</li>
					</#if>
					<#if userDarkMode?number = 1>
					<li class="nav-item" id="switch-darkmode">
						<div class="nav-link d-none d-lg-flex" tabindex="0" role="button">
							<i class="ti ti-moon"></i><span class="visually-hidden">#i18n{portal.users.admin_header.labelMode} <span>#i18n{portal.users.admin_header.labelDarkMode}</span></span>
						</div>
						<div class="nav-link d-flex d-lg-none" tabindex="0" role="button">
							<i class="ti ti-moon me-2"></i> <span>#i18n{portal.users.admin_header.labelMode} #i18n{portal.users.admin_header.labelDarkMode}</span>
						</div>
					</li>
					</#if>
					<#if userReadMode?number = 1>
						<@adminReadMode />
					</#if>
					<#if user.userLevel == 0>
					<li class="nav-item d-none d-lg-flex">
						<a class="nav-link" href="jsp/admin/ManageProperties.jsp" title="#i18n{portal.site.adminFeature.properties_management.name}" >
							<i class="ti ti-home-cog"></i>
							<span class="visually-hidden">#i18n{portal.site.adminFeature.properties_management.name}</span>
						</a>
					</li>
					<li class="nav-item d-flex d-lg-none">
						<a class="nav-link" href="jsp/admin/ManageProperties.jsp" title="#i18n{portal.site.adminFeature.properties_management.name}" >
							<i class="ti ti-home-cog me-2"></i> <span>#i18n{portal.site.adminFeature.properties_management.name}</span>
						</a>
					</li>
					<li class="nav-item d-none d-lg-flex">
						<a class="nav-link" href="jsp/admin/AdminTechnicalMenu.jsp" title="#i18n{portal.admindashboard.view_dashboards.title}">
							<i class="ti ti-settings"></i>
							<span class="visually-hidden">#i18n{portal.admindashboard.view_dashboards.title}</span>
						</a>
					</li>
					<li class="nav-item d-flex d-lg-none">
						<a class="nav-link" href="jsp/admin/AdminTechnicalMenu.jsp" title="#i18n{portal.admindashboard.view_dashboards.title}">
							<i class="ti ti-settings me-2"></i> <span>#i18n{portal.admindashboard.view_dashboards.title}</span>
						</a>
					</li>
					</#if>
					<li class="nav-item dropdown d-none d-lg-flex">
						<a href="#" class="nav-link nav-info nav-user-info lh-1 py-0 px-2 text-reset dropdown-toggle" data-bs-toggle="dropdown" role="button" >
							<span class="visually-hidden">#i18n{portal.util.labelMore}</span>
							<span class="avatar avatar-sm" style="background-image:url(<#if adminAvatar>servlet/plugins/adminavatar/avatar?id_user=${user.userId}<#else>#dskey{portal.site.site_property.avatar_default}</#if>)"></span>
							<div class="ps-2 pt-3 user-infos">
								<p class="mb-0 fs-5 user-login">${dashboard_zone_4!}</p>
								<p class="mt-0 small user-date">${user.dateLastLogin!}</p>
							</div>
						</a>
						<div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow nav-info">
						<#if userMenuItems?has_content>   
							<#list userMenuItems as item>
								${item.content}
							</#list>
						</#if>
						<div class="dropdown-divider nav-info"></div>
						<#if admin_logout_url?has_content>
						<a class="dropdown-item dropdown-logout d-none d-lg-flex" href="${admin_logout_url}" title="#i18n{portal.users.admin_header.deconnectionLink}">
							<i class="ti ti-logout me-1"></i> 
							<span>#i18n{portal.users.admin_header.deconnectionLink}</span>
						</a>
						</#if> 
						</div>
					</li>
					<li class="nav-item justify-content-start d-lg-none">
						<a class="nav-link dropdown-toggle" id="mobile_usermenuitems" role="button" data-bs-toggle="dropdown" role="button" aria-expanded="false" href=""><i class="ti ti-user me-2"></i>${dashboard_zone_4!}</a>
						<#if userMenuItems?has_content>
							<ul class="dropdown-menu">
								<li class="nav-item d-flex"><span class="dropdown-item"><i class="ti ti-calendar me-2"></i> #i18n{portal.users.admin_header.labelLastLogin} ${user.dateLastLogin!}</span></li>
								<#list userMenuItems as item>
								<li class="nav-item d-flex">${item.content}</li>
								</#list>
							</ul>
						</#if>
					</li>
					<#if admin_logout_url?has_content>
					<li class="nav-item d-flex d-lg-none">
						<a class="nav-link" href="${admin_logout_url}" title="#i18n{portal.users.admin_header.deconnectionLink}">
							<i class="ti ti-logout me-2"></i> 
							<span>#i18n{portal.users.admin_header.deconnectionLink}</span>
						</a>
					</li>	
					</#if>
				</ul>
				<button id="aside-header-collapse" class="btn btn-dark btn-sm mt-5" type="button" title="#i18n{portal.util.labelToggleSize}">
					<span class="ti ti-switch-horizontal"></span>
					<span class="visually-hidden">#i18n{portal.util.labelToggleSize}</span>
				</button>
			</div>
		</div>
	</nav>
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
</header> 
</#macro>	