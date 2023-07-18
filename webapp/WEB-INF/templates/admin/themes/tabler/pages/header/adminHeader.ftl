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
<#local logoUrl = (dskey('portal.site.site_property.logo_url')!)?has_content?then(dskey('portal.site.site_property.logo_url'), 'themes/admin/shared/images/logo-header-icon.png')>
</head>
<body class="antialiased${darkMode!}"${readMode}>
<@adminSkipNav />
<div id="lutece-layout-wrapper" class="${layout!}">
<header class="lutece-header" > 
	<nav class="lutece-nav navbar navbar-expand-lg navbar-dark d-print-none">
		<div class="container-fluid">
			<a class="lutece-brand navbar-brand navbar-brand-autodark d-none-navbar-horizontal pe-0 pe-md-3" href="jsp/site/Portal.jsp" title="#i18n{portal.users.admin_header.title.viewSite} ${site_name}" target="_blank" title="#i18n{portal.site.portal_footer.newWindow}">
				<img src="${logoUrl}" height="30" alt="Logo ${site_name}" aria-hidden="true">
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
												<a class="dropdown-item" href="${feature.url}?plugin_name=${feature.pluginName}"><i class="${feature.iconUrl} me-1"></i> ${feature.name} </a>
											<#else>
												<a class="dropdown-item" href="${feature.url}">
													<#if feature.iconUrl?has_content><i class="${feature.iconUrl} me-1"></i></#if> ${feature.name}
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
									<a class="nav-link" href="${feature.url}?plugin_name=${feature.pluginName}"><#if feature.iconUrl?has_content><i class="${feature.iconUrl} me-1"></i></#if> ${feature.name}</a>
								<#else>
									<a class="nav-link" href="${feature.url}"><#if feature.iconUrl?has_content><i class="${feature.iconUrl} me-1"></i></#if> ${feature.name}</a>
								</#if>
								</li>
							</#list>
						</#if>
					</#list>
				</ul>
			</div>
			<div class="lutece-nav-user navbar-nav">
				<div class="nav-item dropdown d-none d-md-flex me-2">
					<a class="nav-link" href="${admin_url}" title="#i18n{portal.users.admin_header.homePage}" id="go-home">
						<i class="ti ti-home"></i>
						<label for="go-home">
							<span class="visually-hidden">#i18n{portal.users.admin_header.homePage}</span>
						</label>
					</a>
				</div>
				<#if userMenuMode?number = 1>
				<div class="nav-item dropdown d-none d-md-flex me-2">
					<div class="nav-link" title="#i18n{portal.users.admin_header.homePage}" id="switch-menu" tabindex="0" role="button" data-bs-toggle="tooltip" data-bs-animation="false" data-bs-placement="bottom" data-bs-original-title="#i18n{portal.users.admin_header.labelMenuV} / #i18n{portal.users.admin_header.labelMenuH}">
						<i class="ti ti-layout-navbar-collapse menu-rotate-icon"></i>
						<label for="switch-menu">
							<span class="visually-hidden">#i18n{portal.users.admin_header.labelMenuV} / #i18n{portal.users.admin_header.labelMenuH}</span>
						</label>
					</div> 
				</div>
				</#if>
         		<#if userDarkMode?number = 1>
				<div class="nav-item dropdown d-none d-md-flex me-2" id="switch-darkmode">
					<div class="nav-link" tabindex="0" role="button">
						<i class="ti ti-moon"></i>
						<label for="switch-darkmode"> 
							<span class="visually-hidden">#i18n{portal.users.admin_header.labelMode} <span>#i18n{portal.users.admin_header.labelDarkMode}</span></span>
						</label>
					</div>
				</div>
				</#if>
				<#if userReadMode?number = 1>
					<@adminReadMode />
				</#if>
				<#if user.userLevel == 0>
				<#if listLoggersInfo??>
				<#assign listLogDebug = listLoggersInfo?filter( logInfo -> ( logInfo.level = 'DEBUG' || logInfo.level = 'TRACE' ) ) />
				<#if listLogDebug?has_content>
					<div class="nav-item d-none d-md-flex me-2">
						<a href="#" class="nav-link px-0" data-bs-toggle="dropdown" tabindex="-1" aria-label="Show notifications" aria-expanded="false">
							<svg xmlns="http://www.w3.org/2000/svg" class="icon" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"></path><path d="M10 5a2 2 0 0 1 4 0a7 7 0 0 1 4 6v3a4 4 0 0 0 2 3h-16a4 4 0 0 0 2 -3v-3a7 7 0 0 1 4 -6"></path><path d="M9 17v1a3 3 0 0 0 6 0v-1"></path></svg>
							<#if listLoggersInfo?has_content><span class="badge bg-red"></span></#if>
						</a>
						<div class="dropdown-menu dropdown-menu-arrow dropdown-menu-end dropdown-menu-card">
							<div class="card">
								<div class="card-header">
									<h3 class="card-title">Notifications</h3>
								</div>
								<div class="list-group list-group-flush list-group-hoverable">
									<div class="list-group-item logger">
										<div class="row align-items-start">
											<div class="col-auto pt-2">
												<span class="badge bg-red d-block"></span>
											</div>
											<div class="col text-truncate">
												<h3><a href="#" class="text-body d-block">#i18n{portal.util.log.warningLevel}</a></h3>
												<#list listLogDebug as logInfo>
													<#if logInfo?size gt 0><div class="d-block text-truncate mt-1" title="${logInfo.path!}"><strong>${logInfo.name!}  - ${logInfo.level!}</strong> ${logInfo.path!}</div></#if>
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
				<div class="nav-item d-none d-md-flex me-2">
					<a class="nav-link px-0" href="jsp/admin/ManageProperties.jsp" title="#i18n{portal.site.adminFeature.properties_management.name}" >
						<i class="ti ti-home-cog"></i><span class="visually-hidden">#i18n{portal.site.adminFeature.properties_management.name}</span>
					</a>
				</div>
				<div class="nav-item d-none d-md-flex me-2">
					<a class="nav-link px-0" href="jsp/admin/AdminTechnicalMenu.jsp" title="#i18n{portal.admindashboard.view_dashboards.title}">
						<i class="ti ti-settings"></i><span class="visually-hidden">#i18n{portal.admindashboard.view_dashboards.title}</span>
					</a>
				</div>
				</#if>
		  	</div> 
			<div class="nav-item dropdown">
				<a href="#" class="nav-link d-flex lh-1 text-reset p-0 dropdown-toggle" data-bs-toggle="dropdown" role="button" >
					<span class="visually-hidden">#i18n{portal.util.labelMore}</span>
					<span class="avatar avatar-sm" style="background-image:url(<#if adminAvatar>servlet/plugins/adminavatar/avatar?id_user=${user.userId}<#else>#dskey{portal.site.site_property.avatar_default}</#if>)"></span>
					<div class="d-none d-sm-block ps-2">
						<div>${dashboard_zone_4!}</div>
						<div class="mt-1 small">${user.dateLastLogin!}</div>
					</div>
				</a>
				<div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
				<#if userMenuItems?has_content>   
					<#list userMenuItems as item>
						${item.content}
					</#list>
				</#if>
				<#--  <div class="dropdown-divider"></div>  -->
				<#--  <div class="ps-2" id="switch-darkmode"><i class="ti ti-moon"></i> #i18n{portal.users.admin_header.labelMode} <span>#i18n{portal.users.admin_header.labelDarkMode}</span></div>  -->
				<div class="dropdown-divider"></div>
				<#if admin_logout_url?has_content>
					<a class="dropdown-item dropdown-logout" href="${admin_logout_url}" title="#i18n{portal.users.admin_header.deconnectionLink}">
						<i class="ti ti-logout me-1"></i> #i18n{portal.users.admin_header.deconnectionLink}
					</a>
				</#if> 
				</div>
			</div>
			<#if admin_logout_url?has_content>
				<a id="lutece-logout" class="nav-link ms-2" href="${admin_logout_url}" title="#i18n{portal.users.admin_header.deconnectionLink}">
					<i class="ti ti-logout me-1"></i> <span>#i18n{portal.users.admin_header.deconnectionLink}</span>
				</a>
			</#if>
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