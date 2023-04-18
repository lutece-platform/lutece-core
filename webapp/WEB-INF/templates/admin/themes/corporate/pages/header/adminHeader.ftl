<#--
   Macro: adminHeader
   Description: Generates a header section for an administrative page, including a navigation menu and user account menu.
   Parameters:
   - site_name (string, required): the name of the website or application.
   -->
   <#macro adminHeader site_name=site_name admin_url=admin_url>
      </head>
      <body class="antialiased<#if false>theme-dark</#if>" data-bs-theme="light">
		<@pageContainer class="d-md-block d-lg-none">
    <@pageColumn id="lutece-login-block" class="border-end p-0" height="full">
	<@div class="d-flex align-items-center justify-content-evenly vh-100">
			<@div class="container-tight">
					<@div class="card shadow-lg rounded-4 p-4 mx-5 mw-30 ">
					<@div class="card-body p-5 fs-6">
					<@div class="text-center mb-4">
				<@link href='.'>
               <img src="themes/admin/corporate/images/core-corporate.png" height="40" alt="Logo">
						<span class="sr-only">
							${site_name!'Lutece'}
						</span>
					</@link>
				</@div>
												 <h1 class="mb-4"> <center>#i18n{portal.admin.admin_login.welcome} ${site_name!}</center></h1>
				<center><i class="ti ti-device-mobile-off" style="font-size:120px !important"></i></center>
				</@div>
			</@div>
			</@div>
		</@div>
	</@pageColumn>
	</@pageContainer>
<div class="lutece-app d-none d-lg-block">
         <div id="menu" class="border-end d-flex flex-column flex-shrink-0 shadow">
            <a href="/" class="d-block  link-dark text-decoration-none text-center mt-4 mb-4" title="" data-bs-toggle="tooltip" data-bs-animation="false" data-bs-placement="right" data-bs-original-title="${site_name}">
               <img src="themes/admin/corporate/images/core-corporate.png" height="40" alt="Logo">
            </a>
            <ul class="nav nav-pills nav-flush flex-column mb-auto text-center">
               <li class="nav-item py-1">
                  <a class="nav-link d-flex feature-link lutece-tooltip " href="${admin_url}" feature-group-label="home" feature-group="home" aria-current="page" title="" data-bs-animation="false" data-bs-tooltip="Accueil">
                     <i class="fs-5 ti ti-home-2 mx-auto align-self-center"></i>
                  </a>
               </li>
               <#list feature_group_list as feature_group>
<#if feature_group.icon?length < 1>
  <#assign icon_class = "ti ti-mood-empty">
<#else>
  <#assign icon_class = feature_group.icon>
</#if>
                  <#if feature_group.features?size &gt; 1>
                     <li class="nav-item py-1">
                        <a href="#${feature_group.id}" feature-group-label="${feature_group.label}" feature-group="${feature_group.id}" class="nav-link d-flex" aria-current="page" title="" data-bs-toggle="tooltip" data-bs-animation="false" data-bs-placement="right" data-bs-original-title="${feature_group.label}">
                           <i class="fs-5 align-self-center mx-auto ${icon_class}"></i>
                        </a>
                     </li>
                     <#else>
                        <#list feature_group.features as feature>
                           <#if !feature.externalFeature>
                              <li class="nav-item py-1">
                                 <a href="${feature.url}?plugin_name=${feature.pluginName}" feature-group-label="${feature_group.label}" feature-group="${feature_group.id}" class="nav-link feature-link d-flex" aria-current="page" title="" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-animation="false" data-bs-original-title="${feature.name}">
                                    <i class="fs-5 ${icon_class} align-self-center mx-auto"></i>
                                 </a>
                              </li>
                              <#else>
                                 <li class="nav-item py-1">
                                    <a href="${feature.url}?plugin_name=${feature.pluginName}" feature-group-label="${feature_group.label}" feature-group="${feature_group.id}" class="nav-link feature-link d-flex" aria-current="page" title="" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-animation="false" data-bs-original-title="${feature.name}">
                                       <i class="fs-5  align-self-center mx-auto ${icon_class}"></i>
                                    </a>
                                 </li>
                           </#if>
                        </#list>
                  </#if>
               </#list>
            </ul>
            <div>
            </div>
            <img src="images/logo-header.png" id="logo-lutece" class="position-absolute">
         </div>
         <header id="top-menu" class="navbar navbar-expand-md navbar-light d-none d-lg-flex d-print-none border-bottom h-60 shadow">
            <div class="container-fluid">
               <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar-menu">
                  <span class="navbar-toggler-icon"></span>
               </button>
               <h1></h1>
               <ul class="navbar-nav flex-row order-md-last">
                  <li class="nav-item">
                     <div id="darkmode">
                        <input type="checkbox" id="toggle-theme" />
                        <label for="toggle-theme" class="border"></label>
                     </div>
                  </li>
                  <#if user.userLevel==0>
                     <#assign hasIcon=false />
                     <#if listLoggersInfo?has_content>
                        <li class="nav-item dropdown">
                           <a class="border btn btn-light btn-rounded" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                              <i class="ti ti-bell fs-5"></i>
                           </a>
                           <ul class="dropdown-menu p-3">
                              <li class="border-bottom mb-3">
                                 <span class="badge bg-danger-subtle text-danger-emphasis p-1 px-2 rounded-5 mb-3"><small>#i18n{portal.util.log.warningLevel}
                                    </small></span>
                              </li>
                              <#list listLoggersInfo?filter( logInfo -> ( logInfo.level = 'DEBUG' || logInfo.level = 'TRACE' ) ) as logInfo>
                                 <#if logInfo?size gt 0>
                                    <li>
                                       <#assign path=logInfo.path?string?replace("\\", "/" )>
                                          <#assign dernier_element=path?split("/")?last>
                                             <div class="d-block text-truncate mb-2 fw-bold" title="${logInfo.path!}">
                                                ${logInfo.name!}
                                                <br>
                                                <#if logInfo.level='DEBUG'>
                                                   <span class="badge bg-warning-subtle text-warning-emphasis p-1 px-2 rounded-5"><small>Debug</small></span>
                                                </#if>
                                                <#if logInfo.level='TRACE'>
                                                   <span class="badge bg-danger-subtle text-danger-emphasis p-1 px-2 rounded-5"><small>Trace</small></span>
                                                </#if>
                                                <span class="badge bg-secondary-subtle text-dark-emphasis p-1 px-2 rounded-5"><small><i class="ti ti-file-code-2"></i>
                                                      ${dernier_element!}
                                                </span></small>
                                             </div>
                                    </li>
                                 </#if>
                              </#list>
                           </ul>
                        </li>
                     </#if>
                     <li class="nav-item">
                        <a class="border btn btn-light btn-rounded" href="jsp/admin/AdminTechnicalMenu.jsp" title="#i18n{portal.admindashboard.view_dashboards.title}">
