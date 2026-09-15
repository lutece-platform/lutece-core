<#ftl strip_whitespace=true strip_text=true />
<#-- DO NOT UPDATE                            -->
<#-- WARNING : be careful to white-space and lines break in FreeMarker macros.
 # This macro template can be used to output white-space-sensitive formats (like RSS files).
 # See http://dev.lutece.paris.fr/jira/browse/LUTECE-765
-->
<#-- THEME LINKS AND LABELS VARIABLES -->
<#-- Theme Code           -->
<#-- Assigned by expression and not by capture: these values build template paths, and a capture
     produces a markup_output when auto-escaping is on, which #include and .get_optional_template
     refuse ("Expected a string [...] but this has evaluated to a markup_output"). -->
<#assign commonsGlobalThemeCode = ( !dskey('theme.globalThemeCode')?starts_with('DS') && dskey('theme.globalThemeCode')?has_content )?then( dskey('theme.globalThemeCode'), 'lutece' ) />
<#assign commonsGlobalThemeVersion = ( !dskey('theme.globalThemeVersion')?starts_with('DS') && dskey('theme.globalThemeVersion')?has_content )?then( dskey('theme.globalThemeVersion'), '1.0' ) />
<#-- Path                 -->
<#assign commonsSiteSharedPath='themes/shared/' /> 
<#assign commonsSiteThemePath='themes/skin/${commonsGlobalThemeCode}/' /> 
<#assign commonsSharedThemePath='themes/skin/shared/' />
<#assign commonsTplPath='${commonsGlobalThemeCode}/tpl/' />
<#assign commonsSiteJsPath='js/' /> 
<#assign commonsSiteJsModulesPath='js/modules/' /> 
<#assign commonsSiteCssPath='css/' /> 
<#assign commonsSiteImagesPath='images/' /> 
<#assign commonsMacrosPath='macros/' />
<#assign commonsFtlPath='${commonsGlobalThemeCode}/macros/' />
<#-- Theme Macros                                                               -->
<#-- MACRO cTpl : If find theme template then use it else use current template  -->
<#macro cTpl tpl=''>
<#local tpl = tpl?is_markup_output?then( tpl?markup_string, tpl ) />
<#local tplName = tpl?has_content?then( tpl, .caller_template_name?keep_after("skin/") ) />
<#local tplPath='../themes/${commonsGlobalThemeCode}/tpl/${tplName}' >
<#assign optTemp = .get_optional_template( tplPath )>
<#if optTemp.exists>
<@optTemp.include />
<#else>
<#nested> 
</#if>
</#macro>
<#-- MACRO cMacro : If find theme macro then use it else use current one -->
<#macro cMacro name='' group='' >
<#local macroPath='../themes/${commonsGlobalThemeCode}/macros/${group}/${name}.ftl' >
<#assign macroTheme = .get_optional_template( macroPath )>
<#if macroTheme.exists><@macroTheme.include /><#else><#include "${commonsMacrosPath}${group}/${name}.ftl" /></#if>
</#macro>
<#-- THEME SPEC                            -->
<#include "${commonsGlobalThemeCode}/_theme.ftl" />
<#-- MACROS LIST                            -->
<#include "theme_commons_macros.ftl" />
<#-- BANNER MANAGEMENT        -->
<#assign hasBanner><#if !dskey('portal.theme.site_property.banner.shown.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.banner.shown.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign hasBanner = hasBanner?is_markup_output?then(hasBanner?markup_string, hasBanner) />
<#assign urlDefaultBannerImage>${dskey('portal.theme.site_property.banner')}</#assign>
<#assign urlDefaultBannerImage = urlDefaultBannerImage?is_markup_output?then(urlDefaultBannerImage?markup_string, urlDefaultBannerImage) />
<#assign isBannerOnlyHome><#if !dskey('portal.theme.site_property.banner.onlyhome.checkbox')?starts_with('DS') && dskey('portal.theme.site_property.banner.onlyhome.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign isBannerOnlyHome = isBannerOnlyHome?is_markup_output?then(isBannerOnlyHome?markup_string, isBannerOnlyHome) />
<#assign isBannerFixed><#if !dskey('portal.theme.site_property.banner.fixed.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.banner.fixed.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign isBannerFixed = isBannerFixed?is_markup_output?then(isBannerFixed?markup_string, isBannerFixed) />
<#assign hasBannerInternalStyle><#if !dskey('portal.theme.site_property.banner.internal.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.banner.internal.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign hasBannerInternalStyle = hasBannerInternalStyle?is_markup_output?then(hasBannerInternalStyle?markup_string, hasBannerInternalStyle) />
<#-- END BANNER MANAGEMENT    -->
<#-- MENU MANAGEMENT          -->
<#assign isRtl><#if !dskey('portal.theme.site_property.layout.dir.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.layout.dir.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign isRtl = isRtl?is_markup_output?then(isRtl?markup_string, isRtl) />
<#assign hasUserThemeSwitch><#if !dskey('portal.theme.site_property.menu.user.themes.switch.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.user.themes.switch.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign hasUserThemeSwitch = hasUserThemeSwitch?is_markup_output?then(hasUserThemeSwitch?markup_string, hasUserThemeSwitch) />
<#assign hasUserThemeDensity><#if !dskey('portal.theme.site_property.menu.user.themes.density.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.user.themes.density.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign hasUserThemeDensity = hasUserThemeDensity?is_markup_output?then(hasUserThemeDensity?markup_string, hasUserThemeDensity) />
<#assign hasUserThemeColors><#if !dskey('portal.theme.site_property.menu.user.themes.colors.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.user.themes.colors.checkbox') =='1'>true<#else>false</#if></#assign>
<#assign hasUserThemeColors = hasUserThemeColors?is_markup_output?then(hasUserThemeColors?markup_string, hasUserThemeColors) />
<#assign isDark><#if dskey('portal.theme.site_property.layout.theme.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign isDark = isDark?is_markup_output?then(isDark?markup_string, isDark) />
<#assign skipLinkMenu><#if !dskey('portal.theme.site_property.menu.skipLinkMenu.checkbox')?starts_with('DS') && dskey('portal.theme.site_property.menu.skipLinkMenu.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign skipLinkMenu = skipLinkMenu?is_markup_output?then(skipLinkMenu?markup_string, skipLinkMenu) />
<#assign skipLinkMainId>${dskey('portal.theme.site_property.menu.skipLinkMainId')}</#assign>
<#assign skipLinkMainId = skipLinkMainId?is_markup_output?then(skipLinkMainId?markup_string, skipLinkMainId) />
<#assign hasDefaultMenu><#if !dskey('portal.theme.site_property.menu.hasDefaultMenu.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.hasDefaultMenu.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign hasDefaultMenu = hasDefaultMenu?is_markup_output?then(hasDefaultMenu?markup_string, hasDefaultMenu) />
<#assign hasSearchMenu><#if !dskey('portal.theme.site_property.menu.search.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.search.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign hasSearchMenu = hasSearchMenu?is_markup_output?then(hasSearchMenu?markup_string, hasSearchMenu) />
<#assign hasTranslateMenu><#if !dskey('portal.theme.site_property.menu.translate.checkbox')?starts_with('DS') &&  dskey('portal.theme.site_property.menu.translate.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign hasTranslateMenu = hasTranslateMenu?is_markup_output?then(hasTranslateMenu?markup_string, hasTranslateMenu) />
<#assign isFixedMenu><#if dskey('portal.theme.site_property.menu.fixedMenu.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign isFixedMenu = isFixedMenu?is_markup_output?then(isFixedMenu?markup_string, isFixedMenu) />
<#assign isMainSidebarMenu><#if dskey('portal.theme.site_property.menu.sidebarMenu.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign isMainSidebarMenu = isMainSidebarMenu?is_markup_output?then(isMainSidebarMenu?markup_string, isMainSidebarMenu) />
<#assign isMainSidebarMenuCollapse><#if dskey('portal.theme.site_property.menu.sidebarMenuCollapse.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign isMainSidebarMenuCollapse = isMainSidebarMenuCollapse?is_markup_output?then(isMainSidebarMenuCollapse?markup_string, isMainSidebarMenuCollapse) />
<#assign urlDefaultSearch>${dskey('portal.theme.site_property.Url.search')!}</#assign>
<#assign urlDefaultSearch = urlDefaultSearch?is_markup_output?then(urlDefaultSearch?markup_string, urlDefaultSearch) />
<#assign mainNavClass='' />
<#-- MENU MANAGEMENT          -->
<#-- LAYOUT MANAGEMENT        -->
<#assign isLayoutFluid><#if dskey('portal.theme.site_property.layout.type.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign isLayoutFluid = isLayoutFluid?is_markup_output?then(isLayoutFluid?markup_string, isLayoutFluid) />
<#-- END LAYOUT MANAGEMENT    -->
<#-- UTILS MANAGEMENT         -->
<#assign addGoToTop><#if dskey('portal.theme.site_property.menu.gototop.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign addGoToTop = addGoToTop?is_markup_output?then(addGoToTop?markup_string, addGoToTop) />
<#assign isTargetDefaultIconShown><#if dskey('portal.theme.site_property.link.showTargetIcon.checkbox') == '1'>true<#else>false</#if></#assign>
<#assign isTargetDefaultIconShown = isTargetDefaultIconShown?is_markup_output?then(isTargetDefaultIconShown?markup_string, isTargetDefaultIconShown) />
<#-- END UTILS MANAGEMENT     -->