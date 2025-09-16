<#--
   Macro: adminHeader
   Description: Generates a header section for an administrative page, including a navigation menu and user account menu.
   Parameters:
   - site_name (string, required): the name of the website or application.
-->
<#macro adminHeader site_name=site_name admin_url=admin_url>
<#local readMode><#attempt><#if dskey('portal.site.site_property.layout.readmode.checkbox')?number = 1> dir="rtl"</#if><#recover></#attempt></#local>
<#local darkMode><#attempt><#if dskey('portal.site.site_property.layout.darkmode.checkbox')?number==1>dark<#else>light</#if><#recover>light</#attempt></#local>
<#local layout><#attempt><#if dskey('portal.site.site_property.layout.menu.checkbox')?number==1>left<#else>top</#if><#recover>top</#attempt></#local>
<#local userReadMode><#attempt>${dskey('portal.site.site_property.layout.user.readmode.show.checkbox')?number}<#recover>0</#attempt></#local>
<#local userDarkMode><#attempt>${dskey('portal.site.site_property.layout.user.darkmode.show.checkbox')?number}<#recover>0</#attempt></#local>
<#local userMenuMode><#attempt>${dskey('portal.site.site_property.layout.user.menumode.show.checkbox')?number}<#recover>0</#attempt></#local>
<#local logoUrl = (dskey('portal.site.site_property.logo_url')!)?has_content?then(dskey('portal.site.site_property.logo_url'), 'themes/admin/shared/images/logo-header.svg')>
<style>.icon-item-new {width: .5rem;height: .5rem;background: #c00;border-radius: 100%; display: block; position: absolute; right: 0; top: 0; z-index: 1}</style>
</head>
<body class="antialiased"${readMode} data-bs-theme="${darkMode!}" data-bs-theme-menu="${layout!}" data-layout="${layout!}">
<@adminSkipNav />
<div class="lutece-app<#if dskey('portal.site.site_property.bo.showXs.checkbox') == '0'> d-none d-lg-block<#else> d-block</#if>">   
<nav id="menu" class="border-end d-flex flex-column flex-shrink-0 shadow" aria-label="${site_name!}">
   <a href="${dskey('portal.site.site_property.home_url')}" class="d-block text-center mt-4 mb-4 text-white feature-link menu-logo" target="_blank" title="${site_name!}">
      <@img url=logoUrl title='${site_name!}' params='style="height:35px" aria-hidden="true"' />
      <span class="visually-hidden">#i18n{portal.site.page_home.label} ${site_name}<br> [#i18n{portal.site.portal_footer.newWindow}]</span>
   </a>
   <#if site_name != "LUTECE"><div class="d-inline-block h-100 text-white fw-bold ps-2 align-content-center d-none-side" style="font-size:24px;line-height:74px;">${site_name}</div></#if>
   <div class="position-absolute top-0 end-0 d-flex p-4 d-lg-flex d-xl-none">
      <button id="menu-mobile-back" class="btn btn-sm btn-outline-light d-flex align-items-center justify-content-center d-none" type="button" >
      <i class="ti ti-arrow-left"></i>
         </button>
         <button id="menu-mobile-close" class="btn btn-sm btn-outline-light d-flex align-items-center justify-content-center" type="button" >
            <i class="ti ti-x"></i>
         </button>
   </div>
   <ul id="main-menu" class="nav nav-pills nav-flush flex-column mb-auto text-center ms-top-4" role="menubar" aria-label="${site_name!}">
      <li class="nav-item py-1" role="none">
         <a class="nav-link  feature-link lutece-tooltip" role="menuitem" href="${admin_url}" feature-group-label="home" feature-group="home" aria-label="#i18n{portal.util.labelHome}" data-bs-animation="false" data-bs-tooltip="#i18n{portal.util.labelHome}">
            <i class="fs-5 ti ti-home-2 mx-auto align-self-center"></i>
            <span class="menu-label">#i18n{portal.util.labelHome}</span>
         </a>
      </li>
      <#list feature_group_list as feature_group>
         <#if feature_group.icon?length < 1>
            <#assign icon_class = "ti ti-mood-empty">
         <#else>
            <#assign icon_class = feature_group.icon>
         </#if>
         <#if feature_group.features?size &gt; 1>
            <li class="nav-item py-1 text-center" role="none">
               <a href="#${feature_group.id}" feature-group-label="${feature_group.label}" feature-group="${feature_group.id}" class="nav-link text-center" role="menuitem" aria-haspopup="true" aria-expanded="false" title="" data-bs-toggle="tooltip" data-bs-animation="false" data-bs-placement="right" data-bs-original-title="${feature_group.label}" aria-label="${feature_group.label}">
                  <i class="fs-5 align-self-center mx-auto ${icon_class}"></i>  <div class="menu-label">${feature_group.label}</div>
               </a>
            </li>
         <#else>
            <#list feature_group.features as feature>
               <#if !feature.externalFeature>
                  <li class="nav-item py-1 text-center" role="none">
                     <a href="${feature.url}?plugin_name=${feature.pluginName}" role="menuitem" aria-haspopup="true" aria-expanded="false" feature-group-label="${feature_group.label}" feature-group="${feature_group.id}" class="nav-link feature-link text-center" aria-current="page" title="" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-animation="false" data-bs-original-title="${feature.name}" aria-label="${feature.name}">
                        <i class="fs-5 ${icon_class} align-self-center mx-auto"></i>  <div class="menu-label">${feature_group.label}</div>
                     </a>
                  </li>
               <#else>
                  <li class="nav-item py-1 text-center" role="none">
                     <a href="${feature.url}?plugin_name=${feature.pluginName}"  role="menuitem"  feature-group-label="${feature_group.label}" feature-group="${feature_group.id}" class="nav-link feature-link text-center" aria-current="page" title="" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-animation="false" data-bs-original-title="${feature.name}" aria-label="${feature.name}">
                        <i class="fs-5  align-self-center mx-auto ${icon_class}"></i>  <div class="menu-label">${feature_group.label}</div>
                     </a>
                  </li>
               </#if>
            </#list>
         </#if>
      </#list>
   </ul>
</div>
<div id="child-menu" class="d-flex flex-column align-items-stretch flex-shrink-0 border-end d-none no-pin shadow" role="navigation">
   <div class="content">
      <div id="menu-search-container" class="h-60 border-bottom">
         <div class="d-flex h-60  align-items-center justify-content-center pe-4">
            <div class="flex-fill">
               <div class="input-group input-icon input-group-flat h-60">
                  <label for="search-menu"><span class="visually-hidden">#i18n{portal.util.labelSearchMenu}</span></label>
                  <span class="input-icon-addon">
                     <i class="ps-2 ti ti-search" aria-hidden="true"></i>
                  </span>
                  <input class="form-control border-0" type="text" placeholder="#i18n{portal.util.labelSearchMenu}" id="search-menu">
               </div>
            </div>
            <div> 
               <div id="menu-switcher" class="border btn btn-light btn-rounded" role="button" aria-label="#i18n{portal.users.admin_header.labelLockMenu}">
                  <i id="menu-icon" class="ti ti-check fs-6"></i>
                  <span class="visually-hidden">#i18n{portal.users.admin_header.labelLockMenu}</span>
               </div>
         </div>
      </div>
   </div>
   <div id="feature-list" class="h-60 border-bottom">
      <#list feature_group_list as feature_group>
         <div class="w-100 feature-group d-none" feature-group-label="${feature_group.label}" feature-group="${feature_group.id}">
            <div class="d-flex h-60  align-items-center justify-content-center px-4">
               <div class="flex-fill">   
                  <h2 class="fw-bolder mb-0 h-60 lh-60 h5">${feature_group.label}</h2>
               </div>
                  <div>
                  <#if user.userLevel==0>
                     <a href="jsp/admin/AdminTechnicalMenu.jsp?tab=assign_features_groups#features_management#${feature_group.id}" class="border btn btn-light btn-rounded">
                        <i class="fs-6 float-end lh-60 h-60 ${feature_group.icon!''}"></i>
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
                  <a admin-url="${admin_url}" feature-url="${feature.url}" feature-group-label="${feature_group.label}" feature-group="${feature_group.id}" plugin_name="${feature.pluginName}" class="list-group-item list-group-item-action p-4 lh-tight" href="${feature.url}?plugin_name=${feature.pluginName}" title="${feature.name}" >
                     <div class="row align-items-center">
                        <div class="col">
                           <h3 class="mb-1 fw-bold title h6">
                              <span>${feature.name}</span>
                           </h3>
                           <div class="d-block text-muted mt-n1">
                              <span>${feature.description!''}</span>
                           </div>
                        </div>
                     </div>
                  </a>
               <#else>
                  <a class="dropdown-item" href="${feature.url}" >
                     <#if feature.iconUrl?has_content><i class="${feature.iconUrl}"></i></#if> ${feature.name} ${feature.descriptionKey}
                  </a>
               </#if>
            </#list>
            <#else>
         </#if>
      </#list>
      <li id="child-menu-more" class="nav-item dropdown list-unstyled d-none" style="position:static !important;">
          <a class="nav-link" id="child-menu-more-btn" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" data-bs-boundary="viewport" aria-expanded="false" title="#i18n{portal.users.admin_header.labelMore}">
            <h3><i class="ti ti-dots fs-2"></i></h3>
          </a>
          <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
          </ul>
        </li>
   </div>
</div>
</div>
<header id="top-menu" class="navbar navbar-expand-md navbar-light d-print-none border-bottom h-60 shadow">
   <div class="container-fluid">
      <button id="menu-mobile" class="border btn btn-light btn-rounded">
        <i class="ti ti-menu-2"></i>
      </button>
      <span class="d-block d-xl-none text-white fw-bold ps-2">${site_name}</span>
      <ul class="navbar-nav flex-row order-md-last ms-auto">
         <#if userMenuMode?number = 1>
         <li class="nav-item d-none d-xl-flex">
            <div id="menu-rotate" class="border btn btn-light btn-rounded" tabindex="0" role="button" data-bs-toggle="tooltip" data-bs-animation="false" data-bs-placement="bottom" data-bs-original-title="#i18n{portal.users.admin_header.labelMenuV} / #i18n{portal.users.admin_header.labelMenuH}">
               <i class="ti ti-layout-navbar-collapse menu-rotate-icon"></i>
               <label for="menu-rotate">
                  <span class="visually-hidden">#i18n{portal.users.admin_header.labelMode} #i18n{portal.users.admin_header.labelMenuH} / #i18n{portal.users.admin_header.labelMenuV}</span>
               </label>
            </div>
         </li>
         </#if>
         <#if userDarkMode?number = 1>
         <li class="nav-item d-none d-sm-block">
            <div id="toggle-theme" class="border btn btn-light btn-rounded" tabindex="0" role="button"  data-bs-toggle="tooltip" data-bs-animation="false" data-bs-placement="bottom" data-bs-original-title="#i18n{portal.users.admin_header.labelMode} #i18n{portal.users.admin_header.labelDarkMode} / #i18n{portal.users.admin_header.labelLightMode}">
               <i class="ti ti-moon darkmode-moon"></i>
               <i class="ti ti-sun-high darkmode-sun"></i>
               <label for="toggle-theme">
                  <span class="visually-hidden">#i18n{portal.users.admin_header.labelMode} #i18n{portal.users.admin_header.labelDarkMode} / #i18n{portal.users.admin_header.labelLightMode}</span>
               </label>
            </div>
         </li>
         </#if>
         <#if userReadMode?number = 1>
            <@adminReadMode />
         </#if>
         <#if user.userLevel==0>            
            <li class="nav-item d-none d-xl-flex">
               <a class="border btn btn-light btn-rounded" href="jsp/admin/ManageProperties.jsp" title="#i18n{portal.site.adminFeature.properties_management.name}" data-bs-toggle="tooltip" data-bs-animation="false" data-bs-placement="bottom" data-bs-original-title="#i18n{portal.site.adminFeature.properties_management.name}">
                  <i class="ti ti-home-cog fs-5"></i><span class="visually-hidden">#i18n{portal.site.adminFeature.properties_management.name}</span>
               </a>
            </li>
            <li class="nav-item d-none d-xl-flex">
               <a class="border btn btn-light btn-rounded" href="jsp/admin/AdminTechnicalMenu.jsp" title="#i18n{portal.admindashboard.view_dashboards.title}" data-bs-toggle="tooltip" data-bs-animation="false" data-bs-placement="bottom" data-bs-original-title="#i18n{portal.admindashboard.view_dashboards.title}">
                  <i class="ti ti-adjustments-horizontal fs-5"></i><span class="visually-hidden">#i18n{portal.admindashboard.view_dashboards.title}</span>
               </a>
            </li>
         </#if>
         <#if userMenuItems?has_content>   
            <#list userMenuItems as item>
               <#if item.content?contains("jsp/admin/DoChangeLanguage.jsp")>
                   ${item.content}
               </#if>
            </#list>
         </#if>
         <li class="nav-item dropdown lutece-profile d-none d-sm-block me-0">
            <a href="#" class="border btn btn-light btn-rounded" data-bs-toggle="dropdown" >
               <div class="lutece-profile-name">
                  <div class="small fw-bold">
                     <span id="lutece-profile-name" class="truncate">${dashboard_zone_4!}</span> <i class="ti ti-chevron-down"></i>
                  </div>
               </div>
            </a>
            <div class="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
            <#if userMenuItems?has_content>   
               <#list userMenuItems as item>
                  <#if !item.content?contains("jsp/admin/DoChangeLanguage.jsp")>
                     ${item.content}
                  </#if>
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
		 <li class="nav-item dropdown d-sm-none">
            <a href="#" class="border btn btn-light btn-rounded" data-bs-toggle="dropdown" >
				<span class="d-md-none btn-rounded"><i class="ti ti-user"></i></span>
            </a>
			<div class="dropdown-menu dropdown-menu-end dropdown-menu-lg-start">
				<span class="dropdown-header">${dashboard_zone_4!}</span>
				<hr class="dropdown-divider">
            <#if userMenuItems?has_content>   
               <#list userMenuItems as item>
                  <#if !item.content?contains("jsp/admin/DoChangeLanguage.jsp")>
                     ${item.content}
                  </#if>
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
<div id="page" class="w-100">
</#macro>