<i class="ti ti-settings fs-5"></i>
                        </a>
                     </li>
                  </#if>
                  <li class="nav-item dropdown lutece-profile">
                     <a href="#" class="border btn btn-light btn-rounded" data-bs-toggle="dropdown" >
                        <div class="lutece-profile-name">
                           <div class="small fw-bold">
                              <span id="lutece-profile-name">${dashboard_zone_4!}</span> <i class="ti ti-chevron-down"></i>
                           </div>
                        </div>
                     </a>
                   	<div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
				<#if userMenuItems?has_content>   
					<#list userMenuItems as item>
						${item.content}
					</#list>
				</#if>
				<div class="dropdown-divider"></div>
				<#if admin_logout_url?has_content>
					<a class="dropdown-item" href="${admin_logout_url}" title="#i18n{portal.users.admin_header.deconnectionLink}">
						<i class="ti ti-logout me-1"></i> #i18n{portal.users.admin_header.deconnectionLink}
					</a>
				</#if>
				</div>
                     </li>
               </ul>
            </div>
         </header>
         <main id="lutece-main" class="d-flex overflow-hidden">
            <div id="child-menu" class="d-flex flex-column align-items-stretch flex-shrink-0 border-end d-none shadow">
               <div class="content">
                  <div class="h-60 border-bottom">
                  <div class="d-flex h-60  align-items-center justify-content-center pe-4">
  <div class="flex-fill">
<div class="input-group input-icon input-group-flat h-60">
   <span class="input-icon-addon">
      <i class="ps-2 ti ti-search" aria-hidden="true"></i>
   </span>
	<input class="form-control border-0 " type="text" placeholder="Recherchez dans le menu.." id="search-menu">
</div></div>
  <div > <div id="menu-switcher" class="border btn btn-light btn-rounded"><i id="menu-icon" class="ti fs-6"></i>
</div></div>
</div>
                  </div>
                  <div id="feature-list" class="h-60 border-bottom">
                     <#list feature_group_list as feature_group>
                        <div class="w-100 feature-group d-none" feature-group-label="${feature_group.label}" feature-group="${feature_group.id}">
                        <div class="d-flex h-60  align-items-center justify-content-center px-4">
  <div class="flex-fill">   <h5 class="fw-bolder mb-0 h-60 lh-60">
                              ${feature_group.label}
                           </h5></div>
  <div><#if user.userLevel==0><a href="jsp/admin/AdminTechnicalMenu.jsp?tab=assign_features_groups#features_management#${feature_group.id}" class="border btn btn-light btn-rounded"><i class="fs-6 float-end lh-60 h-60 ${feature_group.icon!''}"></i>
</a>
</#if>
</div>
</div>
                        </div>
                     </#list>
                  </div>
                  <div id="right-list" class="list-group list-group-flush scrollarea pb-5">
                     <#list feature_group_list as feature_group>
                        <#if feature_group.features?size &gt; 1>
                           <#list feature_group.features as feature>
                              <#if !feature.externalFeature>
                                 <a admin-url="${admin_url}" feature-url="${feature.url}" feature-group-label="${feature_group.label}" feature-group="${feature_group.id}" plugin_name="${feature.pluginName}" class="list-group-item list-group-item-action p-4 lh-tight" href="${feature.url}?plugin_name=${feature.pluginName}">
                                    <div class="row align-items-center">
                                       <div class="col">
                                          <h6 class="mb-1 fw-bold title">
                                             <span>${feature.name}</span>
                                          </h6>
                                          <div class="d-block text-muted mt-n1">
                                             <span>${feature.description!''}</span>
                                          </div>
                                       </div>
                                    </div>
                                 </a>
                                 <!-- 
				   <a class="" admin-url="${admin_url}" feature-url="${feature.url}" feature-group-label="${feature_group.label}" feature-group="${feature_group.id}" plugin_name="${feature.pluginName}" href="${feature.url}?plugin_name=${feature.pluginName}">
${feature.name} - ${feature.description!''}
</a> -->
                                 <#else>
                                    <a class="dropdown-item" href="${feature.url}">
                                       <#if feature.iconUrl?has_content><i class="${feature.iconUrl}"></i></#if>
                                       ${feature.name}
                                       ${feature.descriptionKey}
                                    </a>
                              </#if>
                           </#list>
                           <#else>
                        </#if>
                     </#list>
                  </div>
               </div>
            </div>
            <div id="page" class="w-100">
   </#macro>